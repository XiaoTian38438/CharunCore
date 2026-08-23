/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Unit;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VillageSectionSynchronizer
/*     */   extends TrackingDebugSynchronizer<Unit>
/*     */ {
/*     */   public VillageSectionSynchronizer() {
/* 271 */     super(DebugSubscriptions.VILLAGE_SECTIONS);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void sendInitialChunk(ServerPlayer paramServerPlayer, ChunkPos paramChunkPos) {
/* 276 */     ServerLevel serverLevel = paramServerPlayer.level();
/* 277 */     PoiManager poiManager = serverLevel.getPoiManager();
/* 278 */     poiManager.getInChunk(paramHolder -> true, paramChunkPos, PoiManager.Occupancy.ANY).forEach(paramPoiRecord -> {
/*     */           SectionPos sectionPos = SectionPos.of(paramPoiRecord.getPos());
/*     */           forEachVillageSectionUpdate(paramServerLevel, sectionPos, ());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPoiAdded(ServerLevel paramServerLevel, PoiRecord paramPoiRecord) {
/* 290 */     sendVillageSectionsPacket(paramServerLevel, paramPoiRecord.getPos());
/*     */   }
/*     */   
/*     */   public void onPoiRemoved(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 294 */     sendVillageSectionsPacket(paramServerLevel, paramBlockPos);
/*     */   }
/*     */   
/*     */   private void sendVillageSectionsPacket(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 298 */     forEachVillageSectionUpdate(paramServerLevel, SectionPos.of(paramBlockPos), (paramSectionPos, paramBoolean) -> {
/*     */           BlockPos blockPos = paramSectionPos.center();
/*     */           if (paramBoolean.booleanValue()) {
/*     */             sendToPlayersTrackingChunk(paramServerLevel, new ChunkPos(blockPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(blockPos, this.subscription.packUpdate(Unit.INSTANCE)));
/*     */           } else {
/*     */             sendToPlayersTrackingChunk(paramServerLevel, new ChunkPos(blockPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(blockPos, this.subscription.emptyUpdate()));
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static void forEachVillageSectionUpdate(ServerLevel paramServerLevel, SectionPos paramSectionPos, BiConsumer<SectionPos, Boolean> paramBiConsumer) {
/* 309 */     for (byte b = -1; b <= 1; b++) {
/* 310 */       for (byte b1 = -1; b1 <= 1; b1++) {
/* 311 */         for (byte b2 = -1; b2 <= 1; b2++) {
/* 312 */           SectionPos sectionPos = paramSectionPos.offset(b1, b2, b);
/* 313 */           if (paramServerLevel.isVillage(sectionPos.center())) {
/* 314 */             paramBiConsumer.accept(sectionPos, Boolean.valueOf(true));
/*     */           } else {
/* 316 */             paramBiConsumer.accept(sectionPos, Boolean.valueOf(false));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\debug\TrackingDebugSynchronizer$VillageSectionSynchronizer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */