/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.BitStorage;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.SimpleBitStorage;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ public class ChunkSkyLightSources
/*     */ {
/*     */   private static final int SIZE = 16;
/*     */   public static final int NEGATIVE_INFINITY = -2147483648;
/*     */   private final int minY;
/*     */   private final BitStorage heightmap;
/*  26 */   private final BlockPos.MutableBlockPos mutablePos1 = new BlockPos.MutableBlockPos();
/*  27 */   private final BlockPos.MutableBlockPos mutablePos2 = new BlockPos.MutableBlockPos();
/*     */ 
/*     */   
/*     */   public ChunkSkyLightSources(LevelHeightAccessor paramLevelHeightAccessor) {
/*  31 */     this.minY = paramLevelHeightAccessor.getMinY() - 1;
/*  32 */     int i = paramLevelHeightAccessor.getMaxY() + 1;
/*  33 */     int j = Mth.ceillog2(i - this.minY + 1);
/*  34 */     this.heightmap = (BitStorage)new SimpleBitStorage(j, 256);
/*     */   }
/*     */   
/*     */   public void fillFrom(ChunkAccess paramChunkAccess) {
/*  38 */     int i = paramChunkAccess.getHighestFilledSectionIndex();
/*  39 */     if (i == -1) {
/*  40 */       fill(this.minY);
/*     */       
/*     */       return;
/*     */     } 
/*  44 */     for (byte b = 0; b < 16; b++) {
/*  45 */       for (byte b1 = 0; b1 < 16; b1++) {
/*  46 */         int j = Math.max(findLowestSourceY(paramChunkAccess, i, b1, b), this.minY);
/*  47 */         set(index(b1, b), j);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int findLowestSourceY(ChunkAccess paramChunkAccess, int paramInt1, int paramInt2, int paramInt3) {
/*  53 */     int i = SectionPos.sectionToBlockCoord(paramChunkAccess.getSectionYFromSectionIndex(paramInt1) + 1);
/*     */     
/*  55 */     BlockPos.MutableBlockPos mutableBlockPos1 = this.mutablePos1.set(paramInt2, i, paramInt3);
/*  56 */     BlockPos.MutableBlockPos mutableBlockPos2 = this.mutablePos2.setWithOffset((Vec3i)mutableBlockPos1, Direction.DOWN);
/*     */     
/*  58 */     BlockState blockState = Blocks.AIR.defaultBlockState();
/*     */     
/*  60 */     for (int j = paramInt1; j >= 0; j--) {
/*  61 */       LevelChunkSection levelChunkSection = paramChunkAccess.getSection(j);
/*  62 */       if (levelChunkSection.hasOnlyAir()) {
/*     */         
/*  64 */         blockState = Blocks.AIR.defaultBlockState();
/*  65 */         int k = paramChunkAccess.getSectionYFromSectionIndex(j);
/*  66 */         mutableBlockPos1.setY(SectionPos.sectionToBlockCoord(k));
/*  67 */         mutableBlockPos2.setY(mutableBlockPos1.getY() - 1);
/*     */       } else {
/*     */         
/*  70 */         for (byte b = 15; b >= 0; b--) {
/*  71 */           BlockState blockState1 = levelChunkSection.getBlockState(paramInt2, b, paramInt3);
/*  72 */           if (isEdgeOccluded(blockState, blockState1)) {
/*  73 */             return mutableBlockPos1.getY();
/*     */           }
/*  75 */           blockState = blockState1;
/*  76 */           mutableBlockPos1.set((Vec3i)mutableBlockPos2);
/*  77 */           mutableBlockPos2.move(Direction.DOWN);
/*     */         } 
/*     */       } 
/*     */     } 
/*  81 */     return this.minY;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean update(BlockGetter paramBlockGetter, int paramInt1, int paramInt2, int paramInt3) {
/*  86 */     int i = paramInt2 + 1;
/*     */     
/*  88 */     int j = index(paramInt1, paramInt3);
/*  89 */     int k = get(j);
/*  90 */     if (i < k) {
/*  91 */       return false;
/*     */     }
/*     */     
/*  94 */     BlockPos.MutableBlockPos mutableBlockPos1 = this.mutablePos1.set(paramInt1, paramInt2 + 1, paramInt3);
/*  95 */     BlockState blockState1 = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos1);
/*  96 */     BlockPos.MutableBlockPos mutableBlockPos2 = this.mutablePos2.set(paramInt1, paramInt2, paramInt3);
/*  97 */     BlockState blockState2 = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos2);
/*  98 */     if (updateEdge(paramBlockGetter, j, k, (BlockPos)mutableBlockPos1, blockState1, (BlockPos)mutableBlockPos2, blockState2)) {
/*  99 */       return true;
/*     */     }
/*     */     
/* 102 */     BlockPos.MutableBlockPos mutableBlockPos3 = this.mutablePos1.set(paramInt1, paramInt2 - 1, paramInt3);
/* 103 */     BlockState blockState3 = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos3);
/* 104 */     return updateEdge(paramBlockGetter, j, k, (BlockPos)mutableBlockPos2, blockState2, (BlockPos)mutableBlockPos3, blockState3);
/*     */   }
/*     */   
/*     */   private boolean updateEdge(BlockGetter paramBlockGetter, int paramInt1, int paramInt2, BlockPos paramBlockPos1, BlockState paramBlockState1, BlockPos paramBlockPos2, BlockState paramBlockState2) {
/* 108 */     int i = paramBlockPos1.getY();
/* 109 */     if (isEdgeOccluded(paramBlockState1, paramBlockState2)) {
/* 110 */       if (i > paramInt2) {
/* 111 */         set(paramInt1, i);
/* 112 */         return true;
/*     */       }
/*     */     
/* 115 */     } else if (i == paramInt2) {
/* 116 */       set(paramInt1, findLowestSourceBelow(paramBlockGetter, paramBlockPos2, paramBlockState2));
/* 117 */       return true;
/*     */     } 
/*     */     
/* 120 */     return false;
/*     */   }
/*     */   
/*     */   private int findLowestSourceBelow(BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 124 */     BlockPos.MutableBlockPos mutableBlockPos1 = this.mutablePos1.set((Vec3i)paramBlockPos);
/* 125 */     BlockPos.MutableBlockPos mutableBlockPos2 = this.mutablePos2.setWithOffset((Vec3i)paramBlockPos, Direction.DOWN);
/* 126 */     BlockState blockState = paramBlockState;
/* 127 */     while (mutableBlockPos2.getY() >= this.minY) {
/* 128 */       BlockState blockState1 = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos2);
/* 129 */       if (isEdgeOccluded(blockState, blockState1)) {
/* 130 */         return mutableBlockPos1.getY();
/*     */       }
/* 132 */       blockState = blockState1;
/* 133 */       mutableBlockPos1.set((Vec3i)mutableBlockPos2);
/* 134 */       mutableBlockPos2.move(Direction.DOWN);
/*     */     } 
/* 136 */     return this.minY;
/*     */   }
/*     */   
/*     */   private static boolean isEdgeOccluded(BlockState paramBlockState1, BlockState paramBlockState2) {
/* 140 */     if (paramBlockState2.getLightBlock() != 0) {
/* 141 */       return true;
/*     */     }
/* 143 */     VoxelShape voxelShape1 = LightEngine.getOcclusionShape(paramBlockState1, Direction.DOWN);
/* 144 */     VoxelShape voxelShape2 = LightEngine.getOcclusionShape(paramBlockState2, Direction.UP);
/* 145 */     return Shapes.faceShapeOccludes(voxelShape1, voxelShape2);
/*     */   }
/*     */   
/*     */   public int getLowestSourceY(int paramInt1, int paramInt2) {
/* 149 */     int i = get(index(paramInt1, paramInt2));
/* 150 */     return extendSourcesBelowWorld(i);
/*     */   }
/*     */   
/*     */   public int getHighestLowestSourceY() {
/* 154 */     int i = Integer.MIN_VALUE;
/* 155 */     for (byte b = 0; b < this.heightmap.getSize(); b++) {
/* 156 */       int j = this.heightmap.get(b);
/* 157 */       if (j > i) {
/* 158 */         i = j;
/*     */       }
/*     */     } 
/* 161 */     return extendSourcesBelowWorld(i + this.minY);
/*     */   }
/*     */   
/*     */   private void fill(int paramInt) {
/* 165 */     int i = paramInt - this.minY;
/* 166 */     for (byte b = 0; b < this.heightmap.getSize(); b++) {
/* 167 */       this.heightmap.set(b, i);
/*     */     }
/*     */   }
/*     */   
/*     */   private void set(int paramInt1, int paramInt2) {
/* 172 */     this.heightmap.set(paramInt1, paramInt2 - this.minY);
/*     */   }
/*     */   
/*     */   private int get(int paramInt) {
/* 176 */     return this.heightmap.get(paramInt) + this.minY;
/*     */   }
/*     */   
/*     */   private int extendSourcesBelowWorld(int paramInt) {
/* 180 */     if (paramInt == this.minY) {
/* 181 */       return Integer.MIN_VALUE;
/*     */     }
/* 183 */     return paramInt;
/*     */   }
/*     */   
/*     */   private static int index(int paramInt1, int paramInt2) {
/* 187 */     return paramInt1 + paramInt2 * 16;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\lighting\ChunkSkyLightSources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */