/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.item.slot;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.slot.SlotCollection;
import net.minecraft.world.item.slot.SlotSource;
import net.minecraft.world.item.slot.TransformedSlotSource;

public class LimitSlotSource
extends TransformedSlotSource {
    public static final MapCodec<LimitSlotSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> LimitSlotSource.commonFields(instance).and(((MapCodec)ExtraCodecs.POSITIVE_INT.fieldOf("limit")).forGetter(limitSlotSource -> limitSlotSource.limit)).apply((Applicative<LimitSlotSource, ?>)instance, LimitSlotSource::new));
    private final int limit;

    private LimitSlotSource(SlotSource slotSource, int n) {
        super(slotSource);
        this.limit = n;
    }

    public MapCodec<LimitSlotSource> codec() {
        return MAP_CODEC;
    }

    @Override
    protected SlotCollection transform(SlotCollection slotCollection) {
        return slotCollection.limit(this.limit);
    }
}

