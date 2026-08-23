/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.resources.ResourceKey;
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
/*    */ public class Factory
/*    */ {
/* 28 */   private final Map<ResourceKey<? extends Registry<?>>, Cloner<?>> codecs = new HashMap<>();
/*    */   
/*    */   public <T> Factory addCodec(ResourceKey<? extends Registry<? extends T>> paramResourceKey, Codec<T> paramCodec) {
/* 31 */     this.codecs.put(paramResourceKey, new Cloner(paramCodec));
/* 32 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Cloner<T> cloner(ResourceKey<? extends Registry<? extends T>> paramResourceKey) {
/* 37 */     return (Cloner<T>)this.codecs.get(paramResourceKey);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\Cloner$Factory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */