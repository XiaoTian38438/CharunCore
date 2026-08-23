/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import java.util.function.Function;
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
/*     */ class null
/*     */   extends MapDecoder.Implementation<B>
/*     */ {
/*     */   public <T> DataResult<B> decode(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike) {
/*  94 */     return MapDecoder.this.decode(paramDynamicOps, paramMapLike).map(function);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/*  99 */     return MapDecoder.this.keys(paramDynamicOps);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     return MapDecoder.this.toString() + "[mapped]";
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\MapDecoder$4.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */