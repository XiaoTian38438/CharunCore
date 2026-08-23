/*    */ package net.minecraft.util.debugchart;
/*    */ 
/*    */ public class LocalSampleLogger extends AbstractSampleLogger implements SampleStorage {
/*    */   public static final int CAPACITY = 240;
/*    */   private final long[][] samples;
/*    */   private int start;
/*    */   private int size;
/*    */   
/*    */   public LocalSampleLogger(int paramInt) {
/* 10 */     this(paramInt, new long[paramInt]);
/*    */   }
/*    */   
/*    */   public LocalSampleLogger(int paramInt, long[] paramArrayOflong) {
/* 14 */     super(paramInt, paramArrayOflong);
/* 15 */     this.samples = new long[240][paramInt];
/*    */   }
/*    */ 
/*    */   
/*    */   protected void useSample() {
/* 20 */     int i = wrapIndex(this.start + this.size);
/* 21 */     System.arraycopy(this.sample, 0, this.samples[i], 0, this.sample.length);
/* 22 */     if (this.size < 240) {
/* 23 */       this.size++;
/*    */     } else {
/* 25 */       this.start = wrapIndex(this.start + 1);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int capacity() {
/* 31 */     return this.samples.length;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 36 */     return this.size;
/*    */   }
/*    */ 
/*    */   
/*    */   public long get(int paramInt) {
/* 41 */     return get(paramInt, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public long get(int paramInt1, int paramInt2) {
/* 46 */     if (paramInt1 < 0 || paramInt1 >= this.size) {
/* 47 */       throw new IndexOutOfBoundsException("" + paramInt1 + " out of bounds for length " + paramInt1);
/*    */     }
/* 49 */     long[] arrayOfLong = this.samples[wrapIndex(this.start + paramInt1)];
/* 50 */     if (paramInt2 < 0 || paramInt2 >= arrayOfLong.length) {
/* 51 */       throw new IndexOutOfBoundsException("" + paramInt2 + " out of bounds for dimensions " + paramInt2);
/*    */     }
/* 53 */     return arrayOfLong[paramInt2];
/*    */   }
/*    */   
/*    */   private int wrapIndex(int paramInt) {
/* 57 */     return paramInt % 240;
/*    */   }
/*    */ 
/*    */   
/*    */   public void reset() {
/* 62 */     this.start = 0;
/* 63 */     this.size = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\debugchart\LocalSampleLogger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */