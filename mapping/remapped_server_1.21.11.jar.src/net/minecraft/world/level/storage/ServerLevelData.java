/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.world.level.GameType;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.border.WorldBorder;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ import net.minecraft.world.level.timers.TimerQueue;
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
/*    */ public interface ServerLevelData
/*    */   extends WritableLevelData
/*    */ {
/*    */   default void fillCrashReportCategory(CrashReportCategory paramCrashReportCategory, LevelHeightAccessor paramLevelHeightAccessor) {
/* 31 */     super.fillCrashReportCategory(paramCrashReportCategory, paramLevelHeightAccessor);
/* 32 */     paramCrashReportCategory.setDetail("Level name", this::getLevelName);
/* 33 */     paramCrashReportCategory.setDetail("Level game mode", () -> String.format(Locale.ROOT, "Game mode: %s (ID %d). Hardcore: %b. Commands: %b", new Object[] { getGameType().getName(), Integer.valueOf(getGameType().getId()), Boolean.valueOf(isHardcore()), Boolean.valueOf(isAllowCommands()) }));
/* 34 */     paramCrashReportCategory.setDetail("Level weather", () -> String.format(Locale.ROOT, "Rain time: %d (now: %b), thunder time: %d (now: %b)", new Object[] { Integer.valueOf(getRainTime()), Boolean.valueOf(isRaining()), Integer.valueOf(getThunderTime()), Boolean.valueOf(isThundering()) }));
/*    */   }
/*    */   
/*    */   String getLevelName();
/*    */   
/*    */   void setThundering(boolean paramBoolean);
/*    */   
/*    */   int getRainTime();
/*    */   
/*    */   void setRainTime(int paramInt);
/*    */   
/*    */   void setThunderTime(int paramInt);
/*    */   
/*    */   int getThunderTime();
/*    */   
/*    */   int getClearWeatherTime();
/*    */   
/*    */   void setClearWeatherTime(int paramInt);
/*    */   
/*    */   int getWanderingTraderSpawnDelay();
/*    */   
/*    */   void setWanderingTraderSpawnDelay(int paramInt);
/*    */   
/*    */   int getWanderingTraderSpawnChance();
/*    */   
/*    */   void setWanderingTraderSpawnChance(int paramInt);
/*    */   
/*    */   UUID getWanderingTraderId();
/*    */   
/*    */   void setWanderingTraderId(UUID paramUUID);
/*    */   
/*    */   GameType getGameType();
/*    */   
/*    */   @Deprecated
/*    */   Optional<WorldBorder.Settings> getLegacyWorldBorderSettings();
/*    */   
/*    */   @Deprecated
/*    */   void setLegacyWorldBorderSettings(Optional<WorldBorder.Settings> paramOptional);
/*    */   
/*    */   boolean isInitialized();
/*    */   
/*    */   void setInitialized(boolean paramBoolean);
/*    */   
/*    */   boolean isAllowCommands();
/*    */   
/*    */   void setGameType(GameType paramGameType);
/*    */   
/*    */   TimerQueue<MinecraftServer> getScheduledEvents();
/*    */   
/*    */   void setGameTime(long paramLong);
/*    */   
/*    */   void setDayTime(long paramLong);
/*    */   
/*    */   GameRules getGameRules();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\ServerLevelData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */