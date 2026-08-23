/*    */ package net.minecraft;
/*    */ 
/*    */ public class ReportedException extends RuntimeException {
/*    */   private final CrashReport report;
/*    */   
/*    */   public ReportedException(CrashReport paramCrashReport) {
/*  7 */     this.report = paramCrashReport;
/*    */   }
/*    */   
/*    */   public CrashReport getReport() {
/* 11 */     return this.report;
/*    */   }
/*    */ 
/*    */   
/*    */   public Throwable getCause() {
/* 16 */     return this.report.getException();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 21 */     return this.report.getTitle();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\ReportedException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */