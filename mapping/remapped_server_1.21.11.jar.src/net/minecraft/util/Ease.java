/*     */ package net.minecraft.util;
/*     */ 
/*     */ public class Ease
/*     */ {
/*     */   public static float inBack(float paramFloat) {
/*   6 */     float f1 = 1.70158F;
/*   7 */     float f2 = 2.70158F;
/*   8 */     return Mth.square(paramFloat) * (2.70158F * paramFloat - 1.70158F);
/*     */   }
/*     */   
/*     */   public static float inBounce(float paramFloat) {
/*  12 */     return 1.0F - outBounce(1.0F - paramFloat);
/*     */   }
/*     */   
/*     */   public static float inCubic(float paramFloat) {
/*  16 */     return Mth.cube(paramFloat);
/*     */   }
/*     */   
/*     */   public static float inElastic(float paramFloat) {
/*  20 */     if (paramFloat == 0.0F) {
/*  21 */       return 0.0F;
/*     */     }
/*  23 */     if (paramFloat == 1.0F) {
/*  24 */       return 1.0F;
/*     */     }
/*  26 */     float f = 2.0943952F;
/*  27 */     return (float)(-Math.pow(2.0D, 10.0D * paramFloat - 10.0D) * Math.sin((paramFloat * 10.0D - 10.75D) * 2.094395160675049D));
/*     */   }
/*     */   
/*     */   public static float inExpo(float paramFloat) {
/*  31 */     return (paramFloat == 0.0F) ? 0.0F : (float)Math.pow(2.0D, 10.0D * paramFloat - 10.0D);
/*     */   }
/*     */   
/*     */   public static float inQuart(float paramFloat) {
/*  35 */     return Mth.square(Mth.square(paramFloat));
/*     */   }
/*     */   
/*     */   public static float inQuint(float paramFloat) {
/*  39 */     return Mth.square(Mth.square(paramFloat)) * paramFloat;
/*     */   }
/*     */   
/*     */   public static float inSine(float paramFloat) {
/*  43 */     return 1.0F - Mth.cos((paramFloat * 1.5707964F));
/*     */   }
/*     */   
/*     */   public static float inOutBounce(float paramFloat) {
/*  47 */     if (paramFloat < 0.5F) {
/*  48 */       return (1.0F - outBounce(1.0F - 2.0F * paramFloat)) / 2.0F;
/*     */     }
/*  50 */     return (1.0F + outBounce(2.0F * paramFloat - 1.0F)) / 2.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float inOutCirc(float paramFloat) {
/*  55 */     if (paramFloat < 0.5F) {
/*  56 */       return (float)((1.0D - Math.sqrt(1.0D - Math.pow(2.0D * paramFloat, 2.0D))) / 2.0D);
/*     */     }
/*  58 */     return (float)((Math.sqrt(1.0D - Math.pow(-2.0D * paramFloat + 2.0D, 2.0D)) + 1.0D) / 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float inOutCubic(float paramFloat) {
/*  63 */     if (paramFloat < 0.5F) {
/*  64 */       return 4.0F * Mth.cube(paramFloat);
/*     */     }
/*  66 */     return (float)(1.0D - Math.pow(-2.0D * paramFloat + 2.0D, 3.0D) / 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float inOutQuad(float paramFloat) {
/*  71 */     if (paramFloat < 0.5F) {
/*  72 */       return 2.0F * Mth.square(paramFloat);
/*     */     }
/*  74 */     return (float)(1.0D - Math.pow(-2.0D * paramFloat + 2.0D, 2.0D) / 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float inOutQuart(float paramFloat) {
/*  79 */     if (paramFloat < 0.5F) {
/*  80 */       return 8.0F * Mth.square(Mth.square(paramFloat));
/*     */     }
/*  82 */     return (float)(1.0D - Math.pow(-2.0D * paramFloat + 2.0D, 4.0D) / 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float inOutQuint(float paramFloat) {
/*  87 */     if (paramFloat < 0.5D) {
/*  88 */       return 16.0F * paramFloat * paramFloat * paramFloat * paramFloat * paramFloat;
/*     */     }
/*  90 */     return (float)(1.0D - Math.pow(-2.0D * paramFloat + 2.0D, 5.0D) / 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float outBounce(float paramFloat) {
/*  95 */     float f1 = 7.5625F;
/*  96 */     float f2 = 2.75F;
/*  97 */     if (paramFloat < 0.36363637F)
/*  98 */       return 7.5625F * Mth.square(paramFloat); 
/*  99 */     if (paramFloat < 0.72727275F)
/* 100 */       return 7.5625F * Mth.square(paramFloat - 0.54545456F) + 0.75F; 
/* 101 */     if (paramFloat < 0.9090909090909091D) {
/* 102 */       return 7.5625F * Mth.square(paramFloat - 0.8181818F) + 0.9375F;
/*     */     }
/* 104 */     return 7.5625F * Mth.square(paramFloat - 0.95454544F) + 0.984375F;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float outElastic(float paramFloat) {
/* 109 */     float f = 2.0943952F;
/* 110 */     if (paramFloat == 0.0F) {
/* 111 */       return 0.0F;
/*     */     }
/* 113 */     if (paramFloat == 1.0F) {
/* 114 */       return 1.0F;
/*     */     }
/* 116 */     return (float)(Math.pow(2.0D, -10.0D * paramFloat) * Math.sin((paramFloat * 10.0D - 0.75D) * 2.094395160675049D) + 1.0D);
/*     */   }
/*     */   
/*     */   public static float outExpo(float paramFloat) {
/* 120 */     if (paramFloat == 1.0F) {
/* 121 */       return 1.0F;
/*     */     }
/* 123 */     return 1.0F - (float)Math.pow(2.0D, -10.0D * paramFloat);
/*     */   }
/*     */   
/*     */   public static float outQuad(float paramFloat) {
/* 127 */     return 1.0F - Mth.square(1.0F - paramFloat);
/*     */   }
/*     */   
/*     */   public static float outQuint(float paramFloat) {
/* 131 */     return 1.0F - (float)Math.pow(1.0D - paramFloat, 5.0D);
/*     */   }
/*     */   
/*     */   public static float outSine(float paramFloat) {
/* 135 */     return Mth.sin((paramFloat * 1.5707964F));
/*     */   }
/*     */   
/*     */   public static float inOutSine(float paramFloat) {
/* 139 */     return -(Mth.cos((3.1415927F * paramFloat)) - 1.0F) / 2.0F;
/*     */   }
/*     */   
/*     */   public static float outBack(float paramFloat) {
/* 143 */     float f1 = 1.70158F;
/* 144 */     float f2 = 2.70158F;
/* 145 */     return 1.0F + 2.70158F * Mth.cube(paramFloat - 1.0F) + 1.70158F * Mth.square(paramFloat - 1.0F);
/*     */   }
/*     */   
/*     */   public static float outQuart(float paramFloat) {
/* 149 */     return 1.0F - Mth.square(Mth.square(1.0F - paramFloat));
/*     */   }
/*     */   
/*     */   public static float outCubic(float paramFloat) {
/* 153 */     return 1.0F - Mth.cube(1.0F - paramFloat);
/*     */   }
/*     */   
/*     */   public static float inOutExpo(float paramFloat) {
/* 157 */     if (paramFloat < 0.5F) {
/* 158 */       return (paramFloat == 0.0F) ? 0.0F : (float)(Math.pow(2.0D, 20.0D * paramFloat - 10.0D) / 2.0D);
/*     */     }
/* 160 */     return (paramFloat == 1.0F) ? 1.0F : (float)((2.0D - Math.pow(2.0D, -20.0D * paramFloat + 10.0D)) / 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float inQuad(float paramFloat) {
/* 165 */     return paramFloat * paramFloat;
/*     */   }
/*     */   
/*     */   public static float outCirc(float paramFloat) {
/* 169 */     return (float)Math.sqrt((1.0F - Mth.square(paramFloat - 1.0F)));
/*     */   }
/*     */   
/*     */   public static float inOutElastic(float paramFloat) {
/* 173 */     float f = 1.3962635F;
/* 174 */     if (paramFloat == 0.0F) {
/* 175 */       return 0.0F;
/*     */     }
/* 177 */     if (paramFloat == 1.0F) {
/* 178 */       return 1.0F;
/*     */     }
/* 180 */     double d = Math.sin((20.0D * paramFloat - 11.125D) * 1.3962634801864624D);
/* 181 */     if (paramFloat < 0.5F) {
/* 182 */       return (float)(-(Math.pow(2.0D, 20.0D * paramFloat - 10.0D) * d) / 2.0D);
/*     */     }
/* 184 */     return (float)(Math.pow(2.0D, -20.0D * paramFloat + 10.0D) * d / 2.0D + 1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float inCirc(float paramFloat) {
/* 189 */     return (float)-Math.sqrt((1.0F - paramFloat * paramFloat)) + 1.0F;
/*     */   }
/*     */   
/*     */   public static float inOutBack(float paramFloat) {
/* 193 */     float f1 = 1.70158F;
/* 194 */     float f2 = 2.5949094F;
/* 195 */     if (paramFloat < 0.5F) {
/* 196 */       return 4.0F * paramFloat * paramFloat * (7.189819F * paramFloat - 2.5949094F) / 2.0F;
/*     */     }
/* 198 */     float f3 = 2.0F * paramFloat - 2.0F;
/* 199 */     return (f3 * f3 * (3.5949094F * f3 + 2.5949094F) + 2.0F) / 2.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\Ease.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */