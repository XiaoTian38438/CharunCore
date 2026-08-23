package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;
import com.CharunCore.server.network.NetworkHandler;

public final class EntityTargetEvent extends Event {

    private final Object entity;
    private NetworkHandler target;
    private final NetworkHandler oldTarget;

    public EntityTargetEvent(Object entity, NetworkHandler target, NetworkHandler oldTarget) {{
        this.entity = entity;
        this.target = target;
        this.oldTarget = oldTarget;
    }}

    public Object getEntity() {{ return entity; }}
    public NetworkHandler getTarget() {{ return target; }}
    public NetworkHandler getOldTarget() {{ return oldTarget; }}
    public void setTarget(NetworkHandler target) {{ this.target = target; }}
}
