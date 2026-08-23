/*    */ package net.minecraft.util.debugchart;
/*    */ 
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundDebugSamplePacket;
/*    */ import net.minecraft.util.debug.ServerDebugSubscribers;
/*    */ 
/*    */ public class RemoteSampleLogger extends AbstractSampleLogger {
/*    */   private final ServerDebugSubscribers subscribers;
/*    */   
/*    */   public RemoteSampleLogger(int paramInt, ServerDebugSubscribers paramServerDebugSubscribers, RemoteDebugSampleType paramRemoteDebugSampleType) {
/* 11 */     this(paramInt, paramServerDebugSubscribers, paramRemoteDebugSampleType, new long[paramInt]);
/*    */   }
/*    */   private final RemoteDebugSampleType sampleType;
/*    */   public RemoteSampleLogger(int paramInt, ServerDebugSubscribers paramServerDebugSubscribers, RemoteDebugSampleType paramRemoteDebugSampleType, long[] paramArrayOflong) {
/* 15 */     super(paramInt, paramArrayOflong);
/* 16 */     this.subscribers = paramServerDebugSubscribers;
/* 17 */     this.sampleType = paramRemoteDebugSampleType;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void useSample() {
/* 22 */     if (this.subscribers.hasAnySubscriberFor(this.sampleType.subscription()))
/* 23 */       this.subscribers.broadcastToAll(this.sampleType.subscription(), (Packet)new ClientboundDebugSamplePacket((long[])this.sample.clone(), this.sampleType)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\debugchart\RemoteSampleLogger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */