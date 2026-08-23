package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class EntityDamageByEntityEvent extends Event {

    private final Object entity;
    private final Object damager;
    private float amount;
    private final String source;

    public EntityDamageByEntityEvent(Object entity, Object damager, float amount, String source) {{
        this.entity = entity;
        this.damager = damager;
        this.amount = amount;
        this.source = source;
    }}

    public Object getEntity() {{ return entity; }}
    public Object getDamager() {{ return damager; }}
    public float getAmount() {{ return amount; }}
    public void setAmount(float amount) {{ this.amount = amount; }}
    public String getSource() {{ return source; }}
}
