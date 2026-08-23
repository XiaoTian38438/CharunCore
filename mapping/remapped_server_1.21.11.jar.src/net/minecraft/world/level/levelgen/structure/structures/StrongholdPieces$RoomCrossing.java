/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LadderBlock;
/*     */ import net.minecraft.world.level.block.WallTorchBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RoomCrossing
/*     */   extends StrongholdPieces.StrongholdPiece
/*     */ {
/*     */   protected static final int WIDTH = 11;
/*     */   protected static final int HEIGHT = 7;
/*     */   protected static final int DEPTH = 11;
/*     */   protected final int type;
/*     */   
/*     */   public RoomCrossing(int paramInt, RandomSource paramRandomSource, BoundingBox paramBoundingBox, Direction paramDirection) {
/* 865 */     super(StructurePieceType.STRONGHOLD_ROOM_CROSSING, paramInt, paramBoundingBox);
/*     */     
/* 867 */     setOrientation(paramDirection);
/* 868 */     this.entryDoor = randomSmallDoor(paramRandomSource);
/* 869 */     this.type = paramRandomSource.nextInt(5);
/*     */   }
/*     */   
/*     */   public RoomCrossing(CompoundTag paramCompoundTag) {
/* 873 */     super(StructurePieceType.STRONGHOLD_ROOM_CROSSING, paramCompoundTag);
/* 874 */     this.type = paramCompoundTag.getIntOr("Type", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/* 879 */     super.addAdditionalSaveData(paramStructurePieceSerializationContext, paramCompoundTag);
/* 880 */     paramCompoundTag.putInt("Type", this.type);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addChildren(StructurePiece paramStructurePiece, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource) {
/* 885 */     generateSmallDoorChildForward((StrongholdPieces.StartPiece)paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, 4, 1);
/* 886 */     generateSmallDoorChildLeft((StrongholdPieces.StartPiece)paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, 1, 4);
/* 887 */     generateSmallDoorChildRight((StrongholdPieces.StartPiece)paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, 1, 4);
/*     */   }
/*     */   
/*     */   public static RoomCrossing createPiece(StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, Direction paramDirection, int paramInt4) {
/* 891 */     BoundingBox boundingBox = BoundingBox.orientBox(paramInt1, paramInt2, paramInt3, -4, -1, 0, 11, 7, 11, paramDirection);
/*     */     
/* 893 */     if (!isOkBox(boundingBox) || paramStructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/* 894 */       return null;
/*     */     }
/*     */     
/* 897 */     return new RoomCrossing(paramInt4, paramRandomSource, boundingBox, paramDirection);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/* 903 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 0, 0, 10, 6, 10, true, paramRandomSource, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*     */     
/* 905 */     generateSmallDoor(paramWorldGenLevel, paramRandomSource, paramBoundingBox, this.entryDoor, 4, 1, 0);
/*     */     
/* 907 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 1, 10, 6, 3, 10, CAVE_AIR, CAVE_AIR, false);
/* 908 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 1, 4, 0, 3, 6, CAVE_AIR, CAVE_AIR, false);
/* 909 */     generateBox(paramWorldGenLevel, paramBoundingBox, 10, 1, 4, 10, 3, 6, CAVE_AIR, CAVE_AIR, false);
/*     */     
/* 911 */     switch (this.type) {
/*     */       default:
/*     */         return;
/*     */       
/*     */       case 0:
/* 916 */         placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, paramBoundingBox);
/* 917 */         placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 5, 2, 5, paramBoundingBox);
/* 918 */         placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 5, 3, 5, paramBoundingBox);
/* 919 */         placeBlock(paramWorldGenLevel, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.WEST), 4, 3, 5, paramBoundingBox);
/* 920 */         placeBlock(paramWorldGenLevel, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.EAST), 6, 3, 5, paramBoundingBox);
/* 921 */         placeBlock(paramWorldGenLevel, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.SOUTH), 5, 3, 4, paramBoundingBox);
/* 922 */         placeBlock(paramWorldGenLevel, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.NORTH), 5, 3, 6, paramBoundingBox);
/* 923 */         placeBlock(paramWorldGenLevel, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 4, paramBoundingBox);
/* 924 */         placeBlock(paramWorldGenLevel, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 5, paramBoundingBox);
/* 925 */         placeBlock(paramWorldGenLevel, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 6, paramBoundingBox);
/* 926 */         placeBlock(paramWorldGenLevel, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 4, paramBoundingBox);
/* 927 */         placeBlock(paramWorldGenLevel, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 5, paramBoundingBox);
/* 928 */         placeBlock(paramWorldGenLevel, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 6, paramBoundingBox);
/* 929 */         placeBlock(paramWorldGenLevel, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 5, 1, 4, paramBoundingBox);
/* 930 */         placeBlock(paramWorldGenLevel, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 5, 1, 6, paramBoundingBox);
/*     */       
/*     */       case 1:
/* 933 */         for (b = 0; b < 5; b++) {
/* 934 */           placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 3, 1, 3 + b, paramBoundingBox);
/* 935 */           placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 7, 1, 3 + b, paramBoundingBox);
/* 936 */           placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 3 + b, 1, 3, paramBoundingBox);
/* 937 */           placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 3 + b, 1, 7, paramBoundingBox);
/*     */         } 
/* 939 */         placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, paramBoundingBox);
/* 940 */         placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 5, 2, 5, paramBoundingBox);
/* 941 */         placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 5, 3, 5, paramBoundingBox);
/* 942 */         placeBlock(paramWorldGenLevel, Blocks.WATER.defaultBlockState(), 5, 4, 5, paramBoundingBox);
/*     */       case 2:
/*     */         break;
/* 945 */     }  byte b; for (b = 1; b <= 9; b++) {
/* 946 */       placeBlock(paramWorldGenLevel, Blocks.COBBLESTONE.defaultBlockState(), 1, 3, b, paramBoundingBox);
/* 947 */       placeBlock(paramWorldGenLevel, Blocks.COBBLESTONE.defaultBlockState(), 9, 3, b, paramBoundingBox);
/*     */     } 
/* 949 */     for (b = 1; b <= 9; b++) {
/* 950 */       placeBlock(paramWorldGenLevel, Blocks.COBBLESTONE.defaultBlockState(), b, 3, 1, paramBoundingBox);
/* 951 */       placeBlock(paramWorldGenLevel, Blocks.COBBLESTONE.defaultBlockState(), b, 3, 9, paramBoundingBox);
/*     */     } 
/* 953 */     placeBlock(paramWorldGenLevel, Blocks.COBBLESTONE.defaultBlockState(), 5, 1, 4, paramBoundingBox);
/* 954 */     placeBlock(paramWorldGenLevel, Blocks.COBBLESTONE.defaultBlockState(), 5, 1, 6, paramBoundingBox);
/* 955 */     placeBlock(paramWorldGenLevel, Blocks.COBBLESTONE.defaultBlockState(), 5, 3, 4, paramBoundingBox);
/* 956 */     placeBlock(paramWorldGenLevel, Blocks.COBBLESTONE.defaultBlockState(), 5, 3, 6, paramBoundingBox);
/* 957 */     placeBlock(paramWorldGenLevel, Blocks.COBBLESTONE.defaultBlockState(), 4, 1, 5, paramBoundingBox);
/* 958 */     placeBlock(paramWorldGenLevel, Blocks.COBBLESTONE.defaultBlockState(), 6, 1, 5, paramBoundingBox);
/* 959 */     placeBlock(paramWorldGenLevel, Blocks.COBBLESTONE.defaultBlockState(), 4, 3, 5, paramBoundingBox);
/* 960 */     placeBlock(paramWorldGenLevel, Blocks.COBBLESTONE.defaultBlockState(), 6, 3, 5, paramBoundingBox);
/* 961 */     for (b = 1; b <= 3; b++) {
/* 962 */       placeBlock(paramWorldGenLevel, Blocks.COBBLESTONE.defaultBlockState(), 4, b, 4, paramBoundingBox);
/* 963 */       placeBlock(paramWorldGenLevel, Blocks.COBBLESTONE.defaultBlockState(), 6, b, 4, paramBoundingBox);
/* 964 */       placeBlock(paramWorldGenLevel, Blocks.COBBLESTONE.defaultBlockState(), 4, b, 6, paramBoundingBox);
/* 965 */       placeBlock(paramWorldGenLevel, Blocks.COBBLESTONE.defaultBlockState(), 6, b, 6, paramBoundingBox);
/*     */     } 
/* 967 */     placeBlock(paramWorldGenLevel, Blocks.WALL_TORCH.defaultBlockState(), 5, 3, 5, paramBoundingBox);
/* 968 */     for (b = 2; b <= 8; b++) {
/* 969 */       placeBlock(paramWorldGenLevel, Blocks.OAK_PLANKS.defaultBlockState(), 2, 3, b, paramBoundingBox);
/* 970 */       placeBlock(paramWorldGenLevel, Blocks.OAK_PLANKS.defaultBlockState(), 3, 3, b, paramBoundingBox);
/* 971 */       if (b <= 3 || b >= 7) {
/* 972 */         placeBlock(paramWorldGenLevel, Blocks.OAK_PLANKS.defaultBlockState(), 4, 3, b, paramBoundingBox);
/* 973 */         placeBlock(paramWorldGenLevel, Blocks.OAK_PLANKS.defaultBlockState(), 5, 3, b, paramBoundingBox);
/* 974 */         placeBlock(paramWorldGenLevel, Blocks.OAK_PLANKS.defaultBlockState(), 6, 3, b, paramBoundingBox);
/*     */       } 
/* 976 */       placeBlock(paramWorldGenLevel, Blocks.OAK_PLANKS.defaultBlockState(), 7, 3, b, paramBoundingBox);
/* 977 */       placeBlock(paramWorldGenLevel, Blocks.OAK_PLANKS.defaultBlockState(), 8, 3, b, paramBoundingBox);
/*     */     } 
/* 979 */     BlockState blockState = (BlockState)Blocks.LADDER.defaultBlockState().setValue((Property)LadderBlock.FACING, (Comparable)Direction.WEST);
/* 980 */     placeBlock(paramWorldGenLevel, blockState, 9, 1, 3, paramBoundingBox);
/* 981 */     placeBlock(paramWorldGenLevel, blockState, 9, 2, 3, paramBoundingBox);
/* 982 */     placeBlock(paramWorldGenLevel, blockState, 9, 3, 3, paramBoundingBox);
/*     */     
/* 984 */     createChest(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 3, 4, 8, BuiltInLootTables.STRONGHOLD_CROSSING);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$RoomCrossing.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */