package net.minecraft.world.attribute;

import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface Positional<Value> extends EnvironmentAttributeLayer<Value> {
  Value applyPositional(Value paramValue, Vec3 paramVec3, SpatialAttributeInterpolator paramSpatialAttributeInterpolator);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\EnvironmentAttributeLayer$Positional.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */