/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.world.entity.ai.util;

import com.google.common.annotations.VisibleForTesting;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class RandomPos {
    private static final int RANDOM_POS_ATTEMPTS = 10;

    public static BlockPos generateRandomDirection(RandomSource randomSource, int n, int n2) {
        int n3 = randomSource.nextInt(2 * n + 1) - n;
        int n4 = randomSource.nextInt(2 * n2 + 1) - n2;
        int n5 = randomSource.nextInt(2 * n + 1) - n;
        return new BlockPos(n3, n4, n5);
    }

    public static @Nullable BlockPos generateRandomDirectionWithinRadians(RandomSource randomSource, double d, double d2, int n, int n2, double d3, double d4, double d5) {
        double d6 = Mth.atan2(d4, d3) - 1.5707963705062866;
        double d7 = d6 + (double)(2.0f * randomSource.nextFloat() - 1.0f) * d5;
        double d8 = Mth.lerp(Math.sqrt(randomSource.nextDouble()), d, d2) * (double)Mth.SQRT_OF_TWO;
        double d9 = -d8 * Math.sin(d7);
        double d10 = d8 * Math.cos(d7);
        if (Math.abs(d9) > d2 || Math.abs(d10) > d2) {
            return null;
        }
        int n3 = randomSource.nextInt(2 * n + 1) - n + n2;
        return BlockPos.containing(d9, n3, d10);
    }

    @VisibleForTesting
    public static BlockPos moveUpOutOfSolid(BlockPos blockPos, int n, Predicate<BlockPos> predicate) {
        if (predicate.test(blockPos)) {
            BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable().move(Direction.UP);
            while (mutableBlockPos.getY() <= n && predicate.test(mutableBlockPos)) {
                mutableBlockPos.move(Direction.UP);
            }
            return mutableBlockPos.immutable();
        }
        return blockPos;
    }

    @VisibleForTesting
    public static BlockPos moveUpToAboveSolid(BlockPos blockPos, int n, int n2, Predicate<BlockPos> predicate) {
        if (n < 0) {
            throw new IllegalArgumentException("aboveSolidAmount was " + n + ", expected >= 0");
        }
        if (predicate.test(blockPos)) {
            BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable().move(Direction.UP);
            while (mutableBlockPos.getY() <= n2 && predicate.test(mutableBlockPos)) {
                mutableBlockPos.move(Direction.UP);
            }
            int n3 = mutableBlockPos.getY();
            while (mutableBlockPos.getY() <= n2 && mutableBlockPos.getY() - n3 < n) {
                mutableBlockPos.move(Direction.UP);
                if (!predicate.test(mutableBlockPos)) continue;
                mutableBlockPos.move(Direction.DOWN);
                break;
            }
            return mutableBlockPos.immutable();
        }
        return blockPos;
    }

    public static @Nullable Vec3 generateRandomPos(PathfinderMob pathfinderMob, Supplier<@Nullable BlockPos> supplier) {
        return RandomPos.generateRandomPos(supplier, pathfinderMob::getWalkTargetValue);
    }

    public static @Nullable Vec3 generateRandomPos(Supplier<@Nullable BlockPos> supplier, ToDoubleFunction<BlockPos> toDoubleFunction) {
        double d = Double.NEGATIVE_INFINITY;
        BlockPos blockPos = null;
        for (int i = 0; i < 10; ++i) {
            double d2;
            BlockPos blockPos2 = supplier.get();
            if (blockPos2 == null || !((d2 = toDoubleFunction.applyAsDouble(blockPos2)) > d)) continue;
            d = d2;
            blockPos = blockPos2;
        }
        return blockPos != null ? Vec3.atBottomCenterOf(blockPos) : null;
    }

    public static BlockPos generateRandomPosTowardDirection(PathfinderMob pathfinderMob, double d, RandomSource randomSource, BlockPos blockPos) {
        double d2 = blockPos.getX();
        double d3 = blockPos.getZ();
        if (pathfinderMob.hasHome() && d > 1.0) {
            BlockPos blockPos2 = pathfinderMob.getHomePosition();
            d2 = pathfinderMob.getX() > (double)blockPos2.getX() ? (d2 -= randomSource.nextDouble() * d / 2.0) : (d2 += randomSource.nextDouble() * d / 2.0);
            d3 = pathfinderMob.getZ() > (double)blockPos2.getZ() ? (d3 -= randomSource.nextDouble() * d / 2.0) : (d3 += randomSource.nextDouble() * d / 2.0);
        }
        return BlockPos.containing(d2 + pathfinderMob.getX(), (double)blockPos.getY() + pathfinderMob.getY(), d3 + pathfinderMob.getZ());
    }
}

