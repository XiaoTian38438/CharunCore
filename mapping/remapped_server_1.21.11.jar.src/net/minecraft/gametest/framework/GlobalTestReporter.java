/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ public class GlobalTestReporter {
/*  4 */   private static TestReporter DELEGATE = new LogTestReporter();
/*    */   
/*    */   public static void replaceWith(TestReporter paramTestReporter) {
/*  7 */     DELEGATE = paramTestReporter;
/*    */   }
/*    */   
/*    */   public static void onTestFailed(GameTestInfo paramGameTestInfo) {
/* 11 */     DELEGATE.onTestFailed(paramGameTestInfo);
/*    */   }
/*    */   
/*    */   public static void onTestSuccess(GameTestInfo paramGameTestInfo) {
/* 15 */     DELEGATE.onTestSuccess(paramGameTestInfo);
/*    */   }
/*    */   
/*    */   public static void finish() {
/* 19 */     DELEGATE.finish();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\GlobalTestReporter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */