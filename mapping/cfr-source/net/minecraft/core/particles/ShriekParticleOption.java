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

public class ShriekParticleOption
implements ParticleOptions {
    public static final MapCodec<ShriekParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.INT.fieldOf("delay")).forGetter(shriekParticleOption -> shriekParticleOption.delay)).apply((Applicative<ShriekParticleOption, ?>)instance, ShriekParticleOption::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ShriekParticleOption> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, shriekParticleOption -> shriekParticleOption.delay, ShriekParticleOption::new);
    private final int delay;

    public ShriekParticleOption(int n) {
        this.delay = n;
    }

    public ParticleType<ShriekParticleOption> getType() {
        return ParticleTypes.SHRIEK;
    }

    public int getDelay() {
        return this.delay;
    }
}

