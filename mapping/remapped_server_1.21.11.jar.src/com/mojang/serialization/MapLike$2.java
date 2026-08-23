/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Stream;
/*    */ import javax.annotation.Nullable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements MapLike<T>
/*    */ {
/*    */   @Nullable
/*    */   public T get(T paramT) {
/* 58 */     return (T)map.get(paramT);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T get(String paramString) {
/* 64 */     return get(ops.createString(paramString));
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<Pair<T, T>> entries() {
/* 69 */     return map.entrySet().stream().map(paramEntry -> Pair.of(paramEntry.getKey(), paramEntry.getValue()));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 74 */     return "MapLike[" + String.valueOf(map) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\MapLike$2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */