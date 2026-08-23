/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufUtil;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import io.netty.handler.codec.EncoderException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ 
/*    */ 
/*    */ public class Utf8String
/*    */ {
/*    */   public static String read(ByteBuf paramByteBuf, int paramInt) {
/* 13 */     int i = ByteBufUtil.utf8MaxBytes(paramInt);
/*    */     
/* 15 */     int j = VarInt.read(paramByteBuf);
/* 16 */     if (j > i) {
/* 17 */       throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + i + ")");
/*    */     }
/* 19 */     if (j < 0) {
/* 20 */       throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
/*    */     }
/* 22 */     int k = paramByteBuf.readableBytes();
/* 23 */     if (j > k) {
/* 24 */       throw new DecoderException("Not enough bytes in buffer, expected " + j + ", but got " + k);
/*    */     }
/*    */     
/* 27 */     String str = paramByteBuf.toString(paramByteBuf.readerIndex(), j, StandardCharsets.UTF_8);
/* 28 */     paramByteBuf.readerIndex(paramByteBuf.readerIndex() + j);
/* 29 */     if (str.length() > paramInt) {
/* 30 */       throw new DecoderException("The received string length is longer than maximum allowed (" + str.length() + " > " + paramInt + ")");
/*    */     }
/*    */     
/* 33 */     return str;
/*    */   }
/*    */   
/*    */   public static void write(ByteBuf paramByteBuf, CharSequence paramCharSequence, int paramInt) {
/* 37 */     if (paramCharSequence.length() > paramInt) {
/* 38 */       throw new EncoderException("String too big (was " + paramCharSequence.length() + " characters, max " + paramInt + ")");
/*    */     }
/*    */     
/* 41 */     int i = ByteBufUtil.utf8MaxBytes(paramCharSequence);
/* 42 */     ByteBuf byteBuf = paramByteBuf.alloc().buffer(i);
/*    */     try {
/* 44 */       int j = ByteBufUtil.writeUtf8(byteBuf, paramCharSequence);
/* 45 */       int k = ByteBufUtil.utf8MaxBytes(paramInt);
/* 46 */       if (j > k) {
/* 47 */         throw new EncoderException("String too big (was " + j + " bytes encoded, max " + k + ")");
/*    */       }
/* 49 */       VarInt.write(paramByteBuf, j);
/* 50 */       paramByteBuf.writeBytes(byteBuf);
/*    */     } finally {
/* 52 */       byteBuf.release();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\Utf8String.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */