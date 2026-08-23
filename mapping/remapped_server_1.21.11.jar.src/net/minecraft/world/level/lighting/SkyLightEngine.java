/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunk;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ 
/*     */ public final class SkyLightEngine
/*     */   extends LightEngine<SkyLightSectionStorage.SkyDataLayerStorageMap, SkyLightSectionStorage> {
/*  18 */   private static final long REMOVE_TOP_SKY_SOURCE_ENTRY = LightEngine.QueueEntry.decreaseAllDirections(15);
/*  19 */   private static final long REMOVE_SKY_SOURCE_ENTRY = LightEngine.QueueEntry.decreaseSkipOneDirection(15, Direction.UP);
/*  20 */   private static final long ADD_SKY_SOURCE_ENTRY = LightEngine.QueueEntry.increaseSkipOneDirection(15, false, Direction.UP);
/*     */   
/*  22 */   private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
/*     */   
/*     */   private final ChunkSkyLightSources emptyChunkSources;
/*     */   
/*     */   public SkyLightEngine(LightChunkGetter paramLightChunkGetter) {
/*  27 */     this(paramLightChunkGetter, new SkyLightSectionStorage(paramLightChunkGetter));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   protected SkyLightEngine(LightChunkGetter paramLightChunkGetter, SkyLightSectionStorage paramSkyLightSectionStorage) {
/*  32 */     super(paramLightChunkGetter, paramSkyLightSectionStorage);
/*  33 */     this.emptyChunkSources = new ChunkSkyLightSources((LevelHeightAccessor)paramLightChunkGetter.getLevel());
/*     */   }
/*     */   
/*     */   private static boolean isSourceLevel(int paramInt) {
/*  37 */     return (paramInt == 15);
/*     */   }
/*     */   
/*     */   private int getLowestSourceY(int paramInt1, int paramInt2, int paramInt3) {
/*  41 */     ChunkSkyLightSources chunkSkyLightSources = getChunkSources(SectionPos.blockToSectionCoord(paramInt1), SectionPos.blockToSectionCoord(paramInt2));
/*  42 */     if (chunkSkyLightSources == null) {
/*  43 */       return paramInt3;
/*     */     }
/*  45 */     return chunkSkyLightSources.getLowestSourceY(SectionPos.sectionRelative(paramInt1), SectionPos.sectionRelative(paramInt2));
/*     */   }
/*     */   
/*     */   private ChunkSkyLightSources getChunkSources(int paramInt1, int paramInt2) {
/*  49 */     LightChunk lightChunk = this.chunkSource.getChunkForLighting(paramInt1, paramInt2);
/*  50 */     return (lightChunk != null) ? lightChunk.getSkyLightSources() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkNode(long paramLong) {
/*  55 */     int i = BlockPos.getX(paramLong);
/*  56 */     int j = BlockPos.getY(paramLong);
/*  57 */     int k = BlockPos.getZ(paramLong);
/*  58 */     long l = SectionPos.blockToSection(paramLong);
/*     */     
/*  60 */     int m = this.storage.lightOnInSection(l) ? getLowestSourceY(i, k, 2147483647) : Integer.MAX_VALUE;
/*  61 */     if (m != Integer.MAX_VALUE) {
/*  62 */       updateSourcesInColumn(i, k, m);
/*     */     }
/*     */     
/*  65 */     if (!this.storage.storingLightForSection(l)) {
/*     */       return;
/*     */     }
/*     */     
/*  69 */     boolean bool = (j >= m) ? true : false;
/*  70 */     if (bool) {
/*  71 */       enqueueDecrease(paramLong, REMOVE_SKY_SOURCE_ENTRY);
/*  72 */       enqueueIncrease(paramLong, ADD_SKY_SOURCE_ENTRY);
/*     */     } else {
/*  74 */       int n = this.storage.getStoredLevel(paramLong);
/*  75 */       if (n > 0) {
/*  76 */         this.storage.setStoredLevel(paramLong, 0);
/*  77 */         enqueueDecrease(paramLong, LightEngine.QueueEntry.decreaseAllDirections(n));
/*     */       } else {
/*  79 */         enqueueDecrease(paramLong, PULL_LIGHT_IN_ENTRY);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateSourcesInColumn(int paramInt1, int paramInt2, int paramInt3) {
/*  85 */     int i = SectionPos.sectionToBlockCoord(this.storage.getBottomSectionY());
/*  86 */     removeSourcesBelow(paramInt1, paramInt2, paramInt3, i);
/*  87 */     addSourcesAbove(paramInt1, paramInt2, paramInt3, i);
/*     */   }
/*     */   
/*     */   private void removeSourcesBelow(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  91 */     if (paramInt3 <= paramInt4) {
/*     */       return;
/*     */     }
/*     */     
/*  95 */     int i = SectionPos.blockToSectionCoord(paramInt1);
/*  96 */     int j = SectionPos.blockToSectionCoord(paramInt2);
/*     */     
/*  98 */     int k = paramInt3 - 1;
/*     */     
/* 100 */     int m = SectionPos.blockToSectionCoord(k);
/* 101 */     while (this.storage.hasLightDataAtOrBelow(m)) {
/* 102 */       if (this.storage.storingLightForSection(SectionPos.asLong(i, m, j))) {
/* 103 */         int n = SectionPos.sectionToBlockCoord(m);
/* 104 */         int i1 = n + 15;
/* 105 */         for (int i2 = Math.min(i1, k); i2 >= n; i2--) {
/* 106 */           long l = BlockPos.asLong(paramInt1, i2, paramInt2);
/* 107 */           if (!isSourceLevel(this.storage.getStoredLevel(l))) {
/*     */             return;
/*     */           }
/* 110 */           this.storage.setStoredLevel(l, 0);
/*     */           
/* 112 */           enqueueDecrease(l, (i2 == paramInt3 - 1) ? REMOVE_TOP_SKY_SOURCE_ENTRY : REMOVE_SKY_SOURCE_ENTRY);
/*     */         } 
/*     */       } 
/* 115 */       m--;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addSourcesAbove(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 120 */     int i = SectionPos.blockToSectionCoord(paramInt1);
/* 121 */     int j = SectionPos.blockToSectionCoord(paramInt2);
/*     */     
/* 123 */     int k = Math.max(
/* 124 */         Math.max(getLowestSourceY(paramInt1 - 1, paramInt2, -2147483648), getLowestSourceY(paramInt1 + 1, paramInt2, -2147483648)), 
/* 125 */         Math.max(getLowestSourceY(paramInt1, paramInt2 - 1, -2147483648), getLowestSourceY(paramInt1, paramInt2 + 1, -2147483648)));
/*     */ 
/*     */     
/* 128 */     int m = Math.max(paramInt3, paramInt4);
/* 129 */     long l = SectionPos.asLong(i, SectionPos.blockToSectionCoord(m), j);
/* 130 */     while (!this.storage.isAboveData(l)) {
/* 131 */       if (this.storage.storingLightForSection(l)) {
/* 132 */         int n = SectionPos.sectionToBlockCoord(SectionPos.y(l));
/* 133 */         int i1 = n + 15;
/* 134 */         for (int i2 = Math.max(n, m); i2 <= i1; i2++) {
/* 135 */           long l1 = BlockPos.asLong(paramInt1, i2, paramInt2);
/* 136 */           if (isSourceLevel(this.storage.getStoredLevel(l1))) {
/*     */             return;
/*     */           }
/* 139 */           this.storage.setStoredLevel(l1, 15);
/* 140 */           if (i2 < k || i2 == paramInt3)
/*     */           {
/* 142 */             enqueueIncrease(l1, ADD_SKY_SOURCE_ENTRY);
/*     */           }
/*     */         } 
/*     */       } 
/* 146 */       l = SectionPos.offset(l, Direction.UP);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void propagateIncrease(long paramLong1, long paramLong2, int paramInt) {
/* 152 */     BlockState blockState = null;
/* 153 */     int i = countEmptySectionsBelowIfAtBorder(paramLong1);
/* 154 */     for (Direction direction : PROPAGATION_DIRECTIONS) {
/* 155 */       if (LightEngine.QueueEntry.shouldPropagateInDirection(paramLong2, direction)) {
/*     */ 
/*     */         
/* 158 */         long l = BlockPos.offset(paramLong1, direction);
/* 159 */         if (this.storage.storingLightForSection(SectionPos.blockToSection(l))) {
/*     */ 
/*     */ 
/*     */           
/* 163 */           int j = this.storage.getStoredLevel(l);
/* 164 */           int k = paramInt - 1;
/* 165 */           if (k > j) {
/*     */ 
/*     */ 
/*     */             
/* 169 */             this.mutablePos.set(l);
/* 170 */             BlockState blockState1 = getState((BlockPos)this.mutablePos);
/* 171 */             int m = paramInt - getOpacity(blockState1);
/* 172 */             if (m > j) {
/*     */ 
/*     */ 
/*     */               
/* 176 */               if (blockState == null) {
/* 177 */                 blockState = LightEngine.QueueEntry.isFromEmptyShape(paramLong2) ? Blocks.AIR.defaultBlockState() : getState((BlockPos)this.mutablePos.set(paramLong1));
/*     */               }
/* 179 */               if (!shapeOccludes(blockState, blockState1, direction)) {
/* 180 */                 this.storage.setStoredLevel(l, m);
/* 181 */                 if (m > 1) {
/* 182 */                   enqueueIncrease(l, LightEngine.QueueEntry.increaseSkipOneDirection(m, isEmptyShape(blockState1), direction.getOpposite()));
/*     */                 }
/* 184 */                 propagateFromEmptySections(l, direction, m, true, i);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }  } protected void propagateDecrease(long paramLong1, long paramLong2) {
/* 191 */     int i = countEmptySectionsBelowIfAtBorder(paramLong1);
/* 192 */     int j = LightEngine.QueueEntry.getFromLevel(paramLong2);
/* 193 */     for (Direction direction : PROPAGATION_DIRECTIONS) {
/* 194 */       if (LightEngine.QueueEntry.shouldPropagateInDirection(paramLong2, direction)) {
/*     */ 
/*     */         
/* 197 */         long l = BlockPos.offset(paramLong1, direction);
/* 198 */         if (this.storage.storingLightForSection(SectionPos.blockToSection(l))) {
/*     */ 
/*     */ 
/*     */           
/* 202 */           int k = this.storage.getStoredLevel(l);
/* 203 */           if (k != 0)
/*     */           {
/*     */ 
/*     */             
/* 207 */             if (k <= j - 1) {
/* 208 */               this.storage.setStoredLevel(l, 0);
/* 209 */               enqueueDecrease(l, LightEngine.QueueEntry.decreaseSkipOneDirection(k, direction.getOpposite()));
/* 210 */               propagateFromEmptySections(l, direction, k, false, i);
/*     */             } else {
/* 212 */               enqueueIncrease(l, LightEngine.QueueEntry.increaseOnlyOneDirection(k, false, direction.getOpposite()));
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int countEmptySectionsBelowIfAtBorder(long paramLong) {
/* 222 */     int i = BlockPos.getY(paramLong);
/* 223 */     int j = SectionPos.sectionRelative(i);
/* 224 */     if (j != 0) {
/* 225 */       return 0;
/*     */     }
/* 227 */     int k = BlockPos.getX(paramLong);
/* 228 */     int m = BlockPos.getZ(paramLong);
/* 229 */     int n = SectionPos.sectionRelative(k);
/* 230 */     int i1 = SectionPos.sectionRelative(m);
/* 231 */     if (n == 0 || n == 15 || i1 == 0 || i1 == 15) {
/* 232 */       int i2 = SectionPos.blockToSectionCoord(k);
/* 233 */       int i3 = SectionPos.blockToSectionCoord(i);
/* 234 */       int i4 = SectionPos.blockToSectionCoord(m);
/* 235 */       byte b = 0;
/* 236 */       while (!this.storage.storingLightForSection(SectionPos.asLong(i2, i3 - b - 1, i4)) && this.storage.hasLightDataAtOrBelow(i3 - b - 1)) {
/* 237 */         b++;
/*     */       }
/* 239 */       return b;
/*     */     } 
/* 241 */     return 0;
/*     */   }
/*     */   
/*     */   private void propagateFromEmptySections(long paramLong, Direction paramDirection, int paramInt1, boolean paramBoolean, int paramInt2) {
/* 245 */     if (paramInt2 == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 249 */     int i = BlockPos.getX(paramLong);
/* 250 */     int j = BlockPos.getZ(paramLong);
/* 251 */     if (!crossedSectionEdge(paramDirection, SectionPos.sectionRelative(i), SectionPos.sectionRelative(j))) {
/*     */       return;
/*     */     }
/*     */     
/* 255 */     int k = BlockPos.getY(paramLong);
/* 256 */     int m = SectionPos.blockToSectionCoord(i);
/* 257 */     int n = SectionPos.blockToSectionCoord(j);
/* 258 */     int i1 = SectionPos.blockToSectionCoord(k) - 1;
/*     */     
/* 260 */     int i2 = i1 - paramInt2 + 1;
/* 261 */     while (i1 >= i2) {
/* 262 */       if (!this.storage.storingLightForSection(SectionPos.asLong(m, i1, n))) {
/* 263 */         i1--;
/*     */         continue;
/*     */       } 
/* 266 */       int i3 = SectionPos.sectionToBlockCoord(i1);
/* 267 */       for (byte b = 15; b >= 0; b--) {
/* 268 */         long l = BlockPos.asLong(i, i3 + b, j);
/* 269 */         if (paramBoolean) {
/* 270 */           this.storage.setStoredLevel(l, paramInt1);
/* 271 */           if (paramInt1 > 1)
/*     */           {
/* 273 */             enqueueIncrease(l, LightEngine.QueueEntry.increaseSkipOneDirection(paramInt1, true, paramDirection.getOpposite()));
/*     */           }
/*     */         } else {
/* 276 */           this.storage.setStoredLevel(l, 0);
/* 277 */           enqueueDecrease(l, LightEngine.QueueEntry.decreaseSkipOneDirection(paramInt1, paramDirection.getOpposite()));
/*     */         } 
/*     */       } 
/* 280 */       i1--;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean crossedSectionEdge(Direction paramDirection, int paramInt1, int paramInt2) {
/* 285 */     switch (paramDirection) { case NORTH: return 
/* 286 */           (paramInt2 == 15);
/* 287 */       case SOUTH: return (paramInt2 == 0);
/* 288 */       case WEST: return (paramInt1 == 15);
/* 289 */       case EAST: return (paramInt1 == 0); }
/*     */     
/*     */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLightEnabled(ChunkPos paramChunkPos, boolean paramBoolean) {
/* 296 */     super.setLightEnabled(paramChunkPos, paramBoolean);
/*     */ 
/*     */ 
/*     */     
/* 300 */     if (paramBoolean) {
/* 301 */       ChunkSkyLightSources chunkSkyLightSources = Objects.<ChunkSkyLightSources>requireNonNullElse(getChunkSources(paramChunkPos.x, paramChunkPos.z), this.emptyChunkSources);
/* 302 */       int i = chunkSkyLightSources.getHighestLowestSourceY() - 1;
/* 303 */       int j = SectionPos.blockToSectionCoord(i) + 1;
/*     */       
/* 305 */       long l = SectionPos.getZeroNode(paramChunkPos.x, paramChunkPos.z);
/* 306 */       int k = this.storage.getTopSectionY(l);
/* 307 */       int m = Math.max(this.storage.getBottomSectionY(), j);
/* 308 */       for (int n = k - 1; n >= m; n--) {
/* 309 */         DataLayer dataLayer = this.storage.getDataLayerToWrite(SectionPos.asLong(paramChunkPos.x, n, paramChunkPos.z));
/* 310 */         if (dataLayer != null && dataLayer.isEmpty()) {
/* 311 */           dataLayer.fill(15);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void propagateLightSources(ChunkPos paramChunkPos) {
/* 319 */     long l = SectionPos.getZeroNode(paramChunkPos.x, paramChunkPos.z);
/* 320 */     this.storage.setLightEnabled(l, true);
/*     */     
/* 322 */     ChunkSkyLightSources chunkSkyLightSources1 = Objects.<ChunkSkyLightSources>requireNonNullElse(getChunkSources(paramChunkPos.x, paramChunkPos.z), this.emptyChunkSources);
/* 323 */     ChunkSkyLightSources chunkSkyLightSources2 = Objects.<ChunkSkyLightSources>requireNonNullElse(getChunkSources(paramChunkPos.x, paramChunkPos.z - 1), this.emptyChunkSources);
/* 324 */     ChunkSkyLightSources chunkSkyLightSources3 = Objects.<ChunkSkyLightSources>requireNonNullElse(getChunkSources(paramChunkPos.x, paramChunkPos.z + 1), this.emptyChunkSources);
/* 325 */     ChunkSkyLightSources chunkSkyLightSources4 = Objects.<ChunkSkyLightSources>requireNonNullElse(getChunkSources(paramChunkPos.x - 1, paramChunkPos.z), this.emptyChunkSources);
/* 326 */     ChunkSkyLightSources chunkSkyLightSources5 = Objects.<ChunkSkyLightSources>requireNonNullElse(getChunkSources(paramChunkPos.x + 1, paramChunkPos.z), this.emptyChunkSources);
/*     */     
/* 328 */     int i = this.storage.getTopSectionY(l);
/* 329 */     int j = this.storage.getBottomSectionY();
/*     */     
/* 331 */     int k = SectionPos.sectionToBlockCoord(paramChunkPos.x);
/* 332 */     int m = SectionPos.sectionToBlockCoord(paramChunkPos.z);
/*     */     
/* 334 */     for (int n = i - 1; n >= j; n--) {
/* 335 */       long l1 = SectionPos.asLong(paramChunkPos.x, n, paramChunkPos.z);
/* 336 */       DataLayer dataLayer = this.storage.getDataLayerToWrite(l1);
/* 337 */       if (dataLayer != null) {
/*     */ 
/*     */ 
/*     */         
/* 341 */         int i1 = SectionPos.sectionToBlockCoord(n);
/* 342 */         int i2 = i1 + 15;
/*     */         
/* 344 */         boolean bool = false;
/*     */         
/* 346 */         for (byte b = 0; b < 16; b++) {
/* 347 */           for (byte b1 = 0; b1 < 16; b1++) {
/* 348 */             int i3 = chunkSkyLightSources1.getLowestSourceY(b1, b);
/* 349 */             if (i3 <= i2) {
/*     */ 
/*     */ 
/*     */               
/* 353 */               int i4 = (b == 0) ? chunkSkyLightSources2.getLowestSourceY(b1, 15) : chunkSkyLightSources1.getLowestSourceY(b1, b - 1);
/* 354 */               int i5 = (b == 15) ? chunkSkyLightSources3.getLowestSourceY(b1, 0) : chunkSkyLightSources1.getLowestSourceY(b1, b + 1);
/* 355 */               int i6 = (b1 == 0) ? chunkSkyLightSources4.getLowestSourceY(15, b) : chunkSkyLightSources1.getLowestSourceY(b1 - 1, b);
/* 356 */               int i7 = (b1 == 15) ? chunkSkyLightSources5.getLowestSourceY(0, b) : chunkSkyLightSources1.getLowestSourceY(b1 + 1, b);
/* 357 */               int i8 = Math.max(
/* 358 */                   Math.max(i4, i5), 
/* 359 */                   Math.max(i6, i7));
/*     */ 
/*     */               
/* 362 */               for (int i9 = i2; i9 >= Math.max(i1, i3); i9--) {
/* 363 */                 dataLayer.set(b1, SectionPos.sectionRelative(i9), b, 15);
/* 364 */                 if (i9 == i3 || i9 < i8) {
/* 365 */                   long l2 = BlockPos.asLong(k + b1, i9, m + b);
/* 366 */                   enqueueIncrease(l2, LightEngine.QueueEntry.increaseSkySourceInDirections((i9 == i3), (i9 < i4), (i9 < i5), (i9 < i6), (i9 < i7)));
/*     */                 } 
/*     */               } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 376 */               if (i3 < i1) {
/* 377 */                 bool = true;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/* 382 */         if (!bool)
/*     */           break; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\lighting\SkyLightEngine.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */