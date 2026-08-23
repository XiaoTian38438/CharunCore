/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   implements RecipeSerializer<SmithingTrimRecipe>
/*     */ {
/*     */   private static final MapCodec<SmithingTrimRecipe> CODEC;
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, SmithingTrimRecipe> STREAM_CODEC;
/*     */   
/*     */   static {
/* 106 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Ingredient.CODEC.fieldOf("template").forGetter(()), (App)Ingredient.CODEC.fieldOf("base").forGetter(()), (App)Ingredient.CODEC.fieldOf("addition").forGetter(()), (App)TrimPattern.CODEC.fieldOf("pattern").forGetter(())).apply((Applicative)paramInstance, SmithingTrimRecipe::new));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     STREAM_CODEC = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, paramSmithingTrimRecipe -> paramSmithingTrimRecipe.template, Ingredient.CONTENTS_STREAM_CODEC, paramSmithingTrimRecipe -> paramSmithingTrimRecipe.base, Ingredient.CONTENTS_STREAM_CODEC, paramSmithingTrimRecipe -> paramSmithingTrimRecipe.addition, TrimPattern.STREAM_CODEC, paramSmithingTrimRecipe -> paramSmithingTrimRecipe.pattern, SmithingTrimRecipe::new);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapCodec<SmithingTrimRecipe> codec() {
/* 123 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamCodec<RegistryFriendlyByteBuf, SmithingTrimRecipe> streamCodec() {
/* 128 */     return STREAM_CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\SmithingTrimRecipe$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */