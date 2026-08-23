/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.Noises;
/*     */ import net.minecraft.world.level.levelgen.SurfaceRules;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SurfaceRuleData
/*     */ {
/*  21 */   private static final SurfaceRules.RuleSource AIR = makeStateRule(Blocks.AIR);
/*     */   
/*  23 */   private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
/*  24 */   private static final SurfaceRules.RuleSource WHITE_TERRACOTTA = makeStateRule(Blocks.WHITE_TERRACOTTA);
/*  25 */   private static final SurfaceRules.RuleSource ORANGE_TERRACOTTA = makeStateRule(Blocks.ORANGE_TERRACOTTA);
/*  26 */   private static final SurfaceRules.RuleSource TERRACOTTA = makeStateRule(Blocks.TERRACOTTA);
/*  27 */   private static final SurfaceRules.RuleSource RED_SAND = makeStateRule(Blocks.RED_SAND);
/*  28 */   private static final SurfaceRules.RuleSource RED_SANDSTONE = makeStateRule(Blocks.RED_SANDSTONE);
/*  29 */   private static final SurfaceRules.RuleSource STONE = makeStateRule(Blocks.STONE);
/*  30 */   private static final SurfaceRules.RuleSource DEEPSLATE = makeStateRule(Blocks.DEEPSLATE);
/*  31 */   private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
/*  32 */   private static final SurfaceRules.RuleSource PODZOL = makeStateRule(Blocks.PODZOL);
/*  33 */   private static final SurfaceRules.RuleSource COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
/*  34 */   private static final SurfaceRules.RuleSource MYCELIUM = makeStateRule(Blocks.MYCELIUM);
/*  35 */   private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
/*  36 */   private static final SurfaceRules.RuleSource CALCITE = makeStateRule(Blocks.CALCITE);
/*  37 */   private static final SurfaceRules.RuleSource GRAVEL = makeStateRule(Blocks.GRAVEL);
/*  38 */   private static final SurfaceRules.RuleSource SAND = makeStateRule(Blocks.SAND);
/*  39 */   private static final SurfaceRules.RuleSource SANDSTONE = makeStateRule(Blocks.SANDSTONE);
/*  40 */   private static final SurfaceRules.RuleSource PACKED_ICE = makeStateRule(Blocks.PACKED_ICE);
/*  41 */   private static final SurfaceRules.RuleSource SNOW_BLOCK = makeStateRule(Blocks.SNOW_BLOCK);
/*  42 */   private static final SurfaceRules.RuleSource MUD = makeStateRule(Blocks.MUD);
/*  43 */   private static final SurfaceRules.RuleSource POWDER_SNOW = makeStateRule(Blocks.POWDER_SNOW);
/*  44 */   private static final SurfaceRules.RuleSource ICE = makeStateRule(Blocks.ICE);
/*  45 */   private static final SurfaceRules.RuleSource WATER = makeStateRule(Blocks.WATER);
/*     */   
/*  47 */   private static final SurfaceRules.RuleSource LAVA = makeStateRule(Blocks.LAVA);
/*  48 */   private static final SurfaceRules.RuleSource NETHERRACK = makeStateRule(Blocks.NETHERRACK);
/*  49 */   private static final SurfaceRules.RuleSource SOUL_SAND = makeStateRule(Blocks.SOUL_SAND);
/*  50 */   private static final SurfaceRules.RuleSource SOUL_SOIL = makeStateRule(Blocks.SOUL_SOIL);
/*  51 */   private static final SurfaceRules.RuleSource BASALT = makeStateRule(Blocks.BASALT);
/*  52 */   private static final SurfaceRules.RuleSource BLACKSTONE = makeStateRule(Blocks.BLACKSTONE);
/*  53 */   private static final SurfaceRules.RuleSource WARPED_WART_BLOCK = makeStateRule(Blocks.WARPED_WART_BLOCK);
/*  54 */   private static final SurfaceRules.RuleSource WARPED_NYLIUM = makeStateRule(Blocks.WARPED_NYLIUM);
/*  55 */   private static final SurfaceRules.RuleSource NETHER_WART_BLOCK = makeStateRule(Blocks.NETHER_WART_BLOCK);
/*  56 */   private static final SurfaceRules.RuleSource CRIMSON_NYLIUM = makeStateRule(Blocks.CRIMSON_NYLIUM);
/*     */   
/*  58 */   private static final SurfaceRules.RuleSource ENDSTONE = makeStateRule(Blocks.END_STONE);
/*     */   
/*     */   private static SurfaceRules.RuleSource makeStateRule(Block paramBlock) {
/*  61 */     return SurfaceRules.state(paramBlock.defaultBlockState());
/*     */   }
/*     */   
/*     */   public static SurfaceRules.RuleSource overworld() {
/*  65 */     return overworldLike(true, false, true);
/*     */   }
/*     */   
/*     */   public static SurfaceRules.RuleSource overworldLike(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/*  69 */     SurfaceRules.ConditionSource conditionSource1 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(97), 2);
/*  70 */     SurfaceRules.ConditionSource conditionSource2 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(256), 0);
/*  71 */     SurfaceRules.ConditionSource conditionSource3 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(63), -1);
/*  72 */     SurfaceRules.ConditionSource conditionSource4 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(74), 1);
/*     */     
/*  74 */     SurfaceRules.ConditionSource conditionSource5 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(60), 0);
/*  75 */     SurfaceRules.ConditionSource conditionSource6 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
/*  76 */     SurfaceRules.ConditionSource conditionSource7 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);
/*     */ 
/*     */ 
/*     */     
/*  80 */     SurfaceRules.ConditionSource conditionSource8 = SurfaceRules.waterBlockCheck(-1, 0);
/*  81 */     SurfaceRules.ConditionSource conditionSource9 = SurfaceRules.waterBlockCheck(0, 0);
/*     */     
/*  83 */     SurfaceRules.ConditionSource conditionSource10 = SurfaceRules.waterStartCheck(-6, -1);
/*     */     
/*  85 */     SurfaceRules.ConditionSource conditionSource11 = SurfaceRules.hole();
/*  86 */     SurfaceRules.ConditionSource conditionSource12 = SurfaceRules.isBiome(new ResourceKey[] { Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN });
/*  87 */     SurfaceRules.ConditionSource conditionSource13 = SurfaceRules.steep();
/*     */     
/*  89 */     SurfaceRules.RuleSource ruleSource1 = SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/*  90 */           SurfaceRules.ifTrue(conditionSource9, GRASS_BLOCK), DIRT
/*     */         });
/*     */ 
/*     */     
/*  94 */     SurfaceRules.RuleSource ruleSource2 = SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/*  95 */           SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SANDSTONE), SAND
/*     */         });
/*     */ 
/*     */     
/*  99 */     SurfaceRules.RuleSource ruleSource3 = SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 100 */           SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, STONE), GRAVEL
/*     */         });
/*     */ 
/*     */     
/* 104 */     SurfaceRules.ConditionSource conditionSource14 = SurfaceRules.isBiome(new ResourceKey[] { Biomes.WARM_OCEAN, Biomes.BEACH, Biomes.SNOWY_BEACH });
/* 105 */     SurfaceRules.ConditionSource conditionSource15 = SurfaceRules.isBiome(new ResourceKey[] { Biomes.DESERT });
/*     */     
/* 107 */     SurfaceRules.RuleSource ruleSource4 = SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 108 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.STONY_PEAKS }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 109 */                 SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.CALCITE, -0.0125D, 0.0125D), CALCITE), STONE
/*     */ 
/*     */               
/* 112 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.STONY_SHORE }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 113 */                 SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.GRAVEL, -0.05D, 0.05D), ruleSource3), STONE
/*     */ 
/*     */               
/* 116 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.WINDSWEPT_HILLS }, ), SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE)), 
/* 117 */           SurfaceRules.ifTrue(conditionSource14, ruleSource2), 
/* 118 */           SurfaceRules.ifTrue(conditionSource15, ruleSource2), 
/* 119 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.DRIPSTONE_CAVES }, ), STONE)
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 124 */     SurfaceRules.RuleSource ruleSource5 = SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.45D, 0.58D), SurfaceRules.ifTrue(conditionSource9, POWDER_SNOW));
/* 125 */     SurfaceRules.RuleSource ruleSource6 = SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.35D, 0.6D), SurfaceRules.ifTrue(conditionSource9, POWDER_SNOW));
/*     */     
/* 127 */     SurfaceRules.RuleSource ruleSource7 = SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 128 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.FROZEN_PEAKS }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 129 */                 SurfaceRules.ifTrue(conditionSource13, PACKED_ICE), 
/* 130 */                 SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PACKED_ICE, -0.5D, 0.2D), PACKED_ICE), 
/* 131 */                 SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, -0.0625D, 0.025D), ICE), 
/* 132 */                 SurfaceRules.ifTrue(conditionSource9, SNOW_BLOCK)
/*     */               
/* 134 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.SNOWY_SLOPES }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 135 */                 SurfaceRules.ifTrue(conditionSource13, STONE), ruleSource5, 
/*     */                 
/* 137 */                 SurfaceRules.ifTrue(conditionSource9, SNOW_BLOCK)
/*     */               
/* 139 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.JAGGED_PEAKS }, ), STONE), 
/* 140 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.GROVE }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { ruleSource5, DIRT })), ruleSource4, 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 145 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.WINDSWEPT_SAVANNA }, ), SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE)), 
/* 146 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.WINDSWEPT_GRAVELLY_HILLS }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 147 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), ruleSource3), 
/* 148 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE), 
/* 149 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), DIRT), ruleSource3
/*     */ 
/*     */               
/* 152 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.MANGROVE_SWAMP }, ), MUD), DIRT
/*     */         });
/*     */ 
/*     */     
/* 156 */     SurfaceRules.RuleSource ruleSource8 = SurfaceRules.sequence(new SurfaceRules.RuleSource[] { 
/* 157 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.FROZEN_PEAKS }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 158 */                 SurfaceRules.ifTrue(conditionSource13, PACKED_ICE), 
/* 159 */                 SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PACKED_ICE, 0.0D, 0.2D), PACKED_ICE), 
/* 160 */                 SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, 0.0D, 0.025D), ICE), 
/* 161 */                 SurfaceRules.ifTrue(conditionSource9, SNOW_BLOCK)
/*     */               
/* 163 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.SNOWY_SLOPES }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 164 */                 SurfaceRules.ifTrue(conditionSource13, STONE), ruleSource6, 
/*     */                 
/* 166 */                 SurfaceRules.ifTrue(conditionSource9, SNOW_BLOCK)
/*     */               
/* 168 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.JAGGED_PEAKS }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 169 */                 SurfaceRules.ifTrue(conditionSource13, STONE), 
/* 170 */                 SurfaceRules.ifTrue(conditionSource9, SNOW_BLOCK)
/*     */               
/* 172 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.GROVE }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { ruleSource6, 
/*     */                 
/* 174 */                 SurfaceRules.ifTrue(conditionSource9, SNOW_BLOCK)
/*     */ 
/*     */               
/* 177 */               })), ruleSource4, SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.WINDSWEPT_SAVANNA }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 178 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE), 
/* 179 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(-0.5D), COARSE_DIRT)
/*     */               
/* 181 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.WINDSWEPT_GRAVELLY_HILLS }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 182 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), ruleSource3), 
/* 183 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE), 
/* 184 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), ruleSource1), ruleSource3
/*     */ 
/*     */               
/* 187 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 188 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), COARSE_DIRT), 
/* 189 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(-0.95D), PODZOL)
/*     */               
/* 191 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.ICE_SPIKES }, ), SurfaceRules.ifTrue(conditionSource9, SNOW_BLOCK)), 
/* 192 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.MANGROVE_SWAMP }, ), MUD), 
/* 193 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.MUSHROOM_FIELDS }, ), MYCELIUM), ruleSource1 });
/*     */ 
/*     */ 
/*     */     
/* 197 */     SurfaceRules.ConditionSource conditionSource16 = SurfaceRules.noiseCondition(Noises.SURFACE, -0.909D, -0.5454D);
/* 198 */     SurfaceRules.ConditionSource conditionSource17 = SurfaceRules.noiseCondition(Noises.SURFACE, -0.1818D, 0.1818D);
/* 199 */     SurfaceRules.ConditionSource conditionSource18 = SurfaceRules.noiseCondition(Noises.SURFACE, 0.5454D, 0.909D);
/*     */     
/* 201 */     SurfaceRules.RuleSource ruleSource9 = SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 202 */           SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 203 */                 SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.WOODED_BADLANDS }, ), SurfaceRules.ifTrue(conditionSource1, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 204 */                         SurfaceRules.ifTrue(conditionSource16, COARSE_DIRT), 
/* 205 */                         SurfaceRules.ifTrue(conditionSource17, COARSE_DIRT), 
/* 206 */                         SurfaceRules.ifTrue(conditionSource18, COARSE_DIRT), ruleSource1
/*     */ 
/*     */                       
/* 209 */                       }))), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.SWAMP }, ), SurfaceRules.ifTrue(conditionSource6, SurfaceRules.ifTrue(SurfaceRules.not(conditionSource7), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, 0.0D), WATER)))), 
/* 210 */                 SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.MANGROVE_SWAMP }, ), SurfaceRules.ifTrue(conditionSource5, SurfaceRules.ifTrue(SurfaceRules.not(conditionSource7), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, 0.0D), WATER))))
/*     */               
/* 212 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 213 */                 SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 214 */                       SurfaceRules.ifTrue(conditionSource2, ORANGE_TERRACOTTA), 
/* 215 */                       SurfaceRules.ifTrue(conditionSource4, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 216 */                             SurfaceRules.ifTrue(conditionSource16, TERRACOTTA), 
/* 217 */                             SurfaceRules.ifTrue(conditionSource17, TERRACOTTA), 
/* 218 */                             SurfaceRules.ifTrue(conditionSource18, TERRACOTTA), 
/* 219 */                             SurfaceRules.bandlands()
/*     */                           
/* 221 */                           })), SurfaceRules.ifTrue(conditionSource8, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 222 */                             SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, RED_SANDSTONE), RED_SAND
/*     */ 
/*     */                           
/* 225 */                           })), SurfaceRules.ifTrue(SurfaceRules.not(conditionSource11), ORANGE_TERRACOTTA), 
/* 226 */                       SurfaceRules.ifTrue(conditionSource10, WHITE_TERRACOTTA), ruleSource3
/*     */ 
/*     */                     
/* 229 */                     })), SurfaceRules.ifTrue(conditionSource3, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 230 */                       SurfaceRules.ifTrue(conditionSource7, SurfaceRules.ifTrue(SurfaceRules.not(conditionSource4), ORANGE_TERRACOTTA)), 
/* 231 */                       SurfaceRules.bandlands()
/*     */                     
/* 233 */                     })), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(conditionSource10, WHITE_TERRACOTTA))
/*     */               
/* 235 */               })), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(conditionSource8, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 236 */                   SurfaceRules.ifTrue(conditionSource12, SurfaceRules.ifTrue(conditionSource11, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 237 */                           SurfaceRules.ifTrue(conditionSource9, AIR), 
/* 238 */                           SurfaceRules.ifTrue(SurfaceRules.temperature(), ICE), WATER
/*     */ 
/*     */                         
/*     */                         }))), ruleSource8
/*     */                 
/* 243 */                 }))), SurfaceRules.ifTrue(conditionSource10, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 244 */                 SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(conditionSource12, SurfaceRules.ifTrue(conditionSource11, WATER))), 
/* 245 */                 SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, ruleSource7), 
/* 246 */                 SurfaceRules.ifTrue(conditionSource14, SurfaceRules.ifTrue(SurfaceRules.DEEP_UNDER_FLOOR, SANDSTONE)), 
/* 247 */                 SurfaceRules.ifTrue(conditionSource15, SurfaceRules.ifTrue(SurfaceRules.VERY_DEEP_UNDER_FLOOR, SANDSTONE))
/*     */               
/* 249 */               })), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 250 */                 SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS }, ), STONE), 
/* 251 */                 SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN }, ), ruleSource2), ruleSource3
/*     */               }))
/*     */         });
/*     */ 
/*     */     
/* 256 */     ImmutableList.Builder builder = ImmutableList.builder();
/*     */     
/* 258 */     if (paramBoolean2) {
/* 259 */       builder.add(SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK));
/*     */     }
/* 261 */     if (paramBoolean3) {
/* 262 */       builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK));
/*     */     }
/* 264 */     SurfaceRules.RuleSource ruleSource10 = SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), ruleSource9);
/* 265 */     builder.add(paramBoolean1 ? ruleSource10 : ruleSource9);
/* 266 */     builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)), DEEPSLATE));
/*     */     
/* 268 */     return SurfaceRules.sequence((SurfaceRules.RuleSource[])builder.build().toArray(paramInt -> new SurfaceRules.RuleSource[paramInt]));
/*     */   }
/*     */   
/*     */   public static SurfaceRules.RuleSource nether() {
/* 272 */     SurfaceRules.ConditionSource conditionSource1 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(31), 0);
/* 273 */     SurfaceRules.ConditionSource conditionSource2 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(32), 0);
/*     */     
/* 275 */     SurfaceRules.ConditionSource conditionSource3 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(30), 0);
/* 276 */     SurfaceRules.ConditionSource conditionSource4 = SurfaceRules.not(SurfaceRules.yStartCheck(VerticalAnchor.absolute(35), 0));
/*     */     
/* 278 */     SurfaceRules.ConditionSource conditionSource5 = SurfaceRules.yBlockCheck(VerticalAnchor.belowTop(5), 0);
/*     */     
/* 280 */     SurfaceRules.ConditionSource conditionSource6 = SurfaceRules.hole();
/*     */     
/* 282 */     SurfaceRules.ConditionSource conditionSource7 = SurfaceRules.noiseCondition(Noises.SOUL_SAND_LAYER, -0.012D);
/* 283 */     SurfaceRules.ConditionSource conditionSource8 = SurfaceRules.noiseCondition(Noises.GRAVEL_LAYER, -0.012D);
/* 284 */     SurfaceRules.ConditionSource conditionSource9 = SurfaceRules.noiseCondition(Noises.PATCH, -0.012D);
/* 285 */     SurfaceRules.ConditionSource conditionSource10 = SurfaceRules.noiseCondition(Noises.NETHERRACK, 0.54D);
/* 286 */     SurfaceRules.ConditionSource conditionSource11 = SurfaceRules.noiseCondition(Noises.NETHER_WART, 1.17D);
/* 287 */     SurfaceRules.ConditionSource conditionSource12 = SurfaceRules.noiseCondition(Noises.NETHER_STATE_SELECTOR, 0.0D);
/*     */     
/* 289 */     SurfaceRules.RuleSource ruleSource = SurfaceRules.ifTrue(conditionSource9, SurfaceRules.ifTrue(conditionSource3, SurfaceRules.ifTrue(conditionSource4, GRAVEL)));
/*     */     
/* 291 */     return SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 292 */           SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK), 
/* 293 */           SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK), 
/* 294 */           SurfaceRules.ifTrue(conditionSource5, NETHERRACK), 
/* 295 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.BASALT_DELTAS }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 296 */                 SurfaceRules.ifTrue(SurfaceRules.UNDER_CEILING, BASALT), 
/* 297 */                 SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/*     */                       
/* 299 */                       ruleSource, SurfaceRules.ifTrue(conditionSource12, BASALT), BLACKSTONE
/*     */                     
/*     */                     }))
/*     */               
/* 303 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.SOUL_SAND_VALLEY }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 304 */                 SurfaceRules.ifTrue(SurfaceRules.UNDER_CEILING, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 305 */                       SurfaceRules.ifTrue(conditionSource12, SOUL_SAND), SOUL_SOIL
/*     */ 
/*     */                     
/* 308 */                     })), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/*     */                       
/* 310 */                       ruleSource, SurfaceRules.ifTrue(conditionSource12, SOUL_SAND), SOUL_SOIL
/*     */                     
/*     */                     }))
/*     */               
/* 314 */               })), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 315 */                 SurfaceRules.ifTrue(SurfaceRules.not(conditionSource2), SurfaceRules.ifTrue(conditionSource6, LAVA)), 
/* 316 */                 SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.WARPED_FOREST }, ), SurfaceRules.ifTrue(SurfaceRules.not(conditionSource10), SurfaceRules.ifTrue(conditionSource1, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 317 */                           SurfaceRules.ifTrue(conditionSource11, WARPED_WART_BLOCK), WARPED_NYLIUM
/*     */ 
/*     */                         
/* 320 */                         })))), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.CRIMSON_FOREST }, ), SurfaceRules.ifTrue(SurfaceRules.not(conditionSource10), SurfaceRules.ifTrue(conditionSource1, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 321 */                           SurfaceRules.ifTrue(conditionSource11, NETHER_WART_BLOCK), CRIMSON_NYLIUM
/*     */                         
/*     */                         }))))
/*     */               
/* 325 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.NETHER_WASTES }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 326 */                 SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(conditionSource7, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 327 */                         SurfaceRules.ifTrue(SurfaceRules.not(conditionSource6), SurfaceRules.ifTrue(conditionSource3, SurfaceRules.ifTrue(conditionSource4, SOUL_SAND))), NETHERRACK
/*     */ 
/*     */                       
/* 330 */                       }))), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(conditionSource1, SurfaceRules.ifTrue(conditionSource4, SurfaceRules.ifTrue(conditionSource8, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 331 */                             SurfaceRules.ifTrue(conditionSource2, GRAVEL), 
/* 332 */                             SurfaceRules.ifTrue(SurfaceRules.not(conditionSource6), GRAVEL)
/*     */                           })))))
/*     */               })), NETHERRACK
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public static SurfaceRules.RuleSource end() {
/* 340 */     return ENDSTONE;
/*     */   }
/*     */   
/*     */   public static SurfaceRules.RuleSource air() {
/* 344 */     return AIR;
/*     */   }
/*     */   
/*     */   private static SurfaceRules.ConditionSource surfaceNoiseAbove(double paramDouble) {
/* 348 */     return SurfaceRules.noiseCondition(Noises.SURFACE, paramDouble / 8.25D, Double.MAX_VALUE);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\SurfaceRuleData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */