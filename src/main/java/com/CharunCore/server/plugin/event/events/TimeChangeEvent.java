package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class TimeChangeEvent extends Event {
    private final int oldTime;
    private final int newTime;

    public TimeChangeEvent(int oldTime, int newTime) { this.oldTime=oldTime; this.newTime=newTime; }

    public int getOldTime() { return oldTime; }
    public int getNewTime() { return newTime; }
}
