/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.block;

import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public interface SelectableSlotContainer {
    public int getRows();

    public int getColumns();

    default public OptionalInt getHitSlot(BlockHitResult blockHitResult, Direction direction) {
        return SelectableSlotContainer.getRelativeHitCoordinatesForBlockFace(blockHitResult, direction).map(vec2 -> {
            int n = SelectableSlotContainer.getSection(1.0f - vec2.y, this.getRows());
            int n2 = SelectableSlotContainer.getSection(vec2.x, this.getColumns());
            return OptionalInt.of(n2 + n * this.getColumns());
        }).orElseGet(OptionalInt::empty);
    }

    private static Optional<Vec2> getRelativeHitCoordinatesForBlockFace(BlockHitResult blockHitResult, Direction direction) {
        Direction direction2 = blockHitResult.getDirection();
        if (direction != direction2) {
            return Optional.empty();
        }
        BlockPos blockPos = blockHitResult.getBlockPos().relative(direction2);
        Vec3 vec3 = blockHitResult.getLocation().subtract(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        double d = vec3.x();
        double d2 = vec3.y();
        double d3 = vec3.z();
        return switch (direction2) {
            default -> throw new MatchException(null, null);
            case Direction.NORTH -> Optional.of(new Vec2((float)(1.0 - d), (float)d2));
            case Direction.SOUTH -> Optional.of(new Vec2((float)d, (float)d2));
            case Direction.WEST -> Optional.of(new Vec2((float)d3, (float)d2));
            case Direction.EAST -> Optional.of(new Vec2((float)(1.0 - d3), (float)d2));
            case Direction.DOWN, Direction.UP -> Optional.empty();
        };
    }

    private static int getSection(float f, int n) {
        float f2 = f * 16.0f;
        float f3 = 16.0f / (float)n;
        return Mth.clamp(Mth.floor(f2 / f3), 0, n - 1);
    }
}

