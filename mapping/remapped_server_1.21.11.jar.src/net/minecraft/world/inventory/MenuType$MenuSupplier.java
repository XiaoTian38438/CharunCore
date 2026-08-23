package net.minecraft.world.inventory;

import net.minecraft.world.entity.player.Inventory;

interface MenuSupplier<T extends AbstractContainerMenu> {
  T create(int paramInt, Inventory paramInventory);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\MenuType$MenuSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */