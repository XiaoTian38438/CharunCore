/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class RecipeBookCategories {
/*  7 */   public static final RecipeBookCategory CRAFTING_BUILDING_BLOCKS = register("crafting_building_blocks");
/*  8 */   public static final RecipeBookCategory CRAFTING_REDSTONE = register("crafting_redstone");
/*  9 */   public static final RecipeBookCategory CRAFTING_EQUIPMENT = register("crafting_equipment");
/* 10 */   public static final RecipeBookCategory CRAFTING_MISC = register("crafting_misc");
/*    */   
/* 12 */   public static final RecipeBookCategory FURNACE_FOOD = register("furnace_food");
/* 13 */   public static final RecipeBookCategory FURNACE_BLOCKS = register("furnace_blocks");
/* 14 */   public static final RecipeBookCategory FURNACE_MISC = register("furnace_misc");
/*    */   
/* 16 */   public static final RecipeBookCategory BLAST_FURNACE_BLOCKS = register("blast_furnace_blocks");
/* 17 */   public static final RecipeBookCategory BLAST_FURNACE_MISC = register("blast_furnace_misc");
/*    */   
/* 19 */   public static final RecipeBookCategory SMOKER_FOOD = register("smoker_food");
/*    */   
/* 21 */   public static final RecipeBookCategory STONECUTTER = register("stonecutter");
/*    */   
/* 23 */   public static final RecipeBookCategory SMITHING = register("smithing");
/*    */   
/* 25 */   public static final RecipeBookCategory CAMPFIRE = register("campfire");
/*    */   
/*    */   private static RecipeBookCategory register(String paramString) {
/* 28 */     return (RecipeBookCategory)Registry.register(BuiltInRegistries.RECIPE_BOOK_CATEGORY, paramString, new RecipeBookCategory());
/*    */   }
/*    */   
/*    */   public static RecipeBookCategory bootstrap(Registry<RecipeBookCategory> paramRegistry) {
/* 32 */     return CAMPFIRE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\RecipeBookCategories.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */