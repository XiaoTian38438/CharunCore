/*     */ package com.mojang.datafixers.functions;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.types.Func;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.Algebra;
/*     */ import com.mojang.datafixers.types.families.ListAlgebra;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.templates.Product;
/*     */ import com.mojang.datafixers.types.templates.Sum;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collections;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface PointFreeRule
/*     */ {
/*     */   <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> paramPointFree);
/*     */   
/*     */   default <A> PointFree<A> rewriteOrNop(PointFree<A> paramPointFree) {
/*  40 */     return (PointFree<A>)DataFixUtils.orElse(rewrite(paramPointFree), paramPointFree);
/*     */   }
/*     */   
/*     */   static PointFreeRule nop() {
/*  44 */     return Nop.INSTANCE;
/*     */   }
/*     */   
/*     */   public enum Nop implements PointFreeRule, Supplier<PointFreeRule> {
/*  48 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public <A> Optional<PointFree<A>> rewrite(PointFree<A> param1PointFree) {
/*  52 */       return Optional.of(param1PointFree);
/*     */     }
/*     */ 
/*     */     
/*     */     public PointFreeRule get() {
/*  57 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum BangEta implements PointFreeRule {
/*  62 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> param1PointFree) {
/*  67 */       if (param1PointFree instanceof Bang) {
/*  68 */         return Optional.empty();
/*     */       }
/*  70 */       Type<A> type = param1PointFree.type(); if (type instanceof Func) { Func func = (Func)type;
/*  71 */         if (func.second() instanceof com.mojang.datafixers.types.constant.EmptyPart) {
/*  72 */           return Optional.of((PointFree)Functions.bang(func.first()));
/*     */         } }
/*     */       
/*  75 */       return Optional.empty();
/*     */     }
/*     */   }
/*     */   
/*     */   public enum LensAppId implements PointFreeRule {
/*  80 */     INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> param1PointFree) {
/*  86 */       if (param1PointFree instanceof Apply) { Apply apply = (Apply)param1PointFree;
/*  87 */         PointFree pointFree = apply.func;
/*  88 */         if (pointFree instanceof ProfunctorTransformer && Functions.isId(apply.arg)) {
/*  89 */           return Optional.of((PointFree)Functions.id(((Func)apply.type()).first()));
/*     */         } }
/*     */       
/*  92 */       return Optional.empty();
/*     */     }
/*     */   }
/*     */   
/*     */   public enum AppNest implements PointFreeRule {
/*  97 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> param1PointFree) {
/* 102 */       if (param1PointFree instanceof Apply) { Apply apply = (Apply)param1PointFree;
/* 103 */         PointFree pointFree = apply.arg; if (pointFree instanceof Apply) { Apply apply1 = (Apply)pointFree;
/* 104 */           return Optional.of(Functions.app(compose(apply.func, apply1.func), apply1.arg)); }
/*     */          }
/*     */       
/* 107 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private <A, B, C> PointFree<Function<A, C>> compose(PointFree<? extends Function<?, ?>> param1PointFree1, PointFree<? extends Function<?, ?>> param1PointFree2) {
/* 113 */       if (param1PointFree1 instanceof ProfunctorTransformer) { ProfunctorTransformer<?, ?, ?, ?> profunctorTransformer = (ProfunctorTransformer)param1PointFree1; if (param1PointFree2 instanceof ProfunctorTransformer) { ProfunctorTransformer<?, ?, ?, ?> profunctorTransformer1 = (ProfunctorTransformer)param1PointFree2;
/* 114 */           return cap(profunctorTransformer, profunctorTransformer1); }
/*     */          }
/* 116 */        return Functions.comp((PointFree)param1PointFree1, (PointFree)param1PointFree2);
/*     */     }
/*     */ 
/*     */     
/*     */     private <R, X, Y, S, T, A, B> R cap(ProfunctorTransformer<X, Y, ?, ?> param1ProfunctorTransformer, ProfunctorTransformer<S, T, A, B> param1ProfunctorTransformer1) {
/* 121 */       ProfunctorTransformer<X, Y, ?, ?> profunctorTransformer = param1ProfunctorTransformer;
/* 122 */       return (R)Functions.profunctorTransformer(profunctorTransformer.optic.compose(param1ProfunctorTransformer1.optic));
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface CompRewrite extends PointFreeRule {
/*     */     static CompRewrite together(CompRewrite... param1VarArgs) {
/* 128 */       return (param1PointFree1, param1PointFree2) -> {
/*     */           for (CompRewrite compRewrite : param1ArrayOfCompRewrite) {
/*     */             Optional<? extends PointFree<? extends Function<?, ?>>> optional = compRewrite.doRewrite(param1PointFree1, param1PointFree2);
/*     */             if (optional.isPresent()) {
/*     */               return optional;
/*     */             }
/*     */           } 
/*     */           return Optional.empty();
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     default <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> param1PointFree) {
/* 142 */       if (param1PointFree instanceof Comp) { Comp comp = (Comp)param1PointFree;
/* 143 */         return rewrite(comp.functions).map(param1ArrayOfPointFree -> (param1ArrayOfPointFree.length == 1) ? param1ArrayOfPointFree[0] : new Comp<>((PointFree<? extends Function<?, ?>>[])param1ArrayOfPointFree)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 150 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     private Optional<PointFree<? extends Function<?, ?>>[]> rewrite(PointFree<? extends Function<?, ?>>[] param1ArrayOfPointFree) {
/* 155 */       ArrayDeque<PointFree> arrayDeque = new ArrayDeque(param1ArrayOfPointFree.length);
/* 156 */       boolean bool = false;
/*     */       
/* 158 */       ArrayDeque<? super PointFree<? extends Function<?, ?>>> arrayDeque1 = new ArrayDeque(param1ArrayOfPointFree.length);
/* 159 */       Collections.addAll(arrayDeque1, param1ArrayOfPointFree);
/*     */       
/* 161 */       while (!arrayDeque1.isEmpty()) {
/* 162 */         PointFree<? extends Function<?, ?>> pointFree1 = arrayDeque1.removeFirst();
/* 163 */         PointFree<? extends Function<?, ?>> pointFree2 = arrayDeque.peekLast();
/*     */         
/* 165 */         Optional<PointFree<? extends Function<?, ?>>> optional = (Optional<PointFree<? extends Function<?, ?>>>)((pointFree2 != null) ? doRewrite(pointFree2, pointFree1) : Optional.empty());
/* 166 */         if (optional.isPresent()) {
/* 167 */           arrayDeque.removeLast();
/* 168 */           addFirst((Deque)arrayDeque1, optional.get());
/* 169 */           bool = true; continue;
/*     */         } 
/* 171 */         arrayDeque.add(pointFree1);
/*     */       } 
/*     */ 
/*     */       
/* 175 */       return bool ? (Optional)Optional.of(arrayDeque.toArray(param1Int -> new PointFree[param1Int])) : (Optional)Optional.<PointFree<? extends Function<?, ?>>[]>empty();
/*     */     }
/*     */     
/*     */     private static void addFirst(Deque<PointFree<? extends Function<?, ?>>> param1Deque, PointFree<? extends Function<?, ?>> param1PointFree) {
/* 179 */       if (param1PointFree instanceof Comp) { Comp comp = (Comp)param1PointFree;
/* 180 */         for (int i = comp.functions.length - 1; i >= 0; i--) {
/* 181 */           param1Deque.addFirst(comp.functions[i]);
/*     */         } }
/*     */       else
/* 184 */       { param1Deque.addFirst(param1PointFree); }
/*     */     
/*     */     }
/*     */     
/*     */     Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> param1PointFree1, PointFree<? extends Function<?, ?>> param1PointFree2);
/*     */   }
/*     */   
/*     */   public enum SortProj implements CompRewrite {
/* 192 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     public Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> param1PointFree1, PointFree<? extends Function<?, ?>> param1PointFree2) {
/* 197 */       if (param1PointFree1 instanceof Apply) { Apply<?, ?> apply = (Apply)param1PointFree1; if (param1PointFree2 instanceof Apply) { Apply<?, ?> apply1 = (Apply)param1PointFree2;
/* 198 */           PointFree pointFree1 = apply.func;
/* 199 */           PointFree pointFree2 = apply1.func;
/* 200 */           if (pointFree1 instanceof ProfunctorTransformer) { ProfunctorTransformer profunctorTransformer = (ProfunctorTransformer)pointFree1; if (pointFree2 instanceof ProfunctorTransformer) { ProfunctorTransformer profunctorTransformer1 = (ProfunctorTransformer)pointFree2;
/* 201 */               if (!Optics.isProj2(profunctorTransformer.optic.outermost())) {
/* 202 */                 return Optional.empty();
/*     */               }
/*     */               
/* 205 */               if (!Optics.isProj1(profunctorTransformer1.optic.outermost())) {
/* 206 */                 return Optional.empty();
/*     */               }
/*     */               
/* 209 */               return Optional.of(cap(apply, apply1)); }  }
/*     */            }
/*     */          }
/* 212 */        return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     private <R, A, A2, B, B2> R cap(Apply<?, ?> param1Apply1, Apply<?, ?> param1Apply2) {
/* 217 */       ProfunctorTransformer profunctorTransformer1 = (ProfunctorTransformer)param1Apply1.func;
/* 218 */       ProfunctorTransformer profunctorTransformer2 = (ProfunctorTransformer)param1Apply2.func;
/* 219 */       PointFree<?> pointFree1 = param1Apply1.arg;
/* 220 */       PointFree<?> pointFree2 = param1Apply2.arg;
/*     */       
/* 222 */       Func func1 = (Func)param1Apply1.type;
/* 223 */       Func func2 = (Func)param1Apply2.type;
/* 224 */       Product.ProductType productType1 = (Product.ProductType)func2.first();
/* 225 */       Product.ProductType productType2 = (Product.ProductType)func1.second();
/*     */       
/* 227 */       return (R)new Comp<>((PointFree<? extends Function<?, ?>>[])new PointFree[] { new Apply<>(profunctorTransformer2
/* 228 */               .castOuterUnchecked(DSL.and(productType2.first(), productType1.second()), (Type)productType2), pointFree2), new Apply<>(profunctorTransformer1
/* 229 */               .castOuterUnchecked((Type)productType1, DSL.and(productType2.first(), productType1.second())), pointFree1) });
/*     */     }
/*     */   }
/*     */   
/*     */   public enum SortInj
/*     */     implements CompRewrite {
/* 235 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     public Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> param1PointFree1, PointFree<? extends Function<?, ?>> param1PointFree2) {
/* 240 */       if (param1PointFree1 instanceof Apply) { Apply<?, ?> apply = (Apply)param1PointFree1; if (param1PointFree2 instanceof Apply) { Apply<?, ?> apply1 = (Apply)param1PointFree2;
/* 241 */           PointFree pointFree1 = apply.func;
/* 242 */           PointFree pointFree2 = apply1.func;
/* 243 */           if (pointFree1 instanceof ProfunctorTransformer) { ProfunctorTransformer profunctorTransformer = (ProfunctorTransformer)pointFree1; if (pointFree2 instanceof ProfunctorTransformer) { ProfunctorTransformer profunctorTransformer1 = (ProfunctorTransformer)pointFree2;
/* 244 */               if (!Optics.isInj2(profunctorTransformer.optic.outermost())) {
/* 245 */                 return Optional.empty();
/*     */               }
/*     */               
/* 248 */               if (!Optics.isInj1(profunctorTransformer1.optic.outermost())) {
/* 249 */                 return Optional.empty();
/*     */               }
/*     */               
/* 252 */               return Optional.of(cap(apply, apply1)); }  }
/*     */            }
/*     */          }
/* 255 */        return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     private <R, A, A2, B, B2> R cap(Apply<?, ?> param1Apply1, Apply<?, ?> param1Apply2) {
/* 260 */       ProfunctorTransformer profunctorTransformer1 = (ProfunctorTransformer)param1Apply1.func;
/* 261 */       ProfunctorTransformer profunctorTransformer2 = (ProfunctorTransformer)param1Apply2.func;
/* 262 */       PointFree<?> pointFree1 = param1Apply1.arg;
/* 263 */       PointFree<?> pointFree2 = param1Apply2.arg;
/*     */       
/* 265 */       Func func1 = (Func)param1Apply1.type;
/* 266 */       Func func2 = (Func)param1Apply2.type;
/* 267 */       Sum.SumType sumType1 = (Sum.SumType)func2.first();
/* 268 */       Sum.SumType sumType2 = (Sum.SumType)func1.second();
/*     */       
/* 270 */       return (R)new Comp<>((PointFree<? extends Function<?, ?>>[])new PointFree[] { new Apply<>(profunctorTransformer2
/* 271 */               .castOuterUnchecked(DSL.or(sumType2.first(), sumType1.second()), (Type)sumType2), pointFree2), new Apply<>(profunctorTransformer1
/* 272 */               .castOuterUnchecked((Type)sumType1, DSL.or(sumType2.first(), sumType1.second())), pointFree1) });
/*     */     }
/*     */   }
/*     */   
/*     */   public enum LensComp
/*     */     implements CompRewrite {
/* 278 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     public Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> param1PointFree1, PointFree<? extends Function<?, ?>> param1PointFree2) {
/* 283 */       if (param1PointFree1 instanceof Apply) { Apply apply = (Apply)param1PointFree1; if (param1PointFree2 instanceof Apply) { Apply apply1 = (Apply)param1PointFree2;
/* 284 */           PointFree pointFree1 = apply.func;
/* 285 */           PointFree pointFree2 = apply1.func;
/* 286 */           if (pointFree1 instanceof ProfunctorTransformer) { ProfunctorTransformer profunctorTransformer = (ProfunctorTransformer)pointFree1; if (pointFree2 instanceof ProfunctorTransformer) { ProfunctorTransformer profunctorTransformer1 = (ProfunctorTransformer)pointFree2;
/* 287 */               List<? extends TypedOptic.Element<?, ?, ?, ?>> list1 = profunctorTransformer.optic.elements();
/* 288 */               List<? extends TypedOptic.Element<?, ?, ?, ?>> list2 = profunctorTransformer1.optic.elements();
/* 289 */               int i = findCommonPrefix(list1, list2);
/* 290 */               if (i == 0) {
/* 291 */                 return Optional.empty();
/*     */               }
/*     */               
/* 294 */               if (i == list1.size() && i == list2.size()) {
/* 295 */                 return Optional.of(capApp(profunctorTransformer.optic, capComp(apply.arg, apply1.arg)));
/*     */               }
/*     */               
/* 298 */               Sets.SetView setView = Sets.union(profunctorTransformer.optic.bounds(), profunctorTransformer1.optic.bounds());
/*     */               
/* 300 */               TypedOptic<?, ?, ?, ?> typedOptic = new TypedOptic((Set)setView, list1.subList(0, i));
/* 301 */               PointFree<?> pointFree3 = capApp(new TypedOptic((Set)setView, list1.subList(i, list1.size())), apply.arg);
/* 302 */               PointFree<?> pointFree4 = capApp(new TypedOptic((Set)setView, list2.subList(i, list2.size())), apply1.arg);
/*     */               
/* 304 */               return Optional.of(capApp(typedOptic, capComp(pointFree3, pointFree4))); }  }
/*     */            }
/*     */          }
/* 307 */        return Optional.empty();
/*     */     }
/*     */     
/*     */     private static int findCommonPrefix(List<? extends TypedOptic.Element<?, ?, ?, ?>> param1List1, List<? extends TypedOptic.Element<?, ?, ?, ?>> param1List2) {
/* 311 */       int i = Math.min(param1List1.size(), param1List2.size());
/* 312 */       for (byte b = 0; b < i; b++) {
/* 313 */         if (!((TypedOptic.Element)param1List1.get(b)).optic().equals(((TypedOptic.Element)param1List2.get(b)).optic())) {
/* 314 */           return b;
/*     */         }
/*     */       } 
/* 317 */       return i;
/*     */     }
/*     */     
/*     */     private <A, B, C> PointFree<Function<A, C>> capComp(PointFree<?> param1PointFree1, PointFree<?> param1PointFree2) {
/* 321 */       return Functions.comp((PointFree)param1PointFree1, (PointFree)param1PointFree2);
/*     */     }
/*     */     
/*     */     private <R, A, B, S, T> PointFree<R> capApp(TypedOptic<S, T, A, B> param1TypedOptic, PointFree<?> param1PointFree) {
/* 325 */       if (param1TypedOptic.elements().isEmpty()) {
/* 326 */         return (PointFree)param1PointFree;
/*     */       }
/* 328 */       return Functions.app((PointFree)new ProfunctorTransformer<>(param1TypedOptic), param1PointFree);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum CataFuseSame implements CompRewrite {
/* 333 */     INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> param1PointFree1, PointFree<? extends Function<?, ?>> param1PointFree2) {
/* 339 */       if (param1PointFree1 instanceof Fold) { Fold fold = (Fold)param1PointFree1; if (param1PointFree2 instanceof Fold) { Fold fold1 = (Fold)param1PointFree2;
/*     */           
/* 341 */           RecursiveTypeFamily recursiveTypeFamily = fold.aType.family();
/* 342 */           if (fold.index == fold1.index && Objects.equals(recursiveTypeFamily, fold1.aType.family())) {
/* 343 */             RecursiveTypeFamily recursiveTypeFamily1 = fold.bType.family();
/*     */             
/* 345 */             ArrayList<RewriteResult> arrayList = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */             
/* 349 */             boolean bool = false;
/* 350 */             for (byte b = 0; b < recursiveTypeFamily.size(); b++) {
/* 351 */               RewriteResult<?, ?> rewriteResult1 = fold.algebra.apply(b);
/* 352 */               RewriteResult<?, ?> rewriteResult2 = fold1.algebra.apply(b);
/* 353 */               boolean bool1 = rewriteResult1.view().isNop();
/* 354 */               boolean bool2 = rewriteResult2.view().isNop();
/*     */               
/* 356 */               if (bool1 && bool2) {
/* 357 */                 arrayList.add(rewriteResult1);
/* 358 */               } else if (!bool && !bool1 && !bool2) {
/* 359 */                 arrayList.add(getCompose(rewriteResult1, rewriteResult2));
/* 360 */                 bool = true;
/*     */               } else {
/* 362 */                 return Optional.empty();
/*     */               } 
/*     */             } 
/* 365 */             ListAlgebra listAlgebra = new ListAlgebra("FusedSame", arrayList);
/* 366 */             return Optional.of(((RewriteResult)recursiveTypeFamily.fold((Algebra)listAlgebra, recursiveTypeFamily1).apply(fold.index)).view().function());
/*     */           }  }
/*     */          }
/* 369 */        return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     private <B> RewriteResult<?, ?> getCompose(RewriteResult<B, ?> param1RewriteResult, RewriteResult<?, ?> param1RewriteResult1) {
/* 374 */       return param1RewriteResult.compose(param1RewriteResult1);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum CataFuseDifferent implements CompRewrite {
/* 379 */     INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> param1PointFree1, PointFree<? extends Function<?, ?>> param1PointFree2) {
/* 385 */       if (param1PointFree1 instanceof Fold) { Fold fold = (Fold)param1PointFree1; if (param1PointFree2 instanceof Fold) { Fold fold1 = (Fold)param1PointFree2;
/*     */           
/* 387 */           RecursiveTypeFamily recursiveTypeFamily = fold.aType.family();
/* 388 */           if (fold.index == fold1.index && Objects.equals(recursiveTypeFamily, fold1.aType.family())) {
/* 389 */             RecursiveTypeFamily recursiveTypeFamily1 = fold.bType.family();
/*     */             
/* 391 */             ArrayList<RewriteResult> arrayList = Lists.newArrayList();
/*     */             
/* 393 */             BitSet bitSet1 = new BitSet(recursiveTypeFamily.size());
/* 394 */             BitSet bitSet2 = new BitSet(recursiveTypeFamily.size());
/*     */             byte b;
/* 396 */             for (b = 0; b < recursiveTypeFamily.size(); b++) {
/* 397 */               RewriteResult rewriteResult1 = fold.algebra.apply(b);
/* 398 */               RewriteResult rewriteResult2 = fold1.algebra.apply(b);
/* 399 */               boolean bool1 = rewriteResult1.view().isNop();
/* 400 */               boolean bool2 = rewriteResult2.view().isNop();
/* 401 */               if (!bool1 && !bool2) {
/* 402 */                 return Optional.empty();
/*     */               }
/* 404 */               bitSet1.set(b, !bool1);
/* 405 */               bitSet2.set(b, !bool2);
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 410 */             for (b = 0; b < recursiveTypeFamily.size(); b++) {
/* 411 */               RewriteResult rewriteResult1 = fold.algebra.apply(b);
/* 412 */               RewriteResult rewriteResult2 = fold1.algebra.apply(b);
/* 413 */               if (rewriteResult1.recData().intersects(bitSet2) || rewriteResult2.recData().intersects(bitSet1))
/*     */               {
/* 415 */                 return Optional.empty();
/*     */               }
/* 417 */               if (rewriteResult1.view().isNop()) {
/* 418 */                 arrayList.add(rewriteResult2);
/*     */               } else {
/* 420 */                 arrayList.add(rewriteResult1);
/*     */               } 
/*     */             } 
/*     */ 
/*     */             
/* 425 */             ListAlgebra listAlgebra = new ListAlgebra("FusedDifferent", arrayList);
/* 426 */             return Optional.of(((RewriteResult)recursiveTypeFamily.fold((Algebra)listAlgebra, recursiveTypeFamily1).apply(fold.index)).view().function());
/*     */           }  }
/*     */          }
/* 429 */        return Optional.empty();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static PointFreeRule seq(PointFreeRule... paramVarArgs) {
/* 450 */     return new Seq(paramVarArgs);
/*     */   }
/*     */   public static final class Seq extends Record implements PointFreeRule { private final PointFreeRule[] rules;
/* 453 */     public Seq(PointFreeRule[] param1ArrayOfPointFreeRule) { this.rules = param1ArrayOfPointFreeRule; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/datafixers/functions/PointFreeRule$Seq;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 453 */       //   #453	-> 0 } public PointFreeRule[] rules() { return this.rules; }
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> param1PointFree) {
/* 456 */       PointFree<A> pointFree = param1PointFree;
/* 457 */       for (PointFreeRule pointFreeRule : this.rules) {
/* 458 */         pointFree = pointFreeRule.rewriteOrNop(pointFree);
/*     */       }
/* 460 */       return Optional.of(pointFree);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/* 465 */       if (param1Object == this) {
/* 466 */         return true;
/*     */       }
/* 468 */       if (param1Object instanceof Seq) { Seq seq = (Seq)param1Object; if (Arrays.equals((Object[])this.rules, (Object[])seq.rules)); }  return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 473 */       return Arrays.hashCode((Object[])this.rules);
/*     */     } }
/*     */ 
/*     */   
/*     */   static PointFreeRule choice(PointFreeRule... paramVarArgs) {
/* 478 */     if (paramVarArgs.length == 1)
/* 479 */       return paramVarArgs[0]; 
/* 480 */     if (paramVarArgs.length == 2) {
/* 481 */       return new Choice2(paramVarArgs[0], paramVarArgs[1]);
/*     */     }
/* 483 */     return new Choice(paramVarArgs);
/*     */   }
/*     */   public static final class Choice2 extends Record implements PointFreeRule { private final PointFreeRule first; private final PointFreeRule second;
/* 486 */     public Choice2(PointFreeRule param1PointFreeRule1, PointFreeRule param1PointFreeRule2) { this.first = param1PointFreeRule1; this.second = param1PointFreeRule2; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/datafixers/functions/PointFreeRule$Choice2;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 486 */       //   #486	-> 0 } public PointFreeRule first() { return this.first; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/functions/PointFreeRule$Choice2;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #486	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/functions/PointFreeRule$Choice2;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 486 */       //   #486	-> 0 } public PointFreeRule second() { return this.second; }
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> param1PointFree) {
/* 489 */       Optional<? extends PointFree<A>> optional = this.first.rewrite(param1PointFree);
/* 490 */       if (optional.isPresent()) {
/* 491 */         return optional;
/*     */       }
/* 493 */       return this.second.rewrite(param1PointFree);
/*     */     } }
/*     */   public static final class Choice extends Record implements PointFreeRule { private final PointFreeRule[] rules;
/*     */     
/* 497 */     public Choice(PointFreeRule[] param1ArrayOfPointFreeRule) { this.rules = param1ArrayOfPointFreeRule; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/datafixers/functions/PointFreeRule$Choice;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 497 */       //   #497	-> 0 } public PointFreeRule[] rules() { return this.rules; }
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> param1PointFree) {
/* 500 */       for (PointFreeRule pointFreeRule : this.rules) {
/* 501 */         Optional<? extends PointFree<A>> optional = pointFreeRule.rewrite(param1PointFree);
/* 502 */         if (optional.isPresent()) {
/* 503 */           return optional;
/*     */         }
/*     */       } 
/* 506 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/* 511 */       if (param1Object == this) {
/* 512 */         return true;
/*     */       }
/* 514 */       if (param1Object instanceof Choice) { Choice choice = (Choice)param1Object; if (Arrays.equals((Object[])this.rules, (Object[])choice.rules)); }  return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 519 */       return Arrays.hashCode((Object[])this.rules);
/*     */     } }
/*     */ 
/*     */   
/*     */   static PointFreeRule all(PointFreeRule paramPointFreeRule) {
/* 524 */     return new All(paramPointFreeRule);
/*     */   }
/*     */   
/*     */   static PointFreeRule one(PointFreeRule paramPointFreeRule) {
/* 528 */     return new One(paramPointFreeRule);
/*     */   }
/*     */   
/*     */   static PointFreeRule once(PointFreeRule paramPointFreeRule) {
/* 532 */     return new Once(paramPointFreeRule);
/*     */   }
/*     */   public static final class Once extends Record implements PointFreeRule { private final PointFreeRule rule;
/* 535 */     public Once(PointFreeRule param1PointFreeRule) { this.rule = param1PointFreeRule; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/datafixers/functions/PointFreeRule$Once;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #535	-> 0 } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/functions/PointFreeRule$Once;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #535	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/functions/PointFreeRule$Once;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 535 */       //   #535	-> 0 } public PointFreeRule rule() { return this.rule; }
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> param1PointFree) {
/* 538 */       Optional<? extends PointFree<A>> optional = this.rule.rewrite(param1PointFree);
/* 539 */       if (optional.isPresent()) {
/* 540 */         return optional;
/*     */       }
/* 542 */       return param1PointFree.one(this);
/*     */     } }
/*     */ 
/*     */   
/*     */   static PointFreeRule many(PointFreeRule paramPointFreeRule) {
/* 547 */     return new Many(paramPointFreeRule);
/*     */   }
/*     */   
/*     */   static PointFreeRule everywhere(PointFreeRule paramPointFreeRule1, PointFreeRule paramPointFreeRule2) {
/* 551 */     return new Everywhere(paramPointFreeRule1, paramPointFreeRule2);
/*     */   }
/*     */   public static final class Everywhere extends Record implements PointFreeRule { private final PointFreeRule topDown; private final PointFreeRule bottomUp;
/* 554 */     public Everywhere(PointFreeRule param1PointFreeRule1, PointFreeRule param1PointFreeRule2) { this.topDown = param1PointFreeRule1; this.bottomUp = param1PointFreeRule2; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/datafixers/functions/PointFreeRule$Everywhere;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #554	-> 0 } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/functions/PointFreeRule$Everywhere;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #554	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/functions/PointFreeRule$Everywhere;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 554 */       //   #554	-> 0 } public PointFreeRule topDown() { return this.topDown; } public PointFreeRule bottomUp() { return this.bottomUp; }
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> param1PointFree) {
/* 557 */       PointFree<A> pointFree = this.topDown.rewriteOrNop(param1PointFree);
/* 558 */       PointFree<?> pointFree1 = (PointFree)DataFixUtils.orElse(pointFree.all(this), pointFree);
/* 559 */       PointFree<?> pointFree2 = this.bottomUp.rewriteOrNop(pointFree1);
/* 560 */       return (Optional)Optional.of(pointFree2);
/*     */     } }
/*     */   public static final class All extends Record implements PointFreeRule { private final PointFreeRule rule;
/*     */     
/* 564 */     public All(PointFreeRule param1PointFreeRule) { this.rule = param1PointFreeRule; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/datafixers/functions/PointFreeRule$All;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #564	-> 0 } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/functions/PointFreeRule$All;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #564	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/functions/PointFreeRule$All;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 564 */       //   #564	-> 0 } public PointFreeRule rule() { return this.rule; }
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> param1PointFree) {
/* 567 */       return param1PointFree.all(this.rule);
/*     */     } }
/*     */   public static final class One extends Record implements PointFreeRule { private final PointFreeRule rule;
/*     */     
/* 571 */     public One(PointFreeRule param1PointFreeRule) { this.rule = param1PointFreeRule; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/datafixers/functions/PointFreeRule$One;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #571	-> 0 } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/functions/PointFreeRule$One;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #571	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/functions/PointFreeRule$One;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 571 */       //   #571	-> 0 } public PointFreeRule rule() { return this.rule; }
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> param1PointFree) {
/* 574 */       return param1PointFree.one(this.rule);
/*     */     } }
/*     */   public static final class Many extends Record implements PointFreeRule { private final PointFreeRule rule;
/*     */     
/* 578 */     public Many(PointFreeRule param1PointFreeRule) { this.rule = param1PointFreeRule; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/datafixers/functions/PointFreeRule$Many;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #578	-> 0 } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/functions/PointFreeRule$Many;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #578	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/functions/PointFreeRule$Many;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 578 */       //   #578	-> 0 } public PointFreeRule rule() { return this.rule; }
/*     */     
/*     */     public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> param1PointFree) {
/* 581 */       Optional<PointFree<A>> optional = Optional.of(param1PointFree);
/*     */       while (true) {
/* 583 */         Objects.requireNonNull(this.rule); Optional<?> optional1 = optional.flatMap(this.rule::rewrite);
/* 584 */         if (optional1.isEmpty()) {
/* 585 */           return optional;
/*     */         }
/* 587 */         optional = (Optional)optional1;
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\PointFreeRule.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */