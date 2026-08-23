/*    */ package net.minecraft.network;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import io.netty.channel.ChannelFuture;
/*    */ import io.netty.channel.ChannelFutureListener;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PacketSendListener
/*    */ {
/* 17 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static ChannelFutureListener thenRun(Runnable paramRunnable) {
/* 20 */     return paramChannelFuture -> {
/*    */         paramRunnable.run();
/*    */         if (!paramChannelFuture.isSuccess()) {
/*    */           paramChannelFuture.channel().pipeline().fireExceptionCaught(paramChannelFuture.cause());
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static ChannelFutureListener exceptionallySend(Supplier<Packet<?>> paramSupplier) {
/* 29 */     return paramChannelFuture -> {
/*    */         if (!paramChannelFuture.isSuccess()) {
/*    */           Packet packet = paramSupplier.get();
/*    */           if (packet != null) {
/*    */             LOGGER.warn("Failed to deliver packet, sending fallback {}", packet.type(), paramChannelFuture.cause());
/*    */             paramChannelFuture.channel().writeAndFlush(packet, paramChannelFuture.channel().voidPromise());
/*    */           } else {
/*    */             paramChannelFuture.channel().pipeline().fireExceptionCaught(paramChannelFuture.cause());
/*    */           } 
/*    */         } 
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\PacketSendListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */