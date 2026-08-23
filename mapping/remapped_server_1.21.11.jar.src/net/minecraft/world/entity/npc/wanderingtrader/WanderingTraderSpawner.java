/*     */ package net.minecraft.world.entity.npc.wanderingtrader;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.SpawnPlacementType;
/*     */ import net.minecraft.world.entity.SpawnPlacements;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.entity.animal.equine.TraderLlama;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CustomSpawner;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.storage.ServerLevelData;
/*     */ 
/*     */ 
/*     */ public class WanderingTraderSpawner
/*     */   implements CustomSpawner
/*     */ {
/*     */   private static final int DEFAULT_TICK_DELAY = 1200;
/*     */   public static final int DEFAULT_SPAWN_DELAY = 24000;
/*     */   private static final int MIN_SPAWN_CHANCE = 25;
/*     */   private static final int MAX_SPAWN_CHANCE = 75;
/*     */   private static final int SPAWN_CHANCE_INCREASE = 25;
/*     */   private static final int SPAWN_ONE_IN_X_CHANCE = 10;
/*     */   private static final int NUMBER_OF_SPAWN_ATTEMPTS = 10;
/*  37 */   private final RandomSource random = RandomSource.create();
/*     */   private final ServerLevelData serverLevelData;
/*     */   private int tickDelay;
/*     */   private int spawnDelay;
/*     */   private int spawnChance;
/*     */   
/*     */   public WanderingTraderSpawner(ServerLevelData paramServerLevelData) {
/*  44 */     this.serverLevelData = paramServerLevelData;
/*  45 */     this.tickDelay = 1200;
/*  46 */     this.spawnDelay = paramServerLevelData.getWanderingTraderSpawnDelay();
/*  47 */     this.spawnChance = paramServerLevelData.getWanderingTraderSpawnChance();
/*     */     
/*  49 */     if (this.spawnDelay == 0 && this.spawnChance == 0) {
/*  50 */       this.spawnDelay = 24000;
/*  51 */       paramServerLevelData.setWanderingTraderSpawnDelay(this.spawnDelay);
/*  52 */       this.spawnChance = 25;
/*  53 */       paramServerLevelData.setWanderingTraderSpawnChance(this.spawnChance);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(ServerLevel paramServerLevel, boolean paramBoolean) {
/*  59 */     if (!((Boolean)paramServerLevel.getGameRules().get(GameRules.SPAWN_WANDERING_TRADERS)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  63 */     if (--this.tickDelay > 0) {
/*     */       return;
/*     */     }
/*  66 */     this.tickDelay = 1200;
/*     */     
/*  68 */     this.spawnDelay -= 1200;
/*  69 */     this.serverLevelData.setWanderingTraderSpawnDelay(this.spawnDelay);
/*  70 */     if (this.spawnDelay > 0) {
/*     */       return;
/*     */     }
/*  73 */     this.spawnDelay = 24000;
/*     */     
/*  75 */     int i = this.spawnChance;
/*  76 */     this.spawnChance = Mth.clamp(this.spawnChance + 25, 25, 75);
/*  77 */     this.serverLevelData.setWanderingTraderSpawnChance(this.spawnChance);
/*     */     
/*  79 */     if (this.random.nextInt(100) > i) {
/*     */       return;
/*     */     }
/*     */     
/*  83 */     if (spawn(paramServerLevel)) {
/*  84 */       this.spawnChance = 25;
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean spawn(ServerLevel paramServerLevel) {
/*  89 */     ServerPlayer serverPlayer = paramServerLevel.getRandomPlayer();
/*  90 */     if (serverPlayer == null) {
/*  91 */       return true;
/*     */     }
/*     */     
/*  94 */     if (this.random.nextInt(10) != 0) {
/*  95 */       return false;
/*     */     }
/*     */     
/*  98 */     BlockPos blockPos1 = serverPlayer.blockPosition();
/*  99 */     byte b = 48;
/*     */     
/* 101 */     PoiManager poiManager = paramServerLevel.getPoiManager();
/* 102 */     Optional<BlockPos> optional = poiManager.find(paramHolder -> paramHolder.is(PoiTypes.MEETING), paramBlockPos -> true, blockPos1, 48, PoiManager.Occupancy.ANY);
/*     */     
/* 104 */     BlockPos blockPos2 = optional.orElse(blockPos1);
/* 105 */     BlockPos blockPos3 = findSpawnPositionNear((LevelReader)paramServerLevel, blockPos2, 48);
/*     */     
/* 107 */     if (blockPos3 != null && hasEnoughSpace((BlockGetter)paramServerLevel, blockPos3)) {
/* 108 */       if (paramServerLevel.getBiome(blockPos3).is(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS)) {
/* 109 */         return false;
/*     */       }
/*     */       
/* 112 */       WanderingTrader wanderingTrader = (WanderingTrader)EntityType.WANDERING_TRADER.spawn(paramServerLevel, blockPos3, EntitySpawnReason.EVENT);
/*     */       
/* 114 */       if (wanderingTrader != null) {
/* 115 */         for (byte b1 = 0; b1 < 2; b1++) {
/* 116 */           tryToSpawnLlamaFor(paramServerLevel, wanderingTrader, 4);
/*     */         }
/* 118 */         this.serverLevelData.setWanderingTraderId(wanderingTrader.getUUID());
/* 119 */         wanderingTrader.setDespawnDelay(48000);
/*     */         
/* 121 */         wanderingTrader.setWanderTarget(blockPos2);
/* 122 */         wanderingTrader.setHomeTo(blockPos2, 16);
/* 123 */         return true;
/*     */       } 
/*     */     } 
/* 126 */     return false;
/*     */   }
/*     */   
/*     */   private void tryToSpawnLlamaFor(ServerLevel paramServerLevel, WanderingTrader paramWanderingTrader, int paramInt) {
/* 130 */     BlockPos blockPos = findSpawnPositionNear((LevelReader)paramServerLevel, paramWanderingTrader.blockPosition(), paramInt);
/* 131 */     if (blockPos == null) {
/*     */       return;
/*     */     }
/*     */     
/* 135 */     TraderLlama traderLlama = (TraderLlama)EntityType.TRADER_LLAMA.spawn(paramServerLevel, blockPos, EntitySpawnReason.EVENT);
/* 136 */     if (traderLlama == null) {
/*     */       return;
/*     */     }
/*     */     
/* 140 */     traderLlama.setLeashedTo((Entity)paramWanderingTrader, true);
/*     */   }
/*     */   
/*     */   private BlockPos findSpawnPositionNear(LevelReader paramLevelReader, BlockPos paramBlockPos, int paramInt) {
/* 144 */     BlockPos blockPos = null;
/*     */     
/* 146 */     SpawnPlacementType spawnPlacementType = SpawnPlacements.getPlacementType(EntityType.WANDERING_TRADER);
/* 147 */     for (byte b = 0; b < 10; b++) {
/* 148 */       int i = paramBlockPos.getX() + this.random.nextInt(paramInt * 2) - paramInt;
/* 149 */       int j = paramBlockPos.getZ() + this.random.nextInt(paramInt * 2) - paramInt;
/* 150 */       int k = paramLevelReader.getHeight(Heightmap.Types.WORLD_SURFACE, i, j);
/* 151 */       BlockPos blockPos1 = new BlockPos(i, k, j);
/*     */       
/* 153 */       if (spawnPlacementType.isSpawnPositionOk(paramLevelReader, blockPos1, EntityType.WANDERING_TRADER)) {
/* 154 */         blockPos = blockPos1;
/*     */         break;
/*     */       } 
/*     */     } 
/* 158 */     return blockPos;
/*     */   }
/*     */   
/*     */   private boolean hasEnoughSpace(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 162 */     for (BlockPos blockPos : BlockPos.betweenClosed(paramBlockPos, paramBlockPos.offset(1, 2, 1))) {
/* 163 */       if (!paramBlockGetter.getBlockState(blockPos).getCollisionShape(paramBlockGetter, blockPos).isEmpty()) {
/* 164 */         return false;
/*     */       }
/*     */     } 
/* 167 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\npc\wanderingtrader\WanderingTraderSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */