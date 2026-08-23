/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.minecraft.world.item.enchantment;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record Enchantable(int value) {
    public static final Codec<Enchantable> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)ExtraCodecs.POSITIVE_INT.fieldOf("value")).forGetter(Enchantable::value)).apply((Applicative<Enchantable, ?>)instance, Enchantable::new));
    public static final StreamCodec<ByteBuf, Enchantable> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, Enchantable::value, Enchantable::new);

    public Enchantable {
        if (n <= 0) {
            throw new IllegalArgumentException("Enchantment value must be positive, but was " + n);
        }
    }
}

