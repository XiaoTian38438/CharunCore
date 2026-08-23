/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.advancements.criterion;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.CriterionValidator;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jspecify.annotations.Nullable;

public class KilledByArrowTrigger
extends SimpleCriterionTrigger<TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer serverPlayer, Collection<Entity> collection, @Nullable ItemStack itemStack) {
        ArrayList arrayList = Lists.newArrayList();
        HashSet hashSet = Sets.newHashSet();
        for (Entity entity : collection) {
            hashSet.add(entity.getType());
            arrayList.add(EntityPredicate.createContext(serverPlayer, entity));
        }
        this.trigger(serverPlayer, triggerInstance -> triggerInstance.matches(arrayList, hashSet.size(), itemStack));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, List<ContextAwarePredicate> victims, MinMaxBounds.Ints uniqueEntityTypes, Optional<ItemPredicate> firedFromWeapon) implements SimpleCriterionTrigger.SimpleInstance
    {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), EntityPredicate.ADVANCEMENT_CODEC.listOf().optionalFieldOf("victims", List.of()).forGetter(TriggerInstance::victims), MinMaxBounds.Ints.CODEC.optionalFieldOf("unique_entity_types", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::uniqueEntityTypes), ItemPredicate.CODEC.optionalFieldOf("fired_from_weapon").forGetter(TriggerInstance::firedFromWeapon)).apply((Applicative<TriggerInstance, ?>)instance, TriggerInstance::new));

        public static Criterion<TriggerInstance> crossbowKilled(HolderGetter<Item> holderGetter, EntityPredicate.Builder ... builderArray) {
            return CriteriaTriggers.KILLED_BY_ARROW.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(builderArray), MinMaxBounds.Ints.ANY, Optional.of(ItemPredicate.Builder.item().of(holderGetter, Items.CROSSBOW).build())));
        }

        public static Criterion<TriggerInstance> crossbowKilled(HolderGetter<Item> holderGetter, MinMaxBounds.Ints ints) {
            return CriteriaTriggers.KILLED_BY_ARROW.createCriterion(new TriggerInstance(Optional.empty(), List.of(), ints, Optional.of(ItemPredicate.Builder.item().of(holderGetter, Items.CROSSBOW).build())));
        }

        public boolean matches(Collection<LootContext> collection, int n, @Nullable ItemStack itemStack) {
            if (this.firedFromWeapon.isPresent() && (itemStack == null || !this.firedFromWeapon.get().test(itemStack))) {
                return false;
            }
            if (!this.victims.isEmpty()) {
                ArrayList arrayList = Lists.newArrayList(collection);
                for (ContextAwarePredicate contextAwarePredicate : this.victims) {
                    boolean bl = false;
                    Iterator iterator = arrayList.iterator();
                    while (iterator.hasNext()) {
                        LootContext lootContext = (LootContext)iterator.next();
                        if (!contextAwarePredicate.matches(lootContext)) continue;
                        iterator.remove();
                        bl = true;
                        break;
                    }
                    if (bl) continue;
                    return false;
                }
            }
            return this.uniqueEntityTypes.matches(n);
        }

        @Override
        public void validate(CriterionValidator criterionValidator) {
            SimpleCriterionTrigger.SimpleInstance.super.validate(criterionValidator);
            criterionValidator.validateEntities(this.victims, "victims");
        }
    }
}

