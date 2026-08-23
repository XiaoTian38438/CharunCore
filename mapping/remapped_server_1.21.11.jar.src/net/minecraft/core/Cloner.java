/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JavaOps;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class Cloner<T>
/*    */ {
/*    */   private final Codec<T> directCodec;
/*    */   
/*    */   Cloner(Codec<T> paramCodec) {
/* 16 */     this.directCodec = paramCodec;
/*    */   }
/*    */   
/*    */   public T clone(T paramT, HolderLookup.Provider paramProvider1, HolderLookup.Provider paramProvider2) {
/* 20 */     RegistryOps<?> registryOps1 = paramProvider1.createSerializationContext((DynamicOps<?>)JavaOps.INSTANCE);
/* 21 */     RegistryOps<?> registryOps2 = paramProvider2.createSerializationContext((DynamicOps<?>)JavaOps.INSTANCE);
/*    */     
/* 23 */     Object object = this.directCodec.encodeStart((DynamicOps)registryOps1, paramT).getOrThrow(paramString -> new IllegalStateException("Failed to encode: " + paramString));
/* 24 */     return (T)this.directCodec.parse((DynamicOps)registryOps2, object).getOrThrow(paramString -> new IllegalStateException("Failed to decode: " + paramString));
/*    */   }
/*    */   
/*    */   public static class Factory {
/* 28 */     private final Map<ResourceKey<? extends Registry<?>>, Cloner<?>> codecs = new HashMap<>();
/*    */     
/*    */     public <T> Factory addCodec(ResourceKey<? extends Registry<? extends T>> param1ResourceKey, Codec<T> param1Codec) {
/* 31 */       this.codecs.put(param1ResourceKey, new Cloner(param1Codec));
/* 32 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public <T> Cloner<T> cloner(ResourceKey<? extends Registry<? extends T>> param1ResourceKey) {
/* 37 */       return (Cloner<T>)this.codecs.get(param1ResourceKey);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\Cloner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */