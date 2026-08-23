/*   */ package net.minecraft.network.protocol.game;
/*   */ 
/*   */ import net.minecraft.network.protocol.BundleDelimiterPacket;
/*   */ import net.minecraft.network.protocol.PacketType;
/*   */ 
/*   */ public class ClientboundBundleDelimiterPacket
/*   */   extends BundleDelimiterPacket<ClientGamePacketListener> {
/*   */   public PacketType<ClientboundBundleDelimiterPacket> type() {
/* 9 */     return GamePacketTypes.CLIENTBOUND_BUNDLE_DELIMITER;
/*   */   }
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ClientboundBundleDelimiterPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */