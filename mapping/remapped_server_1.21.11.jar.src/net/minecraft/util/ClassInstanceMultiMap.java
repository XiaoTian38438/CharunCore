/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.AbstractCollection;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class ClassInstanceMultiMap<T> extends AbstractCollection<T> {
/* 16 */   private final Map<Class<?>, List<T>> byClass = Maps.newHashMap();
/*    */   
/*    */   private final Class<T> baseClass;
/* 19 */   private final List<T> allInstances = Lists.newArrayList();
/*    */   
/*    */   public ClassInstanceMultiMap(Class<T> paramClass) {
/* 22 */     this.baseClass = paramClass;
/* 23 */     this.byClass.put(paramClass, this.allInstances);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(T paramT) {
/* 28 */     boolean bool = false;
/* 29 */     for (Map.Entry<Class<?>, List<T>> entry : this.byClass.entrySet()) {
/* 30 */       if (((Class)entry.getKey()).isInstance(paramT)) {
/* 31 */         bool |= ((List<T>)entry.getValue()).add(paramT);
/*    */       }
/*    */     } 
/* 34 */     return bool;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(Object paramObject) {
/* 39 */     boolean bool = false;
/* 40 */     for (Map.Entry<Class<?>, List<T>> entry : this.byClass.entrySet()) {
/* 41 */       if (((Class)entry.getKey()).isInstance(paramObject)) {
/* 42 */         List list = (List)entry.getValue();
/* 43 */         bool |= list.remove(paramObject);
/*    */       } 
/*    */     } 
/* 46 */     return bool;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(Object paramObject) {
/* 51 */     return find(paramObject.getClass()).contains(paramObject);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> Collection<S> find(Class<S> paramClass) {
/* 56 */     if (!this.baseClass.isAssignableFrom(paramClass)) {
/* 57 */       throw new IllegalArgumentException("Don't know how to search for " + String.valueOf(paramClass));
/*    */     }
/* 59 */     List<? extends S> list = this.byClass.computeIfAbsent(paramClass, paramClass -> { Objects.requireNonNull(paramClass); return (List)this.allInstances.stream().filter(paramClass::isInstance).collect(Util.toMutableList());
/* 60 */         }); return Collections.unmodifiableCollection(list);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<T> iterator() {
/* 65 */     if (this.allInstances.isEmpty()) {
/* 66 */       return Collections.emptyIterator();
/*    */     }
/* 68 */     return (Iterator<T>)Iterators.unmodifiableIterator(this.allInstances.iterator());
/*    */   }
/*    */   
/*    */   public List<T> getAllInstances() {
/* 72 */     return (List<T>)ImmutableList.copyOf(this.allInstances);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 77 */     return this.allInstances.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\ClassInstanceMultiMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */