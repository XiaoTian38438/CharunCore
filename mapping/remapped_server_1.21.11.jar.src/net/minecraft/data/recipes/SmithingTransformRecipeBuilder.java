/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementRequirements;
/*    */ import net.minecraft.advancements.AdvancementRewards;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.crafting.Ingredient;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.SmithingTransformRecipe;
/*    */ import net.minecraft.world.item.crafting.TransmuteResult;
/*    */ 
/*    */ public class SmithingTransformRecipeBuilder {
/*    */   private final Ingredient template;
/*    */   private final Ingredient base;
/*    */   private final Ingredient addition;
/*    */   private final RecipeCategory category;
/*    */   private final Item result;
/* 27 */   private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
/*    */   
/*    */   public SmithingTransformRecipeBuilder(Ingredient paramIngredient1, Ingredient paramIngredient2, Ingredient paramIngredient3, RecipeCategory paramRecipeCategory, Item paramItem) {
/* 30 */     this.category = paramRecipeCategory;
/* 31 */     this.template = paramIngredient1;
/* 32 */     this.base = paramIngredient2;
/* 33 */     this.addition = paramIngredient3;
/* 34 */     this.result = paramItem;
/*    */   }
/*    */   
/*    */   public static SmithingTransformRecipeBuilder smithing(Ingredient paramIngredient1, Ingredient paramIngredient2, Ingredient paramIngredient3, RecipeCategory paramRecipeCategory, Item paramItem) {
/* 38 */     return new SmithingTransformRecipeBuilder(paramIngredient1, paramIngredient2, paramIngredient3, paramRecipeCategory, paramItem);
/*    */   }
/*    */   
/*    */   public SmithingTransformRecipeBuilder unlocks(String paramString, Criterion<?> paramCriterion) {
/* 42 */     this.criteria.put(paramString, paramCriterion);
/* 43 */     return this;
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput paramRecipeOutput, String paramString) {
/* 47 */     save(paramRecipeOutput, ResourceKey.create(Registries.RECIPE, Identifier.parse(paramString)));
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput paramRecipeOutput, ResourceKey<Recipe<?>> paramResourceKey) {
/* 51 */     ensureValid(paramResourceKey);
/*    */ 
/*    */ 
/*    */     
/* 55 */     Advancement.Builder builder = paramRecipeOutput.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(paramResourceKey)).rewards(AdvancementRewards.Builder.recipe(paramResourceKey)).requirements(AdvancementRequirements.Strategy.OR);
/* 56 */     Objects.requireNonNull(builder); this.criteria.forEach(builder::addCriterion);
/*    */ 
/*    */ 
/*    */     
/* 60 */     SmithingTransformRecipe smithingTransformRecipe = new SmithingTransformRecipe(Optional.of(this.template), this.base, Optional.of(this.addition), new TransmuteResult(this.result));
/*    */ 
/*    */     
/* 63 */     paramRecipeOutput.accept(paramResourceKey, (Recipe<?>)smithingTransformRecipe, builder.build(paramResourceKey.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
/*    */   }
/*    */   
/*    */   private void ensureValid(ResourceKey<Recipe<?>> paramResourceKey) {
/* 67 */     if (this.criteria.isEmpty())
/* 68 */       throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(paramResourceKey.identifier())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\recipes\SmithingTransformRecipeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */