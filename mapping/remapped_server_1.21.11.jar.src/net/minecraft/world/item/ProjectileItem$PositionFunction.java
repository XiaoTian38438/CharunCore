package net.minecraft.world.item;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;

@FunctionalInterface
public interface PositionFunction {
  Position getDispensePosition(BlockSource paramBlockSource, Direction paramDirection);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\ProjectileItem$PositionFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */