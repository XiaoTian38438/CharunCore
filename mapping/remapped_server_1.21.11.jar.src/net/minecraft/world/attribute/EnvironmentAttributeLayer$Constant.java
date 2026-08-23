package net.minecraft.world.attribute;

@FunctionalInterface
public interface Constant<Value> extends EnvironmentAttributeLayer<Value> {
  Value applyConstant(Value paramValue);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\EnvironmentAttributeLayer$Constant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */