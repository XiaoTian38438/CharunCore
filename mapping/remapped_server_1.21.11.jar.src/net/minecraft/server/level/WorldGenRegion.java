/*     */ package net.minecraft.server.level;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StaticCache2D;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeReader;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.EntityBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkSource;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStep;
/*     */ import net.minecraft.world.level.chunk.status.ChunkType;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.ticks.LevelTickAccess;
/*     */ import net.minecraft.world.ticks.TickContainerAccess;
/*     */ import net.minecraft.world.ticks.WorldGenTickAccess;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class WorldGenRegion implements WorldGenLevel {
/*  66 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final StaticCache2D<GenerationChunkHolder> cache;
/*     */   private final ChunkAccess center;
/*     */   private final ServerLevel level;
/*     */   private final long seed;
/*     */   private final LevelData levelData;
/*     */   private final RandomSource random;
/*     */   private final DimensionType dimensionType;
/*     */   private final WorldGenTickAccess<Block> blockTicks;
/*     */   private final WorldGenTickAccess<Fluid> fluidTicks;
/*     */   private final BiomeManager biomeManager;
/*     */   private final ChunkStep generatingStep;
/*     */   private Supplier<String> currentlyGenerating;
/*     */   private final AtomicLong subTickCount;
/*  81 */   private static final Identifier WORLDGEN_REGION_RANDOM = Identifier.withDefaultNamespace("worldgen_region_random"); public WorldGenRegion(ServerLevel paramServerLevel, StaticCache2D<GenerationChunkHolder> paramStaticCache2D, ChunkStep paramChunkStep, ChunkAccess paramChunkAccess) { this.blockTicks = new WorldGenTickAccess(paramBlockPos -> getChunk(paramBlockPos).getBlockTicks());
/*     */     this.fluidTicks = new WorldGenTickAccess(paramBlockPos -> getChunk(paramBlockPos).getFluidTicks());
/*     */     this.subTickCount = new AtomicLong();
/*  84 */     this.generatingStep = paramChunkStep;
/*  85 */     this.cache = paramStaticCache2D;
/*  86 */     this.center = paramChunkAccess;
/*  87 */     this.level = paramServerLevel;
/*  88 */     this.seed = paramServerLevel.getSeed();
/*  89 */     this.levelData = paramServerLevel.getLevelData();
/*  90 */     this.random = paramServerLevel.getChunkSource().randomState().getOrCreateRandomFactory(WORLDGEN_REGION_RANDOM).at(this.center.getPos().getWorldPosition());
/*     */     
/*  92 */     this.dimensionType = paramServerLevel.dimensionType();
/*  93 */     this.biomeManager = new BiomeManager((BiomeManager.NoiseBiomeSource)this, BiomeManager.obfuscateSeed(this.seed)); }
/*     */ 
/*     */   
/*     */   public boolean isOldChunkAround(ChunkPos paramChunkPos, int paramInt) {
/*  97 */     return (this.level.getChunkSource()).chunkMap.isOldChunkAround(paramChunkPos, paramInt);
/*     */   }
/*     */   
/*     */   public ChunkPos getCenter() {
/* 101 */     return this.center.getPos();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCurrentlyGenerating(Supplier<String> paramSupplier) {
/* 106 */     this.currentlyGenerating = paramSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkAccess getChunk(int paramInt1, int paramInt2) {
/* 111 */     return getChunk(paramInt1, paramInt2, ChunkStatus.EMPTY);
/*     */   }
/*     */   
/*     */   public ChunkAccess getChunk(int paramInt1, int paramInt2, ChunkStatus paramChunkStatus, boolean paramBoolean) {
/*     */     Object object;
/* 116 */     int i = this.center.getPos().getChessboardDistance(paramInt1, paramInt2);
/* 117 */     ChunkStatus chunkStatus = (i >= this.generatingStep.directDependencies().size()) ? null : this.generatingStep.directDependencies().get(i);
/*     */     
/* 119 */     if (chunkStatus != null) {
/* 120 */       object = this.cache.get(paramInt1, paramInt2);
/* 121 */       if (paramChunkStatus.isOrBefore(chunkStatus)) {
/* 122 */         ChunkAccess chunkAccess = object.getChunkIfPresentUnchecked(chunkStatus);
/* 123 */         if (chunkAccess != null) {
/* 124 */           return chunkAccess;
/*     */         }
/*     */       } 
/*     */     } else {
/* 128 */       object = null;
/*     */     } 
/* 130 */     CrashReport crashReport = CrashReport.forThrowable(new IllegalStateException("Requested chunk unavailable during world generation"), "Exception generating new chunk");
/* 131 */     CrashReportCategory crashReportCategory = crashReport.addCategory("Chunk request details");
/* 132 */     crashReportCategory.setDetail("Requested chunk", String.format(Locale.ROOT, "%d, %d", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) }));
/* 133 */     crashReportCategory.setDetail("Generating status", () -> this.generatingStep.targetStatus().getName());
/* 134 */     Objects.requireNonNull(paramChunkStatus); crashReportCategory.setDetail("Requested status", paramChunkStatus::getName);
/* 135 */     crashReportCategory.setDetail("Actual status", () -> (paramGenerationChunkHolder == null) ? "[out of cache bounds]" : paramGenerationChunkHolder.getPersistedStatus().getName());
/* 136 */     crashReportCategory.setDetail("Maximum allowed status", () -> (paramChunkStatus == null) ? "null" : paramChunkStatus.getName());
/* 137 */     Objects.requireNonNull(this.generatingStep.directDependencies()); crashReportCategory.setDetail("Dependencies", this.generatingStep.directDependencies()::toString);
/* 138 */     crashReportCategory.setDetail("Requested distance", Integer.valueOf(i));
/* 139 */     Objects.requireNonNull(this.center.getPos()); crashReportCategory.setDetail("Generating chunk", this.center.getPos()::toString);
/* 140 */     throw new ReportedException(crashReport);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasChunk(int paramInt1, int paramInt2) {
/* 145 */     int i = this.center.getPos().getChessboardDistance(paramInt1, paramInt2);
/* 146 */     return (i < this.generatingStep.directDependencies().size());
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getBlockState(BlockPos paramBlockPos) {
/* 151 */     return getChunk(SectionPos.blockToSectionCoord(paramBlockPos.getX()), SectionPos.blockToSectionCoord(paramBlockPos.getZ())).getBlockState(paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockPos paramBlockPos) {
/* 156 */     return getChunk(paramBlockPos).getFluidState(paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public Player getNearestPlayer(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, Predicate<Entity> paramPredicate) {
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSkyDarken() {
/* 166 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public BiomeManager getBiomeManager() {
/* 171 */     return this.biomeManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public Holder<Biome> getUncachedNoiseBiome(int paramInt1, int paramInt2, int paramInt3) {
/* 176 */     return this.level.getUncachedNoiseBiome(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getShade(Direction paramDirection, boolean paramBoolean) {
/* 181 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelLightEngine getLightEngine() {
/* 186 */     return this.level.getLightEngine();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean destroyBlock(BlockPos paramBlockPos, boolean paramBoolean, Entity paramEntity, int paramInt) {
/* 191 */     BlockState blockState = getBlockState(paramBlockPos);
/* 192 */     if (blockState.isAir()) {
/* 193 */       return false;
/*     */     }
/*     */     
/* 196 */     if (paramBoolean) {
/* 197 */       BlockEntity blockEntity = blockState.hasBlockEntity() ? getBlockEntity(paramBlockPos) : null;
/* 198 */       Block.dropResources(blockState, this.level, paramBlockPos, blockEntity, paramEntity, ItemStack.EMPTY);
/*     */     } 
/* 200 */     return setBlock(paramBlockPos, Blocks.AIR.defaultBlockState(), 3, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockEntity getBlockEntity(BlockPos paramBlockPos) {
/* 206 */     ChunkAccess chunkAccess = getChunk(paramBlockPos);
/* 207 */     BlockEntity blockEntity = chunkAccess.getBlockEntity(paramBlockPos);
/*     */     
/* 209 */     if (blockEntity != null) {
/* 210 */       return blockEntity;
/*     */     }
/*     */     
/* 213 */     CompoundTag compoundTag = chunkAccess.getBlockEntityNbt(paramBlockPos);
/* 214 */     BlockState blockState = chunkAccess.getBlockState(paramBlockPos);
/* 215 */     if (compoundTag != null) {
/* 216 */       if ("DUMMY".equals(compoundTag.getStringOr("id", ""))) {
/* 217 */         if (!blockState.hasBlockEntity()) {
/* 218 */           return null;
/*     */         }
/* 220 */         blockEntity = ((EntityBlock)blockState.getBlock()).newBlockEntity(paramBlockPos, blockState);
/*     */       } else {
/* 222 */         blockEntity = BlockEntity.loadStatic(paramBlockPos, blockState, compoundTag, (HolderLookup.Provider)this.level.registryAccess());
/*     */       } 
/*     */       
/* 225 */       if (blockEntity != null) {
/* 226 */         chunkAccess.setBlockEntity(blockEntity);
/* 227 */         return blockEntity;
/*     */       } 
/*     */     } 
/*     */     
/* 231 */     if (blockState.hasBlockEntity()) {
/* 232 */       LOGGER.warn("Tried to access a block entity before it was created. {}", paramBlockPos);
/*     */     }
/*     */     
/* 235 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ensureCanWrite(BlockPos paramBlockPos) {
/* 240 */     int i = SectionPos.blockToSectionCoord(paramBlockPos.getX());
/* 241 */     int j = SectionPos.blockToSectionCoord(paramBlockPos.getZ());
/*     */     
/* 243 */     ChunkPos chunkPos = getCenter();
/* 244 */     int k = Math.abs(chunkPos.x - i);
/* 245 */     int m = Math.abs(chunkPos.z - j);
/*     */     
/* 247 */     if (k > this.generatingStep.blockStateWriteRadius() || m > this.generatingStep.blockStateWriteRadius()) {
/* 248 */       Util.logAndPauseIfInIde("Detected setBlock in a far chunk [" + i + ", " + j + "], pos: " + String.valueOf(paramBlockPos) + ", status: " + String.valueOf(this.generatingStep.targetStatus()) + ((this.currentlyGenerating == null) ? "" : (", currently generating: " + (String)this.currentlyGenerating.get())));
/* 249 */       return false;
/*     */     } 
/*     */     
/* 252 */     if (this.center.isUpgrading()) {
/* 253 */       LevelHeightAccessor levelHeightAccessor = this.center.getHeightAccessorForGeneration();
/* 254 */       if (levelHeightAccessor.isOutsideBuildHeight(paramBlockPos.getY())) {
/* 255 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 259 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setBlock(BlockPos paramBlockPos, BlockState paramBlockState, @UpdateFlags int paramInt1, int paramInt2) {
/* 264 */     if (!ensureCanWrite(paramBlockPos)) {
/* 265 */       return false;
/*     */     }
/*     */     
/* 268 */     ChunkAccess chunkAccess = getChunk(paramBlockPos);
/* 269 */     BlockState blockState = chunkAccess.setBlockState(paramBlockPos, paramBlockState, paramInt1);
/*     */     
/* 271 */     if (blockState != null) {
/* 272 */       this.level.updatePOIOnBlockStateChange(paramBlockPos, blockState, paramBlockState);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 277 */     if (paramBlockState.hasBlockEntity()) {
/* 278 */       if (chunkAccess.getPersistedStatus().getChunkType() == ChunkType.LEVELCHUNK) {
/* 279 */         BlockEntity blockEntity = ((EntityBlock)paramBlockState.getBlock()).newBlockEntity(paramBlockPos, paramBlockState);
/* 280 */         if (blockEntity != null) {
/* 281 */           chunkAccess.setBlockEntity(blockEntity);
/*     */         } else {
/* 283 */           chunkAccess.removeBlockEntity(paramBlockPos);
/*     */         } 
/*     */       } else {
/* 286 */         CompoundTag compoundTag = new CompoundTag();
/* 287 */         compoundTag.putInt("x", paramBlockPos.getX());
/* 288 */         compoundTag.putInt("y", paramBlockPos.getY());
/* 289 */         compoundTag.putInt("z", paramBlockPos.getZ());
/* 290 */         compoundTag.putString("id", "DUMMY");
/* 291 */         chunkAccess.setBlockEntityNbt(compoundTag);
/*     */       } 
/* 293 */     } else if (blockState != null && blockState.hasBlockEntity()) {
/* 294 */       chunkAccess.removeBlockEntity(paramBlockPos);
/*     */     } 
/*     */     
/* 297 */     if (paramBlockState.hasPostProcess((BlockGetter)this, paramBlockPos) && (paramInt1 & 0x10) == 0) {
/* 298 */       markPosForPostprocessing(paramBlockPos);
/*     */     }
/*     */     
/* 301 */     return true;
/*     */   }
/*     */   
/*     */   private void markPosForPostprocessing(BlockPos paramBlockPos) {
/* 305 */     getChunk(paramBlockPos).markPosForPostprocessing(paramBlockPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addFreshEntity(Entity paramEntity) {
/* 313 */     int i = SectionPos.blockToSectionCoord(paramEntity.getBlockX());
/* 314 */     int j = SectionPos.blockToSectionCoord(paramEntity.getBlockZ());
/*     */     
/* 316 */     getChunk(i, j).addEntity(paramEntity);
/* 317 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeBlock(BlockPos paramBlockPos, boolean paramBoolean) {
/* 322 */     return setBlock(paramBlockPos, Blocks.AIR.defaultBlockState(), 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldBorder getWorldBorder() {
/* 327 */     return this.level.getWorldBorder();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClientSide() {
/* 332 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ServerLevel getLevel() {
/* 338 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public RegistryAccess registryAccess() {
/* 343 */     return this.level.registryAccess();
/*     */   }
/*     */ 
/*     */   
/*     */   public FeatureFlagSet enabledFeatures() {
/* 348 */     return this.level.enabledFeatures();
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelData getLevelData() {
/* 353 */     return this.levelData;
/*     */   }
/*     */ 
/*     */   
/*     */   public DifficultyInstance getCurrentDifficultyAt(BlockPos paramBlockPos) {
/* 358 */     if (!hasChunk(SectionPos.blockToSectionCoord(paramBlockPos.getX()), SectionPos.blockToSectionCoord(paramBlockPos.getZ()))) {
/* 359 */       throw new RuntimeException("We are asking a region for a chunk out of bound");
/*     */     }
/*     */     
/* 362 */     return new DifficultyInstance(this.level.getDifficulty(), this.level.getDayTime(), 0L, this.level.getMoonBrightness(paramBlockPos));
/*     */   }
/*     */ 
/*     */   
/*     */   public MinecraftServer getServer() {
/* 367 */     return this.level.getServer();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkSource getChunkSource() {
/* 372 */     return this.level.getChunkSource();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getSeed() {
/* 377 */     return this.seed;
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelTickAccess<Block> getBlockTicks() {
/* 382 */     return (LevelTickAccess<Block>)this.blockTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelTickAccess<Fluid> getFluidTicks() {
/* 387 */     return (LevelTickAccess<Fluid>)this.fluidTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSeaLevel() {
/* 392 */     return this.level.getSeaLevel();
/*     */   }
/*     */ 
/*     */   
/*     */   public RandomSource getRandom() {
/* 397 */     return this.random;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight(Heightmap.Types paramTypes, int paramInt1, int paramInt2) {
/* 402 */     return getChunk(SectionPos.blockToSectionCoord(paramInt1), SectionPos.blockToSectionCoord(paramInt2)).getHeight(paramTypes, paramInt1 & 0xF, paramInt2 & 0xF) + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playSound(Entity paramEntity, BlockPos paramBlockPos, SoundEvent paramSoundEvent, SoundSource paramSoundSource, float paramFloat1, float paramFloat2) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParticle(ParticleOptions paramParticleOptions, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void levelEvent(Entity paramEntity, int paramInt1, BlockPos paramBlockPos, int paramInt2) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void gameEvent(Holder<GameEvent> paramHolder, Vec3 paramVec3, GameEvent.Context paramContext) {}
/*     */ 
/*     */   
/*     */   public DimensionType dimensionType() {
/* 423 */     return this.dimensionType;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStateAtPosition(BlockPos paramBlockPos, Predicate<BlockState> paramPredicate) {
/* 428 */     return paramPredicate.test(getBlockState(paramBlockPos));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFluidAtPosition(BlockPos paramBlockPos, Predicate<FluidState> paramPredicate) {
/* 433 */     return paramPredicate.test(getFluidState(paramBlockPos));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> paramEntityTypeTest, AABB paramAABB, Predicate<? super T> paramPredicate) {
/* 438 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Entity> getEntities(Entity paramEntity, AABB paramAABB, Predicate<? super Entity> paramPredicate) {
/* 443 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Player> players() {
/* 448 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMinY() {
/* 453 */     return this.level.getMinY();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 458 */     return this.level.getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public long nextSubTickCount() {
/* 463 */     return this.subTickCount.getAndIncrement();
/*     */   }
/*     */ 
/*     */   
/*     */   public EnvironmentAttributeReader environmentAttributes() {
/* 468 */     return EnvironmentAttributeReader.EMPTY;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\WorldGenRegion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */