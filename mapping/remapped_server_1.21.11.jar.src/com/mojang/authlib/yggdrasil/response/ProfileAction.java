/*   */ package com.mojang.authlib.yggdrasil.response;
/*   */ 
/*   */ import com.mojang.authlib.yggdrasil.ProfileActionType;
/*   */ 
/*   */ public final class ProfileAction extends Record {
/* 6 */   public ProfileAction(ProfileActionType paramProfileActionType) { this.type = paramProfileActionType; } @SerializedName("action") private final ProfileActionType type; @SerializedName("action") public ProfileActionType type() { return this.type; }
/*   */ 
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/ProfileAction;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/ProfileAction;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */   }
/*   */   
/*   */   public final boolean equals(Object paramObject) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/ProfileAction;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */   }
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\response\ProfileAction.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */