/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.minecraft.core.particles;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;

public class SpellParticleOption
implements ParticleOptions {
    private final ParticleType<SpellParticleOption> type;
    private final int color;
    private final float power;

    public static MapCodec<SpellParticleOption> codec(ParticleType<SpellParticleOption> particleType) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color", -1).forGetter(spellParticleOption -> spellParticleOption.color), Codec.FLOAT.optionalFieldOf("power", Float.valueOf(1.0f)).forGetter(spellParticleOption -> Float.valueOf(spellParticleOption.power))).apply((Applicative<SpellParticleOption, ?>)instance, (n, f) -> new SpellParticleOption(particleType, (int)n, f.floatValue())));
    }

    public static StreamCodec<? super ByteBuf, SpellParticleOption> streamCodec(ParticleType<SpellParticleOption> particleType) {
        return StreamCodec.composite(ByteBufCodecs.INT, spellParticleOption -> spellParticleOption.color, ByteBufCodecs.FLOAT, spellParticleOption -> Float.valueOf(spellParticleOption.power), (n, f) -> new SpellParticleOption(particleType, (int)n, f.floatValue()));
    }

    private SpellParticleOption(ParticleType<SpellParticleOption> particleType, int n, float f) {
        this.type = particleType;
        this.color = n;
        this.power = f;
    }

    public ParticleType<SpellParticleOption> getType() {
        return this.type;
    }

    public float getRed() {
        return (float)ARGB.red(this.color) / 255.0f;
    }

    public float getGreen() {
        return (float)ARGB.green(this.color) / 255.0f;
    }

    public float getBlue() {
        return (float)ARGB.blue(this.color) / 255.0f;
    }

    public float getPower() {
        return this.power;
    }

    public static SpellParticleOption create(ParticleType<SpellParticleOption> particleType, int n, float f) {
        return new SpellParticleOption(particleType, n, f);
    }

    public static SpellParticleOption create(ParticleType<SpellParticleOption> particleType, float f, float f2, float f3, float f4) {
        return SpellParticleOption.create(particleType, ARGB.colorFromFloat(1.0f, f, f2, f3), f4);
    }
}

