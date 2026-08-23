/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ 
/*     */ public class EnchantedCountIncreaseFunction extends LootItemConditionalFunction {
/*     */   static {
/*  30 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)Enchantment.CODEC.fieldOf("enchantment").forGetter(()), (App)NumberProviders.CODEC.fieldOf("count").forGetter(()), (App)Codec.INT.optionalFieldOf("limit", Integer.valueOf(0)).forGetter(()))).apply((Applicative)paramInstance, EnchantedCountIncreaseFunction::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int NO_LIMIT = 0;
/*     */   public static final MapCodec<EnchantedCountIncreaseFunction> CODEC;
/*     */   private final Holder<Enchantment> enchantment;
/*     */   private final NumberProvider value;
/*     */   private final int limit;
/*     */   
/*     */   EnchantedCountIncreaseFunction(List<LootItemCondition> paramList, Holder<Enchantment> paramHolder, NumberProvider paramNumberProvider, int paramInt) {
/*  41 */     super(paramList);
/*  42 */     this.enchantment = paramHolder;
/*  43 */     this.value = paramNumberProvider;
/*  44 */     this.limit = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType<EnchantedCountIncreaseFunction> getType() {
/*  49 */     return LootItemFunctions.ENCHANTED_COUNT_INCREASE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<ContextKey<?>> getReferencedContextParams() {
/*  54 */     return (Set<ContextKey<?>>)Sets.union((Set)ImmutableSet.of(LootContextParams.ATTACKING_ENTITY), this.value.getReferencedContextParams());
/*     */   }
/*     */   
/*     */   private boolean hasLimit() {
/*  58 */     return (this.limit > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/*  63 */     Entity entity = (Entity)paramLootContext.getOptionalParameter(LootContextParams.ATTACKING_ENTITY);
/*     */     
/*  65 */     if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/*  66 */       int i = EnchantmentHelper.getEnchantmentLevel(this.enchantment, livingEntity);
/*  67 */       if (i == 0) {
/*  68 */         return paramItemStack;
/*     */       }
/*  70 */       float f = i * this.value.getFloat(paramLootContext);
/*  71 */       paramItemStack.grow(Math.round(f));
/*     */       
/*  73 */       if (hasLimit()) {
/*  74 */         paramItemStack.limitSize(this.limit);
/*     */       } }
/*     */ 
/*     */     
/*  78 */     return paramItemStack;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*     */     private final Holder<Enchantment> enchantment;
/*     */     private final NumberProvider count;
/*  84 */     private int limit = 0;
/*     */     
/*     */     public Builder(Holder<Enchantment> param1Holder, NumberProvider param1NumberProvider) {
/*  87 */       this.enchantment = param1Holder;
/*  88 */       this.count = param1NumberProvider;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Builder getThis() {
/*  93 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setLimit(int param1Int) {
/*  97 */       this.limit = param1Int;
/*  98 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootItemFunction build() {
/* 103 */       return new EnchantedCountIncreaseFunction(getConditions(), this.enchantment, this.count, this.limit);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder lootingMultiplier(HolderLookup.Provider paramProvider, NumberProvider paramNumberProvider) {
/* 108 */     HolderLookup.RegistryLookup registryLookup = paramProvider.lookupOrThrow(Registries.ENCHANTMENT);
/* 109 */     return new Builder((Holder<Enchantment>)registryLookup.getOrThrow(Enchantments.LOOTING), paramNumberProvider);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\EnchantedCountIncreaseFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */