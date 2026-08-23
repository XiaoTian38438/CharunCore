/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class StatusPacketTypes
/*    */ {
/*  9 */   public static final PacketType<ClientboundStatusResponsePacket> CLIENTBOUND_STATUS_RESPONSE = createClientbound("status_response");
/*    */   
/* 11 */   public static final PacketType<ServerboundStatusRequestPacket> SERVERBOUND_STATUS_REQUEST = createServerbound("status_request");
/*    */   
/*    */   private static <T extends net.minecraft.network.protocol.Packet<ClientStatusPacketListener>> PacketType<T> createClientbound(String paramString) {
/* 14 */     return new PacketType(PacketFlow.CLIENTBOUND, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   private static <T extends net.minecraft.network.protocol.Packet<ServerStatusPacketListener>> PacketType<T> createServerbound(String paramString) {
/* 18 */     return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\status\StatusPacketTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */