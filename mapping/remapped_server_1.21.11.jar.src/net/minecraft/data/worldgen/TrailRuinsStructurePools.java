/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ 
/*     */ 
/*     */ public class TrailRuinsStructurePools
/*     */ {
/*  15 */   public static final ResourceKey<StructureTemplatePool> START = Pools.createKey("trail_ruins/tower");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<StructureTemplatePool> paramBootstrapContext) {
/*  18 */     HolderGetter<?> holderGetter1 = paramBootstrapContext.lookup(Registries.TEMPLATE_POOL);
/*  19 */     Holder.Reference reference1 = holderGetter1.getOrThrow(Pools.EMPTY);
/*     */     
/*  21 */     HolderGetter<?> holderGetter2 = paramBootstrapContext.lookup(Registries.PROCESSOR_LIST);
/*  22 */     Holder.Reference reference2 = holderGetter2.getOrThrow(ProcessorLists.TRAIL_RUINS_HOUSES_ARCHAEOLOGY);
/*  23 */     Holder.Reference reference3 = holderGetter2.getOrThrow(ProcessorLists.TRAIL_RUINS_ROADS_ARCHAEOLOGY);
/*  24 */     Holder.Reference reference4 = holderGetter2.getOrThrow(ProcessorLists.TRAIL_RUINS_TOWER_TOP_ARCHAEOLOGY);
/*     */     
/*  26 */     paramBootstrapContext.register(START, new StructureTemplatePool((Holder)reference1, 
/*     */           
/*  28 */           List.of(
/*  29 */             Pair.of(StructurePoolElement.single("trail_ruins/tower/tower_1", (Holder)reference2), Integer.valueOf(1)), 
/*  30 */             Pair.of(StructurePoolElement.single("trail_ruins/tower/tower_2", (Holder)reference2), Integer.valueOf(1)), 
/*  31 */             Pair.of(StructurePoolElement.single("trail_ruins/tower/tower_3", (Holder)reference2), Integer.valueOf(1)), 
/*  32 */             Pair.of(StructurePoolElement.single("trail_ruins/tower/tower_4", (Holder)reference2), Integer.valueOf(1)), 
/*  33 */             Pair.of(StructurePoolElement.single("trail_ruins/tower/tower_5", (Holder)reference2), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  38 */     Pools.register(paramBootstrapContext, "trail_ruins/tower/tower_top", new StructureTemplatePool((Holder)reference1, 
/*     */           
/*  40 */           List.of(
/*  41 */             Pair.of(StructurePoolElement.single("trail_ruins/tower/tower_top_1", (Holder)reference4), Integer.valueOf(1)), 
/*  42 */             Pair.of(StructurePoolElement.single("trail_ruins/tower/tower_top_2", (Holder)reference4), Integer.valueOf(1)), 
/*  43 */             Pair.of(StructurePoolElement.single("trail_ruins/tower/tower_top_3", (Holder)reference4), Integer.valueOf(1)), 
/*  44 */             Pair.of(StructurePoolElement.single("trail_ruins/tower/tower_top_4", (Holder)reference4), Integer.valueOf(1)), 
/*  45 */             Pair.of(StructurePoolElement.single("trail_ruins/tower/tower_top_5", (Holder)reference4), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  50 */     Pools.register(paramBootstrapContext, "trail_ruins/tower/additions", new StructureTemplatePool((Holder)reference1, 
/*     */           
/*  52 */           List.of(new Pair[] { 
/*  53 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/hall_1", (Holder)reference2), Integer.valueOf(1)), 
/*  54 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/hall_2", (Holder)reference2), Integer.valueOf(1)), 
/*  55 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/hall_3", (Holder)reference2), Integer.valueOf(1)), 
/*  56 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/hall_4", (Holder)reference2), Integer.valueOf(1)), 
/*  57 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/hall_5", (Holder)reference2), Integer.valueOf(1)), 
/*  58 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/large_hall_1", (Holder)reference2), Integer.valueOf(1)), 
/*  59 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/large_hall_2", (Holder)reference2), Integer.valueOf(1)), 
/*  60 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/large_hall_3", (Holder)reference2), Integer.valueOf(1)), 
/*  61 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/large_hall_4", (Holder)reference2), Integer.valueOf(1)), 
/*  62 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/large_hall_5", (Holder)reference2), Integer.valueOf(1)), 
/*  63 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/one_room_1", (Holder)reference2), Integer.valueOf(1)), 
/*  64 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/one_room_2", (Holder)reference2), Integer.valueOf(1)), 
/*  65 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/one_room_3", (Holder)reference2), Integer.valueOf(1)), 
/*  66 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/one_room_4", (Holder)reference2), Integer.valueOf(1)), 
/*  67 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/one_room_5", (Holder)reference2), Integer.valueOf(1)), 
/*  68 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/platform_1", (Holder)reference2), Integer.valueOf(1)), 
/*  69 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/platform_2", (Holder)reference2), Integer.valueOf(1)), 
/*  70 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/platform_3", (Holder)reference2), Integer.valueOf(1)), 
/*  71 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/platform_4", (Holder)reference2), Integer.valueOf(1)), 
/*  72 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/platform_5", (Holder)reference2), Integer.valueOf(1)), 
/*  73 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/stable_1", (Holder)reference2), Integer.valueOf(1)), 
/*  74 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/stable_2", (Holder)reference2), Integer.valueOf(1)), 
/*  75 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/stable_3", (Holder)reference2), Integer.valueOf(1)), 
/*  76 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/stable_4", (Holder)reference2), Integer.valueOf(1)), 
/*  77 */               Pair.of(StructurePoolElement.single("trail_ruins/tower/stable_5", (Holder)reference2), Integer.valueOf(1)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     Pools.register(paramBootstrapContext, "trail_ruins/roads", new StructureTemplatePool((Holder)reference1, 
/*     */           
/*  85 */           List.of(
/*  86 */             Pair.of(StructurePoolElement.single("trail_ruins/roads/long_road_end", (Holder)reference3), Integer.valueOf(1)), 
/*  87 */             Pair.of(StructurePoolElement.single("trail_ruins/roads/road_end_1", (Holder)reference3), Integer.valueOf(1)), 
/*  88 */             Pair.of(StructurePoolElement.single("trail_ruins/roads/road_section_1", (Holder)reference3), Integer.valueOf(1)), 
/*  89 */             Pair.of(StructurePoolElement.single("trail_ruins/roads/road_section_2", (Holder)reference3), Integer.valueOf(1)), 
/*  90 */             Pair.of(StructurePoolElement.single("trail_ruins/roads/road_section_3", (Holder)reference3), Integer.valueOf(1)), 
/*  91 */             Pair.of(StructurePoolElement.single("trail_ruins/roads/road_section_4", (Holder)reference3), Integer.valueOf(1)), 
/*  92 */             Pair.of(StructurePoolElement.single("trail_ruins/roads/road_spacer_1", (Holder)reference3), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     Pools.register(paramBootstrapContext, "trail_ruins/buildings", new StructureTemplatePool((Holder)reference1, 
/*     */           
/*  99 */           List.of(new Pair[] {
/*     */               
/* 101 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_hall_1", (Holder)reference2), Integer.valueOf(1)), 
/* 102 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_hall_2", (Holder)reference2), Integer.valueOf(1)), 
/* 103 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_hall_3", (Holder)reference2), Integer.valueOf(1)), 
/* 104 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_hall_4", (Holder)reference2), Integer.valueOf(1)), 
/* 105 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_hall_5", (Holder)reference2), Integer.valueOf(1)), 
/*     */               
/* 107 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/large_room_1", (Holder)reference2), Integer.valueOf(1)), 
/* 108 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/large_room_2", (Holder)reference2), Integer.valueOf(1)), 
/* 109 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/large_room_3", (Holder)reference2), Integer.valueOf(1)), 
/* 110 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/large_room_4", (Holder)reference2), Integer.valueOf(1)), 
/* 111 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/large_room_5", (Holder)reference2), Integer.valueOf(1)), 
/* 112 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/one_room_1", (Holder)reference2), Integer.valueOf(1)), 
/* 113 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/one_room_2", (Holder)reference2), Integer.valueOf(1)), 
/* 114 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/one_room_3", (Holder)reference2), Integer.valueOf(1)), 
/* 115 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/one_room_4", (Holder)reference2), Integer.valueOf(1)), 
/* 116 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/one_room_5", (Holder)reference2), Integer.valueOf(1))
/*     */             }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */     
/* 121 */     Pools.register(paramBootstrapContext, "trail_ruins/buildings/grouped", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 123 */           List.of(new Pair[] { 
/* 124 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_full_1", (Holder)reference2), Integer.valueOf(1)), 
/* 125 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_full_2", (Holder)reference2), Integer.valueOf(1)), 
/* 126 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_full_3", (Holder)reference2), Integer.valueOf(1)), 
/* 127 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_full_4", (Holder)reference2), Integer.valueOf(1)), 
/* 128 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_full_5", (Holder)reference2), Integer.valueOf(1)), 
/* 129 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_lower_1", (Holder)reference2), Integer.valueOf(1)), 
/* 130 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_lower_2", (Holder)reference2), Integer.valueOf(1)), 
/* 131 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_lower_3", (Holder)reference2), Integer.valueOf(1)), 
/* 132 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_lower_4", (Holder)reference2), Integer.valueOf(1)), 
/* 133 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_lower_5", (Holder)reference2), Integer.valueOf(1)), 
/* 134 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_upper_1", (Holder)reference2), Integer.valueOf(1)), 
/* 135 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_upper_2", (Holder)reference2), Integer.valueOf(1)), 
/* 136 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_upper_3", (Holder)reference2), Integer.valueOf(1)), 
/* 137 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_upper_4", (Holder)reference2), Integer.valueOf(1)), 
/* 138 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_upper_5", (Holder)reference2), Integer.valueOf(1)), 
/* 139 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_room_1", (Holder)reference2), Integer.valueOf(1)), 
/* 140 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_room_2", (Holder)reference2), Integer.valueOf(1)), 
/* 141 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_room_3", (Holder)reference2), Integer.valueOf(1)), 
/* 142 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_room_4", (Holder)reference2), Integer.valueOf(1)), 
/* 143 */               Pair.of(StructurePoolElement.single("trail_ruins/buildings/group_room_5", (Holder)reference2), Integer.valueOf(1)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     Pools.register(paramBootstrapContext, "trail_ruins/decor", new StructureTemplatePool((Holder)reference1, 
/*     */           
/* 151 */           List.of(
/* 152 */             Pair.of(StructurePoolElement.single("trail_ruins/decor/decor_1", (Holder)reference2), Integer.valueOf(1)), 
/* 153 */             Pair.of(StructurePoolElement.single("trail_ruins/decor/decor_2", (Holder)reference2), Integer.valueOf(1)), 
/* 154 */             Pair.of(StructurePoolElement.single("trail_ruins/decor/decor_3", (Holder)reference2), Integer.valueOf(1)), 
/* 155 */             Pair.of(StructurePoolElement.single("trail_ruins/decor/decor_4", (Holder)reference2), Integer.valueOf(1)), 
/* 156 */             Pair.of(StructurePoolElement.single("trail_ruins/decor/decor_5", (Holder)reference2), Integer.valueOf(1)), 
/* 157 */             Pair.of(StructurePoolElement.single("trail_ruins/decor/decor_6", (Holder)reference2), Integer.valueOf(1)), 
/* 158 */             Pair.of(StructurePoolElement.single("trail_ruins/decor/decor_7", (Holder)reference2), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\TrailRuinsStructurePools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */