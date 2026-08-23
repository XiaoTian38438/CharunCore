/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import net.minecraft.network.Connection;
/*    */ import net.minecraft.network.DisconnectionDetails;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.handshake.ClientIntent;
/*    */ import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
/*    */ import net.minecraft.network.protocol.handshake.ServerHandshakePacketListener;
/*    */ import net.minecraft.network.protocol.login.LoginProtocols;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public class MemoryServerHandshakePacketListenerImpl implements ServerHandshakePacketListener {
/*    */   private final MinecraftServer server;
/*    */   
/*    */   public MemoryServerHandshakePacketListenerImpl(MinecraftServer paramMinecraftServer, Connection paramConnection) {
/* 16 */     this.server = paramMinecraftServer;
/* 17 */     this.connection = paramConnection;
/*    */   }
/*    */   private final Connection connection;
/*    */   
/*    */   public void handleIntention(ClientIntentionPacket paramClientIntentionPacket) {
/* 22 */     if (paramClientIntentionPacket.intention() != ClientIntent.LOGIN) {
/* 23 */       throw new UnsupportedOperationException("Invalid intention " + String.valueOf(paramClientIntentionPacket.intention()));
/*    */     }
/* 25 */     this.connection.setupInboundProtocol(LoginProtocols.SERVERBOUND, (PacketListener)new ServerLoginPacketListenerImpl(this.server, this.connection, false));
/*    */     
/* 27 */     this.connection.setupOutboundProtocol(LoginProtocols.CLIENTBOUND);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisconnect(DisconnectionDetails paramDisconnectionDetails) {}
/*    */ 
/*    */   
/*    */   public boolean isAcceptingMessages() {
/* 36 */     return this.connection.isConnected();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\MemoryServerHandshakePacketListenerImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */