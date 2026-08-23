/*    */ package net.minecraft.gizmos;
/*    */ 
/*    */ 
/*    */ public interface GizmoCollector
/*    */ {
/*  6 */   public static final GizmoProperties IGNORED = new GizmoProperties()
/*    */     {
/*    */       public GizmoProperties setAlwaysOnTop()
/*    */       {
/* 10 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public GizmoProperties persistForMillis(int param1Int) {
/* 15 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public GizmoProperties fadeOut() {
/* 20 */         return this;
/*    */       }
/*    */     };
/*    */   
/*    */   public static final GizmoCollector NOOP = paramGizmo -> IGNORED;
/*    */   
/*    */   GizmoProperties add(Gizmo paramGizmo);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gizmos\GizmoCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */