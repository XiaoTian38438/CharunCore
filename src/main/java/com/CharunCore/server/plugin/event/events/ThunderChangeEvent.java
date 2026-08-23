package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class ThunderChangeEvent extends Event {
    private final boolean thundering;

    public ThunderChangeEvent(boolean thundering) { this.thundering=thundering; }

    public boolean isThundering() { return thundering; }
}
