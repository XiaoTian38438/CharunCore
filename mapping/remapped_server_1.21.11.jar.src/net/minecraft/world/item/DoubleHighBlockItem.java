/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DoubleHighBlockItem extends BlockItem {
/*    */   public DoubleHighBlockItem(Block paramBlock, Item.Properties paramProperties) {
/* 12 */     super(paramBlock, paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean placeBlock(BlockPlaceContext paramBlockPlaceContext, BlockState paramBlockState) {
/* 17 */     Level level = paramBlockPlaceContext.getLevel();
/* 18 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos().above();
/* 19 */     BlockState blockState = level.isWaterAt(blockPos) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
/* 20 */     level.setBlock(blockPos, blockState, 27);
/* 21 */     return super.placeBlock(paramBlockPlaceContext, paramBlockState);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\DoubleHighBlockItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */