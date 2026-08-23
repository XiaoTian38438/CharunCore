/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ 
/*     */ public class BastionTreasureRoomPools {
/*     */   public static void bootstrap(BootstrapContext<StructureTemplatePool> paramBootstrapContext) {
/*  14 */     HolderGetter<?> holderGetter1 = paramBootstrapContext.lookup(Registries.PROCESSOR_LIST);
/*  15 */     Holder.Reference reference1 = holderGetter1.getOrThrow(ProcessorLists.TREASURE_ROOMS);
/*  16 */     Holder.Reference reference2 = holderGetter1.getOrThrow(ProcessorLists.HIGH_WALL);
/*  17 */     Holder.Reference reference3 = holderGetter1.getOrThrow(ProcessorLists.BOTTOM_RAMPART);
/*  18 */     Holder.Reference reference4 = holderGetter1.getOrThrow(ProcessorLists.HIGH_RAMPART);
/*  19 */     Holder.Reference reference5 = holderGetter1.getOrThrow(ProcessorLists.ROOF);
/*     */     
/*  21 */     HolderGetter<?> holderGetter2 = paramBootstrapContext.lookup(Registries.TEMPLATE_POOL);
/*  22 */     Holder.Reference reference6 = holderGetter2.getOrThrow(Pools.EMPTY);
/*     */     
/*  24 */     Pools.register(paramBootstrapContext, "bastion/treasure/bases", new StructureTemplatePool((Holder)reference6, 
/*     */           
/*  26 */           (List)ImmutableList.of(
/*  27 */             Pair.of(StructurePoolElement.single("bastion/treasure/bases/lava_basin", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  32 */     Pools.register(paramBootstrapContext, "bastion/treasure/stairs", new StructureTemplatePool((Holder)reference6, 
/*     */           
/*  34 */           (List)ImmutableList.of(
/*  35 */             Pair.of(StructurePoolElement.single("bastion/treasure/stairs/lower_stairs", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  40 */     Pools.register(paramBootstrapContext, "bastion/treasure/bases/centers", new StructureTemplatePool((Holder)reference6, 
/*     */           
/*  42 */           (List)ImmutableList.of(
/*  43 */             Pair.of(StructurePoolElement.single("bastion/treasure/bases/centers/center_0", (Holder)reference1), Integer.valueOf(1)), 
/*  44 */             Pair.of(StructurePoolElement.single("bastion/treasure/bases/centers/center_1", (Holder)reference1), Integer.valueOf(1)), 
/*  45 */             Pair.of(StructurePoolElement.single("bastion/treasure/bases/centers/center_2", (Holder)reference1), Integer.valueOf(1)), 
/*  46 */             Pair.of(StructurePoolElement.single("bastion/treasure/bases/centers/center_3", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     Pools.register(paramBootstrapContext, "bastion/treasure/brains", new StructureTemplatePool((Holder)reference6, 
/*     */           
/*  53 */           (List)ImmutableList.of(
/*  54 */             Pair.of(StructurePoolElement.single("bastion/treasure/brains/center_brain", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     Pools.register(paramBootstrapContext, "bastion/treasure/walls", new StructureTemplatePool((Holder)reference6, 
/*     */           
/*  61 */           (List)ImmutableList.of(
/*  62 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/lava_wall", (Holder)reference1), Integer.valueOf(1)), 
/*  63 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/entrance_wall", (Holder)reference2), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     Pools.register(paramBootstrapContext, "bastion/treasure/walls/outer", new StructureTemplatePool((Holder)reference6, 
/*     */           
/*  70 */           (List)ImmutableList.of(
/*  71 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/top_corner", (Holder)reference2), Integer.valueOf(1)), 
/*  72 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/mid_corner", (Holder)reference2), Integer.valueOf(1)), 
/*  73 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/bottom_corner", (Holder)reference2), Integer.valueOf(1)), 
/*  74 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/outer_wall", (Holder)reference2), Integer.valueOf(1)), 
/*  75 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/medium_outer_wall", (Holder)reference2), Integer.valueOf(1)), 
/*  76 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/tall_outer_wall", (Holder)reference2), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     Pools.register(paramBootstrapContext, "bastion/treasure/walls/bottom", new StructureTemplatePool((Holder)reference6, 
/*     */           
/*  83 */           (List)ImmutableList.of(
/*  84 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/bottom/wall_0", (Holder)reference1), Integer.valueOf(1)), 
/*  85 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/bottom/wall_1", (Holder)reference1), Integer.valueOf(1)), 
/*  86 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/bottom/wall_2", (Holder)reference1), Integer.valueOf(1)), 
/*  87 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/bottom/wall_3", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     Pools.register(paramBootstrapContext, "bastion/treasure/walls/mid", new StructureTemplatePool((Holder)reference6, 
/*     */           
/*  94 */           (List)ImmutableList.of(
/*  95 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/mid/wall_0", (Holder)reference1), Integer.valueOf(1)), 
/*  96 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/mid/wall_1", (Holder)reference1), Integer.valueOf(1)), 
/*  97 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/mid/wall_2", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     Pools.register(paramBootstrapContext, "bastion/treasure/walls/top", new StructureTemplatePool((Holder)reference6, 
/*     */           
/* 104 */           (List)ImmutableList.of(
/* 105 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/top/main_entrance", (Holder)reference1), Integer.valueOf(1)), 
/* 106 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/top/wall_0", (Holder)reference1), Integer.valueOf(1)), 
/* 107 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/top/wall_1", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     Pools.register(paramBootstrapContext, "bastion/treasure/connectors", new StructureTemplatePool((Holder)reference6, 
/*     */           
/* 114 */           (List)ImmutableList.of(
/* 115 */             Pair.of(StructurePoolElement.single("bastion/treasure/connectors/center_to_wall_middle", (Holder)reference1), Integer.valueOf(1)), 
/* 116 */             Pair.of(StructurePoolElement.single("bastion/treasure/connectors/center_to_wall_top", (Holder)reference1), Integer.valueOf(1)), 
/* 117 */             Pair.of(StructurePoolElement.single("bastion/treasure/connectors/center_to_wall_top_entrance", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     Pools.register(paramBootstrapContext, "bastion/treasure/entrances", new StructureTemplatePool((Holder)reference6, 
/*     */           
/* 124 */           (List)ImmutableList.of(
/* 125 */             Pair.of(StructurePoolElement.single("bastion/treasure/entrances/entrance_0", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     Pools.register(paramBootstrapContext, "bastion/treasure/ramparts", new StructureTemplatePool((Holder)reference6, 
/*     */           
/* 132 */           (List)ImmutableList.of(
/* 133 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/mid_wall_main", (Holder)reference1), Integer.valueOf(1)), 
/* 134 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/mid_wall_side", (Holder)reference1), Integer.valueOf(1)), 
/* 135 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/bottom_wall_0", (Holder)reference3), Integer.valueOf(1)), 
/* 136 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/top_wall", (Holder)reference4), Integer.valueOf(1)), 
/* 137 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/lava_basin_side", (Holder)reference1), Integer.valueOf(1)), 
/* 138 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/lava_basin_main", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     Pools.register(paramBootstrapContext, "bastion/treasure/corners/bottom", new StructureTemplatePool((Holder)reference6, 
/*     */           
/* 145 */           (List)ImmutableList.of(
/* 146 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/bottom/corner_0", (Holder)reference1), Integer.valueOf(1)), 
/* 147 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/bottom/corner_1", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     Pools.register(paramBootstrapContext, "bastion/treasure/corners/edges", new StructureTemplatePool((Holder)reference6, 
/*     */           
/* 154 */           (List)ImmutableList.of(
/* 155 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/edges/bottom", (Holder)reference2), Integer.valueOf(1)), 
/* 156 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/edges/middle", (Holder)reference2), Integer.valueOf(1)), 
/* 157 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/edges/top", (Holder)reference2), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     Pools.register(paramBootstrapContext, "bastion/treasure/corners/middle", new StructureTemplatePool((Holder)reference6, 
/*     */           
/* 164 */           (List)ImmutableList.of(
/* 165 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/middle/corner_0", (Holder)reference1), Integer.valueOf(1)), 
/* 166 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/middle/corner_1", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     Pools.register(paramBootstrapContext, "bastion/treasure/corners/top", new StructureTemplatePool((Holder)reference6, 
/*     */           
/* 173 */           (List)ImmutableList.of(
/* 174 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/top/corner_0", (Holder)reference1), Integer.valueOf(1)), 
/* 175 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/top/corner_1", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     Pools.register(paramBootstrapContext, "bastion/treasure/extensions/large_pool", new StructureTemplatePool((Holder)reference6, 
/*     */           
/* 182 */           (List)ImmutableList.of(
/* 183 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/empty", (Holder)reference1), Integer.valueOf(1)), 
/* 184 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/empty", (Holder)reference1), Integer.valueOf(1)), 
/* 185 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/fire_room", (Holder)reference1), Integer.valueOf(1)), 
/* 186 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/large_bridge_0", (Holder)reference1), Integer.valueOf(1)), 
/* 187 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/large_bridge_1", (Holder)reference1), Integer.valueOf(1)), 
/* 188 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/large_bridge_2", (Holder)reference1), Integer.valueOf(1)), 
/* 189 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/large_bridge_3", (Holder)reference1), Integer.valueOf(1)), 
/* 190 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/roofed_bridge", (Holder)reference1), Integer.valueOf(1)), 
/* 191 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/empty", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 196 */     Pools.register(paramBootstrapContext, "bastion/treasure/extensions/small_pool", new StructureTemplatePool((Holder)reference6, 
/*     */           
/* 198 */           (List)ImmutableList.of(
/* 199 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/empty", (Holder)reference1), Integer.valueOf(1)), 
/* 200 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/fire_room", (Holder)reference1), Integer.valueOf(1)), 
/* 201 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/empty", (Holder)reference1), Integer.valueOf(1)), 
/* 202 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/small_bridge_0", (Holder)reference1), Integer.valueOf(1)), 
/* 203 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/small_bridge_1", (Holder)reference1), Integer.valueOf(1)), 
/* 204 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/small_bridge_2", (Holder)reference1), Integer.valueOf(1)), 
/* 205 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/small_bridge_3", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     Pools.register(paramBootstrapContext, "bastion/treasure/extensions/houses", new StructureTemplatePool((Holder)reference6, 
/*     */           
/* 212 */           (List)ImmutableList.of(
/* 213 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/house_0", (Holder)reference1), Integer.valueOf(1)), 
/* 214 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/house_1", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     Pools.register(paramBootstrapContext, "bastion/treasure/roofs", new StructureTemplatePool((Holder)reference6, 
/*     */           
/* 221 */           (List)ImmutableList.of(
/* 222 */             Pair.of(StructurePoolElement.single("bastion/treasure/roofs/wall_roof", (Holder)reference5), Integer.valueOf(1)), 
/* 223 */             Pair.of(StructurePoolElement.single("bastion/treasure/roofs/corner_roof", (Holder)reference5), Integer.valueOf(1)), 
/* 224 */             Pair.of(StructurePoolElement.single("bastion/treasure/roofs/center_roof", (Holder)reference5), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\BastionTreasureRoomPools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */