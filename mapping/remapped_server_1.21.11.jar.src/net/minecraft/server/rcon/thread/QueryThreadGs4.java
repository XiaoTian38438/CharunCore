/*     */ package net.minecraft.server.rcon.thread;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.PortUnreachableException;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.ServerInterface;
/*     */ import net.minecraft.server.rcon.NetworkDataOutputStream;
/*     */ import net.minecraft.server.rcon.PktUtils;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class QueryThreadGs4
/*     */   extends GenericThread
/*     */ {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final String GAME_TYPE = "SMP";
/*     */   private static final String GAME_ID = "MINECRAFT";
/*     */   private static final long CHALLENGE_CHECK_INTERVAL = 30000L;
/*     */   private static final long RESPONSE_CACHE_TIME = 5000L;
/*     */   private long lastChallengeCheck;
/*     */   private final int port;
/*     */   private final int serverPort;
/*     */   private final int maxPlayers;
/*     */   private final String serverName;
/*     */   private final String worldName;
/*     */   private DatagramSocket socket;
/*  39 */   private final byte[] buffer = new byte[1460];
/*     */   private String hostIp;
/*     */   private String serverIp;
/*     */   private final Map<SocketAddress, RequestChallenge> validChallenges;
/*     */   private final NetworkDataOutputStream rulesResponse;
/*     */   private long lastRulesResponse;
/*     */   private final ServerInterface serverInterface;
/*     */   
/*     */   private QueryThreadGs4(ServerInterface paramServerInterface, int paramInt) {
/*  48 */     super("Query Listener");
/*  49 */     this.serverInterface = paramServerInterface;
/*     */     
/*  51 */     this.port = paramInt;
/*  52 */     this.serverIp = paramServerInterface.getServerIp();
/*  53 */     this.serverPort = paramServerInterface.getServerPort();
/*  54 */     this.serverName = paramServerInterface.getServerName();
/*  55 */     this.maxPlayers = paramServerInterface.getMaxPlayers();
/*  56 */     this.worldName = paramServerInterface.getLevelIdName();
/*     */ 
/*     */     
/*  59 */     this.lastRulesResponse = 0L;
/*     */     
/*  61 */     this.hostIp = "0.0.0.0";
/*     */ 
/*     */     
/*  64 */     if (this.serverIp.isEmpty() || this.hostIp.equals(this.serverIp)) {
/*     */       
/*  66 */       this.serverIp = "0.0.0.0";
/*     */       try {
/*  68 */         InetAddress inetAddress = InetAddress.getLocalHost();
/*  69 */         this.hostIp = inetAddress.getHostAddress();
/*  70 */       } catch (UnknownHostException unknownHostException) {
/*  71 */         LOGGER.warn("Unable to determine local host IP, please set server-ip in server.properties", unknownHostException);
/*     */       } 
/*     */     } else {
/*  74 */       this.hostIp = this.serverIp;
/*     */     } 
/*     */ 
/*     */     
/*  78 */     this.rulesResponse = new NetworkDataOutputStream(1460);
/*  79 */     this.validChallenges = Maps.newHashMap();
/*     */   }
/*     */   
/*     */   public static QueryThreadGs4 create(ServerInterface paramServerInterface) {
/*  83 */     int i = (paramServerInterface.getProperties()).queryPort;
/*  84 */     if (0 >= i || 65535 < i) {
/*  85 */       LOGGER.warn("Invalid query port {} found in server.properties (queries disabled)", Integer.valueOf(i));
/*  86 */       return null;
/*     */     } 
/*     */     
/*  89 */     QueryThreadGs4 queryThreadGs4 = new QueryThreadGs4(paramServerInterface, i);
/*  90 */     if (!queryThreadGs4.start()) {
/*  91 */       return null;
/*     */     }
/*  93 */     return queryThreadGs4;
/*     */   }
/*     */   
/*     */   private void sendTo(byte[] paramArrayOfbyte, DatagramPacket paramDatagramPacket) throws IOException {
/*  97 */     this.socket.send(new DatagramPacket(paramArrayOfbyte, paramArrayOfbyte.length, paramDatagramPacket.getSocketAddress()));
/*     */   }
/*     */   private boolean processPacket(DatagramPacket paramDatagramPacket) throws IOException {
/*     */     NetworkDataOutputStream networkDataOutputStream;
/* 101 */     byte[] arrayOfByte = paramDatagramPacket.getData();
/* 102 */     int i = paramDatagramPacket.getLength();
/* 103 */     SocketAddress socketAddress = paramDatagramPacket.getSocketAddress();
/* 104 */     LOGGER.debug("Packet len {} [{}]", Integer.valueOf(i), socketAddress);
/* 105 */     if (3 > i || -2 != arrayOfByte[0] || -3 != arrayOfByte[1]) {
/*     */       
/* 107 */       LOGGER.debug("Invalid packet [{}]", socketAddress);
/* 108 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 112 */     LOGGER.debug("Packet '{}' [{}]", PktUtils.toHexString(arrayOfByte[2]), socketAddress);
/* 113 */     switch (arrayOfByte[2]) {
/*     */       
/*     */       case 9:
/* 116 */         sendChallenge(paramDatagramPacket);
/* 117 */         LOGGER.debug("Challenge [{}]", socketAddress);
/* 118 */         return true;
/*     */ 
/*     */       
/*     */       case 0:
/* 122 */         if (!validChallenge(paramDatagramPacket).booleanValue()) {
/* 123 */           LOGGER.debug("Invalid challenge [{}]", socketAddress);
/* 124 */           return false;
/*     */         } 
/*     */         
/* 127 */         if (15 == i) {
/*     */           
/* 129 */           sendTo(buildRuleResponse(paramDatagramPacket), paramDatagramPacket);
/* 130 */           LOGGER.debug("Rules [{}]", socketAddress);
/*     */           break;
/*     */         } 
/* 133 */         networkDataOutputStream = new NetworkDataOutputStream(1460);
/* 134 */         networkDataOutputStream.write(0);
/* 135 */         networkDataOutputStream.writeBytes(getIdentBytes(paramDatagramPacket.getSocketAddress()));
/* 136 */         networkDataOutputStream.writeString(this.serverName);
/* 137 */         networkDataOutputStream.writeString("SMP");
/* 138 */         networkDataOutputStream.writeString(this.worldName);
/* 139 */         networkDataOutputStream.writeString(Integer.toString(this.serverInterface.getPlayerCount()));
/* 140 */         networkDataOutputStream.writeString(Integer.toString(this.maxPlayers));
/* 141 */         networkDataOutputStream.writeShort((short)this.serverPort);
/* 142 */         networkDataOutputStream.writeString(this.hostIp);
/*     */         
/* 144 */         sendTo(networkDataOutputStream.toByteArray(), paramDatagramPacket);
/* 145 */         LOGGER.debug("Status [{}]", socketAddress);
/*     */         break;
/*     */     } 
/*     */     
/* 149 */     return true;
/*     */   }
/*     */   
/*     */   private byte[] buildRuleResponse(DatagramPacket paramDatagramPacket) throws IOException {
/* 153 */     long l = Util.getMillis();
/* 154 */     if (l < this.lastRulesResponse + 5000L) {
/*     */       
/* 156 */       byte[] arrayOfByte1 = this.rulesResponse.toByteArray();
/* 157 */       byte[] arrayOfByte2 = getIdentBytes(paramDatagramPacket.getSocketAddress());
/* 158 */       arrayOfByte1[1] = arrayOfByte2[0];
/* 159 */       arrayOfByte1[2] = arrayOfByte2[1];
/* 160 */       arrayOfByte1[3] = arrayOfByte2[2];
/* 161 */       arrayOfByte1[4] = arrayOfByte2[3];
/*     */       
/* 163 */       return arrayOfByte1;
/*     */     } 
/*     */     
/* 166 */     this.lastRulesResponse = l;
/*     */     
/* 168 */     this.rulesResponse.reset();
/* 169 */     this.rulesResponse.write(0);
/* 170 */     this.rulesResponse.writeBytes(getIdentBytes(paramDatagramPacket.getSocketAddress()));
/* 171 */     this.rulesResponse.writeString("splitnum");
/* 172 */     this.rulesResponse.write(128);
/* 173 */     this.rulesResponse.write(0);
/*     */ 
/*     */     
/* 176 */     this.rulesResponse.writeString("hostname");
/* 177 */     this.rulesResponse.writeString(this.serverName);
/* 178 */     this.rulesResponse.writeString("gametype");
/* 179 */     this.rulesResponse.writeString("SMP");
/* 180 */     this.rulesResponse.writeString("game_id");
/* 181 */     this.rulesResponse.writeString("MINECRAFT");
/* 182 */     this.rulesResponse.writeString("version");
/* 183 */     this.rulesResponse.writeString(this.serverInterface.getServerVersion());
/* 184 */     this.rulesResponse.writeString("plugins");
/* 185 */     this.rulesResponse.writeString(this.serverInterface.getPluginNames());
/* 186 */     this.rulesResponse.writeString("map");
/* 187 */     this.rulesResponse.writeString(this.worldName);
/* 188 */     this.rulesResponse.writeString("numplayers");
/* 189 */     this.rulesResponse.writeString("" + this.serverInterface.getPlayerCount());
/* 190 */     this.rulesResponse.writeString("maxplayers");
/* 191 */     this.rulesResponse.writeString("" + this.maxPlayers);
/* 192 */     this.rulesResponse.writeString("hostport");
/* 193 */     this.rulesResponse.writeString("" + this.serverPort);
/* 194 */     this.rulesResponse.writeString("hostip");
/* 195 */     this.rulesResponse.writeString(this.hostIp);
/* 196 */     this.rulesResponse.write(0);
/* 197 */     this.rulesResponse.write(1);
/*     */ 
/*     */ 
/*     */     
/* 201 */     this.rulesResponse.writeString("player_");
/* 202 */     this.rulesResponse.write(0);
/*     */     
/* 204 */     String[] arrayOfString = this.serverInterface.getPlayerNames();
/* 205 */     for (String str : arrayOfString) {
/* 206 */       this.rulesResponse.writeString(str);
/*     */     }
/* 208 */     this.rulesResponse.write(0);
/*     */     
/* 210 */     return this.rulesResponse.toByteArray();
/*     */   }
/*     */   
/*     */   private byte[] getIdentBytes(SocketAddress paramSocketAddress) {
/* 214 */     return ((RequestChallenge)this.validChallenges.get(paramSocketAddress)).getIdentBytes();
/*     */   }
/*     */   
/*     */   private Boolean validChallenge(DatagramPacket paramDatagramPacket) {
/* 218 */     SocketAddress socketAddress = paramDatagramPacket.getSocketAddress();
/* 219 */     if (!this.validChallenges.containsKey(socketAddress))
/*     */     {
/* 221 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/* 224 */     byte[] arrayOfByte = paramDatagramPacket.getData();
/* 225 */     return Boolean.valueOf((((RequestChallenge)this.validChallenges.get(socketAddress)).getChallenge() == PktUtils.intFromNetworkByteArray(arrayOfByte, 7, paramDatagramPacket.getLength())));
/*     */   }
/*     */   
/*     */   private void sendChallenge(DatagramPacket paramDatagramPacket) throws IOException {
/* 229 */     RequestChallenge requestChallenge = new RequestChallenge(paramDatagramPacket);
/* 230 */     this.validChallenges.put(paramDatagramPacket.getSocketAddress(), requestChallenge);
/*     */     
/* 232 */     sendTo(requestChallenge.getChallengeBytes(), paramDatagramPacket);
/*     */   }
/*     */   
/*     */   private void pruneChallenges() {
/* 236 */     if (!this.running) {
/*     */       return;
/*     */     }
/*     */     
/* 240 */     long l = Util.getMillis();
/* 241 */     if (l < this.lastChallengeCheck + 30000L) {
/*     */       return;
/*     */     }
/* 244 */     this.lastChallengeCheck = l;
/*     */     
/* 246 */     this.validChallenges.values().removeIf(paramRequestChallenge -> paramRequestChallenge.before(paramLong).booleanValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/* 251 */     LOGGER.info("Query running on {}:{}", this.serverIp, Integer.valueOf(this.port));
/* 252 */     this.lastChallengeCheck = Util.getMillis();
/* 253 */     DatagramPacket datagramPacket = new DatagramPacket(this.buffer, this.buffer.length);
/*     */     
/*     */     try {
/* 256 */       while (this.running) {
/*     */         try {
/* 258 */           this.socket.receive(datagramPacket);
/*     */ 
/*     */           
/* 261 */           pruneChallenges();
/*     */ 
/*     */           
/* 264 */           processPacket(datagramPacket);
/* 265 */         } catch (SocketTimeoutException socketTimeoutException) {
/*     */           
/* 267 */           pruneChallenges();
/* 268 */         } catch (PortUnreachableException portUnreachableException) {
/*     */         
/* 270 */         } catch (IOException iOException) {
/*     */           
/* 272 */           recoverSocketError(iOException);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 276 */       LOGGER.debug("closeSocket: {}:{}", this.serverIp, Integer.valueOf(this.port));
/* 277 */       this.socket.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean start() {
/* 283 */     if (this.running) {
/* 284 */       return true;
/*     */     }
/*     */     
/* 287 */     if (!initSocket()) {
/* 288 */       return false;
/*     */     }
/*     */     
/* 291 */     return super.start();
/*     */   }
/*     */   
/*     */   private void recoverSocketError(Exception paramException) {
/* 295 */     if (!this.running) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 300 */     LOGGER.warn("Unexpected exception", paramException);
/*     */ 
/*     */     
/* 303 */     if (!initSocket()) {
/* 304 */       LOGGER.error("Failed to recover from exception, shutting down!");
/* 305 */       this.running = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean initSocket() {
/*     */     try {
/* 311 */       this.socket = new DatagramSocket(this.port, InetAddress.getByName(this.serverIp));
/* 312 */       this.socket.setSoTimeout(500);
/* 313 */       return true;
/* 314 */     } catch (Exception exception) {
/* 315 */       LOGGER.warn("Unable to initialise query system on {}:{}", new Object[] { this.serverIp, Integer.valueOf(this.port), exception });
/*     */       
/* 317 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RequestChallenge
/*     */   {
/* 328 */     private final long time = (new Date()).getTime(); private final int challenge; private final byte[] identBytes; public RequestChallenge(DatagramPacket param1DatagramPacket) {
/* 329 */       byte[] arrayOfByte = param1DatagramPacket.getData();
/* 330 */       this.identBytes = new byte[4];
/* 331 */       this.identBytes[0] = arrayOfByte[3];
/* 332 */       this.identBytes[1] = arrayOfByte[4];
/* 333 */       this.identBytes[2] = arrayOfByte[5];
/* 334 */       this.identBytes[3] = arrayOfByte[6];
/* 335 */       this.ident = new String(this.identBytes, StandardCharsets.UTF_8);
/* 336 */       this.challenge = RandomSource.create().nextInt(16777216);
/* 337 */       this.challengeBytes = String.format(Locale.ROOT, "\t%s%d\000", new Object[] { this.ident, Integer.valueOf(this.challenge) }).getBytes(StandardCharsets.UTF_8);
/*     */     }
/*     */     private final byte[] challengeBytes; private final String ident;
/*     */     public Boolean before(long param1Long) {
/* 341 */       return Boolean.valueOf((this.time < param1Long));
/*     */     }
/*     */     
/*     */     public int getChallenge() {
/* 345 */       return this.challenge;
/*     */     }
/*     */     
/*     */     public byte[] getChallengeBytes() {
/* 349 */       return this.challengeBytes;
/*     */     }
/*     */     
/*     */     public byte[] getIdentBytes() {
/* 353 */       return this.identBytes;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getIdent() {
/* 358 */       return this.ident;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\rcon\thread\QueryThreadGs4.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */