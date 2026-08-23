/*    */ package com.mojang.logging.plugins;
/*    */ 
/*    */ import com.mojang.logging.LogQueues;
/*    */ import java.io.Serializable;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import org.apache.logging.log4j.core.Filter;
/*    */ import org.apache.logging.log4j.core.Layout;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*    */ 
/*    */ @Plugin(name = "Queue", category = "Core", elementType = "appender", printObject = true)
/*    */ public class QueueLogAppender
/*    */   extends AbstractAppender
/*    */ {
/*    */   private static final int MAX_CAPACITY = 250;
/*    */   private final BlockingQueue<String> queue;
/*    */   
/*    */   public QueueLogAppender(String paramString, Filter paramFilter, Layout<? extends Serializable> paramLayout, boolean paramBoolean, BlockingQueue<String> paramBlockingQueue) {
/* 25 */     super(paramString, paramFilter, paramLayout, paramBoolean);
/* 26 */     this.queue = paramBlockingQueue;
/*    */   }
/*    */ 
/*    */   
/*    */   public void append(LogEvent paramLogEvent) {
/* 31 */     if (this.queue.size() >= 250) {
/* 32 */       this.queue.clear();
/*    */     }
/* 34 */     this.queue.add(getLayout().toSerializable(paramLogEvent).toString());
/*    */   }
/*    */   @PluginFactory
/*    */   public static QueueLogAppender createAppender(@PluginAttribute("name") String paramString1, @PluginAttribute("ignoreExceptions") String paramString2, @PluginElement("Layout") Layout<? extends Serializable> paramLayout, @PluginElement("Filters") Filter paramFilter, @PluginAttribute("target") String paramString3) {
/*    */     PatternLayout patternLayout;
/* 39 */     boolean bool = Boolean.parseBoolean(paramString2);
/*    */     
/* 41 */     if (paramString1 == null) {
/* 42 */       LOGGER.error("No name provided for QueueLogAppender");
/* 43 */       return null;
/*    */     } 
/*    */     
/* 46 */     BlockingQueue<String> blockingQueue = LogQueues.getOrCreateQueue(Objects.<String>requireNonNullElse(paramString3, paramString1));
/*    */     
/* 48 */     if (paramLayout == null) {
/* 49 */       patternLayout = PatternLayout.newBuilder().build();
/*    */     }
/*    */     
/* 52 */     return new QueueLogAppender(paramString1, paramFilter, (Layout<? extends Serializable>)patternLayout, bool, blockingQueue);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\logging\plugins\QueueLogAppender.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */