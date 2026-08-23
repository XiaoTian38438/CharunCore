/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class KeyCompressor<T>
/*    */ {
/* 13 */   private final Int2ObjectMap<T> decompress = (Int2ObjectMap<T>)new Int2ObjectArrayMap();
/* 14 */   private final Object2IntMap<T> compress = (Object2IntMap<T>)new Object2IntArrayMap();
/* 15 */   private final Object2IntMap<String> compressString = (Object2IntMap<String>)new Object2IntArrayMap();
/*    */   private final int size;
/*    */   private final DynamicOps<T> ops;
/*    */   
/*    */   public KeyCompressor(DynamicOps<T> paramDynamicOps, Stream<T> paramStream) {
/* 20 */     this.ops = paramDynamicOps;
/*    */     
/* 22 */     this.compressString.defaultReturnValue(-1);
/*    */     
/* 24 */     paramStream.forEach(paramObject -> {
/*    */           if (this.compress.containsKey(paramObject)) {
/*    */             return;
/*    */           }
/*    */           
/*    */           int i = this.compress.size();
/*    */           
/*    */           this.compress.put(paramObject, i);
/*    */           
/*    */           paramDynamicOps.getStringValue(paramObject).result().ifPresent(());
/*    */           this.decompress.put(i, paramObject);
/*    */         });
/* 36 */     this.size = this.compress.size();
/*    */   }
/*    */   
/*    */   public T decompress(int paramInt) {
/* 40 */     return (T)this.decompress.get(paramInt);
/*    */   }
/*    */   
/*    */   public int compress(String paramString) {
/* 44 */     int i = this.compressString.getInt(paramString);
/* 45 */     return (i == -1) ? compress(this.ops.createString(paramString)) : i;
/*    */   }
/*    */   
/*    */   public int compress(T paramT) {
/* 49 */     return this.compress.getInt(paramT);
/*    */   }
/*    */   
/*    */   public int size() {
/* 53 */     return this.size;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\KeyCompressor.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */