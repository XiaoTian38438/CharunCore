/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class SetEnchantmentsFunction extends LootItemConditionalFunction {
/*    */   static {
/* 25 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)Codec.unboundedMap(Enchantment.CODEC, NumberProviders.CODEC).optionalFieldOf("enchantments", Map.of()).forGetter(()), (App)Codec.BOOL.fieldOf("add").orElse(Boolean.valueOf(false)).forGetter(()))).apply((Applicative)paramInstance, SetEnchantmentsFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SetEnchantmentsFunction> CODEC;
/*    */   private final Map<Holder<Enchantment>, NumberProvider> enchantments;
/*    */   private final boolean add;
/*    */   
/*    */   SetEnchantmentsFunction(List<LootItemCondition> paramList, Map<Holder<Enchantment>, NumberProvider> paramMap, boolean paramBoolean) {
/* 34 */     super(paramList);
/* 35 */     this.enchantments = Map.copyOf(paramMap);
/* 36 */     this.add = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetEnchantmentsFunction> getType() {
/* 41 */     return LootItemFunctions.SET_ENCHANTMENTS;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 46 */     return (Set<ContextKey<?>>)this.enchantments.values().stream().flatMap(paramNumberProvider -> paramNumberProvider.getReferencedContextParams().stream()).collect(ImmutableSet.toImmutableSet());
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 51 */     if (paramItemStack.is(Items.BOOK)) {
/* 52 */       paramItemStack = paramItemStack.transmuteCopy((ItemLike)Items.ENCHANTED_BOOK);
/*    */     }
/*    */     
/* 55 */     EnchantmentHelper.updateEnchantments(paramItemStack, paramMutable -> {
/*    */           if (this.add) {
/*    */             this.enchantments.forEach(());
/*    */           } else {
/*    */             this.enchantments.forEach(());
/*    */           } 
/*    */         });
/* 62 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/* 66 */     private final ImmutableMap.Builder<Holder<Enchantment>, NumberProvider> enchantments = ImmutableMap.builder();
/*    */     private final boolean add;
/*    */     
/*    */     public Builder() {
/* 70 */       this(false);
/*    */     }
/*    */     
/*    */     public Builder(boolean param1Boolean) {
/* 74 */       this.add = param1Boolean;
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 79 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withEnchantment(Holder<Enchantment> param1Holder, NumberProvider param1NumberProvider) {
/* 83 */       this.enchantments.put(param1Holder, param1NumberProvider);
/* 84 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemFunction build() {
/* 89 */       return new SetEnchantmentsFunction(getConditions(), (Map<Holder<Enchantment>, NumberProvider>)this.enchantments.build(), this.add);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetEnchantmentsFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */