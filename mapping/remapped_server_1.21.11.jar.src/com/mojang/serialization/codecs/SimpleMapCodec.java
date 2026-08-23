/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Keyable;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SimpleMapCodec<K, V>
/*    */   extends MapCodec<Map<K, V>>
/*    */   implements BaseMapCodec<K, V>
/*    */ {
/*    */   private final Codec<K> keyCodec;
/*    */   private final Codec<V> elementCodec;
/*    */   private final Keyable keys;
/*    */   
/*    */   public SimpleMapCodec(Codec<K> paramCodec, Codec<V> paramCodec1, Keyable paramKeyable) {
/* 26 */     this.keyCodec = paramCodec;
/* 27 */     this.elementCodec = paramCodec1;
/* 28 */     this.keys = paramKeyable;
/*    */   }
/*    */ 
/*    */   
/*    */   public Codec<K> keyCodec() {
/* 33 */     return this.keyCodec;
/*    */   }
/*    */ 
/*    */   
/*    */   public Codec<V> elementCodec() {
/* 38 */     return this.elementCodec;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/* 43 */     return this.keys.keys(paramDynamicOps);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Map<K, V>> decode(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike) {
/* 48 */     return (DataResult)super.decode(paramDynamicOps, paramMapLike);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> RecordBuilder<T> encode(Map<K, V> paramMap, DynamicOps<T> paramDynamicOps, RecordBuilder<T> paramRecordBuilder) {
/* 53 */     return super.encode((Map)paramMap, paramDynamicOps, paramRecordBuilder);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 58 */     if (this == paramObject) {
/* 59 */       return true;
/*    */     }
/* 61 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 62 */       return false;
/*    */     }
/* 64 */     SimpleMapCodec simpleMapCodec = (SimpleMapCodec)paramObject;
/* 65 */     return (Objects.equals(this.keyCodec, simpleMapCodec.keyCodec) && Objects.equals(this.elementCodec, simpleMapCodec.elementCodec));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 70 */     return Objects.hash(new Object[] { this.keyCodec, this.elementCodec });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 75 */     return "SimpleMapCodec[" + String.valueOf(this.keyCodec) + " -> " + String.valueOf(this.elementCodec) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\SimpleMapCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */