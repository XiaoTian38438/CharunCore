/*    */ package com.mojang.authlib.minecraft;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class UserProperties
/*    */   extends Record
/*    */ {
/*    */   private final Set<UserApiService.UserFlag> flags;
/*    */   private final Map<String, BanDetails> bannedScopes;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/minecraft/UserApiService$UserProperties;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #43	-> 0
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/minecraft/UserApiService$UserProperties;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #43	-> 0
/*    */   }
/*    */   
/*    */   public final boolean equals(Object paramObject) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/minecraft/UserApiService$UserProperties;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #43	-> 0
/*    */   }
/*    */   
/*    */   public UserProperties(Set<UserApiService.UserFlag> paramSet, Map<String, BanDetails> paramMap) {
/* 43 */     this.flags = paramSet; this.bannedScopes = paramMap; } public Set<UserApiService.UserFlag> flags() { return this.flags; } public Map<String, BanDetails> bannedScopes() { return this.bannedScopes; }
/*    */    public boolean flag(UserApiService.UserFlag paramUserFlag) {
/* 45 */     return this.flags.contains(paramUserFlag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\UserApiService$UserProperties.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */