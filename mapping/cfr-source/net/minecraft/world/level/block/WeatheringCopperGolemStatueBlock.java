/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.block;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.debug.DebugValueSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.CopperGolemStatueBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.CopperGolemStatueBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class WeatheringCopperGolemStatueBlock
extends CopperGolemStatueBlock
implements WeatheringCopper {
    public static final MapCodec<WeatheringCopperGolemStatueBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state")).forGetter(ChangeOverTimeBlock::getAge), WeatheringCopperGolemStatueBlock.propertiesCodec()).apply((Applicative<WeatheringCopperGolemStatueBlock, ?>)instance, WeatheringCopperGolemStatueBlock::new));

    public MapCodec<WeatheringCopperGolemStatueBlock> codec() {
        return CODEC;
    }

    public WeatheringCopperGolemStatueBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
        super(weatherState, properties);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState blockState) {
        return WeatheringCopper.getNext(blockState.getBlock()).isPresent();
    }

    @Override
    protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        this.changeOverTime(blockState, serverLevel, blockPos, randomSource);
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return this.getWeatheringState();
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        DebugValueSource debugValueSource = level.getBlockEntity(blockPos);
        if (debugValueSource instanceof CopperGolemStatueBlockEntity) {
            CopperGolemStatueBlockEntity copperGolemStatueBlockEntity = (CopperGolemStatueBlockEntity)debugValueSource;
            if (itemStack.is(ItemTags.AXES)) {
                if (this.getAge().equals(WeatheringCopper.WeatherState.UNAFFECTED)) {
                    debugValueSource = copperGolemStatueBlockEntity.removeStatue(blockState);
                    itemStack.hurtAndBreak(1, (LivingEntity)player, interactionHand.asEquipmentSlot());
                    if (debugValueSource != null) {
                        level.addFreshEntity((Entity)debugValueSource);
                        level.removeBlock(blockPos, false);
                        return InteractionResult.SUCCESS;
                    }
                }
            } else {
                if (itemStack.is(Items.HONEYCOMB)) {
                    return InteractionResult.PASS;
                }
                this.updatePose(level, blockState, blockPos, player);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public /* synthetic */ Enum getAge() {
        return this.getAge();
    }
}

