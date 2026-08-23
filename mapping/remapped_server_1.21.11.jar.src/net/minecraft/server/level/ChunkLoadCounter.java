/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*    */ 
/*    */ public class ChunkLoadCounter
/*    */ {
/* 11 */   private final List<ChunkHolder> pendingChunks = new ArrayList<>();
/*    */   private int totalChunks;
/*    */   
/*    */   public void track(ServerLevel paramServerLevel, Runnable paramRunnable) {
/* 15 */     ServerChunkCache serverChunkCache = paramServerLevel.getChunkSource();
/*    */     
/* 17 */     LongOpenHashSet longOpenHashSet = new LongOpenHashSet();
/* 18 */     serverChunkCache.runDistanceManagerUpdates();
/* 19 */     serverChunkCache.chunkMap.allChunksWithAtLeastStatus(ChunkStatus.FULL)
/* 20 */       .forEach(paramChunkHolder -> paramLongSet.add(paramChunkHolder.getPos().toLong()));
/*    */     
/* 22 */     paramRunnable.run();
/*    */     
/* 24 */     serverChunkCache.runDistanceManagerUpdates();
/* 25 */     serverChunkCache.chunkMap.allChunksWithAtLeastStatus(ChunkStatus.FULL).forEach(paramChunkHolder -> {
/*    */           if (!paramLongSet.contains(paramChunkHolder.getPos().toLong())) {
/*    */             this.pendingChunks.add(paramChunkHolder);
/*    */             this.totalChunks++;
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   public int readyChunks() {
/* 34 */     return this.totalChunks - pendingChunks();
/*    */   }
/*    */   
/*    */   public int pendingChunks() {
/* 38 */     this.pendingChunks.removeIf(paramChunkHolder -> (paramChunkHolder.getLatestStatus() == ChunkStatus.FULL));
/* 39 */     return this.pendingChunks.size();
/*    */   }
/*    */   
/*    */   public int totalChunks() {
/* 43 */     return this.totalChunks;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\ChunkLoadCounter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */