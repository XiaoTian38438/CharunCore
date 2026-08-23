/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.ContainerHelper;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ 
/*    */ public class ResultContainer implements Container, RecipeCraftingHolder {
/* 12 */   private final NonNullList<ItemStack> itemStacks = NonNullList.withSize(1, ItemStack.EMPTY);
/*    */   
/*    */   private RecipeHolder<?> recipeUsed;
/*    */   
/*    */   public int getContainerSize() {
/* 17 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 22 */     for (ItemStack itemStack : this.itemStacks) {
/* 23 */       if (!itemStack.isEmpty()) {
/* 24 */         return false;
/*    */       }
/*    */     } 
/* 27 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getItem(int paramInt) {
/* 32 */     return (ItemStack)this.itemStacks.get(0);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack removeItem(int paramInt1, int paramInt2) {
/* 37 */     return ContainerHelper.takeItem((List)this.itemStacks, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack removeItemNoUpdate(int paramInt) {
/* 42 */     return ContainerHelper.takeItem((List)this.itemStacks, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setItem(int paramInt, ItemStack paramItemStack) {
/* 47 */     this.itemStacks.set(0, paramItemStack);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setChanged() {}
/*    */ 
/*    */   
/*    */   public boolean stillValid(Player paramPlayer) {
/* 56 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void clearContent() {
/* 61 */     this.itemStacks.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRecipeUsed(RecipeHolder<?> paramRecipeHolder) {
/* 66 */     this.recipeUsed = paramRecipeHolder;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeHolder<?> getRecipeUsed() {
/* 71 */     return this.recipeUsed;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\ResultContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */