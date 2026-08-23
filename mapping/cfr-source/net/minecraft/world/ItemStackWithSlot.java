/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

public record ItemStackWithSlot(int slot, ItemStack stack) {
    public static final Codec<ItemStackWithSlot> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)ExtraCodecs.UNSIGNED_BYTE.fieldOf("Slot")).orElse(0).forGetter(ItemStackWithSlot::slot), ItemStack.MAP_CODEC.forGetter(ItemStackWithSlot::stack)).apply((Applicative<ItemStackWithSlot, ?>)instance, ItemStackWithSlot::new));

    public boolean isValidInContainer(int n) {
        return this.slot >= 0 && this.slot < n;
    }
}

