/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.CraftingInput;
/*    */ 
/*    */ public interface CraftingContainer
/*    */   extends Container, StackedContentsCompatible
/*    */ {
/*    */   int getWidth();
/*    */   
/*    */   int getHeight();
/*    */   
/*    */   List<ItemStack> getItems();
/*    */   
/*    */   default CraftingInput asCraftInput() {
/* 18 */     return asPositionedCraftInput().input();
/*    */   }
/*    */   
/*    */   default CraftingInput.Positioned asPositionedCraftInput() {
/* 22 */     return CraftingInput.ofPositioned(getWidth(), getHeight(), getItems());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\CraftingContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */