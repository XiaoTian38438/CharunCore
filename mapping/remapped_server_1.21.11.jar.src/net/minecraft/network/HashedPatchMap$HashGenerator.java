package net.minecraft.network;

import java.util.function.Function;
import net.minecraft.core.component.TypedDataComponent;

@FunctionalInterface
public interface HashGenerator extends Function<TypedDataComponent<?>, Integer> {}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\HashedPatchMap$HashGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */