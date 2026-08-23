/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.FurnaceMenu;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class FurnaceBlockEntity extends AbstractFurnaceBlockEntity {
/* 12 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("container.furnace");
/*    */   
/*    */   public FurnaceBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 15 */     super(BlockEntityType.FURNACE, paramBlockPos, paramBlockState, RecipeType.SMELTING);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Component getDefaultName() {
/* 20 */     return DEFAULT_NAME;
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory) {
/* 25 */     return (AbstractContainerMenu)new FurnaceMenu(paramInt, paramInventory, this, this.dataAccess);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\FurnaceBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */