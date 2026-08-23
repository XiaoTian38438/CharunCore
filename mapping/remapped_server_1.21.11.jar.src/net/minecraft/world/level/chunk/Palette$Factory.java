package net.minecraft.world.level.chunk;

import java.util.List;

public interface Factory {
  <A> Palette<A> create(int paramInt, List<A> paramList);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\Palette$Factory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */