/*     */ package net.minecraft.world.level;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.level.chunk.StructureAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureCheck;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
/*     */ 
/*     */ public class StructureManager {
/*     */   private final LevelAccessor level;
/*     */   private final WorldOptions worldOptions;
/*     */   private final StructureCheck structureCheck;
/*     */   
/*     */   public StructureManager(LevelAccessor paramLevelAccessor, WorldOptions paramWorldOptions, StructureCheck paramStructureCheck) {
/*  36 */     this.level = paramLevelAccessor;
/*  37 */     this.worldOptions = paramWorldOptions;
/*  38 */     this.structureCheck = paramStructureCheck;
/*     */   }
/*     */ 
/*     */   
/*     */   public StructureManager forWorldGenRegion(WorldGenRegion paramWorldGenRegion) {
/*  43 */     if (paramWorldGenRegion.getLevel() != this.level) {
/*  44 */       throw new IllegalStateException("Using invalid structure manager (source level: " + String.valueOf(paramWorldGenRegion.getLevel()) + ", region: " + String.valueOf(paramWorldGenRegion));
/*     */     }
/*  46 */     return new StructureManager((LevelAccessor)paramWorldGenRegion, this.worldOptions, this.structureCheck);
/*     */   }
/*     */   
/*     */   public List<StructureStart> startsForStructure(ChunkPos paramChunkPos, Predicate<Structure> paramPredicate) {
/*  50 */     Map map = this.level.getChunk(paramChunkPos.x, paramChunkPos.z, ChunkStatus.STRUCTURE_REFERENCES).getAllReferences();
/*  51 */     ImmutableList.Builder builder = ImmutableList.builder();
/*     */     
/*  53 */     for (Map.Entry entry : map.entrySet()) {
/*  54 */       Structure structure = (Structure)entry.getKey();
/*  55 */       if (paramPredicate.test(structure)) {
/*  56 */         Objects.requireNonNull(builder); fillStartsForStructure(structure, (LongSet)entry.getValue(), builder::add);
/*     */       } 
/*     */     } 
/*     */     
/*  60 */     return (List<StructureStart>)builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<StructureStart> startsForStructure(SectionPos paramSectionPos, Structure paramStructure) {
/*  68 */     LongSet longSet = this.level.getChunk(paramSectionPos.x(), paramSectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES).getReferencesForStructure(paramStructure);
/*  69 */     ImmutableList.Builder builder = ImmutableList.builder();
/*  70 */     Objects.requireNonNull(builder); fillStartsForStructure(paramStructure, longSet, builder::add);
/*  71 */     return (List<StructureStart>)builder.build();
/*     */   }
/*     */   
/*     */   public void fillStartsForStructure(Structure paramStructure, LongSet paramLongSet, Consumer<StructureStart> paramConsumer) {
/*  75 */     for (LongIterator<Long> longIterator = paramLongSet.iterator(); longIterator.hasNext(); ) { long l = ((Long)longIterator.next()).longValue();
/*  76 */       SectionPos sectionPos = SectionPos.of(new ChunkPos(l), this.level.getMinSectionY());
/*  77 */       StructureStart structureStart = getStartForStructure(sectionPos, paramStructure, (StructureAccess)this.level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_STARTS));
/*  78 */       if (structureStart != null && structureStart.isValid()) {
/*  79 */         paramConsumer.accept(structureStart);
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   public StructureStart getStartForStructure(SectionPos paramSectionPos, Structure paramStructure, StructureAccess paramStructureAccess) {
/*  85 */     return paramStructureAccess.getStartForStructure(paramStructure);
/*     */   }
/*     */   
/*     */   public void setStartForStructure(SectionPos paramSectionPos, Structure paramStructure, StructureStart paramStructureStart, StructureAccess paramStructureAccess) {
/*  89 */     paramStructureAccess.setStartForStructure(paramStructure, paramStructureStart);
/*     */   }
/*     */   
/*     */   public void addReferenceForStructure(SectionPos paramSectionPos, Structure paramStructure, long paramLong, StructureAccess paramStructureAccess) {
/*  93 */     paramStructureAccess.addReferenceForStructure(paramStructure, paramLong);
/*     */   }
/*     */   
/*     */   public boolean shouldGenerateStructures() {
/*  97 */     return this.worldOptions.generateStructures();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructureStart getStructureAt(BlockPos paramBlockPos, Structure paramStructure) {
/* 104 */     for (StructureStart structureStart : startsForStructure(SectionPos.of(paramBlockPos), paramStructure)) {
/* 105 */       if (structureStart.getBoundingBox().isInside((Vec3i)paramBlockPos)) {
/* 106 */         return structureStart;
/*     */       }
/*     */     } 
/* 109 */     return StructureStart.INVALID_START;
/*     */   }
/*     */   
/*     */   public StructureStart getStructureWithPieceAt(BlockPos paramBlockPos, TagKey<Structure> paramTagKey) {
/* 113 */     return getStructureWithPieceAt(paramBlockPos, paramHolder -> paramHolder.is(paramTagKey));
/*     */   }
/*     */   
/*     */   public StructureStart getStructureWithPieceAt(BlockPos paramBlockPos, HolderSet<Structure> paramHolderSet) {
/* 117 */     Objects.requireNonNull(paramHolderSet); return getStructureWithPieceAt(paramBlockPos, paramHolderSet::contains);
/*     */   }
/*     */ 
/*     */   
/*     */   public StructureStart getStructureWithPieceAt(BlockPos paramBlockPos, Predicate<Holder<Structure>> paramPredicate) {
/* 122 */     Registry registry = registryAccess().lookupOrThrow(Registries.STRUCTURE);
/* 123 */     for (StructureStart structureStart : startsForStructure(new ChunkPos(paramBlockPos), paramStructure -> { Objects.requireNonNull(paramPredicate); return ((Boolean)paramRegistry.get(paramRegistry.getId(paramStructure)).map(paramPredicate::test).orElse(Boolean.valueOf(false))).booleanValue();
/* 124 */         })) { if (structureHasPieceAt(paramBlockPos, structureStart)) {
/* 125 */         return structureStart;
/*     */       } }
/*     */     
/* 128 */     return StructureStart.INVALID_START;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructureStart getStructureWithPieceAt(BlockPos paramBlockPos, Structure paramStructure) {
/* 135 */     for (StructureStart structureStart : startsForStructure(SectionPos.of(paramBlockPos), paramStructure)) {
/* 136 */       if (structureHasPieceAt(paramBlockPos, structureStart)) {
/* 137 */         return structureStart;
/*     */       }
/*     */     } 
/* 140 */     return StructureStart.INVALID_START;
/*     */   }
/*     */   
/*     */   public boolean structureHasPieceAt(BlockPos paramBlockPos, StructureStart paramStructureStart) {
/* 144 */     for (StructurePiece structurePiece : paramStructureStart.getPieces()) {
/* 145 */       if (structurePiece.getBoundingBox().isInside((Vec3i)paramBlockPos)) {
/* 146 */         return true;
/*     */       }
/*     */     } 
/* 149 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasAnyStructureAt(BlockPos paramBlockPos) {
/* 153 */     SectionPos sectionPos = SectionPos.of(paramBlockPos);
/* 154 */     return this.level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES).hasAnyStructureReferences();
/*     */   }
/*     */   
/*     */   public Map<Structure, LongSet> getAllStructuresAt(BlockPos paramBlockPos) {
/* 158 */     SectionPos sectionPos = SectionPos.of(paramBlockPos);
/* 159 */     return this.level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES).getAllReferences();
/*     */   }
/*     */   
/*     */   public StructureCheckResult checkStructurePresence(ChunkPos paramChunkPos, Structure paramStructure, StructurePlacement paramStructurePlacement, boolean paramBoolean) {
/* 163 */     return this.structureCheck.checkStart(paramChunkPos, paramStructure, paramStructurePlacement, paramBoolean);
/*     */   }
/*     */   
/*     */   public void addReference(StructureStart paramStructureStart) {
/* 167 */     paramStructureStart.addReference();
/* 168 */     this.structureCheck.incrementReference(paramStructureStart.getChunkPos(), paramStructureStart.getStructure());
/*     */   }
/*     */   
/*     */   public RegistryAccess registryAccess() {
/* 172 */     return this.level.registryAccess();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\StructureManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */