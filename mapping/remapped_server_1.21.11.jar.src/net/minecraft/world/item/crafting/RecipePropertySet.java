/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class RecipePropertySet
/*    */ {
/* 19 */   public static final ResourceKey<? extends Registry<RecipePropertySet>> TYPE_KEY = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("recipe_property_set"));
/*    */   
/* 21 */   public static final ResourceKey<RecipePropertySet> SMITHING_BASE = registerVanilla("smithing_base");
/* 22 */   public static final ResourceKey<RecipePropertySet> SMITHING_TEMPLATE = registerVanilla("smithing_template");
/* 23 */   public static final ResourceKey<RecipePropertySet> SMITHING_ADDITION = registerVanilla("smithing_addition");
/* 24 */   public static final ResourceKey<RecipePropertySet> FURNACE_INPUT = registerVanilla("furnace_input");
/* 25 */   public static final ResourceKey<RecipePropertySet> BLAST_FURNACE_INPUT = registerVanilla("blast_furnace_input");
/* 26 */   public static final ResourceKey<RecipePropertySet> SMOKER_INPUT = registerVanilla("smoker_input");
/* 27 */   public static final ResourceKey<RecipePropertySet> CAMPFIRE_INPUT = registerVanilla("campfire_input");
/*    */   
/*    */   static {
/* 30 */     STREAM_CODEC = Item.STREAM_CODEC.apply(ByteBufCodecs.list()).map(paramList -> new RecipePropertySet(Set.copyOf(paramList)), paramRecipePropertySet -> List.copyOf(paramRecipePropertySet.items));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final StreamCodec<RegistryFriendlyByteBuf, RecipePropertySet> STREAM_CODEC;
/* 35 */   public static final RecipePropertySet EMPTY = new RecipePropertySet(Set.of());
/*    */   
/*    */   private final Set<Holder<Item>> items;
/*    */   
/*    */   private RecipePropertySet(Set<Holder<Item>> paramSet) {
/* 40 */     this.items = paramSet;
/*    */   }
/*    */   
/*    */   private static ResourceKey<RecipePropertySet> registerVanilla(String paramString) {
/* 44 */     return ResourceKey.create(TYPE_KEY, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   public boolean test(ItemStack paramItemStack) {
/* 48 */     return this.items.contains(paramItemStack.getItemHolder());
/*    */   }
/*    */   
/*    */   static RecipePropertySet create(Collection<Ingredient> paramCollection) {
/* 52 */     Set<Holder<Item>> set = (Set)paramCollection.stream().flatMap(Ingredient::items).collect(Collectors.toUnmodifiableSet());
/* 53 */     return new RecipePropertySet(set);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\RecipePropertySet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */