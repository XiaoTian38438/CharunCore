/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.google.common.collect.ObjectArrays
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 */
package com.mojang.datafixers;

import com.google.common.collect.Maps;
import com.google.common.collect.ObjectArrays;
import com.mojang.datafixers.FieldFinder;
import com.mojang.datafixers.NamedChoiceFinder;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Func;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.constant.EmptyPart;
import com.mojang.datafixers.types.constant.EmptyPartPassthrough;
import com.mojang.datafixers.types.templates.Check;
import com.mojang.datafixers.types.templates.CompoundList;
import com.mojang.datafixers.types.templates.Const;
import com.mojang.datafixers.types.templates.Hook;
import com.mojang.datafixers.types.templates.List;
import com.mojang.datafixers.types.templates.Named;
import com.mojang.datafixers.types.templates.Product;
import com.mojang.datafixers.types.templates.RecursivePoint;
import com.mojang.datafixers.types.templates.Sum;
import com.mojang.datafixers.types.templates.Tag;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface DSL {
    public static Type<Boolean> bool() {
        return Instances.BOOL_TYPE;
    }

    public static Type<Integer> intType() {
        return Instances.INT_TYPE;
    }

    public static Type<Long> longType() {
        return Instances.LONG_TYPE;
    }

    public static Type<Byte> byteType() {
        return Instances.BYTE_TYPE;
    }

    public static Type<Short> shortType() {
        return Instances.SHORT_TYPE;
    }

    public static Type<Float> floatType() {
        return Instances.FLOAT_TYPE;
    }

    public static Type<Double> doubleType() {
        return Instances.DOUBLE_TYPE;
    }

    public static Type<String> string() {
        return Instances.STRING_TYPE;
    }

    public static TypeTemplate emptyPart() {
        return DSL.constType(Instances.EMPTY_PART);
    }

    public static Type<Unit> emptyPartType() {
        return Instances.EMPTY_PART;
    }

    public static TypeTemplate remainder() {
        return DSL.constType(Instances.EMPTY_PASSTHROUGH);
    }

    public static Type<Dynamic<?>> remainderType() {
        return Instances.EMPTY_PASSTHROUGH;
    }

    public static TypeTemplate check(String string, int n, TypeTemplate typeTemplate) {
        return new Check(string, n, typeTemplate);
    }

    public static TypeTemplate compoundList(TypeTemplate typeTemplate) {
        return DSL.compoundList(DSL.constType(DSL.string()), typeTemplate);
    }

    public static <V> CompoundList.CompoundListType<String, V> compoundList(Type<V> type) {
        return DSL.compoundList(DSL.string(), type);
    }

    public static TypeTemplate compoundList(TypeTemplate typeTemplate, TypeTemplate typeTemplate2) {
        return DSL.and((TypeTemplate)new CompoundList(typeTemplate, typeTemplate2), DSL.remainder());
    }

    public static <K, V> CompoundList.CompoundListType<K, V> compoundList(Type<K> type, Type<V> type2) {
        return new CompoundList.CompoundListType<K, V>(type, type2);
    }

    public static TypeTemplate constType(Type<?> type) {
        return new Const(type);
    }

    public static TypeTemplate hook(TypeTemplate typeTemplate, Hook.HookFunction hookFunction, Hook.HookFunction hookFunction2) {
        return new Hook(typeTemplate, hookFunction, hookFunction2);
    }

    public static <A> Type<A> hook(Type<A> type, Hook.HookFunction hookFunction, Hook.HookFunction hookFunction2) {
        return new Hook.HookType<A>(type, hookFunction, hookFunction2);
    }

    public static TypeTemplate list(TypeTemplate typeTemplate) {
        return new List(typeTemplate);
    }

    public static <A> List.ListType<A> list(Type<A> type) {
        return new List.ListType<A>(type);
    }

    public static TypeTemplate named(String string, TypeTemplate typeTemplate) {
        return new Named(string, typeTemplate);
    }

    public static <A> Type<Pair<String, A>> named(String string, Type<A> type) {
        return new Named.NamedType<A>(string, type);
    }

    public static TypeTemplate and(TypeTemplate typeTemplate, TypeTemplate typeTemplate2) {
        return new Product(typeTemplate, typeTemplate2);
    }

    public static TypeTemplate and(TypeTemplate typeTemplate, TypeTemplate ... typeTemplateArray) {
        if (typeTemplateArray.length == 0) {
            return typeTemplate;
        }
        TypeTemplate typeTemplate2 = typeTemplateArray[typeTemplateArray.length - 1];
        for (int i = typeTemplateArray.length - 2; i >= 0; --i) {
            typeTemplate2 = DSL.and(typeTemplateArray[i], typeTemplate2);
        }
        return DSL.and(typeTemplate, typeTemplate2);
    }

    public static TypeTemplate and(java.util.List<TypeTemplate> list) {
        return switch (list.size()) {
            case 0 -> throw new IllegalArgumentException("Must have at least one type");
            case 1 -> list.get(0);
            default -> DSL.and(list.get(0), (TypeTemplate[])list.subList(1, list.size()).toArray(TypeTemplate[]::new));
        };
    }

    public static TypeTemplate allWithRemainder(TypeTemplate typeTemplate, TypeTemplate ... typeTemplateArray) {
        return DSL.and(typeTemplate, (TypeTemplate[])ObjectArrays.concat((Object[])typeTemplateArray, (Object)DSL.remainder()));
    }

    public static <F, G> Type<Pair<F, G>> and(Type<F> type, Type<G> type2) {
        return new Product.ProductType<F, G>(type, type2);
    }

    public static <F, G, H> Type<Pair<F, Pair<G, H>>> and(Type<F> type, Type<G> type2, Type<H> type3) {
        return DSL.and(type, DSL.and(type2, type3));
    }

    public static <F, G, H, I> Type<Pair<F, Pair<G, Pair<H, I>>>> and(Type<F> type, Type<G> type2, Type<H> type3, Type<I> type4) {
        return DSL.and(type, DSL.and(type2, DSL.and(type3, type4)));
    }

    public static TypeTemplate id(int n) {
        return new RecursivePoint(n);
    }

    public static TypeTemplate or(TypeTemplate typeTemplate, TypeTemplate typeTemplate2) {
        return new Sum(typeTemplate, typeTemplate2);
    }

    public static <F, G> Type<Either<F, G>> or(Type<F> type, Type<G> type2) {
        return new Sum.SumType<F, G>(type, type2);
    }

    public static TypeTemplate field(String string, TypeTemplate typeTemplate) {
        return new Tag(string, typeTemplate);
    }

    public static <A> Tag.TagType<A> field(String string, Type<A> type) {
        return new Tag.TagType<A>(string, type);
    }

    public static <K> TaggedChoice<K> taggedChoice(String string, Type<K> type, Map<K, TypeTemplate> map) {
        return new TaggedChoice<K>(string, type, new Object2ObjectOpenHashMap(map));
    }

    public static <K> TaggedChoice<K> taggedChoiceLazy(String string, Type<K> type, Map<K, Supplier<TypeTemplate>> map) {
        return DSL.taggedChoice(string, type, map.entrySet().stream().map(entry -> Pair.of(entry.getKey(), (TypeTemplate)((Supplier)entry.getValue()).get())).collect(Pair.toMap()));
    }

    public static <K> Type<Pair<K, ?>> taggedChoiceType(String string, Type<K> type, Map<K, ? extends Type<?>> map) {
        return Instances.TAGGED_CHOICE_TYPE_CACHE.computeIfAbsent(new Instances.TaggedChoiceCacheKey<K>(string, type, map), Instances.TaggedChoiceCacheKey::build);
    }

    public static <A, B> Type<Function<A, B>> func(Type<A> type, Type<B> type2) {
        return new Func<A, B>(type, type2);
    }

    public static <A> Type<Either<A, Unit>> optional(Type<A> type) {
        return DSL.or(type, DSL.emptyPartType());
    }

    public static TypeTemplate optional(TypeTemplate typeTemplate) {
        return DSL.or(typeTemplate, DSL.emptyPart());
    }

    public static TypeTemplate fields(String string, TypeTemplate typeTemplate) {
        return DSL.allWithRemainder(DSL.field(string, typeTemplate), new TypeTemplate[0]);
    }

    public static TypeTemplate fields(String string, TypeTemplate typeTemplate, String string2, TypeTemplate typeTemplate2) {
        return DSL.allWithRemainder(DSL.field(string, typeTemplate), DSL.field(string2, typeTemplate2));
    }

    public static TypeTemplate fields(String string, TypeTemplate typeTemplate, String string2, TypeTemplate typeTemplate2, String string3, TypeTemplate typeTemplate3) {
        return DSL.allWithRemainder(DSL.field(string, typeTemplate), DSL.field(string2, typeTemplate2), DSL.field(string3, typeTemplate3));
    }

    public static TypeTemplate fields(String string, TypeTemplate typeTemplate, TypeTemplate typeTemplate2) {
        return DSL.and(DSL.field(string, typeTemplate), typeTemplate2);
    }

    public static TypeTemplate fields(String string, TypeTemplate typeTemplate, String string2, TypeTemplate typeTemplate2, TypeTemplate typeTemplate3) {
        return DSL.and(DSL.field(string, typeTemplate), DSL.field(string2, typeTemplate2), typeTemplate3);
    }

    public static TypeTemplate fields(String string, TypeTemplate typeTemplate, String string2, TypeTemplate typeTemplate2, String string3, TypeTemplate typeTemplate3, TypeTemplate typeTemplate4) {
        return DSL.and(DSL.field(string, typeTemplate), DSL.field(string2, typeTemplate2), DSL.field(string3, typeTemplate3), typeTemplate4);
    }

    public static TypeTemplate optionalFields(String string, TypeTemplate typeTemplate) {
        return DSL.allWithRemainder(DSL.optional(DSL.field(string, typeTemplate)), new TypeTemplate[0]);
    }

    public static TypeTemplate optionalFields(String string, TypeTemplate typeTemplate, String string2, TypeTemplate typeTemplate2) {
        return DSL.allWithRemainder(DSL.optional(DSL.field(string, typeTemplate)), DSL.optional(DSL.field(string2, typeTemplate2)));
    }

    public static TypeTemplate optionalFields(String string, TypeTemplate typeTemplate, String string2, TypeTemplate typeTemplate2, String string3, TypeTemplate typeTemplate3) {
        return DSL.allWithRemainder(DSL.optional(DSL.field(string, typeTemplate)), DSL.optional(DSL.field(string2, typeTemplate2)), DSL.optional(DSL.field(string3, typeTemplate3)));
    }

    public static TypeTemplate optionalFields(String string, TypeTemplate typeTemplate, String string2, TypeTemplate typeTemplate2, String string3, TypeTemplate typeTemplate3, String string4, TypeTemplate typeTemplate4) {
        return DSL.allWithRemainder(DSL.optional(DSL.field(string, typeTemplate)), DSL.optional(DSL.field(string2, typeTemplate2)), DSL.optional(DSL.field(string3, typeTemplate3)), DSL.optional(DSL.field(string4, typeTemplate4)));
    }

    public static TypeTemplate optionalFields(String string, TypeTemplate typeTemplate, String string2, TypeTemplate typeTemplate2, String string3, TypeTemplate typeTemplate3, String string4, TypeTemplate typeTemplate4, String string5, TypeTemplate typeTemplate5) {
        return DSL.allWithRemainder(DSL.optional(DSL.field(string, typeTemplate)), DSL.optional(DSL.field(string2, typeTemplate2)), DSL.optional(DSL.field(string3, typeTemplate3)), DSL.optional(DSL.field(string4, typeTemplate4)), DSL.optional(DSL.field(string5, typeTemplate5)));
    }

    public static TypeTemplate optionalFields(String string, TypeTemplate typeTemplate, TypeTemplate typeTemplate2) {
        return DSL.and(DSL.optional(DSL.field(string, typeTemplate)), typeTemplate2);
    }

    public static TypeTemplate optionalFields(String string, TypeTemplate typeTemplate, String string2, TypeTemplate typeTemplate2, TypeTemplate typeTemplate3) {
        return DSL.and(DSL.optional(DSL.field(string, typeTemplate)), DSL.optional(DSL.field(string2, typeTemplate2)), typeTemplate3);
    }

    public static TypeTemplate optionalFields(String string, TypeTemplate typeTemplate, String string2, TypeTemplate typeTemplate2, String string3, TypeTemplate typeTemplate3, TypeTemplate typeTemplate4) {
        return DSL.and(DSL.optional(DSL.field(string, typeTemplate)), DSL.optional(DSL.field(string2, typeTemplate2)), DSL.optional(DSL.field(string3, typeTemplate3)), typeTemplate4);
    }

    public static TypeTemplate optionalFields(String string, TypeTemplate typeTemplate, String string2, TypeTemplate typeTemplate2, String string3, TypeTemplate typeTemplate3, String string4, TypeTemplate typeTemplate4, TypeTemplate typeTemplate5) {
        return DSL.and(DSL.optional(DSL.field(string, typeTemplate)), DSL.optional(DSL.field(string2, typeTemplate2)), DSL.optional(DSL.field(string3, typeTemplate3)), DSL.optional(DSL.field(string4, typeTemplate4)), typeTemplate5);
    }

    public static TypeTemplate optionalFields(String string, TypeTemplate typeTemplate, String string2, TypeTemplate typeTemplate2, String string3, TypeTemplate typeTemplate3, String string4, TypeTemplate typeTemplate4, String string5, TypeTemplate typeTemplate5, TypeTemplate typeTemplate6) {
        return DSL.and(DSL.optional(DSL.field(string, typeTemplate)), DSL.optional(DSL.field(string2, typeTemplate2)), DSL.optional(DSL.field(string3, typeTemplate3)), DSL.optional(DSL.field(string4, typeTemplate4)), DSL.optional(DSL.field(string5, typeTemplate5)), typeTemplate6);
    }

    @SafeVarargs
    public static TypeTemplate optionalFields(Pair<String, TypeTemplate> ... pairArray) {
        return DSL.and(Stream.concat(Arrays.stream(pairArray).map(pair -> DSL.optional(DSL.field((String)pair.getFirst(), (TypeTemplate)pair.getSecond()))), Stream.of(DSL.remainder())).toList());
    }

    public static TypeTemplate optionalFieldsLazy(Map<String, Supplier<TypeTemplate>> map) {
        return DSL.and(Stream.concat(map.entrySet().stream().map(entry -> DSL.optional(DSL.field((String)entry.getKey(), (TypeTemplate)((Supplier)entry.getValue()).get()))), Stream.of(DSL.remainder())).toList());
    }

    public static OpticFinder<Dynamic<?>> remainderFinder() {
        return Instances.REMAINDER_FINDER;
    }

    public static <FT> OpticFinder<FT> typeFinder(Type<FT> type) {
        return new FieldFinder<FT>(null, type);
    }

    public static <FT> OpticFinder<FT> fieldFinder(String string, Type<FT> type) {
        return new FieldFinder<FT>(string, type);
    }

    public static <FT> OpticFinder<FT> namedChoice(String string, Type<FT> type) {
        return new NamedChoiceFinder<FT>(string, type);
    }

    public static Unit unit() {
        return Unit.INSTANCE;
    }

    public static final class Instances {
        private static final Type<Boolean> BOOL_TYPE = new Const.PrimitiveType<Boolean>(Codec.BOOL);
        private static final Type<Integer> INT_TYPE = new Const.PrimitiveType<Integer>(Codec.INT);
        private static final Type<Long> LONG_TYPE = new Const.PrimitiveType<Long>(Codec.LONG);
        private static final Type<Byte> BYTE_TYPE = new Const.PrimitiveType<Byte>(Codec.BYTE);
        private static final Type<Short> SHORT_TYPE = new Const.PrimitiveType<Short>(Codec.SHORT);
        private static final Type<Float> FLOAT_TYPE = new Const.PrimitiveType<Float>(Codec.FLOAT);
        private static final Type<Double> DOUBLE_TYPE = new Const.PrimitiveType<Double>(Codec.DOUBLE);
        private static final Type<String> STRING_TYPE = new Const.PrimitiveType<String>(Codec.STRING);
        private static final Type<Unit> EMPTY_PART = new EmptyPart();
        private static final Type<Dynamic<?>> EMPTY_PASSTHROUGH = new EmptyPartPassthrough();
        private static final OpticFinder<Dynamic<?>> REMAINDER_FINDER = DSL.remainderType().finder();
        private static final Map<TaggedChoiceCacheKey<?>, Type<? extends Pair<?, ?>>> TAGGED_CHOICE_TYPE_CACHE = Maps.newConcurrentMap();

        public record TaggedChoiceCacheKey<K>(String name, Type<K> keyType, Map<K, ? extends Type<?>> types) {
            public TaggedChoice.TaggedChoiceType<K> build() {
                return new TaggedChoice.TaggedChoiceType<K>(this.name, this.keyType, new Object2ObjectOpenHashMap(this.types));
            }
        }
    }

    public static interface TypeReference {
        public String typeName();

        default public TypeTemplate in(Schema schema) {
            return schema.id(this.typeName());
        }
    }
}

