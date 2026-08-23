/*     */ package com.mojang.datafixers.types;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.FieldFinder;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.View;
/*     */ import com.mojang.datafixers.functions.Functions;
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public abstract class Type<A>
/*     */   implements App<Type.Mu, A>
/*     */ {
/*  36 */   private static final Map<RewriteCacheKey, CompletableFuture<Optional<? extends RewriteResult<?, ?>>>> PENDING_REWRITE_CACHE = Maps.newConcurrentMap();
/*  37 */   private static final Map<RewriteCacheKey, Optional<? extends RewriteResult<?, ?>>> REWRITE_CACHE = Maps.newConcurrentMap(); @Nullable
/*     */   private TypeTemplate template; @Nullable
/*  39 */   private Codec<A> codec; private static final class RewriteCacheKey extends Record { private final Type<?> type; private final TypeRewriteRule rule; private final PointFreeRule optimizationRule; private RewriteCacheKey(Type<?> param1Type, TypeRewriteRule param1TypeRewriteRule, PointFreeRule param1PointFreeRule) { this.type = param1Type; this.rule = param1TypeRewriteRule; this.optimizationRule = param1PointFreeRule; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/datafixers/types/Type$RewriteCacheKey;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*  39 */       //   #39	-> 0 } public Type<?> type() { return this.type; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/types/Type$RewriteCacheKey;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/types/Type$RewriteCacheKey;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*  39 */       //   #39	-> 0 } public TypeRewriteRule rule() { return this.rule; } public PointFreeRule optimizationRule() { return this.optimizationRule; }
/*     */      }
/*     */   
/*     */   public static class Mu implements K1 {}
/*     */   
/*     */   public static <A> Type<A> unbox(App<Mu, A> paramApp) {
/*  45 */     return (Type)paramApp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RewriteResult<A, ?> rewriteOrNop(TypeRewriteRule paramTypeRewriteRule) {
/*  55 */     return (RewriteResult<A, ?>)DataFixUtils.orElseGet(paramTypeRewriteRule.rewrite(this), () -> RewriteResult.nop(this));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <S, T, A, B> RewriteResult<S, T> opticView(Type<S> paramType, RewriteResult<A, B> paramRewriteResult, TypedOptic<S, T, A, B> paramTypedOptic) {
/*  60 */     if (paramRewriteResult.view().isNop()) {
/*  61 */       return RewriteResult.nop(paramType);
/*     */     }
/*     */     
/*  64 */     return RewriteResult.create(View.create(
/*  65 */           Functions.app(
/*  66 */             Functions.profunctorTransformer(paramTypedOptic), paramRewriteResult
/*  67 */             .view().function())), paramRewriteResult
/*  68 */         .recData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RewriteResult<A, ?> all(TypeRewriteRule paramTypeRewriteRule, boolean paramBoolean1, boolean paramBoolean2) {
/*  76 */     return RewriteResult.nop(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<RewriteResult<A, ?>> one(TypeRewriteRule paramTypeRewriteRule) {
/*  83 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<RewriteResult<A, ?>> everywhere(TypeRewriteRule paramTypeRewriteRule, PointFreeRule paramPointFreeRule, boolean paramBoolean1, boolean paramBoolean2) {
/*  87 */     TypeRewriteRule typeRewriteRule = TypeRewriteRule.seq(TypeRewriteRule.orElse(paramTypeRewriteRule, TypeRewriteRule::nop), TypeRewriteRule.all(TypeRewriteRule.everywhere(paramTypeRewriteRule, paramPointFreeRule, paramBoolean1, paramBoolean2), paramBoolean1, paramBoolean2));
/*  88 */     return rewrite(typeRewriteRule, paramPointFreeRule);
/*     */   }
/*     */   
/*     */   public Type<?> updateMu(RecursiveTypeFamily paramRecursiveTypeFamily) {
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public TypeTemplate template() {
/*  96 */     if (this.template == null) {
/*  97 */       this.template = buildTemplate();
/*     */     }
/*  99 */     return this.template;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String paramString, int paramInt) {
/* 105 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<Type<?>> findCheckedType(int paramInt) {
/* 109 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public final <T> DataResult<Pair<A, Dynamic<T>>> read(Dynamic<T> paramDynamic) {
/* 113 */     return codec().decode(paramDynamic.getOps(), paramDynamic.getValue()).map(paramPair -> paramPair.mapSecond(()));
/*     */   }
/*     */   
/*     */   public final Codec<A> codec() {
/* 117 */     if (this.codec == null) {
/* 118 */       this.codec = buildCodec();
/*     */     }
/* 120 */     return this.codec;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> DataResult<T> write(DynamicOps<T> paramDynamicOps, A paramA) {
/* 126 */     return codec().encode(paramA, paramDynamicOps, paramDynamicOps.empty());
/*     */   }
/*     */   
/*     */   public final <T> DataResult<Dynamic<T>> writeDynamic(DynamicOps<T> paramDynamicOps, A paramA) {
/* 130 */     return write(paramDynamicOps, paramA).map(paramObject -> new Dynamic(paramDynamicOps, paramObject));
/*     */   }
/*     */   
/*     */   public <T> DataResult<Pair<Typed<A>, T>> readTyped(Dynamic<T> paramDynamic) {
/* 134 */     return readTyped(paramDynamic.getOps(), (T)paramDynamic.getValue());
/*     */   }
/*     */   
/*     */   public <T> DataResult<Pair<Typed<A>, T>> readTyped(DynamicOps<T> paramDynamicOps, T paramT) {
/* 138 */     return codec().decode(paramDynamicOps, paramT).map(paramPair -> paramPair.mapFirst(()));
/*     */   }
/*     */   
/*     */   public <T> DataResult<Pair<Optional<?>, T>> read(DynamicOps<T> paramDynamicOps, TypeRewriteRule paramTypeRewriteRule, PointFreeRule paramPointFreeRule, T paramT) {
/* 142 */     return codec().decode(paramDynamicOps, paramT).map(paramPair -> paramPair.mapFirst(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> DataResult<T> readAndWrite(DynamicOps<T> paramDynamicOps, Type<?> paramType, TypeRewriteRule paramTypeRewriteRule, PointFreeRule paramPointFreeRule, T paramT) {
/* 149 */     Optional<RewriteResult<A, ?>> optional = rewrite(paramTypeRewriteRule, paramPointFreeRule);
/* 150 */     if (!optional.isPresent()) {
/* 151 */       return DataResult.error(() -> "Could not build a rewrite rule: " + String.valueOf(paramTypeRewriteRule) + " " + String.valueOf(paramPointFreeRule), paramT);
/*     */     }
/* 153 */     View view = ((RewriteResult)optional.get()).view();
/* 154 */     if (view.isNop()) {
/* 155 */       return DataResult.success(paramT);
/*     */     }
/*     */     
/* 158 */     return codec().decode(paramDynamicOps, paramT).flatMap(paramPair -> capWrite(paramDynamicOps, paramType, paramPair.getSecond(), paramPair.getFirst(), paramView));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <T, B> DataResult<T> capWrite(DynamicOps<T> paramDynamicOps, Type<?> paramType, T paramT, A paramA, View<A, B> paramView) {
/* 164 */     if (!paramType.equals(paramView.newType(), true, true)) {
/* 165 */       return DataResult.error(() -> "Rewritten type doesn't match");
/*     */     }
/* 167 */     Object object = ((Function<A, Object>)paramView.function().evalCached().apply(paramDynamicOps)).apply(paramA);
/* 168 */     return paramView.newType().codec().encode(object, paramDynamicOps, paramT);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<RewriteResult<A, ?>> rewrite(TypeRewriteRule paramTypeRewriteRule, PointFreeRule paramPointFreeRule) {
/* 173 */     RewriteCacheKey rewriteCacheKey = new RewriteCacheKey(this, paramTypeRewriteRule, paramPointFreeRule);
/*     */ 
/*     */ 
/*     */     
/* 177 */     Optional<RewriteResult<A, ?>> optional = (Optional)REWRITE_CACHE.get(rewriteCacheKey);
/* 178 */     if (optional != null) {
/* 179 */       return optional;
/*     */     }
/* 181 */     AtomicReference atomicReference = new AtomicReference();
/*     */     
/* 183 */     CompletableFuture<Optional<? extends RewriteResult<?, ?>>> completableFuture = PENDING_REWRITE_CACHE.computeIfAbsent(rewriteCacheKey, paramRewriteCacheKey -> {
/*     */           CompletableFuture completableFuture = new CompletableFuture();
/*     */           
/*     */           paramAtomicReference.setPlain(completableFuture);
/*     */           return completableFuture;
/*     */         });
/* 189 */     if (atomicReference.getPlain() != null) {
/* 190 */       Optional<? extends RewriteResult<?, ?>> optional1 = paramTypeRewriteRule.rewrite(this).flatMap(paramRewriteResult -> paramRewriteResult.view().rewrite(paramPointFreeRule).map(()));
/* 191 */       REWRITE_CACHE.put(rewriteCacheKey, optional1);
/* 192 */       completableFuture.complete(optional1);
/* 193 */       PENDING_REWRITE_CACHE.remove(rewriteCacheKey);
/* 194 */       return (Optional)optional1;
/*     */     } 
/* 196 */     return (Optional<RewriteResult<A, ?>>)completableFuture.join();
/*     */   }
/*     */   
/*     */   public <FT, FR> Type<?> getSetType(OpticFinder<FT> paramOpticFinder, Type<FR> paramType) {
/* 200 */     return ((TypedOptic)paramOpticFinder.findType(this, paramType, false).orThrow()).tType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findFieldTypeOpt(String paramString) {
/* 209 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Type<?> findFieldType(String paramString) {
/* 213 */     return findFieldTypeOpt(paramString).<Throwable>orElseThrow(() -> new IllegalArgumentException("Field not found: " + paramString));
/*     */   }
/*     */   
/*     */   public OpticFinder<?> findField(String paramString) {
/* 217 */     return (OpticFinder<?>)new FieldFinder(paramString, findFieldType(paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<A> point(DynamicOps<?> paramDynamicOps) {
/* 225 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<Typed<A>> pointTyped(DynamicOps<?> paramDynamicOps) {
/* 229 */     return point(paramDynamicOps).map(paramObject -> new Typed(this, paramDynamicOps, paramObject));
/*     */   }
/*     */   
/*     */   public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findTypeCached(Type<FT> paramType, Type<FR> paramType1, TypeMatcher<FT, FR> paramTypeMatcher, boolean paramBoolean) {
/* 233 */     return findType(paramType, paramType1, paramTypeMatcher, paramBoolean);
/*     */   }
/*     */   
/*     */   public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findType(Type<FT> paramType, Type<FR> paramType1, TypeMatcher<FT, FR> paramTypeMatcher, boolean paramBoolean) {
/* 237 */     return (Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException>)paramTypeMatcher.<S>match(this).map(Either::left, paramFieldNotFoundException -> (paramFieldNotFoundException instanceof Continue) ? findTypeInChildren(paramType1, paramType2, paramTypeMatcher, paramBoolean) : Either.right(paramFieldNotFoundException));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(Type<FT> paramType, Type<FR> paramType1, TypeMatcher<FT, FR> paramTypeMatcher, boolean paramBoolean) {
/* 246 */     return Either.right(new FieldNotFoundException("No more children"));
/*     */   }
/*     */   
/*     */   public OpticFinder<A> finder() {
/* 250 */     return DSL.typeFinder(this);
/*     */   }
/*     */   
/*     */   public <B> Optional<A> ifSame(Typed<B> paramTyped) {
/* 254 */     return ifSame(paramTyped.getType(), paramTyped.getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public <B> Optional<A> ifSame(Type<B> paramType, B paramB) {
/* 259 */     if (equals(paramType, true, true)) {
/* 260 */       return Optional.of((A)paramB);
/*     */     }
/* 262 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public <B> Optional<RewriteResult<A, ?>> ifSame(Type<B> paramType, RewriteResult<B, ?> paramRewriteResult) {
/* 267 */     if (equals(paramType, true, true)) {
/* 268 */       return Optional.of(paramRewriteResult);
/*     */     }
/* 270 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(Object paramObject) {
/* 275 */     if (this == paramObject) {
/* 276 */       return true;
/*     */     }
/* 278 */     return equals(paramObject, false, true);
/*     */   }
/*     */   public abstract TypeTemplate buildTemplate();
/*     */   protected abstract Codec<A> buildCodec();
/*     */   public abstract boolean equals(Object paramObject, boolean paramBoolean1, boolean paramBoolean2);
/*     */   
/*     */   public static abstract class TypeError { private final String message;
/*     */     
/*     */     public TypeError(String param1String) {
/* 287 */       this.message = param1String;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 292 */       return this.message;
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class FieldNotFoundException extends TypeError {
/*     */     public FieldNotFoundException(String param1String) {
/* 298 */       super(param1String);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Continue extends FieldNotFoundException {
/*     */     public Continue() {
/* 304 */       super("Continue");
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface TypeMatcher<FT, FR> {
/*     */     <S> Either<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException> match(Type<S> param1Type);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\Type.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */