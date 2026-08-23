/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortSet;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.IntConsumer;
/*     */ import java.util.function.IntSupplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ 
/*     */ public class ChunkHolder
/*     */   extends GenerationChunkHolder
/*     */ {
/*  33 */   public static final ChunkResult<LevelChunk> UNLOADED_LEVEL_CHUNK = ChunkResult.error("Unloaded level chunk");
/*  34 */   private static final CompletableFuture<ChunkResult<LevelChunk>> UNLOADED_LEVEL_CHUNK_FUTURE = CompletableFuture.completedFuture(UNLOADED_LEVEL_CHUNK);
/*     */   
/*     */   private final LevelHeightAccessor levelHeightAccessor;
/*     */   
/*  38 */   private volatile CompletableFuture<ChunkResult<LevelChunk>> fullChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*  39 */   private volatile CompletableFuture<ChunkResult<LevelChunk>> tickingChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*  40 */   private volatile CompletableFuture<ChunkResult<LevelChunk>> entityTickingChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*     */   
/*     */   private int oldTicketLevel;
/*     */   
/*     */   private int ticketLevel;
/*     */   
/*     */   private int queueLevel;
/*     */   private boolean hasChangedSections;
/*     */   private final ShortSet[] changedBlocksPerSection;
/*  49 */   private final BitSet blockChangedLightSectionFilter = new BitSet();
/*  50 */   private final BitSet skyChangedLightSectionFilter = new BitSet();
/*     */   
/*     */   private final LevelLightEngine lightEngine;
/*     */   private final LevelChangeListener onLevelChange;
/*     */   private final PlayerProvider playerProvider;
/*     */   private boolean wasAccessibleSinceLastSave;
/*  56 */   private CompletableFuture<?> pendingFullStateConfirmation = CompletableFuture.completedFuture(null);
/*  57 */   private CompletableFuture<?> sendSync = CompletableFuture.completedFuture(null);
/*  58 */   private CompletableFuture<?> saveSync = CompletableFuture.completedFuture(null);
/*     */   
/*     */   public ChunkHolder(ChunkPos paramChunkPos, int paramInt, LevelHeightAccessor paramLevelHeightAccessor, LevelLightEngine paramLevelLightEngine, LevelChangeListener paramLevelChangeListener, PlayerProvider paramPlayerProvider) {
/*  61 */     super(paramChunkPos);
/*  62 */     this.levelHeightAccessor = paramLevelHeightAccessor;
/*  63 */     this.lightEngine = paramLevelLightEngine;
/*  64 */     this.onLevelChange = paramLevelChangeListener;
/*  65 */     this.playerProvider = paramPlayerProvider;
/*  66 */     this.oldTicketLevel = ChunkLevel.MAX_LEVEL + 1;
/*  67 */     this.ticketLevel = this.oldTicketLevel;
/*  68 */     this.queueLevel = this.oldTicketLevel;
/*  69 */     setTicketLevel(paramInt);
/*  70 */     this.changedBlocksPerSection = new ShortSet[paramLevelHeightAccessor.getSectionsCount()];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkResult<LevelChunk>> getTickingChunkFuture() {
/*  77 */     return this.tickingChunkFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkResult<LevelChunk>> getEntityTickingChunkFuture() {
/*  84 */     return this.entityTickingChunkFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkResult<LevelChunk>> getFullChunkFuture() {
/*  91 */     return this.fullChunkFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LevelChunk getTickingChunk() {
/*  98 */     return ((ChunkResult<LevelChunk>)getTickingChunkFuture().getNow(UNLOADED_LEVEL_CHUNK)).orElse(null);
/*     */   }
/*     */   
/*     */   public LevelChunk getChunkToSend() {
/* 102 */     if (!this.sendSync.isDone()) {
/* 103 */       return null;
/*     */     }
/* 105 */     return getTickingChunk();
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> getSendSyncFuture() {
/* 109 */     return this.sendSync;
/*     */   }
/*     */   
/*     */   public void addSendDependency(CompletableFuture<?> paramCompletableFuture) {
/* 113 */     if (this.sendSync.isDone()) {
/* 114 */       this.sendSync = paramCompletableFuture;
/*     */     } else {
/* 116 */       this.sendSync = this.sendSync.thenCombine(paramCompletableFuture, (paramObject1, paramObject2) -> null);
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> getSaveSyncFuture() {
/* 121 */     return this.saveSync;
/*     */   }
/*     */   
/*     */   public boolean isReadyForSaving() {
/* 125 */     return this.saveSync.isDone();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addSaveDependency(CompletableFuture<?> paramCompletableFuture) {
/* 130 */     if (this.saveSync.isDone()) {
/* 131 */       this.saveSync = paramCompletableFuture;
/*     */     } else {
/* 133 */       this.saveSync = this.saveSync.thenCombine(paramCompletableFuture, (paramObject1, paramObject2) -> null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean blockChanged(BlockPos paramBlockPos) {
/*     */     ShortOpenHashSet shortOpenHashSet;
/* 140 */     LevelChunk levelChunk = getTickingChunk();
/* 141 */     if (levelChunk == null) {
/* 142 */       return false;
/*     */     }
/*     */     
/* 145 */     boolean bool = this.hasChangedSections;
/* 146 */     int i = this.levelHeightAccessor.getSectionIndex(paramBlockPos.getY());
/* 147 */     ShortSet shortSet = this.changedBlocksPerSection[i];
/* 148 */     if (shortSet == null) {
/* 149 */       this.hasChangedSections = true;
/* 150 */       shortOpenHashSet = new ShortOpenHashSet();
/* 151 */       this.changedBlocksPerSection[i] = (ShortSet)shortOpenHashSet;
/*     */     } 
/* 153 */     shortOpenHashSet.add(SectionPos.sectionRelativePos(paramBlockPos));
/* 154 */     return !bool;
/*     */   }
/*     */   
/*     */   public boolean sectionLightChanged(LightLayer paramLightLayer, int paramInt) {
/* 158 */     ChunkAccess chunkAccess = getChunkIfPresent(ChunkStatus.INITIALIZE_LIGHT);
/* 159 */     if (chunkAccess == null) {
/* 160 */       return false;
/*     */     }
/*     */     
/* 163 */     chunkAccess.markUnsaved();
/*     */     
/* 165 */     LevelChunk levelChunk = getTickingChunk();
/* 166 */     if (levelChunk == null) {
/* 167 */       return false;
/*     */     }
/*     */     
/* 170 */     int i = this.lightEngine.getMinLightSection();
/* 171 */     int j = this.lightEngine.getMaxLightSection();
/* 172 */     if (paramInt < i || paramInt > j) {
/* 173 */       return false;
/*     */     }
/*     */     
/* 176 */     BitSet bitSet = (paramLightLayer == LightLayer.SKY) ? this.skyChangedLightSectionFilter : this.blockChangedLightSectionFilter;
/* 177 */     int k = paramInt - i;
/* 178 */     if (!bitSet.get(k)) {
/* 179 */       bitSet.set(k);
/* 180 */       return true;
/*     */     } 
/* 182 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasChangesToBroadcast() {
/* 186 */     return (this.hasChangedSections || !this.skyChangedLightSectionFilter.isEmpty() || !this.blockChangedLightSectionFilter.isEmpty());
/*     */   }
/*     */   
/*     */   public void broadcastChanges(LevelChunk paramLevelChunk) {
/* 190 */     if (!hasChangesToBroadcast()) {
/*     */       return;
/*     */     }
/*     */     
/* 194 */     Level level = paramLevelChunk.getLevel();
/*     */     
/* 196 */     if (!this.skyChangedLightSectionFilter.isEmpty() || !this.blockChangedLightSectionFilter.isEmpty()) {
/* 197 */       List<ServerPlayer> list1 = this.playerProvider.getPlayers(this.pos, true);
/* 198 */       if (!list1.isEmpty()) {
/* 199 */         ClientboundLightUpdatePacket clientboundLightUpdatePacket = new ClientboundLightUpdatePacket(paramLevelChunk.getPos(), this.lightEngine, this.skyChangedLightSectionFilter, this.blockChangedLightSectionFilter);
/* 200 */         broadcast(list1, (Packet<?>)clientboundLightUpdatePacket);
/*     */       } 
/* 202 */       this.skyChangedLightSectionFilter.clear();
/* 203 */       this.blockChangedLightSectionFilter.clear();
/*     */     } 
/*     */     
/* 206 */     if (!this.hasChangedSections) {
/*     */       return;
/*     */     }
/*     */     
/* 210 */     List<ServerPlayer> list = this.playerProvider.getPlayers(this.pos, false);
/* 211 */     for (byte b = 0; b < this.changedBlocksPerSection.length; b++) {
/* 212 */       ShortSet shortSet = this.changedBlocksPerSection[b];
/* 213 */       if (shortSet != null) {
/*     */ 
/*     */         
/* 216 */         this.changedBlocksPerSection[b] = null;
/*     */         
/* 218 */         if (!list.isEmpty()) {
/*     */ 
/*     */           
/* 221 */           int i = this.levelHeightAccessor.getSectionYFromSectionIndex(b);
/* 222 */           SectionPos sectionPos = SectionPos.of(paramLevelChunk.getPos(), i);
/*     */           
/* 224 */           if (shortSet.size() == 1)
/* 225 */           { BlockPos blockPos = sectionPos.relativeToBlockPos(shortSet.iterator().nextShort());
/* 226 */             BlockState blockState = level.getBlockState(blockPos);
/*     */             
/* 228 */             broadcast(list, (Packet<?>)new ClientboundBlockUpdatePacket(blockPos, blockState));
/* 229 */             broadcastBlockEntityIfNeeded(list, level, blockPos, blockState); }
/*     */           else
/* 231 */           { LevelChunkSection levelChunkSection = paramLevelChunk.getSection(b);
/* 232 */             ClientboundSectionBlocksUpdatePacket clientboundSectionBlocksUpdatePacket = new ClientboundSectionBlocksUpdatePacket(sectionPos, shortSet, levelChunkSection);
/*     */             
/* 234 */             broadcast(list, (Packet<?>)clientboundSectionBlocksUpdatePacket);
/* 235 */             clientboundSectionBlocksUpdatePacket.runUpdates((paramBlockPos, paramBlockState) -> broadcastBlockEntityIfNeeded(paramList, paramLevel, paramBlockPos, paramBlockState)); } 
/*     */         } 
/*     */       } 
/* 238 */     }  this.hasChangedSections = false;
/*     */   }
/*     */   
/*     */   private void broadcastBlockEntityIfNeeded(List<ServerPlayer> paramList, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 242 */     if (paramBlockState.hasBlockEntity()) {
/* 243 */       broadcastBlockEntity(paramList, paramLevel, paramBlockPos);
/*     */     }
/*     */   }
/*     */   
/*     */   private void broadcastBlockEntity(List<ServerPlayer> paramList, Level paramLevel, BlockPos paramBlockPos) {
/* 248 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 249 */     if (blockEntity != null) {
/* 250 */       Packet<?> packet = blockEntity.getUpdatePacket();
/* 251 */       if (packet != null) {
/* 252 */         broadcast(paramList, packet);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void broadcast(List<ServerPlayer> paramList, Packet<?> paramPacket) {
/* 258 */     paramList.forEach(paramServerPlayer -> paramServerPlayer.connection.send(paramPacket));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTicketLevel() {
/* 263 */     return this.ticketLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getQueueLevel() {
/* 268 */     return this.queueLevel;
/*     */   }
/*     */   
/*     */   private void setQueueLevel(int paramInt) {
/* 272 */     this.queueLevel = paramInt;
/*     */   }
/*     */   
/*     */   public void setTicketLevel(int paramInt) {
/* 276 */     this.ticketLevel = paramInt;
/*     */   }
/*     */   
/*     */   private void scheduleFullChunkPromotion(ChunkMap paramChunkMap, CompletableFuture<ChunkResult<LevelChunk>> paramCompletableFuture, Executor paramExecutor, FullChunkStatus paramFullChunkStatus) {
/* 280 */     this.pendingFullStateConfirmation.cancel(false);
/* 281 */     CompletableFuture<?> completableFuture = new CompletableFuture();
/* 282 */     completableFuture.thenRunAsync(() -> paramChunkMap.onFullChunkStatusChange(this.pos, paramFullChunkStatus), paramExecutor);
/* 283 */     this.pendingFullStateConfirmation = completableFuture;
/* 284 */     paramCompletableFuture.thenAccept(paramChunkResult -> paramChunkResult.ifSuccess(()));
/*     */   }
/*     */   
/*     */   private void demoteFullChunk(ChunkMap paramChunkMap, FullChunkStatus paramFullChunkStatus) {
/* 288 */     this.pendingFullStateConfirmation.cancel(false);
/* 289 */     paramChunkMap.onFullChunkStatusChange(this.pos, paramFullChunkStatus);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateFutures(ChunkMap paramChunkMap, Executor paramExecutor) {
/* 296 */     FullChunkStatus fullChunkStatus1 = ChunkLevel.fullStatus(this.oldTicketLevel);
/* 297 */     FullChunkStatus fullChunkStatus2 = ChunkLevel.fullStatus(this.ticketLevel);
/*     */     
/* 299 */     boolean bool1 = fullChunkStatus1.isOrAfter(FullChunkStatus.FULL);
/* 300 */     boolean bool2 = fullChunkStatus2.isOrAfter(FullChunkStatus.FULL);
/* 301 */     this.wasAccessibleSinceLastSave |= bool2;
/*     */     
/* 303 */     if (!bool1 && bool2) {
/* 304 */       this.fullChunkFuture = paramChunkMap.prepareAccessibleChunk(this);
/* 305 */       scheduleFullChunkPromotion(paramChunkMap, this.fullChunkFuture, paramExecutor, FullChunkStatus.FULL);
/* 306 */       addSaveDependency(this.fullChunkFuture);
/*     */     } 
/* 308 */     if (bool1 && !bool2) {
/* 309 */       this.fullChunkFuture.complete(UNLOADED_LEVEL_CHUNK);
/* 310 */       this.fullChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*     */     } 
/*     */     
/* 313 */     boolean bool3 = fullChunkStatus1.isOrAfter(FullChunkStatus.BLOCK_TICKING);
/* 314 */     boolean bool4 = fullChunkStatus2.isOrAfter(FullChunkStatus.BLOCK_TICKING);
/*     */     
/* 316 */     if (!bool3 && bool4) {
/* 317 */       this.tickingChunkFuture = paramChunkMap.prepareTickingChunk(this);
/* 318 */       scheduleFullChunkPromotion(paramChunkMap, this.tickingChunkFuture, paramExecutor, FullChunkStatus.BLOCK_TICKING);
/* 319 */       addSaveDependency(this.tickingChunkFuture);
/*     */     } 
/* 321 */     if (bool3 && !bool4) {
/* 322 */       this.tickingChunkFuture.complete(UNLOADED_LEVEL_CHUNK);
/* 323 */       this.tickingChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*     */     } 
/*     */     
/* 326 */     boolean bool5 = fullChunkStatus1.isOrAfter(FullChunkStatus.ENTITY_TICKING);
/* 327 */     boolean bool6 = fullChunkStatus2.isOrAfter(FullChunkStatus.ENTITY_TICKING);
/*     */     
/* 329 */     if (!bool5 && bool6) {
/* 330 */       if (this.entityTickingChunkFuture != UNLOADED_LEVEL_CHUNK_FUTURE) {
/* 331 */         throw (IllegalStateException)Util.pauseInIde(new IllegalStateException());
/*     */       }
/* 333 */       this.entityTickingChunkFuture = paramChunkMap.prepareEntityTickingChunk(this);
/* 334 */       scheduleFullChunkPromotion(paramChunkMap, this.entityTickingChunkFuture, paramExecutor, FullChunkStatus.ENTITY_TICKING);
/* 335 */       addSaveDependency(this.entityTickingChunkFuture);
/*     */     } 
/* 337 */     if (bool5 && !bool6) {
/* 338 */       this.entityTickingChunkFuture.complete(UNLOADED_LEVEL_CHUNK);
/* 339 */       this.entityTickingChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*     */     } 
/*     */     
/* 342 */     if (!fullChunkStatus2.isOrAfter(fullChunkStatus1)) {
/* 343 */       demoteFullChunk(paramChunkMap, fullChunkStatus2);
/*     */     }
/*     */     
/* 346 */     this.onLevelChange.onLevelChange(this.pos, this::getQueueLevel, this.ticketLevel, this::setQueueLevel);
/* 347 */     this.oldTicketLevel = this.ticketLevel;
/*     */   }
/*     */   
/*     */   public boolean wasAccessibleSinceLastSave() {
/* 351 */     return this.wasAccessibleSinceLastSave;
/*     */   }
/*     */   
/*     */   public void refreshAccessibility() {
/* 355 */     this.wasAccessibleSinceLastSave = ChunkLevel.fullStatus(this.ticketLevel).isOrAfter(FullChunkStatus.FULL);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface LevelChangeListener {
/*     */     void onLevelChange(ChunkPos param1ChunkPos, IntSupplier param1IntSupplier, int param1Int, IntConsumer param1IntConsumer);
/*     */   }
/*     */   
/*     */   public static interface PlayerProvider {
/*     */     List<ServerPlayer> getPlayers(ChunkPos param1ChunkPos, boolean param1Boolean);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\ChunkHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */