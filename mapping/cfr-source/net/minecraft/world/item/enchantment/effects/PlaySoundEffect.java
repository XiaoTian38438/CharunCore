/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.item.enchantment.effects;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record PlaySoundEffect(List<Holder<SoundEvent>> soundEvents, FloatProvider volume, FloatProvider pitch) implements EnchantmentEntityEffect
{
    public static final MapCodec<PlaySoundEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)ExtraCodecs.compactListCodec(SoundEvent.CODEC, SoundEvent.CODEC.sizeLimitedListOf(255)).fieldOf("sound")).forGetter(PlaySoundEffect::soundEvents), ((MapCodec)FloatProvider.codec(1.0E-5f, 10.0f).fieldOf("volume")).forGetter(PlaySoundEffect::volume), ((MapCodec)FloatProvider.codec(1.0E-5f, 2.0f).fieldOf("pitch")).forGetter(PlaySoundEffect::pitch)).apply((Applicative<PlaySoundEffect, ?>)instance, PlaySoundEffect::new));

    @Override
    public void apply(ServerLevel serverLevel, int n, EnchantedItemInUse enchantedItemInUse, Entity entity, Vec3 vec3) {
        if (entity.isSilent()) {
            return;
        }
        RandomSource randomSource = entity.getRandom();
        int n2 = Mth.clamp(n - 1, 0, this.soundEvents.size() - 1);
        serverLevel.playSound(null, vec3.x(), vec3.y(), vec3.z(), this.soundEvents.get(n2), entity.getSoundSource(), this.volume.sample(randomSource), this.pitch.sample(randomSource));
    }

    public MapCodec<PlaySoundEffect> codec() {
        return CODEC;
    }
}

