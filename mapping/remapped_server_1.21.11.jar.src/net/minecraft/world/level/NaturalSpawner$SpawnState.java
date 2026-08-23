/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpawnState
/*     */ {
/*     */   private final int spawnableChunkCount;
/*     */   private final Object2IntOpenHashMap<MobCategory> mobCategoryCounts;
/*     */   private final PotentialCalculator spawnPotential;
/*     */   private final Object2IntMap<MobCategory> unmodifiableMobCategoryCounts;
/*     */   private final LocalMobCapCalculator localMobCapCalculator;
/*     */   private BlockPos lastCheckedPos;
/*     */   private EntityType<?> lastCheckedType;
/*     */   private double lastCharge;
/*     */   
/*     */   SpawnState(int paramInt, Object2IntOpenHashMap<MobCategory> paramObject2IntOpenHashMap, PotentialCalculator paramPotentialCalculator, LocalMobCapCalculator paramLocalMobCapCalculator) {
/*  80 */     this.spawnableChunkCount = paramInt;
/*  81 */     this.mobCategoryCounts = paramObject2IntOpenHashMap;
/*  82 */     this.spawnPotential = paramPotentialCalculator;
/*  83 */     this.localMobCapCalculator = paramLocalMobCapCalculator;
/*  84 */     this.unmodifiableMobCategoryCounts = Object2IntMaps.unmodifiable((Object2IntMap)paramObject2IntOpenHashMap);
/*     */   }
/*     */   
/*     */   private boolean canSpawn(EntityType<?> paramEntityType, BlockPos paramBlockPos, ChunkAccess paramChunkAccess) {
/*  88 */     this.lastCheckedPos = paramBlockPos;
/*  89 */     this.lastCheckedType = paramEntityType;
/*     */     
/*  91 */     MobSpawnSettings.MobSpawnCost mobSpawnCost = NaturalSpawner.getRoughBiome(paramBlockPos, paramChunkAccess).getMobSettings().getMobSpawnCost(paramEntityType);
/*  92 */     if (mobSpawnCost == null) {
/*  93 */       this.lastCharge = 0.0D;
/*  94 */       return true;
/*     */     } 
/*  96 */     double d1 = mobSpawnCost.charge();
/*  97 */     this.lastCharge = d1;
/*  98 */     double d2 = this.spawnPotential.getPotentialEnergyChange(paramBlockPos, d1);
/*  99 */     return (d2 <= mobSpawnCost.energyBudget());
/*     */   }
/*     */   private void afterSpawn(Mob paramMob, ChunkAccess paramChunkAccess) {
/*     */     double d;
/* 103 */     EntityType<?> entityType = paramMob.getType();
/*     */     
/* 105 */     BlockPos blockPos = paramMob.blockPosition();
/* 106 */     if (blockPos.equals(this.lastCheckedPos) && entityType == this.lastCheckedType) {
/* 107 */       d = this.lastCharge;
/*     */     } else {
/*     */       
/* 110 */       MobSpawnSettings.MobSpawnCost mobSpawnCost = NaturalSpawner.getRoughBiome(blockPos, paramChunkAccess).getMobSettings().getMobSpawnCost(entityType);
/* 111 */       if (mobSpawnCost != null) {
/* 112 */         d = mobSpawnCost.charge();
/*     */       } else {
/* 114 */         d = 0.0D;
/*     */       } 
/*     */     } 
/* 117 */     this.spawnPotential.addCharge(blockPos, d);
/* 118 */     MobCategory mobCategory = entityType.getCategory();
/* 119 */     this.mobCategoryCounts.addTo(mobCategory, 1);
/* 120 */     this.localMobCapCalculator.addMob(new ChunkPos(blockPos), mobCategory);
/*     */   }
/*     */   
/*     */   public int getSpawnableChunkCount() {
/* 124 */     return this.spawnableChunkCount;
/*     */   }
/*     */   
/*     */   public Object2IntMap<MobCategory> getMobCategoryCounts() {
/* 128 */     return this.unmodifiableMobCategoryCounts;
/*     */   }
/*     */   
/*     */   boolean canSpawnForCategoryGlobal(MobCategory paramMobCategory) {
/* 132 */     int i = paramMobCategory.getMaxInstancesPerChunk() * this.spawnableChunkCount / NaturalSpawner.MAGIC_NUMBER;
/* 133 */     return (this.mobCategoryCounts.getInt(paramMobCategory) < i);
/*     */   }
/*     */   
/*     */   boolean canSpawnForCategoryLocal(MobCategory paramMobCategory, ChunkPos paramChunkPos) {
/* 137 */     return (this.localMobCapCalculator.canSpawn(paramMobCategory, paramChunkPos) || SharedConstants.DEBUG_IGNORE_LOCAL_MOB_CAP);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\NaturalSpawner$SpawnState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */