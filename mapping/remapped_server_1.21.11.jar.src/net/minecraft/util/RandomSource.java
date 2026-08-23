/*    */ package net.minecraft.util;
/*    */ 
/*    */ import io.netty.util.internal.ThreadLocalRandom;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.PositionalRandomFactory;
/*    */ import net.minecraft.world.level.levelgen.RandomSupport;
/*    */ import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
/*    */ import net.minecraft.world.level.levelgen.ThreadSafeLegacyRandomSource;
/*    */ 
/*    */ public interface RandomSource {
/*    */   static RandomSource create() {
/* 12 */     return create(RandomSupport.generateUniqueSeed());
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public static final double GAUSSIAN_SPREAD_FACTOR = 2.297D;
/*    */   
/*    */   @Deprecated
/*    */   static RandomSource createThreadSafe() {
/* 20 */     return (RandomSource)new ThreadSafeLegacyRandomSource(RandomSupport.generateUniqueSeed());
/*    */   }
/*    */   
/*    */   static RandomSource create(long paramLong) {
/* 24 */     return (RandomSource)new LegacyRandomSource(paramLong);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static RandomSource createNewThreadLocalInstance() {
/* 32 */     return (RandomSource)new SingleThreadedRandomSource(ThreadLocalRandom.current().nextLong());
/*    */   }
/*    */ 
/*    */   
/*    */   RandomSource fork();
/*    */ 
/*    */   
/*    */   PositionalRandomFactory forkPositional();
/*    */ 
/*    */   
/*    */   void setSeed(long paramLong);
/*    */ 
/*    */   
/*    */   int nextInt();
/*    */ 
/*    */   
/*    */   int nextInt(int paramInt);
/*    */ 
/*    */   
/*    */   default int nextIntBetweenInclusive(int paramInt1, int paramInt2) {
/* 52 */     return nextInt(paramInt2 - paramInt1 + 1) + paramInt1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   long nextLong();
/*    */ 
/*    */ 
/*    */   
/*    */   boolean nextBoolean();
/*    */ 
/*    */ 
/*    */   
/*    */   float nextFloat();
/*    */ 
/*    */   
/*    */   double nextDouble();
/*    */ 
/*    */   
/*    */   double nextGaussian();
/*    */ 
/*    */   
/*    */   default double triangle(double paramDouble1, double paramDouble2) {
/* 75 */     return paramDouble1 + paramDouble2 * (nextDouble() - nextDouble());
/*    */   }
/*    */   
/*    */   default float triangle(float paramFloat1, float paramFloat2) {
/* 79 */     return paramFloat1 + paramFloat2 * (nextFloat() - nextFloat());
/*    */   }
/*    */   
/*    */   default void consumeCount(int paramInt) {
/* 83 */     for (byte b = 0; b < paramInt; b++) {
/* 84 */       nextInt();
/*    */     }
/*    */   }
/*    */   
/*    */   default int nextInt(int paramInt1, int paramInt2) {
/* 89 */     if (paramInt1 >= paramInt2) {
/* 90 */       throw new IllegalArgumentException("bound - origin is non positive");
/*    */     }
/* 92 */     return paramInt1 + nextInt(paramInt2 - paramInt1);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\RandomSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */