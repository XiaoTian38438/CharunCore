/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentStructure;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StructureStart
/*     */ {
/*     */   public static final String INVALID_START_ID = "INVALID";
/*  26 */   public static final StructureStart INVALID_START = new StructureStart(null, new ChunkPos(0, 0), 0, new PiecesContainer(List.of()));
/*     */   
/*  28 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Structure structure;
/*     */   
/*     */   private final PiecesContainer pieceContainer;
/*     */   
/*     */   private final ChunkPos chunkPos;
/*     */   private int references;
/*     */   private volatile BoundingBox cachedBoundingBox;
/*     */   
/*     */   public StructureStart(Structure paramStructure, ChunkPos paramChunkPos, int paramInt, PiecesContainer paramPiecesContainer) {
/*  39 */     this.structure = paramStructure;
/*  40 */     this.chunkPos = paramChunkPos;
/*  41 */     this.references = paramInt;
/*  42 */     this.pieceContainer = paramPiecesContainer;
/*     */   }
/*     */   
/*     */   public static StructureStart loadStaticStart(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag, long paramLong) {
/*  46 */     String str = paramCompoundTag.getStringOr("id", "");
/*  47 */     if ("INVALID".equals(str)) {
/*  48 */       return INVALID_START;
/*     */     }
/*     */ 
/*     */     
/*  52 */     Registry registry = paramStructurePieceSerializationContext.registryAccess().lookupOrThrow(Registries.STRUCTURE);
/*  53 */     Structure structure = (Structure)registry.getValue(Identifier.parse(str));
/*  54 */     if (structure == null) {
/*  55 */       LOGGER.error("Unknown stucture id: {}", str);
/*  56 */       return null;
/*     */     } 
/*     */     
/*  59 */     ChunkPos chunkPos = new ChunkPos(paramCompoundTag.getIntOr("ChunkX", 0), paramCompoundTag.getIntOr("ChunkZ", 0));
/*  60 */     int i = paramCompoundTag.getIntOr("references", 0);
/*  61 */     ListTag listTag = paramCompoundTag.getListOrEmpty("Children");
/*     */     
/*     */     try {
/*  64 */       PiecesContainer piecesContainer = PiecesContainer.load(listTag, paramStructurePieceSerializationContext);
/*  65 */       if (structure instanceof OceanMonumentStructure)
/*     */       {
/*  67 */         piecesContainer = OceanMonumentStructure.regeneratePiecesAfterLoad(chunkPos, paramLong, piecesContainer);
/*     */       }
/*  69 */       return new StructureStart(structure, chunkPos, i, piecesContainer);
/*  70 */     } catch (Exception exception) {
/*  71 */       LOGGER.error("Failed Start with id {}", str, exception);
/*  72 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public BoundingBox getBoundingBox() {
/*  77 */     BoundingBox boundingBox = this.cachedBoundingBox;
/*  78 */     if (boundingBox == null) {
/*  79 */       boundingBox = this.structure.adjustBoundingBox(this.pieceContainer.calculateBoundingBox());
/*  80 */       this.cachedBoundingBox = boundingBox;
/*     */     } 
/*  82 */     return boundingBox;
/*     */   }
/*     */   
/*     */   public void placeInChunk(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos) {
/*  86 */     List list = this.pieceContainer.pieces();
/*  87 */     if (list.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  92 */     BoundingBox boundingBox = ((StructurePiece)list.get(0)).boundingBox;
/*  93 */     BlockPos blockPos1 = boundingBox.getCenter();
/*  94 */     BlockPos blockPos2 = new BlockPos(blockPos1.getX(), boundingBox.minY(), blockPos1.getZ());
/*  95 */     for (StructurePiece structurePiece : list) {
/*  96 */       if (structurePiece.getBoundingBox().intersects(paramBoundingBox)) {
/*  97 */         structurePiece.postProcess(paramWorldGenLevel, paramStructureManager, paramChunkGenerator, paramRandomSource, paramBoundingBox, paramChunkPos, blockPos2);
/*     */       }
/*     */     } 
/*     */     
/* 101 */     this.structure.afterPlace(paramWorldGenLevel, paramStructureManager, paramChunkGenerator, paramRandomSource, paramBoundingBox, paramChunkPos, this.pieceContainer);
/*     */   }
/*     */   
/*     */   public CompoundTag createTag(StructurePieceSerializationContext paramStructurePieceSerializationContext, ChunkPos paramChunkPos) {
/* 105 */     CompoundTag compoundTag = new CompoundTag();
/*     */     
/* 107 */     if (isValid()) {
/* 108 */       compoundTag.putString("id", paramStructurePieceSerializationContext.registryAccess().lookupOrThrow(Registries.STRUCTURE).getKey(this.structure).toString());
/*     */     } else {
/* 110 */       compoundTag.putString("id", "INVALID");
/* 111 */       return compoundTag;
/*     */     } 
/* 113 */     compoundTag.putInt("ChunkX", paramChunkPos.x);
/* 114 */     compoundTag.putInt("ChunkZ", paramChunkPos.z);
/* 115 */     compoundTag.putInt("references", this.references);
/* 116 */     compoundTag.put("Children", this.pieceContainer.save(paramStructurePieceSerializationContext));
/*     */     
/* 118 */     return compoundTag;
/*     */   }
/*     */   
/*     */   public boolean isValid() {
/* 122 */     return !this.pieceContainer.isEmpty();
/*     */   }
/*     */   
/*     */   public ChunkPos getChunkPos() {
/* 126 */     return this.chunkPos;
/*     */   }
/*     */   
/*     */   public boolean canBeReferenced() {
/* 130 */     return (this.references < getMaxReferences());
/*     */   }
/*     */   
/*     */   public void addReference() {
/* 134 */     this.references++;
/*     */   }
/*     */   
/*     */   public int getReferences() {
/* 138 */     return this.references;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getMaxReferences() {
/* 143 */     return 1;
/*     */   }
/*     */   
/*     */   public Structure getStructure() {
/* 147 */     return this.structure;
/*     */   }
/*     */   
/*     */   public List<StructurePiece> getPieces() {
/* 151 */     return this.pieceContainer.pieces();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\StructureStart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */