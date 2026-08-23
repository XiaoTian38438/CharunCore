/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ 
/*     */ public abstract class BiomeSource
/*     */   implements BiomeResolver {
/*  29 */   public static final Codec<BiomeSource> CODEC = BuiltInRegistries.BIOME_SOURCE.byNameCodec().dispatchStable(BiomeSource::codec, Function.identity());
/*     */ 
/*     */   
/*  32 */   private final Supplier<Set<Holder<Biome>>> possibleBiomes = (Supplier<Set<Holder<Biome>>>)Suppliers.memoize(() -> (Set)collectPossibleBiomes().distinct().collect(ImmutableSet.toImmutableSet()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Holder<Biome>> possibleBiomes() {
/*  42 */     return this.possibleBiomes.get();
/*     */   }
/*     */   
/*     */   public Set<Holder<Biome>> getBiomesWithin(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Climate.Sampler paramSampler) {
/*  46 */     int i = QuartPos.fromBlock(paramInt1 - paramInt4);
/*  47 */     int j = QuartPos.fromBlock(paramInt2 - paramInt4);
/*  48 */     int k = QuartPos.fromBlock(paramInt3 - paramInt4);
/*  49 */     int m = QuartPos.fromBlock(paramInt1 + paramInt4);
/*  50 */     int n = QuartPos.fromBlock(paramInt2 + paramInt4);
/*  51 */     int i1 = QuartPos.fromBlock(paramInt3 + paramInt4);
/*     */     
/*  53 */     int i2 = m - i + 1;
/*  54 */     int i3 = n - j + 1;
/*  55 */     int i4 = i1 - k + 1;
/*     */     
/*  57 */     HashSet<Holder<Biome>> hashSet = Sets.newHashSet();
/*     */     
/*  59 */     for (byte b = 0; b < i4; b++) {
/*  60 */       for (byte b1 = 0; b1 < i2; b1++) {
/*  61 */         for (byte b2 = 0; b2 < i3; b2++) {
/*  62 */           int i5 = i + b1;
/*  63 */           int i6 = j + b2;
/*  64 */           int i7 = k + b;
/*  65 */           hashSet.add(getNoiseBiome(i5, i6, i7, paramSampler));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  70 */     return hashSet;
/*     */   }
/*     */   
/*     */   public Pair<BlockPos, Holder<Biome>> findBiomeHorizontal(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Predicate<Holder<Biome>> paramPredicate, RandomSource paramRandomSource, Climate.Sampler paramSampler) {
/*  74 */     return findBiomeHorizontal(paramInt1, paramInt2, paramInt3, paramInt4, 1, paramPredicate, paramRandomSource, false, paramSampler);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Pair<BlockPos, Holder<Biome>> findClosestBiome3d(BlockPos paramBlockPos, int paramInt1, int paramInt2, int paramInt3, Predicate<Holder<Biome>> paramPredicate, Climate.Sampler paramSampler, LevelReader paramLevelReader) {
/*  80 */     Set set = (Set)possibleBiomes().stream().filter(paramPredicate).collect(Collectors.toUnmodifiableSet());
/*     */     
/*  82 */     if (set.isEmpty()) {
/*  83 */       return null;
/*     */     }
/*     */     
/*  86 */     int i = Math.floorDiv(paramInt1, paramInt2);
/*  87 */     int[] arrayOfInt = Mth.outFromOrigin(paramBlockPos.getY(), paramLevelReader.getMinY() + 1, paramLevelReader.getMaxY() + 1, paramInt3).toArray();
/*     */     
/*  89 */     for (BlockPos.MutableBlockPos mutableBlockPos : BlockPos.spiralAround(BlockPos.ZERO, i, Direction.EAST, Direction.SOUTH)) {
/*  90 */       int j = paramBlockPos.getX() + mutableBlockPos.getX() * paramInt2;
/*  91 */       int k = paramBlockPos.getZ() + mutableBlockPos.getZ() * paramInt2;
/*  92 */       int m = QuartPos.fromBlock(j);
/*  93 */       int n = QuartPos.fromBlock(k);
/*  94 */       for (int i1 : arrayOfInt) {
/*  95 */         int i2 = QuartPos.fromBlock(i1);
/*  96 */         Holder<Biome> holder = getNoiseBiome(m, i2, n, paramSampler);
/*  97 */         if (set.contains(holder)) {
/*  98 */           return Pair.of(new BlockPos(j, i1, k), holder);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pair<BlockPos, Holder<Biome>> findBiomeHorizontal(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Predicate<Holder<Biome>> paramPredicate, RandomSource paramRandomSource, boolean paramBoolean, Climate.Sampler paramSampler) {
/* 114 */     int i = QuartPos.fromBlock(paramInt1);
/* 115 */     int j = QuartPos.fromBlock(paramInt3);
/* 116 */     int k = QuartPos.fromBlock(paramInt4);
/*     */     
/* 118 */     int m = QuartPos.fromBlock(paramInt2);
/*     */     
/* 120 */     Pair<BlockPos, Holder<Biome>> pair = null;
/* 121 */     byte b1 = 0;
/*     */     
/* 123 */     byte b2 = paramBoolean ? 0 : k; int n;
/* 124 */     for (n = b2; n <= k; n += paramInt5) {
/* 125 */       int i1; for (i1 = (SharedConstants.DEBUG_ONLY_GENERATE_HALF_THE_WORLD || SharedConstants.debugGenerateSquareTerrainWithoutNoise) ? 0 : -n; i1 <= n; i1 += paramInt5) {
/* 126 */         boolean bool = (Math.abs(i1) == n) ? true : false; int i2;
/* 127 */         for (i2 = -n; i2 <= n; i2 += paramInt5) {
/* 128 */           if (paramBoolean) {
/*     */             
/* 130 */             boolean bool1 = (Math.abs(i2) == n) ? true : false;
/* 131 */             if (!bool1 && !bool) {
/*     */               continue;
/*     */             }
/*     */           } 
/*     */           
/* 136 */           int i3 = i + i2;
/* 137 */           int i4 = j + i1;
/* 138 */           Holder<Biome> holder = getNoiseBiome(i3, m, i4, paramSampler);
/* 139 */           if (paramPredicate.test(holder)) {
/* 140 */             if (pair == null || paramRandomSource.nextInt(b1 + 1) == 0) {
/* 141 */               BlockPos blockPos = new BlockPos(QuartPos.toBlock(i3), paramInt2, QuartPos.toBlock(i4));
/* 142 */               if (paramBoolean) {
/* 143 */                 return Pair.of(blockPos, holder);
/*     */               }
/* 145 */               pair = Pair.of(blockPos, holder);
/*     */             } 
/* 147 */             b1++;
/*     */           } 
/*     */           continue;
/*     */         } 
/*     */       } 
/*     */     } 
/* 153 */     return pair;
/*     */   }
/*     */   
/*     */   public void addDebugInfo(List<String> paramList, BlockPos paramBlockPos, Climate.Sampler paramSampler) {}
/*     */   
/*     */   protected abstract MapCodec<? extends BiomeSource> codec();
/*     */   
/*     */   protected abstract Stream<Holder<Biome>> collectPossibleBiomes();
/*     */   
/*     */   public abstract Holder<Biome> getNoiseBiome(int paramInt1, int paramInt2, int paramInt3, Climate.Sampler paramSampler);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\BiomeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */