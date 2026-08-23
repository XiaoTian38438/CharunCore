/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.ByteToMessageDecoder;
/*    */ import io.netty.handler.codec.CorruptedFrameException;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Varint21FrameDecoder
/*    */   extends ByteToMessageDecoder
/*    */ {
/*    */   private static final int MAX_VARINT21_BYTES = 3;
/* 16 */   private final ByteBuf helperBuf = Unpooled.directBuffer(3);
/*    */   private final BandwidthDebugMonitor monitor;
/*    */   
/*    */   public Varint21FrameDecoder(BandwidthDebugMonitor paramBandwidthDebugMonitor) {
/* 20 */     this.monitor = paramBandwidthDebugMonitor;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void handlerRemoved0(ChannelHandlerContext paramChannelHandlerContext) {
/* 25 */     this.helperBuf.release();
/*    */   }
/*    */   
/*    */   private static boolean copyVarint(ByteBuf paramByteBuf1, ByteBuf paramByteBuf2) {
/* 29 */     for (byte b = 0; b < 3; b++) {
/* 30 */       if (!paramByteBuf1.isReadable()) {
/* 31 */         return false;
/*    */       }
/*    */       
/* 34 */       byte b1 = paramByteBuf1.readByte();
/* 35 */       paramByteBuf2.writeByte(b1);
/*    */       
/* 37 */       if (!VarInt.hasContinuationBit(b1)) {
/* 38 */         return true;
/*    */       }
/*    */     } 
/*    */     
/* 42 */     throw new CorruptedFrameException("length wider than 21-bit");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void decode(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf, List<Object> paramList) {
/* 48 */     paramByteBuf.markReaderIndex();
/*    */     
/* 50 */     this.helperBuf.clear();
/*    */     
/* 52 */     if (!copyVarint(paramByteBuf, this.helperBuf)) {
/* 53 */       paramByteBuf.resetReaderIndex();
/*    */       
/*    */       return;
/*    */     } 
/* 57 */     int i = VarInt.read(this.helperBuf);
/* 58 */     if (i == 0) {
/* 59 */       throw new CorruptedFrameException("Frame length cannot be zero");
/*    */     }
/*    */     
/* 62 */     if (paramByteBuf.readableBytes() < i) {
/* 63 */       paramByteBuf.resetReaderIndex();
/*    */       
/*    */       return;
/*    */     } 
/* 67 */     if (this.monitor != null) {
/* 68 */       this.monitor.onReceive(i + VarInt.getByteSize(i));
/*    */     }
/*    */     
/* 71 */     paramList.add(paramByteBuf.readBytes(i));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\Varint21FrameDecoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */