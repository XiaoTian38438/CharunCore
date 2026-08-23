/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.WallHangingSignBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class HangingSignItem extends SignItem {
/*    */   public HangingSignItem(Block paramBlock1, Block paramBlock2, Item.Properties paramProperties) {
/* 12 */     super(paramProperties, paramBlock1, paramBlock2, Direction.UP);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canPlace(LevelReader paramLevelReader, BlockState paramBlockState, BlockPos paramBlockPos) {
/* 17 */     Block block = paramBlockState.getBlock(); if (block instanceof WallHangingSignBlock) { WallHangingSignBlock wallHangingSignBlock = (WallHangingSignBlock)block;
/* 18 */       if (!wallHangingSignBlock.canPlace(paramBlockState, paramLevelReader, paramBlockPos)) {
/* 19 */         return false;
/*    */       } }
/*    */     
/* 22 */     return super.canPlace(paramLevelReader, paramBlockState, paramBlockPos);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\HangingSignItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */