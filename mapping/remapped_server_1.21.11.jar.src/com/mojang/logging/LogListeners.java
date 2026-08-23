/*    */ package com.mojang.logging;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.core.Layout;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.slf4j.event.Level;
/*    */ 
/*    */ public class LogListeners {
/* 14 */   private static final Map<String, Target> TARGETS = new ConcurrentHashMap<>();
/*    */   
/*    */   public static Target getOrCreateTarget(String paramString) {
/* 17 */     return TARGETS.computeIfAbsent(paramString, paramString -> new Target());
/*    */   }
/*    */   
/*    */   public static void addListener(String paramString, Listener paramListener) {
/* 21 */     getOrCreateTarget(paramString).addListener(paramListener);
/*    */   }
/*    */   
/*    */   public static class Target {
/* 25 */     private volatile List<LogListeners.Listener> listeners = List.of();
/*    */     
/*    */     private synchronized void addListener(LogListeners.Listener param1Listener) {
/* 28 */       ArrayList<LogListeners.Listener> arrayList = new ArrayList(this.listeners.size() + 1);
/* 29 */       arrayList.addAll(this.listeners);
/* 30 */       arrayList.add(param1Listener);
/* 31 */       this.listeners = arrayList;
/*    */     }
/*    */     
/*    */     public void post(Layout<? extends Serializable> param1Layout, LogEvent param1LogEvent) {
/* 35 */       if (this.listeners.isEmpty()) {
/*    */         return;
/*    */       }
/* 38 */       String str = param1Layout.toSerializable(param1LogEvent).toString();
/* 39 */       Level level = log4jToSlf4jLevel(param1LogEvent.getLevel());
/* 40 */       for (LogListeners.Listener listener : this.listeners) {
/* 41 */         listener.accept(str, level);
/*    */       }
/*    */     }
/*    */     
/*    */     private static Level log4jToSlf4jLevel(Level param1Level) {
/* 46 */       if (param1Level == Level.ERROR)
/* 47 */         return Level.ERROR; 
/* 48 */       if (param1Level == Level.WARN)
/* 49 */         return Level.WARN; 
/* 50 */       if (param1Level == Level.INFO)
/* 51 */         return Level.INFO; 
/* 52 */       if (param1Level == Level.DEBUG)
/* 53 */         return Level.DEBUG; 
/* 54 */       if (param1Level == Level.TRACE) {
/* 55 */         return Level.TRACE;
/*    */       }
/* 57 */       return Level.INFO;
/*    */     }
/*    */   }
/*    */   
/*    */   public static interface Listener {
/*    */     void accept(String param1String, Level param1Level);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\logging\LogListeners.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */