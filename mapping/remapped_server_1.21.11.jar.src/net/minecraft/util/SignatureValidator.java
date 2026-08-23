/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.authlib.yggdrasil.ServicesKeyInfo;
/*    */ import com.mojang.authlib.yggdrasil.ServicesKeySet;
/*    */ import com.mojang.authlib.yggdrasil.ServicesKeyType;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.security.PublicKey;
/*    */ import java.security.Signature;
/*    */ import java.security.SignatureException;
/*    */ import java.util.Collection;
/*    */ import java.util.Objects;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public interface SignatureValidator
/*    */ {
/*    */   public static final SignatureValidator NO_VALIDATION = (paramSignatureUpdater, paramArrayOfbyte) -> true;
/* 18 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean validate(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/* 23 */     return validate(paramOutput -> paramOutput.update(paramArrayOfbyte), paramArrayOfbyte2);
/*    */   }
/*    */   
/*    */   private static boolean verifySignature(SignatureUpdater paramSignatureUpdater, byte[] paramArrayOfbyte, Signature paramSignature) throws SignatureException {
/* 27 */     Objects.requireNonNull(paramSignature); paramSignatureUpdater.update(paramSignature::update);
/* 28 */     return paramSignature.verify(paramArrayOfbyte);
/*    */   }
/*    */   
/*    */   static SignatureValidator from(PublicKey paramPublicKey, String paramString) {
/* 32 */     return (paramSignatureUpdater, paramArrayOfbyte) -> {
/*    */         try {
/*    */           Signature signature = Signature.getInstance(paramString);
/*    */           signature.initVerify(paramPublicKey);
/*    */           return verifySignature(paramSignatureUpdater, paramArrayOfbyte, signature);
/* 37 */         } catch (Exception exception) {
/*    */           LOGGER.error("Failed to verify signature", exception);
/*    */           return false;
/*    */         } 
/*    */       };
/*    */   }
/*    */   
/*    */   static SignatureValidator from(ServicesKeySet paramServicesKeySet, ServicesKeyType paramServicesKeyType) {
/* 45 */     Collection collection = paramServicesKeySet.keys(paramServicesKeyType);
/* 46 */     if (collection.isEmpty()) {
/* 47 */       return null;
/*    */     }
/* 49 */     return (paramSignatureUpdater, paramArrayOfbyte) -> paramCollection.stream().anyMatch(());
/*    */   }
/*    */   
/*    */   boolean validate(SignatureUpdater paramSignatureUpdater, byte[] paramArrayOfbyte);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\SignatureValidator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */