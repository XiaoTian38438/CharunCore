/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnimationState
/*    */ {
/*    */   private static final int STOPPED = -2147483648;
/* 10 */   private int startTick = Integer.MIN_VALUE;
/*    */   
/*    */   public void start(int paramInt) {
/* 13 */     this.startTick = paramInt;
/*    */   }
/*    */   
/*    */   public void startIfStopped(int paramInt) {
/* 17 */     if (!isStarted()) {
/* 18 */       start(paramInt);
/*    */     }
/*    */   }
/*    */   
/*    */   public void animateWhen(boolean paramBoolean, int paramInt) {
/* 23 */     if (paramBoolean) {
/* 24 */       startIfStopped(paramInt);
/*    */     } else {
/* 26 */       stop();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void stop() {
/* 31 */     this.startTick = Integer.MIN_VALUE;
/*    */   }
/*    */   
/*    */   public void ifStarted(Consumer<AnimationState> paramConsumer) {
/* 35 */     if (isStarted()) {
/* 36 */       paramConsumer.accept(this);
/*    */     }
/*    */   }
/*    */   
/*    */   public void fastForward(int paramInt, float paramFloat) {
/* 41 */     if (!isStarted()) {
/*    */       return;
/*    */     }
/* 44 */     this.startTick -= (int)(paramInt * paramFloat);
/*    */   }
/*    */   
/*    */   public long getTimeInMillis(float paramFloat) {
/* 48 */     float f = paramFloat - this.startTick;
/* 49 */     return (long)(f * 50.0F);
/*    */   }
/*    */   
/*    */   public boolean isStarted() {
/* 53 */     return (this.startTick != Integer.MIN_VALUE);
/*    */   }
/*    */   
/*    */   public void copyFrom(AnimationState paramAnimationState) {
/* 57 */     this.startTick = paramAnimationState.startTick;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\AnimationState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */