/*     */ package net.minecraft.world.level.levelgen.presets;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
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
/*     */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ 
/*     */ public class WorldPresets
/*     */ {
/*  35 */   public static final ResourceKey<WorldPreset> NORMAL = register("normal");
/*  36 */   public static final ResourceKey<WorldPreset> FLAT = register("flat");
/*  37 */   public static final ResourceKey<WorldPreset> LARGE_BIOMES = register("large_biomes");
/*  38 */   public static final ResourceKey<WorldPreset> AMPLIFIED = register("amplified");
/*  39 */   public static final ResourceKey<WorldPreset> SINGLE_BIOME_SURFACE = register("single_biome_surface");
/*  40 */   public static final ResourceKey<WorldPreset> DEBUG = register("debug_all_block_states");
/*     */ 
/*     */   
/*     */   private static class Bootstrap
/*     */   {
/*     */     private final BootstrapContext<WorldPreset> context;
/*     */     private final HolderGetter<NoiseGeneratorSettings> noiseSettings;
/*     */     private final HolderGetter<Biome> biomes;
/*     */     private final HolderGetter<PlacedFeature> placedFeatures;
/*     */     private final HolderGetter<StructureSet> structureSets;
/*     */     private final HolderGetter<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterLists;
/*     */     private final Holder<DimensionType> overworldDimensionType;
/*     */     private final LevelStem netherStem;
/*     */     private final LevelStem endStem;
/*     */     
/*     */     Bootstrap(BootstrapContext<WorldPreset> param1BootstrapContext) {
/*  56 */       this.context = param1BootstrapContext;
/*     */       
/*  58 */       HolderGetter holderGetter = param1BootstrapContext.lookup(Registries.DIMENSION_TYPE);
/*     */       
/*  60 */       this.noiseSettings = param1BootstrapContext.lookup(Registries.NOISE_SETTINGS);
/*  61 */       this.biomes = param1BootstrapContext.lookup(Registries.BIOME);
/*  62 */       this.placedFeatures = param1BootstrapContext.lookup(Registries.PLACED_FEATURE);
/*  63 */       this.structureSets = param1BootstrapContext.lookup(Registries.STRUCTURE_SET);
/*  64 */       this.multiNoiseBiomeSourceParameterLists = param1BootstrapContext.lookup(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST);
/*     */       
/*  66 */       this.overworldDimensionType = (Holder<DimensionType>)holderGetter.getOrThrow(BuiltinDimensionTypes.OVERWORLD);
/*     */       
/*  68 */       Holder.Reference reference1 = holderGetter.getOrThrow(BuiltinDimensionTypes.NETHER);
/*  69 */       Holder.Reference reference2 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.NETHER);
/*     */       
/*  71 */       Holder.Reference reference3 = this.multiNoiseBiomeSourceParameterLists.getOrThrow(MultiNoiseBiomeSourceParameterLists.NETHER);
/*     */       
/*  73 */       this.netherStem = new LevelStem((Holder)reference1, (ChunkGenerator)new NoiseBasedChunkGenerator((BiomeSource)MultiNoiseBiomeSource.createFromPreset((Holder)reference3), (Holder)reference2));
/*     */       
/*  75 */       Holder.Reference reference4 = holderGetter.getOrThrow(BuiltinDimensionTypes.END);
/*  76 */       Holder.Reference reference5 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.END);
/*     */       
/*  78 */       this.endStem = new LevelStem((Holder)reference4, (ChunkGenerator)new NoiseBasedChunkGenerator((BiomeSource)TheEndBiomeSource.create(this.biomes), (Holder)reference5));
/*     */     }
/*     */     
/*     */     private LevelStem makeOverworld(ChunkGenerator param1ChunkGenerator) {
/*  82 */       return new LevelStem(this.overworldDimensionType, param1ChunkGenerator);
/*     */     }
/*     */     
/*     */     private LevelStem makeNoiseBasedOverworld(BiomeSource param1BiomeSource, Holder<NoiseGeneratorSettings> param1Holder) {
/*  86 */       return makeOverworld((ChunkGenerator)new NoiseBasedChunkGenerator(param1BiomeSource, param1Holder));
/*     */     }
/*     */     
/*     */     private WorldPreset createPresetWithCustomOverworld(LevelStem param1LevelStem) {
/*  90 */       return new WorldPreset(
/*  91 */           Map.of(LevelStem.OVERWORLD, param1LevelStem, LevelStem.NETHER, this.netherStem, LevelStem.END, this.endStem));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void registerCustomOverworldPreset(ResourceKey<WorldPreset> param1ResourceKey, LevelStem param1LevelStem) {
/* 100 */       this.context.register(param1ResourceKey, createPresetWithCustomOverworld(param1LevelStem));
/*     */     }
/*     */     
/*     */     private void registerOverworlds(BiomeSource param1BiomeSource) {
/* 104 */       Holder.Reference reference1 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD);
/* 105 */       registerCustomOverworldPreset(WorldPresets.NORMAL, makeNoiseBasedOverworld(param1BiomeSource, (Holder<NoiseGeneratorSettings>)reference1));
/*     */       
/* 107 */       Holder.Reference reference2 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.LARGE_BIOMES);
/* 108 */       registerCustomOverworldPreset(WorldPresets.LARGE_BIOMES, makeNoiseBasedOverworld(param1BiomeSource, (Holder<NoiseGeneratorSettings>)reference2));
/*     */       
/* 110 */       Holder.Reference reference3 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED);
/* 111 */       registerCustomOverworldPreset(WorldPresets.AMPLIFIED, makeNoiseBasedOverworld(param1BiomeSource, (Holder<NoiseGeneratorSettings>)reference3));
/*     */     }
/*     */     
/*     */     public void bootstrap() {
/* 115 */       Holder.Reference reference1 = this.multiNoiseBiomeSourceParameterLists.getOrThrow(MultiNoiseBiomeSourceParameterLists.OVERWORLD);
/* 116 */       registerOverworlds((BiomeSource)MultiNoiseBiomeSource.createFromPreset((Holder)reference1));
/*     */       
/* 118 */       Holder.Reference reference2 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD);
/* 119 */       Holder.Reference reference3 = this.biomes.getOrThrow(Biomes.PLAINS);
/* 120 */       registerCustomOverworldPreset(WorldPresets.SINGLE_BIOME_SURFACE, makeNoiseBasedOverworld((BiomeSource)new FixedBiomeSource((Holder)reference3), (Holder<NoiseGeneratorSettings>)reference2));
/*     */       
/* 122 */       registerCustomOverworldPreset(WorldPresets.FLAT, makeOverworld((ChunkGenerator)new FlatLevelSource(FlatLevelGeneratorSettings.getDefault(this.biomes, this.structureSets, this.placedFeatures))));
/*     */       
/* 124 */       registerCustomOverworldPreset(WorldPresets.DEBUG, makeOverworld((ChunkGenerator)new DebugLevelSource(reference3)));
/*     */     }
/*     */   }
/*     */   
/*     */   public static void bootstrap(BootstrapContext<WorldPreset> paramBootstrapContext) {
/* 129 */     (new Bootstrap(paramBootstrapContext)).bootstrap();
/*     */   }
/*     */   
/*     */   private static ResourceKey<WorldPreset> register(String paramString) {
/* 133 */     return ResourceKey.create(Registries.WORLD_PRESET, Identifier.withDefaultNamespace(paramString));
/*     */   }
/*     */   
/*     */   public static Optional<ResourceKey<WorldPreset>> fromSettings(WorldDimensions paramWorldDimensions) {
/* 137 */     return paramWorldDimensions.get(LevelStem.OVERWORLD).flatMap(paramLevelStem -> {
/*     */           // Byte code:
/*     */           //   0: aload_0
/*     */           //   1: invokevirtual generator : ()Lnet/minecraft/world/level/chunk/ChunkGenerator;
/*     */           //   4: dup
/*     */           //   5: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */           //   8: pop
/*     */           //   9: astore_1
/*     */           //   10: iconst_0
/*     */           //   11: istore_2
/*     */           //   12: aload_1
/*     */           //   13: iload_2
/*     */           //   14: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */           //   19: tableswitch default -> 88, 0 -> 44, 1 -> 58, 2 -> 73
/*     */           //   44: aload_1
/*     */           //   45: checkcast net/minecraft/world/level/levelgen/FlatLevelSource
/*     */           //   48: astore_3
/*     */           //   49: getstatic net/minecraft/world/level/levelgen/presets/WorldPresets.FLAT : Lnet/minecraft/resources/ResourceKey;
/*     */           //   52: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */           //   55: goto -> 91
/*     */           //   58: aload_1
/*     */           //   59: checkcast net/minecraft/world/level/levelgen/DebugLevelSource
/*     */           //   62: astore #4
/*     */           //   64: getstatic net/minecraft/world/level/levelgen/presets/WorldPresets.DEBUG : Lnet/minecraft/resources/ResourceKey;
/*     */           //   67: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */           //   70: goto -> 91
/*     */           //   73: aload_1
/*     */           //   74: checkcast net/minecraft/world/level/levelgen/NoiseBasedChunkGenerator
/*     */           //   77: astore #5
/*     */           //   79: getstatic net/minecraft/world/level/levelgen/presets/WorldPresets.NORMAL : Lnet/minecraft/resources/ResourceKey;
/*     */           //   82: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */           //   85: goto -> 91
/*     */           //   88: invokestatic empty : ()Ljava/util/Optional;
/*     */           //   91: areturn
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #137	-> 0
/*     */           //   #138	-> 44
/*     */           //   #139	-> 58
/*     */           //   #142	-> 73
/*     */           //   #143	-> 88
/*     */           //   #142	-> 91
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WorldDimensions createNormalWorldDimensions(HolderLookup.Provider paramProvider) {
/* 148 */     return ((WorldPreset)paramProvider.lookupOrThrow(Registries.WORLD_PRESET).getOrThrow(NORMAL).value()).createWorldDimensions();
/*     */   }
/*     */   
/*     */   public static LevelStem getNormalOverworld(HolderLookup.Provider paramProvider) {
/* 152 */     return ((WorldPreset)paramProvider.lookupOrThrow(Registries.WORLD_PRESET).getOrThrow(NORMAL).value()).overworld().orElseThrow();
/*     */   }
/*     */   
/*     */   public static WorldDimensions createFlatWorldDimensions(HolderLookup.Provider paramProvider) {
/* 156 */     return ((WorldPreset)paramProvider.lookupOrThrow(Registries.WORLD_PRESET).getOrThrow(FLAT).value()).createWorldDimensions();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\presets\WorldPresets.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */