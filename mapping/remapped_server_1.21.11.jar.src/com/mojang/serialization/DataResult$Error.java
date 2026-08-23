/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Error<R>
/*     */   extends Record
/*     */   implements DataResult<R>
/*     */ {
/*     */   private final Supplier<String> messageSupplier;
/*     */   private final Optional<R> partialValue;
/*     */   private final Lifecycle lifecycle;
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/serialization/DataResult$Error;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #250	-> 0
/*     */   }
/*     */   
/*     */   public final boolean equals(Object paramObject) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/serialization/DataResult$Error;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #250	-> 0
/*     */   }
/*     */   
/*     */   public Error(Supplier<String> paramSupplier, Optional<R> paramOptional, Lifecycle paramLifecycle) {
/* 250 */     this.messageSupplier = paramSupplier; this.partialValue = paramOptional; this.lifecycle = paramLifecycle; } public Supplier<String> messageSupplier() { return this.messageSupplier; } public Optional<R> partialValue() { return this.partialValue; } public Lifecycle lifecycle() { return this.lifecycle; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String message() {
/* 256 */     return this.messageSupplier.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<R> result() {
/* 261 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Error<R>> error() {
/* 266 */     return Optional.of(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasResultOrPartial() {
/* 271 */     return this.partialValue.isPresent();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<R> resultOrPartial(Consumer<String> paramConsumer) {
/* 276 */     paramConsumer.accept(this.messageSupplier.get());
/* 277 */     return this.partialValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<R> resultOrPartial() {
/* 282 */     return this.partialValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends Throwable> R getOrThrow(Function<String, E> paramFunction) throws E {
/* 287 */     throw (E)paramFunction.apply(message());
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends Throwable> R getPartialOrThrow(Function<String, E> paramFunction) throws E {
/* 292 */     if (this.partialValue.isPresent()) {
/* 293 */       return this.partialValue.get();
/*     */     }
/* 295 */     throw (E)paramFunction.apply(message());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Error<T> map(Function<? super R, ? extends T> paramFunction) {
/* 301 */     if (this.partialValue.isEmpty()) {
/* 302 */       return this;
/*     */     }
/* 304 */     return new Error(this.messageSupplier, this.partialValue.map(paramFunction), this.lifecycle);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T mapOrElse(Function<? super R, ? extends T> paramFunction, Function<? super Error<R>, ? extends T> paramFunction1) {
/* 309 */     return paramFunction1.apply(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<R> ifSuccess(Consumer<? super R> paramConsumer) {
/* 314 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<R> ifError(Consumer<? super Error<R>> paramConsumer) {
/* 319 */     paramConsumer.accept(this);
/* 320 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<R> promotePartial(Consumer<String> paramConsumer) {
/* 325 */     paramConsumer.accept(this.messageSupplier.get());
/* 326 */     return this.partialValue.<DataResult<R>>map(paramObject -> new DataResult.Success(paramObject, this.lifecycle)).orElse(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <R2> Error<R2> flatMap(Function<? super R, ? extends DataResult<R2>> paramFunction) {
/* 332 */     if (this.partialValue.isEmpty()) {
/* 333 */       return this;
/*     */     }
/* 335 */     DataResult dataResult = paramFunction.apply(this.partialValue.get());
/* 336 */     Lifecycle lifecycle = this.lifecycle.add(dataResult.lifecycle());
/* 337 */     if (dataResult instanceof DataResult.Success) { DataResult.Success success = (DataResult.Success)dataResult;
/* 338 */       return new Error(this.messageSupplier, Optional.of(success.value), lifecycle); }
/* 339 */      if (dataResult instanceof Error) { Error error = (Error)dataResult;
/* 340 */       return new Error(() -> DataResult.appendMessages(this.messageSupplier.get(), paramError.messageSupplier.get()), error.partialValue, lifecycle); }
/*     */ 
/*     */     
/* 343 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <R2> Error<R2> ap(DataResult<Function<R, R2>> paramDataResult) {
/* 349 */     Lifecycle lifecycle = this.lifecycle.add(paramDataResult.lifecycle());
/* 350 */     if (paramDataResult instanceof DataResult.Success) { DataResult.Success success = (DataResult.Success)paramDataResult;
/* 351 */       return new Error(this.messageSupplier, this.partialValue.map((Function<? super R, ? extends R>)success.value), lifecycle); }
/* 352 */      if (paramDataResult instanceof Error) { Error error = (Error)paramDataResult;
/* 353 */       return new Error(() -> DataResult.appendMessages(this.messageSupplier.get(), paramError.messageSupplier.get()), this.partialValue
/*     */           
/* 355 */           .flatMap(paramObject -> paramError.partialValue.map(())), lifecycle); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 360 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Error<R> setPartial(Supplier<R> paramSupplier) {
/* 366 */     return setPartial(paramSupplier.get());
/*     */   }
/*     */ 
/*     */   
/*     */   public Error<R> setPartial(R paramR) {
/* 371 */     return new Error(this.messageSupplier, Optional.of(paramR), this.lifecycle);
/*     */   }
/*     */ 
/*     */   
/*     */   public Error<R> mapError(UnaryOperator<String> paramUnaryOperator) {
/* 376 */     return new Error(() -> (String)paramUnaryOperator.apply(this.messageSupplier.get()), this.partialValue, this.lifecycle);
/*     */   }
/*     */ 
/*     */   
/*     */   public Error<R> setLifecycle(Lifecycle paramLifecycle) {
/* 381 */     if (this.lifecycle.equals(paramLifecycle)) {
/* 382 */       return this;
/*     */     }
/* 384 */     return new Error(this.messageSupplier, this.partialValue, paramLifecycle);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSuccess() {
/* 389 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 394 */     return "DataResult.Error['" + message() + "'" + (String)this.partialValue.<String>map(paramObject -> ": " + String.valueOf(paramObject)).orElse("") + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\DataResult$Error.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */