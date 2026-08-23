package net.minecraft.world.inventory;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface MenuConstructor {
  AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory, Player paramPlayer);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\MenuConstructor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */