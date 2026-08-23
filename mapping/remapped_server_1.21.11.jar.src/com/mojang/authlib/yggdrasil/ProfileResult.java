/*    */ package com.mojang.authlib.yggdrasil;
/*    */ 
/*    */ public final class ProfileResult extends Record {
/*    */   private final GameProfile profile;
/*    */   private final Set<ProfileActionType> actions;
/*    */   
/*  7 */   public Set<ProfileActionType> actions() { return this.actions; } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/ProfileResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*  7 */     //   #7	-> 0 } public GameProfile profile() { return this.profile; } public ProfileResult(GameProfile paramGameProfile, Set<ProfileActionType> paramSet) { this.profile = paramGameProfile; this.actions = paramSet; } public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/ProfileResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */   } public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/ProfileResult;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */   } public ProfileResult(GameProfile paramGameProfile) {
/* 12 */     this(paramGameProfile, Set.of());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\ProfileResult.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */