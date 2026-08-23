/*     */ package net.minecraft.world.level.levelgen;
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.NoiseColumn;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.BiomeResolver;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.CarvingMask;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.ProtoChunk;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ import net.minecraft.world.level.levelgen.carver.CarvingContext;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public final class NoiseBasedChunkGenerator extends ChunkGenerator {
/*     */   static {
/*  53 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BiomeSource.CODEC.fieldOf("biome_source").forGetter(()), (App)NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(())).apply((Applicative)paramInstance, paramInstance.stable(NoiseBasedChunkGenerator::new)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<NoiseBasedChunkGenerator> CODEC;
/*  58 */   private static final BlockState AIR = Blocks.AIR.defaultBlockState();
/*     */   
/*     */   private final Holder<NoiseGeneratorSettings> settings;
/*     */   
/*     */   private final Supplier<Aquifer.FluidPicker> globalFluidPicker;
/*     */   
/*     */   public NoiseBasedChunkGenerator(BiomeSource paramBiomeSource, Holder<NoiseGeneratorSettings> paramHolder) {
/*  65 */     super(paramBiomeSource);
/*     */     
/*  67 */     this.settings = paramHolder;
/*  68 */     this.globalFluidPicker = (Supplier<Aquifer.FluidPicker>)Suppliers.memoize(() -> createFluidPicker((NoiseGeneratorSettings)paramHolder.value()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static Aquifer.FluidPicker createFluidPicker(NoiseGeneratorSettings paramNoiseGeneratorSettings) {
/*  73 */     Aquifer.FluidStatus fluidStatus1 = new Aquifer.FluidStatus(-54, Blocks.LAVA.defaultBlockState());
/*  74 */     int i = paramNoiseGeneratorSettings.seaLevel();
/*  75 */     Aquifer.FluidStatus fluidStatus2 = new Aquifer.FluidStatus(i, paramNoiseGeneratorSettings.defaultFluid());
/*     */     
/*  77 */     Aquifer.FluidStatus fluidStatus3 = new Aquifer.FluidStatus(DimensionType.MIN_Y * 2, Blocks.AIR.defaultBlockState());
/*     */ 
/*     */     
/*  80 */     return (paramInt2, paramInt3, paramInt4) -> SharedConstants.DEBUG_DISABLE_FLUID_GENERATION ? paramFluidStatus1 : ((paramInt3 < Math.min(-54, paramInt1)) ? paramFluidStatus2 : paramFluidStatus3);
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
/*     */   public CompletableFuture<ChunkAccess> createBiomes(RandomState paramRandomState, Blender paramBlender, StructureManager paramStructureManager, ChunkAccess paramChunkAccess) {
/*  93 */     return CompletableFuture.supplyAsync(() -> {
/*     */           doCreateBiomes(paramBlender, paramRandomState, paramStructureManager, paramChunkAccess);
/*     */           return paramChunkAccess;
/*  96 */         }Util.backgroundExecutor().forName("init_biomes"));
/*     */   }
/*     */   
/*     */   private void doCreateBiomes(Blender paramBlender, RandomState paramRandomState, StructureManager paramStructureManager, ChunkAccess paramChunkAccess) {
/* 100 */     NoiseChunk noiseChunk = paramChunkAccess.getOrCreateNoiseChunk(paramChunkAccess -> createNoiseChunk(paramChunkAccess, paramStructureManager, paramBlender, paramRandomState));
/*     */     
/* 102 */     BiomeResolver biomeResolver = BelowZeroRetrogen.getBiomeResolver(paramBlender.getBiomeResolver((BiomeResolver)this.biomeSource), paramChunkAccess);
/*     */     
/* 104 */     paramChunkAccess.fillBiomesFromNoise(biomeResolver, noiseChunk.cachedClimateSampler(paramRandomState.router(), ((NoiseGeneratorSettings)this.settings.value()).spawnTarget()));
/*     */   }
/*     */   
/*     */   private NoiseChunk createNoiseChunk(ChunkAccess paramChunkAccess, StructureManager paramStructureManager, Blender paramBlender, RandomState paramRandomState) {
/* 108 */     return NoiseChunk.forChunk(paramChunkAccess, paramRandomState, Beardifier.forStructuresInChunk(paramStructureManager, paramChunkAccess.getPos()), (NoiseGeneratorSettings)this.settings.value(), this.globalFluidPicker.get(), paramBlender);
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapCodec<? extends ChunkGenerator> codec() {
/* 113 */     return (MapCodec)CODEC;
/*     */   }
/*     */   
/*     */   public Holder<NoiseGeneratorSettings> generatorSettings() {
/* 117 */     return this.settings;
/*     */   }
/*     */   
/*     */   public boolean stable(ResourceKey<NoiseGeneratorSettings> paramResourceKey) {
/* 121 */     return this.settings.is(paramResourceKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBaseHeight(int paramInt1, int paramInt2, Heightmap.Types paramTypes, LevelHeightAccessor paramLevelHeightAccessor, RandomState paramRandomState) {
/* 126 */     return iterateNoiseColumn(paramLevelHeightAccessor, paramRandomState, paramInt1, paramInt2, (MutableObject<NoiseColumn>)null, paramTypes.isOpaque()).orElse(paramLevelHeightAccessor.getMinY());
/*     */   }
/*     */ 
/*     */   
/*     */   public NoiseColumn getBaseColumn(int paramInt1, int paramInt2, LevelHeightAccessor paramLevelHeightAccessor, RandomState paramRandomState) {
/* 131 */     MutableObject<NoiseColumn> mutableObject = new MutableObject();
/* 132 */     iterateNoiseColumn(paramLevelHeightAccessor, paramRandomState, paramInt1, paramInt2, mutableObject, (Predicate<BlockState>)null);
/* 133 */     return (NoiseColumn)mutableObject.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addDebugScreenInfo(List<String> paramList, RandomState paramRandomState, BlockPos paramBlockPos) {
/* 138 */     DecimalFormat decimalFormat = new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.ROOT));
/*     */     
/* 140 */     NoiseRouter noiseRouter = paramRandomState.router();
/* 141 */     DensityFunction.SinglePointContext singlePointContext = new DensityFunction.SinglePointContext(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ());
/*     */     
/* 143 */     double d = noiseRouter.ridges().compute(singlePointContext);
/* 144 */     paramList.add("NoiseRouter T: " + decimalFormat
/* 145 */         .format(noiseRouter.temperature().compute(singlePointContext)) + " V: " + decimalFormat
/* 146 */         .format(noiseRouter.vegetation().compute(singlePointContext)) + " C: " + decimalFormat
/* 147 */         .format(noiseRouter.continents().compute(singlePointContext)) + " E: " + decimalFormat
/* 148 */         .format(noiseRouter.erosion().compute(singlePointContext)) + " D: " + decimalFormat
/* 149 */         .format(noiseRouter.depth().compute(singlePointContext)) + " W: " + decimalFormat
/* 150 */         .format(d) + " PV: " + decimalFormat
/* 151 */         .format(NoiseRouterData.peaksAndValleys((float)d)) + " PS: " + decimalFormat
/* 152 */         .format(noiseRouter.preliminarySurfaceLevel().compute(singlePointContext)) + " N: " + decimalFormat
/* 153 */         .format(noiseRouter.finalDensity().compute(singlePointContext)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private OptionalInt iterateNoiseColumn(LevelHeightAccessor paramLevelHeightAccessor, RandomState paramRandomState, int paramInt1, int paramInt2, MutableObject<NoiseColumn> paramMutableObject, Predicate<BlockState> paramPredicate) {
/*     */     BlockState[] arrayOfBlockState;
/* 164 */     NoiseSettings noiseSettings = ((NoiseGeneratorSettings)this.settings.value()).noiseSettings().clampToHeightAccessor(paramLevelHeightAccessor);
/* 165 */     int i = noiseSettings.getCellHeight();
/*     */     
/* 167 */     int j = noiseSettings.minY();
/* 168 */     int k = Mth.floorDiv(j, i);
/* 169 */     int m = Mth.floorDiv(noiseSettings.height(), i);
/*     */     
/* 171 */     if (m <= 0) {
/* 172 */       return OptionalInt.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 177 */     if (paramMutableObject == null) {
/* 178 */       arrayOfBlockState = null;
/*     */     } else {
/* 180 */       arrayOfBlockState = new BlockState[noiseSettings.height()];
/* 181 */       paramMutableObject.setValue(new NoiseColumn(j, arrayOfBlockState));
/*     */     } 
/*     */     
/* 184 */     int n = noiseSettings.getCellWidth();
/*     */     
/* 186 */     int i1 = Math.floorDiv(paramInt1, n);
/* 187 */     int i2 = Math.floorDiv(paramInt2, n);
/* 188 */     int i3 = Math.floorMod(paramInt1, n);
/* 189 */     int i4 = Math.floorMod(paramInt2, n);
/* 190 */     int i5 = i1 * n;
/* 191 */     int i6 = i2 * n;
/*     */     
/* 193 */     double d1 = i3 / n;
/* 194 */     double d2 = i4 / n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     NoiseChunk noiseChunk = new NoiseChunk(1, paramRandomState, i5, i6, noiseSettings, DensityFunctions.BeardifierMarker.INSTANCE, (NoiseGeneratorSettings)this.settings.value(), this.globalFluidPicker.get(), Blender.empty());
/*     */ 
/*     */     
/* 203 */     noiseChunk.initializeForFirstCellX();
/* 204 */     noiseChunk.advanceCellX(0);
/*     */     
/* 206 */     for (int i7 = m - 1; i7 >= 0; i7--) {
/* 207 */       noiseChunk.selectCellYZ(i7, 0);
/*     */       
/* 209 */       for (int i8 = i - 1; i8 >= 0; i8--) {
/* 210 */         int i9 = (k + i7) * i + i8;
/*     */         
/* 212 */         double d = i8 / i;
/* 213 */         noiseChunk.updateForY(i9, d);
/* 214 */         noiseChunk.updateForX(paramInt1, d1);
/* 215 */         noiseChunk.updateForZ(paramInt2, d2);
/*     */         
/* 217 */         BlockState blockState1 = noiseChunk.getInterpolatedState();
/* 218 */         BlockState blockState2 = (blockState1 == null) ? ((NoiseGeneratorSettings)this.settings.value()).defaultBlock() : blockState1;
/*     */         
/* 220 */         if (arrayOfBlockState != null) {
/* 221 */           int i10 = i7 * i + i8;
/* 222 */           arrayOfBlockState[i10] = blockState2;
/*     */         } 
/* 224 */         if (paramPredicate != null && paramPredicate.test(blockState2)) {
/* 225 */           noiseChunk.stopInterpolation();
/* 226 */           return OptionalInt.of(i9 + 1);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 231 */     noiseChunk.stopInterpolation();
/*     */     
/* 233 */     return OptionalInt.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void buildSurface(WorldGenRegion paramWorldGenRegion, StructureManager paramStructureManager, RandomState paramRandomState, ChunkAccess paramChunkAccess) {
/* 238 */     if (SharedConstants.debugVoidTerrain(paramChunkAccess.getPos()) || SharedConstants.DEBUG_DISABLE_SURFACE) {
/*     */       return;
/*     */     }
/*     */     
/* 242 */     WorldGenerationContext worldGenerationContext = new WorldGenerationContext(this, (LevelHeightAccessor)paramWorldGenRegion);
/*     */     
/* 244 */     buildSurface(paramChunkAccess, worldGenerationContext, paramRandomState, paramStructureManager, paramWorldGenRegion.getBiomeManager(), paramWorldGenRegion.registryAccess().lookupOrThrow(Registries.BIOME), Blender.of(paramWorldGenRegion));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void buildSurface(ChunkAccess paramChunkAccess, WorldGenerationContext paramWorldGenerationContext, RandomState paramRandomState, StructureManager paramStructureManager, BiomeManager paramBiomeManager, Registry<Biome> paramRegistry, Blender paramBlender) {
/* 249 */     NoiseChunk noiseChunk = paramChunkAccess.getOrCreateNoiseChunk(paramChunkAccess -> createNoiseChunk(paramChunkAccess, paramStructureManager, paramBlender, paramRandomState));
/* 250 */     NoiseGeneratorSettings noiseGeneratorSettings = (NoiseGeneratorSettings)this.settings.value();
/* 251 */     paramRandomState.surfaceSystem().buildSurface(paramRandomState, paramBiomeManager, paramRegistry, noiseGeneratorSettings.useLegacyRandomSource(), paramWorldGenerationContext, paramChunkAccess, noiseChunk, noiseGeneratorSettings.surfaceRule());
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyCarvers(WorldGenRegion paramWorldGenRegion, long paramLong, RandomState paramRandomState, BiomeManager paramBiomeManager, StructureManager paramStructureManager, ChunkAccess paramChunkAccess) {
/* 256 */     if (SharedConstants.DEBUG_DISABLE_CARVERS) {
/*     */       return;
/*     */     }
/* 259 */     BiomeManager biomeManager = paramBiomeManager.withDifferentSource((paramInt1, paramInt2, paramInt3) -> this.biomeSource.getNoiseBiome(paramInt1, paramInt2, paramInt3, paramRandomState.sampler()));
/*     */     
/* 261 */     WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
/* 262 */     byte b = 8;
/*     */     
/* 264 */     ChunkPos chunkPos = paramChunkAccess.getPos();
/*     */     
/* 266 */     NoiseChunk noiseChunk = paramChunkAccess.getOrCreateNoiseChunk(paramChunkAccess -> createNoiseChunk(paramChunkAccess, paramStructureManager, Blender.of(paramWorldGenRegion), paramRandomState));
/* 267 */     Aquifer aquifer = noiseChunk.aquifer();
/* 268 */     CarvingContext carvingContext = new CarvingContext(this, paramWorldGenRegion.registryAccess(), paramChunkAccess.getHeightAccessorForGeneration(), noiseChunk, paramRandomState, ((NoiseGeneratorSettings)this.settings.value()).surfaceRule());
/*     */     
/* 270 */     CarvingMask carvingMask = ((ProtoChunk)paramChunkAccess).getOrCreateCarvingMask();
/* 271 */     for (byte b1 = -8; b1 <= 8; b1++) {
/* 272 */       for (byte b2 = -8; b2 <= 8; b2++) {
/* 273 */         ChunkPos chunkPos1 = new ChunkPos(chunkPos.x + b1, chunkPos.z + b2);
/* 274 */         ChunkAccess chunkAccess = paramWorldGenRegion.getChunk(chunkPos1.x, chunkPos1.z);
/* 275 */         BiomeGenerationSettings biomeGenerationSettings = chunkAccess.carverBiome(() -> getBiomeGenerationSettings(this.biomeSource.getNoiseBiome(QuartPos.fromBlock(paramChunkPos.getMinBlockX()), 0, QuartPos.fromBlock(paramChunkPos.getMinBlockZ()), paramRandomState.sampler())));
/* 276 */         Iterable iterable = biomeGenerationSettings.getCarvers();
/*     */         
/* 278 */         byte b3 = 0;
/* 279 */         for (Holder holder : iterable) {
/* 280 */           ConfiguredWorldCarver configuredWorldCarver = (ConfiguredWorldCarver)holder.value();
/* 281 */           worldgenRandom.setLargeFeatureSeed(paramLong + b3, chunkPos1.x, chunkPos1.z);
/* 282 */           if (configuredWorldCarver.isStartChunk(worldgenRandom)) {
/* 283 */             Objects.requireNonNull(biomeManager); configuredWorldCarver.carve(carvingContext, paramChunkAccess, biomeManager::getBiome, worldgenRandom, aquifer, chunkPos1, carvingMask);
/*     */           } 
/* 285 */           b3++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkAccess> fillFromNoise(Blender paramBlender, RandomState paramRandomState, StructureManager paramStructureManager, ChunkAccess paramChunkAccess) {
/* 293 */     NoiseSettings noiseSettings = ((NoiseGeneratorSettings)this.settings.value()).noiseSettings().clampToHeightAccessor(paramChunkAccess.getHeightAccessorForGeneration());
/*     */     
/* 295 */     int i = noiseSettings.minY();
/* 296 */     int j = Mth.floorDiv(i, noiseSettings.getCellHeight());
/* 297 */     int k = Mth.floorDiv(noiseSettings.height(), noiseSettings.getCellHeight());
/*     */     
/* 299 */     if (k <= 0) {
/* 300 */       return CompletableFuture.completedFuture(paramChunkAccess);
/*     */     }
/*     */     
/* 303 */     return CompletableFuture.supplyAsync(() -> {
/*     */           int i = paramChunkAccess.getSectionIndex(paramInt1 * paramNoiseSettings.getCellHeight() - 1 + paramInt2);
/*     */           
/*     */           int j = paramChunkAccess.getSectionIndex(paramInt2);
/*     */           
/*     */           HashSet<LevelChunkSection> hashSet = Sets.newHashSet();
/*     */           for (int k = i; k >= j; k--) {
/*     */             LevelChunkSection levelChunkSection = paramChunkAccess.getSection(k);
/*     */             levelChunkSection.acquire();
/*     */             hashSet.add(levelChunkSection);
/*     */           } 
/*     */           try {
/*     */             return doFill(paramBlender, paramStructureManager, paramRandomState, paramChunkAccess, paramInt3, paramInt1);
/*     */           } finally {
/*     */             for (LevelChunkSection levelChunkSection : hashSet) {
/*     */               levelChunkSection.release();
/*     */             }
/*     */           } 
/* 321 */         }Util.backgroundExecutor().forName("wgen_fill_noise"));
/*     */   }
/*     */ 
/*     */   
/*     */   private ChunkAccess doFill(Blender paramBlender, StructureManager paramStructureManager, RandomState paramRandomState, ChunkAccess paramChunkAccess, int paramInt1, int paramInt2) {
/* 326 */     NoiseChunk noiseChunk = paramChunkAccess.getOrCreateNoiseChunk(paramChunkAccess -> createNoiseChunk(paramChunkAccess, paramStructureManager, paramBlender, paramRandomState));
/*     */     
/* 328 */     Heightmap heightmap1 = paramChunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
/* 329 */     Heightmap heightmap2 = paramChunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
/*     */     
/* 331 */     ChunkPos chunkPos = paramChunkAccess.getPos();
/*     */     
/* 333 */     int i = chunkPos.getMinBlockX();
/* 334 */     int j = chunkPos.getMinBlockZ();
/*     */     
/* 336 */     Aquifer aquifer = noiseChunk.aquifer();
/*     */     
/* 338 */     noiseChunk.initializeForFirstCellX();
/*     */     
/* 340 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*     */     
/* 342 */     int k = noiseChunk.cellWidth();
/* 343 */     int m = noiseChunk.cellHeight();
/*     */     
/* 345 */     int n = 16 / k;
/* 346 */     int i1 = 16 / k;
/*     */ 
/*     */     
/* 349 */     for (byte b = 0; b < n; b++) {
/* 350 */       noiseChunk.advanceCellX(b);
/*     */       
/* 352 */       for (byte b1 = 0; b1 < i1; b1++) {
/* 353 */         int i2 = paramChunkAccess.getSectionsCount() - 1;
/* 354 */         LevelChunkSection levelChunkSection = paramChunkAccess.getSection(i2);
/*     */         
/* 356 */         for (int i3 = paramInt2 - 1; i3 >= 0; i3--) {
/* 357 */           noiseChunk.selectCellYZ(i3, b1);
/*     */           
/* 359 */           for (int i4 = m - 1; i4 >= 0; i4--) {
/* 360 */             int i5 = (paramInt1 + i3) * m + i4;
/* 361 */             int i6 = i5 & 0xF;
/*     */             
/* 363 */             int i7 = paramChunkAccess.getSectionIndex(i5);
/* 364 */             if (i2 != i7) {
/* 365 */               i2 = i7;
/* 366 */               levelChunkSection = paramChunkAccess.getSection(i7);
/*     */             } 
/*     */             
/* 369 */             double d = i4 / m;
/* 370 */             noiseChunk.updateForY(i5, d);
/*     */             
/* 372 */             for (byte b2 = 0; b2 < k; b2++) {
/* 373 */               int i8 = i + b * k + b2;
/* 374 */               int i9 = i8 & 0xF;
/*     */               
/* 376 */               double d1 = b2 / k;
/* 377 */               noiseChunk.updateForX(i8, d1);
/*     */               
/* 379 */               for (byte b3 = 0; b3 < k; b3++) {
/* 380 */                 int i10 = j + b1 * k + b3;
/* 381 */                 int i11 = i10 & 0xF;
/*     */                 
/* 383 */                 double d2 = b3 / k;
/*     */                 
/* 385 */                 noiseChunk.updateForZ(i10, d2);
/*     */                 
/* 387 */                 BlockState blockState = noiseChunk.getInterpolatedState();
/*     */                 
/* 389 */                 if (blockState == null) {
/* 390 */                   blockState = ((NoiseGeneratorSettings)this.settings.value()).defaultBlock();
/*     */                 }
/*     */                 
/* 393 */                 blockState = debugPreliminarySurfaceLevel(noiseChunk, i8, i5, i10, blockState);
/*     */                 
/* 395 */                 if (blockState != AIR && !SharedConstants.debugVoidTerrain(paramChunkAccess.getPos())) {
/*     */ 
/*     */                   
/* 398 */                   levelChunkSection.setBlockState(i9, i6, i11, blockState, false);
/* 399 */                   heightmap1.update(i9, i5, i11, blockState);
/* 400 */                   heightmap2.update(i9, i5, i11, blockState);
/*     */                   
/* 402 */                   if (aquifer.shouldScheduleFluidUpdate() && !blockState.getFluidState().isEmpty()) {
/* 403 */                     mutableBlockPos.set(i8, i5, i10);
/*     */                     
/* 405 */                     paramChunkAccess.markPosForPostprocessing((BlockPos)mutableBlockPos);
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 412 */       }  noiseChunk.swapSlices();
/*     */     } 
/* 414 */     noiseChunk.stopInterpolation();
/* 415 */     return paramChunkAccess;
/*     */   }
/*     */   
/*     */   private BlockState debugPreliminarySurfaceLevel(NoiseChunk paramNoiseChunk, int paramInt1, int paramInt2, int paramInt3, BlockState paramBlockState) {
/* 419 */     if (SharedConstants.DEBUG_AQUIFERS && paramInt3 >= 0 && paramInt3 % 4 == 0) {
/* 420 */       int i = paramNoiseChunk.preliminarySurfaceLevel(paramInt1, paramInt3);
/* 421 */       int j = i + 8;
/* 422 */       if (paramInt2 == j) {
/* 423 */         paramBlockState = (j < getSeaLevel()) ? Blocks.SLIME_BLOCK.defaultBlockState() : Blocks.HONEY_BLOCK.defaultBlockState();
/*     */       }
/*     */     } 
/* 426 */     return paramBlockState;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGenDepth() {
/* 431 */     return ((NoiseGeneratorSettings)this.settings.value()).noiseSettings().height();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSeaLevel() {
/* 436 */     return ((NoiseGeneratorSettings)this.settings.value()).seaLevel();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMinY() {
/* 441 */     return ((NoiseGeneratorSettings)this.settings.value()).noiseSettings().minY();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void spawnOriginalMobs(WorldGenRegion paramWorldGenRegion) {
/* 447 */     if (((NoiseGeneratorSettings)this.settings.value()).disableMobGeneration()) {
/*     */       return;
/*     */     }
/* 450 */     ChunkPos chunkPos = paramWorldGenRegion.getCenter();
/*     */     
/* 452 */     Holder holder = paramWorldGenRegion.getBiome(chunkPos.getWorldPosition().atY(paramWorldGenRegion.getMaxY()));
/*     */     
/* 454 */     WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
/* 455 */     worldgenRandom.setDecorationSeed(paramWorldGenRegion.getSeed(), chunkPos.getMinBlockX(), chunkPos.getMinBlockZ());
/* 456 */     NaturalSpawner.spawnMobsForChunkGeneration((ServerLevelAccessor)paramWorldGenRegion, holder, chunkPos, worldgenRandom);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\NoiseBasedChunkGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */