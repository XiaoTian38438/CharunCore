/*     */ package com.mojang.datafixers.functions;
/*     */ 
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collections;
/*     */ import java.util.Deque;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface CompRewrite
/*     */   extends PointFreeRule
/*     */ {
/*     */   static CompRewrite together(CompRewrite... paramVarArgs) {
/* 128 */     return (paramPointFree1, paramPointFree2) -> {
/*     */         for (CompRewrite compRewrite : paramArrayOfCompRewrite) {
/*     */           Optional<? extends PointFree<? extends Function<?, ?>>> optional = compRewrite.doRewrite(paramPointFree1, paramPointFree2);
/*     */           if (optional.isPresent()) {
/*     */             return optional;
/*     */           }
/*     */         } 
/*     */         return Optional.empty();
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> paramPointFree) {
/* 142 */     if (paramPointFree instanceof Comp) { Comp comp = (Comp)paramPointFree;
/* 143 */       return rewrite(comp.functions).map(paramArrayOfPointFree -> (paramArrayOfPointFree.length == 1) ? paramArrayOfPointFree[0] : new Comp<>((PointFree<? extends Function<?, ?>>[])paramArrayOfPointFree)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   private Optional<PointFree<? extends Function<?, ?>>[]> rewrite(PointFree<? extends Function<?, ?>>[] paramArrayOfPointFree) {
/* 155 */     ArrayDeque<PointFree> arrayDeque = new ArrayDeque(paramArrayOfPointFree.length);
/* 156 */     boolean bool = false;
/*     */     
/* 158 */     ArrayDeque<? super PointFree<? extends Function<?, ?>>> arrayDeque1 = new ArrayDeque(paramArrayOfPointFree.length);
/* 159 */     Collections.addAll(arrayDeque1, paramArrayOfPointFree);
/*     */     
/* 161 */     while (!arrayDeque1.isEmpty()) {
/* 162 */       PointFree<? extends Function<?, ?>> pointFree1 = arrayDeque1.removeFirst();
/* 163 */       PointFree<? extends Function<?, ?>> pointFree2 = arrayDeque.peekLast();
/*     */       
/* 165 */       Optional<PointFree<? extends Function<?, ?>>> optional = (Optional<PointFree<? extends Function<?, ?>>>)((pointFree2 != null) ? doRewrite(pointFree2, pointFree1) : Optional.empty());
/* 166 */       if (optional.isPresent()) {
/* 167 */         arrayDeque.removeLast();
/* 168 */         addFirst((Deque)arrayDeque1, optional.get());
/* 169 */         bool = true; continue;
/*     */       } 
/* 171 */       arrayDeque.add(pointFree1);
/*     */     } 
/*     */ 
/*     */     
/* 175 */     return bool ? (Optional)Optional.of(arrayDeque.toArray(paramInt -> new PointFree[paramInt])) : (Optional)Optional.<PointFree<? extends Function<?, ?>>[]>empty();
/*     */   }
/*     */   
/*     */   private static void addFirst(Deque<PointFree<? extends Function<?, ?>>> paramDeque, PointFree<? extends Function<?, ?>> paramPointFree) {
/* 179 */     if (paramPointFree instanceof Comp) { Comp comp = (Comp)paramPointFree;
/* 180 */       for (int i = comp.functions.length - 1; i >= 0; i--) {
/* 181 */         paramDeque.addFirst(comp.functions[i]);
/*     */       } }
/*     */     else
/* 184 */     { paramDeque.addFirst(paramPointFree); }
/*     */   
/*     */   }
/*     */   
/*     */   Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> paramPointFree1, PointFree<? extends Function<?, ?>> paramPointFree2);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\PointFreeRule$CompRewrite.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */