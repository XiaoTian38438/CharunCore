/*     */ package net.minecraft.data.worldgen.placement;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.features.OreFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.placement.RarityFilter;
/*     */ 
/*     */ public class OrePlacements {
/*     */   private static List<PlacementModifier> orePlacement(PlacementModifier paramPlacementModifier1, PlacementModifier paramPlacementModifier2) {
/*  24 */     return (List)List.of(paramPlacementModifier1, 
/*     */         
/*  26 */         InSquarePlacement.spread(), paramPlacementModifier2, 
/*     */         
/*  28 */         BiomeFilter.biome());
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<PlacementModifier> commonOrePlacement(int paramInt, PlacementModifier paramPlacementModifier) {
/*  33 */     return orePlacement((PlacementModifier)CountPlacement.of(paramInt), paramPlacementModifier);
/*     */   }
/*     */   
/*     */   private static List<PlacementModifier> rareOrePlacement(int paramInt, PlacementModifier paramPlacementModifier) {
/*  37 */     return orePlacement((PlacementModifier)RarityFilter.onAverageOnceEvery(paramInt), paramPlacementModifier);
/*     */   }
/*     */   
/*  40 */   public static final ResourceKey<PlacedFeature> ORE_MAGMA = PlacementUtils.createKey("ore_magma");
/*  41 */   public static final ResourceKey<PlacedFeature> ORE_SOUL_SAND = PlacementUtils.createKey("ore_soul_sand");
/*  42 */   public static final ResourceKey<PlacedFeature> ORE_GOLD_DELTAS = PlacementUtils.createKey("ore_gold_deltas");
/*  43 */   public static final ResourceKey<PlacedFeature> ORE_QUARTZ_DELTAS = PlacementUtils.createKey("ore_quartz_deltas");
/*  44 */   public static final ResourceKey<PlacedFeature> ORE_GOLD_NETHER = PlacementUtils.createKey("ore_gold_nether");
/*  45 */   public static final ResourceKey<PlacedFeature> ORE_QUARTZ_NETHER = PlacementUtils.createKey("ore_quartz_nether");
/*  46 */   public static final ResourceKey<PlacedFeature> ORE_GRAVEL_NETHER = PlacementUtils.createKey("ore_gravel_nether");
/*  47 */   public static final ResourceKey<PlacedFeature> ORE_BLACKSTONE = PlacementUtils.createKey("ore_blackstone");
/*  48 */   public static final ResourceKey<PlacedFeature> ORE_DIRT = PlacementUtils.createKey("ore_dirt");
/*  49 */   public static final ResourceKey<PlacedFeature> ORE_GRAVEL = PlacementUtils.createKey("ore_gravel");
/*  50 */   public static final ResourceKey<PlacedFeature> ORE_GRANITE_UPPER = PlacementUtils.createKey("ore_granite_upper");
/*  51 */   public static final ResourceKey<PlacedFeature> ORE_GRANITE_LOWER = PlacementUtils.createKey("ore_granite_lower");
/*  52 */   public static final ResourceKey<PlacedFeature> ORE_DIORITE_UPPER = PlacementUtils.createKey("ore_diorite_upper");
/*  53 */   public static final ResourceKey<PlacedFeature> ORE_DIORITE_LOWER = PlacementUtils.createKey("ore_diorite_lower");
/*  54 */   public static final ResourceKey<PlacedFeature> ORE_ANDESITE_UPPER = PlacementUtils.createKey("ore_andesite_upper");
/*  55 */   public static final ResourceKey<PlacedFeature> ORE_ANDESITE_LOWER = PlacementUtils.createKey("ore_andesite_lower");
/*  56 */   public static final ResourceKey<PlacedFeature> ORE_TUFF = PlacementUtils.createKey("ore_tuff");
/*  57 */   public static final ResourceKey<PlacedFeature> ORE_COAL_UPPER = PlacementUtils.createKey("ore_coal_upper");
/*  58 */   public static final ResourceKey<PlacedFeature> ORE_COAL_LOWER = PlacementUtils.createKey("ore_coal_lower");
/*  59 */   public static final ResourceKey<PlacedFeature> ORE_IRON_UPPER = PlacementUtils.createKey("ore_iron_upper");
/*  60 */   public static final ResourceKey<PlacedFeature> ORE_IRON_MIDDLE = PlacementUtils.createKey("ore_iron_middle");
/*  61 */   public static final ResourceKey<PlacedFeature> ORE_IRON_SMALL = PlacementUtils.createKey("ore_iron_small");
/*  62 */   public static final ResourceKey<PlacedFeature> ORE_GOLD_EXTRA = PlacementUtils.createKey("ore_gold_extra");
/*  63 */   public static final ResourceKey<PlacedFeature> ORE_GOLD = PlacementUtils.createKey("ore_gold");
/*  64 */   public static final ResourceKey<PlacedFeature> ORE_GOLD_LOWER = PlacementUtils.createKey("ore_gold_lower");
/*  65 */   public static final ResourceKey<PlacedFeature> ORE_REDSTONE = PlacementUtils.createKey("ore_redstone");
/*  66 */   public static final ResourceKey<PlacedFeature> ORE_REDSTONE_LOWER = PlacementUtils.createKey("ore_redstone_lower");
/*  67 */   public static final ResourceKey<PlacedFeature> ORE_DIAMOND = PlacementUtils.createKey("ore_diamond");
/*  68 */   public static final ResourceKey<PlacedFeature> ORE_DIAMOND_MEDIUM = PlacementUtils.createKey("ore_diamond_medium");
/*  69 */   public static final ResourceKey<PlacedFeature> ORE_DIAMOND_LARGE = PlacementUtils.createKey("ore_diamond_large");
/*  70 */   public static final ResourceKey<PlacedFeature> ORE_DIAMOND_BURIED = PlacementUtils.createKey("ore_diamond_buried");
/*  71 */   public static final ResourceKey<PlacedFeature> ORE_LAPIS = PlacementUtils.createKey("ore_lapis");
/*  72 */   public static final ResourceKey<PlacedFeature> ORE_LAPIS_BURIED = PlacementUtils.createKey("ore_lapis_buried");
/*  73 */   public static final ResourceKey<PlacedFeature> ORE_INFESTED = PlacementUtils.createKey("ore_infested");
/*  74 */   public static final ResourceKey<PlacedFeature> ORE_EMERALD = PlacementUtils.createKey("ore_emerald");
/*  75 */   public static final ResourceKey<PlacedFeature> ORE_ANCIENT_DEBRIS_LARGE = PlacementUtils.createKey("ore_ancient_debris_large");
/*  76 */   public static final ResourceKey<PlacedFeature> ORE_ANCIENT_DEBRIS_SMALL = PlacementUtils.createKey("ore_debris_small");
/*  77 */   public static final ResourceKey<PlacedFeature> ORE_COPPER = PlacementUtils.createKey("ore_copper");
/*  78 */   public static final ResourceKey<PlacedFeature> ORE_COPPER_LARGE = PlacementUtils.createKey("ore_copper_large");
/*  79 */   public static final ResourceKey<PlacedFeature> ORE_CLAY = PlacementUtils.createKey("ore_clay");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> paramBootstrapContext) {
/*  82 */     HolderGetter holderGetter = paramBootstrapContext.lookup(Registries.CONFIGURED_FEATURE);
/*  83 */     Holder.Reference reference1 = holderGetter.getOrThrow(OreFeatures.ORE_MAGMA);
/*  84 */     Holder.Reference reference2 = holderGetter.getOrThrow(OreFeatures.ORE_SOUL_SAND);
/*  85 */     Holder.Reference reference3 = holderGetter.getOrThrow(OreFeatures.ORE_NETHER_GOLD);
/*  86 */     Holder.Reference reference4 = holderGetter.getOrThrow(OreFeatures.ORE_QUARTZ);
/*  87 */     Holder.Reference reference5 = holderGetter.getOrThrow(OreFeatures.ORE_GRAVEL_NETHER);
/*  88 */     Holder.Reference reference6 = holderGetter.getOrThrow(OreFeatures.ORE_BLACKSTONE);
/*  89 */     Holder.Reference reference7 = holderGetter.getOrThrow(OreFeatures.ORE_DIRT);
/*  90 */     Holder.Reference reference8 = holderGetter.getOrThrow(OreFeatures.ORE_GRAVEL);
/*  91 */     Holder.Reference reference9 = holderGetter.getOrThrow(OreFeatures.ORE_GRANITE);
/*  92 */     Holder.Reference reference10 = holderGetter.getOrThrow(OreFeatures.ORE_DIORITE);
/*  93 */     Holder.Reference reference11 = holderGetter.getOrThrow(OreFeatures.ORE_ANDESITE);
/*  94 */     Holder.Reference reference12 = holderGetter.getOrThrow(OreFeatures.ORE_TUFF);
/*  95 */     Holder.Reference reference13 = holderGetter.getOrThrow(OreFeatures.ORE_COAL);
/*  96 */     Holder.Reference reference14 = holderGetter.getOrThrow(OreFeatures.ORE_COAL_BURIED);
/*  97 */     Holder.Reference reference15 = holderGetter.getOrThrow(OreFeatures.ORE_IRON);
/*  98 */     Holder.Reference reference16 = holderGetter.getOrThrow(OreFeatures.ORE_IRON_SMALL);
/*  99 */     Holder.Reference reference17 = holderGetter.getOrThrow(OreFeatures.ORE_GOLD);
/* 100 */     Holder.Reference reference18 = holderGetter.getOrThrow(OreFeatures.ORE_GOLD_BURIED);
/* 101 */     Holder.Reference reference19 = holderGetter.getOrThrow(OreFeatures.ORE_REDSTONE);
/* 102 */     Holder.Reference reference20 = holderGetter.getOrThrow(OreFeatures.ORE_DIAMOND_SMALL);
/* 103 */     Holder.Reference reference21 = holderGetter.getOrThrow(OreFeatures.ORE_DIAMOND_MEDIUM);
/* 104 */     Holder.Reference reference22 = holderGetter.getOrThrow(OreFeatures.ORE_DIAMOND_LARGE);
/* 105 */     Holder.Reference reference23 = holderGetter.getOrThrow(OreFeatures.ORE_DIAMOND_BURIED);
/* 106 */     Holder.Reference reference24 = holderGetter.getOrThrow(OreFeatures.ORE_LAPIS);
/* 107 */     Holder.Reference reference25 = holderGetter.getOrThrow(OreFeatures.ORE_LAPIS_BURIED);
/* 108 */     Holder.Reference reference26 = holderGetter.getOrThrow(OreFeatures.ORE_INFESTED);
/* 109 */     Holder.Reference reference27 = holderGetter.getOrThrow(OreFeatures.ORE_EMERALD);
/* 110 */     Holder.Reference reference28 = holderGetter.getOrThrow(OreFeatures.ORE_ANCIENT_DEBRIS_LARGE);
/* 111 */     Holder.Reference reference29 = holderGetter.getOrThrow(OreFeatures.ORE_ANCIENT_DEBRIS_SMALL);
/* 112 */     Holder.Reference reference30 = holderGetter.getOrThrow(OreFeatures.ORE_COPPPER_SMALL);
/* 113 */     Holder.Reference reference31 = holderGetter.getOrThrow(OreFeatures.ORE_COPPER_LARGE);
/* 114 */     Holder.Reference reference32 = holderGetter.getOrThrow(OreFeatures.ORE_CLAY);
/*     */     
/* 116 */     PlacementUtils.register(paramBootstrapContext, ORE_MAGMA, (Holder<ConfiguredFeature<?, ?>>)reference1, 
/* 117 */         commonOrePlacement(4, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(27), VerticalAnchor.absolute(36))));
/*     */     
/* 119 */     PlacementUtils.register(paramBootstrapContext, ORE_SOUL_SAND, (Holder<ConfiguredFeature<?, ?>>)reference2, 
/* 120 */         commonOrePlacement(12, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(31))));
/*     */     
/* 122 */     PlacementUtils.register(paramBootstrapContext, ORE_GOLD_DELTAS, (Holder<ConfiguredFeature<?, ?>>)reference3, 
/* 123 */         commonOrePlacement(20, PlacementUtils.RANGE_10_10));
/*     */     
/* 125 */     PlacementUtils.register(paramBootstrapContext, ORE_QUARTZ_DELTAS, (Holder<ConfiguredFeature<?, ?>>)reference4, 
/* 126 */         commonOrePlacement(32, PlacementUtils.RANGE_10_10));
/*     */     
/* 128 */     PlacementUtils.register(paramBootstrapContext, ORE_GOLD_NETHER, (Holder<ConfiguredFeature<?, ?>>)reference3, 
/* 129 */         commonOrePlacement(10, PlacementUtils.RANGE_10_10));
/*     */     
/* 131 */     PlacementUtils.register(paramBootstrapContext, ORE_QUARTZ_NETHER, (Holder<ConfiguredFeature<?, ?>>)reference4, 
/* 132 */         commonOrePlacement(16, PlacementUtils.RANGE_10_10));
/*     */     
/* 134 */     PlacementUtils.register(paramBootstrapContext, ORE_GRAVEL_NETHER, (Holder<ConfiguredFeature<?, ?>>)reference5, 
/* 135 */         commonOrePlacement(2, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(5), VerticalAnchor.absolute(41))));
/*     */     
/* 137 */     PlacementUtils.register(paramBootstrapContext, ORE_BLACKSTONE, (Holder<ConfiguredFeature<?, ?>>)reference6, 
/* 138 */         commonOrePlacement(2, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(5), VerticalAnchor.absolute(31))));
/*     */     
/* 140 */     PlacementUtils.register(paramBootstrapContext, ORE_DIRT, (Holder<ConfiguredFeature<?, ?>>)reference7, 
/* 141 */         commonOrePlacement(7, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(160))));
/*     */     
/* 143 */     PlacementUtils.register(paramBootstrapContext, ORE_GRAVEL, (Holder<ConfiguredFeature<?, ?>>)reference8, 
/* 144 */         commonOrePlacement(14, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top())));
/*     */     
/* 146 */     PlacementUtils.register(paramBootstrapContext, ORE_GRANITE_UPPER, (Holder<ConfiguredFeature<?, ?>>)reference9, 
/* 147 */         rareOrePlacement(6, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128))));
/*     */     
/* 149 */     PlacementUtils.register(paramBootstrapContext, ORE_GRANITE_LOWER, (Holder<ConfiguredFeature<?, ?>>)reference9, 
/* 150 */         commonOrePlacement(2, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60))));
/*     */     
/* 152 */     PlacementUtils.register(paramBootstrapContext, ORE_DIORITE_UPPER, (Holder<ConfiguredFeature<?, ?>>)reference10, 
/* 153 */         rareOrePlacement(6, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128))));
/*     */     
/* 155 */     PlacementUtils.register(paramBootstrapContext, ORE_DIORITE_LOWER, (Holder<ConfiguredFeature<?, ?>>)reference10, 
/* 156 */         commonOrePlacement(2, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60))));
/*     */     
/* 158 */     PlacementUtils.register(paramBootstrapContext, ORE_ANDESITE_UPPER, (Holder<ConfiguredFeature<?, ?>>)reference11, 
/* 159 */         rareOrePlacement(6, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128))));
/*     */     
/* 161 */     PlacementUtils.register(paramBootstrapContext, ORE_ANDESITE_LOWER, (Holder<ConfiguredFeature<?, ?>>)reference11, 
/* 162 */         commonOrePlacement(2, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60))));
/*     */     
/* 164 */     PlacementUtils.register(paramBootstrapContext, ORE_TUFF, (Holder<ConfiguredFeature<?, ?>>)reference12, 
/* 165 */         commonOrePlacement(2, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(0))));
/*     */     
/* 167 */     PlacementUtils.register(paramBootstrapContext, ORE_COAL_UPPER, (Holder<ConfiguredFeature<?, ?>>)reference13, 
/* 168 */         commonOrePlacement(30, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(136), VerticalAnchor.top())));
/*     */     
/* 170 */     PlacementUtils.register(paramBootstrapContext, ORE_COAL_LOWER, (Holder<ConfiguredFeature<?, ?>>)reference14, 
/* 171 */         commonOrePlacement(20, (PlacementModifier)HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(192))));
/*     */     
/* 173 */     PlacementUtils.register(paramBootstrapContext, ORE_IRON_UPPER, (Holder<ConfiguredFeature<?, ?>>)reference15, 
/* 174 */         commonOrePlacement(90, (PlacementModifier)HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384))));
/*     */     
/* 176 */     PlacementUtils.register(paramBootstrapContext, ORE_IRON_MIDDLE, (Holder<ConfiguredFeature<?, ?>>)reference15, 
/* 177 */         commonOrePlacement(10, (PlacementModifier)HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56))));
/*     */     
/* 179 */     PlacementUtils.register(paramBootstrapContext, ORE_IRON_SMALL, (Holder<ConfiguredFeature<?, ?>>)reference16, 
/* 180 */         commonOrePlacement(10, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72))));
/*     */     
/* 182 */     PlacementUtils.register(paramBootstrapContext, ORE_GOLD_EXTRA, (Holder<ConfiguredFeature<?, ?>>)reference17, 
/* 183 */         commonOrePlacement(50, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256))));
/*     */     
/* 185 */     PlacementUtils.register(paramBootstrapContext, ORE_GOLD, (Holder<ConfiguredFeature<?, ?>>)reference18, 
/* 186 */         commonOrePlacement(4, (PlacementModifier)HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));
/*     */     
/* 188 */     PlacementUtils.register(paramBootstrapContext, ORE_GOLD_LOWER, (Holder<ConfiguredFeature<?, ?>>)reference18, 
/* 189 */         orePlacement(
/* 190 */           (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(0, 1)), 
/* 191 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-48))));
/*     */ 
/*     */     
/* 194 */     PlacementUtils.register(paramBootstrapContext, ORE_REDSTONE, (Holder<ConfiguredFeature<?, ?>>)reference19, 
/* 195 */         commonOrePlacement(4, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(15))));
/*     */     
/* 197 */     PlacementUtils.register(paramBootstrapContext, ORE_REDSTONE_LOWER, (Holder<ConfiguredFeature<?, ?>>)reference19, 
/* 198 */         commonOrePlacement(8, (PlacementModifier)HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-32), VerticalAnchor.aboveBottom(32))));
/*     */     
/* 200 */     PlacementUtils.register(paramBootstrapContext, ORE_DIAMOND, (Holder<ConfiguredFeature<?, ?>>)reference20, 
/* 201 */         commonOrePlacement(7, (PlacementModifier)HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
/*     */     
/* 203 */     PlacementUtils.register(paramBootstrapContext, ORE_DIAMOND_MEDIUM, (Holder<ConfiguredFeature<?, ?>>)reference21, 
/* 204 */         commonOrePlacement(2, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-4))));
/*     */     
/* 206 */     PlacementUtils.register(paramBootstrapContext, ORE_DIAMOND_LARGE, (Holder<ConfiguredFeature<?, ?>>)reference22, 
/* 207 */         rareOrePlacement(9, (PlacementModifier)HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
/*     */     
/* 209 */     PlacementUtils.register(paramBootstrapContext, ORE_DIAMOND_BURIED, (Holder<ConfiguredFeature<?, ?>>)reference23, 
/* 210 */         commonOrePlacement(4, (PlacementModifier)HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
/*     */     
/* 212 */     PlacementUtils.register(paramBootstrapContext, ORE_LAPIS, (Holder<ConfiguredFeature<?, ?>>)reference24, 
/* 213 */         commonOrePlacement(2, (PlacementModifier)HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(32))));
/*     */     
/* 215 */     PlacementUtils.register(paramBootstrapContext, ORE_LAPIS_BURIED, (Holder<ConfiguredFeature<?, ?>>)reference25, 
/* 216 */         commonOrePlacement(4, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64))));
/*     */     
/* 218 */     PlacementUtils.register(paramBootstrapContext, ORE_INFESTED, (Holder<ConfiguredFeature<?, ?>>)reference26, 
/* 219 */         commonOrePlacement(14, (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(63))));
/*     */     
/* 221 */     PlacementUtils.register(paramBootstrapContext, ORE_EMERALD, (Holder<ConfiguredFeature<?, ?>>)reference27, 
/* 222 */         commonOrePlacement(100, (PlacementModifier)HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(480))));
/*     */     
/* 224 */     PlacementUtils.register(paramBootstrapContext, ORE_ANCIENT_DEBRIS_LARGE, (Holder<ConfiguredFeature<?, ?>>)reference28, new PlacementModifier[] {
/* 225 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 226 */           (PlacementModifier)HeightRangePlacement.triangle(VerticalAnchor.absolute(8), VerticalAnchor.absolute(24)), 
/* 227 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 229 */     PlacementUtils.register(paramBootstrapContext, ORE_ANCIENT_DEBRIS_SMALL, (Holder<ConfiguredFeature<?, ?>>)reference29, new PlacementModifier[] {
/* 230 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_8_8, 
/*     */           
/* 232 */           (PlacementModifier)BiomeFilter.biome()
/*     */         });
/* 234 */     PlacementUtils.register(paramBootstrapContext, ORE_COPPER, (Holder<ConfiguredFeature<?, ?>>)reference30, 
/* 235 */         commonOrePlacement(16, (PlacementModifier)HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112))));
/*     */     
/* 237 */     PlacementUtils.register(paramBootstrapContext, ORE_COPPER_LARGE, (Holder<ConfiguredFeature<?, ?>>)reference31, 
/* 238 */         commonOrePlacement(16, (PlacementModifier)HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112))));
/*     */     
/* 240 */     PlacementUtils.register(paramBootstrapContext, ORE_CLAY, (Holder<ConfiguredFeature<?, ?>>)reference32, 
/* 241 */         commonOrePlacement(46, PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\placement\OrePlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */