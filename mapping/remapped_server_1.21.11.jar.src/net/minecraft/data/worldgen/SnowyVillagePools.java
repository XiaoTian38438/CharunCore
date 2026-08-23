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
/*     */ public class SnowyVillagePools
/*     */ {
/*  16 */   public static final ResourceKey<StructureTemplatePool> START = Pools.createKey("village/snowy/town_centers");
/*  17 */   private static final ResourceKey<StructureTemplatePool> TERMINATORS_KEY = Pools.createKey("village/snowy/terminators");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<StructureTemplatePool> paramBootstrapContext) {
/*  20 */     HolderGetter<?> holderGetter1 = paramBootstrapContext.lookup(Registries.PLACED_FEATURE);
/*  21 */     Holder.Reference reference1 = holderGetter1.getOrThrow(VillagePlacements.SPRUCE_VILLAGE);
/*  22 */     Holder.Reference reference2 = holderGetter1.getOrThrow(VillagePlacements.PILE_SNOW_VILLAGE);
/*  23 */     Holder.Reference reference3 = holderGetter1.getOrThrow(VillagePlacements.PILE_ICE_VILLAGE);
/*     */     
/*  25 */     HolderGetter<?> holderGetter2 = paramBootstrapContext.lookup(Registries.PROCESSOR_LIST);
/*  26 */     Holder.Reference reference4 = holderGetter2.getOrThrow(ProcessorLists.STREET_SNOWY_OR_TAIGA);
/*  27 */     Holder.Reference reference5 = holderGetter2.getOrThrow(ProcessorLists.FARM_SNOWY);
/*  28 */     Holder.Reference reference6 = holderGetter2.getOrThrow(ProcessorLists.ZOMBIE_SNOWY);
/*     */     
/*  30 */     HolderGetter<?> holderGetter3 = paramBootstrapContext.lookup(Registries.TEMPLATE_POOL);
/*  31 */     Holder.Reference reference7 = holderGetter3.getOrThrow(Pools.EMPTY);
/*  32 */     Holder.Reference reference8 = holderGetter3.getOrThrow(TERMINATORS_KEY);
/*     */     
/*  34 */     paramBootstrapContext.register(START, new StructureTemplatePool((Holder)reference7, 
/*     */           
/*  36 */           (List)ImmutableList.of(
/*  37 */             Pair.of(StructurePoolElement.legacy("village/snowy/town_centers/snowy_meeting_point_1"), Integer.valueOf(100)), 
/*  38 */             Pair.of(StructurePoolElement.legacy("village/snowy/town_centers/snowy_meeting_point_2"), Integer.valueOf(50)), 
/*  39 */             Pair.of(StructurePoolElement.legacy("village/snowy/town_centers/snowy_meeting_point_3"), Integer.valueOf(150)), 
/*  40 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/town_centers/snowy_meeting_point_1"), Integer.valueOf(2)), 
/*  41 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/town_centers/snowy_meeting_point_2"), Integer.valueOf(1)), 
/*  42 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/town_centers/snowy_meeting_point_3"), Integer.valueOf(3))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  47 */     Pools.register(paramBootstrapContext, "village/snowy/streets", new StructureTemplatePool((Holder)reference8, 
/*     */           
/*  49 */           (List)ImmutableList.of(
/*  50 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/corner_01", (Holder)reference4), Integer.valueOf(2)), 
/*  51 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/corner_02", (Holder)reference4), Integer.valueOf(2)), 
/*  52 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/corner_03", (Holder)reference4), Integer.valueOf(2)), 
/*  53 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/square_01", (Holder)reference4), Integer.valueOf(2)), 
/*  54 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/straight_01", (Holder)reference4), Integer.valueOf(4)), 
/*  55 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/straight_02", (Holder)reference4), Integer.valueOf(4)), 
/*  56 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/straight_03", (Holder)reference4), Integer.valueOf(4)), 
/*  57 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/straight_04", (Holder)reference4), Integer.valueOf(7)), 
/*  58 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/straight_06", (Holder)reference4), Integer.valueOf(4)), 
/*  59 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/straight_08", (Holder)reference4), Integer.valueOf(4)), 
/*  60 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/crossroad_02", (Holder)reference4), Integer.valueOf(1)), 
/*  61 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/crossroad_03", (Holder)reference4), Integer.valueOf(2)), (Object[])new Pair[] {
/*  62 */               Pair.of(StructurePoolElement.legacy("village/snowy/streets/crossroad_04", (Holder)reference4), Integer.valueOf(2)), 
/*  63 */               Pair.of(StructurePoolElement.legacy("village/snowy/streets/crossroad_05", (Holder)reference4), Integer.valueOf(2)), 
/*  64 */               Pair.of(StructurePoolElement.legacy("village/snowy/streets/crossroad_06", (Holder)reference4), Integer.valueOf(2)), 
/*  65 */               Pair.of(StructurePoolElement.legacy("village/snowy/streets/turn_01", (Holder)reference4), Integer.valueOf(3))
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/*  70 */     Pools.register(paramBootstrapContext, "village/snowy/zombie/streets", new StructureTemplatePool((Holder)reference8, 
/*     */           
/*  72 */           (List)ImmutableList.of(
/*  73 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/corner_01", (Holder)reference4), Integer.valueOf(2)), 
/*  74 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/corner_02", (Holder)reference4), Integer.valueOf(2)), 
/*  75 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/corner_03", (Holder)reference4), Integer.valueOf(2)), 
/*  76 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/square_01", (Holder)reference4), Integer.valueOf(2)), 
/*  77 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/straight_01", (Holder)reference4), Integer.valueOf(4)), 
/*  78 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/straight_02", (Holder)reference4), Integer.valueOf(4)), 
/*  79 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/straight_03", (Holder)reference4), Integer.valueOf(4)), 
/*  80 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/straight_04", (Holder)reference4), Integer.valueOf(7)), 
/*  81 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/straight_06", (Holder)reference4), Integer.valueOf(4)), 
/*  82 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/straight_08", (Holder)reference4), Integer.valueOf(4)), 
/*  83 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/crossroad_02", (Holder)reference4), Integer.valueOf(1)), 
/*  84 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/crossroad_03", (Holder)reference4), Integer.valueOf(2)), (Object[])new Pair[] {
/*  85 */               Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/crossroad_04", (Holder)reference4), Integer.valueOf(2)), 
/*  86 */               Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/crossroad_05", (Holder)reference4), Integer.valueOf(2)), 
/*  87 */               Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/crossroad_06", (Holder)reference4), Integer.valueOf(2)), 
/*  88 */               Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/turn_01", (Holder)reference4), Integer.valueOf(3))
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/*  93 */     Pools.register(paramBootstrapContext, "village/snowy/houses", new StructureTemplatePool((Holder)reference8, 
/*     */           
/*  95 */           (List)ImmutableList.of(
/*  96 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_small_house_1"), Integer.valueOf(2)), 
/*  97 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_small_house_2"), Integer.valueOf(2)), 
/*  98 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_small_house_3"), Integer.valueOf(2)), 
/*  99 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_small_house_4"), Integer.valueOf(3)), 
/* 100 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_small_house_5"), Integer.valueOf(2)), 
/* 101 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_small_house_6"), Integer.valueOf(2)), 
/* 102 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_small_house_7"), Integer.valueOf(2)), 
/* 103 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_small_house_8"), Integer.valueOf(2)), 
/* 104 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_medium_house_1"), Integer.valueOf(2)), 
/* 105 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_medium_house_2"), Integer.valueOf(2)), 
/* 106 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_medium_house_3"), Integer.valueOf(2)), 
/* 107 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_butchers_shop_1"), Integer.valueOf(2)), (Object[])new Pair[] { 
/* 108 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_butchers_shop_2"), Integer.valueOf(2)), 
/* 109 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_tool_smith_1"), Integer.valueOf(2)), 
/* 110 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_fletcher_house_1"), Integer.valueOf(2)), 
/* 111 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_shepherds_house_1"), Integer.valueOf(3)), 
/* 112 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_armorer_house_1"), Integer.valueOf(1)), 
/* 113 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_armorer_house_2"), Integer.valueOf(1)), 
/* 114 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_fisher_cottage"), Integer.valueOf(2)), 
/* 115 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_tannery_1"), Integer.valueOf(2)), 
/* 116 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_cartographer_house_1"), Integer.valueOf(2)), 
/* 117 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_library_1"), Integer.valueOf(2)), 
/* 118 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_masons_house_1"), Integer.valueOf(2)), 
/* 119 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_masons_house_2"), Integer.valueOf(2)), 
/* 120 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_weapon_smith_1"), Integer.valueOf(2)), 
/* 121 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_temple_1"), Integer.valueOf(2)), 
/* 122 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_farm_1", (Holder)reference5), Integer.valueOf(3)), 
/* 123 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_farm_2", (Holder)reference5), Integer.valueOf(3)), 
/* 124 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_animal_pen_1"), Integer.valueOf(2)), 
/* 125 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_animal_pen_2"), Integer.valueOf(2)), 
/* 126 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(6)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     Pools.register(paramBootstrapContext, "village/snowy/zombie/houses", new StructureTemplatePool((Holder)reference8, 
/*     */           
/* 133 */           (List)ImmutableList.of(
/* 134 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_small_house_1", (Holder)reference6), Integer.valueOf(2)), 
/* 135 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_small_house_2", (Holder)reference6), Integer.valueOf(2)), 
/* 136 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_small_house_3", (Holder)reference6), Integer.valueOf(2)), 
/* 137 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_small_house_4", (Holder)reference6), Integer.valueOf(2)), 
/* 138 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_small_house_5", (Holder)reference6), Integer.valueOf(2)), 
/* 139 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_small_house_6", (Holder)reference6), Integer.valueOf(2)), 
/* 140 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_small_house_7", (Holder)reference6), Integer.valueOf(2)), 
/* 141 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_small_house_8", (Holder)reference6), Integer.valueOf(2)), 
/* 142 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_medium_house_1", (Holder)reference6), Integer.valueOf(2)), 
/* 143 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_medium_house_2", (Holder)reference6), Integer.valueOf(2)), 
/* 144 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_medium_house_3", (Holder)reference6), Integer.valueOf(1)), 
/* 145 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_butchers_shop_1", (Holder)reference6), Integer.valueOf(2)), (Object[])new Pair[] { 
/* 146 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_butchers_shop_2", (Holder)reference6), Integer.valueOf(2)), 
/* 147 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_tool_smith_1", (Holder)reference6), Integer.valueOf(2)), 
/* 148 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_fletcher_house_1", (Holder)reference6), Integer.valueOf(2)), 
/* 149 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_shepherds_house_1", (Holder)reference6), Integer.valueOf(2)), 
/* 150 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_armorer_house_1", (Holder)reference6), Integer.valueOf(1)), 
/* 151 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_armorer_house_2", (Holder)reference6), Integer.valueOf(1)), 
/* 152 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_fisher_cottage", (Holder)reference6), Integer.valueOf(2)), 
/* 153 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_tannery_1", (Holder)reference6), Integer.valueOf(2)), 
/* 154 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_cartographer_house_1", (Holder)reference6), Integer.valueOf(2)), 
/* 155 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_library_1", (Holder)reference6), Integer.valueOf(2)), 
/* 156 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_masons_house_1", (Holder)reference6), Integer.valueOf(2)), 
/* 157 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_masons_house_2", (Holder)reference6), Integer.valueOf(2)), 
/* 158 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_weapon_smith_1", (Holder)reference6), Integer.valueOf(2)), 
/* 159 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_temple_1", (Holder)reference6), Integer.valueOf(2)), 
/* 160 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_farm_1", (Holder)reference6), Integer.valueOf(3)), 
/* 161 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_farm_2", (Holder)reference6), Integer.valueOf(3)), 
/* 162 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_animal_pen_1", (Holder)reference6), Integer.valueOf(2)), 
/* 163 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_animal_pen_2", (Holder)reference6), Integer.valueOf(2)), 
/* 164 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(6)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     paramBootstrapContext.register(TERMINATORS_KEY, new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 171 */           (List)ImmutableList.of(
/* 172 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_01", (Holder)reference4), Integer.valueOf(1)), 
/* 173 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_02", (Holder)reference4), Integer.valueOf(1)), 
/* 174 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_03", (Holder)reference4), Integer.valueOf(1)), 
/* 175 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_04", (Holder)reference4), Integer.valueOf(1))), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     Pools.register(paramBootstrapContext, "village/snowy/trees", new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 182 */           (List)ImmutableList.of(
/* 183 */             Pair.of(StructurePoolElement.feature((Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     Pools.register(paramBootstrapContext, "village/snowy/decor", new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 190 */           (List)ImmutableList.of(
/* 191 */             Pair.of(StructurePoolElement.legacy("village/snowy/snowy_lamp_post_01"), Integer.valueOf(4)), 
/* 192 */             Pair.of(StructurePoolElement.legacy("village/snowy/snowy_lamp_post_02"), Integer.valueOf(4)), 
/* 193 */             Pair.of(StructurePoolElement.legacy("village/snowy/snowy_lamp_post_03"), Integer.valueOf(1)), 
/* 194 */             Pair.of(StructurePoolElement.feature((Holder)reference1), Integer.valueOf(4)), 
/* 195 */             Pair.of(StructurePoolElement.feature((Holder)reference2), Integer.valueOf(4)), 
/* 196 */             Pair.of(StructurePoolElement.feature((Holder)reference3), Integer.valueOf(1)), 
/* 197 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(9))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 202 */     Pools.register(paramBootstrapContext, "village/snowy/zombie/decor", new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 204 */           (List)ImmutableList.of(
/* 205 */             Pair.of(StructurePoolElement.legacy("village/snowy/snowy_lamp_post_01", (Holder)reference6), Integer.valueOf(1)), 
/* 206 */             Pair.of(StructurePoolElement.legacy("village/snowy/snowy_lamp_post_02", (Holder)reference6), Integer.valueOf(1)), 
/* 207 */             Pair.of(StructurePoolElement.legacy("village/snowy/snowy_lamp_post_03", (Holder)reference6), Integer.valueOf(1)), 
/* 208 */             Pair.of(StructurePoolElement.feature((Holder)reference1), Integer.valueOf(4)), 
/* 209 */             Pair.of(StructurePoolElement.feature((Holder)reference2), Integer.valueOf(4)), 
/* 210 */             Pair.of(StructurePoolElement.feature((Holder)reference3), Integer.valueOf(4)), 
/* 211 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(7))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     Pools.register(paramBootstrapContext, "village/snowy/villagers", new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 218 */           (List)ImmutableList.of(
/* 219 */             Pair.of(StructurePoolElement.legacy("village/snowy/villagers/nitwit"), Integer.valueOf(1)), 
/* 220 */             Pair.of(StructurePoolElement.legacy("village/snowy/villagers/baby"), Integer.valueOf(1)), 
/* 221 */             Pair.of(StructurePoolElement.legacy("village/snowy/villagers/unemployed"), Integer.valueOf(10))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 226 */     Pools.register(paramBootstrapContext, "village/snowy/zombie/villagers", new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 228 */           (List)ImmutableList.of(
/* 229 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/villagers/nitwit"), Integer.valueOf(1)), 
/* 230 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/villagers/unemployed"), Integer.valueOf(10))), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\SnowyVillagePools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */