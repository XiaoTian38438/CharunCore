/*    */ package net.minecraft.world.entity.ai.behavior.declarative;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.behavior.OneShot;
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
/*    */ class null
/*    */   extends OneShot<E>
/*    */ {
/*    */   public boolean trigger(ServerLevel paramServerLevel, E paramE, long paramLong) {
/* 48 */     Trigger trigger = (Trigger)resolvedBuilder.tryTrigger(paramServerLevel, paramE, paramLong);
/* 49 */     if (trigger == null) {
/* 50 */       return false;
/*    */     }
/*    */     
/* 53 */     return trigger.trigger(paramServerLevel, paramE, paramLong);
/*    */   }
/*    */ 
/*    */   
/*    */   public String debugString() {
/* 58 */     return "OneShot[" + resolvedBuilder.debugString() + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 63 */     return debugString();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */