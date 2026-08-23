/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.ImmutableSet$Builder
 *  com.google.common.collect.Maps
 *  com.google.common.reflect.TypeToken
 */
package com.mojang.datafixers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.optics.InjTagged;
import com.mojang.datafixers.optics.Optic;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.profunctors.Cartesian;
import com.mojang.datafixers.optics.profunctors.Cocartesian;
import com.mojang.datafixers.optics.profunctors.Profunctor;
import com.mojang.datafixers.optics.profunctors.TraversalP;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record TypedOptic<S, T, A, B>(Set<TypeToken<? extends K1>> bounds, List<? extends Element<?, ?, ?, ?>> elements) {
    public TypedOptic(TypeToken<? extends K1> typeToken, Type<S> type, Type<T> type2, Type<A> type3, Type<B> type4, Optic<?, S, T, A, B> optic) {
        this((Set<TypeToken<? extends K1>>)ImmutableSet.of(typeToken), type, type2, type3, type4, optic);
    }

    public TypedOptic(Set<TypeToken<? extends K1>> set, Type<S> type, Type<T> type2, Type<A> type3, Type<B> type4, Optic<?, S, T, A, B> optic) {
        this(set, List.of(new Element<S, T, A, B>(type, type2, type3, type4, optic)));
    }

    public <P extends K2, Proof2 extends K1> App2<P, S, T> apply(TypeToken<Proof2> typeToken, App<Proof2, P> app, App2<P, A, B> app2) {
        return this.upCast(typeToken).orElseThrow(() -> new IllegalArgumentException("Couldn't upcast")).eval(app).apply(app2);
    }

    public Optic<?, S, T, ?, ?> outermost() {
        return this.outermostElement().optic();
    }

    public Optic<?, ?, ?, A, B> innermost() {
        return this.innermostElement().optic();
    }

    private Element<S, T, ?, ?> outermostElement() {
        return this.elements.get(0);
    }

    private Element<?, ?, A, B> innermostElement() {
        return this.elements.get(this.elements.size() - 1);
    }

    public Type<S> sType() {
        return this.outermostElement().sType();
    }

    public Type<T> tType() {
        return this.outermostElement().tType();
    }

    public Type<A> aType() {
        return this.innermostElement().aType();
    }

    public Type<B> bType() {
        return this.innermostElement().bType();
    }

    public <A1, B1> TypedOptic<S, T, A1, B1> compose(TypedOptic<A, B, A1, B1> typedOptic) {
        ImmutableSet.Builder builder = ImmutableSet.builder();
        builder.addAll(this.bounds);
        builder.addAll(typedOptic.bounds);
        ImmutableList.Builder builder2 = ImmutableList.builderWithExpectedSize((int)(this.elements().size() + typedOptic.elements().size()));
        builder2.addAll(this.elements());
        builder2.addAll(typedOptic.elements());
        return new TypedOptic<S, T, A, B>((Set<TypeToken<K1>>)((Set<TypeToken<? extends K1>>)builder.build()), (List<Element<?, ?, ?, ?>>)builder2.build());
    }

    public <Proof2 extends K1> Optional<Optic<? super Proof2, S, T, A, B>> upCast(TypeToken<Proof2> typeToken) {
        if (TypedOptic.instanceOf(this.bounds, typeToken)) {
            if (this.elements.size() == 1) {
                return Optional.of(this.elements.get(0).optic());
            }
            List list = this.elements.stream().map(element -> element.optic()).collect(Collectors.toList());
            return Optional.of(new Optic.CompositionOptic(list));
        }
        return Optional.empty();
    }

    public static <Proof2 extends K1> boolean instanceOf(Collection<TypeToken<? extends K1>> collection, TypeToken<Proof2> typeToken) {
        return collection.stream().allMatch(typeToken2 -> typeToken2.isSupertypeOf(typeToken));
    }

    public static <S, T> TypedOptic<S, T, S, T> adapter(Type<S> type, Type<T> type2) {
        return new TypedOptic<S, T, S, T>(Profunctor.Mu.TYPE_TOKEN, type, type2, type, type2, Optics.id());
    }

    public static <F, G, F2> TypedOptic<Pair<F, G>, Pair<F2, G>, F, F2> proj1(Type<F> type, Type<G> type2, Type<F2> type3) {
        return new TypedOptic<Pair<F, G>, Pair<F2, G>, F, F2>(Cartesian.Mu.TYPE_TOKEN, DSL.and(type, type2), DSL.and(type3, type2), type, type3, Optics.proj1());
    }

    public static <F, G, G2> TypedOptic<Pair<F, G>, Pair<F, G2>, G, G2> proj2(Type<F> type, Type<G> type2, Type<G2> type3) {
        return new TypedOptic<Pair<F, G>, Pair<F, G2>, G, G2>(Cartesian.Mu.TYPE_TOKEN, DSL.and(type, type2), DSL.and(type, type3), type2, type3, Optics.proj2());
    }

    public static <F, G, F2> TypedOptic<Either<F, G>, Either<F2, G>, F, F2> inj1(Type<F> type, Type<G> type2, Type<F2> type3) {
        return new TypedOptic<Either<F, G>, Either<F2, G>, F, F2>(Cocartesian.Mu.TYPE_TOKEN, DSL.or(type, type2), DSL.or(type3, type2), type, type3, Optics.inj1());
    }

    public static <F, G, G2> TypedOptic<Either<F, G>, Either<F, G2>, G, G2> inj2(Type<F> type, Type<G> type2, Type<G2> type3) {
        return new TypedOptic<Either<F, G>, Either<F, G2>, G, G2>(Cocartesian.Mu.TYPE_TOKEN, DSL.or(type, type2), DSL.or(type, type3), type2, type3, Optics.inj2());
    }

    public static <K, V, K2> TypedOptic<List<Pair<K, V>>, List<Pair<K2, V>>, K, K2> compoundListKeys(Type<K> type, Type<K2> type2, Type<V> type3) {
        return new TypedOptic(TraversalP.Mu.TYPE_TOKEN, DSL.compoundList(type, type3), DSL.compoundList(type2, type3), DSL.and(type, type3), DSL.and(type2, type3), Optics.listTraversal()).compose(new TypedOptic<Pair<K, V>, Pair<K2, V>, K, K2>(TraversalP.Mu.TYPE_TOKEN, DSL.and(type, type3), DSL.and(type2, type3), type, type2, Optics.proj1()));
    }

    public static <K, V, V2> TypedOptic<List<Pair<K, V>>, List<Pair<K, V2>>, V, V2> compoundListElements(Type<K> type, Type<V> type2, Type<V2> type3) {
        return new TypedOptic(TraversalP.Mu.TYPE_TOKEN, DSL.compoundList(type, type2), DSL.compoundList(type, type3), DSL.and(type, type2), DSL.and(type, type3), Optics.listTraversal()).compose(new TypedOptic<Pair<K, V>, Pair<K, V2>, V, V2>(TraversalP.Mu.TYPE_TOKEN, DSL.and(type, type2), DSL.and(type, type3), type2, type3, Optics.proj2()));
    }

    public static <A, B> TypedOptic<List<A>, List<B>, A, B> list(Type<A> type, Type<B> type2) {
        return new TypedOptic<A, B, A, B>(TraversalP.Mu.TYPE_TOKEN, DSL.list(type), DSL.list(type2), type, type2, Optics.listTraversal());
    }

    public static <K, A, B> TypedOptic<Pair<K, ?>, Pair<K, ?>, A, B> tagged(TaggedChoice.TaggedChoiceType<K> taggedChoiceType, K k, Type<A> type, Type<B> type2) {
        return new TypedOptic(Cocartesian.Mu.TYPE_TOKEN, taggedChoiceType, TypedOptic.replaceTagged(taggedChoiceType, k, type, type2), type, type2, new InjTagged(k));
    }

    private static <K, A, B> Type<Pair<K, ?>> replaceTagged(TaggedChoice.TaggedChoiceType<K> taggedChoiceType, K k, Type<A> type, Type<B> type2) {
        if (Objects.equals(type, type2)) {
            return taggedChoiceType;
        }
        if (!Objects.equals(taggedChoiceType.types().get(k), type)) {
            throw new IllegalArgumentException("Focused type doesn't match.");
        }
        HashMap hashMap = Maps.newHashMap(taggedChoiceType.types());
        hashMap.put(k, type2);
        return DSL.taggedChoiceType(taggedChoiceType.getName(), taggedChoiceType.getKeyType(), hashMap);
    }

    public TypedOptic<S, T, A, B> castOuter(Type<S> type, Type<T> type2) {
        return this.castOuterUnchecked(type, type2);
    }

    public <S2, T2> TypedOptic<S2, T2, A, B> castOuterUnchecked(Type<S2> type, Type<T2> type2) {
        ArrayList arrayList = new ArrayList(this.elements);
        arrayList.set(0, ((Element)arrayList.get(0)).castOuterUnchecked(type, type2));
        return new TypedOptic<S, T, A, B>(this.bounds, arrayList);
    }

    @Override
    public String toString() {
        return "(" + this.elements.stream().map(Object::toString).collect(Collectors.joining(" \u25e6 ")) + ")";
    }

    public record Element<S, T, A, B>(Type<S> sType, Type<T> tType, Type<A> aType, Type<B> bType, Optic<?, S, T, A, B> optic) {
        public <S2, T2> Element<S2, T2, A, B> castOuterUnchecked(Type<S2> type, Type<T2> type2) {
            return new Element<S2, T2, A, B>(type, type2, this.aType, this.bType, this.optic);
        }

        @Override
        public String toString() {
            return this.optic.toString();
        }
    }
}

