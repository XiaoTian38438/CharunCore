/*    */ package net.minecraft.util.profiling.metrics.storage;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import net.minecraft.util.profiling.ProfileResults;
/*    */ 
/*    */ public final class RecordedDeviation
/*    */ {
/*    */   public final Instant timestamp;
/*    */   public final int tick;
/*    */   public final ProfileResults profilerResultAtTick;
/*    */   
/*    */   public RecordedDeviation(Instant paramInstant, int paramInt, ProfileResults paramProfileResults) {
/* 13 */     this.timestamp = paramInstant;
/* 14 */     this.tick = paramInt;
/* 15 */     this.profilerResultAtTick = paramProfileResults;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\metrics\storage\RecordedDeviation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */