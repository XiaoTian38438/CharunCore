/*     */ package net.minecraft.data.advancements.packs;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementRequirements;
/*     */ import net.minecraft.advancements.AdvancementType;
/*     */ import net.minecraft.advancements.criterion.ChangeDimensionTrigger;
/*     */ import net.minecraft.advancements.criterion.CuredZombieVillagerTrigger;
/*     */ import net.minecraft.advancements.criterion.DamagePredicate;
/*     */ import net.minecraft.advancements.criterion.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.criterion.EnchantedItemTrigger;
/*     */ import net.minecraft.advancements.criterion.EntityHurtPlayerTrigger;
/*     */ import net.minecraft.advancements.criterion.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.criterion.ItemPredicate;
/*     */ import net.minecraft.advancements.criterion.LocationPredicate;
/*     */ import net.minecraft.advancements.criterion.PlayerTrigger;
/*     */ import net.minecraft.advancements.criterion.TagPredicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.advancements.AdvancementSubProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*     */ 
/*     */ public class VanillaStoryAdvancements
/*     */   implements AdvancementSubProvider
/*     */ {
/*     */   public void generate(HolderLookup.Provider paramProvider, Consumer<AdvancementHolder> paramConsumer) {
/*  38 */     HolderLookup.RegistryLookup registryLookup = paramProvider.lookupOrThrow(Registries.ITEM);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  43 */     AdvancementHolder advancementHolder1 = Advancement.Builder.advancement().display((ItemLike)Blocks.GRASS_BLOCK, (Component)Component.translatable("advancements.story.root.title"), (Component)Component.translatable("advancements.story.root.description"), Identifier.withDefaultNamespace("gui/advancements/backgrounds/stone"), AdvancementType.TASK, false, false, false).addCriterion("crafting_table", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Blocks.CRAFTING_TABLE })).save(paramConsumer, "story/root");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  49 */     AdvancementHolder advancementHolder2 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Items.WOODEN_PICKAXE, (Component)Component.translatable("advancements.story.mine_stone.title"), (Component)Component.translatable("advancements.story.mine_stone.description"), null, AdvancementType.TASK, true, true, false).addCriterion("get_stone", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemPredicate.Builder[] { ItemPredicate.Builder.item().of((HolderGetter)registryLookup, ItemTags.STONE_TOOL_MATERIALS) })).save(paramConsumer, "story/mine_stone");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  55 */     AdvancementHolder advancementHolder3 = Advancement.Builder.advancement().parent(advancementHolder2).display((ItemLike)Items.STONE_PICKAXE, (Component)Component.translatable("advancements.story.upgrade_tools.title"), (Component)Component.translatable("advancements.story.upgrade_tools.description"), null, AdvancementType.TASK, true, true, false).addCriterion("stone_pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.STONE_PICKAXE })).save(paramConsumer, "story/upgrade_tools");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     AdvancementHolder advancementHolder4 = Advancement.Builder.advancement().parent(advancementHolder3).display((ItemLike)Items.IRON_INGOT, (Component)Component.translatable("advancements.story.smelt_iron.title"), (Component)Component.translatable("advancements.story.smelt_iron.description"), null, AdvancementType.TASK, true, true, false).addCriterion("iron", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_INGOT })).save(paramConsumer, "story/smelt_iron");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     AdvancementHolder advancementHolder5 = Advancement.Builder.advancement().parent(advancementHolder4).display((ItemLike)Items.IRON_PICKAXE, (Component)Component.translatable("advancements.story.iron_tools.title"), (Component)Component.translatable("advancements.story.iron_tools.description"), null, AdvancementType.TASK, true, true, false).addCriterion("iron_pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_PICKAXE })).save(paramConsumer, "story/iron_tools");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     AdvancementHolder advancementHolder6 = Advancement.Builder.advancement().parent(advancementHolder5).display((ItemLike)Items.DIAMOND, (Component)Component.translatable("advancements.story.mine_diamond.title"), (Component)Component.translatable("advancements.story.mine_diamond.description"), null, AdvancementType.TASK, true, true, false).addCriterion("diamond", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND })).save(paramConsumer, "story/mine_diamond");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     AdvancementHolder advancementHolder7 = Advancement.Builder.advancement().parent(advancementHolder4).display((ItemLike)Items.LAVA_BUCKET, (Component)Component.translatable("advancements.story.lava_bucket.title"), (Component)Component.translatable("advancements.story.lava_bucket.description"), null, AdvancementType.TASK, true, true, false).addCriterion("lava_bucket", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.LAVA_BUCKET })).save(paramConsumer, "story/lava_bucket");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     AdvancementHolder advancementHolder8 = Advancement.Builder.advancement().parent(advancementHolder4).display((ItemLike)Items.IRON_CHESTPLATE, (Component)Component.translatable("advancements.story.obtain_armor.title"), (Component)Component.translatable("advancements.story.obtain_armor.description"), null, AdvancementType.TASK, true, true, false).requirements(AdvancementRequirements.Strategy.OR).addCriterion("iron_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_HELMET })).addCriterion("iron_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_CHESTPLATE })).addCriterion("iron_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_LEGGINGS })).addCriterion("iron_boots", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_BOOTS })).save(paramConsumer, "story/obtain_armor");
/*     */     
/*  91 */     Advancement.Builder.advancement()
/*  92 */       .parent(advancementHolder6)
/*  93 */       .display((ItemLike)Items.ENCHANTED_BOOK, (Component)Component.translatable("advancements.story.enchant_item.title"), (Component)Component.translatable("advancements.story.enchant_item.description"), null, AdvancementType.TASK, true, true, false)
/*  94 */       .addCriterion("enchanted_item", EnchantedItemTrigger.TriggerInstance.enchantedItem())
/*  95 */       .save(paramConsumer, "story/enchant_item");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     AdvancementHolder advancementHolder9 = Advancement.Builder.advancement().parent(advancementHolder7).display((ItemLike)Blocks.OBSIDIAN, (Component)Component.translatable("advancements.story.form_obsidian.title"), (Component)Component.translatable("advancements.story.form_obsidian.description"), null, AdvancementType.TASK, true, true, false).addCriterion("obsidian", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Blocks.OBSIDIAN })).save(paramConsumer, "story/form_obsidian");
/*     */     
/* 103 */     Advancement.Builder.advancement()
/* 104 */       .parent(advancementHolder8)
/* 105 */       .display((ItemLike)Items.SHIELD, (Component)Component.translatable("advancements.story.deflect_arrow.title"), (Component)Component.translatable("advancements.story.deflect_arrow.description"), null, AdvancementType.TASK, true, true, false)
/* 106 */       .addCriterion("deflected_projectile", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE))).blocked(Boolean.valueOf(true))))
/* 107 */       .save(paramConsumer, "story/deflect_arrow");
/*     */     
/* 109 */     Advancement.Builder.advancement()
/* 110 */       .parent(advancementHolder6)
/* 111 */       .display((ItemLike)Items.DIAMOND_CHESTPLATE, (Component)Component.translatable("advancements.story.shiny_gear.title"), (Component)Component.translatable("advancements.story.shiny_gear.description"), null, AdvancementType.TASK, true, true, false)
/* 112 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 113 */       .addCriterion("diamond_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND_HELMET
/* 114 */           })).addCriterion("diamond_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND_CHESTPLATE
/* 115 */           })).addCriterion("diamond_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND_LEGGINGS
/* 116 */           })).addCriterion("diamond_boots", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND_BOOTS
/* 117 */           })).save(paramConsumer, "story/shiny_gear");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     AdvancementHolder advancementHolder10 = Advancement.Builder.advancement().parent(advancementHolder9).display((ItemLike)Items.FLINT_AND_STEEL, (Component)Component.translatable("advancements.story.enter_the_nether.title"), (Component)Component.translatable("advancements.story.enter_the_nether.description"), null, AdvancementType.TASK, true, true, false).addCriterion("entered_nether", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.NETHER)).save(paramConsumer, "story/enter_the_nether");
/*     */     
/* 125 */     Advancement.Builder.advancement()
/* 126 */       .parent(advancementHolder10)
/* 127 */       .display((ItemLike)Items.GOLDEN_APPLE, (Component)Component.translatable("advancements.story.cure_zombie_villager.title"), (Component)Component.translatable("advancements.story.cure_zombie_villager.description"), null, AdvancementType.GOAL, true, true, false)
/* 128 */       .addCriterion("cured_zombie", CuredZombieVillagerTrigger.TriggerInstance.curedZombieVillager())
/* 129 */       .save(paramConsumer, "story/cure_zombie_villager");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     AdvancementHolder advancementHolder11 = Advancement.Builder.advancement().parent(advancementHolder10).display((ItemLike)Items.ENDER_EYE, (Component)Component.translatable("advancements.story.follow_ender_eye.title"), (Component)Component.translatable("advancements.story.follow_ender_eye.description"), null, AdvancementType.TASK, true, true, false).addCriterion("in_stronghold", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure((Holder)paramProvider.lookupOrThrow(Registries.STRUCTURE).getOrThrow(BuiltinStructures.STRONGHOLD)))).save(paramConsumer, "story/follow_ender_eye");
/*     */     
/* 137 */     Advancement.Builder.advancement()
/* 138 */       .parent(advancementHolder11)
/* 139 */       .display((ItemLike)Blocks.END_STONE, (Component)Component.translatable("advancements.story.enter_the_end.title"), (Component)Component.translatable("advancements.story.enter_the_end.description"), null, AdvancementType.TASK, true, true, false)
/* 140 */       .addCriterion("entered_end", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.END))
/* 141 */       .save(paramConsumer, "story/enter_the_end");
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\advancements\packs\VanillaStoryAdvancements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */