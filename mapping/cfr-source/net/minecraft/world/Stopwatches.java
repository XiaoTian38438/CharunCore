/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.UnaryOperator;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.Stopwatch;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jspecify.annotations.Nullable;

public class Stopwatches
extends SavedData {
    private static final Codec<Stopwatches> CODEC = ((MapCodec)Codec.unboundedMap(Identifier.CODEC, Codec.LONG).fieldOf("stopwatches")).codec().xmap(Stopwatches::unpack, Stopwatches::pack);
    public static final SavedDataType<Stopwatches> TYPE = new SavedDataType<Stopwatches>("stopwatches", Stopwatches::new, CODEC, DataFixTypes.SAVED_DATA_STOPWATCHES);
    private final Map<Identifier, Stopwatch> stopwatches = new Object2ObjectOpenHashMap();

    private Stopwatches() {
    }

    private static Stopwatches unpack(Map<Identifier, Long> map) {
        Stopwatches stopwatches = new Stopwatches();
        long l = Stopwatches.currentTime();
        map.forEach((identifier, l2) -> stopwatches.stopwatches.put((Identifier)identifier, new Stopwatch(l, (long)l2)));
        return stopwatches;
    }

    private Map<Identifier, Long> pack() {
        long l = Stopwatches.currentTime();
        TreeMap<Identifier, Long> treeMap = new TreeMap<Identifier, Long>();
        this.stopwatches.forEach((identifier, stopwatch) -> treeMap.put((Identifier)identifier, stopwatch.elapsedMilliseconds(l)));
        return treeMap;
    }

    public @Nullable Stopwatch get(Identifier identifier) {
        return this.stopwatches.get(identifier);
    }

    public boolean add(Identifier identifier, Stopwatch stopwatch) {
        if (this.stopwatches.putIfAbsent(identifier, stopwatch) == null) {
            this.setDirty();
            return true;
        }
        return false;
    }

    public boolean update(Identifier identifier2, UnaryOperator<Stopwatch> unaryOperator) {
        if (this.stopwatches.computeIfPresent(identifier2, (identifier, stopwatch) -> (Stopwatch)unaryOperator.apply((Stopwatch)stopwatch)) != null) {
            this.setDirty();
            return true;
        }
        return false;
    }

    public boolean remove(Identifier identifier) {
        boolean bl;
        boolean bl2 = bl = this.stopwatches.remove(identifier) != null;
        if (bl) {
            this.setDirty();
        }
        return bl;
    }

    @Override
    public boolean isDirty() {
        return super.isDirty() || !this.stopwatches.isEmpty();
    }

    public List<Identifier> ids() {
        return List.copyOf(this.stopwatches.keySet());
    }

    public static long currentTime() {
        return Util.getMillis();
    }
}

