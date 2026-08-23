/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   implements Codec<Pair<String, A>>
/*     */ {
/*     */   public <T> DataResult<Pair<Pair<String, A>, T>> decode(DynamicOps<T> paramDynamicOps, T paramT) {
/* 118 */     return Named.NamedType.this.element.codec().decode(paramDynamicOps, paramT).map(paramPair -> paramPair.mapFirst(())).setLifecycle(Lifecycle.experimental());
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> DataResult<T> encode(Pair<String, A> paramPair, DynamicOps<T> paramDynamicOps, T paramT) {
/* 123 */     if (!Objects.equals(paramPair.getFirst(), Named.NamedType.this.name)) {
/* 124 */       return DataResult.error(() -> "Named type name doesn't match: expected: " + Named.NamedType.this.name + ", got: " + (String)paramPair.getFirst(), paramT);
/*     */     }
/* 126 */     return Named.NamedType.this.element.codec().encode(paramPair.getSecond(), paramDynamicOps, paramT).setLifecycle(Lifecycle.experimental());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Named$NamedType$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */