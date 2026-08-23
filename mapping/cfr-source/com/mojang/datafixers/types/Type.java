/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  javax.annotation.Nullable
 */
package com.mojang.datafixers.types;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.FieldFinder;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.View;
import com.mojang.datafixers.functions.Functions;
import com.mojang.datafixers.functions.PointFreeRule;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nullable;

public abstract class Type<A>
implements App<Mu, A> {
    private static final Map<RewriteCacheKey, CompletableFuture<Optional<? extends RewriteResult<?, ?>>>> PENDING_REWRITE_CACHE = Maps.newConcurrentMap();
    private static final Map<RewriteCacheKey, Optional<? extends RewriteResult<?, ?>>> REWRITE_CACHE = Maps.newConcurrentMap();
    @Nullable
    private TypeTemplate template;
    @Nullable
    private Codec<A> codec;

    public static <A> Type<A> unbox(App<Mu, A> app) {
        return (Type)app;
    }

    public RewriteResult<A, ?> rewriteOrNop(TypeRewriteRule typeRewriteRule) {
        return DataFixUtils.orElseGet(typeRewriteRule.rewrite(this), () -> RewriteResult.nop(this));
    }

    public static <S, T, A, B> RewriteResult<S, T> opticView(Type<S> type, RewriteResult<A, B> rewriteResult, TypedOptic<S, T, A, B> typedOptic) {
        if (rewriteResult.view().isNop()) {
            return RewriteResult.nop(type);
        }
        return RewriteResult.create(View.create(Functions.app(Functions.profunctorTransformer(typedOptic), rewriteResult.view().function())), rewriteResult.recData());
    }

    public RewriteResult<A, ?> all(TypeRewriteRule typeRewriteRule, boolean bl, boolean bl2) {
        return RewriteResult.nop(this);
    }

    public Optional<RewriteResult<A, ?>> one(TypeRewriteRule typeRewriteRule) {
        return Optional.empty();
    }

    public Optional<RewriteResult<A, ?>> everywhere(TypeRewriteRule typeRewriteRule, PointFreeRule pointFreeRule, boolean bl, boolean bl2) {
        TypeRewriteRule typeRewriteRule2 = TypeRewriteRule.seq(TypeRewriteRule.orElse(typeRewriteRule, TypeRewriteRule::nop), TypeRewriteRule.all(TypeRewriteRule.everywhere(typeRewriteRule, pointFreeRule, bl, bl2), bl, bl2));
        return this.rewrite(typeRewriteRule2, pointFreeRule);
    }

    public Type<?> updateMu(RecursiveTypeFamily recursiveTypeFamily) {
        return this;
    }

    public TypeTemplate template() {
        if (this.template == null) {
            this.template = this.buildTemplate();
        }
        return this.template;
    }

    public abstract TypeTemplate buildTemplate();

    public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String string, int n) {
        return Optional.empty();
    }

    public Optional<Type<?>> findCheckedType(int n) {
        return Optional.empty();
    }

    public final <T> DataResult<Pair<A, Dynamic<T>>> read(Dynamic<T> dynamic) {
        return this.codec().decode(dynamic.getOps(), dynamic.getValue()).map(pair -> pair.mapSecond(object -> new Dynamic<Object>(dynamic.getOps(), object)));
    }

    public final Codec<A> codec() {
        if (this.codec == null) {
            this.codec = this.buildCodec();
        }
        return this.codec;
    }

    protected abstract Codec<A> buildCodec();

    public final <T> DataResult<T> write(DynamicOps<T> dynamicOps, A a) {
        return this.codec().encode(a, dynamicOps, dynamicOps.empty());
    }

    public final <T> DataResult<Dynamic<T>> writeDynamic(DynamicOps<T> dynamicOps, A a) {
        return this.write(dynamicOps, a).map(object -> new Dynamic<Object>(dynamicOps, object));
    }

    public <T> DataResult<Pair<Typed<A>, T>> readTyped(Dynamic<T> dynamic) {
        return this.readTyped(dynamic.getOps(), dynamic.getValue());
    }

    public <T> DataResult<Pair<Typed<A>, T>> readTyped(DynamicOps<T> dynamicOps, T t) {
        return this.codec().decode(dynamicOps, t).map(pair -> pair.mapFirst(object -> new Typed<Object>(this, dynamicOps, object)));
    }

    public <T> DataResult<Pair<Optional<?>, T>> read(DynamicOps<T> dynamicOps, TypeRewriteRule typeRewriteRule, PointFreeRule pointFreeRule, T t) {
        return this.codec().decode(dynamicOps, t).map(pair -> pair.mapFirst(object -> this.rewrite(typeRewriteRule, pointFreeRule).map(rewriteResult -> rewriteResult.view().function().evalCached().apply(dynamicOps).apply(object))));
    }

    public <T> DataResult<T> readAndWrite(DynamicOps<T> dynamicOps, Type<?> type, TypeRewriteRule typeRewriteRule, PointFreeRule pointFreeRule, T t) {
        Optional<RewriteResult<A, ?>> optional = this.rewrite(typeRewriteRule, pointFreeRule);
        if (!optional.isPresent()) {
            return DataResult.error(() -> "Could not build a rewrite rule: " + String.valueOf(typeRewriteRule) + " " + String.valueOf(pointFreeRule), t);
        }
        View<A, ?> view = optional.get().view();
        if (view.isNop()) {
            return DataResult.success(t);
        }
        return this.codec().decode(dynamicOps, t).flatMap(pair -> this.capWrite(dynamicOps, type, pair.getSecond(), pair.getFirst(), view));
    }

    private <T, B> DataResult<T> capWrite(DynamicOps<T> dynamicOps, Type<?> type, T t, A a, View<A, B> view) {
        if (!type.equals(view.newType(), true, true)) {
            return DataResult.error(() -> "Rewritten type doesn't match");
        }
        B b = view.function().evalCached().apply(dynamicOps).apply(a);
        return view.newType().codec().encode(b, dynamicOps, t);
    }

    public Optional<RewriteResult<A, ?>> rewrite(TypeRewriteRule typeRewriteRule, PointFreeRule pointFreeRule) {
        RewriteCacheKey rewriteCacheKey2 = new RewriteCacheKey(this, typeRewriteRule, pointFreeRule);
        Optional<RewriteResult<A, ?>> optional = REWRITE_CACHE.get(rewriteCacheKey2);
        if (optional != null) {
            return optional;
        }
        AtomicReference atomicReference = new AtomicReference();
        CompletableFuture completableFuture = PENDING_REWRITE_CACHE.computeIfAbsent(rewriteCacheKey2, rewriteCacheKey -> {
            CompletableFuture completableFuture = new CompletableFuture();
            atomicReference.setPlain(completableFuture);
            return completableFuture;
        });
        if (atomicReference.getPlain() != null) {
            Optional<RewriteResult<A, ?>> optional2 = typeRewriteRule.rewrite(this).flatMap(rewriteResult -> rewriteResult.view().rewrite(pointFreeRule).map(view -> RewriteResult.create(view, rewriteResult.recData())));
            REWRITE_CACHE.put(rewriteCacheKey2, optional2);
            completableFuture.complete(optional2);
            PENDING_REWRITE_CACHE.remove(rewriteCacheKey2);
            return optional2;
        }
        return (Optional)completableFuture.join();
    }

    public <FT, FR> Type<?> getSetType(OpticFinder<FT> opticFinder, Type<FR> type) {
        return opticFinder.findType(this, type, false).orThrow().tType();
    }

    public Optional<Type<?>> findFieldTypeOpt(String string) {
        return Optional.empty();
    }

    public Type<?> findFieldType(String string) {
        return this.findFieldTypeOpt(string).orElseThrow(() -> new IllegalArgumentException("Field not found: " + string));
    }

    public OpticFinder<?> findField(String string) {
        return new FieldFinder(string, this.findFieldType(string));
    }

    public Optional<A> point(DynamicOps<?> dynamicOps) {
        return Optional.empty();
    }

    public Optional<Typed<A>> pointTyped(DynamicOps<?> dynamicOps) {
        return this.point(dynamicOps).map(object -> new Typed<Object>(this, dynamicOps, object));
    }

    public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findTypeCached(Type<FT> type, Type<FR> type2, TypeMatcher<FT, FR> typeMatcher, boolean bl) {
        return this.findType(type, type2, typeMatcher, bl);
    }

    public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findType(Type<FT> type, Type<FR> type2, TypeMatcher<FT, FR> typeMatcher, boolean bl) {
        return typeMatcher.match(this).map(Either::left, fieldNotFoundException -> {
            if (fieldNotFoundException instanceof Continue) {
                return this.findTypeInChildren(type, type2, typeMatcher, bl);
            }
            return Either.right(fieldNotFoundException);
        });
    }

    public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> type2, TypeMatcher<FT, FR> typeMatcher, boolean bl) {
        return Either.right(new FieldNotFoundException("No more children"));
    }

    public OpticFinder<A> finder() {
        return DSL.typeFinder(this);
    }

    public <B> Optional<A> ifSame(Typed<B> typed) {
        return this.ifSame(typed.getType(), typed.getValue());
    }

    public <B> Optional<A> ifSame(Type<B> type, B b) {
        if (this.equals(type, true, true)) {
            return Optional.of(b);
        }
        return Optional.empty();
    }

    public <B> Optional<RewriteResult<A, ?>> ifSame(Type<B> type, RewriteResult<B, ?> rewriteResult) {
        if (this.equals(type, true, true)) {
            return Optional.of(rewriteResult);
        }
        return Optional.empty();
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        return this.equals(object, false, true);
    }

    public abstract boolean equals(Object var1, boolean var2, boolean var3);

    private record RewriteCacheKey(Type<?> type, TypeRewriteRule rule, PointFreeRule optimizationRule) {
    }

    public static interface TypeMatcher<FT, FR> {
        public <S> Either<TypedOptic<S, ?, FT, FR>, FieldNotFoundException> match(Type<S> var1);
    }

    public static class FieldNotFoundException
    extends TypeError {
        public FieldNotFoundException(String string) {
            super(string);
        }
    }

    public static final class Continue
    extends FieldNotFoundException {
        public Continue() {
            super("Continue");
        }
    }

    public static abstract class TypeError {
        private final String message;

        public TypeError(String string) {
            this.message = string;
        }

        public String toString() {
            return this.message;
        }
    }

    public static class Mu
    implements K1 {
    }
}

