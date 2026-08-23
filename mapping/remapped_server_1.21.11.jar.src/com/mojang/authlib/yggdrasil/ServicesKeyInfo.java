/*    */ package com.mojang.authlib.yggdrasil;
/*    */ 
/*    */ import com.mojang.authlib.properties.Property;
/*    */ import java.security.Signature;
/*    */ 
/*    */ public interface ServicesKeyInfo
/*    */ {
/*    */   int keyBitCount();
/*    */   
/*    */   default int signatureBitCount() {
/* 11 */     return keyBitCount();
/*    */   }
/*    */   
/*    */   Signature signature();
/*    */   
/*    */   boolean validateProperty(Property paramProperty);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\ServicesKeyInfo.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */