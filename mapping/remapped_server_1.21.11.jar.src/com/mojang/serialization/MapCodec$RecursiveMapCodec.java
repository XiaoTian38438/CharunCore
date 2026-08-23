/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class RecursiveMapCodec<A>
/*     */   extends MapCodec<A>
/*     */ {
/*     */   private final String name;
/*     */   private final Supplier<MapCodec<A>> wrapped;
/*     */   
/*     */   private RecursiveMapCodec(String paramString, Function<Codec<A>, MapCodec<A>> paramFunction) {
/* 102 */     this.name = paramString;
/* 103 */     this.wrapped = (Supplier<MapCodec<A>>)Suppliers.memoize(() -> (MapCodec)paramFunction.apply(codec()));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> RecordBuilder<T> encode(A paramA, DynamicOps<T> paramDynamicOps, RecordBuilder<T> paramRecordBuilder) {
/* 108 */     return ((MapCodec<A>)this.wrapped.get()).encode(paramA, paramDynamicOps, paramRecordBuilder);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> DataResult<A> decode(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike) {
/* 113 */     return ((MapCodec<A>)this.wrapped.get()).decode(paramDynamicOps, paramMapLike);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/* 118 */     return ((MapCodec)this.wrapped.get()).keys(paramDynamicOps);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 123 */     return "RecursiveMapCodec[" + this.name + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\MapCodec$RecursiveMapCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */