/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.minecraft.world.item.component;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;

public record LodestoneTracker(Optional<GlobalPos> target, boolean tracked) {
    public static final Codec<LodestoneTracker> CODEC = RecordCodecBuilder.create(instance -> instance.group(GlobalPos.CODEC.optionalFieldOf("target").forGetter(LodestoneTracker::target), Codec.BOOL.optionalFieldOf("tracked", true).forGetter(LodestoneTracker::tracked)).apply((Applicative<LodestoneTracker, ?>)instance, LodestoneTracker::new));
    public static final StreamCodec<ByteBuf, LodestoneTracker> STREAM_CODEC = StreamCodec.composite(GlobalPos.STREAM_CODEC.apply(ByteBufCodecs::optional), LodestoneTracker::target, ByteBufCodecs.BOOL, LodestoneTracker::tracked, LodestoneTracker::new);

    public LodestoneTracker tick(ServerLevel serverLevel) {
        if (!this.tracked || this.target.isEmpty()) {
            return this;
        }
        if (this.target.get().dimension() != serverLevel.dimension()) {
            return this;
        }
        BlockPos blockPos = this.target.get().pos();
        if (!serverLevel.isInWorldBounds(blockPos) || !serverLevel.getPoiManager().existsAtPosition(PoiTypes.LODESTONE, blockPos)) {
            return new LodestoneTracker(Optional.empty(), true);
        }
        return this;
    }
}

