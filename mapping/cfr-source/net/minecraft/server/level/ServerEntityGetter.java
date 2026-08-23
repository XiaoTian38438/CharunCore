/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.server.level;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

public interface ServerEntityGetter
extends EntityGetter {
    public ServerLevel getLevel();

    default public @Nullable Player getNearestPlayer(TargetingConditions targetingConditions, LivingEntity livingEntity) {
        return this.getNearestEntity(this.players(), targetingConditions, livingEntity, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
    }

    default public @Nullable Player getNearestPlayer(TargetingConditions targetingConditions, LivingEntity livingEntity, double d, double d2, double d3) {
        return this.getNearestEntity(this.players(), targetingConditions, livingEntity, d, d2, d3);
    }

    default public @Nullable Player getNearestPlayer(TargetingConditions targetingConditions, double d, double d2, double d3) {
        return this.getNearestEntity(this.players(), targetingConditions, null, d, d2, d3);
    }

    default public <T extends LivingEntity> @Nullable T getNearestEntity(Class<? extends T> clazz, TargetingConditions targetingConditions, @Nullable LivingEntity livingEntity2, double d, double d2, double d3, AABB aABB) {
        return (T)this.getNearestEntity(this.getEntitiesOfClass(clazz, aABB, livingEntity -> true), targetingConditions, livingEntity2, d, d2, d3);
    }

    default public @Nullable LivingEntity getNearestEntity(TagKey<EntityType<?>> tagKey, TargetingConditions targetingConditions, @Nullable LivingEntity livingEntity2, double d, double d2, double d3, AABB aABB) {
        double d4 = Double.MAX_VALUE;
        LivingEntity livingEntity3 = null;
        for (LivingEntity livingEntity4 : this.getEntitiesOfClass(LivingEntity.class, aABB, livingEntity -> livingEntity.getType().is(tagKey))) {
            double d5;
            if (!targetingConditions.test(this.getLevel(), livingEntity2, livingEntity4) || !((d5 = livingEntity4.distanceToSqr(d, d2, d3)) < d4)) continue;
            d4 = d5;
            livingEntity3 = livingEntity4;
        }
        return livingEntity3;
    }

    default public <T extends LivingEntity> @Nullable T getNearestEntity(List<? extends T> list, TargetingConditions targetingConditions, @Nullable LivingEntity livingEntity, double d, double d2, double d3) {
        double d4 = -1.0;
        LivingEntity livingEntity2 = null;
        for (LivingEntity livingEntity3 : list) {
            if (!targetingConditions.test(this.getLevel(), livingEntity, livingEntity3)) continue;
            double d5 = livingEntity3.distanceToSqr(d, d2, d3);
            if (d4 != -1.0 && !(d5 < d4)) continue;
            d4 = d5;
            livingEntity2 = livingEntity3;
        }
        return (T)livingEntity2;
    }

    default public List<Player> getNearbyPlayers(TargetingConditions targetingConditions, LivingEntity livingEntity, AABB aABB) {
        ArrayList<Player> arrayList = new ArrayList<Player>();
        for (Player player : this.players()) {
            if (!aABB.contains(player.getX(), player.getY(), player.getZ()) || !targetingConditions.test(this.getLevel(), livingEntity, player)) continue;
            arrayList.add(player);
        }
        return arrayList;
    }

    default public <T extends LivingEntity> List<T> getNearbyEntities(Class<T> clazz, TargetingConditions targetingConditions, LivingEntity livingEntity2, AABB aABB) {
        List<LivingEntity> list = this.getEntitiesOfClass(clazz, aABB, livingEntity -> true);
        ArrayList<LivingEntity> arrayList = new ArrayList<LivingEntity>();
        for (LivingEntity livingEntity3 : list) {
            if (!targetingConditions.test(this.getLevel(), livingEntity2, livingEntity3)) continue;
            arrayList.add(livingEntity3);
        }
        return arrayList;
    }
}

