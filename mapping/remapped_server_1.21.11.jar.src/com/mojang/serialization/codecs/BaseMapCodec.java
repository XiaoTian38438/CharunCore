/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface BaseMapCodec<K, V>
/*    */ {
/*    */   default <T> DataResult<Map<K, V>> decode(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike) {
/* 27 */     Object2ObjectArrayMap object2ObjectArrayMap = new Object2ObjectArrayMap();
/* 28 */     Stream.Builder<?> builder = Stream.builder();
/*    */     
/* 30 */     DataResult dataResult = (DataResult)paramMapLike.entries().reduce(
/* 31 */         DataResult.success(Unit.INSTANCE, Lifecycle.stable()), (paramDataResult, paramPair) -> {
/*    */           DataResult dataResult1 = keyCodec().parse(paramDynamicOps, paramPair.getFirst());
/*    */           
/*    */           DataResult dataResult2 = elementCodec().parse(paramDynamicOps, paramPair.getSecond());
/*    */           
/*    */           DataResult dataResult3 = dataResult1.apply2stable(Pair::of, dataResult2);
/*    */           
/*    */           Optional<Pair> optional = dataResult3.resultOrPartial();
/*    */           
/*    */           if (optional.isPresent()) {
/*    */             Object object = paramObject2ObjectMap.putIfAbsent(((Pair)optional.get()).getFirst(), ((Pair)optional.get()).getSecond());
/*    */             
/*    */             if (object != null) {
/*    */               paramBuilder.add(paramPair);
/*    */               
/*    */               return paramDataResult.apply2stable((), DataResult.error(()));
/*    */             } 
/*    */           } 
/*    */           if (dataResult3.isError()) {
/*    */             paramBuilder.add(paramPair);
/*    */           }
/*    */           return paramDataResult.apply2stable((), dataResult3);
/*    */         }(paramDataResult1, paramDataResult2) -> paramDataResult1.apply2stable((), paramDataResult2));
/* 54 */     ImmutableMap immutableMap = ImmutableMap.copyOf((Map)object2ObjectArrayMap);
/* 55 */     Object object = paramDynamicOps.createMap(builder.build());
/*    */     
/* 57 */     return dataResult.map(paramUnit -> paramMap).setPartial(immutableMap).mapError(paramString -> paramString + " missed input: " + paramString);
/*    */   }
/*    */   
/*    */   default <T> RecordBuilder<T> encode(Map<K, V> paramMap, DynamicOps<T> paramDynamicOps, RecordBuilder<T> paramRecordBuilder) {
/* 61 */     for (Map.Entry<K, V> entry : paramMap.entrySet()) {
/* 62 */       paramRecordBuilder.add(keyCodec().encodeStart(paramDynamicOps, entry.getKey()), elementCodec().encodeStart(paramDynamicOps, entry.getValue()));
/*    */     }
/* 64 */     return paramRecordBuilder;
/*    */   }
/*    */   
/*    */   Codec<K> keyCodec();
/*    */   
/*    */   Codec<V> elementCodec();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\BaseMapCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */