/*    */ package net.minecraft.data.advancements.packs;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.advancements.AdvancementRewards;
/*    */ import net.minecraft.advancements.AdvancementType;
/*    */ import net.minecraft.advancements.criterion.ChangeDimensionTrigger;
/*    */ import net.minecraft.advancements.criterion.DistancePredicate;
/*    */ import net.minecraft.advancements.criterion.EnterBlockTrigger;
/*    */ import net.minecraft.advancements.criterion.EntityPredicate;
/*    */ import net.minecraft.advancements.criterion.InventoryChangeTrigger;
/*    */ import net.minecraft.advancements.criterion.KilledTrigger;
/*    */ import net.minecraft.advancements.criterion.LevitationTrigger;
/*    */ import net.minecraft.advancements.criterion.LocationPredicate;
/*    */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*    */ import net.minecraft.advancements.criterion.PlayerTrigger;
/*    */ import net.minecraft.advancements.criterion.SummonedEntityTrigger;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.advancements.AdvancementSubProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*    */ 
/*    */ public class VanillaTheEndAdvancements
/*    */   implements AdvancementSubProvider {
/*    */   public void generate(HolderLookup.Provider paramProvider, Consumer<AdvancementHolder> paramConsumer) {
/* 36 */     HolderLookup.RegistryLookup registryLookup = paramProvider.lookupOrThrow(Registries.ENTITY_TYPE);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     AdvancementHolder advancementHolder1 = Advancement.Builder.advancement().display((ItemLike)Blocks.END_STONE, (Component)Component.translatable("advancements.end.root.title"), (Component)Component.translatable("advancements.end.root.description"), Identifier.withDefaultNamespace("gui/advancements/backgrounds/end"), AdvancementType.TASK, false, false, false).addCriterion("entered_end", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.END)).save(paramConsumer, "end/root");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 47 */     AdvancementHolder advancementHolder2 = Advancement.Builder.advancement().parent(advancementHolder1).display((ItemLike)Blocks.DRAGON_HEAD, (Component)Component.translatable("advancements.end.kill_dragon.title"), (Component)Component.translatable("advancements.end.kill_dragon.description"), null, AdvancementType.TASK, true, true, false).addCriterion("killed_dragon", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup, EntityType.ENDER_DRAGON))).save(paramConsumer, "end/kill_dragon");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 53 */     AdvancementHolder advancementHolder3 = Advancement.Builder.advancement().parent(advancementHolder2).display((ItemLike)Items.ENDER_PEARL, (Component)Component.translatable("advancements.end.enter_end_gateway.title"), (Component)Component.translatable("advancements.end.enter_end_gateway.description"), null, AdvancementType.TASK, true, true, false).addCriterion("entered_end_gateway", EnterBlockTrigger.TriggerInstance.entersBlock(Blocks.END_GATEWAY)).save(paramConsumer, "end/enter_end_gateway");
/*    */     
/* 55 */     Advancement.Builder.advancement()
/* 56 */       .parent(advancementHolder2)
/* 57 */       .display((ItemLike)Items.END_CRYSTAL, (Component)Component.translatable("advancements.end.respawn_dragon.title"), (Component)Component.translatable("advancements.end.respawn_dragon.description"), null, AdvancementType.GOAL, true, true, false)
/* 58 */       .addCriterion("summoned_dragon", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup, EntityType.ENDER_DRAGON)))
/* 59 */       .save(paramConsumer, "end/respawn_dragon");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 65 */     AdvancementHolder advancementHolder4 = Advancement.Builder.advancement().parent(advancementHolder3).display((ItemLike)Blocks.PURPUR_BLOCK, (Component)Component.translatable("advancements.end.find_end_city.title"), (Component)Component.translatable("advancements.end.find_end_city.description"), null, AdvancementType.TASK, true, true, false).addCriterion("in_city", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure((Holder)paramProvider.lookupOrThrow(Registries.STRUCTURE).getOrThrow(BuiltinStructures.END_CITY)))).save(paramConsumer, "end/find_end_city");
/*    */     
/* 67 */     Advancement.Builder.advancement()
/* 68 */       .parent(advancementHolder2)
/* 69 */       .display((ItemLike)Items.DRAGON_BREATH, (Component)Component.translatable("advancements.end.dragon_breath.title"), (Component)Component.translatable("advancements.end.dragon_breath.description"), null, AdvancementType.GOAL, true, true, false)
/* 70 */       .addCriterion("dragon_breath", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DRAGON_BREATH
/* 71 */           })).save(paramConsumer, "end/dragon_breath");
/*    */     
/* 73 */     Advancement.Builder.advancement()
/* 74 */       .parent(advancementHolder4)
/* 75 */       .display((ItemLike)Items.SHULKER_SHELL, (Component)Component.translatable("advancements.end.levitate.title"), (Component)Component.translatable("advancements.end.levitate.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 76 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 77 */       .addCriterion("levitated", LevitationTrigger.TriggerInstance.levitated(DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(50.0D))))
/* 78 */       .save(paramConsumer, "end/levitate");
/*    */     
/* 80 */     Advancement.Builder.advancement()
/* 81 */       .parent(advancementHolder4)
/* 82 */       .display((ItemLike)Items.ELYTRA, (Component)Component.translatable("advancements.end.elytra.title"), (Component)Component.translatable("advancements.end.elytra.description"), null, AdvancementType.GOAL, true, true, false)
/* 83 */       .addCriterion("elytra", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.ELYTRA
/* 84 */           })).save(paramConsumer, "end/elytra");
/*    */     
/* 86 */     Advancement.Builder.advancement()
/* 87 */       .parent(advancementHolder2)
/* 88 */       .display((ItemLike)Blocks.DRAGON_EGG, (Component)Component.translatable("advancements.end.dragon_egg.title"), (Component)Component.translatable("advancements.end.dragon_egg.description"), null, AdvancementType.GOAL, true, true, false)
/* 89 */       .addCriterion("dragon_egg", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Blocks.DRAGON_EGG
/* 90 */           })).save(paramConsumer, "end/dragon_egg");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\advancements\packs\VanillaTheEndAdvancements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */