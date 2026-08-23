/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.LinkedList;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class WoodlandMansionStructure extends Structure {
/* 24 */   public static final MapCodec<WoodlandMansionStructure> CODEC = simpleCodec(WoodlandMansionStructure::new);
/*    */   
/*    */   public WoodlandMansionStructure(Structure.StructureSettings paramStructureSettings) {
/* 27 */     super(paramStructureSettings);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext paramGenerationContext) {
/* 32 */     Rotation rotation = Rotation.getRandom((RandomSource)paramGenerationContext.random());
/*    */ 
/*    */     
/* 35 */     BlockPos blockPos = getLowestYIn5by5BoxOffset7Blocks(paramGenerationContext, rotation);
/*    */ 
/*    */     
/* 38 */     if (blockPos.getY() < 60) {
/* 39 */       return Optional.empty();
/*    */     }
/*    */     
/* 42 */     return Optional.of(new Structure.GenerationStub(blockPos, paramStructurePiecesBuilder -> generatePieces(paramStructurePiecesBuilder, paramGenerationContext, paramBlockPos, paramRotation)));
/*    */   }
/*    */   
/*    */   private void generatePieces(StructurePiecesBuilder paramStructurePiecesBuilder, Structure.GenerationContext paramGenerationContext, BlockPos paramBlockPos, Rotation paramRotation) {
/* 46 */     LinkedList<WoodlandMansionPieces.WoodlandMansionPiece> linkedList = Lists.newLinkedList();
/* 47 */     WoodlandMansionPieces.generateMansion(paramGenerationContext.structureTemplateManager(), paramBlockPos, paramRotation, linkedList, (RandomSource)paramGenerationContext.random());
/* 48 */     Objects.requireNonNull(paramStructurePiecesBuilder); linkedList.forEach(paramStructurePiecesBuilder::addPiece);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterPlace(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, PiecesContainer paramPiecesContainer) {
/* 54 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 55 */     int i = paramWorldGenLevel.getMinY();
/* 56 */     BoundingBox boundingBox = paramPiecesContainer.calculateBoundingBox();
/*    */     
/* 58 */     int j = boundingBox.minY();
/* 59 */     for (int k = paramBoundingBox.minX(); k <= paramBoundingBox.maxX(); k++) {
/* 60 */       for (int m = paramBoundingBox.minZ(); m <= paramBoundingBox.maxZ(); m++) {
/* 61 */         mutableBlockPos.set(k, j, m);
/*    */         
/* 63 */         if (!paramWorldGenLevel.isEmptyBlock((BlockPos)mutableBlockPos) && boundingBox.isInside((Vec3i)mutableBlockPos) && paramPiecesContainer.isInsidePiece((BlockPos)mutableBlockPos)) {
/* 64 */           for (int n = j - 1; n > i; ) {
/* 65 */             mutableBlockPos.setY(n);
/* 66 */             if (paramWorldGenLevel.isEmptyBlock((BlockPos)mutableBlockPos) || paramWorldGenLevel.getBlockState((BlockPos)mutableBlockPos).liquid()) {
/* 67 */               paramWorldGenLevel.setBlock((BlockPos)mutableBlockPos, Blocks.COBBLESTONE.defaultBlockState(), 2);
/*    */               n--;
/*    */             } 
/*    */           } 
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public StructureType<?> type() {
/* 79 */     return StructureType.WOODLAND_MANSION;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */