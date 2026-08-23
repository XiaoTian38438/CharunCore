/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.IoHandlerFactory;
/*    */ import io.netty.channel.ServerChannel;
/*    */ import io.netty.channel.kqueue.KQueueIoHandler;
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
/*    */ class null
/*    */   extends EventLoopGroupHolder
/*    */ {
/*    */   null(String paramString, Class<? extends Channel> paramClass1, Class<? extends ServerChannel> paramClass2) {
/* 45 */     super(paramString, paramClass1, paramClass2);
/*    */   }
/*    */   protected IoHandlerFactory ioHandlerFactory() {
/* 48 */     return KQueueIoHandler.newFactory();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\EventLoopGroupHolder$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */