package net.minecraft.world.inventory;

import net.minecraft.world.item.ItemStack;

public interface ContainerListener {
  void slotChanged(AbstractContainerMenu paramAbstractContainerMenu, int paramInt, ItemStack paramItemStack);
  
  void dataChanged(AbstractContainerMenu paramAbstractContainerMenu, int paramInt1, int paramInt2);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\ContainerListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */