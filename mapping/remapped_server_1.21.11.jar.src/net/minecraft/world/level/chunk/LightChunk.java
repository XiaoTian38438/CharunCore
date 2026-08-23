package net.minecraft.world.level.chunk;

import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.ChunkSkyLightSources;

public interface LightChunk extends BlockGetter {
  void findBlockLightSources(BiConsumer<BlockPos, BlockState> paramBiConsumer);
  
  ChunkSkyLightSources getSkyLightSources();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\LightChunk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */