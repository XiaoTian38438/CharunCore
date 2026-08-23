/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.PlacementInfo;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ 
/*    */ public class StackedItemContents
/*    */ {
/* 13 */   private final StackedContents<Holder<Item>> raw = new StackedContents<>();
/*    */   
/*    */   public void accountSimpleStack(ItemStack paramItemStack) {
/* 16 */     if (Inventory.isUsableForCrafting(paramItemStack)) {
/* 17 */       accountStack(paramItemStack);
/*    */     }
/*    */   }
/*    */   
/*    */   public void accountStack(ItemStack paramItemStack) {
/* 22 */     accountStack(paramItemStack, paramItemStack.getMaxStackSize());
/*    */   }
/*    */   
/*    */   public void accountStack(ItemStack paramItemStack, int paramInt) {
/* 26 */     if (!paramItemStack.isEmpty()) {
/* 27 */       int i = Math.min(paramInt, paramItemStack.getCount());
/* 28 */       this.raw.account(paramItemStack.getItemHolder(), i);
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean canCraft(Recipe<?> paramRecipe, StackedContents.Output<Holder<Item>> paramOutput) {
/* 33 */     return canCraft(paramRecipe, 1, paramOutput);
/*    */   }
/*    */   
/*    */   public boolean canCraft(Recipe<?> paramRecipe, int paramInt, StackedContents.Output<Holder<Item>> paramOutput) {
/* 37 */     PlacementInfo placementInfo = paramRecipe.placementInfo();
/* 38 */     if (placementInfo.isImpossibleToPlace()) {
/* 39 */       return false;
/*    */     }
/* 41 */     return canCraft(placementInfo.ingredients(), paramInt, paramOutput);
/*    */   }
/*    */   
/*    */   public boolean canCraft(List<? extends StackedContents.IngredientInfo<Holder<Item>>> paramList, StackedContents.Output<Holder<Item>> paramOutput) {
/* 45 */     return canCraft(paramList, 1, paramOutput);
/*    */   }
/*    */   
/*    */   private boolean canCraft(List<? extends StackedContents.IngredientInfo<Holder<Item>>> paramList, int paramInt, StackedContents.Output<Holder<Item>> paramOutput) {
/* 49 */     return this.raw.tryPick(paramList, paramInt, paramOutput);
/*    */   }
/*    */   
/*    */   public int getBiggestCraftableStack(Recipe<?> paramRecipe, StackedContents.Output<Holder<Item>> paramOutput) {
/* 53 */     return getBiggestCraftableStack(paramRecipe, 2147483647, paramOutput);
/*    */   }
/*    */   
/*    */   public int getBiggestCraftableStack(Recipe<?> paramRecipe, int paramInt, StackedContents.Output<Holder<Item>> paramOutput) {
/* 57 */     return this.raw.tryPickAll(paramRecipe.placementInfo().ingredients(), paramInt, paramOutput);
/*    */   }
/*    */   
/*    */   public void clear() {
/* 61 */     this.raw.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\player\StackedItemContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */