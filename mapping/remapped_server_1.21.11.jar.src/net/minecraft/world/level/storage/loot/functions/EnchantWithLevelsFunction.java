/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.EnchantmentTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class EnchantWithLevelsFunction extends LootItemConditionalFunction {
/*    */   static {
/* 26 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)NumberProviders.CODEC.fieldOf("levels").forGetter(()), (App)RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).optionalFieldOf("options").forGetter(()))).apply((Applicative)paramInstance, EnchantWithLevelsFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<EnchantWithLevelsFunction> CODEC;
/*    */   private final NumberProvider levels;
/*    */   private final Optional<HolderSet<Enchantment>> options;
/*    */   
/*    */   EnchantWithLevelsFunction(List<LootItemCondition> paramList, NumberProvider paramNumberProvider, Optional<HolderSet<Enchantment>> paramOptional) {
/* 35 */     super(paramList);
/* 36 */     this.levels = paramNumberProvider;
/* 37 */     this.options = paramOptional;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<EnchantWithLevelsFunction> getType() {
/* 42 */     return LootItemFunctions.ENCHANT_WITH_LEVELS;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 47 */     return this.levels.getReferencedContextParams();
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 52 */     RandomSource randomSource = paramLootContext.getRandom();
/* 53 */     RegistryAccess registryAccess = paramLootContext.getLevel().registryAccess();
/* 54 */     return EnchantmentHelper.enchantItem(randomSource, paramItemStack, this.levels.getInt(paramLootContext), registryAccess, this.options);
/*    */   }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*    */     private final NumberProvider levels;
/* 59 */     private Optional<HolderSet<Enchantment>> options = Optional.empty();
/*    */     
/*    */     public Builder(NumberProvider param1NumberProvider) {
/* 62 */       this.levels = param1NumberProvider;
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 67 */       return this;
/*    */     }
/*    */     
/*    */     public Builder fromOptions(HolderSet<Enchantment> param1HolderSet) {
/* 71 */       this.options = Optional.of(param1HolderSet);
/* 72 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemFunction build() {
/* 77 */       return new EnchantWithLevelsFunction(getConditions(), this.levels, this.options);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder enchantWithLevels(HolderLookup.Provider paramProvider, NumberProvider paramNumberProvider) {
/* 82 */     return (new Builder(paramNumberProvider)).fromOptions((HolderSet<Enchantment>)paramProvider.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.ON_RANDOM_LOOT));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\EnchantWithLevelsFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */