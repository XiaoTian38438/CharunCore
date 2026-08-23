/*    */ package net.minecraft.network.protocol.handshake;
/*    */ 
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class HandshakePacketTypes
/*    */ {
/*  9 */   public static final PacketType<ClientIntentionPacket> CLIENT_INTENTION = createServerbound("intention");
/*    */   
/*    */   private static <T extends net.minecraft.network.protocol.Packet<ServerHandshakePacketListener>> PacketType<T> createServerbound(String paramString) {
/* 12 */     return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\handshake\HandshakePacketTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */