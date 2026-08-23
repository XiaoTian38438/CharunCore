/*     */ package net.minecraft.server.network;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.server.ServerInfo;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class LegacyQueryHandler extends ChannelInboundHandlerAdapter {
/*  16 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final ServerInfo server;
/*     */   
/*     */   public LegacyQueryHandler(ServerInfo paramServerInfo) {
/*  21 */     this.server = paramServerInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext paramChannelHandlerContext, Object paramObject) {
/*  26 */     ByteBuf byteBuf = (ByteBuf)paramObject;
/*     */     
/*  28 */     byteBuf.markReaderIndex();
/*     */     
/*  30 */     boolean bool = true;
/*     */     
/*  32 */     try { if (byteBuf.readUnsignedByte() != 254) {
/*     */         return;
/*     */       }
/*     */       
/*  36 */       SocketAddress socketAddress = paramChannelHandlerContext.channel().remoteAddress();
/*     */       
/*  38 */       int i = byteBuf.readableBytes();
/*  39 */       if (i == 0) {
/*  40 */         LOGGER.debug("Ping: (<1.3.x) from {}", socketAddress);
/*     */ 
/*     */         
/*  43 */         String str = createVersion0Response(this.server);
/*  44 */         sendFlushAndClose(paramChannelHandlerContext, createLegacyDisconnectPacket(paramChannelHandlerContext.alloc(), str));
/*     */       } else {
/*     */         
/*  47 */         if (byteBuf.readUnsignedByte() != 1) {
/*     */           return;
/*     */         }
/*     */         
/*  51 */         if (byteBuf.isReadable()) {
/*     */ 
/*     */           
/*  54 */           if (!readCustomPayloadPacket(byteBuf)) {
/*     */             return;
/*     */           }
/*  57 */           LOGGER.debug("Ping: (1.6) from {}", socketAddress);
/*     */         } else {
/*  59 */           LOGGER.debug("Ping: (1.4-1.5.x) from {}", socketAddress);
/*     */         } 
/*     */         
/*  62 */         String str = createVersion1Response(this.server);
/*  63 */         sendFlushAndClose(paramChannelHandlerContext, createLegacyDisconnectPacket(paramChannelHandlerContext.alloc(), str));
/*     */       } 
/*     */       
/*  66 */       byteBuf.release();
/*  67 */       bool = false; }
/*  68 */     catch (RuntimeException runtimeException) {  }
/*     */     finally
/*  70 */     { if (bool) {
/*     */         
/*  72 */         byteBuf.resetReaderIndex();
/*  73 */         paramChannelHandlerContext.channel().pipeline().remove((ChannelHandler)this);
/*  74 */         paramChannelHandlerContext.fireChannelRead(paramObject);
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   private static boolean readCustomPayloadPacket(ByteBuf paramByteBuf) {
/*  80 */     short s1 = paramByteBuf.readUnsignedByte();
/*  81 */     if (s1 != 250) {
/*  82 */       return false;
/*     */     }
/*  84 */     String str1 = LegacyProtocolUtils.readLegacyString(paramByteBuf);
/*  85 */     if (!"MC|PingHost".equals(str1)) {
/*  86 */       return false;
/*     */     }
/*  88 */     int i = paramByteBuf.readUnsignedShort();
/*  89 */     if (paramByteBuf.readableBytes() != i) {
/*  90 */       return false;
/*     */     }
/*  92 */     short s2 = paramByteBuf.readUnsignedByte();
/*  93 */     if (s2 < 73) {
/*  94 */       return false;
/*     */     }
/*  96 */     String str2 = LegacyProtocolUtils.readLegacyString(paramByteBuf);
/*  97 */     int j = paramByteBuf.readInt();
/*  98 */     if (j > 65535) {
/*  99 */       return false;
/*     */     }
/* 101 */     return true;
/*     */   }
/*     */   
/*     */   private static String createVersion0Response(ServerInfo paramServerInfo) {
/* 105 */     return String.format(Locale.ROOT, "%s§%d§%d", new Object[] { paramServerInfo.getMotd(), Integer.valueOf(paramServerInfo.getPlayerCount()), Integer.valueOf(paramServerInfo.getMaxPlayers()) });
/*     */   }
/*     */   
/*     */   private static String createVersion1Response(ServerInfo paramServerInfo) {
/* 109 */     return String.format(Locale.ROOT, "§1\000%d\000%s\000%s\000%d\000%d", new Object[] { Integer.valueOf(127), paramServerInfo.getServerVersion(), paramServerInfo.getMotd(), Integer.valueOf(paramServerInfo.getPlayerCount()), Integer.valueOf(paramServerInfo.getMaxPlayers()) });
/*     */   }
/*     */   
/*     */   private static void sendFlushAndClose(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf) {
/* 113 */     paramChannelHandlerContext.pipeline().firstContext().writeAndFlush(paramByteBuf).addListener((GenericFutureListener)ChannelFutureListener.CLOSE);
/*     */   }
/*     */   
/*     */   private static ByteBuf createLegacyDisconnectPacket(ByteBufAllocator paramByteBufAllocator, String paramString) {
/* 117 */     ByteBuf byteBuf = paramByteBufAllocator.buffer();
/* 118 */     byteBuf.writeByte(255);
/* 119 */     LegacyProtocolUtils.writeLegacyString(byteBuf, paramString);
/* 120 */     return byteBuf;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\LegacyQueryHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */