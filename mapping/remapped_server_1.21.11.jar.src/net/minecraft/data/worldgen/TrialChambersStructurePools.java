/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBindings;
/*     */ 
/*     */ 
/*     */ public class TrialChambersStructurePools
/*     */ {
/*  19 */   public static final ResourceKey<StructureTemplatePool> START = Pools.createKey("trial_chambers/chamber/end");
/*  20 */   public static final ResourceKey<StructureTemplatePool> HALLWAY_FALLBACK = Pools.createKey("trial_chambers/hallway/fallback");
/*  21 */   public static final List<PoolAliasBinding> ALIAS_BINDINGS = (List<PoolAliasBinding>)ImmutableList.builder()
/*  22 */     .add(PoolAliasBinding.randomGroup(WeightedList.builder()
/*  23 */         .add(List.of(
/*  24 */             PoolAliasBinding.direct(spawner("contents/ranged"), spawner("ranged/skeleton")), 
/*  25 */             PoolAliasBinding.direct(spawner("contents/slow_ranged"), spawner("slow_ranged/skeleton"))))
/*     */         
/*  27 */         .add(List.of(
/*  28 */             PoolAliasBinding.direct(spawner("contents/ranged"), spawner("ranged/stray")), 
/*  29 */             PoolAliasBinding.direct(spawner("contents/slow_ranged"), spawner("slow_ranged/stray"))))
/*     */         
/*  31 */         .add(List.of(
/*  32 */             PoolAliasBinding.direct(spawner("contents/ranged"), spawner("ranged/poison_skeleton")), 
/*  33 */             PoolAliasBinding.direct(spawner("contents/slow_ranged"), spawner("slow_ranged/poison_skeleton"))))
/*     */         
/*  35 */         .build()))
/*     */     
/*  37 */     .add(PoolAliasBinding.random(spawner("contents/melee"), WeightedList.builder()
/*  38 */         .add(spawner("melee/zombie"))
/*  39 */         .add(spawner("melee/husk"))
/*  40 */         .add(spawner("melee/spider"))
/*  41 */         .build()))
/*     */     
/*  43 */     .add(PoolAliasBinding.random(spawner("contents/small_melee"), WeightedList.builder()
/*  44 */         .add(spawner("small_melee/slime"))
/*  45 */         .add(spawner("small_melee/cave_spider"))
/*  46 */         .add(spawner("small_melee/silverfish"))
/*  47 */         .add(spawner("small_melee/baby_zombie"))
/*  48 */         .build()))
/*     */     
/*  50 */     .build();
/*     */   
/*     */   public static String spawner(String paramString) {
/*  53 */     return "trial_chambers/spawner/" + paramString;
/*     */   }
/*     */   
/*     */   public static void bootstrap(BootstrapContext<StructureTemplatePool> paramBootstrapContext) {
/*  57 */     HolderGetter<?> holderGetter1 = paramBootstrapContext.lookup(Registries.TEMPLATE_POOL);
/*  58 */     Holder.Reference reference1 = holderGetter1.getOrThrow(Pools.EMPTY);
/*  59 */     Holder.Reference reference2 = holderGetter1.getOrThrow(HALLWAY_FALLBACK);
/*     */     
/*  61 */     HolderGetter<?> holderGetter2 = paramBootstrapContext.lookup(Registries.PROCESSOR_LIST);
/*  62 */     Holder.Reference reference3 = holderGetter2.getOrThrow(ProcessorLists.TRIAL_CHAMBERS_COPPER_BULB_DEGRADATION);
/*     */     
/*  64 */     paramBootstrapContext.register(START, new StructureTemplatePool((Holder)reference1, 
/*     */           
/*  66 */           List.of(
/*  67 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/end_1", (Holder)reference3), Integer.valueOf(1)), 
/*  68 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/end_2", (Holder)reference3), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     Pools.register(paramBootstrapContext, "trial_chambers/chamber/entrance_cap", new StructureTemplatePool((Holder)reference1, 
/*     */           
/*  76 */           List.of(
/*  77 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/entrance_cap", (Holder)reference3), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  82 */     Pools.register(paramBootstrapContext, "trial_chambers/chambers/end", new StructureTemplatePool((Holder)reference2, 
/*     */           
/*  84 */           List.of(
/*  85 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/chamber_1", (Holder)reference3), Integer.valueOf(1)), 
/*  86 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly", (Holder)reference3), Integer.valueOf(1)), 
/*  87 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/eruption", (Holder)reference3), Integer.valueOf(1)), 
/*  88 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted", (Holder)reference3), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     Pools.register(paramBootstrapContext, "trial_chambers/corridor", new StructureTemplatePool((Holder)reference1, 
/*     */           
/*  95 */           List.of(
/*     */             
/*  97 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/second_plate"), Integer.valueOf(1)), 
/*  98 */             Pair.of(StructurePoolElement.single("trial_chambers/intersection/intersection_1", (Holder)reference3), Integer.valueOf(1)), 
/*  99 */             Pair.of(StructurePoolElement.single("trial_chambers/intersection/intersection_2", (Holder)reference3), Integer.valueOf(1)), 
/* 100 */             Pair.of(StructurePoolElement.single("trial_chambers/intersection/intersection_3", (Holder)reference3), Integer.valueOf(1)), 
/* 101 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/first_plate"), Integer.valueOf(1)), 
/* 102 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/atrium_1", (Holder)reference3), Integer.valueOf(1)), 
/* 103 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/entrance_1", (Holder)reference3), Integer.valueOf(1)), 
/* 104 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/entrance_2", (Holder)reference3), Integer.valueOf(1)), 
/* 105 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/entrance_3", (Holder)reference3), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     Pools.register(paramBootstrapContext, "trial_chambers/chamber/addon", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 112 */           List.of(
/* 113 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/addon/full_stacked_walkway"), Integer.valueOf(1)), 
/* 114 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/addon/full_stacked_walkway_2"), Integer.valueOf(1)), 
/* 115 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/addon/full_corner_column"), Integer.valueOf(1)), 
/* 116 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/addon/grate_bridge"), Integer.valueOf(1)), 
/* 117 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/addon/hanging_platform"), Integer.valueOf(1)), 
/* 118 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/addon/short_grate_platform"), Integer.valueOf(1)), 
/* 119 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/addon/short_platform"), Integer.valueOf(1)), 
/* 120 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/addon/lower_staircase_down"), Integer.valueOf(1)), 
/* 121 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/addon/walkway_with_bridge_1"), Integer.valueOf(1)), 
/* 122 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/addon/c1_breeze"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 127 */     Pools.register(paramBootstrapContext, "trial_chambers/chamber/assembly", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 129 */           List.of(new Pair[] {
/*     */               
/* 131 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/full_column"), Integer.valueOf(2)), 
/* 132 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/cover_1"), Integer.valueOf(2)), 
/* 133 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/cover_2"), Integer.valueOf(2)), 
/* 134 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/cover_3"), Integer.valueOf(2)), 
/* 135 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/cover_4"), Integer.valueOf(2)), 
/* 136 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/cover_5"), Integer.valueOf(2)), 
/* 137 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/cover_6"), Integer.valueOf(2)), 
/* 138 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/cover_7"), Integer.valueOf(5)), 
/* 139 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/platform_1"), Integer.valueOf(2)), 
/* 140 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/spawner_1"), Integer.valueOf(1)),
/*     */               
/* 142 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/hanging_1"), Integer.valueOf(2)), 
/* 143 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/hanging_2"), Integer.valueOf(1)), 
/* 144 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/hanging_3"), Integer.valueOf(2)), 
/* 145 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/hanging_4"), Integer.valueOf(2)), 
/* 146 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/hanging_5"), Integer.valueOf(4)), 
/*     */               
/* 148 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/left_staircase_1"), Integer.valueOf(1)), 
/* 149 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/left_staircase_2"), Integer.valueOf(1)), 
/* 150 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/left_staircase_3"), Integer.valueOf(1)), 
/* 151 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/right_staircase_1"), Integer.valueOf(1)), 
/* 152 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/right_staircase_2"), Integer.valueOf(1)), 
/* 153 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly/right_staircase_3"), Integer.valueOf(1))
/*     */             }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     Pools.register(paramBootstrapContext, "trial_chambers/chamber/eruption", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 161 */           List.of(
/*     */             
/* 163 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/eruption/center_1"), Integer.valueOf(1)), 
/*     */ 
/*     */             
/* 166 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/eruption/breeze_slice_1"), Integer.valueOf(1)), 
/*     */             
/* 168 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/eruption/slice_1"), Integer.valueOf(1)), 
/* 169 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/eruption/slice_2"), Integer.valueOf(1)), 
/* 170 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/eruption/slice_3"), Integer.valueOf(1)), 
/*     */             
/* 172 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/eruption/quadrant_1"), Integer.valueOf(1)), 
/* 173 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/eruption/quadrant_2"), Integer.valueOf(1)), 
/* 174 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/eruption/quadrant_3"), Integer.valueOf(1)), 
/* 175 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/eruption/quadrant_4"), Integer.valueOf(1)), 
/* 176 */             Pair.of(StructurePoolElement.single("trial_chambers/chamber/eruption/quadrant_5"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     Pools.register(paramBootstrapContext, "trial_chambers/chamber/slanted", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 183 */           List.of(new Pair[] { 
/* 184 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/center"), Integer.valueOf(1)), 
/*     */               
/* 186 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/hallway_1"), Integer.valueOf(1)), 
/* 187 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/hallway_2"), Integer.valueOf(1)), 
/* 188 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/hallway_3"), Integer.valueOf(1)), 
/*     */               
/* 190 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/quadrant_1"), Integer.valueOf(1)), 
/* 191 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/quadrant_2"), Integer.valueOf(1)), 
/* 192 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/quadrant_3"), Integer.valueOf(1)), 
/* 193 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/quadrant_4"), Integer.valueOf(1)), 
/*     */               
/* 195 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/ramp_1"), Integer.valueOf(1)), 
/* 196 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/ramp_2"), Integer.valueOf(1)), 
/* 197 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/ramp_3"), Integer.valueOf(1)), 
/* 198 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/ramp_4"), Integer.valueOf(1)), 
/*     */               
/* 200 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/ominous_upper_arm_1"), Integer.valueOf(1)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     Pools.register(paramBootstrapContext, "trial_chambers/chamber/pedestal", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 207 */           List.of(new Pair[] { 
/* 208 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/pedestal/center_1"), Integer.valueOf(1)), 
/*     */               
/* 210 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/pedestal/slice_1"), Integer.valueOf(1)), 
/* 211 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/pedestal/slice_2"), Integer.valueOf(3)), 
/* 212 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/pedestal/slice_3"), Integer.valueOf(3)), 
/* 213 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/pedestal/slice_4"), Integer.valueOf(3)), 
/* 214 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/pedestal/slice_5"), Integer.valueOf(3)), 
/*     */               
/* 216 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/pedestal/ominous_slice_1"), Integer.valueOf(1)), 
/*     */               
/* 218 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/pedestal/quadrant_1"), Integer.valueOf(1)), 
/* 219 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/pedestal/quadrant_2"), Integer.valueOf(1)), 
/* 220 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/pedestal/quadrant_3"), Integer.valueOf(1)),
/*     */               
/* 222 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/quadrant_1"), Integer.valueOf(1)), 
/* 223 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/quadrant_2"), Integer.valueOf(1)), 
/* 224 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/quadrant_3"), Integer.valueOf(1)), 
/* 225 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted/quadrant_4"), Integer.valueOf(1)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     Pools.register(paramBootstrapContext, "trial_chambers/corridor/slices", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 233 */           List.of(
/* 234 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/straight_1", (Holder)reference3), Integer.valueOf(1)), 
/* 235 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/straight_2", (Holder)reference3), Integer.valueOf(2)), 
/* 236 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/straight_3", (Holder)reference3), Integer.valueOf(2)), 
/* 237 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/straight_4", (Holder)reference3), Integer.valueOf(2)), 
/* 238 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/straight_5", (Holder)reference3), Integer.valueOf(2)), 
/* 239 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/straight_6", (Holder)reference3), Integer.valueOf(2)), 
/* 240 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/straight_7", (Holder)reference3), Integer.valueOf(1)), 
/* 241 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/straight_8", (Holder)reference3), Integer.valueOf(2))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 246 */     paramBootstrapContext.register(HALLWAY_FALLBACK, new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 248 */           List.of(
/* 249 */             Pair.of(StructurePoolElement.single("trial_chambers/hallway/rubble"), Integer.valueOf(1)), 
/* 250 */             Pair.of(StructurePoolElement.single("trial_chambers/hallway/rubble_chamber"), Integer.valueOf(1)), 
/* 251 */             Pair.of(StructurePoolElement.single("trial_chambers/hallway/rubble_thin"), Integer.valueOf(1)), 
/* 252 */             Pair.of(StructurePoolElement.single("trial_chambers/hallway/rubble_chamber_thin"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 257 */     Pools.register(paramBootstrapContext, "trial_chambers/hallway", new StructureTemplatePool((Holder)reference2, 
/*     */           
/* 259 */           List.of(new Pair[] {
/*     */               
/* 261 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/corridor_connector_1"), Integer.valueOf(1)), 
/* 262 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/upper_hallway_connector", (Holder)reference3), Integer.valueOf(1)), 
/* 263 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/lower_hallway_connector", (Holder)reference3), Integer.valueOf(1)), 
/*     */               
/* 265 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/rubble"), Integer.valueOf(1)), 
/*     */ 
/*     */               
/* 268 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/chamber_1", (Holder)reference3), Integer.valueOf(150)), 
/* 269 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/chamber_2", (Holder)reference3), Integer.valueOf(150)), 
/* 270 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/chamber_4", (Holder)reference3), Integer.valueOf(150)), 
/* 271 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/chamber_8", (Holder)reference3), Integer.valueOf(150)), 
/* 272 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/assembly", (Holder)reference3), Integer.valueOf(150)), 
/* 273 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/eruption", (Holder)reference3), Integer.valueOf(150)), 
/* 274 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/slanted", (Holder)reference3), Integer.valueOf(150)), 
/* 275 */               Pair.of(StructurePoolElement.single("trial_chambers/chamber/pedestal", (Holder)reference3), Integer.valueOf(150)), 
/*     */ 
/*     */               
/* 278 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/rubble_chamber", (Holder)reference3), Integer.valueOf(10)), 
/* 279 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/rubble_chamber_thin", (Holder)reference3), Integer.valueOf(1)), 
/*     */ 
/*     */               
/* 282 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/cache_1", (Holder)reference3), Integer.valueOf(1)), 
/* 283 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/left_corner", (Holder)reference3), Integer.valueOf(1)), 
/* 284 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/right_corner", (Holder)reference3), Integer.valueOf(1)), 
/* 285 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/corner_staircase", (Holder)reference3), Integer.valueOf(1)), 
/* 286 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/corner_staircase_down", (Holder)reference3), Integer.valueOf(1)), 
/* 287 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/long_straight_staircase", (Holder)reference3), Integer.valueOf(1)), 
/* 288 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/long_straight_staircase_down", (Holder)reference3), Integer.valueOf(1)), 
/* 289 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/straight", (Holder)reference3), Integer.valueOf(1)), 
/* 290 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/straight_staircase", (Holder)reference3), Integer.valueOf(1)), 
/* 291 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/straight_staircase_down", (Holder)reference3), Integer.valueOf(1)), 
/* 292 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/trapped_staircase", (Holder)reference3), Integer.valueOf(1)), 
/*     */ 
/*     */               
/* 295 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/encounter_1", (Holder)reference3), Integer.valueOf(1)), 
/* 296 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/encounter_2", (Holder)reference3), Integer.valueOf(1)), 
/* 297 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/encounter_3", (Holder)reference3), Integer.valueOf(1)), 
/* 298 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/encounter_4", (Holder)reference3), Integer.valueOf(1)), 
/* 299 */               Pair.of(StructurePoolElement.single("trial_chambers/hallway/encounter_5", (Holder)reference3), Integer.valueOf(1))
/*     */             }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 305 */     Pools.register(paramBootstrapContext, "trial_chambers/corridors/addon/lower", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 307 */           List.of(
/* 308 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(8)), 
/* 309 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/addon/staircase"), Integer.valueOf(1)), 
/* 310 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/addon/wall"), Integer.valueOf(1)), 
/* 311 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/addon/ladder_to_middle"), Integer.valueOf(1)), 
/* 312 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/addon/arrow_dispenser"), Integer.valueOf(1)), 
/* 313 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/addon/bridge_lower"), Integer.valueOf(2))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 318 */     Pools.register(paramBootstrapContext, "trial_chambers/corridors/addon/middle", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 320 */           List.of(
/* 321 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(8)), 
/* 322 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/addon/open_walkway"), Integer.valueOf(2)), 
/* 323 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/addon/walled_walkway"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 328 */     Pools.register(paramBootstrapContext, "trial_chambers/corridors/addon/middle_upper", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 330 */           List.of(
/* 331 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(6)), 
/* 332 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/addon/open_walkway_upper"), Integer.valueOf(2)), 
/* 333 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/addon/chandelier_upper"), Integer.valueOf(1)), 
/* 334 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/addon/decoration_upper"), Integer.valueOf(1)), 
/* 335 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/addon/head_upper"), Integer.valueOf(1)), 
/* 336 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/addon/reward_upper"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 341 */     Pools.register(paramBootstrapContext, "trial_chambers/atrium", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 343 */           List.of(
/* 344 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/atrium/bogged_relief"), Integer.valueOf(1)), 
/* 345 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/atrium/breeze_relief"), Integer.valueOf(1)), 
/* 346 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/atrium/spiral_relief"), Integer.valueOf(1)), 
/* 347 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/atrium/spider_relief"), Integer.valueOf(1)), 
/* 348 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/atrium/grand_staircase_1"), Integer.valueOf(1)), 
/* 349 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/atrium/grand_staircase_2"), Integer.valueOf(1)), 
/* 350 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/atrium/grand_staircase_3"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 355 */     Pools.register(paramBootstrapContext, "trial_chambers/decor", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 357 */           List.of(new Pair[] { 
/* 358 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(22)), 
/* 359 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/empty_pot"), Integer.valueOf(2)), 
/* 360 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/dead_bush_pot"), Integer.valueOf(2)), 
/* 361 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/undecorated_pot"), Integer.valueOf(10)), 
/* 362 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/flow_pot"), Integer.valueOf(1)), 
/* 363 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/guster_pot"), Integer.valueOf(1)), 
/* 364 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/scrape_pot"), Integer.valueOf(1)), 
/* 365 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/candle_1"), Integer.valueOf(1)), 
/* 366 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/candle_2"), Integer.valueOf(1)), 
/* 367 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/candle_3"), Integer.valueOf(1)), 
/* 368 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/candle_4"), Integer.valueOf(1)), 
/* 369 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/barrel"), Integer.valueOf(2)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 374 */     Pools.register(paramBootstrapContext, "trial_chambers/decor/disposal", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 376 */           List.of(
/* 377 */             Pair.of(StructurePoolElement.single("trial_chambers/decor/disposal"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 382 */     Pools.register(paramBootstrapContext, "trial_chambers/decor/bed", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 384 */           List.of(new Pair[] { 
/* 385 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/white_bed"), Integer.valueOf(3)), 
/* 386 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/light_gray_bed"), Integer.valueOf(3)), 
/* 387 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/gray_bed"), Integer.valueOf(3)), 
/* 388 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/black_bed"), Integer.valueOf(3)), 
/* 389 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/brown_bed"), Integer.valueOf(3)), 
/* 390 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/red_bed"), Integer.valueOf(3)), 
/* 391 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/orange_bed"), Integer.valueOf(3)), 
/* 392 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/yellow_bed"), Integer.valueOf(3)), 
/* 393 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/lime_bed"), Integer.valueOf(3)), 
/* 394 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/green_bed"), Integer.valueOf(3)), 
/* 395 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/cyan_bed"), Integer.valueOf(3)), 
/* 396 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/light_blue_bed"), Integer.valueOf(3)), 
/* 397 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/blue_bed"), Integer.valueOf(3)), 
/* 398 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/purple_bed"), Integer.valueOf(3)), 
/* 399 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/magenta_bed"), Integer.valueOf(3)), 
/* 400 */               Pair.of(StructurePoolElement.single("trial_chambers/decor/pink_bed"), Integer.valueOf(1)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 405 */     Pools.register(paramBootstrapContext, "trial_chambers/entrance", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 407 */           List.of(
/* 408 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/addon/display_1"), Integer.valueOf(1)), 
/* 409 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/addon/display_2"), Integer.valueOf(1)), 
/* 410 */             Pair.of(StructurePoolElement.single("trial_chambers/corridor/addon/display_3"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 415 */     Pools.register(paramBootstrapContext, "trial_chambers/decor/chamber", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 417 */           List.of(
/* 418 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(4)), 
/* 419 */             Pair.of(StructurePoolElement.single("trial_chambers/decor/undecorated_pot"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 424 */     Pools.register(paramBootstrapContext, "trial_chambers/reward/all", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 426 */           List.of(
/* 427 */             Pair.of(StructurePoolElement.single("trial_chambers/reward/vault"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 432 */     Pools.register(paramBootstrapContext, "trial_chambers/reward/ominous_vault", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 434 */           List.of(
/* 435 */             Pair.of(StructurePoolElement.single("trial_chambers/reward/ominous_vault"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 440 */     Pools.register(paramBootstrapContext, "trial_chambers/reward/contents/default", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 442 */           List.of(
/* 443 */             Pair.of(StructurePoolElement.single("trial_chambers/reward/vault"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 448 */     Pools.register(paramBootstrapContext, "trial_chambers/chests/supply", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 450 */           List.of(
/* 451 */             Pair.of(StructurePoolElement.single("trial_chambers/chests/connectors/supply"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 456 */     Pools.register(paramBootstrapContext, "trial_chambers/chests/contents/supply", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 458 */           List.of(
/* 459 */             Pair.of(StructurePoolElement.single("trial_chambers/chests/supply"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 464 */     Pools.register(paramBootstrapContext, "trial_chambers/spawner/ranged", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 466 */           List.of(
/* 467 */             Pair.of(StructurePoolElement.single("trial_chambers/spawner/connectors/ranged"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 472 */     Pools.register(paramBootstrapContext, "trial_chambers/spawner/slow_ranged", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 474 */           List.of(
/* 475 */             Pair.of(StructurePoolElement.single("trial_chambers/spawner/connectors/slow_ranged"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 480 */     Pools.register(paramBootstrapContext, "trial_chambers/spawner/melee", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 482 */           List.of(
/* 483 */             Pair.of(StructurePoolElement.single("trial_chambers/spawner/connectors/melee"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 488 */     Pools.register(paramBootstrapContext, "trial_chambers/spawner/small_melee", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 490 */           List.of(
/* 491 */             Pair.of(StructurePoolElement.single("trial_chambers/spawner/connectors/small_melee"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 496 */     Pools.register(paramBootstrapContext, "trial_chambers/spawner/breeze", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 498 */           List.of(
/* 499 */             Pair.of(StructurePoolElement.single("trial_chambers/spawner/connectors/breeze"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 504 */     Pools.register(paramBootstrapContext, "trial_chambers/spawner/all", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 506 */           List.of(
/* 507 */             Pair.of(StructurePoolElement.single("trial_chambers/spawner/connectors/ranged"), Integer.valueOf(1)), 
/* 508 */             Pair.of(StructurePoolElement.single("trial_chambers/spawner/connectors/melee"), Integer.valueOf(1)), 
/* 509 */             Pair.of(StructurePoolElement.single("trial_chambers/spawner/connectors/small_melee"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 514 */     Pools.register(paramBootstrapContext, "trial_chambers/spawner/contents/breeze", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 516 */           List.of(
/* 517 */             Pair.of(StructurePoolElement.single("trial_chambers/spawner/breeze/breeze"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 522 */     Pools.register(paramBootstrapContext, "trial_chambers/dispensers/chamber", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 524 */           List.of(
/* 525 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(1)), 
/* 526 */             Pair.of(StructurePoolElement.single("trial_chambers/dispensers/chamber"), Integer.valueOf(1)), 
/* 527 */             Pair.of(StructurePoolElement.single("trial_chambers/dispensers/wall_dispenser"), Integer.valueOf(1)), 
/* 528 */             Pair.of(StructurePoolElement.single("trial_chambers/dispensers/floor_dispenser"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 533 */     PoolAliasBindings.registerTargetsAsPools(paramBootstrapContext, (Holder)reference1, ALIAS_BINDINGS);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\TrialChambersStructurePools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */