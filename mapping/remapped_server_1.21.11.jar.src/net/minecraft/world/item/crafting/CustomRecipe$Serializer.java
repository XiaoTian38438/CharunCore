/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
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
/*    */ public class Serializer<T extends CraftingRecipe>
/*    */   implements RecipeSerializer<T>
/*    */ {
/*    */   private final MapCodec<T> codec;
/*    */   private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
/*    */   
/*    */   public Serializer(Factory<T> paramFactory) {
/* 38 */     this.codec = RecordCodecBuilder.mapCodec(paramInstance -> {
/*    */           Objects.requireNonNull(paramFactory);
/*    */           
/*    */           return paramInstance.group((App)CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(CraftingRecipe::category)).apply((Applicative)paramInstance, paramFactory::create);
/*    */         });
/*    */     
/* 44 */     Objects.requireNonNull(paramFactory); this.streamCodec = StreamCodec.composite(CraftingBookCategory.STREAM_CODEC, CraftingRecipe::category, paramFactory::create);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public MapCodec<T> codec() {
/* 50 */     return this.codec;
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
/* 55 */     return this.streamCodec;
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Factory<T extends CraftingRecipe> {
/*    */     T create(CraftingBookCategory param2CraftingBookCategory);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\CustomRecipe$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */