package net.minecraft.util.debug;

@FunctionalInterface
public interface EventVisitor<T> {
  void accept(T paramT, int paramInt1, int paramInt2);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\debug\DebugValueAccess$EventVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */