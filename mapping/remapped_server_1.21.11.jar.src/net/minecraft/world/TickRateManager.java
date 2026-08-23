/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.util.TimeUtil;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ 
/*    */ public class TickRateManager
/*    */ {
/*    */   public static final float MIN_TICKRATE = 1.0F;
/* 10 */   protected float tickrate = 20.0F;
/* 11 */   protected long nanosecondsPerTick = TimeUtil.NANOSECONDS_PER_SECOND / 20L;
/* 12 */   protected int frozenTicksToRun = 0;
/*    */   protected boolean runGameElements = true;
/*    */   protected boolean isFrozen = false;
/*    */   
/*    */   public void setTickRate(float paramFloat) {
/* 17 */     this.tickrate = Math.max(paramFloat, 1.0F);
/* 18 */     this.nanosecondsPerTick = (long)(TimeUtil.NANOSECONDS_PER_SECOND / this.tickrate);
/*    */   }
/*    */   
/*    */   public float tickrate() {
/* 22 */     return this.tickrate;
/*    */   }
/*    */   
/*    */   public float millisecondsPerTick() {
/* 26 */     return (float)this.nanosecondsPerTick / (float)TimeUtil.NANOSECONDS_PER_MILLISECOND;
/*    */   }
/*    */   
/*    */   public long nanosecondsPerTick() {
/* 30 */     return this.nanosecondsPerTick;
/*    */   }
/*    */   
/*    */   public boolean runsNormally() {
/* 34 */     return this.runGameElements;
/*    */   }
/*    */   
/*    */   public boolean isSteppingForward() {
/* 38 */     return (this.frozenTicksToRun > 0);
/*    */   }
/*    */   
/*    */   public void setFrozenTicksToRun(int paramInt) {
/* 42 */     this.frozenTicksToRun = paramInt;
/*    */   }
/*    */   
/*    */   public int frozenTicksToRun() {
/* 46 */     return this.frozenTicksToRun;
/*    */   }
/*    */   
/*    */   public void setFrozen(boolean paramBoolean) {
/* 50 */     this.isFrozen = paramBoolean;
/*    */   }
/*    */   
/*    */   public boolean isFrozen() {
/* 54 */     return this.isFrozen;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 58 */     this.runGameElements = (!this.isFrozen || this.frozenTicksToRun > 0);
/* 59 */     if (this.frozenTicksToRun > 0) {
/* 60 */       this.frozenTicksToRun--;
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isEntityFrozen(Entity paramEntity) {
/* 65 */     return (!runsNormally() && !(paramEntity instanceof net.minecraft.world.entity.player.Player) && paramEntity.countPlayerPassengers() <= 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\TickRateManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */