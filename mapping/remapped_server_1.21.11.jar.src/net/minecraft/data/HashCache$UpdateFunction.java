package net.minecraft.data;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface UpdateFunction {
  CompletableFuture<?> update(CachedOutput paramCachedOutput);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\HashCache$UpdateFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */