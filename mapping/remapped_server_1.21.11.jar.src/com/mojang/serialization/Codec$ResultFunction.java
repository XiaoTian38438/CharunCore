package com.mojang.serialization;

import com.mojang.datafixers.util.Pair;

public interface ResultFunction<A> {
  <T> DataResult<Pair<A, T>> apply(DynamicOps<T> paramDynamicOps, T paramT, DataResult<Pair<A, T>> paramDataResult);
  
  <T> DataResult<T> coApply(DynamicOps<T> paramDynamicOps, A paramA, DataResult<T> paramDataResult);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Codec$ResultFunction.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */