/*     */ package com.mojang.authlib.yggdrasil;
/*     */ 
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import com.google.gson.annotations.SerializedName;
/*     */ import com.mojang.authlib.exceptions.MinecraftClientException;
/*     */ import com.mojang.authlib.minecraft.client.MinecraftClient;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import java.net.URL;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.Signature;
/*     */ import java.security.SignatureException;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ import java.util.Base64;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.annotation.Nullable;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class YggdrasilServicesKeyInfo
/*     */   implements ServicesKeyInfo
/*     */ {
/*  34 */   private static final Logger LOGGER = LoggerFactory.getLogger(YggdrasilServicesKeyInfo.class);
/*     */   
/*  36 */   private static final ScheduledExecutorService FETCHER_EXECUTOR = Executors.newScheduledThreadPool(1, (new ThreadFactoryBuilder())
/*  37 */       .setNameFormat("Yggdrasil Key Fetcher")
/*  38 */       .setDaemon(true)
/*  39 */       .build());
/*     */   
/*     */   private static final int KEY_SIZE_BITS = 4096;
/*     */   
/*     */   private static final String KEY_ALGORITHM = "RSA";
/*     */   
/*     */   private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
/*     */   
/*     */   private static final int REFRESH_INTERVAL_HOURS = 24;
/*     */   
/*     */   private static final int BASE_FAILURE_INTERVAL_MINUTES = 5;
/*     */   private static final int MAX_BACKOFF_EXPONENT = 6;
/*     */   private final PublicKey publicKey;
/*     */   
/*     */   private YggdrasilServicesKeyInfo(PublicKey paramPublicKey) {
/*  54 */     this.publicKey = paramPublicKey;
/*  55 */     String str = paramPublicKey.getAlgorithm();
/*  56 */     if (!str.equals("RSA")) {
/*  57 */       throw new IllegalArgumentException("Expected RSA key, got " + str);
/*     */     }
/*     */   }
/*     */   
/*     */   public static ServicesKeyInfo parse(byte[] paramArrayOfbyte) {
/*     */     try {
/*  63 */       X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(paramArrayOfbyte);
/*  64 */       KeyFactory keyFactory = KeyFactory.getInstance("RSA");
/*  65 */       PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
/*  66 */       return new YggdrasilServicesKeyInfo(publicKey);
/*  67 */     } catch (NoSuchAlgorithmException|java.security.spec.InvalidKeySpecException noSuchAlgorithmException) {
/*  68 */       throw new IllegalArgumentException("Invalid yggdrasil public key!", noSuchAlgorithmException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<ServicesKeyInfo> parseList(@Nullable List<KeyData> paramList) {
/*  73 */     if (paramList == null) {
/*  74 */       return List.of();
/*     */     }
/*  76 */     return paramList.stream()
/*  77 */       .map(paramKeyData -> parse(paramKeyData.publicKey.array()))
/*  78 */       .toList();
/*     */   }
/*     */   
/*     */   public static ServicesKeySet get(final URL url, final MinecraftClient client) {
/*  82 */     final CompletableFuture ready = new CompletableFuture();
/*  83 */     final AtomicReference keySet = new AtomicReference();
/*  84 */     FETCHER_EXECUTOR.execute(new Runnable() {
/*  85 */           private final AtomicInteger failureCount = new AtomicInteger();
/*     */ 
/*     */           
/*     */           public void run() {
/*  89 */             Objects.requireNonNull(keySet); YggdrasilServicesKeyInfo.fetch(url, client).ifPresent(keySet::set);
/*  90 */             ready.complete(null);
/*  91 */             reschedule();
/*     */           }
/*     */           
/*     */           private void reschedule() {
/*  95 */             if (keySet.get() == null) {
/*  96 */               int i = Math.min(this.failureCount.getAndIncrement(), 6);
/*  97 */               int j = 5 * (1 << i);
/*  98 */               YggdrasilServicesKeyInfo.FETCHER_EXECUTOR.schedule(this, j, TimeUnit.MINUTES);
/*     */               return;
/*     */             } 
/* 101 */             YggdrasilServicesKeyInfo.FETCHER_EXECUTOR.schedule(this, 24L, TimeUnit.HOURS);
/*     */           }
/*     */         });
/*     */     
/* 105 */     return ServicesKeySet.lazy(() -> {
/*     */           paramCompletableFuture.join();
/*     */           return Objects.<ServicesKeySet>requireNonNullElse(paramAtomicReference.get(), ServicesKeySet.EMPTY);
/*     */         });
/*     */   }
/*     */   
/*     */   private static Optional<ServicesKeySet> fetch(URL paramURL, MinecraftClient paramMinecraftClient) {
/*     */     KeySetResponse keySetResponse;
/*     */     try {
/* 114 */       keySetResponse = (KeySetResponse)paramMinecraftClient.get(paramURL, KeySetResponse.class);
/* 115 */     } catch (MinecraftClientException minecraftClientException) {
/* 116 */       LOGGER.error("Failed to request yggdrasil public key", (Throwable)minecraftClientException);
/* 117 */       return Optional.empty();
/*     */     } 
/*     */     
/* 120 */     if (keySetResponse == null) {
/* 121 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 125 */     try { List<ServicesKeyInfo> list1 = parseList(keySetResponse.profilePropertyKeys);
/* 126 */       List<ServicesKeyInfo> list2 = parseList(keySetResponse.playerCertificateKeys);
/* 127 */       return Optional.of(paramServicesKeyType -> { switch (paramServicesKeyType) { default: throw new IncompatibleClassChangeError();
/*     */               case PROFILE_PROPERTY:
/*     */               
/*     */               case PROFILE_KEY:
/* 131 */                 break; }  return paramList2; }); } catch (Exception exception)
/* 132 */     { LOGGER.error("Received malformed yggdrasil public key data", exception);
/* 133 */       return Optional.empty(); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public Signature signature() {
/*     */     try {
/* 140 */       Signature signature = Signature.getInstance("SHA1withRSA");
/* 141 */       signature.initVerify(this.publicKey);
/* 142 */       return signature;
/* 143 */     } catch (NoSuchAlgorithmException|java.security.InvalidKeyException noSuchAlgorithmException) {
/* 144 */       throw new AssertionError("Failed to create signature", noSuchAlgorithmException);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int keyBitCount() {
/* 150 */     return 4096;
/*     */   }
/*     */   
/*     */   public boolean validateProperty(Property paramProperty) {
/*     */     byte[] arrayOfByte;
/* 155 */     Signature signature = signature();
/*     */     
/*     */     try {
/* 158 */       arrayOfByte = Base64.getDecoder().decode(paramProperty.signature());
/* 159 */     } catch (IllegalArgumentException illegalArgumentException) {
/* 160 */       LOGGER.error("Malformed signature encoding on property {}", paramProperty, illegalArgumentException);
/* 161 */       return false;
/*     */     } 
/*     */     try {
/* 164 */       signature.update(paramProperty.value().getBytes());
/* 165 */       return signature.verify(arrayOfByte);
/* 166 */     } catch (SignatureException signatureException) {
/* 167 */       LOGGER.error("Failed to verify signature on property {}", paramProperty, signatureException);
/*     */       
/* 169 */       return false;
/*     */     }  } private static final class KeySetResponse extends Record { @SerializedName("profilePropertyKeys") @Nullable private final List<YggdrasilServicesKeyInfo.KeyData> profilePropertyKeys; @SerializedName("playerCertificateKeys")
/*     */     @Nullable
/* 172 */     private final List<YggdrasilServicesKeyInfo.KeyData> playerCertificateKeys; private KeySetResponse(@Nullable List<YggdrasilServicesKeyInfo.KeyData> param1List1, @Nullable List<YggdrasilServicesKeyInfo.KeyData> param1List2) { this.profilePropertyKeys = param1List1; this.playerCertificateKeys = param1List2; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/YggdrasilServicesKeyInfo$KeySetResponse;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 172 */       //   #172	-> 0 } @SerializedName("profilePropertyKeys") @Nullable public List<YggdrasilServicesKeyInfo.KeyData> profilePropertyKeys() { return this.profilePropertyKeys; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/YggdrasilServicesKeyInfo$KeySetResponse;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #172	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/YggdrasilServicesKeyInfo$KeySetResponse;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 172 */       //   #172	-> 0 } @SerializedName("playerCertificateKeys") @Nullable public List<YggdrasilServicesKeyInfo.KeyData> playerCertificateKeys() { return this.playerCertificateKeys; }
/*     */      }
/*     */ 
/*     */   
/*     */   private static final class KeyData extends Record {
/*     */     @SerializedName("publicKey")
/*     */     private final ByteBuffer publicKey;
/*     */     
/* 180 */     private KeyData(ByteBuffer param1ByteBuffer) { this.publicKey = param1ByteBuffer; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/YggdrasilServicesKeyInfo$KeyData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #180	-> 0 } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/YggdrasilServicesKeyInfo$KeyData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #180	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/YggdrasilServicesKeyInfo$KeyData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 180 */       //   #180	-> 0 } @SerializedName("publicKey") public ByteBuffer publicKey() { return this.publicKey; }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\YggdrasilServicesKeyInfo.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */