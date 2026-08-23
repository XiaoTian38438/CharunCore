/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ 
/*    */ public class VarInt {
/*    */   public static final int MAX_VARINT_SIZE = 5;
/*    */   private static final int DATA_BITS_MASK = 127;
/*    */   private static final int CONTINUATION_BIT_MASK = 128;
/*    */   private static final int DATA_BITS_PER_BYTE = 7;
/*    */   
/*    */   public static int getByteSize(int paramInt) {
/* 12 */     for (byte b = 1; b < 5; b++) {
/* 13 */       if ((paramInt & -1 << b * 7) == 0) {
/* 14 */         return b;
/*    */       }
/*    */     } 
/* 17 */     return 5;
/*    */   }
/*    */   
/*    */   public static boolean hasContinuationBit(byte paramByte) {
/* 21 */     return ((paramByte & 0x80) == 128);
/*    */   }
/*    */   public static int read(ByteBuf paramByteBuf) {
/*    */     byte b1;
/* 25 */     int i = 0;
/* 26 */     byte b = 0;
/*    */     do {
/* 28 */       b1 = paramByteBuf.readByte();
/*    */       
/* 30 */       i |= (b1 & Byte.MAX_VALUE) << b++ * 7;
/*    */       
/* 32 */       if (b > 5) {
/* 33 */         throw new RuntimeException("VarInt too big");
/*    */       }
/*    */     }
/* 36 */     while (hasContinuationBit(b1));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     return i;
/*    */   }
/*    */   
/*    */   public static ByteBuf write(ByteBuf paramByteBuf, int paramInt) {
/*    */     while (true) {
/* 46 */       if ((paramInt & 0xFFFFFF80) == 0) {
/* 47 */         paramByteBuf.writeByte(paramInt);
/* 48 */         return paramByteBuf;
/*    */       } 
/*    */       
/* 51 */       paramByteBuf.writeByte(paramInt & 0x7F | 0x80);
/* 52 */       paramInt >>>= 7;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\VarInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */