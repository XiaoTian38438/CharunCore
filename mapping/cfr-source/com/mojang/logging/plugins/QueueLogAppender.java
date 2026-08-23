/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.core.Filter
 *  org.apache.logging.log4j.core.Layout
 *  org.apache.logging.log4j.core.LogEvent
 *  org.apache.logging.log4j.core.appender.AbstractAppender
 *  org.apache.logging.log4j.core.config.plugins.Plugin
 *  org.apache.logging.log4j.core.config.plugins.PluginAttribute
 *  org.apache.logging.log4j.core.config.plugins.PluginElement
 *  org.apache.logging.log4j.core.config.plugins.PluginFactory
 *  org.apache.logging.log4j.core.layout.PatternLayout
 */
package com.mojang.logging.plugins;

import com.mojang.logging.LogQueues;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(name="Queue", category="Core", elementType="appender", printObject=true)
public class QueueLogAppender
extends AbstractAppender {
    private static final int MAX_CAPACITY = 250;
    private final BlockingQueue<String> queue;

    public QueueLogAppender(String string, Filter filter, Layout<? extends Serializable> layout, boolean bl, BlockingQueue<String> blockingQueue) {
        super(string, filter, layout, bl);
        this.queue = blockingQueue;
    }

    public void append(LogEvent logEvent) {
        if (this.queue.size() >= 250) {
            this.queue.clear();
        }
        this.queue.add(this.getLayout().toSerializable(logEvent).toString());
    }

    @PluginFactory
    public static QueueLogAppender createAppender(@PluginAttribute(value="name") String string, @PluginAttribute(value="ignoreExceptions") String string2, @PluginElement(value="Layout") Layout<? extends Serializable> patternLayout, @PluginElement(value="Filters") Filter filter, @PluginAttribute(value="target") String string3) {
        boolean bl = Boolean.parseBoolean(string2);
        if (string == null) {
            LOGGER.error("No name provided for QueueLogAppender");
            return null;
        }
        BlockingQueue<String> blockingQueue = LogQueues.getOrCreateQueue(Objects.requireNonNullElse(string3, string));
        if (patternLayout == null) {
            patternLayout = PatternLayout.newBuilder().build();
        }
        return new QueueLogAppender(string, filter, (Layout<? extends Serializable>)patternLayout, bl, blockingQueue);
    }
}

