/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.block;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class TorchBlock
extends BaseTorchBlock {
    protected static final MapCodec<SimpleParticleType> PARTICLE_OPTIONS_FIELD = BuiltInRegistries.PARTICLE_TYPE.byNameCodec().comapFlatMap(particleType -> {
        DataResult<Object> dataResult;
        if (particleType instanceof SimpleParticleType) {
            SimpleParticleType simpleParticleType = (SimpleParticleType)particleType;
            dataResult = DataResult.success(simpleParticleType);
        } else {
            dataResult = DataResult.error(() -> "Not a SimpleParticleType: " + String.valueOf(particleType));
        }
        return dataResult;
    }, simpleParticleType -> simpleParticleType).fieldOf("particle_options");
    public static final MapCodec<TorchBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(PARTICLE_OPTIONS_FIELD.forGetter(torchBlock -> torchBlock.flameParticle), TorchBlock.propertiesCodec()).apply((Applicative<TorchBlock, ?>)instance, TorchBlock::new));
    protected final SimpleParticleType flameParticle;

    public MapCodec<? extends TorchBlock> codec() {
        return CODEC;
    }

    protected TorchBlock(SimpleParticleType simpleParticleType, BlockBehaviour.Properties properties) {
        super(properties);
        this.flameParticle = simpleParticleType;
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        double d = (double)blockPos.getX() + 0.5;
        double d2 = (double)blockPos.getY() + 0.7;
        double d3 = (double)blockPos.getZ() + 0.5;
        level.addParticle(ParticleTypes.SMOKE, d, d2, d3, 0.0, 0.0, 0.0);
        level.addParticle(this.flameParticle, d, d2, d3, 0.0, 0.0, 0.0);
    }
}

