/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
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
/*     */ public class Serializer
/*     */   implements RecipeSerializer<ShapelessRecipe>
/*     */ {
/*     */   private static final MapCodec<ShapelessRecipe> CODEC;
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessRecipe> STREAM_CODEC;
/*     */   
/*     */   static {
/*  84 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.STRING.optionalFieldOf("group", "").forGetter(()), (App)CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(()), (App)ItemStack.STRICT_CODEC.fieldOf("result").forGetter(()), (App)Ingredient.CODEC.listOf(1, 9).fieldOf("ingredients").forGetter(())).apply((Applicative)paramInstance, ShapelessRecipe::new));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, paramShapelessRecipe -> paramShapelessRecipe.group, CraftingBookCategory.STREAM_CODEC, paramShapelessRecipe -> paramShapelessRecipe.category, ItemStack.STREAM_CODEC, paramShapelessRecipe -> paramShapelessRecipe.result, Ingredient.CONTENTS_STREAM_CODEC
/*     */ 
/*     */ 
/*     */         
/*  95 */         .apply(ByteBufCodecs.list()), paramShapelessRecipe -> paramShapelessRecipe.ingredients, ShapelessRecipe::new);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MapCodec<ShapelessRecipe> codec() {
/* 101 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamCodec<RegistryFriendlyByteBuf, ShapelessRecipe> streamCodec() {
/* 106 */     return STREAM_CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\ShapelessRecipe$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */