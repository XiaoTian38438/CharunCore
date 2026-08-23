/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.OutputStream;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DebugLoggedPrintStream
/*    */   extends LoggedPrintStream
/*    */ {
/* 10 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public DebugLoggedPrintStream(String paramString, OutputStream paramOutputStream) {
/* 13 */     super(paramString, paramOutputStream);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void logLine(String paramString) {
/* 18 */     StackTraceElement[] arrayOfStackTraceElement = Thread.currentThread().getStackTrace();
/* 19 */     StackTraceElement stackTraceElement = arrayOfStackTraceElement[Math.min(3, arrayOfStackTraceElement.length)];
/* 20 */     LOGGER.info("[{}]@.({}:{}): {}", new Object[] { this.name, stackTraceElement.getFileName(), Integer.valueOf(stackTraceElement.getLineNumber()), paramString });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\DebugLoggedPrintStream.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */