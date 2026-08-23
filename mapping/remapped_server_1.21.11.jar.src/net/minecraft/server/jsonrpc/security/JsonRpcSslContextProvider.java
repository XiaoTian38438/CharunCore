/*    */ package net.minecraft.server.jsonrpc.security;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import io.netty.handler.ssl.SslContext;
/*    */ import io.netty.handler.ssl.SslContextBuilder;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.security.KeyStore;
/*    */ import javax.net.ssl.KeyManagerFactory;
/*    */ import javax.net.ssl.TrustManagerFactory;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public class JsonRpcSslContextProvider
/*    */ {
/*    */   private static final String PASSWORD_ENV_VARIABLE_KEY = "MINECRAFT_MANAGEMENT_TLS_KEYSTORE_PASSWORD";
/*    */   private static final String PASSWORD_SYSTEM_PROPERTY_KEY = "management.tls.keystore.password";
/* 18 */   private static final Logger log = LogUtils.getLogger();
/*    */   
/*    */   public static SslContext createFrom(String paramString1, String paramString2) throws Exception {
/* 21 */     if (paramString1.isEmpty()) {
/* 22 */       throw new IllegalArgumentException("TLS is enabled but keystore is not configured");
/*    */     }
/* 24 */     File file = new File(paramString1);
/* 25 */     if (!file.exists() || !file.isFile()) {
/* 26 */       throw new IllegalArgumentException("Supplied keystore is not a file or does not exist: '" + paramString1 + "'");
/*    */     }
/*    */     
/* 29 */     String str = getKeystorePassword(paramString2);
/* 30 */     return loadKeystoreFromPath(file, str);
/*    */   }
/*    */   
/*    */   private static String getKeystorePassword(String paramString) {
/* 34 */     String str1 = System.getenv().get("MINECRAFT_MANAGEMENT_TLS_KEYSTORE_PASSWORD");
/* 35 */     if (str1 != null) {
/* 36 */       return str1;
/*    */     }
/*    */     
/* 39 */     String str2 = System.getProperty("management.tls.keystore.password", null);
/* 40 */     if (str2 != null) {
/* 41 */       return str2;
/*    */     }
/*    */     
/* 44 */     return paramString;
/*    */   }
/*    */   
/*    */   private static SslContext loadKeystoreFromPath(File paramFile, String paramString) throws Exception {
/* 48 */     KeyStore keyStore = KeyStore.getInstance("PKCS12");
/* 49 */     FileInputStream fileInputStream = new FileInputStream(paramFile); 
/* 50 */     try { keyStore.load(fileInputStream, paramString.toCharArray());
/* 51 */       fileInputStream.close(); } catch (Throwable throwable) { try { fileInputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*    */        throw throwable; }
/* 53 */      KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
/* 54 */     keyManagerFactory.init(keyStore, paramString.toCharArray());
/*    */     
/* 56 */     TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/* 57 */     trustManagerFactory.init(keyStore);
/*    */     
/* 59 */     return SslContextBuilder.forServer(keyManagerFactory)
/* 60 */       .trustManager(trustManagerFactory)
/* 61 */       .build();
/*    */   }
/*    */   
/*    */   public static void printInstructions() {
/* 65 */     log.info("To use TLS for the management server, please follow these steps:");
/* 66 */     log.info("1. Set the server property 'management-server-tls-enabled' to 'true' to enable TLS");
/* 67 */     log.info("2. Create a keystore file of type PKCS12 containing your server certificate and private key");
/* 68 */     log.info("3. Set the server property 'management-server-tls-keystore' to the path of your keystore file");
/* 69 */     log.info("4. Set the keystore password via the environment variable 'MINECRAFT_MANAGEMENT_TLS_KEYSTORE_PASSWORD', or system property 'management.tls.keystore.password', or server property 'management-server-tls-keystore-password'");
/* 70 */     log.info("5. Restart the server to apply the changes.");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\security\JsonRpcSslContextProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */