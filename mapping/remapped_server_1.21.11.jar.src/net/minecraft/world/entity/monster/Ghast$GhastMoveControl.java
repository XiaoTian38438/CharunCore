/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
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
/*     */ public class GhastMoveControl
/*     */   extends MoveControl
/*     */ {
/*     */   private final Mob ghast;
/*     */   private int floatDuration;
/*     */   private final boolean careful;
/*     */   private final BooleanSupplier shouldBeStopped;
/*     */   
/*     */   public GhastMoveControl(Mob paramMob, boolean paramBoolean, BooleanSupplier paramBooleanSupplier) {
/* 210 */     super(paramMob);
/* 211 */     this.ghast = paramMob;
/* 212 */     this.careful = paramBoolean;
/* 213 */     this.shouldBeStopped = paramBooleanSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 218 */     if (this.shouldBeStopped.getAsBoolean()) {
/* 219 */       this.operation = MoveControl.Operation.WAIT;
/* 220 */       this.ghast.stopInPlace();
/*     */     } 
/*     */     
/* 223 */     if (this.operation != MoveControl.Operation.MOVE_TO) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 228 */     if (this.floatDuration-- <= 0) {
/* 229 */       this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 234 */       Vec3 vec3 = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
/*     */ 
/*     */       
/* 237 */       if (canReach(vec3)) {
/* 238 */         this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(vec3.normalize().scale(this.ghast.getAttributeValue(Attributes.FLYING_SPEED) * 5.0D / 3.0D)));
/*     */       } else {
/* 240 */         this.operation = MoveControl.Operation.WAIT;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canReach(Vec3 paramVec3) {
/* 246 */     AABB aABB1 = this.ghast.getBoundingBox();
/* 247 */     AABB aABB2 = aABB1.move(paramVec3);
/* 248 */     if (this.careful) {
/* 249 */       for (BlockPos blockPos : BlockPos.betweenClosed(aABB2.inflate(1.0D))) {
/* 250 */         if (!blockTraversalPossible((BlockGetter)this.ghast.level(), (Vec3)null, (Vec3)null, blockPos, false, false)) {
/* 251 */           return false;
/*     */         }
/*     */       } 
/*     */     }
/* 255 */     boolean bool1 = this.ghast.isInWater();
/* 256 */     boolean bool2 = this.ghast.isInLava();
/* 257 */     Vec3 vec31 = this.ghast.position();
/* 258 */     Vec3 vec32 = vec31.add(paramVec3);
/*     */     
/* 260 */     return BlockGetter.forEachBlockIntersectedBetween(vec31, vec32, aABB2, (paramBlockPos, paramInt) -> paramAABB.intersects(paramBlockPos) ? true : blockTraversalPossible((BlockGetter)this.ghast.level(), paramVec31, paramVec32, paramBlockPos, paramBoolean1, paramBoolean2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean blockTraversalPossible(BlockGetter paramBlockGetter, Vec3 paramVec31, Vec3 paramVec32, BlockPos paramBlockPos, boolean paramBoolean1, boolean paramBoolean2) {
/* 269 */     BlockState blockState = paramBlockGetter.getBlockState(paramBlockPos);
/* 270 */     if (blockState.isAir()) {
/* 271 */       return true;
/*     */     }
/* 273 */     boolean bool1 = (paramVec31 != null && paramVec32 != null) ? true : false;
/* 274 */     boolean bool2 = bool1 ? (!this.ghast.collidedWithShapeMovingFrom(paramVec31, paramVec32, blockState.getCollisionShape(paramBlockGetter, paramBlockPos).move(new Vec3((Vec3i)paramBlockPos)).toAabbs()) ? true : false) : blockState.getCollisionShape(paramBlockGetter, paramBlockPos).isEmpty();
/* 275 */     if (!this.careful) {
/* 276 */       return bool2;
/*     */     }
/* 278 */     if (blockState.is(BlockTags.HAPPY_GHAST_AVOIDS)) {
/* 279 */       return false;
/*     */     }
/* 281 */     FluidState fluidState = paramBlockGetter.getFluidState(paramBlockPos);
/* 282 */     if (!fluidState.isEmpty() && (!bool1 || this.ghast.collidedWithFluid(fluidState, paramBlockPos, paramVec31, paramVec32))) {
/* 283 */       if (fluidState.is(FluidTags.WATER)) {
/* 284 */         return paramBoolean1;
/*     */       }
/* 286 */       if (fluidState.is(FluidTags.LAVA)) {
/* 287 */         return paramBoolean2;
/*     */       }
/*     */     } 
/* 290 */     return bool2;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Ghast$GhastMoveControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */