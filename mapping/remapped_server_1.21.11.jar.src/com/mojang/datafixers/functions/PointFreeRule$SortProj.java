/*     */ package com.mojang.datafixers.functions;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.types.Func;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.Product;
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
/*     */ public enum SortProj
/*     */   implements PointFreeRule.CompRewrite
/*     */ {
/* 192 */   INSTANCE;
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> paramPointFree1, PointFree<? extends Function<?, ?>> paramPointFree2) {
/* 197 */     if (paramPointFree1 instanceof Apply) { Apply<?, ?> apply = (Apply)paramPointFree1; if (paramPointFree2 instanceof Apply) { Apply<?, ?> apply1 = (Apply)paramPointFree2;
/* 198 */         PointFree pointFree1 = apply.func;
/* 199 */         PointFree pointFree2 = apply1.func;
/* 200 */         if (pointFree1 instanceof ProfunctorTransformer) { ProfunctorTransformer profunctorTransformer = (ProfunctorTransformer)pointFree1; if (pointFree2 instanceof ProfunctorTransformer) { ProfunctorTransformer profunctorTransformer1 = (ProfunctorTransformer)pointFree2;
/* 201 */             if (!Optics.isProj2(profunctorTransformer.optic.outermost())) {
/* 202 */               return Optional.empty();
/*     */             }
/*     */             
/* 205 */             if (!Optics.isProj1(profunctorTransformer1.optic.outermost())) {
/* 206 */               return Optional.empty();
/*     */             }
/*     */             
/* 209 */             return Optional.of(cap(apply, apply1)); }  }
/*     */          }
/*     */        }
/* 212 */      return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   private <R, A, A2, B, B2> R cap(Apply<?, ?> paramApply1, Apply<?, ?> paramApply2) {
/* 217 */     ProfunctorTransformer profunctorTransformer1 = (ProfunctorTransformer)paramApply1.func;
/* 218 */     ProfunctorTransformer profunctorTransformer2 = (ProfunctorTransformer)paramApply2.func;
/* 219 */     PointFree<?> pointFree1 = paramApply1.arg;
/* 220 */     PointFree<?> pointFree2 = paramApply2.arg;
/*     */     
/* 222 */     Func func1 = (Func)paramApply1.type;
/* 223 */     Func func2 = (Func)paramApply2.type;
/* 224 */     Product.ProductType productType1 = (Product.ProductType)func2.first();
/* 225 */     Product.ProductType productType2 = (Product.ProductType)func1.second();
/*     */     
/* 227 */     return (R)new Comp<>((PointFree<? extends Function<?, ?>>[])new PointFree[] { new Apply<>(profunctorTransformer2
/* 228 */             .castOuterUnchecked(DSL.and(productType2.first(), productType1.second()), (Type)productType2), pointFree2), new Apply<>(profunctorTransformer1
/* 229 */             .castOuterUnchecked((Type)productType1, DSL.and(productType2.first(), productType1.second())), pointFree1) });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\PointFreeRule$SortProj.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */