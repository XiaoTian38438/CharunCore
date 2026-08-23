/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.storage.loot.entries;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

public record LootPoolEntryType(MapCodec<? extends LootPoolEntryContainer> codec) {
}

