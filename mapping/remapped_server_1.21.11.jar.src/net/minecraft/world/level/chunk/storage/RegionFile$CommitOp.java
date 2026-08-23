package net.minecraft.world.level.chunk.storage;

import java.io.IOException;

interface CommitOp {
  void run() throws IOException;
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\storage\RegionFile$CommitOp.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */