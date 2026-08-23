/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.block;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class WeatheringCopperSlabBlock
extends SlabBlock
implements WeatheringCopper {
    public static final MapCodec<WeatheringCopperSlabBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state")).forGetter(ChangeOverTimeBlock::getAge), WeatheringCopperSlabBlock.propertiesCodec()).apply((Applicative<WeatheringCopperSlabBlock, ?>)instance, WeatheringCopperSlabBlock::new));
    private final WeatheringCopper.WeatherState weatherState;

    public MapCodec<WeatheringCopperSlabBlock> codec() {
        return CODEC;
    }

    public WeatheringCopperSlabBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
        super(properties);
        this.weatherState = weatherState;
    }

    @Override
    protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        this.changeOverTime(blockState, serverLevel, blockPos, randomSource);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState blockState) {
        return WeatheringCopper.getNext(blockState.getBlock()).isPresent();
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    public /* synthetic */ Enum getAge() {
        return this.getAge();
    }
}

