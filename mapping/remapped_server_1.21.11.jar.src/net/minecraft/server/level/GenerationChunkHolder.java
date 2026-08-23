/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.StaticCache2D;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ImposterProtoChunk;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStep;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GenerationChunkHolder
/*     */ {
/*  26 */   private static final List<ChunkStatus> CHUNK_STATUSES = ChunkStatus.getStatusList();
/*     */   
/*  28 */   private static final ChunkResult<ChunkAccess> NOT_DONE_YET = ChunkResult.error("Not done yet");
/*  29 */   public static final ChunkResult<ChunkAccess> UNLOADED_CHUNK = ChunkResult.error("Unloaded chunk");
/*  30 */   public static final CompletableFuture<ChunkResult<ChunkAccess>> UNLOADED_CHUNK_FUTURE = CompletableFuture.completedFuture(UNLOADED_CHUNK);
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ChunkPos pos;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile ChunkStatus highestAllowedStatus;
/*     */ 
/*     */   
/*  41 */   private final AtomicReference<ChunkStatus> startedWork = new AtomicReference<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   private final AtomicReferenceArray<CompletableFuture<ChunkResult<ChunkAccess>>> futures = new AtomicReferenceArray<>(CHUNK_STATUSES.size());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private final AtomicReference<ChunkGenerationTask> task = new AtomicReference<>();
/*     */ 
/*     */ 
/*     */   
/*  61 */   private final AtomicInteger generationRefCount = new AtomicInteger();
/*     */   
/*  63 */   private volatile CompletableFuture<Void> generationSaveSyncFuture = CompletableFuture.completedFuture(null);
/*     */   
/*     */   public GenerationChunkHolder(ChunkPos paramChunkPos) {
/*  66 */     this.pos = paramChunkPos;
/*  67 */     if (!paramChunkPos.isValid()) {
/*  68 */       throw new IllegalStateException("Trying to create chunk out of reasonable bounds: " + String.valueOf(paramChunkPos));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkResult<ChunkAccess>> scheduleChunkGenerationTask(ChunkStatus paramChunkStatus, ChunkMap paramChunkMap) {
/*  76 */     if (isStatusDisallowed(paramChunkStatus)) {
/*  77 */       return UNLOADED_CHUNK_FUTURE;
/*     */     }
/*  79 */     CompletableFuture<ChunkResult<ChunkAccess>> completableFuture = getOrCreateFuture(paramChunkStatus);
/*  80 */     if (completableFuture.isDone()) {
/*  81 */       return completableFuture;
/*     */     }
/*  83 */     ChunkGenerationTask chunkGenerationTask = this.task.get();
/*  84 */     if (chunkGenerationTask == null || paramChunkStatus.isAfter(chunkGenerationTask.targetStatus)) {
/*  85 */       rescheduleChunkTask(paramChunkMap, paramChunkStatus);
/*     */     }
/*  87 */     return completableFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CompletableFuture<ChunkResult<ChunkAccess>> applyStep(ChunkStep paramChunkStep, GeneratingChunkMap paramGeneratingChunkMap, StaticCache2D<GenerationChunkHolder> paramStaticCache2D) {
/*  94 */     if (isStatusDisallowed(paramChunkStep.targetStatus())) {
/*  95 */       return UNLOADED_CHUNK_FUTURE;
/*     */     }
/*     */ 
/*     */     
/*  99 */     if (acquireStatusBump(paramChunkStep.targetStatus())) {
/* 100 */       return paramGeneratingChunkMap.applyStep(this, paramChunkStep, paramStaticCache2D).handle((paramChunkAccess, paramThrowable) -> {
/*     */             if (paramThrowable != null) {
/*     */               CrashReport crashReport = CrashReport.forThrowable(paramThrowable, "Exception chunk generation/loading");
/*     */               
/*     */               MinecraftServer.setFatalException((RuntimeException)new ReportedException(crashReport));
/*     */             } else {
/*     */               completeFuture(paramChunkStep.targetStatus(), paramChunkAccess);
/*     */             } 
/*     */             return ChunkResult.of(paramChunkAccess);
/*     */           });
/*     */     }
/* 111 */     return getOrCreateFuture(paramChunkStep.targetStatus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateHighestAllowedStatus(ChunkMap paramChunkMap) {
/* 118 */     ChunkStatus chunkStatus1 = this.highestAllowedStatus;
/* 119 */     ChunkStatus chunkStatus2 = ChunkLevel.generationStatus(getTicketLevel());
/* 120 */     this.highestAllowedStatus = chunkStatus2;
/* 121 */     boolean bool = (chunkStatus1 != null && (chunkStatus2 == null || chunkStatus2.isBefore(chunkStatus1))) ? true : false;
/* 122 */     if (bool) {
/* 123 */       failAndClearPendingFuturesBetween(chunkStatus2, chunkStatus1);
/*     */       
/* 125 */       if (this.task.get() != null) {
/* 126 */         rescheduleChunkTask(paramChunkMap, findHighestStatusWithPendingFuture(chunkStatus2));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replaceProtoChunk(ImposterProtoChunk paramImposterProtoChunk) {
/* 135 */     CompletableFuture<?> completableFuture = CompletableFuture.completedFuture(ChunkResult.of(paramImposterProtoChunk));
/*     */     
/* 137 */     for (byte b = 0; b < this.futures.length() - 1; b++) {
/* 138 */       CompletableFuture<ChunkResult<ChunkAccess>> completableFuture1 = this.futures.get(b);
/* 139 */       Objects.requireNonNull(completableFuture1);
/*     */       
/* 141 */       ChunkAccess chunkAccess = ((ChunkResult<ChunkAccess>)completableFuture1.getNow(NOT_DONE_YET)).orElse(null);
/* 142 */       if (chunkAccess instanceof net.minecraft.world.level.chunk.ProtoChunk) {
/* 143 */         if (!this.futures.compareAndSet(b, completableFuture1, completableFuture)) {
/* 144 */           throw new IllegalStateException("Future changed by other thread while trying to replace it");
/*     */         }
/*     */       } else {
/* 147 */         throw new IllegalStateException("Trying to replace a ProtoChunk, but found " + String.valueOf(chunkAccess));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void removeTask(ChunkGenerationTask paramChunkGenerationTask) {
/* 156 */     this.task.compareAndSet(paramChunkGenerationTask, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void rescheduleChunkTask(ChunkMap paramChunkMap, ChunkStatus paramChunkStatus) {
/*     */     ChunkGenerationTask chunkGenerationTask1;
/* 164 */     if (paramChunkStatus != null) {
/* 165 */       chunkGenerationTask1 = paramChunkMap.scheduleGenerationTask(paramChunkStatus, getPos());
/*     */     } else {
/* 167 */       chunkGenerationTask1 = null;
/*     */     } 
/* 169 */     ChunkGenerationTask chunkGenerationTask2 = this.task.getAndSet(chunkGenerationTask1);
/* 170 */     if (chunkGenerationTask2 != null) {
/* 171 */       chunkGenerationTask2.markForCancellation();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CompletableFuture<ChunkResult<ChunkAccess>> getOrCreateFuture(ChunkStatus paramChunkStatus) {
/* 179 */     if (isStatusDisallowed(paramChunkStatus)) {
/* 180 */       return UNLOADED_CHUNK_FUTURE;
/*     */     }
/*     */     
/* 183 */     int i = paramChunkStatus.getIndex();
/* 184 */     CompletableFuture<ChunkResult<ChunkAccess>> completableFuture = this.futures.get(i);
/* 185 */     while (completableFuture == null) {
/* 186 */       CompletableFuture<ChunkResult<ChunkAccess>> completableFuture1 = new CompletableFuture();
/* 187 */       completableFuture = this.futures.compareAndExchange(i, null, completableFuture1);
/* 188 */       if (completableFuture == null) {
/* 189 */         if (isStatusDisallowed(paramChunkStatus)) {
/* 190 */           failAndClearPendingFuture(i, completableFuture1);
/* 191 */           return UNLOADED_CHUNK_FUTURE;
/*     */         } 
/* 193 */         return completableFuture1;
/*     */       } 
/*     */     } 
/* 196 */     return completableFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void failAndClearPendingFuturesBetween(ChunkStatus paramChunkStatus1, ChunkStatus paramChunkStatus2) {
/* 203 */     byte b1 = (paramChunkStatus1 == null) ? 0 : (paramChunkStatus1.getIndex() + 1);
/* 204 */     int i = paramChunkStatus2.getIndex();
/* 205 */     for (byte b2 = b1; b2 <= i; b2++) {
/* 206 */       CompletableFuture<ChunkResult<ChunkAccess>> completableFuture = this.futures.get(b2);
/* 207 */       if (completableFuture != null)
/*     */       {
/*     */         
/* 210 */         failAndClearPendingFuture(b2, completableFuture);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void failAndClearPendingFuture(int paramInt, CompletableFuture<ChunkResult<ChunkAccess>> paramCompletableFuture) {
/* 219 */     if (paramCompletableFuture.complete(UNLOADED_CHUNK) && 
/* 220 */       !this.futures.compareAndSet(paramInt, paramCompletableFuture, null)) {
/* 221 */       throw new IllegalStateException("Nothing else should replace the future here");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void completeFuture(ChunkStatus paramChunkStatus, ChunkAccess paramChunkAccess) {
/* 230 */     ChunkResult<ChunkAccess> chunkResult = ChunkResult.of(paramChunkAccess);
/* 231 */     int i = paramChunkStatus.getIndex();
/*     */ 
/*     */     
/*     */     while (true) {
/* 235 */       CompletableFuture<ChunkResult<ChunkAccess>> completableFuture = this.futures.get(i);
/* 236 */       if (completableFuture == null) {
/* 237 */         if (this.futures.compareAndSet(i, null, CompletableFuture.completedFuture(chunkResult)))
/*     */           return; 
/*     */         continue;
/*     */       } 
/* 241 */       if (completableFuture.complete(chunkResult)) {
/*     */         return;
/*     */       }
/* 244 */       if (((ChunkResult)completableFuture.getNow(NOT_DONE_YET)).isSuccess()) {
/* 245 */         throw new IllegalStateException("Trying to complete a future but found it to be completed successfully already");
/*     */       }
/*     */       
/* 248 */       Thread.yield();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ChunkStatus findHighestStatusWithPendingFuture(ChunkStatus paramChunkStatus) {
/* 257 */     if (paramChunkStatus == null) {
/* 258 */       return null;
/*     */     }
/* 260 */     ChunkStatus chunkStatus1 = paramChunkStatus;
/* 261 */     ChunkStatus chunkStatus2 = this.startedWork.get();
/* 262 */     while (chunkStatus2 == null || chunkStatus1.isAfter(chunkStatus2)) {
/*     */       
/* 264 */       if (this.futures.get(chunkStatus1.getIndex()) != null)
/*     */       {
/* 266 */         return chunkStatus1;
/*     */       }
/* 268 */       if (chunkStatus1 == ChunkStatus.EMPTY) {
/*     */         break;
/*     */       }
/* 271 */       chunkStatus1 = chunkStatus1.getParent();
/*     */     } 
/* 273 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean acquireStatusBump(ChunkStatus paramChunkStatus) {
/* 280 */     ChunkStatus chunkStatus1 = (paramChunkStatus == ChunkStatus.EMPTY) ? null : paramChunkStatus.getParent();
/*     */     
/* 282 */     ChunkStatus chunkStatus2 = this.startedWork.compareAndExchange(chunkStatus1, paramChunkStatus);
/* 283 */     if (chunkStatus2 == chunkStatus1) {
/* 284 */       return true;
/*     */     }
/*     */     
/* 287 */     if (chunkStatus2 == null || paramChunkStatus.isAfter(chunkStatus2)) {
/* 288 */       throw new IllegalStateException("Unexpected last startedWork status: " + String.valueOf(chunkStatus2) + " while trying to start: " + String.valueOf(paramChunkStatus));
/*     */     }
/*     */     
/* 291 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isStatusDisallowed(ChunkStatus paramChunkStatus) {
/* 295 */     ChunkStatus chunkStatus = this.highestAllowedStatus;
/* 296 */     return (chunkStatus == null || paramChunkStatus.isAfter(chunkStatus));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void increaseGenerationRefCount() {
/* 305 */     if (this.generationRefCount.getAndIncrement() == 0) {
/* 306 */       this.generationSaveSyncFuture = new CompletableFuture<>();
/* 307 */       addSaveDependency(this.generationSaveSyncFuture);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decreaseGenerationRefCount() {
/* 315 */     CompletableFuture<Void> completableFuture = this.generationSaveSyncFuture;
/* 316 */     int i = this.generationRefCount.decrementAndGet();
/* 317 */     if (i == 0) {
/* 318 */       completableFuture.complete(null);
/*     */     }
/* 320 */     if (i < 0) {
/* 321 */       throw new IllegalStateException("More releases than claims. Count: " + i);
/*     */     }
/*     */   }
/*     */   
/*     */   public ChunkAccess getChunkIfPresentUnchecked(ChunkStatus paramChunkStatus) {
/* 326 */     CompletableFuture<ChunkResult<ChunkAccess>> completableFuture = this.futures.get(paramChunkStatus.getIndex());
/* 327 */     return (completableFuture == null) ? null : ((ChunkResult<ChunkAccess>)completableFuture.getNow(NOT_DONE_YET)).orElse(null);
/*     */   }
/*     */   
/*     */   public ChunkAccess getChunkIfPresent(ChunkStatus paramChunkStatus) {
/* 331 */     if (isStatusDisallowed(paramChunkStatus)) {
/* 332 */       return null;
/*     */     }
/* 334 */     return getChunkIfPresentUnchecked(paramChunkStatus);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkAccess getLatestChunk() {
/* 341 */     ChunkStatus chunkStatus = this.startedWork.get();
/* 342 */     if (chunkStatus == null) {
/* 343 */       return null;
/*     */     }
/* 345 */     ChunkAccess chunkAccess = getChunkIfPresentUnchecked(chunkStatus);
/* 346 */     if (chunkAccess != null) {
/* 347 */       return chunkAccess;
/*     */     }
/* 349 */     return getChunkIfPresentUnchecked(chunkStatus.getParent());
/*     */   }
/*     */   
/*     */   public ChunkStatus getPersistedStatus() {
/* 353 */     CompletableFuture<ChunkResult<ChunkAccess>> completableFuture = this.futures.get(ChunkStatus.EMPTY.getIndex());
/* 354 */     ChunkAccess chunkAccess = (completableFuture == null) ? null : ((ChunkResult<ChunkAccess>)completableFuture.getNow(NOT_DONE_YET)).orElse(null);
/* 355 */     return (chunkAccess == null) ? null : chunkAccess.getPersistedStatus();
/*     */   }
/*     */   
/*     */   public ChunkPos getPos() {
/* 359 */     return this.pos;
/*     */   }
/*     */   
/*     */   public FullChunkStatus getFullStatus() {
/* 363 */     return ChunkLevel.fullStatus(getTicketLevel());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/*     */   public List<Pair<ChunkStatus, CompletableFuture<ChunkResult<ChunkAccess>>>> getAllFutures() {
/* 375 */     ArrayList<Pair> arrayList = new ArrayList();
/*     */     
/* 377 */     for (byte b = 0; b < CHUNK_STATUSES.size(); b++) {
/* 378 */       arrayList.add(Pair.of(CHUNK_STATUSES.get(b), this.futures.get(b)));
/*     */     }
/* 380 */     return (List)arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/*     */   public ChunkStatus getLatestStatus() {
/* 388 */     ChunkStatus chunkStatus = this.startedWork.get();
/* 389 */     if (chunkStatus == null) {
/* 390 */       return null;
/*     */     }
/* 392 */     ChunkAccess chunkAccess = getChunkIfPresentUnchecked(chunkStatus);
/* 393 */     if (chunkAccess != null) {
/* 394 */       return chunkStatus;
/*     */     }
/* 396 */     return chunkStatus.getParent();
/*     */   }
/*     */   
/*     */   protected abstract void addSaveDependency(CompletableFuture<?> paramCompletableFuture);
/*     */   
/*     */   public abstract int getTicketLevel();
/*     */   
/*     */   public abstract int getQueueLevel();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\GenerationChunkHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */