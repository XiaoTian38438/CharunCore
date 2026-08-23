/*     */ package net.minecraft.server.notifications;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.players.IpBanListEntry;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.ServerOpListEntry;
/*     */ import net.minecraft.server.players.UserBanListEntry;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ 
/*     */ public class NotificationManager
/*     */   implements NotificationService
/*     */ {
/*  15 */   private final List<NotificationService> notificationServices = Lists.newArrayList();
/*     */   
/*     */   public void registerService(NotificationService paramNotificationService) {
/*  18 */     this.notificationServices.add(paramNotificationService);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerJoined(ServerPlayer paramServerPlayer) {
/*  23 */     this.notificationServices.forEach(paramNotificationService -> paramNotificationService.playerJoined(paramServerPlayer));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerLeft(ServerPlayer paramServerPlayer) {
/*  28 */     this.notificationServices.forEach(paramNotificationService -> paramNotificationService.playerLeft(paramServerPlayer));
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverStarted() {
/*  33 */     this.notificationServices.forEach(NotificationService::serverStarted);
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverShuttingDown() {
/*  38 */     this.notificationServices.forEach(NotificationService::serverShuttingDown);
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverSaveStarted() {
/*  43 */     this.notificationServices.forEach(NotificationService::serverSaveStarted);
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverSaveCompleted() {
/*  48 */     this.notificationServices.forEach(NotificationService::serverSaveCompleted);
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverActivityOccured() {
/*  53 */     this.notificationServices.forEach(NotificationService::serverActivityOccured);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerOped(ServerOpListEntry paramServerOpListEntry) {
/*  58 */     this.notificationServices.forEach(paramNotificationService -> paramNotificationService.playerOped(paramServerOpListEntry));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerDeoped(ServerOpListEntry paramServerOpListEntry) {
/*  63 */     this.notificationServices.forEach(paramNotificationService -> paramNotificationService.playerDeoped(paramServerOpListEntry));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerAddedToAllowlist(NameAndId paramNameAndId) {
/*  68 */     this.notificationServices.forEach(paramNotificationService -> paramNotificationService.playerAddedToAllowlist(paramNameAndId));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerRemovedFromAllowlist(NameAndId paramNameAndId) {
/*  73 */     this.notificationServices.forEach(paramNotificationService -> paramNotificationService.playerRemovedFromAllowlist(paramNameAndId));
/*     */   }
/*     */ 
/*     */   
/*     */   public void ipBanned(IpBanListEntry paramIpBanListEntry) {
/*  78 */     this.notificationServices.forEach(paramNotificationService -> paramNotificationService.ipBanned(paramIpBanListEntry));
/*     */   }
/*     */ 
/*     */   
/*     */   public void ipUnbanned(String paramString) {
/*  83 */     this.notificationServices.forEach(paramNotificationService -> paramNotificationService.ipUnbanned(paramString));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerBanned(UserBanListEntry paramUserBanListEntry) {
/*  88 */     this.notificationServices.forEach(paramNotificationService -> paramNotificationService.playerBanned(paramUserBanListEntry));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerUnbanned(NameAndId paramNameAndId) {
/*  93 */     this.notificationServices.forEach(paramNotificationService -> paramNotificationService.playerUnbanned(paramNameAndId));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> void onGameRuleChanged(GameRule<T> paramGameRule, T paramT) {
/*  98 */     this.notificationServices.forEach(paramNotificationService -> paramNotificationService.onGameRuleChanged(paramGameRule, paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public void statusHeartbeat() {
/* 103 */     this.notificationServices.forEach(NotificationService::statusHeartbeat);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\notifications\NotificationManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */