/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.HashMultimap;
/*    */ import com.google.common.collect.Multimap;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DependencySorter<K, V extends DependencySorter.Entry<K>>
/*    */ {
/* 19 */   private final Map<K, V> contents = new HashMap<>();
/*    */   
/*    */   public DependencySorter<K, V> addEntry(K paramK, V paramV) {
/* 22 */     this.contents.put(paramK, paramV);
/* 23 */     return this;
/*    */   }
/*    */   
/*    */   private void visitDependenciesAndElement(Multimap<K, K> paramMultimap, Set<K> paramSet, K paramK, BiConsumer<K, V> paramBiConsumer) {
/* 27 */     if (!paramSet.add(paramK)) {
/*    */       return;
/*    */     }
/*    */     
/* 31 */     paramMultimap.get(paramK).forEach(paramObject -> visitDependenciesAndElement(paramMultimap, paramSet, (K)paramObject, paramBiConsumer));
/*    */     
/* 33 */     Entry entry = (Entry)this.contents.get(paramK);
/* 34 */     if (entry != null) {
/* 35 */       paramBiConsumer.accept(paramK, (V)entry);
/*    */     }
/*    */   }
/*    */   
/*    */   private static <K> boolean isCyclic(Multimap<K, K> paramMultimap, K paramK1, K paramK2) {
/* 40 */     Collection collection = paramMultimap.get(paramK2);
/* 41 */     if (collection.contains(paramK1)) {
/* 42 */       return true;
/*    */     }
/* 44 */     return collection.stream().anyMatch(paramObject2 -> isCyclic(paramMultimap, paramObject1, paramObject2));
/*    */   }
/*    */   
/*    */   private static <K> void addDependencyIfNotCyclic(Multimap<K, K> paramMultimap, K paramK1, K paramK2) {
/* 48 */     if (!isCyclic(paramMultimap, paramK1, paramK2)) {
/* 49 */       paramMultimap.put(paramK1, paramK2);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void orderByDependencies(BiConsumer<K, V> paramBiConsumer) {
/* 59 */     HashMultimap hashMultimap = HashMultimap.create();
/*    */ 
/*    */ 
/*    */     
/* 63 */     this.contents.forEach((paramObject, paramEntry) -> paramEntry.visitRequiredDependencies(()));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 70 */     this.contents.forEach((paramObject, paramEntry) -> paramEntry.visitOptionalDependencies(()));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 77 */     HashSet hashSet = new HashSet();
/* 78 */     this.contents.keySet().forEach(paramObject -> visitDependenciesAndElement(paramMultimap, paramSet, (K)paramObject, paramBiConsumer));
/*    */   }
/*    */   
/*    */   public static interface Entry<K> {
/*    */     void visitRequiredDependencies(Consumer<K> param1Consumer);
/*    */     
/*    */     void visitOptionalDependencies(Consumer<K> param1Consumer);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\DependencySorter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */