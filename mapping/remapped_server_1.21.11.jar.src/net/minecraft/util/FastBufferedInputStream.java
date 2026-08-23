/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FastBufferedInputStream
/*    */   extends InputStream
/*    */ {
/*    */   private static final int DEFAULT_BUFFER_SIZE = 8192;
/*    */   private final InputStream in;
/*    */   private final byte[] buffer;
/*    */   private int limit;
/*    */   private int position;
/*    */   
/*    */   public FastBufferedInputStream(InputStream paramInputStream) {
/* 21 */     this(paramInputStream, 8192);
/*    */   }
/*    */   
/*    */   public FastBufferedInputStream(InputStream paramInputStream, int paramInt) {
/* 25 */     this.in = paramInputStream;
/* 26 */     this.buffer = new byte[paramInt];
/*    */   }
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 31 */     if (this.position >= this.limit) {
/* 32 */       fill();
/* 33 */       if (this.position >= this.limit) {
/* 34 */         return -1;
/*    */       }
/*    */     } 
/* 37 */     return Byte.toUnsignedInt(this.buffer[this.position++]);
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
/* 42 */     int i = bytesInBuffer();
/* 43 */     if (i <= 0) {
/* 44 */       if (paramInt2 >= this.buffer.length) {
/* 45 */         return this.in.read(paramArrayOfbyte, paramInt1, paramInt2);
/*    */       }
/* 47 */       fill();
/* 48 */       i = bytesInBuffer();
/* 49 */       if (i <= 0) {
/* 50 */         return -1;
/*    */       }
/*    */     } 
/* 53 */     if (paramInt2 > i) {
/* 54 */       paramInt2 = i;
/*    */     }
/* 56 */     System.arraycopy(this.buffer, this.position, paramArrayOfbyte, paramInt1, paramInt2);
/* 57 */     this.position += paramInt2;
/* 58 */     return paramInt2;
/*    */   }
/*    */ 
/*    */   
/*    */   public long skip(long paramLong) throws IOException {
/* 63 */     if (paramLong <= 0L) {
/* 64 */       return 0L;
/*    */     }
/* 66 */     long l = bytesInBuffer();
/* 67 */     if (l <= 0L) {
/* 68 */       return this.in.skip(paramLong);
/*    */     }
/* 70 */     if (paramLong > l) {
/* 71 */       paramLong = l;
/*    */     }
/* 73 */     this.position = (int)(this.position + paramLong);
/* 74 */     return paramLong;
/*    */   }
/*    */ 
/*    */   
/*    */   public int available() throws IOException {
/* 79 */     return bytesInBuffer() + this.in.available();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 84 */     this.in.close();
/*    */   }
/*    */   
/*    */   private int bytesInBuffer() {
/* 88 */     return this.limit - this.position;
/*    */   }
/*    */   
/*    */   private void fill() throws IOException {
/* 92 */     this.limit = 0;
/* 93 */     this.position = 0;
/* 94 */     int i = this.in.read(this.buffer, 0, this.buffer.length);
/* 95 */     if (i > 0)
/* 96 */       this.limit = i; 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\FastBufferedInputStream.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */