/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ 
/*    */ public class BlastFurnaceMenu extends AbstractFurnaceMenu {
/*    */   public BlastFurnaceMenu(int paramInt, Inventory paramInventory) {
/* 10 */     super(MenuType.BLAST_FURNACE, RecipeType.BLASTING, RecipePropertySet.BLAST_FURNACE_INPUT, RecipeBookType.BLAST_FURNACE, paramInt, paramInventory);
/*    */   }
/*    */   
/*    */   public BlastFurnaceMenu(int paramInt, Inventory paramInventory, Container paramContainer, ContainerData paramContainerData) {
/* 14 */     super(MenuType.BLAST_FURNACE, RecipeType.BLASTING, RecipePropertySet.BLAST_FURNACE_INPUT, RecipeBookType.BLAST_FURNACE, paramInt, paramInventory, paramContainer, paramContainerData);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\BlastFurnaceMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */