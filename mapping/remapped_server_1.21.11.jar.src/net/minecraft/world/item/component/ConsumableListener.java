package net.minecraft.world.item.component;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ConsumableListener {
  void onConsume(Level paramLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack, Consumable paramConsumable);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\component\ConsumableListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */