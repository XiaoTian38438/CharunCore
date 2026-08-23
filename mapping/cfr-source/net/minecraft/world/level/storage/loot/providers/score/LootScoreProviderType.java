/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.storage.loot.providers.score;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.providers.score.ScoreboardNameProvider;

public record LootScoreProviderType(MapCodec<? extends ScoreboardNameProvider> codec) {
}

