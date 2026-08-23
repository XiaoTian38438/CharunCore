/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
/*     */ 
/*     */ 
/*     */ public interface StructureSets
/*     */ {
/*     */   static void bootstrap(BootstrapContext<StructureSet> paramBootstrapContext) {
/*  23 */     HolderGetter<?> holderGetter1 = paramBootstrapContext.lookup(Registries.STRUCTURE);
/*  24 */     HolderGetter<?> holderGetter2 = paramBootstrapContext.lookup(Registries.BIOME);
/*     */ 
/*     */     
/*  27 */     Holder.Reference<StructureSet> reference = paramBootstrapContext.register(BuiltinStructureSets.VILLAGES, new StructureSet(
/*  28 */           List.of(
/*  29 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.VILLAGE_PLAINS)), 
/*  30 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.VILLAGE_DESERT)), 
/*  31 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.VILLAGE_SAVANNA)), 
/*  32 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.VILLAGE_SNOWY)), 
/*  33 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.VILLAGE_TAIGA))), (StructurePlacement)new RandomSpreadStructurePlacement(34, 8, RandomSpreadType.LINEAR, 10387312)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  38 */     paramBootstrapContext.register(BuiltinStructureSets.DESERT_PYRAMIDS, new StructureSet((Holder)holderGetter1.getOrThrow(BuiltinStructures.DESERT_PYRAMID), (StructurePlacement)new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 14357617)));
/*     */     
/*  40 */     paramBootstrapContext.register(BuiltinStructureSets.IGLOOS, new StructureSet((Holder)holderGetter1.getOrThrow(BuiltinStructures.IGLOO), (StructurePlacement)new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 14357618)));
/*     */     
/*  42 */     paramBootstrapContext.register(BuiltinStructureSets.JUNGLE_TEMPLES, new StructureSet((Holder)holderGetter1.getOrThrow(BuiltinStructures.JUNGLE_TEMPLE), (StructurePlacement)new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 14357619)));
/*     */     
/*  44 */     paramBootstrapContext.register(BuiltinStructureSets.SWAMP_HUTS, new StructureSet((Holder)holderGetter1.getOrThrow(BuiltinStructures.SWAMP_HUT), (StructurePlacement)new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 14357620)));
/*     */     
/*  46 */     paramBootstrapContext.register(BuiltinStructureSets.PILLAGER_OUTPOSTS, new StructureSet((Holder)holderGetter1.getOrThrow(BuiltinStructures.PILLAGER_OUTPOST), (StructurePlacement)new RandomSpreadStructurePlacement(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.LEGACY_TYPE_1, 0.2F, 165745296, Optional.of(new StructurePlacement.ExclusionZone((Holder)reference, 10)), 32, 8, RandomSpreadType.LINEAR)));
/*     */     
/*  48 */     paramBootstrapContext.register(BuiltinStructureSets.ANCIENT_CITIES, new StructureSet((Holder)holderGetter1.getOrThrow(BuiltinStructures.ANCIENT_CITY), (StructurePlacement)new RandomSpreadStructurePlacement(24, 8, RandomSpreadType.LINEAR, 20083232)));
/*     */     
/*  50 */     paramBootstrapContext.register(BuiltinStructureSets.OCEAN_MONUMENTS, new StructureSet((Holder)holderGetter1.getOrThrow(BuiltinStructures.OCEAN_MONUMENT), (StructurePlacement)new RandomSpreadStructurePlacement(32, 5, RandomSpreadType.TRIANGULAR, 10387313)));
/*     */     
/*  52 */     paramBootstrapContext.register(BuiltinStructureSets.WOODLAND_MANSIONS, new StructureSet((Holder)holderGetter1.getOrThrow(BuiltinStructures.WOODLAND_MANSION), (StructurePlacement)new RandomSpreadStructurePlacement(80, 20, RandomSpreadType.TRIANGULAR, 10387319)));
/*     */     
/*  54 */     paramBootstrapContext.register(BuiltinStructureSets.BURIED_TREASURES, new StructureSet((Holder)holderGetter1.getOrThrow(BuiltinStructures.BURIED_TREASURE), (StructurePlacement)new RandomSpreadStructurePlacement(new Vec3i(9, 0, 9), StructurePlacement.FrequencyReductionMethod.LEGACY_TYPE_2, 0.01F, 0, Optional.empty(), 1, 0, RandomSpreadType.LINEAR)));
/*     */     
/*  56 */     paramBootstrapContext.register(BuiltinStructureSets.MINESHAFTS, new StructureSet(
/*  57 */           List.of(
/*  58 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.MINESHAFT)), 
/*  59 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.MINESHAFT_MESA))), (StructurePlacement)new RandomSpreadStructurePlacement(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.LEGACY_TYPE_3, 0.004F, 0, 
/*     */             
/*  61 */             Optional.empty(), 1, 0, RandomSpreadType.LINEAR)));
/*     */ 
/*     */     
/*  64 */     paramBootstrapContext.register(BuiltinStructureSets.RUINED_PORTALS, new StructureSet(
/*  65 */           List.of(
/*  66 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.RUINED_PORTAL_STANDARD)), 
/*  67 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.RUINED_PORTAL_DESERT)), 
/*  68 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.RUINED_PORTAL_JUNGLE)), 
/*  69 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.RUINED_PORTAL_SWAMP)), 
/*  70 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.RUINED_PORTAL_MOUNTAIN)), 
/*  71 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.RUINED_PORTAL_OCEAN)), 
/*  72 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.RUINED_PORTAL_NETHER))), (StructurePlacement)new RandomSpreadStructurePlacement(40, 15, RandomSpreadType.LINEAR, 34222645)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     paramBootstrapContext.register(BuiltinStructureSets.SHIPWRECKS, new StructureSet(
/*  78 */           List.of(
/*  79 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.SHIPWRECK)), 
/*  80 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.SHIPWRECK_BEACHED))), (StructurePlacement)new RandomSpreadStructurePlacement(24, 4, RandomSpreadType.LINEAR, 165745295)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     paramBootstrapContext.register(BuiltinStructureSets.OCEAN_RUINS, new StructureSet(
/*  86 */           List.of(
/*  87 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.OCEAN_RUIN_COLD)), 
/*  88 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.OCEAN_RUIN_WARM))), (StructurePlacement)new RandomSpreadStructurePlacement(20, 8, RandomSpreadType.LINEAR, 14357621)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     paramBootstrapContext.register(BuiltinStructureSets.NETHER_COMPLEXES, new StructureSet(
/*  94 */           List.of(
/*  95 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.FORTRESS), 2), 
/*  96 */             StructureSet.entry((Holder)holderGetter1.getOrThrow(BuiltinStructures.BASTION_REMNANT), 3)), (StructurePlacement)new RandomSpreadStructurePlacement(27, 4, RandomSpreadType.LINEAR, 30084232)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     paramBootstrapContext.register(BuiltinStructureSets.NETHER_FOSSILS, new StructureSet((Holder)holderGetter1.getOrThrow(BuiltinStructures.NETHER_FOSSIL), (StructurePlacement)new RandomSpreadStructurePlacement(2, 1, RandomSpreadType.LINEAR, 14357921)));
/*     */     
/* 103 */     paramBootstrapContext.register(BuiltinStructureSets.END_CITIES, new StructureSet((Holder)holderGetter1.getOrThrow(BuiltinStructures.END_CITY), (StructurePlacement)new RandomSpreadStructurePlacement(20, 11, RandomSpreadType.TRIANGULAR, 10387313)));
/*     */     
/* 105 */     paramBootstrapContext.register(BuiltinStructureSets.STRONGHOLDS, new StructureSet((Holder)holderGetter1.getOrThrow(BuiltinStructures.STRONGHOLD), (StructurePlacement)new ConcentricRingsStructurePlacement(32, 3, 128, (HolderSet)holderGetter2.getOrThrow(BiomeTags.STRONGHOLD_BIASED_TO))));
/*     */     
/* 107 */     paramBootstrapContext.register(BuiltinStructureSets.TRAIL_RUINS, new StructureSet((Holder)holderGetter1.getOrThrow(BuiltinStructures.TRAIL_RUINS), (StructurePlacement)new RandomSpreadStructurePlacement(34, 8, RandomSpreadType.LINEAR, 83469867)));
/*     */     
/* 109 */     paramBootstrapContext.register(BuiltinStructureSets.TRIAL_CHAMBERS, new StructureSet((Holder)holderGetter1.getOrThrow(BuiltinStructures.TRIAL_CHAMBERS), (StructurePlacement)new RandomSpreadStructurePlacement(34, 12, RandomSpreadType.LINEAR, 94251327)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\StructureSets.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */