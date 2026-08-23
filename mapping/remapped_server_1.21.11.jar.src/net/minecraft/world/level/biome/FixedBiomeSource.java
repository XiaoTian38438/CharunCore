/*    */ package net.minecraft.world.level.biome;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ 
/*    */ public class FixedBiomeSource extends BiomeSource implements BiomeManager.NoiseBiomeSource {
/*    */   public static final MapCodec<FixedBiomeSource> CODEC;
/*    */   
/*    */   static {
/* 18 */     CODEC = Biome.CODEC.fieldOf("biome").xmap(FixedBiomeSource::new, paramFixedBiomeSource -> paramFixedBiomeSource.biome).stable();
/*    */   }
/*    */   private final Holder<Biome> biome;
/*    */   
/*    */   public FixedBiomeSource(Holder<Biome> paramHolder) {
/* 23 */     this.biome = paramHolder;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Stream<Holder<Biome>> collectPossibleBiomes() {
/* 28 */     return Stream.of(this.biome);
/*    */   }
/*    */ 
/*    */   
/*    */   protected MapCodec<? extends BiomeSource> codec() {
/* 33 */     return (MapCodec)CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public Holder<Biome> getNoiseBiome(int paramInt1, int paramInt2, int paramInt3, Climate.Sampler paramSampler) {
/* 38 */     return this.biome;
/*    */   }
/*    */ 
/*    */   
/*    */   public Holder<Biome> getNoiseBiome(int paramInt1, int paramInt2, int paramInt3) {
/* 43 */     return this.biome;
/*    */   }
/*    */ 
/*    */   
/*    */   public Pair<BlockPos, Holder<Biome>> findBiomeHorizontal(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Predicate<Holder<Biome>> paramPredicate, RandomSource paramRandomSource, boolean paramBoolean, Climate.Sampler paramSampler) {
/* 48 */     if (paramPredicate.test(this.biome)) {
/* 49 */       if (paramBoolean) {
/* 50 */         return Pair.of(new BlockPos(paramInt1, paramInt2, paramInt3), this.biome);
/*    */       }
/* 52 */       return Pair.of(new BlockPos(paramInt1 - paramInt4 + paramRandomSource.nextInt(paramInt4 * 2 + 1), paramInt2, paramInt3 - paramInt4 + paramRandomSource.nextInt(paramInt4 * 2 + 1)), this.biome);
/*    */     } 
/*    */     
/* 55 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Pair<BlockPos, Holder<Biome>> findClosestBiome3d(BlockPos paramBlockPos, int paramInt1, int paramInt2, int paramInt3, Predicate<Holder<Biome>> paramPredicate, Climate.Sampler paramSampler, LevelReader paramLevelReader) {
/* 60 */     return paramPredicate.test(this.biome) ? Pair.of(paramBlockPos.atY(Mth.clamp(paramBlockPos.getY(), paramLevelReader.getMinY() + 1, paramLevelReader.getMaxY() + 1)), this.biome) : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<Holder<Biome>> getBiomesWithin(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Climate.Sampler paramSampler) {
/* 65 */     return Sets.newHashSet(Set.of(this.biome));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\FixedBiomeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */