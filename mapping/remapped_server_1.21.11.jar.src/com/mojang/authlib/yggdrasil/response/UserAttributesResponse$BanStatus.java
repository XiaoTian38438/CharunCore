/*    */ package com.mojang.authlib.yggdrasil.response;
/*    */ 
/*    */ import com.google.gson.annotations.SerializedName;
/*    */ import java.time.Instant;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class BanStatus
/*    */   extends Record
/*    */ {
/*    */   @SerializedName("bannedScopes")
/*    */   private final Map<String, BannedScope> bannedScopes;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #63	-> 0
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #63	-> 0
/*    */   }
/*    */   
/*    */   public BanStatus(Map<String, BannedScope> paramMap) {
/* 63 */     this.bannedScopes = paramMap; } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 63 */     //   #63	-> 0 } @SerializedName("bannedScopes") public Map<String, BannedScope> bannedScopes() { return this.bannedScopes; } public static final class BannedScope extends Record { @SerializedName("banId") private final UUID banId; @SerializedName("expires") @Nullable
/*    */     private final Instant expires; @SerializedName("reason")
/*    */     private final String reason; @SerializedName("reasonMessage")
/*    */     @Nullable
/* 67 */     private final String reasonMessage; public BannedScope(UUID param2UUID, @Nullable Instant param2Instant, String param2String1, @Nullable String param2String2) { this.banId = param2UUID; this.expires = param2Instant; this.reason = param2String1; this.reasonMessage = param2String2; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus$BannedScope;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 67 */       //   #67	-> 0 } @SerializedName("banId") public UUID banId() { return this.banId; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus$BannedScope;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #67	-> 0 } public final boolean equals(Object param2Object) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus$BannedScope;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 67 */       //   #67	-> 0 } @SerializedName("expires") @Nullable public Instant expires() { return this.expires; } @SerializedName("reason") public String reason() { return this.reason; } @SerializedName("reasonMessage") @Nullable public String reasonMessage() { return this.reasonMessage; }
/*    */      }
/*    */ 
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\response\UserAttributesResponse$BanStatus.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */