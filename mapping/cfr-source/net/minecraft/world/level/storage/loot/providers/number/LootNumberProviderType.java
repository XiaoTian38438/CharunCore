/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.storage.loot.providers.number;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public record LootNumberProviderType(MapCodec<? extends NumberProvider> codec) {
}

