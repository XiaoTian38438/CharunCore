/*    */ package net.minecraft.world.level.levelgen.structure.pools.alias;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface PoolAliasLookup {
/*    */   public static final PoolAliasLookup EMPTY;
/*    */   
/*    */   static {
/* 15 */     EMPTY = (paramResourceKey -> paramResourceKey);
/*    */   }
/*    */ 
/*    */   
/*    */   static PoolAliasLookup create(List<PoolAliasBinding> paramList, BlockPos paramBlockPos, long paramLong) {
/* 20 */     if (paramList.isEmpty()) {
/* 21 */       return EMPTY;
/*    */     }
/*    */     
/* 24 */     RandomSource randomSource = RandomSource.create(paramLong).forkPositional().at(paramBlockPos);
/* 25 */     ImmutableMap.Builder builder = ImmutableMap.builder();
/* 26 */     paramList.forEach(paramPoolAliasBinding -> { Objects.requireNonNull(paramBuilder); paramPoolAliasBinding.forEachResolved(paramRandomSource, paramBuilder::put);
/* 27 */         }); ImmutableMap immutableMap = builder.build();
/*    */     
/* 29 */     return paramResourceKey -> (ResourceKey)Objects.<ResourceKey>requireNonNull((ResourceKey)paramMap.getOrDefault(paramResourceKey, paramResourceKey), ());
/*    */   }
/*    */   
/*    */   ResourceKey<StructureTemplatePool> lookup(ResourceKey<StructureTemplatePool> paramResourceKey);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\pools\alias\PoolAliasLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */