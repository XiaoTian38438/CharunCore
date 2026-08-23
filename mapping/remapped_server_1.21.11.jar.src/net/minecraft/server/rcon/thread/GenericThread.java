/*    */ package net.minecraft.server.rcon.thread;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import net.minecraft.DefaultUncaughtExceptionHandlerWithName;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public abstract class GenericThread
/*    */   implements Runnable
/*    */ {
/* 11 */   private static final Logger LOGGER = LogUtils.getLogger();
/* 12 */   private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
/*    */   private static final int MAX_STOP_WAIT = 5;
/*    */   protected volatile boolean running;
/*    */   protected final String name;
/*    */   protected Thread thread;
/*    */   
/*    */   protected GenericThread(String paramString) {
/* 19 */     this.name = paramString;
/*    */   }
/*    */   
/*    */   public synchronized boolean start() {
/* 23 */     if (this.running) {
/* 24 */       return true;
/*    */     }
/* 26 */     this.running = true;
/* 27 */     this.thread = new Thread(this, this.name + " #" + this.name);
/* 28 */     this.thread.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandlerWithName(LOGGER));
/* 29 */     this.thread.start();
/* 30 */     LOGGER.info("Thread {} started", this.name);
/* 31 */     return true;
/*    */   }
/*    */   
/*    */   public synchronized void stop() {
/* 35 */     this.running = false;
/* 36 */     if (null == this.thread) {
/*    */       return;
/*    */     }
/* 39 */     byte b = 0;
/* 40 */     while (this.thread.isAlive()) {
/*    */       
/*    */       try {
/* 43 */         this.thread.join(1000L);
/* 44 */         b++;
/* 45 */         if (b >= 5) {
/*    */ 
/*    */ 
/*    */           
/* 49 */           LOGGER.warn("Waited {} seconds attempting force stop!", Integer.valueOf(b)); continue;
/* 50 */         }  if (this.thread.isAlive()) {
/* 51 */           LOGGER.warn("Thread {} ({}) failed to exit after {} second(s)", new Object[] { this, this.thread.getState(), Integer.valueOf(b), new Exception("Stack:") });
/*    */           
/* 53 */           this.thread.interrupt();
/*    */         } 
/* 55 */       } catch (InterruptedException interruptedException) {}
/*    */     } 
/*    */ 
/*    */     
/* 59 */     LOGGER.info("Thread {} stopped", this.name);
/* 60 */     this.thread = null;
/*    */   }
/*    */   
/*    */   public boolean isRunning() {
/* 64 */     return this.running;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\rcon\thread\GenericThread.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */