/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.level.levelgen.DebugLevelSource;
/*    */ import net.minecraft.world.level.levelgen.FlatLevelSource;
/*    */ import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
/*    */ 
/*    */ public class ChunkGenerators {
/*    */   public static MapCodec<? extends ChunkGenerator> bootstrap(Registry<MapCodec<? extends ChunkGenerator>> paramRegistry) {
/* 11 */     Registry.register(paramRegistry, "noise", NoiseBasedChunkGenerator.CODEC);
/* 12 */     Registry.register(paramRegistry, "flat", FlatLevelSource.CODEC);
/* 13 */     return (MapCodec<? extends ChunkGenerator>)Registry.register(paramRegistry, "debug", DebugLevelSource.CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\ChunkGenerators.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */