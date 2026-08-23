package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class EntityDeathEvent extends Event {

    private final Object entity;
    private int droppedExp;

    public EntityDeathEvent(Object entity, int droppedExp) {{
        this.entity = entity;
        this.droppedExp = droppedExp;
    }}

    public Object getEntity() {{ return entity; }}
    public int getDroppedExp() {{ return droppedExp; }}
    public void setDroppedExp(int droppedExp) {{ this.droppedExp = droppedExp; }}
}
