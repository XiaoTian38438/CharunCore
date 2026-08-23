/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.Semaphore;
/*    */ import java.util.concurrent.locks.Lock;
/*    */ import java.util.concurrent.locks.ReentrantLock;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ThreadingDetector
/*    */ {
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final String name;
/*    */   
/* 23 */   private final Semaphore lock = new Semaphore(1);
/*    */   
/* 25 */   private final Lock stackTraceLock = new ReentrantLock();
/*    */   private volatile Thread threadThatFailedToAcquire;
/*    */   private volatile ReportedException fullException;
/*    */   
/*    */   public ThreadingDetector(String paramString) {
/* 30 */     this.name = paramString;
/*    */   }
/*    */   
/*    */   public void checkAndLock() {
/* 34 */     boolean bool = false;
/*    */     try {
/* 36 */       this.stackTraceLock.lock();
/*    */ 
/*    */       
/* 39 */       if (!this.lock.tryAcquire()) {
/*    */         
/* 41 */         this.threadThatFailedToAcquire = Thread.currentThread();
/* 42 */         bool = true;
/* 43 */         this.stackTraceLock.unlock();
/*    */         
/*    */         try {
/* 46 */           this.lock.acquire();
/* 47 */         } catch (InterruptedException interruptedException) {
/* 48 */           Thread.currentThread().interrupt();
/*    */         } 
/*    */         
/* 51 */         throw this.fullException;
/*    */       } 
/*    */     } finally {
/* 54 */       if (!bool) {
/* 55 */         this.stackTraceLock.unlock();
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public void checkAndUnlock() {
/*    */     try {
/* 62 */       this.stackTraceLock.lock();
/* 63 */       Thread thread = this.threadThatFailedToAcquire;
/* 64 */       if (thread != null) {
/*    */ 
/*    */         
/* 67 */         ReportedException reportedException = makeThreadingException(this.name, thread);
/* 68 */         this.fullException = reportedException;
/* 69 */         this.lock.release();
/* 70 */         throw reportedException;
/*    */       } 
/* 72 */       this.lock.release();
/*    */     } finally {
/*    */       
/* 75 */       this.stackTraceLock.unlock();
/*    */     } 
/*    */   }
/*    */   
/*    */   public static ReportedException makeThreadingException(String paramString, Thread paramThread) {
/* 80 */     String str1 = Stream.<Thread>of(new Thread[] { Thread.currentThread(), paramThread }).filter(Objects::nonNull).map(ThreadingDetector::stackTrace).collect(Collectors.joining("\n"));
/* 81 */     String str2 = "Accessing " + paramString + " from multiple threads";
/* 82 */     CrashReport crashReport = new CrashReport(str2, new IllegalStateException(str2));
/* 83 */     CrashReportCategory crashReportCategory = crashReport.addCategory("Thread dumps");
/* 84 */     crashReportCategory.setDetail("Thread dumps", str1);
/* 85 */     LOGGER.error("Thread dumps: \n{}", str1);
/* 86 */     return new ReportedException(crashReport);
/*    */   }
/*    */   
/*    */   private static String stackTrace(Thread paramThread) {
/* 90 */     return paramThread.getName() + ": \n\tat " + paramThread.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\ThreadingDetector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */