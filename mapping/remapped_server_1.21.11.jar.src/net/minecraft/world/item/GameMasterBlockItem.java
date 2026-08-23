/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class GameMasterBlockItem
/*    */   extends BlockItem {
/*    */   public GameMasterBlockItem(Block paramBlock, Item.Properties paramProperties) {
/* 11 */     super(paramBlock, paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState getPlacementState(BlockPlaceContext paramBlockPlaceContext) {
/* 16 */     Player player = paramBlockPlaceContext.getPlayer();
/* 17 */     return (player == null || player.canUseGameMasterBlocks()) ? super.getPlacementState(paramBlockPlaceContext) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\GameMasterBlockItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */