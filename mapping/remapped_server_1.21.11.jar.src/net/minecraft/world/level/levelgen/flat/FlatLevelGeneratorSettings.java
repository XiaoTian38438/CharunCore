/*     */ package net.minecraft.world.level.levelgen.flat;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FlatLevelGeneratorSettings {
/*  36 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */   
/*     */   public static final Codec<FlatLevelGeneratorSettings> CODEC;
/*     */ 
/*     */   
/*     */   private final Optional<HolderSet<StructureSet>> structureOverrides;
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  47 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)RegistryCodecs.homogeneousList(Registries.STRUCTURE_SET).lenientOptionalFieldOf("structure_overrides").forGetter(()), (App)FlatLayerInfo.CODEC.listOf().fieldOf("layers").forGetter(FlatLevelGeneratorSettings::getLayersInfo), (App)Codec.BOOL.fieldOf("lakes").orElse(Boolean.valueOf(false)).forGetter(()), (App)Codec.BOOL.fieldOf("features").orElse(Boolean.valueOf(false)).forGetter(()), (App)Biome.CODEC.lenientOptionalFieldOf("biome").orElseGet(Optional::empty).forGetter(()), (App)RegistryOps.retrieveElement(Biomes.PLAINS), (App)RegistryOps.retrieveElement(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND), (App)RegistryOps.retrieveElement(MiscOverworldPlacements.LAKE_LAVA_SURFACE)).apply((Applicative)paramInstance, FlatLevelGeneratorSettings::new)).comapFlatMap(FlatLevelGeneratorSettings::validateHeight, Function.identity()).stable();
/*     */   }
/*     */   private static DataResult<FlatLevelGeneratorSettings> validateHeight(FlatLevelGeneratorSettings paramFlatLevelGeneratorSettings) {
/*  50 */     int i = paramFlatLevelGeneratorSettings.layersInfo.stream().mapToInt(FlatLayerInfo::getHeight).sum();
/*     */     
/*  52 */     if (i > DimensionType.Y_SIZE) {
/*  53 */       return DataResult.error(() -> "Sum of layer heights is > " + DimensionType.Y_SIZE, paramFlatLevelGeneratorSettings);
/*     */     }
/*  55 */     return DataResult.success(paramFlatLevelGeneratorSettings);
/*     */   }
/*     */ 
/*     */   
/*  59 */   private final List<FlatLayerInfo> layersInfo = Lists.newArrayList();
/*     */   private final Holder<Biome> biome;
/*     */   private final List<BlockState> layers;
/*     */   private boolean voidGen;
/*     */   private boolean decoration;
/*     */   private boolean addLakes;
/*     */   private final List<Holder<PlacedFeature>> lakes;
/*     */   
/*     */   private FlatLevelGeneratorSettings(Optional<HolderSet<StructureSet>> paramOptional, List<FlatLayerInfo> paramList, boolean paramBoolean1, boolean paramBoolean2, Optional<Holder<Biome>> paramOptional1, Holder.Reference<Biome> paramReference, Holder<PlacedFeature> paramHolder1, Holder<PlacedFeature> paramHolder2) {
/*  68 */     this(paramOptional, getBiome(paramOptional1, (Holder<Biome>)paramReference), List.of(paramHolder1, paramHolder2));
/*  69 */     if (paramBoolean1) {
/*  70 */       setAddLakes();
/*     */     }
/*  72 */     if (paramBoolean2) {
/*  73 */       setDecoration();
/*     */     }
/*  75 */     this.layersInfo.addAll(paramList);
/*  76 */     updateLayers();
/*     */   }
/*     */   
/*     */   private static Holder<Biome> getBiome(Optional<? extends Holder<Biome>> paramOptional, Holder<Biome> paramHolder) {
/*  80 */     if (paramOptional.isEmpty()) {
/*  81 */       LOGGER.error("Unknown biome, defaulting to plains");
/*  82 */       return paramHolder;
/*     */     } 
/*  84 */     return paramOptional.get();
/*     */   }
/*     */   
/*     */   public FlatLevelGeneratorSettings(Optional<HolderSet<StructureSet>> paramOptional, Holder<Biome> paramHolder, List<Holder<PlacedFeature>> paramList) {
/*  88 */     this.structureOverrides = paramOptional;
/*  89 */     this.biome = paramHolder;
/*  90 */     this.layers = Lists.newArrayList();
/*  91 */     this.lakes = paramList;
/*     */   }
/*     */   
/*     */   public FlatLevelGeneratorSettings withBiomeAndLayers(List<FlatLayerInfo> paramList, Optional<HolderSet<StructureSet>> paramOptional, Holder<Biome> paramHolder) {
/*  95 */     FlatLevelGeneratorSettings flatLevelGeneratorSettings = new FlatLevelGeneratorSettings(paramOptional, paramHolder, this.lakes);
/*  96 */     for (FlatLayerInfo flatLayerInfo : paramList) {
/*  97 */       flatLevelGeneratorSettings.layersInfo.add(new FlatLayerInfo(flatLayerInfo.getHeight(), flatLayerInfo.getBlockState().getBlock()));
/*  98 */       flatLevelGeneratorSettings.updateLayers();
/*     */     } 
/* 100 */     if (this.decoration) {
/* 101 */       flatLevelGeneratorSettings.setDecoration();
/*     */     }
/* 103 */     if (this.addLakes) {
/* 104 */       flatLevelGeneratorSettings.setAddLakes();
/*     */     }
/* 106 */     return flatLevelGeneratorSettings;
/*     */   }
/*     */   
/*     */   public void setDecoration() {
/* 110 */     this.decoration = true;
/*     */   }
/*     */   
/*     */   public void setAddLakes() {
/* 114 */     this.addLakes = true;
/*     */   }
/*     */   
/*     */   public BiomeGenerationSettings adjustGenerationSettings(Holder<Biome> paramHolder) {
/* 118 */     if (!paramHolder.equals(this.biome)) {
/* 119 */       return ((Biome)paramHolder.value()).getGenerationSettings();
/*     */     }
/* 121 */     BiomeGenerationSettings biomeGenerationSettings = ((Biome)getBiome().value()).getGenerationSettings();
/*     */     
/* 123 */     BiomeGenerationSettings.PlainBuilder plainBuilder = new BiomeGenerationSettings.PlainBuilder();
/*     */     
/* 125 */     if (this.addLakes) {
/* 126 */       for (Holder<PlacedFeature> holder : this.lakes) {
/* 127 */         plainBuilder.addFeature(GenerationStep.Decoration.LAKES, holder);
/*     */       }
/*     */     }
/*     */     
/* 131 */     boolean bool = ((!this.voidGen || paramHolder.is(Biomes.THE_VOID)) && this.decoration) ? true : false;
/*     */     
/* 133 */     if (bool) {
/* 134 */       List<HolderSet> list1 = biomeGenerationSettings.features();
/* 135 */       for (byte b1 = 0; b1 < list1.size(); b1++) {
/* 136 */         if (b1 != GenerationStep.Decoration.UNDERGROUND_STRUCTURES.ordinal() && b1 != GenerationStep.Decoration.SURFACE_STRUCTURES
/* 137 */           .ordinal() && (!this.addLakes || b1 != GenerationStep.Decoration.LAKES
/* 138 */           .ordinal())) {
/*     */ 
/*     */ 
/*     */           
/* 142 */           HolderSet holderSet = list1.get(b1);
/* 143 */           for (Holder holder : holderSet) {
/* 144 */             plainBuilder.addFeature(b1, holder);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 149 */     List<BlockState> list = getLayers();
/* 150 */     for (byte b = 0; b < list.size(); b++) {
/* 151 */       BlockState blockState = list.get(b);
/*     */ 
/*     */       
/* 154 */       if (!Heightmap.Types.MOTION_BLOCKING.isOpaque().test(blockState)) {
/* 155 */         list.set(b, null);
/* 156 */         plainBuilder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacementUtils.inlinePlaced(Feature.FILL_LAYER, (FeatureConfiguration)new LayerConfiguration(b, blockState), new net.minecraft.world.level.levelgen.placement.PlacementModifier[0]));
/*     */       } 
/*     */     } 
/*     */     
/* 160 */     return plainBuilder.build();
/*     */   }
/*     */   
/*     */   public Optional<HolderSet<StructureSet>> structureOverrides() {
/* 164 */     return this.structureOverrides;
/*     */   }
/*     */   
/*     */   public Holder<Biome> getBiome() {
/* 168 */     return this.biome;
/*     */   }
/*     */   
/*     */   public List<FlatLayerInfo> getLayersInfo() {
/* 172 */     return this.layersInfo;
/*     */   }
/*     */   
/*     */   public List<BlockState> getLayers() {
/* 176 */     return this.layers;
/*     */   }
/*     */   
/*     */   public void updateLayers() {
/* 180 */     this.layers.clear();
/*     */     
/* 182 */     for (FlatLayerInfo flatLayerInfo : this.layersInfo) {
/* 183 */       for (byte b = 0; b < flatLayerInfo.getHeight(); b++) {
/* 184 */         this.layers.add(flatLayerInfo.getBlockState());
/*     */       }
/*     */     } 
/*     */     
/* 188 */     this.voidGen = this.layers.stream().allMatch(paramBlockState -> paramBlockState.is(Blocks.AIR));
/*     */   }
/*     */   
/*     */   public static FlatLevelGeneratorSettings getDefault(HolderGetter<Biome> paramHolderGetter, HolderGetter<StructureSet> paramHolderGetter1, HolderGetter<PlacedFeature> paramHolderGetter2) {
/* 192 */     HolderSet.Direct direct = HolderSet.direct(new Holder[] { (Holder)paramHolderGetter1
/* 193 */           .getOrThrow(BuiltinStructureSets.STRONGHOLDS), (Holder)paramHolderGetter1
/* 194 */           .getOrThrow(BuiltinStructureSets.VILLAGES) });
/*     */ 
/*     */     
/* 197 */     FlatLevelGeneratorSettings flatLevelGeneratorSettings = new FlatLevelGeneratorSettings((Optional)Optional.of(direct), getDefaultBiome(paramHolderGetter), createLakesList(paramHolderGetter2));
/* 198 */     flatLevelGeneratorSettings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.BEDROCK));
/* 199 */     flatLevelGeneratorSettings.getLayersInfo().add(new FlatLayerInfo(2, Blocks.DIRT));
/* 200 */     flatLevelGeneratorSettings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.GRASS_BLOCK));
/* 201 */     flatLevelGeneratorSettings.updateLayers();
/*     */     
/* 203 */     return flatLevelGeneratorSettings;
/*     */   }
/*     */   
/*     */   public static Holder<Biome> getDefaultBiome(HolderGetter<Biome> paramHolderGetter) {
/* 207 */     return (Holder<Biome>)paramHolderGetter.getOrThrow(Biomes.PLAINS);
/*     */   }
/*     */   
/*     */   public static List<Holder<PlacedFeature>> createLakesList(HolderGetter<PlacedFeature> paramHolderGetter) {
/* 211 */     return (List)List.of(paramHolderGetter
/* 212 */         .getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND), paramHolderGetter
/* 213 */         .getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\flat\FlatLevelGeneratorSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */