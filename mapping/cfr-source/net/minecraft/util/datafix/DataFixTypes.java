/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.Set;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.datafix.fixes.References;

public enum DataFixTypes {
    LEVEL(References.LEVEL),
    LEVEL_SUMMARY(References.LIGHTWEIGHT_LEVEL),
    PLAYER(References.PLAYER),
    CHUNK(References.CHUNK),
    HOTBAR(References.HOTBAR),
    OPTIONS(References.OPTIONS),
    STRUCTURE(References.STRUCTURE),
    STATS(References.STATS),
    SAVED_DATA_COMMAND_STORAGE(References.SAVED_DATA_COMMAND_STORAGE),
    SAVED_DATA_FORCED_CHUNKS(References.SAVED_DATA_TICKETS),
    SAVED_DATA_MAP_DATA(References.SAVED_DATA_MAP_DATA),
    SAVED_DATA_MAP_INDEX(References.SAVED_DATA_MAP_INDEX),
    SAVED_DATA_RAIDS(References.SAVED_DATA_RAIDS),
    SAVED_DATA_RANDOM_SEQUENCES(References.SAVED_DATA_RANDOM_SEQUENCES),
    SAVED_DATA_SCOREBOARD(References.SAVED_DATA_SCOREBOARD),
    SAVED_DATA_STOPWATCHES(References.SAVED_DATA_STOPWATCHES),
    SAVED_DATA_STRUCTURE_FEATURE_INDICES(References.SAVED_DATA_STRUCTURE_FEATURE_INDICES),
    SAVED_DATA_WORLD_BORDER(References.SAVED_DATA_WORLD_BORDER),
    ADVANCEMENTS(References.ADVANCEMENTS),
    POI_CHUNK(References.POI_CHUNK),
    WORLD_GEN_SETTINGS(References.WORLD_GEN_SETTINGS),
    ENTITY_CHUNK(References.ENTITY_CHUNK),
    DEBUG_PROFILE(References.DEBUG_PROFILE);

    public static final Set<DSL.TypeReference> TYPES_FOR_LEVEL_LIST;
    private final DSL.TypeReference type;

    private DataFixTypes(DSL.TypeReference typeReference) {
        this.type = typeReference;
    }

    static int currentVersion() {
        return SharedConstants.getCurrentVersion().dataVersion().version();
    }

    public <A> Codec<A> wrapCodec(final Codec<A> codec, final DataFixer dataFixer, final int n) {
        return new Codec<A>(){

            @Override
            public <T> DataResult<T> encode(A a, DynamicOps<T> dynamicOps, T t) {
                return codec.encode(a, dynamicOps, t).flatMap((? super R object) -> dynamicOps.mergeToMap(object, dynamicOps.createString("DataVersion"), dynamicOps.createInt(DataFixTypes.currentVersion())));
            }

            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
                int n2 = dynamicOps.get(t, "DataVersion").flatMap(dynamicOps::getNumberValue).map(Number::intValue).result().orElse(n);
                Dynamic<T> dynamic = new Dynamic<T>(dynamicOps, dynamicOps.remove(t, "DataVersion"));
                Dynamic<T> dynamic2 = DataFixTypes.this.updateToCurrentVersion(dataFixer, dynamic, n2);
                return codec.decode(dynamic2);
            }
        };
    }

    public <T> Dynamic<T> update(DataFixer dataFixer, Dynamic<T> dynamic, int n, int n2) {
        return dataFixer.update(this.type, dynamic, n, n2);
    }

    public <T> Dynamic<T> updateToCurrentVersion(DataFixer dataFixer, Dynamic<T> dynamic, int n) {
        return this.update(dataFixer, dynamic, n, DataFixTypes.currentVersion());
    }

    public CompoundTag update(DataFixer dataFixer, CompoundTag compoundTag, int n, int n2) {
        return this.update(dataFixer, new Dynamic<CompoundTag>(NbtOps.INSTANCE, compoundTag), n, n2).getValue();
    }

    public CompoundTag updateToCurrentVersion(DataFixer dataFixer, CompoundTag compoundTag, int n) {
        return this.update(dataFixer, compoundTag, n, DataFixTypes.currentVersion());
    }

    static {
        TYPES_FOR_LEVEL_LIST = Set.of(DataFixTypes.LEVEL_SUMMARY.type);
    }
}

