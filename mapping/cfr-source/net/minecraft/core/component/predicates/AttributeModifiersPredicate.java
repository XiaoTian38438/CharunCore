/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.core.component.predicates;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.advancements.criterion.CollectionPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.SingleComponentItemPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public record AttributeModifiersPredicate(Optional<CollectionPredicate<ItemAttributeModifiers.Entry, EntryPredicate>> modifiers) implements SingleComponentItemPredicate<ItemAttributeModifiers>
{
    public static final Codec<AttributeModifiersPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(CollectionPredicate.codec(EntryPredicate.CODEC).optionalFieldOf("modifiers").forGetter(AttributeModifiersPredicate::modifiers)).apply((Applicative<AttributeModifiersPredicate, ?>)instance, AttributeModifiersPredicate::new));

    @Override
    public DataComponentType<ItemAttributeModifiers> componentType() {
        return DataComponents.ATTRIBUTE_MODIFIERS;
    }

    @Override
    public boolean matches(ItemAttributeModifiers itemAttributeModifiers) {
        return !this.modifiers.isPresent() || this.modifiers.get().test(itemAttributeModifiers.modifiers());
    }

    public record EntryPredicate(Optional<HolderSet<Attribute>> attribute, Optional<Identifier> id, MinMaxBounds.Doubles amount, Optional<AttributeModifier.Operation> operation, Optional<EquipmentSlotGroup> slot) implements Predicate<ItemAttributeModifiers.Entry>
    {
        public static final Codec<EntryPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(RegistryCodecs.homogeneousList(Registries.ATTRIBUTE).optionalFieldOf("attribute").forGetter(EntryPredicate::attribute), Identifier.CODEC.optionalFieldOf("id").forGetter(EntryPredicate::id), MinMaxBounds.Doubles.CODEC.optionalFieldOf("amount", MinMaxBounds.Doubles.ANY).forGetter(EntryPredicate::amount), AttributeModifier.Operation.CODEC.optionalFieldOf("operation").forGetter(EntryPredicate::operation), EquipmentSlotGroup.CODEC.optionalFieldOf("slot").forGetter(EntryPredicate::slot)).apply((Applicative<EntryPredicate, ?>)instance, EntryPredicate::new));

        @Override
        public boolean test(ItemAttributeModifiers.Entry entry) {
            if (this.attribute.isPresent() && !this.attribute.get().contains(entry.attribute())) {
                return false;
            }
            if (this.id.isPresent() && !this.id.get().equals(entry.modifier().id())) {
                return false;
            }
            if (!this.amount.matches(entry.modifier().amount())) {
                return false;
            }
            if (this.operation.isPresent() && this.operation.get() != entry.modifier().operation()) {
                return false;
            }
            return !this.slot.isPresent() || this.slot.get() == entry.slot();
        }

        @Override
        public /* synthetic */ boolean test(Object object) {
            return this.test((ItemAttributeModifiers.Entry)object);
        }
    }
}

