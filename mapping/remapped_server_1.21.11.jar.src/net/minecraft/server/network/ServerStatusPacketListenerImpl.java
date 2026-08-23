/*    */ package net.minecraft.server.network;
/*    */ import net.minecraft.network.Connection;
/*    */ import net.minecraft.network.DisconnectionDetails;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
/*    */ import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
/*    */ import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
/*    */ import net.minecraft.network.protocol.status.ServerStatus;
/*    */ import net.minecraft.network.protocol.status.ServerStatusPacketListener;
/*    */ import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket;
/*    */ 
/*    */ public class ServerStatusPacketListenerImpl implements ServerStatusPacketListener {
/* 14 */   private static final Component DISCONNECT_REASON = (Component)Component.translatable("multiplayer.status.request_handled");
/*    */   
/*    */   private final ServerStatus status;
/*    */   private final Connection connection;
/*    */   private boolean hasRequestedStatus;
/*    */   
/*    */   public ServerStatusPacketListenerImpl(ServerStatus paramServerStatus, Connection paramConnection) {
/* 21 */     this.status = paramServerStatus;
/* 22 */     this.connection = paramConnection;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisconnect(DisconnectionDetails paramDisconnectionDetails) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isAcceptingMessages() {
/* 32 */     return this.connection.isConnected();
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleStatusRequest(ServerboundStatusRequestPacket paramServerboundStatusRequestPacket) {
/* 37 */     if (this.hasRequestedStatus) {
/* 38 */       this.connection.disconnect(DISCONNECT_REASON);
/*    */       return;
/*    */     } 
/* 41 */     this.hasRequestedStatus = true;
/* 42 */     this.connection.send((Packet)new ClientboundStatusResponsePacket(this.status));
/*    */   }
/*    */ 
/*    */   
/*    */   public void handlePingRequest(ServerboundPingRequestPacket paramServerboundPingRequestPacket) {
/* 47 */     this.connection.send((Packet)new ClientboundPongResponsePacket(paramServerboundPingRequestPacket.getTime()));
/* 48 */     this.connection.disconnect(DISCONNECT_REASON);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\ServerStatusPacketListenerImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */