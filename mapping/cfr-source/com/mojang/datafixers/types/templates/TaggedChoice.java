/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  com.google.common.reflect.TypeToken
 *  it.unimi.dsi.fastutil.objects.Object2ObjectMap
 *  it.unimi.dsi.fastutil.objects.Object2ObjectMap$Entry
 *  it.unimi.dsi.fastutil.objects.Object2ObjectMaps
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  it.unimi.dsi.fastutil.objects.ObjectIterator
 *  javax.annotation.Nullable
 */
package com.mojang.datafixers.types.templates;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.View;
import com.mojang.datafixers.functions.Functions;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.optics.Affine;
import com.mojang.datafixers.optics.Lens;
import com.mojang.datafixers.optics.Optic;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.Traversal;
import com.mojang.datafixers.optics.profunctors.AffineP;
import com.mojang.datafixers.optics.profunctors.Cartesian;
import com.mojang.datafixers.optics.profunctors.TraversalP;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.types.families.TypeFamily;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.annotation.Nullable;

public final class TaggedChoice<K>
implements TypeTemplate {
    private final String name;
    private final Type<K> keyType;
    private final Object2ObjectMap<K, TypeTemplate> templates;
    private final Map<Pair<TypeFamily, Integer>, Type<?>> types = Maps.newConcurrentMap();
    private final int size;

    public TaggedChoice(String string, Type<K> type, Object2ObjectMap<K, TypeTemplate> object2ObjectMap) {
        this.name = string;
        this.keyType = type;
        this.templates = object2ObjectMap;
        this.size = object2ObjectMap.values().stream().mapToInt(TypeTemplate::size).max().orElse(0);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public TypeFamily apply(TypeFamily typeFamily) {
        return n -> this.types.computeIfAbsent(Pair.of(typeFamily, n), pair -> {
            Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap(this.templates.size());
            for (Map.Entry entry : Object2ObjectMaps.fastIterable(this.templates)) {
                object2ObjectOpenHashMap.put(entry.getKey(), ((TypeTemplate)entry.getValue()).apply((TypeFamily)pair.getFirst()).apply((Integer)pair.getSecond()));
            }
            return DSL.taggedChoiceType(this.name, this.keyType, object2ObjectOpenHashMap);
        });
    }

    @Override
    public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> familyOptic, Type<A> type, Type<B> type2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <A, B> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int n, @Nullable String string, Type<A> type, Type<B> type2) {
        return Either.right(new Type.FieldNotFoundException("Not implemented"));
    }

    @Override
    public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily typeFamily, IntFunction<RewriteResult<?, ?>> intFunction) {
        return n -> {
            RewriteResult rewriteResult = RewriteResult.nop((TaggedChoiceType)this.apply(typeFamily).apply(n));
            for (Map.Entry entry : this.templates.entrySet()) {
                RewriteResult<?, ?> rewriteResult2 = ((TypeTemplate)entry.getValue()).hmap(typeFamily, intFunction).apply(n);
                rewriteResult = TaggedChoiceType.elementResult(entry.getKey(), (TaggedChoiceType)rewriteResult.view().newType(), rewriteResult2).compose(rewriteResult);
            }
            return rewriteResult;
        };
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof TaggedChoice)) {
            return false;
        }
        TaggedChoice taggedChoice = (TaggedChoice)object;
        return Objects.equals(this.name, taggedChoice.name) && Objects.equals(this.keyType, taggedChoice.keyType) && Objects.equals(this.templates, taggedChoice.templates);
    }

    public int hashCode() {
        int n = this.name.hashCode();
        n = 31 * n + this.keyType.hashCode();
        n = 31 * n + this.templates.hashCode();
        return n;
    }

    public String toString() {
        return "TaggedChoice[" + this.name + ", " + Joiner.on((String)", ").withKeyValueSeparator(" -> ").join(this.templates) + "]";
    }

    public static final class TaggedChoiceType<K>
    extends Type<Pair<K, ?>> {
        private final String name;
        private final Type<K> keyType;
        protected final Object2ObjectMap<K, Type<?>> types;
        private final int hashCode;

        public TaggedChoiceType(String string, Type<K> type, Object2ObjectMap<K, Type<?>> object2ObjectMap) {
            this.name = string;
            this.keyType = type;
            this.types = object2ObjectMap;
            this.hashCode = Objects.hash(string, type, object2ObjectMap);
        }

        @Override
        public RewriteResult<Pair<K, ?>, ?> all(TypeRewriteRule typeRewriteRule, boolean bl, boolean bl2) {
            Object object;
            Object object22;
            Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap(this.types.size());
            for (Object object22 : Object2ObjectMaps.fastIterable(this.types)) {
                ObjectIterator objectIterator = typeRewriteRule.rewrite((Type)object22.getValue());
                if (!objectIterator.isPresent() || objectIterator.get().view().isNop()) continue;
                object2ObjectOpenHashMap.put(object22.getKey(), (Object)((RewriteResult)objectIterator.get()));
            }
            if (object2ObjectOpenHashMap.isEmpty()) {
                return RewriteResult.nop(this);
            }
            if (object2ObjectOpenHashMap.size() == 1) {
                object = (Map.Entry)object2ObjectOpenHashMap.entrySet().iterator().next();
                return TaggedChoiceType.elementResult(object.getKey(), this, (RewriteResult)object.getValue());
            }
            object = new Object2ObjectOpenHashMap(this.types);
            object22 = new BitSet();
            for (Map.Entry entry : Object2ObjectMaps.fastIterable((Object2ObjectMap)object2ObjectOpenHashMap)) {
                object.put(entry.getKey(), ((RewriteResult)entry.getValue()).view().newType());
                ((BitSet)object22).or(((RewriteResult)entry.getValue()).recData());
            }
            return RewriteResult.create(View.create(Functions.fun("TaggedChoiceTypeRewriteResult " + object2ObjectOpenHashMap.size(), new RewriteFunc(object2ObjectOpenHashMap), this, DSL.taggedChoiceType(this.name, this.keyType, object))), (BitSet)object22);
        }

        public static <K, FT, FR> RewriteResult<Pair<K, ?>, Pair<K, ?>> elementResult(K k, TaggedChoiceType<K> taggedChoiceType, RewriteResult<FT, FR> rewriteResult) {
            return TaggedChoiceType.opticView(taggedChoiceType, rewriteResult, TypedOptic.tagged(taggedChoiceType, k, rewriteResult.view().type(), rewriteResult.view().newType()));
        }

        @Override
        public Optional<RewriteResult<Pair<K, ?>, ?>> one(TypeRewriteRule typeRewriteRule) {
            for (Map.Entry entry : this.types.entrySet()) {
                Optional optional = typeRewriteRule.rewrite((Type)entry.getValue());
                if (!optional.isPresent()) continue;
                return Optional.of(TaggedChoiceType.elementResult(entry.getKey(), this, optional.get()));
            }
            return Optional.empty();
        }

        @Override
        public Type<?> updateMu(RecursiveTypeFamily recursiveTypeFamily) {
            Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap(this.types.size());
            for (Object2ObjectMap.Entry entry : Object2ObjectMaps.fastIterable(this.types)) {
                object2ObjectOpenHashMap.put(entry.getKey(), ((Type)entry.getValue()).updateMu(recursiveTypeFamily));
            }
            return DSL.taggedChoiceType(this.name, this.keyType, object2ObjectOpenHashMap);
        }

        @Override
        public TypeTemplate buildTemplate() {
            Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap(this.types.size());
            for (Object2ObjectMap.Entry entry : Object2ObjectMaps.fastIterable(this.types)) {
                object2ObjectOpenHashMap.put(entry.getKey(), (Object)((Type)entry.getValue()).template());
            }
            return DSL.taggedChoice(this.name, this.keyType, object2ObjectOpenHashMap);
        }

        @Override
        protected Codec<Pair<K, ?>> buildCodec() {
            return this.keyType.codec().partialDispatch(this.name, pair -> DataResult.success(pair.getFirst()), object -> this.getMapCodec(object).map(mapCodec -> TaggedChoiceType.asEntryPair(object, mapCodec)));
        }

        private static <K, V> MapCodec<Pair<K, V>> asEntryPair(K k, MapCodec<V> mapCodec) {
            return mapCodec.xmap(object2 -> Pair.of(k, object2), Pair::getSecond);
        }

        private DataResult<? extends MapCodec<?>> getMapCodec(K k) {
            return Optional.ofNullable((Type)this.types.get(k)).map(type -> DataResult.success(MapCodec.assumeMapUnsafe(type.codec()))).orElseGet(() -> DataResult.error(() -> "Unsupported key: " + String.valueOf(k)));
        }

        @Override
        public Optional<Type<?>> findFieldTypeOpt(String string) {
            return this.types.values().stream().map(type -> type.findFieldTypeOpt(string)).filter(Optional::isPresent).findFirst().flatMap(Function.identity());
        }

        @Override
        public Optional<Pair<K, ?>> point(DynamicOps<?> dynamicOps) {
            return this.types.entrySet().stream().map(entry -> ((Type)entry.getValue()).point(dynamicOps).map(object -> Pair.of(entry.getKey(), object))).filter(Optional::isPresent).findFirst().flatMap(Function.identity()).map(pair -> pair);
        }

        public Optional<Typed<Pair<K, ?>>> point(DynamicOps<?> dynamicOps, K k, Object object) {
            if (!this.types.containsKey(k)) {
                return Optional.empty();
            }
            return Optional.of(new Typed<Pair<K, Object>>(this, dynamicOps, Pair.of(k, object)));
        }

        @Override
        public <FT, FR> Either<TypedOptic<Pair<K, ?>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> type2, Type.TypeMatcher<FT, FR> typeMatcher, boolean bl) {
            Optic optic;
            Object object;
            final Map map = this.types.entrySet().stream().map(entry -> Pair.of(entry.getKey(), ((Type)entry.getValue()).findType(type, type2, typeMatcher, bl))).filter(pair -> ((Either)pair.getSecond()).left().isPresent()).map(pair -> pair.mapSecond(either -> (TypedOptic)either.left().get())).collect(Pair.toMap());
            if (map.isEmpty()) {
                return Either.right(new Type.FieldNotFoundException("Not found in any choices"));
            }
            if (map.size() == 1) {
                Map.Entry entry2 = map.entrySet().iterator().next();
                return Either.left(this.cap(this, entry2.getKey(), (TypedOptic)entry2.getValue()));
            }
            HashSet hashSet = Sets.newHashSet();
            map.values().forEach(typedOptic -> hashSet.addAll(typedOptic.bounds()));
            if (TypedOptic.instanceOf(hashSet, Cartesian.Mu.TYPE_TOKEN) && map.size() == this.types.size()) {
                object = Cartesian.Mu.TYPE_TOKEN;
                optic = new Lens<Pair<K, ?>, Pair<K, ?>, FT, FR>(){

                    @Override
                    public FT view(Pair<K, ?> pair) {
                        TypedOptic typedOptic = (TypedOptic)map.get(pair.getFirst());
                        return this.capView(pair, typedOptic);
                    }

                    private <S, T> FT capView(Pair<K, ?> pair, TypedOptic<S, T, FT, FR> typedOptic) {
                        return Optics.toLens(typedOptic.upCast(Cartesian.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).view(pair.getSecond());
                    }

                    @Override
                    public Pair<K, ?> update(FR FR, Pair<K, ?> pair) {
                        TypedOptic typedOptic = (TypedOptic)map.get(pair.getFirst());
                        return this.capUpdate(FR, pair, typedOptic);
                    }

                    private <S, T> Pair<K, ?> capUpdate(FR FR, Pair<K, ?> pair, TypedOptic<S, T, FT, FR> typedOptic) {
                        return Pair.of(pair.getFirst(), Optics.toLens(typedOptic.upCast(Cartesian.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).update(FR, pair.getSecond()));
                    }
                };
            } else if (TypedOptic.instanceOf(hashSet, AffineP.Mu.TYPE_TOKEN)) {
                object = AffineP.Mu.TYPE_TOKEN;
                optic = new Affine<Pair<K, ?>, Pair<K, ?>, FT, FR>(){

                    @Override
                    public Either<Pair<K, ?>, FT> preview(Pair<K, ?> pair) {
                        if (!map.containsKey(pair.getFirst())) {
                            return Either.left(pair);
                        }
                        TypedOptic typedOptic = (TypedOptic)map.get(pair.getFirst());
                        return this.capPreview(pair, typedOptic);
                    }

                    private <S, T> Either<Pair<K, ?>, FT> capPreview(Pair<K, ?> pair, TypedOptic<S, T, FT, FR> typedOptic) {
                        return Optics.toAffine(typedOptic.upCast(AffineP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).preview(pair.getSecond()).mapLeft(object -> Pair.of(pair.getFirst(), object));
                    }

                    @Override
                    public Pair<K, ?> set(FR FR, Pair<K, ?> pair) {
                        if (!map.containsKey(pair.getFirst())) {
                            return pair;
                        }
                        TypedOptic typedOptic = (TypedOptic)map.get(pair.getFirst());
                        return this.capSet(FR, pair, typedOptic);
                    }

                    private <S, T> Pair<K, ?> capSet(FR FR, Pair<K, ?> pair, TypedOptic<S, T, FT, FR> typedOptic) {
                        return Pair.of(pair.getFirst(), Optics.toAffine(typedOptic.upCast(AffineP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).set(FR, pair.getSecond()));
                    }
                };
            } else if (TypedOptic.instanceOf(hashSet, TraversalP.Mu.TYPE_TOKEN)) {
                object = TraversalP.Mu.TYPE_TOKEN;
                optic = new Traversal<Pair<K, ?>, Pair<K, ?>, FT, FR>(){

                    @Override
                    public <F extends K1> FunctionType<Pair<K, ?>, App<F, Pair<K, ?>>> wander(Applicative<F, ?> applicative, FunctionType<FT, App<F, FR>> functionType) {
                        return pair -> {
                            if (!map.containsKey(pair.getFirst())) {
                                return applicative.point(pair);
                            }
                            TypedOptic typedOptic = (TypedOptic)map.get(pair.getFirst());
                            return this.capTraversal(applicative, functionType, (Pair)pair, typedOptic);
                        };
                    }

                    private <S, T, F extends K1> App<F, Pair<K, ?>> capTraversal(Applicative<F, ?> applicative, FunctionType<FT, App<F, FR>> functionType, Pair<K, ?> pair, TypedOptic<S, T, FT, FR> typedOptic) {
                        Traversal traversal = Optics.toTraversal(typedOptic.upCast(TraversalP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new));
                        return applicative.ap(object -> Pair.of(pair.getFirst(), object), traversal.wander(applicative, functionType).apply(pair.getSecond()));
                    }
                };
            } else {
                throw new IllegalStateException("Could not merge TaggedChoiceType optics, unknown bound: " + Arrays.toString(hashSet.toArray()));
            }
            Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap(this.types);
            for (Object2ObjectMap.Entry entry3 : Object2ObjectMaps.fastIterable((Object2ObjectMap)object2ObjectOpenHashMap)) {
                TypedOptic typedOptic2 = (TypedOptic)map.get(entry3.getKey());
                if (typedOptic2 == null) continue;
                entry3.setValue(typedOptic2.tType());
            }
            return Either.left(new TypedOptic((TypeToken<K1>)object, this, DSL.taggedChoiceType(this.name, this.keyType, object2ObjectOpenHashMap), type, type2, optic));
        }

        private <S, T, FT, FR> TypedOptic<Pair<K, ?>, Pair<K, ?>, FT, FR> cap(TaggedChoiceType<K> taggedChoiceType, K k, TypedOptic<S, T, FT, FR> typedOptic) {
            return TypedOptic.tagged(taggedChoiceType, k, typedOptic.sType(), typedOptic.tType()).compose(typedOptic);
        }

        @Override
        public Optional<TaggedChoiceType<?>> findChoiceType(String string, int n) {
            if (Objects.equals(string, this.name)) {
                return Optional.of(this);
            }
            return Optional.empty();
        }

        @Override
        public Optional<Type<?>> findCheckedType(int n) {
            return this.types.values().stream().map(type -> type.findCheckedType(n)).filter(Optional::isPresent).findFirst().flatMap(Function.identity());
        }

        @Override
        public boolean equals(Object object, boolean bl, boolean bl2) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof TaggedChoiceType)) {
                return false;
            }
            TaggedChoiceType taggedChoiceType = (TaggedChoiceType)object;
            if (!Objects.equals(this.name, taggedChoiceType.name)) {
                return false;
            }
            if (!this.keyType.equals(taggedChoiceType.keyType, bl, bl2)) {
                return false;
            }
            if (this.types.size() != taggedChoiceType.types.size()) {
                return false;
            }
            for (Map.Entry entry : this.types.entrySet()) {
                if (((Type)entry.getValue()).equals(taggedChoiceType.types.get(entry.getKey()), bl, bl2)) continue;
                return false;
            }
            return true;
        }

        public int hashCode() {
            return this.hashCode;
        }

        public String toString() {
            return "TaggedChoiceType[" + this.name + ", " + Joiner.on((String)", \n").withKeyValueSeparator(" -> ").join(this.types) + "]\n";
        }

        public String getName() {
            return this.name;
        }

        public Type<K> getKeyType() {
            return this.keyType;
        }

        public boolean hasType(K k) {
            return this.types.containsKey(k);
        }

        public Map<K, Type<?>> types() {
            return this.types;
        }

        private static final class RewriteFunc<K>
        implements Function<DynamicOps<?>, Function<Pair<K, ?>, Pair<K, ?>>> {
            private final Map<K, ? extends RewriteResult<?, ?>> results;

            public RewriteFunc(Map<K, ? extends RewriteResult<?, ?>> map) {
                this.results = map;
            }

            @Override
            public FunctionType<Pair<K, ?>, Pair<K, ?>> apply(DynamicOps<?> dynamicOps) {
                return pair -> {
                    RewriteResult<?, ?> rewriteResult = this.results.get(pair.getFirst());
                    if (rewriteResult == null) {
                        return pair;
                    }
                    return this.capRuleApply(dynamicOps, (Pair<K, ?>)pair, rewriteResult);
                };
            }

            private <A, B> Pair<K, B> capRuleApply(DynamicOps<?> dynamicOps, Pair<K, ?> pair, RewriteResult<A, B> rewriteResult) {
                return pair.mapSecond(object -> rewriteResult.view().function().evalCached().apply(dynamicOps).apply(object));
            }

            public boolean equals(Object object) {
                if (this == object) {
                    return true;
                }
                if (object == null || this.getClass() != object.getClass()) {
                    return false;
                }
                RewriteFunc rewriteFunc = (RewriteFunc)object;
                return Objects.equals(this.results, rewriteFunc.results);
            }

            public int hashCode() {
                return this.results.hashCode();
            }
        }
    }
}

