/*    */ package com.mojang.authlib.yggdrasil.response;
/*    */ public final class KeyPair extends Record { @SerializedName("privateKey")
/*    */   private final String privateKey; @SerializedName("publicKey")
/*    */   private final String publicKey;
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/KeyPairResponse$KeyPair;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/KeyPairResponse$KeyPair;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */   }
/*    */   
/*    */   public final boolean equals(Object paramObject) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/KeyPairResponse$KeyPair;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */   }
/*    */   
/* 17 */   public KeyPair(String paramString1, String paramString2) { this.privateKey = paramString1; this.publicKey = paramString2; } @SerializedName("privateKey") public String privateKey() { return this.privateKey; } @SerializedName("publicKey") public String publicKey() { return this.publicKey; }
/*    */    }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\response\KeyPairResponse$KeyPair.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */