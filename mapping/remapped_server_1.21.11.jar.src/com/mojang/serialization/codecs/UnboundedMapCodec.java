/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Map;
/*    */ 
/*    */ public final class UnboundedMapCodec<K, V> extends Record implements BaseMapCodec<K, V>, Codec<Map<K, V>> {
/*    */   private final Codec<K> keyCodec;
/*    */   private final Codec<V> elementCodec;
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/serialization/codecs/UnboundedMapCodec;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */   }
/*    */   
/* 16 */   public UnboundedMapCodec(Codec<K> paramCodec, Codec<V> paramCodec1) { this.keyCodec = paramCodec; this.elementCodec = paramCodec1; } public Codec<K> keyCodec() { return this.keyCodec; } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/serialization/codecs/UnboundedMapCodec;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 16 */     //   #16	-> 0 } public Codec<V> elementCodec() { return this.elementCodec; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> paramDynamicOps, T paramT) {
/* 22 */     return paramDynamicOps.getMap(paramT).setLifecycle(Lifecycle.stable()).flatMap(paramMapLike -> decode(paramDynamicOps, paramMapLike)).map(paramMap -> Pair.of(paramMap, paramObject));
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(Map<K, V> paramMap, DynamicOps<T> paramDynamicOps, T paramT) {
/* 27 */     return encode(paramMap, paramDynamicOps, paramDynamicOps.mapBuilder()).build(paramT);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 32 */     return "UnboundedMapCodec[" + String.valueOf(this.keyCodec) + " -> " + String.valueOf(this.elementCodec) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\UnboundedMapCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */