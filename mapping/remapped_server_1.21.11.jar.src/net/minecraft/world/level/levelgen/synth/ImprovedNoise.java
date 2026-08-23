/*     */ package net.minecraft.world.level.levelgen.synth;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ 
/*     */ public final class ImprovedNoise
/*     */ {
/*     */   private static final float SHIFT_UP_EPSILON = 1.0E-7F;
/*     */   private final byte[] p;
/*     */   public final double xo;
/*     */   public final double yo;
/*     */   public final double zo;
/*     */   
/*     */   public ImprovedNoise(RandomSource paramRandomSource) {
/*  17 */     this.xo = paramRandomSource.nextDouble() * 256.0D;
/*  18 */     this.yo = paramRandomSource.nextDouble() * 256.0D;
/*  19 */     this.zo = paramRandomSource.nextDouble() * 256.0D;
/*     */     
/*  21 */     this.p = new byte[256];
/*     */     byte b;
/*  23 */     for (b = 0; b < 'Ā'; b++) {
/*  24 */       this.p[b] = (byte)b;
/*     */     }
/*     */     
/*  27 */     for (b = 0; b < 'Ā'; b++) {
/*  28 */       int i = paramRandomSource.nextInt(256 - b);
/*  29 */       byte b1 = this.p[b];
/*  30 */       this.p[b] = this.p[b + i];
/*  31 */       this.p[b + i] = b1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public double noise(double paramDouble1, double paramDouble2, double paramDouble3) {
/*  37 */     return noise(paramDouble1, paramDouble2, paramDouble3, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public double noise(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5) {
/*  45 */     double d7, d1 = paramDouble1 + this.xo;
/*  46 */     double d2 = paramDouble2 + this.yo;
/*  47 */     double d3 = paramDouble3 + this.zo;
/*     */     
/*  49 */     int i = Mth.floor(d1);
/*  50 */     int j = Mth.floor(d2);
/*  51 */     int k = Mth.floor(d3);
/*     */ 
/*     */     
/*  54 */     double d4 = d1 - i;
/*  55 */     double d5 = d2 - j;
/*  56 */     double d6 = d3 - k;
/*     */ 
/*     */ 
/*     */     
/*  60 */     if (paramDouble4 != 0.0D) {
/*     */       double d;
/*     */       
/*  63 */       if (paramDouble5 >= 0.0D && paramDouble5 < d5) {
/*  64 */         d = paramDouble5;
/*     */       } else {
/*  66 */         d = d5;
/*     */       } 
/*     */       
/*  69 */       d7 = Mth.floor(d / paramDouble4 + 1.0000000116860974E-7D) * paramDouble4;
/*     */     } else {
/*  71 */       d7 = 0.0D;
/*     */     } 
/*     */ 
/*     */     
/*  75 */     return sampleAndLerp(i, j, k, d4, d5 - d7, d6, d5);
/*     */   }
/*     */   
/*     */   public double noiseWithDerivative(double paramDouble1, double paramDouble2, double paramDouble3, double[] paramArrayOfdouble) {
/*  79 */     double d1 = paramDouble1 + this.xo;
/*  80 */     double d2 = paramDouble2 + this.yo;
/*  81 */     double d3 = paramDouble3 + this.zo;
/*     */     
/*  83 */     int i = Mth.floor(d1);
/*  84 */     int j = Mth.floor(d2);
/*  85 */     int k = Mth.floor(d3);
/*     */ 
/*     */     
/*  88 */     double d4 = d1 - i;
/*  89 */     double d5 = d2 - j;
/*  90 */     double d6 = d3 - k;
/*     */     
/*  92 */     return sampleWithDerivative(i, j, k, d4, d5, d6, paramArrayOfdouble);
/*     */   }
/*     */   
/*     */   private static double gradDot(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3) {
/*  96 */     return SimplexNoise.dot(SimplexNoise.GRADIENT[paramInt & 0xF], paramDouble1, paramDouble2, paramDouble3);
/*     */   }
/*     */   
/*     */   private int p(int paramInt) {
/* 100 */     return this.p[paramInt & 0xFF] & 0xFF;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private double sampleAndLerp(int paramInt1, int paramInt2, int paramInt3, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 106 */     int i = p(paramInt1);
/* 107 */     int j = p(paramInt1 + 1);
/*     */     
/* 109 */     int k = p(i + paramInt2);
/* 110 */     int m = p(i + paramInt2 + 1);
/* 111 */     int n = p(j + paramInt2);
/* 112 */     int i1 = p(j + paramInt2 + 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     double d1 = gradDot(p(k + paramInt3), paramDouble1, paramDouble2, paramDouble3);
/* 121 */     double d2 = gradDot(p(n + paramInt3), paramDouble1 - 1.0D, paramDouble2, paramDouble3);
/* 122 */     double d3 = gradDot(p(m + paramInt3), paramDouble1, paramDouble2 - 1.0D, paramDouble3);
/* 123 */     double d4 = gradDot(p(i1 + paramInt3), paramDouble1 - 1.0D, paramDouble2 - 1.0D, paramDouble3);
/* 124 */     double d5 = gradDot(p(k + paramInt3 + 1), paramDouble1, paramDouble2, paramDouble3 - 1.0D);
/* 125 */     double d6 = gradDot(p(n + paramInt3 + 1), paramDouble1 - 1.0D, paramDouble2, paramDouble3 - 1.0D);
/* 126 */     double d7 = gradDot(p(m + paramInt3 + 1), paramDouble1, paramDouble2 - 1.0D, paramDouble3 - 1.0D);
/* 127 */     double d8 = gradDot(p(i1 + paramInt3 + 1), paramDouble1 - 1.0D, paramDouble2 - 1.0D, paramDouble3 - 1.0D);
/*     */ 
/*     */     
/* 130 */     double d9 = Mth.smoothstep(paramDouble1);
/* 131 */     double d10 = Mth.smoothstep(paramDouble4);
/* 132 */     double d11 = Mth.smoothstep(paramDouble3);
/*     */ 
/*     */     
/* 135 */     return Mth.lerp3(d9, d10, d11, d1, d2, d3, d4, d5, d6, d7, d8);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private double sampleWithDerivative(int paramInt1, int paramInt2, int paramInt3, double paramDouble1, double paramDouble2, double paramDouble3, double[] paramArrayOfdouble) {
/* 141 */     int i = p(paramInt1);
/* 142 */     int j = p(paramInt1 + 1);
/*     */     
/* 144 */     int k = p(i + paramInt2);
/* 145 */     int m = p(i + paramInt2 + 1);
/* 146 */     int n = p(j + paramInt2);
/* 147 */     int i1 = p(j + paramInt2 + 1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     int i2 = p(k + paramInt3);
/* 153 */     int i3 = p(n + paramInt3);
/* 154 */     int i4 = p(m + paramInt3);
/* 155 */     int i5 = p(i1 + paramInt3);
/* 156 */     int i6 = p(k + paramInt3 + 1);
/* 157 */     int i7 = p(n + paramInt3 + 1);
/* 158 */     int i8 = p(m + paramInt3 + 1);
/* 159 */     int i9 = p(i1 + paramInt3 + 1);
/*     */     
/* 161 */     int[] arrayOfInt1 = SimplexNoise.GRADIENT[i2 & 0xF];
/* 162 */     int[] arrayOfInt2 = SimplexNoise.GRADIENT[i3 & 0xF];
/* 163 */     int[] arrayOfInt3 = SimplexNoise.GRADIENT[i4 & 0xF];
/* 164 */     int[] arrayOfInt4 = SimplexNoise.GRADIENT[i5 & 0xF];
/* 165 */     int[] arrayOfInt5 = SimplexNoise.GRADIENT[i6 & 0xF];
/* 166 */     int[] arrayOfInt6 = SimplexNoise.GRADIENT[i7 & 0xF];
/* 167 */     int[] arrayOfInt7 = SimplexNoise.GRADIENT[i8 & 0xF];
/* 168 */     int[] arrayOfInt8 = SimplexNoise.GRADIENT[i9 & 0xF];
/*     */     
/* 170 */     double d1 = SimplexNoise.dot(arrayOfInt1, paramDouble1, paramDouble2, paramDouble3);
/* 171 */     double d2 = SimplexNoise.dot(arrayOfInt2, paramDouble1 - 1.0D, paramDouble2, paramDouble3);
/* 172 */     double d3 = SimplexNoise.dot(arrayOfInt3, paramDouble1, paramDouble2 - 1.0D, paramDouble3);
/* 173 */     double d4 = SimplexNoise.dot(arrayOfInt4, paramDouble1 - 1.0D, paramDouble2 - 1.0D, paramDouble3);
/* 174 */     double d5 = SimplexNoise.dot(arrayOfInt5, paramDouble1, paramDouble2, paramDouble3 - 1.0D);
/* 175 */     double d6 = SimplexNoise.dot(arrayOfInt6, paramDouble1 - 1.0D, paramDouble2, paramDouble3 - 1.0D);
/* 176 */     double d7 = SimplexNoise.dot(arrayOfInt7, paramDouble1, paramDouble2 - 1.0D, paramDouble3 - 1.0D);
/* 177 */     double d8 = SimplexNoise.dot(arrayOfInt8, paramDouble1 - 1.0D, paramDouble2 - 1.0D, paramDouble3 - 1.0D);
/*     */     
/* 179 */     double d9 = Mth.smoothstep(paramDouble1);
/* 180 */     double d10 = Mth.smoothstep(paramDouble2);
/* 181 */     double d11 = Mth.smoothstep(paramDouble3);
/*     */     
/* 183 */     double d12 = Mth.lerp3(d9, d10, d11, arrayOfInt1[0], arrayOfInt2[0], arrayOfInt3[0], arrayOfInt4[0], arrayOfInt5[0], arrayOfInt6[0], arrayOfInt7[0], arrayOfInt8[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     double d13 = Mth.lerp3(d9, d10, d11, arrayOfInt1[1], arrayOfInt2[1], arrayOfInt3[1], arrayOfInt4[1], arrayOfInt5[1], arrayOfInt6[1], arrayOfInt7[1], arrayOfInt8[1]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 199 */     double d14 = Mth.lerp3(d9, d10, d11, arrayOfInt1[2], arrayOfInt2[2], arrayOfInt3[2], arrayOfInt4[2], arrayOfInt5[2], arrayOfInt6[2], arrayOfInt7[2], arrayOfInt8[2]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 207 */     double d15 = Mth.lerp2(d10, d11, d2 - d1, d4 - d3, d6 - d5, d8 - d7);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     double d16 = Mth.lerp2(d11, d9, d3 - d1, d7 - d5, d4 - d2, d8 - d6);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 223 */     double d17 = Mth.lerp2(d9, d10, d5 - d1, d6 - d2, d7 - d3, d8 - d4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     double d18 = Mth.smoothstepDerivative(paramDouble1);
/* 232 */     double d19 = Mth.smoothstepDerivative(paramDouble2);
/* 233 */     double d20 = Mth.smoothstepDerivative(paramDouble3);
/*     */     
/* 235 */     double d21 = d12 + d18 * d15;
/* 236 */     double d22 = d13 + d19 * d16;
/* 237 */     double d23 = d14 + d20 * d17;
/*     */     
/* 239 */     paramArrayOfdouble[0] = paramArrayOfdouble[0] + d21;
/* 240 */     paramArrayOfdouble[1] = paramArrayOfdouble[1] + d22;
/* 241 */     paramArrayOfdouble[2] = paramArrayOfdouble[2] + d23;
/*     */ 
/*     */     
/* 244 */     return Mth.lerp3(d9, d10, d11, d1, d2, d3, d4, d5, d6, d7, d8);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void parityConfigString(StringBuilder paramStringBuilder) {
/* 249 */     NoiseUtils.parityNoiseOctaveConfigString(paramStringBuilder, this.xo, this.yo, this.zo, this.p);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\synth\ImprovedNoise.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */