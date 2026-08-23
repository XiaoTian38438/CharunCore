/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface WaypointTransmitter
/*     */   extends Waypoint
/*     */ {
/*     */   public static final int REALLY_FAR_DISTANCE = 332;
/*     */   
/*     */   boolean isTransmittingWaypoint();
/*     */   
/*     */   Optional<Connection> makeWaypointConnectionWith(ServerPlayer paramServerPlayer);
/*     */   
/*     */   Waypoint.Icon waypointIcon();
/*     */   
/*     */   static boolean doesSourceIgnoreReceiver(LivingEntity paramLivingEntity, ServerPlayer paramServerPlayer) {
/*  31 */     if (paramServerPlayer.isSpectator()) {
/*  32 */       return false;
/*     */     }
/*  34 */     if (paramLivingEntity.isSpectator() || paramLivingEntity.hasIndirectPassenger((Entity)paramServerPlayer)) {
/*  35 */       return true;
/*     */     }
/*  37 */     double d = Math.min(paramLivingEntity
/*  38 */         .getAttributeValue(Attributes.WAYPOINT_TRANSMIT_RANGE), paramServerPlayer
/*  39 */         .getAttributeValue(Attributes.WAYPOINT_RECEIVE_RANGE));
/*     */     
/*  41 */     return (paramLivingEntity.distanceTo((Entity)paramServerPlayer) >= d);
/*     */   }
/*     */   
/*     */   static boolean isChunkVisible(ChunkPos paramChunkPos, ServerPlayer paramServerPlayer) {
/*  45 */     return paramServerPlayer.getChunkTrackingView().isInViewDistance(paramChunkPos.x, paramChunkPos.z);
/*     */   }
/*     */   
/*     */   static boolean isReallyFar(LivingEntity paramLivingEntity, ServerPlayer paramServerPlayer) {
/*  49 */     return (paramLivingEntity.distanceTo((Entity)paramServerPlayer) > 332.0F);
/*     */   }
/*     */   public static interface Connection {
/*     */     void connect();
/*     */     void disconnect();
/*     */     void update();
/*     */     boolean isBroken(); }
/*     */   public static interface BlockConnection extends Connection { default boolean isBroken() {
/*  57 */       return (distanceManhattan() > 1);
/*     */     }
/*     */     
/*     */     int distanceManhattan(); }
/*     */   
/*     */   public static class EntityBlockConnection implements BlockConnection { private final LivingEntity source;
/*     */     private final Waypoint.Icon icon;
/*     */     private final ServerPlayer receiver;
/*     */     private BlockPos lastPosition;
/*     */     
/*     */     public EntityBlockConnection(LivingEntity param1LivingEntity, Waypoint.Icon param1Icon, ServerPlayer param1ServerPlayer) {
/*  68 */       this.source = param1LivingEntity;
/*  69 */       this.receiver = param1ServerPlayer;
/*  70 */       this.icon = param1Icon;
/*  71 */       this.lastPosition = param1LivingEntity.blockPosition();
/*     */     }
/*     */ 
/*     */     
/*     */     public void connect() {
/*  76 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.addWaypointPosition(this.source.getUUID(), this.icon, (Vec3i)this.lastPosition));
/*     */     }
/*     */ 
/*     */     
/*     */     public void disconnect() {
/*  81 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/*  86 */       BlockPos blockPos = this.source.blockPosition();
/*  87 */       if (blockPos.distManhattan((Vec3i)this.lastPosition) > 0) {
/*  88 */         this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.updateWaypointPosition(this.source.getUUID(), this.icon, (Vec3i)blockPos));
/*  89 */         this.lastPosition = blockPos;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int distanceManhattan() {
/*  95 */       return this.lastPosition.distManhattan((Vec3i)this.source.blockPosition());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isBroken() {
/* 100 */       return (super.isBroken() || WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver));
/*     */     } }
/*     */ 
/*     */   
/*     */   public static interface ChunkConnection
/*     */     extends Connection {
/*     */     int distanceChessboard();
/*     */     
/*     */     default boolean isBroken() {
/* 109 */       return (distanceChessboard() > 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class EntityChunkConnection implements ChunkConnection {
/*     */     private final LivingEntity source;
/*     */     private final Waypoint.Icon icon;
/*     */     private final ServerPlayer receiver;
/*     */     private ChunkPos lastPosition;
/*     */     
/*     */     public EntityChunkConnection(LivingEntity param1LivingEntity, Waypoint.Icon param1Icon, ServerPlayer param1ServerPlayer) {
/* 120 */       this.source = param1LivingEntity;
/* 121 */       this.icon = param1Icon;
/* 122 */       this.receiver = param1ServerPlayer;
/* 123 */       this.lastPosition = param1LivingEntity.chunkPosition();
/*     */     }
/*     */ 
/*     */     
/*     */     public int distanceChessboard() {
/* 128 */       return this.lastPosition.getChessboardDistance(this.source.chunkPosition());
/*     */     }
/*     */ 
/*     */     
/*     */     public void connect() {
/* 133 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.addWaypointChunk(this.source.getUUID(), this.icon, this.lastPosition));
/*     */     }
/*     */ 
/*     */     
/*     */     public void disconnect() {
/* 138 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 143 */       ChunkPos chunkPos = this.source.chunkPosition();
/* 144 */       if (chunkPos.getChessboardDistance(this.lastPosition) > 0) {
/* 145 */         this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.updateWaypointChunk(this.source.getUUID(), this.icon, chunkPos));
/* 146 */         this.lastPosition = chunkPos;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isBroken() {
/* 152 */       if (super.isBroken() || WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver)) {
/* 153 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 157 */       return WaypointTransmitter.isChunkVisible(this.lastPosition, this.receiver);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class EntityAzimuthConnection implements Connection {
/*     */     private final LivingEntity source;
/*     */     private final Waypoint.Icon icon;
/*     */     private final ServerPlayer receiver;
/*     */     private float lastAngle;
/*     */     
/*     */     public EntityAzimuthConnection(LivingEntity param1LivingEntity, Waypoint.Icon param1Icon, ServerPlayer param1ServerPlayer) {
/* 168 */       this.source = param1LivingEntity;
/* 169 */       this.icon = param1Icon;
/* 170 */       this.receiver = param1ServerPlayer;
/*     */       
/* 172 */       Vec3 vec3 = param1ServerPlayer.position().subtract(param1LivingEntity.position()).rotateClockwise90();
/* 173 */       this.lastAngle = (float)Mth.atan2(vec3.z(), vec3.x());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isBroken() {
/* 178 */       return (WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver) || WaypointTransmitter.isChunkVisible(this.source.chunkPosition(), this.receiver) || !WaypointTransmitter.isReallyFar(this.source, this.receiver));
/*     */     }
/*     */ 
/*     */     
/*     */     public void connect() {
/* 183 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.addWaypointAzimuth(this.source.getUUID(), this.icon, this.lastAngle));
/*     */     }
/*     */ 
/*     */     
/*     */     public void disconnect() {
/* 188 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 193 */       Vec3 vec3 = this.receiver.position().subtract(this.source.position()).rotateClockwise90();
/* 194 */       float f = (float)Mth.atan2(vec3.z(), vec3.x());
/* 195 */       if (Mth.abs(f - this.lastAngle) > 0.008726646F) {
/* 196 */         this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.updateWaypointAzimuth(this.source.getUUID(), this.icon, f));
/* 197 */         this.lastAngle = f;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\waypoints\WaypointTransmitter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */