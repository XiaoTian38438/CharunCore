/*    */ package com.mojang.logging;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.core.Layout;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.slf4j.event.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Target
/*    */ {
/* 25 */   private volatile List<LogListeners.Listener> listeners = List.of();
/*    */   
/*    */   private synchronized void addListener(LogListeners.Listener paramListener) {
/* 28 */     ArrayList<LogListeners.Listener> arrayList = new ArrayList(this.listeners.size() + 1);
/* 29 */     arrayList.addAll(this.listeners);
/* 30 */     arrayList.add(paramListener);
/* 31 */     this.listeners = arrayList;
/*    */   }
/*    */   
/*    */   public void post(Layout<? extends Serializable> paramLayout, LogEvent paramLogEvent) {
/* 35 */     if (this.listeners.isEmpty()) {
/*    */       return;
/*    */     }
/* 38 */     String str = paramLayout.toSerializable(paramLogEvent).toString();
/* 39 */     Level level = log4jToSlf4jLevel(paramLogEvent.getLevel());
/* 40 */     for (LogListeners.Listener listener : this.listeners) {
/* 41 */       listener.accept(str, level);
/*    */     }
/*    */   }
/*    */   
/*    */   private static Level log4jToSlf4jLevel(Level paramLevel) {
/* 46 */     if (paramLevel == Level.ERROR)
/* 47 */       return Level.ERROR; 
/* 48 */     if (paramLevel == Level.WARN)
/* 49 */       return Level.WARN; 
/* 50 */     if (paramLevel == Level.INFO)
/* 51 */       return Level.INFO; 
/* 52 */     if (paramLevel == Level.DEBUG)
/* 53 */       return Level.DEBUG; 
/* 54 */     if (paramLevel == Level.TRACE) {
/* 55 */       return Level.TRACE;
/*    */     }
/* 57 */     return Level.INFO;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\logging\LogListeners$Target.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */