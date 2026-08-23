/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class LinearCongruentialGenerator {
/*    */   private static final long MULTIPLIER = 6364136223846793005L;
/*    */   private static final long INCREMENT = 1442695040888963407L;
/*    */   
/*    */   public static long next(long paramLong1, long paramLong2) {
/*  8 */     paramLong1 *= paramLong1 * 6364136223846793005L + 1442695040888963407L;
/*  9 */     paramLong1 += paramLong2;
/* 10 */     return paramLong1;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\LinearCongruentialGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */