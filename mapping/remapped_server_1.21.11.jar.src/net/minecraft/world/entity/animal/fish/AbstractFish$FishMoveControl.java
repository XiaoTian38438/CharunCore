/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ 
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
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
/*     */ class FishMoveControl
/*     */   extends MoveControl
/*     */ {
/*     */   private final AbstractFish fish;
/*     */   
/*     */   FishMoveControl(AbstractFish paramAbstractFish) {
/* 179 */     super((Mob)paramAbstractFish);
/* 180 */     this.fish = paramAbstractFish;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 185 */     if (this.fish.isEyeInFluid(FluidTags.WATER))
/*     */     {
/* 187 */       this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
/*     */     }
/*     */     
/* 190 */     if (this.operation != MoveControl.Operation.MOVE_TO || this.fish.getNavigation().isDone()) {
/* 191 */       this.fish.setSpeed(0.0F);
/*     */       
/*     */       return;
/*     */     } 
/* 195 */     float f = (float)(this.speedModifier * this.fish.getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 196 */     this.fish.setSpeed(Mth.lerp(0.125F, this.fish.getSpeed(), f));
/*     */     
/* 198 */     double d1 = this.wantedX - this.fish.getX();
/* 199 */     double d2 = this.wantedY - this.fish.getY();
/* 200 */     double d3 = this.wantedZ - this.fish.getZ();
/*     */     
/* 202 */     if (d2 != 0.0D) {
/* 203 */       double d = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
/*     */       
/* 205 */       this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D, this.fish.getSpeed() * d2 / d * 0.1D, 0.0D));
/*     */     } 
/*     */     
/* 208 */     if (d1 != 0.0D || d3 != 0.0D) {
/* 209 */       float f1 = (float)(Mth.atan2(d3, d1) * 57.2957763671875D) - 90.0F;
/*     */       
/* 211 */       this.fish.setYRot(rotlerp(this.fish.getYRot(), f1, 90.0F));
/* 212 */       this.fish.yBodyRot = this.fish.getYRot();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\fish\AbstractFish$FishMoveControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */