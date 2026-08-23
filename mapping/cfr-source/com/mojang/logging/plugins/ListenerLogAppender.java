/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.apache.logging.log4j.core.Filter
 *  org.apache.logging.log4j.core.Layout
 *  org.apache.logging.log4j.core.LogEvent
 *  org.apache.logging.log4j.core.appender.AbstractAppender
 *  org.apache.logging.log4j.core.config.Property
 *  org.apache.logging.log4j.core.config.plugins.Plugin
 *  org.apache.logging.log4j.core.config.plugins.PluginAttribute
 *  org.apache.logging.log4j.core.config.plugins.PluginElement
 *  org.apache.logging.log4j.core.config.plugins.PluginFactory
 *  org.apache.logging.log4j.core.layout.PatternLayout
 */
package com.mojang.logging.plugins;

import com.mojang.logging.LogListeners;
import java.io.Serializable;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(name="Listener", category="Core", elementType="appender", printObject=true)
public class ListenerLogAppender
extends AbstractAppender {
    private final LogListeners.Target output;

    public ListenerLogAppender(String string, Filter filter, Layout<? extends Serializable> layout, boolean bl, LogListeners.Target target) {
        super(string, filter, layout, bl, Property.EMPTY_ARRAY);
        this.output = target;
    }

    public void append(LogEvent logEvent) {
        this.output.post((Layout<? extends Serializable>)this.getLayout(), logEvent);
    }

    @PluginFactory
    @Nullable
    public static ListenerLogAppender createAppender(@PluginAttribute(value="name") @Nullable String string, @PluginAttribute(value="ignoreExceptions") String string2, @PluginElement(value="Layout") @Nullable Layout<? extends Serializable> patternLayout, @PluginElement(value="Filters") Filter filter, @PluginAttribute(value="target") @Nullable String string3) {
        boolean bl = Boolean.parseBoolean(string2);
        if (string == null) {
            LOGGER.error("No name provided for ListenerLogAppender");
            return null;
        }
        LogListeners.Target target = LogListeners.getOrCreateTarget(Objects.requireNonNullElse(string3, string));
        if (patternLayout == null) {
            patternLayout = PatternLayout.newBuilder().build();
        }
        return new ListenerLogAppender(string, filter, (Layout<? extends Serializable>)patternLayout, bl, target);
    }
}

