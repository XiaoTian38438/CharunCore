/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.advancements.criterion;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ItemDurabilityTrigger
extends SimpleCriterionTrigger<TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer serverPlayer, ItemStack itemStack, int n) {
        this.trigger(serverPlayer, triggerInstance -> triggerInstance.matches(itemStack, n));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, MinMaxBounds.Ints durability, MinMaxBounds.Ints delta) implements SimpleCriterionTrigger.SimpleInstance
    {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item), MinMaxBounds.Ints.CODEC.optionalFieldOf("durability", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::durability), MinMaxBounds.Ints.CODEC.optionalFieldOf("delta", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::delta)).apply((Applicative<TriggerInstance, ?>)instance, TriggerInstance::new));

        public static Criterion<TriggerInstance> changedDurability(Optional<ItemPredicate> optional, MinMaxBounds.Ints ints) {
            return TriggerInstance.changedDurability(Optional.empty(), optional, ints);
        }

        public static Criterion<TriggerInstance> changedDurability(Optional<ContextAwarePredicate> optional, Optional<ItemPredicate> optional2, MinMaxBounds.Ints ints) {
            return CriteriaTriggers.ITEM_DURABILITY_CHANGED.createCriterion(new TriggerInstance(optional, optional2, ints, MinMaxBounds.Ints.ANY));
        }

        public boolean matches(ItemStack itemStack, int n) {
            if (this.item.isPresent() && !this.item.get().test(itemStack)) {
                return false;
            }
            if (!this.durability.matches(itemStack.getMaxDamage() - n)) {
                return false;
            }
            return this.delta.matches(itemStack.getDamageValue() - n);
        }
    }
}

