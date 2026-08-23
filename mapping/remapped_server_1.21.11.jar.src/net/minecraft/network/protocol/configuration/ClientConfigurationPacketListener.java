/*   */ package net.minecraft.network.protocol.configuration;
/*   */ 
/*   */ import net.minecraft.network.ConnectionProtocol;
/*   */ import net.minecraft.network.protocol.common.ClientCommonPacketListener;
/*   */ 
/*   */ public interface ClientConfigurationPacketListener
/*   */   extends ClientCommonPacketListener {
/*   */   default ConnectionProtocol protocol() {
/* 9 */     return ConnectionProtocol.CONFIGURATION;
/*   */   }
/*   */   
/*   */   void handleCodeOfConduct(ClientboundCodeOfConductPacket paramClientboundCodeOfConductPacket);
/*   */   
/*   */   void handleConfigurationFinished(ClientboundFinishConfigurationPacket paramClientboundFinishConfigurationPacket);
/*   */   
/*   */   void handleRegistryData(ClientboundRegistryDataPacket paramClientboundRegistryDataPacket);
/*   */   
/*   */   void handleEnabledFeatures(ClientboundUpdateEnabledFeaturesPacket paramClientboundUpdateEnabledFeaturesPacket);
/*   */   
/*   */   void handleSelectKnownPacks(ClientboundSelectKnownPacks paramClientboundSelectKnownPacks);
/*   */   
/*   */   void handleResetChat(ClientboundResetChatPacket paramClientboundResetChatPacket);
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\configuration\ClientConfigurationPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */