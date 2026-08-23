/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MineShaftCrossing
/*     */   extends MineshaftPieces.MineShaftPiece
/*     */ {
/*     */   private final Direction direction;
/*     */   private final boolean isTwoFloored;
/*     */   
/*     */   public MineShaftCrossing(CompoundTag paramCompoundTag) {
/* 694 */     super(StructurePieceType.MINE_SHAFT_CROSSING, paramCompoundTag);
/* 695 */     this.isTwoFloored = paramCompoundTag.getBooleanOr("tf", false);
/* 696 */     this.direction = paramCompoundTag.read("D", Direction.LEGACY_ID_CODEC_2D).orElse(Direction.SOUTH);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/* 701 */     super.addAdditionalSaveData(paramStructurePieceSerializationContext, paramCompoundTag);
/* 702 */     paramCompoundTag.putBoolean("tf", this.isTwoFloored);
/* 703 */     paramCompoundTag.store("D", Direction.LEGACY_ID_CODEC_2D, this.direction);
/*     */   }
/*     */   
/*     */   public MineShaftCrossing(int paramInt, BoundingBox paramBoundingBox, Direction paramDirection, MineshaftStructure.Type paramType) {
/* 707 */     super(StructurePieceType.MINE_SHAFT_CROSSING, paramInt, paramType, paramBoundingBox);
/*     */     
/* 709 */     this.direction = paramDirection;
/* 710 */     this.isTwoFloored = (paramBoundingBox.getYSpan() > 3);
/*     */   }
/*     */   public static BoundingBox findCrossing(StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, Direction paramDirection) {
/*     */     byte b;
/*     */     BoundingBox boundingBox;
/* 715 */     if (paramRandomSource.nextInt(4) == 0) {
/* 716 */       b = 6;
/*     */     } else {
/* 718 */       b = 2;
/*     */     } 
/*     */ 
/*     */     
/* 722 */     switch (MineshaftPieces.null.$SwitchMap$net$minecraft$core$Direction[paramDirection.ordinal()]) {
/*     */       
/*     */       default:
/* 725 */         boundingBox = new BoundingBox(-1, 0, -4, 3, b, 0);
/*     */         break;
/*     */       case 2:
/* 728 */         boundingBox = new BoundingBox(-1, 0, 0, 3, b, 4);
/*     */         break;
/*     */       case 3:
/* 731 */         boundingBox = new BoundingBox(-4, 0, -1, 0, b, 3);
/*     */         break;
/*     */       case 4:
/* 734 */         boundingBox = new BoundingBox(0, 0, -1, 4, b, 3);
/*     */         break;
/*     */     } 
/*     */     
/* 738 */     boundingBox.move(paramInt1, paramInt2, paramInt3);
/*     */     
/* 740 */     if (paramStructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/* 741 */       return null;
/*     */     }
/*     */     
/* 744 */     return boundingBox;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addChildren(StructurePiece paramStructurePiece, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource) {
/* 749 */     int i = getGenDepth();
/*     */ 
/*     */     
/* 752 */     switch (MineshaftPieces.null.$SwitchMap$net$minecraft$core$Direction[this.direction.ordinal()]) {
/*     */       
/*     */       default:
/* 755 */         MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i);
/* 756 */         MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, i);
/* 757 */         MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, i);
/*     */         break;
/*     */       case 2:
/* 760 */         MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
/* 761 */         MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, i);
/* 762 */         MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, i);
/*     */         break;
/*     */       case 3:
/* 765 */         MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i);
/* 766 */         MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
/* 767 */         MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, i);
/*     */         break;
/*     */       case 4:
/* 770 */         MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i);
/* 771 */         MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
/* 772 */         MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, i);
/*     */         break;
/*     */     } 
/*     */     
/* 776 */     if (this.isTwoFloored) {
/* 777 */       if (paramRandomSource.nextBoolean()) {
/* 778 */         MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3 + 1, this.boundingBox.minZ() - 1, Direction.NORTH, i);
/*     */       }
/* 780 */       if (paramRandomSource.nextBoolean()) {
/* 781 */         MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() - 1, this.boundingBox.minY() + 3 + 1, this.boundingBox.minZ() + 1, Direction.WEST, i);
/*     */       }
/* 783 */       if (paramRandomSource.nextBoolean()) {
/* 784 */         MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY() + 3 + 1, this.boundingBox.minZ() + 1, Direction.EAST, i);
/*     */       }
/* 786 */       if (paramRandomSource.nextBoolean()) {
/* 787 */         MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3 + 1, this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/* 794 */     if (isInInvalidLocation((LevelAccessor)paramWorldGenLevel, paramBoundingBox)) {
/*     */       return;
/*     */     }
/*     */     
/* 798 */     BlockState blockState = this.type.getPlanksState();
/*     */ 
/*     */     
/* 801 */     if (this.isTwoFloored) {
/* 802 */       generateBox(paramWorldGenLevel, paramBoundingBox, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.minY() + 3 - 1, this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 803 */       generateBox(paramWorldGenLevel, paramBoundingBox, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.minY() + 3 - 1, this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
/* 804 */       generateBox(paramWorldGenLevel, paramBoundingBox, this.boundingBox.minX() + 1, this.boundingBox.maxY() - 2, this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 805 */       generateBox(paramWorldGenLevel, paramBoundingBox, this.boundingBox.minX(), this.boundingBox.maxY() - 2, this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
/* 806 */       generateBox(paramWorldGenLevel, paramBoundingBox, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3, this.boundingBox.minZ() + 1, this.boundingBox.maxX() - 1, this.boundingBox.minY() + 3, this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
/*     */     } else {
/* 808 */       generateBox(paramWorldGenLevel, paramBoundingBox, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 809 */       generateBox(paramWorldGenLevel, paramBoundingBox, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
/*     */     } 
/*     */ 
/*     */     
/* 813 */     placeSupportPillar(paramWorldGenLevel, paramBoundingBox, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxY());
/* 814 */     placeSupportPillar(paramWorldGenLevel, paramBoundingBox, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 1, this.boundingBox.maxY());
/* 815 */     placeSupportPillar(paramWorldGenLevel, paramBoundingBox, this.boundingBox.maxX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxY());
/* 816 */     placeSupportPillar(paramWorldGenLevel, paramBoundingBox, this.boundingBox.maxX() - 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 1, this.boundingBox.maxY());
/*     */ 
/*     */ 
/*     */     
/* 820 */     int i = this.boundingBox.minY() - 1;
/* 821 */     for (int j = this.boundingBox.minX(); j <= this.boundingBox.maxX(); j++) {
/* 822 */       for (int k = this.boundingBox.minZ(); k <= this.boundingBox.maxZ(); k++) {
/* 823 */         setPlanksBlock(paramWorldGenLevel, paramBoundingBox, blockState, j, i, k);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeSupportPillar(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 829 */     if (!getBlock((BlockGetter)paramWorldGenLevel, paramInt1, paramInt4 + 1, paramInt3, paramBoundingBox).isAir())
/* 830 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt1, paramInt2, paramInt3, paramInt1, paramInt4, paramInt3, this.type.getPlanksState(), CAVE_AIR, false); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\MineshaftPieces$MineShaftCrossing.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */