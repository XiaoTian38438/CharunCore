/*     */ package com.mojang.datafixers.functions;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.types.families.Algebra;
/*     */ import com.mojang.datafixers.types.families.ListAlgebra;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum CataFuseDifferent
/*     */   implements PointFreeRule.CompRewrite
/*     */ {
/* 379 */   INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> paramPointFree1, PointFree<? extends Function<?, ?>> paramPointFree2) {
/* 385 */     if (paramPointFree1 instanceof Fold) { Fold fold = (Fold)paramPointFree1; if (paramPointFree2 instanceof Fold) { Fold fold1 = (Fold)paramPointFree2;
/*     */         
/* 387 */         RecursiveTypeFamily recursiveTypeFamily = fold.aType.family();
/* 388 */         if (fold.index == fold1.index && Objects.equals(recursiveTypeFamily, fold1.aType.family())) {
/* 389 */           RecursiveTypeFamily recursiveTypeFamily1 = fold.bType.family();
/*     */           
/* 391 */           ArrayList<RewriteResult> arrayList = Lists.newArrayList();
/*     */           
/* 393 */           BitSet bitSet1 = new BitSet(recursiveTypeFamily.size());
/* 394 */           BitSet bitSet2 = new BitSet(recursiveTypeFamily.size());
/*     */           byte b;
/* 396 */           for (b = 0; b < recursiveTypeFamily.size(); b++) {
/* 397 */             RewriteResult rewriteResult1 = fold.algebra.apply(b);
/* 398 */             RewriteResult rewriteResult2 = fold1.algebra.apply(b);
/* 399 */             boolean bool1 = rewriteResult1.view().isNop();
/* 400 */             boolean bool2 = rewriteResult2.view().isNop();
/* 401 */             if (!bool1 && !bool2) {
/* 402 */               return Optional.empty();
/*     */             }
/* 404 */             bitSet1.set(b, !bool1);
/* 405 */             bitSet2.set(b, !bool2);
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 410 */           for (b = 0; b < recursiveTypeFamily.size(); b++) {
/* 411 */             RewriteResult rewriteResult1 = fold.algebra.apply(b);
/* 412 */             RewriteResult rewriteResult2 = fold1.algebra.apply(b);
/* 413 */             if (rewriteResult1.recData().intersects(bitSet2) || rewriteResult2.recData().intersects(bitSet1))
/*     */             {
/* 415 */               return Optional.empty();
/*     */             }
/* 417 */             if (rewriteResult1.view().isNop()) {
/* 418 */               arrayList.add(rewriteResult2);
/*     */             } else {
/* 420 */               arrayList.add(rewriteResult1);
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 425 */           ListAlgebra listAlgebra = new ListAlgebra("FusedDifferent", arrayList);
/* 426 */           return Optional.of(((RewriteResult)recursiveTypeFamily.fold((Algebra)listAlgebra, recursiveTypeFamily1).apply(fold.index)).view().function());
/*     */         }  }
/*     */        }
/* 429 */      return Optional.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\PointFreeRule$CataFuseDifferent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */