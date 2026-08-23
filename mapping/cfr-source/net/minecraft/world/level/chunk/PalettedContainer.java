/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
 *  it.unimi.dsi.fastutil.ints.IntArraySet
 *  it.unimi.dsi.fastutil.ints.IntSet
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.world.level.chunk;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.LongStream;
import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.BitStorage;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.util.ThreadingDetector;
import net.minecraft.util.ZeroBitStorage;
import net.minecraft.world.level.chunk.Configuration;
import net.minecraft.world.level.chunk.HashMapPalette;
import net.minecraft.world.level.chunk.Palette;
import net.minecraft.world.level.chunk.PaletteResize;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.chunk.Strategy;
import org.jspecify.annotations.Nullable;

public class PalettedContainer<T>
implements PaletteResize<T>,
PalettedContainerRO<T> {
    private static final int MIN_PALETTE_BITS = 0;
    private volatile Data<T> data;
    private final Strategy<T> strategy;
    private final ThreadingDetector threadingDetector = new ThreadingDetector("PalettedContainer");

    public void acquire() {
        this.threadingDetector.checkAndLock();
    }

    public void release() {
        this.threadingDetector.checkAndUnlock();
    }

    public static <T> Codec<PalettedContainer<T>> codecRW(Codec<T> codec, Strategy<T> strategy, T t) {
        PalettedContainerRO.Unpacker unpacker = PalettedContainer::unpack;
        return PalettedContainer.codec(codec, strategy, t, unpacker);
    }

    public static <T> Codec<PalettedContainerRO<T>> codecRO(Codec<T> codec, Strategy<T> strategy2, T t) {
        PalettedContainerRO.Unpacker unpacker = (strategy, packedData) -> PalettedContainer.unpack(strategy, packedData).map(palettedContainer -> palettedContainer);
        return PalettedContainer.codec(codec, strategy2, t, unpacker);
    }

    private static <T, C extends PalettedContainerRO<T>> Codec<C> codec(Codec<T> codec, Strategy<T> strategy, T t, PalettedContainerRO.Unpacker<T, C> unpacker) {
        return RecordCodecBuilder.create(instance -> instance.group(((MapCodec)codec.mapResult(ExtraCodecs.orElsePartial(t)).listOf().fieldOf("palette")).forGetter(PalettedContainerRO.PackedData::paletteEntries), Codec.LONG_STREAM.lenientOptionalFieldOf("data").forGetter(PalettedContainerRO.PackedData::storage)).apply((Applicative<PalettedContainerRO.PackedData, ?>)instance, PalettedContainerRO.PackedData::new)).comapFlatMap(packedData -> unpacker.read(strategy, (PalettedContainerRO.PackedData)packedData), palettedContainerRO -> palettedContainerRO.pack(strategy));
    }

    private PalettedContainer(Strategy<T> strategy, Configuration configuration, BitStorage bitStorage, Palette<T> palette) {
        this.strategy = strategy;
        this.data = new Data<T>(configuration, bitStorage, palette);
    }

    private PalettedContainer(PalettedContainer<T> palettedContainer) {
        this.strategy = palettedContainer.strategy;
        this.data = palettedContainer.data.copy();
    }

    public PalettedContainer(T t, Strategy<T> strategy) {
        this.strategy = strategy;
        this.data = this.createOrReuseData(null, 0);
        this.data.palette.idFor(t, this);
    }

    private Data<T> createOrReuseData(@Nullable Data<T> data, int n) {
        Configuration configuration = this.strategy.getConfigurationForBitCount(n);
        if (data != null && configuration.equals(data.configuration())) {
            return data;
        }
        BitStorage bitStorage = configuration.bitsInMemory() == 0 ? new ZeroBitStorage(this.strategy.entryCount()) : new SimpleBitStorage(configuration.bitsInMemory(), this.strategy.entryCount());
        Palette<T> palette = configuration.createPalette(this.strategy, List.of());
        return new Data<T>(configuration, bitStorage, palette);
    }

    @Override
    public int onResize(int n, T t) {
        Data<T> data = this.data;
        Data data2 = this.createOrReuseData(data, n);
        data2.copyFrom(data.palette, data.storage);
        this.data = data2;
        return data2.palette.idFor(t, PaletteResize.noResizeExpected());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public T getAndSet(int n, int n2, int n3, T t) {
        this.acquire();
        try {
            T t2 = this.getAndSet(this.strategy.getIndex(n, n2, n3), t);
            return t2;
        }
        finally {
            this.release();
        }
    }

    public T getAndSetUnchecked(int n, int n2, int n3, T t) {
        return this.getAndSet(this.strategy.getIndex(n, n2, n3), t);
    }

    private T getAndSet(int n, T t) {
        int n2 = this.data.palette.idFor(t, this);
        int n3 = this.data.storage.getAndSet(n, n2);
        return this.data.palette.valueFor(n3);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void set(int n, int n2, int n3, T t) {
        this.acquire();
        try {
            this.set(this.strategy.getIndex(n, n2, n3), t);
        }
        finally {
            this.release();
        }
    }

    private void set(int n, T t) {
        int n2 = this.data.palette.idFor(t, this);
        this.data.storage.set(n, n2);
    }

    @Override
    public T get(int n, int n2, int n3) {
        return this.get(this.strategy.getIndex(n, n2, n3));
    }

    protected T get(int n) {
        Data<T> data = this.data;
        return data.palette.valueFor(data.storage.get(n));
    }

    @Override
    public void getAll(Consumer<T> consumer) {
        Palette palette = this.data.palette();
        IntArraySet intArraySet = new IntArraySet();
        this.data.storage.getAll(arg_0 -> ((IntSet)intArraySet).add(arg_0));
        intArraySet.forEach(n -> consumer.accept(palette.valueFor(n)));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void read(FriendlyByteBuf friendlyByteBuf) {
        this.acquire();
        try {
            byte by = friendlyByteBuf.readByte();
            Data<T> data = this.createOrReuseData(this.data, by);
            data.palette.read(friendlyByteBuf, this.strategy.globalMap());
            friendlyByteBuf.readFixedSizeLongArray(data.storage.getRaw());
            this.data = data;
        }
        finally {
            this.release();
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        this.acquire();
        try {
            this.data.write(friendlyByteBuf, this.strategy.globalMap());
        }
        finally {
            this.release();
        }
    }

    @VisibleForTesting
    public static <T> DataResult<PalettedContainer<T>> unpack(Strategy<T> strategy, PalettedContainerRO.PackedData<T> packedData) {
        BitStorage bitStorage;
        Palette<T> palette;
        List<T> list = packedData.paletteEntries();
        int n = strategy.entryCount();
        Configuration configuration = strategy.getConfigurationForPaletteSize(list.size());
        int n2 = configuration.bitsInStorage();
        if (packedData.bitsPerEntry() != -1 && n2 != packedData.bitsPerEntry()) {
            return DataResult.error(() -> "Invalid bit count, calculated " + n2 + ", but container declared " + packedData.bitsPerEntry());
        }
        if (configuration.bitsInMemory() == 0) {
            palette = configuration.createPalette(strategy, list);
            bitStorage = new ZeroBitStorage(n);
        } else {
            Optional<LongStream> optional = packedData.storage();
            if (optional.isEmpty()) {
                return DataResult.error(() -> "Missing values for non-zero storage");
            }
            long[] lArray = optional.get().toArray();
            try {
                if (configuration.alwaysRepack() || configuration.bitsInMemory() != n2) {
                    HashMapPalette<T> hashMapPalette = new HashMapPalette<T>(n2, list);
                    SimpleBitStorage simpleBitStorage = new SimpleBitStorage(n2, n, lArray);
                    Palette<T> palette2 = configuration.createPalette(strategy, list);
                    int[] nArray = PalettedContainer.reencodeContents(simpleBitStorage, hashMapPalette, palette2);
                    palette = palette2;
                    bitStorage = new SimpleBitStorage(configuration.bitsInMemory(), n, nArray);
                } else {
                    palette = configuration.createPalette(strategy, list);
                    bitStorage = new SimpleBitStorage(configuration.bitsInMemory(), n, lArray);
                }
            }
            catch (SimpleBitStorage.InitializationException initializationException) {
                return DataResult.error(() -> "Failed to read PalettedContainer: " + initializationException.getMessage());
            }
        }
        return DataResult.success(new PalettedContainer<T>(strategy, configuration, bitStorage, palette));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public PalettedContainerRO.PackedData<T> pack(Strategy<T> strategy) {
        this.acquire();
        try {
            Optional<LongStream> optional;
            Object object;
            BitStorage bitStorage = this.data.storage;
            Palette palette = this.data.palette;
            HashMapPalette hashMapPalette = new HashMapPalette(bitStorage.getBits());
            int n = strategy.entryCount();
            int[] nArray = PalettedContainer.reencodeContents(bitStorage, palette, hashMapPalette);
            Configuration configuration = strategy.getConfigurationForPaletteSize(hashMapPalette.getSize());
            int n2 = configuration.bitsInStorage();
            if (n2 != 0) {
                object = new SimpleBitStorage(n2, n, nArray);
                optional = Optional.of(Arrays.stream(((SimpleBitStorage)object).getRaw()));
            } else {
                optional = Optional.empty();
            }
            object = new PalettedContainerRO.PackedData(hashMapPalette.getEntries(), optional, n2);
            return object;
        }
        finally {
            this.release();
        }
    }

    private static <T> int[] reencodeContents(BitStorage bitStorage, Palette<T> palette, Palette<T> palette2) {
        int[] nArray = new int[bitStorage.getSize()];
        bitStorage.unpack(nArray);
        PaletteResize paletteResize = PaletteResize.noResizeExpected();
        int n = -1;
        int n2 = -1;
        for (int i = 0; i < nArray.length; ++i) {
            int n3 = nArray[i];
            if (n3 != n) {
                n = n3;
                n2 = palette2.idFor(palette.valueFor(n3), paletteResize);
            }
            nArray[i] = n2;
        }
        return nArray;
    }

    @Override
    public int getSerializedSize() {
        return this.data.getSerializedSize(this.strategy.globalMap());
    }

    @Override
    public int bitsPerEntry() {
        return this.data.storage().getBits();
    }

    @Override
    public boolean maybeHas(Predicate<T> predicate) {
        return this.data.palette.maybeHas(predicate);
    }

    @Override
    public PalettedContainer<T> copy() {
        return new PalettedContainer<T>(this);
    }

    @Override
    public PalettedContainer<T> recreate() {
        return new PalettedContainer(this.data.palette.valueFor(0), this.strategy);
    }

    @Override
    public void count(CountConsumer<T> countConsumer) {
        if (this.data.palette.getSize() == 1) {
            countConsumer.accept(this.data.palette.valueFor(0), this.data.storage.getSize());
            return;
        }
        Int2IntOpenHashMap int2IntOpenHashMap = new Int2IntOpenHashMap();
        this.data.storage.getAll((int n) -> int2IntOpenHashMap.addTo(n, 1));
        int2IntOpenHashMap.int2IntEntrySet().forEach(entry -> countConsumer.accept(this.data.palette.valueFor(entry.getIntKey()), entry.getIntValue()));
    }

    static final class Data<T>
    extends Record {
        private final Configuration configuration;
        final BitStorage storage;
        final Palette<T> palette;

        Data(Configuration configuration, BitStorage bitStorage, Palette<T> palette) {
            this.configuration = configuration;
            this.storage = bitStorage;
            this.palette = palette;
        }

        public void copyFrom(Palette<T> palette, BitStorage bitStorage) {
            PaletteResize paletteResize = PaletteResize.noResizeExpected();
            for (int i = 0; i < bitStorage.getSize(); ++i) {
                T t = palette.valueFor(bitStorage.get(i));
                this.storage.set(i, this.palette.idFor(t, paletteResize));
            }
        }

        public int getSerializedSize(IdMap<T> idMap) {
            return 1 + this.palette.getSerializedSize(idMap) + this.storage.getRaw().length * 8;
        }

        public void write(FriendlyByteBuf friendlyByteBuf, IdMap<T> idMap) {
            friendlyByteBuf.writeByte(this.storage.getBits());
            this.palette.write(friendlyByteBuf, idMap);
            friendlyByteBuf.writeFixedSizeLongArray(this.storage.getRaw());
        }

        public Data<T> copy() {
            return new Data<T>(this.configuration, this.storage.copy(), this.palette.copy());
        }

        @Override
        public final String toString() {
            return ObjectMethods.bootstrap("toString", new MethodHandle[]{Data.class, "configuration;storage;palette", "configuration", "storage", "palette"}, this);
        }

        @Override
        public final int hashCode() {
            return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{Data.class, "configuration;storage;palette", "configuration", "storage", "palette"}, this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{Data.class, "configuration;storage;palette", "configuration", "storage", "palette"}, this, object);
        }

        public Configuration configuration() {
            return this.configuration;
        }

        public BitStorage storage() {
            return this.storage;
        }

        public Palette<T> palette() {
            return this.palette;
        }
    }

    @FunctionalInterface
    public static interface CountConsumer<T> {
        public void accept(T var1, int var2);
    }
}

