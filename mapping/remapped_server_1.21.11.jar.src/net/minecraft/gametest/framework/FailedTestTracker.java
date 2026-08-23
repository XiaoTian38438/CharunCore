/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ 
/*    */ public class FailedTestTracker
/*    */ {
/* 10 */   private static final Set<Holder.Reference<GameTestInstance>> LAST_FAILED_TESTS = Sets.newHashSet();
/*    */   
/*    */   public static Stream<Holder.Reference<GameTestInstance>> getLastFailedTests() {
/* 13 */     return LAST_FAILED_TESTS.stream();
/*    */   }
/*    */   
/*    */   public static void rememberFailedTest(Holder.Reference<GameTestInstance> paramReference) {
/* 17 */     LAST_FAILED_TESTS.add(paramReference);
/*    */   }
/*    */   
/*    */   public static void forgetFailedTests() {
/* 21 */     LAST_FAILED_TESTS.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\FailedTestTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */