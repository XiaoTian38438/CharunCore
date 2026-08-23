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
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class WeatheringCopperStairBlock
extends StairBlock
implements WeatheringCopper {
    public static final MapCodec<WeatheringCopperStairBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state")).forGetter(ChangeOverTimeBlock::getAge), ((MapCodec)BlockState.CODEC.fieldOf("base_state")).forGetter(weatheringCopperStairBlock -> weatheringCopperStairBlock.baseState), WeatheringCopperStairBlock.propertiesCodec()).apply((Applicative<WeatheringCopperStairBlock, ?>)instance, WeatheringCopperStairBlock::new));
    private final WeatheringCopper.WeatherState weatherState;

    public MapCodec<WeatheringCopperStairBlock> codec() {
        return CODEC;
    }

    public WeatheringCopperStairBlock(WeatheringCopper.WeatherState weatherState, BlockState blockState, BlockBehaviour.Properties properties) {
        super(blockState, properties);
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

