/*    */ package net.minecraft.core;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class QuartPos
/*    */ {
/*    */   public static final int BITS = 2;
/*    */   public static final int SIZE = 4;
/*    */   public static final int MASK = 3;
/*    */   private static final int SECTION_TO_QUARTS_BITS = 2;
/*    */   
/*    */   public static int fromBlock(int paramInt) {
/* 14 */     return paramInt >> 2;
/*    */   }
/*    */   
/*    */   public static int quartLocal(int paramInt) {
/* 18 */     return paramInt & 0x3;
/*    */   }
/*    */   
/*    */   public static int toBlock(int paramInt) {
/* 22 */     return paramInt << 2;
/*    */   }
/*    */   
/*    */   public static int fromSection(int paramInt) {
/* 26 */     return paramInt << 2;
/*    */   }
/*    */   
/*    */   public static int toSection(int paramInt) {
/* 30 */     return paramInt >> 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\QuartPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */