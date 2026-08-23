/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class GaussianSampler {
/*    */   private static final int GAUSSIAN_SAMPLE_RADIUS = 2;
/*    */   private static final int GAUSSIAN_SAMPLE_BREADTH = 6;
/*  9 */   private static final double[] GAUSSIAN_SAMPLE_KERNEL = new double[] { 0.0D, 1.0D, 4.0D, 6.0D, 4.0D, 1.0D, 0.0D };
/*    */   
/*    */   public static <V> void sample(Vec3 paramVec3, Sampler<V> paramSampler, Accumulator<V> paramAccumulator) {
/* 12 */     paramVec3 = paramVec3.subtract(0.5D, 0.5D, 0.5D);
/*    */     
/* 14 */     int i = Mth.floor(paramVec3.x());
/* 15 */     int j = Mth.floor(paramVec3.y());
/* 16 */     int k = Mth.floor(paramVec3.z());
/*    */     
/* 18 */     double d1 = paramVec3.x() - i;
/* 19 */     double d2 = paramVec3.y() - j;
/* 20 */     double d3 = paramVec3.z() - k;
/*    */     
/* 22 */     for (byte b = 0; b < 6; b++) {
/* 23 */       double d = Mth.lerp(d3, GAUSSIAN_SAMPLE_KERNEL[b + 1], GAUSSIAN_SAMPLE_KERNEL[b]);
/* 24 */       int m = k - 2 + b;
/*    */       
/* 26 */       for (byte b1 = 0; b1 < 6; b1++) {
/* 27 */         double d4 = Mth.lerp(d1, GAUSSIAN_SAMPLE_KERNEL[b1 + 1], GAUSSIAN_SAMPLE_KERNEL[b1]);
/* 28 */         int n = i - 2 + b1;
/*    */         
/* 30 */         for (byte b2 = 0; b2 < 6; b2++) {
/* 31 */           double d5 = Mth.lerp(d2, GAUSSIAN_SAMPLE_KERNEL[b2 + 1], GAUSSIAN_SAMPLE_KERNEL[b2]);
/* 32 */           int i1 = j - 2 + b2;
/*    */           
/* 34 */           double d6 = d4 * d5 * d;
/* 35 */           V v = paramSampler.get(n, i1, m);
/* 36 */           paramAccumulator.accumulate(d6, v);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Sampler<V> {
/*    */     V get(int param1Int1, int param1Int2, int param1Int3);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Accumulator<V> {
/*    */     void accumulate(double param1Double, V param1V);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\GaussianSampler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */