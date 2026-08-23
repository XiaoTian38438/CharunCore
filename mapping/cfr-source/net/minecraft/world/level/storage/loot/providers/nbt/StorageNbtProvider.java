/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.storage.loot.providers.nbt;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Set;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType;
import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;
import net.minecraft.world.level.storage.loot.providers.nbt.NbtProviders;

public record StorageNbtProvider(Identifier id) implements NbtProvider
{
    public static final MapCodec<StorageNbtProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Identifier.CODEC.fieldOf("source")).forGetter(StorageNbtProvider::id)).apply((Applicative<StorageNbtProvider, ?>)instance, StorageNbtProvider::new));

    @Override
    public LootNbtProviderType getType() {
        return NbtProviders.STORAGE;
    }

    @Override
    public Tag get(LootContext lootContext) {
        return lootContext.getLevel().getServer().getCommandStorage().get(this.id);
    }

    @Override
    public Set<ContextKey<?>> getReferencedContextParams() {
        return Set.of();
    }
}

