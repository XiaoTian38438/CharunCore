/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ 
/*    */ public class BuriedTreasurePieces {
/*    */   public static class BuriedTreasurePiece extends StructurePiece {
/*    */     public BuriedTreasurePiece(BlockPos param1BlockPos) {
/* 24 */       super(StructurePieceType.BURIED_TREASURE_PIECE, 0, new BoundingBox(param1BlockPos));
/*    */     }
/*    */     
/*    */     public BuriedTreasurePiece(CompoundTag param1CompoundTag) {
/* 28 */       super(StructurePieceType.BURIED_TREASURE_PIECE, param1CompoundTag);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {}
/*    */ 
/*    */     
/*    */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 37 */       int i = param1WorldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, this.boundingBox.minX(), this.boundingBox.minZ());
/* 38 */       BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(this.boundingBox.minX(), i, this.boundingBox.minZ());
/*    */       
/* 40 */       while (mutableBlockPos.getY() > param1WorldGenLevel.getMinY()) {
/* 41 */         BlockState blockState1 = param1WorldGenLevel.getBlockState((BlockPos)mutableBlockPos);
/* 42 */         BlockState blockState2 = param1WorldGenLevel.getBlockState(mutableBlockPos.below());
/*    */         
/* 44 */         if (blockState2 == Blocks.SANDSTONE.defaultBlockState() || blockState2 == Blocks.STONE
/* 45 */           .defaultBlockState() || blockState2 == Blocks.ANDESITE
/* 46 */           .defaultBlockState() || blockState2 == Blocks.GRANITE
/* 47 */           .defaultBlockState() || blockState2 == Blocks.DIORITE
/* 48 */           .defaultBlockState()) {
/*    */           
/* 50 */           BlockState blockState = (blockState1.isAir() || isLiquid(blockState1)) ? Blocks.SAND.defaultBlockState() : blockState1;
/*    */           
/* 52 */           for (Direction direction : Direction.values()) {
/* 53 */             BlockPos blockPos = mutableBlockPos.relative(direction);
/* 54 */             BlockState blockState3 = param1WorldGenLevel.getBlockState(blockPos);
/*    */             
/* 56 */             if (blockState3.isAir() || isLiquid(blockState3)) {
/* 57 */               BlockPos blockPos1 = blockPos.below();
/* 58 */               BlockState blockState4 = param1WorldGenLevel.getBlockState(blockPos1);
/*    */               
/* 60 */               if ((blockState4.isAir() || isLiquid(blockState4)) && direction != Direction.UP) {
/* 61 */                 param1WorldGenLevel.setBlock(blockPos, blockState2, 3);
/*    */               } else {
/* 63 */                 param1WorldGenLevel.setBlock(blockPos, blockState, 3);
/*    */               } 
/*    */             } 
/*    */           } 
/* 67 */           this.boundingBox = new BoundingBox((BlockPos)mutableBlockPos);
/* 68 */           createChest((ServerLevelAccessor)param1WorldGenLevel, param1BoundingBox, param1RandomSource, (BlockPos)mutableBlockPos, BuiltInLootTables.BURIED_TREASURE, null);
/*    */           
/*    */           return;
/*    */         } 
/* 72 */         mutableBlockPos.move(0, -1, 0);
/*    */       } 
/*    */     }
/*    */     
/*    */     private boolean isLiquid(BlockState param1BlockState) {
/* 77 */       return (param1BlockState == Blocks.WATER.defaultBlockState() || param1BlockState == Blocks.LAVA
/* 78 */         .defaultBlockState());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\BuriedTreasurePieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */