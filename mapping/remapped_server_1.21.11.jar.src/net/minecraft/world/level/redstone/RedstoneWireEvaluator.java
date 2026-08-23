/*    */ package net.minecraft.world.level.redstone;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.RedStoneWireBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public abstract class RedstoneWireEvaluator {
/*    */   protected RedstoneWireEvaluator(RedStoneWireBlock paramRedStoneWireBlock) {
/* 14 */     this.wireBlock = paramRedStoneWireBlock;
/*    */   }
/*    */   protected final RedStoneWireBlock wireBlock;
/*    */   public abstract void updatePowerStrength(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Orientation paramOrientation, boolean paramBoolean);
/*    */   
/*    */   protected int getBlockSignal(Level paramLevel, BlockPos paramBlockPos) {
/* 20 */     return this.wireBlock.getBlockSignal(paramLevel, paramBlockPos);
/*    */   }
/*    */   
/*    */   protected int getWireSignal(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 24 */     return paramBlockState.is((Block)this.wireBlock) ? ((Integer)paramBlockState.getValue((Property)RedStoneWireBlock.POWER)).intValue() : 0;
/*    */   }
/*    */   
/*    */   protected int getIncomingWireSignal(Level paramLevel, BlockPos paramBlockPos) {
/* 28 */     int i = 0;
/* 29 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 30 */       BlockPos blockPos1 = paramBlockPos.relative(direction);
/* 31 */       BlockState blockState = paramLevel.getBlockState(blockPos1);
/*    */       
/* 33 */       i = Math.max(i, getWireSignal(blockPos1, blockState));
/*    */       
/* 35 */       BlockPos blockPos2 = paramBlockPos.above();
/* 36 */       if (blockState.isRedstoneConductor((BlockGetter)paramLevel, blockPos1) && !paramLevel.getBlockState(blockPos2).isRedstoneConductor((BlockGetter)paramLevel, blockPos2)) {
/* 37 */         BlockPos blockPos = blockPos1.above();
/* 38 */         i = Math.max(i, getWireSignal(blockPos, paramLevel.getBlockState(blockPos))); continue;
/* 39 */       }  if (!blockState.isRedstoneConductor((BlockGetter)paramLevel, blockPos1)) {
/* 40 */         BlockPos blockPos = blockPos1.below();
/* 41 */         i = Math.max(i, getWireSignal(blockPos, paramLevel.getBlockState(blockPos)));
/*    */       } 
/*    */     } 
/* 44 */     return Math.max(0, i - 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\redstone\RedstoneWireEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */