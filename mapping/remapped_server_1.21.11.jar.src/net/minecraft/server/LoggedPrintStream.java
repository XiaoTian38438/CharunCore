/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class LoggedPrintStream
/*    */   extends PrintStream
/*    */ {
/* 12 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   protected final String name;
/*    */   
/*    */   public LoggedPrintStream(String paramString, OutputStream paramOutputStream) {
/* 17 */     super(paramOutputStream, false, StandardCharsets.UTF_8);
/* 18 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */   
/*    */   public void println(String paramString) {
/* 23 */     logLine(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public void println(Object paramObject) {
/* 28 */     logLine(String.valueOf(paramObject));
/*    */   }
/*    */   
/*    */   protected void logLine(String paramString) {
/* 32 */     LOGGER.info("[{}]: {}", this.name, paramString);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\LoggedPrintStream.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */