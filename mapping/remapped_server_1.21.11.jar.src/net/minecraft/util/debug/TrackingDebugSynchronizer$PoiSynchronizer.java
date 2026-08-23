/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PoiSynchronizer
/*     */   extends TrackingDebugSynchronizer<DebugPoiInfo>
/*     */ {
/*     */   public PoiSynchronizer() {
/* 242 */     super(DebugSubscriptions.POIS);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void sendInitialChunk(ServerPlayer paramServerPlayer, ChunkPos paramChunkPos) {
/* 247 */     ServerLevel serverLevel = paramServerPlayer.level();
/* 248 */     PoiManager poiManager = serverLevel.getPoiManager();
/* 249 */     poiManager.getInChunk(paramHolder -> true, paramChunkPos, PoiManager.Occupancy.ANY).forEach(paramPoiRecord -> paramServerPlayer.connection.send((Packet)new ClientboundDebugBlockValuePacket(paramPoiRecord.getPos(), this.subscription.packUpdate(new DebugPoiInfo(paramPoiRecord)))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPoiAdded(ServerLevel paramServerLevel, PoiRecord paramPoiRecord) {
/* 257 */     sendToPlayersTrackingChunk(paramServerLevel, new ChunkPos(paramPoiRecord.getPos()), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(paramPoiRecord.getPos(), this.subscription.packUpdate(new DebugPoiInfo(paramPoiRecord))));
/*     */   }
/*     */   
/*     */   public void onPoiRemoved(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 261 */     sendToPlayersTrackingChunk(paramServerLevel, new ChunkPos(paramBlockPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(paramBlockPos, this.subscription.emptyUpdate()));
/*     */   }
/*     */   
/*     */   public void onPoiTicketCountChanged(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 265 */     sendToPlayersTrackingChunk(paramServerLevel, new ChunkPos(paramBlockPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(paramBlockPos, this.subscription.packUpdate(paramServerLevel.getPoiManager().getDebugPoiInfo(paramBlockPos))));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\debug\TrackingDebugSynchronizer$PoiSynchronizer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */