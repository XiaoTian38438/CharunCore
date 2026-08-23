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
/*    */ public class AncientCityStructurePieces {
/* 14 */   public static final ResourceKey<StructureTemplatePool> START = Pools.createKey("ancient_city/city_center");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<StructureTemplatePool> paramBootstrapContext) {
/* 17 */     HolderGetter<?> holderGetter1 = paramBootstrapContext.lookup(Registries.PROCESSOR_LIST);
/* 18 */     Holder.Reference reference1 = holderGetter1.getOrThrow(ProcessorLists.ANCIENT_CITY_START_DEGRADATION);
/*    */     
/* 20 */     HolderGetter<?> holderGetter2 = paramBootstrapContext.lookup(Registries.TEMPLATE_POOL);
/* 21 */     Holder.Reference reference2 = holderGetter2.getOrThrow(Pools.EMPTY);
/*    */     
/* 23 */     paramBootstrapContext.register(START, new StructureTemplatePool((Holder)reference2, 
/*    */           
/* 25 */           (List)ImmutableList.of(
/* 26 */             Pair.of(StructurePoolElement.single("ancient_city/city_center/city_center_1", (Holder)reference1), Integer.valueOf(1)), 
/* 27 */             Pair.of(StructurePoolElement.single("ancient_city/city_center/city_center_2", (Holder)reference1), Integer.valueOf(1)), 
/* 28 */             Pair.of(StructurePoolElement.single("ancient_city/city_center/city_center_3", (Holder)reference1), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 33 */     AncientCityStructurePools.bootstrap(paramBootstrapContext);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\AncientCityStructurePieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */