/*     */ package net.minecraft.world.food;
/*     */ 
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class FoodData
/*     */ {
/*     */   private static final int DEFAULT_TICK_TIMER = 0;
/*     */   private static final float DEFAULT_EXHAUSTION_LEVEL = 0.0F;
/*  15 */   private int foodLevel = 20;
/*  16 */   private float saturationLevel = 5.0F;
/*     */   
/*     */   private float exhaustionLevel;
/*     */   private int tickTimer;
/*     */   
/*     */   private void add(int paramInt, float paramFloat) {
/*  22 */     this.foodLevel = Mth.clamp(paramInt + this.foodLevel, 0, 20);
/*  23 */     this.saturationLevel = Mth.clamp(paramFloat + this.saturationLevel, 0.0F, this.foodLevel);
/*     */   }
/*     */   
/*     */   public void eat(int paramInt, float paramFloat) {
/*  27 */     add(paramInt, FoodConstants.saturationByModifier(paramInt, paramFloat));
/*     */   }
/*     */   
/*     */   public void eat(FoodProperties paramFoodProperties) {
/*  31 */     add(paramFoodProperties.nutrition(), paramFoodProperties.saturation());
/*     */   }
/*     */   
/*     */   public void tick(ServerPlayer paramServerPlayer) {
/*  35 */     ServerLevel serverLevel = paramServerPlayer.level();
/*  36 */     Difficulty difficulty = serverLevel.getDifficulty();
/*     */     
/*  38 */     if (this.exhaustionLevel > 4.0F) {
/*  39 */       this.exhaustionLevel -= 4.0F;
/*     */       
/*  41 */       if (this.saturationLevel > 0.0F) {
/*  42 */         this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
/*  43 */       } else if (difficulty != Difficulty.PEACEFUL) {
/*  44 */         this.foodLevel = Math.max(this.foodLevel - 1, 0);
/*     */       } 
/*     */     } 
/*     */     
/*  48 */     boolean bool = ((Boolean)serverLevel.getGameRules().get(GameRules.NATURAL_HEALTH_REGENERATION)).booleanValue();
/*  49 */     if (bool && this.saturationLevel > 0.0F && paramServerPlayer.isHurt() && this.foodLevel >= 20) {
/*  50 */       this.tickTimer++;
/*  51 */       if (this.tickTimer >= 10) {
/*  52 */         float f = Math.min(this.saturationLevel, 6.0F);
/*  53 */         paramServerPlayer.heal(f / 6.0F);
/*  54 */         addExhaustion(f);
/*  55 */         this.tickTimer = 0;
/*     */       } 
/*  57 */     } else if (bool && this.foodLevel >= 18 && paramServerPlayer.isHurt()) {
/*  58 */       this.tickTimer++;
/*  59 */       if (this.tickTimer >= 80) {
/*  60 */         paramServerPlayer.heal(1.0F);
/*  61 */         addExhaustion(6.0F);
/*  62 */         this.tickTimer = 0;
/*     */       } 
/*  64 */     } else if (this.foodLevel <= 0) {
/*  65 */       this.tickTimer++;
/*  66 */       if (this.tickTimer >= 80) {
/*  67 */         if (paramServerPlayer.getHealth() > 10.0F || difficulty == Difficulty.HARD || (paramServerPlayer.getHealth() > 1.0F && difficulty == Difficulty.NORMAL)) {
/*  68 */           paramServerPlayer.hurtServer(serverLevel, paramServerPlayer.damageSources().starve(), 1.0F);
/*     */         }
/*  70 */         this.tickTimer = 0;
/*     */       } 
/*     */     } else {
/*  73 */       this.tickTimer = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void readAdditionalSaveData(ValueInput paramValueInput) {
/*  78 */     this.foodLevel = paramValueInput.getIntOr("foodLevel", 20);
/*  79 */     this.tickTimer = paramValueInput.getIntOr("foodTickTimer", 0);
/*  80 */     this.saturationLevel = paramValueInput.getFloatOr("foodSaturationLevel", 5.0F);
/*  81 */     this.exhaustionLevel = paramValueInput.getFloatOr("foodExhaustionLevel", 0.0F);
/*     */   }
/*     */   
/*     */   public void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  85 */     paramValueOutput.putInt("foodLevel", this.foodLevel);
/*  86 */     paramValueOutput.putInt("foodTickTimer", this.tickTimer);
/*  87 */     paramValueOutput.putFloat("foodSaturationLevel", this.saturationLevel);
/*  88 */     paramValueOutput.putFloat("foodExhaustionLevel", this.exhaustionLevel);
/*     */   }
/*     */   
/*     */   public int getFoodLevel() {
/*  92 */     return this.foodLevel;
/*     */   }
/*     */   
/*     */   public boolean hasEnoughFood() {
/*  96 */     return (getFoodLevel() > 6.0F);
/*     */   }
/*     */   
/*     */   public boolean needsFood() {
/* 100 */     return (this.foodLevel < 20);
/*     */   }
/*     */   
/*     */   public void addExhaustion(float paramFloat) {
/* 104 */     this.exhaustionLevel = Math.min(this.exhaustionLevel + paramFloat, 40.0F);
/*     */   }
/*     */   
/*     */   public float getSaturationLevel() {
/* 108 */     return this.saturationLevel;
/*     */   }
/*     */   
/*     */   public void setFoodLevel(int paramInt) {
/* 112 */     this.foodLevel = paramInt;
/*     */   }
/*     */   
/*     */   public void setSaturation(float paramFloat) {
/* 116 */     this.saturationLevel = paramFloat;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\food\FoodData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */