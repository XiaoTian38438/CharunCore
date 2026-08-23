/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EnchantRandomlyFunction extends LootItemConditionalFunction {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger(); public static final MapCodec<EnchantRandomlyFunction> CODEC;
/*     */   static {
/*  31 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).optionalFieldOf("options").forGetter(()), (App)Codec.BOOL.optionalFieldOf("only_compatible", Boolean.valueOf(true)).forGetter(()))).apply((Applicative)paramInstance, EnchantRandomlyFunction::new));
/*     */   }
/*     */ 
/*     */   
/*     */   private final Optional<HolderSet<Enchantment>> options;
/*     */   
/*     */   private final boolean onlyCompatible;
/*     */   
/*     */   EnchantRandomlyFunction(List<LootItemCondition> paramList, Optional<HolderSet<Enchantment>> paramOptional, boolean paramBoolean) {
/*  40 */     super(paramList);
/*  41 */     this.options = paramOptional;
/*  42 */     this.onlyCompatible = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType<EnchantRandomlyFunction> getType() {
/*  47 */     return LootItemFunctions.ENCHANT_RANDOMLY;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/*  52 */     RandomSource randomSource = paramLootContext.getRandom();
/*  53 */     boolean bool = paramItemStack.is(Items.BOOK);
/*  54 */     boolean bool1 = (!bool && this.onlyCompatible) ? true : false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     Stream stream = ((Stream)this.options.<Stream>map(HolderSet::stream).orElseGet(() -> paramLootContext.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).listElements().map(Function.identity()))).filter(paramHolder -> (!paramBoolean || ((Enchantment)paramHolder.value()).canEnchant(paramItemStack)));
/*  62 */     List list = stream.toList();
/*  63 */     Optional<Holder<Enchantment>> optional = Util.getRandomSafe(list, randomSource);
/*  64 */     if (optional.isEmpty()) {
/*  65 */       LOGGER.warn("Couldn't find a compatible enchantment for {}", paramItemStack);
/*  66 */       return paramItemStack;
/*     */     } 
/*     */     
/*  69 */     return enchantItem(paramItemStack, optional.get(), randomSource);
/*     */   }
/*     */   
/*     */   private static ItemStack enchantItem(ItemStack paramItemStack, Holder<Enchantment> paramHolder, RandomSource paramRandomSource) {
/*  73 */     int i = Mth.nextInt(paramRandomSource, ((Enchantment)paramHolder.value()).getMinLevel(), ((Enchantment)paramHolder.value()).getMaxLevel());
/*  74 */     if (paramItemStack.is(Items.BOOK)) {
/*  75 */       paramItemStack = new ItemStack((ItemLike)Items.ENCHANTED_BOOK);
/*     */     }
/*  77 */     paramItemStack.enchant(paramHolder, i);
/*  78 */     return paramItemStack;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*  82 */     private Optional<HolderSet<Enchantment>> options = Optional.empty();
/*     */     
/*     */     private boolean onlyCompatible = true;
/*     */     
/*     */     protected Builder getThis() {
/*  87 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withEnchantment(Holder<Enchantment> param1Holder) {
/*  91 */       this.options = Optional.of(HolderSet.direct(new Holder[] { param1Holder }));
/*  92 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withOneOf(HolderSet<Enchantment> param1HolderSet) {
/*  96 */       this.options = Optional.of(param1HolderSet);
/*  97 */       return this;
/*     */     }
/*     */     
/*     */     public Builder allowingIncompatibleEnchantments() {
/* 101 */       this.onlyCompatible = false;
/* 102 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootItemFunction build() {
/* 107 */       return new EnchantRandomlyFunction(getConditions(), this.options, this.onlyCompatible);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder randomEnchantment() {
/* 112 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static Builder randomApplicableEnchantment(HolderLookup.Provider paramProvider) {
/* 116 */     return randomEnchantment().withOneOf((HolderSet<Enchantment>)paramProvider.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.ON_RANDOM_LOOT));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\EnchantRandomlyFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */