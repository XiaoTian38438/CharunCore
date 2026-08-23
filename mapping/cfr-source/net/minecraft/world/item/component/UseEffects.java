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
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record UseEffects(boolean canSprint, boolean interactVibrations, float speedMultiplier) {
    public static final UseEffects DEFAULT = new UseEffects(false, true, 0.2f);
    public static final Codec<UseEffects> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.BOOL.optionalFieldOf("can_sprint", UseEffects.DEFAULT.canSprint).forGetter(UseEffects::canSprint), Codec.BOOL.optionalFieldOf("interact_vibrations", UseEffects.DEFAULT.interactVibrations).forGetter(UseEffects::interactVibrations), Codec.floatRange(0.0f, 1.0f).optionalFieldOf("speed_multiplier", Float.valueOf(UseEffects.DEFAULT.speedMultiplier)).forGetter(UseEffects::speedMultiplier)).apply((Applicative<UseEffects, ?>)instance, UseEffects::new));
    public static final StreamCodec<ByteBuf, UseEffects> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, UseEffects::canSprint, ByteBufCodecs.BOOL, UseEffects::interactVibrations, ByteBufCodecs.FLOAT, UseEffects::speedMultiplier, UseEffects::new);
}

