/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*    */ 
/*    */ public class MonitoredLocalFrameDecoder extends ChannelInboundHandlerAdapter {
/*    */   private final BandwidthDebugMonitor monitor;
/*    */   
/*    */   public MonitoredLocalFrameDecoder(BandwidthDebugMonitor paramBandwidthDebugMonitor) {
/* 11 */     this.monitor = paramBandwidthDebugMonitor;
/*    */   }
/*    */ 
/*    */   
/*    */   public void channelRead(ChannelHandlerContext paramChannelHandlerContext, Object paramObject) {
/* 16 */     paramObject = HiddenByteBuf.unpack(paramObject);
/* 17 */     if (paramObject instanceof ByteBuf) { ByteBuf byteBuf = (ByteBuf)paramObject;
/* 18 */       this.monitor.onReceive(byteBuf.readableBytes()); }
/*    */     
/* 20 */     paramChannelHandlerContext.fireChannelRead(paramObject);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\MonitoredLocalFrameDecoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */