package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class FurnaceSmeltEvent extends Event {
    private final int x;
    private final int y;
    private final int z;
    private final String source;
    private final String result;

    public FurnaceSmeltEvent(int x, int y, int z, String source, String result) { this.x=x; this.y=y; this.z=z; this.source=source; this.result=result; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public String getSource() { return source; }
    public String getResult() { return result; }
}
