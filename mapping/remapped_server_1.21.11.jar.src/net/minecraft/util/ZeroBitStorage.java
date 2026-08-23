/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.function.IntConsumer;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class ZeroBitStorage
/*    */   implements BitStorage {
/*  9 */   public static final long[] RAW = new long[0];
/*    */   
/*    */   private final int size;
/*    */   
/*    */   public ZeroBitStorage(int paramInt) {
/* 14 */     this.size = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getAndSet(int paramInt1, int paramInt2) {
/* 19 */     Validate.inclusiveBetween(0L, (this.size - 1), paramInt1);
/* 20 */     Validate.inclusiveBetween(0L, 0L, paramInt2);
/* 21 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(int paramInt1, int paramInt2) {
/* 26 */     Validate.inclusiveBetween(0L, (this.size - 1), paramInt1);
/* 27 */     Validate.inclusiveBetween(0L, 0L, paramInt2);
/*    */   }
/*    */ 
/*    */   
/*    */   public int get(int paramInt) {
/* 32 */     Validate.inclusiveBetween(0L, (this.size - 1), paramInt);
/* 33 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public long[] getRaw() {
/* 38 */     return RAW;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSize() {
/* 43 */     return this.size;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBits() {
/* 48 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void getAll(IntConsumer paramIntConsumer) {
/* 53 */     for (byte b = 0; b < this.size; b++) {
/* 54 */       paramIntConsumer.accept(0);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void unpack(int[] paramArrayOfint) {
/* 60 */     Arrays.fill(paramArrayOfint, 0, this.size, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public BitStorage copy() {
/* 65 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\ZeroBitStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */