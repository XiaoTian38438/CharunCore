/*    */ package com.mojang.datafixers.util;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class Left<L, R>
/*    */   extends Either<L, R>
/*    */ {
/*    */   private final L value;
/*    */   
/*    */   public Left(L paramL) {
/* 28 */     this.value = paramL;
/*    */   }
/*    */ 
/*    */   
/*    */   public <C, D> Either<C, D> mapBoth(Function<? super L, ? extends C> paramFunction, Function<? super R, ? extends D> paramFunction1) {
/* 33 */     return new Left((L)paramFunction.apply(this.value));
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T map(Function<? super L, ? extends T> paramFunction, Function<? super R, ? extends T> paramFunction1) {
/* 38 */     return paramFunction.apply(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public Either<L, R> ifLeft(Consumer<? super L> paramConsumer) {
/* 43 */     paramConsumer.accept(this.value);
/* 44 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public Either<L, R> ifRight(Consumer<? super R> paramConsumer) {
/* 49 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<L> left() {
/* 54 */     return Optional.of(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<R> right() {
/* 59 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 64 */     return "Left[" + String.valueOf(this.value) + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 69 */     if (this == paramObject) {
/* 70 */       return true;
/*    */     }
/* 72 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 73 */       return false;
/*    */     }
/* 75 */     Left left = (Left)paramObject;
/* 76 */     return Objects.equals(this.value, left.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 81 */     return this.value.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixer\\util\Either$Left.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */