/*     */ package net.minecraft.server.rcon.thread;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.util.List;
/*     */ import net.minecraft.server.ServerInterface;
/*     */ import net.minecraft.server.dedicated.DedicatedServerProperties;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RconThread
/*     */   extends GenericThread
/*     */ {
/*  18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final ServerSocket socket;
/*     */   private final String rconPassword;
/*  22 */   private final List<RconClient> clients = Lists.newArrayList();
/*     */   private final ServerInterface serverInterface;
/*     */   
/*     */   private RconThread(ServerInterface paramServerInterface, ServerSocket paramServerSocket, String paramString) {
/*  26 */     super("RCON Listener");
/*  27 */     this.serverInterface = paramServerInterface;
/*  28 */     this.socket = paramServerSocket;
/*  29 */     this.rconPassword = paramString;
/*     */   }
/*     */   
/*     */   private void clearClients() {
/*  33 */     this.clients.removeIf(paramRconClient -> !paramRconClient.isRunning());
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/*  39 */       while (this.running) {
/*     */         
/*     */         try {
/*  42 */           Socket socket = this.socket.accept();
/*  43 */           RconClient rconClient = new RconClient(this.serverInterface, this.rconPassword, socket);
/*  44 */           rconClient.start();
/*  45 */           this.clients.add(rconClient);
/*     */ 
/*     */           
/*  48 */           clearClients();
/*  49 */         } catch (SocketTimeoutException socketTimeoutException) {
/*     */           
/*  51 */           clearClients();
/*  52 */         } catch (IOException iOException) {
/*  53 */           if (this.running) {
/*  54 */             LOGGER.info("IO exception: ", iOException);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } finally {
/*  59 */       closeSocket(this.socket);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static RconThread create(ServerInterface paramServerInterface) {
/*  64 */     DedicatedServerProperties dedicatedServerProperties = paramServerInterface.getProperties();
/*     */     
/*  66 */     String str1 = paramServerInterface.getServerIp();
/*  67 */     if (str1.isEmpty()) {
/*  68 */       str1 = "0.0.0.0";
/*     */     }
/*     */     
/*  71 */     int i = dedicatedServerProperties.rconPort;
/*  72 */     if (0 >= i || 65535 < i) {
/*  73 */       LOGGER.warn("Invalid rcon port {} found in server.properties, rcon disabled!", Integer.valueOf(i));
/*  74 */       return null;
/*     */     } 
/*     */     
/*  77 */     String str2 = dedicatedServerProperties.rconPassword;
/*  78 */     if (str2.isEmpty()) {
/*  79 */       LOGGER.warn("No rcon password set in server.properties, rcon disabled!");
/*  80 */       return null;
/*     */     } 
/*     */     
/*     */     try {
/*  84 */       ServerSocket serverSocket = new ServerSocket(i, 0, InetAddress.getByName(str1));
/*  85 */       serverSocket.setSoTimeout(500);
/*     */       
/*  87 */       RconThread rconThread = new RconThread(paramServerInterface, serverSocket, str2);
/*  88 */       if (!rconThread.start()) {
/*  89 */         return null;
/*     */       }
/*  91 */       LOGGER.info("RCON running on {}:{}", str1, Integer.valueOf(i));
/*  92 */       return rconThread;
/*  93 */     } catch (IOException iOException) {
/*  94 */       LOGGER.warn("Unable to initialise RCON on {}:{}", new Object[] { str1, Integer.valueOf(i), iOException });
/*     */ 
/*     */       
/*  97 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() {
/* 102 */     this.running = false;
/* 103 */     closeSocket(this.socket);
/* 104 */     super.stop();
/*     */     
/* 106 */     for (RconClient rconClient : this.clients) {
/* 107 */       if (rconClient.isRunning()) {
/* 108 */         rconClient.stop();
/*     */       }
/*     */     } 
/* 111 */     this.clients.clear();
/*     */   }
/*     */   
/*     */   private void closeSocket(ServerSocket paramServerSocket) {
/* 115 */     LOGGER.debug("closeSocket: {}", paramServerSocket);
/*     */     
/*     */     try {
/* 118 */       paramServerSocket.close();
/* 119 */     } catch (IOException iOException) {
/* 120 */       LOGGER.warn("Failed to close socket", iOException);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\rcon\thread\RconThread.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */