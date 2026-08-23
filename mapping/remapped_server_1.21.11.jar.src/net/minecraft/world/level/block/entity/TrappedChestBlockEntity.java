/*    */ package net.minecraft.world.level.block.entity;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.TrappedChestBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*    */ import net.minecraft.world.level.redstone.Orientation;
/*    */ 
/*    */ public class TrappedChestBlockEntity extends ChestBlockEntity {
/*    */   public TrappedChestBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 14 */     super(BlockEntityType.TRAPPED_CHEST, paramBlockPos, paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void signalOpenCount(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, int paramInt1, int paramInt2) {
/* 19 */     super.signalOpenCount(paramLevel, paramBlockPos, paramBlockState, paramInt1, paramInt2);
/* 20 */     if (paramInt1 != paramInt2) {
/* 21 */       Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(paramLevel, ((Direction)paramBlockState.getValue((Property)TrappedChestBlock.FACING)).getOpposite(), Direction.UP);
/* 22 */       Block block = paramBlockState.getBlock();
/* 23 */       paramLevel.updateNeighborsAt(paramBlockPos, block, orientation);
/* 24 */       paramLevel.updateNeighborsAt(paramBlockPos.below(), block, orientation);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\TrappedChestBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */