/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.block;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class WeightedPressurePlateBlock
extends BasePressurePlateBlock {
    public static final MapCodec<WeightedPressurePlateBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.intRange(1, 1024).fieldOf("max_weight")).forGetter(weightedPressurePlateBlock -> weightedPressurePlateBlock.maxWeight), ((MapCodec)BlockSetType.CODEC.fieldOf("block_set_type")).forGetter(weightedPressurePlateBlock -> weightedPressurePlateBlock.type), WeightedPressurePlateBlock.propertiesCodec()).apply((Applicative<WeightedPressurePlateBlock, ?>)instance, WeightedPressurePlateBlock::new));
    public static final IntegerProperty POWER = BlockStateProperties.POWER;
    private final int maxWeight;

    public MapCodec<WeightedPressurePlateBlock> codec() {
        return CODEC;
    }

    protected WeightedPressurePlateBlock(int n, BlockSetType blockSetType, BlockBehaviour.Properties properties) {
        super(properties, blockSetType);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(POWER, 0));
        this.maxWeight = n;
    }

    @Override
    protected int getSignalStrength(Level level, BlockPos blockPos) {
        int n = Math.min(WeightedPressurePlateBlock.getEntityCount(level, TOUCH_AABB.move(blockPos), Entity.class), this.maxWeight);
        if (n > 0) {
            float f = (float)Math.min(this.maxWeight, n) / (float)this.maxWeight;
            return Mth.ceil(f * 15.0f);
        }
        return 0;
    }

    @Override
    protected int getSignalForState(BlockState blockState) {
        return blockState.getValue(POWER);
    }

    @Override
    protected BlockState setSignalForState(BlockState blockState, int n) {
        return (BlockState)blockState.setValue(POWER, n);
    }

    @Override
    protected int getPressedTime() {
        return 10;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER);
    }
}

