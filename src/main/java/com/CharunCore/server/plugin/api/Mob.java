package com.CharunCore.server.plugin.api;

import com.CharunCore.server.world.entity.LivingEntity;
import com.CharunCore.server.world.entity.MobEntity;

/** 生物门面(对标 Paper LivingEntity/Mob): 血量/伤害/目标控制。 */
public final class Mob extends Entity {

    Mob(com.CharunCore.server.world.entity.Entity handle) {
        super(handle);
    }

    public float getHealth() { return h.health; }

    public void setHealth(float health) {
        h.health = Math.max(0, Math.min(health, maxHealth()));
        if (h.health <= 0 && h instanceof LivingEntity le) {
            le.damage(1.0f, "plugin");
        }
    }

    /** 设置最大生命(同时刷新到当前值上限)。 */
    public void setMaxHealth(float max) {
        if (h instanceof MobEntity mob) {
            mob.maxHealth = Math.max(1.0f, max);
            h.health = Math.min(h.health, mob.maxHealth);
        }
    }

    public float maxHealth() {
        return h instanceof MobEntity mob ? mob.maxHealth : h.health;
    }

    /** 直接造成伤害(走底层 damage 管线, 触发 EntityDamageEvent)。 */
    public void damage(float amount) { h.damage(amount, "plugin"); }

    public void damage(float amount, String source) { h.damage(amount, source); }

    /** 是否为 MobEntity(有 AI 的生物)。 */
    public boolean isMob() { return h instanceof MobEntity; }

    /** MobEntity 专属句柄(高级用法)。 */
    public MobEntity mobHandle() { return h instanceof MobEntity m ? m : null; }
}
