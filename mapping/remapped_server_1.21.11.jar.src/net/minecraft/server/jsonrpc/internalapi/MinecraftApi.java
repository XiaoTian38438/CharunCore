/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.notifications.NotificationManager;
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
/*    */ public class MinecraftApi
/*    */ {
/*    */   private final NotificationManager notificationManager;
/*    */   private final MinecraftAllowListService allowListService;
/*    */   private final MinecraftBanListService banListService;
/*    */   private final MinecraftPlayerListService minecraftPlayerListService;
/*    */   private final MinecraftGameRuleService gameRuleService;
/*    */   private final MinecraftOperatorListService minecraftOperatorListService;
/*    */   private final MinecraftServerSettingsService minecraftServerSettingsService;
/*    */   private final MinecraftServerStateService minecraftServerStateService;
/*    */   private final MinecraftExecutorService executorService;
/*    */   
/*    */   public MinecraftApi(NotificationManager paramNotificationManager, MinecraftAllowListService paramMinecraftAllowListService, MinecraftBanListService paramMinecraftBanListService, MinecraftPlayerListService paramMinecraftPlayerListService, MinecraftGameRuleService paramMinecraftGameRuleService, MinecraftOperatorListService paramMinecraftOperatorListService, MinecraftServerSettingsService paramMinecraftServerSettingsService, MinecraftServerStateService paramMinecraftServerStateService, MinecraftExecutorService paramMinecraftExecutorService) {
/* 32 */     this.notificationManager = paramNotificationManager;
/* 33 */     this.allowListService = paramMinecraftAllowListService;
/* 34 */     this.banListService = paramMinecraftBanListService;
/* 35 */     this.minecraftPlayerListService = paramMinecraftPlayerListService;
/* 36 */     this.gameRuleService = paramMinecraftGameRuleService;
/* 37 */     this.minecraftOperatorListService = paramMinecraftOperatorListService;
/* 38 */     this.minecraftServerSettingsService = paramMinecraftServerSettingsService;
/* 39 */     this.minecraftServerStateService = paramMinecraftServerStateService;
/* 40 */     this.executorService = paramMinecraftExecutorService;
/*    */   }
/*    */   
/*    */   public <V> CompletableFuture<V> submit(Supplier<V> paramSupplier) {
/* 44 */     return this.executorService.submit(paramSupplier);
/*    */   }
/*    */   
/*    */   public CompletableFuture<Void> submit(Runnable paramRunnable) {
/* 48 */     return this.executorService.submit(paramRunnable);
/*    */   }
/*    */   
/*    */   public MinecraftAllowListService allowListService() {
/* 52 */     return this.allowListService;
/*    */   }
/*    */   
/*    */   public MinecraftBanListService banListService() {
/* 56 */     return this.banListService;
/*    */   }
/*    */   
/*    */   public MinecraftPlayerListService playerListService() {
/* 60 */     return this.minecraftPlayerListService;
/*    */   }
/*    */   
/*    */   public MinecraftGameRuleService gameRuleService() {
/* 64 */     return this.gameRuleService;
/*    */   }
/*    */   
/*    */   public MinecraftOperatorListService operatorListService() {
/* 68 */     return this.minecraftOperatorListService;
/*    */   }
/*    */   
/*    */   public MinecraftServerSettingsService serverSettingsService() {
/* 72 */     return this.minecraftServerSettingsService;
/*    */   }
/*    */   
/*    */   public MinecraftServerStateService serverStateService() {
/* 76 */     return this.minecraftServerStateService;
/*    */   }
/*    */   
/*    */   public NotificationManager notificationManager() {
/* 80 */     return this.notificationManager;
/*    */   }
/*    */   
/*    */   public static MinecraftApi of(DedicatedServer paramDedicatedServer) {
/* 84 */     JsonRpcLogger jsonRpcLogger = new JsonRpcLogger();
/* 85 */     MinecraftAllowListServiceImpl minecraftAllowListServiceImpl = new MinecraftAllowListServiceImpl(paramDedicatedServer, jsonRpcLogger);
/* 86 */     MinecraftBanListServiceImpl minecraftBanListServiceImpl = new MinecraftBanListServiceImpl((MinecraftServer)paramDedicatedServer, jsonRpcLogger);
/* 87 */     MinecraftPlayerListServiceImpl minecraftPlayerListServiceImpl = new MinecraftPlayerListServiceImpl(paramDedicatedServer, jsonRpcLogger);
/* 88 */     MinecraftGameRuleServiceImpl minecraftGameRuleServiceImpl = new MinecraftGameRuleServiceImpl(paramDedicatedServer, jsonRpcLogger);
/* 89 */     MinecraftOperatorListServiceImpl minecraftOperatorListServiceImpl = new MinecraftOperatorListServiceImpl((MinecraftServer)paramDedicatedServer, jsonRpcLogger);
/* 90 */     MinecraftServerSettingsServiceImpl minecraftServerSettingsServiceImpl = new MinecraftServerSettingsServiceImpl(paramDedicatedServer, jsonRpcLogger);
/* 91 */     MinecraftServerStateServiceImpl minecraftServerStateServiceImpl = new MinecraftServerStateServiceImpl(paramDedicatedServer, jsonRpcLogger);
/* 92 */     MinecraftExecutorServiceImpl minecraftExecutorServiceImpl = new MinecraftExecutorServiceImpl(paramDedicatedServer);
/* 93 */     return new MinecraftApi(paramDedicatedServer
/* 94 */         .notificationManager(), minecraftAllowListServiceImpl, minecraftBanListServiceImpl, minecraftPlayerListServiceImpl, minecraftGameRuleServiceImpl, minecraftOperatorListServiceImpl, minecraftServerSettingsServiceImpl, minecraftServerStateServiceImpl, minecraftExecutorServiceImpl);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftApi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */