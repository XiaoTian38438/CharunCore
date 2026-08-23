/*    */ package com.mojang.authlib.properties;
/*    */ 
/*    */ import com.google.gson.annotations.SerializedName;
/*    */ import java.security.Signature;
/*    */ 
/*    */ public final class Property extends Record {
/*    */   @SerializedName("name")
/*    */   private final String name;
/*    */   @SerializedName("value")
/*    */   private final String value;
/*    */   @SerializedName("signature")
/*    */   @Nullable
/*    */   private final String signature;
/*    */   
/* 15 */   public Property(String paramString1, String paramString2, @Nullable String paramString3) { this.name = paramString1; this.value = paramString2; this.signature = paramString3; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/properties/Property;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 15 */     //   #15	-> 0 } @SerializedName("name") public String name() { return this.name; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/properties/Property;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/properties/Property;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 15 */     //   #15	-> 0 } @SerializedName("value") public String value() { return this.value; } @SerializedName("signature") @Nullable public String signature() { return this.signature; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Property(String paramString1, String paramString2) {
/* 24 */     this(paramString1, paramString2, null);
/*    */   }
/*    */   
/*    */   public boolean hasSignature() {
/* 28 */     return (this.signature != null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public boolean isSignatureValid(PublicKey paramPublicKey) {
/*    */     try {
/* 37 */       Signature signature = Signature.getInstance("SHA1withRSA");
/* 38 */       signature.initVerify(paramPublicKey);
/* 39 */       signature.update(this.value.getBytes(StandardCharsets.US_ASCII));
/* 40 */       return signature.verify(Base64.getDecoder().decode(this.signature));
/* 41 */     } catch (NoSuchAlgorithmException|java.security.InvalidKeyException|java.security.SignatureException noSuchAlgorithmException) {
/* 42 */       noSuchAlgorithmException.printStackTrace();
/*    */       
/* 44 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\properties\Property.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */