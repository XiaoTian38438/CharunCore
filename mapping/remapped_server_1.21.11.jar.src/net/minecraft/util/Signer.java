/*    */ package net.minecraft.util;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.security.PrivateKey;
/*    */ import java.security.Signature;
/*    */ import java.security.SignatureException;
/*    */ import java.util.Objects;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public interface Signer {
/* 10 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */ 
/*    */   
/*    */   default byte[] sign(byte[] paramArrayOfbyte) {
/* 15 */     return sign(paramOutput -> paramOutput.update(paramArrayOfbyte));
/*    */   }
/*    */   
/*    */   static Signer from(PrivateKey paramPrivateKey, String paramString) {
/* 19 */     return paramSignatureUpdater -> {
/*    */         try {
/*    */           Signature signature = Signature.getInstance(paramString); signature.initSign(paramPrivateKey);
/*    */           Objects.requireNonNull(signature);
/*    */           paramSignatureUpdater.update(signature::update);
/*    */           return signature.sign();
/* 25 */         } catch (Exception exception) {
/*    */           throw new IllegalStateException("Failed to sign message", exception);
/*    */         } 
/*    */       };
/*    */   }
/*    */   
/*    */   byte[] sign(SignatureUpdater paramSignatureUpdater);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\Signer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */