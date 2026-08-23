/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.ByteToMessageDecoder;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.List;
/*    */ import java.util.zip.DataFormatException;
/*    */ import java.util.zip.Inflater;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompressionDecoder
/*    */   extends ByteToMessageDecoder
/*    */ {
/*    */   public static final int MAXIMUM_COMPRESSED_LENGTH = 2097152;
/*    */   public static final int MAXIMUM_UNCOMPRESSED_LENGTH = 8388608;
/*    */   private final Inflater inflater;
/*    */   private int threshold;
/*    */   private boolean validateDecompressed;
/*    */   
/*    */   public CompressionDecoder(int paramInt, boolean paramBoolean) {
/* 25 */     this.threshold = paramInt;
/* 26 */     this.validateDecompressed = paramBoolean;
/* 27 */     this.inflater = new Inflater();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void decode(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf, List<Object> paramList) throws Exception {
/* 32 */     int i = VarInt.read(paramByteBuf);
/*    */     
/* 34 */     if (i == 0) {
/* 35 */       paramList.add(paramByteBuf.readBytes(paramByteBuf.readableBytes()));
/*    */       
/*    */       return;
/*    */     } 
/* 39 */     if (this.validateDecompressed) {
/* 40 */       if (i < this.threshold)
/* 41 */         throw new DecoderException("Badly compressed packet - size of " + i + " is below server threshold of " + this.threshold); 
/* 42 */       if (i > 8388608) {
/* 43 */         throw new DecoderException("Badly compressed packet - size of " + i + " is larger than protocol maximum of 8388608");
/*    */       }
/*    */     } 
/*    */     
/* 47 */     setupInflaterInput(paramByteBuf);
/* 48 */     ByteBuf byteBuf = inflate(paramChannelHandlerContext, i);
/* 49 */     this.inflater.reset();
/*    */     
/* 51 */     paramList.add(byteBuf);
/*    */   }
/*    */   
/*    */   private void setupInflaterInput(ByteBuf paramByteBuf) {
/*    */     ByteBuffer byteBuffer;
/* 56 */     if (paramByteBuf.nioBufferCount() > 0) {
/* 57 */       byteBuffer = paramByteBuf.nioBuffer();
/* 58 */       paramByteBuf.skipBytes(paramByteBuf.readableBytes());
/*    */     } else {
/*    */       
/* 61 */       byteBuffer = ByteBuffer.allocateDirect(paramByteBuf.readableBytes());
/* 62 */       paramByteBuf.readBytes(byteBuffer);
/* 63 */       byteBuffer.flip();
/*    */     } 
/* 65 */     this.inflater.setInput(byteBuffer);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private ByteBuf inflate(ChannelHandlerContext paramChannelHandlerContext, int paramInt) throws DataFormatException {
/* 71 */     ByteBuf byteBuf = paramChannelHandlerContext.alloc().directBuffer(paramInt);
/*    */     try {
/* 73 */       ByteBuffer byteBuffer = byteBuf.internalNioBuffer(0, paramInt);
/* 74 */       int i = byteBuffer.position();
/* 75 */       this.inflater.inflate(byteBuffer);
/* 76 */       int j = byteBuffer.position() - i;
/* 77 */       if (j != paramInt) {
/* 78 */         throw new DecoderException("Badly compressed packet - actual length of uncompressed payload " + j + " is does not match declared size " + paramInt);
/*    */       }
/* 80 */       byteBuf.writerIndex(byteBuf.writerIndex() + j);
/* 81 */       return byteBuf;
/* 82 */     } catch (Exception exception) {
/* 83 */       byteBuf.release();
/* 84 */       throw exception;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setThreshold(int paramInt, boolean paramBoolean) {
/* 89 */     this.threshold = paramInt;
/* 90 */     this.validateDecompressed = paramBoolean;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\CompressionDecoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */