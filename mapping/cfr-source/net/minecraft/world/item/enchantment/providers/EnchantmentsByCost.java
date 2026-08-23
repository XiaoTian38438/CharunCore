/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.item.enchantment.providers;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;

public record EnchantmentsByCost(HolderSet<Enchantment> enchantments, IntProvider cost) implements EnchantmentProvider
{
    public static final MapCodec<EnchantmentsByCost> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).fieldOf("enchantments")).forGetter(EnchantmentsByCost::enchantments), ((MapCodec)IntProvider.CODEC.fieldOf("cost")).forGetter(EnchantmentsByCost::cost)).apply((Applicative<EnchantmentsByCost, ?>)instance, EnchantmentsByCost::new));

    @Override
    public void enchant(ItemStack itemStack, ItemEnchantments.Mutable mutable, RandomSource randomSource, DifficultyInstance difficultyInstance) {
        List<EnchantmentInstance> list = EnchantmentHelper.selectEnchantment(randomSource, itemStack, this.cost.sample(randomSource), this.enchantments.stream());
        for (EnchantmentInstance enchantmentInstance : list) {
            mutable.upgrade(enchantmentInstance.enchantment(), enchantmentInstance.level());
        }
    }

    public MapCodec<EnchantmentsByCost> codec() {
        return CODEC;
    }
}

