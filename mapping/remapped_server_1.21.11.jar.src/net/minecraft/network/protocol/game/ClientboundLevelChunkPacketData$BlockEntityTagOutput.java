package net.minecraft.network.protocol.game;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;

@FunctionalInterface
public interface BlockEntityTagOutput {
  void accept(BlockPos paramBlockPos, BlockEntityType<?> paramBlockEntityType, CompoundTag paramCompoundTag);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ClientboundLevelChunkPacketData$BlockEntityTagOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */