/*    */ package net.minecraft.world.entity.item;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Explosion;
/*    */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
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
/*    */ 
/*    */ class null
/*    */   extends ExplosionDamageCalculator
/*    */ {
/*    */   public boolean shouldBlockExplode(Explosion paramExplosion, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, float paramFloat) {
/* 46 */     if (paramBlockState.is(Blocks.NETHER_PORTAL)) {
/* 47 */       return false;
/*    */     }
/* 49 */     return super.shouldBlockExplode(paramExplosion, paramBlockGetter, paramBlockPos, paramBlockState, paramFloat);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Float> getBlockExplosionResistance(Explosion paramExplosion, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState) {
/* 54 */     if (paramBlockState.is(Blocks.NETHER_PORTAL)) {
/* 55 */       return Optional.empty();
/*    */     }
/* 57 */     return super.getBlockExplosionResistance(paramExplosion, paramBlockGetter, paramBlockPos, paramBlockState, paramFluidState);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\item\PrimedTnt$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */