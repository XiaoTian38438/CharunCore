/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class PlayerHeadItem extends StandingAndWallBlockItem {
/*    */   public PlayerHeadItem(Block paramBlock1, Block paramBlock2, Item.Properties paramProperties) {
/* 11 */     super(paramBlock1, paramBlock2, Direction.DOWN, paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getName(ItemStack paramItemStack) {
/* 16 */     ResolvableProfile resolvableProfile = (ResolvableProfile)paramItemStack.get(DataComponents.PROFILE);
/* 17 */     if (resolvableProfile != null && resolvableProfile.name().isPresent()) {
/* 18 */       return (Component)Component.translatable(this.descriptionId + ".named", new Object[] { resolvableProfile.name().get() });
/*    */     }
/* 20 */     return super.getName(paramItemStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\PlayerHeadItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */