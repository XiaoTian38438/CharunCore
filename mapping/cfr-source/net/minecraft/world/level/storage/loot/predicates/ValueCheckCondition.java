/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 */
package net.minecraft.world.level.storage.loot.predicates;

import com.google.common.collect.Sets;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Set;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public record ValueCheckCondition(NumberProvider provider, IntRange range) implements LootItemCondition
{
    public static final MapCodec<ValueCheckCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)NumberProviders.CODEC.fieldOf("value")).forGetter(ValueCheckCondition::provider), ((MapCodec)IntRange.CODEC.fieldOf("range")).forGetter(ValueCheckCondition::range)).apply((Applicative<ValueCheckCondition, ?>)instance, ValueCheckCondition::new));

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.VALUE_CHECK;
    }

    @Override
    public Set<ContextKey<?>> getReferencedContextParams() {
        return Sets.union(this.provider.getReferencedContextParams(), this.range.getReferencedContextParams());
    }

    @Override
    public boolean test(LootContext lootContext) {
        return this.range.test(lootContext, this.provider.getInt(lootContext));
    }

    public static LootItemCondition.Builder hasValue(NumberProvider numberProvider, IntRange intRange) {
        return () -> new ValueCheckCondition(numberProvider, intRange);
    }

    @Override
    public /* synthetic */ boolean test(Object object) {
        return this.test((LootContext)object);
    }
}

