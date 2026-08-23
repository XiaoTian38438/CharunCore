/*    */ package net.minecraft.network;
/*    */ 
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.PacketUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface PacketListener
/*    */ {
/*    */   default void onPacketError(Packet paramPacket, Exception paramException) throws ReportedException {
/* 19 */     throw PacketUtils.makeReportedException(paramException, paramPacket, this);
/*    */   }
/*    */   
/*    */   default DisconnectionDetails createDisconnectionInfo(Component paramComponent, Throwable paramThrowable) {
/* 23 */     return new DisconnectionDetails(paramComponent);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean shouldHandleMessage(Packet<?> paramPacket) {
/* 29 */     return isAcceptingMessages();
/*    */   }
/*    */   
/*    */   default void fillCrashReport(CrashReport paramCrashReport) {
/* 33 */     CrashReportCategory crashReportCategory = paramCrashReport.addCategory("Connection");
/* 34 */     crashReportCategory.setDetail("Protocol", () -> protocol().id());
/* 35 */     crashReportCategory.setDetail("Flow", () -> flow().toString());
/* 36 */     fillListenerSpecificCrashDetails(paramCrashReport, crashReportCategory);
/*    */   }
/*    */   
/*    */   default void fillListenerSpecificCrashDetails(CrashReport paramCrashReport, CrashReportCategory paramCrashReportCategory) {}
/*    */   
/*    */   PacketFlow flow();
/*    */   
/*    */   ConnectionProtocol protocol();
/*    */   
/*    */   void onDisconnect(DisconnectionDetails paramDisconnectionDetails);
/*    */   
/*    */   boolean isAcceptingMessages();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\PacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */