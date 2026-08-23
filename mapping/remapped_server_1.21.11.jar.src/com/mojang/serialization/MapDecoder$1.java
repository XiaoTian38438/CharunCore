/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import java.util.stream.IntStream;
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
/*    */ class null
/*    */   implements MapLike<T>
/*    */ {
/*    */   @Nullable
/*    */   public T get(T paramT) {
/* 35 */     return entries.get(compressor.compress(paramT));
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T get(String paramString) {
/* 41 */     return entries.get(compressor.compress(paramString));
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<Pair<T, T>> entries() {
/* 46 */     return IntStream.range(0, entries.size()).<Pair<T, T>>mapToObj(paramInt -> Pair.of(paramKeyCompressor.decompress(paramInt), paramList.get(paramInt))).filter(paramPair -> (paramPair.getSecond() != null));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\MapDecoder$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */