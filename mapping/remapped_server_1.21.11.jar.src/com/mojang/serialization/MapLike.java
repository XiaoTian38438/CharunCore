/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Stream;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface MapLike<T>
/*    */ {
/* 12 */   public static final MapLike<Object> EMPTY = new MapLike<Object>()
/*    */     {
/*    */       @Nullable
/*    */       public Object get(Object param1Object) {
/* 16 */         return null;
/*    */       }
/*    */ 
/*    */       
/*    */       @Nullable
/*    */       public Object get(String param1String) {
/* 22 */         return null;
/*    */       }
/*    */ 
/*    */       
/*    */       public Stream<Pair<Object, Object>> entries() {
/* 27 */         return Stream.empty();
/*    */       }
/*    */ 
/*    */       
/*    */       public String toString() {
/* 32 */         return "EmptyMapLike";
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   static <T> MapLike<T> empty() {
/* 38 */     return (MapLike)EMPTY;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   T get(T paramT);
/*    */   
/*    */   @Nullable
/*    */   T get(String paramString);
/*    */   
/*    */   Stream<Pair<T, T>> entries();
/*    */   
/*    */   static <T> MapLike<T> forMap(final Map<T, T> map, final DynamicOps<T> ops) {
/* 50 */     if (map.isEmpty()) {
/* 51 */       return empty();
/*    */     }
/*    */     
/* 54 */     return new MapLike<T>()
/*    */       {
/*    */         @Nullable
/*    */         public T get(T param1T) {
/* 58 */           return (T)map.get(param1T);
/*    */         }
/*    */ 
/*    */         
/*    */         @Nullable
/*    */         public T get(String param1String) {
/* 64 */           return get(ops.createString(param1String));
/*    */         }
/*    */ 
/*    */         
/*    */         public Stream<Pair<T, T>> entries() {
/* 69 */           return map.entrySet().stream().map(param1Entry -> Pair.of(param1Entry.getKey(), param1Entry.getValue()));
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 74 */           return "MapLike[" + String.valueOf(map) + "]";
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\MapLike.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */