/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.StructureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class OceanMonumentSimpleRoom
/*      */   extends OceanMonumentPieces.OceanMonumentPiece
/*      */ {
/*      */   private int mainDesign;
/*      */   
/*      */   public OceanMonumentSimpleRoom(Direction paramDirection, OceanMonumentPieces.RoomDefinition paramRoomDefinition, RandomSource paramRandomSource) {
/*  857 */     super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_ROOM, 1, paramDirection, paramRoomDefinition, 1, 1, 1);
/*  858 */     this.mainDesign = paramRandomSource.nextInt(3);
/*      */   }
/*      */   
/*      */   public OceanMonumentSimpleRoom(CompoundTag paramCompoundTag) {
/*  862 */     super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_ROOM, paramCompoundTag);
/*      */   }
/*      */ 
/*      */   
/*      */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/*  867 */     if (this.roomDefinition.index / 25 > 0) {
/*  868 */       generateDefaultFloor(paramWorldGenLevel, paramBoundingBox, 0, 0, this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */     }
/*  870 */     if (this.roomDefinition.connections[Direction.UP.get3DDataValue()] == null) {
/*  871 */       generateBoxOnFillOnly(paramWorldGenLevel, paramBoundingBox, 1, 4, 1, 6, 4, 6, BASE_GRAY);
/*      */     }
/*      */     
/*  874 */     boolean bool = (this.mainDesign != 0 && paramRandomSource.nextBoolean() && !this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()] && !this.roomDefinition.hasOpening[Direction.UP.get3DDataValue()] && this.roomDefinition.countOpenings() > 1) ? true : false;
/*      */     
/*  876 */     if (this.mainDesign == 0) {
/*      */       
/*  878 */       generateBox(paramWorldGenLevel, paramBoundingBox, 0, 1, 0, 2, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  879 */       generateBox(paramWorldGenLevel, paramBoundingBox, 0, 3, 0, 2, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  880 */       generateBox(paramWorldGenLevel, paramBoundingBox, 0, 2, 0, 0, 2, 2, BASE_GRAY, BASE_GRAY, false);
/*  881 */       generateBox(paramWorldGenLevel, paramBoundingBox, 1, 2, 0, 2, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  882 */       placeBlock(paramWorldGenLevel, LAMP_BLOCK, 1, 2, 1, paramBoundingBox);
/*      */ 
/*      */       
/*  885 */       generateBox(paramWorldGenLevel, paramBoundingBox, 5, 1, 0, 7, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  886 */       generateBox(paramWorldGenLevel, paramBoundingBox, 5, 3, 0, 7, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  887 */       generateBox(paramWorldGenLevel, paramBoundingBox, 7, 2, 0, 7, 2, 2, BASE_GRAY, BASE_GRAY, false);
/*  888 */       generateBox(paramWorldGenLevel, paramBoundingBox, 5, 2, 0, 6, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  889 */       placeBlock(paramWorldGenLevel, LAMP_BLOCK, 6, 2, 1, paramBoundingBox);
/*      */ 
/*      */       
/*  892 */       generateBox(paramWorldGenLevel, paramBoundingBox, 0, 1, 5, 2, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  893 */       generateBox(paramWorldGenLevel, paramBoundingBox, 0, 3, 5, 2, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  894 */       generateBox(paramWorldGenLevel, paramBoundingBox, 0, 2, 5, 0, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  895 */       generateBox(paramWorldGenLevel, paramBoundingBox, 1, 2, 7, 2, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  896 */       placeBlock(paramWorldGenLevel, LAMP_BLOCK, 1, 2, 6, paramBoundingBox);
/*      */ 
/*      */       
/*  899 */       generateBox(paramWorldGenLevel, paramBoundingBox, 5, 1, 5, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  900 */       generateBox(paramWorldGenLevel, paramBoundingBox, 5, 3, 5, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  901 */       generateBox(paramWorldGenLevel, paramBoundingBox, 7, 2, 5, 7, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  902 */       generateBox(paramWorldGenLevel, paramBoundingBox, 5, 2, 7, 6, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  903 */       placeBlock(paramWorldGenLevel, LAMP_BLOCK, 6, 2, 6, paramBoundingBox);
/*      */       
/*  905 */       if (this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/*  906 */         generateBox(paramWorldGenLevel, paramBoundingBox, 3, 3, 0, 4, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } else {
/*  908 */         generateBox(paramWorldGenLevel, paramBoundingBox, 3, 3, 0, 4, 3, 1, BASE_LIGHT, BASE_LIGHT, false);
/*  909 */         generateBox(paramWorldGenLevel, paramBoundingBox, 3, 2, 0, 4, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  910 */         generateBox(paramWorldGenLevel, paramBoundingBox, 3, 1, 0, 4, 1, 1, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*  912 */       if (this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/*  913 */         generateBox(paramWorldGenLevel, paramBoundingBox, 3, 3, 7, 4, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } else {
/*  915 */         generateBox(paramWorldGenLevel, paramBoundingBox, 3, 3, 6, 4, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  916 */         generateBox(paramWorldGenLevel, paramBoundingBox, 3, 2, 7, 4, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  917 */         generateBox(paramWorldGenLevel, paramBoundingBox, 3, 1, 6, 4, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*  919 */       if (this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/*  920 */         generateBox(paramWorldGenLevel, paramBoundingBox, 0, 3, 3, 0, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } else {
/*  922 */         generateBox(paramWorldGenLevel, paramBoundingBox, 0, 3, 3, 1, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*  923 */         generateBox(paramWorldGenLevel, paramBoundingBox, 0, 2, 3, 0, 2, 4, BASE_GRAY, BASE_GRAY, false);
/*  924 */         generateBox(paramWorldGenLevel, paramBoundingBox, 0, 1, 3, 1, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*  926 */       if (this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
/*  927 */         generateBox(paramWorldGenLevel, paramBoundingBox, 7, 3, 3, 7, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } else {
/*  929 */         generateBox(paramWorldGenLevel, paramBoundingBox, 6, 3, 3, 7, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*  930 */         generateBox(paramWorldGenLevel, paramBoundingBox, 7, 2, 3, 7, 2, 4, BASE_GRAY, BASE_GRAY, false);
/*  931 */         generateBox(paramWorldGenLevel, paramBoundingBox, 6, 1, 3, 7, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*  933 */     } else if (this.mainDesign == 1) {
/*      */       
/*  935 */       generateBox(paramWorldGenLevel, paramBoundingBox, 2, 1, 2, 2, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  936 */       generateBox(paramWorldGenLevel, paramBoundingBox, 2, 1, 5, 2, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
/*  937 */       generateBox(paramWorldGenLevel, paramBoundingBox, 5, 1, 5, 5, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
/*  938 */       generateBox(paramWorldGenLevel, paramBoundingBox, 5, 1, 2, 5, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  939 */       placeBlock(paramWorldGenLevel, LAMP_BLOCK, 2, 2, 2, paramBoundingBox);
/*  940 */       placeBlock(paramWorldGenLevel, LAMP_BLOCK, 2, 2, 5, paramBoundingBox);
/*  941 */       placeBlock(paramWorldGenLevel, LAMP_BLOCK, 5, 2, 5, paramBoundingBox);
/*  942 */       placeBlock(paramWorldGenLevel, LAMP_BLOCK, 5, 2, 2, paramBoundingBox);
/*      */ 
/*      */       
/*  945 */       generateBox(paramWorldGenLevel, paramBoundingBox, 0, 1, 0, 1, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  946 */       generateBox(paramWorldGenLevel, paramBoundingBox, 0, 1, 1, 0, 3, 1, BASE_LIGHT, BASE_LIGHT, false);
/*  947 */       generateBox(paramWorldGenLevel, paramBoundingBox, 0, 1, 7, 1, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  948 */       generateBox(paramWorldGenLevel, paramBoundingBox, 0, 1, 6, 0, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  949 */       generateBox(paramWorldGenLevel, paramBoundingBox, 6, 1, 7, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  950 */       generateBox(paramWorldGenLevel, paramBoundingBox, 7, 1, 6, 7, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  951 */       generateBox(paramWorldGenLevel, paramBoundingBox, 6, 1, 0, 7, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  952 */       generateBox(paramWorldGenLevel, paramBoundingBox, 7, 1, 1, 7, 3, 1, BASE_LIGHT, BASE_LIGHT, false);
/*  953 */       placeBlock(paramWorldGenLevel, BASE_GRAY, 1, 2, 0, paramBoundingBox);
/*  954 */       placeBlock(paramWorldGenLevel, BASE_GRAY, 0, 2, 1, paramBoundingBox);
/*  955 */       placeBlock(paramWorldGenLevel, BASE_GRAY, 1, 2, 7, paramBoundingBox);
/*  956 */       placeBlock(paramWorldGenLevel, BASE_GRAY, 0, 2, 6, paramBoundingBox);
/*  957 */       placeBlock(paramWorldGenLevel, BASE_GRAY, 6, 2, 7, paramBoundingBox);
/*  958 */       placeBlock(paramWorldGenLevel, BASE_GRAY, 7, 2, 6, paramBoundingBox);
/*  959 */       placeBlock(paramWorldGenLevel, BASE_GRAY, 6, 2, 0, paramBoundingBox);
/*  960 */       placeBlock(paramWorldGenLevel, BASE_GRAY, 7, 2, 1, paramBoundingBox);
/*  961 */       if (!this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/*  962 */         generateBox(paramWorldGenLevel, paramBoundingBox, 1, 3, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  963 */         generateBox(paramWorldGenLevel, paramBoundingBox, 1, 2, 0, 6, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  964 */         generateBox(paramWorldGenLevel, paramBoundingBox, 1, 1, 0, 6, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*  966 */       if (!this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/*  967 */         generateBox(paramWorldGenLevel, paramBoundingBox, 1, 3, 7, 6, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  968 */         generateBox(paramWorldGenLevel, paramBoundingBox, 1, 2, 7, 6, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  969 */         generateBox(paramWorldGenLevel, paramBoundingBox, 1, 1, 7, 6, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*  971 */       if (!this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/*  972 */         generateBox(paramWorldGenLevel, paramBoundingBox, 0, 3, 1, 0, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  973 */         generateBox(paramWorldGenLevel, paramBoundingBox, 0, 2, 1, 0, 2, 6, BASE_GRAY, BASE_GRAY, false);
/*  974 */         generateBox(paramWorldGenLevel, paramBoundingBox, 0, 1, 1, 0, 1, 6, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*  976 */       if (!this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
/*  977 */         generateBox(paramWorldGenLevel, paramBoundingBox, 7, 3, 1, 7, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  978 */         generateBox(paramWorldGenLevel, paramBoundingBox, 7, 2, 1, 7, 2, 6, BASE_GRAY, BASE_GRAY, false);
/*  979 */         generateBox(paramWorldGenLevel, paramBoundingBox, 7, 1, 1, 7, 1, 6, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*  981 */     } else if (this.mainDesign == 2) {
/*  982 */       generateBox(paramWorldGenLevel, paramBoundingBox, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  983 */       generateBox(paramWorldGenLevel, paramBoundingBox, 7, 1, 0, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  984 */       generateBox(paramWorldGenLevel, paramBoundingBox, 1, 1, 0, 6, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  985 */       generateBox(paramWorldGenLevel, paramBoundingBox, 1, 1, 7, 6, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/*  987 */       generateBox(paramWorldGenLevel, paramBoundingBox, 0, 2, 0, 0, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*  988 */       generateBox(paramWorldGenLevel, paramBoundingBox, 7, 2, 0, 7, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*  989 */       generateBox(paramWorldGenLevel, paramBoundingBox, 1, 2, 0, 6, 2, 0, BASE_BLACK, BASE_BLACK, false);
/*  990 */       generateBox(paramWorldGenLevel, paramBoundingBox, 1, 2, 7, 6, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*      */       
/*  992 */       generateBox(paramWorldGenLevel, paramBoundingBox, 0, 3, 0, 0, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  993 */       generateBox(paramWorldGenLevel, paramBoundingBox, 7, 3, 0, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  994 */       generateBox(paramWorldGenLevel, paramBoundingBox, 1, 3, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  995 */       generateBox(paramWorldGenLevel, paramBoundingBox, 1, 3, 7, 6, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/*  997 */       generateBox(paramWorldGenLevel, paramBoundingBox, 0, 1, 3, 0, 2, 4, BASE_BLACK, BASE_BLACK, false);
/*  998 */       generateBox(paramWorldGenLevel, paramBoundingBox, 7, 1, 3, 7, 2, 4, BASE_BLACK, BASE_BLACK, false);
/*  999 */       generateBox(paramWorldGenLevel, paramBoundingBox, 3, 1, 0, 4, 2, 0, BASE_BLACK, BASE_BLACK, false);
/* 1000 */       generateBox(paramWorldGenLevel, paramBoundingBox, 3, 1, 7, 4, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*      */       
/* 1002 */       if (this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1003 */         generateWaterBox(paramWorldGenLevel, paramBoundingBox, 3, 1, 0, 4, 2, 0);
/*      */       }
/* 1005 */       if (this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1006 */         generateWaterBox(paramWorldGenLevel, paramBoundingBox, 3, 1, 7, 4, 2, 7);
/*      */       }
/* 1008 */       if (this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1009 */         generateWaterBox(paramWorldGenLevel, paramBoundingBox, 0, 1, 3, 0, 2, 4);
/*      */       }
/* 1011 */       if (this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1012 */         generateWaterBox(paramWorldGenLevel, paramBoundingBox, 7, 1, 3, 7, 2, 4);
/*      */       }
/*      */     } 
/* 1015 */     if (bool) {
/* 1016 */       generateBox(paramWorldGenLevel, paramBoundingBox, 3, 1, 3, 4, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1017 */       generateBox(paramWorldGenLevel, paramBoundingBox, 3, 2, 3, 4, 2, 4, BASE_GRAY, BASE_GRAY, false);
/* 1018 */       generateBox(paramWorldGenLevel, paramBoundingBox, 3, 3, 3, 4, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces$OceanMonumentSimpleRoom.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */