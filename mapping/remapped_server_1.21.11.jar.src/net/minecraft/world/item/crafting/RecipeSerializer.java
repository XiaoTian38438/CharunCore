/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public interface RecipeSerializer<T extends Recipe<?>> {
/* 10 */   public static final RecipeSerializer<ShapedRecipe> SHAPED_RECIPE = register("crafting_shaped", new ShapedRecipe.Serializer());
/* 11 */   public static final RecipeSerializer<ShapelessRecipe> SHAPELESS_RECIPE = register("crafting_shapeless", new ShapelessRecipe.Serializer());
/* 12 */   public static final RecipeSerializer<ArmorDyeRecipe> ARMOR_DYE = register("crafting_special_armordye", new CustomRecipe.Serializer<>(ArmorDyeRecipe::new));
/* 13 */   public static final RecipeSerializer<BookCloningRecipe> BOOK_CLONING = register("crafting_special_bookcloning", new CustomRecipe.Serializer<>(BookCloningRecipe::new));
/* 14 */   public static final RecipeSerializer<MapCloningRecipe> MAP_CLONING = register("crafting_special_mapcloning", new CustomRecipe.Serializer<>(MapCloningRecipe::new));
/* 15 */   public static final RecipeSerializer<MapExtendingRecipe> MAP_EXTENDING = register("crafting_special_mapextending", new CustomRecipe.Serializer<>(MapExtendingRecipe::new));
/* 16 */   public static final RecipeSerializer<FireworkRocketRecipe> FIREWORK_ROCKET = register("crafting_special_firework_rocket", new CustomRecipe.Serializer<>(FireworkRocketRecipe::new));
/* 17 */   public static final RecipeSerializer<FireworkStarRecipe> FIREWORK_STAR = register("crafting_special_firework_star", new CustomRecipe.Serializer<>(FireworkStarRecipe::new));
/* 18 */   public static final RecipeSerializer<FireworkStarFadeRecipe> FIREWORK_STAR_FADE = register("crafting_special_firework_star_fade", new CustomRecipe.Serializer<>(FireworkStarFadeRecipe::new));
/* 19 */   public static final RecipeSerializer<TippedArrowRecipe> TIPPED_ARROW = register("crafting_special_tippedarrow", new CustomRecipe.Serializer<>(TippedArrowRecipe::new));
/* 20 */   public static final RecipeSerializer<BannerDuplicateRecipe> BANNER_DUPLICATE = register("crafting_special_bannerduplicate", new CustomRecipe.Serializer<>(BannerDuplicateRecipe::new));
/* 21 */   public static final RecipeSerializer<ShieldDecorationRecipe> SHIELD_DECORATION = register("crafting_special_shielddecoration", new CustomRecipe.Serializer<>(ShieldDecorationRecipe::new));
/* 22 */   public static final RecipeSerializer<TransmuteRecipe> TRANSMUTE = register("crafting_transmute", new TransmuteRecipe.Serializer());
/* 23 */   public static final RecipeSerializer<RepairItemRecipe> REPAIR_ITEM = register("crafting_special_repairitem", new CustomRecipe.Serializer<>(RepairItemRecipe::new));
/* 24 */   public static final RecipeSerializer<SmeltingRecipe> SMELTING_RECIPE = register("smelting", new AbstractCookingRecipe.Serializer<>(SmeltingRecipe::new, 200));
/* 25 */   public static final RecipeSerializer<BlastingRecipe> BLASTING_RECIPE = register("blasting", new AbstractCookingRecipe.Serializer<>(BlastingRecipe::new, 100));
/* 26 */   public static final RecipeSerializer<SmokingRecipe> SMOKING_RECIPE = register("smoking", new AbstractCookingRecipe.Serializer<>(SmokingRecipe::new, 100));
/* 27 */   public static final RecipeSerializer<CampfireCookingRecipe> CAMPFIRE_COOKING_RECIPE = register("campfire_cooking", new AbstractCookingRecipe.Serializer<>(CampfireCookingRecipe::new, 100));
/* 28 */   public static final RecipeSerializer<StonecutterRecipe> STONECUTTER = register("stonecutting", new SingleItemRecipe.Serializer<>(StonecutterRecipe::new));
/* 29 */   public static final RecipeSerializer<SmithingTransformRecipe> SMITHING_TRANSFORM = register("smithing_transform", new SmithingTransformRecipe.Serializer());
/* 30 */   public static final RecipeSerializer<SmithingTrimRecipe> SMITHING_TRIM = register("smithing_trim", new SmithingTrimRecipe.Serializer());
/* 31 */   public static final RecipeSerializer<DecoratedPotRecipe> DECORATED_POT_RECIPE = register("crafting_decorated_pot", new CustomRecipe.Serializer<>(DecoratedPotRecipe::new));
/*    */ 
/*    */   
/*    */   MapCodec<T> codec();
/*    */   
/*    */   @Deprecated
/*    */   StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();
/*    */   
/*    */   static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String paramString, S paramS) {
/* 40 */     return (S)Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, paramString, paramS);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\RecipeSerializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */