/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;

public class RandomSequence {
    public static final Codec<RandomSequence> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)XoroshiroRandomSource.CODEC.fieldOf("source")).forGetter(randomSequence -> randomSequence.source)).apply((Applicative<RandomSequence, ?>)instance, RandomSequence::new));
    private final XoroshiroRandomSource source;

    public RandomSequence(XoroshiroRandomSource xoroshiroRandomSource) {
        this.source = xoroshiroRandomSource;
    }

    public RandomSequence(long l, Identifier identifier) {
        this(RandomSequence.createSequence(l, Optional.of(identifier)));
    }

    public RandomSequence(long l, Optional<Identifier> optional) {
        this(RandomSequence.createSequence(l, optional));
    }

    private static XoroshiroRandomSource createSequence(long l, Optional<Identifier> optional) {
        RandomSupport.Seed128bit seed128bit = RandomSupport.upgradeSeedTo128bitUnmixed(l);
        if (optional.isPresent()) {
            seed128bit = seed128bit.xor(RandomSequence.seedForKey(optional.get()));
        }
        return new XoroshiroRandomSource(seed128bit.mixed());
    }

    public static RandomSupport.Seed128bit seedForKey(Identifier identifier) {
        return RandomSupport.seedFromHashOf(identifier.toString());
    }

    public RandomSource random() {
        return this.source;
    }
}

