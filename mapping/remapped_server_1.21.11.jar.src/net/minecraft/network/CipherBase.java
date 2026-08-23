/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import javax.crypto.Cipher;
/*    */ import javax.crypto.ShortBufferException;
/*    */ 
/*    */ public class CipherBase
/*    */ {
/*    */   private final Cipher cipher;
/* 11 */   private byte[] heapIn = new byte[0];
/* 12 */   private byte[] heapOut = new byte[0];
/*    */   
/*    */   protected CipherBase(Cipher paramCipher) {
/* 15 */     this.cipher = paramCipher;
/*    */   }
/*    */   
/*    */   private byte[] bufToByte(ByteBuf paramByteBuf) {
/* 19 */     int i = paramByteBuf.readableBytes();
/* 20 */     if (this.heapIn.length < i) {
/* 21 */       this.heapIn = new byte[i];
/*    */     }
/* 23 */     paramByteBuf.readBytes(this.heapIn, 0, i);
/* 24 */     return this.heapIn;
/*    */   }
/*    */   
/*    */   protected ByteBuf decipher(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf) throws ShortBufferException {
/* 28 */     int i = paramByteBuf.readableBytes();
/* 29 */     byte[] arrayOfByte = bufToByte(paramByteBuf);
/*    */     
/* 31 */     ByteBuf byteBuf = paramChannelHandlerContext.alloc().heapBuffer(this.cipher.getOutputSize(i));
/* 32 */     byteBuf.writerIndex(this.cipher.update(arrayOfByte, 0, i, byteBuf.array(), byteBuf.arrayOffset()));
/*    */     
/* 34 */     return byteBuf;
/*    */   }
/*    */   
/*    */   protected void encipher(ByteBuf paramByteBuf1, ByteBuf paramByteBuf2) throws ShortBufferException {
/* 38 */     int i = paramByteBuf1.readableBytes();
/* 39 */     byte[] arrayOfByte = bufToByte(paramByteBuf1);
/*    */     
/* 41 */     int j = this.cipher.getOutputSize(i);
/* 42 */     if (this.heapOut.length < j) {
/* 43 */       this.heapOut = new byte[j];
/*    */     }
/* 45 */     paramByteBuf2.writeBytes(this.heapOut, 0, this.cipher.update(arrayOfByte, 0, i, this.heapOut));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\CipherBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */