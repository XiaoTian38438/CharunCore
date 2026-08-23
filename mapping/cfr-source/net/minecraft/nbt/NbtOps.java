/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.bytes.ByteArrayList
 *  it.unimi.dsi.fastutil.ints.IntArrayList
 *  it.unimi.dsi.fastutil.longs.LongArrayList
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.nbt;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.lang.runtime.SwitchBootstraps;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

public class NbtOps
implements DynamicOps<Tag> {
    public static final NbtOps INSTANCE = new NbtOps();

    private NbtOps() {
    }

    @Override
    public Tag empty() {
        return EndTag.INSTANCE;
    }

    @Override
    public Tag emptyList() {
        return new ListTag();
    }

    @Override
    public Tag emptyMap() {
        return new CompoundTag();
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public <U> U convertTo(DynamicOps<U> dynamicOps, Tag tag) {
        U u;
        Tag tag2 = tag;
        Objects.requireNonNull(tag2);
        Tag tag3 = tag2;
        int n = 0;
        switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{EndTag.class, ByteTag.class, ShortTag.class, IntTag.class, LongTag.class, FloatTag.class, DoubleTag.class, ByteArrayTag.class, StringTag.class, ListTag.class, CompoundTag.class, IntArrayTag.class, LongArrayTag.class}, (Object)tag3, n)) {
            default: {
                throw new MatchException(null, null);
            }
            case 0: {
                EndTag endTag = (EndTag)tag3;
                u = dynamicOps.empty();
                return u;
            }
            case 1: {
                ByteTag byteTag = (ByteTag)tag3;
                try {
                    byte by;
                    byte by2 = by = byteTag.value();
                    u = dynamicOps.createByte(by2);
                    return u;
                }
                catch (Throwable throwable) {
                    throw new MatchException(throwable.toString(), throwable);
                }
            }
            case 2: {
                ShortTag shortTag = (ShortTag)tag3;
                {
                    short s;
                    short s2 = s = shortTag.value();
                    u = dynamicOps.createShort(s2);
                    return u;
                }
            }
            case 3: {
                IntTag intTag = (IntTag)tag3;
                {
                    int n2;
                    int n3 = n2 = intTag.value();
                    u = dynamicOps.createInt(n3);
                    return u;
                }
            }
            case 4: {
                LongTag longTag = (LongTag)tag3;
                {
                    long l;
                    long l2 = l = longTag.value();
                    u = dynamicOps.createLong(l2);
                    return u;
                }
            }
            case 5: {
                FloatTag floatTag = (FloatTag)tag3;
                {
                    float f;
                    float f2 = f = floatTag.value();
                    u = dynamicOps.createFloat(f2);
                    return u;
                }
            }
            case 6: {
                DoubleTag doubleTag = (DoubleTag)tag3;
                {
                    double d;
                    double d2 = d = doubleTag.value();
                    u = dynamicOps.createDouble(d2);
                    return u;
                }
            }
            case 7: {
                ByteArrayTag byteArrayTag = (ByteArrayTag)tag3;
                u = dynamicOps.createByteList(ByteBuffer.wrap(byteArrayTag.getAsByteArray()));
                return u;
            }
            case 8: {
                StringTag stringTag = (StringTag)tag3;
                {
                    String string;
                    String string2 = string = stringTag.value();
                    u = dynamicOps.createString(string2);
                    return u;
                }
            }
            case 9: {
                ListTag listTag = (ListTag)tag3;
                u = this.convertList(dynamicOps, listTag);
                return u;
            }
            case 10: {
                CompoundTag compoundTag = (CompoundTag)tag3;
                u = this.convertMap(dynamicOps, compoundTag);
                return u;
            }
            case 11: {
                IntArrayTag intArrayTag = (IntArrayTag)tag3;
                u = dynamicOps.createIntList(Arrays.stream(intArrayTag.getAsIntArray()));
                return u;
            }
            case 12: 
        }
        LongArrayTag longArrayTag = (LongArrayTag)tag3;
        u = dynamicOps.createLongList(Arrays.stream(longArrayTag.getAsLongArray()));
        return u;
    }

    @Override
    public DataResult<Number> getNumberValue(Tag tag) {
        return tag.asNumber().map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Not a number"));
    }

    @Override
    public Tag createNumeric(Number number) {
        return DoubleTag.valueOf(number.doubleValue());
    }

    @Override
    public Tag createByte(byte by) {
        return ByteTag.valueOf(by);
    }

    @Override
    public Tag createShort(short s) {
        return ShortTag.valueOf(s);
    }

    @Override
    public Tag createInt(int n) {
        return IntTag.valueOf(n);
    }

    @Override
    public Tag createLong(long l) {
        return LongTag.valueOf(l);
    }

    @Override
    public Tag createFloat(float f) {
        return FloatTag.valueOf(f);
    }

    @Override
    public Tag createDouble(double d) {
        return DoubleTag.valueOf(d);
    }

    @Override
    public Tag createBoolean(boolean bl) {
        return ByteTag.valueOf(bl);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public DataResult<String> getStringValue(Tag tag) {
        String string2;
        if (!(tag instanceof StringTag)) return DataResult.error(() -> "Not a string");
        StringTag stringTag = (StringTag)tag;
        try {
            String string;
            string2 = string = stringTag.value();
        }
        catch (Throwable throwable) {
            throw new MatchException(throwable.toString(), throwable);
        }
        return DataResult.success(string2);
    }

    @Override
    public Tag createString(String string) {
        return StringTag.valueOf(string);
    }

    @Override
    public DataResult<Tag> mergeToList(Tag tag, Tag tag2) {
        return NbtOps.createCollector(tag).map(listCollector -> DataResult.success(listCollector.accept(tag2).result())).orElseGet(() -> DataResult.error(() -> "mergeToList called with not a list: " + String.valueOf(tag), tag));
    }

    @Override
    public DataResult<Tag> mergeToList(Tag tag, List<Tag> list) {
        return NbtOps.createCollector(tag).map(listCollector -> DataResult.success(listCollector.acceptAll(list).result())).orElseGet(() -> DataResult.error(() -> "mergeToList called with not a list: " + String.valueOf(tag), tag));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public DataResult<Tag> mergeToMap(Tag tag, Tag tag2, Tag tag3) {
        CompoundTag compoundTag;
        String string;
        Object object;
        if (!(tag instanceof CompoundTag) && !(tag instanceof EndTag)) {
            return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(tag), tag);
        }
        if (!(tag2 instanceof StringTag)) return DataResult.error(() -> "key is not a string: " + String.valueOf(tag2), tag);
        Tag tag4 = (StringTag)tag2;
        try {
            object = ((StringTag)tag4).value();
            string = object;
        }
        catch (Throwable throwable) {
            throw new MatchException(throwable.toString(), throwable);
        }
        if (tag instanceof CompoundTag) {
            object = (CompoundTag)tag;
            compoundTag = ((CompoundTag)object).shallowCopy();
        } else {
            compoundTag = new CompoundTag();
        }
        tag4 = compoundTag;
        ((CompoundTag)tag4).put(string, tag3);
        return DataResult.success(tag4);
    }

    @Override
    public DataResult<Tag> mergeToMap(Tag tag, MapLike<Tag> mapLike) {
        CompoundTag compoundTag;
        Object object;
        if (!(tag instanceof CompoundTag) && !(tag instanceof EndTag)) {
            return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(tag), tag);
        }
        Iterator iterator = mapLike.entries().iterator();
        if (!iterator.hasNext()) {
            if (tag == this.empty()) {
                return DataResult.success(this.emptyMap());
            }
            return DataResult.success(tag);
        }
        if (tag instanceof CompoundTag) {
            object = (CompoundTag)tag;
            compoundTag = ((CompoundTag)object).shallowCopy();
        } else {
            compoundTag = new CompoundTag();
        }
        CompoundTag compoundTag2 = compoundTag;
        object = new ArrayList();
        iterator.forEachRemaining(arg_0 -> NbtOps.lambda$mergeToMap$12((List)object, compoundTag2, arg_0));
        if (!object.isEmpty()) {
            return DataResult.error(() -> NbtOps.lambda$mergeToMap$13((List)object), compoundTag2);
        }
        return DataResult.success(compoundTag2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public DataResult<Tag> mergeToMap(Tag tag, Map<Tag, Tag> map) {
        CompoundTag compoundTag;
        Object object;
        if (!(tag instanceof CompoundTag) && !(tag instanceof EndTag)) {
            return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(tag), tag);
        }
        if (map.isEmpty()) {
            if (tag == this.empty()) {
                return DataResult.success(this.emptyMap());
            }
            return DataResult.success(tag);
        }
        if (tag instanceof CompoundTag) {
            object = (CompoundTag)tag;
            compoundTag = ((CompoundTag)object).shallowCopy();
        } else {
            compoundTag = new CompoundTag();
        }
        CompoundTag compoundTag2 = compoundTag;
        object = new ArrayList();
        for (Map.Entry<Tag, Tag> entry : map.entrySet()) {
            Tag tag2 = entry.getKey();
            if (tag2 instanceof StringTag) {
                StringTag stringTag = (StringTag)tag2;
                try {
                    String string;
                    String string2 = string = stringTag.value();
                    compoundTag2.put(string2, entry.getValue());
                    continue;
                }
                catch (Throwable throwable) {
                    throw new MatchException(throwable.toString(), throwable);
                }
            }
            object.add(tag2);
        }
        if (!object.isEmpty()) {
            return DataResult.error(() -> NbtOps.lambda$mergeToMap$15((List)object), compoundTag2);
        }
        return DataResult.success(compoundTag2);
    }

    @Override
    public DataResult<Stream<Pair<Tag, Tag>>> getMapValues(Tag tag) {
        if (tag instanceof CompoundTag) {
            CompoundTag compoundTag = (CompoundTag)tag;
            return DataResult.success(compoundTag.entrySet().stream().map(entry -> Pair.of(this.createString((String)entry.getKey()), (Tag)entry.getValue())));
        }
        return DataResult.error(() -> "Not a map: " + String.valueOf(tag));
    }

    @Override
    public DataResult<Consumer<BiConsumer<Tag, Tag>>> getMapEntries(Tag tag) {
        if (tag instanceof CompoundTag) {
            CompoundTag compoundTag = (CompoundTag)tag;
            return DataResult.success(biConsumer -> {
                for (Map.Entry<String, Tag> entry : compoundTag.entrySet()) {
                    biConsumer.accept(this.createString(entry.getKey()), entry.getValue());
                }
            });
        }
        return DataResult.error(() -> "Not a map: " + String.valueOf(tag));
    }

    @Override
    public DataResult<MapLike<Tag>> getMap(Tag tag) {
        if (tag instanceof CompoundTag) {
            final CompoundTag compoundTag = (CompoundTag)tag;
            return DataResult.success(new MapLike<Tag>(){

                /*
                 * Enabled force condition propagation
                 * Lifted jumps to return sites
                 */
                @Override
                public @Nullable Tag get(Tag tag) {
                    if (!(tag instanceof StringTag)) throw new UnsupportedOperationException("Cannot get map entry with non-string key: " + String.valueOf(tag));
                    StringTag stringTag = (StringTag)tag;
                    try {
                        String string;
                        String string2 = string = stringTag.value();
                        return compoundTag.get(string2);
                    }
                    catch (Throwable throwable) {
                        throw new MatchException(throwable.toString(), throwable);
                    }
                }

                @Override
                public @Nullable Tag get(String string) {
                    return compoundTag.get(string);
                }

                @Override
                public Stream<Pair<Tag, Tag>> entries() {
                    return compoundTag.entrySet().stream().map(entry -> Pair.of(NbtOps.this.createString((String)entry.getKey()), (Tag)entry.getValue()));
                }

                public String toString() {
                    return "MapLike[" + String.valueOf(compoundTag) + "]";
                }

                @Override
                public /* synthetic */ @Nullable Object get(String string) {
                    return this.get(string);
                }

                @Override
                public /* synthetic */ @Nullable Object get(Object object) {
                    return this.get((Tag)object);
                }
            });
        }
        return DataResult.error(() -> "Not a map: " + String.valueOf(tag));
    }

    @Override
    public Tag createMap(Stream<Pair<Tag, Tag>> stream) {
        CompoundTag compoundTag = new CompoundTag();
        stream.forEach(pair -> {
            Tag tag = (Tag)pair.getFirst();
            Tag tag2 = (Tag)pair.getSecond();
            if (!(tag instanceof StringTag)) throw new UnsupportedOperationException("Cannot create map with non-string key: " + String.valueOf(tag));
            StringTag stringTag = (StringTag)tag;
            try {
                String string;
                String string2 = string = stringTag.value();
                compoundTag.put(string2, tag2);
            }
            catch (Throwable throwable) {
                throw new MatchException(throwable.toString(), throwable);
            }
        });
        return compoundTag;
    }

    @Override
    public DataResult<Stream<Tag>> getStream(Tag tag) {
        if (tag instanceof CollectionTag) {
            CollectionTag collectionTag = (CollectionTag)tag;
            return DataResult.success(collectionTag.stream());
        }
        return DataResult.error(() -> "Not a list");
    }

    @Override
    public DataResult<Consumer<Consumer<Tag>>> getList(Tag tag) {
        if (tag instanceof CollectionTag) {
            CollectionTag collectionTag = (CollectionTag)tag;
            return DataResult.success(collectionTag::forEach);
        }
        return DataResult.error(() -> "Not a list: " + String.valueOf(tag));
    }

    @Override
    public DataResult<ByteBuffer> getByteBuffer(Tag tag) {
        if (tag instanceof ByteArrayTag) {
            ByteArrayTag byteArrayTag = (ByteArrayTag)tag;
            return DataResult.success(ByteBuffer.wrap(byteArrayTag.getAsByteArray()));
        }
        return DynamicOps.super.getByteBuffer(tag);
    }

    @Override
    public Tag createByteList(ByteBuffer byteBuffer) {
        ByteBuffer byteBuffer2 = byteBuffer.duplicate().clear();
        byte[] byArray = new byte[byteBuffer.capacity()];
        byteBuffer2.get(0, byArray, 0, byArray.length);
        return new ByteArrayTag(byArray);
    }

    @Override
    public DataResult<IntStream> getIntStream(Tag tag) {
        if (tag instanceof IntArrayTag) {
            IntArrayTag intArrayTag = (IntArrayTag)tag;
            return DataResult.success(Arrays.stream(intArrayTag.getAsIntArray()));
        }
        return DynamicOps.super.getIntStream(tag);
    }

    @Override
    public Tag createIntList(IntStream intStream) {
        return new IntArrayTag(intStream.toArray());
    }

    @Override
    public DataResult<LongStream> getLongStream(Tag tag) {
        if (tag instanceof LongArrayTag) {
            LongArrayTag longArrayTag = (LongArrayTag)tag;
            return DataResult.success(Arrays.stream(longArrayTag.getAsLongArray()));
        }
        return DynamicOps.super.getLongStream(tag);
    }

    @Override
    public Tag createLongList(LongStream longStream) {
        return new LongArrayTag(longStream.toArray());
    }

    @Override
    public Tag createList(Stream<Tag> stream) {
        return new ListTag(stream.collect(Util.toMutableList()));
    }

    @Override
    public Tag remove(Tag tag, String string) {
        if (tag instanceof CompoundTag) {
            CompoundTag compoundTag = (CompoundTag)tag;
            CompoundTag compoundTag2 = compoundTag.shallowCopy();
            compoundTag2.remove(string);
            return compoundTag2;
        }
        return tag;
    }

    public String toString() {
        return "NBT";
    }

    @Override
    public RecordBuilder<Tag> mapBuilder() {
        return new NbtRecordBuilder(this);
    }

    private static Optional<ListCollector> createCollector(Tag tag) {
        if (tag instanceof EndTag) {
            return Optional.of(new GenericListCollector());
        }
        if (tag instanceof CollectionTag) {
            CollectionTag collectionTag = (CollectionTag)tag;
            if (collectionTag.isEmpty()) {
                return Optional.of(new GenericListCollector());
            }
            CollectionTag collectionTag2 = collectionTag;
            Objects.requireNonNull(collectionTag2);
            CollectionTag collectionTag3 = collectionTag2;
            int n = 0;
            return switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{ListTag.class, ByteArrayTag.class, IntArrayTag.class, LongArrayTag.class}, (Object)collectionTag3, n)) {
                default -> throw new MatchException(null, null);
                case 0 -> {
                    ListTag var4_4 = (ListTag)collectionTag3;
                    yield Optional.of(new GenericListCollector(var4_4));
                }
                case 1 -> {
                    ByteArrayTag var5_5 = (ByteArrayTag)collectionTag3;
                    yield Optional.of(new ByteListCollector(var5_5.getAsByteArray()));
                }
                case 2 -> {
                    IntArrayTag var6_6 = (IntArrayTag)collectionTag3;
                    yield Optional.of(new IntListCollector(var6_6.getAsIntArray()));
                }
                case 3 -> {
                    LongArrayTag var7_7 = (LongArrayTag)collectionTag3;
                    yield Optional.of(new LongListCollector(var7_7.getAsLongArray()));
                }
            };
        }
        return Optional.empty();
    }

    @Override
    public /* synthetic */ Object remove(Object object, String string) {
        return this.remove((Tag)object, string);
    }

    @Override
    public /* synthetic */ Object createLongList(LongStream longStream) {
        return this.createLongList(longStream);
    }

    @Override
    public /* synthetic */ DataResult getLongStream(Object object) {
        return this.getLongStream((Tag)object);
    }

    @Override
    public /* synthetic */ Object createIntList(IntStream intStream) {
        return this.createIntList(intStream);
    }

    @Override
    public /* synthetic */ DataResult getIntStream(Object object) {
        return this.getIntStream((Tag)object);
    }

    @Override
    public /* synthetic */ Object createByteList(ByteBuffer byteBuffer) {
        return this.createByteList(byteBuffer);
    }

    @Override
    public /* synthetic */ DataResult getByteBuffer(Object object) {
        return this.getByteBuffer((Tag)object);
    }

    @Override
    public /* synthetic */ Object createList(Stream stream) {
        return this.createList(stream);
    }

    @Override
    public /* synthetic */ DataResult getList(Object object) {
        return this.getList((Tag)object);
    }

    @Override
    public /* synthetic */ DataResult getStream(Object object) {
        return this.getStream((Tag)object);
    }

    @Override
    public /* synthetic */ DataResult getMap(Object object) {
        return this.getMap((Tag)object);
    }

    @Override
    public /* synthetic */ Object createMap(Stream stream) {
        return this.createMap(stream);
    }

    @Override
    public /* synthetic */ DataResult getMapEntries(Object object) {
        return this.getMapEntries((Tag)object);
    }

    @Override
    public /* synthetic */ DataResult getMapValues(Object object) {
        return this.getMapValues((Tag)object);
    }

    @Override
    public /* synthetic */ DataResult mergeToMap(Object object, MapLike mapLike) {
        return this.mergeToMap((Tag)object, (MapLike<Tag>)mapLike);
    }

    @Override
    public /* synthetic */ DataResult mergeToMap(Object object, Map map) {
        return this.mergeToMap((Tag)object, (Map<Tag, Tag>)map);
    }

    @Override
    public /* synthetic */ DataResult mergeToMap(Object object, Object object2, Object object3) {
        return this.mergeToMap((Tag)object, (Tag)object2, (Tag)object3);
    }

    @Override
    public /* synthetic */ DataResult mergeToList(Object object, List list) {
        return this.mergeToList((Tag)object, (List<Tag>)list);
    }

    @Override
    public /* synthetic */ DataResult mergeToList(Object object, Object object2) {
        return this.mergeToList((Tag)object, (Tag)object2);
    }

    @Override
    public /* synthetic */ Object createString(String string) {
        return this.createString(string);
    }

    @Override
    public /* synthetic */ DataResult getStringValue(Object object) {
        return this.getStringValue((Tag)object);
    }

    @Override
    public /* synthetic */ Object createBoolean(boolean bl) {
        return this.createBoolean(bl);
    }

    @Override
    public /* synthetic */ Object createDouble(double d) {
        return this.createDouble(d);
    }

    @Override
    public /* synthetic */ Object createFloat(float f) {
        return this.createFloat(f);
    }

    @Override
    public /* synthetic */ Object createLong(long l) {
        return this.createLong(l);
    }

    @Override
    public /* synthetic */ Object createInt(int n) {
        return this.createInt(n);
    }

    @Override
    public /* synthetic */ Object createShort(short s) {
        return this.createShort(s);
    }

    @Override
    public /* synthetic */ Object createByte(byte by) {
        return this.createByte(by);
    }

    @Override
    public /* synthetic */ Object createNumeric(Number number) {
        return this.createNumeric(number);
    }

    @Override
    public /* synthetic */ DataResult getNumberValue(Object object) {
        return this.getNumberValue((Tag)object);
    }

    @Override
    public /* synthetic */ Object convertTo(DynamicOps dynamicOps, Object object) {
        return this.convertTo(dynamicOps, (Tag)object);
    }

    @Override
    public /* synthetic */ Object emptyList() {
        return this.emptyList();
    }

    @Override
    public /* synthetic */ Object emptyMap() {
        return this.emptyMap();
    }

    @Override
    public /* synthetic */ Object empty() {
        return this.empty();
    }

    private static /* synthetic */ String lambda$mergeToMap$15(List list) {
        return "some keys are not strings: " + String.valueOf(list);
    }

    private static /* synthetic */ String lambda$mergeToMap$13(List list) {
        return "some keys are not strings: " + String.valueOf(list);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static /* synthetic */ void lambda$mergeToMap$12(List list, CompoundTag compoundTag, Pair pair) {
        String string;
        Tag tag = (Tag)pair.getFirst();
        if (!(tag instanceof StringTag)) {
            list.add(tag);
            return;
        }
        StringTag stringTag = (StringTag)tag;
        try {
            String string2;
            string = string2 = stringTag.value();
        }
        catch (Throwable throwable) {
            throw new MatchException(throwable.toString(), throwable);
        }
        compoundTag.put(string, (Tag)pair.getSecond());
    }

    class NbtRecordBuilder
    extends RecordBuilder.AbstractStringBuilder<Tag, CompoundTag> {
        protected NbtRecordBuilder(NbtOps nbtOps) {
            super(nbtOps);
        }

        @Override
        protected CompoundTag initBuilder() {
            return new CompoundTag();
        }

        @Override
        protected CompoundTag append(String string, Tag tag, CompoundTag compoundTag) {
            compoundTag.put(string, tag);
            return compoundTag;
        }

        @Override
        protected DataResult<Tag> build(CompoundTag compoundTag, Tag tag) {
            if (tag == null || tag == EndTag.INSTANCE) {
                return DataResult.success(compoundTag);
            }
            if (tag instanceof CompoundTag) {
                CompoundTag compoundTag2 = (CompoundTag)tag;
                CompoundTag compoundTag3 = compoundTag2.shallowCopy();
                for (Map.Entry<String, Tag> entry : compoundTag.entrySet()) {
                    compoundTag3.put(entry.getKey(), entry.getValue());
                }
                return DataResult.success(compoundTag3);
            }
            return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(tag), tag);
        }

        @Override
        protected /* synthetic */ Object append(String string, Object object, Object object2) {
            return this.append(string, (Tag)object, (CompoundTag)object2);
        }

        @Override
        protected /* synthetic */ DataResult build(Object object, Object object2) {
            return this.build((CompoundTag)object, (Tag)object2);
        }

        @Override
        protected /* synthetic */ Object initBuilder() {
            return this.initBuilder();
        }
    }

    static class GenericListCollector
    implements ListCollector {
        private final ListTag result = new ListTag();

        GenericListCollector() {
        }

        GenericListCollector(ListTag listTag) {
            this.result.addAll(listTag);
        }

        public GenericListCollector(IntArrayList intArrayList) {
            intArrayList.forEach(n -> this.result.add(IntTag.valueOf(n)));
        }

        public GenericListCollector(ByteArrayList byteArrayList) {
            byteArrayList.forEach(by -> this.result.add(ByteTag.valueOf(by)));
        }

        public GenericListCollector(LongArrayList longArrayList) {
            longArrayList.forEach(l -> this.result.add(LongTag.valueOf(l)));
        }

        @Override
        public ListCollector accept(Tag tag) {
            this.result.add(tag);
            return this;
        }

        @Override
        public Tag result() {
            return this.result;
        }
    }

    static class ByteListCollector
    implements ListCollector {
        private final ByteArrayList values = new ByteArrayList();

        public ByteListCollector(byte[] byArray) {
            this.values.addElements(0, byArray);
        }

        @Override
        public ListCollector accept(Tag tag) {
            if (tag instanceof ByteTag) {
                ByteTag byteTag = (ByteTag)tag;
                this.values.add(byteTag.byteValue());
                return this;
            }
            return new GenericListCollector(this.values).accept(tag);
        }

        @Override
        public Tag result() {
            return new ByteArrayTag(this.values.toByteArray());
        }
    }

    static class IntListCollector
    implements ListCollector {
        private final IntArrayList values = new IntArrayList();

        public IntListCollector(int[] nArray) {
            this.values.addElements(0, nArray);
        }

        @Override
        public ListCollector accept(Tag tag) {
            if (tag instanceof IntTag) {
                IntTag intTag = (IntTag)tag;
                this.values.add(intTag.intValue());
                return this;
            }
            return new GenericListCollector(this.values).accept(tag);
        }

        @Override
        public Tag result() {
            return new IntArrayTag(this.values.toIntArray());
        }
    }

    static class LongListCollector
    implements ListCollector {
        private final LongArrayList values = new LongArrayList();

        public LongListCollector(long[] lArray) {
            this.values.addElements(0, lArray);
        }

        @Override
        public ListCollector accept(Tag tag) {
            if (tag instanceof LongTag) {
                LongTag longTag = (LongTag)tag;
                this.values.add(longTag.longValue());
                return this;
            }
            return new GenericListCollector(this.values).accept(tag);
        }

        @Override
        public Tag result() {
            return new LongArrayTag(this.values.toLongArray());
        }
    }

    static interface ListCollector {
        public ListCollector accept(Tag var1);

        default public ListCollector acceptAll(Iterable<Tag> iterable) {
            ListCollector listCollector = this;
            for (Tag tag : iterable) {
                listCollector = listCollector.accept(tag);
            }
            return listCollector;
        }

        default public ListCollector acceptAll(Stream<Tag> stream) {
            return this.acceptAll(stream::iterator);
        }

        public Tag result();
    }
}

