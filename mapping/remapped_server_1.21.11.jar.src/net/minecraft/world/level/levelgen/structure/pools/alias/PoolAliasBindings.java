/*    */ package net.minecraft.world.level.levelgen.structure.pools.alias;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.data.worldgen.Pools;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public class PoolAliasBindings {
/*    */   public static MapCodec<? extends PoolAliasBinding> bootstrap(Registry<MapCodec<? extends PoolAliasBinding>> paramRegistry) {
/* 16 */     Registry.register(paramRegistry, "random", RandomPoolAlias.CODEC);
/* 17 */     Registry.register(paramRegistry, "random_group", RandomGroupPoolAlias.CODEC);
/* 18 */     return (MapCodec<? extends PoolAliasBinding>)Registry.register(paramRegistry, "direct", DirectPoolAlias.CODEC);
/*    */   }
/*    */   
/*    */   public static void registerTargetsAsPools(BootstrapContext<StructureTemplatePool> paramBootstrapContext, Holder<StructureTemplatePool> paramHolder, List<PoolAliasBinding> paramList) {
/* 22 */     paramList.stream()
/* 23 */       .flatMap(PoolAliasBinding::allTargets)
/* 24 */       .map(paramResourceKey -> paramResourceKey.identifier().getPath())
/* 25 */       .forEach(paramString -> Pools.register(paramBootstrapContext, paramString, new StructureTemplatePool(paramHolder, List.of(Pair.of(StructurePoolElement.single(paramString), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\pools\alias\PoolAliasBindings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */