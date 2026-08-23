/*    */ package com.mojang.authlib.yggdrasil.response;
/*    */ 
/*    */ import com.google.gson.annotations.SerializedName;
/*    */ import java.time.Instant;
/*    */ import java.util.UUID;
/*    */ import javax.annotation.Nullable;
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
/*    */ public final class BannedScope
/*    */   extends Record
/*    */ {
/*    */   @SerializedName("banId")
/*    */   private final UUID banId;
/*    */   @SerializedName("expires")
/*    */   @Nullable
/*    */   private final Instant expires;
/*    */   @SerializedName("reason")
/*    */   private final String reason;
/*    */   @SerializedName("reasonMessage")
/*    */   @Nullable
/*    */   private final String reasonMessage;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus$BannedScope;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #67	-> 0
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus$BannedScope;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #67	-> 0
/*    */   }
/*    */   
/*    */   public final boolean equals(Object paramObject) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus$BannedScope;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #67	-> 0
/*    */   }
/*    */   
/*    */   public BannedScope(UUID paramUUID, @Nullable Instant paramInstant, String paramString1, @Nullable String paramString2) {
/* 67 */     this.banId = paramUUID; this.expires = paramInstant; this.reason = paramString1; this.reasonMessage = paramString2; } @SerializedName("banId") public UUID banId() { return this.banId; } @SerializedName("expires") @Nullable public Instant expires() { return this.expires; } @SerializedName("reason") public String reason() { return this.reason; } @SerializedName("reasonMessage") @Nullable public String reasonMessage() { return this.reasonMessage; }
/*    */ 
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\response\UserAttributesResponse$BanStatus$BannedScope.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */