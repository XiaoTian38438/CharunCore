/*     */ package net.minecraft.data.loot;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.advancements.criterion.BlockPredicate;
/*     */ import net.minecraft.advancements.criterion.DataComponentMatchers;
/*     */ import net.minecraft.advancements.criterion.ItemPredicate;
/*     */ import net.minecraft.advancements.criterion.LocationPredicate;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.advancements.criterion.StatePropertiesPredicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicates;
/*     */ import net.minecraft.core.component.predicates.EnchantmentsPredicate;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.BeehiveBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CandleBlock;
/*     */ import net.minecraft.world.level.block.CaveVines;
/*     */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.block.DoublePlantBlock;
/*     */ import net.minecraft.world.level.block.FlowerPotBlock;
/*     */ import net.minecraft.world.level.block.MultifaceBlock;
/*     */ import net.minecraft.world.level.block.SegmentableBlock;
/*     */ import net.minecraft.world.level.block.SlabBlock;
/*     */ import net.minecraft.world.level.block.StemBlock;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.SlabType;
/*     */ import net.minecraft.world.level.storage.loot.IntRange;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
/*     */ import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
/*     */ import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
/*     */ import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
/*     */ import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LimitCount;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.MatchTool;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*     */ 
/*     */ public abstract class BlockLootSubProvider implements LootTableSubProvider {
/*     */   protected final HolderLookup.Provider registries;
/*     */   protected final Set<Item> explosionResistant;
/*     */   
/*     */   protected LootItemCondition.Builder hasSilkTouch() {
/*  85 */     return MatchTool.toolMatches(ItemPredicate.Builder.item().withComponents(
/*  86 */           DataComponentMatchers.Builder.components().partial(DataComponentPredicates.ENCHANTMENTS, (DataComponentPredicate)EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate((Holder)this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))).build()));
/*     */   }
/*     */   protected final FeatureFlagSet enabledFeatures; protected final Map<ResourceKey<LootTable>, LootTable.Builder> map;
/*     */   
/*     */   protected LootItemCondition.Builder doesNotHaveSilkTouch() {
/*  91 */     return hasSilkTouch().invert();
/*     */   }
/*     */   
/*     */   protected LootItemCondition.Builder hasShears() {
/*  95 */     return MatchTool.toolMatches(ItemPredicate.Builder.item().of((HolderGetter)this.registries.lookupOrThrow(Registries.ITEM), new ItemLike[] { (ItemLike)Items.SHEARS }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private LootItemCondition.Builder hasShearsOrSilkTouch() {
/* 101 */     return (LootItemCondition.Builder)hasShears().or(hasSilkTouch());
/*     */   }
/*     */   
/*     */   private LootItemCondition.Builder doesNotHaveShearsOrSilkTouch() {
/* 105 */     return hasShearsOrSilkTouch().invert();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   protected static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[] { 0.05F, 0.0625F, 0.083333336F, 0.1F };
/* 114 */   private static final float[] NORMAL_LEAVES_STICK_CHANCES = new float[] { 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F };
/*     */   
/*     */   protected BlockLootSubProvider(Set<Item> paramSet, FeatureFlagSet paramFeatureFlagSet, HolderLookup.Provider paramProvider) {
/* 117 */     this(paramSet, paramFeatureFlagSet, new HashMap<>(), paramProvider);
/*     */   }
/*     */   
/*     */   protected BlockLootSubProvider(Set<Item> paramSet, FeatureFlagSet paramFeatureFlagSet, Map<ResourceKey<LootTable>, LootTable.Builder> paramMap, HolderLookup.Provider paramProvider) {
/* 121 */     this.explosionResistant = paramSet;
/* 122 */     this.enabledFeatures = paramFeatureFlagSet;
/* 123 */     this.map = paramMap;
/* 124 */     this.registries = paramProvider;
/*     */   }
/*     */   
/*     */   protected <T extends FunctionUserBuilder<T>> T applyExplosionDecay(ItemLike paramItemLike, FunctionUserBuilder<T> paramFunctionUserBuilder) {
/* 128 */     if (!this.explosionResistant.contains(paramItemLike.asItem())) {
/* 129 */       return (T)paramFunctionUserBuilder.apply((LootItemFunction.Builder)ApplyExplosionDecay.explosionDecay());
/*     */     }
/*     */     
/* 132 */     return (T)paramFunctionUserBuilder.unwrap();
/*     */   }
/*     */   
/*     */   protected <T extends ConditionUserBuilder<T>> T applyExplosionCondition(ItemLike paramItemLike, ConditionUserBuilder<T> paramConditionUserBuilder) {
/* 136 */     if (!this.explosionResistant.contains(paramItemLike.asItem())) {
/* 137 */       return (T)paramConditionUserBuilder.when(ExplosionCondition.survivesExplosion());
/*     */     }
/*     */     
/* 140 */     return (T)paramConditionUserBuilder.unwrap();
/*     */   }
/*     */   
/*     */   public LootTable.Builder createSingleItemTable(ItemLike paramItemLike) {
/* 144 */     return LootTable.lootTable()
/* 145 */       .withPool(applyExplosionCondition(paramItemLike, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 146 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 147 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(paramItemLike))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static LootTable.Builder createSelfDropDispatchTable(Block paramBlock, LootItemCondition.Builder paramBuilder, LootPoolEntryContainer.Builder<?> paramBuilder1) {
/* 152 */     return LootTable.lootTable()
/* 153 */       .withPool(LootPool.lootPool()
/* 154 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 155 */         .add((LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)paramBlock)
/* 156 */           .when(paramBuilder))
/* 157 */           .otherwise(paramBuilder1)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createSilkTouchDispatchTable(Block paramBlock, LootPoolEntryContainer.Builder<?> paramBuilder) {
/* 163 */     return createSelfDropDispatchTable(paramBlock, hasSilkTouch(), paramBuilder);
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createShearsDispatchTable(Block paramBlock, LootPoolEntryContainer.Builder<?> paramBuilder) {
/* 167 */     return createSelfDropDispatchTable(paramBlock, hasShears(), paramBuilder);
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createSilkTouchOrShearsDispatchTable(Block paramBlock, LootPoolEntryContainer.Builder<?> paramBuilder) {
/* 171 */     return createSelfDropDispatchTable(paramBlock, hasShearsOrSilkTouch(), paramBuilder);
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createSingleItemTableWithSilkTouch(Block paramBlock, ItemLike paramItemLike) {
/* 175 */     return createSilkTouchDispatchTable(paramBlock, applyExplosionCondition((ItemLike)paramBlock, (ConditionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem(paramItemLike)));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createSingleItemTable(ItemLike paramItemLike, NumberProvider paramNumberProvider) {
/* 179 */     return LootTable.lootTable()
/* 180 */       .withPool(LootPool.lootPool()
/* 181 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 182 */         .add(applyExplosionDecay(paramItemLike, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem(paramItemLike).apply((LootItemFunction.Builder)SetItemCountFunction.setCount(paramNumberProvider)))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createSingleItemTableWithSilkTouch(Block paramBlock, ItemLike paramItemLike, NumberProvider paramNumberProvider) {
/* 187 */     return createSilkTouchDispatchTable(paramBlock, applyExplosionDecay((ItemLike)paramBlock, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem(paramItemLike).apply((LootItemFunction.Builder)SetItemCountFunction.setCount(paramNumberProvider))));
/*     */   }
/*     */   
/*     */   private LootTable.Builder createSilkTouchOnlyTable(ItemLike paramItemLike) {
/* 191 */     return LootTable.lootTable()
/* 192 */       .withPool(LootPool.lootPool()
/* 193 */         .when(hasSilkTouch())
/* 194 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 195 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(paramItemLike)));
/*     */   }
/*     */ 
/*     */   
/*     */   private LootTable.Builder createPotFlowerItemTable(ItemLike paramItemLike) {
/* 200 */     return LootTable.lootTable()
/* 201 */       .withPool(applyExplosionCondition((ItemLike)Blocks.FLOWER_POT, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 202 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 203 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.FLOWER_POT))))
/*     */       
/* 205 */       .withPool(applyExplosionCondition(paramItemLike, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 206 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 207 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(paramItemLike))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createSlabItemTable(Block paramBlock) {
/* 212 */     return LootTable.lootTable()
/* 213 */       .withPool(LootPool.lootPool()
/* 214 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 215 */         .add(applyExplosionDecay((ItemLike)paramBlock, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)paramBlock)
/* 216 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)).when(
/* 217 */                 (LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(paramBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SlabBlock.TYPE, (Comparable)SlabType.DOUBLE)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T extends Comparable<T> & net.minecraft.util.StringRepresentable> LootTable.Builder createSinglePropConditionTable(Block paramBlock, Property<T> paramProperty, T paramT) {
/* 224 */     return LootTable.lootTable()
/* 225 */       .withPool(applyExplosionCondition((ItemLike)paramBlock, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 226 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 227 */           .add(LootItem.lootTableItem((ItemLike)paramBlock)
/* 228 */             .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(paramBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(paramProperty, (Comparable)paramT))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createNameableBlockEntityTable(Block paramBlock) {
/* 234 */     return LootTable.lootTable()
/* 235 */       .withPool(applyExplosionCondition((ItemLike)paramBlock, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 236 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 237 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)paramBlock)
/* 238 */             .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 239 */               .include(DataComponents.CUSTOM_NAME)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createShulkerBoxDrop(Block paramBlock) {
/* 245 */     return LootTable.lootTable()
/* 246 */       .withPool(applyExplosionCondition((ItemLike)paramBlock, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 247 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 248 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)paramBlock)
/* 249 */             .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 250 */               .include(DataComponents.CUSTOM_NAME)
/* 251 */               .include(DataComponents.CONTAINER)
/* 252 */               .include(DataComponents.LOCK)
/* 253 */               .include(DataComponents.CONTAINER_LOOT)))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createCopperOreDrops(Block paramBlock) {
/* 260 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 261 */     return createSilkTouchDispatchTable(paramBlock, 
/* 262 */         applyExplosionDecay((ItemLike)paramBlock, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.RAW_COPPER)
/* 263 */           .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F)))
/* 264 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addOreBonusCount((Holder)registryLookup.getOrThrow(Enchantments.FORTUNE)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createLapisOreDrops(Block paramBlock) {
/* 270 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 271 */     return createSilkTouchDispatchTable(paramBlock, 
/* 272 */         applyExplosionDecay((ItemLike)paramBlock, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI)
/* 273 */           .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F)))
/* 274 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addOreBonusCount((Holder)registryLookup.getOrThrow(Enchantments.FORTUNE)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createRedstoneOreDrops(Block paramBlock) {
/* 280 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 281 */     return createSilkTouchDispatchTable(paramBlock, 
/* 282 */         applyExplosionDecay((ItemLike)paramBlock, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.REDSTONE)
/* 283 */           .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 5.0F)))
/* 284 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addUniformBonusCount((Holder)registryLookup.getOrThrow(Enchantments.FORTUNE)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createBannerDrop(Block paramBlock) {
/* 290 */     return LootTable.lootTable()
/* 291 */       .withPool(applyExplosionCondition((ItemLike)paramBlock, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 292 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 293 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)paramBlock)
/* 294 */             .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 295 */               .include(DataComponents.CUSTOM_NAME)
/* 296 */               .include(DataComponents.ITEM_NAME)
/* 297 */               .include(DataComponents.TOOLTIP_DISPLAY)
/* 298 */               .include(DataComponents.BANNER_PATTERNS)
/* 299 */               .include(DataComponents.RARITY)))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createBeeNestDrop(Block paramBlock) {
/* 306 */     return LootTable.lootTable()
/* 307 */       .withPool(LootPool.lootPool()
/* 308 */         .when(hasSilkTouch())
/* 309 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 310 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)paramBlock)
/* 311 */           .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 312 */             .include(DataComponents.BEES))
/*     */           
/* 314 */           .apply((LootItemFunction.Builder)CopyBlockState.copyState(paramBlock).copy((Property)BeehiveBlock.HONEY_LEVEL))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createBeeHiveDrop(Block paramBlock) {
/* 320 */     return LootTable.lootTable()
/* 321 */       .withPool(LootPool.lootPool()
/* 322 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 323 */         .add((LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)paramBlock)
/* 324 */           .when(hasSilkTouch()))
/* 325 */           .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 326 */             .include(DataComponents.BEES))
/*     */           
/* 328 */           .apply((LootItemFunction.Builder)CopyBlockState.copyState(paramBlock).copy((Property)BeehiveBlock.HONEY_LEVEL))
/* 329 */           .otherwise((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)paramBlock))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createCaveVinesDrop(Block paramBlock) {
/* 335 */     return LootTable.lootTable()
/* 336 */       .withPool(LootPool.lootPool()
/* 337 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLOW_BERRIES))
/* 338 */         .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(paramBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)CaveVines.BERRIES, true))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createCopperGolemStatueBlock(Block paramBlock) {
/* 343 */     return LootTable.lootTable()
/* 344 */       .withPool(applyExplosionCondition((ItemLike)paramBlock, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 345 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 346 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)paramBlock)
/* 347 */             .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 348 */               .include(DataComponents.CUSTOM_NAME))
/* 349 */             .apply((LootItemFunction.Builder)CopyBlockState.copyState(paramBlock).copy((Property)CopperGolemStatueBlock.POSE)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createOreDrop(Block paramBlock, Item paramItem) {
/* 355 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 356 */     return createSilkTouchDispatchTable(paramBlock, 
/* 357 */         applyExplosionDecay((ItemLike)paramBlock, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)paramItem)
/* 358 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addOreBonusCount((Holder)registryLookup.getOrThrow(Enchantments.FORTUNE)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createMushroomBlockDrop(Block paramBlock, ItemLike paramItemLike) {
/* 364 */     return createSilkTouchDispatchTable(paramBlock, applyExplosionDecay((ItemLike)paramBlock, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem(paramItemLike)
/* 365 */           .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(-6.0F, 2.0F)))
/* 366 */           .apply((LootItemFunction.Builder)LimitCount.limitCount(IntRange.lowerBound(0)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createGrassDrops(Block paramBlock) {
/* 372 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 373 */     return createShearsDispatchTable(paramBlock, applyExplosionDecay((ItemLike)paramBlock, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS)
/* 374 */           .when(LootItemRandomChanceCondition.randomChance(0.125F)))
/* 375 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addUniformBonusCount((Holder)registryLookup.getOrThrow(Enchantments.FORTUNE), 2))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LootTable.Builder createStemDrops(Block paramBlock, Item paramItem) {
/* 381 */     return LootTable.lootTable()
/* 382 */       .withPool(applyExplosionDecay((ItemLike)paramBlock, (FunctionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 383 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 384 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)paramItem)
/* 385 */             .apply(StemBlock.AGE.getPossibleValues(), paramInteger -> SetItemCountFunction.setCount((NumberProvider)BinomialDistributionGenerator.binomial(3, (paramInteger.intValue() + 1) / 15.0F)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(paramBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)StemBlock.AGE, paramInteger.intValue())))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LootTable.Builder createAttachedStemDrops(Block paramBlock, Item paramItem) {
/* 391 */     return LootTable.lootTable()
/* 392 */       .withPool(applyExplosionDecay((ItemLike)paramBlock, (FunctionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 393 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 394 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)paramItem)
/* 395 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)BinomialDistributionGenerator.binomial(3, 0.53333336F))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createShearsOnlyDrop(ItemLike paramItemLike) {
/* 401 */     return LootTable.lootTable()
/* 402 */       .withPool(LootPool.lootPool()
/* 403 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 404 */         .when(hasShears())
/* 405 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(paramItemLike)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createShearsOrSilkTouchOnlyDrop(ItemLike paramItemLike) {
/* 410 */     return LootTable.lootTable()
/* 411 */       .withPool(LootPool.lootPool()
/* 412 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 413 */         .when(hasShearsOrSilkTouch())
/* 414 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(paramItemLike)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createMultifaceBlockDrops(Block paramBlock, LootItemCondition.Builder paramBuilder) {
/* 419 */     return LootTable.lootTable()
/* 420 */       .withPool(LootPool.lootPool()
/* 421 */         .add(applyExplosionDecay((ItemLike)paramBlock, 
/* 422 */             (FunctionUserBuilder<LootPoolEntryContainer.Builder>)((LootPoolSingletonContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)paramBlock)
/* 423 */             .when(paramBuilder))
/* 424 */             .apply((Object[])Direction.values(), paramDirection -> SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F), true).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(paramBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)MultifaceBlock.getFaceProperty(paramDirection), true)))))
/* 425 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(-1.0F), true)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createMultifaceBlockDrops(Block paramBlock) {
/* 431 */     return LootTable.lootTable()
/* 432 */       .withPool(LootPool.lootPool()
/* 433 */         .add(applyExplosionDecay((ItemLike)paramBlock, 
/* 434 */             (FunctionUserBuilder<LootPoolEntryContainer.Builder>)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)paramBlock)
/* 435 */             .apply((Object[])Direction.values(), paramDirection -> SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F), true).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(paramBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)MultifaceBlock.getFaceProperty(paramDirection), true)))))
/* 436 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(-1.0F), true)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createMossyCarpetBlockDrops(Block paramBlock) {
/* 442 */     return LootTable.lootTable()
/* 443 */       .withPool(LootPool.lootPool()
/* 444 */         .add(applyExplosionDecay((ItemLike)paramBlock, 
/* 445 */             (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)paramBlock)
/* 446 */             .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(paramBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)MossyCarpetBlock.BASE, true))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createLeavesDrops(Block paramBlock1, Block paramBlock2, float... paramVarArgs) {
/* 452 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 453 */     return createSilkTouchOrShearsDispatchTable(paramBlock1, ((LootPoolSingletonContainer.Builder)
/* 454 */         applyExplosionCondition((ItemLike)paramBlock1, (ConditionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)paramBlock2)))
/* 455 */         .when(BonusLevelTableCondition.bonusLevelFlatChance((Holder)registryLookup.getOrThrow(Enchantments.FORTUNE), paramVarArgs)))
/*     */       
/* 457 */       .withPool(LootPool.lootPool()
/* 458 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 459 */         .when(doesNotHaveShearsOrSilkTouch())
/* 460 */         .add(((LootPoolSingletonContainer.Builder)applyExplosionDecay((ItemLike)paramBlock1, (FunctionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.STICK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F)))))
/* 461 */           .when(BonusLevelTableCondition.bonusLevelFlatChance((Holder)registryLookup.getOrThrow(Enchantments.FORTUNE), NORMAL_LEAVES_STICK_CHANCES))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createOakLeavesDrops(Block paramBlock1, Block paramBlock2, float... paramVarArgs) {
/* 467 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 468 */     return 
/* 469 */       createLeavesDrops(paramBlock1, paramBlock2, paramVarArgs)
/* 470 */       .withPool(LootPool.lootPool()
/* 471 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 472 */         .when(doesNotHaveShearsOrSilkTouch())
/* 473 */         .add(((LootPoolSingletonContainer.Builder)applyExplosionCondition((ItemLike)paramBlock1, (ConditionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.APPLE)))
/* 474 */           .when(BonusLevelTableCondition.bonusLevelFlatChance((Holder)registryLookup.getOrThrow(Enchantments.FORTUNE), new float[] { 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F }))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createMangroveLeavesDrops(Block paramBlock) {
/* 480 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 481 */     return createSilkTouchOrShearsDispatchTable(paramBlock, ((LootPoolSingletonContainer.Builder)
/* 482 */         applyExplosionDecay((ItemLike)Blocks.MANGROVE_LEAVES, (FunctionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.STICK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F)))))
/* 483 */         .when(BonusLevelTableCondition.bonusLevelFlatChance((Holder)registryLookup.getOrThrow(Enchantments.FORTUNE), NORMAL_LEAVES_STICK_CHANCES)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createCropDrops(Block paramBlock, Item paramItem1, Item paramItem2, LootItemCondition.Builder paramBuilder) {
/* 488 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 489 */     return applyExplosionDecay((ItemLike)paramBlock, (FunctionUserBuilder<LootTable.Builder>)LootTable.lootTable()
/* 490 */         .withPool(LootPool.lootPool()
/* 491 */           .add((LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)paramItem1)
/* 492 */             .when(paramBuilder))
/* 493 */             .otherwise((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)paramItem2))))
/*     */ 
/*     */         
/* 496 */         .withPool(LootPool.lootPool()
/* 497 */           .when(paramBuilder)
/* 498 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)paramItem2).apply((LootItemFunction.Builder)ApplyBonusCount.addBonusBinomialDistributionCount((Holder)registryLookup.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createDoublePlantShearsDrop(Block paramBlock) {
/* 504 */     return LootTable.lootTable().withPool(LootPool.lootPool()
/* 505 */         .when(hasShears())
/* 506 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)paramBlock).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createDoublePlantWithSeedDrops(Block paramBlock1, Block paramBlock2) {
/* 510 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.BLOCK);
/*     */ 
/*     */ 
/*     */     
/* 514 */     AlternativesEntry.Builder builder = ((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)paramBlock2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F))).when(hasShears())).otherwise(((LootPoolSingletonContainer.Builder)applyExplosionCondition((ItemLike)paramBlock1, (ConditionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS)))
/* 515 */         .when(LootItemRandomChanceCondition.randomChance(0.125F)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 520 */     return LootTable.lootTable()
/* 521 */       .withPool(
/* 522 */         LootPool.lootPool()
/* 523 */         .add((LootPoolEntryContainer.Builder)builder)
/* 524 */         .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(paramBlock1).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.LOWER)))
/* 525 */         .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup, new Block[] { paramBlock1 }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.UPPER))), new BlockPos(0, 1, 0))))
/*     */       
/* 527 */       .withPool(
/* 528 */         LootPool.lootPool()
/* 529 */         .add((LootPoolEntryContainer.Builder)builder)
/* 530 */         .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(paramBlock1).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.UPPER)))
/* 531 */         .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup, new Block[] { paramBlock1 }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.LOWER))), new BlockPos(0, -1, 0))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createCandleDrops(Block paramBlock) {
/* 536 */     return LootTable.lootTable()
/* 537 */       .withPool(LootPool.lootPool()
/* 538 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 539 */         .add(applyExplosionDecay((ItemLike)paramBlock, LootItem.lootTableItem((ItemLike)paramBlock)
/* 540 */             .apply(List.of(Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)), paramInteger -> SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(paramInteger.intValue())).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(paramBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)CandleBlock.CANDLES, paramInteger.intValue())))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LootTable.Builder createSegmentedBlockDrops(Block paramBlock) {
/* 546 */     if (paramBlock instanceof SegmentableBlock) { SegmentableBlock segmentableBlock = (SegmentableBlock)paramBlock;
/* 547 */       return LootTable.lootTable()
/* 548 */         .withPool(LootPool.lootPool()
/* 549 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 550 */           .add(applyExplosionDecay((ItemLike)paramBlock, LootItem.lootTableItem((ItemLike)paramBlock)
/* 551 */               .apply(IntStream.rangeClosed(1, 4).boxed().toList(), paramInteger -> SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(paramInteger.intValue())).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(paramBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)paramSegmentableBlock.getSegmentAmountProperty(), paramInteger.intValue()))))))); }
/*     */ 
/*     */ 
/*     */     
/* 555 */     return noDrop();
/*     */   }
/*     */   
/*     */   protected static LootTable.Builder createCandleCakeDrops(Block paramBlock) {
/* 559 */     return LootTable.lootTable()
/* 560 */       .withPool(LootPool.lootPool()
/* 561 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 562 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)paramBlock)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static LootTable.Builder noDrop() {
/* 567 */     return LootTable.lootTable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> paramBiConsumer) {
/* 574 */     generate();
/*     */     
/* 576 */     HashSet hashSet = new HashSet();
/* 577 */     for (Iterator<Block> iterator = BuiltInRegistries.BLOCK.iterator(); iterator.hasNext(); ) { Block block = iterator.next();
/* 578 */       if (!block.isEnabled(this.enabledFeatures)) {
/*     */         continue;
/*     */       }
/* 581 */       block.getLootTable().ifPresent(paramResourceKey -> {
/*     */             if (paramSet.add(paramResourceKey)) {
/*     */               LootTable.Builder builder = this.map.remove(paramResourceKey);
/*     */               
/*     */               if (builder == null) {
/*     */                 throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", new Object[] { paramResourceKey.identifier(), BuiltInRegistries.BLOCK.getKey(paramBlock) }));
/*     */               }
/*     */               paramBiConsumer.accept(paramResourceKey, builder);
/*     */             } 
/*     */           }); }
/*     */     
/* 592 */     if (!this.map.isEmpty()) {
/* 593 */       throw new IllegalStateException("Created block loot tables for non-blocks: " + String.valueOf(this.map.keySet()));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addNetherVinesDropTable(Block paramBlock1, Block paramBlock2) {
/* 598 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 599 */     LootTable.Builder builder = createSilkTouchOrShearsDispatchTable(paramBlock1, 
/* 600 */         LootItem.lootTableItem((ItemLike)paramBlock1).when(BonusLevelTableCondition.bonusLevelFlatChance((Holder)registryLookup.getOrThrow(Enchantments.FORTUNE), new float[] { 0.33F, 0.55F, 0.77F, 1.0F })));
/* 601 */     add(paramBlock1, builder);
/* 602 */     add(paramBlock2, builder);
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createDoorTable(Block paramBlock) {
/* 606 */     return createSinglePropConditionTable(paramBlock, (Property<DoubleBlockHalf>)DoorBlock.HALF, DoubleBlockHalf.LOWER);
/*     */   }
/*     */   
/*     */   protected void dropPottedContents(Block paramBlock) {
/* 610 */     add(paramBlock, paramBlock -> createPotFlowerItemTable((ItemLike)((FlowerPotBlock)paramBlock).getPotted()));
/*     */   }
/*     */   
/*     */   protected void otherWhenSilkTouch(Block paramBlock1, Block paramBlock2) {
/* 614 */     add(paramBlock1, createSilkTouchOnlyTable((ItemLike)paramBlock2));
/*     */   }
/*     */   
/*     */   protected void dropOther(Block paramBlock, ItemLike paramItemLike) {
/* 618 */     add(paramBlock, createSingleItemTable(paramItemLike));
/*     */   }
/*     */   
/*     */   protected void dropWhenSilkTouch(Block paramBlock) {
/* 622 */     otherWhenSilkTouch(paramBlock, paramBlock);
/*     */   }
/*     */   
/*     */   protected void dropSelf(Block paramBlock) {
/* 626 */     dropOther(paramBlock, (ItemLike)paramBlock);
/*     */   }
/*     */   
/*     */   protected void add(Block paramBlock, Function<Block, LootTable.Builder> paramFunction) {
/* 630 */     add(paramBlock, paramFunction.apply(paramBlock));
/*     */   }
/*     */   
/*     */   protected void add(Block paramBlock, LootTable.Builder paramBuilder) {
/* 634 */     this.map.put((ResourceKey<LootTable>)paramBlock.getLootTable().orElseThrow(() -> new IllegalStateException("Block " + String.valueOf(paramBlock) + " does not have loot table")), paramBuilder);
/*     */   }
/*     */   
/*     */   protected abstract void generate();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\loot\BlockLootSubProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */