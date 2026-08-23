/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.FriendlyByteBuf;
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
/*     */ class EmptyWaypoint
/*     */   extends TrackedWaypoint
/*     */ {
/*     */   private EmptyWaypoint(Either<UUID, String> paramEither, Waypoint.Icon paramIcon, FriendlyByteBuf paramFriendlyByteBuf) {
/* 120 */     super(paramEither, paramIcon, TrackedWaypoint.Type.EMPTY);
/*     */   }
/*     */   
/*     */   EmptyWaypoint(UUID paramUUID) {
/* 124 */     super(Either.left(paramUUID), Waypoint.Icon.NULL, TrackedWaypoint.Type.EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(TrackedWaypoint paramTrackedWaypoint) {}
/*     */ 
/*     */   
/*     */   public void writeContents(ByteBuf paramByteBuf) {}
/*     */ 
/*     */   
/*     */   public double yawAngleToCamera(Level paramLevel, TrackedWaypoint.Camera paramCamera, PartialTickSupplier paramPartialTickSupplier) {
/* 135 */     return Double.NaN;
/*     */   }
/*     */ 
/*     */   
/*     */   public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level paramLevel, TrackedWaypoint.Projector paramProjector, PartialTickSupplier paramPartialTickSupplier) {
/* 140 */     return TrackedWaypoint.PitchDirection.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public double distanceSquared(Entity paramEntity) {
/* 145 */     return Double.POSITIVE_INFINITY;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\waypoints\TrackedWaypoint$EmptyWaypoint.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */