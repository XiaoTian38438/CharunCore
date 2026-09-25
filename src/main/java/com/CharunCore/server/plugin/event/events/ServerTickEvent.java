package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

/** 服务器 tick(每 50ms)。在此做高频任务, 不要做重活。 */
public final class ServerTickEvent extends Event {

    private final long tickCount;
    private final long dayTime;

    public ServerTickEvent(long tickCount, long dayTime) {
        this.tickCount = tickCount;
        this.dayTime = dayTime;
    }

    /** 服务器累计 tick 数。 */
    public long getTickCount() { return tickCount; }

    /** 当前昼夜时间 (0-23999)。 */
    public long getDayTime() { return dayTime; }
}
