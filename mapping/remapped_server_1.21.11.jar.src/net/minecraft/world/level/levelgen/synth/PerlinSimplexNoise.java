/*    */ package net.minecraft.world.level.levelgen.synth;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
/*    */ import it.unimi.dsi.fastutil.ints.IntSortedSet;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ 
/*    */ 
/*    */ public class PerlinSimplexNoise
/*    */ {
/*    */   private final SimplexNoise[] noiseLevels;
/*    */   private final double highestFreqValueFactor;
/*    */   private final double highestFreqInputFactor;
/*    */   
/*    */   public PerlinSimplexNoise(RandomSource paramRandomSource, List<Integer> paramList) {
/* 18 */     this(paramRandomSource, (IntSortedSet)new IntRBTreeSet(paramList));
/*    */   }
/*    */   
/*    */   private PerlinSimplexNoise(RandomSource paramRandomSource, IntSortedSet paramIntSortedSet) {
/* 22 */     if (paramIntSortedSet.isEmpty()) {
/* 23 */       throw new IllegalArgumentException("Need some octaves!");
/*    */     }
/*    */     
/* 26 */     int i = -paramIntSortedSet.firstInt();
/* 27 */     int j = paramIntSortedSet.lastInt();
/*    */     
/* 29 */     int k = i + j + 1;
/* 30 */     if (k < 1) {
/* 31 */       throw new IllegalArgumentException("Total number of octaves needs to be >= 1");
/*    */     }
/*    */     
/* 34 */     SimplexNoise simplexNoise = new SimplexNoise(paramRandomSource);
/* 35 */     int m = j;
/*    */     
/* 37 */     this.noiseLevels = new SimplexNoise[k];
/* 38 */     if (m >= 0 && m < k && paramIntSortedSet.contains(0)) {
/* 39 */       this.noiseLevels[m] = simplexNoise;
/*    */     }
/*    */     
/* 42 */     for (int n = m + 1; n < k; n++) {
/* 43 */       if (n >= 0 && paramIntSortedSet.contains(m - n)) {
/* 44 */         this.noiseLevels[n] = new SimplexNoise(paramRandomSource);
/*    */       } else {
/* 46 */         paramRandomSource.consumeCount(262);
/*    */       } 
/*    */     } 
/*    */     
/* 50 */     if (j > 0) {
/*    */       
/* 52 */       long l = (long)(simplexNoise.getValue(simplexNoise.xo, simplexNoise.yo, simplexNoise.zo) * 9.223372036854776E18D);
/* 53 */       WorldgenRandom worldgenRandom = new WorldgenRandom((RandomSource)new LegacyRandomSource(l));
/* 54 */       for (int i1 = m - 1; i1 >= 0; i1--) {
/* 55 */         if (i1 < k && paramIntSortedSet.contains(m - i1)) {
/* 56 */           this.noiseLevels[i1] = new SimplexNoise((RandomSource)worldgenRandom);
/*    */         } else {
/* 58 */           worldgenRandom.consumeCount(262);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 63 */     this.highestFreqInputFactor = Math.pow(2.0D, j);
/* 64 */     this.highestFreqValueFactor = 1.0D / (Math.pow(2.0D, k) - 1.0D);
/*    */   }
/*    */   
/*    */   public double getValue(double paramDouble1, double paramDouble2, boolean paramBoolean) {
/* 68 */     double d1 = 0.0D;
/* 69 */     double d2 = this.highestFreqInputFactor;
/* 70 */     double d3 = this.highestFreqValueFactor;
/*    */     
/* 72 */     for (SimplexNoise simplexNoise : this.noiseLevels) {
/* 73 */       if (simplexNoise != null) {
/* 74 */         d1 += simplexNoise.getValue(paramDouble1 * d2 + (paramBoolean ? simplexNoise.xo : 0.0D), paramDouble2 * d2 + (paramBoolean ? simplexNoise.yo : 0.0D)) * d3;
/*    */       }
/* 76 */       d2 /= 2.0D;
/* 77 */       d3 *= 2.0D;
/*    */     } 
/*    */     
/* 80 */     return d1;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\synth\PerlinSimplexNoise.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */