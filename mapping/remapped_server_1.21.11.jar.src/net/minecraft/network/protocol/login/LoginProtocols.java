/*    */ package net.minecraft.network.protocol.login;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.ProtocolInfo;
/*    */ import net.minecraft.network.protocol.ProtocolInfoBuilder;
/*    */ import net.minecraft.network.protocol.SimpleUnboundProtocol;
/*    */ import net.minecraft.network.protocol.cookie.ClientboundCookieRequestPacket;
/*    */ import net.minecraft.network.protocol.cookie.CookiePacketTypes;
/*    */ import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
/*    */ 
/*    */ public class LoginProtocols {
/*    */   static {
/* 13 */     SERVERBOUND_TEMPLATE = ProtocolInfoBuilder.serverboundProtocol(ConnectionProtocol.LOGIN, paramProtocolInfoBuilder -> paramProtocolInfoBuilder.addPacket(LoginPacketTypes.SERVERBOUND_HELLO, ServerboundHelloPacket.STREAM_CODEC).addPacket(LoginPacketTypes.SERVERBOUND_KEY, ServerboundKeyPacket.STREAM_CODEC).addPacket(LoginPacketTypes.SERVERBOUND_CUSTOM_QUERY_ANSWER, ServerboundCustomQueryAnswerPacket.STREAM_CODEC).addPacket(LoginPacketTypes.SERVERBOUND_LOGIN_ACKNOWLEDGED, ServerboundLoginAcknowledgedPacket.STREAM_CODEC).addPacket(CookiePacketTypes.SERVERBOUND_COOKIE_RESPONSE, ServerboundCookieResponsePacket.STREAM_CODEC));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final SimpleUnboundProtocol<ServerLoginPacketListener, FriendlyByteBuf> SERVERBOUND_TEMPLATE;
/*    */ 
/*    */   
/* 21 */   public static final ProtocolInfo<ServerLoginPacketListener> SERVERBOUND = SERVERBOUND_TEMPLATE.bind(FriendlyByteBuf::new);
/*    */   public static final SimpleUnboundProtocol<ClientLoginPacketListener, FriendlyByteBuf> CLIENTBOUND_TEMPLATE;
/*    */   
/*    */   static {
/* 25 */     CLIENTBOUND_TEMPLATE = ProtocolInfoBuilder.clientboundProtocol(ConnectionProtocol.LOGIN, paramProtocolInfoBuilder -> paramProtocolInfoBuilder.addPacket(LoginPacketTypes.CLIENTBOUND_LOGIN_DISCONNECT, ClientboundLoginDisconnectPacket.STREAM_CODEC).addPacket(LoginPacketTypes.CLIENTBOUND_HELLO, ClientboundHelloPacket.STREAM_CODEC).addPacket(LoginPacketTypes.CLIENTBOUND_LOGIN_FINISHED, ClientboundLoginFinishedPacket.STREAM_CODEC).addPacket(LoginPacketTypes.CLIENTBOUND_LOGIN_COMPRESSION, ClientboundLoginCompressionPacket.STREAM_CODEC).addPacket(LoginPacketTypes.CLIENTBOUND_CUSTOM_QUERY, ClientboundCustomQueryPacket.STREAM_CODEC).addPacket(CookiePacketTypes.CLIENTBOUND_COOKIE_REQUEST, ClientboundCookieRequestPacket.STREAM_CODEC));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public static final ProtocolInfo<ClientLoginPacketListener> CLIENTBOUND = CLIENTBOUND_TEMPLATE.bind(FriendlyByteBuf::new);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\login\LoginProtocols.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */