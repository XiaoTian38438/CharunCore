package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class WeatherChangeEvent extends Event {

    private final boolean raining;
    private final boolean thundering;

    public WeatherChangeEvent(boolean raining, boolean thundering) {{
        this.raining = raining;
        this.thundering = thundering;
    }}

    public boolean isRaining() {{ return raining; }}
    public boolean isThundering() {{ return thundering; }}
}
