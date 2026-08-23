package net.minecraft.util.debug;

import net.minecraft.server.level.ServerLevel;

public interface DebugValueSource {
  void registerDebugValues(ServerLevel paramServerLevel, Registration paramRegistration);
  
  public static interface ValueGetter<T> {
    T get();
  }
  
  public static interface Registration {
    <T> void register(DebugSubscription<T> param1DebugSubscription, DebugValueSource.ValueGetter<T> param1ValueGetter);
  }
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\debug\DebugValueSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */