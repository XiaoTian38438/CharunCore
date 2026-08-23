package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class EntityDamageEvent extends Event {

    private final Object entity;
    private float amount;
    private final String source;

    public EntityDamageEvent(Object entity, float amount, String source) {{
        this.entity = entity;
        this.amount = amount;
        this.source = source;
    }}

    public Object getEntity() {{ return entity; }}
    public float getAmount() {{ return amount; }}
    public void setAmount(float amount) {{ this.amount = amount; }}
    public String getSource() {{ return source; }}
}
