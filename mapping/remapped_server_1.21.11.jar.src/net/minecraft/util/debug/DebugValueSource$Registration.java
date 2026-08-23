package net.minecraft.util.debug;

public interface Registration {
  <T> void register(DebugSubscription<T> paramDebugSubscription, DebugValueSource.ValueGetter<T> paramValueGetter);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\debug\DebugValueSource$Registration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */