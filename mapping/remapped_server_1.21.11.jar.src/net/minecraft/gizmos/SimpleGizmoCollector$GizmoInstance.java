/*    */ package net.minecraft.gizmos;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.Util;
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
/*    */ public class GizmoInstance
/*    */   implements GizmoProperties
/*    */ {
/*    */   private final Gizmo gizmo;
/*    */   private boolean isAlwaysOnTop;
/*    */   private long startTimeMillis;
/*    */   private long expireTimeMillis;
/*    */   private boolean shouldFadeOut;
/*    */   
/*    */   GizmoInstance(Gizmo paramGizmo) {
/* 46 */     this.gizmo = paramGizmo;
/*    */   }
/*    */ 
/*    */   
/*    */   public GizmoProperties setAlwaysOnTop() {
/* 51 */     this.isAlwaysOnTop = true;
/* 52 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public GizmoProperties persistForMillis(int paramInt) {
/* 57 */     this.startTimeMillis = Util.getMillis();
/* 58 */     this.expireTimeMillis = this.startTimeMillis + paramInt;
/* 59 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public GizmoProperties fadeOut() {
/* 64 */     this.shouldFadeOut = true;
/* 65 */     return this;
/*    */   }
/*    */   
/*    */   public float getAlphaMultiplier(long paramLong) {
/* 69 */     if (this.shouldFadeOut) {
/* 70 */       long l1 = this.expireTimeMillis - this.startTimeMillis;
/* 71 */       long l2 = paramLong - this.startTimeMillis;
/* 72 */       return 1.0F - Mth.clamp((float)l2 / (float)l1, 0.0F, 1.0F);
/*    */     } 
/* 74 */     return 1.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAlwaysOnTop() {
/* 79 */     return this.isAlwaysOnTop;
/*    */   }
/*    */   
/*    */   public long getExpireTimeMillis() {
/* 83 */     return this.expireTimeMillis;
/*    */   }
/*    */   
/*    */   public Gizmo gizmo() {
/* 87 */     return this.gizmo;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gizmos\SimpleGizmoCollector$GizmoInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */