/*     */ package com.mojang.datafixers.functions;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
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
/*     */ public enum LensComp
/*     */   implements PointFreeRule.CompRewrite
/*     */ {
/* 278 */   INSTANCE;
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> paramPointFree1, PointFree<? extends Function<?, ?>> paramPointFree2) {
/* 283 */     if (paramPointFree1 instanceof Apply) { Apply apply = (Apply)paramPointFree1; if (paramPointFree2 instanceof Apply) { Apply apply1 = (Apply)paramPointFree2;
/* 284 */         PointFree pointFree1 = apply.func;
/* 285 */         PointFree pointFree2 = apply1.func;
/* 286 */         if (pointFree1 instanceof ProfunctorTransformer) { ProfunctorTransformer profunctorTransformer = (ProfunctorTransformer)pointFree1; if (pointFree2 instanceof ProfunctorTransformer) { ProfunctorTransformer profunctorTransformer1 = (ProfunctorTransformer)pointFree2;
/* 287 */             List<? extends TypedOptic.Element<?, ?, ?, ?>> list1 = profunctorTransformer.optic.elements();
/* 288 */             List<? extends TypedOptic.Element<?, ?, ?, ?>> list2 = profunctorTransformer1.optic.elements();
/* 289 */             int i = findCommonPrefix(list1, list2);
/* 290 */             if (i == 0) {
/* 291 */               return Optional.empty();
/*     */             }
/*     */             
/* 294 */             if (i == list1.size() && i == list2.size()) {
/* 295 */               return Optional.of(capApp(profunctorTransformer.optic, capComp(apply.arg, apply1.arg)));
/*     */             }
/*     */             
/* 298 */             Sets.SetView setView = Sets.union(profunctorTransformer.optic.bounds(), profunctorTransformer1.optic.bounds());
/*     */             
/* 300 */             TypedOptic<?, ?, ?, ?> typedOptic = new TypedOptic((Set)setView, list1.subList(0, i));
/* 301 */             PointFree<?> pointFree3 = capApp(new TypedOptic((Set)setView, list1.subList(i, list1.size())), apply.arg);
/* 302 */             PointFree<?> pointFree4 = capApp(new TypedOptic((Set)setView, list2.subList(i, list2.size())), apply1.arg);
/*     */             
/* 304 */             return Optional.of(capApp(typedOptic, capComp(pointFree3, pointFree4))); }  }
/*     */          }
/*     */        }
/* 307 */      return Optional.empty();
/*     */   }
/*     */   
/*     */   private static int findCommonPrefix(List<? extends TypedOptic.Element<?, ?, ?, ?>> paramList1, List<? extends TypedOptic.Element<?, ?, ?, ?>> paramList2) {
/* 311 */     int i = Math.min(paramList1.size(), paramList2.size());
/* 312 */     for (byte b = 0; b < i; b++) {
/* 313 */       if (!((TypedOptic.Element)paramList1.get(b)).optic().equals(((TypedOptic.Element)paramList2.get(b)).optic())) {
/* 314 */         return b;
/*     */       }
/*     */     } 
/* 317 */     return i;
/*     */   }
/*     */   
/*     */   private <A, B, C> PointFree<Function<A, C>> capComp(PointFree<?> paramPointFree1, PointFree<?> paramPointFree2) {
/* 321 */     return Functions.comp((PointFree)paramPointFree1, (PointFree)paramPointFree2);
/*     */   }
/*     */   
/*     */   private <R, A, B, S, T> PointFree<R> capApp(TypedOptic<S, T, A, B> paramTypedOptic, PointFree<?> paramPointFree) {
/* 325 */     if (paramTypedOptic.elements().isEmpty()) {
/* 326 */       return (PointFree)paramPointFree;
/*     */     }
/* 328 */     return Functions.app((PointFree)new ProfunctorTransformer<>(paramTypedOptic), paramPointFree);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\PointFreeRule$LensComp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */