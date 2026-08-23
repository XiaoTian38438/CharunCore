/*    */ package com.mojang.authlib;
/*    */ 
/*    */ public final class GameProfile extends Record {
/*    */   private final UUID id;
/*    */   private final String name;
/*    */   private final PropertyMap properties;
/*    */   
/*  8 */   public UUID id() { return this.id; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/GameProfile;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0 } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/GameProfile;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/GameProfile;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*  8 */     //   #8	-> 0 } public String name() { return this.name; } public PropertyMap properties() { return this.properties; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GameProfile(UUID paramUUID, String paramString, PropertyMap paramPropertyMap)
/*    */   {
/* 23 */     Objects.requireNonNull(paramUUID, "Profile ID must not be null");
/* 24 */     Objects.requireNonNull(paramString, "Profile name must not be null");
/* 25 */     Objects.requireNonNull(paramPropertyMap, "Profile properties must not be null");
/*    */     this.id = paramUUID;
/*    */     this.name = paramString;
/*    */     this.properties = paramPropertyMap; } public GameProfile(UUID paramUUID, String paramString) {
/* 29 */     this(paramUUID, paramString, PropertyMap.EMPTY);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\GameProfile.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */