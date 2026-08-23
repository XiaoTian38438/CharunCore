/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.attribute;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public record AmbientMoodSettings(Holder<SoundEvent> soundEvent, int tickDelay, int blockSearchExtent, double soundPositionOffset) {
    public static final Codec<AmbientMoodSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)SoundEvent.CODEC.fieldOf("sound")).forGetter(ambientMoodSettings -> ambientMoodSettings.soundEvent), ((MapCodec)Codec.INT.fieldOf("tick_delay")).forGetter(ambientMoodSettings -> ambientMoodSettings.tickDelay), ((MapCodec)Codec.INT.fieldOf("block_search_extent")).forGetter(ambientMoodSettings -> ambientMoodSettings.blockSearchExtent), ((MapCodec)Codec.DOUBLE.fieldOf("offset")).forGetter(ambientMoodSettings -> ambientMoodSettings.soundPositionOffset)).apply((Applicative<AmbientMoodSettings, ?>)instance, AmbientMoodSettings::new));
    public static final AmbientMoodSettings LEGACY_CAVE_SETTINGS = new AmbientMoodSettings(SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0);
}

