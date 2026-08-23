/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.google.common.collect.Lists;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ public class IdMapper<T>
/*    */   implements IdMap<T>
/*    */ {
/*    */   private int nextId;
/*    */   private final Reference2IntMap<T> tToId;
/*    */   private final List<T> idToT;
/*    */   
/*    */   public IdMapper() {
/* 20 */     this(512);
/*    */   }
/*    */   
/*    */   public IdMapper(int paramInt) {
/* 24 */     this.idToT = Lists.newArrayListWithExpectedSize(paramInt);
/* 25 */     this.tToId = (Reference2IntMap<T>)new Reference2IntOpenHashMap(paramInt);
/* 26 */     this.tToId.defaultReturnValue(-1);
/*    */   }
/*    */   
/*    */   public void addMapping(T paramT, int paramInt) {
/* 30 */     this.tToId.put(paramT, paramInt);
/*    */ 
/*    */     
/* 33 */     while (this.idToT.size() <= paramInt) {
/* 34 */       this.idToT.add(null);
/*    */     }
/*    */     
/* 37 */     this.idToT.set(paramInt, paramT);
/*    */     
/* 39 */     if (this.nextId <= paramInt) {
/* 40 */       this.nextId = paramInt + 1;
/*    */     }
/*    */   }
/*    */   
/*    */   public void add(T paramT) {
/* 45 */     addMapping(paramT, this.nextId);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getId(T paramT) {
/* 50 */     return this.tToId.getInt(paramT);
/*    */   }
/*    */ 
/*    */   
/*    */   public final T byId(int paramInt) {
/* 55 */     if (paramInt >= 0 && paramInt < this.idToT.size()) {
/* 56 */       return this.idToT.get(paramInt);
/*    */     }
/*    */     
/* 59 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<T> iterator() {
/* 64 */     return (Iterator<T>)Iterators.filter(this.idToT.iterator(), Objects::nonNull);
/*    */   }
/*    */   
/*    */   public boolean contains(int paramInt) {
/* 68 */     return (byId(paramInt) != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 73 */     return this.tToId.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\IdMapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */