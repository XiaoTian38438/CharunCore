/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityBlockConnection
/*     */   implements WaypointTransmitter.BlockConnection
/*     */ {
/*     */   private final LivingEntity source;
/*     */   private final Waypoint.Icon icon;
/*     */   private final ServerPlayer receiver;
/*     */   private BlockPos lastPosition;
/*     */   
/*     */   public EntityBlockConnection(LivingEntity paramLivingEntity, Waypoint.Icon paramIcon, ServerPlayer paramServerPlayer) {
/*  68 */     this.source = paramLivingEntity;
/*  69 */     this.receiver = paramServerPlayer;
/*  70 */     this.icon = paramIcon;
/*  71 */     this.lastPosition = paramLivingEntity.blockPosition();
/*     */   }
/*     */ 
/*     */   
/*     */   public void connect() {
/*  76 */     this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.addWaypointPosition(this.source.getUUID(), this.icon, (Vec3i)this.lastPosition));
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnect() {
/*  81 */     this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/*  86 */     BlockPos blockPos = this.source.blockPosition();
/*  87 */     if (blockPos.distManhattan((Vec3i)this.lastPosition) > 0) {
/*  88 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.updateWaypointPosition(this.source.getUUID(), this.icon, (Vec3i)blockPos));
/*  89 */       this.lastPosition = blockPos;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int distanceManhattan() {
/*  95 */     return this.lastPosition.distManhattan((Vec3i)this.source.blockPosition());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBroken() {
/* 100 */     return (super.isBroken() || WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\waypoints\WaypointTransmitter$EntityBlockConnection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */