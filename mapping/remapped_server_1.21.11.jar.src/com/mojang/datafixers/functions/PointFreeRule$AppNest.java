/*     */ package com.mojang.datafixers.functions;
/*     */ 
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
/*     */ public enum AppNest
/*     */   implements PointFreeRule
/*     */ {
/*  97 */   INSTANCE;
/*     */ 
/*     */ 
/*     */   
/*     */   public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> paramPointFree) {
/* 102 */     if (paramPointFree instanceof Apply) { Apply apply = (Apply)paramPointFree;
/* 103 */       PointFree pointFree = apply.arg; if (pointFree instanceof Apply) { Apply apply1 = (Apply)pointFree;
/* 104 */         return Optional.of(Functions.app(compose(apply.func, apply1.func), apply1.arg)); }
/*     */        }
/*     */     
/* 107 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <A, B, C> PointFree<Function<A, C>> compose(PointFree<? extends Function<?, ?>> paramPointFree1, PointFree<? extends Function<?, ?>> paramPointFree2) {
/* 113 */     if (paramPointFree1 instanceof ProfunctorTransformer) { ProfunctorTransformer<?, ?, ?, ?> profunctorTransformer = (ProfunctorTransformer)paramPointFree1; if (paramPointFree2 instanceof ProfunctorTransformer) { ProfunctorTransformer<?, ?, ?, ?> profunctorTransformer1 = (ProfunctorTransformer)paramPointFree2;
/* 114 */         return cap(profunctorTransformer, profunctorTransformer1); }
/*     */        }
/* 116 */      return Functions.comp((PointFree)paramPointFree1, (PointFree)paramPointFree2);
/*     */   }
/*     */ 
/*     */   
/*     */   private <R, X, Y, S, T, A, B> R cap(ProfunctorTransformer<X, Y, ?, ?> paramProfunctorTransformer, ProfunctorTransformer<S, T, A, B> paramProfunctorTransformer1) {
/* 121 */     ProfunctorTransformer<X, Y, ?, ?> profunctorTransformer = paramProfunctorTransformer;
/* 122 */     return (R)Functions.profunctorTransformer(profunctorTransformer.optic.compose(paramProfunctorTransformer1.optic));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\PointFreeRule$AppNest.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */