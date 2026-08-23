/*     */ package net.minecraft.util;
/*     */ 
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector4f;
/*     */ 
/*     */ public class ARGB {
/*     */   private static final int LINEAR_CHANNEL_DEPTH = 1024;
/*     */   
/*     */   static {
/*  11 */     SRGB_TO_LINEAR = Util.<short[]>make(new short[256], paramArrayOfshort -> {
/*     */           for (byte b = 0; b < paramArrayOfshort.length; b++) {
/*     */             float f = b / 255.0F;
/*     */             paramArrayOfshort[b] = (short)Math.round(computeSrgbToLinear(f) * 1023.0F);
/*     */           } 
/*     */         });
/*  17 */     LINEAR_TO_SRGB = Util.<byte[]>make(new byte[1024], paramArrayOfbyte -> {
/*     */           for (byte b = 0; b < paramArrayOfbyte.length; b++) {
/*     */             float f = b / 1023.0F;
/*     */             paramArrayOfbyte[b] = (byte)Math.round(computeLinearToSrgb(f) * 255.0F);
/*     */           } 
/*     */         });
/*     */   }
/*     */   private static final short[] SRGB_TO_LINEAR; private static final byte[] LINEAR_TO_SRGB;
/*     */   private static float computeSrgbToLinear(float paramFloat) {
/*  26 */     if (paramFloat >= 0.04045F) {
/*  27 */       return (float)Math.pow((paramFloat + 0.055D) / 1.055D, 2.4D);
/*     */     }
/*  29 */     return paramFloat / 12.92F;
/*     */   }
/*     */ 
/*     */   
/*     */   private static float computeLinearToSrgb(float paramFloat) {
/*  34 */     if (paramFloat >= 0.0031308F) {
/*  35 */       return (float)(1.055D * Math.pow(paramFloat, 0.4166666666666667D) - 0.055D);
/*     */     }
/*  37 */     return 12.92F * paramFloat;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float srgbToLinearChannel(int paramInt) {
/*  42 */     return SRGB_TO_LINEAR[paramInt] / 1023.0F;
/*     */   }
/*     */   
/*     */   public static int linearToSrgbChannel(float paramFloat) {
/*  46 */     return LINEAR_TO_SRGB[Mth.floor(paramFloat * 1023.0F)] & 0xFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int meanLinear(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  53 */     return color((
/*  54 */         alpha(paramInt1) + alpha(paramInt2) + alpha(paramInt3) + alpha(paramInt4)) / 4, 
/*  55 */         linearChannelMean(red(paramInt1), red(paramInt2), red(paramInt3), red(paramInt4)), 
/*  56 */         linearChannelMean(green(paramInt1), green(paramInt2), green(paramInt3), green(paramInt4)), 
/*  57 */         linearChannelMean(blue(paramInt1), blue(paramInt2), blue(paramInt3), blue(paramInt4)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int linearChannelMean(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  62 */     int i = (SRGB_TO_LINEAR[paramInt1] + SRGB_TO_LINEAR[paramInt2] + SRGB_TO_LINEAR[paramInt3] + SRGB_TO_LINEAR[paramInt4]) / 4;
/*  63 */     return LINEAR_TO_SRGB[i] & 0xFF;
/*     */   }
/*     */   
/*     */   public static int alpha(int paramInt) {
/*  67 */     return paramInt >>> 24;
/*     */   }
/*     */   
/*     */   public static int red(int paramInt) {
/*  71 */     return paramInt >> 16 & 0xFF;
/*     */   }
/*     */   
/*     */   public static int green(int paramInt) {
/*  75 */     return paramInt >> 8 & 0xFF;
/*     */   }
/*     */   
/*     */   public static int blue(int paramInt) {
/*  79 */     return paramInt & 0xFF;
/*     */   }
/*     */   
/*     */   public static int color(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  83 */     return (paramInt1 & 0xFF) << 24 | (paramInt2 & 0xFF) << 16 | (paramInt3 & 0xFF) << 8 | paramInt4 & 0xFF;
/*     */   }
/*     */   
/*     */   public static int color(int paramInt1, int paramInt2, int paramInt3) {
/*  87 */     return color(255, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */   
/*     */   public static int color(Vec3 paramVec3) {
/*  91 */     return color(as8BitChannel((float)paramVec3.x()), as8BitChannel((float)paramVec3.y()), as8BitChannel((float)paramVec3.z()));
/*     */   }
/*     */   
/*     */   public static int multiply(int paramInt1, int paramInt2) {
/*  95 */     if (paramInt1 == -1)
/*  96 */       return paramInt2; 
/*  97 */     if (paramInt2 == -1) {
/*  98 */       return paramInt1;
/*     */     }
/* 100 */     return color(
/* 101 */         alpha(paramInt1) * alpha(paramInt2) / 255, 
/* 102 */         red(paramInt1) * red(paramInt2) / 255, 
/* 103 */         green(paramInt1) * green(paramInt2) / 255, 
/* 104 */         blue(paramInt1) * blue(paramInt2) / 255);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int addRgb(int paramInt1, int paramInt2) {
/* 109 */     return color(
/* 110 */         alpha(paramInt1), 
/* 111 */         Math.min(red(paramInt1) + red(paramInt2), 255), 
/* 112 */         Math.min(green(paramInt1) + green(paramInt2), 255), 
/* 113 */         Math.min(blue(paramInt1) + blue(paramInt2), 255));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int subtractRgb(int paramInt1, int paramInt2) {
/* 118 */     return color(
/* 119 */         alpha(paramInt1), 
/* 120 */         Math.max(red(paramInt1) - red(paramInt2), 0), 
/* 121 */         Math.max(green(paramInt1) - green(paramInt2), 0), 
/* 122 */         Math.max(blue(paramInt1) - blue(paramInt2), 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int multiplyAlpha(int paramInt, float paramFloat) {
/* 127 */     if (paramInt == 0 || paramFloat <= 0.0F)
/* 128 */       return 0; 
/* 129 */     if (paramFloat >= 1.0F) {
/* 130 */       return paramInt;
/*     */     }
/* 132 */     return color(alphaFloat(paramInt) * paramFloat, paramInt);
/*     */   }
/*     */   
/*     */   public static int scaleRGB(int paramInt, float paramFloat) {
/* 136 */     return scaleRGB(paramInt, paramFloat, paramFloat, paramFloat);
/*     */   }
/*     */   
/*     */   public static int scaleRGB(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3) {
/* 140 */     return color(
/* 141 */         alpha(paramInt), 
/* 142 */         Math.clamp((int)(red(paramInt) * paramFloat1), 0, 255), 
/* 143 */         Math.clamp((int)(green(paramInt) * paramFloat2), 0, 255), 
/* 144 */         Math.clamp((int)(blue(paramInt) * paramFloat3), 0, 255));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int scaleRGB(int paramInt1, int paramInt2) {
/* 149 */     return color(
/* 150 */         alpha(paramInt1), 
/* 151 */         Math.clamp(red(paramInt1) * paramInt2 / 255L, 0, 255), 
/* 152 */         Math.clamp(green(paramInt1) * paramInt2 / 255L, 0, 255), 
/* 153 */         Math.clamp(blue(paramInt1) * paramInt2 / 255L, 0, 255));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int greyscale(int paramInt) {
/* 158 */     int i = (int)(red(paramInt) * 0.3F + green(paramInt) * 0.59F + blue(paramInt) * 0.11F);
/* 159 */     return color(alpha(paramInt), i, i, i);
/*     */   }
/*     */   
/*     */   public static int alphaBlend(int paramInt1, int paramInt2) {
/* 163 */     int i = alpha(paramInt1);
/* 164 */     int j = alpha(paramInt2);
/* 165 */     if (j == 255)
/* 166 */       return paramInt2; 
/* 167 */     if (j == 0) {
/* 168 */       return paramInt1;
/*     */     }
/* 170 */     int k = j + i * (255 - j) / 255;
/* 171 */     return color(k, 
/* 172 */         alphaBlendChannel(k, j, red(paramInt1), red(paramInt2)), 
/* 173 */         alphaBlendChannel(k, j, green(paramInt1), green(paramInt2)), 
/* 174 */         alphaBlendChannel(k, j, blue(paramInt1), blue(paramInt2)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int alphaBlendChannel(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 180 */     return (paramInt4 * paramInt2 + paramInt3 * (paramInt1 - paramInt2)) / paramInt1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int srgbLerp(float paramFloat, int paramInt1, int paramInt2) {
/* 189 */     int i = Mth.lerpInt(paramFloat, alpha(paramInt1), alpha(paramInt2));
/* 190 */     int j = Mth.lerpInt(paramFloat, red(paramInt1), red(paramInt2));
/* 191 */     int k = Mth.lerpInt(paramFloat, green(paramInt1), green(paramInt2));
/* 192 */     int m = Mth.lerpInt(paramFloat, blue(paramInt1), blue(paramInt2));
/* 193 */     return color(i, j, k, m);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int linearLerp(float paramFloat, int paramInt1, int paramInt2) {
/* 201 */     return color(
/* 202 */         Mth.lerpInt(paramFloat, alpha(paramInt1), alpha(paramInt2)), LINEAR_TO_SRGB[
/* 203 */           Mth.lerpInt(paramFloat, SRGB_TO_LINEAR[red(paramInt1)], SRGB_TO_LINEAR[red(paramInt2)])] & 0xFF, LINEAR_TO_SRGB[
/* 204 */           Mth.lerpInt(paramFloat, SRGB_TO_LINEAR[green(paramInt1)], SRGB_TO_LINEAR[green(paramInt2)])] & 0xFF, LINEAR_TO_SRGB[
/* 205 */           Mth.lerpInt(paramFloat, SRGB_TO_LINEAR[blue(paramInt1)], SRGB_TO_LINEAR[blue(paramInt2)])] & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int opaque(int paramInt) {
/* 210 */     return paramInt | 0xFF000000;
/*     */   }
/*     */   
/*     */   public static int transparent(int paramInt) {
/* 214 */     return paramInt & 0xFFFFFF;
/*     */   }
/*     */   
/*     */   public static int color(int paramInt1, int paramInt2) {
/* 218 */     return paramInt1 << 24 | paramInt2 & 0xFFFFFF;
/*     */   }
/*     */   
/*     */   public static int color(float paramFloat, int paramInt) {
/* 222 */     return as8BitChannel(paramFloat) << 24 | paramInt & 0xFFFFFF;
/*     */   }
/*     */   
/*     */   public static int white(float paramFloat) {
/* 226 */     return as8BitChannel(paramFloat) << 24 | 0xFFFFFF;
/*     */   }
/*     */   
/*     */   public static int white(int paramInt) {
/* 230 */     return paramInt << 24 | 0xFFFFFF;
/*     */   }
/*     */   
/*     */   public static int black(float paramFloat) {
/* 234 */     return as8BitChannel(paramFloat) << 24;
/*     */   }
/*     */   
/*     */   public static int black(int paramInt) {
/* 238 */     return paramInt << 24;
/*     */   }
/*     */   
/*     */   public static int colorFromFloat(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
/* 242 */     return color(
/* 243 */         as8BitChannel(paramFloat1), 
/* 244 */         as8BitChannel(paramFloat2), 
/* 245 */         as8BitChannel(paramFloat3), 
/* 246 */         as8BitChannel(paramFloat4));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Vector3f vector3fFromRGB24(int paramInt) {
/* 251 */     return new Vector3f(redFloat(paramInt), greenFloat(paramInt), blueFloat(paramInt));
/*     */   }
/*     */   
/*     */   public static Vector4f vector4fFromARGB32(int paramInt) {
/* 255 */     return new Vector4f(redFloat(paramInt), greenFloat(paramInt), blueFloat(paramInt), alphaFloat(paramInt));
/*     */   }
/*     */   
/*     */   public static int average(int paramInt1, int paramInt2) {
/* 259 */     return color((
/* 260 */         alpha(paramInt1) + alpha(paramInt2)) / 2, (
/* 261 */         red(paramInt1) + red(paramInt2)) / 2, (
/* 262 */         green(paramInt1) + green(paramInt2)) / 2, (
/* 263 */         blue(paramInt1) + blue(paramInt2)) / 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int as8BitChannel(float paramFloat) {
/* 268 */     return Mth.floor(paramFloat * 255.0F);
/*     */   }
/*     */   
/*     */   public static float alphaFloat(int paramInt) {
/* 272 */     return from8BitChannel(alpha(paramInt));
/*     */   }
/*     */   
/*     */   public static float redFloat(int paramInt) {
/* 276 */     return from8BitChannel(red(paramInt));
/*     */   }
/*     */   
/*     */   public static float greenFloat(int paramInt) {
/* 280 */     return from8BitChannel(green(paramInt));
/*     */   }
/*     */   
/*     */   public static float blueFloat(int paramInt) {
/* 284 */     return from8BitChannel(blue(paramInt));
/*     */   }
/*     */   
/*     */   private static float from8BitChannel(int paramInt) {
/* 288 */     return paramInt / 255.0F;
/*     */   }
/*     */   
/*     */   public static int toABGR(int paramInt) {
/* 292 */     return paramInt & 0xFF00FF00 | (paramInt & 0xFF0000) >> 16 | (paramInt & 0xFF) << 16;
/*     */   }
/*     */   
/*     */   public static int fromABGR(int paramInt) {
/* 296 */     return toABGR(paramInt);
/*     */   }
/*     */   public static int setBrightness(int paramInt, float paramFloat) {
/*     */     float f2, f3;
/* 300 */     int i = red(paramInt);
/* 301 */     int j = green(paramInt);
/* 302 */     int k = blue(paramInt);
/* 303 */     int m = alpha(paramInt);
/*     */ 
/*     */     
/* 306 */     int n = Math.max(Math.max(i, j), k);
/* 307 */     int i1 = Math.min(Math.min(i, j), k);
/* 308 */     float f1 = (n - i1);
/*     */ 
/*     */     
/* 311 */     if (n != 0) {
/* 312 */       f2 = f1 / n;
/*     */     } else {
/* 314 */       f2 = 0.0F;
/*     */     } 
/*     */ 
/*     */     
/* 318 */     if (f2 == 0.0F) {
/* 319 */       f3 = 0.0F;
/*     */     } else {
/* 321 */       float f9 = (n - i) / f1;
/* 322 */       float f10 = (n - j) / f1;
/* 323 */       float f11 = (n - k) / f1;
/*     */       
/* 325 */       if (i == n) {
/* 326 */         f3 = f11 - f10;
/* 327 */       } else if (j == n) {
/* 328 */         f3 = 2.0F + f9 - f11;
/*     */       } else {
/* 330 */         f3 = 4.0F + f10 - f9;
/*     */       } 
/*     */       
/* 333 */       f3 /= 6.0F;
/*     */       
/* 335 */       if (f3 < 0.0F) {
/* 336 */         f3++;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 341 */     if (f2 == 0.0F) {
/* 342 */       i = j = k = Math.round(paramFloat * 255.0F);
/* 343 */       return color(m, i, j, k);
/*     */     } 
/*     */     
/* 346 */     float f4 = (f3 - (float)Math.floor(f3)) * 6.0F;
/* 347 */     float f5 = f4 - (float)Math.floor(f4);
/* 348 */     float f6 = paramFloat * (1.0F - f2);
/* 349 */     float f7 = paramFloat * (1.0F - f2 * f5);
/* 350 */     float f8 = paramFloat * (1.0F - f2 * (1.0F - f5));
/*     */     
/* 352 */     switch ((int)f4) {
/*     */       case 0:
/* 354 */         i = Math.round(paramFloat * 255.0F);
/* 355 */         j = Math.round(f8 * 255.0F);
/* 356 */         k = Math.round(f6 * 255.0F);
/*     */         break;
/*     */       case 1:
/* 359 */         i = Math.round(f7 * 255.0F);
/* 360 */         j = Math.round(paramFloat * 255.0F);
/* 361 */         k = Math.round(f6 * 255.0F);
/*     */         break;
/*     */       case 2:
/* 364 */         i = Math.round(f6 * 255.0F);
/* 365 */         j = Math.round(paramFloat * 255.0F);
/* 366 */         k = Math.round(f8 * 255.0F);
/*     */         break;
/*     */       case 3:
/* 369 */         i = Math.round(f6 * 255.0F);
/* 370 */         j = Math.round(f7 * 255.0F);
/* 371 */         k = Math.round(paramFloat * 255.0F);
/*     */         break;
/*     */       case 4:
/* 374 */         i = Math.round(f8 * 255.0F);
/* 375 */         j = Math.round(f6 * 255.0F);
/* 376 */         k = Math.round(paramFloat * 255.0F);
/*     */         break;
/*     */       case 5:
/* 379 */         i = Math.round(paramFloat * 255.0F);
/* 380 */         j = Math.round(f6 * 255.0F);
/* 381 */         k = Math.round(f7 * 255.0F);
/*     */         break;
/*     */     } 
/*     */     
/* 385 */     return color(m, i, j, k);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\ARGB.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */