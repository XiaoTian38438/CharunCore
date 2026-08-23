package com.mojang.datafixers;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;

public interface DataFixer {
  <T> Dynamic<T> update(DSL.TypeReference paramTypeReference, Dynamic<T> paramDynamic, int paramInt1, int paramInt2);
  
  Schema getSchema(int paramInt);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\DataFixer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */