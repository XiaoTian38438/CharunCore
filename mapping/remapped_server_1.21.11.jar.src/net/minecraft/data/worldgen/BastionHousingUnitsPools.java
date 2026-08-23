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
/*     */ public class BastionHousingUnitsPools {
/*     */   public static void bootstrap(BootstrapContext<StructureTemplatePool> paramBootstrapContext) {
/*  14 */     HolderGetter<?> holderGetter1 = paramBootstrapContext.lookup(Registries.PROCESSOR_LIST);
/*  15 */     Holder.Reference reference1 = holderGetter1.getOrThrow(ProcessorLists.HOUSING);
/*     */     
/*  17 */     HolderGetter<?> holderGetter2 = paramBootstrapContext.lookup(Registries.TEMPLATE_POOL);
/*  18 */     Holder.Reference reference2 = holderGetter2.getOrThrow(Pools.EMPTY);
/*     */     
/*  20 */     Pools.register(paramBootstrapContext, "bastion/units/center_pieces", new StructureTemplatePool((Holder)reference2, 
/*     */           
/*  22 */           (List)ImmutableList.of(
/*  23 */             Pair.of(StructurePoolElement.single("bastion/units/center_pieces/center_0", (Holder)reference1), Integer.valueOf(1)), 
/*  24 */             Pair.of(StructurePoolElement.single("bastion/units/center_pieces/center_1", (Holder)reference1), Integer.valueOf(1)), 
/*  25 */             Pair.of(StructurePoolElement.single("bastion/units/center_pieces/center_2", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  30 */     Pools.register(paramBootstrapContext, "bastion/units/pathways", new StructureTemplatePool((Holder)reference2, 
/*     */           
/*  32 */           (List)ImmutableList.of(
/*  33 */             Pair.of(StructurePoolElement.single("bastion/units/pathways/pathway_0", (Holder)reference1), Integer.valueOf(1)), 
/*  34 */             Pair.of(StructurePoolElement.single("bastion/units/pathways/pathway_wall_0", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  39 */     Pools.register(paramBootstrapContext, "bastion/units/walls/wall_bases", new StructureTemplatePool((Holder)reference2, 
/*     */           
/*  41 */           (List)ImmutableList.of(
/*  42 */             Pair.of(StructurePoolElement.single("bastion/units/walls/wall_base", (Holder)reference1), Integer.valueOf(1)), 
/*  43 */             Pair.of(StructurePoolElement.single("bastion/units/walls/connected_wall", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  48 */     Pools.register(paramBootstrapContext, "bastion/units/stages/stage_0", new StructureTemplatePool((Holder)reference2, 
/*     */           
/*  50 */           (List)ImmutableList.of(
/*  51 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_0_0", (Holder)reference1), Integer.valueOf(1)), 
/*  52 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_0_1", (Holder)reference1), Integer.valueOf(1)), 
/*  53 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_0_2", (Holder)reference1), Integer.valueOf(1)), 
/*  54 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_0_3", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     Pools.register(paramBootstrapContext, "bastion/units/stages/stage_1", new StructureTemplatePool((Holder)reference2, 
/*     */           
/*  61 */           (List)ImmutableList.of(
/*  62 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_1_0", (Holder)reference1), Integer.valueOf(1)), 
/*  63 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_1_1", (Holder)reference1), Integer.valueOf(1)), 
/*  64 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_1_2", (Holder)reference1), Integer.valueOf(1)), 
/*  65 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_1_3", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     Pools.register(paramBootstrapContext, "bastion/units/stages/rot/stage_1", new StructureTemplatePool((Holder)reference2, 
/*     */           
/*  73 */           (List)ImmutableList.of(
/*  74 */             Pair.of(StructurePoolElement.single("bastion/units/stages/rot/stage_1_0", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     Pools.register(paramBootstrapContext, "bastion/units/stages/stage_2", new StructureTemplatePool((Holder)reference2, 
/*     */           
/*  81 */           (List)ImmutableList.of(
/*  82 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_2_0", (Holder)reference1), Integer.valueOf(1)), 
/*  83 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_2_1", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     Pools.register(paramBootstrapContext, "bastion/units/stages/stage_3", new StructureTemplatePool((Holder)reference2, 
/*     */           
/*  90 */           (List)ImmutableList.of(
/*  91 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_3_0", (Holder)reference1), Integer.valueOf(1)), 
/*  92 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_3_1", (Holder)reference1), Integer.valueOf(1)), 
/*  93 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_3_2", (Holder)reference1), Integer.valueOf(1)), 
/*  94 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_3_3", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     Pools.register(paramBootstrapContext, "bastion/units/fillers/stage_0", new StructureTemplatePool((Holder)reference2, 
/*     */           
/* 101 */           (List)ImmutableList.of(
/* 102 */             Pair.of(StructurePoolElement.single("bastion/units/fillers/stage_0", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     Pools.register(paramBootstrapContext, "bastion/units/edges", new StructureTemplatePool((Holder)reference2, 
/*     */           
/* 109 */           (List)ImmutableList.of(
/* 110 */             Pair.of(StructurePoolElement.single("bastion/units/edges/edge_0", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     Pools.register(paramBootstrapContext, "bastion/units/wall_units", new StructureTemplatePool((Holder)reference2, 
/*     */           
/* 117 */           (List)ImmutableList.of(
/* 118 */             Pair.of(StructurePoolElement.single("bastion/units/wall_units/unit_0", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     Pools.register(paramBootstrapContext, "bastion/units/edge_wall_units", new StructureTemplatePool((Holder)reference2, 
/*     */           
/* 125 */           (List)ImmutableList.of(
/* 126 */             Pair.of(StructurePoolElement.single("bastion/units/wall_units/edge_0_large", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     Pools.register(paramBootstrapContext, "bastion/units/ramparts", new StructureTemplatePool((Holder)reference2, 
/*     */           
/* 133 */           (List)ImmutableList.of(
/* 134 */             Pair.of(StructurePoolElement.single("bastion/units/ramparts/ramparts_0", (Holder)reference1), Integer.valueOf(1)), 
/* 135 */             Pair.of(StructurePoolElement.single("bastion/units/ramparts/ramparts_1", (Holder)reference1), Integer.valueOf(1)), 
/* 136 */             Pair.of(StructurePoolElement.single("bastion/units/ramparts/ramparts_2", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     Pools.register(paramBootstrapContext, "bastion/units/large_ramparts", new StructureTemplatePool((Holder)reference2, 
/*     */           
/* 143 */           (List)ImmutableList.of(
/* 144 */             Pair.of(StructurePoolElement.single("bastion/units/ramparts/ramparts_0", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     Pools.register(paramBootstrapContext, "bastion/units/rampart_plates", new StructureTemplatePool((Holder)reference2, 
/*     */           
/* 151 */           (List)ImmutableList.of(
/* 152 */             Pair.of(StructurePoolElement.single("bastion/units/rampart_plates/plate_0", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\BastionHousingUnitsPools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */