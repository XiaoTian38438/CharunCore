/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ import net.minecraft.obfuscate.DontObfuscate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Name("minecraft.PacketReceived")
/*    */ @Label("Network Packet Received")
/*    */ @DontObfuscate
/*    */ public class PacketReceivedEvent
/*    */   extends PacketEvent
/*    */ {
/*    */   public static final String NAME = "minecraft.PacketReceived";
/* 20 */   public static final EventType TYPE = EventType.getEventType((Class)PacketReceivedEvent.class);
/*    */   
/*    */   public PacketReceivedEvent(String paramString1, String paramString2, String paramString3, SocketAddress paramSocketAddress, int paramInt) {
/* 23 */     super(paramString1, paramString2, paramString3, paramSocketAddress, paramInt);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\jfr\event\PacketReceivedEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */