/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.feline.Cat;
/*     */ import net.minecraft.world.entity.monster.Witch;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.StairBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.StairsShape;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ public class SwampHutPiece extends ScatteredFeaturePiece {
/*     */   private boolean spawnedWitch;
/*     */   
/*     */   public SwampHutPiece(RandomSource paramRandomSource, int paramInt1, int paramInt2) {
/*  30 */     super(StructurePieceType.SWAMPLAND_HUT, paramInt1, 64, paramInt2, 7, 7, 9, getRandomHorizontalDirection(paramRandomSource));
/*     */   }
/*     */   private boolean spawnedCat;
/*     */   public SwampHutPiece(CompoundTag paramCompoundTag) {
/*  34 */     super(StructurePieceType.SWAMPLAND_HUT, paramCompoundTag);
/*  35 */     this.spawnedWitch = paramCompoundTag.getBooleanOr("Witch", false);
/*  36 */     this.spawnedCat = paramCompoundTag.getBooleanOr("Cat", false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/*  41 */     super.addAdditionalSaveData(paramStructurePieceSerializationContext, paramCompoundTag);
/*  42 */     paramCompoundTag.putBoolean("Witch", this.spawnedWitch);
/*  43 */     paramCompoundTag.putBoolean("Cat", this.spawnedCat);
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/*  48 */     if (!updateAverageGroundHeight((LevelAccessor)paramWorldGenLevel, paramBoundingBox, 0)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  53 */     generateBox(paramWorldGenLevel, paramBoundingBox, 1, 1, 1, 5, 1, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*  54 */     generateBox(paramWorldGenLevel, paramBoundingBox, 1, 4, 2, 5, 4, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*  55 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 1, 0, 4, 1, 0, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*     */ 
/*     */     
/*  58 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 2, 2, 3, 3, 2, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*  59 */     generateBox(paramWorldGenLevel, paramBoundingBox, 1, 2, 3, 1, 3, 6, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*  60 */     generateBox(paramWorldGenLevel, paramBoundingBox, 5, 2, 3, 5, 3, 6, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*  61 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 2, 7, 4, 3, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*     */ 
/*     */     
/*  64 */     generateBox(paramWorldGenLevel, paramBoundingBox, 1, 0, 2, 1, 3, 2, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
/*  65 */     generateBox(paramWorldGenLevel, paramBoundingBox, 5, 0, 2, 5, 3, 2, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
/*  66 */     generateBox(paramWorldGenLevel, paramBoundingBox, 1, 0, 7, 1, 3, 7, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
/*  67 */     generateBox(paramWorldGenLevel, paramBoundingBox, 5, 0, 7, 5, 3, 7, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
/*     */ 
/*     */     
/*  70 */     placeBlock(paramWorldGenLevel, Blocks.OAK_FENCE.defaultBlockState(), 2, 3, 2, paramBoundingBox);
/*  71 */     placeBlock(paramWorldGenLevel, Blocks.OAK_FENCE.defaultBlockState(), 3, 3, 7, paramBoundingBox);
/*  72 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 1, 3, 4, paramBoundingBox);
/*  73 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 5, 3, 4, paramBoundingBox);
/*  74 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 5, 3, 5, paramBoundingBox);
/*  75 */     placeBlock(paramWorldGenLevel, Blocks.POTTED_RED_MUSHROOM.defaultBlockState(), 1, 3, 5, paramBoundingBox);
/*     */ 
/*     */     
/*  78 */     placeBlock(paramWorldGenLevel, Blocks.CRAFTING_TABLE.defaultBlockState(), 3, 2, 6, paramBoundingBox);
/*  79 */     placeBlock(paramWorldGenLevel, Blocks.CAULDRON.defaultBlockState(), 4, 2, 6, paramBoundingBox);
/*     */ 
/*     */     
/*  82 */     placeBlock(paramWorldGenLevel, Blocks.OAK_FENCE.defaultBlockState(), 1, 2, 1, paramBoundingBox);
/*  83 */     placeBlock(paramWorldGenLevel, Blocks.OAK_FENCE.defaultBlockState(), 5, 2, 1, paramBoundingBox);
/*     */ 
/*     */     
/*  86 */     BlockState blockState1 = (BlockState)Blocks.SPRUCE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.NORTH);
/*  87 */     BlockState blockState2 = (BlockState)Blocks.SPRUCE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.EAST);
/*  88 */     BlockState blockState3 = (BlockState)Blocks.SPRUCE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.WEST);
/*  89 */     BlockState blockState4 = (BlockState)Blocks.SPRUCE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.SOUTH);
/*     */     
/*  91 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 4, 1, 6, 4, 1, blockState1, blockState1, false);
/*  92 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 4, 2, 0, 4, 7, blockState2, blockState2, false);
/*  93 */     generateBox(paramWorldGenLevel, paramBoundingBox, 6, 4, 2, 6, 4, 7, blockState3, blockState3, false);
/*  94 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 4, 8, 6, 4, 8, blockState4, blockState4, false);
/*  95 */     placeBlock(paramWorldGenLevel, (BlockState)blockState1.setValue((Property)StairBlock.SHAPE, (Comparable)StairsShape.OUTER_RIGHT), 0, 4, 1, paramBoundingBox);
/*  96 */     placeBlock(paramWorldGenLevel, (BlockState)blockState1.setValue((Property)StairBlock.SHAPE, (Comparable)StairsShape.OUTER_LEFT), 6, 4, 1, paramBoundingBox);
/*  97 */     placeBlock(paramWorldGenLevel, (BlockState)blockState4.setValue((Property)StairBlock.SHAPE, (Comparable)StairsShape.OUTER_LEFT), 0, 4, 8, paramBoundingBox);
/*  98 */     placeBlock(paramWorldGenLevel, (BlockState)blockState4.setValue((Property)StairBlock.SHAPE, (Comparable)StairsShape.OUTER_RIGHT), 6, 4, 8, paramBoundingBox);
/*     */ 
/*     */     
/* 101 */     for (byte b = 2; b <= 7; b += 5) {
/* 102 */       for (byte b1 = 1; b1 <= 5; b1 += 4) {
/* 103 */         fillColumnDown(paramWorldGenLevel, Blocks.OAK_LOG.defaultBlockState(), b1, -1, b, paramBoundingBox);
/*     */       }
/*     */     } 
/*     */     
/* 107 */     if (!this.spawnedWitch) {
/* 108 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(2, 2, 5);
/* 109 */       if (paramBoundingBox.isInside((Vec3i)mutableBlockPos)) {
/* 110 */         this.spawnedWitch = true;
/*     */         
/* 112 */         Witch witch = (Witch)EntityType.WITCH.create((Level)paramWorldGenLevel.getLevel(), EntitySpawnReason.STRUCTURE);
/* 113 */         if (witch != null) {
/* 114 */           witch.setPersistenceRequired();
/* 115 */           witch.snapTo(mutableBlockPos.getX() + 0.5D, mutableBlockPos.getY(), mutableBlockPos.getZ() + 0.5D, 0.0F, 0.0F);
/* 116 */           witch.finalizeSpawn((ServerLevelAccessor)paramWorldGenLevel, paramWorldGenLevel.getCurrentDifficultyAt((BlockPos)mutableBlockPos), EntitySpawnReason.STRUCTURE, null);
/* 117 */           paramWorldGenLevel.addFreshEntityWithPassengers((Entity)witch);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 122 */     spawnCat((ServerLevelAccessor)paramWorldGenLevel, paramBoundingBox);
/*     */   }
/*     */   
/*     */   private void spawnCat(ServerLevelAccessor paramServerLevelAccessor, BoundingBox paramBoundingBox) {
/* 126 */     if (!this.spawnedCat) {
/* 127 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(2, 2, 5);
/* 128 */       if (paramBoundingBox.isInside((Vec3i)mutableBlockPos)) {
/* 129 */         this.spawnedCat = true;
/*     */         
/* 131 */         Cat cat = (Cat)EntityType.CAT.create((Level)paramServerLevelAccessor.getLevel(), EntitySpawnReason.STRUCTURE);
/* 132 */         if (cat != null) {
/* 133 */           cat.setPersistenceRequired();
/* 134 */           cat.snapTo(mutableBlockPos.getX() + 0.5D, mutableBlockPos.getY(), mutableBlockPos.getZ() + 0.5D, 0.0F, 0.0F);
/* 135 */           cat.finalizeSpawn(paramServerLevelAccessor, paramServerLevelAccessor.getCurrentDifficultyAt((BlockPos)mutableBlockPos), EntitySpawnReason.STRUCTURE, null);
/* 136 */           paramServerLevelAccessor.addFreshEntityWithPassengers((Entity)cat);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\SwampHutPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */