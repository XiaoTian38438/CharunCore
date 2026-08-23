/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Vec3iWaypoint
/*     */   extends TrackedWaypoint
/*     */ {
/*     */   private Vec3i vector;
/*     */   
/*     */   public Vec3iWaypoint(UUID paramUUID, Waypoint.Icon paramIcon, Vec3i paramVec3i) {
/* 153 */     super(Either.left(paramUUID), paramIcon, TrackedWaypoint.Type.VEC3I);
/* 154 */     this.vector = paramVec3i;
/*     */   }
/*     */   
/*     */   public Vec3iWaypoint(Either<UUID, String> paramEither, Waypoint.Icon paramIcon, FriendlyByteBuf paramFriendlyByteBuf) {
/* 158 */     super(paramEither, paramIcon, TrackedWaypoint.Type.VEC3I);
/* 159 */     this
/*     */ 
/*     */       
/* 162 */       .vector = new Vec3i(paramFriendlyByteBuf.readVarInt(), paramFriendlyByteBuf.readVarInt(), paramFriendlyByteBuf.readVarInt());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(TrackedWaypoint paramTrackedWaypoint) {
/* 168 */     if (paramTrackedWaypoint instanceof Vec3iWaypoint) { Vec3iWaypoint vec3iWaypoint = (Vec3iWaypoint)paramTrackedWaypoint;
/* 169 */       this.vector = vec3iWaypoint.vector; }
/*     */     else
/* 171 */     { TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", paramTrackedWaypoint.getClass()); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeContents(ByteBuf paramByteBuf) {
/* 177 */     VarInt.write(paramByteBuf, this.vector.getX());
/* 178 */     VarInt.write(paramByteBuf, this.vector.getY());
/* 179 */     VarInt.write(paramByteBuf, this.vector.getZ());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Vec3 position(Level paramLevel, PartialTickSupplier paramPartialTickSupplier) {
/* 185 */     Objects.requireNonNull(paramLevel); return this.identifier.left().map(paramLevel::getEntity).map(paramEntity -> (paramEntity.blockPosition().distManhattan(this.vector) > 3) ? null : paramEntity.getEyePosition(paramPartialTickSupplier.apply(paramEntity)))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 191 */       .orElseGet(() -> Vec3.atCenterOf(this.vector));
/*     */   }
/*     */ 
/*     */   
/*     */   public double yawAngleToCamera(Level paramLevel, TrackedWaypoint.Camera paramCamera, PartialTickSupplier paramPartialTickSupplier) {
/* 196 */     Vec3 vec3 = paramCamera.position().subtract(position(paramLevel, paramPartialTickSupplier)).rotateClockwise90();
/* 197 */     float f = (float)Mth.atan2(vec3.z(), vec3.x()) * 57.295776F;
/* 198 */     return Mth.degreesDifference(paramCamera.yaw(), f);
/*     */   }
/*     */ 
/*     */   
/*     */   public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level paramLevel, TrackedWaypoint.Projector paramProjector, PartialTickSupplier paramPartialTickSupplier) {
/* 203 */     Vec3 vec3 = paramProjector.projectPointToScreen(position(paramLevel, paramPartialTickSupplier));
/* 204 */     boolean bool = (vec3.z > 1.0D) ? true : false;
/*     */     
/* 206 */     double d = bool ? -vec3.y : vec3.y;
/* 207 */     if (d < -1.0D) {
/* 208 */       return TrackedWaypoint.PitchDirection.DOWN;
/*     */     }
/* 210 */     if (d > 1.0D) {
/* 211 */       return TrackedWaypoint.PitchDirection.UP;
/*     */     }
/* 213 */     if (bool) {
/* 214 */       if (vec3.y > 0.0D) {
/* 215 */         return TrackedWaypoint.PitchDirection.UP;
/*     */       }
/* 217 */       if (vec3.y < 0.0D) {
/* 218 */         return TrackedWaypoint.PitchDirection.DOWN;
/*     */       }
/*     */     } 
/* 221 */     return TrackedWaypoint.PitchDirection.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public double distanceSquared(Entity paramEntity) {
/* 226 */     return paramEntity.distanceToSqr(Vec3.atCenterOf(this.vector));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\waypoints\TrackedWaypoint$Vec3iWaypoint.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */