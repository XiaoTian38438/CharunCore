/*     */ package net.minecraft.network;
/*     */ 
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandler;
/*     */ import io.netty.channel.ChannelOutboundHandler;
/*     */ import io.netty.channel.ChannelOutboundHandlerAdapter;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.EncoderException;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ 
/*     */ public class UnconfiguredPipelineHandler
/*     */ {
/*     */   public static class Inbound
/*     */     extends ChannelDuplexHandler {
/*     */     public void channelRead(ChannelHandlerContext param1ChannelHandlerContext, Object param1Object) {
/*  19 */       if (param1Object instanceof io.netty.buffer.ByteBuf || param1Object instanceof net.minecraft.network.protocol.Packet) {
/*  20 */         ReferenceCountUtil.release(param1Object);
/*  21 */         throw new DecoderException("Pipeline has no inbound protocol configured, can't process packet " + String.valueOf(param1Object));
/*     */       } 
/*     */       
/*  24 */       param1ChannelHandlerContext.fireChannelRead(param1Object);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(ChannelHandlerContext param1ChannelHandlerContext, Object param1Object, ChannelPromise param1ChannelPromise) throws Exception {
/*  30 */       if (param1Object instanceof UnconfiguredPipelineHandler.InboundConfigurationTask) { UnconfiguredPipelineHandler.InboundConfigurationTask inboundConfigurationTask = (UnconfiguredPipelineHandler.InboundConfigurationTask)param1Object;
/*     */         try {
/*  32 */           inboundConfigurationTask.run(param1ChannelHandlerContext);
/*     */         } finally {
/*  34 */           ReferenceCountUtil.release(param1Object);
/*     */         } 
/*  36 */         param1ChannelPromise.setSuccess(); }
/*     */       else
/*  38 */       { param1ChannelHandlerContext.write(param1Object, param1ChannelPromise); }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Outbound
/*     */     extends ChannelOutboundHandlerAdapter {
/*     */     public void write(ChannelHandlerContext param1ChannelHandlerContext, Object param1Object, ChannelPromise param1ChannelPromise) throws Exception {
/*  46 */       if (param1Object instanceof net.minecraft.network.protocol.Packet) {
/*  47 */         ReferenceCountUtil.release(param1Object);
/*  48 */         throw new EncoderException("Pipeline has no outbound protocol configured, can't process packet " + String.valueOf(param1Object));
/*  49 */       }  if (param1Object instanceof UnconfiguredPipelineHandler.OutboundConfigurationTask) { UnconfiguredPipelineHandler.OutboundConfigurationTask outboundConfigurationTask = (UnconfiguredPipelineHandler.OutboundConfigurationTask)param1Object;
/*     */         try {
/*  51 */           outboundConfigurationTask.run(param1ChannelHandlerContext);
/*     */         } finally {
/*  53 */           ReferenceCountUtil.release(param1Object);
/*     */         } 
/*  55 */         param1ChannelPromise.setSuccess(); }
/*     */       else
/*  57 */       { param1ChannelHandlerContext.write(param1Object, param1ChannelPromise); }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface InboundConfigurationTask {
/*     */     void run(ChannelHandlerContext param1ChannelHandlerContext);
/*     */     
/*     */     default InboundConfigurationTask andThen(InboundConfigurationTask param1InboundConfigurationTask) {
/*  67 */       return param1ChannelHandlerContext -> {
/*     */           run(param1ChannelHandlerContext);
/*     */           param1InboundConfigurationTask.run(param1ChannelHandlerContext);
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface OutboundConfigurationTask {
/*     */     void run(ChannelHandlerContext param1ChannelHandlerContext);
/*     */     
/*     */     default OutboundConfigurationTask andThen(OutboundConfigurationTask param1OutboundConfigurationTask) {
/*  79 */       return param1ChannelHandlerContext -> {
/*     */           run(param1ChannelHandlerContext);
/*     */           param1OutboundConfigurationTask.run(param1ChannelHandlerContext);
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T extends PacketListener> InboundConfigurationTask setupInboundProtocol(ProtocolInfo<T> paramProtocolInfo) {
/*  87 */     return setupInboundHandler((ChannelInboundHandler)new PacketDecoder<>(paramProtocolInfo));
/*     */   }
/*     */   
/*     */   private static InboundConfigurationTask setupInboundHandler(ChannelInboundHandler paramChannelInboundHandler) {
/*  91 */     return paramChannelHandlerContext -> {
/*     */         paramChannelHandlerContext.pipeline().replace(paramChannelHandlerContext.name(), "decoder", (ChannelHandler)paramChannelInboundHandler);
/*     */         paramChannelHandlerContext.channel().config().setAutoRead(true);
/*     */       };
/*     */   }
/*     */   
/*     */   public static <T extends PacketListener> OutboundConfigurationTask setupOutboundProtocol(ProtocolInfo<T> paramProtocolInfo) {
/*  98 */     return setupOutboundHandler((ChannelOutboundHandler)new PacketEncoder<>(paramProtocolInfo));
/*     */   }
/*     */   
/*     */   private static OutboundConfigurationTask setupOutboundHandler(ChannelOutboundHandler paramChannelOutboundHandler) {
/* 102 */     return paramChannelHandlerContext -> paramChannelHandlerContext.pipeline().replace(paramChannelHandlerContext.name(), "encoder", (ChannelHandler)paramChannelOutboundHandler);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\UnconfiguredPipelineHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */