/*     */ package com.mojang.datafixers.util;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
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
/*     */ final class Right<L, R>
/*     */   extends Either<L, R>
/*     */ {
/*     */   private final R value;
/*     */   
/*     */   public Right(R paramR) {
/*  89 */     this.value = paramR;
/*     */   }
/*     */ 
/*     */   
/*     */   public <C, D> Either<C, D> mapBoth(Function<? super L, ? extends C> paramFunction, Function<? super R, ? extends D> paramFunction1) {
/*  94 */     return new Right((R)paramFunction1.apply(this.value));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T map(Function<? super L, ? extends T> paramFunction, Function<? super R, ? extends T> paramFunction1) {
/*  99 */     return paramFunction1.apply(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Either<L, R> ifLeft(Consumer<? super L> paramConsumer) {
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Either<L, R> ifRight(Consumer<? super R> paramConsumer) {
/* 109 */     paramConsumer.accept(this.value);
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<L> left() {
/* 115 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<R> right() {
/* 120 */     return Optional.of(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 125 */     return "Right[" + String.valueOf(this.value) + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 130 */     if (this == paramObject) {
/* 131 */       return true;
/*     */     }
/* 133 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 134 */       return false;
/*     */     }
/* 136 */     Right right = (Right)paramObject;
/* 137 */     return Objects.equals(this.value, right.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 142 */     return this.value.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixer\\util\Either$Right.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */