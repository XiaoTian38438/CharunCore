/*     */ package net.minecraft.world.level.chunk.status;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*     */   private final ChunkStatus status;
/*     */   private final ChunkStep parent;
/*     */   private ChunkStatus[] directDependenciesByRadius;
/*  54 */   private int blockStateWriteRadius = -1;
/*  55 */   private ChunkStatusTask task = ChunkStatusTasks::passThrough;
/*     */   
/*     */   protected Builder(ChunkStatus paramChunkStatus) {
/*  58 */     if (paramChunkStatus.getParent() != paramChunkStatus) {
/*  59 */       throw new IllegalArgumentException("Not starting with the first status: " + String.valueOf(paramChunkStatus));
/*     */     }
/*  61 */     this.status = paramChunkStatus;
/*  62 */     this.parent = null;
/*  63 */     this.directDependenciesByRadius = new ChunkStatus[0];
/*     */   }
/*     */   
/*     */   protected Builder(ChunkStatus paramChunkStatus, ChunkStep paramChunkStep) {
/*  67 */     if (paramChunkStep.targetStatus.getIndex() != paramChunkStatus.getIndex() - 1) {
/*  68 */       throw new IllegalArgumentException("Out of order status: " + String.valueOf(paramChunkStatus));
/*     */     }
/*  70 */     this.status = paramChunkStatus;
/*  71 */     this.parent = paramChunkStep;
/*  72 */     this.directDependenciesByRadius = new ChunkStatus[] { paramChunkStep.targetStatus };
/*     */   }
/*     */   
/*     */   public Builder addRequirement(ChunkStatus paramChunkStatus, int paramInt) {
/*  76 */     if (paramChunkStatus.isOrAfter(this.status)) {
/*  77 */       throw new IllegalArgumentException("Status " + String.valueOf(paramChunkStatus) + " can not be required by " + String.valueOf(this.status));
/*     */     }
/*  79 */     ChunkStatus[] arrayOfChunkStatus = this.directDependenciesByRadius;
/*  80 */     int i = paramInt + 1;
/*  81 */     if (i > arrayOfChunkStatus.length) {
/*  82 */       this.directDependenciesByRadius = new ChunkStatus[i];
/*  83 */       Arrays.fill((Object[])this.directDependenciesByRadius, paramChunkStatus);
/*     */     } 
/*  85 */     for (byte b = 0; b < Math.min(i, arrayOfChunkStatus.length); b++) {
/*  86 */       this.directDependenciesByRadius[b] = ChunkStatus.max(arrayOfChunkStatus[b], paramChunkStatus);
/*     */     }
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public Builder blockStateWriteRadius(int paramInt) {
/*  92 */     this.blockStateWriteRadius = paramInt;
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setTask(ChunkStatusTask paramChunkStatusTask) {
/*  97 */     this.task = paramChunkStatusTask;
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public ChunkStep build() {
/* 102 */     return new ChunkStep(this.status, new ChunkDependencies(ImmutableList.copyOf((Object[])this.directDependenciesByRadius)), new ChunkDependencies(ImmutableList.copyOf((Object[])buildAccumulatedDependencies())), this.blockStateWriteRadius, this.task);
/*     */   }
/*     */   
/*     */   private ChunkStatus[] buildAccumulatedDependencies() {
/* 106 */     if (this.parent == null) {
/* 107 */       return this.directDependenciesByRadius;
/*     */     }
/* 109 */     int i = getRadiusOfParent(this.parent.targetStatus);
/* 110 */     ChunkDependencies chunkDependencies = this.parent.accumulatedDependencies;
/* 111 */     ChunkStatus[] arrayOfChunkStatus = new ChunkStatus[Math.max(i + chunkDependencies.size(), this.directDependenciesByRadius.length)];
/* 112 */     for (byte b = 0; b < arrayOfChunkStatus.length; b++) {
/* 113 */       int j = b - i;
/* 114 */       if (j < 0 || j >= chunkDependencies.size()) {
/* 115 */         arrayOfChunkStatus[b] = this.directDependenciesByRadius[b];
/* 116 */       } else if (b >= this.directDependenciesByRadius.length) {
/* 117 */         arrayOfChunkStatus[b] = chunkDependencies.get(j);
/*     */       } else {
/* 119 */         arrayOfChunkStatus[b] = ChunkStatus.max(this.directDependenciesByRadius[b], chunkDependencies.get(j));
/*     */       } 
/*     */     } 
/* 122 */     return arrayOfChunkStatus;
/*     */   }
/*     */   
/*     */   private int getRadiusOfParent(ChunkStatus paramChunkStatus) {
/* 126 */     for (int i = this.directDependenciesByRadius.length - 1; i >= 0; i--) {
/* 127 */       if (this.directDependenciesByRadius[i].isOrAfter(paramChunkStatus)) {
/* 128 */         return i;
/*     */       }
/*     */     } 
/* 131 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\status\ChunkStep$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */