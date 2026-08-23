/*    */ package net.minecraft.gametest;
/*    */ 
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.gametest.framework.GameTestMainUtil;
/*    */ import net.minecraft.obfuscate.DontObfuscate;
/*    */ 
/*    */ public class Main {
/*    */   @DontObfuscate
/*    */   public static void main(String[] paramArrayOfString) throws Exception {
/* 10 */     SharedConstants.tryDetectVersion();
/* 11 */     GameTestMainUtil.runGameTestServer(paramArrayOfString, paramString -> {
/*    */         
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\Main.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */