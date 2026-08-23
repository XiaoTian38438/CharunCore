/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityChunkConnection
/*     */   implements WaypointTransmitter.ChunkConnection
/*     */ {
/*     */   private final LivingEntity source;
/*     */   private final Waypoint.Icon icon;
/*     */   private final ServerPlayer receiver;
/*     */   private ChunkPos lastPosition;
/*     */   
/*     */   public EntityChunkConnection(LivingEntity paramLivingEntity, Waypoint.Icon paramIcon, ServerPlayer paramServerPlayer) {
/* 120 */     this.source = paramLivingEntity;
/* 121 */     this.icon = paramIcon;
/* 122 */     this.receiver = paramServerPlayer;
/* 123 */     this.lastPosition = paramLivingEntity.chunkPosition();
/*     */   }
/*     */ 
/*     */   
/*     */   public int distanceChessboard() {
/* 128 */     return this.lastPosition.getChessboardDistance(this.source.chunkPosition());
/*     */   }
/*     */ 
/*     */   
/*     */   public void connect() {
/* 133 */     this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.addWaypointChunk(this.source.getUUID(), this.icon, this.lastPosition));
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnect() {
/* 138 */     this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/* 143 */     ChunkPos chunkPos = this.source.chunkPosition();
/* 144 */     if (chunkPos.getChessboardDistance(this.lastPosition) > 0) {
/* 145 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.updateWaypointChunk(this.source.getUUID(), this.icon, chunkPos));
/* 146 */       this.lastPosition = chunkPos;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBroken() {
/* 152 */     if (super.isBroken() || WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver)) {
/* 153 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 157 */     return WaypointTransmitter.isChunkVisible(this.lastPosition, this.receiver);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\waypoints\WaypointTransmitter$EntityChunkConnection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */