/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class ThreadSafeLegacyRandomSource
/*    */   implements BitRandomSource
/*    */ {
/*    */   private static final int MODULUS_BITS = 48;
/*    */   private static final long MODULUS_MASK = 281474976710655L;
/*    */   private static final long MULTIPLIER = 25214903917L;
/*    */   private static final long INCREMENT = 11L;
/* 19 */   private final AtomicLong seed = new AtomicLong();
/* 20 */   private final MarsagliaPolarGaussian gaussianSource = new MarsagliaPolarGaussian(this);
/*    */   
/*    */   public ThreadSafeLegacyRandomSource(long paramLong) {
/* 23 */     setSeed(paramLong);
/*    */   }
/*    */ 
/*    */   
/*    */   public RandomSource fork() {
/* 28 */     return new ThreadSafeLegacyRandomSource(nextLong());
/*    */   }
/*    */ 
/*    */   
/*    */   public PositionalRandomFactory forkPositional() {
/* 33 */     return new LegacyRandomSource.LegacyPositionalRandomFactory(nextLong());
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSeed(long paramLong) {
/* 38 */     this.seed.set((paramLong ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int next(int paramInt) {
/*    */     while (true) {
/* 46 */       long l1 = this.seed.get();
/* 47 */       long l2 = l1 * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
/* 48 */       if (this.seed.compareAndSet(l1, l2))
/* 49 */         return (int)(l2 >>> 48 - paramInt); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public double nextGaussian() {
/* 54 */     return this.gaussianSource.nextGaussian();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\ThreadSafeLegacyRandomSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */