/*    */ package net.minecraft.gizmos;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class SimpleGizmoCollector
/*    */   implements GizmoCollector {
/* 11 */   private final List<GizmoInstance> gizmos = new ArrayList<>();
/* 12 */   private final List<GizmoInstance> temporaryGizmos = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public GizmoProperties add(Gizmo paramGizmo) {
/* 16 */     GizmoInstance gizmoInstance = new GizmoInstance(paramGizmo);
/* 17 */     this.gizmos.add(gizmoInstance);
/* 18 */     return gizmoInstance;
/*    */   }
/*    */   
/*    */   public List<GizmoInstance> drainGizmos() {
/* 22 */     ArrayList<GizmoInstance> arrayList = new ArrayList<>(this.gizmos);
/* 23 */     arrayList.addAll(this.temporaryGizmos);
/* 24 */     long l = Util.getMillis();
/* 25 */     this.gizmos.removeIf(paramGizmoInstance -> (paramGizmoInstance.getExpireTimeMillis() < paramLong));
/* 26 */     this.temporaryGizmos.clear();
/* 27 */     return arrayList;
/*    */   }
/*    */   
/*    */   public List<GizmoInstance> getGizmos() {
/* 31 */     return this.gizmos;
/*    */   }
/*    */   
/*    */   public void addTemporaryGizmos(Collection<GizmoInstance> paramCollection) {
/* 35 */     this.temporaryGizmos.addAll(paramCollection);
/*    */   }
/*    */   
/*    */   public static class GizmoInstance implements GizmoProperties {
/*    */     private final Gizmo gizmo;
/*    */     private boolean isAlwaysOnTop;
/*    */     private long startTimeMillis;
/*    */     private long expireTimeMillis;
/*    */     private boolean shouldFadeOut;
/*    */     
/*    */     GizmoInstance(Gizmo param1Gizmo) {
/* 46 */       this.gizmo = param1Gizmo;
/*    */     }
/*    */ 
/*    */     
/*    */     public GizmoProperties setAlwaysOnTop() {
/* 51 */       this.isAlwaysOnTop = true;
/* 52 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public GizmoProperties persistForMillis(int param1Int) {
/* 57 */       this.startTimeMillis = Util.getMillis();
/* 58 */       this.expireTimeMillis = this.startTimeMillis + param1Int;
/* 59 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public GizmoProperties fadeOut() {
/* 64 */       this.shouldFadeOut = true;
/* 65 */       return this;
/*    */     }
/*    */     
/*    */     public float getAlphaMultiplier(long param1Long) {
/* 69 */       if (this.shouldFadeOut) {
/* 70 */         long l1 = this.expireTimeMillis - this.startTimeMillis;
/* 71 */         long l2 = param1Long - this.startTimeMillis;
/* 72 */         return 1.0F - Mth.clamp((float)l2 / (float)l1, 0.0F, 1.0F);
/*    */       } 
/* 74 */       return 1.0F;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isAlwaysOnTop() {
/* 79 */       return this.isAlwaysOnTop;
/*    */     }
/*    */     
/*    */     public long getExpireTimeMillis() {
/* 83 */       return this.expireTimeMillis;
/*    */     }
/*    */     
/*    */     public Gizmo gizmo() {
/* 87 */       return this.gizmo;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gizmos\SimpleGizmoCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */