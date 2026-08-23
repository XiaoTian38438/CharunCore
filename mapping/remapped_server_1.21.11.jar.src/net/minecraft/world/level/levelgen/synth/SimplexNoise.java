/*     */ package net.minecraft.world.level.levelgen.synth;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class SimplexNoise {
/*   7 */   protected static final int[][] GRADIENT = new int[][] { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 }, { 1, 1, 0 }, { 0, -1, 1 }, { -1, 1, 0 }, { 0, -1, -1 } };
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
/*     */ 
/*     */ 
/*     */   
/*  26 */   private static final double SQRT_3 = Math.sqrt(3.0D);
/*  27 */   private static final double F2 = 0.5D * (SQRT_3 - 1.0D);
/*  28 */   private static final double G2 = (3.0D - SQRT_3) / 6.0D;
/*     */   
/*  30 */   private final int[] p = new int[512];
/*     */   
/*     */   public final double xo;
/*     */   public final double yo;
/*     */   public final double zo;
/*     */   
/*     */   public SimplexNoise(RandomSource paramRandomSource) {
/*  37 */     this.xo = paramRandomSource.nextDouble() * 256.0D;
/*  38 */     this.yo = paramRandomSource.nextDouble() * 256.0D;
/*  39 */     this.zo = paramRandomSource.nextDouble() * 256.0D; byte b;
/*  40 */     for (b = 0; b < 'Ā'; b++) {
/*  41 */       this.p[b] = b;
/*     */     }
/*     */     
/*  44 */     for (b = 0; b < 'Ā'; b++) {
/*  45 */       int i = paramRandomSource.nextInt(256 - b);
/*  46 */       int j = this.p[b];
/*  47 */       this.p[b] = this.p[i + b];
/*  48 */       this.p[i + b] = j;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int p(int paramInt) {
/*  53 */     return this.p[paramInt & 0xFF];
/*     */   }
/*     */   
/*     */   protected static double dot(int[] paramArrayOfint, double paramDouble1, double paramDouble2, double paramDouble3) {
/*  57 */     return paramArrayOfint[0] * paramDouble1 + paramArrayOfint[1] * paramDouble2 + paramArrayOfint[2] * paramDouble3;
/*     */   }
/*     */ 
/*     */   
/*     */   private double getCornerNoise3D(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/*  62 */     double d1, d2 = paramDouble4 - paramDouble1 * paramDouble1 - paramDouble2 * paramDouble2 - paramDouble3 * paramDouble3;
/*  63 */     if (d2 < 0.0D) {
/*  64 */       d1 = 0.0D;
/*     */     } else {
/*  66 */       d2 *= d2;
/*  67 */       d1 = d2 * d2 * dot(GRADIENT[paramInt], paramDouble1, paramDouble2, paramDouble3);
/*     */     } 
/*  69 */     return d1;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getValue(double paramDouble1, double paramDouble2) {
/*     */     byte b1, b2;
/*  75 */     double d1 = (paramDouble1 + paramDouble2) * F2;
/*  76 */     int i = Mth.floor(paramDouble1 + d1);
/*  77 */     int j = Mth.floor(paramDouble2 + d1);
/*     */ 
/*     */     
/*  80 */     double d2 = (i + j) * G2;
/*  81 */     double d3 = i - d2;
/*  82 */     double d4 = j - d2;
/*     */ 
/*     */     
/*  85 */     double d5 = paramDouble1 - d3;
/*  86 */     double d6 = paramDouble2 - d4;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     if (d5 > d6) {
/*     */       
/*  96 */       b1 = 1;
/*  97 */       b2 = 0;
/*     */     } else {
/*     */       
/* 100 */       b1 = 0;
/* 101 */       b2 = 1;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     double d7 = d5 - b1 + G2;
/* 109 */     double d8 = d6 - b2 + G2;
/*     */ 
/*     */     
/* 112 */     double d9 = d5 - 1.0D + 2.0D * G2;
/* 113 */     double d10 = d6 - 1.0D + 2.0D * G2;
/*     */ 
/*     */     
/* 116 */     int k = i & 0xFF;
/* 117 */     int m = j & 0xFF;
/*     */     
/* 119 */     int n = p(k + p(m)) % 12;
/* 120 */     int i1 = p(k + b1 + p(m + b2)) % 12;
/* 121 */     int i2 = p(k + 1 + p(m + 1)) % 12;
/*     */ 
/*     */ 
/*     */     
/* 125 */     double d11 = getCornerNoise3D(n, d5, d6, 0.0D, 0.5D);
/* 126 */     double d12 = getCornerNoise3D(i1, d7, d8, 0.0D, 0.5D);
/* 127 */     double d13 = getCornerNoise3D(i2, d9, d10, 0.0D, 0.5D);
/*     */ 
/*     */ 
/*     */     
/* 131 */     return 70.0D * (d11 + d12 + d13);
/*     */   }
/*     */   
/*     */   public double getValue(double paramDouble1, double paramDouble2, double paramDouble3) {
/*     */     byte b1, b2, b3, b4, b5, b6;
/* 136 */     double d1 = 0.3333333333333333D;
/* 137 */     double d2 = (paramDouble1 + paramDouble2 + paramDouble3) * 0.3333333333333333D;
/*     */     
/* 139 */     int i = Mth.floor(paramDouble1 + d2);
/* 140 */     int j = Mth.floor(paramDouble2 + d2);
/* 141 */     int k = Mth.floor(paramDouble3 + d2);
/* 142 */     double d3 = 0.16666666666666666D;
/* 143 */     double d4 = (i + j + k) * 0.16666666666666666D;
/*     */     
/* 145 */     double d5 = i - d4;
/* 146 */     double d6 = j - d4;
/* 147 */     double d7 = k - d4;
/*     */     
/* 149 */     double d8 = paramDouble1 - d5;
/* 150 */     double d9 = paramDouble2 - d6;
/* 151 */     double d10 = paramDouble3 - d7;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     if (d8 >= d9) {
/* 161 */       if (d9 >= d10) {
/*     */         
/* 163 */         b1 = 1;
/* 164 */         b2 = 0;
/* 165 */         b3 = 0;
/* 166 */         b4 = 1;
/* 167 */         b5 = 1;
/* 168 */         b6 = 0;
/* 169 */       } else if (d8 >= d10) {
/*     */         
/* 171 */         b1 = 1;
/* 172 */         b2 = 0;
/* 173 */         b3 = 0;
/* 174 */         b4 = 1;
/* 175 */         b5 = 0;
/* 176 */         b6 = 1;
/*     */       } else {
/*     */         
/* 179 */         b1 = 0;
/* 180 */         b2 = 0;
/* 181 */         b3 = 1;
/* 182 */         b4 = 1;
/* 183 */         b5 = 0;
/* 184 */         b6 = 1;
/*     */       }
/*     */     
/*     */     }
/* 188 */     else if (d9 < d10) {
/*     */       
/* 190 */       b1 = 0;
/* 191 */       b2 = 0;
/* 192 */       b3 = 1;
/* 193 */       b4 = 0;
/* 194 */       b5 = 1;
/* 195 */       b6 = 1;
/* 196 */     } else if (d8 < d10) {
/*     */       
/* 198 */       b1 = 0;
/* 199 */       b2 = 1;
/* 200 */       b3 = 0;
/* 201 */       b4 = 0;
/* 202 */       b5 = 1;
/* 203 */       b6 = 1;
/*     */     } else {
/*     */       
/* 206 */       b1 = 0;
/* 207 */       b2 = 1;
/* 208 */       b3 = 0;
/* 209 */       b4 = 1;
/* 210 */       b5 = 1;
/* 211 */       b6 = 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 220 */     double d11 = d8 - b1 + 0.16666666666666666D;
/* 221 */     double d12 = d9 - b2 + 0.16666666666666666D;
/* 222 */     double d13 = d10 - b3 + 0.16666666666666666D;
/*     */ 
/*     */     
/* 225 */     double d14 = d8 - b4 + 0.3333333333333333D;
/* 226 */     double d15 = d9 - b5 + 0.3333333333333333D;
/* 227 */     double d16 = d10 - b6 + 0.3333333333333333D;
/*     */ 
/*     */     
/* 230 */     double d17 = d8 - 1.0D + 0.5D;
/* 231 */     double d18 = d9 - 1.0D + 0.5D;
/* 232 */     double d19 = d10 - 1.0D + 0.5D;
/*     */ 
/*     */     
/* 235 */     int m = i & 0xFF;
/* 236 */     int n = j & 0xFF;
/* 237 */     int i1 = k & 0xFF;
/*     */     
/* 239 */     int i2 = p(m + p(n + p(i1))) % 12;
/* 240 */     int i3 = p(m + b1 + p(n + b2 + p(i1 + b3))) % 12;
/* 241 */     int i4 = p(m + b4 + p(n + b5 + p(i1 + b6))) % 12;
/* 242 */     int i5 = p(m + 1 + p(n + 1 + p(i1 + 1))) % 12;
/*     */ 
/*     */     
/* 245 */     double d20 = getCornerNoise3D(i2, d8, d9, d10, 0.6D);
/* 246 */     double d21 = getCornerNoise3D(i3, d11, d12, d13, 0.6D);
/* 247 */     double d22 = getCornerNoise3D(i4, d14, d15, d16, 0.6D);
/* 248 */     double d23 = getCornerNoise3D(i5, d17, d18, d19, 0.6D);
/*     */ 
/*     */ 
/*     */     
/* 252 */     return 32.0D * (d20 + d21 + d22 + d23);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\synth\SimplexNoise.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */