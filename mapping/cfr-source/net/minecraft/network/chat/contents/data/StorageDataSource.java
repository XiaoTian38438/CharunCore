/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.chat.contents.data;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.contents.data.DataSource;
import net.minecraft.resources.Identifier;

public record StorageDataSource(Identifier id) implements DataSource
{
    public static final MapCodec<StorageDataSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Identifier.CODEC.fieldOf("storage")).forGetter(StorageDataSource::id)).apply((Applicative<StorageDataSource, ?>)instance, StorageDataSource::new));

    @Override
    public Stream<CompoundTag> getData(CommandSourceStack commandSourceStack) {
        CompoundTag compoundTag = commandSourceStack.getServer().getCommandStorage().get(this.id);
        return Stream.of(compoundTag);
    }

    public MapCodec<StorageDataSource> codec() {
        return MAP_CODEC;
    }

    @Override
    public String toString() {
        return "storage=" + String.valueOf(this.id);
    }
}

