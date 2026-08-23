/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.tags.BiomeTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.RandomSupport;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class OceanMonumentStructure extends Structure {
/* 25 */   public static final MapCodec<OceanMonumentStructure> CODEC = simpleCodec(OceanMonumentStructure::new);
/*    */   
/*    */   public OceanMonumentStructure(Structure.StructureSettings paramStructureSettings) {
/* 28 */     super(paramStructureSettings);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext paramGenerationContext) {
/* 35 */     int i = paramGenerationContext.chunkPos().getBlockX(9);
/* 36 */     int j = paramGenerationContext.chunkPos().getBlockZ(9);
/*    */     
/* 38 */     Set set = paramGenerationContext.biomeSource().getBiomesWithin(i, paramGenerationContext.chunkGenerator().getSeaLevel(), j, 29, paramGenerationContext.randomState().sampler());
/* 39 */     for (Holder holder : set) {
/* 40 */       if (!holder.is(BiomeTags.REQUIRED_OCEAN_MONUMENT_SURROUNDING)) {
/* 41 */         return Optional.empty();
/*    */       }
/*    */     } 
/*    */     
/* 45 */     return onTopOfChunkCenter(paramGenerationContext, Heightmap.Types.OCEAN_FLOOR_WG, paramStructurePiecesBuilder -> generatePieces(paramStructurePiecesBuilder, paramGenerationContext));
/*    */   }
/*    */   
/*    */   private static StructurePiece createTopPiece(ChunkPos paramChunkPos, WorldgenRandom paramWorldgenRandom) {
/* 49 */     int i = paramChunkPos.getMinBlockX() - 29;
/* 50 */     int j = paramChunkPos.getMinBlockZ() - 29;
/* 51 */     Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection((RandomSource)paramWorldgenRandom);
/* 52 */     return new OceanMonumentPieces.MonumentBuilding((RandomSource)paramWorldgenRandom, i, j, direction);
/*    */   }
/*    */   
/*    */   private static void generatePieces(StructurePiecesBuilder paramStructurePiecesBuilder, Structure.GenerationContext paramGenerationContext) {
/* 56 */     paramStructurePiecesBuilder.addPiece(createTopPiece(paramGenerationContext.chunkPos(), paramGenerationContext.random()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static PiecesContainer regeneratePiecesAfterLoad(ChunkPos paramChunkPos, long paramLong, PiecesContainer paramPiecesContainer) {
/* 62 */     if (paramPiecesContainer.isEmpty()) {
/* 63 */       return paramPiecesContainer;
/*    */     }
/* 65 */     WorldgenRandom worldgenRandom = new WorldgenRandom((RandomSource)new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
/* 66 */     worldgenRandom.setLargeFeatureSeed(paramLong, paramChunkPos.x, paramChunkPos.z);
/*    */     
/* 68 */     StructurePiece structurePiece = paramPiecesContainer.pieces().get(0);
/* 69 */     BoundingBox boundingBox = structurePiece.getBoundingBox();
/*    */ 
/*    */     
/* 72 */     int i = boundingBox.minX();
/* 73 */     int j = boundingBox.minZ();
/* 74 */     Direction direction1 = Direction.Plane.HORIZONTAL.getRandomDirection((RandomSource)worldgenRandom);
/* 75 */     Direction direction2 = Objects.<Direction>requireNonNullElse(structurePiece.getOrientation(), direction1);
/*    */     
/* 77 */     OceanMonumentPieces.MonumentBuilding monumentBuilding = new OceanMonumentPieces.MonumentBuilding((RandomSource)worldgenRandom, i, j, direction2);
/* 78 */     StructurePiecesBuilder structurePiecesBuilder = new StructurePiecesBuilder();
/* 79 */     structurePiecesBuilder.addPiece(monumentBuilding);
/* 80 */     return structurePiecesBuilder.build();
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureType<?> type() {
/* 85 */     return StructureType.OCEAN_MONUMENT;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */