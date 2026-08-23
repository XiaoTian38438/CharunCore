/*     */ package net.minecraft.world.level.levelgen.presets;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.biome.FixedBiomeSource;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
/*     */ import net.minecraft.world.level.biome.TheEndBiomeSource;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.levelgen.DebugLevelSource;
/*     */ import net.minecraft.world.level.levelgen.FlatLevelSource;
/*     */ import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Bootstrap
/*     */ {
/*     */   private final BootstrapContext<WorldPreset> context;
/*     */   private final HolderGetter<NoiseGeneratorSettings> noiseSettings;
/*     */   private final HolderGetter<Biome> biomes;
/*     */   private final HolderGetter<PlacedFeature> placedFeatures;
/*     */   private final HolderGetter<StructureSet> structureSets;
/*     */   private final HolderGetter<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterLists;
/*     */   private final Holder<DimensionType> overworldDimensionType;
/*     */   private final LevelStem netherStem;
/*     */   private final LevelStem endStem;
/*     */   
/*     */   Bootstrap(BootstrapContext<WorldPreset> paramBootstrapContext) {
/*  56 */     this.context = paramBootstrapContext;
/*     */     
/*  58 */     HolderGetter holderGetter = paramBootstrapContext.lookup(Registries.DIMENSION_TYPE);
/*     */     
/*  60 */     this.noiseSettings = paramBootstrapContext.lookup(Registries.NOISE_SETTINGS);
/*  61 */     this.biomes = paramBootstrapContext.lookup(Registries.BIOME);
/*  62 */     this.placedFeatures = paramBootstrapContext.lookup(Registries.PLACED_FEATURE);
/*  63 */     this.structureSets = paramBootstrapContext.lookup(Registries.STRUCTURE_SET);
/*  64 */     this.multiNoiseBiomeSourceParameterLists = paramBootstrapContext.lookup(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST);
/*     */     
/*  66 */     this.overworldDimensionType = (Holder<DimensionType>)holderGetter.getOrThrow(BuiltinDimensionTypes.OVERWORLD);
/*     */     
/*  68 */     Holder.Reference reference1 = holderGetter.getOrThrow(BuiltinDimensionTypes.NETHER);
/*  69 */     Holder.Reference reference2 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.NETHER);
/*     */     
/*  71 */     Holder.Reference reference3 = this.multiNoiseBiomeSourceParameterLists.getOrThrow(MultiNoiseBiomeSourceParameterLists.NETHER);
/*     */     
/*  73 */     this.netherStem = new LevelStem((Holder)reference1, (ChunkGenerator)new NoiseBasedChunkGenerator((BiomeSource)MultiNoiseBiomeSource.createFromPreset((Holder)reference3), (Holder)reference2));
/*     */     
/*  75 */     Holder.Reference reference4 = holderGetter.getOrThrow(BuiltinDimensionTypes.END);
/*  76 */     Holder.Reference reference5 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.END);
/*     */     
/*  78 */     this.endStem = new LevelStem((Holder)reference4, (ChunkGenerator)new NoiseBasedChunkGenerator((BiomeSource)TheEndBiomeSource.create(this.biomes), (Holder)reference5));
/*     */   }
/*     */   
/*     */   private LevelStem makeOverworld(ChunkGenerator paramChunkGenerator) {
/*  82 */     return new LevelStem(this.overworldDimensionType, paramChunkGenerator);
/*     */   }
/*     */   
/*     */   private LevelStem makeNoiseBasedOverworld(BiomeSource paramBiomeSource, Holder<NoiseGeneratorSettings> paramHolder) {
/*  86 */     return makeOverworld((ChunkGenerator)new NoiseBasedChunkGenerator(paramBiomeSource, paramHolder));
/*     */   }
/*     */   
/*     */   private WorldPreset createPresetWithCustomOverworld(LevelStem paramLevelStem) {
/*  90 */     return new WorldPreset(
/*  91 */         Map.of(LevelStem.OVERWORLD, paramLevelStem, LevelStem.NETHER, this.netherStem, LevelStem.END, this.endStem));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void registerCustomOverworldPreset(ResourceKey<WorldPreset> paramResourceKey, LevelStem paramLevelStem) {
/* 100 */     this.context.register(paramResourceKey, createPresetWithCustomOverworld(paramLevelStem));
/*     */   }
/*     */   
/*     */   private void registerOverworlds(BiomeSource paramBiomeSource) {
/* 104 */     Holder.Reference reference1 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD);
/* 105 */     registerCustomOverworldPreset(WorldPresets.NORMAL, makeNoiseBasedOverworld(paramBiomeSource, (Holder<NoiseGeneratorSettings>)reference1));
/*     */     
/* 107 */     Holder.Reference reference2 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.LARGE_BIOMES);
/* 108 */     registerCustomOverworldPreset(WorldPresets.LARGE_BIOMES, makeNoiseBasedOverworld(paramBiomeSource, (Holder<NoiseGeneratorSettings>)reference2));
/*     */     
/* 110 */     Holder.Reference reference3 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED);
/* 111 */     registerCustomOverworldPreset(WorldPresets.AMPLIFIED, makeNoiseBasedOverworld(paramBiomeSource, (Holder<NoiseGeneratorSettings>)reference3));
/*     */   }
/*     */   
/*     */   public void bootstrap() {
/* 115 */     Holder.Reference reference1 = this.multiNoiseBiomeSourceParameterLists.getOrThrow(MultiNoiseBiomeSourceParameterLists.OVERWORLD);
/* 116 */     registerOverworlds((BiomeSource)MultiNoiseBiomeSource.createFromPreset((Holder)reference1));
/*     */     
/* 118 */     Holder.Reference reference2 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD);
/* 119 */     Holder.Reference reference3 = this.biomes.getOrThrow(Biomes.PLAINS);
/* 120 */     registerCustomOverworldPreset(WorldPresets.SINGLE_BIOME_SURFACE, makeNoiseBasedOverworld((BiomeSource)new FixedBiomeSource((Holder)reference3), (Holder<NoiseGeneratorSettings>)reference2));
/*     */     
/* 122 */     registerCustomOverworldPreset(WorldPresets.FLAT, makeOverworld((ChunkGenerator)new FlatLevelSource(FlatLevelGeneratorSettings.getDefault(this.biomes, this.structureSets, this.placedFeatures))));
/*     */     
/* 124 */     registerCustomOverworldPreset(WorldPresets.DEBUG, makeOverworld((ChunkGenerator)new DebugLevelSource(reference3)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\presets\WorldPresets$Bootstrap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */