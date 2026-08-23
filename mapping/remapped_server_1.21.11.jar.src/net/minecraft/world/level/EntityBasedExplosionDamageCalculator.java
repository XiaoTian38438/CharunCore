/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class EntityBasedExplosionDamageCalculator
/*    */   extends ExplosionDamageCalculator {
/*    */   private final Entity source;
/*    */   
/*    */   public EntityBasedExplosionDamageCalculator(Entity paramEntity) {
/* 14 */     this.source = paramEntity;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Float> getBlockExplosionResistance(Explosion paramExplosion, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState) {
/* 19 */     return super.getBlockExplosionResistance(paramExplosion, paramBlockGetter, paramBlockPos, paramBlockState, paramFluidState).map(paramFloat -> Float.valueOf(this.source.getBlockExplosionResistance(paramExplosion, paramBlockGetter, paramBlockPos, paramBlockState, paramFluidState, paramFloat.floatValue())));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldBlockExplode(Explosion paramExplosion, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, float paramFloat) {
/* 24 */     return this.source.shouldBlockExplode(paramExplosion, paramBlockGetter, paramBlockPos, paramBlockState, paramFloat);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\EntityBasedExplosionDamageCalculator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */