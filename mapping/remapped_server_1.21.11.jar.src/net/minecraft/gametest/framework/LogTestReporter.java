/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class LogTestReporter implements TestReporter {
/*  8 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */   
/*    */   public void onTestFailed(GameTestInfo paramGameTestInfo) {
/* 12 */     String str = paramGameTestInfo.getTestBlockPos().toShortString();
/* 13 */     if (paramGameTestInfo.isRequired()) {
/* 14 */       LOGGER.error("{} failed at {}! {}", new Object[] { paramGameTestInfo.id(), str, Util.describeError(paramGameTestInfo.getError()) });
/*    */     } else {
/* 16 */       LOGGER.warn("(optional) {} failed at {}. {}", new Object[] { paramGameTestInfo.id(), str, Util.describeError(paramGameTestInfo.getError()) });
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onTestSuccess(GameTestInfo paramGameTestInfo) {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\LogTestReporter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */