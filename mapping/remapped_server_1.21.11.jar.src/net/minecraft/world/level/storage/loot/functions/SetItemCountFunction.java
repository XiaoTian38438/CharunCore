/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class SetItemCountFunction extends LootItemConditionalFunction {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)NumberProviders.CODEC.fieldOf("count").forGetter(()), (App)Codec.BOOL.fieldOf("add").orElse(Boolean.valueOf(false)).forGetter(()))).apply((Applicative)paramInstance, SetItemCountFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SetItemCountFunction> CODEC;
/*    */   private final NumberProvider value;
/*    */   private final boolean add;
/*    */   
/*    */   private SetItemCountFunction(List<LootItemCondition> paramList, NumberProvider paramNumberProvider, boolean paramBoolean) {
/* 26 */     super(paramList);
/* 27 */     this.value = paramNumberProvider;
/* 28 */     this.add = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetItemCountFunction> getType() {
/* 33 */     return LootItemFunctions.SET_COUNT;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 38 */     return this.value.getReferencedContextParams();
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 43 */     byte b = this.add ? paramItemStack.getCount() : 0;
/* 44 */     paramItemStack.setCount(b + this.value.getInt(paramLootContext));
/* 45 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setCount(NumberProvider paramNumberProvider) {
/* 49 */     return simpleBuilder(paramList -> new SetItemCountFunction(paramList, paramNumberProvider, false));
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setCount(NumberProvider paramNumberProvider, boolean paramBoolean) {
/* 53 */     return simpleBuilder(paramList -> new SetItemCountFunction(paramList, paramNumberProvider, paramBoolean));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetItemCountFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */