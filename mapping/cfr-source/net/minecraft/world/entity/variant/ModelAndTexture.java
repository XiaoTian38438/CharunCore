/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.entity.variant;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.ClientAsset;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record ModelAndTexture<T>(T model, ClientAsset.ResourceTexture asset) {
    public ModelAndTexture(T t, Identifier identifier) {
        this(t, new ClientAsset.ResourceTexture(identifier));
    }

    public static <T> MapCodec<ModelAndTexture<T>> codec(Codec<T> codec, T t) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(codec.optionalFieldOf("model", t).forGetter(ModelAndTexture::model), ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC.forGetter(ModelAndTexture::asset)).apply((Applicative<ModelAndTexture, ?>)instance, ModelAndTexture::new));
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, ModelAndTexture<T>> streamCodec(StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return StreamCodec.composite(streamCodec, ModelAndTexture::model, ClientAsset.ResourceTexture.STREAM_CODEC, ModelAndTexture::asset, ModelAndTexture::new);
    }
}

