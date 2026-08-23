package net.minecraft.world.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

public interface Shearable {
  void shear(ServerLevel paramServerLevel, SoundSource paramSoundSource, ItemStack paramItemStack);
  
  boolean readyForShearing();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\Shearable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */