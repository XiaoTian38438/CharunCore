/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import java.util.Optional;
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
/*     */ 
/*     */ public class Builder
/*     */ {
/*  79 */   private Optional<Boolean> isOnGround = Optional.empty();
/*  80 */   private Optional<Boolean> isOnFire = Optional.empty();
/*  81 */   private Optional<Boolean> isCrouching = Optional.empty();
/*  82 */   private Optional<Boolean> isSprinting = Optional.empty();
/*  83 */   private Optional<Boolean> isSwimming = Optional.empty();
/*  84 */   private Optional<Boolean> isFlying = Optional.empty();
/*  85 */   private Optional<Boolean> isBaby = Optional.empty();
/*  86 */   private Optional<Boolean> isInWater = Optional.empty();
/*  87 */   private Optional<Boolean> isFallFlying = Optional.empty();
/*     */   
/*     */   public static Builder flags() {
/*  90 */     return new Builder();
/*     */   }
/*     */   
/*     */   public Builder setOnGround(Boolean paramBoolean) {
/*  94 */     this.isOnGround = Optional.of(paramBoolean);
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setOnFire(Boolean paramBoolean) {
/*  99 */     this.isOnFire = Optional.of(paramBoolean);
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setCrouching(Boolean paramBoolean) {
/* 104 */     this.isCrouching = Optional.of(paramBoolean);
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setSprinting(Boolean paramBoolean) {
/* 109 */     this.isSprinting = Optional.of(paramBoolean);
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setSwimming(Boolean paramBoolean) {
/* 114 */     this.isSwimming = Optional.of(paramBoolean);
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setIsFlying(Boolean paramBoolean) {
/* 119 */     this.isFlying = Optional.of(paramBoolean);
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setIsBaby(Boolean paramBoolean) {
/* 124 */     this.isBaby = Optional.of(paramBoolean);
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setIsInWater(Boolean paramBoolean) {
/* 129 */     this.isInWater = Optional.of(paramBoolean);
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setIsFallFlying(Boolean paramBoolean) {
/* 134 */     this.isFallFlying = Optional.of(paramBoolean);
/* 135 */     return this;
/*     */   }
/*     */   
/*     */   public EntityFlagsPredicate build() {
/* 139 */     return new EntityFlagsPredicate(this.isOnGround, this.isOnFire, this.isCrouching, this.isSprinting, this.isSwimming, this.isFlying, this.isBaby, this.isInWater, this.isFallFlying);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\EntityFlagsPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */