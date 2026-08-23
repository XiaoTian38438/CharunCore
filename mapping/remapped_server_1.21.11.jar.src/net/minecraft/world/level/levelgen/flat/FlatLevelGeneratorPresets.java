/*     */ package net.minecraft.world.level.levelgen.flat;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ 
/*     */ public class FlatLevelGeneratorPresets {
/*  24 */   public static final ResourceKey<FlatLevelGeneratorPreset> CLASSIC_FLAT = register("classic_flat");
/*  25 */   public static final ResourceKey<FlatLevelGeneratorPreset> TUNNELERS_DREAM = register("tunnelers_dream");
/*  26 */   public static final ResourceKey<FlatLevelGeneratorPreset> WATER_WORLD = register("water_world");
/*  27 */   public static final ResourceKey<FlatLevelGeneratorPreset> OVERWORLD = register("overworld");
/*  28 */   public static final ResourceKey<FlatLevelGeneratorPreset> SNOWY_KINGDOM = register("snowy_kingdom");
/*  29 */   public static final ResourceKey<FlatLevelGeneratorPreset> BOTTOMLESS_PIT = register("bottomless_pit");
/*  30 */   public static final ResourceKey<FlatLevelGeneratorPreset> DESERT = register("desert");
/*  31 */   public static final ResourceKey<FlatLevelGeneratorPreset> REDSTONE_READY = register("redstone_ready");
/*  32 */   public static final ResourceKey<FlatLevelGeneratorPreset> THE_VOID = register("the_void");
/*     */   
/*     */   private static class Bootstrap {
/*     */     private final BootstrapContext<FlatLevelGeneratorPreset> context;
/*     */     
/*     */     Bootstrap(BootstrapContext<FlatLevelGeneratorPreset> param1BootstrapContext) {
/*  38 */       this.context = param1BootstrapContext;
/*     */     }
/*     */     
/*     */     private void register(ResourceKey<FlatLevelGeneratorPreset> param1ResourceKey, ItemLike param1ItemLike, ResourceKey<Biome> param1ResourceKey1, Set<ResourceKey<StructureSet>> param1Set, boolean param1Boolean1, boolean param1Boolean2, FlatLayerInfo... param1VarArgs) {
/*  42 */       HolderGetter holderGetter1 = this.context.lookup(Registries.STRUCTURE_SET);
/*  43 */       HolderGetter<PlacedFeature> holderGetter = this.context.lookup(Registries.PLACED_FEATURE);
/*  44 */       HolderGetter holderGetter2 = this.context.lookup(Registries.BIOME);
/*     */       
/*  46 */       Objects.requireNonNull(holderGetter1); HolderSet.Direct direct = HolderSet.direct((List)param1Set.stream().map(holderGetter1::getOrThrow).collect(Collectors.toList()));
/*  47 */       FlatLevelGeneratorSettings flatLevelGeneratorSettings = new FlatLevelGeneratorSettings((Optional)Optional.of(direct), (Holder<Biome>)holderGetter2.getOrThrow(param1ResourceKey1), FlatLevelGeneratorSettings.createLakesList(holderGetter));
/*  48 */       if (param1Boolean1) {
/*  49 */         flatLevelGeneratorSettings.setDecoration();
/*     */       }
/*     */       
/*  52 */       if (param1Boolean2) {
/*  53 */         flatLevelGeneratorSettings.setAddLakes();
/*     */       }
/*     */       
/*  56 */       for (int i = param1VarArgs.length - 1; i >= 0; i--) {
/*  57 */         flatLevelGeneratorSettings.getLayersInfo().add(param1VarArgs[i]);
/*     */       }
/*     */       
/*  60 */       this.context.register(param1ResourceKey, new FlatLevelGeneratorPreset((Holder<Item>)param1ItemLike
/*  61 */             .asItem().builtInRegistryHolder(), flatLevelGeneratorSettings));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*  67 */       register(FlatLevelGeneratorPresets.CLASSIC_FLAT, (ItemLike)Blocks.GRASS_BLOCK, Biomes.PLAINS, 
/*     */ 
/*     */           
/*  70 */           (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.VILLAGES), false, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(2, Blocks.DIRT), new FlatLayerInfo(1, Blocks.BEDROCK) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  80 */       register(FlatLevelGeneratorPresets.TUNNELERS_DREAM, (ItemLike)Blocks.STONE, Biomes.WINDSWEPT_HILLS, 
/*     */ 
/*     */           
/*  83 */           (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.MINESHAFTS, BuiltinStructureSets.STRONGHOLDS), true, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(230, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
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
/*  95 */       register(FlatLevelGeneratorPresets.WATER_WORLD, (ItemLike)Items.WATER_BUCKET, Biomes.DEEP_OCEAN, 
/*     */ 
/*     */           
/*  98 */           (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.OCEAN_RUINS, BuiltinStructureSets.SHIPWRECKS, BuiltinStructureSets.OCEAN_MONUMENTS), false, false, new FlatLayerInfo[] { new FlatLayerInfo(90, Blocks.WATER), new FlatLayerInfo(5, Blocks.GRAVEL), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(5, Blocks.STONE), new FlatLayerInfo(64, Blocks.DEEPSLATE), new FlatLayerInfo(1, Blocks.BEDROCK) });
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
/* 113 */       register(FlatLevelGeneratorPresets.OVERWORLD, (ItemLike)Blocks.SHORT_GRASS, Biomes.PLAINS, 
/*     */ 
/*     */           
/* 116 */           (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.VILLAGES, BuiltinStructureSets.MINESHAFTS, BuiltinStructureSets.PILLAGER_OUTPOSTS, BuiltinStructureSets.RUINED_PORTALS, BuiltinStructureSets.STRONGHOLDS), true, true, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
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
/* 131 */       register(FlatLevelGeneratorPresets.SNOWY_KINGDOM, (ItemLike)Blocks.SNOW, Biomes.SNOWY_PLAINS, 
/*     */ 
/*     */           
/* 134 */           (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.VILLAGES, BuiltinStructureSets.IGLOOS), false, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.SNOW), new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
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
/* 147 */       register(FlatLevelGeneratorPresets.BOTTOMLESS_PIT, (ItemLike)Items.FEATHER, Biomes.PLAINS, 
/*     */ 
/*     */           
/* 150 */           (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.VILLAGES), false, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(2, Blocks.COBBLESTONE) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 160 */       register(FlatLevelGeneratorPresets.DESERT, (ItemLike)Blocks.SAND, Biomes.DESERT, 
/*     */ 
/*     */           
/* 163 */           (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.VILLAGES, BuiltinStructureSets.DESERT_PYRAMIDS, BuiltinStructureSets.MINESHAFTS, BuiltinStructureSets.STRONGHOLDS), true, false, new FlatLayerInfo[] { new FlatLayerInfo(8, Blocks.SAND), new FlatLayerInfo(52, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
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
/* 177 */       register(FlatLevelGeneratorPresets.REDSTONE_READY, (ItemLike)Items.REDSTONE, Biomes.DESERT, 
/*     */ 
/*     */           
/* 180 */           (Set<ResourceKey<StructureSet>>)ImmutableSet.of(), false, false, new FlatLayerInfo[] { new FlatLayerInfo(116, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 188 */       register(FlatLevelGeneratorPresets.THE_VOID, (ItemLike)Blocks.BARRIER, Biomes.THE_VOID, 
/*     */ 
/*     */           
/* 191 */           (Set<ResourceKey<StructureSet>>)ImmutableSet.of(), true, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.AIR) });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void bootstrap(BootstrapContext<FlatLevelGeneratorPreset> paramBootstrapContext) {
/* 200 */     (new Bootstrap(paramBootstrapContext)).run();
/*     */   }
/*     */   
/*     */   private static ResourceKey<FlatLevelGeneratorPreset> register(String paramString) {
/* 204 */     return ResourceKey.create(Registries.FLAT_LEVEL_GENERATOR_PRESET, Identifier.withDefaultNamespace(paramString));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\flat\FlatLevelGeneratorPresets.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */