package net.minecraft.world.attribute;

import net.minecraft.world.phys.Vec3;

public interface EnvironmentAttributeLayer<Value> {
  @FunctionalInterface
  public static interface Positional<Value> extends EnvironmentAttributeLayer<Value> {
    Value applyPositional(Value param1Value, Vec3 param1Vec3, SpatialAttributeInterpolator param1SpatialAttributeInterpolator);
  }
  
  @FunctionalInterface
  public static interface TimeBased<Value> extends EnvironmentAttributeLayer<Value> {
    Value applyTimeBased(Value param1Value, int param1Int);
  }
  
  @FunctionalInterface
  public static interface Constant<Value> extends EnvironmentAttributeLayer<Value> {
    Value applyConstant(Value param1Value);
  }
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\EnvironmentAttributeLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */