/*    */ package net.minecraft.world;
/*    */ 
/*    */ import javax.annotation.concurrent.Immutable;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class DifficultyInstance
/*    */ {
/*    */   private static final float DIFFICULTY_TIME_GLOBAL_OFFSET = -72000.0F;
/*    */   private static final float MAX_DIFFICULTY_TIME_GLOBAL = 1440000.0F;
/*    */   private static final float MAX_DIFFICULTY_TIME_LOCAL = 3600000.0F;
/*    */   private final Difficulty base;
/*    */   private final float effectiveDifficulty;
/*    */   
/*    */   public DifficultyInstance(Difficulty paramDifficulty, long paramLong1, long paramLong2, float paramFloat) {
/* 22 */     this.base = paramDifficulty;
/* 23 */     this.effectiveDifficulty = calculateDifficulty(paramDifficulty, paramLong1, paramLong2, paramFloat);
/*    */   }
/*    */   
/*    */   public Difficulty getDifficulty() {
/* 27 */     return this.base;
/*    */   }
/*    */   
/*    */   public float getEffectiveDifficulty() {
/* 31 */     return this.effectiveDifficulty;
/*    */   }
/*    */   
/*    */   public boolean isHard() {
/* 35 */     return (this.effectiveDifficulty >= Difficulty.HARD.ordinal());
/*    */   }
/*    */   
/*    */   public boolean isHarderThan(float paramFloat) {
/* 39 */     return (this.effectiveDifficulty > paramFloat);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float getSpecialMultiplier() {
/* 52 */     if (this.effectiveDifficulty < 2.0F) {
/* 53 */       return 0.0F;
/*    */     }
/* 55 */     if (this.effectiveDifficulty > 4.0F) {
/* 56 */       return 1.0F;
/*    */     }
/* 58 */     return (this.effectiveDifficulty - 2.0F) / 2.0F;
/*    */   }
/*    */   
/*    */   private float calculateDifficulty(Difficulty paramDifficulty, long paramLong1, long paramLong2, float paramFloat) {
/* 62 */     if (paramDifficulty == Difficulty.PEACEFUL) {
/* 63 */       return 0.0F;
/*    */     }
/*    */     
/* 66 */     boolean bool = (paramDifficulty == Difficulty.HARD) ? true : false;
/* 67 */     float f1 = 0.75F;
/*    */ 
/*    */     
/* 70 */     float f2 = Mth.clamp(((float)paramLong1 + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;
/* 71 */     f1 += f2;
/*    */     
/* 73 */     float f3 = 0.0F;
/*    */ 
/*    */     
/* 76 */     f3 += Mth.clamp((float)paramLong2 / 3600000.0F, 0.0F, 1.0F) * (bool ? 1.0F : 0.75F);
/* 77 */     f3 += Mth.clamp(paramFloat * 0.25F, 0.0F, f2);
/*    */     
/* 79 */     if (paramDifficulty == Difficulty.EASY) {
/* 80 */       f3 *= 0.5F;
/*    */     }
/* 82 */     f1 += f3;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 87 */     return paramDifficulty.getId() * f1;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\DifficultyInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */