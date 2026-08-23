/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;

public class AxisAlignedLinearPosTest
extends PosRuleTest {
    public static final MapCodec<AxisAlignedLinearPosTest> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.FLOAT.fieldOf("min_chance")).orElse(Float.valueOf(0.0f)).forGetter(axisAlignedLinearPosTest -> Float.valueOf(axisAlignedLinearPosTest.minChance)), ((MapCodec)Codec.FLOAT.fieldOf("max_chance")).orElse(Float.valueOf(0.0f)).forGetter(axisAlignedLinearPosTest -> Float.valueOf(axisAlignedLinearPosTest.maxChance)), ((MapCodec)Codec.INT.fieldOf("min_dist")).orElse(0).forGetter(axisAlignedLinearPosTest -> axisAlignedLinearPosTest.minDist), ((MapCodec)Codec.INT.fieldOf("max_dist")).orElse(0).forGetter(axisAlignedLinearPosTest -> axisAlignedLinearPosTest.maxDist), ((MapCodec)Direction.Axis.CODEC.fieldOf("axis")).orElse(Direction.Axis.Y).forGetter(axisAlignedLinearPosTest -> axisAlignedLinearPosTest.axis)).apply((Applicative<AxisAlignedLinearPosTest, ?>)instance, AxisAlignedLinearPosTest::new));
    private final float minChance;
    private final float maxChance;
    private final int minDist;
    private final int maxDist;
    private final Direction.Axis axis;

    public AxisAlignedLinearPosTest(float f, float f2, int n, int n2, Direction.Axis axis) {
        if (n >= n2) {
            throw new IllegalArgumentException("Invalid range: [" + n + "," + n2 + "]");
        }
        this.minChance = f;
        this.maxChance = f2;
        this.minDist = n;
        this.maxDist = n2;
        this.axis = axis;
    }

    @Override
    public boolean test(BlockPos blockPos, BlockPos blockPos2, BlockPos blockPos3, RandomSource randomSource) {
        Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, this.axis);
        float f = Math.abs((blockPos2.getX() - blockPos3.getX()) * direction.getStepX());
        float f2 = Math.abs((blockPos2.getY() - blockPos3.getY()) * direction.getStepY());
        float f3 = Math.abs((blockPos2.getZ() - blockPos3.getZ()) * direction.getStepZ());
        int n = (int)(f + f2 + f3);
        float f4 = randomSource.nextFloat();
        return f4 <= Mth.clampedLerp(Mth.inverseLerp(n, this.minDist, this.maxDist), this.minChance, this.maxChance);
    }

    @Override
    protected PosRuleTestType<?> getType() {
        return PosRuleTestType.AXIS_ALIGNED_LINEAR_POS_TEST;
    }
}

