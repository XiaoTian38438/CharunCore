/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.entity.animal.wolf;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.sounds.SoundEvent;

public record WolfSoundVariant(Holder<SoundEvent> ambientSound, Holder<SoundEvent> deathSound, Holder<SoundEvent> growlSound, Holder<SoundEvent> hurtSound, Holder<SoundEvent> pantSound, Holder<SoundEvent> whineSound) {
    public static final Codec<WolfSoundVariant> DIRECT_CODEC = WolfSoundVariant.getWolfSoundVariantCodec();
    public static final Codec<WolfSoundVariant> NETWORK_CODEC = WolfSoundVariant.getWolfSoundVariantCodec();
    public static final Codec<Holder<WolfSoundVariant>> CODEC = RegistryFixedCodec.create(Registries.WOLF_SOUND_VARIANT);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<WolfSoundVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.WOLF_SOUND_VARIANT);

    private static Codec<WolfSoundVariant> getWolfSoundVariantCodec() {
        return RecordCodecBuilder.create(instance -> instance.group(((MapCodec)SoundEvent.CODEC.fieldOf("ambient_sound")).forGetter(WolfSoundVariant::ambientSound), ((MapCodec)SoundEvent.CODEC.fieldOf("death_sound")).forGetter(WolfSoundVariant::deathSound), ((MapCodec)SoundEvent.CODEC.fieldOf("growl_sound")).forGetter(WolfSoundVariant::growlSound), ((MapCodec)SoundEvent.CODEC.fieldOf("hurt_sound")).forGetter(WolfSoundVariant::hurtSound), ((MapCodec)SoundEvent.CODEC.fieldOf("pant_sound")).forGetter(WolfSoundVariant::pantSound), ((MapCodec)SoundEvent.CODEC.fieldOf("whine_sound")).forGetter(WolfSoundVariant::whineSound)).apply((Applicative<WolfSoundVariant, ?>)instance, WolfSoundVariant::new));
    }
}

