/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.core.Direction;
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
/*    */ public class SegmentedAnglePrecision
/*    */ {
/*    */   private final int mask;
/*    */   private final int precision;
/*    */   private final float degreeToAngle;
/*    */   private final float angleToDegree;
/*    */   
/*    */   public SegmentedAnglePrecision(int paramInt) {
/* 24 */     if (paramInt < 2) {
/* 25 */       throw new IllegalArgumentException("Precision cannot be less than 2 bits");
/*    */     }
/* 27 */     if (paramInt > 30) {
/* 28 */       throw new IllegalArgumentException("Precision cannot be greater than 30 bits");
/*    */     }
/*    */     
/* 31 */     int i = 1 << paramInt;
/* 32 */     this.mask = i - 1;
/* 33 */     this.precision = paramInt;
/* 34 */     this.degreeToAngle = i / 360.0F;
/* 35 */     this.angleToDegree = 360.0F / i;
/*    */   }
/*    */   
/*    */   public boolean isSameAxis(int paramInt1, int paramInt2) {
/* 39 */     int i = getMask() >> 1;
/*    */     
/* 41 */     return ((paramInt1 & i) == (paramInt2 & i));
/*    */   }
/*    */   
/*    */   public int fromDirection(Direction paramDirection) {
/* 45 */     if (paramDirection.getAxis().isVertical()) {
/* 46 */       return 0;
/*    */     }
/* 48 */     int i = paramDirection.get2DDataValue();
/* 49 */     return i << this.precision - 2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int fromDegreesWithTurns(float paramFloat) {
/* 58 */     return Math.round(paramFloat * this.degreeToAngle);
/*    */   }
/*    */   
/*    */   public int fromDegrees(float paramFloat) {
/* 62 */     return normalize(fromDegreesWithTurns(paramFloat));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float toDegreesWithTurns(int paramInt) {
/* 71 */     return paramInt * this.angleToDegree;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float toDegrees(int paramInt) {
/* 80 */     float f = toDegreesWithTurns(normalize(paramInt));
/* 81 */     return (f >= 180.0F) ? (f - 360.0F) : f;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int normalize(int paramInt) {
/* 90 */     return paramInt & this.mask;
/*    */   }
/*    */   
/*    */   public int getMask() {
/* 94 */     return this.mask;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\SegmentedAnglePrecision.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */