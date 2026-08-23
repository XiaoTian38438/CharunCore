/*    */ package com.mojang.authlib.yggdrasil.response;public final class KeyPairResponse extends Record { @SerializedName("keyPair")
/*    */   private final KeyPair keyPair; @SerializedName("publicKeySignatureV2")
/*    */   private final ByteBuffer publicKeySignature; @SerializedName("expiresAt")
/*    */   private final String expiresAt; @SerializedName("refreshedAfter")
/*    */   private final String refreshedAfter;
/*    */   
/*  7 */   public KeyPairResponse(KeyPair paramKeyPair, ByteBuffer paramByteBuffer, String paramString1, String paramString2) { this.keyPair = paramKeyPair; this.publicKeySignature = paramByteBuffer; this.expiresAt = paramString1; this.refreshedAfter = paramString2; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/KeyPairResponse;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*  7 */     //   #7	-> 0 } @SerializedName("keyPair") public KeyPair keyPair() { return this.keyPair; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/KeyPairResponse;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/KeyPairResponse;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*  7 */     //   #7	-> 0 } @SerializedName("publicKeySignatureV2") public ByteBuffer publicKeySignature() { return this.publicKeySignature; } @SerializedName("expiresAt") public String expiresAt() { return this.expiresAt; } @SerializedName("refreshedAfter") public String refreshedAfter() { return this.refreshedAfter; }
/*    */ 
/*    */   
/*    */   public static final class KeyPair extends Record {
/*    */     @SerializedName("privateKey")
/*    */     private final String privateKey;
/*    */     @SerializedName("publicKey")
/*    */     private final String publicKey;
/*    */     
/*    */     public KeyPair(String param1String1, String param1String2) {
/* 17 */       this.privateKey = param1String1; this.publicKey = param1String2; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/KeyPairResponse$KeyPair;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #17	-> 0 } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/KeyPairResponse$KeyPair;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #17	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/KeyPairResponse$KeyPair;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 17 */       //   #17	-> 0 } @SerializedName("privateKey") public String privateKey() { return this.privateKey; } @SerializedName("publicKey") public String publicKey() { return this.publicKey; }
/*    */   
/*    */   } }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\response\KeyPairResponse.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */