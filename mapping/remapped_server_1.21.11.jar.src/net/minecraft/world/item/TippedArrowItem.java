/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ import net.minecraft.world.item.alchemy.Potions;
/*    */ 
/*    */ public class TippedArrowItem extends ArrowItem {
/*    */   public TippedArrowItem(Item.Properties paramProperties) {
/* 10 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getDefaultInstance() {
/* 15 */     ItemStack itemStack = super.getDefaultInstance();
/* 16 */     itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.POISON));
/* 17 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getName(ItemStack paramItemStack) {
/* 22 */     PotionContents potionContents = (PotionContents)paramItemStack.get(DataComponents.POTION_CONTENTS);
/* 23 */     return (potionContents != null) ? potionContents.getName(this.descriptionId + ".effect.") : super.getName(paramItemStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\TippedArrowItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */