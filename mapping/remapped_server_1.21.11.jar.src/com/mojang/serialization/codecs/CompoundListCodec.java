/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ public final class CompoundListCodec<K, V> implements Codec<List<Pair<K, V>>> {
/*    */   private final Codec<K> keyCodec;
/*    */   
/*    */   public CompoundListCodec(Codec<K> paramCodec, Codec<V> paramCodec1) {
/* 24 */     this.keyCodec = paramCodec;
/* 25 */     this.elementCodec = paramCodec1;
/*    */   }
/*    */   private final Codec<V> elementCodec;
/*    */   
/*    */   public <T> DataResult<Pair<List<Pair<K, V>>, T>> decode(DynamicOps<T> paramDynamicOps, T paramT) {
/* 30 */     return paramDynamicOps.getMapEntries(paramT).flatMap(paramConsumer -> {
/*    */           ImmutableList.Builder builder = ImmutableList.builder();
/*    */           ImmutableMap.Builder builder1 = ImmutableMap.builder();
/*    */           AtomicReference<DataResult> atomicReference = new AtomicReference<>(DataResult.success(Unit.INSTANCE, Lifecycle.experimental()));
/*    */           paramConsumer.accept(());
/*    */           ImmutableList immutableList = builder.build();
/*    */           Object object = paramDynamicOps.createMap((Map)builder1.build());
/*    */           Pair pair = Pair.of(immutableList, object);
/*    */           return ((DataResult)atomicReference.getPlain()).map(()).setPartial(pair);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(List<Pair<K, V>> paramList, DynamicOps<T> paramDynamicOps, T paramT) {
/* 61 */     RecordBuilder recordBuilder = paramDynamicOps.mapBuilder();
/*    */     
/* 63 */     for (Pair<K, V> pair : paramList) {
/* 64 */       recordBuilder.add(this.keyCodec.encodeStart(paramDynamicOps, pair.getFirst()), this.elementCodec.encodeStart(paramDynamicOps, pair.getSecond()));
/*    */     }
/*    */     
/* 67 */     return recordBuilder.build(paramT);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 72 */     if (this == paramObject) {
/* 73 */       return true;
/*    */     }
/* 75 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 76 */       return false;
/*    */     }
/* 78 */     CompoundListCodec compoundListCodec = (CompoundListCodec)paramObject;
/* 79 */     return (Objects.equals(this.keyCodec, compoundListCodec.keyCodec) && Objects.equals(this.elementCodec, compoundListCodec.elementCodec));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 84 */     return Objects.hash(new Object[] { this.keyCodec, this.elementCodec });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 89 */     return "CompoundListCodec[" + String.valueOf(this.keyCodec) + " -> " + String.valueOf(this.elementCodec) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\CompoundListCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */