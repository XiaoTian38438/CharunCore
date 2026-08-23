/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
import net.minecraft.world.level.chunk.MissingPaletteEntryException;
import net.minecraft.world.level.chunk.Palette;
import net.minecraft.world.level.chunk.PaletteResize;

public class HashMapPalette<T>
implements Palette<T> {
    private final CrudeIncrementalIntIdentityHashBiMap<T> values;
    private final int bits;

    public HashMapPalette(int n, List<T> list) {
        this(n);
        list.forEach(this.values::add);
    }

    public HashMapPalette(int n) {
        this(n, CrudeIncrementalIntIdentityHashBiMap.create(1 << n));
    }

    private HashMapPalette(int n, CrudeIncrementalIntIdentityHashBiMap<T> crudeIncrementalIntIdentityHashBiMap) {
        this.bits = n;
        this.values = crudeIncrementalIntIdentityHashBiMap;
    }

    public static <A> Palette<A> create(int n, List<A> list) {
        return new HashMapPalette<A>(n, list);
    }

    @Override
    public int idFor(T t, PaletteResize<T> paletteResize) {
        int n = this.values.getId(t);
        if (n == -1 && (n = this.values.add(t)) >= 1 << this.bits) {
            n = paletteResize.onResize(this.bits + 1, t);
        }
        return n;
    }

    @Override
    public boolean maybeHas(Predicate<T> predicate) {
        for (int i = 0; i < this.getSize(); ++i) {
            if (!predicate.test(this.values.byId(i))) continue;
            return true;
        }
        return false;
    }

    @Override
    public T valueFor(int n) {
        T t = this.values.byId(n);
        if (t == null) {
            throw new MissingPaletteEntryException(n);
        }
        return t;
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf, IdMap<T> idMap) {
        this.values.clear();
        int n = friendlyByteBuf.readVarInt();
        for (int i = 0; i < n; ++i) {
            this.values.add(idMap.byIdOrThrow(friendlyByteBuf.readVarInt()));
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf, IdMap<T> idMap) {
        int n = this.getSize();
        friendlyByteBuf.writeVarInt(n);
        for (int i = 0; i < n; ++i) {
            friendlyByteBuf.writeVarInt(idMap.getId(this.values.byId(i)));
        }
    }

    @Override
    public int getSerializedSize(IdMap<T> idMap) {
        int n = VarInt.getByteSize(this.getSize());
        for (int i = 0; i < this.getSize(); ++i) {
            n += VarInt.getByteSize(idMap.getId(this.values.byId(i)));
        }
        return n;
    }

    public List<T> getEntries() {
        ArrayList arrayList = new ArrayList();
        this.values.iterator().forEachRemaining(arrayList::add);
        return arrayList;
    }

    @Override
    public int getSize() {
        return this.values.size();
    }

    @Override
    public Palette<T> copy() {
        return new HashMapPalette<T>(this.bits, this.values.copy());
    }
}

