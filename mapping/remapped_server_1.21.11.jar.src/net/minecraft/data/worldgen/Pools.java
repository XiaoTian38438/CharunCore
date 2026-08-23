/*    */ package net.minecraft.data.worldgen;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public class Pools {
/* 12 */   public static final ResourceKey<StructureTemplatePool> EMPTY = createKey("empty");
/*    */   
/*    */   public static ResourceKey<StructureTemplatePool> createKey(Identifier paramIdentifier) {
/* 15 */     return ResourceKey.create(Registries.TEMPLATE_POOL, paramIdentifier);
/*    */   }
/*    */   
/*    */   public static ResourceKey<StructureTemplatePool> createKey(String paramString) {
/* 19 */     return createKey(Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   public static ResourceKey<StructureTemplatePool> parseKey(String paramString) {
/* 23 */     return createKey(Identifier.parse(paramString));
/*    */   }
/*    */   
/*    */   public static void register(BootstrapContext<StructureTemplatePool> paramBootstrapContext, String paramString, StructureTemplatePool paramStructureTemplatePool) {
/* 27 */     paramBootstrapContext.register(createKey(paramString), paramStructureTemplatePool);
/*    */   }
/*    */   
/*    */   public static void bootstrap(BootstrapContext<StructureTemplatePool> paramBootstrapContext) {
/* 31 */     HolderGetter<?> holderGetter = paramBootstrapContext.lookup(Registries.TEMPLATE_POOL);
/* 32 */     Holder.Reference reference = holderGetter.getOrThrow(EMPTY);
/*    */ 
/*    */     
/* 35 */     paramBootstrapContext.register(EMPTY, new StructureTemplatePool((Holder)reference, (List)ImmutableList.of(), StructureTemplatePool.Projection.RIGID));
/*    */     
/* 37 */     BastionPieces.bootstrap(paramBootstrapContext);
/* 38 */     PillagerOutpostPools.bootstrap(paramBootstrapContext);
/* 39 */     VillagePools.bootstrap(paramBootstrapContext);
/* 40 */     AncientCityStructurePieces.bootstrap(paramBootstrapContext);
/* 41 */     TrailRuinsStructurePools.bootstrap(paramBootstrapContext);
/* 42 */     TrialChambersStructurePools.bootstrap(paramBootstrapContext);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\Pools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */