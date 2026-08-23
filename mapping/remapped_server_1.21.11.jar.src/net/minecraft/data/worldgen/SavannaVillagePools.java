/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.placement.VillagePlacements;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ 
/*     */ public class SavannaVillagePools
/*     */ {
/*  16 */   public static final ResourceKey<StructureTemplatePool> START = Pools.createKey("village/savanna/town_centers");
/*  17 */   private static final ResourceKey<StructureTemplatePool> TERMINATORS_KEY = Pools.createKey("village/savanna/terminators");
/*  18 */   private static final ResourceKey<StructureTemplatePool> ZOMBIE_TERMINATORS_KEY = Pools.createKey("village/savanna/zombie/terminators");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<StructureTemplatePool> paramBootstrapContext) {
/*  21 */     HolderGetter<?> holderGetter1 = paramBootstrapContext.lookup(Registries.PLACED_FEATURE);
/*  22 */     Holder.Reference reference1 = holderGetter1.getOrThrow(VillagePlacements.ACACIA_VILLAGE);
/*  23 */     Holder.Reference reference2 = holderGetter1.getOrThrow(VillagePlacements.PILE_HAY_VILLAGE);
/*  24 */     Holder.Reference reference3 = holderGetter1.getOrThrow(VillagePlacements.PILE_MELON_VILLAGE);
/*     */     
/*  26 */     HolderGetter<?> holderGetter2 = paramBootstrapContext.lookup(Registries.PROCESSOR_LIST);
/*  27 */     Holder.Reference reference4 = holderGetter2.getOrThrow(ProcessorLists.ZOMBIE_SAVANNA);
/*  28 */     Holder.Reference reference5 = holderGetter2.getOrThrow(ProcessorLists.STREET_SAVANNA);
/*  29 */     Holder.Reference reference6 = holderGetter2.getOrThrow(ProcessorLists.FARM_SAVANNA);
/*     */     
/*  31 */     HolderGetter<?> holderGetter3 = paramBootstrapContext.lookup(Registries.TEMPLATE_POOL);
/*  32 */     Holder.Reference reference7 = holderGetter3.getOrThrow(Pools.EMPTY);
/*  33 */     Holder.Reference reference8 = holderGetter3.getOrThrow(TERMINATORS_KEY);
/*  34 */     Holder.Reference reference9 = holderGetter3.getOrThrow(ZOMBIE_TERMINATORS_KEY);
/*     */     
/*  36 */     paramBootstrapContext.register(START, new StructureTemplatePool((Holder)reference7, 
/*     */           
/*  38 */           (List)ImmutableList.of(
/*  39 */             Pair.of(StructurePoolElement.legacy("village/savanna/town_centers/savanna_meeting_point_1"), Integer.valueOf(100)), 
/*  40 */             Pair.of(StructurePoolElement.legacy("village/savanna/town_centers/savanna_meeting_point_2"), Integer.valueOf(50)), 
/*  41 */             Pair.of(StructurePoolElement.legacy("village/savanna/town_centers/savanna_meeting_point_3"), Integer.valueOf(150)), 
/*  42 */             Pair.of(StructurePoolElement.legacy("village/savanna/town_centers/savanna_meeting_point_4"), Integer.valueOf(150)), 
/*  43 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/town_centers/savanna_meeting_point_1", (Holder)reference4), Integer.valueOf(2)), 
/*  44 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/town_centers/savanna_meeting_point_2", (Holder)reference4), Integer.valueOf(1)), 
/*  45 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/town_centers/savanna_meeting_point_3", (Holder)reference4), Integer.valueOf(3)), 
/*  46 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/town_centers/savanna_meeting_point_4", (Holder)reference4), Integer.valueOf(3))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     Pools.register(paramBootstrapContext, "village/savanna/streets", new StructureTemplatePool((Holder)reference8, 
/*     */           
/*  53 */           (List)ImmutableList.of(
/*  54 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/corner_01", (Holder)reference5), Integer.valueOf(2)), 
/*  55 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/corner_03", (Holder)reference5), Integer.valueOf(2)), 
/*  56 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/straight_02", (Holder)reference5), Integer.valueOf(4)), 
/*  57 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/straight_04", (Holder)reference5), Integer.valueOf(7)), 
/*  58 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/straight_05", (Holder)reference5), Integer.valueOf(3)), 
/*  59 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/straight_06", (Holder)reference5), Integer.valueOf(4)), 
/*  60 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/straight_08", (Holder)reference5), Integer.valueOf(4)), 
/*  61 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/straight_09", (Holder)reference5), Integer.valueOf(4)), 
/*  62 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/straight_10", (Holder)reference5), Integer.valueOf(4)), 
/*  63 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/straight_11", (Holder)reference5), Integer.valueOf(4)), 
/*  64 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/crossroad_02", (Holder)reference5), Integer.valueOf(1)), 
/*  65 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/crossroad_03", (Holder)reference5), Integer.valueOf(2)), (Object[])new Pair[] {
/*  66 */               Pair.of(StructurePoolElement.legacy("village/savanna/streets/crossroad_04", (Holder)reference5), Integer.valueOf(2)), 
/*  67 */               Pair.of(StructurePoolElement.legacy("village/savanna/streets/crossroad_05", (Holder)reference5), Integer.valueOf(2)), 
/*  68 */               Pair.of(StructurePoolElement.legacy("village/savanna/streets/crossroad_06", (Holder)reference5), Integer.valueOf(2)), 
/*  69 */               Pair.of(StructurePoolElement.legacy("village/savanna/streets/crossroad_07", (Holder)reference5), Integer.valueOf(2)), 
/*  70 */               Pair.of(StructurePoolElement.legacy("village/savanna/streets/split_01", (Holder)reference5), Integer.valueOf(2)), 
/*  71 */               Pair.of(StructurePoolElement.legacy("village/savanna/streets/split_02", (Holder)reference5), Integer.valueOf(2)), 
/*  72 */               Pair.of(StructurePoolElement.legacy("village/savanna/streets/turn_01", (Holder)reference5), Integer.valueOf(3))
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/*  77 */     Pools.register(paramBootstrapContext, "village/savanna/zombie/streets", new StructureTemplatePool((Holder)reference9, 
/*     */           
/*  79 */           (List)ImmutableList.of(
/*  80 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/corner_01", (Holder)reference5), Integer.valueOf(2)), 
/*  81 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/corner_03", (Holder)reference5), Integer.valueOf(2)), 
/*  82 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/straight_02", (Holder)reference5), Integer.valueOf(4)), 
/*  83 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/straight_04", (Holder)reference5), Integer.valueOf(7)), 
/*  84 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/straight_05", (Holder)reference5), Integer.valueOf(3)), 
/*  85 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/straight_06", (Holder)reference5), Integer.valueOf(4)), 
/*  86 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/straight_08", (Holder)reference5), Integer.valueOf(4)), 
/*  87 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/straight_09", (Holder)reference5), Integer.valueOf(4)), 
/*  88 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/straight_10", (Holder)reference5), Integer.valueOf(4)), 
/*  89 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/straight_11", (Holder)reference5), Integer.valueOf(4)), 
/*  90 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/crossroad_02", (Holder)reference5), Integer.valueOf(1)), 
/*  91 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/crossroad_03", (Holder)reference5), Integer.valueOf(2)), (Object[])new Pair[] {
/*  92 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/crossroad_04", (Holder)reference5), Integer.valueOf(2)), 
/*  93 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/crossroad_05", (Holder)reference5), Integer.valueOf(2)), 
/*  94 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/crossroad_06", (Holder)reference5), Integer.valueOf(2)), 
/*  95 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/crossroad_07", (Holder)reference5), Integer.valueOf(2)), 
/*  96 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/split_01", (Holder)reference5), Integer.valueOf(2)), 
/*  97 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/split_02", (Holder)reference5), Integer.valueOf(2)), 
/*  98 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/turn_01", (Holder)reference5), Integer.valueOf(3))
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/* 103 */     Pools.register(paramBootstrapContext, "village/savanna/houses", new StructureTemplatePool((Holder)reference8, 
/*     */           
/* 105 */           (List)ImmutableList.of(
/* 106 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_house_1"), Integer.valueOf(2)), 
/* 107 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_house_2"), Integer.valueOf(2)), 
/* 108 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_house_3"), Integer.valueOf(2)), 
/* 109 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_house_4"), Integer.valueOf(2)), 
/* 110 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_house_5"), Integer.valueOf(2)), 
/* 111 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_house_6"), Integer.valueOf(2)), 
/* 112 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_house_7"), Integer.valueOf(2)), 
/* 113 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_house_8"), Integer.valueOf(2)), 
/* 114 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_medium_house_1"), Integer.valueOf(2)), 
/* 115 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_medium_house_2"), Integer.valueOf(2)), 
/* 116 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_butchers_shop_1"), Integer.valueOf(2)), 
/* 117 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_butchers_shop_2"), Integer.valueOf(2)), (Object[])new Pair[] { 
/* 118 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_tool_smith_1"), Integer.valueOf(2)), 
/* 119 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_fletcher_house_1"), Integer.valueOf(2)), 
/* 120 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_shepherd_1"), Integer.valueOf(7)), 
/* 121 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_armorer_1"), Integer.valueOf(1)), 
/* 122 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_fisher_cottage_1"), Integer.valueOf(3)), 
/* 123 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_tannery_1"), Integer.valueOf(2)), 
/* 124 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_cartographer_1"), Integer.valueOf(2)), 
/* 125 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_library_1"), Integer.valueOf(2)), 
/* 126 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_mason_1"), Integer.valueOf(2)), 
/* 127 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_weaponsmith_1"), Integer.valueOf(2)), 
/* 128 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_weaponsmith_2"), Integer.valueOf(2)), 
/* 129 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_temple_1"), Integer.valueOf(2)), 
/* 130 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_temple_2"), Integer.valueOf(3)), 
/* 131 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_large_farm_1", (Holder)reference6), Integer.valueOf(4)), 
/* 132 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_large_farm_2", (Holder)reference6), Integer.valueOf(6)), 
/* 133 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_farm", (Holder)reference6), Integer.valueOf(4)), 
/* 134 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_animal_pen_1"), Integer.valueOf(2)), 
/* 135 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_animal_pen_2"), Integer.valueOf(2)), 
/* 136 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_animal_pen_3"), Integer.valueOf(2)), 
/* 137 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(5)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     Pools.register(paramBootstrapContext, "village/savanna/zombie/houses", new StructureTemplatePool((Holder)reference9, 
/*     */           
/* 144 */           (List)ImmutableList.of(
/* 145 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_small_house_1", (Holder)reference4), Integer.valueOf(2)), 
/* 146 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_small_house_2", (Holder)reference4), Integer.valueOf(2)), 
/* 147 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_small_house_3", (Holder)reference4), Integer.valueOf(2)), 
/* 148 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_small_house_4", (Holder)reference4), Integer.valueOf(2)), 
/* 149 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_small_house_5", (Holder)reference4), Integer.valueOf(2)), 
/* 150 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_small_house_6", (Holder)reference4), Integer.valueOf(2)), 
/* 151 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_small_house_7", (Holder)reference4), Integer.valueOf(2)), 
/* 152 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_small_house_8", (Holder)reference4), Integer.valueOf(2)), 
/* 153 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_medium_house_1", (Holder)reference4), Integer.valueOf(2)), 
/* 154 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_medium_house_2", (Holder)reference4), Integer.valueOf(2)), 
/* 155 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_butchers_shop_1", (Holder)reference4), Integer.valueOf(2)), 
/* 156 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_butchers_shop_2", (Holder)reference4), Integer.valueOf(2)), (Object[])new Pair[] { 
/* 157 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_tool_smith_1", (Holder)reference4), Integer.valueOf(2)), 
/* 158 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_fletcher_house_1", (Holder)reference4), Integer.valueOf(2)), 
/* 159 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_shepherd_1", (Holder)reference4), Integer.valueOf(2)), 
/* 160 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_armorer_1", (Holder)reference4), Integer.valueOf(1)), 
/* 161 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_fisher_cottage_1", (Holder)reference4), Integer.valueOf(2)), 
/* 162 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_tannery_1", (Holder)reference4), Integer.valueOf(2)), 
/* 163 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_cartographer_1", (Holder)reference4), Integer.valueOf(2)), 
/* 164 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_library_1", (Holder)reference4), Integer.valueOf(2)), 
/* 165 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_mason_1", (Holder)reference4), Integer.valueOf(2)), 
/* 166 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_weaponsmith_1", (Holder)reference4), Integer.valueOf(2)), 
/* 167 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_weaponsmith_2", (Holder)reference4), Integer.valueOf(2)), 
/* 168 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_temple_1", (Holder)reference4), Integer.valueOf(1)), 
/* 169 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_temple_2", (Holder)reference4), Integer.valueOf(3)), 
/* 170 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_large_farm_1", (Holder)reference4), Integer.valueOf(4)), 
/* 171 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_large_farm_2", (Holder)reference4), Integer.valueOf(4)), 
/* 172 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_farm", (Holder)reference4), Integer.valueOf(4)), 
/* 173 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_animal_pen_1", (Holder)reference4), Integer.valueOf(2)), 
/* 174 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_animal_pen_2", (Holder)reference4), Integer.valueOf(2)), 
/* 175 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_animal_pen_3", (Holder)reference4), Integer.valueOf(2)), 
/* 176 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(5)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     paramBootstrapContext.register(TERMINATORS_KEY, new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 183 */           (List)ImmutableList.of(
/* 184 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_01", (Holder)reference5), Integer.valueOf(1)), 
/* 185 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_02", (Holder)reference5), Integer.valueOf(1)), 
/* 186 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_03", (Holder)reference5), Integer.valueOf(1)), 
/* 187 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_04", (Holder)reference5), Integer.valueOf(1)), 
/* 188 */             Pair.of(StructurePoolElement.legacy("village/savanna/terminators/terminator_05", (Holder)reference5), Integer.valueOf(1))), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     paramBootstrapContext.register(ZOMBIE_TERMINATORS_KEY, new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 195 */           (List)ImmutableList.of(
/* 196 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_01", (Holder)reference5), Integer.valueOf(1)), 
/* 197 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_02", (Holder)reference5), Integer.valueOf(1)), 
/* 198 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_03", (Holder)reference5), Integer.valueOf(1)), 
/* 199 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_04", (Holder)reference5), Integer.valueOf(1)), 
/* 200 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/terminators/terminator_05", (Holder)reference5), Integer.valueOf(1))), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     Pools.register(paramBootstrapContext, "village/savanna/trees", new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 207 */           (List)ImmutableList.of(
/* 208 */             Pair.of(StructurePoolElement.feature((Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     Pools.register(paramBootstrapContext, "village/savanna/decor", new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 215 */           (List)ImmutableList.of(
/* 216 */             Pair.of(StructurePoolElement.legacy("village/savanna/savanna_lamp_post_01"), Integer.valueOf(4)), 
/* 217 */             Pair.of(StructurePoolElement.feature((Holder)reference1), Integer.valueOf(4)), 
/* 218 */             Pair.of(StructurePoolElement.feature((Holder)reference2), Integer.valueOf(4)), 
/* 219 */             Pair.of(StructurePoolElement.feature((Holder)reference3), Integer.valueOf(1)), 
/* 220 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(4))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     Pools.register(paramBootstrapContext, "village/savanna/zombie/decor", new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 227 */           (List)ImmutableList.of(
/* 228 */             Pair.of(StructurePoolElement.legacy("village/savanna/savanna_lamp_post_01", (Holder)reference4), Integer.valueOf(4)), 
/* 229 */             Pair.of(StructurePoolElement.feature((Holder)reference1), Integer.valueOf(4)), 
/* 230 */             Pair.of(StructurePoolElement.feature((Holder)reference2), Integer.valueOf(4)), 
/* 231 */             Pair.of(StructurePoolElement.feature((Holder)reference3), Integer.valueOf(1)), 
/* 232 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(4))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     Pools.register(paramBootstrapContext, "village/savanna/villagers", new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 239 */           (List)ImmutableList.of(
/* 240 */             Pair.of(StructurePoolElement.legacy("village/savanna/villagers/nitwit"), Integer.valueOf(1)), 
/* 241 */             Pair.of(StructurePoolElement.legacy("village/savanna/villagers/baby"), Integer.valueOf(1)), 
/* 242 */             Pair.of(StructurePoolElement.legacy("village/savanna/villagers/unemployed"), Integer.valueOf(10))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     Pools.register(paramBootstrapContext, "village/savanna/zombie/villagers", new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 249 */           (List)ImmutableList.of(
/* 250 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/villagers/nitwit"), Integer.valueOf(1)), 
/* 251 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/villagers/unemployed"), Integer.valueOf(10))), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\SavannaVillagePools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */