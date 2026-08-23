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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Serializer<T extends SingleItemRecipe>
/*    */   implements RecipeSerializer<T>
/*    */ {
/*    */   private final MapCodec<T> codec;
/*    */   private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
/*    */   
/*    */   protected Serializer(SingleItemRecipe.Factory<T> paramFactory) {
/* 69 */     this.codec = RecordCodecBuilder.mapCodec(paramInstance -> {
/*    */           Objects.requireNonNull(paramFactory);
/*    */ 
/*    */ 
/*    */           
/*    */           return paramInstance.group((App)Codec.STRING.optionalFieldOf("group", "").forGetter(SingleItemRecipe::group), (App)Ingredient.CODEC.fieldOf("ingredient").forGetter(SingleItemRecipe::input), (App)ItemStack.STRICT_CODEC.fieldOf("result").forGetter(SingleItemRecipe::result)).apply((Applicative)paramInstance, paramFactory::create);
/*    */         });
/*    */ 
/*    */ 
/*    */     
/* 79 */     Objects.requireNonNull(paramFactory); this.streamCodec = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SingleItemRecipe::group, Ingredient.CONTENTS_STREAM_CODEC, SingleItemRecipe::input, ItemStack.STREAM_CODEC, SingleItemRecipe::result, paramFactory::create);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public MapCodec<T> codec() {
/* 85 */     return this.codec;
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
/* 90 */     return this.streamCodec;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\SingleItemRecipe$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */