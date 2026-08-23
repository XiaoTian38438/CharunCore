/*    */ package com.mojang.logging.plugins;
/*    */ 
/*    */ import com.mojang.logging.LogListeners;
/*    */ import java.io.Serializable;
/*    */ import java.util.Objects;
/*    */ import javax.annotation.Nullable;
/*    */ import org.apache.logging.log4j.core.Filter;
/*    */ import org.apache.logging.log4j.core.Layout;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*    */ import org.apache.logging.log4j.core.config.Property;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*    */ 
/*    */ @Plugin(name = "Listener", category = "Core", elementType = "appender", printObject = true)
/*    */ public class ListenerLogAppender
/*    */   extends AbstractAppender {
/*    */   private final LogListeners.Target output;
/*    */   
/*    */   public ListenerLogAppender(String paramString, Filter paramFilter, Layout<? extends Serializable> paramLayout, boolean paramBoolean, LogListeners.Target paramTarget) {
/* 24 */     super(paramString, paramFilter, paramLayout, paramBoolean, Property.EMPTY_ARRAY);
/* 25 */     this.output = paramTarget;
/*    */   }
/*    */ 
/*    */   
/*    */   public void append(LogEvent paramLogEvent) {
/* 30 */     this.output.post(getLayout(), paramLogEvent);
/*    */   }
/*    */   @PluginFactory
/*    */   @Nullable
/*    */   public static ListenerLogAppender createAppender(@PluginAttribute("name") @Nullable String paramString1, @PluginAttribute("ignoreExceptions") String paramString2, @PluginElement("Layout") @Nullable Layout<? extends Serializable> paramLayout, @PluginElement("Filters") Filter paramFilter, @PluginAttribute("target") @Nullable String paramString3) {
/*    */     PatternLayout patternLayout;
/* 36 */     boolean bool = Boolean.parseBoolean(paramString2);
/*    */     
/* 38 */     if (paramString1 == null) {
/* 39 */       LOGGER.error("No name provided for ListenerLogAppender");
/* 40 */       return null;
/*    */     } 
/*    */     
/* 43 */     LogListeners.Target target = LogListeners.getOrCreateTarget(Objects.<String>requireNonNullElse(paramString3, paramString1));
/*    */     
/* 45 */     if (paramLayout == null) {
/* 46 */       patternLayout = PatternLayout.newBuilder().build();
/*    */     }
/*    */     
/* 49 */     return new ListenerLogAppender(paramString1, paramFilter, (Layout<? extends Serializable>)patternLayout, bool, target);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\logging\plugins\ListenerLogAppender.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */