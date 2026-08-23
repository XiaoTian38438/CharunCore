/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.SortedArraySet;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.BrushableBlockEntity;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.SinglePieceStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ 
/*    */ public class DesertPyramidStructure extends SinglePieceStructure {
/* 27 */   public static final MapCodec<DesertPyramidStructure> CODEC = simpleCodec(DesertPyramidStructure::new);
/*    */   
/*    */   public DesertPyramidStructure(Structure.StructureSettings paramStructureSettings) {
/* 30 */     super(DesertPyramidPiece::new, 21, 21, paramStructureSettings);
/*    */   }
/*    */ 
/*    */   
/*    */   public void afterPlace(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, PiecesContainer paramPiecesContainer) {
/* 35 */     SortedArraySet<BlockPos> sortedArraySet = SortedArraySet.create(Vec3i::compareTo);
/* 36 */     for (StructurePiece structurePiece : paramPiecesContainer.pieces()) {
/* 37 */       if (structurePiece instanceof DesertPyramidPiece) { DesertPyramidPiece desertPyramidPiece = (DesertPyramidPiece)structurePiece;
/* 38 */         sortedArraySet.addAll(desertPyramidPiece.getPotentialSuspiciousSandWorldPositions());
/*    */         
/* 40 */         placeSuspiciousSand(paramBoundingBox, paramWorldGenLevel, desertPyramidPiece.getRandomCollapsedRoofPos()); }
/*    */     
/*    */     } 
/*    */     
/* 44 */     ObjectArrayList objectArrayList = new ObjectArrayList(sortedArraySet.stream().toList());
/* 45 */     RandomSource randomSource = RandomSource.create(paramWorldGenLevel.getSeed()).forkPositional().at(paramPiecesContainer.calculateBoundingBox().getCenter());
/* 46 */     Util.shuffle((List)objectArrayList, randomSource);
/* 47 */     int i = Math.min(sortedArraySet.size(), randomSource.nextInt(5, 8));
/* 48 */     for (ObjectListIterator<BlockPos> objectListIterator = objectArrayList.iterator(); objectListIterator.hasNext(); ) { BlockPos blockPos = objectListIterator.next();
/* 49 */       if (i > 0) {
/* 50 */         i--;
/* 51 */         placeSuspiciousSand(paramBoundingBox, paramWorldGenLevel, blockPos); continue;
/* 52 */       }  if (paramBoundingBox.isInside((Vec3i)blockPos)) {
/* 53 */         paramWorldGenLevel.setBlock(blockPos, Blocks.SAND.defaultBlockState(), 2);
/*    */       } }
/*    */   
/*    */   }
/*    */   
/*    */   private static void placeSuspiciousSand(BoundingBox paramBoundingBox, WorldGenLevel paramWorldGenLevel, BlockPos paramBlockPos) {
/* 59 */     if (paramBoundingBox.isInside((Vec3i)paramBlockPos)) {
/* 60 */       paramWorldGenLevel.setBlock(paramBlockPos, Blocks.SUSPICIOUS_SAND.defaultBlockState(), 2);
/* 61 */       paramWorldGenLevel.getBlockEntity(paramBlockPos, BlockEntityType.BRUSHABLE_BLOCK).ifPresent(paramBrushableBlockEntity -> paramBrushableBlockEntity.setLootTable(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY, paramBlockPos.asLong()));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureType<?> type() {
/* 67 */     return StructureType.DESERT_PYRAMID;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\DesertPyramidStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */