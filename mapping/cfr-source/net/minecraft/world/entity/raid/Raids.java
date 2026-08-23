/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap$Entry
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 *  it.unimi.dsi.fastutil.objects.ObjectIterator
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.world.entity.raid;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import java.util.List;
import java.util.OptionalInt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Raids
extends SavedData {
    private static final String RAID_FILE_ID = "raids";
    public static final Codec<Raids> CODEC = RecordCodecBuilder.create(instance -> instance.group(RaidWithId.CODEC.listOf().optionalFieldOf(RAID_FILE_ID, List.of()).forGetter(raids -> raids.raidMap.int2ObjectEntrySet().stream().map(RaidWithId::from).toList()), ((MapCodec)Codec.INT.fieldOf("next_id")).forGetter(raids -> raids.nextId), ((MapCodec)Codec.INT.fieldOf("tick")).forGetter(raids -> raids.tick)).apply((Applicative<Raids, ?>)instance, Raids::new));
    public static final SavedDataType<Raids> TYPE = new SavedDataType<Raids>("raids", Raids::new, CODEC, DataFixTypes.SAVED_DATA_RAIDS);
    public static final SavedDataType<Raids> TYPE_END = new SavedDataType<Raids>("raids_end", Raids::new, CODEC, DataFixTypes.SAVED_DATA_RAIDS);
    private final Int2ObjectMap<Raid> raidMap = new Int2ObjectOpenHashMap();
    private int nextId = 1;
    private int tick;

    public static SavedDataType<Raids> getType(Holder<DimensionType> holder) {
        if (holder.is(BuiltinDimensionTypes.END)) {
            return TYPE_END;
        }
        return TYPE;
    }

    public Raids() {
        this.setDirty();
    }

    private Raids(List<RaidWithId> list, int n, int n2) {
        for (RaidWithId raidWithId : list) {
            this.raidMap.put(raidWithId.id, (Object)raidWithId.raid);
        }
        this.nextId = n;
        this.tick = n2;
    }

    public @Nullable Raid get(int n) {
        return (Raid)this.raidMap.get(n);
    }

    public OptionalInt getId(Raid raid) {
        for (Int2ObjectMap.Entry entry : this.raidMap.int2ObjectEntrySet()) {
            if (entry.getValue() != raid) continue;
            return OptionalInt.of(entry.getIntKey());
        }
        return OptionalInt.empty();
    }

    public void tick(ServerLevel serverLevel) {
        ++this.tick;
        ObjectIterator objectIterator = this.raidMap.values().iterator();
        while (objectIterator.hasNext()) {
            Raid raid = (Raid)objectIterator.next();
            if (!serverLevel.getGameRules().get(GameRules.RAIDS).booleanValue()) {
                raid.stop();
            }
            if (raid.isStopped()) {
                objectIterator.remove();
                this.setDirty();
                continue;
            }
            raid.tick(serverLevel);
        }
        if (this.tick % 200 == 0) {
            this.setDirty();
        }
    }

    public static boolean canJoinRaid(Raider raider) {
        return raider.isAlive() && raider.canJoinRaid() && raider.getNoActionTime() <= 2400;
    }

    public @Nullable Raid createOrExtendRaid(ServerPlayer serverPlayer, BlockPos blockPos) {
        Object object;
        if (serverPlayer.isSpectator()) {
            return null;
        }
        ServerLevel serverLevel = serverPlayer.level();
        if (!serverLevel.getGameRules().get(GameRules.RAIDS).booleanValue()) {
            return null;
        }
        if (!serverLevel.environmentAttributes().getValue(EnvironmentAttributes.CAN_START_RAID, blockPos).booleanValue()) {
            return null;
        }
        List<PoiRecord> list = serverLevel.getPoiManager().getInRange(holder -> holder.is(PoiTypeTags.VILLAGE), blockPos, 64, PoiManager.Occupancy.IS_OCCUPIED).toList();
        int n = 0;
        Vec3 vec3 = Vec3.ZERO;
        for (PoiRecord object22 : list) {
            BlockPos blockPos2 = object22.getPos();
            vec3 = vec3.add(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());
            ++n;
        }
        if (n > 0) {
            vec3 = vec3.scale(1.0 / (double)n);
            object = BlockPos.containing(vec3);
        } else {
            object = blockPos;
        }
        Raid raid = this.getOrCreateRaid(serverLevel, (BlockPos)object);
        if (!raid.isStarted() && !this.raidMap.containsValue((Object)raid)) {
            this.raidMap.put(this.getUniqueId(), (Object)raid);
        }
        if (!raid.isStarted() || raid.getRaidOmenLevel() < raid.getMaxRaidOmenLevel()) {
            raid.absorbRaidOmen(serverPlayer);
        }
        this.setDirty();
        return raid;
    }

    private Raid getOrCreateRaid(ServerLevel serverLevel, BlockPos blockPos) {
        Raid raid = serverLevel.getRaidAt(blockPos);
        return raid != null ? raid : new Raid(blockPos, serverLevel.getDifficulty());
    }

    public static Raids load(CompoundTag compoundTag) {
        return CODEC.parse(NbtOps.INSTANCE, compoundTag).resultOrPartial().orElseGet(Raids::new);
    }

    private int getUniqueId() {
        return ++this.nextId;
    }

    public @Nullable Raid getNearbyRaid(BlockPos blockPos, int n) {
        Raid raid = null;
        double d = n;
        for (Raid raid2 : this.raidMap.values()) {
            double d2 = raid2.getCenter().distSqr(blockPos);
            if (!raid2.isActive() || !(d2 < d)) continue;
            raid = raid2;
            d = d2;
        }
        return raid;
    }

    @VisibleForDebug
    public List<BlockPos> getRaidCentersInChunk(ChunkPos chunkPos) {
        return this.raidMap.values().stream().map(Raid::getCenter).filter(chunkPos::contains).toList();
    }

    static final class RaidWithId
    extends Record {
        final int id;
        final Raid raid;
        public static final Codec<RaidWithId> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Codec.INT.fieldOf("id")).forGetter(RaidWithId::id), Raid.MAP_CODEC.forGetter(RaidWithId::raid)).apply((Applicative<RaidWithId, ?>)instance, RaidWithId::new));

        private RaidWithId(int n, Raid raid) {
            this.id = n;
            this.raid = raid;
        }

        public static RaidWithId from(Int2ObjectMap.Entry<Raid> entry) {
            return new RaidWithId(entry.getIntKey(), (Raid)entry.getValue());
        }

        @Override
        public final String toString() {
            return ObjectMethods.bootstrap("toString", new MethodHandle[]{RaidWithId.class, "id;raid", "id", "raid"}, this);
        }

        @Override
        public final int hashCode() {
            return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{RaidWithId.class, "id;raid", "id", "raid"}, this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{RaidWithId.class, "id;raid", "id", "raid"}, this, object);
        }

        public int id() {
            return this.id;
        }

        public Raid raid() {
            return this.raid;
        }
    }
}

