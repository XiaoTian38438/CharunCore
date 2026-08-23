/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.methods.BanlistService;
/*     */ import net.minecraft.server.jsonrpc.methods.GameRulesService;
/*     */ import net.minecraft.server.jsonrpc.methods.IpBanlistService;
/*     */ import net.minecraft.server.jsonrpc.methods.OperatorService;
/*     */ import net.minecraft.server.jsonrpc.methods.ServerStateService;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.notifications.NotificationService;
/*     */ import net.minecraft.server.players.IpBanListEntry;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.ServerOpListEntry;
/*     */ import net.minecraft.server.players.UserBanListEntry;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ 
/*     */ public class JsonRpcNotificationService
/*     */   implements NotificationService
/*     */ {
/*     */   private final ManagementServer managementServer;
/*     */   private final MinecraftApi minecraftApi;
/*     */   
/*     */   public JsonRpcNotificationService(MinecraftApi paramMinecraftApi, ManagementServer paramManagementServer) {
/*  26 */     this.minecraftApi = paramMinecraftApi;
/*  27 */     this.managementServer = paramManagementServer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerJoined(ServerPlayer paramServerPlayer) {
/*  32 */     broadcastNotification(OutgoingRpcMethods.PLAYER_JOINED, PlayerDto.from(paramServerPlayer));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerLeft(ServerPlayer paramServerPlayer) {
/*  37 */     broadcastNotification(OutgoingRpcMethods.PLAYER_LEFT, PlayerDto.from(paramServerPlayer));
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverStarted() {
/*  42 */     broadcastNotification(OutgoingRpcMethods.SERVER_STARTED);
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverShuttingDown() {
/*  47 */     broadcastNotification(OutgoingRpcMethods.SERVER_SHUTTING_DOWN);
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverSaveStarted() {
/*  52 */     broadcastNotification(OutgoingRpcMethods.SERVER_SAVE_STARTED);
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverSaveCompleted() {
/*  57 */     broadcastNotification(OutgoingRpcMethods.SERVER_SAVE_COMPLETED);
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverActivityOccured() {
/*  62 */     broadcastNotification(OutgoingRpcMethods.SERVER_ACTIVITY_OCCURRED);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerOped(ServerOpListEntry paramServerOpListEntry) {
/*  67 */     broadcastNotification(OutgoingRpcMethods.PLAYER_OPED, OperatorService.OperatorDto.from(paramServerOpListEntry));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerDeoped(ServerOpListEntry paramServerOpListEntry) {
/*  72 */     broadcastNotification(OutgoingRpcMethods.PLAYER_DEOPED, OperatorService.OperatorDto.from(paramServerOpListEntry));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerAddedToAllowlist(NameAndId paramNameAndId) {
/*  77 */     broadcastNotification(OutgoingRpcMethods.PLAYER_ADDED_TO_ALLOWLIST, PlayerDto.from(paramNameAndId));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerRemovedFromAllowlist(NameAndId paramNameAndId) {
/*  82 */     broadcastNotification(OutgoingRpcMethods.PLAYER_REMOVED_FROM_ALLOWLIST, PlayerDto.from(paramNameAndId));
/*     */   }
/*     */ 
/*     */   
/*     */   public void ipBanned(IpBanListEntry paramIpBanListEntry) {
/*  87 */     broadcastNotification(OutgoingRpcMethods.IP_BANNED, IpBanlistService.IpBanDto.from(paramIpBanListEntry));
/*     */   }
/*     */ 
/*     */   
/*     */   public void ipUnbanned(String paramString) {
/*  92 */     broadcastNotification(OutgoingRpcMethods.IP_UNBANNED, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerBanned(UserBanListEntry paramUserBanListEntry) {
/*  97 */     broadcastNotification(OutgoingRpcMethods.PLAYER_BANNED, BanlistService.UserBanDto.from(paramUserBanListEntry));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerUnbanned(NameAndId paramNameAndId) {
/* 102 */     broadcastNotification(OutgoingRpcMethods.PLAYER_UNBANNED, PlayerDto.from(paramNameAndId));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> void onGameRuleChanged(GameRule<T> paramGameRule, T paramT) {
/* 107 */     broadcastNotification(OutgoingRpcMethods.GAMERULE_CHANGED, GameRulesService.getTypedRule(this.minecraftApi, paramGameRule, paramT));
/*     */   }
/*     */ 
/*     */   
/*     */   public void statusHeartbeat() {
/* 112 */     broadcastNotification(OutgoingRpcMethods.STATUS_HEARTBEAT, ServerStateService.status(this.minecraftApi));
/*     */   }
/*     */   
/*     */   private void broadcastNotification(Holder.Reference<? extends OutgoingRpcMethod<Void, ?>> paramReference) {
/* 116 */     this.managementServer.forEachConnection(paramConnection -> paramConnection.sendNotification(paramReference));
/*     */   }
/*     */   
/*     */   private <Params> void broadcastNotification(Holder.Reference<? extends OutgoingRpcMethod<Params, ?>> paramReference, Params paramParams) {
/* 120 */     this.managementServer.forEachConnection(paramConnection -> paramConnection.sendNotification(paramReference, paramObject));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\JsonRpcNotificationService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */