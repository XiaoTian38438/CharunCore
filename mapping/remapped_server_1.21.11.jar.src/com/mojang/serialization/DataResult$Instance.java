/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import java.util.function.BiFunction;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum Instance
/*     */   implements Applicative<DataResult.Mu, DataResult.Instance.Mu>
/*     */ {
/* 399 */   INSTANCE;
/*     */   
/*     */   public static final class Mu
/*     */     implements Applicative.Mu {}
/*     */   
/*     */   public <T, R> App<DataResult.Mu, R> map(Function<? super T, ? extends R> paramFunction, App<DataResult.Mu, T> paramApp) {
/* 405 */     return DataResult.<T>unbox(paramApp).map(paramFunction);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A> App<DataResult.Mu, A> point(A paramA) {
/* 410 */     return DataResult.success(paramA);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, R> Function<App<DataResult.Mu, A>, App<DataResult.Mu, R>> lift1(App<DataResult.Mu, Function<A, R>> paramApp) {
/* 415 */     return paramApp2 -> ap(paramApp1, paramApp2);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, R> App<DataResult.Mu, R> ap(App<DataResult.Mu, Function<A, R>> paramApp, App<DataResult.Mu, A> paramApp1) {
/* 420 */     return DataResult.<A>unbox(paramApp1).ap(DataResult.unbox(paramApp));
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B, R> App<DataResult.Mu, R> ap2(App<DataResult.Mu, BiFunction<A, B, R>> paramApp, App<DataResult.Mu, A> paramApp1, App<DataResult.Mu, B> paramApp2) {
/* 425 */     DataResult<BiFunction<A, B, R>> dataResult = DataResult.unbox(paramApp);
/* 426 */     DataResult<A> dataResult1 = DataResult.unbox(paramApp1);
/* 427 */     DataResult<B> dataResult2 = DataResult.unbox(paramApp2);
/*     */ 
/*     */     
/* 430 */     if (dataResult.result().isPresent() && dataResult1
/* 431 */       .result().isPresent() && dataResult2
/* 432 */       .result().isPresent())
/*     */     {
/* 434 */       return new DataResult.Success<>((R)((BiFunction)dataResult.result().get()).apply(dataResult1
/* 435 */             .result().get(), dataResult2
/* 436 */             .result().get()), dataResult
/* 437 */           .lifecycle().add(dataResult1.lifecycle()).add(dataResult2.lifecycle()));
/*     */     }
/*     */     
/* 440 */     return super.ap2(paramApp, paramApp1, paramApp2);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T1, T2, T3, R> App<DataResult.Mu, R> ap3(App<DataResult.Mu, Function3<T1, T2, T3, R>> paramApp, App<DataResult.Mu, T1> paramApp1, App<DataResult.Mu, T2> paramApp2, App<DataResult.Mu, T3> paramApp3) {
/* 445 */     DataResult<Function3<T1, T2, T3, R>> dataResult = DataResult.unbox(paramApp);
/* 446 */     DataResult<T1> dataResult1 = DataResult.unbox(paramApp1);
/* 447 */     DataResult<T2> dataResult2 = DataResult.unbox(paramApp2);
/* 448 */     DataResult<T3> dataResult3 = DataResult.unbox(paramApp3);
/*     */ 
/*     */     
/* 451 */     if (dataResult.result().isPresent() && dataResult1
/* 452 */       .result().isPresent() && dataResult2
/* 453 */       .result().isPresent() && dataResult3
/* 454 */       .result().isPresent())
/*     */     {
/* 456 */       return new DataResult.Success<>((R)((Function3)dataResult.result().get()).apply(dataResult1
/* 457 */             .result().get(), dataResult2
/* 458 */             .result().get(), dataResult3
/* 459 */             .result().get()), dataResult
/* 460 */           .lifecycle().add(dataResult1.lifecycle()).add(dataResult2.lifecycle()).add(dataResult3.lifecycle()));
/*     */     }
/*     */     
/* 463 */     return super.ap3(paramApp, paramApp1, paramApp2, paramApp3);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\DataResult$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */