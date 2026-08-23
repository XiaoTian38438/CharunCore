/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.common.collect.Comparators;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundChunkBatchFinishedPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundChunkBatchStartPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PlayerChunkSender {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final float MIN_CHUNKS_PER_TICK = 0.01F;
/*     */   public static final float MAX_CHUNKS_PER_TICK = 64.0F;
/*     */   private static final float START_CHUNKS_PER_TICK = 9.0F;
/*     */   private static final int MAX_UNACKNOWLEDGED_BATCHES = 10;
/*  31 */   private final LongSet pendingChunks = (LongSet)new LongOpenHashSet();
/*     */   
/*     */   private final boolean memoryConnection;
/*  34 */   private float desiredChunksPerTick = 9.0F;
/*     */   private float batchQuota;
/*     */   private int unacknowledgedBatches;
/*  37 */   private int maxUnacknowledgedBatches = 1;
/*     */   
/*     */   public PlayerChunkSender(boolean paramBoolean) {
/*  40 */     this.memoryConnection = paramBoolean;
/*     */   }
/*     */   
/*     */   public void markChunkPendingToSend(LevelChunk paramLevelChunk) {
/*  44 */     this.pendingChunks.add(paramLevelChunk.getPos().toLong());
/*     */   }
/*     */   
/*     */   public void dropChunk(ServerPlayer paramServerPlayer, ChunkPos paramChunkPos) {
/*  48 */     if (!this.pendingChunks.remove(paramChunkPos.toLong()))
/*     */     {
/*  50 */       if (paramServerPlayer.isAlive()) {
/*  51 */         paramServerPlayer.connection.send((Packet<?>)new ClientboundForgetLevelChunkPacket(paramChunkPos));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendNextChunks(ServerPlayer paramServerPlayer) {
/*  57 */     if (this.unacknowledgedBatches >= this.maxUnacknowledgedBatches) {
/*     */       return;
/*     */     }
/*  60 */     float f = Math.max(1.0F, this.desiredChunksPerTick);
/*  61 */     this.batchQuota = Math.min(this.batchQuota + this.desiredChunksPerTick, f);
/*  62 */     if (this.batchQuota < 1.0F) {
/*     */       return;
/*     */     }
/*  65 */     if (this.pendingChunks.isEmpty()) {
/*     */       return;
/*     */     }
/*  68 */     ServerLevel serverLevel = paramServerPlayer.level();
/*  69 */     ChunkMap chunkMap = (serverLevel.getChunkSource()).chunkMap;
/*     */     
/*  71 */     List<LevelChunk> list = collectChunksToSend(chunkMap, paramServerPlayer.chunkPosition());
/*  72 */     if (list.isEmpty()) {
/*     */       return;
/*     */     }
/*  75 */     ServerGamePacketListenerImpl serverGamePacketListenerImpl = paramServerPlayer.connection;
/*     */     
/*  77 */     this.unacknowledgedBatches++;
/*  78 */     serverGamePacketListenerImpl.send((Packet<?>)ClientboundChunkBatchStartPacket.INSTANCE);
/*  79 */     for (LevelChunk levelChunk : list) {
/*  80 */       sendChunk(serverGamePacketListenerImpl, serverLevel, levelChunk);
/*     */     }
/*  82 */     serverGamePacketListenerImpl.send((Packet<?>)new ClientboundChunkBatchFinishedPacket(list.size()));
/*  83 */     this.batchQuota -= list.size();
/*     */   }
/*     */   
/*     */   private static void sendChunk(ServerGamePacketListenerImpl paramServerGamePacketListenerImpl, ServerLevel paramServerLevel, LevelChunk paramLevelChunk) {
/*  87 */     paramServerGamePacketListenerImpl.send((Packet<?>)new ClientboundLevelChunkWithLightPacket(paramLevelChunk, paramServerLevel.getLightEngine(), null, null));
/*  88 */     ChunkPos chunkPos = paramLevelChunk.getPos();
/*  89 */     if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/*  90 */       LOGGER.debug("SEN {}", chunkPos);
/*     */     }
/*  92 */     paramServerLevel.debugSynchronizers().startTrackingChunk(paramServerGamePacketListenerImpl.player, paramLevelChunk.getPos());
/*     */   }
/*     */   
/*     */   private List<LevelChunk> collectChunksToSend(ChunkMap paramChunkMap, ChunkPos paramChunkPos) {
/*     */     List<LevelChunk> list;
/*  97 */     int i = Mth.floor(this.batchQuota);
/*  98 */     if (this.memoryConnection || this.pendingChunks.size() <= i) {
/*     */       
/* 100 */       Objects.requireNonNull(paramChunkMap);
/*     */ 
/*     */       
/* 103 */       list = this.pendingChunks.longStream().mapToObj(paramChunkMap::getChunkToSend).filter(Objects::nonNull).sorted(Comparator.comparingInt(paramLevelChunk -> paramChunkPos.distanceSquared(paramLevelChunk.getPos()))).toList();
/*     */     } else {
/*     */       
/* 106 */       Objects.requireNonNull(paramChunkPos);
/*     */       
/* 108 */       Objects.requireNonNull(paramChunkMap);
/*     */       
/* 110 */       list = ((List)this.pendingChunks.stream().collect(Comparators.least(i, Comparator.comparingInt(paramChunkPos::distanceSquared)))).stream().mapToLong(Long::longValue).mapToObj(paramChunkMap::getChunkToSend).filter(Objects::nonNull).toList();
/*     */     } 
/* 112 */     for (LevelChunk levelChunk : list) {
/* 113 */       this.pendingChunks.remove(levelChunk.getPos().toLong());
/*     */     }
/* 115 */     return list;
/*     */   }
/*     */   
/*     */   public void onChunkBatchReceivedByClient(float paramFloat) {
/* 119 */     this.unacknowledgedBatches--;
/* 120 */     this.desiredChunksPerTick = Double.isNaN(paramFloat) ? 0.01F : Mth.clamp(paramFloat, 0.01F, 64.0F);
/* 121 */     if (this.unacknowledgedBatches == 0)
/*     */     {
/* 123 */       this.batchQuota = 1.0F;
/*     */     }
/* 125 */     this.maxUnacknowledgedBatches = 10;
/*     */   }
/*     */   
/*     */   public boolean isPending(long paramLong) {
/* 129 */     return this.pendingChunks.contains(paramLong);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\PlayerChunkSender.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */