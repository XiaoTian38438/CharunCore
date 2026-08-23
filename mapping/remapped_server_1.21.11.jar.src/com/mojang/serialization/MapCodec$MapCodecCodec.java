/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
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
/*     */ public final class MapCodecCodec<A>
/*     */   extends Record
/*     */   implements Codec<A>
/*     */ {
/*     */   private final MapCodec<A> codec;
/*     */   
/*     */   public MapCodecCodec(MapCodec<A> paramMapCodec) {
/* 156 */     this.codec = paramMapCodec; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/serialization/MapCodec$MapCodecCodec;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/* 156 */     //   #156	-> 0 } public MapCodec<A> codec() { return this.codec; } public final boolean equals(Object paramObject) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/serialization/MapCodec$MapCodecCodec;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #156	-> 0
/*     */   } public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> paramDynamicOps, T paramT) {
/* 159 */     return this.codec.compressedDecode(paramDynamicOps, paramT).map(paramObject2 -> Pair.of(paramObject2, paramObject1));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> DataResult<T> encode(A paramA, DynamicOps<T> paramDynamicOps, T paramT) {
/* 164 */     return this.codec.encode(paramA, paramDynamicOps, this.codec.compressedBuilder(paramDynamicOps)).build(paramT);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 169 */     return this.codec.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\MapCodec$MapCodecCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */