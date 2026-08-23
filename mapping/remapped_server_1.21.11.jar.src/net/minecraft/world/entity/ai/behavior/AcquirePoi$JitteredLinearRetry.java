/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class JitteredLinearRetry
/*     */ {
/*     */   private static final int MIN_INTERVAL_INCREASE = 40;
/*     */   private static final int MAX_INTERVAL_INCREASE = 80;
/*     */   private static final int MAX_RETRY_PATHFINDING_INTERVAL = 400;
/*     */   private final RandomSource random;
/*     */   private long previousAttemptTimestamp;
/*     */   private long nextScheduledAttemptTimestamp;
/*     */   private int currentDelay;
/*     */   
/*     */   JitteredLinearRetry(RandomSource paramRandomSource, long paramLong) {
/* 151 */     this.random = paramRandomSource;
/* 152 */     markAttempt(paramLong);
/*     */   }
/*     */   
/*     */   public void markAttempt(long paramLong) {
/* 156 */     this.previousAttemptTimestamp = paramLong;
/* 157 */     int i = this.currentDelay + this.random.nextInt(40) + 40;
/* 158 */     this.currentDelay = Math.min(i, 400);
/* 159 */     this.nextScheduledAttemptTimestamp = paramLong + this.currentDelay;
/*     */   }
/*     */   
/*     */   public boolean isStillValid(long paramLong) {
/* 163 */     return (paramLong - this.previousAttemptTimestamp < 400L);
/*     */   }
/*     */   
/*     */   public boolean shouldRetry(long paramLong) {
/* 167 */     return (paramLong >= this.nextScheduledAttemptTimestamp);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 172 */     return "RetryMarker{, previousAttemptAt=" + this.previousAttemptTimestamp + ", nextScheduledAttemptAt=" + this.nextScheduledAttemptTimestamp + ", currentDelay=" + this.currentDelay + "}";
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\AcquirePoi$JitteredLinearRetry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */