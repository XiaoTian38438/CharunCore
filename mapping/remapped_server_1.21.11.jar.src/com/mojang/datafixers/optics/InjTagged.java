/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class InjTagged<K, A, B>
/*    */   implements Prism<Pair<K, ?>, Pair<K, ?>, A, B>
/*    */ {
/*    */   private final K key;
/*    */   
/*    */   public InjTagged(K paramK) {
/* 17 */     this.key = paramK;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Either<Pair<K, ?>, A> match(Pair<K, ?> paramPair) {
/* 23 */     return Objects.equals(this.key, paramPair.getFirst()) ? Either.right(paramPair.getSecond()) : Either.left(paramPair);
/*    */   }
/*    */ 
/*    */   
/*    */   public Pair<K, ?> build(B paramB) {
/* 28 */     return Pair.of(this.key, paramB);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 33 */     return "inj[" + String.valueOf(this.key) + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 38 */     return (paramObject instanceof InjTagged && Objects.equals(((InjTagged)paramObject).key, this.key));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 43 */     return this.key.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\InjTagged.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */