/*    */ package com.mojang.authlib.minecraft;
/*    */ 
/*    */ import com.mojang.authlib.exceptions.AuthenticationException;
/*    */ import com.mojang.authlib.minecraft.report.AbuseReportLimits;
/*    */ import com.mojang.authlib.yggdrasil.request.AbuseReportRequest;
/*    */ import com.mojang.authlib.yggdrasil.response.KeyPairResponse;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.Executor;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface UserApiService
/*    */ {
/*    */   public enum UserFlag
/*    */   {
/* 19 */     SERVERS_ALLOWED,
/*    */ 
/*    */ 
/*    */     
/* 23 */     REALMS_ALLOWED,
/*    */ 
/*    */ 
/*    */     
/* 27 */     CHAT_ALLOWED,
/*    */ 
/*    */ 
/*    */     
/* 31 */     TELEMETRY_ENABLED,
/*    */ 
/*    */ 
/*    */     
/* 35 */     PROFANITY_FILTER_ENABLED,
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 40 */     OPTIONAL_TELEMETRY_AVAILABLE; }
/*    */   public static final class UserProperties extends Record { private final Set<UserApiService.UserFlag> flags; private final Map<String, BanDetails> bannedScopes;
/*    */     
/* 43 */     public UserProperties(Set<UserApiService.UserFlag> param1Set, Map<String, BanDetails> param1Map) { this.flags = param1Set; this.bannedScopes = param1Map; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/authlib/minecraft/UserApiService$UserProperties;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 43 */       //   #43	-> 0 } public Set<UserApiService.UserFlag> flags() { return this.flags; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/minecraft/UserApiService$UserProperties;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/authlib/minecraft/UserApiService$UserProperties;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 43 */       //   #43	-> 0 } public Map<String, BanDetails> bannedScopes() { return this.bannedScopes; }
/*    */      public boolean flag(UserApiService.UserFlag param1UserFlag) {
/* 45 */       return this.flags.contains(param1UserFlag);
/*    */     } }
/*    */ 
/*    */   
/* 49 */   public static final UserProperties OFFLINE_PROPERTIES = new UserProperties(Set.of(UserFlag.CHAT_ALLOWED, UserFlag.REALMS_ALLOWED, UserFlag.SERVERS_ALLOWED), Map.of());
/*    */   
/* 51 */   public static final UserApiService OFFLINE = new UserApiService()
/*    */     {
/*    */       public UserApiService.UserProperties fetchProperties() {
/* 54 */         return OFFLINE_PROPERTIES;
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean isBlockedPlayer(UUID param1UUID) {
/* 59 */         return false;
/*    */       }
/*    */ 
/*    */ 
/*    */       
/*    */       public void refreshBlockList() {}
/*    */ 
/*    */       
/*    */       public TelemetrySession newTelemetrySession(Executor param1Executor) {
/* 68 */         return TelemetrySession.DISABLED;
/*    */       }
/*    */ 
/*    */       
/*    */       @Nullable
/*    */       public KeyPairResponse getKeyPair() {
/* 74 */         return null;
/*    */       }
/*    */ 
/*    */ 
/*    */       
/*    */       public void reportAbuse(AbuseReportRequest param1AbuseReportRequest) {}
/*    */ 
/*    */       
/*    */       public boolean canSendReports() {
/* 83 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public AbuseReportLimits getAbuseReportLimits() {
/* 88 */         return AbuseReportLimits.DEFAULTS;
/*    */       }
/*    */     };
/*    */   
/*    */   UserProperties fetchProperties() throws AuthenticationException;
/*    */   
/*    */   boolean isBlockedPlayer(UUID paramUUID);
/*    */   
/*    */   void refreshBlockList();
/*    */   
/*    */   TelemetrySession newTelemetrySession(Executor paramExecutor);
/*    */   
/*    */   @Nullable
/*    */   KeyPairResponse getKeyPair();
/*    */   
/*    */   void reportAbuse(AbuseReportRequest paramAbuseReportRequest);
/*    */   
/*    */   boolean canSendReports();
/*    */   
/*    */   AbuseReportLimits getAbuseReportLimits();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\UserApiService.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */