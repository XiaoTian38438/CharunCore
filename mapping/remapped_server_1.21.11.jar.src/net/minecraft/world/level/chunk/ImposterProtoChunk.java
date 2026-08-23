/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeResolver;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.blending.BlendingData;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.lighting.ChunkSkyLightSources;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.ticks.BlackholeTickAccess;
/*     */ import net.minecraft.world.ticks.TickContainerAccess;
/*     */ 
/*     */ public class ImposterProtoChunk
/*     */   extends ProtoChunk {
/*     */   private final LevelChunk wrapped;
/*     */   private final boolean allowWrites;
/*     */   
/*     */   public ImposterProtoChunk(LevelChunk paramLevelChunk, boolean paramBoolean) {
/*  38 */     super(paramLevelChunk.getPos(), UpgradeData.EMPTY, paramLevelChunk.levelHeightAccessor, paramLevelChunk.getLevel().palettedContainerFactory(), paramLevelChunk.getBlendingData());
/*     */     
/*  40 */     this.wrapped = paramLevelChunk;
/*  41 */     this.allowWrites = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity getBlockEntity(BlockPos paramBlockPos) {
/*  46 */     return this.wrapped.getBlockEntity(paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getBlockState(BlockPos paramBlockPos) {
/*  51 */     return this.wrapped.getBlockState(paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockPos paramBlockPos) {
/*  56 */     return this.wrapped.getFluidState(paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelChunkSection getSection(int paramInt) {
/*  61 */     if (this.allowWrites) {
/*  62 */       return this.wrapped.getSection(paramInt);
/*     */     }
/*  64 */     return super.getSection(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState setBlockState(BlockPos paramBlockPos, BlockState paramBlockState, @UpdateFlags int paramInt) {
/*  69 */     if (this.allowWrites) {
/*  70 */       return this.wrapped.setBlockState(paramBlockPos, paramBlockState, paramInt);
/*     */     }
/*  72 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockEntity(BlockEntity paramBlockEntity) {
/*  77 */     if (this.allowWrites) {
/*  78 */       this.wrapped.setBlockEntity(paramBlockEntity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEntity(Entity paramEntity) {
/*  84 */     if (this.allowWrites) {
/*  85 */       this.wrapped.addEntity(paramEntity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPersistedStatus(ChunkStatus paramChunkStatus) {
/*  91 */     if (this.allowWrites) {
/*  92 */       super.setPersistedStatus(paramChunkStatus);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelChunkSection[] getSections() {
/*  98 */     return this.wrapped.getSections();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeightmap(Heightmap.Types paramTypes, long[] paramArrayOflong) {}
/*     */ 
/*     */   
/*     */   private Heightmap.Types fixType(Heightmap.Types paramTypes) {
/* 106 */     if (paramTypes == Heightmap.Types.WORLD_SURFACE_WG) {
/* 107 */       return Heightmap.Types.WORLD_SURFACE;
/*     */     }
/*     */     
/* 110 */     if (paramTypes == Heightmap.Types.OCEAN_FLOOR_WG) {
/* 111 */       return Heightmap.Types.OCEAN_FLOOR;
/*     */     }
/*     */     
/* 114 */     return paramTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public Heightmap getOrCreateHeightmapUnprimed(Heightmap.Types paramTypes) {
/* 119 */     return this.wrapped.getOrCreateHeightmapUnprimed(paramTypes);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight(Heightmap.Types paramTypes, int paramInt1, int paramInt2) {
/* 124 */     return this.wrapped.getHeight(fixType(paramTypes), paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Holder<Biome> getNoiseBiome(int paramInt1, int paramInt2, int paramInt3) {
/* 129 */     return this.wrapped.getNoiseBiome(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkPos getPos() {
/* 134 */     return this.wrapped.getPos();
/*     */   }
/*     */ 
/*     */   
/*     */   public StructureStart getStartForStructure(Structure paramStructure) {
/* 139 */     return this.wrapped.getStartForStructure(paramStructure);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartForStructure(Structure paramStructure, StructureStart paramStructureStart) {}
/*     */ 
/*     */   
/*     */   public Map<Structure, StructureStart> getAllStarts() {
/* 148 */     return this.wrapped.getAllStarts();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllStarts(Map<Structure, StructureStart> paramMap) {}
/*     */ 
/*     */   
/*     */   public LongSet getReferencesForStructure(Structure paramStructure) {
/* 157 */     return this.wrapped.getReferencesForStructure(paramStructure);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addReferenceForStructure(Structure paramStructure, long paramLong) {}
/*     */ 
/*     */   
/*     */   public Map<Structure, LongSet> getAllReferences() {
/* 166 */     return this.wrapped.getAllReferences();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllReferences(Map<Structure, LongSet> paramMap) {}
/*     */ 
/*     */   
/*     */   public void markUnsaved() {
/* 175 */     this.wrapped.markUnsaved();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeSerialized() {
/* 180 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean tryMarkSaved() {
/* 185 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnsaved() {
/* 191 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkStatus getPersistedStatus() {
/* 196 */     return this.wrapped.getPersistedStatus();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeBlockEntity(BlockPos paramBlockPos) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void markPosForPostprocessing(BlockPos paramBlockPos) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockEntityNbt(CompoundTag paramCompoundTag) {}
/*     */ 
/*     */   
/*     */   public CompoundTag getBlockEntityNbt(BlockPos paramBlockPos) {
/* 213 */     return this.wrapped.getBlockEntityNbt(paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getBlockEntityNbtForSaving(BlockPos paramBlockPos, HolderLookup.Provider paramProvider) {
/* 218 */     return this.wrapped.getBlockEntityNbtForSaving(paramBlockPos, paramProvider);
/*     */   }
/*     */ 
/*     */   
/*     */   public void findBlocks(Predicate<BlockState> paramPredicate, BiConsumer<BlockPos, BlockState> paramBiConsumer) {
/* 223 */     this.wrapped.findBlocks(paramPredicate, paramBiConsumer);
/*     */   }
/*     */ 
/*     */   
/*     */   public TickContainerAccess<Block> getBlockTicks() {
/* 228 */     if (this.allowWrites) {
/* 229 */       return this.wrapped.getBlockTicks();
/*     */     }
/* 231 */     return BlackholeTickAccess.emptyContainer();
/*     */   }
/*     */ 
/*     */   
/*     */   public TickContainerAccess<Fluid> getFluidTicks() {
/* 236 */     if (this.allowWrites) {
/* 237 */       return this.wrapped.getFluidTicks();
/*     */     }
/* 239 */     return BlackholeTickAccess.emptyContainer();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkAccess.PackedTicks getTicksForSerialization(long paramLong) {
/* 244 */     return this.wrapped.getTicksForSerialization(paramLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlendingData getBlendingData() {
/* 249 */     return this.wrapped.getBlendingData();
/*     */   }
/*     */ 
/*     */   
/*     */   public CarvingMask getCarvingMask() {
/* 254 */     if (this.allowWrites) {
/* 255 */       return super.getCarvingMask();
/*     */     }
/* 257 */     throw (UnsupportedOperationException)Util.pauseInIde(new UnsupportedOperationException("Meaningless in this context"));
/*     */   }
/*     */ 
/*     */   
/*     */   public CarvingMask getOrCreateCarvingMask() {
/* 262 */     if (this.allowWrites) {
/* 263 */       return super.getOrCreateCarvingMask();
/*     */     }
/* 265 */     throw (UnsupportedOperationException)Util.pauseInIde(new UnsupportedOperationException("Meaningless in this context"));
/*     */   }
/*     */   
/*     */   public LevelChunk getWrapped() {
/* 269 */     return this.wrapped;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLightCorrect() {
/* 274 */     return this.wrapped.isLightCorrect();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLightCorrect(boolean paramBoolean) {
/* 279 */     this.wrapped.setLightCorrect(paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillBiomesFromNoise(BiomeResolver paramBiomeResolver, Climate.Sampler paramSampler) {
/* 284 */     if (this.allowWrites) {
/* 285 */       this.wrapped.fillBiomesFromNoise(paramBiomeResolver, paramSampler);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void initializeLightSources() {
/* 291 */     this.wrapped.initializeLightSources();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkSkyLightSources getSkyLightSources() {
/* 296 */     return this.wrapped.getSkyLightSources();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\ImposterProtoChunk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */