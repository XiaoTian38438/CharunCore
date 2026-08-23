/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class SinglePieceStructure
/*    */   extends Structure
/*    */ {
/*    */   private final PieceConstructor constructor;
/*    */   private final int width;
/*    */   private final int depth;
/*    */   
/*    */   protected SinglePieceStructure(PieceConstructor paramPieceConstructor, int paramInt1, int paramInt2, Structure.StructureSettings paramStructureSettings) {
/* 21 */     super(paramStructureSettings);
/* 22 */     this.constructor = paramPieceConstructor;
/* 23 */     this.width = paramInt1;
/* 24 */     this.depth = paramInt2;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext paramGenerationContext) {
/* 29 */     if (getLowestY(paramGenerationContext, this.width, this.depth) < paramGenerationContext.chunkGenerator().getSeaLevel()) {
/* 30 */       return Optional.empty();
/*    */     }
/*    */     
/* 33 */     return onTopOfChunkCenter(paramGenerationContext, Heightmap.Types.WORLD_SURFACE_WG, paramStructurePiecesBuilder -> generatePieces(paramStructurePiecesBuilder, paramGenerationContext));
/*    */   }
/*    */   
/*    */   private void generatePieces(StructurePiecesBuilder paramStructurePiecesBuilder, Structure.GenerationContext paramGenerationContext) {
/* 37 */     ChunkPos chunkPos = paramGenerationContext.chunkPos();
/* 38 */     paramStructurePiecesBuilder.addPiece(this.constructor.construct(paramGenerationContext.random(), chunkPos.getMinBlockX(), chunkPos.getMinBlockZ()));
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   protected static interface PieceConstructor {
/*    */     StructurePiece construct(WorldgenRandom param1WorldgenRandom, int param1Int1, int param1Int2);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\SinglePieceStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */