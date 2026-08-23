/*    */ package com.mojang.logging;
/*    */ import java.util.function.Supplier;
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.core.LifeCycle;
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.LoggerConfig;
/*    */ import org.apache.logging.log4j.spi.LoggerContext;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.slf4j.Marker;
/*    */ import org.slf4j.MarkerFactory;
/*    */ import org.slf4j.event.Level;
/*    */ 
/*    */ public class LogUtils {
/*    */   public static final String FATAL_MARKER_ID = "FATAL";
/* 18 */   public static final Marker FATAL_MARKER = MarkerFactory.getMarker("FATAL");
/* 19 */   private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
/*    */   
/*    */   public static boolean isLoggerActive() {
/* 22 */     LoggerContext loggerContext = LogManager.getContext();
/* 23 */     if (loggerContext instanceof LifeCycle) { LifeCycle lifeCycle = (LifeCycle)loggerContext;
/* 24 */       return !lifeCycle.isStopped(); }
/*    */     
/* 26 */     return true;
/*    */   }
/*    */   
/*    */   public static void configureRootLoggingLevel(Level paramLevel) {
/* 30 */     LoggerContext loggerContext = (LoggerContext)LogManager.getContext(false);
/* 31 */     Configuration configuration = loggerContext.getConfiguration();
/* 32 */     LoggerConfig loggerConfig = configuration.getLoggerConfig("");
/* 33 */     loggerConfig.setLevel(convertLevel(paramLevel));
/* 34 */     loggerContext.updateLoggers();
/*    */   }
/*    */   
/*    */   private static Level convertLevel(Level paramLevel) {
/* 38 */     switch (paramLevel) { default: throw new IncompatibleClassChangeError();case INFO: case WARN: case DEBUG: case ERROR: case TRACE: break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 43 */       Level.TRACE;
/*    */   }
/*    */ 
/*    */   
/*    */   public static Object defer(final Supplier<Object> result) {
/*    */     class ToString
/*    */     {
/*    */       public String toString() {
/* 51 */         return result.get().toString();
/*    */       }
/*    */     };
/*    */     
/* 55 */     return new ToString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Logger getLogger() {
/* 62 */     return LoggerFactory.getLogger(STACK_WALKER.getCallerClass());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\logging\LogUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */