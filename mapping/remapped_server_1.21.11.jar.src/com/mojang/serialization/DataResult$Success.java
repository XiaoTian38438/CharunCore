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
/*     */ public final class Success<R>
/*     */   extends Record
/*     */   implements DataResult<R>
/*     */ {
/*     */   private final R value;
/*     */   private final Lifecycle lifecycle;
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/serialization/DataResult$Success;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #137	-> 0
/*     */   }
/*     */   
/*     */   public final boolean equals(Object paramObject) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/serialization/DataResult$Success;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #137	-> 0
/*     */   }
/*     */   
/*     */   public Success(R paramR, Lifecycle paramLifecycle) {
/* 137 */     this.value = paramR; this.lifecycle = paramLifecycle; } public R value() { return this.value; } public Lifecycle lifecycle() { return this.lifecycle; }
/*     */   
/*     */   public Optional<R> result() {
/* 140 */     return Optional.of(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<DataResult.Error<R>> error() {
/* 145 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasResultOrPartial() {
/* 150 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<R> resultOrPartial(Consumer<String> paramConsumer) {
/* 155 */     return Optional.of(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<R> resultOrPartial() {
/* 160 */     return Optional.of(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends Throwable> R getOrThrow(Function<String, E> paramFunction) throws E {
/* 165 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends Throwable> R getPartialOrThrow(Function<String, E> paramFunction) throws E {
/* 170 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> DataResult<T> map(Function<? super R, ? extends T> paramFunction) {
/* 175 */     return new Success((R)paramFunction.apply(this.value), this.lifecycle);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T mapOrElse(Function<? super R, ? extends T> paramFunction, Function<? super DataResult.Error<R>, ? extends T> paramFunction1) {
/* 180 */     return paramFunction.apply(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<R> ifSuccess(Consumer<? super R> paramConsumer) {
/* 185 */     paramConsumer.accept(this.value);
/* 186 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<R> ifError(Consumer<? super DataResult.Error<R>> paramConsumer) {
/* 191 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<R> promotePartial(Consumer<String> paramConsumer) {
/* 196 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public <R2> DataResult<R2> flatMap(Function<? super R, ? extends DataResult<R2>> paramFunction) {
/* 201 */     return ((DataResult<R2>)paramFunction.apply(this.value)).addLifecycle(this.lifecycle);
/*     */   }
/*     */ 
/*     */   
/*     */   public <R2> DataResult<R2> ap(DataResult<Function<R, R2>> paramDataResult) {
/* 206 */     Lifecycle lifecycle = this.lifecycle.add(paramDataResult.lifecycle());
/* 207 */     if (paramDataResult instanceof Success) { Success success = (Success)paramDataResult;
/* 208 */       return new Success(((Function<R, R>)success.value).apply(this.value), lifecycle); }
/* 209 */      if (paramDataResult instanceof DataResult.Error) { DataResult.Error error = (DataResult.Error)paramDataResult;
/* 210 */       return new DataResult.Error<>(error.messageSupplier, error.partialValue.map(paramFunction -> paramFunction.apply(this.value)), lifecycle); }
/*     */     
/* 212 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataResult<R> setPartial(Supplier<R> paramSupplier) {
/* 218 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<R> setPartial(R paramR) {
/* 223 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<R> mapError(UnaryOperator<String> paramUnaryOperator) {
/* 228 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<R> setLifecycle(Lifecycle paramLifecycle) {
/* 233 */     if (this.lifecycle.equals(paramLifecycle)) {
/* 234 */       return this;
/*     */     }
/* 236 */     return new Success(this.value, paramLifecycle);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSuccess() {
/* 241 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 246 */     return "DataResult.Success[" + String.valueOf(this.value) + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\DataResult$Success.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */