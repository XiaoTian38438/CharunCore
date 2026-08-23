/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class StrongholdStructure extends Structure {
/* 13 */   public static final MapCodec<StrongholdStructure> CODEC = simpleCodec(StrongholdStructure::new);
/*    */   
/*    */   public StrongholdStructure(Structure.StructureSettings paramStructureSettings) {
/* 16 */     super(paramStructureSettings);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext paramGenerationContext) {
/* 21 */     return Optional.of(new Structure.GenerationStub(paramGenerationContext.chunkPos().getWorldPosition(), paramStructurePiecesBuilder -> generatePieces(paramStructurePiecesBuilder, paramGenerationContext)));
/*    */   }
/*    */   private static void generatePieces(StructurePiecesBuilder paramStructurePiecesBuilder, Structure.GenerationContext paramGenerationContext) {
/*    */     StrongholdPieces.StartPiece startPiece;
/* 25 */     byte b = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     do {
/* 31 */       paramStructurePiecesBuilder.clear();
/* 32 */       paramGenerationContext.random().setLargeFeatureSeed(paramGenerationContext.seed() + b++, (paramGenerationContext.chunkPos()).x, (paramGenerationContext.chunkPos()).z);
/* 33 */       StrongholdPieces.resetPieces();
/*    */       
/* 35 */       startPiece = new StrongholdPieces.StartPiece((RandomSource)paramGenerationContext.random(), paramGenerationContext.chunkPos().getBlockX(2), paramGenerationContext.chunkPos().getBlockZ(2));
/* 36 */       paramStructurePiecesBuilder.addPiece(startPiece);
/* 37 */       startPiece.addChildren(startPiece, (StructurePieceAccessor)paramStructurePiecesBuilder, (RandomSource)paramGenerationContext.random());
/*    */       
/* 39 */       List<StructurePiece> list = startPiece.pendingChildren;
/* 40 */       while (!list.isEmpty()) {
/* 41 */         int i = paramGenerationContext.random().nextInt(list.size());
/* 42 */         StructurePiece structurePiece = list.remove(i);
/* 43 */         structurePiece.addChildren(startPiece, (StructurePieceAccessor)paramStructurePiecesBuilder, (RandomSource)paramGenerationContext.random());
/*    */       } 
/*    */       
/* 46 */       paramStructurePiecesBuilder.moveBelowSeaLevel(paramGenerationContext.chunkGenerator().getSeaLevel(), paramGenerationContext.chunkGenerator().getMinY(), (RandomSource)paramGenerationContext.random(), 10);
/* 47 */     } while (paramStructurePiecesBuilder.isEmpty() || startPiece.portalRoomPiece == null);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureType<?> type() {
/* 52 */     return StructureType.STRONGHOLD;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */