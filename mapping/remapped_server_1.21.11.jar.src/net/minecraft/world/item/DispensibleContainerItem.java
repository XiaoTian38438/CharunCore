package net.minecraft.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public interface DispensibleContainerItem {
  default void checkExtraContent(LivingEntity paramLivingEntity, Level paramLevel, ItemStack paramItemStack, BlockPos paramBlockPos) {}
  
  boolean emptyContents(LivingEntity paramLivingEntity, Level paramLevel, BlockPos paramBlockPos, BlockHitResult paramBlockHitResult);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\DispensibleContainerItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */