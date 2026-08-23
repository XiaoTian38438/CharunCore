/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Serializer
/*     */   implements RecipeSerializer<TransmuteRecipe>
/*     */ {
/*     */   private static final MapCodec<TransmuteRecipe> CODEC;
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, TransmuteRecipe> STREAM_CODEC;
/*     */   
/*     */   static {
/* 114 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.STRING.optionalFieldOf("group", "").forGetter(()), (App)CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(()), (App)Ingredient.CODEC.fieldOf("input").forGetter(()), (App)Ingredient.CODEC.fieldOf("material").forGetter(()), (App)TransmuteResult.CODEC.fieldOf("result").forGetter(())).apply((Applicative)paramInstance, TransmuteRecipe::new));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, paramTransmuteRecipe -> paramTransmuteRecipe.group, CraftingBookCategory.STREAM_CODEC, paramTransmuteRecipe -> paramTransmuteRecipe.category, Ingredient.CONTENTS_STREAM_CODEC, paramTransmuteRecipe -> paramTransmuteRecipe.input, Ingredient.CONTENTS_STREAM_CODEC, paramTransmuteRecipe -> paramTransmuteRecipe.material, TransmuteResult.STREAM_CODEC, paramTransmuteRecipe -> paramTransmuteRecipe.result, TransmuteRecipe::new);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapCodec<TransmuteRecipe> codec() {
/* 133 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamCodec<RegistryFriendlyByteBuf, TransmuteRecipe> streamCodec() {
/* 138 */     return STREAM_CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\TransmuteRecipe$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */