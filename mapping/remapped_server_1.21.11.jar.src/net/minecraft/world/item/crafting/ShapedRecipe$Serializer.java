/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.ItemStack;
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
/*     */ public class Serializer
/*     */   implements RecipeSerializer<ShapedRecipe>
/*     */ {
/*     */   public static final MapCodec<ShapedRecipe> CODEC;
/*     */   
/*     */   static {
/* 105 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.STRING.optionalFieldOf("group", "").forGetter(()), (App)CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(()), (App)ShapedRecipePattern.MAP_CODEC.forGetter(()), (App)ItemStack.STRICT_CODEC.fieldOf("result").forGetter(()), (App)Codec.BOOL.optionalFieldOf("show_notification", Boolean.valueOf(true)).forGetter(())).apply((Applicative)paramInstance, ShapedRecipe::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   public static final StreamCodec<RegistryFriendlyByteBuf, ShapedRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);
/*     */ 
/*     */   
/*     */   public MapCodec<ShapedRecipe> codec() {
/* 117 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamCodec<RegistryFriendlyByteBuf, ShapedRecipe> streamCodec() {
/* 122 */     return STREAM_CODEC;
/*     */   }
/*     */   
/*     */   private static ShapedRecipe fromNetwork(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf) {
/* 126 */     String str = paramRegistryFriendlyByteBuf.readUtf();
/* 127 */     CraftingBookCategory craftingBookCategory = (CraftingBookCategory)paramRegistryFriendlyByteBuf.readEnum(CraftingBookCategory.class);
/* 128 */     ShapedRecipePattern shapedRecipePattern = (ShapedRecipePattern)ShapedRecipePattern.STREAM_CODEC.decode(paramRegistryFriendlyByteBuf);
/* 129 */     ItemStack itemStack = (ItemStack)ItemStack.STREAM_CODEC.decode(paramRegistryFriendlyByteBuf);
/* 130 */     boolean bool = paramRegistryFriendlyByteBuf.readBoolean();
/* 131 */     return new ShapedRecipe(str, craftingBookCategory, shapedRecipePattern, itemStack, bool);
/*     */   }
/*     */   
/*     */   private static void toNetwork(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf, ShapedRecipe paramShapedRecipe) {
/* 135 */     paramRegistryFriendlyByteBuf.writeUtf(paramShapedRecipe.group);
/* 136 */     paramRegistryFriendlyByteBuf.writeEnum(paramShapedRecipe.category);
/* 137 */     ShapedRecipePattern.STREAM_CODEC.encode(paramRegistryFriendlyByteBuf, paramShapedRecipe.pattern);
/* 138 */     ItemStack.STREAM_CODEC.encode(paramRegistryFriendlyByteBuf, paramShapedRecipe.result);
/* 139 */     paramRegistryFriendlyByteBuf.writeBoolean(paramShapedRecipe.showNotification);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\ShapedRecipe$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */