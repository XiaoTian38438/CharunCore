/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.minecraft.world.item.component;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public record AttackRange(float minRange, float maxRange, float minCreativeRange, float maxCreativeRange, float hitboxMargin, float mobFactor) {
    public static final Codec<AttackRange> CODEC = RecordCodecBuilder.create(instance -> instance.group(ExtraCodecs.floatRange(0.0f, 64.0f).optionalFieldOf("min_reach", Float.valueOf(0.0f)).forGetter(AttackRange::minRange), ExtraCodecs.floatRange(0.0f, 64.0f).optionalFieldOf("max_reach", Float.valueOf(3.0f)).forGetter(AttackRange::maxRange), ExtraCodecs.floatRange(0.0f, 64.0f).optionalFieldOf("min_creative_reach", Float.valueOf(0.0f)).forGetter(AttackRange::minCreativeRange), ExtraCodecs.floatRange(0.0f, 64.0f).optionalFieldOf("max_creative_reach", Float.valueOf(5.0f)).forGetter(AttackRange::maxCreativeRange), ExtraCodecs.floatRange(0.0f, 1.0f).optionalFieldOf("hitbox_margin", Float.valueOf(0.3f)).forGetter(AttackRange::hitboxMargin), Codec.floatRange(0.0f, 2.0f).optionalFieldOf("mob_factor", Float.valueOf(1.0f)).forGetter(AttackRange::mobFactor)).apply((Applicative<AttackRange, ?>)instance, AttackRange::new));
    public static final StreamCodec<ByteBuf, AttackRange> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, AttackRange::minRange, ByteBufCodecs.FLOAT, AttackRange::maxRange, ByteBufCodecs.FLOAT, AttackRange::minCreativeRange, ByteBufCodecs.FLOAT, AttackRange::maxCreativeRange, ByteBufCodecs.FLOAT, AttackRange::hitboxMargin, ByteBufCodecs.FLOAT, AttackRange::mobFactor, AttackRange::new);

    public static AttackRange defaultFor(LivingEntity livingEntity) {
        return new AttackRange(0.0f, (float)livingEntity.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE), 0.0f, (float)livingEntity.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE), 0.0f, 1.0f);
    }

    public HitResult getClosesetHit(Entity entity, float f, Predicate<Entity> predicate) {
        Either<BlockHitResult, Collection<EntityHitResult>> either = ProjectileUtil.getHitEntitiesAlong(entity, this, predicate, ClipContext.Block.OUTLINE);
        if (either.left().isPresent()) {
            return either.left().get();
        }
        Collection<EntityHitResult> collection = either.right().get();
        EntityHitResult object2 = null;
        Vec3 vec3 = entity.getEyePosition(f);
        double d = Double.MAX_VALUE;
        for (EntityHitResult object3 : collection) {
            double d2 = vec3.distanceToSqr(object3.getLocation());
            if (!(d2 < d)) continue;
            d = d2;
            object2 = object3;
        }
        if (object2 != null) {
            return object2;
        }
        Vec3 vec32 = entity.getHeadLookAngle();
        Vec3 vec33 = entity.getEyePosition(f).add(vec32);
        return BlockHitResult.miss(vec33, Direction.getApproximateNearest(vec32), BlockPos.containing(vec33));
    }

    public float effectiveMinRange(Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (player.isSpectator()) {
                return 0.0f;
            }
            return player.isCreative() ? this.minCreativeRange : this.minRange;
        }
        return this.minRange * this.mobFactor;
    }

    public float effectiveMaxRange(Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player)entity;
            return player.isCreative() ? this.maxCreativeRange : this.maxRange;
        }
        return this.maxRange * this.mobFactor;
    }

    public boolean isInRange(LivingEntity livingEntity, Vec3 vec3) {
        return this.isInRange(livingEntity, vec3::distanceToSqr, 0.0);
    }

    public boolean isInRange(LivingEntity livingEntity, AABB aABB, double d) {
        return this.isInRange(livingEntity, aABB::distanceToSqr, d);
    }

    private boolean isInRange(LivingEntity livingEntity, ToDoubleFunction<Vec3> toDoubleFunction, double d) {
        double d2 = Math.sqrt(toDoubleFunction.applyAsDouble(livingEntity.getEyePosition()));
        double d3 = (double)(this.effectiveMinRange(livingEntity) - this.hitboxMargin) - d;
        double d4 = (double)(this.effectiveMaxRange(livingEntity) + this.hitboxMargin) + d;
        return d2 >= d3 && d2 <= d4;
    }
}

