/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public interface BitRandomSource
/*    */   extends RandomSource {
/*    */   public static final float FLOAT_MULTIPLIER = 5.9604645E-8F;
/*    */   public static final double DOUBLE_MULTIPLIER = 1.1102230246251565E-16D;
/*    */   
/*    */   int next(int paramInt);
/*    */   
/*    */   default int nextInt() {
/* 13 */     return next(32);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default int nextInt(int paramInt) {
/* 21 */     if (paramInt <= 0) {
/* 22 */       throw new IllegalArgumentException("Bound must be positive");
/*    */     }
/*    */     
/* 25 */     if ((paramInt & paramInt - 1) == 0)
/*    */     {
/* 27 */       return (int)(paramInt * next(31) >> 31L);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     while (true) {
/* 33 */       int i = next(31);
/* 34 */       int j = i % paramInt;
/* 35 */       if (i - j + paramInt - 1 >= 0) {
/* 36 */         return j;
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   default long nextLong() {
/* 43 */     int i = next(32);
/* 44 */     int j = next(32);
/* 45 */     long l = i << 32L;
/* 46 */     return l + j;
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean nextBoolean() {
/* 51 */     return (next(1) != 0);
/*    */   }
/*    */ 
/*    */   
/*    */   default float nextFloat() {
/* 56 */     return next(24) * 5.9604645E-8F;
/*    */   }
/*    */ 
/*    */   
/*    */   default double nextDouble() {
/* 61 */     int i = next(26);
/* 62 */     int j = next(27);
/* 63 */     long l = (i << 27L) + j;
/* 64 */     return l * 1.1102230246251565E-16D;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\BitRandomSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */