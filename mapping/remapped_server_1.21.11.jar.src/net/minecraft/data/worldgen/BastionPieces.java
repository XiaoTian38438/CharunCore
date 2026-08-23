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
/*    */ public class BastionPieces {
/* 14 */   public static final ResourceKey<StructureTemplatePool> START = Pools.createKey("bastion/starts");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<StructureTemplatePool> paramBootstrapContext) {
/* 17 */     HolderGetter<?> holderGetter1 = paramBootstrapContext.lookup(Registries.PROCESSOR_LIST);
/* 18 */     Holder.Reference reference1 = holderGetter1.getOrThrow(ProcessorLists.BASTION_GENERIC_DEGRADATION);
/*    */     
/* 20 */     HolderGetter<?> holderGetter2 = paramBootstrapContext.lookup(Registries.TEMPLATE_POOL);
/* 21 */     Holder.Reference reference2 = holderGetter2.getOrThrow(Pools.EMPTY);
/*    */     
/* 23 */     paramBootstrapContext.register(START, new StructureTemplatePool((Holder)reference2, 
/*    */           
/* 25 */           (List)ImmutableList.of(
/* 26 */             Pair.of(StructurePoolElement.single("bastion/units/air_base", (Holder)reference1), Integer.valueOf(1)), 
/* 27 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/air_base", (Holder)reference1), Integer.valueOf(1)), 
/* 28 */             Pair.of(StructurePoolElement.single("bastion/treasure/big_air_full", (Holder)reference1), Integer.valueOf(1)), 
/* 29 */             Pair.of(StructurePoolElement.single("bastion/bridge/starting_pieces/entrance_base", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 34 */     BastionHousingUnitsPools.bootstrap(paramBootstrapContext);
/* 35 */     BastionHoglinStablePools.bootstrap(paramBootstrapContext);
/* 36 */     BastionTreasureRoomPools.bootstrap(paramBootstrapContext);
/* 37 */     BastionBridgePools.bootstrap(paramBootstrapContext);
/* 38 */     BastionSharedPools.bootstrap(paramBootstrapContext);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\BastionPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */