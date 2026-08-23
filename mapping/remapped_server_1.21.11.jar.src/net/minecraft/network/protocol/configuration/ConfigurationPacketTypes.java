/*    */ package net.minecraft.network.protocol.configuration;
/*    */ 
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ConfigurationPacketTypes
/*    */ {
/*  9 */   public static final PacketType<ClientboundCodeOfConductPacket> CLIENTBOUND_CODE_OF_CONDUCT = createClientbound("code_of_conduct");
/* 10 */   public static final PacketType<ClientboundFinishConfigurationPacket> CLIENTBOUND_FINISH_CONFIGURATION = createClientbound("finish_configuration");
/* 11 */   public static final PacketType<ClientboundRegistryDataPacket> CLIENTBOUND_REGISTRY_DATA = createClientbound("registry_data");
/* 12 */   public static final PacketType<ClientboundResetChatPacket> CLIENTBOUND_RESET_CHAT = createClientbound("reset_chat");
/* 13 */   public static final PacketType<ClientboundSelectKnownPacks> CLIENTBOUND_SELECT_KNOWN_PACKS = createClientbound("select_known_packs");
/* 14 */   public static final PacketType<ClientboundUpdateEnabledFeaturesPacket> CLIENTBOUND_UPDATE_ENABLED_FEATURES = createClientbound("update_enabled_features");
/*    */   
/* 16 */   public static final PacketType<ServerboundAcceptCodeOfConductPacket> SERVERBOUND_ACCEPT_CODE_OF_CONDUCT = createServerbound("accept_code_of_conduct");
/* 17 */   public static final PacketType<ServerboundFinishConfigurationPacket> SERVERBOUND_FINISH_CONFIGURATION = createServerbound("finish_configuration");
/* 18 */   public static final PacketType<ServerboundSelectKnownPacks> SERVERBOUND_SELECT_KNOWN_PACKS = createServerbound("select_known_packs");
/*    */   
/*    */   private static <T extends net.minecraft.network.protocol.Packet<ClientConfigurationPacketListener>> PacketType<T> createClientbound(String paramString) {
/* 21 */     return new PacketType(PacketFlow.CLIENTBOUND, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   private static <T extends net.minecraft.network.protocol.Packet<ServerConfigurationPacketListener>> PacketType<T> createServerbound(String paramString) {
/* 25 */     return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\configuration\ConfigurationPacketTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */