/*    */ package net.minecraft.world.level.timers;
/*    */ 
/*    */ import com.google.common.primitives.UnsignedLong;
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
/*    */ public class Event<T>
/*    */ {
/*    */   public final long triggerTime;
/*    */   public final UnsignedLong sequentialId;
/*    */   public final String id;
/*    */   public final TimerCallback<T> callback;
/*    */   
/*    */   Event(long paramLong, UnsignedLong paramUnsignedLong, String paramString, TimerCallback<T> paramTimerCallback) {
/* 35 */     this.triggerTime = paramLong;
/* 36 */     this.sequentialId = paramUnsignedLong;
/* 37 */     this.id = paramString;
/* 38 */     this.callback = paramTimerCallback;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\timers\TimerQueue$Event.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */