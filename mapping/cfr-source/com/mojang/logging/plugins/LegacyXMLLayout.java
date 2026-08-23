/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Level
 *  org.apache.logging.log4j.Marker
 *  org.apache.logging.log4j.core.LogEvent
 *  org.apache.logging.log4j.core.config.plugins.Plugin
 *  org.apache.logging.log4j.core.config.plugins.PluginAttribute
 *  org.apache.logging.log4j.core.config.plugins.PluginFactory
 *  org.apache.logging.log4j.core.layout.AbstractStringLayout
 *  org.apache.logging.log4j.core.util.Throwables
 *  org.apache.logging.log4j.core.util.Transform
 *  org.apache.logging.log4j.message.Message
 *  org.apache.logging.log4j.message.MultiformatMessage
 *  org.apache.logging.log4j.util.Strings
 */
package com.mojang.logging.plugins;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.util.Throwables;
import org.apache.logging.log4j.core.util.Transform;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MultiformatMessage;
import org.apache.logging.log4j.util.Strings;

@Plugin(name="LegacyXMLLayout", category="Core", elementType="layout", printObject=true)
public class LegacyXMLLayout
extends AbstractStringLayout {
    private static final String XML_NAMESPACE = "http://logging.apache.org/log4j/2.0/events";
    private static final String ROOT_TAG = "Events";
    private static final int DEFAULT_SIZE = 256;
    private static final String DEFAULT_EOL = "\r\n";
    private static final String COMPACT_EOL = "";
    private static final String DEFAULT_INDENT = "  ";
    private static final String COMPACT_INDENT = "";
    private static final String DEFAULT_NS_PREFIX = "log4j";
    private static final String[] FORMATS = new String[]{"xml"};
    private final boolean locationInfo;
    private final boolean properties;
    private final boolean complete;
    private final String namespacePrefix;
    private final String eol;
    private final String indent1;
    private final String indent2;
    private final String indent3;

    protected LegacyXMLLayout(boolean bl, boolean bl2, boolean bl3, boolean bl4, String string, Charset charset) {
        super(charset);
        this.locationInfo = bl;
        this.properties = bl2;
        this.complete = bl3;
        this.eol = bl4 ? "" : DEFAULT_EOL;
        this.indent1 = bl4 ? "" : DEFAULT_INDENT;
        this.indent2 = this.indent1 + this.indent1;
        this.indent3 = this.indent2 + this.indent1;
        this.namespacePrefix = (Strings.isEmpty((CharSequence)string) ? DEFAULT_NS_PREFIX : string) + ":";
    }

    public String toSerializable(LogEvent logEvent) {
        Throwable throwable;
        Object object;
        StringBuilder stringBuilder = new StringBuilder(256);
        stringBuilder.append(this.indent1);
        stringBuilder.append('<');
        if (!this.complete) {
            stringBuilder.append(this.namespacePrefix);
        }
        stringBuilder.append("Event logger=\"");
        String string = logEvent.getLoggerName();
        if (string.isEmpty()) {
            string = "root";
        }
        stringBuilder.append(Transform.escapeHtmlTags((String)string));
        stringBuilder.append("\" timestamp=\"");
        stringBuilder.append(logEvent.getTimeMillis());
        stringBuilder.append("\" level=\"");
        stringBuilder.append(Transform.escapeHtmlTags((String)LegacyXMLLayout.getEventLevel(logEvent)));
        stringBuilder.append("\" thread=\"");
        stringBuilder.append(Transform.escapeHtmlTags((String)logEvent.getThreadName()));
        stringBuilder.append("\">");
        stringBuilder.append(this.eol);
        Message message = logEvent.getMessage();
        if (message != null) {
            boolean bl = false;
            if (message instanceof MultiformatMessage) {
                object = ((MultiformatMessage)message).getFormats();
                for (String string2 : object) {
                    if (!string2.equalsIgnoreCase("XML")) continue;
                    bl = true;
                    break;
                }
            }
            stringBuilder.append(this.indent2);
            stringBuilder.append('<');
            if (!this.complete) {
                stringBuilder.append(this.namespacePrefix);
            }
            stringBuilder.append("Message>");
            if (bl) {
                stringBuilder.append(((MultiformatMessage)message).getFormattedMessage(FORMATS));
            } else {
                stringBuilder.append("<![CDATA[");
                Transform.appendEscapingCData((StringBuilder)stringBuilder, (String)logEvent.getMessage().getFormattedMessage());
                stringBuilder.append("]]>");
            }
            stringBuilder.append("</");
            if (!this.complete) {
                stringBuilder.append(this.namespacePrefix);
            }
            stringBuilder.append("Message>");
            stringBuilder.append(this.eol);
        }
        if (logEvent.getContextStack().getDepth() > 0) {
            stringBuilder.append(this.indent2);
            stringBuilder.append('<');
            if (!this.complete) {
                stringBuilder.append(this.namespacePrefix);
            }
            stringBuilder.append("NDC><![CDATA[");
            Transform.appendEscapingCData((StringBuilder)stringBuilder, (String)logEvent.getContextStack().toString());
            stringBuilder.append("]]></");
            if (!this.complete) {
                stringBuilder.append(this.namespacePrefix);
            }
            stringBuilder.append("NDC>");
            stringBuilder.append(this.eol);
        }
        if ((throwable = logEvent.getThrown()) != null) {
            object = Throwables.toStringList((Throwable)throwable);
            stringBuilder.append(this.indent2);
            stringBuilder.append('<');
            if (!this.complete) {
                stringBuilder.append(this.namespacePrefix);
            }
            stringBuilder.append("Throwable><![CDATA[");
            Iterator iterator = object.iterator();
            while (iterator.hasNext()) {
                String string3 = (String)iterator.next();
                Transform.appendEscapingCData((StringBuilder)stringBuilder, (String)string3);
                stringBuilder.append(this.eol);
            }
            stringBuilder.append("]]></");
            if (!this.complete) {
                stringBuilder.append(this.namespacePrefix);
            }
            stringBuilder.append("Throwable>");
            stringBuilder.append(this.eol);
        }
        if (this.locationInfo) {
            object = logEvent.getSource();
            stringBuilder.append(this.indent2);
            stringBuilder.append('<');
            if (!this.complete) {
                stringBuilder.append(this.namespacePrefix);
            }
            stringBuilder.append("LocationInfo class=\"");
            stringBuilder.append(Transform.escapeHtmlTags((String)((StackTraceElement)object).getClassName()));
            stringBuilder.append("\" method=\"");
            stringBuilder.append(Transform.escapeHtmlTags((String)((StackTraceElement)object).getMethodName()));
            stringBuilder.append("\" file=\"");
            stringBuilder.append(Transform.escapeHtmlTags((String)((StackTraceElement)object).getFileName()));
            stringBuilder.append("\" line=\"");
            stringBuilder.append(((StackTraceElement)object).getLineNumber());
            stringBuilder.append("\"/>");
            stringBuilder.append(this.eol);
        }
        if (this.properties && logEvent.getContextMap().size() > 0) {
            stringBuilder.append(this.indent2);
            stringBuilder.append('<');
            if (!this.complete) {
                stringBuilder.append(this.namespacePrefix);
            }
            stringBuilder.append("Properties>");
            stringBuilder.append(this.eol);
            for (Map.Entry entry : logEvent.getContextMap().entrySet()) {
                stringBuilder.append(this.indent3);
                stringBuilder.append('<');
                if (!this.complete) {
                    stringBuilder.append(this.namespacePrefix);
                }
                stringBuilder.append("Data name=\"");
                stringBuilder.append(Transform.escapeHtmlTags((String)((String)entry.getKey())));
                stringBuilder.append("\" value=\"");
                stringBuilder.append(Transform.escapeHtmlTags((String)String.valueOf(entry.getValue())));
                stringBuilder.append("\"/>");
                stringBuilder.append(this.eol);
            }
            stringBuilder.append(this.indent2);
            stringBuilder.append("</");
            if (!this.complete) {
                stringBuilder.append(this.namespacePrefix);
            }
            stringBuilder.append("Properties>");
            stringBuilder.append(this.eol);
        }
        stringBuilder.append(this.indent1);
        stringBuilder.append("</");
        if (!this.complete) {
            stringBuilder.append(this.namespacePrefix);
        }
        stringBuilder.append("Event>");
        stringBuilder.append(this.eol);
        return stringBuilder.toString();
    }

    public byte[] getHeader() {
        if (!this.complete) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"");
        stringBuilder.append(this.getCharset().name());
        stringBuilder.append("\"?>");
        stringBuilder.append(this.eol);
        stringBuilder.append('<');
        stringBuilder.append(ROOT_TAG);
        stringBuilder.append(" xmlns=\"http://logging.apache.org/log4j/2.0/events\">");
        stringBuilder.append(this.eol);
        return stringBuilder.toString().getBytes(this.getCharset());
    }

    public byte[] getFooter() {
        if (!this.complete) {
            return null;
        }
        return ("</Events>" + this.eol).getBytes(this.getCharset());
    }

    public Map<String, String> getContentFormat() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("xsd", "log4j-events.xsd");
        hashMap.put("version", "2.0");
        return hashMap;
    }

    public String getContentType() {
        return "text/xml; charset=" + String.valueOf(this.getCharset());
    }

    private static String getEventLevel(LogEvent logEvent) {
        Marker marker = logEvent.getMarker();
        if (marker != null && marker.isInstanceOf("FATAL")) {
            return String.valueOf(Level.FATAL);
        }
        return String.valueOf(logEvent.getLevel());
    }

    @PluginFactory
    public static LegacyXMLLayout createLayout(@PluginAttribute(value="locationInfo") boolean bl, @PluginAttribute(value="properties") boolean bl2, @PluginAttribute(value="complete") boolean bl3, @PluginAttribute(value="compact") boolean bl4, @PluginAttribute(value="namespacePrefix") String string, @PluginAttribute(value="charset", defaultString="UTF-8") Charset charset) {
        return new LegacyXMLLayout(bl, bl2, bl3, bl4, string, charset);
    }
}

