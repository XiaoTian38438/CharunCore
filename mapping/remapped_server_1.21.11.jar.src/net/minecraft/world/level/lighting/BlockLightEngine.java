/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.LightChunk;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ 
/*     */ public final class BlockLightEngine extends LightEngine<BlockLightSectionStorage.BlockDataLayerStorageMap, BlockLightSectionStorage> {
/*  14 */   private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
/*     */   
/*     */   public BlockLightEngine(LightChunkGetter paramLightChunkGetter) {
/*  17 */     this(paramLightChunkGetter, new BlockLightSectionStorage(paramLightChunkGetter));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public BlockLightEngine(LightChunkGetter paramLightChunkGetter, BlockLightSectionStorage paramBlockLightSectionStorage) {
/*  22 */     super(paramLightChunkGetter, paramBlockLightSectionStorage);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkNode(long paramLong) {
/*  27 */     long l = SectionPos.blockToSection(paramLong);
/*  28 */     if (!this.storage.storingLightForSection(l)) {
/*     */       return;
/*     */     }
/*  31 */     BlockState blockState = getState((BlockPos)this.mutablePos.set(paramLong));
/*  32 */     int i = getEmission(paramLong, blockState);
/*  33 */     int j = this.storage.getStoredLevel(paramLong);
/*  34 */     if (i < j) {
/*  35 */       this.storage.setStoredLevel(paramLong, 0);
/*  36 */       enqueueDecrease(paramLong, LightEngine.QueueEntry.decreaseAllDirections(j));
/*     */     } else {
/*  38 */       enqueueDecrease(paramLong, PULL_LIGHT_IN_ENTRY);
/*     */     } 
/*  40 */     if (i > 0) {
/*  41 */       enqueueIncrease(paramLong, LightEngine.QueueEntry.increaseLightFromEmission(i, isEmptyShape(blockState)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void propagateIncrease(long paramLong1, long paramLong2, int paramInt) {
/*  47 */     BlockState blockState = null;
/*  48 */     for (Direction direction : PROPAGATION_DIRECTIONS) {
/*  49 */       if (LightEngine.QueueEntry.shouldPropagateInDirection(paramLong2, direction)) {
/*     */ 
/*     */         
/*  52 */         long l = BlockPos.offset(paramLong1, direction);
/*  53 */         if (this.storage.storingLightForSection(SectionPos.blockToSection(l))) {
/*     */ 
/*     */ 
/*     */           
/*  57 */           int i = this.storage.getStoredLevel(l);
/*  58 */           int j = paramInt - 1;
/*  59 */           if (j > i) {
/*     */ 
/*     */ 
/*     */             
/*  63 */             this.mutablePos.set(l);
/*  64 */             BlockState blockState1 = getState((BlockPos)this.mutablePos);
/*  65 */             int k = paramInt - getOpacity(blockState1);
/*  66 */             if (k > i) {
/*     */ 
/*     */ 
/*     */               
/*  70 */               if (blockState == null) {
/*  71 */                 blockState = LightEngine.QueueEntry.isFromEmptyShape(paramLong2) ? Blocks.AIR.defaultBlockState() : getState((BlockPos)this.mutablePos.set(paramLong1));
/*     */               }
/*  73 */               if (!shapeOccludes(blockState, blockState1, direction)) {
/*  74 */                 this.storage.setStoredLevel(l, k);
/*  75 */                 if (k > 1)
/*  76 */                   enqueueIncrease(l, LightEngine.QueueEntry.increaseSkipOneDirection(k, isEmptyShape(blockState1), direction.getOpposite())); 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } protected void propagateDecrease(long paramLong1, long paramLong2) {
/*  84 */     int i = LightEngine.QueueEntry.getFromLevel(paramLong2);
/*  85 */     for (Direction direction : PROPAGATION_DIRECTIONS) {
/*  86 */       if (LightEngine.QueueEntry.shouldPropagateInDirection(paramLong2, direction)) {
/*     */ 
/*     */         
/*  89 */         long l = BlockPos.offset(paramLong1, direction);
/*  90 */         if (this.storage.storingLightForSection(SectionPos.blockToSection(l))) {
/*     */ 
/*     */ 
/*     */           
/*  94 */           int j = this.storage.getStoredLevel(l);
/*  95 */           if (j != 0)
/*     */           {
/*     */ 
/*     */             
/*  99 */             if (j <= i - 1) {
/* 100 */               BlockState blockState = getState((BlockPos)this.mutablePos.set(l));
/* 101 */               int k = getEmission(l, blockState);
/* 102 */               this.storage.setStoredLevel(l, 0);
/* 103 */               if (k < j) {
/* 104 */                 enqueueDecrease(l, LightEngine.QueueEntry.decreaseSkipOneDirection(j, direction.getOpposite()));
/*     */               }
/* 106 */               if (k > 0) {
/* 107 */                 enqueueIncrease(l, LightEngine.QueueEntry.increaseLightFromEmission(k, isEmptyShape(blockState)));
/*     */               }
/*     */             } else {
/* 110 */               enqueueIncrease(l, LightEngine.QueueEntry.increaseOnlyOneDirection(j, false, direction.getOpposite()));
/*     */             }  } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } private int getEmission(long paramLong, BlockState paramBlockState) {
/* 116 */     int i = paramBlockState.getLightEmission();
/* 117 */     if (i > 0 && this.storage.lightOnInSection(SectionPos.blockToSection(paramLong))) {
/* 118 */       return i;
/*     */     }
/* 120 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void propagateLightSources(ChunkPos paramChunkPos) {
/* 125 */     setLightEnabled(paramChunkPos, true);
/* 126 */     LightChunk lightChunk = this.chunkSource.getChunkForLighting(paramChunkPos.x, paramChunkPos.z);
/* 127 */     if (lightChunk != null)
/* 128 */       lightChunk.findBlockLightSources((paramBlockPos, paramBlockState) -> {
/*     */             int i = paramBlockState.getLightEmission();
/*     */             enqueueIncrease(paramBlockPos.asLong(), LightEngine.QueueEntry.increaseLightFromEmission(i, isEmptyShape(paramBlockState)));
/*     */           }); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\lighting\BlockLightEngine.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */