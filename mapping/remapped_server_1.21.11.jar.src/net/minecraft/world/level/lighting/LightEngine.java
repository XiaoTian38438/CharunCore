/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
/*     */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunk;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LightEngine<M extends DataLayerStorageMap<M>, S extends LayerLightSectionStorage<M>>
/*     */   implements LayerLightEventListener
/*     */ {
/*     */   public static final int MAX_LEVEL = 15;
/*     */   protected static final int MIN_OPACITY = 1;
/*  29 */   protected static final long PULL_LIGHT_IN_ENTRY = QueueEntry.decreaseAllDirections(1);
/*     */   
/*     */   private static final int MIN_QUEUE_SIZE = 512;
/*     */   
/*  33 */   protected static final Direction[] PROPAGATION_DIRECTIONS = Direction.values();
/*     */   
/*     */   protected final LightChunkGetter chunkSource;
/*     */   
/*     */   protected final S storage;
/*  38 */   private final LongOpenHashSet blockNodesToCheck = new LongOpenHashSet(512, 0.5F);
/*  39 */   private final LongArrayFIFOQueue decreaseQueue = new LongArrayFIFOQueue();
/*  40 */   private final LongArrayFIFOQueue increaseQueue = new LongArrayFIFOQueue();
/*     */   
/*     */   private static final int CACHE_SIZE = 2;
/*  43 */   private final long[] lastChunkPos = new long[2];
/*  44 */   private final LightChunk[] lastChunk = new LightChunk[2];
/*     */   
/*     */   protected LightEngine(LightChunkGetter paramLightChunkGetter, S paramS) {
/*  47 */     this.chunkSource = paramLightChunkGetter;
/*  48 */     this.storage = paramS;
/*  49 */     clearChunkCache();
/*     */   }
/*     */   
/*     */   public static boolean hasDifferentLightProperties(BlockState paramBlockState1, BlockState paramBlockState2) {
/*  53 */     if (paramBlockState2 == paramBlockState1) {
/*  54 */       return false;
/*     */     }
/*  56 */     return (paramBlockState2.getLightBlock() != paramBlockState1.getLightBlock() || paramBlockState2
/*  57 */       .getLightEmission() != paramBlockState1.getLightEmission() || paramBlockState2
/*  58 */       .useShapeForLightOcclusion() || paramBlockState1
/*  59 */       .useShapeForLightOcclusion());
/*     */   }
/*     */   
/*     */   public static int getLightBlockInto(BlockState paramBlockState1, BlockState paramBlockState2, Direction paramDirection, int paramInt) {
/*  63 */     boolean bool1 = isEmptyShape(paramBlockState1);
/*  64 */     boolean bool2 = isEmptyShape(paramBlockState2);
/*     */     
/*  66 */     if (bool1 && bool2) {
/*  67 */       return paramInt;
/*     */     }
/*     */     
/*  70 */     VoxelShape voxelShape1 = bool1 ? Shapes.empty() : paramBlockState1.getOcclusionShape();
/*  71 */     VoxelShape voxelShape2 = bool2 ? Shapes.empty() : paramBlockState2.getOcclusionShape();
/*     */     
/*  73 */     if (Shapes.mergedFaceOccludes(voxelShape1, voxelShape2, paramDirection)) {
/*  74 */       return 16;
/*     */     }
/*     */     
/*  77 */     return paramInt;
/*     */   }
/*     */   
/*     */   public static VoxelShape getOcclusionShape(BlockState paramBlockState, Direction paramDirection) {
/*  81 */     return isEmptyShape(paramBlockState) ? Shapes.empty() : paramBlockState.getFaceOcclusionShape(paramDirection);
/*     */   }
/*     */   
/*     */   protected static boolean isEmptyShape(BlockState paramBlockState) {
/*  85 */     return (!paramBlockState.canOcclude() || !paramBlockState.useShapeForLightOcclusion());
/*     */   }
/*     */   
/*     */   protected BlockState getState(BlockPos paramBlockPos) {
/*  89 */     int i = SectionPos.blockToSectionCoord(paramBlockPos.getX());
/*  90 */     int j = SectionPos.blockToSectionCoord(paramBlockPos.getZ());
/*  91 */     LightChunk lightChunk = getChunk(i, j);
/*  92 */     if (lightChunk == null)
/*     */     {
/*     */ 
/*     */       
/*  96 */       return Blocks.BEDROCK.defaultBlockState();
/*     */     }
/*  98 */     return lightChunk.getBlockState(paramBlockPos);
/*     */   }
/*     */   
/*     */   protected int getOpacity(BlockState paramBlockState) {
/* 102 */     return Math.max(1, paramBlockState.getLightBlock());
/*     */   }
/*     */   
/*     */   protected boolean shapeOccludes(BlockState paramBlockState1, BlockState paramBlockState2, Direction paramDirection) {
/* 106 */     VoxelShape voxelShape1 = getOcclusionShape(paramBlockState1, paramDirection);
/* 107 */     VoxelShape voxelShape2 = getOcclusionShape(paramBlockState2, paramDirection.getOpposite());
/* 108 */     return Shapes.faceShapeOccludes(voxelShape1, voxelShape2);
/*     */   }
/*     */   
/*     */   protected LightChunk getChunk(int paramInt1, int paramInt2) {
/* 112 */     long l = ChunkPos.asLong(paramInt1, paramInt2);
/* 113 */     for (byte b1 = 0; b1 < 2; b1++) {
/* 114 */       if (l == this.lastChunkPos[b1]) {
/* 115 */         return this.lastChunk[b1];
/*     */       }
/*     */     } 
/* 118 */     LightChunk lightChunk = this.chunkSource.getChunkForLighting(paramInt1, paramInt2);
/* 119 */     for (byte b2 = 1; b2; b2--) {
/* 120 */       this.lastChunkPos[b2] = this.lastChunkPos[b2 - 1];
/* 121 */       this.lastChunk[b2] = this.lastChunk[b2 - 1];
/*     */     } 
/* 123 */     this.lastChunkPos[0] = l;
/* 124 */     this.lastChunk[0] = lightChunk;
/* 125 */     return lightChunk;
/*     */   }
/*     */   
/*     */   private void clearChunkCache() {
/* 129 */     Arrays.fill(this.lastChunkPos, ChunkPos.INVALID_CHUNK_POS);
/* 130 */     Arrays.fill((Object[])this.lastChunk, (Object)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkBlock(BlockPos paramBlockPos) {
/* 135 */     this.blockNodesToCheck.add(paramBlockPos.asLong());
/*     */   }
/*     */   
/*     */   public void queueSectionData(long paramLong, DataLayer paramDataLayer) {
/* 139 */     this.storage.queueSectionData(paramLong, paramDataLayer);
/*     */   }
/*     */   
/*     */   public void retainData(ChunkPos paramChunkPos, boolean paramBoolean) {
/* 143 */     this.storage.retainData(SectionPos.getZeroNode(paramChunkPos.x, paramChunkPos.z), paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateSectionStatus(SectionPos paramSectionPos, boolean paramBoolean) {
/* 148 */     this.storage.updateSectionStatus(paramSectionPos.asLong(), paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLightEnabled(ChunkPos paramChunkPos, boolean paramBoolean) {
/* 153 */     this.storage.setLightEnabled(SectionPos.getZeroNode(paramChunkPos.x, paramChunkPos.z), paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public int runLightUpdates() {
/* 158 */     LongIterator longIterator = this.blockNodesToCheck.iterator();
/* 159 */     while (longIterator.hasNext()) {
/* 160 */       checkNode(longIterator.nextLong());
/*     */     }
/* 162 */     this.blockNodesToCheck.clear();
/* 163 */     this.blockNodesToCheck.trim(512);
/*     */     
/* 165 */     int i = 0;
/* 166 */     i += propagateDecreases();
/* 167 */     i += propagateIncreases();
/*     */     
/* 169 */     clearChunkCache();
/*     */     
/* 171 */     this.storage.markNewInconsistencies(this);
/* 172 */     this.storage.swapSectionMap();
/*     */     
/* 174 */     return i;
/*     */   }
/*     */   
/*     */   private int propagateIncreases() {
/* 178 */     byte b = 0;
/* 179 */     while (!this.increaseQueue.isEmpty()) {
/* 180 */       long l1 = this.increaseQueue.dequeueLong();
/* 181 */       long l2 = this.increaseQueue.dequeueLong();
/*     */       
/* 183 */       int i = this.storage.getStoredLevel(l1);
/*     */       
/* 185 */       int j = QueueEntry.getFromLevel(l2);
/* 186 */       if (QueueEntry.isIncreaseFromEmission(l2) && i < j) {
/* 187 */         this.storage.setStoredLevel(l1, j);
/* 188 */         i = j;
/*     */       } 
/* 190 */       if (i == j) {
/* 191 */         propagateIncrease(l1, l2, i);
/*     */       }
/*     */       
/* 194 */       b++;
/*     */     } 
/* 196 */     return b;
/*     */   }
/*     */   
/*     */   private int propagateDecreases() {
/* 200 */     byte b = 0;
/* 201 */     while (!this.decreaseQueue.isEmpty()) {
/* 202 */       long l1 = this.decreaseQueue.dequeueLong();
/* 203 */       long l2 = this.decreaseQueue.dequeueLong();
/* 204 */       propagateDecrease(l1, l2);
/* 205 */       b++;
/*     */     } 
/* 207 */     return b;
/*     */   }
/*     */   
/*     */   protected void enqueueDecrease(long paramLong1, long paramLong2) {
/* 211 */     this.decreaseQueue.enqueue(paramLong1);
/* 212 */     this.decreaseQueue.enqueue(paramLong2);
/*     */   }
/*     */   
/*     */   protected void enqueueIncrease(long paramLong1, long paramLong2) {
/* 216 */     this.increaseQueue.enqueue(paramLong1);
/* 217 */     this.increaseQueue.enqueue(paramLong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasLightWork() {
/* 222 */     return (this.storage.hasInconsistencies() || !this.blockNodesToCheck.isEmpty() || !this.decreaseQueue.isEmpty() || !this.increaseQueue.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public DataLayer getDataLayerData(SectionPos paramSectionPos) {
/* 227 */     return this.storage.getDataLayerData(paramSectionPos.asLong());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLightValue(BlockPos paramBlockPos) {
/* 232 */     return this.storage.getLightValue(paramBlockPos.asLong());
/*     */   }
/*     */   
/*     */   public String getDebugData(long paramLong) {
/* 236 */     return getDebugSectionType(paramLong).display();
/*     */   }
/*     */   
/*     */   public LayerLightSectionStorage.SectionType getDebugSectionType(long paramLong) {
/* 240 */     return this.storage.getDebugSectionType(paramLong);
/*     */   }
/*     */   
/*     */   protected abstract void checkNode(long paramLong);
/*     */   
/*     */   protected abstract void propagateIncrease(long paramLong1, long paramLong2, int paramInt);
/*     */   
/*     */   protected abstract void propagateDecrease(long paramLong1, long paramLong2);
/*     */   
/*     */   public static class QueueEntry {
/*     */     private static final int FROM_LEVEL_BITS = 4;
/*     */     private static final int DIRECTION_BITS = 6;
/*     */     private static final long LEVEL_MASK = 15L;
/*     */     private static final long DIRECTIONS_MASK = 1008L;
/*     */     private static final long FLAG_FROM_EMPTY_SHAPE = 1024L;
/*     */     private static final long FLAG_INCREASE_FROM_EMISSION = 2048L;
/*     */     
/*     */     public static long decreaseSkipOneDirection(int param1Int, Direction param1Direction) {
/* 258 */       long l = withoutDirection(1008L, param1Direction);
/* 259 */       return withLevel(l, param1Int);
/*     */     }
/*     */     
/*     */     public static long decreaseAllDirections(int param1Int) {
/* 263 */       return withLevel(1008L, param1Int);
/*     */     }
/*     */     
/*     */     public static long increaseLightFromEmission(int param1Int, boolean param1Boolean) {
/* 267 */       long l = 1008L;
/* 268 */       l |= 0x800L;
/* 269 */       if (param1Boolean) {
/* 270 */         l |= 0x400L;
/*     */       }
/* 272 */       return withLevel(l, param1Int);
/*     */     }
/*     */     
/*     */     public static long increaseSkipOneDirection(int param1Int, boolean param1Boolean, Direction param1Direction) {
/* 276 */       long l = withoutDirection(1008L, param1Direction);
/* 277 */       if (param1Boolean) {
/* 278 */         l |= 0x400L;
/*     */       }
/* 280 */       return withLevel(l, param1Int);
/*     */     }
/*     */     
/*     */     public static long increaseOnlyOneDirection(int param1Int, boolean param1Boolean, Direction param1Direction) {
/* 284 */       long l = 0L;
/* 285 */       if (param1Boolean) {
/* 286 */         l |= 0x400L;
/*     */       }
/* 288 */       l = withDirection(l, param1Direction);
/* 289 */       return withLevel(l, param1Int);
/*     */     }
/*     */     
/*     */     public static long increaseSkySourceInDirections(boolean param1Boolean1, boolean param1Boolean2, boolean param1Boolean3, boolean param1Boolean4, boolean param1Boolean5) {
/* 293 */       long l = withLevel(0L, 15);
/* 294 */       if (param1Boolean1) {
/* 295 */         l = withDirection(l, Direction.DOWN);
/*     */       }
/* 297 */       if (param1Boolean2) {
/* 298 */         l = withDirection(l, Direction.NORTH);
/*     */       }
/* 300 */       if (param1Boolean3) {
/* 301 */         l = withDirection(l, Direction.SOUTH);
/*     */       }
/* 303 */       if (param1Boolean4) {
/* 304 */         l = withDirection(l, Direction.WEST);
/*     */       }
/* 306 */       if (param1Boolean5) {
/* 307 */         l = withDirection(l, Direction.EAST);
/*     */       }
/* 309 */       return l;
/*     */     }
/*     */     
/*     */     public static int getFromLevel(long param1Long) {
/* 313 */       return (int)(param1Long & 0xFL);
/*     */     }
/*     */     
/*     */     public static boolean isFromEmptyShape(long param1Long) {
/* 317 */       return ((param1Long & 0x400L) != 0L);
/*     */     }
/*     */     
/*     */     public static boolean isIncreaseFromEmission(long param1Long) {
/* 321 */       return ((param1Long & 0x800L) != 0L);
/*     */     }
/*     */     
/*     */     public static boolean shouldPropagateInDirection(long param1Long, Direction param1Direction) {
/* 325 */       return ((param1Long & 1L << param1Direction.ordinal() + 4) != 0L);
/*     */     }
/*     */     
/*     */     private static long withLevel(long param1Long, int param1Int) {
/* 329 */       return param1Long & 0xFFFFFFFFFFFFFFF0L | param1Int & 0xFL;
/*     */     }
/*     */     
/*     */     private static long withDirection(long param1Long, Direction param1Direction) {
/* 333 */       return param1Long | 1L << param1Direction.ordinal() + 4;
/*     */     }
/*     */     
/*     */     private static long withoutDirection(long param1Long, Direction param1Direction) {
/* 337 */       return param1Long & (1L << param1Direction.ordinal() + 4 ^ 0xFFFFFFFFFFFFFFFFL);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\lighting\LightEngine.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */