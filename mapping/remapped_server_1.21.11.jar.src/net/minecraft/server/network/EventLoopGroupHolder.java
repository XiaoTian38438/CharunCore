/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.IoHandlerFactory;
/*     */ import io.netty.channel.MultiThreadIoEventLoopGroup;
/*     */ import io.netty.channel.ServerChannel;
/*     */ import io.netty.channel.epoll.Epoll;
/*     */ import io.netty.channel.epoll.EpollIoHandler;
/*     */ import io.netty.channel.epoll.EpollServerSocketChannel;
/*     */ import io.netty.channel.epoll.EpollSocketChannel;
/*     */ import io.netty.channel.kqueue.KQueue;
/*     */ import io.netty.channel.kqueue.KQueueIoHandler;
/*     */ import io.netty.channel.kqueue.KQueueServerSocketChannel;
/*     */ import io.netty.channel.kqueue.KQueueSocketChannel;
/*     */ import io.netty.channel.local.LocalChannel;
/*     */ import io.netty.channel.local.LocalIoHandler;
/*     */ import io.netty.channel.local.LocalServerChannel;
/*     */ import io.netty.channel.nio.NioIoHandler;
/*     */ import io.netty.channel.socket.nio.NioServerSocketChannel;
/*     */ import io.netty.channel.socket.nio.NioSocketChannel;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class EventLoopGroupHolder
/*     */ {
/*  31 */   private static final EventLoopGroupHolder NIO = new EventLoopGroupHolder("NIO", NioSocketChannel.class, NioServerSocketChannel.class)
/*     */     {
/*     */       protected IoHandlerFactory ioHandlerFactory() {
/*  34 */         return NioIoHandler.newFactory();
/*     */       }
/*     */     };
/*     */   
/*  38 */   private static final EventLoopGroupHolder EPOLL = new EventLoopGroupHolder("Epoll", EpollSocketChannel.class, EpollServerSocketChannel.class)
/*     */     {
/*     */       protected IoHandlerFactory ioHandlerFactory() {
/*  41 */         return EpollIoHandler.newFactory();
/*     */       }
/*     */     };
/*     */   
/*  45 */   private static final EventLoopGroupHolder KQUEUE = new EventLoopGroupHolder("Kqueue", KQueueSocketChannel.class, KQueueServerSocketChannel.class)
/*     */     {
/*     */       protected IoHandlerFactory ioHandlerFactory() {
/*  48 */         return KQueueIoHandler.newFactory();
/*     */       }
/*     */     };
/*     */   
/*  52 */   private static final EventLoopGroupHolder LOCAL = new EventLoopGroupHolder("Local", LocalChannel.class, LocalServerChannel.class)
/*     */     {
/*     */       protected IoHandlerFactory ioHandlerFactory() {
/*  55 */         return LocalIoHandler.newFactory();
/*     */       }
/*     */     };
/*     */   private final String type; private final Class<? extends Channel> channelCls;
/*     */   public static EventLoopGroupHolder remote(boolean paramBoolean) {
/*  60 */     if (paramBoolean) {
/*  61 */       if (KQueue.isAvailable()) {
/*  62 */         return KQUEUE;
/*     */       }
/*     */       
/*  65 */       if (Epoll.isAvailable()) {
/*  66 */         return EPOLL;
/*     */       }
/*     */     } 
/*  69 */     return NIO;
/*     */   }
/*     */   private final Class<? extends ServerChannel> serverChannelCls; private volatile EventLoopGroup group;
/*     */   public static EventLoopGroupHolder local() {
/*  73 */     return LOCAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   EventLoopGroupHolder(String paramString, Class<? extends Channel> paramClass, Class<? extends ServerChannel> paramClass1) {
/*  83 */     this.type = paramString;
/*  84 */     this.channelCls = paramClass;
/*  85 */     this.serverChannelCls = paramClass1;
/*     */   }
/*     */   
/*     */   private ThreadFactory createThreadFactory() {
/*  89 */     return (new ThreadFactoryBuilder()).setNameFormat("Netty " + this.type + " IO #%d").setDaemon(true).build();
/*     */   }
/*     */   
/*     */   protected abstract IoHandlerFactory ioHandlerFactory();
/*     */   
/*     */   private EventLoopGroup createEventLoopGroup() {
/*  95 */     return (EventLoopGroup)new MultiThreadIoEventLoopGroup(createThreadFactory(), ioHandlerFactory());
/*     */   }
/*     */   
/*     */   public EventLoopGroup eventLoopGroup() {
/*  99 */     EventLoopGroup eventLoopGroup = this.group;
/* 100 */     if (eventLoopGroup == null) {
/* 101 */       synchronized (this) {
/* 102 */         eventLoopGroup = this.group;
/* 103 */         if (eventLoopGroup == null) {
/* 104 */           eventLoopGroup = createEventLoopGroup();
/* 105 */           this.group = eventLoopGroup;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 110 */     return eventLoopGroup;
/*     */   }
/*     */   
/*     */   public Class<? extends Channel> channelCls() {
/* 114 */     return this.channelCls;
/*     */   }
/*     */   
/*     */   public Class<? extends ServerChannel> serverChannelCls() {
/* 118 */     return this.serverChannelCls;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\EventLoopGroupHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */