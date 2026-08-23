/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelOutboundHandlerAdapter;
/*    */ import io.netty.channel.ChannelPromise;
/*    */ import io.netty.handler.codec.EncoderException;
/*    */ import io.netty.util.ReferenceCountUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Outbound
/*    */   extends ChannelOutboundHandlerAdapter
/*    */ {
/*    */   public void write(ChannelHandlerContext paramChannelHandlerContext, Object paramObject, ChannelPromise paramChannelPromise) throws Exception {
/* 46 */     if (paramObject instanceof net.minecraft.network.protocol.Packet) {
/* 47 */       ReferenceCountUtil.release(paramObject);
/* 48 */       throw new EncoderException("Pipeline has no outbound protocol configured, can't process packet " + String.valueOf(paramObject));
/* 49 */     }  if (paramObject instanceof UnconfiguredPipelineHandler.OutboundConfigurationTask) { UnconfiguredPipelineHandler.OutboundConfigurationTask outboundConfigurationTask = (UnconfiguredPipelineHandler.OutboundConfigurationTask)paramObject;
/*    */       try {
/* 51 */         outboundConfigurationTask.run(paramChannelHandlerContext);
/*    */       } finally {
/* 53 */         ReferenceCountUtil.release(paramObject);
/*    */       } 
/* 55 */       paramChannelPromise.setSuccess(); }
/*    */     else
/* 57 */     { paramChannelHandlerContext.write(paramObject, paramChannelPromise); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\UnconfiguredPipelineHandler$Outbound.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */