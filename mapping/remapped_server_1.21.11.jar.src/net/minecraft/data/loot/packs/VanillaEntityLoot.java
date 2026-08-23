/*     */ package net.minecraft.data.loot.packs;
/*     */ import net.minecraft.advancements.criterion.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.criterion.EntityFlagsPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityPredicate;
/*     */ import net.minecraft.advancements.criterion.EntitySubPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityTypePredicate;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.advancements.criterion.RaiderPredicate;
/*     */ import net.minecraft.advancements.criterion.SlimePredicate;
/*     */ import net.minecraft.advancements.criterion.TagPredicate;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.loot.EntityLootSubProvider;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariants;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.TagEntry;
/*     */ import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetOminousBottleAmplifierFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*     */ 
/*     */ public class VanillaEntityLoot extends EntityLootSubProvider {
/*     */   public VanillaEntityLoot(HolderLookup.Provider paramProvider) {
/*  54 */     super(FeatureFlags.REGISTRY.allFlags(), paramProvider);
/*     */   }
/*     */ 
/*     */   
/*     */   public void generate() {
/*  59 */     HolderLookup.RegistryLookup registryLookup1 = this.registries.lookupOrThrow(Registries.ENTITY_TYPE);
/*  60 */     HolderLookup.RegistryLookup registryLookup2 = this.registries.lookupOrThrow(Registries.FROG_VARIANT);
/*     */     
/*  62 */     add(EntityType.ALLAY, 
/*  63 */         LootTable.lootTable());
/*     */ 
/*     */     
/*  66 */     add(EntityType.ARMADILLO, 
/*  67 */         LootTable.lootTable());
/*     */ 
/*     */     
/*  70 */     add(EntityType.ARMOR_STAND, 
/*  71 */         LootTable.lootTable());
/*     */ 
/*     */     
/*  74 */     add(EntityType.AXOLOTL, 
/*  75 */         LootTable.lootTable());
/*     */ 
/*     */     
/*  78 */     add(EntityType.BAT, 
/*  79 */         LootTable.lootTable());
/*     */ 
/*     */     
/*  82 */     add(EntityType.BEE, 
/*  83 */         LootTable.lootTable());
/*     */ 
/*     */     
/*  86 */     add(EntityType.BLAZE, 
/*  87 */         LootTable.lootTable()
/*  88 */         .withPool(LootPool.lootPool()
/*  89 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  90 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BLAZE_ROD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/*  91 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/*  94 */     add(EntityType.BOGGED, 
/*  95 */         LootTable.lootTable()
/*  96 */         .withPool(LootPool.lootPool()
/*  97 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  98 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 100 */         .withPool(LootPool.lootPool()
/* 101 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 102 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 104 */         .withPool(LootPool.lootPool()
/* 105 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 106 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)).setLimit(1)).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.POISON)))
/* 107 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/* 110 */     add(EntityType.CAT, 
/* 111 */         LootTable.lootTable()
/* 112 */         .withPool(LootPool.lootPool()
/* 113 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 114 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 118 */     add(EntityType.CAMEL, 
/* 119 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 122 */     add(EntityType.CAMEL_HUSK, 
/* 123 */         LootTable.lootTable()
/* 124 */         .withPool(LootPool.lootPool()
/* 125 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 126 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 3.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */     
/* 129 */     add(EntityType.CAVE_SPIDER, 
/* 130 */         LootTable.lootTable()
/* 131 */         .withPool(LootPool.lootPool()
/* 132 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 133 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 135 */         .withPool(LootPool.lootPool()
/* 136 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 137 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPIDER_EYE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(-1.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 138 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/* 141 */     add(EntityType.CHICKEN, 
/* 142 */         LootTable.lootTable()
/* 143 */         .withPool(LootPool.lootPool()
/* 144 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 145 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 147 */         .withPool(LootPool.lootPool()
/* 148 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 149 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHICKEN).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 153 */     add(EntityType.COD, 
/* 154 */         LootTable.lootTable()
/* 155 */         .withPool(LootPool.lootPool()
/* 156 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 157 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot()))))
/*     */         
/* 159 */         .withPool(LootPool.lootPool()
/* 160 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 161 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE_MEAL))
/* 162 */           .when(LootItemRandomChanceCondition.randomChance(0.05F))));
/*     */ 
/*     */     
/* 165 */     add(EntityType.COPPER_GOLEM, 
/* 166 */         LootTable.lootTable()
/* 167 */         .withPool(LootPool.lootPool()
/* 168 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 169 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 173 */     add(EntityType.COW, 
/* 174 */         LootTable.lootTable()
/* 175 */         .withPool(LootPool.lootPool()
/* 176 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 177 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 179 */         .withPool(LootPool.lootPool()
/* 180 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 181 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEEF).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 185 */     add(EntityType.CREEPER, 
/* 186 */         LootTable.lootTable()
/* 187 */         .withPool(LootPool.lootPool()
/* 188 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 189 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 191 */         .withPool(LootPool.lootPool()
/* 192 */           .add((LootPoolEntryContainer.Builder)TagEntry.expandTag(ItemTags.CREEPER_DROP_MUSIC_DISCS))
/* 193 */           .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypeTags.SKELETONS)))));
/*     */ 
/*     */     
/* 196 */     add(EntityType.DOLPHIN, 
/* 197 */         LootTable.lootTable()
/* 198 */         .withPool(LootPool.lootPool()
/* 199 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 200 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())))));
/*     */ 
/*     */ 
/*     */     
/* 204 */     add(EntityType.DONKEY, 
/* 205 */         LootTable.lootTable()
/* 206 */         .withPool(LootPool.lootPool()
/* 207 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 208 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 212 */     add(EntityType.DROWNED, 
/* 213 */         LootTable.lootTable()
/* 214 */         .withPool(LootPool.lootPool()
/* 215 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 216 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 218 */         .withPool(LootPool.lootPool()
/* 219 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 220 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_INGOT))
/* 221 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 222 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.11F, 0.02F))));
/*     */ 
/*     */     
/* 225 */     add(EntityType.ELDER_GUARDIAN, elderGuardianLootTable());
/*     */     
/* 227 */     add(EntityType.ENDER_DRAGON, 
/* 228 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 231 */     add(EntityType.ENDERMAN, 
/* 232 */         LootTable.lootTable()
/* 233 */         .withPool(LootPool.lootPool()
/* 234 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 235 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENDER_PEARL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 239 */     add(EntityType.ENDERMITE, 
/* 240 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 243 */     add(EntityType.EVOKER, 
/* 244 */         LootTable.lootTable()
/* 245 */         .withPool(LootPool.lootPool()
/* 246 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 247 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TOTEM_OF_UNDYING)))
/*     */         
/* 249 */         .withPool(LootPool.lootPool()
/* 250 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 251 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 252 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/* 255 */     add(EntityType.BREEZE, 
/* 256 */         LootTable.lootTable()
/* 257 */         .withPool(LootPool.lootPool()
/* 258 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 259 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREEZE_ROD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/* 260 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/* 263 */     add(EntityType.FOX, 
/* 264 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 267 */     add(EntityType.FROG, 
/* 268 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 271 */     add(EntityType.GHAST, 
/* 272 */         LootTable.lootTable()
/* 273 */         .withPool(LootPool.lootPool()
/* 274 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 275 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GHAST_TEAR).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 277 */         .withPool(LootPool.lootPool()
/* 278 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 279 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 281 */         .withPool(LootPool.lootPool()
/* 282 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 283 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_TEARS)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))
/* 284 */           .when(DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).direct(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.FIREBALL))))
/* 285 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */ 
/*     */     
/* 289 */     add(EntityType.HAPPY_GHAST, 
/* 290 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 293 */     add(EntityType.GIANT, 
/* 294 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 297 */     add(EntityType.GLOW_SQUID, 
/* 298 */         LootTable.lootTable()
/* 299 */         .withPool(LootPool.lootPool()
/* 300 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 301 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLOW_INK_SAC).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 305 */     add(EntityType.GOAT, 
/* 306 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 309 */     add(EntityType.GUARDIAN, 
/* 310 */         LootTable.lootTable()
/* 311 */         .withPool(LootPool.lootPool()
/* 312 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 313 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PRISMARINE_SHARD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 315 */         .withPool(LootPool.lootPool()
/* 316 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 317 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).setWeight(2).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())))
/* 318 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PRISMARINE_CRYSTALS).setWeight(2).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 319 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem()))
/*     */         
/* 321 */         .withPool(LootPool.lootPool()
/* 322 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 323 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.FISHING_FISH).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())))
/* 324 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 325 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F))));
/*     */ 
/*     */     
/* 328 */     add(EntityType.HORSE, 
/* 329 */         LootTable.lootTable()
/* 330 */         .withPool(LootPool.lootPool()
/* 331 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 332 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 336 */     add(EntityType.HUSK, 
/* 337 */         LootTable.lootTable()
/* 338 */         .withPool(LootPool.lootPool()
/* 339 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 340 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 342 */         .withPool(LootPool.lootPool()
/* 343 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 344 */           .add(LootItem.lootTableItem((ItemLike)Items.RABBIT_FOOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))
/* 345 */             .when(
/* 346 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, 
/* 347 */                 EntityPredicate.Builder.entity().vehicle(
/* 348 */                   EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of((HolderGetter)registryLookup1, EntityType.CAMEL_HUSK)))
/* 349 */                 .build()))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 354 */         .withPool(LootPool.lootPool()
/* 355 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 356 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT))
/* 357 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT))
/* 358 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())))
/* 359 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 360 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F))));
/*     */ 
/*     */     
/* 363 */     add(EntityType.RAVAGER, 
/* 364 */         LootTable.lootTable()
/* 365 */         .withPool(LootPool.lootPool()
/* 366 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 367 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 371 */     add(EntityType.ILLUSIONER, 
/* 372 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 375 */     add(EntityType.IRON_GOLEM, 
/* 376 */         LootTable.lootTable()
/* 377 */         .withPool(LootPool.lootPool()
/* 378 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 379 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.POPPY).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F)))))
/*     */         
/* 381 */         .withPool(LootPool.lootPool()
/* 382 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 383 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 5.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 387 */     add(EntityType.LLAMA, 
/* 388 */         LootTable.lootTable()
/* 389 */         .withPool(LootPool.lootPool()
/* 390 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 391 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 395 */     add(EntityType.MAGMA_CUBE, 
/* 396 */         LootTable.lootTable()
/* 397 */         .withPool(LootPool.lootPool()
/* 398 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 399 */           .add(((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAGMA_CREAM).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(-2.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))
/* 400 */             .when(killedByFrog((HolderGetter)registryLookup1).invert()))
/* 401 */             .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate((EntitySubPredicate)SlimePredicate.sized(MinMaxBounds.Ints.atLeast(2))))))
/*     */           
/* 403 */           .add(LootItem.lootTableItem((ItemLike)Items.PEARLESCENT_FROGLIGHT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).when(killedByFrogVariant((HolderGetter)registryLookup1, (HolderGetter)registryLookup2, FrogVariants.WARM)))
/* 404 */           .add(LootItem.lootTableItem((ItemLike)Items.VERDANT_FROGLIGHT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).when(killedByFrogVariant((HolderGetter)registryLookup1, (HolderGetter)registryLookup2, FrogVariants.COLD)))
/* 405 */           .add(LootItem.lootTableItem((ItemLike)Items.OCHRE_FROGLIGHT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).when(killedByFrogVariant((HolderGetter)registryLookup1, (HolderGetter)registryLookup2, FrogVariants.TEMPERATE)))));
/*     */ 
/*     */ 
/*     */     
/* 409 */     add(EntityType.MANNEQUIN, 
/* 410 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 413 */     add(EntityType.MULE, 
/* 414 */         LootTable.lootTable()
/* 415 */         .withPool(LootPool.lootPool()
/* 416 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 417 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 421 */     add(EntityType.MOOSHROOM, 
/* 422 */         LootTable.lootTable()
/* 423 */         .withPool(LootPool.lootPool()
/* 424 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 425 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 427 */         .withPool(LootPool.lootPool()
/* 428 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 429 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEEF).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 433 */     add(EntityType.NAUTILUS, 
/* 434 */         LootTable.lootTable()
/* 435 */         .withPool(LootPool.lootPool()
/* 436 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 437 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NAUTILUS_SHELL))
/* 438 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 439 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.05F, 0.01F))));
/*     */ 
/*     */ 
/*     */     
/* 443 */     add(EntityType.OCELOT, 
/* 444 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 447 */     add(EntityType.PANDA, 
/* 448 */         LootTable.lootTable()
/* 449 */         .withPool(LootPool.lootPool()
/* 450 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 451 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BAMBOO).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 455 */     add(EntityType.PARROT, 
/* 456 */         LootTable.lootTable()
/* 457 */         .withPool(LootPool.lootPool()
/* 458 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 459 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 463 */     add(EntityType.PHANTOM, 
/* 464 */         LootTable.lootTable()
/* 465 */         .withPool(LootPool.lootPool()
/* 466 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 467 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PHANTOM_MEMBRANE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 468 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/* 471 */     add(EntityType.PIG, 
/* 472 */         LootTable.lootTable()
/* 473 */         .withPool(LootPool.lootPool()
/* 474 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 475 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PORKCHOP).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 479 */     add(EntityType.PILLAGER, 
/* 480 */         LootTable.lootTable()
/* 481 */         .withPool(LootPool.lootPool()
/* 482 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 483 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.OMINOUS_BOTTLE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetOminousBottleAmplifierFunction.setAmplifier((NumberProvider)UniformGenerator.between(0.0F, 4.0F))))
/* 484 */           .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate((EntitySubPredicate)RaiderPredicate.CAPTAIN_WITHOUT_RAID)))));
/*     */ 
/*     */ 
/*     */     
/* 488 */     add(EntityType.PLAYER, 
/* 489 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 492 */     add(EntityType.POLAR_BEAR, 
/* 493 */         LootTable.lootTable()
/* 494 */         .withPool(LootPool.lootPool()
/* 495 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 496 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 497 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 501 */     add(EntityType.PUFFERFISH, 
/* 502 */         LootTable.lootTable()
/* 503 */         .withPool(LootPool.lootPool()
/* 504 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 505 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUFFERFISH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))))
/*     */         
/* 507 */         .withPool(LootPool.lootPool()
/* 508 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 509 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE_MEAL))
/* 510 */           .when(LootItemRandomChanceCondition.randomChance(0.05F))));
/*     */ 
/*     */     
/* 513 */     add(EntityType.RABBIT, 
/* 514 */         LootTable.lootTable()
/* 515 */         .withPool(LootPool.lootPool()
/* 516 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 517 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RABBIT_HIDE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 519 */         .withPool(LootPool.lootPool()
/* 520 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 521 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RABBIT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 523 */         .withPool(LootPool.lootPool()
/* 524 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 525 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RABBIT_FOOT))
/* 526 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 527 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.1F, 0.03F))));
/*     */ 
/*     */     
/* 530 */     add(EntityType.SALMON, 
/* 531 */         LootTable.lootTable()
/* 532 */         .withPool(LootPool.lootPool()
/* 533 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 534 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot()))))
/*     */         
/* 536 */         .withPool(LootPool.lootPool()
/* 537 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 538 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE_MEAL))
/* 539 */           .when(LootItemRandomChanceCondition.randomChance(0.05F))));
/*     */ 
/*     */     
/* 542 */     add(EntityType.SHEEP, 
/* 543 */         LootTable.lootTable()
/* 544 */         .withPool(LootPool.lootPool()
/* 545 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 546 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUTTON).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 548 */         .withPool(
/* 549 */           createSheepDispatchPool(BuiltInLootTables.SHEEP_BY_DYE)));
/*     */ 
/*     */ 
/*     */     
/* 553 */     LootData.WOOL_ITEM_BY_DYE.forEach((paramDyeColor, paramItemLike) -> add(EntityType.SHEEP, (ResourceKey)BuiltInLootTables.SHEEP_BY_DYE.get(paramDyeColor), LootTable.lootTable().withPool(LootPool.lootPool().add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(paramItemLike)))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 561 */     add(EntityType.SHULKER, 
/* 562 */         LootTable.lootTable()
/* 563 */         .withPool(LootPool.lootPool()
/* 564 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 565 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SHULKER_SHELL))
/* 566 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.5F, 0.0625F))));
/*     */ 
/*     */     
/* 569 */     add(EntityType.SILVERFISH, 
/* 570 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 573 */     add(EntityType.SKELETON, 
/* 574 */         LootTable.lootTable()
/* 575 */         .withPool(LootPool.lootPool()
/* 576 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 577 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 579 */         .withPool(LootPool.lootPool()
/* 580 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 581 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 585 */     add(EntityType.SKELETON_HORSE, 
/* 586 */         LootTable.lootTable()
/* 587 */         .withPool(LootPool.lootPool()
/* 588 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 589 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 593 */     add(EntityType.SLIME, 
/* 594 */         LootTable.lootTable()
/* 595 */         .withPool(LootPool.lootPool()
/* 596 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 597 */           .add(LootItem.lootTableItem((ItemLike)Items.SLIME_BALL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))).when(killedByFrog((HolderGetter)registryLookup1).invert()))
/* 598 */           .add(LootItem.lootTableItem((ItemLike)Items.SLIME_BALL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).when(killedByFrog((HolderGetter)registryLookup1)))
/* 599 */           .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate((EntitySubPredicate)SlimePredicate.sized(MinMaxBounds.Ints.exactly(1)))))));
/*     */ 
/*     */ 
/*     */     
/* 603 */     add(EntityType.SNIFFER, 
/* 604 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 607 */     add(EntityType.SNOW_GOLEM, 
/* 608 */         LootTable.lootTable()
/* 609 */         .withPool(LootPool.lootPool()
/* 610 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 611 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 15.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 615 */     add(EntityType.SPIDER, 
/* 616 */         LootTable.lootTable()
/* 617 */         .withPool(LootPool.lootPool()
/* 618 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 619 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 621 */         .withPool(LootPool.lootPool()
/* 622 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 623 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPIDER_EYE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(-1.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 624 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/* 627 */     add(EntityType.SQUID, 
/* 628 */         LootTable.lootTable()
/* 629 */         .withPool(LootPool.lootPool()
/* 630 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 631 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.INK_SAC).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 635 */     add(EntityType.STRAY, 
/* 636 */         LootTable.lootTable()
/* 637 */         .withPool(LootPool.lootPool()
/* 638 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 639 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 641 */         .withPool(LootPool.lootPool()
/* 642 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 643 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 645 */         .withPool(LootPool.lootPool()
/* 646 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 647 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)).setLimit(1)).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SLOWNESS)))
/* 648 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/* 651 */     add(EntityType.PARCHED, 
/* 652 */         LootTable.lootTable()
/* 653 */         .withPool(LootPool.lootPool()
/* 654 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 655 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 657 */         .withPool(LootPool.lootPool()
/* 658 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 659 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 661 */         .withPool(LootPool.lootPool()
/* 662 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 663 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)).setLimit(1)).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WEAKNESS)))
/* 664 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/* 667 */     add(EntityType.STRIDER, 
/* 668 */         LootTable.lootTable()
/* 669 */         .withPool(LootPool.lootPool()
/* 670 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 671 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 675 */     add(EntityType.TADPOLE, 
/* 676 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 679 */     add(EntityType.TRADER_LLAMA, 
/* 680 */         LootTable.lootTable()
/* 681 */         .withPool(LootPool.lootPool()
/* 682 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 683 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 687 */     add(EntityType.TROPICAL_FISH, 
/* 688 */         LootTable.lootTable()
/* 689 */         .withPool(LootPool.lootPool()
/* 690 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 691 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TROPICAL_FISH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))))
/*     */         
/* 693 */         .withPool(LootPool.lootPool()
/* 694 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 695 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE_MEAL))
/* 696 */           .when(LootItemRandomChanceCondition.randomChance(0.05F))));
/*     */ 
/*     */     
/* 699 */     add(EntityType.TURTLE, 
/* 700 */         LootTable.lootTable()
/* 701 */         .withPool(LootPool.lootPool()
/* 702 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 703 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SEAGRASS).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 705 */         .withPool(LootPool.lootPool()
/* 706 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 707 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOWL))
/* 708 */           .when(DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_LIGHTNING))))));
/*     */ 
/*     */ 
/*     */     
/* 712 */     add(EntityType.VEX, 
/* 713 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 716 */     add(EntityType.VILLAGER, 
/* 717 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 720 */     add(EntityType.WARDEN, 
/* 721 */         LootTable.lootTable()
/* 722 */         .withPool(LootPool.lootPool()
/* 723 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 724 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SCULK_CATALYST))));
/*     */ 
/*     */     
/* 727 */     add(EntityType.WANDERING_TRADER, 
/* 728 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 731 */     add(EntityType.VINDICATOR, 
/* 732 */         LootTable.lootTable()
/* 733 */         .withPool(LootPool.lootPool()
/* 734 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 735 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 736 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/* 739 */     add(EntityType.WITCH, 
/* 740 */         LootTable.lootTable()
/* 741 */         .withPool(LootPool.lootPool()
/* 742 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/* 743 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLOWSTONE_DUST).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 744 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SUGAR).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 745 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPIDER_EYE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 746 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLASS_BOTTLE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 747 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 748 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 750 */         .withPool(LootPool.lootPool()
/* 751 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 752 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 756 */     add(EntityType.WITHER, 
/* 757 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 760 */     add(EntityType.WITHER_SKELETON, 
/* 761 */         LootTable.lootTable()
/* 762 */         .withPool(LootPool.lootPool()
/* 763 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 764 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(-1.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 766 */         .withPool(LootPool.lootPool()
/* 767 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 768 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 770 */         .withPool(LootPool.lootPool()
/* 771 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 772 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.WITHER_SKELETON_SKULL))
/* 773 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 774 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F))));
/*     */ 
/*     */     
/* 777 */     add(EntityType.WOLF, 
/* 778 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 781 */     add(EntityType.ZOGLIN, 
/* 782 */         LootTable.lootTable()
/* 783 */         .withPool(LootPool.lootPool()
/* 784 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 785 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 789 */     add(EntityType.CREAKING, 
/* 790 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 793 */     add(EntityType.ZOMBIE, 
/* 794 */         LootTable.lootTable()
/* 795 */         .withPool(LootPool.lootPool()
/* 796 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 797 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 799 */         .withPool(LootPool.lootPool()
/* 800 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 801 */           .add(LootItem.lootTableItem((ItemLike)Items.RED_MUSHROOM).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))
/* 802 */             .when(
/* 803 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, 
/* 804 */                 EntityPredicate.Builder.entity().vehicle(
/* 805 */                   EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of((HolderGetter)registryLookup1, EntityType.ZOMBIE_HORSE)))
/* 806 */                 .build()))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 811 */         .withPool(LootPool.lootPool()
/* 812 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 813 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT))
/* 814 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT))
/* 815 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())))
/* 816 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 817 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F)))
/* 818 */         .withPool(LootPool.lootPool()
/* 819 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_LAVA_CHICKEN))
/* 820 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 821 */           .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
/* 822 */               .flags(EntityFlagsPredicate.Builder.flags().setIsBaby(Boolean.valueOf(true)))
/* 823 */               .vehicle(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.CHICKEN))))));
/*     */ 
/*     */     
/* 826 */     add(EntityType.ZOMBIE_HORSE, 
/* 827 */         LootTable.lootTable()
/* 828 */         .withPool(LootPool.lootPool()
/* 829 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 830 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 3.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 834 */     add(EntityType.ZOMBIE_NAUTILUS, 
/* 835 */         LootTable.lootTable()
/* 836 */         .withPool(LootPool.lootPool()
/* 837 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 838 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 3.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 839 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */ 
/*     */     
/* 843 */     add(EntityType.ZOMBIFIED_PIGLIN, 
/* 844 */         LootTable.lootTable()
/* 845 */         .withPool(LootPool.lootPool()
/* 846 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 847 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 849 */         .withPool(LootPool.lootPool()
/* 850 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 851 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 853 */         .withPool(LootPool.lootPool()
/* 854 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 855 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT))
/* 856 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 857 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F))));
/*     */ 
/*     */     
/* 860 */     add(EntityType.HOGLIN, 
/* 861 */         LootTable.lootTable()
/* 862 */         .withPool(LootPool.lootPool()
/* 863 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 864 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PORKCHOP).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 866 */         .withPool(LootPool.lootPool()
/* 867 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 868 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 872 */     add(EntityType.PIGLIN, 
/* 873 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 876 */     add(EntityType.PIGLIN_BRUTE, 
/* 877 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 880 */     add(EntityType.ZOMBIE_VILLAGER, 
/* 881 */         LootTable.lootTable()
/* 882 */         .withPool(LootPool.lootPool()
/* 883 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 884 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 886 */         .withPool(LootPool.lootPool()
/* 887 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 888 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT))
/* 889 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT))
/* 890 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())))
/* 891 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 892 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F))));
/*     */   }
/*     */ 
/*     */   
/*     */   public LootTable.Builder elderGuardianLootTable() {
/* 897 */     return LootTable.lootTable()
/* 898 */       .withPool(LootPool.lootPool()
/* 899 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 900 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PRISMARINE_SHARD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */       
/* 902 */       .withPool(LootPool.lootPool()
/* 903 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 904 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).setWeight(3).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())))
/* 905 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PRISMARINE_CRYSTALS).setWeight(2).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 906 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem()))
/*     */       
/* 908 */       .withPool(LootPool.lootPool()
/* 909 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 910 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.WET_SPONGE))
/* 911 */         .when(LootItemKilledByPlayerCondition.killedByPlayer()))
/* 912 */       .withPool(LootPool.lootPool()
/* 913 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 914 */         .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.FISHING_FISH).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())))
/* 915 */         .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 916 */         .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F)))
/* 917 */       .withPool(LootPool.lootPool()
/* 918 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 919 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(4))
/* 920 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\loot\packs\VanillaEntityLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */