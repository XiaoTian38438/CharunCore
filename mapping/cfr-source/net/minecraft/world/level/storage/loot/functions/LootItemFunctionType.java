/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

public record LootItemFunctionType<T extends LootItemFunction>(MapCodec<T> codec) {
}

