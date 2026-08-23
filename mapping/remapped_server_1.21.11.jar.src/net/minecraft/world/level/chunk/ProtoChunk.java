/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortList;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.blending.BlendingData;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.lighting.LightEngine;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.ticks.LevelChunkTicks;
/*     */ import net.minecraft.world.ticks.ProtoChunkTicks;
/*     */ import net.minecraft.world.ticks.TickContainerAccess;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ProtoChunk extends ChunkAccess {
/*  45 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private volatile LevelLightEngine lightEngine;
/*     */   
/*  49 */   private volatile ChunkStatus status = ChunkStatus.EMPTY;
/*  50 */   private final List<CompoundTag> entities = Lists.newArrayList();
/*     */   
/*     */   private CarvingMask carvingMask;
/*     */   
/*     */   private BelowZeroRetrogen belowZeroRetrogen;
/*     */   private final ProtoChunkTicks<Block> blockTicks;
/*     */   private final ProtoChunkTicks<Fluid> fluidTicks;
/*     */   
/*     */   public ProtoChunk(ChunkPos paramChunkPos, UpgradeData paramUpgradeData, LevelHeightAccessor paramLevelHeightAccessor, PalettedContainerFactory paramPalettedContainerFactory, BlendingData paramBlendingData) {
/*  59 */     this(paramChunkPos, paramUpgradeData, (LevelChunkSection[])null, new ProtoChunkTicks(), new ProtoChunkTicks(), paramLevelHeightAccessor, paramPalettedContainerFactory, paramBlendingData);
/*     */   }
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
/*     */   public ProtoChunk(ChunkPos paramChunkPos, UpgradeData paramUpgradeData, LevelChunkSection[] paramArrayOfLevelChunkSection, ProtoChunkTicks<Block> paramProtoChunkTicks, ProtoChunkTicks<Fluid> paramProtoChunkTicks1, LevelHeightAccessor paramLevelHeightAccessor, PalettedContainerFactory paramPalettedContainerFactory, BlendingData paramBlendingData) {
/*  72 */     super(paramChunkPos, paramUpgradeData, paramLevelHeightAccessor, paramPalettedContainerFactory, 0L, paramArrayOfLevelChunkSection, paramBlendingData);
/*  73 */     this.blockTicks = paramProtoChunkTicks;
/*  74 */     this.fluidTicks = paramProtoChunkTicks1;
/*     */   }
/*     */ 
/*     */   
/*     */   public TickContainerAccess<Block> getBlockTicks() {
/*  79 */     return (TickContainerAccess<Block>)this.blockTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public TickContainerAccess<Fluid> getFluidTicks() {
/*  84 */     return (TickContainerAccess<Fluid>)this.fluidTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkAccess.PackedTicks getTicksForSerialization(long paramLong) {
/*  89 */     return new ChunkAccess.PackedTicks(this.blockTicks.pack(paramLong), this.fluidTicks.pack(paramLong));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getBlockState(BlockPos paramBlockPos) {
/*  94 */     int i = paramBlockPos.getY();
/*  95 */     if (isOutsideBuildHeight(i)) {
/*  96 */       return Blocks.VOID_AIR.defaultBlockState();
/*     */     }
/*     */     
/*  99 */     LevelChunkSection levelChunkSection = getSection(getSectionIndex(i));
/* 100 */     if (levelChunkSection.hasOnlyAir()) {
/* 101 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 104 */     return levelChunkSection.getBlockState(paramBlockPos.getX() & 0xF, i & 0xF, paramBlockPos.getZ() & 0xF);
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockPos paramBlockPos) {
/* 109 */     int i = paramBlockPos.getY();
/* 110 */     if (isOutsideBuildHeight(i)) {
/* 111 */       return Fluids.EMPTY.defaultFluidState();
/*     */     }
/*     */     
/* 114 */     LevelChunkSection levelChunkSection = getSection(getSectionIndex(i));
/* 115 */     if (levelChunkSection.hasOnlyAir()) {
/* 116 */       return Fluids.EMPTY.defaultFluidState();
/*     */     }
/*     */     
/* 119 */     return levelChunkSection.getFluidState(paramBlockPos.getX() & 0xF, i & 0xF, paramBlockPos.getZ() & 0xF);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState setBlockState(BlockPos paramBlockPos, BlockState paramBlockState, @UpdateFlags int paramInt) {
/* 124 */     int i = paramBlockPos.getX();
/* 125 */     int j = paramBlockPos.getY();
/* 126 */     int k = paramBlockPos.getZ();
/*     */     
/* 128 */     if (isOutsideBuildHeight(j)) {
/* 129 */       return Blocks.VOID_AIR.defaultBlockState();
/*     */     }
/*     */     
/* 132 */     int m = getSectionIndex(j);
/* 133 */     LevelChunkSection levelChunkSection = getSection(m);
/* 134 */     boolean bool = levelChunkSection.hasOnlyAir();
/* 135 */     if (bool && paramBlockState.is(Blocks.AIR)) {
/* 136 */       return paramBlockState;
/*     */     }
/*     */     
/* 139 */     int n = SectionPos.sectionRelative(i);
/* 140 */     int i1 = SectionPos.sectionRelative(j);
/* 141 */     int i2 = SectionPos.sectionRelative(k);
/* 142 */     BlockState blockState = levelChunkSection.setBlockState(n, i1, i2, paramBlockState);
/*     */     
/* 144 */     if (this.status.isOrAfter(ChunkStatus.INITIALIZE_LIGHT)) {
/* 145 */       boolean bool1 = levelChunkSection.hasOnlyAir();
/* 146 */       if (bool1 != bool) {
/* 147 */         this.lightEngine.updateSectionStatus(paramBlockPos, bool1);
/*     */       }
/*     */       
/* 150 */       if (LightEngine.hasDifferentLightProperties(blockState, paramBlockState)) {
/* 151 */         this.skyLightSources.update(this, n, j, i2);
/* 152 */         this.lightEngine.checkBlock(paramBlockPos);
/*     */       } 
/*     */     } 
/*     */     
/* 156 */     EnumSet enumSet = getPersistedStatus().heightmapsAfter();
/* 157 */     EnumSet<Heightmap.Types> enumSet1 = null;
/*     */     
/* 159 */     for (Heightmap.Types types : enumSet) {
/* 160 */       Heightmap heightmap = this.heightmaps.get(types);
/* 161 */       if (heightmap == null) {
/* 162 */         if (enumSet1 == null) {
/* 163 */           enumSet1 = EnumSet.noneOf(Heightmap.Types.class);
/*     */         }
/* 165 */         enumSet1.add(types);
/*     */       } 
/*     */     } 
/*     */     
/* 169 */     if (enumSet1 != null) {
/* 170 */       Heightmap.primeHeightmaps(this, enumSet1);
/*     */     }
/*     */     
/* 173 */     for (Heightmap.Types types : enumSet) {
/* 174 */       ((Heightmap)this.heightmaps.get(types)).update(n, j, i2, paramBlockState);
/*     */     }
/*     */     
/* 177 */     return blockState;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockEntity(BlockEntity paramBlockEntity) {
/* 182 */     this.pendingBlockEntities.remove(paramBlockEntity.getBlockPos());
/* 183 */     this.blockEntities.put(paramBlockEntity.getBlockPos(), paramBlockEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity getBlockEntity(BlockPos paramBlockPos) {
/* 188 */     return this.blockEntities.get(paramBlockPos);
/*     */   }
/*     */   
/*     */   public Map<BlockPos, BlockEntity> getBlockEntities() {
/* 192 */     return this.blockEntities;
/*     */   }
/*     */   
/*     */   public void addEntity(CompoundTag paramCompoundTag) {
/* 196 */     this.entities.add(paramCompoundTag);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEntity(Entity paramEntity) {
/* 201 */     if (paramEntity.isPassenger()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 207 */     ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(paramEntity.problemPath(), LOGGER); 
/* 208 */     try { TagValueOutput tagValueOutput = TagValueOutput.createWithContext((ProblemReporter)scopedCollector, (HolderLookup.Provider)paramEntity.registryAccess());
/* 209 */       paramEntity.save((ValueOutput)tagValueOutput);
/* 210 */       addEntity(tagValueOutput.buildResult());
/* 211 */       scopedCollector.close(); }
/*     */     catch (Throwable throwable) { try { scopedCollector.close(); }
/*     */       catch (Throwable throwable1)
/*     */       { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 216 */      } public void setStartForStructure(Structure paramStructure, StructureStart paramStructureStart) { BelowZeroRetrogen belowZeroRetrogen = getBelowZeroRetrogen();
/* 217 */     if (belowZeroRetrogen != null && paramStructureStart.isValid()) {
/* 218 */       BoundingBox boundingBox = paramStructureStart.getBoundingBox();
/* 219 */       LevelHeightAccessor levelHeightAccessor = getHeightAccessorForGeneration();
/* 220 */       if (boundingBox.minY() < levelHeightAccessor.getMinY() || boundingBox.maxY() > levelHeightAccessor.getMaxY()) {
/*     */         return;
/*     */       }
/*     */     } 
/* 224 */     super.setStartForStructure(paramStructure, paramStructureStart); }
/*     */ 
/*     */   
/*     */   public List<CompoundTag> getEntities() {
/* 228 */     return this.entities;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkStatus getPersistedStatus() {
/* 233 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setPersistedStatus(ChunkStatus paramChunkStatus) {
/* 237 */     this.status = paramChunkStatus;
/* 238 */     if (this.belowZeroRetrogen != null && paramChunkStatus.isOrAfter(this.belowZeroRetrogen.targetStatus())) {
/* 239 */       setBelowZeroRetrogen((BelowZeroRetrogen)null);
/*     */     }
/* 241 */     markUnsaved();
/*     */   }
/*     */ 
/*     */   
/*     */   public Holder<Biome> getNoiseBiome(int paramInt1, int paramInt2, int paramInt3) {
/* 246 */     if (getHighestGeneratedStatus().isOrAfter(ChunkStatus.BIOMES)) {
/* 247 */       return super.getNoiseBiome(paramInt1, paramInt2, paramInt3);
/*     */     }
/* 249 */     throw new IllegalStateException("Asking for biomes before we have biomes");
/*     */   }
/*     */   
/*     */   public static short packOffsetCoordinates(BlockPos paramBlockPos) {
/* 253 */     int i = paramBlockPos.getX();
/* 254 */     int j = paramBlockPos.getY();
/* 255 */     int k = paramBlockPos.getZ();
/* 256 */     int m = i & 0xF;
/* 257 */     int n = j & 0xF;
/* 258 */     int i1 = k & 0xF;
/* 259 */     return (short)(m | n << 4 | i1 << 8);
/*     */   }
/*     */   
/*     */   public static BlockPos unpackOffsetCoordinates(short paramShort, int paramInt, ChunkPos paramChunkPos) {
/* 263 */     int i = SectionPos.sectionToBlockCoord(paramChunkPos.x, paramShort & 0xF);
/* 264 */     int j = SectionPos.sectionToBlockCoord(paramInt, paramShort >>> 4 & 0xF);
/* 265 */     int k = SectionPos.sectionToBlockCoord(paramChunkPos.z, paramShort >>> 8 & 0xF);
/* 266 */     return new BlockPos(i, j, k);
/*     */   }
/*     */ 
/*     */   
/*     */   public void markPosForPostprocessing(BlockPos paramBlockPos) {
/* 271 */     if (!isOutsideBuildHeight(paramBlockPos)) {
/* 272 */       ChunkAccess.getOrCreateOffsetList(this.postProcessing, getSectionIndex(paramBlockPos.getY())).add(packOffsetCoordinates(paramBlockPos));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPackedPostProcess(ShortList paramShortList, int paramInt) {
/* 278 */     ChunkAccess.getOrCreateOffsetList(this.postProcessing, paramInt).addAll(paramShortList);
/*     */   }
/*     */   
/*     */   public Map<BlockPos, CompoundTag> getBlockEntityNbts() {
/* 282 */     return Collections.unmodifiableMap(this.pendingBlockEntities);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getBlockEntityNbtForSaving(BlockPos paramBlockPos, HolderLookup.Provider paramProvider) {
/* 287 */     BlockEntity blockEntity = getBlockEntity(paramBlockPos);
/* 288 */     if (blockEntity != null) {
/* 289 */       return blockEntity.saveWithFullMetadata(paramProvider);
/*     */     }
/* 291 */     return this.pendingBlockEntities.get(paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeBlockEntity(BlockPos paramBlockPos) {
/* 296 */     this.blockEntities.remove(paramBlockPos);
/* 297 */     this.pendingBlockEntities.remove(paramBlockPos);
/*     */   }
/*     */   
/*     */   public CarvingMask getCarvingMask() {
/* 301 */     return this.carvingMask;
/*     */   }
/*     */   
/*     */   public CarvingMask getOrCreateCarvingMask() {
/* 305 */     if (this.carvingMask == null) {
/* 306 */       this.carvingMask = new CarvingMask(getHeight(), getMinY());
/*     */     }
/* 308 */     return this.carvingMask;
/*     */   }
/*     */   
/*     */   public void setCarvingMask(CarvingMask paramCarvingMask) {
/* 312 */     this.carvingMask = paramCarvingMask;
/*     */   }
/*     */   
/*     */   public void setLightEngine(LevelLightEngine paramLevelLightEngine) {
/* 316 */     this.lightEngine = paramLevelLightEngine;
/*     */   }
/*     */   
/*     */   public void setBelowZeroRetrogen(BelowZeroRetrogen paramBelowZeroRetrogen) {
/* 320 */     this.belowZeroRetrogen = paramBelowZeroRetrogen;
/*     */   }
/*     */ 
/*     */   
/*     */   public BelowZeroRetrogen getBelowZeroRetrogen() {
/* 325 */     return this.belowZeroRetrogen;
/*     */   }
/*     */   
/*     */   private static <T> LevelChunkTicks<T> unpackTicks(ProtoChunkTicks<T> paramProtoChunkTicks) {
/* 329 */     return new LevelChunkTicks(paramProtoChunkTicks.scheduledTicks());
/*     */   }
/*     */   
/*     */   public LevelChunkTicks<Block> unpackBlockTicks() {
/* 333 */     return unpackTicks(this.blockTicks);
/*     */   }
/*     */   
/*     */   public LevelChunkTicks<Fluid> unpackFluidTicks() {
/* 337 */     return unpackTicks(this.fluidTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelHeightAccessor getHeightAccessorForGeneration() {
/* 342 */     if (isUpgrading()) {
/* 343 */       return BelowZeroRetrogen.UPGRADE_HEIGHT_ACCESSOR;
/*     */     }
/* 345 */     return (LevelHeightAccessor)this;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\ProtoChunk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */