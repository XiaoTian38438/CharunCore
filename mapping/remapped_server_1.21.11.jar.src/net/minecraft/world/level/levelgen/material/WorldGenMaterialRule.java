package net.minecraft.world.level.levelgen.material;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.NoiseChunk;

public interface WorldGenMaterialRule {
  BlockState apply(NoiseChunk paramNoiseChunk, int paramInt1, int paramInt2, int paramInt3);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\material\WorldGenMaterialRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */