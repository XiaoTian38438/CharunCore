/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SingleKeyCache<K, V>
/*    */ {
/*    */   private final Function<K, V> computeValue;
/* 15 */   private K cacheKey = null;
/*    */   private V cachedValue;
/*    */   
/*    */   public SingleKeyCache(Function<K, V> paramFunction) {
/* 19 */     this.computeValue = paramFunction;
/*    */   }
/*    */   
/*    */   public V getValue(K paramK) {
/* 23 */     if (this.cachedValue == null || !Objects.equals(this.cacheKey, paramK)) {
/* 24 */       this.cachedValue = this.computeValue.apply(paramK);
/* 25 */       this.cacheKey = paramK;
/*    */     } 
/* 27 */     return this.cachedValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\SingleKeyCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */