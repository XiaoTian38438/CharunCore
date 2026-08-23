/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class ChunkGeneratorStructureState
/*     */ {
/*  36 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final RandomState randomState;
/*     */   
/*     */   private final BiomeSource biomeSource;
/*     */   
/*     */   private final long levelSeed;
/*     */   
/*     */   private final long concentricRingsSeed;
/*     */   
/*  46 */   private final Map<Structure, List<StructurePlacement>> placementsForStructure = (Map<Structure, List<StructurePlacement>>)new Object2ObjectOpenHashMap();
/*     */   
/*  48 */   private final Map<ConcentricRingsStructurePlacement, CompletableFuture<List<ChunkPos>>> ringPositions = (Map<ConcentricRingsStructurePlacement, CompletableFuture<List<ChunkPos>>>)new Object2ObjectArrayMap();
/*     */ 
/*     */   
/*     */   private boolean hasGeneratedPositions;
/*     */   
/*     */   private final List<Holder<StructureSet>> possibleStructureSets;
/*     */ 
/*     */   
/*     */   public static ChunkGeneratorStructureState createForFlat(RandomState paramRandomState, long paramLong, BiomeSource paramBiomeSource, Stream<Holder<StructureSet>> paramStream) {
/*  57 */     List<Holder<StructureSet>> list = paramStream.filter(paramHolder -> hasBiomesForStructureSet((StructureSet)paramHolder.value(), paramBiomeSource)).toList();
/*     */     
/*  59 */     return new ChunkGeneratorStructureState(paramRandomState, paramBiomeSource, paramLong, 0L, list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ChunkGeneratorStructureState createForNormal(RandomState paramRandomState, long paramLong, BiomeSource paramBiomeSource, HolderLookup<StructureSet> paramHolderLookup) {
/*  67 */     List<Holder<StructureSet>> list = (List)paramHolderLookup.listElements().filter(paramReference -> hasBiomesForStructureSet((StructureSet)paramReference.value(), paramBiomeSource)).collect(Collectors.toUnmodifiableList());
/*     */     
/*  69 */     return new ChunkGeneratorStructureState(paramRandomState, paramBiomeSource, paramLong, paramLong, list);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean hasBiomesForStructureSet(StructureSet paramStructureSet, BiomeSource paramBiomeSource) {
/*  74 */     Stream stream = paramStructureSet.structures().stream().flatMap(paramStructureSelectionEntry -> {
/*     */           Structure structure = (Structure)paramStructureSelectionEntry.structure().value();
/*     */           
/*     */           return structure.biomes().stream();
/*     */         });
/*  79 */     Objects.requireNonNull(paramBiomeSource.possibleBiomes()); return stream.anyMatch(paramBiomeSource.possibleBiomes()::contains);
/*     */   }
/*     */   
/*     */   private ChunkGeneratorStructureState(RandomState paramRandomState, BiomeSource paramBiomeSource, long paramLong1, long paramLong2, List<Holder<StructureSet>> paramList) {
/*  83 */     this.randomState = paramRandomState;
/*  84 */     this.levelSeed = paramLong1;
/*  85 */     this.biomeSource = paramBiomeSource;
/*  86 */     this.concentricRingsSeed = paramLong2;
/*  87 */     this.possibleStructureSets = paramList;
/*     */   }
/*     */   
/*     */   public List<Holder<StructureSet>> possibleStructureSets() {
/*  91 */     return this.possibleStructureSets;
/*     */   }
/*     */   
/*     */   private void generatePositions() {
/*  95 */     Set set = this.biomeSource.possibleBiomes();
/*  96 */     possibleStructureSets().forEach(paramHolder -> {
/*     */           StructureSet structureSet = (StructureSet)paramHolder.value();
/*     */           boolean bool = false;
/*     */           for (StructureSet.StructureSelectionEntry structureSelectionEntry : structureSet.structures()) {
/*     */             Structure structure = (Structure)structureSelectionEntry.structure().value();
/*     */             Objects.requireNonNull(paramSet);
/*     */             if (structure.biomes().stream().anyMatch(paramSet::contains)) {
/*     */               ((List<StructurePlacement>)this.placementsForStructure.computeIfAbsent(structure, ())).add(structureSet.placement());
/*     */               bool = true;
/*     */             } 
/*     */           } 
/*     */           if (bool) {
/*     */             StructurePlacement structurePlacement = structureSet.placement();
/*     */             if (structurePlacement instanceof ConcentricRingsStructurePlacement) {
/*     */               ConcentricRingsStructurePlacement concentricRingsStructurePlacement = (ConcentricRingsStructurePlacement)structurePlacement;
/*     */               this.ringPositions.put(concentricRingsStructurePlacement, generateRingPositions(paramHolder, concentricRingsStructurePlacement));
/*     */             } 
/*     */           } 
/*     */         }); } private CompletableFuture<List<ChunkPos>> generateRingPositions(Holder<StructureSet> paramHolder, ConcentricRingsStructurePlacement paramConcentricRingsStructurePlacement) {
/* 115 */     if (paramConcentricRingsStructurePlacement.count() == 0) {
/* 116 */       return CompletableFuture.completedFuture(List.of());
/*     */     }
/*     */     
/* 119 */     Stopwatch stopwatch = Stopwatch.createStarted(Util.TICKER);
/*     */     
/* 121 */     int i = paramConcentricRingsStructurePlacement.distance();
/* 122 */     int j = paramConcentricRingsStructurePlacement.count();
/*     */     
/* 124 */     ArrayList arrayList = new ArrayList(j);
/*     */     
/* 126 */     int k = paramConcentricRingsStructurePlacement.spread();
/* 127 */     HolderSet holderSet = paramConcentricRingsStructurePlacement.preferredBiomes();
/*     */     
/* 129 */     RandomSource randomSource = RandomSource.create();
/*     */ 
/*     */     
/* 132 */     randomSource.setSeed(this.concentricRingsSeed);
/*     */     
/* 134 */     double d = randomSource.nextDouble() * Math.PI * 2.0D;
/*     */     
/* 136 */     byte b1 = 0;
/* 137 */     byte b2 = 0;
/* 138 */     for (byte b3 = 0; b3 < j; b3++) {
/* 139 */       double d1 = (4 * i + i * b2 * 6) + (randomSource.nextDouble() - 0.5D) * i * 2.5D;
/* 140 */       int m = (int)Math.round(Math.cos(d) * d1);
/* 141 */       int n = (int)Math.round(Math.sin(d) * d1);
/*     */       
/* 143 */       RandomSource randomSource1 = randomSource.fork();
/* 144 */       arrayList.add(CompletableFuture.supplyAsync(() -> {
/*     */               Objects.requireNonNull(paramHolderSet); Pair pair = this.biomeSource.findBiomeHorizontal(SectionPos.sectionToBlockCoord(paramInt1, 8), 0, SectionPos.sectionToBlockCoord(paramInt2, 8), 112, paramHolderSet::contains, paramRandomSource, this.randomState.sampler());
/*     */               if (pair != null) {
/*     */                 BlockPos blockPos = (BlockPos)pair.getFirst();
/*     */                 return new ChunkPos(SectionPos.blockToSectionCoord(blockPos.getX()), SectionPos.blockToSectionCoord(blockPos.getZ()));
/*     */               } 
/*     */               return new ChunkPos(paramInt1, paramInt2);
/* 151 */             }Util.backgroundExecutor().forName("structureRings")));
/*     */       
/* 153 */       d += 6.283185307179586D / k;
/*     */       
/* 155 */       if (++b1 == k) {
/* 156 */         b2++;
/* 157 */         b1 = 0;
/* 158 */         k += 2 * k / (b2 + 1);
/* 159 */         k = Math.min(k, j - b3);
/* 160 */         d += randomSource.nextDouble() * Math.PI * 2.0D;
/*     */       } 
/*     */     } 
/*     */     
/* 164 */     return Util.sequence(arrayList).thenApply(paramList -> {
/*     */           double d = paramStopwatch.stop().elapsed(TimeUnit.MILLISECONDS) / 1000.0D;
/*     */           LOGGER.debug("Calculation for {} took {}s", paramHolder, Double.valueOf(d));
/*     */           return paramList;
/*     */         });
/*     */   }
/*     */   
/*     */   public void ensureStructuresGenerated() {
/* 172 */     if (!this.hasGeneratedPositions) {
/* 173 */       generatePositions();
/* 174 */       this.hasGeneratedPositions = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<ChunkPos> getRingPositionsFor(ConcentricRingsStructurePlacement paramConcentricRingsStructurePlacement) {
/* 179 */     ensureStructuresGenerated();
/* 180 */     CompletableFuture<List<ChunkPos>> completableFuture = this.ringPositions.get(paramConcentricRingsStructurePlacement);
/* 181 */     return (completableFuture != null) ? completableFuture.join() : null;
/*     */   }
/*     */   
/*     */   public List<StructurePlacement> getPlacementsForStructure(Holder<Structure> paramHolder) {
/* 185 */     ensureStructuresGenerated();
/* 186 */     return this.placementsForStructure.getOrDefault(paramHolder.value(), List.of());
/*     */   }
/*     */   
/*     */   public RandomState randomState() {
/* 190 */     return this.randomState;
/*     */   }
/*     */   
/*     */   public boolean hasStructureChunkInRange(Holder<StructureSet> paramHolder, int paramInt1, int paramInt2, int paramInt3) {
/* 194 */     StructurePlacement structurePlacement = ((StructureSet)paramHolder.value()).placement();
/* 195 */     for (int i = paramInt1 - paramInt3; i <= paramInt1 + paramInt3; i++) {
/* 196 */       for (int j = paramInt2 - paramInt3; j <= paramInt2 + paramInt3; j++) {
/* 197 */         if (structurePlacement.isStructureChunk(this, i, j)) {
/* 198 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 203 */     return false;
/*     */   }
/*     */   
/*     */   public long getLevelSeed() {
/* 207 */     return this.levelSeed;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\ChunkGeneratorStructureState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */