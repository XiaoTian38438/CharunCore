/*    */ package com.mojang.authlib.minecraft;
/*    */ 
/*    */ import com.mojang.authlib.minecraft.report.AbuseReportLimits;
/*    */ import com.mojang.authlib.yggdrasil.request.AbuseReportRequest;
/*    */ import com.mojang.authlib.yggdrasil.response.KeyPairResponse;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.Executor;
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
/*    */ 
/*    */ class null
/*    */   implements UserApiService
/*    */ {
/*    */   public UserApiService.UserProperties fetchProperties() {
/* 54 */     return OFFLINE_PROPERTIES;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBlockedPlayer(UUID paramUUID) {
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void refreshBlockList() {}
/*    */ 
/*    */   
/*    */   public TelemetrySession newTelemetrySession(Executor paramExecutor) {
/* 68 */     return TelemetrySession.DISABLED;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public KeyPairResponse getKeyPair() {
/* 74 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void reportAbuse(AbuseReportRequest paramAbuseReportRequest) {}
/*    */ 
/*    */   
/*    */   public boolean canSendReports() {
/* 83 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public AbuseReportLimits getAbuseReportLimits() {
/* 88 */     return AbuseReportLimits.DEFAULTS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\UserApiService$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */