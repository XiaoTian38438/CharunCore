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
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class WeatheringCopperTrapDoorBlock
extends TrapDoorBlock
implements WeatheringCopper {
    public static final MapCodec<WeatheringCopperTrapDoorBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)BlockSetType.CODEC.fieldOf("block_set_type")).forGetter(TrapDoorBlock::getType), ((MapCodec)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state")).forGetter(WeatheringCopperTrapDoorBlock::getAge), WeatheringCopperTrapDoorBlock.propertiesCodec()).apply((Applicative<WeatheringCopperTrapDoorBlock, ?>)instance, WeatheringCopperTrapDoorBlock::new));
    private final WeatheringCopper.WeatherState weatherState;

    public MapCodec<WeatheringCopperTrapDoorBlock> codec() {
        return CODEC;
    }

    protected WeatheringCopperTrapDoorBlock(BlockSetType blockSetType, WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
        super(blockSetType, properties);
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

