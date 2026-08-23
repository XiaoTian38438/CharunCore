/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ 
/*    */ public class FurnaceMenu extends AbstractFurnaceMenu {
/*    */   public FurnaceMenu(int paramInt, Inventory paramInventory) {
/* 10 */     super(MenuType.FURNACE, RecipeType.SMELTING, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, paramInt, paramInventory);
/*    */   }
/*    */   
/*    */   public FurnaceMenu(int paramInt, Inventory paramInventory, Container paramContainer, ContainerData paramContainerData) {
/* 14 */     super(MenuType.FURNACE, RecipeType.SMELTING, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, paramInt, paramInventory, paramContainer, paramContainerData);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\FurnaceMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */