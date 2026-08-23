/*    */ package com.mojang.math;
/*    */ @FunctionalInterface
/*    */ public interface Axis {
/*    */   public static final Axis XN;
/*    */   public static final Axis XP;
/*    */   public static final Axis YN;
/*    */   
/*    */   static {
/*  9 */     XN = (paramFloat -> (new Quaternionf()).rotationX(-paramFloat));
/* 10 */     XP = (paramFloat -> (new Quaternionf()).rotationX(paramFloat));
/* 11 */     YN = (paramFloat -> (new Quaternionf()).rotationY(-paramFloat));
/* 12 */     YP = (paramFloat -> (new Quaternionf()).rotationY(paramFloat));
/* 13 */     ZN = (paramFloat -> (new Quaternionf()).rotationZ(-paramFloat));
/* 14 */     ZP = (paramFloat -> (new Quaternionf()).rotationZ(paramFloat));
/*    */   } public static final Axis YP; public static final Axis ZN; public static final Axis ZP;
/*    */   static Axis of(Vector3f paramVector3f) {
/* 17 */     return paramFloat -> (new Quaternionf()).rotationAxis(paramFloat, (Vector3fc)paramVector3f);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   default Quaternionf rotationDegrees(float paramFloat) {
/* 23 */     return rotation(paramFloat * 0.017453292F);
/*    */   }
/*    */   
/*    */   Quaternionf rotation(float paramFloat);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\math\Axis.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */