/*    */ package net.minecraft.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MemoryReserve
/*    */ {
/*    */   private static byte[] reserve;
/*    */   
/*    */   public static void allocate() {
/* 10 */     reserve = new byte[10485760];
/*    */   }
/*    */   
/*    */   public static void release() {
/* 14 */     if (reserve != null) {
/* 15 */       reserve = null;
/*    */       try {
/* 17 */         System.gc();
/* 18 */         System.gc();
/* 19 */         System.gc();
/* 20 */       } catch (Throwable throwable) {}
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\MemoryReserve.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */