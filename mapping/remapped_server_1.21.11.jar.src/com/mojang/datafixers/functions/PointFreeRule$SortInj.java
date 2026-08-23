/*     */ package com.mojang.datafixers.functions;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.types.Func;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.Sum;
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
/*     */ public enum SortInj
/*     */   implements PointFreeRule.CompRewrite
/*     */ {
/* 235 */   INSTANCE;
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> paramPointFree1, PointFree<? extends Function<?, ?>> paramPointFree2) {
/* 240 */     if (paramPointFree1 instanceof Apply) { Apply<?, ?> apply = (Apply)paramPointFree1; if (paramPointFree2 instanceof Apply) { Apply<?, ?> apply1 = (Apply)paramPointFree2;
/* 241 */         PointFree pointFree1 = apply.func;
/* 242 */         PointFree pointFree2 = apply1.func;
/* 243 */         if (pointFree1 instanceof ProfunctorTransformer) { ProfunctorTransformer profunctorTransformer = (ProfunctorTransformer)pointFree1; if (pointFree2 instanceof ProfunctorTransformer) { ProfunctorTransformer profunctorTransformer1 = (ProfunctorTransformer)pointFree2;
/* 244 */             if (!Optics.isInj2(profunctorTransformer.optic.outermost())) {
/* 245 */               return Optional.empty();
/*     */             }
/*     */             
/* 248 */             if (!Optics.isInj1(profunctorTransformer1.optic.outermost())) {
/* 249 */               return Optional.empty();
/*     */             }
/*     */             
/* 252 */             return Optional.of(cap(apply, apply1)); }  }
/*     */          }
/*     */        }
/* 255 */      return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   private <R, A, A2, B, B2> R cap(Apply<?, ?> paramApply1, Apply<?, ?> paramApply2) {
/* 260 */     ProfunctorTransformer profunctorTransformer1 = (ProfunctorTransformer)paramApply1.func;
/* 261 */     ProfunctorTransformer profunctorTransformer2 = (ProfunctorTransformer)paramApply2.func;
/* 262 */     PointFree<?> pointFree1 = paramApply1.arg;
/* 263 */     PointFree<?> pointFree2 = paramApply2.arg;
/*     */     
/* 265 */     Func func1 = (Func)paramApply1.type;
/* 266 */     Func func2 = (Func)paramApply2.type;
/* 267 */     Sum.SumType sumType1 = (Sum.SumType)func2.first();
/* 268 */     Sum.SumType sumType2 = (Sum.SumType)func1.second();
/*     */     
/* 270 */     return (R)new Comp<>((PointFree<? extends Function<?, ?>>[])new PointFree[] { new Apply<>(profunctorTransformer2
/* 271 */             .castOuterUnchecked(DSL.or(sumType2.first(), sumType1.second()), (Type)sumType2), pointFree2), new Apply<>(profunctorTransformer1
/* 272 */             .castOuterUnchecked((Type)sumType1, DSL.or(sumType2.first(), sumType1.second())), pointFree1) });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\PointFreeRule$SortInj.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */