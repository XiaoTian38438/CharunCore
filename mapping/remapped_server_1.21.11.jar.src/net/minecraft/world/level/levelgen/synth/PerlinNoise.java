/*     */ package net.minecraft.world.level.levelgen.synth;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
/*     */ import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSortedSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.levelgen.PositionalRandomFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PerlinNoise
/*     */ {
/*     */   private static final int ROUND_OFF = 33554432;
/*     */   private final ImprovedNoise[] noiseLevels;
/*     */   private final int firstOctave;
/*     */   private final DoubleList amplitudes;
/*     */   private final double lowestFreqValueFactor;
/*     */   private final double lowestFreqInputFactor;
/*     */   private final double maxValue;
/*     */   
/*     */   @Deprecated
/*     */   public static PerlinNoise createLegacyForBlendedNoise(RandomSource paramRandomSource, IntStream paramIntStream) {
/*  35 */     return new PerlinNoise(paramRandomSource, makeAmplitudes((IntSortedSet)new IntRBTreeSet(paramIntStream.boxed().collect(ImmutableList.toImmutableList()))), false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static PerlinNoise createLegacyForLegacyNetherBiome(RandomSource paramRandomSource, int paramInt, DoubleList paramDoubleList) {
/*  40 */     return new PerlinNoise(paramRandomSource, Pair.of(Integer.valueOf(paramInt), paramDoubleList), false);
/*     */   }
/*     */   
/*     */   public static PerlinNoise create(RandomSource paramRandomSource, IntStream paramIntStream) {
/*  44 */     return create(paramRandomSource, paramIntStream.boxed().collect(ImmutableList.toImmutableList()));
/*     */   }
/*     */   
/*     */   public static PerlinNoise create(RandomSource paramRandomSource, List<Integer> paramList) {
/*  48 */     return new PerlinNoise(paramRandomSource, makeAmplitudes((IntSortedSet)new IntRBTreeSet(paramList)), true);
/*     */   }
/*     */   
/*     */   public static PerlinNoise create(RandomSource paramRandomSource, int paramInt, double paramDouble, double... paramVarArgs) {
/*  52 */     DoubleArrayList doubleArrayList = new DoubleArrayList(paramVarArgs);
/*  53 */     doubleArrayList.add(0, paramDouble);
/*  54 */     return new PerlinNoise(paramRandomSource, Pair.of(Integer.valueOf(paramInt), doubleArrayList), true);
/*     */   }
/*     */   
/*     */   public static PerlinNoise create(RandomSource paramRandomSource, int paramInt, DoubleList paramDoubleList) {
/*  58 */     return new PerlinNoise(paramRandomSource, Pair.of(Integer.valueOf(paramInt), paramDoubleList), true);
/*     */   }
/*     */   
/*     */   private static Pair<Integer, DoubleList> makeAmplitudes(IntSortedSet paramIntSortedSet) {
/*  62 */     if (paramIntSortedSet.isEmpty()) {
/*  63 */       throw new IllegalArgumentException("Need some octaves!");
/*     */     }
/*     */     
/*  66 */     int i = -paramIntSortedSet.firstInt();
/*  67 */     int j = paramIntSortedSet.lastInt();
/*     */     
/*  69 */     int k = i + j + 1;
/*  70 */     if (k < 1) {
/*  71 */       throw new IllegalArgumentException("Total number of octaves needs to be >= 1");
/*     */     }
/*     */     
/*  74 */     DoubleArrayList doubleArrayList = new DoubleArrayList(new double[k]);
/*  75 */     IntBidirectionalIterator intBidirectionalIterator = paramIntSortedSet.iterator();
/*  76 */     while (intBidirectionalIterator.hasNext()) {
/*  77 */       int m = intBidirectionalIterator.nextInt();
/*  78 */       doubleArrayList.set(m + i, 1.0D);
/*     */     } 
/*     */     
/*  81 */     return Pair.of(Integer.valueOf(-i), doubleArrayList);
/*     */   }
/*     */   
/*     */   protected PerlinNoise(RandomSource paramRandomSource, Pair<Integer, DoubleList> paramPair, boolean paramBoolean) {
/*  85 */     this.firstOctave = ((Integer)paramPair.getFirst()).intValue();
/*  86 */     this.amplitudes = (DoubleList)paramPair.getSecond();
/*  87 */     int i = this.amplitudes.size();
/*  88 */     int j = -this.firstOctave;
/*     */     
/*  90 */     this.noiseLevels = new ImprovedNoise[i];
/*     */     
/*  92 */     if (paramBoolean) {
/*  93 */       PositionalRandomFactory positionalRandomFactory = paramRandomSource.forkPositional();
/*  94 */       for (byte b = 0; b < i; b++) {
/*  95 */         if (this.amplitudes.getDouble(b) != 0.0D) {
/*  96 */           int k = this.firstOctave + b;
/*  97 */           this.noiseLevels[b] = new ImprovedNoise(positionalRandomFactory.fromHashOf("octave_" + k));
/*     */         } 
/*     */       } 
/*     */     } else {
/* 101 */       ImprovedNoise improvedNoise = new ImprovedNoise(paramRandomSource);
/* 102 */       if (j >= 0 && j < i) {
/* 103 */         double d = this.amplitudes.getDouble(j);
/* 104 */         if (d != 0.0D) {
/* 105 */           this.noiseLevels[j] = improvedNoise;
/*     */         }
/*     */       } 
/*     */       
/* 109 */       for (int k = j - 1; k >= 0; k--) {
/* 110 */         if (k < i) {
/* 111 */           double d = this.amplitudes.getDouble(k);
/* 112 */           if (d != 0.0D) {
/* 113 */             this.noiseLevels[k] = new ImprovedNoise(paramRandomSource);
/*     */           } else {
/* 115 */             skipOctave(paramRandomSource);
/*     */           } 
/*     */         } else {
/* 118 */           skipOctave(paramRandomSource);
/*     */         } 
/*     */       } 
/*     */       
/* 122 */       if (Arrays.<ImprovedNoise>stream(this.noiseLevels).filter(Objects::nonNull).count() != this.amplitudes.stream().filter(paramDouble -> (paramDouble.doubleValue() != 0.0D)).count()) {
/* 123 */         throw new IllegalStateException("Failed to create correct number of noise levels for given non-zero amplitudes");
/*     */       }
/*     */       
/* 126 */       if (j < i - 1)
/*     */       {
/* 128 */         throw new IllegalArgumentException("Positive octaves are temporarily disabled");
/*     */       }
/*     */     } 
/*     */     
/* 132 */     this.lowestFreqInputFactor = Math.pow(2.0D, -j);
/* 133 */     this.lowestFreqValueFactor = Math.pow(2.0D, (i - 1)) / (Math.pow(2.0D, i) - 1.0D);
/*     */ 
/*     */     
/* 136 */     this.maxValue = edgeValue(2.0D);
/*     */   }
/*     */   
/*     */   protected double maxValue() {
/* 140 */     return this.maxValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void skipOctave(RandomSource paramRandomSource) {
/* 146 */     paramRandomSource.consumeCount(262);
/*     */   }
/*     */   
/*     */   public double getValue(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 150 */     return getValue(paramDouble1, paramDouble2, paramDouble3, 0.0D, 0.0D, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public double getValue(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, boolean paramBoolean) {
/* 158 */     double d1 = 0.0D;
/* 159 */     double d2 = this.lowestFreqInputFactor;
/* 160 */     double d3 = this.lowestFreqValueFactor;
/*     */     
/* 162 */     for (byte b = 0; b < this.noiseLevels.length; b++) {
/* 163 */       ImprovedNoise improvedNoise = this.noiseLevels[b];
/* 164 */       if (improvedNoise != null) {
/* 165 */         double d = improvedNoise.noise(wrap(paramDouble1 * d2), paramBoolean ? -improvedNoise.yo : wrap(paramDouble2 * d2), wrap(paramDouble3 * d2), paramDouble4 * d2, paramDouble5 * d2);
/* 166 */         d1 += this.amplitudes.getDouble(b) * d * d3;
/*     */       } 
/* 168 */       d2 *= 2.0D;
/* 169 */       d3 /= 2.0D;
/*     */     } 
/*     */     
/* 172 */     return d1;
/*     */   }
/*     */ 
/*     */   
/*     */   public double maxBrokenValue(double paramDouble) {
/* 177 */     return edgeValue(paramDouble + 2.0D);
/*     */   }
/*     */   
/*     */   private double edgeValue(double paramDouble) {
/* 181 */     double d1 = 0.0D;
/* 182 */     double d2 = this.lowestFreqValueFactor;
/*     */     
/* 184 */     for (byte b = 0; b < this.noiseLevels.length; b++) {
/* 185 */       ImprovedNoise improvedNoise = this.noiseLevels[b];
/* 186 */       if (improvedNoise != null) {
/* 187 */         d1 += this.amplitudes.getDouble(b) * paramDouble * d2;
/*     */       }
/* 189 */       d2 /= 2.0D;
/*     */     } 
/*     */     
/* 192 */     return d1;
/*     */   }
/*     */   
/*     */   public ImprovedNoise getOctaveNoise(int paramInt) {
/* 196 */     return this.noiseLevels[this.noiseLevels.length - 1 - paramInt];
/*     */   }
/*     */   
/*     */   public static double wrap(double paramDouble) {
/* 200 */     return paramDouble - Mth.lfloor(paramDouble / 3.3554432E7D + 0.5D) * 3.3554432E7D;
/*     */   }
/*     */   
/*     */   protected int firstOctave() {
/* 204 */     return this.firstOctave;
/*     */   }
/*     */   
/*     */   protected DoubleList amplitudes() {
/* 208 */     return this.amplitudes;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void parityConfigString(StringBuilder paramStringBuilder) {
/* 213 */     paramStringBuilder.append("PerlinNoise{");
/* 214 */     List list = this.amplitudes.stream().map(paramDouble -> String.format(Locale.ROOT, "%.2f", new Object[] { paramDouble })).toList();
/* 215 */     paramStringBuilder.append("first octave: ").append(this.firstOctave).append(", amplitudes: ").append(list)
/* 216 */       .append(", noise levels: [");
/*     */     
/* 218 */     for (byte b = 0; b < this.noiseLevels.length; b++) {
/* 219 */       paramStringBuilder.append(b).append(": ");
/* 220 */       ImprovedNoise improvedNoise = this.noiseLevels[b];
/* 221 */       if (improvedNoise == null) {
/* 222 */         paramStringBuilder.append("null");
/*     */       } else {
/* 224 */         improvedNoise.parityConfigString(paramStringBuilder);
/*     */       } 
/* 226 */       paramStringBuilder.append(", ");
/*     */     } 
/*     */     
/* 229 */     paramStringBuilder.append("]");
/* 230 */     paramStringBuilder.append("}");
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\synth\PerlinNoise.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */