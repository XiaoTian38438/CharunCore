/*     */ package com.mojang.logging.plugins;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.layout.AbstractStringLayout;
/*     */ import org.apache.logging.log4j.core.util.Throwables;
/*     */ import org.apache.logging.log4j.core.util.Transform;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.MultiformatMessage;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "LegacyXMLLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public class LegacyXMLLayout
/*     */   extends AbstractStringLayout
/*     */ {
/*     */   private static final String XML_NAMESPACE = "http://logging.apache.org/log4j/2.0/events";
/*     */   private static final String ROOT_TAG = "Events";
/*     */   private static final int DEFAULT_SIZE = 256;
/*     */   private static final String DEFAULT_EOL = "\r\n";
/*     */   private static final String COMPACT_EOL = "";
/*     */   private static final String DEFAULT_INDENT = "  ";
/*     */   private static final String COMPACT_INDENT = "";
/*     */   private static final String DEFAULT_NS_PREFIX = "log4j";
/*  99 */   private static final String[] FORMATS = new String[] { "xml" };
/*     */   
/*     */   private final boolean locationInfo;
/*     */   
/*     */   private final boolean properties;
/*     */   private final boolean complete;
/*     */   private final String namespacePrefix;
/*     */   private final String eol;
/*     */   private final String indent1;
/*     */   private final String indent2;
/*     */   private final String indent3;
/*     */   
/*     */   protected LegacyXMLLayout(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, String paramString, Charset paramCharset) {
/* 112 */     super(paramCharset);
/* 113 */     this.locationInfo = paramBoolean1;
/* 114 */     this.properties = paramBoolean2;
/* 115 */     this.complete = paramBoolean3;
/* 116 */     this.eol = paramBoolean4 ? "" : "\r\n";
/* 117 */     this.indent1 = paramBoolean4 ? "" : "  ";
/* 118 */     this.indent2 = this.indent1 + this.indent1;
/* 119 */     this.indent3 = this.indent2 + this.indent2;
/* 120 */     this.namespacePrefix = (Strings.isEmpty(paramString) ? "log4j" : paramString) + ":";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toSerializable(LogEvent paramLogEvent) {
/* 131 */     StringBuilder stringBuilder = new StringBuilder(256);
/*     */     
/* 133 */     stringBuilder.append(this.indent1);
/* 134 */     stringBuilder.append('<');
/* 135 */     if (!this.complete) {
/* 136 */       stringBuilder.append(this.namespacePrefix);
/*     */     }
/* 138 */     stringBuilder.append("Event logger=\"");
/* 139 */     String str = paramLogEvent.getLoggerName();
/* 140 */     if (str.isEmpty()) {
/* 141 */       str = "root";
/*     */     }
/* 143 */     stringBuilder.append(Transform.escapeHtmlTags(str));
/* 144 */     stringBuilder.append("\" timestamp=\"");
/* 145 */     stringBuilder.append(paramLogEvent.getTimeMillis());
/* 146 */     stringBuilder.append("\" level=\"");
/* 147 */     stringBuilder.append(Transform.escapeHtmlTags(getEventLevel(paramLogEvent)));
/* 148 */     stringBuilder.append("\" thread=\"");
/* 149 */     stringBuilder.append(Transform.escapeHtmlTags(paramLogEvent.getThreadName()));
/* 150 */     stringBuilder.append("\">");
/* 151 */     stringBuilder.append(this.eol);
/*     */     
/* 153 */     Message message = paramLogEvent.getMessage();
/* 154 */     if (message != null) {
/* 155 */       boolean bool = false;
/* 156 */       if (message instanceof MultiformatMessage) {
/* 157 */         String[] arrayOfString = ((MultiformatMessage)message).getFormats();
/* 158 */         for (String str1 : arrayOfString) {
/* 159 */           if (str1.equalsIgnoreCase("XML")) {
/* 160 */             bool = true;
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 165 */       stringBuilder.append(this.indent2);
/* 166 */       stringBuilder.append('<');
/* 167 */       if (!this.complete) {
/* 168 */         stringBuilder.append(this.namespacePrefix);
/*     */       }
/* 170 */       stringBuilder.append("Message>");
/* 171 */       if (bool) {
/* 172 */         stringBuilder.append(((MultiformatMessage)message).getFormattedMessage(FORMATS));
/*     */       } else {
/* 174 */         stringBuilder.append("<![CDATA[");
/*     */ 
/*     */         
/* 177 */         Transform.appendEscapingCData(stringBuilder, paramLogEvent.getMessage().getFormattedMessage());
/* 178 */         stringBuilder.append("]]>");
/*     */       } 
/* 180 */       stringBuilder.append("</");
/* 181 */       if (!this.complete) {
/* 182 */         stringBuilder.append(this.namespacePrefix);
/*     */       }
/* 184 */       stringBuilder.append("Message>");
/* 185 */       stringBuilder.append(this.eol);
/*     */     } 
/*     */     
/* 188 */     if (paramLogEvent.getContextStack().getDepth() > 0) {
/* 189 */       stringBuilder.append(this.indent2);
/* 190 */       stringBuilder.append('<');
/* 191 */       if (!this.complete) {
/* 192 */         stringBuilder.append(this.namespacePrefix);
/*     */       }
/* 194 */       stringBuilder.append("NDC><![CDATA[");
/* 195 */       Transform.appendEscapingCData(stringBuilder, paramLogEvent.getContextStack().toString());
/* 196 */       stringBuilder.append("]]></");
/* 197 */       if (!this.complete) {
/* 198 */         stringBuilder.append(this.namespacePrefix);
/*     */       }
/* 200 */       stringBuilder.append("NDC>");
/* 201 */       stringBuilder.append(this.eol);
/*     */     } 
/*     */     
/* 204 */     Throwable throwable = paramLogEvent.getThrown();
/* 205 */     if (throwable != null) {
/* 206 */       List list = Throwables.toStringList(throwable);
/* 207 */       stringBuilder.append(this.indent2);
/* 208 */       stringBuilder.append('<');
/* 209 */       if (!this.complete) {
/* 210 */         stringBuilder.append(this.namespacePrefix);
/*     */       }
/* 212 */       stringBuilder.append("Throwable><![CDATA[");
/* 213 */       for (String str1 : list) {
/* 214 */         Transform.appendEscapingCData(stringBuilder, str1);
/* 215 */         stringBuilder.append(this.eol);
/*     */       } 
/* 217 */       stringBuilder.append("]]></");
/* 218 */       if (!this.complete) {
/* 219 */         stringBuilder.append(this.namespacePrefix);
/*     */       }
/* 221 */       stringBuilder.append("Throwable>");
/* 222 */       stringBuilder.append(this.eol);
/*     */     } 
/*     */     
/* 225 */     if (this.locationInfo) {
/* 226 */       StackTraceElement stackTraceElement = paramLogEvent.getSource();
/* 227 */       stringBuilder.append(this.indent2);
/* 228 */       stringBuilder.append('<');
/* 229 */       if (!this.complete) {
/* 230 */         stringBuilder.append(this.namespacePrefix);
/*     */       }
/* 232 */       stringBuilder.append("LocationInfo class=\"");
/* 233 */       stringBuilder.append(Transform.escapeHtmlTags(stackTraceElement.getClassName()));
/* 234 */       stringBuilder.append("\" method=\"");
/* 235 */       stringBuilder.append(Transform.escapeHtmlTags(stackTraceElement.getMethodName()));
/* 236 */       stringBuilder.append("\" file=\"");
/* 237 */       stringBuilder.append(Transform.escapeHtmlTags(stackTraceElement.getFileName()));
/* 238 */       stringBuilder.append("\" line=\"");
/* 239 */       stringBuilder.append(stackTraceElement.getLineNumber());
/* 240 */       stringBuilder.append("\"/>");
/* 241 */       stringBuilder.append(this.eol);
/*     */     } 
/*     */     
/* 244 */     if (this.properties && paramLogEvent.getContextMap().size() > 0) {
/* 245 */       stringBuilder.append(this.indent2);
/* 246 */       stringBuilder.append('<');
/* 247 */       if (!this.complete) {
/* 248 */         stringBuilder.append(this.namespacePrefix);
/*     */       }
/* 250 */       stringBuilder.append("Properties>");
/* 251 */       stringBuilder.append(this.eol);
/* 252 */       for (Map.Entry entry : paramLogEvent.getContextMap().entrySet()) {
/* 253 */         stringBuilder.append(this.indent3);
/* 254 */         stringBuilder.append('<');
/* 255 */         if (!this.complete) {
/* 256 */           stringBuilder.append(this.namespacePrefix);
/*     */         }
/* 258 */         stringBuilder.append("Data name=\"");
/* 259 */         stringBuilder.append(Transform.escapeHtmlTags((String)entry.getKey()));
/* 260 */         stringBuilder.append("\" value=\"");
/* 261 */         stringBuilder.append(Transform.escapeHtmlTags(String.valueOf(entry.getValue())));
/* 262 */         stringBuilder.append("\"/>");
/* 263 */         stringBuilder.append(this.eol);
/*     */       } 
/* 265 */       stringBuilder.append(this.indent2);
/* 266 */       stringBuilder.append("</");
/* 267 */       if (!this.complete) {
/* 268 */         stringBuilder.append(this.namespacePrefix);
/*     */       }
/* 270 */       stringBuilder.append("Properties>");
/* 271 */       stringBuilder.append(this.eol);
/*     */     } 
/*     */     
/* 274 */     stringBuilder.append(this.indent1);
/* 275 */     stringBuilder.append("</");
/* 276 */     if (!this.complete) {
/* 277 */       stringBuilder.append(this.namespacePrefix);
/*     */     }
/* 279 */     stringBuilder.append("Event>");
/* 280 */     stringBuilder.append(this.eol);
/*     */     
/* 282 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getHeader() {
/* 296 */     if (!this.complete) {
/* 297 */       return null;
/*     */     }
/* 299 */     StringBuilder stringBuilder = new StringBuilder();
/* 300 */     stringBuilder.append("<?xml version=\"1.0\" encoding=\"");
/* 301 */     stringBuilder.append(getCharset().name());
/* 302 */     stringBuilder.append("\"?>");
/* 303 */     stringBuilder.append(this.eol);
/*     */     
/* 305 */     stringBuilder.append('<');
/* 306 */     stringBuilder.append("Events");
/* 307 */     stringBuilder.append(" xmlns=\"http://logging.apache.org/log4j/2.0/events\">");
/* 308 */     stringBuilder.append(this.eol);
/* 309 */     return stringBuilder.toString().getBytes(getCharset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getFooter() {
/* 320 */     if (!this.complete) {
/* 321 */       return null;
/*     */     }
/* 323 */     return ("</Events>" + this.eol).getBytes(getCharset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getContentFormat() {
/* 335 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*     */     
/* 337 */     hashMap.put("xsd", "log4j-events.xsd");
/* 338 */     hashMap.put("version", "2.0");
/* 339 */     return (Map)hashMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 347 */     return "text/xml; charset=" + String.valueOf(getCharset());
/*     */   }
/*     */   
/*     */   private static String getEventLevel(LogEvent paramLogEvent) {
/* 351 */     Marker marker = paramLogEvent.getMarker();
/* 352 */     if (marker != null && marker.isInstanceOf("FATAL")) {
/* 353 */       return String.valueOf(Level.FATAL);
/*     */     }
/* 355 */     return String.valueOf(paramLogEvent.getLevel());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static LegacyXMLLayout createLayout(@PluginAttribute("locationInfo") boolean paramBoolean1, @PluginAttribute("properties") boolean paramBoolean2, @PluginAttribute("complete") boolean paramBoolean3, @PluginAttribute("compact") boolean paramBoolean4, @PluginAttribute("namespacePrefix") String paramString, @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset paramCharset) {
/* 377 */     return new LegacyXMLLayout(paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4, paramString, paramCharset);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\logging\plugins\LegacyXMLLayout.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */