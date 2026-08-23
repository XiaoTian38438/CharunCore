/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import net.minecraft.world.level.TicketStorage;
/*    */ 
/*    */ class LoadingChunkTracker
/*    */   extends ChunkTracker {
/*  7 */   private static final int MAX_LEVEL = ChunkLevel.MAX_LEVEL + 1;
/*    */   
/*    */   private final DistanceManager distanceManager;
/*    */   
/*    */   private final TicketStorage ticketStorage;
/*    */   
/*    */   public LoadingChunkTracker(DistanceManager paramDistanceManager, TicketStorage paramTicketStorage) {
/* 14 */     super(MAX_LEVEL + 1, 16, 256);
/* 15 */     this.distanceManager = paramDistanceManager;
/* 16 */     this.ticketStorage = paramTicketStorage;
/* 17 */     paramTicketStorage.setLoadingChunkUpdatedListener(this::update);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getLevelFromSource(long paramLong) {
/* 22 */     return this.ticketStorage.getTicketLevelAt(paramLong, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getLevel(long paramLong) {
/* 27 */     if (!this.distanceManager.isChunkToRemove(paramLong)) {
/* 28 */       ChunkHolder chunkHolder = this.distanceManager.getChunk(paramLong);
/* 29 */       if (chunkHolder != null) {
/* 30 */         return chunkHolder.getTicketLevel();
/*    */       }
/*    */     } 
/* 33 */     return MAX_LEVEL;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setLevel(long paramLong, int paramInt) {
/* 38 */     ChunkHolder chunkHolder = this.distanceManager.getChunk(paramLong);
/* 39 */     int i = (chunkHolder == null) ? MAX_LEVEL : chunkHolder.getTicketLevel();
/* 40 */     if (i == paramInt) {
/*    */       return;
/*    */     }
/* 43 */     chunkHolder = this.distanceManager.updateChunkScheduling(paramLong, paramInt, chunkHolder, i);
/* 44 */     if (chunkHolder != null) {
/* 45 */       this.distanceManager.chunksToUpdateFutures.add(chunkHolder);
/*    */     }
/*    */   }
/*    */   
/*    */   public int runDistanceUpdates(int paramInt) {
/* 50 */     return runUpdates(paramInt);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\LoadingChunkTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */