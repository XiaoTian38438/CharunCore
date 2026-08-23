/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SumAggregation
/*    */ {
/* 65 */   private final AtomicLong sentBytes = new AtomicLong();
/* 66 */   private final AtomicInteger sentPackets = new AtomicInteger();
/* 67 */   private final AtomicLong receivedBytes = new AtomicLong();
/* 68 */   private final AtomicInteger receivedPackets = new AtomicInteger();
/*    */   private final NetworkSummaryEvent event;
/*    */   
/*    */   public SumAggregation(String paramString) {
/* 72 */     this.event = new NetworkSummaryEvent(paramString);
/* 73 */     this.event.begin();
/*    */   }
/*    */   
/*    */   public void trackSentPacket(int paramInt) {
/* 77 */     this.sentPackets.incrementAndGet();
/* 78 */     this.sentBytes.addAndGet(paramInt);
/*    */   }
/*    */   
/*    */   public void trackReceivedPacket(int paramInt) {
/* 82 */     this.receivedPackets.incrementAndGet();
/* 83 */     this.receivedBytes.addAndGet(paramInt);
/*    */   }
/*    */   
/*    */   public void commitEvent() {
/* 87 */     this.event.sentBytes = this.sentBytes.get();
/* 88 */     this.event.sentPackets = this.sentPackets.get();
/* 89 */     this.event.receivedBytes = this.receivedBytes.get();
/* 90 */     this.event.receivedPackets = this.receivedPackets.get();
/* 91 */     this.event.commit();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\jfr\event\NetworkSummaryEvent$SumAggregation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */