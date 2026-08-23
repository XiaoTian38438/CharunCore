/*     */ package net.minecraft.server.dedicated;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.RegistryLayer;
/*     */ import net.minecraft.server.notifications.NotificationService;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.world.level.storage.PlayerDataStorage;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DedicatedPlayerList extends PlayerList {
/*  14 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public DedicatedPlayerList(DedicatedServer paramDedicatedServer, LayeredRegistryAccess<RegistryLayer> paramLayeredRegistryAccess, PlayerDataStorage paramPlayerDataStorage) {
/*  17 */     super(paramDedicatedServer, paramLayeredRegistryAccess, paramPlayerDataStorage, (NotificationService)paramDedicatedServer.notificationManager());
/*     */     
/*  19 */     setViewDistance(paramDedicatedServer.viewDistance());
/*  20 */     setSimulationDistance(paramDedicatedServer.simulationDistance());
/*     */     
/*  22 */     loadUserBanList();
/*  23 */     saveUserBanList();
/*  24 */     loadIpBanList();
/*  25 */     saveIpBanList();
/*  26 */     loadOps();
/*  27 */     loadWhiteList();
/*  28 */     saveOps();
/*  29 */     if (!getWhiteList().getFile().exists()) {
/*  30 */       saveWhiteList();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void reloadWhiteList() {
/*  36 */     loadWhiteList();
/*     */   }
/*     */   
/*     */   private void saveIpBanList() {
/*     */     try {
/*  41 */       getIpBans().save();
/*  42 */     } catch (IOException iOException) {
/*  43 */       LOGGER.warn("Failed to save ip banlist: ", iOException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void saveUserBanList() {
/*     */     try {
/*  49 */       getBans().save();
/*  50 */     } catch (IOException iOException) {
/*  51 */       LOGGER.warn("Failed to save user banlist: ", iOException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadIpBanList() {
/*     */     try {
/*  57 */       getIpBans().load();
/*  58 */     } catch (IOException iOException) {
/*  59 */       LOGGER.warn("Failed to load ip banlist: ", iOException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadUserBanList() {
/*     */     try {
/*  65 */       getBans().load();
/*  66 */     } catch (IOException iOException) {
/*  67 */       LOGGER.warn("Failed to load user banlist: ", iOException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadOps() {
/*     */     try {
/*  73 */       getOps().load();
/*  74 */     } catch (Exception exception) {
/*  75 */       LOGGER.warn("Failed to load operators list: ", exception);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void saveOps() {
/*     */     try {
/*  81 */       getOps().save();
/*  82 */     } catch (Exception exception) {
/*  83 */       LOGGER.warn("Failed to save operators list: ", exception);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadWhiteList() {
/*     */     try {
/*  89 */       getWhiteList().load();
/*  90 */     } catch (Exception exception) {
/*  91 */       LOGGER.warn("Failed to load white-list: ", exception);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void saveWhiteList() {
/*     */     try {
/*  97 */       getWhiteList().save();
/*  98 */     } catch (Exception exception) {
/*  99 */       LOGGER.warn("Failed to save white-list: ", exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWhiteListed(NameAndId paramNameAndId) {
/* 105 */     return (!isUsingWhitelist() || isOp(paramNameAndId) || getWhiteList().isWhiteListed(paramNameAndId));
/*     */   }
/*     */ 
/*     */   
/*     */   public DedicatedServer getServer() {
/* 110 */     return (DedicatedServer)super.getServer();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBypassPlayerLimit(NameAndId paramNameAndId) {
/* 115 */     return getOps().canBypassPlayerLimit(paramNameAndId);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\dedicated\DedicatedPlayerList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */