/*    */ package net.minecraft.world.level.redstone;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class InstantNeighborUpdater
/*    */   implements NeighborUpdater {
/*    */   public InstantNeighborUpdater(Level paramLevel) {
/* 14 */     this.level = paramLevel;
/*    */   }
/*    */   private final Level level;
/*    */   
/*    */   public void shapeUpdate(Direction paramDirection, BlockState paramBlockState, BlockPos paramBlockPos1, BlockPos paramBlockPos2, @UpdateFlags int paramInt1, int paramInt2) {
/* 19 */     NeighborUpdater.executeShapeUpdate((LevelAccessor)this.level, paramDirection, paramBlockPos1, paramBlockPos2, paramBlockState, paramInt1, paramInt2 - 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void neighborChanged(BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation) {
/* 24 */     BlockState blockState = this.level.getBlockState(paramBlockPos);
/* 25 */     neighborChanged(blockState, paramBlockPos, paramBlock, paramOrientation, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void neighborChanged(BlockState paramBlockState, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 30 */     NeighborUpdater.executeUpdate(this.level, paramBlockState, paramBlockPos, paramBlock, paramOrientation, paramBoolean);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\redstone\InstantNeighborUpdater.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */