/*    */ package net.minecraft.network;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.ByteToMessageDecoder;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class PacketDecoder<T extends PacketListener>
/*    */   extends ByteToMessageDecoder implements ProtocolSwapHandler {
/* 16 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final ProtocolInfo<T> protocolInfo;
/*    */   
/*    */   public PacketDecoder(ProtocolInfo<T> paramProtocolInfo) {
/* 21 */     this.protocolInfo = paramProtocolInfo;
/*    */   }
/*    */   
/*    */   protected void decode(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf, List<Object> paramList) throws Exception {
/*    */     Packet<?> packet;
/* 26 */     int i = paramByteBuf.readableBytes();
/*    */ 
/*    */     
/*    */     try {
/* 30 */       packet = (Packet)this.protocolInfo.codec().decode(paramByteBuf);
/* 31 */     } catch (Exception exception) {
/* 32 */       if (exception instanceof SkipPacketException) {
/* 33 */         paramByteBuf.skipBytes(paramByteBuf.readableBytes());
/*    */       }
/* 35 */       throw exception;
/*    */     } 
/*    */     
/* 38 */     PacketType packetType = packet.type();
/*    */     
/* 40 */     JvmProfiler.INSTANCE.onPacketReceived(this.protocolInfo
/* 41 */         .id(), packetType, paramChannelHandlerContext
/*    */         
/* 43 */         .channel().remoteAddress(), i);
/*    */ 
/*    */ 
/*    */     
/* 47 */     if (paramByteBuf.readableBytes() > 0) {
/* 48 */       throw new IOException("Packet " + this.protocolInfo.id().id() + "/" + String.valueOf(packetType) + " (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + paramByteBuf.readableBytes() + " bytes extra whilst reading packet " + String.valueOf(packetType));
/*    */     }
/* 50 */     paramList.add(packet);
/*    */ 
/*    */     
/* 53 */     if (LOGGER.isDebugEnabled()) {
/* 54 */       LOGGER.debug(Connection.PACKET_RECEIVED_MARKER, " IN: [{}:{}] {} -> {} bytes", new Object[] { this.protocolInfo.id().id(), packetType, packet.getClass().getName(), Integer.valueOf(i) });
/*    */     }
/*    */     
/* 57 */     ProtocolSwapHandler.handleInboundTerminalPacket(paramChannelHandlerContext, packet);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\PacketDecoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */