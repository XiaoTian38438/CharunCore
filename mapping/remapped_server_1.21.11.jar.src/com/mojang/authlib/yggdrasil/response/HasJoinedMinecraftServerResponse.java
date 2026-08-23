/*    */ package com.mojang.authlib.yggdrasil.response;public final class HasJoinedMinecraftServerResponse extends Record { @SerializedName("id")
/*    */   @Nullable
/*    */   private final UUID id; @SerializedName("properties")
/*    */   @Nullable
/*    */   private final PropertyMap properties;
/*    */   @SerializedName("profileActions")
/*    */   @Nullable
/*    */   private final Set<ProfileAction> profileActions;
/*    */   
/* 10 */   public HasJoinedMinecraftServerResponse(@Nullable UUID paramUUID, @Nullable PropertyMap paramPropertyMap, @Nullable Set<ProfileAction> paramSet) { this.id = paramUUID; this.properties = paramPropertyMap; this.profileActions = paramSet; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/HasJoinedMinecraftServerResponse;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 10 */     //   #10	-> 0 } @SerializedName("id") @Nullable public UUID id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/HasJoinedMinecraftServerResponse;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/HasJoinedMinecraftServerResponse;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 10 */     //   #10	-> 0 } @SerializedName("properties") @Nullable public PropertyMap properties() { return this.properties; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<ProfileAction> profileActions() {
/* 20 */     return (this.profileActions != null) ? this.profileActions : Set.<ProfileAction>of();
/*    */   } }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\response\HasJoinedMinecraftServerResponse.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */