/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ 
/*    */ 
/*    */ public class NbtAccounter
/*    */ {
/*    */   public static final int DEFAULT_NBT_QUOTA = 2097152;
/*    */   public static final int UNCOMPRESSED_NBT_QUOTA = 104857600;
/*    */   private static final int MAX_STACK_DEPTH = 512;
/*    */   private final long quota;
/*    */   private long usage;
/*    */   private final int maxDepth;
/*    */   private int depth;
/*    */   
/*    */   public NbtAccounter(long paramLong, int paramInt) {
/* 17 */     this.quota = paramLong;
/* 18 */     this.maxDepth = paramInt;
/*    */   }
/*    */   
/*    */   public static NbtAccounter create(long paramLong) {
/* 22 */     return new NbtAccounter(paramLong, 512);
/*    */   }
/*    */   
/*    */   public static NbtAccounter defaultQuota() {
/* 26 */     return new NbtAccounter(2097152L, 512);
/*    */   }
/*    */   
/*    */   public static NbtAccounter uncompressedQuota() {
/* 30 */     return new NbtAccounter(104857600L, 512);
/*    */   }
/*    */   
/*    */   public static NbtAccounter unlimitedHeap() {
/* 34 */     return new NbtAccounter(Long.MAX_VALUE, 512);
/*    */   }
/*    */   
/*    */   public void accountBytes(long paramLong1, long paramLong2) {
/* 38 */     accountBytes(paramLong1 * paramLong2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void accountBytes(long paramLong) {
/* 43 */     if (paramLong < 0L) {
/* 44 */       throw new IllegalArgumentException("Tried to account NBT tag with negative size: " + paramLong);
/*    */     }
/* 46 */     if (this.usage + paramLong > this.quota) {
/* 47 */       throw new NbtAccounterException("Tried to read NBT tag that was too big; tried to allocate: " + this.usage + " + " + paramLong + " bytes where max allowed: " + this.quota);
/*    */     }
/* 49 */     this.usage += paramLong;
/*    */   }
/*    */   
/*    */   public void pushDepth() {
/* 53 */     if (this.depth >= this.maxDepth) {
/* 54 */       throw new NbtAccounterException("Tried to read NBT tag with too high complexity, depth > " + this.maxDepth);
/*    */     }
/* 56 */     this.depth++;
/*    */   }
/*    */   
/*    */   public void popDepth() {
/* 60 */     if (this.depth <= 0) {
/* 61 */       throw new NbtAccounterException("NBT-Accounter tried to pop stack-depth at top-level");
/*    */     }
/* 63 */     this.depth--;
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public long getUsage() {
/* 68 */     return this.usage;
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public int getDepth() {
/* 73 */     return this.depth;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\NbtAccounter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */