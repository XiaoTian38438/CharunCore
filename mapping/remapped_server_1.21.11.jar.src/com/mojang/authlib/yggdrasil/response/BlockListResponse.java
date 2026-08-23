/*    */ package com.mojang.authlib.yggdrasil.response;
/*    */ 
/*    */ import com.google.gson.annotations.SerializedName;
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ public final class BlockListResponse
/*    */   extends Record
/*    */ {
/*    */   @SerializedName("blockedProfiles")
/*    */   private final Set<UUID> blockedProfiles;
/*    */   
/*    */   public BlockListResponse(Set<UUID> paramSet) {
/* 15 */     this.blockedProfiles = paramSet; } @SerializedName("blockedProfiles") public Set<UUID> blockedProfiles() { return this.blockedProfiles; }
/*    */ 
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/BlockListResponse;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/BlockListResponse;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */   }
/*    */   
/*    */   public final boolean equals(Object paramObject) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/BlockListResponse;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\response\BlockListResponse.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */