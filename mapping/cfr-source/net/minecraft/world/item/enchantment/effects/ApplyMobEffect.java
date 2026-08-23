/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.item.enchantment.effects;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record ApplyMobEffect(HolderSet<MobEffect> toApply, LevelBasedValue minDuration, LevelBasedValue maxDuration, LevelBasedValue minAmplifier, LevelBasedValue maxAmplifier) implements EnchantmentEntityEffect
{
    public static final MapCodec<ApplyMobEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)RegistryCodecs.homogeneousList(Registries.MOB_EFFECT).fieldOf("to_apply")).forGetter(ApplyMobEffect::toApply), ((MapCodec)LevelBasedValue.CODEC.fieldOf("min_duration")).forGetter(ApplyMobEffect::minDuration), ((MapCodec)LevelBasedValue.CODEC.fieldOf("max_duration")).forGetter(ApplyMobEffect::maxDuration), ((MapCodec)LevelBasedValue.CODEC.fieldOf("min_amplifier")).forGetter(ApplyMobEffect::minAmplifier), ((MapCodec)LevelBasedValue.CODEC.fieldOf("max_amplifier")).forGetter(ApplyMobEffect::maxAmplifier)).apply((Applicative<ApplyMobEffect, ?>)instance, ApplyMobEffect::new));

    @Override
    public void apply(ServerLevel serverLevel, int n, EnchantedItemInUse enchantedItemInUse, Entity entity, Vec3 vec3) {
        LivingEntity livingEntity;
        RandomSource randomSource;
        Optional<Holder<MobEffect>> optional;
        if (entity instanceof LivingEntity && (optional = this.toApply.getRandomElement(randomSource = (livingEntity = (LivingEntity)entity).getRandom())).isPresent()) {
            int n2 = Math.round(Mth.randomBetween(randomSource, this.minDuration.calculate(n), this.maxDuration.calculate(n)) * 20.0f);
            int n3 = Math.max(0, Math.round(Mth.randomBetween(randomSource, this.minAmplifier.calculate(n), this.maxAmplifier.calculate(n))));
            livingEntity.addEffect(new MobEffectInstance(optional.get(), n2, n3));
        }
    }

    public MapCodec<ApplyMobEffect> codec() {
        return CODEC;
    }
}

