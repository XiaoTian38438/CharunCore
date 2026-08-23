/*     */ package com.mojang.datafixers.functions;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.types.families.Algebra;
/*     */ import com.mojang.datafixers.types.families.ListAlgebra;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Objects;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum CataFuseSame
/*     */   implements PointFreeRule.CompRewrite
/*     */ {
/* 333 */   INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> paramPointFree1, PointFree<? extends Function<?, ?>> paramPointFree2) {
/* 339 */     if (paramPointFree1 instanceof Fold) { Fold fold = (Fold)paramPointFree1; if (paramPointFree2 instanceof Fold) { Fold fold1 = (Fold)paramPointFree2;
/*     */         
/* 341 */         RecursiveTypeFamily recursiveTypeFamily = fold.aType.family();
/* 342 */         if (fold.index == fold1.index && Objects.equals(recursiveTypeFamily, fold1.aType.family())) {
/* 343 */           RecursiveTypeFamily recursiveTypeFamily1 = fold.bType.family();
/*     */           
/* 345 */           ArrayList<RewriteResult> arrayList = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */           
/* 349 */           boolean bool = false;
/* 350 */           for (byte b = 0; b < recursiveTypeFamily.size(); b++) {
/* 351 */             RewriteResult<?, ?> rewriteResult1 = fold.algebra.apply(b);
/* 352 */             RewriteResult<?, ?> rewriteResult2 = fold1.algebra.apply(b);
/* 353 */             boolean bool1 = rewriteResult1.view().isNop();
/* 354 */             boolean bool2 = rewriteResult2.view().isNop();
/*     */             
/* 356 */             if (bool1 && bool2) {
/* 357 */               arrayList.add(rewriteResult1);
/* 358 */             } else if (!bool && !bool1 && !bool2) {
/* 359 */               arrayList.add(getCompose(rewriteResult1, rewriteResult2));
/* 360 */               bool = true;
/*     */             } else {
/* 362 */               return Optional.empty();
/*     */             } 
/*     */           } 
/* 365 */           ListAlgebra listAlgebra = new ListAlgebra("FusedSame", arrayList);
/* 366 */           return Optional.of(((RewriteResult)recursiveTypeFamily.fold((Algebra)listAlgebra, recursiveTypeFamily1).apply(fold.index)).view().function());
/*     */         }  }
/*     */        }
/* 369 */      return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   private <B> RewriteResult<?, ?> getCompose(RewriteResult<B, ?> paramRewriteResult, RewriteResult<?, ?> paramRewriteResult1) {
/* 374 */     return paramRewriteResult.compose(paramRewriteResult1);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\PointFreeRule$CataFuseSame.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */