/*   */ package com.mojang.authlib.yggdrasil.request;public final class JoinMinecraftServerRequest extends Record { @SerializedName("accessToken")
/*   */   private final String accessToken; @SerializedName("selectedProfile")
/*   */   private final UUID selectedProfile;
/*   */   @SerializedName("serverId")
/*   */   private final String serverId;
/*   */   
/* 7 */   public JoinMinecraftServerRequest(String paramString1, UUID paramUUID, String paramString2) { this.accessToken = paramString1; this.selectedProfile = paramUUID; this.serverId = paramString2; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/request/JoinMinecraftServerRequest;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/* 7 */     //   #7	-> 0 } @SerializedName("accessToken") public String accessToken() { return this.accessToken; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/request/JoinMinecraftServerRequest;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/request/JoinMinecraftServerRequest;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/* 7 */     //   #7	-> 0 } @SerializedName("selectedProfile") public UUID selectedProfile() { return this.selectedProfile; } @SerializedName("serverId") public String serverId() { return this.serverId; }
/*   */    }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\request\JoinMinecraftServerRequest.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */