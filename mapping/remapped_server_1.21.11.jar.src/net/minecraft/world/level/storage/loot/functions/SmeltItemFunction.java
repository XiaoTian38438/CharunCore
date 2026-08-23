/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ import net.minecraft.world.item.crafting.SingleRecipeInput;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class SmeltItemFunction extends LootItemConditionalFunction {
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger(); public static final MapCodec<SmeltItemFunction> CODEC;
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).apply((Applicative)paramInstance, SmeltItemFunction::new));
/*    */   }
/*    */   private SmeltItemFunction(List<LootItemCondition> paramList) {
/* 24 */     super(paramList);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SmeltItemFunction> getType() {
/* 29 */     return LootItemFunctions.FURNACE_SMELT;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 34 */     if (paramItemStack.isEmpty()) {
/* 35 */       return paramItemStack;
/*    */     }
/*    */     
/* 38 */     SingleRecipeInput singleRecipeInput = new SingleRecipeInput(paramItemStack);
/* 39 */     Optional<RecipeHolder> optional = paramLootContext.getLevel().recipeAccess().getRecipeFor(RecipeType.SMELTING, (RecipeInput)singleRecipeInput, (Level)paramLootContext.getLevel());
/* 40 */     if (optional.isPresent()) {
/* 41 */       ItemStack itemStack = ((SmeltingRecipe)((RecipeHolder)optional.get()).value()).assemble(singleRecipeInput, (HolderLookup.Provider)paramLootContext.getLevel().registryAccess());
/*    */       
/* 43 */       if (!itemStack.isEmpty()) {
/* 44 */         return itemStack.copyWithCount(paramItemStack.getCount());
/*    */       }
/*    */     } 
/*    */     
/* 48 */     LOGGER.warn("Couldn't smelt {} because there is no smelting recipe", paramItemStack);
/* 49 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> smelted() {
/* 53 */     return simpleBuilder(SmeltItemFunction::new);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SmeltItemFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */