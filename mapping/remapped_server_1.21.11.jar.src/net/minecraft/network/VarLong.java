/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ 
/*    */ public class VarLong {
/*    */   private static final int MAX_VARLONG_SIZE = 10;
/*    */   private static final int DATA_BITS_MASK = 127;
/*    */   private static final int CONTINUATION_BIT_MASK = 128;
/*    */   private static final int DATA_BITS_PER_BYTE = 7;
/*    */   
/*    */   public static int getByteSize(long paramLong) {
/* 12 */     for (byte b = 1; b < 10; b++) {
/* 13 */       if ((paramLong & -1L << b * 7) == 0L) {
/* 14 */         return b;
/*    */       }
/*    */     } 
/* 17 */     return 10;
/*    */   }
/*    */   
/*    */   public static boolean hasContinuationBit(byte paramByte) {
/* 21 */     return ((paramByte & 0x80) == 128);
/*    */   }
/*    */   public static long read(ByteBuf paramByteBuf) {
/*    */     byte b1;
/* 25 */     long l = 0L;
/* 26 */     byte b = 0;
/*    */     do {
/* 28 */       b1 = paramByteBuf.readByte();
/*    */       
/* 30 */       l |= (b1 & Byte.MAX_VALUE) << b++ * 7;
/*    */       
/* 32 */       if (b > 10) {
/* 33 */         throw new RuntimeException("VarLong too big");
/*    */       }
/*    */     }
/* 36 */     while (hasContinuationBit(b1));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     return l;
/*    */   }
/*    */   
/*    */   public static ByteBuf write(ByteBuf paramByteBuf, long paramLong) {
/*    */     while (true) {
/* 46 */       if ((paramLong & 0xFFFFFFFFFFFFFF80L) == 0L) {
/* 47 */         paramByteBuf.writeByte((int)paramLong);
/* 48 */         return paramByteBuf;
/*    */       } 
/*    */       
/* 51 */       paramByteBuf.writeByte((int)(paramLong & 0x7FL) | 0x80);
/* 52 */       paramLong >>>= 7L;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\VarLong.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */