/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*    */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*    */ 
/*    */ public abstract class AbstractCookingRecipe extends SingleItemRecipe {
/*    */   private final CookingBookCategory category;
/*    */   
/*    */   public AbstractCookingRecipe(String paramString, CookingBookCategory paramCookingBookCategory, Ingredient paramIngredient, ItemStack paramItemStack, float paramFloat, int paramInt) {
/* 23 */     super(paramString, paramIngredient, paramItemStack);
/* 24 */     this.category = paramCookingBookCategory;
/* 25 */     this.experience = paramFloat;
/* 26 */     this.cookingTime = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   private final float experience;
/*    */   
/*    */   private final int cookingTime;
/*    */ 
/*    */   
/*    */   public float experience() {
/* 36 */     return this.experience;
/*    */   } public abstract RecipeSerializer<? extends AbstractCookingRecipe> getSerializer();
/*    */   public abstract RecipeType<? extends AbstractCookingRecipe> getType();
/*    */   public int cookingTime() {
/* 40 */     return this.cookingTime;
/*    */   }
/*    */   
/*    */   public CookingBookCategory category() {
/* 44 */     return this.category;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract Item furnaceIcon();
/*    */   
/*    */   public List<RecipeDisplay> display() {
/* 51 */     return (List)List.of(new FurnaceRecipeDisplay(
/* 52 */           input().display(), (SlotDisplay)SlotDisplay.AnyFuel.INSTANCE, (SlotDisplay)new SlotDisplay.ItemStackSlotDisplay(
/*    */             
/* 54 */             result()), (SlotDisplay)new SlotDisplay.ItemSlotDisplay(
/* 55 */             furnaceIcon()), this.cookingTime, this.experience));
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Factory<T extends AbstractCookingRecipe> {
/*    */     T create(String param1String, CookingBookCategory param1CookingBookCategory, Ingredient param1Ingredient, ItemStack param1ItemStack, float param1Float, int param1Int); }
/*    */   
/*    */   public static class Serializer<T extends AbstractCookingRecipe> implements RecipeSerializer<T> { private final MapCodec<T> codec;
/*    */     private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
/*    */     
/*    */     public Serializer(AbstractCookingRecipe.Factory<T> param1Factory, int param1Int) {
/* 66 */       this.codec = RecordCodecBuilder.mapCodec(param1Instance -> {
/*    */             Objects.requireNonNull(param1Factory);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */             
/*    */             return param1Instance.group((App)Codec.STRING.optionalFieldOf("group", "").forGetter(SingleItemRecipe::group), (App)CookingBookCategory.CODEC.fieldOf("category").orElse(CookingBookCategory.MISC).forGetter(AbstractCookingRecipe::category), (App)Ingredient.CODEC.fieldOf("ingredient").forGetter(SingleItemRecipe::input), (App)ItemStack.STRICT_SINGLE_ITEM_CODEC.fieldOf("result").forGetter(SingleItemRecipe::result), (App)Codec.FLOAT.fieldOf("experience").orElse(Float.valueOf(0.0F)).forGetter(AbstractCookingRecipe::experience), (App)Codec.INT.fieldOf("cookingtime").orElse(Integer.valueOf(param1Int)).forGetter(AbstractCookingRecipe::cookingTime)).apply((Applicative)param1Instance, param1Factory::create);
/*    */           });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 82 */       Objects.requireNonNull(param1Factory); this.streamCodec = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SingleItemRecipe::group, CookingBookCategory.STREAM_CODEC, AbstractCookingRecipe::category, Ingredient.CONTENTS_STREAM_CODEC, SingleItemRecipe::input, ItemStack.STREAM_CODEC, SingleItemRecipe::result, ByteBufCodecs.FLOAT, AbstractCookingRecipe::experience, ByteBufCodecs.INT, AbstractCookingRecipe::cookingTime, param1Factory::create);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public MapCodec<T> codec() {
/* 88 */       return this.codec;
/*    */     }
/*    */ 
/*    */     
/*    */     public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
/* 93 */       return this.streamCodec;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\AbstractCookingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */