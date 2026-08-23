/*    */ package net.minecraft.world.level.levelgen.structure.pools.alias;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.data.worldgen.Pools;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.Weighted;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ 
/*    */ public interface PoolAliasBinding
/*    */ {
/* 20 */   public static final Codec<PoolAliasBinding> CODEC = BuiltInRegistries.POOL_ALIAS_BINDING_TYPE.byNameCodec().dispatch(PoolAliasBinding::codec, Function.identity());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static DirectPoolAlias direct(String paramString1, String paramString2) {
/* 30 */     return direct(Pools.createKey(paramString1), Pools.createKey(paramString2));
/*    */   }
/*    */   
/*    */   static DirectPoolAlias direct(ResourceKey<StructureTemplatePool> paramResourceKey1, ResourceKey<StructureTemplatePool> paramResourceKey2) {
/* 34 */     return new DirectPoolAlias(paramResourceKey1, paramResourceKey2);
/*    */   }
/*    */   
/*    */   static RandomPoolAlias random(String paramString, WeightedList<String> paramWeightedList) {
/* 38 */     WeightedList.Builder builder = WeightedList.builder();
/* 39 */     paramWeightedList.unwrap().forEach(paramWeighted -> paramBuilder.add(Pools.createKey((String)paramWeighted.value()), paramWeighted.weight()));
/*    */     
/* 41 */     return random(Pools.createKey(paramString), builder.build());
/*    */   }
/*    */   
/*    */   static RandomPoolAlias random(ResourceKey<StructureTemplatePool> paramResourceKey, WeightedList<ResourceKey<StructureTemplatePool>> paramWeightedList) {
/* 45 */     return new RandomPoolAlias(paramResourceKey, paramWeightedList);
/*    */   }
/*    */   
/*    */   static RandomGroupPoolAlias randomGroup(WeightedList<List<PoolAliasBinding>> paramWeightedList) {
/* 49 */     return new RandomGroupPoolAlias(paramWeightedList);
/*    */   }
/*    */   
/*    */   void forEachResolved(RandomSource paramRandomSource, BiConsumer<ResourceKey<StructureTemplatePool>, ResourceKey<StructureTemplatePool>> paramBiConsumer);
/*    */   
/*    */   Stream<ResourceKey<StructureTemplatePool>> allTargets();
/*    */   
/*    */   MapCodec<? extends PoolAliasBinding> codec();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\pools\alias\PoolAliasBinding.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */