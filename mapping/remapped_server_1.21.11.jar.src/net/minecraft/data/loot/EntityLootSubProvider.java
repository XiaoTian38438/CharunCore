/*     */ package net.minecraft.data.loot;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.advancements.criterion.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.criterion.DataComponentMatchers;
/*     */ import net.minecraft.advancements.criterion.EnchantmentPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityFlagsPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityPredicate;
/*     */ import net.minecraft.advancements.criterion.EntitySubPredicate;
/*     */ import net.minecraft.advancements.criterion.ItemPredicate;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.advancements.criterion.SheepPredicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.component.DataComponentExactPredicate;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicates;
/*     */ import net.minecraft.core.component.predicates.EnchantmentsPredicate;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariant;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
/*     */ import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
/*     */ import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ 
/*     */ public abstract class EntityLootSubProvider implements LootTableSubProvider {
/*     */   protected final HolderLookup.Provider registries;
/*     */   private final FeatureFlagSet allowed;
/*     */   private final FeatureFlagSet required;
/*     */   private final Map<EntityType<?>, Map<ResourceKey<LootTable>, LootTable.Builder>> map;
/*     */   
/*     */   protected final AnyOfCondition.Builder shouldSmeltLoot() {
/*  54 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/*  55 */     return AnyOfCondition.anyOf(new LootItemCondition.Builder[] {
/*  56 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, 
/*  57 */             EntityPredicate.Builder.entity()
/*  58 */             .flags(EntityFlagsPredicate.Builder.flags().setOnFire(Boolean.valueOf(true)))), 
/*     */           
/*  60 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_ATTACKER, 
/*     */             
/*  62 */             EntityPredicate.Builder.entity()
/*  63 */             .equipment(EntityEquipmentPredicate.Builder.equipment()
/*  64 */               .mainhand(ItemPredicate.Builder.item()
/*  65 */                 .withComponents(DataComponentMatchers.Builder.components().partial(DataComponentPredicates.ENCHANTMENTS, (DataComponentPredicate)EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate((HolderSet)registryLookup.getOrThrow(EnchantmentTags.SMELTS_LOOT), MinMaxBounds.Ints.ANY)))).build()))))
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EntityLootSubProvider(FeatureFlagSet paramFeatureFlagSet, HolderLookup.Provider paramProvider) {
/*  75 */     this(paramFeatureFlagSet, paramFeatureFlagSet, paramProvider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EntityLootSubProvider(FeatureFlagSet paramFeatureFlagSet1, FeatureFlagSet paramFeatureFlagSet2, HolderLookup.Provider paramProvider) {
/* 105 */     this.map = Maps.newHashMap();
/*     */     this.allowed = paramFeatureFlagSet1;
/*     */     this.required = paramFeatureFlagSet2;
/*     */     this.registries = paramProvider;
/*     */   }
/*     */   
/* 111 */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> paramBiConsumer) { generate();
/*     */     
/* 113 */     HashSet hashSet = new HashSet();
/* 114 */     BuiltInRegistries.ENTITY_TYPE.listElements().forEach(paramReference -> {
/*     */           EntityType entityType = (EntityType)paramReference.value();
/*     */ 
/*     */           
/*     */           if (!entityType.isEnabled(this.allowed)) {
/*     */             return;
/*     */           }
/*     */ 
/*     */           
/*     */           Optional optional = entityType.getDefaultLootTable();
/*     */ 
/*     */           
/*     */           if (optional.isPresent()) {
/*     */             Map map = this.map.remove(entityType);
/*     */             
/*     */             if (entityType.isEnabled(this.required) && (map == null || !map.containsKey(optional.get()))) {
/*     */               throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", new Object[] { optional.get(), paramReference.key().identifier() }));
/*     */             }
/*     */             
/*     */             if (map != null) {
/*     */               map.forEach(());
/*     */             }
/*     */           } else {
/*     */             Map map = this.map.remove(entityType);
/*     */             
/*     */             if (map != null) {
/*     */               throw new IllegalStateException(String.format(Locale.ROOT, "Weird loottables '%s' for '%s', not a LivingEntity so should not have loot", new Object[] { map.keySet().stream().map(()).collect(Collectors.joining(",")), paramReference.key().identifier() }));
/*     */             }
/*     */           } 
/*     */         });
/*     */     
/* 145 */     if (!this.map.isEmpty()) {
/* 146 */       throw new IllegalStateException("Created loot tables for entities not supported by datapack: " + String.valueOf(this.map.keySet()));
/*     */     } }
/*     */ 
/*     */   
/*     */   protected LootItemCondition.Builder killedByFrog(HolderGetter<EntityType<?>> paramHolderGetter) {
/* 151 */     return DamageSourceCondition.hasDamageSource(
/* 152 */         DamageSourcePredicate.Builder.damageType().source(
/* 153 */           EntityPredicate.Builder.entity().of(paramHolderGetter, EntityType.FROG))); } public static LootPool.Builder createSheepDispatchPool(Map<DyeColor, ResourceKey<LootTable>> paramMap) {
/*     */     AlternativesEntry.Builder builder = AlternativesEntry.alternatives(new LootPoolEntryContainer.Builder[0]);
/*     */     for (Map.Entry<DyeColor, ResourceKey<LootTable>> entry : paramMap.entrySet())
/*     */       builder = builder.otherwise(NestedLootTable.lootTableReference((ResourceKey)entry.getValue()).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.SHEEP_COLOR, entry.getKey())).build()).subPredicate((EntitySubPredicate)SheepPredicate.hasWool())))); 
/*     */     return LootPool.lootPool().add((LootPoolEntryContainer.Builder)builder);
/*     */   } protected LootItemCondition.Builder killedByFrogVariant(HolderGetter<EntityType<?>> paramHolderGetter, HolderGetter<FrogVariant> paramHolderGetter1, ResourceKey<FrogVariant> paramResourceKey) {
/* 159 */     return DamageSourceCondition.hasDamageSource(
/* 160 */         DamageSourcePredicate.Builder.damageType().source(
/* 161 */           EntityPredicate.Builder.entity()
/* 162 */           .of(paramHolderGetter, EntityType.FROG)
/* 163 */           .components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.FROG_VARIANT, paramHolderGetter1.getOrThrow(paramResourceKey))).build())));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void add(EntityType<?> paramEntityType, LootTable.Builder paramBuilder) {
/* 169 */     add(paramEntityType, (ResourceKey<LootTable>)paramEntityType.getDefaultLootTable().orElseThrow(() -> new IllegalStateException("Entity " + String.valueOf(paramEntityType) + " has no loot table")), paramBuilder);
/*     */   }
/*     */   
/*     */   protected void add(EntityType<?> paramEntityType, ResourceKey<LootTable> paramResourceKey, LootTable.Builder paramBuilder) {
/* 173 */     ((Map<ResourceKey<LootTable>, LootTable.Builder>)this.map.computeIfAbsent(paramEntityType, paramEntityType -> new HashMap<>())).put(paramResourceKey, paramBuilder);
/*     */   }
/*     */   
/*     */   public abstract void generate();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\loot\EntityLootSubProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */