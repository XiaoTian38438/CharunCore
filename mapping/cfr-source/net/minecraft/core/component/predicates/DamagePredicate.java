/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.core.component.predicates;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.predicates.DataComponentPredicate;

public record DamagePredicate(MinMaxBounds.Ints durability, MinMaxBounds.Ints damage) implements DataComponentPredicate
{
    public static final Codec<DamagePredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(MinMaxBounds.Ints.CODEC.optionalFieldOf("durability", MinMaxBounds.Ints.ANY).forGetter(DamagePredicate::durability), MinMaxBounds.Ints.CODEC.optionalFieldOf("damage", MinMaxBounds.Ints.ANY).forGetter(DamagePredicate::damage)).apply((Applicative<DamagePredicate, ?>)instance, DamagePredicate::new));

    @Override
    public boolean matches(DataComponentGetter dataComponentGetter) {
        Integer n = dataComponentGetter.get(DataComponents.DAMAGE);
        if (n == null) {
            return false;
        }
        int n2 = dataComponentGetter.getOrDefault(DataComponents.MAX_DAMAGE, 0);
        if (!this.durability.matches(n2 - n)) {
            return false;
        }
        return this.damage.matches(n);
    }

    public static DamagePredicate durability(MinMaxBounds.Ints ints) {
        return new DamagePredicate(ints, MinMaxBounds.Ints.ANY);
    }
}

