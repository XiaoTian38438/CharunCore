/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.Level;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AzimuthWaypoint
/*     */   extends TrackedWaypoint
/*     */ {
/*     */   private float angle;
/*     */   
/*     */   public AzimuthWaypoint(UUID paramUUID, Waypoint.Icon paramIcon, float paramFloat) {
/* 296 */     super(Either.left(paramUUID), paramIcon, TrackedWaypoint.Type.AZIMUTH);
/* 297 */     this.angle = paramFloat;
/*     */   }
/*     */   
/*     */   public AzimuthWaypoint(Either<UUID, String> paramEither, Waypoint.Icon paramIcon, FriendlyByteBuf paramFriendlyByteBuf) {
/* 301 */     super(paramEither, paramIcon, TrackedWaypoint.Type.AZIMUTH);
/* 302 */     this.angle = paramFriendlyByteBuf.readFloat();
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(TrackedWaypoint paramTrackedWaypoint) {
/* 307 */     if (paramTrackedWaypoint instanceof AzimuthWaypoint) { AzimuthWaypoint azimuthWaypoint = (AzimuthWaypoint)paramTrackedWaypoint;
/* 308 */       this.angle = azimuthWaypoint.angle; }
/*     */     else
/* 310 */     { TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", paramTrackedWaypoint.getClass()); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeContents(ByteBuf paramByteBuf) {
/* 316 */     paramByteBuf.writeFloat(this.angle);
/*     */   }
/*     */ 
/*     */   
/*     */   public double yawAngleToCamera(Level paramLevel, TrackedWaypoint.Camera paramCamera, PartialTickSupplier paramPartialTickSupplier) {
/* 321 */     return Mth.degreesDifference(paramCamera.yaw(), this.angle * 57.295776F);
/*     */   }
/*     */ 
/*     */   
/*     */   public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level paramLevel, TrackedWaypoint.Projector paramProjector, PartialTickSupplier paramPartialTickSupplier) {
/* 326 */     double d = paramProjector.projectHorizonToScreen();
/* 327 */     if (d < -1.0D) {
/* 328 */       return TrackedWaypoint.PitchDirection.DOWN;
/*     */     }
/* 330 */     if (d > 1.0D) {
/* 331 */       return TrackedWaypoint.PitchDirection.UP;
/*     */     }
/* 333 */     return TrackedWaypoint.PitchDirection.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public double distanceSquared(Entity paramEntity) {
/* 338 */     return Double.POSITIVE_INFINITY;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\waypoints\TrackedWaypoint$AzimuthWaypoint.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */