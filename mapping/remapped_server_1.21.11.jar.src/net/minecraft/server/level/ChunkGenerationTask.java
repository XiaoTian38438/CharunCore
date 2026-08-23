/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.util.StaticCache2D;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkDependencies;
/*     */ import net.minecraft.world.level.chunk.status.ChunkPyramid;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ 
/*     */ 
/*     */ public class ChunkGenerationTask
/*     */ {
/*     */   private final GeneratingChunkMap chunkMap;
/*     */   private final ChunkPos pos;
/*  21 */   private ChunkStatus scheduledStatus = null;
/*     */   
/*     */   public final ChunkStatus targetStatus;
/*     */   
/*     */   private volatile boolean markedForCancellation;
/*  26 */   private final List<CompletableFuture<ChunkResult<ChunkAccess>>> scheduledLayer = new ArrayList<>();
/*     */   
/*     */   private final StaticCache2D<GenerationChunkHolder> cache;
/*     */   private boolean needsGeneration;
/*     */   
/*     */   private ChunkGenerationTask(GeneratingChunkMap paramGeneratingChunkMap, ChunkStatus paramChunkStatus, ChunkPos paramChunkPos, StaticCache2D<GenerationChunkHolder> paramStaticCache2D) {
/*  32 */     this.chunkMap = paramGeneratingChunkMap;
/*  33 */     this.targetStatus = paramChunkStatus;
/*  34 */     this.pos = paramChunkPos;
/*  35 */     this.cache = paramStaticCache2D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ChunkGenerationTask create(GeneratingChunkMap paramGeneratingChunkMap, ChunkStatus paramChunkStatus, ChunkPos paramChunkPos) {
/*  42 */     int i = ChunkPyramid.GENERATION_PYRAMID.getStepTo(paramChunkStatus).getAccumulatedRadiusOf(ChunkStatus.EMPTY);
/*  43 */     StaticCache2D<GenerationChunkHolder> staticCache2D = StaticCache2D.create(paramChunkPos.x, paramChunkPos.z, i, (paramInt1, paramInt2) -> paramGeneratingChunkMap.acquireGeneration(ChunkPos.asLong(paramInt1, paramInt2)));
/*     */     
/*  45 */     return new ChunkGenerationTask(paramGeneratingChunkMap, paramChunkStatus, paramChunkPos, staticCache2D);
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> runUntilWait() {
/*     */     while (true) {
/*  50 */       CompletableFuture<?> completableFuture = waitForScheduledLayer();
/*  51 */       if (completableFuture != null) {
/*  52 */         return completableFuture;
/*     */       }
/*  54 */       if (this.markedForCancellation || this.scheduledStatus == this.targetStatus) {
/*     */         
/*  56 */         releaseClaim();
/*  57 */         return null;
/*     */       } 
/*  59 */       scheduleNextLayer();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void scheduleNextLayer() {
/*     */     ChunkStatus chunkStatus;
/*  65 */     if (this.scheduledStatus == null) {
/*  66 */       chunkStatus = ChunkStatus.EMPTY;
/*  67 */     } else if (!this.needsGeneration && this.scheduledStatus == ChunkStatus.EMPTY && !canLoadWithoutGeneration()) {
/*  68 */       this.needsGeneration = true;
/*     */       
/*  70 */       chunkStatus = ChunkStatus.EMPTY;
/*     */     } else {
/*  72 */       chunkStatus = ChunkStatus.getStatusList().get(this.scheduledStatus.getIndex() + 1);
/*     */     } 
/*  74 */     scheduleLayer(chunkStatus, this.needsGeneration);
/*  75 */     this.scheduledStatus = chunkStatus;
/*     */   }
/*     */   
/*     */   public void markForCancellation() {
/*  79 */     this.markedForCancellation = true;
/*     */   }
/*     */   
/*     */   private void releaseClaim() {
/*  83 */     GenerationChunkHolder generationChunkHolder = (GenerationChunkHolder)this.cache.get(this.pos.x, this.pos.z);
/*     */     
/*  85 */     generationChunkHolder.removeTask(this);
/*  86 */     Objects.requireNonNull(this.chunkMap); this.cache.forEach(this.chunkMap::releaseGeneration);
/*     */   }
/*     */   
/*     */   private boolean canLoadWithoutGeneration() {
/*  90 */     if (this.targetStatus == ChunkStatus.EMPTY) {
/*  91 */       return true;
/*     */     }
/*  93 */     ChunkStatus chunkStatus = ((GenerationChunkHolder)this.cache.get(this.pos.x, this.pos.z)).getPersistedStatus();
/*  94 */     if (chunkStatus == null || chunkStatus.isBefore(this.targetStatus)) {
/*  95 */       return false;
/*     */     }
/*  97 */     ChunkDependencies chunkDependencies = ChunkPyramid.LOADING_PYRAMID.getStepTo(this.targetStatus).accumulatedDependencies();
/*  98 */     int i = chunkDependencies.getRadius();
/*  99 */     for (int j = this.pos.x - i; j <= this.pos.x + i; j++) {
/* 100 */       for (int k = this.pos.z - i; k <= this.pos.z + i; k++) {
/* 101 */         int m = this.pos.getChessboardDistance(j, k);
/* 102 */         ChunkStatus chunkStatus1 = chunkDependencies.get(m);
/* 103 */         ChunkStatus chunkStatus2 = ((GenerationChunkHolder)this.cache.get(j, k)).getPersistedStatus();
/* 104 */         if (chunkStatus2 == null || chunkStatus2.isBefore(chunkStatus1)) {
/* 105 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 109 */     return true;
/*     */   }
/*     */   
/*     */   public GenerationChunkHolder getCenter() {
/* 113 */     return (GenerationChunkHolder)this.cache.get(this.pos.x, this.pos.z);
/*     */   }
/*     */   
/*     */   private void scheduleLayer(ChunkStatus paramChunkStatus, boolean paramBoolean) {
/* 117 */     Zone zone = Profiler.get().zone("scheduleLayer"); 
/* 118 */     try { Objects.requireNonNull(paramChunkStatus); zone.addText(paramChunkStatus::getName);
/* 119 */       int i = getRadiusForLayer(paramChunkStatus, paramBoolean);
/* 120 */       for (int j = this.pos.x - i; j <= this.pos.x + i; j++)
/* 121 */       { for (int k = this.pos.z - i; k <= this.pos.z + i; k++)
/* 122 */         { GenerationChunkHolder generationChunkHolder = (GenerationChunkHolder)this.cache.get(j, k);
/* 123 */           if (this.markedForCancellation || !scheduleChunkInLayer(paramChunkStatus, paramBoolean, generationChunkHolder))
/*     */           
/*     */           { 
/*     */ 
/*     */             
/* 128 */             if (zone != null) zone.close();  return; }  }  }  if (zone != null) zone.close();  }
/*     */     catch (Throwable throwable) { if (zone != null)
/*     */         try { zone.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 133 */      } private int getRadiusForLayer(ChunkStatus paramChunkStatus, boolean paramBoolean) { ChunkPyramid chunkPyramid = paramBoolean ? ChunkPyramid.GENERATION_PYRAMID : ChunkPyramid.LOADING_PYRAMID;
/* 134 */     return chunkPyramid.getStepTo(this.targetStatus).getAccumulatedRadiusOf(paramChunkStatus); }
/*     */ 
/*     */   
/*     */   private boolean scheduleChunkInLayer(ChunkStatus paramChunkStatus, boolean paramBoolean, GenerationChunkHolder paramGenerationChunkHolder) {
/* 138 */     ChunkStatus chunkStatus = paramGenerationChunkHolder.getPersistedStatus();
/* 139 */     boolean bool = (chunkStatus != null && paramChunkStatus.isAfter(chunkStatus)) ? true : false;
/* 140 */     ChunkPyramid chunkPyramid = bool ? ChunkPyramid.GENERATION_PYRAMID : ChunkPyramid.LOADING_PYRAMID;
/* 141 */     if (bool && !paramBoolean) {
/* 142 */       throw new IllegalStateException("Can't load chunk, but didn't expect to need to generate");
/*     */     }
/*     */ 
/*     */     
/* 146 */     CompletableFuture<ChunkResult<ChunkAccess>> completableFuture = paramGenerationChunkHolder.applyStep(chunkPyramid.getStepTo(paramChunkStatus), this.chunkMap, this.cache);
/* 147 */     ChunkResult chunkResult = completableFuture.getNow(null);
/* 148 */     if (chunkResult == null) {
/* 149 */       this.scheduledLayer.add(completableFuture);
/* 150 */       return true;
/*     */     } 
/*     */     
/* 153 */     if (chunkResult.isSuccess()) {
/* 154 */       return true;
/*     */     }
/*     */     
/* 157 */     markForCancellation();
/* 158 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private CompletableFuture<?> waitForScheduledLayer() {
/* 163 */     while (!this.scheduledLayer.isEmpty()) {
/* 164 */       CompletableFuture<ChunkResult> completableFuture = (CompletableFuture)this.scheduledLayer.getLast();
/* 165 */       ChunkResult chunkResult = completableFuture.getNow(null);
/* 166 */       if (chunkResult == null) {
/* 167 */         return completableFuture;
/*     */       }
/* 169 */       this.scheduledLayer.removeLast();
/* 170 */       if (!chunkResult.isSuccess()) {
/* 171 */         markForCancellation();
/*     */       }
/*     */     } 
/*     */     
/* 175 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\ChunkGenerationTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */