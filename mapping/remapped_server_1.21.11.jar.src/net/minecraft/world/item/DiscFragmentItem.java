/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.world.item.component.TooltipDisplay;
/*    */ 
/*    */ public class DiscFragmentItem
/*    */   extends Item {
/*    */   public DiscFragmentItem(Item.Properties paramProperties) {
/* 12 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public void appendHoverText(ItemStack paramItemStack, Item.TooltipContext paramTooltipContext, TooltipDisplay paramTooltipDisplay, Consumer<Component> paramConsumer, TooltipFlag paramTooltipFlag) {
/* 17 */     paramConsumer.accept(getDisplayName().withStyle(ChatFormatting.GRAY));
/*    */   }
/*    */   
/*    */   public MutableComponent getDisplayName() {
/* 21 */     return Component.translatable(this.descriptionId + ".desc");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\DiscFragmentItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */