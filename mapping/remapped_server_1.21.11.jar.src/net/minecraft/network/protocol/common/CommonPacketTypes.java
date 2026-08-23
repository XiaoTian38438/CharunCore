/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class CommonPacketTypes
/*    */ {
/*  9 */   public static final PacketType<ClientboundClearDialogPacket> CLIENTBOUND_CLEAR_DIALOG = createClientbound("clear_dialog");
/* 10 */   public static final PacketType<ClientboundCustomPayloadPacket> CLIENTBOUND_CUSTOM_PAYLOAD = createClientbound("custom_payload");
/* 11 */   public static final PacketType<ClientboundCustomReportDetailsPacket> CLIENTBOUND_CUSTOM_REPORT_DETAILS = createClientbound("custom_report_details");
/* 12 */   public static final PacketType<ClientboundDisconnectPacket> CLIENTBOUND_DISCONNECT = createClientbound("disconnect");
/* 13 */   public static final PacketType<ClientboundKeepAlivePacket> CLIENTBOUND_KEEP_ALIVE = createClientbound("keep_alive");
/* 14 */   public static final PacketType<ClientboundPingPacket> CLIENTBOUND_PING = createClientbound("ping");
/* 15 */   public static final PacketType<ClientboundResourcePackPopPacket> CLIENTBOUND_RESOURCE_PACK_POP = createClientbound("resource_pack_pop");
/* 16 */   public static final PacketType<ClientboundResourcePackPushPacket> CLIENTBOUND_RESOURCE_PACK_PUSH = createClientbound("resource_pack_push");
/* 17 */   public static final PacketType<ClientboundServerLinksPacket> CLIENTBOUND_SERVER_LINKS = createClientbound("server_links");
/* 18 */   public static final PacketType<ClientboundShowDialogPacket> CLIENTBOUND_SHOW_DIALOG = createClientbound("show_dialog");
/* 19 */   public static final PacketType<ClientboundStoreCookiePacket> CLIENTBOUND_STORE_COOKIE = createClientbound("store_cookie");
/* 20 */   public static final PacketType<ClientboundTransferPacket> CLIENTBOUND_TRANSFER = createClientbound("transfer");
/* 21 */   public static final PacketType<ClientboundUpdateTagsPacket> CLIENTBOUND_UPDATE_TAGS = createClientbound("update_tags");
/*    */   
/* 23 */   public static final PacketType<ServerboundClientInformationPacket> SERVERBOUND_CLIENT_INFORMATION = createServerbound("client_information");
/* 24 */   public static final PacketType<ServerboundCustomPayloadPacket> SERVERBOUND_CUSTOM_PAYLOAD = createServerbound("custom_payload");
/* 25 */   public static final PacketType<ServerboundKeepAlivePacket> SERVERBOUND_KEEP_ALIVE = createServerbound("keep_alive");
/* 26 */   public static final PacketType<ServerboundPongPacket> SERVERBOUND_PONG = createServerbound("pong");
/* 27 */   public static final PacketType<ServerboundResourcePackPacket> SERVERBOUND_RESOURCE_PACK = createServerbound("resource_pack");
/* 28 */   public static final PacketType<ServerboundCustomClickActionPacket> SERVERBOUND_CUSTOM_CLICK_ACTION = createServerbound("custom_click_action");
/*    */   
/*    */   private static <T extends net.minecraft.network.protocol.Packet<ClientCommonPacketListener>> PacketType<T> createClientbound(String paramString) {
/* 31 */     return new PacketType(PacketFlow.CLIENTBOUND, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   private static <T extends net.minecraft.network.protocol.Packet<ServerCommonPacketListener>> PacketType<T> createServerbound(String paramString) {
/* 35 */     return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\common\CommonPacketTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */