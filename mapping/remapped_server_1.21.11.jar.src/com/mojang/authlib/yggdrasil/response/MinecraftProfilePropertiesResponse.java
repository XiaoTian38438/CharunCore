/*    */ package com.mojang.authlib.yggdrasil.response;public final class MinecraftProfilePropertiesResponse extends Record { @SerializedName("id")
/*    */   private final UUID id;
/*    */   @SerializedName("name")
/*    */   private final String name;
/*    */   @SerializedName("properties")
/*    */   private final PropertyMap properties;
/*    */   @SerializedName("profileActions")
/*    */   @Nullable
/*    */   private final Set<ProfileAction> profileActions;
/*    */   
/* 11 */   public MinecraftProfilePropertiesResponse(UUID paramUUID, String paramString, PropertyMap paramPropertyMap, @Nullable Set<ProfileAction> paramSet) { this.id = paramUUID; this.name = paramString; this.properties = paramPropertyMap; this.profileActions = paramSet; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/MinecraftProfilePropertiesResponse;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 11 */     //   #11	-> 0 } @SerializedName("id") public UUID id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/MinecraftProfilePropertiesResponse;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/MinecraftProfilePropertiesResponse;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 11 */     //   #11	-> 0 } @SerializedName("name") public String name() { return this.name; } @SerializedName("properties") public PropertyMap properties() { return this.properties; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GameProfile profile() {
/* 22 */     return new GameProfile(this.id, this.name, this.properties);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ProfileAction> profileActions() {
/* 27 */     return (this.profileActions != null) ? this.profileActions : Set.<ProfileAction>of();
/*    */   } }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\response\MinecraftProfilePropertiesResponse.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */