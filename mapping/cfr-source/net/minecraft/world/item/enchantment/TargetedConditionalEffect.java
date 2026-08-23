/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.item.enchantment;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record TargetedConditionalEffect<T>(EnchantmentTarget enchanted, EnchantmentTarget affected, T effect, Optional<LootItemCondition> requirements) {
    public static <S> Codec<TargetedConditionalEffect<S>> codec(Codec<S> codec, ContextKeySet contextKeySet) {
        return RecordCodecBuilder.create(instance -> instance.group(((MapCodec)EnchantmentTarget.CODEC.fieldOf("enchanted")).forGetter(TargetedConditionalEffect::enchanted), ((MapCodec)EnchantmentTarget.CODEC.fieldOf("affected")).forGetter(TargetedConditionalEffect::affected), ((MapCodec)codec.fieldOf("effect")).forGetter(TargetedConditionalEffect::effect), ConditionalEffect.conditionCodec(contextKeySet).optionalFieldOf("requirements").forGetter(TargetedConditionalEffect::requirements)).apply((Applicative<TargetedConditionalEffect, ?>)instance, TargetedConditionalEffect::new));
    }

    public static <S> Codec<TargetedConditionalEffect<S>> equipmentDropsCodec(Codec<S> codec, ContextKeySet contextKeySet) {
        return RecordCodecBuilder.create(instance -> instance.group(((MapCodec)EnchantmentTarget.CODEC.validate(enchantmentTarget -> enchantmentTarget != EnchantmentTarget.DAMAGING_ENTITY ? DataResult.success(enchantmentTarget) : DataResult.error(() -> "enchanted must be attacker or victim")).fieldOf("enchanted")).forGetter(TargetedConditionalEffect::enchanted), ((MapCodec)codec.fieldOf("effect")).forGetter(TargetedConditionalEffect::effect), ConditionalEffect.conditionCodec(contextKeySet).optionalFieldOf("requirements").forGetter(TargetedConditionalEffect::requirements)).apply((Applicative<TargetedConditionalEffect, ?>)instance, (enchantmentTarget, object, optional) -> new TargetedConditionalEffect<Object>((EnchantmentTarget)enchantmentTarget, EnchantmentTarget.VICTIM, object, (Optional<LootItemCondition>)optional)));
    }

    public boolean matches(LootContext lootContext) {
        if (this.requirements.isEmpty()) {
            return true;
        }
        return this.requirements.get().test(lootContext);
    }
}

