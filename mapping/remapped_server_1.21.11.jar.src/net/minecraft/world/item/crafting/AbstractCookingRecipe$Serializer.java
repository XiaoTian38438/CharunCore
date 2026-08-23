/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Serializer<T extends AbstractCookingRecipe>
/*    */   implements RecipeSerializer<T>
/*    */ {
/*    */   private final MapCodec<T> codec;
/*    */   private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
/*    */   
/*    */   public Serializer(AbstractCookingRecipe.Factory<T> paramFactory, int paramInt) {
/* 66 */     this.codec = RecordCodecBuilder.mapCodec(paramInstance -> {
/*    */           Objects.requireNonNull(paramFactory);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/*    */           return paramInstance.group((App)Codec.STRING.optionalFieldOf("group", "").forGetter(SingleItemRecipe::group), (App)CookingBookCategory.CODEC.fieldOf("category").orElse(CookingBookCategory.MISC).forGetter(AbstractCookingRecipe::category), (App)Ingredient.CODEC.fieldOf("ingredient").forGetter(SingleItemRecipe::input), (App)ItemStack.STRICT_SINGLE_ITEM_CODEC.fieldOf("result").forGetter(SingleItemRecipe::result), (App)Codec.FLOAT.fieldOf("experience").orElse(Float.valueOf(0.0F)).forGetter(AbstractCookingRecipe::experience), (App)Codec.INT.fieldOf("cookingtime").orElse(Integer.valueOf(paramInt)).forGetter(AbstractCookingRecipe::cookingTime)).apply((Applicative)paramInstance, paramFactory::create);
/*    */         });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 82 */     Objects.requireNonNull(paramFactory); this.streamCodec = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SingleItemRecipe::group, CookingBookCategory.STREAM_CODEC, AbstractCookingRecipe::category, Ingredient.CONTENTS_STREAM_CODEC, SingleItemRecipe::input, ItemStack.STREAM_CODEC, SingleItemRecipe::result, ByteBufCodecs.FLOAT, AbstractCookingRecipe::experience, ByteBufCodecs.INT, AbstractCookingRecipe::cookingTime, paramFactory::create);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public MapCodec<T> codec() {
/* 88 */     return this.codec;
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
/* 93 */     return this.streamCodec;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\AbstractCookingRecipe$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */