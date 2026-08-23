/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import java.util.Optional;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.component.KineticWeapon;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class SpearUseGoal<T extends Monster>
extends Goal {
    static final int MIN_REPOSITION_DISTANCE = 6;
    static final int MAX_REPOSITION_DISTANCE = 7;
    static final int MIN_COOLDOWN_DISTANCE = 9;
    static final int MAX_COOLDOWN_DISTANCE = 11;
    static final double MAX_FLEEING_TIME = SpearUseGoal.reducedTickDelay(100);
    private final T mob;
    private @Nullable SpearUseState state;
    double speedModifierWhenCharging;
    double speedModifierWhenRepositioning;
    float approachDistanceSq;
    float targetInRangeRadiusSq;

    public SpearUseGoal(T t, double d, double d2, float f, float f2) {
        this.mob = t;
        this.speedModifierWhenCharging = d;
        this.speedModifierWhenRepositioning = d2;
        this.approachDistanceSq = f * f;
        this.targetInRangeRadiusSq = f2 * f2;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.ableToAttack() && !((LivingEntity)this.mob).isUsingItem();
    }

    private boolean ableToAttack() {
        return ((Mob)this.mob).getTarget() != null && ((LivingEntity)this.mob).getMainHandItem().has(DataComponents.KINETIC_WEAPON);
    }

    private int getKineticWeaponUseDuration() {
        int n = Optional.ofNullable(((LivingEntity)this.mob).getMainHandItem().get(DataComponents.KINETIC_WEAPON)).map(KineticWeapon::computeDamageUseDuration).orElse(0);
        return SpearUseGoal.reducedTickDelay(n);
    }

    @Override
    public boolean canContinueToUse() {
        return this.state != null && !this.state.done && this.ableToAttack();
    }

    @Override
    public void start() {
        super.start();
        ((Mob)this.mob).setAggressive(true);
        this.state = new SpearUseState();
    }

    @Override
    public void stop() {
        super.stop();
        ((Mob)this.mob).getNavigation().stop();
        ((Mob)this.mob).setAggressive(false);
        this.state = null;
        ((LivingEntity)this.mob).stopUsingItem();
    }

    @Override
    public void tick() {
        double d;
        if (this.state == null) {
            return;
        }
        LivingEntity livingEntity = ((Mob)this.mob).getTarget();
        double d2 = ((Entity)this.mob).distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        Entity entity = ((Entity)this.mob).getRootVehicle();
        float f = 1.0f;
        if (entity instanceof Mob) {
            Mob mob = (Mob)entity;
            f = mob.chargeSpeedModifier();
        }
        int n = ((Entity)this.mob).isPassenger() ? 2 : 0;
        ((Mob)this.mob).lookAt(livingEntity, 30.0f, 30.0f);
        ((Mob)this.mob).getLookControl().setLookAt(livingEntity, 30.0f, 30.0f);
        if (this.state.notEngagedYet()) {
            if (d2 > (double)this.approachDistanceSq) {
                ((Mob)this.mob).getNavigation().moveTo(livingEntity, (double)f * this.speedModifierWhenRepositioning);
                return;
            }
            this.state.startEngagement(this.getKineticWeaponUseDuration());
            ((LivingEntity)this.mob).startUsingItem(InteractionHand.MAIN_HAND);
        }
        if (this.state.tickAndCheckEngagement()) {
            ((LivingEntity)this.mob).stopUsingItem();
            d = Math.sqrt(d2);
            this.state.awayPos = LandRandomPos.getPosAway(this.mob, Math.max(0.0, (double)(9 + n) - d), Math.max(1.0, (double)(11 + n) - d), 7, livingEntity.position());
            this.state.fleeingTime = 1;
        }
        if (this.state.tickAndCheckFleeing()) {
            return;
        }
        if (this.state.awayPos != null) {
            ((Mob)this.mob).getNavigation().moveTo(this.state.awayPos.x, this.state.awayPos.y, this.state.awayPos.z, (double)f * this.speedModifierWhenRepositioning);
            if (((Mob)this.mob).getNavigation().isDone()) {
                if (this.state.fleeingTime > 0) {
                    this.state.done = true;
                    return;
                }
                this.state.awayPos = null;
            }
        } else {
            ((Mob)this.mob).getNavigation().moveTo(livingEntity, (double)f * this.speedModifierWhenCharging);
            if (d2 < (double)this.targetInRangeRadiusSq || ((Mob)this.mob).getNavigation().isDone()) {
                d = Math.sqrt(d2);
                this.state.awayPos = LandRandomPos.getPosAway(this.mob, (double)(6 + n) - d, (double)(7 + n) - d, 7, livingEntity.position());
            }
        }
    }

    public static class SpearUseState {
        private int engageTime = -1;
        int fleeingTime = -1;
        @Nullable Vec3 awayPos;
        boolean done = false;

        public boolean notEngagedYet() {
            return this.engageTime < 0;
        }

        public void startEngagement(int n) {
            this.engageTime = n;
        }

        public boolean tickAndCheckEngagement() {
            if (this.engageTime > 0) {
                --this.engageTime;
                if (this.engageTime == 0) {
                    return true;
                }
            }
            return false;
        }

        public boolean tickAndCheckFleeing() {
            if (this.fleeingTime > 0) {
                ++this.fleeingTime;
                if ((double)this.fleeingTime > MAX_FLEEING_TIME) {
                    this.done = true;
                    return true;
                }
            }
            return false;
        }
    }
}

