/*     */ package net.minecraft.server.rcon.thread;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.server.ServerInterface;
/*     */ import net.minecraft.server.rcon.PktUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RconClient
/*     */   extends GenericThread {
/*  17 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final int SERVERDATA_AUTH = 3;
/*     */   private static final int SERVERDATA_EXECCOMMAND = 2;
/*     */   private static final int SERVERDATA_RESPONSE_VALUE = 0;
/*     */   private static final int SERVERDATA_AUTH_RESPONSE = 2;
/*     */   private static final int SERVERDATA_AUTH_FAILURE = -1;
/*     */   private boolean authed;
/*     */   private final Socket client;
/*  25 */   private final byte[] buf = new byte[1460];
/*     */   private final String rconPassword;
/*     */   private final ServerInterface serverInterface;
/*     */   
/*     */   RconClient(ServerInterface paramServerInterface, String paramString, Socket paramSocket) {
/*  30 */     super("RCON Client " + String.valueOf(paramSocket.getInetAddress()));
/*  31 */     this.serverInterface = paramServerInterface;
/*  32 */     this.client = paramSocket;
/*     */     
/*     */     try {
/*  35 */       this.client.setSoTimeout(0);
/*  36 */     } catch (Exception exception) {
/*  37 */       this.running = false;
/*     */     } 
/*     */     
/*  40 */     this.rconPassword = paramString;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     
/*  46 */     try { while (this.running) {
/*  47 */         String str; BufferedInputStream bufferedInputStream = new BufferedInputStream(this.client.getInputStream());
/*  48 */         int i = bufferedInputStream.read(this.buf, 0, 1460);
/*     */         
/*  50 */         if (10 > i) {
/*     */           return;
/*     */         }
/*     */         
/*  54 */         int j = 0;
/*  55 */         int k = PktUtils.intFromByteArray(this.buf, 0, i);
/*  56 */         if (k != i - 4) {
/*     */           return;
/*     */         }
/*     */         
/*  60 */         j += true;
/*  61 */         int m = PktUtils.intFromByteArray(this.buf, j, i);
/*  62 */         j += true;
/*     */         
/*  64 */         int n = PktUtils.intFromByteArray(this.buf, j);
/*  65 */         j += true;
/*  66 */         switch (n) {
/*     */           case 3:
/*  68 */             str = PktUtils.stringFromByteArray(this.buf, j, i);
/*  69 */             j += str.length();
/*  70 */             if (!str.isEmpty() && str.equals(this.rconPassword)) {
/*  71 */               this.authed = true;
/*  72 */               send(m, 2, ""); continue;
/*     */             } 
/*  74 */             this.authed = false;
/*  75 */             sendAuthFailure();
/*     */             continue;
/*     */           
/*     */           case 2:
/*  79 */             if (this.authed) {
/*  80 */               String str1 = PktUtils.stringFromByteArray(this.buf, j, i);
/*     */               try {
/*  82 */                 sendCmdResponse(m, this.serverInterface.runCommand(str1));
/*  83 */               } catch (Exception exception) {
/*  84 */                 sendCmdResponse(m, "Error executing: " + str1 + " (" + exception.getMessage() + ")");
/*     */               }  continue;
/*     */             } 
/*  87 */             sendAuthFailure();
/*     */             continue;
/*     */         } 
/*     */         
/*  91 */         sendCmdResponse(m, String.format(Locale.ROOT, "Unknown request %s", new Object[] { Integer.toHexString(n) }));
/*     */       }
/*     */        }
/*  94 */     catch (IOException iOException) {  }
/*  95 */     catch (Exception exception)
/*  96 */     { LOGGER.error("Exception whilst parsing RCON input", exception); }
/*     */     finally
/*  98 */     { closeSocket();
/*  99 */       LOGGER.info("Thread {} shutting down", this.name);
/* 100 */       this.running = false; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void send(int paramInt1, int paramInt2, String paramString) throws IOException {
/* 107 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1248);
/* 108 */     DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
/* 109 */     byte[] arrayOfByte = paramString.getBytes(StandardCharsets.UTF_8);
/* 110 */     dataOutputStream.writeInt(Integer.reverseBytes(arrayOfByte.length + 10));
/* 111 */     dataOutputStream.writeInt(Integer.reverseBytes(paramInt1));
/* 112 */     dataOutputStream.writeInt(Integer.reverseBytes(paramInt2));
/* 113 */     dataOutputStream.write(arrayOfByte);
/* 114 */     dataOutputStream.write(0);
/* 115 */     dataOutputStream.write(0);
/* 116 */     this.client.getOutputStream().write(byteArrayOutputStream.toByteArray());
/*     */   }
/*     */   
/*     */   private void sendAuthFailure() throws IOException {
/* 120 */     send(-1, 2, "");
/*     */   }
/*     */   
/*     */   private void sendCmdResponse(int paramInt, String paramString) throws IOException {
/* 124 */     int i = paramString.length();
/*     */     
/*     */     do {
/* 127 */       boolean bool = (4096 <= i) ? true : i;
/* 128 */       send(paramInt, 0, paramString.substring(0, bool));
/* 129 */       paramString = paramString.substring(bool);
/* 130 */       i = paramString.length();
/* 131 */     } while (0 != i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 139 */     this.running = false;
/* 140 */     closeSocket();
/* 141 */     super.stop();
/*     */   }
/*     */   
/*     */   private void closeSocket() {
/*     */     try {
/* 146 */       this.client.close();
/* 147 */     } catch (IOException iOException) {
/* 148 */       LOGGER.warn("Failed to close socket", iOException);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\rcon\thread\RconClient.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */