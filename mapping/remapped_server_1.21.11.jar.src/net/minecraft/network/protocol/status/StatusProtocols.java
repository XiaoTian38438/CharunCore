/*    */ package net.minecraft.network.protocol.status;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.ProtocolInfo;
/*    */ import net.minecraft.network.protocol.ProtocolInfoBuilder;
/*    */ import net.minecraft.network.protocol.SimpleUnboundProtocol;
/*    */ import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
/*    */ import net.minecraft.network.protocol.ping.PingPacketTypes;
/*    */ import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
/*    */ 
/*    */ public class StatusProtocols {
/*    */   static {
/* 14 */     SERVERBOUND_TEMPLATE = ProtocolInfoBuilder.serverboundProtocol(ConnectionProtocol.STATUS, paramProtocolInfoBuilder -> paramProtocolInfoBuilder.addPacket(StatusPacketTypes.SERVERBOUND_STATUS_REQUEST, ServerboundStatusRequestPacket.STREAM_CODEC).addPacket(PingPacketTypes.SERVERBOUND_PING_REQUEST, ServerboundPingRequestPacket.STREAM_CODEC));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 19 */     SERVERBOUND = SERVERBOUND_TEMPLATE.bind(paramByteBuf -> paramByteBuf);
/*    */     
/* 21 */     CLIENTBOUND_TEMPLATE = ProtocolInfoBuilder.clientboundProtocol(ConnectionProtocol.STATUS, paramProtocolInfoBuilder -> paramProtocolInfoBuilder.addPacket(StatusPacketTypes.CLIENTBOUND_STATUS_RESPONSE, ClientboundStatusResponsePacket.STREAM_CODEC).addPacket(PingPacketTypes.CLIENTBOUND_PONG_RESPONSE, ClientboundPongResponsePacket.STREAM_CODEC));
/*    */   }
/*    */   public static final SimpleUnboundProtocol<ServerStatusPacketListener, ByteBuf> SERVERBOUND_TEMPLATE;
/*    */   public static final ProtocolInfo<ServerStatusPacketListener> SERVERBOUND;
/*    */   public static final SimpleUnboundProtocol<ClientStatusPacketListener, FriendlyByteBuf> CLIENTBOUND_TEMPLATE;
/* 26 */   public static final ProtocolInfo<ClientStatusPacketListener> CLIENTBOUND = CLIENTBOUND_TEMPLATE.bind(FriendlyByteBuf::new);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\status\StatusProtocols.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */