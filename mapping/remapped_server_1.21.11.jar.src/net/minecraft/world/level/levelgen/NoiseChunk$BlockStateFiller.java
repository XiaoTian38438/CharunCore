package net.minecraft.world.level.levelgen;

import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface BlockStateFiller {
  BlockState calculate(DensityFunction.FunctionContext paramFunctionContext);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\NoiseChunk$BlockStateFiller.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */