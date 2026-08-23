/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface DataResult<R>
/*     */   extends App<DataResult.Mu, R>
/*     */ {
/*     */   public static final class Mu
/*     */     implements K1 {}
/*     */   
/*     */   static <R> DataResult<R> unbox(App<Mu, R> paramApp) {
/*  25 */     return (DataResult)paramApp;
/*     */   }
/*     */   
/*     */   static <R> DataResult<R> success(R paramR) {
/*  29 */     return success(paramR, Lifecycle.experimental());
/*     */   }
/*     */   
/*     */   static <R> DataResult<R> error(Supplier<String> paramSupplier, R paramR) {
/*  33 */     return error(paramSupplier, paramR, Lifecycle.experimental());
/*     */   }
/*     */   
/*     */   static <R> DataResult<R> error(Supplier<String> paramSupplier) {
/*  37 */     return error(paramSupplier, Lifecycle.experimental());
/*     */   }
/*     */   
/*     */   static <R> DataResult<R> success(R paramR, Lifecycle paramLifecycle) {
/*  41 */     return new Success<>(paramR, paramLifecycle);
/*     */   }
/*     */   
/*     */   static <R> DataResult<R> error(Supplier<String> paramSupplier, R paramR, Lifecycle paramLifecycle) {
/*  45 */     return new Error<>(paramSupplier, Optional.of(paramR), paramLifecycle);
/*     */   }
/*     */   
/*     */   static <R> DataResult<R> error(Supplier<String> paramSupplier, Lifecycle paramLifecycle) {
/*  49 */     return new Error<>(paramSupplier, Optional.empty(), paramLifecycle);
/*     */   }
/*     */   
/*     */   static <K, V> Function<K, DataResult<V>> partialGet(Function<K, V> paramFunction, Supplier<String> paramSupplier) {
/*  53 */     return paramObject -> (DataResult)Optional.ofNullable(paramFunction.apply(paramObject)).map(DataResult::success).orElseGet(());
/*     */   }
/*     */   
/*     */   static Instance instance() {
/*  57 */     return Instance.INSTANCE;
/*     */   }
/*     */   
/*     */   static String appendMessages(String paramString1, String paramString2) {
/*  61 */     return paramString1 + "; " + paramString1;
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
/*     */   default R getOrThrow() {
/*  81 */     return getOrThrow(IllegalStateException::new);
/*     */   }
/*     */   
/*     */   default R getPartialOrThrow() {
/*  85 */     return getPartialOrThrow(IllegalStateException::new);
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
/*     */   
/*     */   default <R2, S> DataResult<S> apply2(BiFunction<R, R2, S> paramBiFunction, DataResult<R2> paramDataResult) {
/* 106 */     return unbox(instance().apply2(paramBiFunction, this, paramDataResult));
/*     */   }
/*     */   
/*     */   default <R2, S> DataResult<S> apply2stable(BiFunction<R, R2, S> paramBiFunction, DataResult<R2> paramDataResult) {
/* 110 */     Instance instance = instance();
/* 111 */     DataResult dataResult = unbox(instance.point(paramBiFunction)).setLifecycle(Lifecycle.stable());
/* 112 */     return unbox(instance.ap2(dataResult, this, paramDataResult));
/*     */   }
/*     */   
/*     */   default <R2, R3, S> DataResult<S> apply3(Function3<R, R2, R3, S> paramFunction3, DataResult<R2> paramDataResult, DataResult<R3> paramDataResult1) {
/* 116 */     return unbox(instance().apply3(paramFunction3, this, paramDataResult, paramDataResult1));
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
/*     */   default DataResult<R> addLifecycle(Lifecycle paramLifecycle) {
/* 128 */     return setLifecycle(lifecycle().add(paramLifecycle));
/*     */   } Optional<R> result(); Optional<Error<R>> error(); Lifecycle lifecycle(); boolean hasResultOrPartial(); Optional<R> resultOrPartial(Consumer<String> paramConsumer); Optional<R> resultOrPartial(); <E extends Throwable> R getOrThrow(Function<String, E> paramFunction) throws E;
/*     */   <E extends Throwable> R getPartialOrThrow(Function<String, E> paramFunction) throws E;
/*     */   <T> DataResult<T> map(Function<? super R, ? extends T> paramFunction);
/*     */   <T> T mapOrElse(Function<? super R, ? extends T> paramFunction, Function<? super Error<R>, ? extends T> paramFunction1);
/*     */   default boolean isError() {
/* 134 */     return !isSuccess();
/*     */   } DataResult<R> ifSuccess(Consumer<? super R> paramConsumer); DataResult<R> ifError(Consumer<? super Error<R>> paramConsumer); DataResult<R> promotePartial(Consumer<String> paramConsumer); <R2> DataResult<R2> flatMap(Function<? super R, ? extends DataResult<R2>> paramFunction); <R2> DataResult<R2> ap(DataResult<Function<R, R2>> paramDataResult); DataResult<R> setPartial(Supplier<R> paramSupplier); DataResult<R> setPartial(R paramR); DataResult<R> mapError(UnaryOperator<String> paramUnaryOperator); DataResult<R> setLifecycle(Lifecycle paramLifecycle); boolean isSuccess(); public static final class Success<R> extends Record implements DataResult<R> {
/*     */     private final R value;
/* 137 */     public Success(R param1R, Lifecycle param1Lifecycle) { this.value = param1R; this.lifecycle = param1Lifecycle; } private final Lifecycle lifecycle; public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/serialization/DataResult$Success;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #137	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/serialization/DataResult$Success;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 137 */       //   #137	-> 0 } public R value() { return this.value; } public Lifecycle lifecycle() { return this.lifecycle; }
/*     */     
/*     */     public Optional<R> result() {
/* 140 */       return Optional.of(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<DataResult.Error<R>> error() {
/* 145 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasResultOrPartial() {
/* 150 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<R> resultOrPartial(Consumer<String> param1Consumer) {
/* 155 */       return Optional.of(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<R> resultOrPartial() {
/* 160 */       return Optional.of(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public <E extends Throwable> R getOrThrow(Function<String, E> param1Function) throws E {
/* 165 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public <E extends Throwable> R getPartialOrThrow(Function<String, E> param1Function) throws E {
/* 170 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> DataResult<T> map(Function<? super R, ? extends T> param1Function) {
/* 175 */       return new Success((R)param1Function.apply(this.value), this.lifecycle);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T mapOrElse(Function<? super R, ? extends T> param1Function, Function<? super DataResult.Error<R>, ? extends T> param1Function1) {
/* 180 */       return param1Function.apply(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public DataResult<R> ifSuccess(Consumer<? super R> param1Consumer) {
/* 185 */       param1Consumer.accept(this.value);
/* 186 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataResult<R> ifError(Consumer<? super DataResult.Error<R>> param1Consumer) {
/* 191 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataResult<R> promotePartial(Consumer<String> param1Consumer) {
/* 196 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public <R2> DataResult<R2> flatMap(Function<? super R, ? extends DataResult<R2>> param1Function) {
/* 201 */       return ((DataResult<R2>)param1Function.apply(this.value)).addLifecycle(this.lifecycle);
/*     */     }
/*     */ 
/*     */     
/*     */     public <R2> DataResult<R2> ap(DataResult<Function<R, R2>> param1DataResult) {
/* 206 */       Lifecycle lifecycle = this.lifecycle.add(param1DataResult.lifecycle());
/* 207 */       if (param1DataResult instanceof Success) { Success success = (Success)param1DataResult;
/* 208 */         return new Success(((Function<R, R>)success.value).apply(this.value), lifecycle); }
/* 209 */        if (param1DataResult instanceof DataResult.Error) { DataResult.Error error = (DataResult.Error)param1DataResult;
/* 210 */         return new DataResult.Error<>(error.messageSupplier, error.partialValue.map(param1Function -> param1Function.apply(this.value)), lifecycle); }
/*     */       
/* 212 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public DataResult<R> setPartial(Supplier<R> param1Supplier) {
/* 218 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataResult<R> setPartial(R param1R) {
/* 223 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataResult<R> mapError(UnaryOperator<String> param1UnaryOperator) {
/* 228 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataResult<R> setLifecycle(Lifecycle param1Lifecycle) {
/* 233 */       if (this.lifecycle.equals(param1Lifecycle)) {
/* 234 */         return this;
/*     */       }
/* 236 */       return new Success(this.value, param1Lifecycle);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSuccess() {
/* 241 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 246 */       return "DataResult.Success[" + String.valueOf(this.value) + "]";
/*     */     } }
/*     */   public static final class Error<R> extends Record implements DataResult<R> { private final Supplier<String> messageSupplier; private final Optional<R> partialValue; private final Lifecycle lifecycle;
/*     */     
/* 250 */     public Error(Supplier<String> param1Supplier, Optional<R> param1Optional, Lifecycle param1Lifecycle) { this.messageSupplier = param1Supplier; this.partialValue = param1Optional; this.lifecycle = param1Lifecycle; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/serialization/DataResult$Error;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #250	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/serialization/DataResult$Error;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 250 */       //   #250	-> 0 } public Supplier<String> messageSupplier() { return this.messageSupplier; } public Optional<R> partialValue() { return this.partialValue; } public Lifecycle lifecycle() { return this.lifecycle; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String message() {
/* 256 */       return this.messageSupplier.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<R> result() {
/* 261 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Error<R>> error() {
/* 266 */       return Optional.of(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasResultOrPartial() {
/* 271 */       return this.partialValue.isPresent();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<R> resultOrPartial(Consumer<String> param1Consumer) {
/* 276 */       param1Consumer.accept(this.messageSupplier.get());
/* 277 */       return this.partialValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<R> resultOrPartial() {
/* 282 */       return this.partialValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public <E extends Throwable> R getOrThrow(Function<String, E> param1Function) throws E {
/* 287 */       throw (E)param1Function.apply(message());
/*     */     }
/*     */ 
/*     */     
/*     */     public <E extends Throwable> R getPartialOrThrow(Function<String, E> param1Function) throws E {
/* 292 */       if (this.partialValue.isPresent()) {
/* 293 */         return this.partialValue.get();
/*     */       }
/* 295 */       throw (E)param1Function.apply(message());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> Error<T> map(Function<? super R, ? extends T> param1Function) {
/* 301 */       if (this.partialValue.isEmpty()) {
/* 302 */         return this;
/*     */       }
/* 304 */       return new Error(this.messageSupplier, this.partialValue.map(param1Function), this.lifecycle);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T mapOrElse(Function<? super R, ? extends T> param1Function, Function<? super Error<R>, ? extends T> param1Function1) {
/* 309 */       return param1Function1.apply(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public DataResult<R> ifSuccess(Consumer<? super R> param1Consumer) {
/* 314 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataResult<R> ifError(Consumer<? super Error<R>> param1Consumer) {
/* 319 */       param1Consumer.accept(this);
/* 320 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataResult<R> promotePartial(Consumer<String> param1Consumer) {
/* 325 */       param1Consumer.accept(this.messageSupplier.get());
/* 326 */       return this.partialValue.<DataResult<R>>map(param1Object -> new DataResult.Success(param1Object, this.lifecycle)).orElse(this);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <R2> Error<R2> flatMap(Function<? super R, ? extends DataResult<R2>> param1Function) {
/* 332 */       if (this.partialValue.isEmpty()) {
/* 333 */         return this;
/*     */       }
/* 335 */       DataResult dataResult = param1Function.apply(this.partialValue.get());
/* 336 */       Lifecycle lifecycle = this.lifecycle.add(dataResult.lifecycle());
/* 337 */       if (dataResult instanceof DataResult.Success) { DataResult.Success success = (DataResult.Success)dataResult;
/* 338 */         return new Error(this.messageSupplier, Optional.of(success.value), lifecycle); }
/* 339 */        if (dataResult instanceof Error) { Error error = (Error)dataResult;
/* 340 */         return new Error(() -> DataResult.appendMessages(this.messageSupplier.get(), param1Error.messageSupplier.get()), error.partialValue, lifecycle); }
/*     */ 
/*     */       
/* 343 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <R2> Error<R2> ap(DataResult<Function<R, R2>> param1DataResult) {
/* 349 */       Lifecycle lifecycle = this.lifecycle.add(param1DataResult.lifecycle());
/* 350 */       if (param1DataResult instanceof DataResult.Success) { DataResult.Success success = (DataResult.Success)param1DataResult;
/* 351 */         return new Error(this.messageSupplier, this.partialValue.map((Function<? super R, ? extends R>)success.value), lifecycle); }
/* 352 */        if (param1DataResult instanceof Error) { Error error = (Error)param1DataResult;
/* 353 */         return new Error(() -> DataResult.appendMessages(this.messageSupplier.get(), param1Error.messageSupplier.get()), this.partialValue
/*     */             
/* 355 */             .flatMap(param1Object -> param1Error.partialValue.map(())), lifecycle); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 360 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Error<R> setPartial(Supplier<R> param1Supplier) {
/* 366 */       return setPartial(param1Supplier.get());
/*     */     }
/*     */ 
/*     */     
/*     */     public Error<R> setPartial(R param1R) {
/* 371 */       return new Error(this.messageSupplier, Optional.of(param1R), this.lifecycle);
/*     */     }
/*     */ 
/*     */     
/*     */     public Error<R> mapError(UnaryOperator<String> param1UnaryOperator) {
/* 376 */       return new Error(() -> (String)param1UnaryOperator.apply(this.messageSupplier.get()), this.partialValue, this.lifecycle);
/*     */     }
/*     */ 
/*     */     
/*     */     public Error<R> setLifecycle(Lifecycle param1Lifecycle) {
/* 381 */       if (this.lifecycle.equals(param1Lifecycle)) {
/* 382 */         return this;
/*     */       }
/* 384 */       return new Error(this.messageSupplier, this.partialValue, param1Lifecycle);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSuccess() {
/* 389 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 394 */       return "DataResult.Error['" + message() + "'" + (String)this.partialValue.<String>map(param1Object -> ": " + String.valueOf(param1Object)).orElse("") + "]";
/*     */     } }
/*     */ 
/*     */   
/*     */   public enum Instance implements Applicative<Mu, Instance.Mu> {
/* 399 */     INSTANCE;
/*     */     
/*     */     public static final class Mu
/*     */       implements Applicative.Mu {}
/*     */     
/*     */     public <T, R> App<DataResult.Mu, R> map(Function<? super T, ? extends R> param1Function, App<DataResult.Mu, T> param1App) {
/* 405 */       return DataResult.<T>unbox(param1App).map(param1Function);
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> App<DataResult.Mu, A> point(A param1A) {
/* 410 */       return DataResult.success(param1A);
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, R> Function<App<DataResult.Mu, A>, App<DataResult.Mu, R>> lift1(App<DataResult.Mu, Function<A, R>> param1App) {
/* 415 */       return param1App2 -> ap(param1App1, param1App2);
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, R> App<DataResult.Mu, R> ap(App<DataResult.Mu, Function<A, R>> param1App, App<DataResult.Mu, A> param1App1) {
/* 420 */       return DataResult.<A>unbox(param1App1).ap(DataResult.unbox(param1App));
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, B, R> App<DataResult.Mu, R> ap2(App<DataResult.Mu, BiFunction<A, B, R>> param1App, App<DataResult.Mu, A> param1App1, App<DataResult.Mu, B> param1App2) {
/* 425 */       DataResult<BiFunction<A, B, R>> dataResult = DataResult.unbox(param1App);
/* 426 */       DataResult<A> dataResult1 = DataResult.unbox(param1App1);
/* 427 */       DataResult<B> dataResult2 = DataResult.unbox(param1App2);
/*     */ 
/*     */       
/* 430 */       if (dataResult.result().isPresent() && dataResult1
/* 431 */         .result().isPresent() && dataResult2
/* 432 */         .result().isPresent())
/*     */       {
/* 434 */         return new DataResult.Success<>((R)((BiFunction)dataResult.result().get()).apply(dataResult1
/* 435 */               .result().get(), dataResult2
/* 436 */               .result().get()), dataResult
/* 437 */             .lifecycle().add(dataResult1.lifecycle()).add(dataResult2.lifecycle()));
/*     */       }
/*     */       
/* 440 */       return super.ap2(param1App, param1App1, param1App2);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T1, T2, T3, R> App<DataResult.Mu, R> ap3(App<DataResult.Mu, Function3<T1, T2, T3, R>> param1App, App<DataResult.Mu, T1> param1App1, App<DataResult.Mu, T2> param1App2, App<DataResult.Mu, T3> param1App3) {
/* 445 */       DataResult<Function3<T1, T2, T3, R>> dataResult = DataResult.unbox(param1App);
/* 446 */       DataResult<T1> dataResult1 = DataResult.unbox(param1App1);
/* 447 */       DataResult<T2> dataResult2 = DataResult.unbox(param1App2);
/* 448 */       DataResult<T3> dataResult3 = DataResult.unbox(param1App3);
/*     */ 
/*     */       
/* 451 */       if (dataResult.result().isPresent() && dataResult1
/* 452 */         .result().isPresent() && dataResult2
/* 453 */         .result().isPresent() && dataResult3
/* 454 */         .result().isPresent())
/*     */       {
/* 456 */         return new DataResult.Success<>((R)((Function3)dataResult.result().get()).apply(dataResult1
/* 457 */               .result().get(), dataResult2
/* 458 */               .result().get(), dataResult3
/* 459 */               .result().get()), dataResult
/* 460 */             .lifecycle().add(dataResult1.lifecycle()).add(dataResult2.lifecycle()).add(dataResult3.lifecycle()));
/*     */       }
/*     */       
/* 463 */       return super.ap3(param1App, param1App1, param1App2, param1App3);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Mu implements Applicative.Mu {}
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\DataResult.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */