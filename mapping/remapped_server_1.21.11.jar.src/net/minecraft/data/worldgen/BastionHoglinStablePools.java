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
/*     */ public class BastionHoglinStablePools {
/*     */   public static void bootstrap(BootstrapContext<StructureTemplatePool> paramBootstrapContext) {
/*  14 */     HolderGetter<?> holderGetter1 = paramBootstrapContext.lookup(Registries.PROCESSOR_LIST);
/*  15 */     Holder.Reference reference1 = holderGetter1.getOrThrow(ProcessorLists.STABLE_DEGRADATION);
/*  16 */     Holder.Reference reference2 = holderGetter1.getOrThrow(ProcessorLists.SIDE_WALL_DEGRADATION);
/*     */     
/*  18 */     HolderGetter<?> holderGetter2 = paramBootstrapContext.lookup(Registries.TEMPLATE_POOL);
/*  19 */     Holder.Reference reference3 = holderGetter2.getOrThrow(Pools.EMPTY);
/*     */     
/*  21 */     Pools.register(paramBootstrapContext, "bastion/hoglin_stable/starting_pieces", new StructureTemplatePool((Holder)reference3, 
/*     */           
/*  23 */           (List)ImmutableList.of(
/*  24 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/starting_stairs_0", (Holder)reference1), Integer.valueOf(1)), 
/*  25 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/starting_stairs_1", (Holder)reference1), Integer.valueOf(1)), 
/*  26 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/starting_stairs_2", (Holder)reference1), Integer.valueOf(1)), 
/*  27 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/starting_stairs_3", (Holder)reference1), Integer.valueOf(1)), 
/*  28 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/starting_stairs_4", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  33 */     Pools.register(paramBootstrapContext, "bastion/hoglin_stable/mirrored_starting_pieces", new StructureTemplatePool((Holder)reference3, 
/*     */           
/*  35 */           (List)ImmutableList.of(
/*  36 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/stairs_0_mirrored", (Holder)reference1), Integer.valueOf(1)), 
/*  37 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/stairs_1_mirrored", (Holder)reference1), Integer.valueOf(1)), 
/*  38 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/stairs_2_mirrored", (Holder)reference1), Integer.valueOf(1)), 
/*  39 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/stairs_3_mirrored", (Holder)reference1), Integer.valueOf(1)), 
/*  40 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/stairs_4_mirrored", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  45 */     Pools.register(paramBootstrapContext, "bastion/hoglin_stable/wall_bases", new StructureTemplatePool((Holder)reference3, 
/*     */           
/*  47 */           (List)ImmutableList.of(
/*  48 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/walls/wall_base", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     Pools.register(paramBootstrapContext, "bastion/hoglin_stable/walls", new StructureTemplatePool((Holder)reference3, 
/*     */           
/*  55 */           (List)ImmutableList.of(
/*  56 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/walls/side_wall_0", (Holder)reference2), Integer.valueOf(1)), 
/*  57 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/walls/side_wall_1", (Holder)reference2), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  62 */     Pools.register(paramBootstrapContext, "bastion/hoglin_stable/stairs", new StructureTemplatePool((Holder)reference3, 
/*     */           
/*  64 */           (List)ImmutableList.of(
/*  65 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_1_0", (Holder)reference1), Integer.valueOf(1)), 
/*  66 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_1_1", (Holder)reference1), Integer.valueOf(1)), 
/*  67 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_1_2", (Holder)reference1), Integer.valueOf(1)), 
/*  68 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_1_3", (Holder)reference1), Integer.valueOf(1)), 
/*  69 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_1_4", (Holder)reference1), Integer.valueOf(1)), 
/*  70 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_2_0", (Holder)reference1), Integer.valueOf(1)), 
/*  71 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_2_1", (Holder)reference1), Integer.valueOf(1)), 
/*  72 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_2_2", (Holder)reference1), Integer.valueOf(1)), 
/*  73 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_2_3", (Holder)reference1), Integer.valueOf(1)), 
/*  74 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_2_4", (Holder)reference1), Integer.valueOf(1)), 
/*  75 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_3_0", (Holder)reference1), Integer.valueOf(1)), 
/*  76 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_3_1", (Holder)reference1), Integer.valueOf(1)), (Object[])new Pair[] {
/*  77 */               Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_3_2", (Holder)reference1), Integer.valueOf(1)), 
/*  78 */               Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_3_3", (Holder)reference1), Integer.valueOf(1)), 
/*  79 */               Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_3_4", (Holder)reference1), Integer.valueOf(1))
/*     */             }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */     
/*  84 */     Pools.register(paramBootstrapContext, "bastion/hoglin_stable/small_stables/inner", new StructureTemplatePool((Holder)reference3, 
/*     */           
/*  86 */           (List)ImmutableList.of(
/*  87 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/small_stables/inner_0", (Holder)reference1), Integer.valueOf(1)), 
/*  88 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/small_stables/inner_1", (Holder)reference1), Integer.valueOf(1)), 
/*  89 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/small_stables/inner_2", (Holder)reference1), Integer.valueOf(1)), 
/*  90 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/small_stables/inner_3", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     Pools.register(paramBootstrapContext, "bastion/hoglin_stable/small_stables/outer", new StructureTemplatePool((Holder)reference3, 
/*     */           
/*  97 */           (List)ImmutableList.of(
/*  98 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/small_stables/outer_0", (Holder)reference1), Integer.valueOf(1)), 
/*  99 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/small_stables/outer_1", (Holder)reference1), Integer.valueOf(1)), 
/* 100 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/small_stables/outer_2", (Holder)reference1), Integer.valueOf(1)), 
/* 101 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/small_stables/outer_3", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     Pools.register(paramBootstrapContext, "bastion/hoglin_stable/large_stables/inner", new StructureTemplatePool((Holder)reference3, 
/*     */           
/* 108 */           (List)ImmutableList.of(
/* 109 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/inner_0", (Holder)reference1), Integer.valueOf(1)), 
/* 110 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/inner_1", (Holder)reference1), Integer.valueOf(1)), 
/* 111 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/inner_2", (Holder)reference1), Integer.valueOf(1)), 
/* 112 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/inner_3", (Holder)reference1), Integer.valueOf(1)), 
/* 113 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/inner_4", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     Pools.register(paramBootstrapContext, "bastion/hoglin_stable/large_stables/outer", new StructureTemplatePool((Holder)reference3, 
/*     */           
/* 120 */           (List)ImmutableList.of(
/* 121 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/outer_0", (Holder)reference1), Integer.valueOf(1)), 
/* 122 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/outer_1", (Holder)reference1), Integer.valueOf(1)), 
/* 123 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/outer_2", (Holder)reference1), Integer.valueOf(1)), 
/* 124 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/outer_3", (Holder)reference1), Integer.valueOf(1)), 
/* 125 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/outer_4", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     Pools.register(paramBootstrapContext, "bastion/hoglin_stable/posts", new StructureTemplatePool((Holder)reference3, 
/*     */           
/* 132 */           (List)ImmutableList.of(
/* 133 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/posts/stair_post", (Holder)reference1), Integer.valueOf(1)), 
/* 134 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/posts/end_post", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     Pools.register(paramBootstrapContext, "bastion/hoglin_stable/ramparts", new StructureTemplatePool((Holder)reference3, 
/*     */           
/* 141 */           (List)ImmutableList.of(
/* 142 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/ramparts/ramparts_1", (Holder)reference1), Integer.valueOf(1)), 
/* 143 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/ramparts/ramparts_2", (Holder)reference1), Integer.valueOf(1)), 
/* 144 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/ramparts/ramparts_3", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     Pools.register(paramBootstrapContext, "bastion/hoglin_stable/rampart_plates", new StructureTemplatePool((Holder)reference3, 
/*     */           
/* 151 */           (List)ImmutableList.of(
/* 152 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/rampart_plates/rampart_plate_1", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     Pools.register(paramBootstrapContext, "bastion/hoglin_stable/connectors", new StructureTemplatePool((Holder)reference3, 
/*     */           
/* 159 */           (List)ImmutableList.of(
/* 160 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/connectors/end_post_connector", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\BastionHoglinStablePools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */