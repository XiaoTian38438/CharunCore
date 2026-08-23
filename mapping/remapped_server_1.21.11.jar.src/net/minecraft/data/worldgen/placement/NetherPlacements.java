/*     */ package net.minecraft.data.worldgen.placement;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.features.NetherFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.valueproviders.BiasedToBottomInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ 
/*     */ public class NetherPlacements {
/*  22 */   public static final ResourceKey<PlacedFeature> DELTA = PlacementUtils.createKey("delta");
/*  23 */   public static final ResourceKey<PlacedFeature> SMALL_BASALT_COLUMNS = PlacementUtils.createKey("small_basalt_columns");
/*  24 */   public static final ResourceKey<PlacedFeature> LARGE_BASALT_COLUMNS = PlacementUtils.createKey("large_basalt_columns");
/*  25 */   public static final ResourceKey<PlacedFeature> BASALT_BLOBS = PlacementUtils.createKey("basalt_blobs");
/*  26 */   public static final ResourceKey<PlacedFeature> BLACKSTONE_BLOBS = PlacementUtils.createKey("blackstone_blobs");
/*     */   
/*  28 */   public static final ResourceKey<PlacedFeature> GLOWSTONE_EXTRA = PlacementUtils.createKey("glowstone_extra");
/*  29 */   public static final ResourceKey<PlacedFeature> GLOWSTONE = PlacementUtils.createKey("glowstone");
/*     */   
/*  31 */   public static final ResourceKey<PlacedFeature> CRIMSON_FOREST_VEGETATION = PlacementUtils.createKey("crimson_forest_vegetation");
/*  32 */   public static final ResourceKey<PlacedFeature> WARPED_FOREST_VEGETATION = PlacementUtils.createKey("warped_forest_vegetation");
/*  33 */   public static final ResourceKey<PlacedFeature> NETHER_SPROUTS = PlacementUtils.createKey("nether_sprouts");
/*  34 */   public static final ResourceKey<PlacedFeature> TWISTING_VINES = PlacementUtils.createKey("twisting_vines");
/*  35 */   public static final ResourceKey<PlacedFeature> WEEPING_VINES = PlacementUtils.createKey("weeping_vines");
/*  36 */   public static final ResourceKey<PlacedFeature> PATCH_CRIMSON_ROOTS = PlacementUtils.createKey("patch_crimson_roots");
/*     */   
/*  38 */   public static final ResourceKey<PlacedFeature> BASALT_PILLAR = PlacementUtils.createKey("basalt_pillar");
/*     */   
/*  40 */   public static final ResourceKey<PlacedFeature> SPRING_DELTA = PlacementUtils.createKey("spring_delta");
/*  41 */   public static final ResourceKey<PlacedFeature> SPRING_CLOSED = PlacementUtils.createKey("spring_closed");
/*  42 */   public static final ResourceKey<PlacedFeature> SPRING_CLOSED_DOUBLE = PlacementUtils.createKey("spring_closed_double");
/*  43 */   public static final ResourceKey<PlacedFeature> SPRING_OPEN = PlacementUtils.createKey("spring_open");
/*     */   
/*  45 */   public static final ResourceKey<PlacedFeature> PATCH_SOUL_FIRE = PlacementUtils.createKey("patch_soul_fire");
/*  46 */   public static final ResourceKey<PlacedFeature> PATCH_FIRE = PlacementUtils.createKey("patch_fire");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> paramBootstrapContext) {
/*  49 */     HolderGetter holderGetter = paramBootstrapContext.lookup(Registries.CONFIGURED_FEATURE);
/*  50 */     Holder.Reference reference1 = holderGetter.getOrThrow(NetherFeatures.DELTA);
/*  51 */     Holder.Reference reference2 = holderGetter.getOrThrow(NetherFeatures.SMALL_BASALT_COLUMNS);
/*  52 */     Holder.Reference reference3 = holderGetter.getOrThrow(NetherFeatures.LARGE_BASALT_COLUMNS);
/*  53 */     Holder.Reference reference4 = holderGetter.getOrThrow(NetherFeatures.BASALT_BLOBS);
/*  54 */     Holder.Reference reference5 = holderGetter.getOrThrow(NetherFeatures.BLACKSTONE_BLOBS);
/*  55 */     Holder.Reference reference6 = holderGetter.getOrThrow(NetherFeatures.GLOWSTONE_EXTRA);
/*  56 */     Holder.Reference reference7 = holderGetter.getOrThrow(NetherFeatures.CRIMSON_FOREST_VEGETATION);
/*  57 */     Holder.Reference reference8 = holderGetter.getOrThrow(NetherFeatures.WARPED_FOREST_VEGETION);
/*  58 */     Holder.Reference reference9 = holderGetter.getOrThrow(NetherFeatures.NETHER_SPROUTS);
/*  59 */     Holder.Reference reference10 = holderGetter.getOrThrow(NetherFeatures.TWISTING_VINES);
/*  60 */     Holder.Reference reference11 = holderGetter.getOrThrow(NetherFeatures.WEEPING_VINES);
/*  61 */     Holder.Reference reference12 = holderGetter.getOrThrow(NetherFeatures.PATCH_CRIMSON_ROOTS);
/*  62 */     Holder.Reference reference13 = holderGetter.getOrThrow(NetherFeatures.BASALT_PILLAR);
/*  63 */     Holder.Reference reference14 = holderGetter.getOrThrow(NetherFeatures.SPRING_LAVA_NETHER);
/*  64 */     Holder.Reference reference15 = holderGetter.getOrThrow(NetherFeatures.SPRING_NETHER_CLOSED);
/*  65 */     Holder.Reference reference16 = holderGetter.getOrThrow(NetherFeatures.SPRING_NETHER_OPEN);
/*  66 */     Holder.Reference reference17 = holderGetter.getOrThrow(NetherFeatures.PATCH_SOUL_FIRE);
/*  67 */     Holder.Reference reference18 = holderGetter.getOrThrow(NetherFeatures.PATCH_FIRE);
/*     */     
/*  69 */     PlacementUtils.register(paramBootstrapContext, DELTA, (Holder<ConfiguredFeature<?, ?>>)reference1, new PlacementModifier[] {
/*  70 */           (PlacementModifier)CountOnEveryLayerPlacement.of(40), 
/*  71 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*  73 */     PlacementUtils.register(paramBootstrapContext, SMALL_BASALT_COLUMNS, (Holder<ConfiguredFeature<?, ?>>)reference2, new PlacementModifier[] {
/*  74 */           (PlacementModifier)CountOnEveryLayerPlacement.of(4), 
/*  75 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*  77 */     PlacementUtils.register(paramBootstrapContext, LARGE_BASALT_COLUMNS, (Holder<ConfiguredFeature<?, ?>>)reference3, new PlacementModifier[] {
/*  78 */           (PlacementModifier)CountOnEveryLayerPlacement.of(2), 
/*  79 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*  81 */     PlacementUtils.register(paramBootstrapContext, BASALT_BLOBS, (Holder<ConfiguredFeature<?, ?>>)reference4, new PlacementModifier[] {
/*  82 */           (PlacementModifier)CountPlacement.of(75), 
/*  83 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/*  85 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*  87 */     PlacementUtils.register(paramBootstrapContext, BLACKSTONE_BLOBS, (Holder<ConfiguredFeature<?, ?>>)reference5, new PlacementModifier[] {
/*  88 */           (PlacementModifier)CountPlacement.of(25), 
/*  89 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/*  91 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/*  94 */     PlacementUtils.register(paramBootstrapContext, GLOWSTONE_EXTRA, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] {
/*  95 */           (PlacementModifier)CountPlacement.of((IntProvider)BiasedToBottomInt.of(0, 9)), 
/*  96 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_4_4, 
/*     */           
/*  98 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 100 */     PlacementUtils.register(paramBootstrapContext, GLOWSTONE, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] {
/* 101 */           (PlacementModifier)CountPlacement.of(10), 
/* 102 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 104 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 107 */     PlacementUtils.register(paramBootstrapContext, CRIMSON_FOREST_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference7, new PlacementModifier[] {
/* 108 */           (PlacementModifier)CountOnEveryLayerPlacement.of(6), 
/* 109 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 111 */     PlacementUtils.register(paramBootstrapContext, WARPED_FOREST_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference8, new PlacementModifier[] {
/* 112 */           (PlacementModifier)CountOnEveryLayerPlacement.of(5), 
/* 113 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 115 */     PlacementUtils.register(paramBootstrapContext, NETHER_SPROUTS, (Holder<ConfiguredFeature<?, ?>>)reference9, new PlacementModifier[] {
/* 116 */           (PlacementModifier)CountOnEveryLayerPlacement.of(4), 
/* 117 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 119 */     PlacementUtils.register(paramBootstrapContext, TWISTING_VINES, (Holder<ConfiguredFeature<?, ?>>)reference10, new PlacementModifier[] {
/* 120 */           (PlacementModifier)CountPlacement.of(10), 
/* 121 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 123 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 125 */     PlacementUtils.register(paramBootstrapContext, WEEPING_VINES, (Holder<ConfiguredFeature<?, ?>>)reference11, new PlacementModifier[] {
/* 126 */           (PlacementModifier)CountPlacement.of(10), 
/* 127 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 129 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 131 */     PlacementUtils.register(paramBootstrapContext, PATCH_CRIMSON_ROOTS, (Holder<ConfiguredFeature<?, ?>>)reference12, new PlacementModifier[] { PlacementUtils.FULL_RANGE, 
/*     */           
/* 133 */           (PlacementModifier)BiomeFilter.biome() });
/*     */ 
/*     */     
/* 136 */     PlacementUtils.register(paramBootstrapContext, BASALT_PILLAR, (Holder<ConfiguredFeature<?, ?>>)reference13, new PlacementModifier[] {
/* 137 */           (PlacementModifier)CountPlacement.of(10), 
/* 138 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 140 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */     
/* 143 */     PlacementUtils.register(paramBootstrapContext, SPRING_DELTA, (Holder<ConfiguredFeature<?, ?>>)reference14, new PlacementModifier[] {
/* 144 */           (PlacementModifier)CountPlacement.of(16), 
/* 145 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_4_4, 
/*     */           
/* 147 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 149 */     PlacementUtils.register(paramBootstrapContext, SPRING_CLOSED, (Holder<ConfiguredFeature<?, ?>>)reference15, new PlacementModifier[] {
/* 150 */           (PlacementModifier)CountPlacement.of(16), 
/* 151 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_10_10, 
/*     */           
/* 153 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 155 */     PlacementUtils.register(paramBootstrapContext, SPRING_CLOSED_DOUBLE, (Holder<ConfiguredFeature<?, ?>>)reference15, new PlacementModifier[] {
/* 156 */           (PlacementModifier)CountPlacement.of(32), 
/* 157 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_10_10, 
/*     */           
/* 159 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 161 */     PlacementUtils.register(paramBootstrapContext, SPRING_OPEN, (Holder<ConfiguredFeature<?, ?>>)reference16, new PlacementModifier[] {
/* 162 */           (PlacementModifier)CountPlacement.of(8), 
/* 163 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_4_4, 
/*     */           
/* 165 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/*     */ 
/*     */     
/* 169 */     List<?> list = List.of(
/* 170 */         CountPlacement.of((IntProvider)UniformInt.of(0, 5)), 
/* 171 */         InSquarePlacement.spread(), PlacementUtils.RANGE_4_4, 
/*     */         
/* 173 */         BiomeFilter.biome());
/*     */     
/* 175 */     PlacementUtils.register(paramBootstrapContext, PATCH_SOUL_FIRE, (Holder<ConfiguredFeature<?, ?>>)reference17, (List)list);
/*     */ 
/*     */     
/* 178 */     PlacementUtils.register(paramBootstrapContext, PATCH_FIRE, (Holder<ConfiguredFeature<?, ?>>)reference18, (List)list);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\placement\NetherPlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */