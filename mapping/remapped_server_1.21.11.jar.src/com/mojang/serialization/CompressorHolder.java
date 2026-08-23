/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public abstract class CompressorHolder
/*    */   implements Compressable
/*    */ {
/* 10 */   private final Map<DynamicOps<?>, KeyCompressor<?>> compressors = (Map<DynamicOps<?>, KeyCompressor<?>>)new Object2ObjectArrayMap();
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> KeyCompressor<T> compressor(DynamicOps<T> paramDynamicOps) {
/* 15 */     return (KeyCompressor<T>)this.compressors.computeIfAbsent(paramDynamicOps, paramDynamicOps2 -> new KeyCompressor(paramDynamicOps1, keys(paramDynamicOps1)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\CompressorHolder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */