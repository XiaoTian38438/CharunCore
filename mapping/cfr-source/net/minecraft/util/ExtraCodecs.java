/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.BiMap
 *  com.google.common.collect.HashBiMap
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.google.common.collect.ImmutableMultimap
 *  com.google.common.collect.ImmutableMultimap$Builder
 *  com.google.common.collect.Multimap
 *  com.google.common.primitives.UnsignedBytes
 *  com.google.gson.JsonElement
 *  it.unimi.dsi.fastutil.floats.FloatArrayList
 *  it.unimi.dsi.fastutil.objects.Object2BooleanMap
 *  it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  org.apache.commons.lang3.StringEscapeUtils
 *  org.apache.commons.lang3.mutable.MutableObject
 *  org.joml.AxisAngle4f
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 *  org.joml.Vector2f
 *  org.joml.Vector2fc
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 *  org.joml.Vector3i
 *  org.joml.Vector3ic
 *  org.joml.Vector4f
 *  org.joml.Vector4fc
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.primitives.UnsignedBytes;
import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JavaOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.codecs.BaseMapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Base64;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;
import net.minecraft.core.HolderSet;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.StringUtil;
import net.minecraft.util.Util;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.mutable.MutableObject;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.jspecify.annotations.Nullable;

public class ExtraCodecs {
    public static final Codec<JsonElement> JSON = ExtraCodecs.converter(JsonOps.INSTANCE);
    public static final Codec<Object> JAVA = ExtraCodecs.converter(JavaOps.INSTANCE);
    public static final Codec<Tag> NBT = ExtraCodecs.converter(NbtOps.INSTANCE);
    public static final Codec<Vector2fc> VECTOR2F = Codec.FLOAT.listOf().comapFlatMap(list2 -> Util.fixedSize(list2, 2).map(list -> new Vector2f(((Float)list.get(0)).floatValue(), ((Float)list.get(1)).floatValue())), vector2fc -> List.of(Float.valueOf(vector2fc.x()), Float.valueOf(vector2fc.y())));
    public static final Codec<Vector3fc> VECTOR3F = Codec.FLOAT.listOf().comapFlatMap(list2 -> Util.fixedSize(list2, 3).map(list -> new Vector3f(((Float)list.get(0)).floatValue(), ((Float)list.get(1)).floatValue(), ((Float)list.get(2)).floatValue())), vector3fc -> List.of(Float.valueOf(vector3fc.x()), Float.valueOf(vector3fc.y()), Float.valueOf(vector3fc.z())));
    public static final Codec<Vector3ic> VECTOR3I = Codec.INT.listOf().comapFlatMap(list2 -> Util.fixedSize(list2, 3).map(list -> new Vector3i(((Integer)list.get(0)).intValue(), ((Integer)list.get(1)).intValue(), ((Integer)list.get(2)).intValue())), vector3ic -> List.of(Integer.valueOf(vector3ic.x()), Integer.valueOf(vector3ic.y()), Integer.valueOf(vector3ic.z())));
    public static final Codec<Vector4fc> VECTOR4F = Codec.FLOAT.listOf().comapFlatMap(list2 -> Util.fixedSize(list2, 4).map(list -> new Vector4f(((Float)list.get(0)).floatValue(), ((Float)list.get(1)).floatValue(), ((Float)list.get(2)).floatValue(), ((Float)list.get(3)).floatValue())), vector4fc -> List.of(Float.valueOf(vector4fc.x()), Float.valueOf(vector4fc.y()), Float.valueOf(vector4fc.z()), Float.valueOf(vector4fc.w())));
    public static final Codec<Quaternionfc> QUATERNIONF_COMPONENTS = Codec.FLOAT.listOf().comapFlatMap(list2 -> Util.fixedSize(list2, 4).map(list -> new Quaternionf(((Float)list.get(0)).floatValue(), ((Float)list.get(1)).floatValue(), ((Float)list.get(2)).floatValue(), ((Float)list.get(3)).floatValue()).normalize()), quaternionfc -> List.of(Float.valueOf(quaternionfc.x()), Float.valueOf(quaternionfc.y()), Float.valueOf(quaternionfc.z()), Float.valueOf(quaternionfc.w())));
    public static final Codec<AxisAngle4f> AXISANGLE4F = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Codec.FLOAT.fieldOf("angle")).forGetter(axisAngle4f -> Float.valueOf(axisAngle4f.angle)), ((MapCodec)VECTOR3F.fieldOf("axis")).forGetter(axisAngle4f -> new Vector3f(axisAngle4f.x, axisAngle4f.y, axisAngle4f.z))).apply((Applicative<AxisAngle4f, ?>)instance, AxisAngle4f::new));
    public static final Codec<Quaternionfc> QUATERNIONF = Codec.withAlternative(QUATERNIONF_COMPONENTS, AXISANGLE4F.xmap(Quaternionf::new, AxisAngle4f::new));
    public static final Codec<Matrix4fc> MATRIX4F = Codec.FLOAT.listOf().comapFlatMap(list2 -> Util.fixedSize(list2, 16).map(list -> {
        Matrix4f matrix4f = new Matrix4f();
        for (int i = 0; i < list.size(); ++i) {
            matrix4f.setRowColumn(i >> 2, i & 3, ((Float)list.get(i)).floatValue());
        }
        return matrix4f.determineProperties();
    }), matrix4fc -> {
        FloatArrayList floatArrayList = new FloatArrayList(16);
        for (int i = 0; i < 16; ++i) {
            floatArrayList.add(matrix4fc.getRowColumn(i >> 2, i & 3));
        }
        return floatArrayList;
    });
    private static final String HEX_COLOR_PREFIX = "#";
    public static final Codec<Integer> RGB_COLOR_CODEC = Codec.withAlternative(Codec.INT, VECTOR3F, vector3fc -> ARGB.colorFromFloat(1.0f, vector3fc.x(), vector3fc.y(), vector3fc.z()));
    public static final Codec<Integer> ARGB_COLOR_CODEC = Codec.withAlternative(Codec.INT, VECTOR4F, vector4fc -> ARGB.colorFromFloat(vector4fc.w(), vector4fc.x(), vector4fc.y(), vector4fc.z()));
    public static final Codec<Integer> STRING_RGB_COLOR = Codec.withAlternative(ExtraCodecs.hexColor(6).xmap(ARGB::opaque, ARGB::transparent), RGB_COLOR_CODEC);
    public static final Codec<Integer> STRING_ARGB_COLOR = Codec.withAlternative(ExtraCodecs.hexColor(8), ARGB_COLOR_CODEC);
    public static final Codec<Integer> UNSIGNED_BYTE = Codec.BYTE.flatComapMap(UnsignedBytes::toInt, n -> {
        if (n > 255) {
            return DataResult.error(() -> "Unsigned byte was too large: " + n + " > 255");
        }
        return DataResult.success(n.byteValue());
    });
    public static final Codec<Integer> NON_NEGATIVE_INT = ExtraCodecs.intRangeWithMessage(0, Integer.MAX_VALUE, n -> "Value must be non-negative: " + n);
    public static final Codec<Integer> POSITIVE_INT = ExtraCodecs.intRangeWithMessage(1, Integer.MAX_VALUE, n -> "Value must be positive: " + n);
    public static final Codec<Long> NON_NEGATIVE_LONG = ExtraCodecs.longRangeWithMessage(0L, Long.MAX_VALUE, l -> "Value must be non-negative: " + l);
    public static final Codec<Long> POSITIVE_LONG = ExtraCodecs.longRangeWithMessage(1L, Long.MAX_VALUE, l -> "Value must be positive: " + l);
    public static final Codec<Float> NON_NEGATIVE_FLOAT = ExtraCodecs.floatRangeMinInclusiveWithMessage(0.0f, Float.MAX_VALUE, f -> "Value must be non-negative: " + f);
    public static final Codec<Float> POSITIVE_FLOAT = ExtraCodecs.floatRangeMinExclusiveWithMessage(0.0f, Float.MAX_VALUE, f -> "Value must be positive: " + f);
    public static final Codec<Pattern> PATTERN = Codec.STRING.comapFlatMap(string -> {
        try {
            return DataResult.success(Pattern.compile(string));
        }
        catch (PatternSyntaxException patternSyntaxException) {
            return DataResult.error(() -> "Invalid regex pattern '" + string + "': " + patternSyntaxException.getMessage());
        }
    }, Pattern::pattern);
    public static final Codec<Instant> INSTANT_ISO8601 = ExtraCodecs.temporalCodec(DateTimeFormatter.ISO_INSTANT).xmap(Instant::from, Function.identity());
    public static final Codec<byte[]> BASE64_STRING = Codec.STRING.comapFlatMap(string -> {
        try {
            return DataResult.success(Base64.getDecoder().decode((String)string));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return DataResult.error(() -> "Malformed base64 string");
        }
    }, byArray -> Base64.getEncoder().encodeToString((byte[])byArray));
    public static final Codec<String> ESCAPED_STRING = Codec.STRING.comapFlatMap(string -> DataResult.success(StringEscapeUtils.unescapeJava((String)string)), StringEscapeUtils::escapeJava);
    public static final Codec<TagOrElementLocation> TAG_OR_ELEMENT_ID = Codec.STRING.comapFlatMap(string -> string.startsWith(HEX_COLOR_PREFIX) ? Identifier.read(string.substring(1)).map(identifier -> new TagOrElementLocation((Identifier)identifier, true)) : Identifier.read(string).map(identifier -> new TagOrElementLocation((Identifier)identifier, false)), TagOrElementLocation::decoratedId);
    public static final Function<Optional<Long>, OptionalLong> toOptionalLong = optional -> optional.map(OptionalLong::of).orElseGet(OptionalLong::empty);
    public static final Function<OptionalLong, Optional<Long>> fromOptionalLong = optionalLong -> optionalLong.isPresent() ? Optional.of(optionalLong.getAsLong()) : Optional.empty();
    public static final Codec<BitSet> BIT_SET = Codec.LONG_STREAM.xmap(longStream -> BitSet.valueOf(longStream.toArray()), bitSet -> Arrays.stream(bitSet.toLongArray()));
    public static final int MAX_PROPERTY_NAME_LENGTH = 64;
    public static final int MAX_PROPERTY_VALUE_LENGTH = Short.MAX_VALUE;
    public static final int MAX_PROPERTY_SIGNATURE_LENGTH = 1024;
    public static final int MAX_PROPERTIES = 16;
    private static final Codec<Property> PROPERTY = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Codec.sizeLimitedString(64).fieldOf("name")).forGetter(Property::name), ((MapCodec)Codec.sizeLimitedString(Short.MAX_VALUE).fieldOf("value")).forGetter(Property::value), Codec.sizeLimitedString(1024).optionalFieldOf("signature").forGetter(property -> Optional.ofNullable(property.signature()))).apply((Applicative<Property, ?>)instance, (string, string2, optional) -> new Property((String)string, (String)string2, optional.orElse(null))));
    public static final Codec<PropertyMap> PROPERTY_MAP = Codec.either(Codec.unboundedMap(Codec.STRING, Codec.STRING.listOf()).validate(map -> map.size() > 16 ? DataResult.error(() -> "Cannot have more than 16 properties, but was " + map.size()) : DataResult.success(map)), PROPERTY.sizeLimitedListOf(16)).xmap(either -> {
        ImmutableMultimap.Builder builder = ImmutableMultimap.builder();
        either.ifLeft(map -> map.forEach((string, list) -> {
            for (String string2 : list) {
                builder.put(string, (Object)new Property((String)string, string2));
            }
        })).ifRight(list -> {
            for (Property property : list) {
                builder.put((Object)property.name(), (Object)property);
            }
        });
        return new PropertyMap((Multimap<String, Property>)builder.build());
    }, propertyMap -> Either.right(propertyMap.values().stream().toList()));
    public static final Codec<String> PLAYER_NAME = Codec.string(0, 16).validate(string -> {
        if (StringUtil.isValidPlayerName(string)) {
            return DataResult.success(string);
        }
        return DataResult.error(() -> "Player name contained disallowed characters: '" + string + "'");
    });
    public static final Codec<GameProfile> AUTHLIB_GAME_PROFILE = ExtraCodecs.gameProfileCodec(UUIDUtil.AUTHLIB_CODEC).codec();
    public static final MapCodec<GameProfile> STORED_GAME_PROFILE = ExtraCodecs.gameProfileCodec(UUIDUtil.CODEC);
    public static final Codec<String> NON_EMPTY_STRING = Codec.STRING.validate(string -> string.isEmpty() ? DataResult.error(() -> "Expected non-empty string") : DataResult.success(string));
    public static final Codec<Integer> CODEPOINT = Codec.STRING.comapFlatMap(string -> {
        int[] nArray = string.codePoints().toArray();
        if (nArray.length != 1) {
            return DataResult.error(() -> "Expected one codepoint, got: " + string);
        }
        return DataResult.success(nArray[0]);
    }, Character::toString);
    public static final Codec<String> RESOURCE_PATH_CODEC = Codec.STRING.validate(string -> {
        if (!Identifier.isValidPath(string)) {
            return DataResult.error(() -> "Invalid string to use as a resource path element: " + string);
        }
        return DataResult.success(string);
    });
    public static final Codec<URI> UNTRUSTED_URI = Codec.STRING.comapFlatMap(string -> {
        try {
            return DataResult.success(Util.parseAndValidateUntrustedUri(string));
        }
        catch (URISyntaxException uRISyntaxException) {
            return DataResult.error(uRISyntaxException::getMessage);
        }
    }, URI::toString);
    public static final Codec<String> CHAT_STRING = Codec.STRING.validate(string -> {
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (StringUtil.isAllowedChatCharacter(c)) continue;
            return DataResult.error(() -> "Disallowed chat character: '" + c + "'");
        }
        return DataResult.success(string);
    });

    public static <T> Codec<T> converter(DynamicOps<T> dynamicOps) {
        return Codec.PASSTHROUGH.xmap(dynamic -> dynamic.convert(dynamicOps).getValue(), object -> new Dynamic<Object>(dynamicOps, object));
    }

    private static Codec<Integer> hexColor(int n) {
        long l = (1L << n * 4) - 1L;
        return Codec.STRING.comapFlatMap(string -> {
            if (!string.startsWith(HEX_COLOR_PREFIX)) {
                return DataResult.error(() -> "Hex color must begin with #");
            }
            int n2 = string.length() - HEX_COLOR_PREFIX.length();
            if (n2 != n) {
                return DataResult.error(() -> "Hex color is wrong size, expected " + n + " digits but got " + n2);
            }
            try {
                long l2 = HexFormat.fromHexDigitsToLong(string, HEX_COLOR_PREFIX.length(), string.length());
                if (l2 < 0L || l2 > l) {
                    return DataResult.error(() -> "Color value out of range: " + string);
                }
                return DataResult.success((int)l2);
            }
            catch (NumberFormatException numberFormatException) {
                return DataResult.error(() -> "Invalid color value: " + string);
            }
        }, n2 -> HEX_COLOR_PREFIX + HexFormat.of().toHexDigits(n2.intValue(), n));
    }

    public static <P, I> Codec<I> intervalCodec(Codec<P> codec, String string, String string2, BiFunction<P, P, DataResult<I>> biFunction, Function<I, P> function, Function<I, P> function2) {
        Codec<Object> codec2 = Codec.list(codec).comapFlatMap(list2 -> Util.fixedSize(list2, 2).flatMap(list -> {
            Object e = list.get(0);
            Object e2 = list.get(1);
            return (DataResult)biFunction.apply(e, e2);
        }), object -> ImmutableList.of(function.apply(object), function2.apply(object)));
        Codec<Object> codec3 = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)codec.fieldOf(string)).forGetter(Pair::getFirst), ((MapCodec)codec.fieldOf(string2)).forGetter(Pair::getSecond)).apply((Applicative<Pair, ?>)instance, Pair::of)).comapFlatMap(pair -> (DataResult)biFunction.apply(pair.getFirst(), pair.getSecond()), object -> Pair.of(function.apply(object), function2.apply(object)));
        Codec<Object> codec4 = Codec.withAlternative(codec2, codec3);
        return Codec.either(codec, codec4).comapFlatMap(either -> either.map(object -> (DataResult)biFunction.apply(object, object), DataResult::success), object -> {
            Object r;
            Object r2 = function.apply(object);
            if (Objects.equals(r2, r = function2.apply(object))) {
                return Either.left(r2);
            }
            return Either.right(object);
        });
    }

    public static <A> Codec.ResultFunction<A> orElsePartial(final A a) {
        return new Codec.ResultFunction<A>(){

            @Override
            public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> dynamicOps, T t, DataResult<Pair<A, T>> dataResult) {
                MutableObject mutableObject = new MutableObject();
                Optional optional = dataResult.resultOrPartial(arg_0 -> ((MutableObject)mutableObject).setValue(arg_0));
                if (optional.isPresent()) {
                    return dataResult;
                }
                return DataResult.error(() -> "(" + (String)mutableObject.get() + " -> using default)", Pair.of(a, t));
            }

            @Override
            public <T> DataResult<T> coApply(DynamicOps<T> dynamicOps, A a2, DataResult<T> dataResult) {
                return dataResult;
            }

            public String toString() {
                return "OrElsePartial[" + String.valueOf(a) + "]";
            }
        };
    }

    public static <E> Codec<E> idResolverCodec(ToIntFunction<E> toIntFunction, IntFunction<@Nullable E> intFunction, int n2) {
        return Codec.INT.flatXmap(n -> Optional.ofNullable(intFunction.apply((int)n)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown element id: " + n)), object -> {
            int n2 = toIntFunction.applyAsInt(object);
            return n2 == n2 ? DataResult.error(() -> "Element with unknown id: " + String.valueOf(object)) : DataResult.success(n2);
        });
    }

    public static <I, E> Codec<E> idResolverCodec(Codec<I> codec, Function<I, @Nullable E> function, Function<E, @Nullable I> function2) {
        return codec.flatXmap(object -> {
            Object r = function.apply(object);
            return r == null ? DataResult.error(() -> "Unknown element id: " + String.valueOf(object)) : DataResult.success(r);
        }, object -> {
            Object r = function2.apply(object);
            if (r == null) {
                return DataResult.error(() -> "Element with unknown id: " + String.valueOf(object));
            }
            return DataResult.success(r);
        });
    }

    public static <E> Codec<E> orCompressed(final Codec<E> codec, final Codec<E> codec2) {
        return new Codec<E>(){

            @Override
            public <T> DataResult<T> encode(E e, DynamicOps<T> dynamicOps, T t) {
                if (dynamicOps.compressMaps()) {
                    return codec2.encode(e, dynamicOps, t);
                }
                return codec.encode(e, dynamicOps, t);
            }

            @Override
            public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> dynamicOps, T t) {
                if (dynamicOps.compressMaps()) {
                    return codec2.decode(dynamicOps, t);
                }
                return codec.decode(dynamicOps, t);
            }

            public String toString() {
                return String.valueOf(codec) + " orCompressed " + String.valueOf(codec2);
            }
        };
    }

    public static <E> MapCodec<E> orCompressed(final MapCodec<E> mapCodec, final MapCodec<E> mapCodec2) {
        return new MapCodec<E>(){

            @Override
            public <T> RecordBuilder<T> encode(E e, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                if (dynamicOps.compressMaps()) {
                    return mapCodec2.encode(e, dynamicOps, recordBuilder);
                }
                return mapCodec.encode(e, dynamicOps, recordBuilder);
            }

            @Override
            public <T> DataResult<E> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                if (dynamicOps.compressMaps()) {
                    return mapCodec2.decode(dynamicOps, mapLike);
                }
                return mapCodec.decode(dynamicOps, mapLike);
            }

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return mapCodec2.keys(dynamicOps);
            }

            public String toString() {
                return String.valueOf(mapCodec) + " orCompressed " + String.valueOf(mapCodec2);
            }
        };
    }

    public static <E> Codec<E> overrideLifecycle(Codec<E> codec, final Function<E, Lifecycle> function, final Function<E, Lifecycle> function2) {
        return codec.mapResult(new Codec.ResultFunction<E>(){

            @Override
            public <T> DataResult<Pair<E, T>> apply(DynamicOps<T> dynamicOps, T t, DataResult<Pair<E, T>> dataResult) {
                return dataResult.result().map(pair -> dataResult.setLifecycle((Lifecycle)function.apply(pair.getFirst()))).orElse(dataResult);
            }

            @Override
            public <T> DataResult<T> coApply(DynamicOps<T> dynamicOps, E e, DataResult<T> dataResult) {
                return dataResult.setLifecycle((Lifecycle)function2.apply(e));
            }

            public String toString() {
                return "WithLifecycle[" + String.valueOf(function) + " " + String.valueOf(function2) + "]";
            }
        });
    }

    public static <E> Codec<E> overrideLifecycle(Codec<E> codec, Function<E, Lifecycle> function) {
        return ExtraCodecs.overrideLifecycle(codec, function, function);
    }

    public static <K, V> StrictUnboundedMapCodec<K, V> strictUnboundedMap(Codec<K> codec, Codec<V> codec2) {
        return new StrictUnboundedMapCodec<K, V>(codec, codec2);
    }

    public static <E> Codec<List<E>> compactListCodec(Codec<E> codec) {
        return ExtraCodecs.compactListCodec(codec, codec.listOf());
    }

    public static <E> Codec<List<E>> compactListCodec(Codec<E> codec, Codec<List<E>> codec2) {
        return Codec.either(codec2, codec).xmap(either -> either.map(list -> list, List::of), list -> list.size() == 1 ? Either.right(list.getFirst()) : Either.left(list));
    }

    private static Codec<Integer> intRangeWithMessage(int n, int n2, Function<Integer, String> function) {
        return Codec.INT.validate(n3 -> {
            if (n3.compareTo(n) >= 0 && n3.compareTo(n2) <= 0) {
                return DataResult.success(n3);
            }
            return DataResult.error(() -> (String)function.apply((Integer)n3));
        });
    }

    public static Codec<Integer> intRange(int n, int n2) {
        return ExtraCodecs.intRangeWithMessage(n, n2, n3 -> "Value must be within range [" + n + ";" + n2 + "]: " + n3);
    }

    private static Codec<Long> longRangeWithMessage(long l, long l2, Function<Long, String> function) {
        return Codec.LONG.validate(l3 -> {
            if ((long)l3.compareTo(l) >= 0L && (long)l3.compareTo(l2) <= 0L) {
                return DataResult.success(l3);
            }
            return DataResult.error(() -> (String)function.apply((Long)l3));
        });
    }

    public static Codec<Long> longRange(int n, int n2) {
        return ExtraCodecs.longRangeWithMessage(n, n2, l -> "Value must be within range [" + n + ";" + n2 + "]: " + l);
    }

    private static Codec<Float> floatRangeMinInclusiveWithMessage(float f, float f2, Function<Float, String> function) {
        return Codec.FLOAT.validate(f3 -> {
            if (f3.compareTo(Float.valueOf(f)) >= 0 && f3.compareTo(Float.valueOf(f2)) <= 0) {
                return DataResult.success(f3);
            }
            return DataResult.error(() -> (String)function.apply((Float)f3));
        });
    }

    private static Codec<Float> floatRangeMinExclusiveWithMessage(float f, float f2, Function<Float, String> function) {
        return Codec.FLOAT.validate(f3 -> {
            if (f3.compareTo(Float.valueOf(f)) > 0 && f3.compareTo(Float.valueOf(f2)) <= 0) {
                return DataResult.success(f3);
            }
            return DataResult.error(() -> (String)function.apply((Float)f3));
        });
    }

    public static Codec<Float> floatRange(float f, float f2) {
        return ExtraCodecs.floatRangeMinInclusiveWithMessage(f, f2, f3 -> "Value must be within range [" + f + ";" + f2 + "]: " + f3);
    }

    public static <T> Codec<List<T>> nonEmptyList(Codec<List<T>> codec) {
        return codec.validate(list -> list.isEmpty() ? DataResult.error(() -> "List must have contents") : DataResult.success(list));
    }

    public static <T> Codec<HolderSet<T>> nonEmptyHolderSet(Codec<HolderSet<T>> codec) {
        return codec.validate(holderSet -> {
            if (holderSet.unwrap().right().filter(List::isEmpty).isPresent()) {
                return DataResult.error(() -> "List must have contents");
            }
            return DataResult.success(holderSet);
        });
    }

    public static <M extends Map<?, ?>> Codec<M> nonEmptyMap(Codec<M> codec) {
        return codec.validate(map -> map.isEmpty() ? DataResult.error(() -> "Map must have contents") : DataResult.success(map));
    }

    public static <E> MapCodec<E> retrieveContext(Function<DynamicOps<?>, DataResult<E>> function) {
        class ContextRetrievalCodec
        extends MapCodec<E> {
            final /* synthetic */ Function val$getter;

            ContextRetrievalCodec(Function function) {
                this.val$getter = function;
            }

            @Override
            public <T> RecordBuilder<T> encode(E e, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                return recordBuilder;
            }

            @Override
            public <T> DataResult<E> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                return (DataResult)this.val$getter.apply(dynamicOps);
            }

            public String toString() {
                return "ContextRetrievalCodec[" + String.valueOf(this.val$getter) + "]";
            }

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return Stream.empty();
            }
        }
        return new ContextRetrievalCodec(function);
    }

    public static <E, L extends Collection<E>, T> Function<L, DataResult<L>> ensureHomogenous(Function<E, T> function) {
        return collection -> {
            Iterator iterator = collection.iterator();
            if (iterator.hasNext()) {
                Object r = function.apply(iterator.next());
                while (iterator.hasNext()) {
                    Object e = iterator.next();
                    Object r2 = function.apply(e);
                    if (r2 == r) continue;
                    return DataResult.error(() -> "Mixed type list: element " + String.valueOf(e) + " had type " + String.valueOf(r2) + ", but list is of type " + String.valueOf(r));
                }
            }
            return DataResult.success(collection, Lifecycle.stable());
        };
    }

    public static <A> Codec<A> catchDecoderException(final Codec<A> codec) {
        return Codec.of(codec, new Decoder<A>(){

            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
                try {
                    return codec.decode(dynamicOps, t);
                }
                catch (Exception exception) {
                    return DataResult.error(() -> "Caught exception decoding " + String.valueOf(t) + ": " + exception.getMessage());
                }
            }
        });
    }

    public static Codec<TemporalAccessor> temporalCodec(DateTimeFormatter dateTimeFormatter) {
        return Codec.STRING.comapFlatMap(string -> {
            try {
                return DataResult.success(dateTimeFormatter.parse((CharSequence)string));
            }
            catch (Exception exception) {
                return DataResult.error(exception::getMessage);
            }
        }, dateTimeFormatter::format);
    }

    public static MapCodec<OptionalLong> asOptionalLong(MapCodec<Optional<Long>> mapCodec) {
        return mapCodec.xmap(toOptionalLong, fromOptionalLong);
    }

    private static MapCodec<GameProfile> gameProfileCodec(Codec<UUID> codec) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)codec.fieldOf("id")).forGetter(GameProfile::id), ((MapCodec)PLAYER_NAME.fieldOf("name")).forGetter(GameProfile::name), PROPERTY_MAP.optionalFieldOf("properties", PropertyMap.EMPTY).forGetter(GameProfile::properties)).apply((Applicative<GameProfile, ?>)instance, GameProfile::new));
    }

    public static <K, V> Codec<Map<K, V>> sizeLimitedMap(Codec<Map<K, V>> codec, int n) {
        return codec.validate(map -> {
            if (map.size() > n) {
                return DataResult.error(() -> "Map is too long: " + map.size() + ", expected range [0-" + n + "]");
            }
            return DataResult.success(map);
        });
    }

    public static <T> Codec<Object2BooleanMap<T>> object2BooleanMap(Codec<T> codec) {
        return Codec.unboundedMap(codec, Codec.BOOL).xmap(Object2BooleanOpenHashMap::new, Object2ObjectOpenHashMap::new);
    }

    @Deprecated
    public static <K, V> MapCodec<V> dispatchOptionalValue(final String string, final String string2, final Codec<K> codec, final Function<? super V, ? extends K> function, final Function<? super K, ? extends Codec<? extends V>> function2) {
        return new MapCodec<V>(){

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return Stream.of(dynamicOps.createString(string), dynamicOps.createString(string2));
            }

            @Override
            public <T> DataResult<V> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                T t = mapLike.get(string);
                if (t == null) {
                    return DataResult.error(() -> "Missing \"" + string + "\" in: " + String.valueOf(mapLike));
                }
                return codec.decode(dynamicOps, t).flatMap((? super R pair) -> {
                    Object object = Objects.requireNonNullElseGet(mapLike.get(string2), dynamicOps::emptyMap);
                    return ((Codec)function2.apply(pair.getFirst())).decode(dynamicOps, object).map(Pair::getFirst);
                });
            }

            @Override
            public <T> RecordBuilder<T> encode(V v, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                Object r = function.apply(v);
                recordBuilder.add(string, codec.encodeStart(dynamicOps, r));
                DataResult<T> dataResult = this.encode((Codec)function2.apply(r), v, dynamicOps);
                if (dataResult.result().isEmpty() || !Objects.equals(dataResult.result().get(), dynamicOps.emptyMap())) {
                    recordBuilder.add(string2, dataResult);
                }
                return recordBuilder;
            }

            private <T, V2 extends V> DataResult<T> encode(Codec<V2> codec2, V v, DynamicOps<T> dynamicOps) {
                return codec2.encodeStart(dynamicOps, v);
            }
        };
    }

    public static <A> Codec<Optional<A>> optionalEmptyMap(final Codec<A> codec) {
        return new Codec<Optional<A>>(){

            @Override
            public <T> DataResult<Pair<Optional<A>, T>> decode(DynamicOps<T> dynamicOps, T t) {
                if (7.isEmptyMap(dynamicOps, t)) {
                    return DataResult.success(Pair.of(Optional.empty(), t));
                }
                return codec.decode(dynamicOps, t).map((? super R pair) -> pair.mapFirst(Optional::of));
            }

            private static <T> boolean isEmptyMap(DynamicOps<T> dynamicOps, T t) {
                Optional<MapLike<T>> optional = dynamicOps.getMap(t).result();
                return optional.isPresent() && optional.get().entries().findAny().isEmpty();
            }

            @Override
            public <T> DataResult<T> encode(Optional<A> optional, DynamicOps<T> dynamicOps, T t) {
                if (optional.isEmpty()) {
                    return DataResult.success(dynamicOps.emptyMap());
                }
                return codec.encode(optional.get(), dynamicOps, t);
            }

            @Override
            public /* synthetic */ DataResult encode(Object object, DynamicOps dynamicOps, Object object2) {
                return this.encode((Optional)object, dynamicOps, object2);
            }
        };
    }

    @Deprecated
    public static <E extends Enum<E>> Codec<E> legacyEnum(Function<String, E> function) {
        return Codec.STRING.comapFlatMap(string -> {
            try {
                return DataResult.success((Enum)function.apply((String)string));
            }
            catch (IllegalArgumentException illegalArgumentException) {
                return DataResult.error(() -> "No value with id: " + string);
            }
        }, Enum::toString);
    }

    public record StrictUnboundedMapCodec<K, V>(Codec<K> keyCodec, Codec<V> elementCodec) implements Codec<Map<K, V>>,
    BaseMapCodec<K, V>
    {
        @Override
        public <T> DataResult<Map<K, V>> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
            ImmutableMap.Builder builder = ImmutableMap.builder();
            for (Pair<T, T> pair : mapLike.entries().toList()) {
                Object object;
                DataResult dataResult;
                DataResult dataResult2 = this.keyCodec().parse(dynamicOps, pair.getFirst());
                DataResult<Pair> dataResult3 = dataResult2.apply2stable(Pair::of, dataResult = this.elementCodec().parse(dynamicOps, pair.getSecond()));
                Optional<DataResult.Error<Pair>> optional = dataResult3.error();
                if (optional.isPresent()) {
                    object = optional.get().message();
                    return DataResult.error(() -> StrictUnboundedMapCodec.lambda$decode$0(dataResult2, (String)object));
                }
                if (dataResult3.result().isPresent()) {
                    object = dataResult3.result().get();
                    builder.put(((Pair)object).getFirst(), ((Pair)object).getSecond());
                    continue;
                }
                return DataResult.error(() -> "Empty or invalid map contents are not allowed");
            }
            ImmutableMap immutableMap = builder.build();
            return DataResult.success(immutableMap);
        }

        @Override
        public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> dynamicOps, T t) {
            return dynamicOps.getMap(t).setLifecycle(Lifecycle.stable()).flatMap((? super R mapLike) -> this.decode(dynamicOps, (Object)mapLike)).map((? super R map) -> Pair.of(map, t));
        }

        @Override
        public <T> DataResult<T> encode(Map<K, V> map, DynamicOps<T> dynamicOps, T t) {
            return this.encode(map, dynamicOps, dynamicOps.mapBuilder()).build(t);
        }

        @Override
        public String toString() {
            return "StrictUnboundedMapCodec[" + String.valueOf(this.keyCodec) + " -> " + String.valueOf(this.elementCodec) + "]";
        }

        @Override
        public /* synthetic */ DataResult encode(Object object, DynamicOps dynamicOps, Object object2) {
            return this.encode((Map)object, dynamicOps, object2);
        }

        private static /* synthetic */ String lambda$decode$0(DataResult dataResult, String string) {
            if (dataResult.result().isPresent()) {
                return "Map entry '" + String.valueOf(dataResult.result().get()) + "' : " + string;
            }
            return string;
        }
    }

    public record TagOrElementLocation(Identifier id, boolean tag) {
        @Override
        public String toString() {
            return this.decoratedId();
        }

        private String decoratedId() {
            return this.tag ? ExtraCodecs.HEX_COLOR_PREFIX + String.valueOf(this.id) : this.id.toString();
        }
    }

    public static class LateBoundIdMapper<I, V> {
        private final BiMap<I, V> idToValue = HashBiMap.create();

        public Codec<V> codec(Codec<I> codec) {
            BiMap biMap = this.idToValue.inverse();
            return ExtraCodecs.idResolverCodec(codec, arg_0 -> this.idToValue.get(arg_0), arg_0 -> biMap.get(arg_0));
        }

        public LateBoundIdMapper<I, V> put(I i, V v) {
            Objects.requireNonNull(v, () -> "Value for " + String.valueOf(i) + " is null");
            this.idToValue.put(i, v);
            return this;
        }

        public Set<V> values() {
            return Collections.unmodifiableSet(this.idToValue.values());
        }
    }
}

