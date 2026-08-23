/*     */ package net.minecraft.gizmos;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Gizmos {
/*  11 */   static final ThreadLocal<GizmoCollector> collector = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TemporaryCollection withCollector(GizmoCollector paramGizmoCollector) {
/*  17 */     TemporaryCollection temporaryCollection = new TemporaryCollection();
/*  18 */     collector.set(paramGizmoCollector);
/*  19 */     return temporaryCollection;
/*     */   }
/*     */   
/*     */   public static GizmoProperties addGizmo(Gizmo paramGizmo) {
/*  23 */     GizmoCollector gizmoCollector = collector.get();
/*  24 */     if (gizmoCollector == null) {
/*  25 */       throw new IllegalStateException("Gizmos cannot be created here! No GizmoCollector has been registered.");
/*     */     }
/*  27 */     return gizmoCollector.add(paramGizmo);
/*     */   }
/*     */   
/*     */   public static GizmoProperties cuboid(AABB paramAABB, GizmoStyle paramGizmoStyle) {
/*  31 */     return cuboid(paramAABB, paramGizmoStyle, false);
/*     */   }
/*     */   
/*     */   public static GizmoProperties cuboid(AABB paramAABB, GizmoStyle paramGizmoStyle, boolean paramBoolean) {
/*  35 */     return addGizmo(new CuboidGizmo(paramAABB, paramGizmoStyle, paramBoolean));
/*     */   }
/*     */   
/*     */   public static GizmoProperties cuboid(BlockPos paramBlockPos, GizmoStyle paramGizmoStyle) {
/*  39 */     return cuboid(new AABB(paramBlockPos), paramGizmoStyle);
/*     */   }
/*     */   
/*     */   public static GizmoProperties cuboid(BlockPos paramBlockPos, float paramFloat, GizmoStyle paramGizmoStyle) {
/*  43 */     return cuboid((new AABB(paramBlockPos)).inflate(paramFloat), paramGizmoStyle);
/*     */   }
/*     */   
/*     */   public static GizmoProperties circle(Vec3 paramVec3, float paramFloat, GizmoStyle paramGizmoStyle) {
/*  47 */     return addGizmo(new CircleGizmo(paramVec3, paramFloat, paramGizmoStyle));
/*     */   }
/*     */   
/*     */   public static GizmoProperties line(Vec3 paramVec31, Vec3 paramVec32, int paramInt) {
/*  51 */     return addGizmo(new LineGizmo(paramVec31, paramVec32, paramInt, 3.0F));
/*     */   }
/*     */   
/*     */   public static GizmoProperties line(Vec3 paramVec31, Vec3 paramVec32, int paramInt, float paramFloat) {
/*  55 */     return addGizmo(new LineGizmo(paramVec31, paramVec32, paramInt, paramFloat));
/*     */   }
/*     */   
/*     */   public static GizmoProperties arrow(Vec3 paramVec31, Vec3 paramVec32, int paramInt) {
/*  59 */     return addGizmo(new ArrowGizmo(paramVec31, paramVec32, paramInt, 2.5F));
/*     */   }
/*     */   
/*     */   public static GizmoProperties arrow(Vec3 paramVec31, Vec3 paramVec32, int paramInt, float paramFloat) {
/*  63 */     return addGizmo(new ArrowGizmo(paramVec31, paramVec32, paramInt, paramFloat));
/*     */   }
/*     */   
/*     */   public static GizmoProperties rect(Vec3 paramVec31, Vec3 paramVec32, Direction paramDirection, GizmoStyle paramGizmoStyle) {
/*  67 */     return addGizmo(RectGizmo.fromCuboidFace(paramVec31, paramVec32, paramDirection, paramGizmoStyle));
/*     */   }
/*     */   
/*     */   public static GizmoProperties rect(Vec3 paramVec31, Vec3 paramVec32, Vec3 paramVec33, Vec3 paramVec34, GizmoStyle paramGizmoStyle) {
/*  71 */     return addGizmo(new RectGizmo(paramVec31, paramVec32, paramVec33, paramVec34, paramGizmoStyle));
/*     */   }
/*     */   
/*     */   public static GizmoProperties point(Vec3 paramVec3, int paramInt, float paramFloat) {
/*  75 */     return addGizmo(new PointGizmo(paramVec3, paramInt, paramFloat));
/*     */   }
/*     */   
/*     */   public static GizmoProperties billboardTextOverBlock(String paramString, BlockPos paramBlockPos, int paramInt1, int paramInt2, float paramFloat) {
/*  79 */     double d1 = 1.3D;
/*  80 */     double d2 = 0.2D;
/*     */     
/*  82 */     GizmoProperties gizmoProperties = billboardText(paramString, Vec3.atLowerCornerWithOffset((Vec3i)paramBlockPos, 0.5D, 1.3D + paramInt1 * 0.2D, 0.5D), TextGizmo.Style.forColorAndCentered(paramInt2).withScale(paramFloat));
/*  83 */     gizmoProperties.setAlwaysOnTop();
/*  84 */     return gizmoProperties;
/*     */   }
/*     */   
/*     */   public static GizmoProperties billboardTextOverMob(Entity paramEntity, int paramInt1, String paramString, int paramInt2, float paramFloat) {
/*  88 */     double d1 = 2.4D;
/*  89 */     double d2 = 0.25D;
/*     */ 
/*     */ 
/*     */     
/*  93 */     double d3 = paramEntity.getBlockX() + 0.5D;
/*  94 */     double d4 = paramEntity.getY() + 2.4D + paramInt1 * 0.25D;
/*  95 */     double d5 = paramEntity.getBlockZ() + 0.5D;
/*     */     
/*  97 */     float f = 0.5F;
/*  98 */     GizmoProperties gizmoProperties = billboardText(paramString, new Vec3(d3, d4, d5), TextGizmo.Style.forColor(paramInt2).withScale(paramFloat).withLeftAlignment(0.5F));
/*  99 */     gizmoProperties.setAlwaysOnTop();
/* 100 */     return gizmoProperties;
/*     */   }
/*     */   
/*     */   public static GizmoProperties billboardText(String paramString, Vec3 paramVec3, TextGizmo.Style paramStyle) {
/* 104 */     return addGizmo(new TextGizmo(paramVec3, paramString, paramStyle));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class TemporaryCollection
/*     */     implements AutoCloseable
/*     */   {
/* 112 */     private final GizmoCollector old = Gizmos.collector.get();
/*     */     
/*     */     private boolean closed;
/*     */     
/*     */     public void close() {
/* 117 */       if (!this.closed) {
/* 118 */         this.closed = true;
/* 119 */         Gizmos.collector.set(this.old);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gizmos\Gizmos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */