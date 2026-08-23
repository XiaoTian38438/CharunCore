/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import net.minecraft.world.inventory.RecipeBookType;
/*    */ 
/*    */ public class RecipeBook {
/*  6 */   protected final RecipeBookSettings bookSettings = new RecipeBookSettings();
/*    */   
/*    */   public boolean isOpen(RecipeBookType paramRecipeBookType) {
/*  9 */     return this.bookSettings.isOpen(paramRecipeBookType);
/*    */   }
/*    */   
/*    */   public void setOpen(RecipeBookType paramRecipeBookType, boolean paramBoolean) {
/* 13 */     this.bookSettings.setOpen(paramRecipeBookType, paramBoolean);
/*    */   }
/*    */   
/*    */   public boolean isFiltering(RecipeBookType paramRecipeBookType) {
/* 17 */     return this.bookSettings.isFiltering(paramRecipeBookType);
/*    */   }
/*    */   
/*    */   public void setFiltering(RecipeBookType paramRecipeBookType, boolean paramBoolean) {
/* 21 */     this.bookSettings.setFiltering(paramRecipeBookType, paramBoolean);
/*    */   }
/*    */   
/*    */   public void setBookSettings(RecipeBookSettings paramRecipeBookSettings) {
/* 25 */     this.bookSettings.replaceFrom(paramRecipeBookSettings);
/*    */   }
/*    */   
/*    */   public RecipeBookSettings getBookSettings() {
/* 29 */     return this.bookSettings;
/*    */   }
/*    */   
/*    */   public void setBookSetting(RecipeBookType paramRecipeBookType, boolean paramBoolean1, boolean paramBoolean2) {
/* 33 */     this.bookSettings.setOpen(paramRecipeBookType, paramBoolean1);
/* 34 */     this.bookSettings.setFiltering(paramRecipeBookType, paramBoolean2);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\stats\RecipeBook.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */