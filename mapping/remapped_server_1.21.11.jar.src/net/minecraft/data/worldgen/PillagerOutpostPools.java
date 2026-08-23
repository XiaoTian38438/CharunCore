/*    */ package net.minecraft.data.worldgen;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public class PillagerOutpostPools {
/* 14 */   public static final ResourceKey<StructureTemplatePool> START = Pools.createKey("pillager_outpost/base_plates");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<StructureTemplatePool> paramBootstrapContext) {
/* 17 */     HolderGetter<?> holderGetter1 = paramBootstrapContext.lookup(Registries.PROCESSOR_LIST);
/* 18 */     Holder.Reference reference1 = holderGetter1.getOrThrow(ProcessorLists.OUTPOST_ROT);
/*    */     
/* 20 */     HolderGetter<?> holderGetter2 = paramBootstrapContext.lookup(Registries.TEMPLATE_POOL);
/* 21 */     Holder.Reference reference2 = holderGetter2.getOrThrow(Pools.EMPTY);
/*    */     
/* 23 */     paramBootstrapContext.register(START, new StructureTemplatePool((Holder)reference2, 
/*    */           
/* 25 */           (List)ImmutableList.of(
/* 26 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/base_plate"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 31 */     Pools.register(paramBootstrapContext, "pillager_outpost/towers", new StructureTemplatePool((Holder)reference2, 
/*    */           
/* 33 */           (List)ImmutableList.of(
/* 34 */             Pair.of(StructurePoolElement.list((List)ImmutableList.of(
/* 35 */                   StructurePoolElement.legacy("pillager_outpost/watchtower"), 
/* 36 */                   StructurePoolElement.legacy("pillager_outpost/watchtower_overgrown", (Holder)reference1))), 
/* 37 */               Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     Pools.register(paramBootstrapContext, "pillager_outpost/feature_plates", new StructureTemplatePool((Holder)reference2, 
/*    */           
/* 44 */           (List)ImmutableList.of(
/* 45 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_plate"), Integer.valueOf(1))), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 50 */     Pools.register(paramBootstrapContext, "pillager_outpost/features", new StructureTemplatePool((Holder)reference2, 
/*    */           
/* 52 */           (List)ImmutableList.of(
/* 53 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_cage1"), Integer.valueOf(1)), 
/* 54 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_cage2"), Integer.valueOf(1)), 
/* 55 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_cage_with_allays"), Integer.valueOf(1)), 
/* 56 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_logs"), Integer.valueOf(1)), 
/* 57 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_tent1"), Integer.valueOf(1)), 
/* 58 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_tent2"), Integer.valueOf(1)), 
/* 59 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_targets"), Integer.valueOf(1)), 
/* 60 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(6))), StructureTemplatePool.Projection.RIGID));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\PillagerOutpostPools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */