package com.CharunCore.server.world.entity;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.plugin.event.EventManager;
import com.CharunCore.server.plugin.event.events.EntityDamageEvent;

public class LivingEntity extends Entity {
    public float maxHealth = 20.0f;
    public int attackTime = 0;
    public int invulnTicks = 0;
    public String lastDamageSource = null;
    public NetworkHandler lastAttacker = null;

    public LivingEntity(int id, int type, double x, double y, double z) {
        super(id, type, x, y, z);
    }

    public void damage(float amount, String source) {
        if (deathTime > 0) return;
        if (invulnTicks > 0 && !"fall".equals(source) && !"void".equals(source)) return;
        var damageEvent = EventManager.INSTANCE.fire(
                new EntityDamageEvent(this, amount, source));
        if (damageEvent.isCancelled()) return;
        amount = damageEvent.getAmount();
        if (amount <= 0) return;
        this.health -= amount;
        this.hurtTime = 10;
        this.invulnTicks = 10;
        this.lastDamageSource = source;
        EntityManager.broadcastEntityHurt(this);
        if (this.health <= 0) {
            this.health = 0;
            this.deathTime = 1;
            EntityManager.broadcastEntityDeath(this);
            onDeath();
        }
    }

    protected void onDeath() {
    }

    @Override
    public void tick() {
        super.tick();
        if (invulnTicks > 0) invulnTicks--;
        if (attackTime > 0) attackTime--;
        if (fireTicks > 0 && fireTicks % 20 == 0) {
            damage(1.0f, "fire");
        }
        if (isInFluid("lava")) {
            fireTicks = Math.max(fireTicks, 160);
            damage(4.0f, "lava");
        }
    }
}
