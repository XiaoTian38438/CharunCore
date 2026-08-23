/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.PacketProcessor;
/*    */ import net.minecraft.server.RunningOnDifferentThreadException;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class PacketUtils
/*    */ {
/* 15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static <T extends PacketListener> void ensureRunningOnSameThread(Packet<T> paramPacket, T paramT, ServerLevel paramServerLevel) throws RunningOnDifferentThreadException {
/* 18 */     ensureRunningOnSameThread(paramPacket, paramT, paramServerLevel.getServer().packetProcessor());
/*    */   }
/*    */   
/*    */   public static <T extends PacketListener> void ensureRunningOnSameThread(Packet<T> paramPacket, T paramT, PacketProcessor paramPacketProcessor) throws RunningOnDifferentThreadException {
/* 22 */     if (!paramPacketProcessor.isSameThread()) {
/* 23 */       paramPacketProcessor.scheduleIfPossible((PacketListener)paramT, paramPacket);
/* 24 */       throw RunningOnDifferentThreadException.RUNNING_ON_DIFFERENT_THREAD;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static <T extends PacketListener> ReportedException makeReportedException(Exception paramException, Packet<T> paramPacket, T paramT) {
/* 29 */     if (paramException instanceof ReportedException) { ReportedException reportedException = (ReportedException)paramException;
/* 30 */       fillCrashReport(reportedException.getReport(), paramT, paramPacket);
/* 31 */       return reportedException; }
/*    */     
/* 33 */     CrashReport crashReport = CrashReport.forThrowable(paramException, "Main thread packet handler");
/* 34 */     fillCrashReport(crashReport, paramT, paramPacket);
/* 35 */     return new ReportedException(crashReport);
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T extends PacketListener> void fillCrashReport(CrashReport paramCrashReport, T paramT, Packet<T> paramPacket) {
/* 40 */     if (paramPacket != null) {
/* 41 */       CrashReportCategory crashReportCategory = paramCrashReport.addCategory("Incoming Packet");
/* 42 */       crashReportCategory.setDetail("Type", () -> paramPacket.type().toString());
/* 43 */       crashReportCategory.setDetail("Is Terminal", () -> Boolean.toString(paramPacket.isTerminal()));
/* 44 */       crashReportCategory.setDetail("Is Skippable", () -> Boolean.toString(paramPacket.isSkippable()));
/*    */     } 
/*    */     
/* 47 */     paramT.fillCrashReport(paramCrashReport);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\PacketUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */