/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.storage.loot.providers.nbt;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;

public record LootNbtProviderType(MapCodec<? extends NbtProvider> codec) {
}

