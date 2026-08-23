package net.minecraft.tags;

import java.util.Collection;
import net.minecraft.resources.Identifier;

public interface Lookup<T> {
  T element(Identifier paramIdentifier, boolean paramBoolean);
  
  Collection<T> tag(Identifier paramIdentifier);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\TagEntry$Lookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */