/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.util.HashedWheelTimer;
/*     */ import io.netty.util.Timeout;
/*     */ import io.netty.util.Timer;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LatencySimulator
/*     */   extends ChannelInboundHandlerAdapter
/*     */ {
/* 173 */   private static final Timer TIMER = (Timer)new HashedWheelTimer();
/*     */   
/*     */   private final int delay;
/*     */   private final int jitter;
/* 177 */   private final List<DelayedMessage> queuedMessages = Lists.newArrayList();
/*     */   
/*     */   public LatencySimulator(int paramInt1, int paramInt2) {
/* 180 */     this.delay = paramInt1;
/* 181 */     this.jitter = paramInt2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext paramChannelHandlerContext, Object paramObject) {
/* 186 */     delayDownstream(paramChannelHandlerContext, paramObject);
/*     */   }
/*     */   
/*     */   private void delayDownstream(ChannelHandlerContext paramChannelHandlerContext, Object paramObject) {
/* 190 */     int i = this.delay + (int)(Math.random() * this.jitter);
/* 191 */     this.queuedMessages.add(new DelayedMessage(paramChannelHandlerContext, paramObject));
/* 192 */     TIMER.newTimeout(this::onTimeout, i, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */   private void onTimeout(Timeout paramTimeout) {
/* 196 */     DelayedMessage delayedMessage = this.queuedMessages.remove(0);
/* 197 */     delayedMessage.ctx.fireChannelRead(delayedMessage.msg);
/*     */   }
/*     */   
/*     */   private static class DelayedMessage {
/*     */     public final ChannelHandlerContext ctx;
/*     */     public final Object msg;
/*     */     
/*     */     public DelayedMessage(ChannelHandlerContext param2ChannelHandlerContext, Object param2Object) {
/* 205 */       this.ctx = param2ChannelHandlerContext;
/* 206 */       this.msg = param2Object;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\ServerConnectionListener$LatencySimulator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */