/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.levelgen.blockpredicates;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;

class NotPredicate
implements BlockPredicate {
    public static final MapCodec<NotPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)BlockPredicate.CODEC.fieldOf("predicate")).forGetter(notPredicate -> notPredicate.predicate)).apply((Applicative<NotPredicate, ?>)instance, NotPredicate::new));
    private final BlockPredicate predicate;

    public NotPredicate(BlockPredicate blockPredicate) {
        this.predicate = blockPredicate;
    }

    @Override
    public boolean test(WorldGenLevel worldGenLevel, BlockPos blockPos) {
        return !this.predicate.test(worldGenLevel, blockPos);
    }

    @Override
    public BlockPredicateType<?> type() {
        return BlockPredicateType.NOT;
    }

    @Override
    public /* synthetic */ boolean test(Object object, Object object2) {
        return this.test((WorldGenLevel)object, (BlockPos)object2);
    }
}

