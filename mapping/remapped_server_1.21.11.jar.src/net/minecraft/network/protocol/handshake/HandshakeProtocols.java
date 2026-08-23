/*    */ package net.minecraft.network.protocol.handshake;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.ProtocolInfo;
/*    */ import net.minecraft.network.protocol.ProtocolInfoBuilder;
/*    */ import net.minecraft.network.protocol.SimpleUnboundProtocol;
/*    */ 
/*    */ public class HandshakeProtocols {
/*    */   static {
/* 10 */     SERVERBOUND_TEMPLATE = ProtocolInfoBuilder.serverboundProtocol(ConnectionProtocol.HANDSHAKING, paramProtocolInfoBuilder -> paramProtocolInfoBuilder.addPacket(HandshakePacketTypes.CLIENT_INTENTION, ClientIntentionPacket.STREAM_CODEC));
/*    */   }
/*    */   
/*    */   public static final SimpleUnboundProtocol<ServerHandshakePacketListener, FriendlyByteBuf> SERVERBOUND_TEMPLATE;
/* 14 */   public static final ProtocolInfo<ServerHandshakePacketListener> SERVERBOUND = SERVERBOUND_TEMPLATE.bind(FriendlyByteBuf::new);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\handshake\HandshakeProtocols.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */