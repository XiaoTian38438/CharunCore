/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.SmokerMenu;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SmokerBlockEntity extends AbstractFurnaceBlockEntity {
/* 13 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("container.smoker");
/*    */   
/*    */   public SmokerBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 16 */     super(BlockEntityType.SMOKER, paramBlockPos, paramBlockState, RecipeType.SMOKING);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Component getDefaultName() {
/* 21 */     return DEFAULT_NAME;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBurnDuration(FuelValues paramFuelValues, ItemStack paramItemStack) {
/* 26 */     return super.getBurnDuration(paramFuelValues, paramItemStack) / 2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory) {
/* 31 */     return (AbstractContainerMenu)new SmokerMenu(paramInt, paramInventory, this, this.dataAccess);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\SmokerBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */