/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*    */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*    */ import net.minecraft.world.item.crafting.display.StonecutterRecipeDisplay;
/*    */ 
/*    */ public class StonecutterRecipe
/*    */   extends SingleItemRecipe {
/*    */   public StonecutterRecipe(String paramString, Ingredient paramIngredient, ItemStack paramItemStack) {
/* 13 */     super(paramString, paramIngredient, paramItemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeType<StonecutterRecipe> getType() {
/* 18 */     return RecipeType.STONECUTTING;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeSerializer<StonecutterRecipe> getSerializer() {
/* 23 */     return RecipeSerializer.STONECUTTER;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<RecipeDisplay> display() {
/* 28 */     return (List)List.of(new StonecutterRecipeDisplay(
/* 29 */           input().display(), 
/* 30 */           resultDisplay(), (SlotDisplay)new SlotDisplay.ItemSlotDisplay(Items.STONECUTTER)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SlotDisplay resultDisplay() {
/* 36 */     return (SlotDisplay)new SlotDisplay.ItemStackSlotDisplay(result());
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeBookCategory recipeBookCategory() {
/* 41 */     return RecipeBookCategories.STONECUTTER;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\StonecutterRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */