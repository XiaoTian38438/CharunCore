/*    */ package net.minecraft.network;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import net.minecraft.util.debugchart.LocalSampleLogger;
/*    */ 
/*    */ public class BandwidthDebugMonitor
/*    */ {
/*  8 */   private final AtomicInteger bytesReceived = new AtomicInteger();
/*    */   private final LocalSampleLogger bandwidthLogger;
/*    */   
/*    */   public BandwidthDebugMonitor(LocalSampleLogger paramLocalSampleLogger) {
/* 12 */     this.bandwidthLogger = paramLocalSampleLogger;
/*    */   }
/*    */   
/*    */   public void onReceive(int paramInt) {
/* 16 */     this.bytesReceived.getAndAdd(paramInt);
/*    */   }
/*    */   
/*    */   public void tick() {
/* 20 */     this.bandwidthLogger.logSample(this.bytesReceived.getAndSet(0));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\BandwidthDebugMonitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */