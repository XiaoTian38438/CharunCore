/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.network.chat.contents.data;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.contents.data.DataSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

public record BlockDataSource(String posPattern, @Nullable Coordinates compiledPos) implements DataSource
{
    public static final MapCodec<BlockDataSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.STRING.fieldOf("block")).forGetter(BlockDataSource::posPattern)).apply((Applicative<BlockDataSource, ?>)instance, BlockDataSource::new));

    public BlockDataSource(String string) {
        this(string, BlockDataSource.compilePos(string));
    }

    private static @Nullable Coordinates compilePos(String string) {
        try {
            return BlockPosArgument.blockPos().parse(new StringReader(string));
        }
        catch (CommandSyntaxException commandSyntaxException) {
            return null;
        }
    }

    @Override
    public Stream<CompoundTag> getData(CommandSourceStack commandSourceStack) {
        BlockEntity blockEntity;
        BlockPos blockPos;
        ServerLevel serverLevel;
        if (this.compiledPos != null && (serverLevel = commandSourceStack.getLevel()).isLoaded(blockPos = this.compiledPos.getBlockPos(commandSourceStack)) && (blockEntity = serverLevel.getBlockEntity(blockPos)) != null) {
            return Stream.of(blockEntity.saveWithFullMetadata(commandSourceStack.registryAccess()));
        }
        return Stream.empty();
    }

    public MapCodec<BlockDataSource> codec() {
        return MAP_CODEC;
    }

    @Override
    public String toString() {
        return "block=" + this.posPattern;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof BlockDataSource)) return false;
        BlockDataSource blockDataSource = (BlockDataSource)object;
        if (!this.posPattern.equals(blockDataSource.posPattern)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return this.posPattern.hashCode();
    }
}

