/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementRequirements;
/*    */ import net.minecraft.advancements.AdvancementRewards;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.crafting.Ingredient;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.SmithingTrimRecipe;
/*    */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*    */ 
/*    */ public class SmithingTrimRecipeBuilder {
/*    */   private final RecipeCategory category;
/*    */   private final Ingredient template;
/*    */   private final Ingredient base;
/*    */   private final Ingredient addition;
/*    */   private final Holder<TrimPattern> pattern;
/* 24 */   private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
/*    */   
/*    */   public SmithingTrimRecipeBuilder(RecipeCategory paramRecipeCategory, Ingredient paramIngredient1, Ingredient paramIngredient2, Ingredient paramIngredient3, Holder<TrimPattern> paramHolder) {
/* 27 */     this.category = paramRecipeCategory;
/* 28 */     this.template = paramIngredient1;
/* 29 */     this.base = paramIngredient2;
/* 30 */     this.addition = paramIngredient3;
/* 31 */     this.pattern = paramHolder;
/*    */   }
/*    */   
/*    */   public static SmithingTrimRecipeBuilder smithingTrim(Ingredient paramIngredient1, Ingredient paramIngredient2, Ingredient paramIngredient3, Holder<TrimPattern> paramHolder, RecipeCategory paramRecipeCategory) {
/* 35 */     return new SmithingTrimRecipeBuilder(paramRecipeCategory, paramIngredient1, paramIngredient2, paramIngredient3, paramHolder);
/*    */   }
/*    */   
/*    */   public SmithingTrimRecipeBuilder unlocks(String paramString, Criterion<?> paramCriterion) {
/* 39 */     this.criteria.put(paramString, paramCriterion);
/* 40 */     return this;
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput paramRecipeOutput, ResourceKey<Recipe<?>> paramResourceKey) {
/* 44 */     ensureValid(paramResourceKey);
/*    */ 
/*    */ 
/*    */     
/* 48 */     Advancement.Builder builder = paramRecipeOutput.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(paramResourceKey)).rewards(AdvancementRewards.Builder.recipe(paramResourceKey)).requirements(AdvancementRequirements.Strategy.OR);
/* 49 */     Objects.requireNonNull(builder); this.criteria.forEach(builder::addCriterion);
/* 50 */     SmithingTrimRecipe smithingTrimRecipe = new SmithingTrimRecipe(this.template, this.base, this.addition, this.pattern);
/* 51 */     paramRecipeOutput.accept(paramResourceKey, (Recipe<?>)smithingTrimRecipe, builder.build(paramResourceKey.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
/*    */   }
/*    */   
/*    */   private void ensureValid(ResourceKey<Recipe<?>> paramResourceKey) {
/* 55 */     if (this.criteria.isEmpty())
/* 56 */       throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(paramResourceKey.identifier())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\recipes\SmithingTrimRecipeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */