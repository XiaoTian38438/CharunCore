/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
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
/*     */ public class Serializer
/*     */   implements RecipeSerializer<SmithingTransformRecipe>
/*     */ {
/*     */   private static final MapCodec<SmithingTransformRecipe> CODEC;
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, SmithingTransformRecipe> STREAM_CODEC;
/*     */   
/*     */   static {
/*  81 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Ingredient.CODEC.optionalFieldOf("template").forGetter(()), (App)Ingredient.CODEC.fieldOf("base").forGetter(()), (App)Ingredient.CODEC.optionalFieldOf("addition").forGetter(()), (App)TransmuteResult.CODEC.fieldOf("result").forGetter(())).apply((Applicative)paramInstance, SmithingTransformRecipe::new));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     STREAM_CODEC = StreamCodec.composite(Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, paramSmithingTransformRecipe -> paramSmithingTransformRecipe.template, Ingredient.CONTENTS_STREAM_CODEC, paramSmithingTransformRecipe -> paramSmithingTransformRecipe.base, Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, paramSmithingTransformRecipe -> paramSmithingTransformRecipe.addition, TransmuteResult.STREAM_CODEC, paramSmithingTransformRecipe -> paramSmithingTransformRecipe.result, SmithingTransformRecipe::new);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapCodec<SmithingTransformRecipe> codec() {
/*  98 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamCodec<RegistryFriendlyByteBuf, SmithingTransformRecipe> streamCodec() {
/* 103 */     return STREAM_CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\SmithingTransformRecipe$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */