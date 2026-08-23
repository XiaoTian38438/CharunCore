/*   */ package com.mojang.authlib.yggdrasil.response;
/*   */ public final class NameAndId extends Record { @SerializedName("id")
/*   */   private final UUID id;
/*   */   @SerializedName("name")
/*   */   private final String name;
/*   */   
/* 7 */   public NameAndId(UUID paramUUID, String paramString) { this.id = paramUUID; this.name = paramString; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/NameAndId;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/* 7 */     //   #7	-> 0 } @SerializedName("id") public UUID id() { return this.id; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/NameAndId;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/NameAndId;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/* 7 */     //   #7	-> 0 } @SerializedName("name") public String name() { return this.name; }
/*   */    }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\response\NameAndId.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */