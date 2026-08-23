/*   */ package com.mojang.authlib.yggdrasil.response;public final class MinecraftTexturesPayload extends Record { @SerializedName("timestamp")
/*   */   private final long timestamp; @SerializedName("profileId")
/*   */   private final UUID profileId; @SerializedName("profileName")
/*   */   private final String profileName; @SerializedName("isPublic")
/*   */   private final boolean isPublic;
/*   */   @SerializedName("textures")
/*   */   private final Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures;
/*   */   
/* 9 */   public MinecraftTexturesPayload(long paramLong, UUID paramUUID, String paramString, boolean paramBoolean, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> paramMap) { this.timestamp = paramLong; this.profileId = paramUUID; this.profileName = paramString; this.isPublic = paramBoolean; this.textures = paramMap; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/MinecraftTexturesPayload;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/* 9 */     //   #9	-> 0 } @SerializedName("timestamp") public long timestamp() { return this.timestamp; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/MinecraftTexturesPayload;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #9	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/MinecraftTexturesPayload;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/* 9 */     //   #9	-> 0 } @SerializedName("profileId") public UUID profileId() { return this.profileId; } @SerializedName("profileName") public String profileName() { return this.profileName; } @SerializedName("isPublic") public boolean isPublic() { return this.isPublic; } @SerializedName("textures") public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures() { return this.textures; }
/*   */    }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\response\MinecraftTexturesPayload.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */