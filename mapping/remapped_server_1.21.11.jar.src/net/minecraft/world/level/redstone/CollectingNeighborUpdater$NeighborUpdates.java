package net.minecraft.world.level.redstone;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

interface NeighborUpdates {
  boolean runNext(Level paramLevel);
  
  void forEachUpdatedPos(Consumer<BlockPos> paramConsumer);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\redstone\CollectingNeighborUpdater$NeighborUpdates.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */