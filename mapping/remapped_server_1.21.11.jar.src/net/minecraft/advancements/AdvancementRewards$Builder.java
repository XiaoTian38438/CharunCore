/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*     */   private int experience;
/*  78 */   private final ImmutableList.Builder<ResourceKey<LootTable>> loot = ImmutableList.builder();
/*  79 */   private final ImmutableList.Builder<ResourceKey<Recipe<?>>> recipes = ImmutableList.builder();
/*  80 */   private Optional<Identifier> function = Optional.empty();
/*     */   
/*     */   public static Builder experience(int paramInt) {
/*  83 */     return (new Builder()).addExperience(paramInt);
/*     */   }
/*     */   
/*     */   public Builder addExperience(int paramInt) {
/*  87 */     this.experience += paramInt;
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public static Builder loot(ResourceKey<LootTable> paramResourceKey) {
/*  92 */     return (new Builder()).addLootTable(paramResourceKey);
/*     */   }
/*     */   
/*     */   public Builder addLootTable(ResourceKey<LootTable> paramResourceKey) {
/*  96 */     this.loot.add(paramResourceKey);
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public static Builder recipe(ResourceKey<Recipe<?>> paramResourceKey) {
/* 101 */     return (new Builder()).addRecipe(paramResourceKey);
/*     */   }
/*     */   
/*     */   public Builder addRecipe(ResourceKey<Recipe<?>> paramResourceKey) {
/* 105 */     this.recipes.add(paramResourceKey);
/* 106 */     return this;
/*     */   }
/*     */   
/*     */   public static Builder function(Identifier paramIdentifier) {
/* 110 */     return (new Builder()).runs(paramIdentifier);
/*     */   }
/*     */   
/*     */   public Builder runs(Identifier paramIdentifier) {
/* 114 */     this.function = Optional.of(paramIdentifier);
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public AdvancementRewards build() {
/* 119 */     return new AdvancementRewards(this.experience, (List<ResourceKey<LootTable>>)this.loot.build(), (List<ResourceKey<Recipe<?>>>)this.recipes.build(), this.function.map(net.minecraft.commands.CacheableFunction::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\AdvancementRewards$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */