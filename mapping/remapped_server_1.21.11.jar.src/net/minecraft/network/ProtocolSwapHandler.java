/*    */ package net.minecraft.network;
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public interface ProtocolSwapHandler {
/*    */   static void handleInboundTerminalPacket(ChannelHandlerContext paramChannelHandlerContext, Packet<?> paramPacket) {
/*  8 */     if (paramPacket.isTerminal()) {
/*    */       
/* 10 */       paramChannelHandlerContext.channel().config().setAutoRead(false);
/*    */ 
/*    */       
/* 13 */       paramChannelHandlerContext.pipeline().addBefore(paramChannelHandlerContext.name(), "inbound_config", (ChannelHandler)new UnconfiguredPipelineHandler.Inbound());
/* 14 */       paramChannelHandlerContext.pipeline().remove(paramChannelHandlerContext.name());
/*    */     } 
/*    */   }
/*    */   
/*    */   static void handleOutboundTerminalPacket(ChannelHandlerContext paramChannelHandlerContext, Packet<?> paramPacket) {
/* 19 */     if (paramPacket.isTerminal()) {
/*    */       
/* 21 */       paramChannelHandlerContext.pipeline().addAfter(paramChannelHandlerContext.name(), "outbound_config", (ChannelHandler)new UnconfiguredPipelineHandler.Outbound());
/* 22 */       paramChannelHandlerContext.pipeline().remove(paramChannelHandlerContext.name());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\ProtocolSwapHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */