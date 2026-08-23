/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.SignBlock;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SignItem extends StandingAndWallBlockItem {
/*    */   public SignItem(Block paramBlock1, Block paramBlock2, Item.Properties paramProperties) {
/* 15 */     super(paramBlock1, paramBlock2, Direction.DOWN, paramProperties);
/*    */   }
/*    */   
/*    */   public SignItem(Item.Properties paramProperties, Block paramBlock1, Block paramBlock2, Direction paramDirection) {
/* 19 */     super(paramBlock1, paramBlock2, paramDirection, paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean updateCustomBlockEntityTag(BlockPos paramBlockPos, Level paramLevel, Player paramPlayer, ItemStack paramItemStack, BlockState paramBlockState) {
/* 24 */     boolean bool = super.updateCustomBlockEntityTag(paramBlockPos, paramLevel, paramPlayer, paramItemStack, paramBlockState);
/*    */     
/* 26 */     if (!paramLevel.isClientSide() && !bool && paramPlayer != null) { BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof SignBlockEntity) { SignBlockEntity signBlockEntity = (SignBlockEntity)blockEntity;
/* 27 */         Block block = paramLevel.getBlockState(paramBlockPos).getBlock(); if (block instanceof SignBlock) { SignBlock signBlock = (SignBlock)block;
/* 28 */           signBlock.openTextEdit(paramPlayer, signBlockEntity, true); }
/*    */          }
/*    */        }
/* 31 */      return bool;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\SignItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */