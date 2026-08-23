/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.world.entity.ai.util;

import java.util.function.ToDoubleFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class LandRandomPos {
    public static @Nullable Vec3 getPos(PathfinderMob pathfinderMob, int n, int n2) {
        return LandRandomPos.getPos(pathfinderMob, n, n2, pathfinderMob::getWalkTargetValue);
    }

    public static @Nullable Vec3 getPos(PathfinderMob pathfinderMob, int n, int n2, ToDoubleFunction<BlockPos> toDoubleFunction) {
        boolean bl = GoalUtils.mobRestricted(pathfinderMob, n);
        return RandomPos.generateRandomPos(() -> {
            BlockPos blockPos = RandomPos.generateRandomDirection(pathfinderMob.getRandom(), n, n2);
            BlockPos blockPos2 = LandRandomPos.generateRandomPosTowardDirection(pathfinderMob, n, bl, blockPos);
            if (blockPos2 == null) {
                return null;
            }
            return LandRandomPos.movePosUpOutOfSolid(pathfinderMob, blockPos2);
        }, toDoubleFunction);
    }

    public static @Nullable Vec3 getPosTowards(PathfinderMob pathfinderMob, int n, int n2, Vec3 vec3) {
        Vec3 vec32 = vec3.subtract(pathfinderMob.getX(), pathfinderMob.getY(), pathfinderMob.getZ());
        boolean bl = GoalUtils.mobRestricted(pathfinderMob, n);
        return LandRandomPos.getPosInDirection(pathfinderMob, 0.0, n, n2, vec32, bl);
    }

    public static @Nullable Vec3 getPosAway(PathfinderMob pathfinderMob, int n, int n2, Vec3 vec3) {
        return LandRandomPos.getPosAway(pathfinderMob, 0.0, n, n2, vec3);
    }

    public static @Nullable Vec3 getPosAway(PathfinderMob pathfinderMob, double d, double d2, int n, Vec3 vec3) {
        Vec3 vec32 = pathfinderMob.position().subtract(vec3);
        if (vec32.length() == 0.0) {
            vec32 = new Vec3(pathfinderMob.getRandom().nextDouble() - 0.5, 0.0, pathfinderMob.getRandom().nextDouble() - 0.5);
        }
        boolean bl = GoalUtils.mobRestricted(pathfinderMob, d2);
        return LandRandomPos.getPosInDirection(pathfinderMob, d, d2, n, vec32, bl);
    }

    private static @Nullable Vec3 getPosInDirection(PathfinderMob pathfinderMob, double d, double d2, int n, Vec3 vec3, boolean bl) {
        return RandomPos.generateRandomPos(pathfinderMob, () -> {
            BlockPos blockPos = RandomPos.generateRandomDirectionWithinRadians(pathfinderMob.getRandom(), d, d2, n, 0, vec3.x, vec3.z, 1.5707963705062866);
            if (blockPos == null) {
                return null;
            }
            BlockPos blockPos2 = LandRandomPos.generateRandomPosTowardDirection(pathfinderMob, d2, bl, blockPos);
            if (blockPos2 == null) {
                return null;
            }
            return LandRandomPos.movePosUpOutOfSolid(pathfinderMob, blockPos2);
        });
    }

    public static @Nullable BlockPos movePosUpOutOfSolid(PathfinderMob pathfinderMob, BlockPos blockPos2) {
        if (GoalUtils.isWater(pathfinderMob, blockPos2 = RandomPos.moveUpOutOfSolid(blockPos2, pathfinderMob.level().getMaxY(), blockPos -> GoalUtils.isSolid(pathfinderMob, blockPos))) || GoalUtils.hasMalus(pathfinderMob, blockPos2)) {
            return null;
        }
        return blockPos2;
    }

    public static @Nullable BlockPos generateRandomPosTowardDirection(PathfinderMob pathfinderMob, double d, boolean bl, BlockPos blockPos) {
        BlockPos blockPos2 = RandomPos.generateRandomPosTowardDirection(pathfinderMob, d, pathfinderMob.getRandom(), blockPos);
        if (GoalUtils.isOutsideLimits(blockPos2, pathfinderMob) || GoalUtils.isRestricted(bl, pathfinderMob, blockPos2) || GoalUtils.isNotStable(pathfinderMob.getNavigation(), blockPos2)) {
            return null;
        }
        return blockPos2;
    }
}

