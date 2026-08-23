/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class LootItemFunctions {
/*    */   static {
/* 18 */     IDENTITY = ((paramItemStack, paramLootContext) -> paramItemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   public static final BiFunction<ItemStack, LootContext, ItemStack> IDENTITY;
/* 23 */   public static final Codec<LootItemFunction> TYPED_CODEC = BuiltInRegistries.LOOT_FUNCTION_TYPE.byNameCodec()
/* 24 */     .dispatch("function", LootItemFunction::getType, LootItemFunctionType::codec);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static final Codec<LootItemFunction> ROOT_CODEC = Codec.lazyInitialized(() -> Codec.withAlternative(TYPED_CODEC, SequenceFunction.INLINE_CODEC));
/*    */   
/* 31 */   public static final Codec<Holder<LootItemFunction>> CODEC = (Codec<Holder<LootItemFunction>>)RegistryFileCodec.create(Registries.ITEM_MODIFIER, ROOT_CODEC);
/*    */   
/* 33 */   public static final LootItemFunctionType<SetItemCountFunction> SET_COUNT = register("set_count", SetItemCountFunction.CODEC);
/* 34 */   public static final LootItemFunctionType<SetItemFunction> SET_ITEM = register("set_item", SetItemFunction.CODEC);
/* 35 */   public static final LootItemFunctionType<EnchantWithLevelsFunction> ENCHANT_WITH_LEVELS = register("enchant_with_levels", EnchantWithLevelsFunction.CODEC);
/* 36 */   public static final LootItemFunctionType<EnchantRandomlyFunction> ENCHANT_RANDOMLY = register("enchant_randomly", EnchantRandomlyFunction.CODEC);
/* 37 */   public static final LootItemFunctionType<SetEnchantmentsFunction> SET_ENCHANTMENTS = register("set_enchantments", SetEnchantmentsFunction.CODEC);
/* 38 */   public static final LootItemFunctionType<SetCustomDataFunction> SET_CUSTOM_DATA = register("set_custom_data", SetCustomDataFunction.CODEC);
/* 39 */   public static final LootItemFunctionType<SetComponentsFunction> SET_COMPONENTS = register("set_components", SetComponentsFunction.CODEC);
/* 40 */   public static final LootItemFunctionType<SmeltItemFunction> FURNACE_SMELT = register("furnace_smelt", SmeltItemFunction.CODEC);
/* 41 */   public static final LootItemFunctionType<EnchantedCountIncreaseFunction> ENCHANTED_COUNT_INCREASE = register("enchanted_count_increase", EnchantedCountIncreaseFunction.CODEC);
/* 42 */   public static final LootItemFunctionType<SetItemDamageFunction> SET_DAMAGE = register("set_damage", SetItemDamageFunction.CODEC);
/* 43 */   public static final LootItemFunctionType<SetAttributesFunction> SET_ATTRIBUTES = register("set_attributes", SetAttributesFunction.CODEC);
/* 44 */   public static final LootItemFunctionType<SetNameFunction> SET_NAME = register("set_name", SetNameFunction.CODEC);
/* 45 */   public static final LootItemFunctionType<ExplorationMapFunction> EXPLORATION_MAP = register("exploration_map", ExplorationMapFunction.CODEC);
/* 46 */   public static final LootItemFunctionType<SetStewEffectFunction> SET_STEW_EFFECT = register("set_stew_effect", SetStewEffectFunction.CODEC);
/* 47 */   public static final LootItemFunctionType<CopyNameFunction> COPY_NAME = register("copy_name", CopyNameFunction.CODEC);
/* 48 */   public static final LootItemFunctionType<SetContainerContents> SET_CONTENTS = register("set_contents", SetContainerContents.CODEC);
/* 49 */   public static final LootItemFunctionType<ModifyContainerContents> MODIFY_CONTENTS = register("modify_contents", ModifyContainerContents.CODEC);
/* 50 */   public static final LootItemFunctionType<FilteredFunction> FILTERED = register("filtered", FilteredFunction.CODEC);
/* 51 */   public static final LootItemFunctionType<LimitCount> LIMIT_COUNT = register("limit_count", LimitCount.CODEC);
/* 52 */   public static final LootItemFunctionType<ApplyBonusCount> APPLY_BONUS = register("apply_bonus", ApplyBonusCount.CODEC);
/* 53 */   public static final LootItemFunctionType<SetContainerLootTable> SET_LOOT_TABLE = register("set_loot_table", SetContainerLootTable.CODEC);
/* 54 */   public static final LootItemFunctionType<ApplyExplosionDecay> EXPLOSION_DECAY = register("explosion_decay", ApplyExplosionDecay.CODEC);
/* 55 */   public static final LootItemFunctionType<SetLoreFunction> SET_LORE = register("set_lore", SetLoreFunction.CODEC);
/* 56 */   public static final LootItemFunctionType<FillPlayerHead> FILL_PLAYER_HEAD = register("fill_player_head", FillPlayerHead.CODEC);
/* 57 */   public static final LootItemFunctionType<CopyCustomDataFunction> COPY_CUSTOM_DATA = register("copy_custom_data", CopyCustomDataFunction.CODEC);
/* 58 */   public static final LootItemFunctionType<CopyBlockState> COPY_STATE = register("copy_state", CopyBlockState.CODEC);
/* 59 */   public static final LootItemFunctionType<SetBannerPatternFunction> SET_BANNER_PATTERN = register("set_banner_pattern", SetBannerPatternFunction.CODEC);
/* 60 */   public static final LootItemFunctionType<SetPotionFunction> SET_POTION = register("set_potion", SetPotionFunction.CODEC);
/* 61 */   public static final LootItemFunctionType<SetInstrumentFunction> SET_INSTRUMENT = register("set_instrument", SetInstrumentFunction.CODEC);
/* 62 */   public static final LootItemFunctionType<FunctionReference> REFERENCE = register("reference", FunctionReference.CODEC);
/* 63 */   public static final LootItemFunctionType<SequenceFunction> SEQUENCE = register("sequence", SequenceFunction.CODEC);
/* 64 */   public static final LootItemFunctionType<CopyComponentsFunction> COPY_COMPONENTS = register("copy_components", CopyComponentsFunction.CODEC);
/* 65 */   public static final LootItemFunctionType<SetFireworksFunction> SET_FIREWORKS = register("set_fireworks", SetFireworksFunction.CODEC);
/* 66 */   public static final LootItemFunctionType<SetFireworkExplosionFunction> SET_FIREWORK_EXPLOSION = register("set_firework_explosion", SetFireworkExplosionFunction.CODEC);
/* 67 */   public static final LootItemFunctionType<SetBookCoverFunction> SET_BOOK_COVER = register("set_book_cover", SetBookCoverFunction.CODEC);
/* 68 */   public static final LootItemFunctionType<SetWrittenBookPagesFunction> SET_WRITTEN_BOOK_PAGES = register("set_written_book_pages", SetWrittenBookPagesFunction.CODEC);
/* 69 */   public static final LootItemFunctionType<SetWritableBookPagesFunction> SET_WRITABLE_BOOK_PAGES = register("set_writable_book_pages", SetWritableBookPagesFunction.CODEC);
/* 70 */   public static final LootItemFunctionType<ToggleTooltips> TOGGLE_TOOLTIPS = register("toggle_tooltips", ToggleTooltips.CODEC);
/* 71 */   public static final LootItemFunctionType<SetOminousBottleAmplifierFunction> SET_OMINOUS_BOTTLE_AMPLIFIER = register("set_ominous_bottle_amplifier", SetOminousBottleAmplifierFunction.CODEC);
/* 72 */   public static final LootItemFunctionType<SetCustomModelDataFunction> SET_CUSTOM_MODEL_DATA = register("set_custom_model_data", SetCustomModelDataFunction.CODEC);
/* 73 */   public static final LootItemFunctionType<DiscardItem> DISCARD = register("discard", DiscardItem.CODEC);
/*    */   
/*    */   private static <T extends LootItemFunction> LootItemFunctionType<T> register(String paramString, MapCodec<T> paramMapCodec) {
/* 76 */     return (LootItemFunctionType<T>)Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, Identifier.withDefaultNamespace(paramString), new LootItemFunctionType<>(paramMapCodec));
/*    */   }
/*    */   public static BiFunction<ItemStack, LootContext, ItemStack> compose(List<? extends BiFunction<ItemStack, LootContext, ItemStack>> paramList) {
/*    */     BiFunction biFunction1, biFunction2;
/* 80 */     List<BiFunction<ItemStack, LootContext, ItemStack>> list = List.copyOf(paramList);
/* 81 */     switch (list.size()) { case 0: 
/*    */       case 1:
/*    */       
/*    */       case 2:
/* 85 */         biFunction1 = list.get(0);
/* 86 */         biFunction2 = list.get(1); }
/*    */     
/*    */     return (paramItemStack, paramLootContext) -> {
/*    */         for (BiFunction<ItemStack, LootContext, ItemStack> biFunction : (Iterable<BiFunction<ItemStack, LootContext, ItemStack>>)paramList)
/*    */           paramItemStack = biFunction.apply(paramItemStack, paramLootContext); 
/*    */         return paramItemStack;
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\LootItemFunctions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */