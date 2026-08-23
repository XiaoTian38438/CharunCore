/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkSource;
/*     */ import net.minecraft.world.level.chunk.EmptyLevelChunk;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathNavigationRegion
/*     */   implements CollisionGetter
/*     */ {
/*     */   protected final int centerX;
/*     */   protected final int centerZ;
/*     */   protected final ChunkAccess[][] chunks;
/*     */   protected boolean allEmpty;
/*     */   protected final Level level;
/*     */   private final Supplier<Holder<Biome>> plains;
/*     */   
/*     */   public PathNavigationRegion(Level paramLevel, BlockPos paramBlockPos1, BlockPos paramBlockPos2) {
/*  39 */     this.level = paramLevel;
/*     */     
/*  41 */     this.plains = (Supplier<Holder<Biome>>)Suppliers.memoize(() -> paramLevel.registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS));
/*     */     
/*  43 */     this.centerX = SectionPos.blockToSectionCoord(paramBlockPos1.getX());
/*  44 */     this.centerZ = SectionPos.blockToSectionCoord(paramBlockPos1.getZ());
/*  45 */     int i = SectionPos.blockToSectionCoord(paramBlockPos2.getX());
/*  46 */     int j = SectionPos.blockToSectionCoord(paramBlockPos2.getZ());
/*     */     
/*  48 */     this.chunks = new ChunkAccess[i - this.centerX + 1][j - this.centerZ + 1];
/*     */     
/*  50 */     ChunkSource chunkSource = paramLevel.getChunkSource();
/*  51 */     this.allEmpty = true; int k;
/*  52 */     for (k = this.centerX; k <= i; k++) {
/*  53 */       for (int m = this.centerZ; m <= j; m++) {
/*  54 */         this.chunks[k - this.centerX][m - this.centerZ] = (ChunkAccess)chunkSource.getChunkNow(k, m);
/*     */       }
/*     */     } 
/*     */     
/*  58 */     for (k = SectionPos.blockToSectionCoord(paramBlockPos1.getX()); k <= SectionPos.blockToSectionCoord(paramBlockPos2.getX()); k++) {
/*  59 */       for (int m = SectionPos.blockToSectionCoord(paramBlockPos1.getZ()); m <= SectionPos.blockToSectionCoord(paramBlockPos2.getZ()); m++) {
/*  60 */         ChunkAccess chunkAccess = this.chunks[k - this.centerX][m - this.centerZ];
/*  61 */         if (chunkAccess != null && 
/*  62 */           !chunkAccess.isYSpaceEmpty(paramBlockPos1.getY(), paramBlockPos2.getY())) {
/*  63 */           this.allEmpty = false;
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private ChunkAccess getChunk(BlockPos paramBlockPos) {
/*  72 */     return getChunk(SectionPos.blockToSectionCoord(paramBlockPos.getX()), SectionPos.blockToSectionCoord(paramBlockPos.getZ()));
/*     */   }
/*     */   
/*     */   private ChunkAccess getChunk(int paramInt1, int paramInt2) {
/*  76 */     int i = paramInt1 - this.centerX;
/*  77 */     int j = paramInt2 - this.centerZ;
/*     */     
/*  79 */     if (i < 0 || i >= this.chunks.length || j < 0 || j >= (this.chunks[i]).length) {
/*  80 */       return (ChunkAccess)new EmptyLevelChunk(this.level, new ChunkPos(paramInt1, paramInt2), this.plains.get());
/*     */     }
/*  82 */     ChunkAccess chunkAccess = this.chunks[i][j];
/*  83 */     return (chunkAccess != null) ? chunkAccess : (ChunkAccess)new EmptyLevelChunk(this.level, new ChunkPos(paramInt1, paramInt2), this.plains.get());
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldBorder getWorldBorder() {
/*  88 */     return this.level.getWorldBorder();
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockGetter getChunkForCollisions(int paramInt1, int paramInt2) {
/*  93 */     return (BlockGetter)getChunk(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<VoxelShape> getEntityCollisions(Entity paramEntity, AABB paramAABB) {
/*  98 */     return List.of();
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity getBlockEntity(BlockPos paramBlockPos) {
/* 103 */     ChunkAccess chunkAccess = getChunk(paramBlockPos);
/* 104 */     return chunkAccess.getBlockEntity(paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getBlockState(BlockPos paramBlockPos) {
/* 109 */     if (isOutsideBuildHeight(paramBlockPos)) {
/* 110 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 113 */     ChunkAccess chunkAccess = getChunk(paramBlockPos);
/* 114 */     return chunkAccess.getBlockState(paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockPos paramBlockPos) {
/* 119 */     if (isOutsideBuildHeight(paramBlockPos)) {
/* 120 */       return Fluids.EMPTY.defaultFluidState();
/*     */     }
/*     */     
/* 123 */     ChunkAccess chunkAccess = getChunk(paramBlockPos);
/* 124 */     return chunkAccess.getFluidState(paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMinY() {
/* 129 */     return this.level.getMinY();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 134 */     return this.level.getHeight();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\PathNavigationRegion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */