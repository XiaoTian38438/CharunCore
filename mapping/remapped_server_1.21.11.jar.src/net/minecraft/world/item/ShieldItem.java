/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class ShieldItem extends Item {
/*    */   public ShieldItem(Item.Properties paramProperties) {
/*  8 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getName(ItemStack paramItemStack) {
/* 13 */     DyeColor dyeColor = (DyeColor)paramItemStack.get(DataComponents.BASE_COLOR);
/* 14 */     if (dyeColor != null) {
/* 15 */       return (Component)Component.translatable(this.descriptionId + "." + this.descriptionId);
/*    */     }
/* 17 */     return super.getName(paramItemStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\ShieldItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */