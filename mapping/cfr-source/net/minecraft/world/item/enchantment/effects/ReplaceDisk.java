/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.item.enchantment.effects;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.phys.Vec3;

public record ReplaceDisk(LevelBasedValue radius, LevelBasedValue height, Vec3i offset, Optional<BlockPredicate> predicate, BlockStateProvider blockState, Optional<Holder<GameEvent>> triggerGameEvent) implements EnchantmentEntityEffect
{
    public static final MapCodec<ReplaceDisk> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)LevelBasedValue.CODEC.fieldOf("radius")).forGetter(ReplaceDisk::radius), ((MapCodec)LevelBasedValue.CODEC.fieldOf("height")).forGetter(ReplaceDisk::height), Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(ReplaceDisk::offset), BlockPredicate.CODEC.optionalFieldOf("predicate").forGetter(ReplaceDisk::predicate), ((MapCodec)BlockStateProvider.CODEC.fieldOf("block_state")).forGetter(ReplaceDisk::blockState), GameEvent.CODEC.optionalFieldOf("trigger_game_event").forGetter(ReplaceDisk::triggerGameEvent)).apply((Applicative<ReplaceDisk, ?>)instance, ReplaceDisk::new));

    @Override
    public void apply(ServerLevel serverLevel, int n, EnchantedItemInUse enchantedItemInUse, Entity entity, Vec3 vec3) {
        BlockPos blockPos = BlockPos.containing(vec3).offset(this.offset);
        RandomSource randomSource = entity.getRandom();
        int n2 = (int)this.radius.calculate(n);
        int n3 = (int)this.height.calculate(n);
        for (BlockPos blockPos2 : BlockPos.betweenClosed(blockPos.offset(-n2, 0, -n2), blockPos.offset(n2, Math.min(n3 - 1, 0), n2))) {
            if (!(blockPos2.distToCenterSqr(vec3.x(), (double)blockPos2.getY() + 0.5, vec3.z()) < (double)Mth.square(n2)) || !this.predicate.map(blockPredicate -> blockPredicate.test(serverLevel, blockPos2)).orElse(true).booleanValue() || !serverLevel.setBlockAndUpdate(blockPos2, this.blockState.getState(randomSource, blockPos2))) continue;
            this.triggerGameEvent.ifPresent(holder -> serverLevel.gameEvent(entity, (Holder<GameEvent>)holder, blockPos2));
        }
    }

    public MapCodec<ReplaceDisk> codec() {
        return CODEC;
    }
}

