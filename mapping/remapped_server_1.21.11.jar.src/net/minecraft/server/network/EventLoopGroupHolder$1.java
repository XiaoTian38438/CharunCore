/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.IoHandlerFactory;
/*    */ import io.netty.channel.ServerChannel;
/*    */ import io.netty.channel.nio.NioIoHandler;
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
/* 31 */     super(paramString, paramClass1, paramClass2);
/*    */   }
/*    */   protected IoHandlerFactory ioHandlerFactory() {
/* 34 */     return NioIoHandler.newFactory();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\EventLoopGroupHolder$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */