/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.SpawnPlacements;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.NetherFortressStructure;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public final class NaturalSpawner
/*     */ {
/*  59 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MIN_SPAWN_DISTANCE = 24;
/*     */   public static final int SPAWN_DISTANCE_CHUNK = 8;
/*     */   public static final int SPAWN_DISTANCE_BLOCK = 128;
/*  64 */   public static final int INSCRIBED_SQUARE_SPAWN_DISTANCE_CHUNK = Mth.floor(8.0F / Mth.SQRT_OF_TWO);
/*  65 */   static final int MAGIC_NUMBER = (int)Math.pow(17.0D, 2.0D); private static final MobCategory[] SPAWNING_CATEGORIES; static {
/*  66 */     SPAWNING_CATEGORIES = (MobCategory[])Stream.<MobCategory>of(MobCategory.values()).filter(paramMobCategory -> (paramMobCategory != MobCategory.MISC)).toArray(paramInt -> new MobCategory[paramInt]);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ChunkGetter {
/*     */     void query(long param1Long, Consumer<LevelChunk> param1Consumer); }
/*     */   
/*     */   public static class SpawnState {
/*     */     private final int spawnableChunkCount;
/*     */     private final Object2IntOpenHashMap<MobCategory> mobCategoryCounts;
/*     */     private final PotentialCalculator spawnPotential;
/*     */     private final Object2IntMap<MobCategory> unmodifiableMobCategoryCounts;
/*     */     
/*     */     SpawnState(int param1Int, Object2IntOpenHashMap<MobCategory> param1Object2IntOpenHashMap, PotentialCalculator param1PotentialCalculator, LocalMobCapCalculator param1LocalMobCapCalculator) {
/*  80 */       this.spawnableChunkCount = param1Int;
/*  81 */       this.mobCategoryCounts = param1Object2IntOpenHashMap;
/*  82 */       this.spawnPotential = param1PotentialCalculator;
/*  83 */       this.localMobCapCalculator = param1LocalMobCapCalculator;
/*  84 */       this.unmodifiableMobCategoryCounts = Object2IntMaps.unmodifiable((Object2IntMap)param1Object2IntOpenHashMap);
/*     */     }
/*     */     private final LocalMobCapCalculator localMobCapCalculator; private BlockPos lastCheckedPos; private EntityType<?> lastCheckedType; private double lastCharge;
/*     */     private boolean canSpawn(EntityType<?> param1EntityType, BlockPos param1BlockPos, ChunkAccess param1ChunkAccess) {
/*  88 */       this.lastCheckedPos = param1BlockPos;
/*  89 */       this.lastCheckedType = param1EntityType;
/*     */       
/*  91 */       MobSpawnSettings.MobSpawnCost mobSpawnCost = NaturalSpawner.getRoughBiome(param1BlockPos, param1ChunkAccess).getMobSettings().getMobSpawnCost(param1EntityType);
/*  92 */       if (mobSpawnCost == null) {
/*  93 */         this.lastCharge = 0.0D;
/*  94 */         return true;
/*     */       } 
/*  96 */       double d1 = mobSpawnCost.charge();
/*  97 */       this.lastCharge = d1;
/*  98 */       double d2 = this.spawnPotential.getPotentialEnergyChange(param1BlockPos, d1);
/*  99 */       return (d2 <= mobSpawnCost.energyBudget());
/*     */     }
/*     */     private void afterSpawn(Mob param1Mob, ChunkAccess param1ChunkAccess) {
/*     */       double d;
/* 103 */       EntityType<?> entityType = param1Mob.getType();
/*     */       
/* 105 */       BlockPos blockPos = param1Mob.blockPosition();
/* 106 */       if (blockPos.equals(this.lastCheckedPos) && entityType == this.lastCheckedType) {
/* 107 */         d = this.lastCharge;
/*     */       } else {
/*     */         
/* 110 */         MobSpawnSettings.MobSpawnCost mobSpawnCost = NaturalSpawner.getRoughBiome(blockPos, param1ChunkAccess).getMobSettings().getMobSpawnCost(entityType);
/* 111 */         if (mobSpawnCost != null) {
/* 112 */           d = mobSpawnCost.charge();
/*     */         } else {
/* 114 */           d = 0.0D;
/*     */         } 
/*     */       } 
/* 117 */       this.spawnPotential.addCharge(blockPos, d);
/* 118 */       MobCategory mobCategory = entityType.getCategory();
/* 119 */       this.mobCategoryCounts.addTo(mobCategory, 1);
/* 120 */       this.localMobCapCalculator.addMob(new ChunkPos(blockPos), mobCategory);
/*     */     }
/*     */     
/*     */     public int getSpawnableChunkCount() {
/* 124 */       return this.spawnableChunkCount;
/*     */     }
/*     */     
/*     */     public Object2IntMap<MobCategory> getMobCategoryCounts() {
/* 128 */       return this.unmodifiableMobCategoryCounts;
/*     */     }
/*     */     
/*     */     boolean canSpawnForCategoryGlobal(MobCategory param1MobCategory) {
/* 132 */       int i = param1MobCategory.getMaxInstancesPerChunk() * this.spawnableChunkCount / NaturalSpawner.MAGIC_NUMBER;
/* 133 */       return (this.mobCategoryCounts.getInt(param1MobCategory) < i);
/*     */     }
/*     */     
/*     */     boolean canSpawnForCategoryLocal(MobCategory param1MobCategory, ChunkPos param1ChunkPos) {
/* 137 */       return (this.localMobCapCalculator.canSpawn(param1MobCategory, param1ChunkPos) || SharedConstants.DEBUG_IGNORE_LOCAL_MOB_CAP);
/*     */     }
/*     */   }
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
/*     */   public static SpawnState createState(int paramInt, Iterable<Entity> paramIterable, ChunkGetter paramChunkGetter, LocalMobCapCalculator paramLocalMobCapCalculator) {
/* 160 */     PotentialCalculator potentialCalculator = new PotentialCalculator();
/* 161 */     Object2IntOpenHashMap<MobCategory> object2IntOpenHashMap = new Object2IntOpenHashMap();
/*     */     
/* 163 */     for (Iterator<Entity> iterator = paramIterable.iterator(); iterator.hasNext(); ) { Entity entity = iterator.next();
/* 164 */       if (entity instanceof Mob) { Mob mob = (Mob)entity; if (mob.isPersistenceRequired() || mob.requiresCustomPersistence())
/*     */           continue;  }
/*     */       
/* 167 */       MobCategory mobCategory = entity.getType().getCategory();
/* 168 */       if (mobCategory == MobCategory.MISC) {
/*     */         continue;
/*     */       }
/*     */       
/* 172 */       BlockPos blockPos = entity.blockPosition();
/*     */       
/* 174 */       paramChunkGetter.query(ChunkPos.asLong(blockPos), paramLevelChunk -> {
/*     */             MobSpawnSettings.MobSpawnCost mobSpawnCost = getRoughBiome(paramBlockPos, (ChunkAccess)paramLevelChunk).getMobSettings().getMobSpawnCost(paramEntity.getType());
/*     */             
/*     */             if (mobSpawnCost != null) {
/*     */               paramPotentialCalculator.addCharge(paramEntity.blockPosition(), mobSpawnCost.charge());
/*     */             }
/*     */             if (paramEntity instanceof Mob) {
/*     */               paramLocalMobCapCalculator.addMob(paramLevelChunk.getPos(), paramMobCategory);
/*     */             }
/*     */             paramObject2IntOpenHashMap.addTo(paramMobCategory, 1);
/*     */           }); }
/*     */     
/* 186 */     return new SpawnState(paramInt, object2IntOpenHashMap, potentialCalculator, paramLocalMobCapCalculator);
/*     */   }
/*     */ 
/*     */   
/*     */   static Biome getRoughBiome(BlockPos paramBlockPos, ChunkAccess paramChunkAccess) {
/* 191 */     return (Biome)paramChunkAccess.getNoiseBiome(QuartPos.fromBlock(paramBlockPos.getX()), QuartPos.fromBlock(paramBlockPos.getY()), QuartPos.fromBlock(paramBlockPos.getZ())).value();
/*     */   }
/*     */   
/*     */   public static List<MobCategory> getFilteredSpawningCategories(SpawnState paramSpawnState, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/* 195 */     ArrayList<MobCategory> arrayList = new ArrayList(SPAWNING_CATEGORIES.length);
/* 196 */     for (MobCategory mobCategory : SPAWNING_CATEGORIES) {
/* 197 */       if ((paramBoolean1 || !mobCategory.isFriendly()) && (paramBoolean2 || mobCategory
/* 198 */         .isFriendly()) && (paramBoolean3 || 
/* 199 */         !mobCategory.isPersistent()) && paramSpawnState
/* 200 */         .canSpawnForCategoryGlobal(mobCategory))
/*     */       {
/* 202 */         arrayList.add(mobCategory);
/*     */       }
/*     */     } 
/* 205 */     return arrayList;
/*     */   }
/*     */   
/*     */   public static void spawnForChunk(ServerLevel paramServerLevel, LevelChunk paramLevelChunk, SpawnState paramSpawnState, List<MobCategory> paramList) {
/* 209 */     ProfilerFiller profilerFiller = Profiler.get();
/* 210 */     profilerFiller.push("spawner");
/* 211 */     for (MobCategory mobCategory : paramList) {
/* 212 */       if (paramSpawnState.canSpawnForCategoryLocal(mobCategory, paramLevelChunk.getPos())) {
/* 213 */         Objects.requireNonNull(paramSpawnState); Objects.requireNonNull(paramSpawnState); spawnCategoryForChunk(mobCategory, paramServerLevel, paramLevelChunk, paramSpawnState::canSpawn, paramSpawnState::afterSpawn);
/*     */       } 
/*     */     } 
/* 216 */     profilerFiller.pop();
/*     */   }
/*     */   
/*     */   public static void spawnCategoryForChunk(MobCategory paramMobCategory, ServerLevel paramServerLevel, LevelChunk paramLevelChunk, SpawnPredicate paramSpawnPredicate, AfterSpawnCallback paramAfterSpawnCallback) {
/* 220 */     BlockPos blockPos = getRandomPosWithin((Level)paramServerLevel, paramLevelChunk);
/*     */     
/* 222 */     if (blockPos.getY() < paramServerLevel.getMinY() + 1) {
/*     */       return;
/*     */     }
/* 225 */     spawnCategoryForPosition(paramMobCategory, paramServerLevel, (ChunkAccess)paramLevelChunk, blockPos, paramSpawnPredicate, paramAfterSpawnCallback);
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public static void spawnCategoryForPosition(MobCategory paramMobCategory, ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 230 */     spawnCategoryForPosition(paramMobCategory, paramServerLevel, paramServerLevel.getChunk(paramBlockPos), paramBlockPos, (paramEntityType, paramBlockPos, paramChunkAccess) -> true, (paramMob, paramChunkAccess) -> {
/*     */         
/*     */         });
/*     */   }
/* 234 */   public static void spawnCategoryForPosition(MobCategory paramMobCategory, ServerLevel paramServerLevel, ChunkAccess paramChunkAccess, BlockPos paramBlockPos, SpawnPredicate paramSpawnPredicate, AfterSpawnCallback paramAfterSpawnCallback) { StructureManager structureManager = paramServerLevel.structureManager();
/* 235 */     ChunkGenerator chunkGenerator = paramServerLevel.getChunkSource().getGenerator();
/* 236 */     int i = paramBlockPos.getY();
/*     */     
/* 238 */     BlockState blockState = paramChunkAccess.getBlockState(paramBlockPos);
/* 239 */     if (blockState.isRedstoneConductor((BlockGetter)paramChunkAccess, paramBlockPos)) {
/*     */       return;
/*     */     }
/*     */     
/* 243 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 244 */     byte b1 = 0;
/*     */     
/* 246 */     for (byte b2 = 0; b2 < 3; b2++) {
/* 247 */       int j = paramBlockPos.getX();
/* 248 */       int k = paramBlockPos.getZ();
/* 249 */       byte b3 = 6;
/*     */       
/* 251 */       MobSpawnSettings.SpawnerData spawnerData = null;
/* 252 */       SpawnGroupData spawnGroupData = null;
/*     */       
/* 254 */       int m = Mth.ceil(paramServerLevel.random.nextFloat() * 4.0F);
/* 255 */       byte b4 = 0;
/*     */ 
/*     */       
/* 258 */       for (byte b5 = 0; b5 < m; b5++) {
/* 259 */         j += paramServerLevel.random.nextInt(6) - paramServerLevel.random.nextInt(6);
/* 260 */         k += paramServerLevel.random.nextInt(6) - paramServerLevel.random.nextInt(6);
/*     */         
/* 262 */         mutableBlockPos.set(j, i, k);
/*     */         
/* 264 */         double d1 = j + 0.5D;
/* 265 */         double d2 = k + 0.5D;
/*     */         
/* 267 */         Player player = paramServerLevel.getNearestPlayer(d1, i, d2, -1.0D, false);
/* 268 */         if (player != null) {
/*     */ 
/*     */ 
/*     */           
/* 272 */           double d = player.distanceToSqr(d1, i, d2);
/* 273 */           if (isRightDistanceToPlayerAndSpawnPoint(paramServerLevel, paramChunkAccess, mutableBlockPos, d)) {
/*     */ 
/*     */ 
/*     */             
/* 277 */             if (spawnerData == null) {
/* 278 */               Optional<MobSpawnSettings.SpawnerData> optional = getRandomSpawnMobAt(paramServerLevel, structureManager, chunkGenerator, paramMobCategory, paramServerLevel.random, (BlockPos)mutableBlockPos);
/* 279 */               if (optional.isEmpty()) {
/*     */                 break;
/*     */               }
/* 282 */               spawnerData = optional.get();
/*     */ 
/*     */               
/* 285 */               m = spawnerData.minCount() + paramServerLevel.random.nextInt(1 + spawnerData.maxCount() - spawnerData.minCount());
/*     */             } 
/*     */             
/* 288 */             if (isValidSpawnPostitionForType(paramServerLevel, paramMobCategory, structureManager, chunkGenerator, spawnerData, mutableBlockPos, d))
/*     */             {
/*     */ 
/*     */               
/* 292 */               if (paramSpawnPredicate.test(spawnerData.type(), (BlockPos)mutableBlockPos, paramChunkAccess)) {
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 297 */                 Mob mob = getMobForSpawn(paramServerLevel, spawnerData.type());
/* 298 */                 if (mob == null) {
/*     */                   return;
/*     */                 }
/*     */                 
/* 302 */                 mob.snapTo(d1, i, d2, paramServerLevel.random.nextFloat() * 360.0F, 0.0F);
/*     */                 
/* 304 */                 if (isValidPositionForMob(paramServerLevel, mob, d)) {
/*     */ 
/*     */ 
/*     */                   
/* 308 */                   spawnGroupData = mob.finalizeSpawn((ServerLevelAccessor)paramServerLevel, paramServerLevel.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.NATURAL, spawnGroupData);
/*     */                   
/* 310 */                   b1++;
/* 311 */                   b4++;
/* 312 */                   paramServerLevel.addFreshEntityWithPassengers((Entity)mob);
/* 313 */                   paramAfterSpawnCallback.run(mob, paramChunkAccess);
/*     */                   
/* 315 */                   if (b1 >= mob.getMaxSpawnClusterSize()) {
/*     */                     return;
/*     */                   }
/* 318 */                   if (mob.isMaxGroupSizeReached(b4))
/*     */                     break; 
/*     */                 } 
/*     */               }  } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }  } private static boolean isRightDistanceToPlayerAndSpawnPoint(ServerLevel paramServerLevel, ChunkAccess paramChunkAccess, BlockPos.MutableBlockPos paramMutableBlockPos, double paramDouble) {
/* 326 */     if (paramDouble <= 576.0D) {
/* 327 */       return false;
/*     */     }
/* 329 */     LevelData.RespawnData respawnData = paramServerLevel.getRespawnData();
/* 330 */     if (respawnData.dimension() == paramServerLevel.dimension() && respawnData.pos().closerToCenterThan((Position)new Vec3(paramMutableBlockPos.getX() + 0.5D, paramMutableBlockPos.getY(), paramMutableBlockPos.getZ() + 0.5D), 24.0D)) {
/* 331 */       return false;
/*     */     }
/*     */     
/* 334 */     ChunkPos chunkPos = new ChunkPos((BlockPos)paramMutableBlockPos);
/* 335 */     return (Objects.equals(chunkPos, paramChunkAccess.getPos()) || paramServerLevel.canSpawnEntitiesInChunk(chunkPos));
/*     */   }
/*     */   
/*     */   private static boolean isValidSpawnPostitionForType(ServerLevel paramServerLevel, MobCategory paramMobCategory, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, MobSpawnSettings.SpawnerData paramSpawnerData, BlockPos.MutableBlockPos paramMutableBlockPos, double paramDouble) {
/* 339 */     EntityType entityType = paramSpawnerData.type();
/*     */     
/* 341 */     if (entityType.getCategory() == MobCategory.MISC) {
/* 342 */       return false;
/*     */     }
/*     */     
/* 345 */     if (!entityType.canSpawnFarFromPlayer() && paramDouble > (entityType.getCategory().getDespawnDistance() * entityType.getCategory().getDespawnDistance())) {
/* 346 */       return false;
/*     */     }
/*     */     
/* 349 */     if (!entityType.canSummon() || !canSpawnMobAt(paramServerLevel, paramStructureManager, paramChunkGenerator, paramMobCategory, paramSpawnerData, (BlockPos)paramMutableBlockPos)) {
/* 350 */       return false;
/*     */     }
/*     */     
/* 353 */     if (!SpawnPlacements.isSpawnPositionOk(entityType, (LevelReader)paramServerLevel, (BlockPos)paramMutableBlockPos)) {
/* 354 */       return false;
/*     */     }
/* 356 */     if (!SpawnPlacements.checkSpawnRules(entityType, (ServerLevelAccessor)paramServerLevel, EntitySpawnReason.NATURAL, (BlockPos)paramMutableBlockPos, paramServerLevel.random)) {
/* 357 */       return false;
/*     */     }
/* 359 */     if (!paramServerLevel.noCollision(entityType.getSpawnAABB(paramMutableBlockPos.getX() + 0.5D, paramMutableBlockPos.getY(), paramMutableBlockPos.getZ() + 0.5D))) {
/* 360 */       return false;
/*     */     }
/* 362 */     return true;
/*     */   }
/*     */   
/*     */   private static Mob getMobForSpawn(ServerLevel paramServerLevel, EntityType<?> paramEntityType) {
/*     */     try {
/* 367 */       Entity entity = paramEntityType.create((Level)paramServerLevel, EntitySpawnReason.NATURAL); if (entity instanceof Mob) return (Mob)entity;
/*     */ 
/*     */       
/* 370 */       LOGGER.warn("Can't spawn entity of type: {}", BuiltInRegistries.ENTITY_TYPE.getKey(paramEntityType));
/* 371 */     } catch (Exception exception) {
/* 372 */       LOGGER.warn("Failed to create mob", exception);
/*     */     } 
/* 374 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean isValidPositionForMob(ServerLevel paramServerLevel, Mob paramMob, double paramDouble) {
/* 378 */     if (paramDouble > (paramMob.getType().getCategory().getDespawnDistance() * paramMob.getType().getCategory().getDespawnDistance()) && paramMob.removeWhenFarAway(paramDouble)) {
/* 379 */       return false;
/*     */     }
/* 381 */     return (paramMob.checkSpawnRules((LevelAccessor)paramServerLevel, EntitySpawnReason.NATURAL) && paramMob.checkSpawnObstruction((LevelReader)paramServerLevel));
/*     */   }
/*     */   
/*     */   private static Optional<MobSpawnSettings.SpawnerData> getRandomSpawnMobAt(ServerLevel paramServerLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, MobCategory paramMobCategory, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 385 */     Holder<Biome> holder = paramServerLevel.getBiome(paramBlockPos);
/*     */     
/* 387 */     if (paramMobCategory == MobCategory.WATER_AMBIENT && holder.is(BiomeTags.REDUCED_WATER_AMBIENT_SPAWNS) && paramRandomSource.nextFloat() < 0.98F) {
/* 388 */       return Optional.empty();
/*     */     }
/* 390 */     return mobsAt(paramServerLevel, paramStructureManager, paramChunkGenerator, paramMobCategory, paramBlockPos, holder).getRandom(paramRandomSource);
/*     */   }
/*     */   
/*     */   private static boolean canSpawnMobAt(ServerLevel paramServerLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, MobCategory paramMobCategory, MobSpawnSettings.SpawnerData paramSpawnerData, BlockPos paramBlockPos) {
/* 394 */     return mobsAt(paramServerLevel, paramStructureManager, paramChunkGenerator, paramMobCategory, paramBlockPos, null).contains(paramSpawnerData);
/*     */   }
/*     */ 
/*     */   
/*     */   private static WeightedList<MobSpawnSettings.SpawnerData> mobsAt(ServerLevel paramServerLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, MobCategory paramMobCategory, BlockPos paramBlockPos, Holder<Biome> paramHolder) {
/* 399 */     if (isInNetherFortressBounds(paramBlockPos, paramServerLevel, paramMobCategory, paramStructureManager)) {
/* 400 */       return NetherFortressStructure.FORTRESS_ENEMIES;
/*     */     }
/* 402 */     return paramChunkGenerator.getMobsAt((paramHolder != null) ? paramHolder : paramServerLevel.getBiome(paramBlockPos), paramStructureManager, paramMobCategory, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isInNetherFortressBounds(BlockPos paramBlockPos, ServerLevel paramServerLevel, MobCategory paramMobCategory, StructureManager paramStructureManager) {
/* 407 */     if (paramMobCategory != MobCategory.MONSTER || !paramServerLevel.getBlockState(paramBlockPos.below()).is(Blocks.NETHER_BRICKS)) {
/* 408 */       return false;
/*     */     }
/* 410 */     Structure structure = (Structure)paramStructureManager.registryAccess().lookupOrThrow(Registries.STRUCTURE).getValue(BuiltinStructures.FORTRESS);
/* 411 */     if (structure == null) {
/* 412 */       return false;
/*     */     }
/* 414 */     return paramStructureManager.getStructureAt(paramBlockPos, structure).isValid();
/*     */   }
/*     */   
/*     */   private static BlockPos getRandomPosWithin(Level paramLevel, LevelChunk paramLevelChunk) {
/* 418 */     ChunkPos chunkPos = paramLevelChunk.getPos();
/* 419 */     int i = chunkPos.getMinBlockX() + paramLevel.random.nextInt(16);
/* 420 */     int j = chunkPos.getMinBlockZ() + paramLevel.random.nextInt(16);
/*     */     
/* 422 */     int k = paramLevelChunk.getHeight(Heightmap.Types.WORLD_SURFACE, i, j) + 1;
/* 423 */     int m = Mth.randomBetweenInclusive(paramLevel.random, paramLevel.getMinY(), k);
/*     */     
/* 425 */     return new BlockPos(i, m, j);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isValidEmptySpawnBlock(BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState, EntityType<?> paramEntityType) {
/* 430 */     if (paramBlockState.isCollisionShapeFullBlock(paramBlockGetter, paramBlockPos)) {
/* 431 */       return false;
/*     */     }
/*     */     
/* 434 */     if (paramBlockState.isSignalSource()) {
/* 435 */       return false;
/*     */     }
/*     */     
/* 438 */     if (!paramFluidState.isEmpty()) {
/* 439 */       return false;
/*     */     }
/*     */     
/* 442 */     if (paramBlockState.is(BlockTags.PREVENT_MOB_SPAWNING_INSIDE)) {
/* 443 */       return false;
/*     */     }
/*     */     
/* 446 */     if (paramEntityType.isBlockDangerous(paramBlockState)) {
/* 447 */       return false;
/*     */     }
/* 449 */     return true;
/*     */   }
/*     */   
/*     */   public static void spawnMobsForChunkGeneration(ServerLevelAccessor paramServerLevelAccessor, Holder<Biome> paramHolder, ChunkPos paramChunkPos, RandomSource paramRandomSource) {
/* 453 */     MobSpawnSettings mobSpawnSettings = ((Biome)paramHolder.value()).getMobSettings();
/* 454 */     WeightedList weightedList = mobSpawnSettings.getMobs(MobCategory.CREATURE);
/* 455 */     if (weightedList.isEmpty() || !((Boolean)paramServerLevelAccessor.getLevel().getGameRules().get(GameRules.SPAWN_MOBS)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 459 */     int i = paramChunkPos.getMinBlockX();
/* 460 */     int j = paramChunkPos.getMinBlockZ();
/*     */ 
/*     */     
/* 463 */     while (paramRandomSource.nextFloat() < mobSpawnSettings.getCreatureProbability()) {
/* 464 */       Optional<MobSpawnSettings.SpawnerData> optional = weightedList.getRandom(paramRandomSource);
/* 465 */       if (optional.isEmpty()) {
/*     */         continue;
/*     */       }
/* 468 */       MobSpawnSettings.SpawnerData spawnerData = optional.get();
/*     */       
/* 470 */       int k = spawnerData.minCount() + paramRandomSource.nextInt(1 + spawnerData.maxCount() - spawnerData.minCount());
/* 471 */       SpawnGroupData spawnGroupData = null;
/*     */       
/* 473 */       int m = i + paramRandomSource.nextInt(16);
/* 474 */       int n = j + paramRandomSource.nextInt(16);
/* 475 */       int i1 = m;
/* 476 */       int i2 = n;
/*     */       
/* 478 */       for (byte b = 0; b < k; b++) {
/* 479 */         boolean bool = false;
/* 480 */         for (byte b1 = 0; !bool && b1 < 4; b1++) {
/*     */ 
/*     */           
/* 483 */           BlockPos blockPos = getTopNonCollidingPos(paramServerLevelAccessor, spawnerData.type(), m, n);
/* 484 */           if (spawnerData.type().canSummon() && SpawnPlacements.isSpawnPositionOk(spawnerData.type(), paramServerLevelAccessor, blockPos)) {
/* 485 */             Entity entity; float f = spawnerData.type().getWidth();
/* 486 */             double d1 = Mth.clamp(m, i + f, i + 16.0D - f);
/* 487 */             double d2 = Mth.clamp(n, j + f, j + 16.0D - f);
/*     */             
/* 489 */             if (!paramServerLevelAccessor.noCollision(spawnerData.type().getSpawnAABB(d1, blockPos.getY(), d2))) {
/*     */               continue;
/*     */             }
/*     */             
/* 493 */             if (!SpawnPlacements.checkSpawnRules(spawnerData.type(), paramServerLevelAccessor, EntitySpawnReason.CHUNK_GENERATION, BlockPos.containing(d1, blockPos.getY(), d2), paramServerLevelAccessor.getRandom())) {
/*     */               continue;
/*     */             }
/*     */ 
/*     */             
/*     */             try {
/* 499 */               entity = spawnerData.type().create((Level)paramServerLevelAccessor.getLevel(), EntitySpawnReason.NATURAL);
/* 500 */             } catch (Exception exception) {
/* 501 */               LOGGER.warn("Failed to create mob", exception);
/*     */               
/*     */               continue;
/*     */             } 
/* 505 */             if (entity == null) {
/*     */               continue;
/*     */             }
/*     */             
/* 509 */             entity.snapTo(d1, blockPos.getY(), d2, paramRandomSource.nextFloat() * 360.0F, 0.0F);
/*     */             
/* 511 */             if (entity instanceof Mob) { Mob mob = (Mob)entity;
/* 512 */               if (mob.checkSpawnRules(paramServerLevelAccessor, EntitySpawnReason.CHUNK_GENERATION) && mob.checkSpawnObstruction(paramServerLevelAccessor)) {
/* 513 */                 spawnGroupData = mob.finalizeSpawn(paramServerLevelAccessor, paramServerLevelAccessor.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.CHUNK_GENERATION, spawnGroupData);
/* 514 */                 paramServerLevelAccessor.addFreshEntityWithPassengers((Entity)mob);
/* 515 */                 bool = true;
/*     */               }  }
/*     */           
/*     */           } 
/*     */           
/* 520 */           m += paramRandomSource.nextInt(5) - paramRandomSource.nextInt(5);
/* 521 */           n += paramRandomSource.nextInt(5) - paramRandomSource.nextInt(5);
/* 522 */           while (m < i || m >= i + 16 || n < j || n >= j + 16) {
/* 523 */             m = i1 + paramRandomSource.nextInt(5) - paramRandomSource.nextInt(5);
/* 524 */             n = i2 + paramRandomSource.nextInt(5) - paramRandomSource.nextInt(5);
/*     */           } 
/*     */           continue;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private static BlockPos getTopNonCollidingPos(LevelReader paramLevelReader, EntityType<?> paramEntityType, int paramInt1, int paramInt2) {
/* 532 */     int i = paramLevelReader.getHeight(SpawnPlacements.getHeightmapType(paramEntityType), paramInt1, paramInt2);
/* 533 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(paramInt1, i, paramInt2);
/*     */     
/* 535 */     if (paramLevelReader.dimensionType().hasCeiling()) {
/*     */       
/*     */       do {
/* 538 */         mutableBlockPos.move(Direction.DOWN);
/* 539 */       } while (!paramLevelReader.getBlockState((BlockPos)mutableBlockPos).isAir());
/*     */       do {
/* 541 */         mutableBlockPos.move(Direction.DOWN);
/* 542 */       } while (paramLevelReader.getBlockState((BlockPos)mutableBlockPos).isAir() && mutableBlockPos.getY() > paramLevelReader.getMinY());
/*     */     } 
/*     */     
/* 545 */     return SpawnPlacements.getPlacementType(paramEntityType).adjustSpawnPosition(paramLevelReader, mutableBlockPos.immutable());
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface SpawnPredicate {
/*     */     boolean test(EntityType<?> param1EntityType, BlockPos param1BlockPos, ChunkAccess param1ChunkAccess);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface AfterSpawnCallback {
/*     */     void run(Mob param1Mob, ChunkAccess param1ChunkAccess);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\NaturalSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */