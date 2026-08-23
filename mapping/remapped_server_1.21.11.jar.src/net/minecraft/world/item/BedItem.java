/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BedItem extends BlockItem {
/*    */   public BedItem(Block paramBlock, Item.Properties paramProperties) {
/*  9 */     super(paramBlock, paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean placeBlock(BlockPlaceContext paramBlockPlaceContext, BlockState paramBlockState) {
/* 14 */     return paramBlockPlaceContext.getLevel().setBlock(paramBlockPlaceContext.getClickedPos(), paramBlockState, 26);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\BedItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */