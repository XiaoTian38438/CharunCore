/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DoNothing
/*    */   implements BehaviorControl<LivingEntity>
/*    */ {
/*    */   private final int minDuration;
/*    */   private final int maxDuration;
/* 15 */   private Behavior.Status status = Behavior.Status.STOPPED;
/*    */   private long endTimestamp;
/*    */   
/*    */   public DoNothing(int paramInt1, int paramInt2) {
/* 19 */     this.minDuration = paramInt1;
/* 20 */     this.maxDuration = paramInt2;
/*    */   }
/*    */ 
/*    */   
/*    */   public Behavior.Status getStatus() {
/* 25 */     return this.status;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean tryStart(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, long paramLong) {
/* 30 */     this.status = Behavior.Status.RUNNING;
/* 31 */     int i = this.minDuration + paramServerLevel.getRandom().nextInt(this.maxDuration + 1 - this.minDuration);
/* 32 */     this.endTimestamp = paramLong + i;
/* 33 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void tickOrStop(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, long paramLong) {
/* 38 */     if (paramLong > this.endTimestamp) {
/* 39 */       doStop(paramServerLevel, paramLivingEntity, paramLong);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public final void doStop(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, long paramLong) {
/* 45 */     this.status = Behavior.Status.STOPPED;
/*    */   }
/*    */ 
/*    */   
/*    */   public String debugString() {
/* 50 */     return getClass().getSimpleName();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\DoNothing.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */