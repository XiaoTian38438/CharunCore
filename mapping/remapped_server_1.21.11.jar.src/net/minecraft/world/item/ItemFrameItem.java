/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.decoration.HangingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class ItemFrameItem extends HangingEntityItem {
/*    */   public ItemFrameItem(EntityType<? extends HangingEntity> paramEntityType, Item.Properties paramProperties) {
/* 11 */     super(paramEntityType, paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlace(Player paramPlayer, Direction paramDirection, ItemStack paramItemStack, BlockPos paramBlockPos) {
/* 16 */     return (!paramPlayer.level().isOutsideBuildHeight(paramBlockPos) && paramPlayer.mayUseItemAt(paramBlockPos, paramDirection, paramItemStack));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\ItemFrameItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */