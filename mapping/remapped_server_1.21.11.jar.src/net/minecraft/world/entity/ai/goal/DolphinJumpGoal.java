/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.animal.dolphin.Dolphin;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class DolphinJumpGoal extends JumpGoal {
/*  13 */   private static final int[] STEPS_TO_CHECK = new int[] { 0, 1, 4, 5, 6, 7 };
/*     */   
/*     */   private final Dolphin dolphin;
/*     */   
/*     */   private final int interval;
/*     */   
/*     */   private boolean breached;
/*     */   
/*     */   public DolphinJumpGoal(Dolphin paramDolphin, int paramInt) {
/*  22 */     this.dolphin = paramDolphin;
/*  23 */     this.interval = reducedTickDelay(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  28 */     if (this.dolphin.getRandom().nextInt(this.interval) != 0) {
/*  29 */       return false;
/*     */     }
/*     */     
/*  32 */     Direction direction = this.dolphin.getMotionDirection();
/*  33 */     int i = direction.getStepX();
/*  34 */     int j = direction.getStepZ();
/*  35 */     BlockPos blockPos = this.dolphin.blockPosition();
/*     */     
/*  37 */     for (int k : STEPS_TO_CHECK) {
/*  38 */       if (!waterIsClear(blockPos, i, j, k) || !surfaceIsClear(blockPos, i, j, k)) {
/*  39 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  43 */     return true;
/*     */   }
/*     */   
/*     */   private boolean waterIsClear(BlockPos paramBlockPos, int paramInt1, int paramInt2, int paramInt3) {
/*  47 */     BlockPos blockPos = paramBlockPos.offset(paramInt1 * paramInt3, 0, paramInt2 * paramInt3);
/*     */     
/*  49 */     return (this.dolphin.level().getFluidState(blockPos).is(FluidTags.WATER) && !this.dolphin.level().getBlockState(blockPos).blocksMotion());
/*     */   }
/*     */   
/*     */   private boolean surfaceIsClear(BlockPos paramBlockPos, int paramInt1, int paramInt2, int paramInt3) {
/*  53 */     return (this.dolphin.level().getBlockState(paramBlockPos.offset(paramInt1 * paramInt3, 1, paramInt2 * paramInt3)).isAir() && this.dolphin
/*  54 */       .level().getBlockState(paramBlockPos.offset(paramInt1 * paramInt3, 2, paramInt2 * paramInt3)).isAir());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  59 */     double d = (this.dolphin.getDeltaMovement()).y;
/*  60 */     return ((d * d >= 0.029999999329447746D || this.dolphin.getXRot() == 0.0F || Math.abs(this.dolphin.getXRot()) >= 10.0F || !this.dolphin.isInWater()) && !this.dolphin.onGround());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterruptable() {
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  71 */     Direction direction = this.dolphin.getMotionDirection();
/*  72 */     this.dolphin.setDeltaMovement(this.dolphin.getDeltaMovement().add(direction
/*  73 */           .getStepX() * 0.6D, 0.7D, direction
/*     */           
/*  75 */           .getStepZ() * 0.6D));
/*     */ 
/*     */     
/*  78 */     this.dolphin.getNavigation().stop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  83 */     this.dolphin.setXRot(0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  88 */     boolean bool = this.breached;
/*  89 */     if (!bool) {
/*  90 */       FluidState fluidState = this.dolphin.level().getFluidState(this.dolphin.blockPosition());
/*  91 */       this.breached = fluidState.is(FluidTags.WATER);
/*     */     } 
/*     */     
/*  94 */     if (this.breached && !bool) {
/*  95 */       this.dolphin.playSound(SoundEvents.DOLPHIN_JUMP, 1.0F, 1.0F);
/*     */     }
/*     */     
/*  98 */     Vec3 vec3 = this.dolphin.getDeltaMovement();
/*  99 */     if (vec3.y * vec3.y < 0.029999999329447746D && this.dolphin.getXRot() != 0.0F) {
/* 100 */       this.dolphin.setXRot(Mth.rotLerp(0.2F, this.dolphin.getXRot(), 0.0F));
/* 101 */     } else if (vec3.length() > 9.999999747378752E-6D) {
/* 102 */       double d1 = vec3.horizontalDistance();
/* 103 */       double d2 = Math.atan2(-vec3.y, d1) * 57.2957763671875D;
/* 104 */       this.dolphin.setXRot((float)d2);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\DolphinJumpGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */