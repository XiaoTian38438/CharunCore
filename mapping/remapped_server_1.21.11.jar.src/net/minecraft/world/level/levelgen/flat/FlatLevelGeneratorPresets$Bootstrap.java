/*     */ package net.minecraft.world.level.levelgen.flat;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Bootstrap
/*     */ {
/*     */   private final BootstrapContext<FlatLevelGeneratorPreset> context;
/*     */   
/*     */   Bootstrap(BootstrapContext<FlatLevelGeneratorPreset> paramBootstrapContext) {
/*  38 */     this.context = paramBootstrapContext;
/*     */   }
/*     */   
/*     */   private void register(ResourceKey<FlatLevelGeneratorPreset> paramResourceKey, ItemLike paramItemLike, ResourceKey<Biome> paramResourceKey1, Set<ResourceKey<StructureSet>> paramSet, boolean paramBoolean1, boolean paramBoolean2, FlatLayerInfo... paramVarArgs) {
/*  42 */     HolderGetter holderGetter1 = this.context.lookup(Registries.STRUCTURE_SET);
/*  43 */     HolderGetter<PlacedFeature> holderGetter = this.context.lookup(Registries.PLACED_FEATURE);
/*  44 */     HolderGetter holderGetter2 = this.context.lookup(Registries.BIOME);
/*     */     
/*  46 */     Objects.requireNonNull(holderGetter1); HolderSet.Direct direct = HolderSet.direct((List)paramSet.stream().map(holderGetter1::getOrThrow).collect(Collectors.toList()));
/*  47 */     FlatLevelGeneratorSettings flatLevelGeneratorSettings = new FlatLevelGeneratorSettings((Optional)Optional.of(direct), (Holder<Biome>)holderGetter2.getOrThrow(paramResourceKey1), FlatLevelGeneratorSettings.createLakesList(holderGetter));
/*  48 */     if (paramBoolean1) {
/*  49 */       flatLevelGeneratorSettings.setDecoration();
/*     */     }
/*     */     
/*  52 */     if (paramBoolean2) {
/*  53 */       flatLevelGeneratorSettings.setAddLakes();
/*     */     }
/*     */     
/*  56 */     for (int i = paramVarArgs.length - 1; i >= 0; i--) {
/*  57 */       flatLevelGeneratorSettings.getLayersInfo().add(paramVarArgs[i]);
/*     */     }
/*     */     
/*  60 */     this.context.register(paramResourceKey, new FlatLevelGeneratorPreset((Holder<Item>)paramItemLike
/*  61 */           .asItem().builtInRegistryHolder(), flatLevelGeneratorSettings));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*  67 */     register(FlatLevelGeneratorPresets.CLASSIC_FLAT, (ItemLike)Blocks.GRASS_BLOCK, Biomes.PLAINS, 
/*     */ 
/*     */         
/*  70 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.VILLAGES), false, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(2, Blocks.DIRT), new FlatLayerInfo(1, Blocks.BEDROCK) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     register(FlatLevelGeneratorPresets.TUNNELERS_DREAM, (ItemLike)Blocks.STONE, Biomes.WINDSWEPT_HILLS, 
/*     */ 
/*     */         
/*  83 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.MINESHAFTS, BuiltinStructureSets.STRONGHOLDS), true, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(230, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
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
/*  95 */     register(FlatLevelGeneratorPresets.WATER_WORLD, (ItemLike)Items.WATER_BUCKET, Biomes.DEEP_OCEAN, 
/*     */ 
/*     */         
/*  98 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.OCEAN_RUINS, BuiltinStructureSets.SHIPWRECKS, BuiltinStructureSets.OCEAN_MONUMENTS), false, false, new FlatLayerInfo[] { new FlatLayerInfo(90, Blocks.WATER), new FlatLayerInfo(5, Blocks.GRAVEL), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(5, Blocks.STONE), new FlatLayerInfo(64, Blocks.DEEPSLATE), new FlatLayerInfo(1, Blocks.BEDROCK) });
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
/* 113 */     register(FlatLevelGeneratorPresets.OVERWORLD, (ItemLike)Blocks.SHORT_GRASS, Biomes.PLAINS, 
/*     */ 
/*     */         
/* 116 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.VILLAGES, BuiltinStructureSets.MINESHAFTS, BuiltinStructureSets.PILLAGER_OUTPOSTS, BuiltinStructureSets.RUINED_PORTALS, BuiltinStructureSets.STRONGHOLDS), true, true, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
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
/* 131 */     register(FlatLevelGeneratorPresets.SNOWY_KINGDOM, (ItemLike)Blocks.SNOW, Biomes.SNOWY_PLAINS, 
/*     */ 
/*     */         
/* 134 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.VILLAGES, BuiltinStructureSets.IGLOOS), false, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.SNOW), new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
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
/* 147 */     register(FlatLevelGeneratorPresets.BOTTOMLESS_PIT, (ItemLike)Items.FEATHER, Biomes.PLAINS, 
/*     */ 
/*     */         
/* 150 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.VILLAGES), false, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(2, Blocks.COBBLESTONE) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     register(FlatLevelGeneratorPresets.DESERT, (ItemLike)Blocks.SAND, Biomes.DESERT, 
/*     */ 
/*     */         
/* 163 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.VILLAGES, BuiltinStructureSets.DESERT_PYRAMIDS, BuiltinStructureSets.MINESHAFTS, BuiltinStructureSets.STRONGHOLDS), true, false, new FlatLayerInfo[] { new FlatLayerInfo(8, Blocks.SAND), new FlatLayerInfo(52, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
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
/* 177 */     register(FlatLevelGeneratorPresets.REDSTONE_READY, (ItemLike)Items.REDSTONE, Biomes.DESERT, 
/*     */ 
/*     */         
/* 180 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(), false, false, new FlatLayerInfo[] { new FlatLayerInfo(116, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     register(FlatLevelGeneratorPresets.THE_VOID, (ItemLike)Blocks.BARRIER, Biomes.THE_VOID, 
/*     */ 
/*     */         
/* 191 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(), true, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.AIR) });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\flat\FlatLevelGeneratorPresets$Bootstrap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */