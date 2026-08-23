/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ 
/*    */ 
/*    */ public abstract class OneShot<E extends LivingEntity>
/*    */   implements BehaviorControl<E>, Trigger<E>
/*    */ {
/* 11 */   private Behavior.Status status = Behavior.Status.STOPPED;
/*    */ 
/*    */   
/*    */   public final Behavior.Status getStatus() {
/* 15 */     return this.status;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean tryStart(ServerLevel paramServerLevel, E paramE, long paramLong) {
/* 20 */     if (trigger(paramServerLevel, (LivingEntity)paramE, paramLong)) {
/* 21 */       this.status = Behavior.Status.RUNNING;
/* 22 */       return true;
/*    */     } 
/* 24 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void tickOrStop(ServerLevel paramServerLevel, E paramE, long paramLong) {
/* 29 */     doStop(paramServerLevel, paramE, paramLong);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void doStop(ServerLevel paramServerLevel, E paramE, long paramLong) {
/* 34 */     this.status = Behavior.Status.STOPPED;
/*    */   }
/*    */ 
/*    */   
/*    */   public String debugString() {
/* 39 */     return getClass().getSimpleName();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\OneShot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */