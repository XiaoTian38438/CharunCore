/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class BinaryAnimator
/*    */ {
/*    */   private final int animationLength;
/*    */   private final EasingType easing;
/*    */   private int ticks;
/*    */   private int ticksOld;
/*    */   
/*    */   public BinaryAnimator(int paramInt, EasingType paramEasingType) {
/* 11 */     this.animationLength = paramInt;
/* 12 */     this.easing = paramEasingType;
/*    */   }
/*    */   
/*    */   public BinaryAnimator(int paramInt) {
/* 16 */     this(paramInt, EasingType.LINEAR);
/*    */   }
/*    */   
/*    */   public void tick(boolean paramBoolean) {
/* 20 */     this.ticksOld = this.ticks;
/* 21 */     if (paramBoolean) {
/* 22 */       if (this.ticks < this.animationLength) {
/* 23 */         this.ticks++;
/*    */       }
/*    */     }
/* 26 */     else if (this.ticks > 0) {
/* 27 */       this.ticks--;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFactor(float paramFloat) {
/* 33 */     float f = Mth.lerp(paramFloat, this.ticksOld, this.ticks) / this.animationLength;
/* 34 */     return this.easing.apply(f);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\BinaryAnimator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */