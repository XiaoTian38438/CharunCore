/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ChunkWaypoint
/*     */   extends TrackedWaypoint
/*     */ {
/*     */   private ChunkPos chunkPos;
/*     */   
/*     */   public ChunkWaypoint(UUID paramUUID, Waypoint.Icon paramIcon, ChunkPos paramChunkPos) {
/* 234 */     super(Either.left(paramUUID), paramIcon, TrackedWaypoint.Type.CHUNK);
/* 235 */     this.chunkPos = paramChunkPos;
/*     */   }
/*     */   
/*     */   public ChunkWaypoint(Either<UUID, String> paramEither, Waypoint.Icon paramIcon, FriendlyByteBuf paramFriendlyByteBuf) {
/* 239 */     super(paramEither, paramIcon, TrackedWaypoint.Type.CHUNK);
/* 240 */     this
/*     */       
/* 242 */       .chunkPos = new ChunkPos(paramFriendlyByteBuf.readVarInt(), paramFriendlyByteBuf.readVarInt());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(TrackedWaypoint paramTrackedWaypoint) {
/* 248 */     if (paramTrackedWaypoint instanceof ChunkWaypoint) { ChunkWaypoint chunkWaypoint = (ChunkWaypoint)paramTrackedWaypoint;
/* 249 */       this.chunkPos = chunkWaypoint.chunkPos; }
/*     */     else
/* 251 */     { TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", paramTrackedWaypoint.getClass()); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeContents(ByteBuf paramByteBuf) {
/* 257 */     VarInt.write(paramByteBuf, this.chunkPos.x);
/* 258 */     VarInt.write(paramByteBuf, this.chunkPos.z);
/*     */   }
/*     */ 
/*     */   
/*     */   private Vec3 position(double paramDouble) {
/* 263 */     return Vec3.atCenterOf((Vec3i)this.chunkPos.getMiddleBlockPosition((int)paramDouble));
/*     */   }
/*     */ 
/*     */   
/*     */   public double yawAngleToCamera(Level paramLevel, TrackedWaypoint.Camera paramCamera, PartialTickSupplier paramPartialTickSupplier) {
/* 268 */     Vec3 vec31 = paramCamera.position();
/* 269 */     Vec3 vec32 = vec31.subtract(position(vec31.y())).rotateClockwise90();
/* 270 */     float f = (float)Mth.atan2(vec32.z(), vec32.x()) * 57.295776F;
/* 271 */     return Mth.degreesDifference(paramCamera.yaw(), f);
/*     */   }
/*     */ 
/*     */   
/*     */   public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level paramLevel, TrackedWaypoint.Projector paramProjector, PartialTickSupplier paramPartialTickSupplier) {
/* 276 */     double d = paramProjector.projectHorizonToScreen();
/* 277 */     if (d < -1.0D) {
/* 278 */       return TrackedWaypoint.PitchDirection.DOWN;
/*     */     }
/* 280 */     if (d > 1.0D) {
/* 281 */       return TrackedWaypoint.PitchDirection.UP;
/*     */     }
/* 283 */     return TrackedWaypoint.PitchDirection.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public double distanceSquared(Entity paramEntity) {
/* 288 */     return paramEntity.distanceToSqr(Vec3.atCenterOf((Vec3i)this.chunkPos.getMiddleBlockPosition(paramEntity.getBlockY())));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\waypoints\TrackedWaypoint$ChunkWaypoint.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */