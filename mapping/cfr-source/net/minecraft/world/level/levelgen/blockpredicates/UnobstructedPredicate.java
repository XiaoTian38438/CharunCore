/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.levelgen.blockpredicates;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.phys.shapes.Shapes;

record UnobstructedPredicate(Vec3i offset) implements BlockPredicate
{
    public static MapCodec<UnobstructedPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(UnobstructedPredicate::offset)).apply((Applicative<UnobstructedPredicate, ?>)instance, UnobstructedPredicate::new));

    @Override
    public BlockPredicateType<?> type() {
        return BlockPredicateType.UNOBSTRUCTED;
    }

    @Override
    public boolean test(WorldGenLevel worldGenLevel, BlockPos blockPos) {
        return worldGenLevel.isUnobstructed(null, Shapes.block().move(blockPos));
    }

    @Override
    public /* synthetic */ boolean test(Object object, Object object2) {
        return this.test((WorldGenLevel)object, (BlockPos)object2);
    }
}

