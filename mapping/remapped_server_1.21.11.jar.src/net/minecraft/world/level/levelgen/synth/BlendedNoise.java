/*     */ package net.minecraft.world.level.levelgen.synth;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Locale;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.levelgen.DensityFunction;
/*     */ import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BlendedNoise
/*     */   implements DensityFunction.SimpleFunction
/*     */ {
/*  26 */   private static final Codec<Double> SCALE_RANGE = Codec.doubleRange(0.001D, 1000.0D);
/*     */   static {
/*  28 */     DATA_CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)SCALE_RANGE.fieldOf("xz_scale").forGetter(()), (App)SCALE_RANGE.fieldOf("y_scale").forGetter(()), (App)SCALE_RANGE.fieldOf("xz_factor").forGetter(()), (App)SCALE_RANGE.fieldOf("y_factor").forGetter(()), (App)Codec.doubleRange(1.0D, 8.0D).fieldOf("smear_scale_multiplier").forGetter(())).apply((Applicative)paramInstance, BlendedNoise::createUnseeded));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final MapCodec<BlendedNoise> DATA_CODEC;
/*     */ 
/*     */   
/*  36 */   public static final KeyDispatchDataCodec<BlendedNoise> CODEC = KeyDispatchDataCodec.of(DATA_CODEC);
/*     */   
/*     */   private final PerlinNoise minLimitNoise;
/*     */   
/*     */   private final PerlinNoise maxLimitNoise;
/*     */   
/*     */   private final PerlinNoise mainNoise;
/*     */   
/*     */   private final double xzMultiplier;
/*     */   private final double yMultiplier;
/*     */   private final double xzFactor;
/*     */   private final double yFactor;
/*     */   private final double smearScaleMultiplier;
/*     */   private final double maxValue;
/*     */   private final double xzScale;
/*     */   private final double yScale;
/*     */   
/*     */   public static BlendedNoise createUnseeded(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5) {
/*  54 */     return new BlendedNoise((RandomSource)new XoroshiroRandomSource(0L), paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5);
/*     */   }
/*     */   
/*     */   private BlendedNoise(PerlinNoise paramPerlinNoise1, PerlinNoise paramPerlinNoise2, PerlinNoise paramPerlinNoise3, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5) {
/*  58 */     this.minLimitNoise = paramPerlinNoise1;
/*  59 */     this.maxLimitNoise = paramPerlinNoise2;
/*  60 */     this.mainNoise = paramPerlinNoise3;
/*     */     
/*  62 */     this.xzScale = paramDouble1;
/*  63 */     this.yScale = paramDouble2;
/*  64 */     this.xzFactor = paramDouble3;
/*  65 */     this.yFactor = paramDouble4;
/*  66 */     this.smearScaleMultiplier = paramDouble5;
/*     */     
/*  68 */     this.xzMultiplier = 684.412D * this.xzScale;
/*  69 */     this.yMultiplier = 684.412D * this.yScale;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     this.maxValue = paramPerlinNoise1.maxBrokenValue(this.yMultiplier);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public BlendedNoise(RandomSource paramRandomSource, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5) {
/*  79 */     this(
/*  80 */         PerlinNoise.createLegacyForBlendedNoise(paramRandomSource, IntStream.rangeClosed(-15, 0)), 
/*  81 */         PerlinNoise.createLegacyForBlendedNoise(paramRandomSource, IntStream.rangeClosed(-15, 0)), 
/*  82 */         PerlinNoise.createLegacyForBlendedNoise(paramRandomSource, IntStream.rangeClosed(-7, 0)), paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlendedNoise withNewRandom(RandomSource paramRandomSource) {
/*  88 */     return new BlendedNoise(paramRandomSource, this.xzScale, this.yScale, this.xzFactor, this.yFactor, this.smearScaleMultiplier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext paramFunctionContext) {
/*  99 */     double d1 = paramFunctionContext.blockX() * this.xzMultiplier;
/* 100 */     double d2 = paramFunctionContext.blockY() * this.yMultiplier;
/* 101 */     double d3 = paramFunctionContext.blockZ() * this.xzMultiplier;
/*     */     
/* 103 */     double d4 = d1 / this.xzFactor;
/* 104 */     double d5 = d2 / this.yFactor;
/* 105 */     double d6 = d3 / this.xzFactor;
/*     */     
/* 107 */     double d7 = this.yMultiplier * this.smearScaleMultiplier;
/* 108 */     double d8 = d7 / this.yFactor;
/*     */     
/* 110 */     double d9 = 0.0D;
/* 111 */     double d10 = 0.0D;
/* 112 */     double d11 = 0.0D;
/*     */     
/* 114 */     boolean bool1 = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     double d12 = 1.0D;
/*     */ 
/*     */     
/* 125 */     for (byte b1 = 0; b1 < 8; b1++) {
/* 126 */       ImprovedNoise improvedNoise = this.mainNoise.getOctaveNoise(b1);
/* 127 */       if (improvedNoise != null) {
/* 128 */         d11 += improvedNoise.noise(PerlinNoise.wrap(d4 * d12), PerlinNoise.wrap(d5 * d12), PerlinNoise.wrap(d6 * d12), d8 * d12, d5 * d12) / d12;
/*     */       }
/* 130 */       d12 /= 2.0D;
/*     */     } 
/*     */     
/* 133 */     double d13 = (d11 / 10.0D + 1.0D) / 2.0D;
/*     */ 
/*     */ 
/*     */     
/* 137 */     boolean bool2 = (d13 >= 1.0D) ? true : false;
/* 138 */     boolean bool3 = (d13 <= 0.0D) ? true : false;
/* 139 */     d12 = 1.0D;
/* 140 */     for (byte b2 = 0; b2 < 16; b2++) {
/* 141 */       double d14 = PerlinNoise.wrap(d1 * d12);
/* 142 */       double d15 = PerlinNoise.wrap(d2 * d12);
/* 143 */       double d16 = PerlinNoise.wrap(d3 * d12);
/* 144 */       double d17 = d7 * d12;
/* 145 */       if (!bool2) {
/* 146 */         ImprovedNoise improvedNoise = this.minLimitNoise.getOctaveNoise(b2);
/* 147 */         if (improvedNoise != null) {
/* 148 */           d9 += improvedNoise.noise(d14, d15, d16, d17, d2 * d12) / d12;
/*     */         }
/*     */       } 
/* 151 */       if (!bool3) {
/* 152 */         ImprovedNoise improvedNoise = this.maxLimitNoise.getOctaveNoise(b2);
/* 153 */         if (improvedNoise != null) {
/* 154 */           d10 += improvedNoise.noise(d14, d15, d16, d17, d2 * d12) / d12;
/*     */         }
/*     */       } 
/* 157 */       d12 /= 2.0D;
/*     */     } 
/*     */ 
/*     */     
/* 161 */     return Mth.clampedLerp(d13, d9 / 512.0D, d10 / 512.0D) / 128.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double minValue() {
/* 166 */     return -maxValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public double maxValue() {
/* 171 */     return this.maxValue;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void parityConfigString(StringBuilder paramStringBuilder) {
/* 176 */     paramStringBuilder.append("BlendedNoise{minLimitNoise=");
/* 177 */     this.minLimitNoise.parityConfigString(paramStringBuilder);
/* 178 */     paramStringBuilder.append(", maxLimitNoise=");
/* 179 */     this.maxLimitNoise.parityConfigString(paramStringBuilder);
/* 180 */     paramStringBuilder.append(", mainNoise=");
/* 181 */     this.mainNoise.parityConfigString(paramStringBuilder);
/*     */     
/* 183 */     paramStringBuilder.append(
/* 184 */         String.format(Locale.ROOT, ", xzScale=%.3f, yScale=%.3f, xzMainScale=%.3f, yMainScale=%.3f, cellWidth=4, cellHeight=8", new Object[] {
/* 185 */             Double.valueOf(684.412D), Double.valueOf(684.412D), Double.valueOf(8.555150000000001D), Double.valueOf(4.277575000000001D)
/* 186 */           })).append('}');
/*     */   }
/*     */ 
/*     */   
/*     */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 191 */     return (KeyDispatchDataCodec)CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\synth\BlendedNoise.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */