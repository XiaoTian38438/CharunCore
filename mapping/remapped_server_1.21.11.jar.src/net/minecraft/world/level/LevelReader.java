/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeReader;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface LevelReader
/*     */   extends BlockAndTintGetter, CollisionGetter, SignalGetter, BiomeManager.NoiseBiomeSource
/*     */ {
/*     */   ChunkAccess getChunk(int paramInt1, int paramInt2, ChunkStatus paramChunkStatus, boolean paramBoolean);
/*     */   
/*     */   @Deprecated
/*     */   boolean hasChunk(int paramInt1, int paramInt2);
/*     */   
/*     */   int getHeight(Heightmap.Types paramTypes, int paramInt1, int paramInt2);
/*     */   
/*     */   default int getHeight(Heightmap.Types paramTypes, BlockPos paramBlockPos) {
/*  39 */     return getHeight(paramTypes, paramBlockPos.getX(), paramBlockPos.getZ());
/*     */   }
/*     */   
/*     */   int getSkyDarken();
/*     */   
/*     */   BiomeManager getBiomeManager();
/*     */   
/*     */   default Holder<Biome> getBiome(BlockPos paramBlockPos) {
/*  47 */     return getBiomeManager().getBiome(paramBlockPos);
/*     */   }
/*     */   
/*     */   default Stream<BlockState> getBlockStatesIfLoaded(AABB paramAABB) {
/*  51 */     int i = Mth.floor(paramAABB.minX);
/*  52 */     int j = Mth.floor(paramAABB.maxX);
/*  53 */     int k = Mth.floor(paramAABB.minY);
/*  54 */     int m = Mth.floor(paramAABB.maxY);
/*  55 */     int n = Mth.floor(paramAABB.minZ);
/*  56 */     int i1 = Mth.floor(paramAABB.maxZ);
/*     */     
/*  58 */     if (hasChunksAt(i, k, n, j, m, i1)) {
/*  59 */       return getBlockStates(paramAABB);
/*     */     }
/*  61 */     return Stream.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   default int getBlockTint(BlockPos paramBlockPos, ColorResolver paramColorResolver) {
/*  66 */     return paramColorResolver.getColor((Biome)getBiome(paramBlockPos).value(), paramBlockPos.getX(), paramBlockPos.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   default Holder<Biome> getNoiseBiome(int paramInt1, int paramInt2, int paramInt3) {
/*  71 */     ChunkAccess chunkAccess = getChunk(QuartPos.toSection(paramInt1), QuartPos.toSection(paramInt3), ChunkStatus.BIOMES, false);
/*  72 */     if (chunkAccess != null) {
/*  73 */       return chunkAccess.getNoiseBiome(paramInt1, paramInt2, paramInt3);
/*     */     }
/*  75 */     return getUncachedNoiseBiome(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   
/*     */   Holder<Biome> getUncachedNoiseBiome(int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   boolean isClientSide();
/*     */   
/*     */   int getSeaLevel();
/*     */   
/*     */   DimensionType dimensionType();
/*     */   
/*     */   default int getMinY() {
/*  88 */     return dimensionType().minY();
/*     */   }
/*     */ 
/*     */   
/*     */   default int getHeight() {
/*  93 */     return dimensionType().height();
/*     */   }
/*     */   
/*     */   default BlockPos getHeightmapPos(Heightmap.Types paramTypes, BlockPos paramBlockPos) {
/*  97 */     return new BlockPos(paramBlockPos.getX(), getHeight(paramTypes, paramBlockPos.getX(), paramBlockPos.getZ()), paramBlockPos.getZ());
/*     */   }
/*     */   
/*     */   default boolean isEmptyBlock(BlockPos paramBlockPos) {
/* 101 */     return getBlockState(paramBlockPos).isAir();
/*     */   }
/*     */   
/*     */   default boolean canSeeSkyFromBelowWater(BlockPos paramBlockPos) {
/* 105 */     if (paramBlockPos.getY() >= getSeaLevel()) {
/* 106 */       return canSeeSky(paramBlockPos);
/*     */     }
/* 108 */     BlockPos blockPos = new BlockPos(paramBlockPos.getX(), getSeaLevel(), paramBlockPos.getZ());
/* 109 */     if (!canSeeSky(blockPos)) {
/* 110 */       return false;
/*     */     }
/* 112 */     blockPos = blockPos.below();
/* 113 */     while (blockPos.getY() > paramBlockPos.getY()) {
/* 114 */       BlockState blockState = getBlockState(blockPos);
/* 115 */       if (blockState.getLightBlock() > 0 && !blockState.liquid()) {
/* 116 */         return false;
/*     */       }
/* 118 */       blockPos = blockPos.below();
/*     */     } 
/* 120 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   default float getPathfindingCostFromLightLevels(BlockPos paramBlockPos) {
/* 125 */     return getLightLevelDependentMagicValue(paramBlockPos) - 0.5F;
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
/*     */   @Deprecated
/*     */   default float getLightLevelDependentMagicValue(BlockPos paramBlockPos) {
/* 141 */     float f1 = getMaxLocalRawBrightness(paramBlockPos) / 15.0F;
/*     */     
/* 143 */     float f2 = f1 / (4.0F - 3.0F * f1);
/* 144 */     return Mth.lerp(dimensionType().ambientLight(), f2, 1.0F);
/*     */   }
/*     */   
/*     */   default ChunkAccess getChunk(BlockPos paramBlockPos) {
/* 148 */     return getChunk(SectionPos.blockToSectionCoord(paramBlockPos.getX()), SectionPos.blockToSectionCoord(paramBlockPos.getZ()));
/*     */   }
/*     */   
/*     */   default ChunkAccess getChunk(int paramInt1, int paramInt2) {
/* 152 */     return getChunk(paramInt1, paramInt2, ChunkStatus.FULL, true);
/*     */   }
/*     */   
/*     */   default ChunkAccess getChunk(int paramInt1, int paramInt2, ChunkStatus paramChunkStatus) {
/* 156 */     return getChunk(paramInt1, paramInt2, paramChunkStatus, true);
/*     */   }
/*     */ 
/*     */   
/*     */   default BlockGetter getChunkForCollisions(int paramInt1, int paramInt2) {
/* 161 */     return (BlockGetter)getChunk(paramInt1, paramInt2, ChunkStatus.EMPTY, false);
/*     */   }
/*     */   
/*     */   default boolean isWaterAt(BlockPos paramBlockPos) {
/* 165 */     return getFluidState(paramBlockPos).is(FluidTags.WATER);
/*     */   }
/*     */   
/*     */   default boolean containsAnyLiquid(AABB paramAABB) {
/* 169 */     int i = Mth.floor(paramAABB.minX);
/* 170 */     int j = Mth.ceil(paramAABB.maxX);
/* 171 */     int k = Mth.floor(paramAABB.minY);
/* 172 */     int m = Mth.ceil(paramAABB.maxY);
/* 173 */     int n = Mth.floor(paramAABB.minZ);
/* 174 */     int i1 = Mth.ceil(paramAABB.maxZ);
/*     */     
/* 176 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 177 */     for (int i2 = i; i2 < j; i2++) {
/* 178 */       for (int i3 = k; i3 < m; i3++) {
/* 179 */         for (int i4 = n; i4 < i1; i4++) {
/* 180 */           BlockState blockState = getBlockState((BlockPos)mutableBlockPos.set(i2, i3, i4));
/* 181 */           if (!blockState.getFluidState().isEmpty()) {
/* 182 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 187 */     return false;
/*     */   }
/*     */   
/*     */   default int getMaxLocalRawBrightness(BlockPos paramBlockPos) {
/* 191 */     return getMaxLocalRawBrightness(paramBlockPos, getSkyDarken());
/*     */   }
/*     */   
/*     */   default int getMaxLocalRawBrightness(BlockPos paramBlockPos, int paramInt) {
/* 195 */     if (paramBlockPos.getX() < -30000000 || paramBlockPos.getZ() < -30000000 || paramBlockPos.getX() >= 30000000 || paramBlockPos.getZ() >= 30000000) {
/* 196 */       return 15;
/*     */     }
/*     */     
/* 199 */     return getRawBrightness(paramBlockPos, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunkAt(int paramInt1, int paramInt2) {
/* 207 */     return hasChunk(SectionPos.blockToSectionCoord(paramInt1), SectionPos.blockToSectionCoord(paramInt2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunkAt(BlockPos paramBlockPos) {
/* 215 */     return hasChunkAt(paramBlockPos.getX(), paramBlockPos.getZ());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunksAt(BlockPos paramBlockPos1, BlockPos paramBlockPos2) {
/* 223 */     return hasChunksAt(paramBlockPos1.getX(), paramBlockPos1.getY(), paramBlockPos1.getZ(), paramBlockPos2.getX(), paramBlockPos2.getY(), paramBlockPos2.getZ());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunksAt(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
/* 231 */     if (paramInt5 < getMinY() || paramInt2 > getMaxY()) {
/* 232 */       return false;
/*     */     }
/*     */     
/* 235 */     return hasChunksAt(paramInt1, paramInt3, paramInt4, paramInt6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunksAt(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 243 */     int i = SectionPos.blockToSectionCoord(paramInt1);
/* 244 */     int j = SectionPos.blockToSectionCoord(paramInt3);
/* 245 */     int k = SectionPos.blockToSectionCoord(paramInt2);
/* 246 */     int m = SectionPos.blockToSectionCoord(paramInt4);
/*     */     
/* 248 */     for (int n = i; n <= j; n++) {
/* 249 */       for (int i1 = k; i1 <= m; i1++) {
/* 250 */         if (!hasChunk(n, i1)) {
/* 251 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 256 */     return true;
/*     */   }
/*     */   
/*     */   RegistryAccess registryAccess();
/*     */   
/*     */   FeatureFlagSet enabledFeatures();
/*     */   
/*     */   default <T> HolderLookup<T> holderLookup(ResourceKey<? extends Registry<? extends T>> paramResourceKey) {
/* 264 */     Registry registry = registryAccess().lookupOrThrow(paramResourceKey);
/* 265 */     return (HolderLookup<T>)registry.filterFeatures(enabledFeatures());
/*     */   }
/*     */   
/*     */   EnvironmentAttributeReader environmentAttributes();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\LevelReader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */