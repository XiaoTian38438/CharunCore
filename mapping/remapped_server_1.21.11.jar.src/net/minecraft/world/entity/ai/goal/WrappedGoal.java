/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ 
/*     */ public class WrappedGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Goal goal;
/*     */   private final int priority;
/*     */   private boolean isRunning;
/*     */   
/*     */   public WrappedGoal(int paramInt, Goal paramGoal) {
/*  13 */     this.priority = paramInt;
/*  14 */     this.goal = paramGoal;
/*     */   }
/*     */   
/*     */   public boolean canBeReplacedBy(WrappedGoal paramWrappedGoal) {
/*  18 */     return (isInterruptable() && paramWrappedGoal.getPriority() < getPriority());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  23 */     return this.goal.canUse();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  28 */     return this.goal.canContinueToUse();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterruptable() {
/*  33 */     return this.goal.isInterruptable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  38 */     if (this.isRunning) {
/*     */       return;
/*     */     }
/*  41 */     this.isRunning = true;
/*  42 */     this.goal.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  47 */     if (!this.isRunning) {
/*     */       return;
/*     */     }
/*  50 */     this.isRunning = false;
/*  51 */     this.goal.stop();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/*  56 */     return this.goal.requiresUpdateEveryTick();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int adjustedTickDelay(int paramInt) {
/*  61 */     return this.goal.adjustedTickDelay(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  66 */     this.goal.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFlags(EnumSet<Goal.Flag> paramEnumSet) {
/*  71 */     this.goal.setFlags(paramEnumSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumSet<Goal.Flag> getFlags() {
/*  76 */     return this.goal.getFlags();
/*     */   }
/*     */   
/*     */   public boolean isRunning() {
/*  80 */     return this.isRunning;
/*     */   }
/*     */   
/*     */   public int getPriority() {
/*  84 */     return this.priority;
/*     */   }
/*     */   
/*     */   public Goal getGoal() {
/*  88 */     return this.goal;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  93 */     if (this == paramObject) {
/*  94 */       return true;
/*     */     }
/*  96 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/*  97 */       return false;
/*     */     }
/*  99 */     return this.goal.equals(((WrappedGoal)paramObject).goal);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 104 */     return this.goal.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\WrappedGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */