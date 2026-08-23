/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.Fluids;
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
/*    */   implements SculkBehaviour
/*    */ {
/*    */   public boolean attemptSpreadVein(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState, Collection<Direction> paramCollection, boolean paramBoolean) {
/* 42 */     if (paramCollection == null) {
/* 43 */       return (((SculkVeinBlock)Blocks.SCULK_VEIN).getSameSpaceSpreader().spreadAll(paramLevelAccessor.getBlockState(paramBlockPos), paramLevelAccessor, paramBlockPos, paramBoolean) > 0L);
/*    */     }
/* 45 */     if (!paramCollection.isEmpty()) {
/* 46 */       if (paramBlockState.isAir() || paramBlockState.getFluidState().is((Fluid)Fluids.WATER)) {
/* 47 */         return SculkVeinBlock.regrow(paramLevelAccessor, paramBlockPos, paramBlockState, paramCollection);
/*    */       }
/* 49 */       return false;
/*    */     } 
/* 51 */     return super.attemptSpreadVein(paramLevelAccessor, paramBlockPos, paramBlockState, paramCollection, paramBoolean);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int attemptUseCharge(SculkSpreader.ChargeCursor paramChargeCursor, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, RandomSource paramRandomSource, SculkSpreader paramSculkSpreader, boolean paramBoolean) {
/* 57 */     return (paramChargeCursor.getDecayDelay() > 0) ? paramChargeCursor.getCharge() : 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public int updateDecayDelay(int paramInt) {
/* 62 */     return Math.max(paramInt - 1, 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SculkBehaviour$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */