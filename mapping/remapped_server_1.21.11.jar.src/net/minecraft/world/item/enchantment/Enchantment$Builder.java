/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
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
/*     */ public class Builder
/*     */ {
/*     */   private final Enchantment.EnchantmentDefinition definition;
/* 493 */   private HolderSet<Enchantment> exclusiveSet = (HolderSet<Enchantment>)HolderSet.direct(new net.minecraft.core.Holder[0]);
/* 494 */   private final Map<DataComponentType<?>, List<?>> effectLists = new HashMap<>();
/* 495 */   private final DataComponentMap.Builder effectMapBuilder = DataComponentMap.builder();
/*     */   
/*     */   public Builder(Enchantment.EnchantmentDefinition paramEnchantmentDefinition) {
/* 498 */     this.definition = paramEnchantmentDefinition;
/*     */   }
/*     */   
/*     */   public Builder exclusiveWith(HolderSet<Enchantment> paramHolderSet) {
/* 502 */     this.exclusiveSet = paramHolderSet;
/* 503 */     return this;
/*     */   }
/*     */   
/*     */   public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> paramDataComponentType, E paramE, LootItemCondition.Builder paramBuilder) {
/* 507 */     getEffectsList(paramDataComponentType).add(new ConditionalEffect<>(paramE, Optional.of(paramBuilder.build())));
/* 508 */     return this;
/*     */   }
/*     */   
/*     */   public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> paramDataComponentType, E paramE) {
/* 512 */     getEffectsList(paramDataComponentType).add(new ConditionalEffect<>(paramE, Optional.empty()));
/* 513 */     return this;
/*     */   }
/*     */   
/*     */   public <E> Builder withEffect(DataComponentType<List<TargetedConditionalEffect<E>>> paramDataComponentType, EnchantmentTarget paramEnchantmentTarget1, EnchantmentTarget paramEnchantmentTarget2, E paramE, LootItemCondition.Builder paramBuilder) {
/* 517 */     getEffectsList(paramDataComponentType).add(new TargetedConditionalEffect<>(paramEnchantmentTarget1, paramEnchantmentTarget2, paramE, Optional.of(paramBuilder.build())));
/* 518 */     return this;
/*     */   }
/*     */   
/*     */   public <E> Builder withEffect(DataComponentType<List<TargetedConditionalEffect<E>>> paramDataComponentType, EnchantmentTarget paramEnchantmentTarget1, EnchantmentTarget paramEnchantmentTarget2, E paramE) {
/* 522 */     getEffectsList(paramDataComponentType).add(new TargetedConditionalEffect<>(paramEnchantmentTarget1, paramEnchantmentTarget2, paramE, Optional.empty()));
/* 523 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withEffect(DataComponentType<List<EnchantmentAttributeEffect>> paramDataComponentType, EnchantmentAttributeEffect paramEnchantmentAttributeEffect) {
/* 527 */     getEffectsList(paramDataComponentType).add(paramEnchantmentAttributeEffect);
/* 528 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public <E> Builder withSpecialEffect(DataComponentType<E> paramDataComponentType, E paramE) {
/* 533 */     this.effectMapBuilder.set(paramDataComponentType, paramE);
/* 534 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withEffect(DataComponentType<Unit> paramDataComponentType) {
/* 538 */     this.effectMapBuilder.set(paramDataComponentType, Unit.INSTANCE);
/* 539 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private <E> List<E> getEffectsList(DataComponentType<List<E>> paramDataComponentType) {
/* 544 */     return (List<E>)this.effectLists.computeIfAbsent(paramDataComponentType, paramDataComponentType2 -> {
/*     */           ArrayList arrayList = new ArrayList();
/*     */           this.effectMapBuilder.set(paramDataComponentType1, arrayList);
/*     */           return arrayList;
/*     */         });
/*     */   }
/*     */   
/*     */   public Enchantment build(Identifier paramIdentifier) {
/* 552 */     return new Enchantment((Component)Component.translatable(Util.makeDescriptionId("enchantment", paramIdentifier)), this.definition, this.exclusiveSet, this.effectMapBuilder.build());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\enchantment\Enchantment$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */