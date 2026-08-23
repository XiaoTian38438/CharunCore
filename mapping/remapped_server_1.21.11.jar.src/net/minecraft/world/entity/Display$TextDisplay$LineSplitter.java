package net.minecraft.world.entity;

import net.minecraft.network.chat.Component;

@FunctionalInterface
public interface LineSplitter {
  Display.TextDisplay.CachedInfo split(Component paramComponent, int paramInt);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\Display$TextDisplay$LineSplitter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */