package net.minecraft.world;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public interface WorldlyContainer extends Container {
  int[] getSlotsForFace(Direction paramDirection);
  
  boolean canPlaceItemThroughFace(int paramInt, ItemStack paramItemStack, Direction paramDirection);
  
  boolean canTakeItemThroughFace(int paramInt, ItemStack paramItemStack, Direction paramDirection);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\WorldlyContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */