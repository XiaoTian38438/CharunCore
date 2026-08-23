/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ 
/*    */ public class LegacyProtocolUtils
/*    */ {
/*    */   public static final int CUSTOM_PAYLOAD_PACKET_ID = 250;
/*    */   public static final String CUSTOM_PAYLOAD_PACKET_PING_CHANNEL = "MC|PingHost";
/*    */   public static final int GET_INFO_PACKET_ID = 254;
/*    */   public static final int GET_INFO_PACKET_VERSION_1 = 1;
/*    */   public static final int DISCONNECT_PACKET_ID = 255;
/*    */   public static final int FAKE_PROTOCOL_VERSION = 127;
/*    */   
/*    */   public static void writeLegacyString(ByteBuf paramByteBuf, String paramString) {
/* 16 */     paramByteBuf.writeShort(paramString.length());
/* 17 */     paramByteBuf.writeCharSequence(paramString, StandardCharsets.UTF_16BE);
/*    */   }
/*    */   
/*    */   public static String readLegacyString(ByteBuf paramByteBuf) {
/* 21 */     short s = paramByteBuf.readShort();
/* 22 */     int i = s * 2;
/* 23 */     String str = paramByteBuf.toString(paramByteBuf.readerIndex(), i, StandardCharsets.UTF_16BE);
/* 24 */     paramByteBuf.skipBytes(i);
/* 25 */     return str;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\LegacyProtocolUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */