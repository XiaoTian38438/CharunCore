/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.core.particles;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SculkChargeParticleOptions(float roll) implements ParticleOptions
{
    public static final MapCodec<SculkChargeParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.FLOAT.fieldOf("roll")).forGetter(sculkChargeParticleOptions -> Float.valueOf(sculkChargeParticleOptions.roll))).apply((Applicative<SculkChargeParticleOptions, ?>)instance, SculkChargeParticleOptions::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SculkChargeParticleOptions> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, sculkChargeParticleOptions -> Float.valueOf(sculkChargeParticleOptions.roll), SculkChargeParticleOptions::new);

    public ParticleType<SculkChargeParticleOptions> getType() {
        return ParticleTypes.SCULK_CHARGE;
    }
}

