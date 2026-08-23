/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableMultimap;
/*    */ import com.google.common.collect.Multimap;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ public class RecipeMap
/*    */ {
/* 15 */   public static final RecipeMap EMPTY = new RecipeMap((Multimap<RecipeType<?>, RecipeHolder<?>>)ImmutableMultimap.of(), Map.of());
/*    */   
/*    */   private final Multimap<RecipeType<?>, RecipeHolder<?>> byType;
/*    */   
/*    */   private final Map<ResourceKey<Recipe<?>>, RecipeHolder<?>> byKey;
/*    */   
/*    */   private RecipeMap(Multimap<RecipeType<?>, RecipeHolder<?>> paramMultimap, Map<ResourceKey<Recipe<?>>, RecipeHolder<?>> paramMap) {
/* 22 */     this.byType = paramMultimap;
/* 23 */     this.byKey = paramMap;
/*    */   }
/*    */   
/*    */   public static RecipeMap create(Iterable<RecipeHolder<?>> paramIterable) {
/* 27 */     ImmutableMultimap.Builder builder = ImmutableMultimap.builder();
/* 28 */     ImmutableMap.Builder builder1 = ImmutableMap.builder();
/*    */     
/* 30 */     for (RecipeHolder<?> recipeHolder : paramIterable) {
/* 31 */       builder.put(recipeHolder.value().getType(), recipeHolder);
/* 32 */       builder1.put(recipeHolder.id(), recipeHolder);
/*    */     } 
/*    */     
/* 35 */     return new RecipeMap((Multimap<RecipeType<?>, RecipeHolder<?>>)builder.build(), (Map<ResourceKey<Recipe<?>>, RecipeHolder<?>>)builder1.build());
/*    */   }
/*    */ 
/*    */   
/*    */   public <I extends RecipeInput, T extends Recipe<I>> Collection<RecipeHolder<T>> byType(RecipeType<T> paramRecipeType) {
/* 40 */     return this.byType.get(paramRecipeType);
/*    */   }
/*    */   
/*    */   public Collection<RecipeHolder<?>> values() {
/* 44 */     return this.byKey.values();
/*    */   }
/*    */   
/*    */   public RecipeHolder<?> byKey(ResourceKey<Recipe<?>> paramResourceKey) {
/* 48 */     return this.byKey.get(paramResourceKey);
/*    */   }
/*    */   
/*    */   public <I extends RecipeInput, T extends Recipe<I>> Stream<RecipeHolder<T>> getRecipesFor(RecipeType<T> paramRecipeType, I paramI, Level paramLevel) {
/* 52 */     if (paramI.isEmpty()) {
/* 53 */       return Stream.empty();
/*    */     }
/* 55 */     return byType(paramRecipeType).stream().filter(paramRecipeHolder -> paramRecipeHolder.value().matches(paramRecipeInput, paramLevel));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\RecipeMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */