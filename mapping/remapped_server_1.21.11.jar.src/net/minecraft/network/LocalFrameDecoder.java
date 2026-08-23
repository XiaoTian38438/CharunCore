/*   */ package net.minecraft.network;
/*   */ 
/*   */ import io.netty.channel.ChannelHandlerContext;
/*   */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*   */ 
/*   */ public class LocalFrameDecoder
/*   */   extends ChannelInboundHandlerAdapter {
/*   */   public void channelRead(ChannelHandlerContext paramChannelHandlerContext, Object paramObject) {
/* 9 */     paramChannelHandlerContext.fireChannelRead(HiddenByteBuf.unpack(paramObject));
/*   */   }
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\LocalFrameDecoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */