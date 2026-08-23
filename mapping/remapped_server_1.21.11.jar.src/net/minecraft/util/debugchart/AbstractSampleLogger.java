/*    */ package net.minecraft.util.debugchart;
/*    */ 
/*    */ public abstract class AbstractSampleLogger implements SampleLogger {
/*    */   protected final long[] defaults;
/*    */   protected final long[] sample;
/*    */   
/*    */   protected AbstractSampleLogger(int paramInt, long[] paramArrayOflong) {
/*  8 */     if (paramArrayOflong.length != paramInt) {
/*  9 */       throw new IllegalArgumentException("defaults have incorrect length of " + paramArrayOflong.length);
/*    */     }
/* 11 */     this.sample = new long[paramInt];
/* 12 */     this.defaults = paramArrayOflong;
/*    */   }
/*    */ 
/*    */   
/*    */   public void logFullSample(long[] paramArrayOflong) {
/* 17 */     System.arraycopy(paramArrayOflong, 0, this.sample, 0, paramArrayOflong.length);
/* 18 */     useSample();
/* 19 */     resetSample();
/*    */   }
/*    */ 
/*    */   
/*    */   public void logSample(long paramLong) {
/* 24 */     this.sample[0] = paramLong;
/* 25 */     useSample();
/* 26 */     resetSample();
/*    */   }
/*    */ 
/*    */   
/*    */   public void logPartialSample(long paramLong, int paramInt) {
/* 31 */     if (paramInt < 1 || paramInt >= this.sample.length) {
/* 32 */       throw new IndexOutOfBoundsException("" + paramInt + " out of bounds for dimensions " + paramInt);
/*    */     }
/* 34 */     this.sample[paramInt] = paramLong;
/*    */   }
/*    */   
/*    */   protected abstract void useSample();
/*    */   
/*    */   protected void resetSample() {
/* 40 */     System.arraycopy(this.defaults, 0, this.sample, 0, this.defaults.length);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\debugchart\AbstractSampleLogger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */