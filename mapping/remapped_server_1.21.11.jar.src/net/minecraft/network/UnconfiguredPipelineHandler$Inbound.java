/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.channel.ChannelDuplexHandler;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelPromise;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import io.netty.util.ReferenceCountUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Inbound
/*    */   extends ChannelDuplexHandler
/*    */ {
/*    */   public void channelRead(ChannelHandlerContext paramChannelHandlerContext, Object paramObject) {
/* 19 */     if (paramObject instanceof io.netty.buffer.ByteBuf || paramObject instanceof net.minecraft.network.protocol.Packet) {
/* 20 */       ReferenceCountUtil.release(paramObject);
/* 21 */       throw new DecoderException("Pipeline has no inbound protocol configured, can't process packet " + String.valueOf(paramObject));
/*    */     } 
/*    */     
/* 24 */     paramChannelHandlerContext.fireChannelRead(paramObject);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(ChannelHandlerContext paramChannelHandlerContext, Object paramObject, ChannelPromise paramChannelPromise) throws Exception {
/* 30 */     if (paramObject instanceof UnconfiguredPipelineHandler.InboundConfigurationTask) { UnconfiguredPipelineHandler.InboundConfigurationTask inboundConfigurationTask = (UnconfiguredPipelineHandler.InboundConfigurationTask)paramObject;
/*    */       try {
/* 32 */         inboundConfigurationTask.run(paramChannelHandlerContext);
/*    */       } finally {
/* 34 */         ReferenceCountUtil.release(paramObject);
/*    */       } 
/* 36 */       paramChannelPromise.setSuccess(); }
/*    */     else
/* 38 */     { paramChannelHandlerContext.write(paramObject, paramChannelPromise); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\UnconfiguredPipelineHandler$Inbound.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */