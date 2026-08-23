/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Level
 *  org.apache.logging.log4j.core.Layout
 *  org.apache.logging.log4j.core.LogEvent
 *  org.slf4j.event.Level
 */
package com.mojang.logging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;

public class LogListeners {
    private static final Map<String, Target> TARGETS = new ConcurrentHashMap<String, Target>();

    public static Target getOrCreateTarget(String string2) {
        return TARGETS.computeIfAbsent(string2, string -> new Target());
    }

    public static void addListener(String string, Listener listener) {
        LogListeners.getOrCreateTarget(string).addListener(listener);
    }

    public static class Target {
        private volatile List<Listener> listeners = List.of();

        private synchronized void addListener(Listener listener) {
            ArrayList<Listener> arrayList = new ArrayList<Listener>(this.listeners.size() + 1);
            arrayList.addAll(this.listeners);
            arrayList.add(listener);
            this.listeners = arrayList;
        }

        public void post(Layout<? extends Serializable> layout, LogEvent logEvent) {
            if (this.listeners.isEmpty()) {
                return;
            }
            String string = layout.toSerializable(logEvent).toString();
            org.slf4j.event.Level level = Target.log4jToSlf4jLevel(logEvent.getLevel());
            for (Listener listener : this.listeners) {
                listener.accept(string, level);
            }
        }

        private static org.slf4j.event.Level log4jToSlf4jLevel(Level level) {
            if (level == Level.ERROR) {
                return org.slf4j.event.Level.ERROR;
            }
            if (level == Level.WARN) {
                return org.slf4j.event.Level.WARN;
            }
            if (level == Level.INFO) {
                return org.slf4j.event.Level.INFO;
            }
            if (level == Level.DEBUG) {
                return org.slf4j.event.Level.DEBUG;
            }
            if (level == Level.TRACE) {
                return org.slf4j.event.Level.TRACE;
            }
            return org.slf4j.event.Level.INFO;
        }
    }

    public static interface Listener {
        public void accept(String var1, org.slf4j.event.Level var2);
    }
}

