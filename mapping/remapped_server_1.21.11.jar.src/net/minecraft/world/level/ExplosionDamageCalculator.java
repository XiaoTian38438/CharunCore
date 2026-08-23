/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ExplosionDamageCalculator
/*    */ {
/*    */   public Optional<Float> getBlockExplosionResistance(Explosion paramExplosion, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState) {
/* 13 */     if (paramBlockState.isAir() && paramFluidState.isEmpty()) {
/* 14 */       return Optional.empty();
/*    */     }
/* 16 */     return Optional.of(Float.valueOf(Math.max(paramBlockState.getBlock().getExplosionResistance(), paramFluidState.getExplosionResistance())));
/*    */   }
/*    */   
/*    */   public boolean shouldBlockExplode(Explosion paramExplosion, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, float paramFloat) {
/* 20 */     return true;
/*    */   }
/*    */   
/*    */   public boolean shouldDamageEntity(Explosion paramExplosion, Entity paramEntity) {
/* 24 */     return true;
/*    */   }
/*    */   
/*    */   public float getKnockbackMultiplier(Entity paramEntity) {
/* 28 */     return 1.0F;
/*    */   }
/*    */   
/*    */   public float getEntityDamageAmount(Explosion paramExplosion, Entity paramEntity, float paramFloat) {
/* 32 */     float f = paramExplosion.radius() * 2.0F;
/* 33 */     Vec3 vec3 = paramExplosion.center();
/*    */     
/* 35 */     double d1 = Math.sqrt(paramEntity.distanceToSqr(vec3)) / f;
/* 36 */     double d2 = (1.0D - d1) * paramFloat;
/*    */     
/* 38 */     return (float)((d2 * d2 + d2) / 2.0D * 7.0D * f + 1.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\ExplosionDamageCalculator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */