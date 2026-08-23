package com.CharunCore.server.world.entity;

import com.CharunCore.server.world.ExplosionEngine;

public class EndCrystalEntity extends LivingEntity {

    public boolean showBottom = true;
    private boolean exploded = false;

    public EndCrystalEntity(int id, double x, double y, double z) {
        super(id, 0, x, y, z);
        this.typeName = "end_crystal";
        this.noPhysics = true;
        this.width = 2.0;
        this.height = 2.0;
        this.maxHealth = 1.0f;
        this.health = 1.0f;
    }

    @Override
    public void damage(float amount, String source) {
        if (exploded || deathTime > 0) return;
        exploded = true;
        this.health = 0;
        this.deathTime = 1;
        onDeath();
    }

    @Override
    protected void onDeath() {
        ExplosionEngine.explode(dim, x, y, z, 6.0f, false);
        EndDragonFight.onCrystalDestroyed(this);
        remove();
    }

    @Override
    public void tick() {
        if (deathTime > 0) return;
        if (invulnTicks > 0) invulnTicks--;
        if (hurtTime > 0) hurtTime--;
    }
}
