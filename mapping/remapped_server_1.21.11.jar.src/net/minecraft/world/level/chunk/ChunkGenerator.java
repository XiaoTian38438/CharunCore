/*     */ package net.minecraft.world.level.chunk;
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.ints.IntArraySet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.NoiseColumn;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.BiomeResolver;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.FeatureSorter;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.RandomSupport;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ import net.minecraft.world.level.levelgen.feature.FeatureCountTracker;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ 
/*     */ public abstract class ChunkGenerator {
/*  82 */   public static final Codec<ChunkGenerator> CODEC = BuiltInRegistries.CHUNK_GENERATOR.byNameCodec().dispatchStable(ChunkGenerator::codec, Function.identity());
/*     */ 
/*     */   
/*     */   protected final BiomeSource biomeSource;
/*     */ 
/*     */   
/*     */   private final Supplier<List<FeatureSorter.StepFeatureData>> featuresPerStep;
/*     */ 
/*     */   
/*     */   private final Function<Holder<Biome>, BiomeGenerationSettings> generationSettingsGetter;
/*     */ 
/*     */   
/*     */   public ChunkGenerator(BiomeSource paramBiomeSource) {
/*  95 */     this(paramBiomeSource, paramHolder -> ((Biome)paramHolder.value()).getGenerationSettings());
/*     */   }
/*     */   
/*     */   public ChunkGenerator(BiomeSource paramBiomeSource, Function<Holder<Biome>, BiomeGenerationSettings> paramFunction) {
/*  99 */     this.biomeSource = paramBiomeSource;
/* 100 */     this.generationSettingsGetter = paramFunction;
/*     */     
/* 102 */     this.featuresPerStep = (Supplier<List<FeatureSorter.StepFeatureData>>)Suppliers.memoize(() -> FeatureSorter.buildFeaturesPerStep(List.copyOf(paramBiomeSource.possibleBiomes()), (), true));
/*     */   }
/*     */   
/*     */   public void validate() {
/* 106 */     this.featuresPerStep.get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkGeneratorStructureState createState(HolderLookup<StructureSet> paramHolderLookup, RandomState paramRandomState, long paramLong) {
/* 112 */     return ChunkGeneratorStructureState.createForNormal(paramRandomState, paramLong, this.biomeSource, paramHolderLookup);
/*     */   }
/*     */   
/*     */   public Optional<ResourceKey<MapCodec<? extends ChunkGenerator>>> getTypeNameForDataFixer() {
/* 116 */     return BuiltInRegistries.CHUNK_GENERATOR.getResourceKey(codec());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkAccess> createBiomes(RandomState paramRandomState, Blender paramBlender, StructureManager paramStructureManager, ChunkAccess paramChunkAccess) {
/* 123 */     return CompletableFuture.supplyAsync(() -> {
/*     */           paramChunkAccess.fillBiomesFromNoise((BiomeResolver)this.biomeSource, paramRandomState.sampler());
/*     */           return paramChunkAccess;
/* 126 */         }Util.backgroundExecutor().forName("init_biomes"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Pair<BlockPos, Holder<Structure>> findNearestMapStructure(ServerLevel paramServerLevel, HolderSet<Structure> paramHolderSet, BlockPos paramBlockPos, int paramInt, boolean paramBoolean) {
/* 132 */     if (SharedConstants.DEBUG_DISABLE_FEATURES) {
/* 133 */       return null;
/*     */     }
/*     */     
/* 136 */     ChunkGeneratorStructureState chunkGeneratorStructureState = paramServerLevel.getChunkSource().getGeneratorState();
/* 137 */     Object2ObjectArrayMap<StructurePlacement, Set<Holder<Structure>>> object2ObjectArrayMap = new Object2ObjectArrayMap();
/* 138 */     for (Holder<Structure> holder : paramHolderSet) {
/* 139 */       for (StructurePlacement structurePlacement : chunkGeneratorStructureState.getPlacementsForStructure(holder)) {
/* 140 */         ((Set<Holder<Structure>>)object2ObjectArrayMap.computeIfAbsent(structurePlacement, paramStructurePlacement -> new ObjectArraySet())).add(holder);
/*     */       }
/*     */     } 
/*     */     
/* 144 */     if (object2ObjectArrayMap.isEmpty()) {
/* 145 */       return null;
/*     */     }
/*     */     
/* 148 */     Pair<BlockPos, Holder<Structure>> pair = null;
/* 149 */     double d = Double.MAX_VALUE;
/* 150 */     StructureManager structureManager = paramServerLevel.structureManager();
/* 151 */     ArrayList<Map.Entry> arrayList = new ArrayList(object2ObjectArrayMap.size());
/* 152 */     for (Map.Entry<StructurePlacement, Set<Holder<Structure>>> entry : object2ObjectArrayMap.entrySet()) {
/* 153 */       StructurePlacement structurePlacement = (StructurePlacement)entry.getKey();
/* 154 */       if (structurePlacement instanceof ConcentricRingsStructurePlacement) { ConcentricRingsStructurePlacement concentricRingsStructurePlacement = (ConcentricRingsStructurePlacement)structurePlacement;
/* 155 */         Pair<BlockPos, Holder<Structure>> pair1 = getNearestGeneratedStructure((Set<Holder<Structure>>)entry.getValue(), paramServerLevel, structureManager, paramBlockPos, paramBoolean, concentricRingsStructurePlacement);
/* 156 */         if (pair1 != null) {
/* 157 */           BlockPos blockPos = (BlockPos)pair1.getFirst();
/* 158 */           double d1 = paramBlockPos.distSqr((Vec3i)blockPos);
/* 159 */           if (d1 < d) {
/* 160 */             d = d1;
/* 161 */             pair = pair1;
/*     */           } 
/*     */         }  continue; }
/* 164 */        if (structurePlacement instanceof RandomSpreadStructurePlacement) {
/* 165 */         arrayList.add(entry);
/*     */       }
/*     */     } 
/*     */     
/* 169 */     if (!arrayList.isEmpty()) {
/* 170 */       int i = SectionPos.blockToSectionCoord(paramBlockPos.getX());
/* 171 */       int j = SectionPos.blockToSectionCoord(paramBlockPos.getZ());
/*     */ 
/*     */       
/* 174 */       for (byte b = 0; b <= paramInt; b++) {
/* 175 */         boolean bool = false;
/* 176 */         for (Map.Entry entry : arrayList) {
/* 177 */           RandomSpreadStructurePlacement randomSpreadStructurePlacement = (RandomSpreadStructurePlacement)entry.getKey();
/* 178 */           Pair<BlockPos, Holder<Structure>> pair1 = getNearestGeneratedStructure((Set<Holder<Structure>>)entry.getValue(), (LevelReader)paramServerLevel, structureManager, i, j, b, paramBoolean, chunkGeneratorStructureState.getLevelSeed(), randomSpreadStructurePlacement);
/* 179 */           if (pair1 != null) {
/* 180 */             bool = true;
/* 181 */             double d1 = paramBlockPos.distSqr((Vec3i)pair1.getFirst());
/* 182 */             if (d1 < d) {
/* 183 */               d = d1;
/* 184 */               pair = pair1;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 189 */         if (bool) {
/* 190 */           return pair;
/*     */         }
/*     */       } 
/*     */     } 
/* 194 */     return pair;
/*     */   }
/*     */   
/*     */   private Pair<BlockPos, Holder<Structure>> getNearestGeneratedStructure(Set<Holder<Structure>> paramSet, ServerLevel paramServerLevel, StructureManager paramStructureManager, BlockPos paramBlockPos, boolean paramBoolean, ConcentricRingsStructurePlacement paramConcentricRingsStructurePlacement) {
/* 198 */     List<ChunkPos> list = paramServerLevel.getChunkSource().getGeneratorState().getRingPositionsFor(paramConcentricRingsStructurePlacement);
/* 199 */     if (list == null) {
/* 200 */       throw new IllegalStateException("Somehow tried to find structures for a placement that doesn't exist");
/*     */     }
/* 202 */     Pair<BlockPos, Holder<Structure>> pair = null;
/* 203 */     double d = Double.MAX_VALUE;
/* 204 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 205 */     for (ChunkPos chunkPos : list) {
/* 206 */       mutableBlockPos.set(SectionPos.sectionToBlockCoord(chunkPos.x, 8), 32, SectionPos.sectionToBlockCoord(chunkPos.z, 8));
/* 207 */       double d1 = mutableBlockPos.distSqr((Vec3i)paramBlockPos);
/* 208 */       boolean bool = (pair == null || d1 < d) ? true : false;
/* 209 */       if (bool) {
/* 210 */         Pair<BlockPos, Holder<Structure>> pair1 = getStructureGeneratingAt(paramSet, (LevelReader)paramServerLevel, paramStructureManager, paramBoolean, (StructurePlacement)paramConcentricRingsStructurePlacement, chunkPos);
/* 211 */         if (pair1 != null) {
/* 212 */           pair = pair1;
/* 213 */           d = d1;
/*     */         } 
/*     */       } 
/*     */     } 
/* 217 */     return pair;
/*     */   }
/*     */   
/*     */   private static Pair<BlockPos, Holder<Structure>> getNearestGeneratedStructure(Set<Holder<Structure>> paramSet, LevelReader paramLevelReader, StructureManager paramStructureManager, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong, RandomSpreadStructurePlacement paramRandomSpreadStructurePlacement) {
/* 221 */     int i = paramRandomSpreadStructurePlacement.spacing();
/*     */     
/* 223 */     for (int j = -paramInt3; j <= paramInt3; j++) {
/* 224 */       boolean bool = (j == -paramInt3 || j == paramInt3) ? true : false;
/* 225 */       for (int k = -paramInt3; k <= paramInt3; k++) {
/* 226 */         boolean bool1 = (k == -paramInt3 || k == paramInt3) ? true : false;
/* 227 */         if (bool || bool1) {
/*     */ 
/*     */ 
/*     */           
/* 231 */           int m = paramInt1 + i * j;
/* 232 */           int n = paramInt2 + i * k;
/*     */           
/* 234 */           ChunkPos chunkPos = paramRandomSpreadStructurePlacement.getPotentialStructureChunk(paramLong, m, n);
/*     */           
/* 236 */           Pair<BlockPos, Holder<Structure>> pair = getStructureGeneratingAt(paramSet, paramLevelReader, paramStructureManager, paramBoolean, (StructurePlacement)paramRandomSpreadStructurePlacement, chunkPos);
/* 237 */           if (pair != null) {
/* 238 */             return pair;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 243 */     return null;
/*     */   }
/*     */   
/*     */   private static Pair<BlockPos, Holder<Structure>> getStructureGeneratingAt(Set<Holder<Structure>> paramSet, LevelReader paramLevelReader, StructureManager paramStructureManager, boolean paramBoolean, StructurePlacement paramStructurePlacement, ChunkPos paramChunkPos) {
/* 247 */     for (Holder<Structure> holder : paramSet) {
/* 248 */       StructureCheckResult structureCheckResult = paramStructureManager.checkStructurePresence(paramChunkPos, (Structure)holder.value(), paramStructurePlacement, paramBoolean);
/*     */       
/* 250 */       if (structureCheckResult == StructureCheckResult.START_NOT_PRESENT)
/*     */         continue; 
/* 252 */       if (!paramBoolean && structureCheckResult == StructureCheckResult.START_PRESENT) {
/* 253 */         return Pair.of(paramStructurePlacement.getLocatePos(paramChunkPos), holder);
/*     */       }
/*     */       
/* 256 */       ChunkAccess chunkAccess = paramLevelReader.getChunk(paramChunkPos.x, paramChunkPos.z, ChunkStatus.STRUCTURE_STARTS);
/* 257 */       StructureStart structureStart = paramStructureManager.getStartForStructure(SectionPos.bottomOf(chunkAccess), (Structure)holder.value(), chunkAccess);
/* 258 */       if (structureStart != null && structureStart.isValid() && (
/* 259 */         !paramBoolean || tryAddReference(paramStructureManager, structureStart))) {
/* 260 */         return Pair.of(paramStructurePlacement.getLocatePos(structureStart.getChunkPos()), holder);
/*     */       }
/*     */     } 
/*     */     
/* 264 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean tryAddReference(StructureManager paramStructureManager, StructureStart paramStructureStart) {
/* 268 */     if (paramStructureStart.canBeReferenced()) {
/* 269 */       paramStructureManager.addReference(paramStructureStart);
/* 270 */       return true;
/*     */     } 
/* 272 */     return false;
/*     */   }
/*     */   
/*     */   public void applyBiomeDecoration(WorldGenLevel paramWorldGenLevel, ChunkAccess paramChunkAccess, StructureManager paramStructureManager) {
/* 276 */     ChunkPos chunkPos = paramChunkAccess.getPos();
/*     */     
/* 278 */     if (SharedConstants.debugVoidTerrain(chunkPos)) {
/*     */       return;
/*     */     }
/*     */     
/* 282 */     SectionPos sectionPos = SectionPos.of(chunkPos, paramWorldGenLevel.getMinSectionY());
/* 283 */     BlockPos blockPos = sectionPos.origin();
/*     */     
/* 285 */     Registry registry = paramWorldGenLevel.registryAccess().lookupOrThrow(Registries.STRUCTURE);
/* 286 */     Map map = (Map)registry.stream().collect(Collectors.groupingBy(paramStructure -> Integer.valueOf(paramStructure.step().ordinal())));
/*     */     
/* 288 */     List<FeatureSorter.StepFeatureData> list = this.featuresPerStep.get();
/*     */     
/* 290 */     WorldgenRandom worldgenRandom = new WorldgenRandom((RandomSource)new XoroshiroRandomSource(RandomSupport.generateUniqueSeed()));
/* 291 */     long l = worldgenRandom.setDecorationSeed(paramWorldGenLevel.getSeed(), blockPos.getX(), blockPos.getZ());
/*     */     
/* 293 */     ObjectArraySet objectArraySet = new ObjectArraySet();
/* 294 */     ChunkPos.rangeClosed(sectionPos.chunk(), 1).forEach(paramChunkPos -> {
/*     */           ChunkAccess chunkAccess = paramWorldGenLevel.getChunk(paramChunkPos.x, paramChunkPos.z);
/*     */           for (LevelChunkSection levelChunkSection : chunkAccess.getSections()) {
/*     */             Objects.requireNonNull(paramSet);
/*     */             levelChunkSection.getBiomes().getAll(paramSet::add);
/*     */           } 
/*     */         });
/* 301 */     objectArraySet.retainAll(this.biomeSource.possibleBiomes());
/* 302 */     int i = list.size();
/*     */     
/*     */     try {
/* 305 */       Registry registry1 = paramWorldGenLevel.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE);
/*     */       
/* 307 */       int j = Math.max((GenerationStep.Decoration.values()).length, i);
/* 308 */       for (byte b = 0; b < j; b++) {
/* 309 */         byte b1 = 0;
/* 310 */         if (paramStructureManager.shouldGenerateStructures()) {
/* 311 */           List list1 = (List)map.getOrDefault(Integer.valueOf(b), Collections.emptyList());
/* 312 */           for (Structure structure : list1) {
/* 313 */             worldgenRandom.setFeatureSeed(l, b1, b);
/*     */             
/* 315 */             Supplier supplier = () -> { Objects.requireNonNull(paramStructure); return paramRegistry.getResourceKey(paramStructure).map(Object::toString).orElseGet(paramStructure::toString);
/*     */               }; try {
/* 317 */               paramWorldGenLevel.setCurrentlyGenerating(supplier);
/*     */               
/* 319 */               paramStructureManager.startsForStructure(sectionPos, structure).forEach(paramStructureStart -> paramStructureStart.placeInChunk(paramWorldGenLevel, paramStructureManager, this, (RandomSource)paramWorldgenRandom, getWritableArea(paramChunkAccess), paramChunkPos));
/*     */             
/*     */             }
/* 322 */             catch (Exception exception) {
/* 323 */               CrashReport crashReport = CrashReport.forThrowable(exception, "Feature placement");
/*     */               
/* 325 */               Objects.requireNonNull(supplier); crashReport.addCategory("Feature").setDetail("Description", supplier::get);
/* 326 */               throw new ReportedException(crashReport);
/*     */             } 
/* 328 */             b1++;
/*     */           } 
/*     */         } 
/* 331 */         if (b < i) {
/* 332 */           IntArraySet intArraySet = new IntArraySet();
/* 333 */           for (Holder<Biome> holder : (Iterable<Holder<Biome>>)objectArraySet) {
/*     */             
/* 335 */             List<HolderSet> list1 = ((BiomeGenerationSettings)this.generationSettingsGetter.apply(holder)).features();
/* 336 */             if (b >= list1.size()) {
/*     */               continue;
/*     */             }
/* 339 */             HolderSet holderSet = list1.get(b);
/* 340 */             FeatureSorter.StepFeatureData stepFeatureData1 = list.get(b);
/* 341 */             holderSet.stream().map(Holder::value).forEach(paramPlacedFeature -> paramIntSet.add(paramStepFeatureData.indexMapping().applyAsInt(paramPlacedFeature)));
/*     */           } 
/*     */           
/* 344 */           int k = intArraySet.size();
/* 345 */           int[] arrayOfInt = intArraySet.toIntArray();
/* 346 */           Arrays.sort(arrayOfInt);
/*     */           
/* 348 */           FeatureSorter.StepFeatureData stepFeatureData = list.get(b);
/* 349 */           for (byte b2 = 0; b2 < k; b2++) {
/* 350 */             int m = arrayOfInt[b2];
/* 351 */             PlacedFeature placedFeature = stepFeatureData.features().get(m);
/*     */             
/* 353 */             Supplier supplier = () -> { Objects.requireNonNull(paramPlacedFeature); return paramRegistry.getResourceKey(paramPlacedFeature).map(Object::toString).orElseGet(paramPlacedFeature::toString);
/* 354 */               }; worldgenRandom.setFeatureSeed(l, m, b);
/*     */             try {
/* 356 */               paramWorldGenLevel.setCurrentlyGenerating(supplier);
/* 357 */               placedFeature.placeWithBiomeCheck(paramWorldGenLevel, this, (RandomSource)worldgenRandom, blockPos);
/* 358 */             } catch (Exception exception) {
/* 359 */               CrashReport crashReport = CrashReport.forThrowable(exception, "Feature placement");
/*     */               
/* 361 */               Objects.requireNonNull(supplier); crashReport.addCategory("Feature").setDetail("Description", supplier::get);
/* 362 */               throw new ReportedException(crashReport);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 367 */       paramWorldGenLevel.setCurrentlyGenerating(null);
/* 368 */       if (SharedConstants.DEBUG_FEATURE_COUNT) {
/* 369 */         FeatureCountTracker.chunkDecorated(paramWorldGenLevel.getLevel());
/*     */       }
/* 371 */     } catch (Exception exception) {
/* 372 */       CrashReport crashReport = CrashReport.forThrowable(exception, "Biome decoration");
/* 373 */       crashReport.addCategory("Generation")
/* 374 */         .setDetail("CenterX", Integer.valueOf(chunkPos.x))
/* 375 */         .setDetail("CenterZ", Integer.valueOf(chunkPos.z))
/* 376 */         .setDetail("Decoration Seed", Long.valueOf(l));
/* 377 */       throw new ReportedException(crashReport);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static BoundingBox getWritableArea(ChunkAccess paramChunkAccess) {
/* 382 */     ChunkPos chunkPos = paramChunkAccess.getPos();
/* 383 */     int i = chunkPos.getMinBlockX();
/* 384 */     int j = chunkPos.getMinBlockZ();
/*     */     
/* 386 */     LevelHeightAccessor levelHeightAccessor = paramChunkAccess.getHeightAccessorForGeneration();
/* 387 */     int k = levelHeightAccessor.getMinY() + 1;
/* 388 */     int m = levelHeightAccessor.getMaxY();
/*     */     
/* 390 */     return new BoundingBox(i, k, j, i + 15, m, j + 15);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSpawnHeight(LevelHeightAccessor paramLevelHeightAccessor) {
/* 401 */     return 64;
/*     */   }
/*     */   
/*     */   public BiomeSource getBiomeSource() {
/* 405 */     return this.biomeSource;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public WeightedList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<Biome> paramHolder, StructureManager paramStructureManager, MobCategory paramMobCategory, BlockPos paramBlockPos) {
/* 411 */     Map map = paramStructureManager.getAllStructuresAt(paramBlockPos);
/*     */     
/* 413 */     for (Map.Entry entry : map.entrySet()) {
/* 414 */       Structure structure = (Structure)entry.getKey();
/* 415 */       StructureSpawnOverride structureSpawnOverride = (StructureSpawnOverride)structure.spawnOverrides().get(paramMobCategory);
/* 416 */       if (structureSpawnOverride == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 420 */       MutableBoolean mutableBoolean = new MutableBoolean(false);
/*     */ 
/*     */       
/* 423 */       Predicate predicate = (structureSpawnOverride.boundingBox() == StructureSpawnOverride.BoundingBoxType.PIECE) ? (paramStructureStart -> paramStructureManager.structureHasPieceAt(paramBlockPos, paramStructureStart)) : (paramStructureStart -> paramStructureStart.getBoundingBox().isInside((Vec3i)paramBlockPos));
/*     */       
/* 425 */       paramStructureManager.fillStartsForStructure(structure, (LongSet)entry.getValue(), paramStructureStart -> {
/*     */             if (paramMutableBoolean.isFalse() && paramPredicate.test(paramStructureStart)) {
/*     */               paramMutableBoolean.setTrue();
/*     */             }
/*     */           });
/* 430 */       if (mutableBoolean.isTrue()) {
/* 431 */         return structureSpawnOverride.spawns();
/*     */       }
/*     */     } 
/*     */     
/* 435 */     return ((Biome)paramHolder.value()).getMobSettings().getMobs(paramMobCategory);
/*     */   }
/*     */   
/*     */   public void createStructures(RegistryAccess paramRegistryAccess, ChunkGeneratorStructureState paramChunkGeneratorStructureState, StructureManager paramStructureManager, ChunkAccess paramChunkAccess, StructureTemplateManager paramStructureTemplateManager, ResourceKey<Level> paramResourceKey) {
/* 439 */     if (SharedConstants.DEBUG_DISABLE_STRUCTURES) {
/*     */       return;
/*     */     }
/*     */     
/* 443 */     ChunkPos chunkPos = paramChunkAccess.getPos();
/* 444 */     SectionPos sectionPos = SectionPos.bottomOf(paramChunkAccess);
/*     */     
/* 446 */     RandomState randomState = paramChunkGeneratorStructureState.randomState();
/*     */     
/* 448 */     paramChunkGeneratorStructureState.possibleStructureSets().forEach(paramHolder -> {
/*     */           StructurePlacement structurePlacement = ((StructureSet)paramHolder.value()).placement();
/*     */           List<StructureSet.StructureSelectionEntry> list = ((StructureSet)paramHolder.value()).structures();
/*     */           for (StructureSet.StructureSelectionEntry structureSelectionEntry : list) {
/*     */             StructureStart structureStart = paramStructureManager.getStartForStructure(paramSectionPos, (Structure)structureSelectionEntry.structure().value(), paramChunkAccess);
/*     */             if (structureStart != null && structureStart.isValid()) {
/*     */               return;
/*     */             }
/*     */           } 
/*     */           if (!structurePlacement.isStructureChunk(paramChunkGeneratorStructureState, paramChunkPos.x, paramChunkPos.z)) {
/*     */             return;
/*     */           }
/*     */           if (list.size() == 1) {
/*     */             tryGenerateStructure(list.get(0), paramStructureManager, paramRegistryAccess, paramRandomState, paramStructureTemplateManager, paramChunkGeneratorStructureState.getLevelSeed(), paramChunkAccess, paramChunkPos, paramSectionPos, paramResourceKey);
/*     */             return;
/*     */           } 
/*     */           ArrayList<StructureSet.StructureSelectionEntry> arrayList = new ArrayList(list.size());
/*     */           arrayList.addAll(list);
/*     */           WorldgenRandom worldgenRandom = new WorldgenRandom((RandomSource)new LegacyRandomSource(0L));
/*     */           worldgenRandom.setLargeFeatureSeed(paramChunkGeneratorStructureState.getLevelSeed(), paramChunkPos.x, paramChunkPos.z);
/*     */           int i = 0;
/*     */           for (StructureSet.StructureSelectionEntry structureSelectionEntry : arrayList) {
/*     */             i += structureSelectionEntry.weight();
/*     */           }
/*     */           while (!arrayList.isEmpty()) {
/*     */             int j = worldgenRandom.nextInt(i);
/*     */             byte b = 0;
/*     */             for (StructureSet.StructureSelectionEntry structureSelectionEntry1 : arrayList) {
/*     */               j -= structureSelectionEntry1.weight();
/*     */               if (j < 0) {
/*     */                 break;
/*     */               }
/*     */               b++;
/*     */             } 
/*     */             StructureSet.StructureSelectionEntry structureSelectionEntry = arrayList.get(b);
/*     */             if (tryGenerateStructure(structureSelectionEntry, paramStructureManager, paramRegistryAccess, paramRandomState, paramStructureTemplateManager, paramChunkGeneratorStructureState.getLevelSeed(), paramChunkAccess, paramChunkPos, paramSectionPos, paramResourceKey)) {
/*     */               return;
/*     */             }
/*     */             arrayList.remove(b);
/*     */             i -= structureSelectionEntry.weight();
/*     */           } 
/*     */         });
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
/*     */   private boolean tryGenerateStructure(StructureSet.StructureSelectionEntry paramStructureSelectionEntry, StructureManager paramStructureManager, RegistryAccess paramRegistryAccess, RandomState paramRandomState, StructureTemplateManager paramStructureTemplateManager, long paramLong, ChunkAccess paramChunkAccess, ChunkPos paramChunkPos, SectionPos paramSectionPos, ResourceKey<Level> paramResourceKey) {
/* 507 */     Structure structure = (Structure)paramStructureSelectionEntry.structure().value();
/* 508 */     int i = fetchReferences(paramStructureManager, paramChunkAccess, paramSectionPos, structure);
/*     */ 
/*     */     
/* 511 */     HolderSet holderSet = structure.biomes();
/* 512 */     Objects.requireNonNull(holderSet); Predicate predicate = holderSet::contains;
/* 513 */     StructureStart structureStart = structure.generate(paramStructureSelectionEntry.structure(), paramResourceKey, paramRegistryAccess, this, this.biomeSource, paramRandomState, paramStructureTemplateManager, paramLong, paramChunkPos, i, (LevelHeightAccessor)paramChunkAccess, predicate);
/* 514 */     if (structureStart.isValid()) {
/* 515 */       paramStructureManager.setStartForStructure(paramSectionPos, structure, structureStart, paramChunkAccess);
/* 516 */       return true;
/*     */     } 
/* 518 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int fetchReferences(StructureManager paramStructureManager, ChunkAccess paramChunkAccess, SectionPos paramSectionPos, Structure paramStructure) {
/* 523 */     StructureStart structureStart = paramStructureManager.getStartForStructure(paramSectionPos, paramStructure, paramChunkAccess);
/* 524 */     return (structureStart != null) ? structureStart.getReferences() : 0;
/*     */   }
/*     */   
/*     */   public void createReferences(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkAccess paramChunkAccess) {
/* 528 */     byte b = 8;
/* 529 */     ChunkPos chunkPos = paramChunkAccess.getPos();
/* 530 */     int i = chunkPos.x;
/* 531 */     int j = chunkPos.z;
/* 532 */     int k = chunkPos.getMinBlockX();
/* 533 */     int m = chunkPos.getMinBlockZ();
/*     */     
/* 535 */     SectionPos sectionPos = SectionPos.bottomOf(paramChunkAccess);
/*     */     
/* 537 */     for (int n = i - 8; n <= i + 8; n++) {
/* 538 */       for (int i1 = j - 8; i1 <= j + 8; i1++) {
/* 539 */         long l = ChunkPos.asLong(n, i1);
/*     */         
/* 541 */         for (StructureStart structureStart : paramWorldGenLevel.getChunk(n, i1).getAllStarts().values()) {
/*     */           try {
/* 543 */             if (structureStart.isValid() && structureStart.getBoundingBox().intersects(k, m, k + 15, m + 15)) {
/* 544 */               paramStructureManager.addReferenceForStructure(sectionPos, structureStart.getStructure(), l, paramChunkAccess);
/*     */             }
/* 546 */           } catch (Exception exception) {
/* 547 */             CrashReport crashReport = CrashReport.forThrowable(exception, "Generating structure reference");
/* 548 */             CrashReportCategory crashReportCategory = crashReport.addCategory("Structure");
/* 549 */             Optional optional = paramWorldGenLevel.registryAccess().lookup(Registries.STRUCTURE);
/* 550 */             crashReportCategory.setDetail("Id", () -> (String)paramOptional.map(()).orElse("UNKNOWN"));
/* 551 */             crashReportCategory.setDetail("Name", () -> BuiltInRegistries.STRUCTURE_TYPE.getKey(paramStructureStart.getStructure().type()).toString());
/* 552 */             crashReportCategory.setDetail("Class", () -> paramStructureStart.getStructure().getClass().getCanonicalName());
/* 553 */             throw new ReportedException(crashReport);
/*     */           } 
/*     */         } 
/*     */       } 
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
/*     */   public int getFirstFreeHeight(int paramInt1, int paramInt2, Heightmap.Types paramTypes, LevelHeightAccessor paramLevelHeightAccessor, RandomState paramRandomState) {
/* 574 */     return getBaseHeight(paramInt1, paramInt2, paramTypes, paramLevelHeightAccessor, paramRandomState);
/*     */   }
/*     */   
/*     */   public int getFirstOccupiedHeight(int paramInt1, int paramInt2, Heightmap.Types paramTypes, LevelHeightAccessor paramLevelHeightAccessor, RandomState paramRandomState) {
/* 578 */     return getBaseHeight(paramInt1, paramInt2, paramTypes, paramLevelHeightAccessor, paramRandomState) - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BiomeGenerationSettings getBiomeGenerationSettings(Holder<Biome> paramHolder) {
/* 589 */     return this.generationSettingsGetter.apply(paramHolder);
/*     */   }
/*     */   
/*     */   protected abstract MapCodec<? extends ChunkGenerator> codec();
/*     */   
/*     */   public abstract void applyCarvers(WorldGenRegion paramWorldGenRegion, long paramLong, RandomState paramRandomState, BiomeManager paramBiomeManager, StructureManager paramStructureManager, ChunkAccess paramChunkAccess);
/*     */   
/*     */   public abstract void buildSurface(WorldGenRegion paramWorldGenRegion, StructureManager paramStructureManager, RandomState paramRandomState, ChunkAccess paramChunkAccess);
/*     */   
/*     */   public abstract void spawnOriginalMobs(WorldGenRegion paramWorldGenRegion);
/*     */   
/*     */   public abstract int getGenDepth();
/*     */   
/*     */   public abstract CompletableFuture<ChunkAccess> fillFromNoise(Blender paramBlender, RandomState paramRandomState, StructureManager paramStructureManager, ChunkAccess paramChunkAccess);
/*     */   
/*     */   public abstract int getSeaLevel();
/*     */   
/*     */   public abstract int getMinY();
/*     */   
/*     */   public abstract int getBaseHeight(int paramInt1, int paramInt2, Heightmap.Types paramTypes, LevelHeightAccessor paramLevelHeightAccessor, RandomState paramRandomState);
/*     */   
/*     */   public abstract NoiseColumn getBaseColumn(int paramInt1, int paramInt2, LevelHeightAccessor paramLevelHeightAccessor, RandomState paramRandomState);
/*     */   
/*     */   public abstract void addDebugScreenInfo(List<String> paramList, RandomState paramRandomState, BlockPos paramBlockPos);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\ChunkGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */