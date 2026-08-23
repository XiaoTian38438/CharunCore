package com.mojang.serialization;

public interface ResultFunction<A> {
  <T> DataResult<A> apply(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike, DataResult<A> paramDataResult);
  
  <T> RecordBuilder<T> coApply(DynamicOps<T> paramDynamicOps, A paramA, RecordBuilder<T> paramRecordBuilder);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\MapCodec$ResultFunction.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */