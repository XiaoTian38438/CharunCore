/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.LivingEntity;
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
/*     */ public class EntityAzimuthConnection
/*     */   implements WaypointTransmitter.Connection
/*     */ {
/*     */   private final LivingEntity source;
/*     */   private final Waypoint.Icon icon;
/*     */   private final ServerPlayer receiver;
/*     */   private float lastAngle;
/*     */   
/*     */   public EntityAzimuthConnection(LivingEntity paramLivingEntity, Waypoint.Icon paramIcon, ServerPlayer paramServerPlayer) {
/* 168 */     this.source = paramLivingEntity;
/* 169 */     this.icon = paramIcon;
/* 170 */     this.receiver = paramServerPlayer;
/*     */     
/* 172 */     Vec3 vec3 = paramServerPlayer.position().subtract(paramLivingEntity.position()).rotateClockwise90();
/* 173 */     this.lastAngle = (float)Mth.atan2(vec3.z(), vec3.x());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBroken() {
/* 178 */     return (WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver) || WaypointTransmitter.isChunkVisible(this.source.chunkPosition(), this.receiver) || !WaypointTransmitter.isReallyFar(this.source, this.receiver));
/*     */   }
/*     */ 
/*     */   
/*     */   public void connect() {
/* 183 */     this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.addWaypointAzimuth(this.source.getUUID(), this.icon, this.lastAngle));
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnect() {
/* 188 */     this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/* 193 */     Vec3 vec3 = this.receiver.position().subtract(this.source.position()).rotateClockwise90();
/* 194 */     float f = (float)Mth.atan2(vec3.z(), vec3.x());
/* 195 */     if (Mth.abs(f - this.lastAngle) > 0.008726646F) {
/* 196 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.updateWaypointAzimuth(this.source.getUUID(), this.icon, f));
/* 197 */       this.lastAngle = f;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\waypoints\WaypointTransmitter$EntityAzimuthConnection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */