/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class BiomeSources {
/*    */   public static MapCodec<? extends BiomeSource> bootstrap(Registry<MapCodec<? extends BiomeSource>> paramRegistry) {
/*  8 */     Registry.register(paramRegistry, "fixed", FixedBiomeSource.CODEC);
/*  9 */     Registry.register(paramRegistry, "multi_noise", MultiNoiseBiomeSource.CODEC);
/* 10 */     Registry.register(paramRegistry, "checkerboard", CheckerboardColumnBiomeSource.CODEC);
/* 11 */     return (MapCodec<? extends BiomeSource>)Registry.register(paramRegistry, "the_end", TheEndBiomeSource.CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\BiomeSources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */