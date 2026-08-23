/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ public interface Recipe<T extends RecipeInput>
/*    */ {
/* 19 */   public static final Codec<Recipe<?>> CODEC = BuiltInRegistries.RECIPE_SERIALIZER.byNameCodec().dispatch(Recipe::getSerializer, RecipeSerializer::codec);
/* 20 */   public static final Codec<ResourceKey<Recipe<?>>> KEY_CODEC = ResourceKey.codec(Registries.RECIPE);
/*    */   
/* 22 */   public static final StreamCodec<RegistryFriendlyByteBuf, Recipe<?>> STREAM_CODEC = ByteBufCodecs.registry(Registries.RECIPE_SERIALIZER).dispatch(Recipe::getSerializer, RecipeSerializer::streamCodec);
/*    */ 
/*    */ 
/*    */   
/*    */   boolean matches(T paramT, Level paramLevel);
/*    */ 
/*    */ 
/*    */   
/*    */   ItemStack assemble(T paramT, HolderLookup.Provider paramProvider);
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean isSpecial() {
/* 35 */     return false;
/*    */   }
/*    */   
/*    */   default boolean showNotification() {
/* 39 */     return true;
/*    */   }
/*    */   
/*    */   default String group() {
/* 43 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   RecipeSerializer<? extends Recipe<T>> getSerializer();
/*    */   
/*    */   RecipeType<? extends Recipe<T>> getType();
/*    */   
/*    */   PlacementInfo placementInfo();
/*    */   
/*    */   default List<RecipeDisplay> display() {
/* 54 */     return List.of();
/*    */   }
/*    */   
/*    */   RecipeBookCategory recipeBookCategory();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\Recipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */