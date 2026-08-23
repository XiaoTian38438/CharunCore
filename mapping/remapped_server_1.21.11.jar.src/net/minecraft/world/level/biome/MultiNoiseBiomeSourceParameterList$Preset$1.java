/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements MultiNoiseBiomeSourceParameterList.Preset.SourceProvider
/*    */ {
/*    */   public <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> paramFunction) {
/* 62 */     return new Climate.ParameterList<>(List.of(
/* 63 */           Pair.of(Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), paramFunction.apply(Biomes.NETHER_WASTES)), 
/* 64 */           Pair.of(Climate.parameters(0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), paramFunction.apply(Biomes.SOUL_SAND_VALLEY)), 
/* 65 */           Pair.of(Climate.parameters(0.4F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), paramFunction.apply(Biomes.CRIMSON_FOREST)), 
/* 66 */           Pair.of(Climate.parameters(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.375F), paramFunction.apply(Biomes.WARPED_FOREST)), 
/* 67 */           Pair.of(Climate.parameters(-0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.175F), paramFunction.apply(Biomes.BASALT_DELTAS))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\MultiNoiseBiomeSourceParameterList$Preset$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */