/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Spliterator;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
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
/*    */ public abstract class ListBacked<T>
/*    */   implements HolderSet<T>
/*    */ {
/*    */   protected abstract List<Holder<T>> contents();
/*    */   
/*    */   public int size() {
/* 43 */     return contents().size();
/*    */   }
/*    */ 
/*    */   
/*    */   public Spliterator<Holder<T>> spliterator() {
/* 48 */     return contents().spliterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<Holder<T>> iterator() {
/* 53 */     return contents().iterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<Holder<T>> stream() {
/* 58 */     return contents().stream();
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Holder<T>> getRandomElement(RandomSource paramRandomSource) {
/* 63 */     return Util.getRandomSafe(contents(), paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   public Holder<T> get(int paramInt) {
/* 68 */     return contents().get(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSerializeIn(HolderOwner<T> paramHolderOwner) {
/* 73 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\HolderSet$ListBacked.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */