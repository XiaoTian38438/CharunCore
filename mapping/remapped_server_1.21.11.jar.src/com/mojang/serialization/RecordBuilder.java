/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ public interface RecordBuilder<T> {
/*     */   DynamicOps<T> ops();
/*     */   
/*     */   RecordBuilder<T> add(T paramT1, T paramT2);
/*     */   
/*     */   RecordBuilder<T> add(T paramT, DataResult<T> paramDataResult);
/*     */   
/*     */   RecordBuilder<T> add(DataResult<T> paramDataResult1, DataResult<T> paramDataResult2);
/*     */   
/*     */   RecordBuilder<T> withErrorsFrom(DataResult<?> paramDataResult);
/*     */   
/*     */   RecordBuilder<T> setLifecycle(Lifecycle paramLifecycle);
/*     */   
/*     */   RecordBuilder<T> mapError(UnaryOperator<String> paramUnaryOperator);
/*     */   
/*     */   DataResult<T> build(T paramT);
/*     */   
/*     */   default DataResult<T> build(DataResult<T> paramDataResult) {
/*  27 */     return paramDataResult.flatMap(this::build);
/*     */   }
/*     */   
/*     */   default RecordBuilder<T> add(String paramString, T paramT) {
/*  31 */     return add(ops().createString(paramString), paramT);
/*     */   }
/*     */   
/*     */   default RecordBuilder<T> add(String paramString, DataResult<T> paramDataResult) {
/*  35 */     return add(ops().createString(paramString), paramDataResult);
/*     */   }
/*     */   
/*     */   default <E> RecordBuilder<T> add(String paramString, E paramE, Encoder<E> paramEncoder) {
/*  39 */     return add(paramString, paramEncoder.encodeStart(ops(), paramE));
/*     */   }
/*     */   
/*     */   public static abstract class AbstractBuilder<T, R> implements RecordBuilder<T> {
/*     */     private final DynamicOps<T> ops;
/*  44 */     protected DataResult<R> builder = DataResult.success(initBuilder(), Lifecycle.stable());
/*     */     
/*     */     protected AbstractBuilder(DynamicOps<T> param1DynamicOps) {
/*  47 */       this.ops = param1DynamicOps;
/*     */     }
/*     */ 
/*     */     
/*     */     public DynamicOps<T> ops() {
/*  52 */       return this.ops;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DataResult<T> build(T param1T) {
/*  61 */       DataResult<?> dataResult = this.builder.flatMap(param1Object2 -> build((R)param1Object2, (T)param1Object1));
/*  62 */       this.builder = DataResult.success(initBuilder(), Lifecycle.stable());
/*  63 */       return (DataResult)dataResult;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> withErrorsFrom(DataResult<?> param1DataResult) {
/*  68 */       this.builder = this.builder.flatMap(param1Object -> param1DataResult.map(()));
/*  69 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> setLifecycle(Lifecycle param1Lifecycle) {
/*  74 */       this.builder = this.builder.setLifecycle(param1Lifecycle);
/*  75 */       return this;
/*     */     }
/*     */     protected abstract R initBuilder();
/*     */     protected abstract DataResult<T> build(R param1R, T param1T);
/*     */     public RecordBuilder<T> mapError(UnaryOperator<String> param1UnaryOperator) {
/*  80 */       this.builder = this.builder.mapError(param1UnaryOperator);
/*  81 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract class AbstractStringBuilder<T, R> extends AbstractBuilder<T, R> {
/*     */     protected AbstractStringBuilder(DynamicOps<T> param1DynamicOps) {
/*  87 */       super(param1DynamicOps);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(String param1String, T param1T) {
/*  94 */       this.builder = this.builder.map(param1Object2 -> append(param1String, (T)param1Object1, (R)param1Object2));
/*  95 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(String param1String, DataResult<T> param1DataResult) {
/* 100 */       this.builder = this.builder.apply2stable((param1Object1, param1Object2) -> append(param1String, (T)param1Object2, (R)param1Object1), param1DataResult);
/* 101 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(T param1T1, T param1T2) {
/* 106 */       this.builder = ops().getStringValue(param1T1).flatMap(param1String -> {
/*     */             add(param1String, (T)param1Object);
/*     */             return this.builder;
/*     */           });
/* 110 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(T param1T, DataResult<T> param1DataResult) {
/* 115 */       this.builder = ops().getStringValue(param1T).flatMap(param1String -> {
/*     */             add(param1String, param1DataResult);
/*     */             return this.builder;
/*     */           });
/* 119 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(DataResult<T> param1DataResult1, DataResult<T> param1DataResult2) {
/* 124 */       Objects.requireNonNull(ops()); this.builder = param1DataResult1.flatMap(ops()::getStringValue).flatMap(param1String -> {
/*     */             add(param1String, param1DataResult);
/*     */             return this.builder;
/*     */           });
/* 128 */       return this;
/*     */     }
/*     */     
/*     */     protected abstract R append(String param1String, T param1T, R param1R); }
/*     */   
/*     */   public static abstract class AbstractUniversalBuilder<T, R> extends AbstractBuilder<T, R> { protected AbstractUniversalBuilder(DynamicOps<T> param1DynamicOps) {
/* 134 */       super(param1DynamicOps);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(T param1T1, T param1T2) {
/* 141 */       this.builder = this.builder.map(param1Object3 -> append((T)param1Object1, (T)param1Object2, (R)param1Object3));
/* 142 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(T param1T, DataResult<T> param1DataResult) {
/* 147 */       this.builder = this.builder.apply2stable((param1Object2, param1Object3) -> append((T)param1Object1, (T)param1Object3, (R)param1Object2), param1DataResult);
/* 148 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(DataResult<T> param1DataResult1, DataResult<T> param1DataResult2) {
/* 153 */       this.builder = this.builder.ap(param1DataResult1.apply2stable((param1Object1, param1Object2) -> (), param1DataResult2));
/* 154 */       return this;
/*     */     }
/*     */     
/*     */     protected abstract R append(T param1T1, T param1T2, R param1R); }
/*     */   
/*     */   public static final class MapBuilder<T> extends AbstractUniversalBuilder<T, ImmutableMap.Builder<T, T>> { public MapBuilder(DynamicOps<T> param1DynamicOps) {
/* 160 */       super(param1DynamicOps);
/*     */     }
/*     */ 
/*     */     
/*     */     protected ImmutableMap.Builder<T, T> initBuilder() {
/* 165 */       return ImmutableMap.builder();
/*     */     }
/*     */ 
/*     */     
/*     */     protected ImmutableMap.Builder<T, T> append(T param1T1, T param1T2, ImmutableMap.Builder<T, T> param1Builder) {
/* 170 */       return param1Builder.put(param1T1, param1T2);
/*     */     }
/*     */ 
/*     */     
/*     */     protected DataResult<T> build(ImmutableMap.Builder<T, T> param1Builder, T param1T) {
/* 175 */       return ops().mergeToMap(param1T, (Map<T, T>)param1Builder.buildKeepingLast());
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\RecordBuilder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */