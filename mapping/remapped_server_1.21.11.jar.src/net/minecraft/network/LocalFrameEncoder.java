/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelOutboundHandlerAdapter;
/*    */ import io.netty.channel.ChannelPromise;
/*    */ 
/*    */ public class LocalFrameEncoder
/*    */   extends ChannelOutboundHandlerAdapter {
/*    */   public void write(ChannelHandlerContext paramChannelHandlerContext, Object paramObject, ChannelPromise paramChannelPromise) {
/* 10 */     paramChannelHandlerContext.write(HiddenByteBuf.pack(paramObject), paramChannelPromise);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\LocalFrameEncoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */