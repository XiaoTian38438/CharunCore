/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.ThreadingDetector;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LegacyRandomSource
/*    */   implements BitRandomSource
/*    */ {
/*    */   private static final int MODULUS_BITS = 48;
/*    */   private static final long MODULUS_MASK = 281474976710655L;
/*    */   private static final long MULTIPLIER = 25214903917L;
/*    */   private static final long INCREMENT = 11L;
/* 19 */   private final AtomicLong seed = new AtomicLong();
/* 20 */   private final MarsagliaPolarGaussian gaussianSource = new MarsagliaPolarGaussian(this);
/*    */   
/*    */   public LegacyRandomSource(long paramLong) {
/* 23 */     setSeed(paramLong);
/*    */   }
/*    */ 
/*    */   
/*    */   public RandomSource fork() {
/* 28 */     return new LegacyRandomSource(nextLong());
/*    */   }
/*    */ 
/*    */   
/*    */   public PositionalRandomFactory forkPositional() {
/* 33 */     return new LegacyPositionalRandomFactory(nextLong());
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSeed(long paramLong) {
/* 38 */     if (!this.seed.compareAndSet(this.seed.get(), (paramLong ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL)) {
/* 39 */       throw ThreadingDetector.makeThreadingException("LegacyRandomSource", null);
/*    */     }
/* 41 */     this.gaussianSource.reset();
/*    */   }
/*    */ 
/*    */   
/*    */   public int next(int paramInt) {
/* 46 */     long l1 = this.seed.get();
/* 47 */     long l2 = l1 * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
/* 48 */     if (!this.seed.compareAndSet(l1, l2)) {
/* 49 */       throw ThreadingDetector.makeThreadingException("LegacyRandomSource", null);
/*    */     }
/*    */     
/* 52 */     return (int)(l2 >> 48 - paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public double nextGaussian() {
/* 57 */     return this.gaussianSource.nextGaussian();
/*    */   }
/*    */   
/*    */   public static class LegacyPositionalRandomFactory implements PositionalRandomFactory {
/*    */     private final long seed;
/*    */     
/*    */     public LegacyPositionalRandomFactory(long param1Long) {
/* 64 */       this.seed = param1Long;
/*    */     }
/*    */ 
/*    */     
/*    */     public RandomSource at(int param1Int1, int param1Int2, int param1Int3) {
/* 69 */       long l1 = Mth.getSeed(param1Int1, param1Int2, param1Int3);
/* 70 */       long l2 = l1 ^ this.seed;
/* 71 */       return new LegacyRandomSource(l2);
/*    */     }
/*    */ 
/*    */     
/*    */     public RandomSource fromHashOf(String param1String) {
/* 76 */       int i = param1String.hashCode();
/* 77 */       return new LegacyRandomSource(i ^ this.seed);
/*    */     }
/*    */ 
/*    */     
/*    */     public RandomSource fromSeed(long param1Long) {
/* 82 */       return new LegacyRandomSource(param1Long);
/*    */     }
/*    */ 
/*    */     
/*    */     @VisibleForTesting
/*    */     public void parityConfigString(StringBuilder param1StringBuilder) {
/* 88 */       param1StringBuilder.append("LegacyPositionalRandomFactory{").append(this.seed).append("}");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\LegacyRandomSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */