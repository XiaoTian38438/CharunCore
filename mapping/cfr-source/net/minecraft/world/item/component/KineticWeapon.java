/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.minecraft.world.item.component;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.item.component.PiercingWeapon;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public record KineticWeapon(int contactCooldownTicks, int delayTicks, Optional<Condition> dismountConditions, Optional<Condition> knockbackConditions, Optional<Condition> damageConditions, float forwardMovement, float damageMultiplier, Optional<Holder<SoundEvent>> sound, Optional<Holder<SoundEvent>> hitSound) {
    public static final int HIT_FEEDBACK_TICKS = 10;
    public static final Codec<KineticWeapon> CODEC = RecordCodecBuilder.create(instance -> instance.group(ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("contact_cooldown_ticks", 10).forGetter(KineticWeapon::contactCooldownTicks), ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("delay_ticks", 0).forGetter(KineticWeapon::delayTicks), Condition.CODEC.optionalFieldOf("dismount_conditions").forGetter(KineticWeapon::dismountConditions), Condition.CODEC.optionalFieldOf("knockback_conditions").forGetter(KineticWeapon::knockbackConditions), Condition.CODEC.optionalFieldOf("damage_conditions").forGetter(KineticWeapon::damageConditions), Codec.FLOAT.optionalFieldOf("forward_movement", Float.valueOf(0.0f)).forGetter(KineticWeapon::forwardMovement), Codec.FLOAT.optionalFieldOf("damage_multiplier", Float.valueOf(1.0f)).forGetter(KineticWeapon::damageMultiplier), SoundEvent.CODEC.optionalFieldOf("sound").forGetter(KineticWeapon::sound), SoundEvent.CODEC.optionalFieldOf("hit_sound").forGetter(KineticWeapon::hitSound)).apply((Applicative<KineticWeapon, ?>)instance, KineticWeapon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, KineticWeapon> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, KineticWeapon::contactCooldownTicks, ByteBufCodecs.VAR_INT, KineticWeapon::delayTicks, Condition.STREAM_CODEC.apply(ByteBufCodecs::optional), KineticWeapon::dismountConditions, Condition.STREAM_CODEC.apply(ByteBufCodecs::optional), KineticWeapon::knockbackConditions, Condition.STREAM_CODEC.apply(ByteBufCodecs::optional), KineticWeapon::damageConditions, ByteBufCodecs.FLOAT, KineticWeapon::forwardMovement, ByteBufCodecs.FLOAT, KineticWeapon::damageMultiplier, SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), KineticWeapon::sound, SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), KineticWeapon::hitSound, KineticWeapon::new);

    public static Vec3 getMotion(Entity entity) {
        if (!(entity instanceof Player) && entity.isPassenger()) {
            entity = entity.getRootVehicle();
        }
        return entity.getKnownSpeed().scale(20.0);
    }

    public void makeSound(Entity entity) {
        this.sound.ifPresent(holder -> entity.level().playSound(entity, entity.getX(), entity.getY(), entity.getZ(), (Holder<SoundEvent>)holder, entity.getSoundSource(), 1.0f, 1.0f));
    }

    public void makeLocalHitSound(Entity entity) {
        this.hitSound.ifPresent(holder -> entity.level().playLocalSound(entity, (SoundEvent)holder.value(), entity.getSoundSource(), 1.0f, 1.0f));
    }

    public int computeDamageUseDuration() {
        return this.delayTicks + this.damageConditions.map(Condition::maxDurationTicks).orElse(0);
    }

    public void damageEntities(ItemStack itemStack, int n, LivingEntity livingEntity, EquipmentSlot equipmentSlot) {
        int n2 = itemStack.getUseDuration(livingEntity) - n;
        if (n2 < this.delayTicks) {
            return;
        }
        n2 -= this.delayTicks;
        Vec3 vec3 = livingEntity.getLookAngle();
        double d = vec3.dot(KineticWeapon.getMotion(livingEntity));
        float f = livingEntity instanceof Player ? 1.0f : 0.2f;
        AttackRange attackRange = livingEntity.entityAttackRange();
        double d2 = livingEntity.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
        boolean bl = false;
        for (EntityHitResult entityHitResult : ProjectileUtil.getHitEntitiesAlong(livingEntity, attackRange, entity -> PiercingWeapon.canHitEntity(livingEntity, entity), ClipContext.Block.COLLIDER).map(blockHitResult -> List.of(), collection -> collection)) {
            boolean bl2;
            boolean bl3;
            Entity entity2 = entityHitResult.getEntity();
            if (entity2 instanceof EnderDragonPart) {
                EnderDragonPart enderDragonPart = (EnderDragonPart)entity2;
                entity2 = enderDragonPart.parentMob;
            }
            if (bl3 = livingEntity.wasRecentlyStabbed(entity2, this.contactCooldownTicks)) continue;
            livingEntity.rememberStabbedEntity(entity2);
            double d3 = vec3.dot(KineticWeapon.getMotion(entity2));
            double d4 = Math.max(0.0, d - d3);
            boolean bl4 = this.dismountConditions.isPresent() && this.dismountConditions.get().test(n2, d, d4, f);
            boolean bl5 = this.knockbackConditions.isPresent() && this.knockbackConditions.get().test(n2, d, d4, f);
            boolean bl6 = bl2 = this.damageConditions.isPresent() && this.damageConditions.get().test(n2, d, d4, f);
            if (!bl4 && !bl5 && !bl2) continue;
            float f2 = (float)d2 + (float)Mth.floor(d4 * (double)this.damageMultiplier);
            bl |= livingEntity.stabAttack(equipmentSlot, entity2, f2, bl2, bl5, bl4);
        }
        if (bl) {
            livingEntity.level().broadcastEntityEvent(livingEntity, (byte)2);
            if (livingEntity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
                CriteriaTriggers.SPEAR_MOBS_TRIGGER.trigger(serverPlayer, livingEntity.stabbedEntities(entity -> entity instanceof LivingEntity));
            }
        }
    }

    public record Condition(int maxDurationTicks, float minSpeed, float minRelativeSpeed) {
        public static final Codec<Condition> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("max_duration_ticks")).forGetter(Condition::maxDurationTicks), Codec.FLOAT.optionalFieldOf("min_speed", Float.valueOf(0.0f)).forGetter(Condition::minSpeed), Codec.FLOAT.optionalFieldOf("min_relative_speed", Float.valueOf(0.0f)).forGetter(Condition::minRelativeSpeed)).apply((Applicative<Condition, ?>)instance, Condition::new));
        public static final StreamCodec<ByteBuf, Condition> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, Condition::maxDurationTicks, ByteBufCodecs.FLOAT, Condition::minSpeed, ByteBufCodecs.FLOAT, Condition::minRelativeSpeed, Condition::new);

        public boolean test(int n, double d, double d2, double d3) {
            return n <= this.maxDurationTicks && d >= (double)this.minSpeed * d3 && d2 >= (double)this.minRelativeSpeed * d3;
        }

        public static Optional<Condition> ofAttackerSpeed(int n, float f) {
            return Optional.of(new Condition(n, f, 0.0f));
        }

        public static Optional<Condition> ofRelativeSpeed(int n, float f) {
            return Optional.of(new Condition(n, 0.0f, f));
        }
    }
}

