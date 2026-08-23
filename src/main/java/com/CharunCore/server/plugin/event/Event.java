package com.CharunCore.server.plugin.event;

public abstract class Event {

    private boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getEventName() {
        return getClass().getSimpleName();
    }
}
