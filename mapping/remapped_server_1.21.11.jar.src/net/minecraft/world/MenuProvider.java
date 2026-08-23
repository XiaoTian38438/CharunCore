package net.minecraft.world;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuConstructor;

public interface MenuProvider extends MenuConstructor {
  Component getDisplayName();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\MenuProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */