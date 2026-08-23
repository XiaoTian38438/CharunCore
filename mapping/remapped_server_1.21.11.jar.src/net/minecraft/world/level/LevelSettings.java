/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class LevelSettings {
/* 11 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   private final String levelName;
/*    */   private final GameType gameType;
/*    */   private final boolean hardcore;
/*    */   private final Difficulty difficulty;
/*    */   private final boolean allowCommands;
/*    */   private final GameRules gameRules;
/*    */   private final WorldDataConfiguration dataConfiguration;
/*    */   
/*    */   public LevelSettings(String paramString, GameType paramGameType, boolean paramBoolean1, Difficulty paramDifficulty, boolean paramBoolean2, GameRules paramGameRules, WorldDataConfiguration paramWorldDataConfiguration) {
/* 21 */     this.levelName = paramString;
/* 22 */     this.gameType = paramGameType;
/* 23 */     this.hardcore = paramBoolean1;
/* 24 */     this.difficulty = paramDifficulty;
/* 25 */     this.allowCommands = paramBoolean2;
/* 26 */     this.gameRules = paramGameRules;
/* 27 */     this.dataConfiguration = paramWorldDataConfiguration;
/*    */   }
/*    */   
/*    */   public static LevelSettings parse(Dynamic<?> paramDynamic, WorldDataConfiguration paramWorldDataConfiguration) {
/* 31 */     GameType gameType = GameType.byId(paramDynamic.get("GameType").asInt(0));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 37 */     Objects.requireNonNull(LOGGER); return new LevelSettings(paramDynamic.get("LevelName").asString(""), gameType, paramDynamic.get("hardcore").asBoolean(false), paramDynamic.get("Difficulty").asNumber().map(paramNumber -> Difficulty.byId(paramNumber.byteValue())).result().orElse(Difficulty.NORMAL), paramDynamic.get("allowCommands").asBoolean((gameType == GameType.CREATIVE)), GameRules.codec(paramWorldDataConfiguration.enabledFeatures()).parse(paramDynamic.get("game_rules").orElseEmptyMap()).resultOrPartial(LOGGER::warn).orElseThrow(), paramWorldDataConfiguration);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String levelName() {
/* 43 */     return this.levelName;
/*    */   }
/*    */   
/*    */   public GameType gameType() {
/* 47 */     return this.gameType;
/*    */   }
/*    */   
/*    */   public boolean hardcore() {
/* 51 */     return this.hardcore;
/*    */   }
/*    */   
/*    */   public Difficulty difficulty() {
/* 55 */     return this.difficulty;
/*    */   }
/*    */   
/*    */   public boolean allowCommands() {
/* 59 */     return this.allowCommands;
/*    */   }
/*    */   
/*    */   public GameRules gameRules() {
/* 63 */     return this.gameRules;
/*    */   }
/*    */   
/*    */   public WorldDataConfiguration getDataConfiguration() {
/* 67 */     return this.dataConfiguration;
/*    */   }
/*    */   
/*    */   public LevelSettings withGameType(GameType paramGameType) {
/* 71 */     return new LevelSettings(this.levelName, paramGameType, this.hardcore, this.difficulty, this.allowCommands, this.gameRules, this.dataConfiguration);
/*    */   }
/*    */   
/*    */   public LevelSettings withDifficulty(Difficulty paramDifficulty) {
/* 75 */     return new LevelSettings(this.levelName, this.gameType, this.hardcore, paramDifficulty, this.allowCommands, this.gameRules, this.dataConfiguration);
/*    */   }
/*    */   
/*    */   public LevelSettings withDataConfiguration(WorldDataConfiguration paramWorldDataConfiguration) {
/* 79 */     return new LevelSettings(this.levelName, this.gameType, this.hardcore, this.difficulty, this.allowCommands, this.gameRules, paramWorldDataConfiguration);
/*    */   }
/*    */   
/*    */   public LevelSettings copy() {
/* 83 */     return new LevelSettings(this.levelName, this.gameType, this.hardcore, this.difficulty, this.allowCommands, this.gameRules.copy(this.dataConfiguration.enabledFeatures()), this.dataConfiguration);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\LevelSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */