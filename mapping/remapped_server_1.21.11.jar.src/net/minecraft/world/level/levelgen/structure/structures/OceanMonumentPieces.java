/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Lists;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.monster.ElderGuardian;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.StructureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ 
/*      */ public class OceanMonumentPieces {
/*      */   protected static abstract class OceanMonumentPiece extends StructurePiece {
/*   34 */     protected static final BlockState BASE_GRAY = Blocks.PRISMARINE.defaultBlockState();
/*   35 */     protected static final BlockState BASE_LIGHT = Blocks.PRISMARINE_BRICKS.defaultBlockState();
/*   36 */     protected static final BlockState BASE_BLACK = Blocks.DARK_PRISMARINE.defaultBlockState();
/*      */     
/*   38 */     protected static final BlockState DOT_DECO_DATA = BASE_LIGHT;
/*      */     
/*   40 */     protected static final BlockState LAMP_BLOCK = Blocks.SEA_LANTERN.defaultBlockState();
/*      */     
/*      */     protected static final boolean DO_FILL = true;
/*   43 */     protected static final BlockState FILL_BLOCK = Blocks.WATER.defaultBlockState();
/*   44 */     protected static final Set<Block> FILL_KEEP = (Set<Block>)ImmutableSet.builder()
/*   45 */       .add(Blocks.ICE)
/*   46 */       .add(Blocks.PACKED_ICE)
/*   47 */       .add(Blocks.BLUE_ICE)
/*   48 */       .add(FILL_BLOCK.getBlock())
/*   49 */       .build();
/*      */     
/*      */     protected static final int GRIDROOM_WIDTH = 8;
/*      */     
/*      */     protected static final int GRIDROOM_DEPTH = 8;
/*      */     protected static final int GRIDROOM_HEIGHT = 4;
/*      */     protected static final int GRID_WIDTH = 5;
/*      */     protected static final int GRID_DEPTH = 5;
/*      */     protected static final int GRID_HEIGHT = 3;
/*      */     protected static final int GRID_FLOOR_COUNT = 25;
/*      */     protected static final int GRID_SIZE = 75;
/*   60 */     protected static final int GRIDROOM_SOURCE_INDEX = getRoomIndex(2, 0, 0);
/*   61 */     protected static final int GRIDROOM_TOP_CONNECT_INDEX = getRoomIndex(2, 2, 0);
/*   62 */     protected static final int GRIDROOM_LEFTWING_CONNECT_INDEX = getRoomIndex(0, 1, 0);
/*   63 */     protected static final int GRIDROOM_RIGHTWING_CONNECT_INDEX = getRoomIndex(4, 1, 0);
/*      */     
/*      */     protected static final int LEFTWING_INDEX = 1001;
/*      */     
/*      */     protected static final int RIGHTWING_INDEX = 1002;
/*      */     protected static final int PENTHOUSE_INDEX = 1003;
/*      */     protected OceanMonumentPieces.RoomDefinition roomDefinition;
/*      */     
/*      */     protected static int getRoomIndex(int param1Int1, int param1Int2, int param1Int3) {
/*   72 */       return param1Int2 * 25 + param1Int3 * 5 + param1Int1;
/*      */     }
/*      */     
/*      */     public OceanMonumentPiece(StructurePieceType param1StructurePieceType, Direction param1Direction, int param1Int, BoundingBox param1BoundingBox) {
/*   76 */       super(param1StructurePieceType, param1Int, param1BoundingBox);
/*   77 */       setOrientation(param1Direction);
/*      */     }
/*      */     
/*      */     protected OceanMonumentPiece(StructurePieceType param1StructurePieceType, int param1Int1, Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition, int param1Int2, int param1Int3, int param1Int4) {
/*   81 */       super(param1StructurePieceType, param1Int1, makeBoundingBox(param1Direction, param1RoomDefinition, param1Int2, param1Int3, param1Int4));
/*      */       
/*   83 */       setOrientation(param1Direction);
/*   84 */       this.roomDefinition = param1RoomDefinition;
/*      */     }
/*      */     
/*      */     private static BoundingBox makeBoundingBox(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition, int param1Int1, int param1Int2, int param1Int3) {
/*   88 */       int i = param1RoomDefinition.index;
/*   89 */       int j = i % 5;
/*   90 */       int k = i / 5 % 5;
/*   91 */       int m = i / 25;
/*      */ 
/*      */ 
/*      */       
/*   95 */       BoundingBox boundingBox = makeBoundingBox(0, 0, 0, param1Direction, param1Int1 * 8, param1Int2 * 4, param1Int3 * 8);
/*      */       
/*   97 */       switch (param1Direction)
/*      */       { case NORTH:
/*   99 */           boundingBox.move(j * 8, m * 4, -(k + param1Int3) * 8 + 1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  114 */           return boundingBox;case SOUTH: boundingBox.move(j * 8, m * 4, k * 8); return boundingBox;case WEST: boundingBox.move(-(k + param1Int3) * 8 + 1, m * 4, j * 8); return boundingBox; }  boundingBox.move(k * 8, m * 4, j * 8); return boundingBox;
/*      */     }
/*      */     
/*      */     public OceanMonumentPiece(StructurePieceType param1StructurePieceType, CompoundTag param1CompoundTag) {
/*  118 */       super(param1StructurePieceType, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {}
/*      */ 
/*      */     
/*      */     protected void generateWaterBox(WorldGenLevel param1WorldGenLevel, BoundingBox param1BoundingBox, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6) {
/*  126 */       for (int i = param1Int2; i <= param1Int5; i++) {
/*  127 */         for (int j = param1Int1; j <= param1Int4; j++) {
/*  128 */           for (int k = param1Int3; k <= param1Int6; k++) {
/*  129 */             BlockState blockState = getBlock((BlockGetter)param1WorldGenLevel, j, i, k, param1BoundingBox);
/*  130 */             if (!FILL_KEEP.contains(blockState.getBlock())) {
/*  131 */               if (getWorldY(i) >= param1WorldGenLevel.getSeaLevel() && blockState != FILL_BLOCK) {
/*  132 */                 placeBlock(param1WorldGenLevel, Blocks.AIR.defaultBlockState(), j, i, k, param1BoundingBox);
/*      */               } else {
/*  134 */                 placeBlock(param1WorldGenLevel, FILL_BLOCK, j, i, k, param1BoundingBox);
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     protected void generateDefaultFloor(WorldGenLevel param1WorldGenLevel, BoundingBox param1BoundingBox, int param1Int1, int param1Int2, boolean param1Boolean) {
/*  143 */       if (param1Boolean) {
/*  144 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int1 + 0, 0, param1Int2 + 0, param1Int1 + 2, 0, param1Int2 + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/*  145 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int1 + 5, 0, param1Int2 + 0, param1Int1 + 8 - 1, 0, param1Int2 + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/*  146 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int1 + 3, 0, param1Int2 + 0, param1Int1 + 4, 0, param1Int2 + 2, BASE_GRAY, BASE_GRAY, false);
/*  147 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int1 + 3, 0, param1Int2 + 5, param1Int1 + 4, 0, param1Int2 + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  149 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int1 + 3, 0, param1Int2 + 2, param1Int1 + 4, 0, param1Int2 + 2, BASE_LIGHT, BASE_LIGHT, false);
/*  150 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int1 + 3, 0, param1Int2 + 5, param1Int1 + 4, 0, param1Int2 + 5, BASE_LIGHT, BASE_LIGHT, false);
/*  151 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int1 + 2, 0, param1Int2 + 3, param1Int1 + 2, 0, param1Int2 + 4, BASE_LIGHT, BASE_LIGHT, false);
/*  152 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int1 + 5, 0, param1Int2 + 3, param1Int1 + 5, 0, param1Int2 + 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } else {
/*  154 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int1 + 0, 0, param1Int2 + 0, param1Int1 + 8 - 1, 0, param1Int2 + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected void generateBoxOnFillOnly(WorldGenLevel param1WorldGenLevel, BoundingBox param1BoundingBox, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, BlockState param1BlockState) {
/*  159 */       for (int i = param1Int2; i <= param1Int5; i++) {
/*  160 */         for (int j = param1Int1; j <= param1Int4; j++) {
/*  161 */           for (int k = param1Int3; k <= param1Int6; k++) {
/*  162 */             if (getBlock((BlockGetter)param1WorldGenLevel, j, i, k, param1BoundingBox) == FILL_BLOCK)
/*      */             {
/*      */               
/*  165 */               placeBlock(param1WorldGenLevel, param1BlockState, j, i, k, param1BoundingBox); } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     protected boolean chunkIntersects(BoundingBox param1BoundingBox, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
/*  172 */       int i = getWorldX(param1Int1, param1Int2);
/*  173 */       int j = getWorldZ(param1Int1, param1Int2);
/*  174 */       int k = getWorldX(param1Int3, param1Int4);
/*  175 */       int m = getWorldZ(param1Int3, param1Int4);
/*  176 */       return param1BoundingBox.intersects(Math.min(i, k), Math.min(j, m), Math.max(i, k), Math.max(j, m));
/*      */     }
/*      */     
/*      */     protected void spawnElder(WorldGenLevel param1WorldGenLevel, BoundingBox param1BoundingBox, int param1Int1, int param1Int2, int param1Int3) {
/*  180 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(param1Int1, param1Int2, param1Int3);
/*  181 */       if (param1BoundingBox.isInside((Vec3i)mutableBlockPos)) {
/*  182 */         ElderGuardian elderGuardian = (ElderGuardian)EntityType.ELDER_GUARDIAN.create((Level)param1WorldGenLevel.getLevel(), EntitySpawnReason.STRUCTURE);
/*  183 */         if (elderGuardian != null) {
/*  184 */           elderGuardian.heal(elderGuardian.getMaxHealth());
/*  185 */           elderGuardian.snapTo(mutableBlockPos.getX() + 0.5D, mutableBlockPos.getY(), mutableBlockPos.getZ() + 0.5D, 0.0F, 0.0F);
/*  186 */           elderGuardian.finalizeSpawn((ServerLevelAccessor)param1WorldGenLevel, param1WorldGenLevel.getCurrentDifficultyAt(elderGuardian.blockPosition()), EntitySpawnReason.STRUCTURE, null);
/*  187 */           param1WorldGenLevel.addFreshEntityWithPassengers((Entity)elderGuardian);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class MonumentBuilding
/*      */     extends OceanMonumentPiece
/*      */   {
/*      */     private static final int WIDTH = 58;
/*      */     
/*      */     private static final int HEIGHT = 22;
/*      */     
/*      */     private static final int DEPTH = 58;
/*      */     
/*      */     public static final int BIOME_RANGE_CHECK = 29;
/*      */     
/*      */     private static final int TOP_POSITION = 61;
/*      */     
/*      */     private OceanMonumentPieces.RoomDefinition sourceRoom;
/*      */     private OceanMonumentPieces.RoomDefinition coreRoom;
/*  209 */     private final List<OceanMonumentPieces.OceanMonumentPiece> childPieces = Lists.newArrayList();
/*      */     
/*      */     public MonumentBuilding(RandomSource param1RandomSource, int param1Int1, int param1Int2, Direction param1Direction) {
/*  212 */       super(StructurePieceType.OCEAN_MONUMENT_BUILDING, param1Direction, 0, makeBoundingBox(param1Int1, 39, param1Int2, param1Direction, 58, 23, 58));
/*      */       
/*  214 */       setOrientation(param1Direction);
/*      */       
/*  216 */       List<OceanMonumentPieces.RoomDefinition> list = generateRoomGraph(param1RandomSource);
/*      */       
/*  218 */       this.sourceRoom.claimed = true;
/*  219 */       this.childPieces.add(new OceanMonumentPieces.OceanMonumentEntryRoom(param1Direction, this.sourceRoom));
/*  220 */       this.childPieces.add(new OceanMonumentPieces.OceanMonumentCoreRoom(param1Direction, this.coreRoom));
/*      */       
/*  222 */       ArrayList<OceanMonumentPieces.FitDoubleXYRoom> arrayList = Lists.newArrayList();
/*  223 */       arrayList.add(new OceanMonumentPieces.FitDoubleXYRoom());
/*  224 */       arrayList.add(new OceanMonumentPieces.FitDoubleYZRoom());
/*  225 */       arrayList.add(new OceanMonumentPieces.FitDoubleZRoom());
/*  226 */       arrayList.add(new OceanMonumentPieces.FitDoubleXRoom());
/*  227 */       arrayList.add(new OceanMonumentPieces.FitDoubleYRoom());
/*  228 */       arrayList.add(new OceanMonumentPieces.FitSimpleTopRoom());
/*  229 */       arrayList.add(new OceanMonumentPieces.FitSimpleRoom());
/*      */       
/*  231 */       for (OceanMonumentPieces.RoomDefinition roomDefinition : list) {
/*  232 */         if (!roomDefinition.claimed && !roomDefinition.isSpecial())
/*      */         {
/*  234 */           for (OceanMonumentPieces.MonumentRoomFitter monumentRoomFitter : arrayList) {
/*  235 */             if (monumentRoomFitter.fits(roomDefinition)) {
/*  236 */               this.childPieces.add(monumentRoomFitter.create(param1Direction, roomDefinition, param1RandomSource));
/*      */             }
/*      */           } 
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  244 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(9, 0, 22);
/*  245 */       for (OceanMonumentPieces.OceanMonumentPiece oceanMonumentPiece : this.childPieces) {
/*  246 */         oceanMonumentPiece.getBoundingBox().move((Vec3i)mutableBlockPos);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  251 */       BoundingBox boundingBox1 = BoundingBox.fromCorners((Vec3i)getWorldPos(1, 1, 1), (Vec3i)getWorldPos(23, 8, 21));
/*  252 */       BoundingBox boundingBox2 = BoundingBox.fromCorners((Vec3i)getWorldPos(34, 1, 1), (Vec3i)getWorldPos(56, 8, 21));
/*  253 */       BoundingBox boundingBox3 = BoundingBox.fromCorners((Vec3i)getWorldPos(22, 13, 22), (Vec3i)getWorldPos(35, 17, 35));
/*      */ 
/*      */       
/*  256 */       int i = param1RandomSource.nextInt();
/*  257 */       this.childPieces.add(new OceanMonumentPieces.OceanMonumentWingRoom(param1Direction, boundingBox1, i++));
/*  258 */       this.childPieces.add(new OceanMonumentPieces.OceanMonumentWingRoom(param1Direction, boundingBox2, i++));
/*      */       
/*  260 */       this.childPieces.add(new OceanMonumentPieces.OceanMonumentPenthouse(param1Direction, boundingBox3));
/*      */     }
/*      */ 
/*      */     
/*      */     public MonumentBuilding(CompoundTag param1CompoundTag) {
/*  265 */       super(StructurePieceType.OCEAN_MONUMENT_BUILDING, param1CompoundTag);
/*      */     }
/*      */     
/*      */     private List<OceanMonumentPieces.RoomDefinition> generateRoomGraph(RandomSource param1RandomSource) {
/*  269 */       OceanMonumentPieces.RoomDefinition[] arrayOfRoomDefinition = new OceanMonumentPieces.RoomDefinition[75];
/*      */       byte b1;
/*  271 */       for (b1 = 0; b1 < 5; b1++) {
/*  272 */         for (byte b = 0; b < 4; b++) {
/*  273 */           boolean bool = false;
/*  274 */           int i = getRoomIndex(b1, 0, b);
/*  275 */           arrayOfRoomDefinition[i] = new OceanMonumentPieces.RoomDefinition(i);
/*      */         } 
/*      */       } 
/*  278 */       for (b1 = 0; b1 < 5; b1++) {
/*  279 */         for (byte b = 0; b < 4; b++) {
/*  280 */           boolean bool = true;
/*  281 */           int i = getRoomIndex(b1, 1, b);
/*  282 */           arrayOfRoomDefinition[i] = new OceanMonumentPieces.RoomDefinition(i);
/*      */         } 
/*      */       } 
/*  285 */       for (b1 = 1; b1 < 4; b1++) {
/*  286 */         for (byte b = 0; b < 2; b++) {
/*  287 */           byte b3 = 2;
/*  288 */           int i = getRoomIndex(b1, 2, b);
/*  289 */           arrayOfRoomDefinition[i] = new OceanMonumentPieces.RoomDefinition(i);
/*      */         } 
/*      */       } 
/*      */       
/*  293 */       this.sourceRoom = arrayOfRoomDefinition[GRIDROOM_SOURCE_INDEX];
/*      */       
/*  295 */       for (b1 = 0; b1 < 5; b1++) {
/*  296 */         for (byte b = 0; b < 5; b++) {
/*  297 */           for (byte b3 = 0; b3 < 3; b3++) {
/*  298 */             int i = getRoomIndex(b1, b3, b);
/*  299 */             if (arrayOfRoomDefinition[i] != null)
/*      */             {
/*      */               
/*  302 */               for (Direction direction : Direction.values()) {
/*  303 */                 int j = b1 + direction.getStepX();
/*  304 */                 int k = b3 + direction.getStepY();
/*  305 */                 int m = b + direction.getStepZ();
/*  306 */                 if (j >= 0 && j < 5 && m >= 0 && m < 5 && k >= 0 && k < 3) {
/*  307 */                   int n = getRoomIndex(j, k, m);
/*  308 */                   if (arrayOfRoomDefinition[n] != null)
/*      */                   {
/*      */                     
/*  311 */                     if (m == b) {
/*  312 */                       arrayOfRoomDefinition[i].setConnection(direction, arrayOfRoomDefinition[n]);
/*      */                     } else {
/*  314 */                       arrayOfRoomDefinition[i].setConnection(direction.getOpposite(), arrayOfRoomDefinition[n]);
/*      */                     }  } 
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*  322 */       OceanMonumentPieces.RoomDefinition roomDefinition1 = new OceanMonumentPieces.RoomDefinition(1003);
/*  323 */       OceanMonumentPieces.RoomDefinition roomDefinition2 = new OceanMonumentPieces.RoomDefinition(1001);
/*  324 */       OceanMonumentPieces.RoomDefinition roomDefinition3 = new OceanMonumentPieces.RoomDefinition(1002);
/*  325 */       arrayOfRoomDefinition[GRIDROOM_TOP_CONNECT_INDEX].setConnection(Direction.UP, roomDefinition1);
/*  326 */       arrayOfRoomDefinition[GRIDROOM_LEFTWING_CONNECT_INDEX].setConnection(Direction.SOUTH, roomDefinition2);
/*  327 */       arrayOfRoomDefinition[GRIDROOM_RIGHTWING_CONNECT_INDEX].setConnection(Direction.SOUTH, roomDefinition3);
/*  328 */       roomDefinition1.claimed = true;
/*  329 */       roomDefinition2.claimed = true;
/*  330 */       roomDefinition3.claimed = true;
/*  331 */       this.sourceRoom.isSource = true;
/*      */ 
/*      */       
/*  334 */       this.coreRoom = arrayOfRoomDefinition[getRoomIndex(param1RandomSource.nextInt(4), 0, 2)];
/*  335 */       this.coreRoom.claimed = true;
/*  336 */       (this.coreRoom.connections[Direction.EAST.get3DDataValue()]).claimed = true;
/*  337 */       (this.coreRoom.connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/*  338 */       ((this.coreRoom.connections[Direction.EAST.get3DDataValue()]).connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/*  339 */       (this.coreRoom.connections[Direction.UP.get3DDataValue()]).claimed = true;
/*  340 */       ((this.coreRoom.connections[Direction.EAST.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/*  341 */       ((this.coreRoom.connections[Direction.NORTH.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/*  342 */       (((this.coreRoom.connections[Direction.EAST.get3DDataValue()]).connections[Direction.NORTH.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/*      */       
/*  344 */       ObjectArrayList objectArrayList = new ObjectArrayList();
/*  345 */       for (OceanMonumentPieces.RoomDefinition roomDefinition : arrayOfRoomDefinition) {
/*  346 */         if (roomDefinition != null) {
/*  347 */           roomDefinition.updateOpenings();
/*  348 */           objectArrayList.add(roomDefinition);
/*      */         } 
/*      */       } 
/*  351 */       roomDefinition1.updateOpenings();
/*      */       
/*  353 */       Util.shuffle((List)objectArrayList, param1RandomSource);
/*  354 */       byte b2 = 1;
/*  355 */       for (ObjectListIterator<OceanMonumentPieces.RoomDefinition> objectListIterator = objectArrayList.iterator(); objectListIterator.hasNext(); ) { OceanMonumentPieces.RoomDefinition roomDefinition = objectListIterator.next();
/*      */         
/*  357 */         byte b3 = 0;
/*  358 */         byte b4 = 0;
/*  359 */         while (b3 < 2 && b4 < 5) {
/*  360 */           b4++;
/*      */           
/*  362 */           int i = param1RandomSource.nextInt(6);
/*  363 */           if (roomDefinition.hasOpening[i]) {
/*  364 */             int j = Direction.from3DDataValue(i).getOpposite().get3DDataValue();
/*      */ 
/*      */             
/*  367 */             roomDefinition.hasOpening[i] = false;
/*  368 */             (roomDefinition.connections[i]).hasOpening[j] = false;
/*      */             
/*  370 */             if (roomDefinition.findSource(b2++) && roomDefinition.connections[i].findSource(b2++)) {
/*  371 */               b3++;
/*      */               continue;
/*      */             } 
/*  374 */             roomDefinition.hasOpening[i] = true;
/*  375 */             (roomDefinition.connections[i]).hasOpening[j] = true;
/*      */           } 
/*      */         }  }
/*      */ 
/*      */       
/*  380 */       objectArrayList.add(roomDefinition1);
/*  381 */       objectArrayList.add(roomDefinition2);
/*  382 */       objectArrayList.add(roomDefinition3);
/*      */       
/*  384 */       return (List<OceanMonumentPieces.RoomDefinition>)objectArrayList;
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/*  389 */       int i = Math.max(param1WorldGenLevel.getSeaLevel(), 64) - this.boundingBox.minY();
/*      */       
/*  391 */       generateWaterBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 0, 58, i, 58);
/*      */ 
/*      */       
/*  394 */       generateWing(false, 0, param1WorldGenLevel, param1RandomSource, param1BoundingBox);
/*      */ 
/*      */       
/*  397 */       generateWing(true, 33, param1WorldGenLevel, param1RandomSource, param1BoundingBox);
/*      */ 
/*      */       
/*  400 */       generateEntranceArchs(param1WorldGenLevel, param1RandomSource, param1BoundingBox);
/*      */       
/*  402 */       generateEntranceWall(param1WorldGenLevel, param1RandomSource, param1BoundingBox);
/*  403 */       generateRoofPiece(param1WorldGenLevel, param1RandomSource, param1BoundingBox);
/*      */       
/*  405 */       generateLowerWall(param1WorldGenLevel, param1RandomSource, param1BoundingBox);
/*  406 */       generateMiddleWall(param1WorldGenLevel, param1RandomSource, param1BoundingBox);
/*  407 */       generateUpperWall(param1WorldGenLevel, param1RandomSource, param1BoundingBox);
/*      */       
/*      */       byte b;
/*  410 */       for (b = 0; b < 7; b++) {
/*  411 */         for (byte b1 = 0; b1 < 7; ) {
/*  412 */           if (b1 == 0 && b == 3)
/*      */           {
/*  414 */             b1 = 6;
/*      */           }
/*      */           
/*  417 */           int j = b * 9;
/*  418 */           int k = b1 * 9;
/*  419 */           for (byte b2 = 0; b2 < 4; b2++) {
/*  420 */             for (byte b3 = 0; b3 < 4; b3++) {
/*  421 */               placeBlock(param1WorldGenLevel, BASE_LIGHT, j + b2, 0, k + b3, param1BoundingBox);
/*  422 */               fillColumnDown(param1WorldGenLevel, BASE_LIGHT, j + b2, -1, k + b3, param1BoundingBox);
/*      */             } 
/*      */           } 
/*      */           
/*  426 */           if (b == 0 || b == 6) {
/*  427 */             b1++; continue;
/*      */           } 
/*  429 */           b1 += 6;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  435 */       for (b = 0; b < 5; b++) {
/*  436 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, -1 - b, 0 + b * 2, -1 - b, -1 - b, 23, 58 + b);
/*  437 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 58 + b, 0 + b * 2, -1 - b, 58 + b, 23, 58 + b);
/*  438 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 0 - b, 0 + b * 2, -1 - b, 57 + b, 23, -1 - b);
/*  439 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 0 - b, 0 + b * 2, 58 + b, 57 + b, 23, 58 + b);
/*      */       } 
/*      */       
/*  442 */       for (OceanMonumentPieces.OceanMonumentPiece oceanMonumentPiece : this.childPieces) {
/*  443 */         if (oceanMonumentPiece.getBoundingBox().intersects(param1BoundingBox)) {
/*  444 */           oceanMonumentPiece.postProcess(param1WorldGenLevel, param1StructureManager, param1ChunkGenerator, param1RandomSource, param1BoundingBox, param1ChunkPos, param1BlockPos);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void generateWing(boolean param1Boolean, int param1Int, WorldGenLevel param1WorldGenLevel, RandomSource param1RandomSource, BoundingBox param1BoundingBox) {
/*  451 */       byte b = 24;
/*  452 */       if (chunkIntersects(param1BoundingBox, param1Int, 0, param1Int + 23, 20)) {
/*  453 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int + 0, 0, 0, param1Int + 24, 0, 20, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  455 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, param1Int + 0, 1, 0, param1Int + 24, 10, 20);
/*      */         int i;
/*  457 */         for (i = 0; i < 4; i++) {
/*  458 */           generateBox(param1WorldGenLevel, param1BoundingBox, param1Int + i, i + 1, i, param1Int + i, i + 1, 20, BASE_LIGHT, BASE_LIGHT, false);
/*  459 */           generateBox(param1WorldGenLevel, param1BoundingBox, param1Int + i + 7, i + 5, i + 7, param1Int + i + 7, i + 5, 20, BASE_LIGHT, BASE_LIGHT, false);
/*  460 */           generateBox(param1WorldGenLevel, param1BoundingBox, param1Int + 17 - i, i + 5, i + 7, param1Int + 17 - i, i + 5, 20, BASE_LIGHT, BASE_LIGHT, false);
/*  461 */           generateBox(param1WorldGenLevel, param1BoundingBox, param1Int + 24 - i, i + 1, i, param1Int + 24 - i, i + 1, 20, BASE_LIGHT, BASE_LIGHT, false);
/*      */           
/*  463 */           generateBox(param1WorldGenLevel, param1BoundingBox, param1Int + i + 1, i + 1, i, param1Int + 23 - i, i + 1, i, BASE_LIGHT, BASE_LIGHT, false);
/*  464 */           generateBox(param1WorldGenLevel, param1BoundingBox, param1Int + i + 8, i + 5, i + 7, param1Int + 16 - i, i + 5, i + 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  466 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int + 4, 4, 4, param1Int + 6, 4, 20, BASE_GRAY, BASE_GRAY, false);
/*  467 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int + 7, 4, 4, param1Int + 17, 4, 6, BASE_GRAY, BASE_GRAY, false);
/*  468 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int + 18, 4, 4, param1Int + 20, 4, 20, BASE_GRAY, BASE_GRAY, false);
/*  469 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int + 11, 8, 11, param1Int + 13, 8, 20, BASE_GRAY, BASE_GRAY, false);
/*  470 */         placeBlock(param1WorldGenLevel, DOT_DECO_DATA, param1Int + 12, 9, 12, param1BoundingBox);
/*  471 */         placeBlock(param1WorldGenLevel, DOT_DECO_DATA, param1Int + 12, 9, 15, param1BoundingBox);
/*  472 */         placeBlock(param1WorldGenLevel, DOT_DECO_DATA, param1Int + 12, 9, 18, param1BoundingBox);
/*      */         
/*  474 */         i = param1Int + (param1Boolean ? 19 : 5);
/*  475 */         int j = param1Int + (param1Boolean ? 5 : 19); byte b1;
/*  476 */         for (b1 = 20; b1 >= 5; b1 -= 3) {
/*  477 */           placeBlock(param1WorldGenLevel, DOT_DECO_DATA, i, 5, b1, param1BoundingBox);
/*      */         }
/*  479 */         for (b1 = 19; b1 >= 7; b1 -= 3) {
/*  480 */           placeBlock(param1WorldGenLevel, DOT_DECO_DATA, j, 5, b1, param1BoundingBox);
/*      */         }
/*  482 */         for (b1 = 0; b1 < 4; b1++) {
/*  483 */           int k = param1Boolean ? (param1Int + 24 - 17 - b1 * 3) : (param1Int + 17 - b1 * 3);
/*  484 */           placeBlock(param1WorldGenLevel, DOT_DECO_DATA, k, 5, 5, param1BoundingBox);
/*      */         } 
/*  486 */         placeBlock(param1WorldGenLevel, DOT_DECO_DATA, j, 5, 5, param1BoundingBox);
/*      */ 
/*      */         
/*  489 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int + 11, 1, 12, param1Int + 13, 7, 12, BASE_GRAY, BASE_GRAY, false);
/*  490 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int + 12, 1, 11, param1Int + 12, 7, 13, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void generateEntranceArchs(WorldGenLevel param1WorldGenLevel, RandomSource param1RandomSource, BoundingBox param1BoundingBox) {
/*  496 */       if (chunkIntersects(param1BoundingBox, 22, 5, 35, 17)) {
/*      */         
/*  498 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 25, 0, 0, 32, 8, 20);
/*      */ 
/*      */         
/*  501 */         for (byte b = 0; b < 4; b++) {
/*  502 */           generateBox(param1WorldGenLevel, param1BoundingBox, 24, 2, 5 + b * 4, 24, 4, 5 + b * 4, BASE_LIGHT, BASE_LIGHT, false);
/*  503 */           generateBox(param1WorldGenLevel, param1BoundingBox, 22, 4, 5 + b * 4, 23, 4, 5 + b * 4, BASE_LIGHT, BASE_LIGHT, false);
/*  504 */           placeBlock(param1WorldGenLevel, BASE_LIGHT, 25, 5, 5 + b * 4, param1BoundingBox);
/*  505 */           placeBlock(param1WorldGenLevel, BASE_LIGHT, 26, 6, 5 + b * 4, param1BoundingBox);
/*  506 */           placeBlock(param1WorldGenLevel, LAMP_BLOCK, 26, 5, 5 + b * 4, param1BoundingBox);
/*      */           
/*  508 */           generateBox(param1WorldGenLevel, param1BoundingBox, 33, 2, 5 + b * 4, 33, 4, 5 + b * 4, BASE_LIGHT, BASE_LIGHT, false);
/*  509 */           generateBox(param1WorldGenLevel, param1BoundingBox, 34, 4, 5 + b * 4, 35, 4, 5 + b * 4, BASE_LIGHT, BASE_LIGHT, false);
/*  510 */           placeBlock(param1WorldGenLevel, BASE_LIGHT, 32, 5, 5 + b * 4, param1BoundingBox);
/*  511 */           placeBlock(param1WorldGenLevel, BASE_LIGHT, 31, 6, 5 + b * 4, param1BoundingBox);
/*  512 */           placeBlock(param1WorldGenLevel, LAMP_BLOCK, 31, 5, 5 + b * 4, param1BoundingBox);
/*      */           
/*  514 */           generateBox(param1WorldGenLevel, param1BoundingBox, 27, 6, 5 + b * 4, 30, 6, 5 + b * 4, BASE_GRAY, BASE_GRAY, false);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void generateEntranceWall(WorldGenLevel param1WorldGenLevel, RandomSource param1RandomSource, BoundingBox param1BoundingBox) {
/*  522 */       if (chunkIntersects(param1BoundingBox, 15, 20, 42, 21)) {
/*  523 */         generateBox(param1WorldGenLevel, param1BoundingBox, 15, 0, 21, 42, 0, 21, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  525 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 26, 1, 21, 31, 3, 21);
/*      */ 
/*      */ 
/*      */         
/*  529 */         generateBox(param1WorldGenLevel, param1BoundingBox, 21, 12, 21, 36, 12, 21, BASE_GRAY, BASE_GRAY, false);
/*  530 */         generateBox(param1WorldGenLevel, param1BoundingBox, 17, 11, 21, 40, 11, 21, BASE_GRAY, BASE_GRAY, false);
/*  531 */         generateBox(param1WorldGenLevel, param1BoundingBox, 16, 10, 21, 41, 10, 21, BASE_GRAY, BASE_GRAY, false);
/*  532 */         generateBox(param1WorldGenLevel, param1BoundingBox, 15, 7, 21, 42, 9, 21, BASE_GRAY, BASE_GRAY, false);
/*  533 */         generateBox(param1WorldGenLevel, param1BoundingBox, 16, 6, 21, 41, 6, 21, BASE_GRAY, BASE_GRAY, false);
/*  534 */         generateBox(param1WorldGenLevel, param1BoundingBox, 17, 5, 21, 40, 5, 21, BASE_GRAY, BASE_GRAY, false);
/*  535 */         generateBox(param1WorldGenLevel, param1BoundingBox, 21, 4, 21, 36, 4, 21, BASE_GRAY, BASE_GRAY, false);
/*  536 */         generateBox(param1WorldGenLevel, param1BoundingBox, 22, 3, 21, 26, 3, 21, BASE_GRAY, BASE_GRAY, false);
/*  537 */         generateBox(param1WorldGenLevel, param1BoundingBox, 31, 3, 21, 35, 3, 21, BASE_GRAY, BASE_GRAY, false);
/*  538 */         generateBox(param1WorldGenLevel, param1BoundingBox, 23, 2, 21, 25, 2, 21, BASE_GRAY, BASE_GRAY, false);
/*  539 */         generateBox(param1WorldGenLevel, param1BoundingBox, 32, 2, 21, 34, 2, 21, BASE_GRAY, BASE_GRAY, false);
/*      */ 
/*      */         
/*  542 */         generateBox(param1WorldGenLevel, param1BoundingBox, 28, 4, 20, 29, 4, 21, BASE_LIGHT, BASE_LIGHT, false);
/*  543 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 27, 3, 21, param1BoundingBox);
/*  544 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 30, 3, 21, param1BoundingBox);
/*  545 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 26, 2, 21, param1BoundingBox);
/*  546 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 31, 2, 21, param1BoundingBox);
/*  547 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 25, 1, 21, param1BoundingBox);
/*  548 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 32, 1, 21, param1BoundingBox); byte b;
/*  549 */         for (b = 0; b < 7; b++) {
/*  550 */           placeBlock(param1WorldGenLevel, BASE_BLACK, 28 - b, 6 + b, 21, param1BoundingBox);
/*  551 */           placeBlock(param1WorldGenLevel, BASE_BLACK, 29 + b, 6 + b, 21, param1BoundingBox);
/*      */         } 
/*  553 */         for (b = 0; b < 4; b++) {
/*  554 */           placeBlock(param1WorldGenLevel, BASE_BLACK, 28 - b, 9 + b, 21, param1BoundingBox);
/*  555 */           placeBlock(param1WorldGenLevel, BASE_BLACK, 29 + b, 9 + b, 21, param1BoundingBox);
/*      */         } 
/*  557 */         placeBlock(param1WorldGenLevel, BASE_BLACK, 28, 12, 21, param1BoundingBox);
/*  558 */         placeBlock(param1WorldGenLevel, BASE_BLACK, 29, 12, 21, param1BoundingBox);
/*  559 */         for (b = 0; b < 3; b++) {
/*  560 */           placeBlock(param1WorldGenLevel, BASE_BLACK, 22 - b * 2, 8, 21, param1BoundingBox);
/*  561 */           placeBlock(param1WorldGenLevel, BASE_BLACK, 22 - b * 2, 9, 21, param1BoundingBox);
/*      */           
/*  563 */           placeBlock(param1WorldGenLevel, BASE_BLACK, 35 + b * 2, 8, 21, param1BoundingBox);
/*  564 */           placeBlock(param1WorldGenLevel, BASE_BLACK, 35 + b * 2, 9, 21, param1BoundingBox);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  569 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 15, 13, 21, 42, 15, 21);
/*  570 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 15, 1, 21, 15, 6, 21);
/*  571 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 16, 1, 21, 16, 5, 21);
/*  572 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 17, 1, 21, 20, 4, 21);
/*  573 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 21, 1, 21, 21, 3, 21);
/*  574 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 22, 1, 21, 22, 2, 21);
/*  575 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 23, 1, 21, 24, 1, 21);
/*  576 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 42, 1, 21, 42, 6, 21);
/*  577 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 41, 1, 21, 41, 5, 21);
/*  578 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 37, 1, 21, 40, 4, 21);
/*  579 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 36, 1, 21, 36, 3, 21);
/*  580 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 33, 1, 21, 34, 1, 21);
/*  581 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 35, 1, 21, 35, 2, 21);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void generateRoofPiece(WorldGenLevel param1WorldGenLevel, RandomSource param1RandomSource, BoundingBox param1BoundingBox) {
/*  589 */       if (chunkIntersects(param1BoundingBox, 21, 21, 36, 36)) {
/*  590 */         generateBox(param1WorldGenLevel, param1BoundingBox, 21, 0, 22, 36, 0, 36, BASE_GRAY, BASE_GRAY, false);
/*      */ 
/*      */ 
/*      */         
/*  594 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 21, 1, 22, 36, 23, 36);
/*      */ 
/*      */         
/*  597 */         for (byte b = 0; b < 4; b++) {
/*  598 */           generateBox(param1WorldGenLevel, param1BoundingBox, 21 + b, 13 + b, 21 + b, 36 - b, 13 + b, 21 + b, BASE_LIGHT, BASE_LIGHT, false);
/*  599 */           generateBox(param1WorldGenLevel, param1BoundingBox, 21 + b, 13 + b, 36 - b, 36 - b, 13 + b, 36 - b, BASE_LIGHT, BASE_LIGHT, false);
/*  600 */           generateBox(param1WorldGenLevel, param1BoundingBox, 21 + b, 13 + b, 22 + b, 21 + b, 13 + b, 35 - b, BASE_LIGHT, BASE_LIGHT, false);
/*  601 */           generateBox(param1WorldGenLevel, param1BoundingBox, 36 - b, 13 + b, 22 + b, 36 - b, 13 + b, 35 - b, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  603 */         generateBox(param1WorldGenLevel, param1BoundingBox, 25, 16, 25, 32, 16, 32, BASE_GRAY, BASE_GRAY, false);
/*  604 */         generateBox(param1WorldGenLevel, param1BoundingBox, 25, 17, 25, 25, 19, 25, BASE_LIGHT, BASE_LIGHT, false);
/*  605 */         generateBox(param1WorldGenLevel, param1BoundingBox, 32, 17, 25, 32, 19, 25, BASE_LIGHT, BASE_LIGHT, false);
/*  606 */         generateBox(param1WorldGenLevel, param1BoundingBox, 25, 17, 32, 25, 19, 32, BASE_LIGHT, BASE_LIGHT, false);
/*  607 */         generateBox(param1WorldGenLevel, param1BoundingBox, 32, 17, 32, 32, 19, 32, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/*  609 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 26, 20, 26, param1BoundingBox);
/*  610 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 27, 21, 27, param1BoundingBox);
/*  611 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 27, 20, 27, param1BoundingBox);
/*  612 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 26, 20, 31, param1BoundingBox);
/*  613 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 27, 21, 30, param1BoundingBox);
/*  614 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 27, 20, 30, param1BoundingBox);
/*  615 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 31, 20, 31, param1BoundingBox);
/*  616 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 30, 21, 30, param1BoundingBox);
/*  617 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 30, 20, 30, param1BoundingBox);
/*  618 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 31, 20, 26, param1BoundingBox);
/*  619 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 30, 21, 27, param1BoundingBox);
/*  620 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 30, 20, 27, param1BoundingBox);
/*      */         
/*  622 */         generateBox(param1WorldGenLevel, param1BoundingBox, 28, 21, 27, 29, 21, 27, BASE_GRAY, BASE_GRAY, false);
/*  623 */         generateBox(param1WorldGenLevel, param1BoundingBox, 27, 21, 28, 27, 21, 29, BASE_GRAY, BASE_GRAY, false);
/*  624 */         generateBox(param1WorldGenLevel, param1BoundingBox, 28, 21, 30, 29, 21, 30, BASE_GRAY, BASE_GRAY, false);
/*  625 */         generateBox(param1WorldGenLevel, param1BoundingBox, 30, 21, 28, 30, 21, 29, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void generateLowerWall(WorldGenLevel param1WorldGenLevel, RandomSource param1RandomSource, BoundingBox param1BoundingBox) {
/*  632 */       if (chunkIntersects(param1BoundingBox, 0, 21, 6, 58)) {
/*  633 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 21, 6, 0, 57, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  635 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 21, 6, 7, 57);
/*      */ 
/*      */         
/*  638 */         generateBox(param1WorldGenLevel, param1BoundingBox, 4, 4, 21, 6, 4, 53, BASE_GRAY, BASE_GRAY, false); byte b;
/*  639 */         for (b = 0; b < 4; b++) {
/*  640 */           generateBox(param1WorldGenLevel, param1BoundingBox, b, b + 1, 21, b, b + 1, 57 - b, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  642 */         for (b = 23; b < 53; b += 3) {
/*  643 */           placeBlock(param1WorldGenLevel, DOT_DECO_DATA, 5, 5, b, param1BoundingBox);
/*      */         }
/*  645 */         placeBlock(param1WorldGenLevel, DOT_DECO_DATA, 5, 5, 52, param1BoundingBox);
/*      */         
/*  647 */         for (b = 0; b < 4; b++) {
/*  648 */           generateBox(param1WorldGenLevel, param1BoundingBox, b, b + 1, 21, b, b + 1, 57 - b, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*      */         
/*  651 */         generateBox(param1WorldGenLevel, param1BoundingBox, 4, 1, 52, 6, 3, 52, BASE_GRAY, BASE_GRAY, false);
/*  652 */         generateBox(param1WorldGenLevel, param1BoundingBox, 5, 1, 51, 5, 3, 53, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  657 */       if (chunkIntersects(param1BoundingBox, 51, 21, 58, 58)) {
/*  658 */         generateBox(param1WorldGenLevel, param1BoundingBox, 51, 0, 21, 57, 0, 57, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  660 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 51, 1, 21, 57, 7, 57);
/*      */ 
/*      */         
/*  663 */         generateBox(param1WorldGenLevel, param1BoundingBox, 51, 4, 21, 53, 4, 53, BASE_GRAY, BASE_GRAY, false); byte b;
/*  664 */         for (b = 0; b < 4; b++) {
/*  665 */           generateBox(param1WorldGenLevel, param1BoundingBox, 57 - b, b + 1, 21, 57 - b, b + 1, 57 - b, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  667 */         for (b = 23; b < 53; b += 3) {
/*  668 */           placeBlock(param1WorldGenLevel, DOT_DECO_DATA, 52, 5, b, param1BoundingBox);
/*      */         }
/*  670 */         placeBlock(param1WorldGenLevel, DOT_DECO_DATA, 52, 5, 52, param1BoundingBox);
/*      */ 
/*      */         
/*  673 */         generateBox(param1WorldGenLevel, param1BoundingBox, 51, 1, 52, 53, 3, 52, BASE_GRAY, BASE_GRAY, false);
/*  674 */         generateBox(param1WorldGenLevel, param1BoundingBox, 52, 1, 51, 52, 3, 53, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  679 */       if (chunkIntersects(param1BoundingBox, 0, 51, 57, 57)) {
/*  680 */         generateBox(param1WorldGenLevel, param1BoundingBox, 7, 0, 51, 50, 0, 57, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  682 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 51, 50, 10, 57);
/*      */ 
/*      */         
/*  685 */         for (byte b = 0; b < 4; b++) {
/*  686 */           generateBox(param1WorldGenLevel, param1BoundingBox, b + 1, b + 1, 57 - b, 56 - b, b + 1, 57 - b, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void generateMiddleWall(WorldGenLevel param1WorldGenLevel, RandomSource param1RandomSource, BoundingBox param1BoundingBox) {
/*  694 */       if (chunkIntersects(param1BoundingBox, 7, 21, 13, 50)) {
/*  695 */         generateBox(param1WorldGenLevel, param1BoundingBox, 7, 0, 21, 13, 0, 50, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  697 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 21, 13, 10, 50);
/*      */ 
/*      */         
/*  700 */         generateBox(param1WorldGenLevel, param1BoundingBox, 11, 8, 21, 13, 8, 53, BASE_GRAY, BASE_GRAY, false); byte b;
/*  701 */         for (b = 0; b < 4; b++) {
/*  702 */           generateBox(param1WorldGenLevel, param1BoundingBox, b + 7, b + 5, 21, b + 7, b + 5, 54, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  704 */         for (b = 21; b <= 45; b += 3) {
/*  705 */           placeBlock(param1WorldGenLevel, DOT_DECO_DATA, 12, 9, b, param1BoundingBox);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  711 */       if (chunkIntersects(param1BoundingBox, 44, 21, 50, 54)) {
/*  712 */         generateBox(param1WorldGenLevel, param1BoundingBox, 44, 0, 21, 50, 0, 50, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  714 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 44, 1, 21, 50, 10, 50);
/*      */ 
/*      */         
/*  717 */         generateBox(param1WorldGenLevel, param1BoundingBox, 44, 8, 21, 46, 8, 53, BASE_GRAY, BASE_GRAY, false); byte b;
/*  718 */         for (b = 0; b < 4; b++) {
/*  719 */           generateBox(param1WorldGenLevel, param1BoundingBox, 50 - b, b + 5, 21, 50 - b, b + 5, 54, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  721 */         for (b = 21; b <= 45; b += 3) {
/*  722 */           placeBlock(param1WorldGenLevel, DOT_DECO_DATA, 45, 9, b, param1BoundingBox);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  728 */       if (chunkIntersects(param1BoundingBox, 8, 44, 49, 54)) {
/*  729 */         generateBox(param1WorldGenLevel, param1BoundingBox, 14, 0, 44, 43, 0, 50, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  731 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 14, 1, 44, 43, 10, 50);
/*      */         
/*      */         byte b;
/*  734 */         for (b = 12; b <= 45; b += 3) {
/*  735 */           placeBlock(param1WorldGenLevel, DOT_DECO_DATA, b, 9, 45, param1BoundingBox);
/*  736 */           placeBlock(param1WorldGenLevel, DOT_DECO_DATA, b, 9, 52, param1BoundingBox);
/*  737 */           if (b == 12 || b == 18 || b == 24 || b == 33 || b == 39 || b == 45) {
/*  738 */             placeBlock(param1WorldGenLevel, DOT_DECO_DATA, b, 9, 47, param1BoundingBox);
/*  739 */             placeBlock(param1WorldGenLevel, DOT_DECO_DATA, b, 9, 50, param1BoundingBox);
/*  740 */             placeBlock(param1WorldGenLevel, DOT_DECO_DATA, b, 10, 45, param1BoundingBox);
/*  741 */             placeBlock(param1WorldGenLevel, DOT_DECO_DATA, b, 10, 46, param1BoundingBox);
/*  742 */             placeBlock(param1WorldGenLevel, DOT_DECO_DATA, b, 10, 51, param1BoundingBox);
/*  743 */             placeBlock(param1WorldGenLevel, DOT_DECO_DATA, b, 10, 52, param1BoundingBox);
/*  744 */             placeBlock(param1WorldGenLevel, DOT_DECO_DATA, b, 11, 47, param1BoundingBox);
/*  745 */             placeBlock(param1WorldGenLevel, DOT_DECO_DATA, b, 11, 50, param1BoundingBox);
/*  746 */             placeBlock(param1WorldGenLevel, DOT_DECO_DATA, b, 12, 48, param1BoundingBox);
/*  747 */             placeBlock(param1WorldGenLevel, DOT_DECO_DATA, b, 12, 49, param1BoundingBox);
/*      */           } 
/*      */         } 
/*      */         
/*  751 */         for (b = 0; b < 3; b++) {
/*  752 */           generateBox(param1WorldGenLevel, param1BoundingBox, 8 + b, 5 + b, 54, 49 - b, 5 + b, 54, BASE_GRAY, BASE_GRAY, false);
/*      */         }
/*  754 */         generateBox(param1WorldGenLevel, param1BoundingBox, 11, 8, 54, 46, 8, 54, BASE_LIGHT, BASE_LIGHT, false);
/*  755 */         generateBox(param1WorldGenLevel, param1BoundingBox, 14, 8, 44, 43, 8, 53, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void generateUpperWall(WorldGenLevel param1WorldGenLevel, RandomSource param1RandomSource, BoundingBox param1BoundingBox) {
/*  762 */       if (chunkIntersects(param1BoundingBox, 14, 21, 20, 43)) {
/*  763 */         generateBox(param1WorldGenLevel, param1BoundingBox, 14, 0, 21, 20, 0, 43, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  765 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 14, 1, 22, 20, 14, 43);
/*      */ 
/*      */         
/*  768 */         generateBox(param1WorldGenLevel, param1BoundingBox, 18, 12, 22, 20, 12, 39, BASE_GRAY, BASE_GRAY, false);
/*  769 */         generateBox(param1WorldGenLevel, param1BoundingBox, 18, 12, 21, 20, 12, 21, BASE_LIGHT, BASE_LIGHT, false); byte b;
/*  770 */         for (b = 0; b < 4; b++) {
/*  771 */           generateBox(param1WorldGenLevel, param1BoundingBox, b + 14, b + 9, 21, b + 14, b + 9, 43 - b, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  773 */         for (b = 23; b <= 39; b += 3) {
/*  774 */           placeBlock(param1WorldGenLevel, DOT_DECO_DATA, 19, 13, b, param1BoundingBox);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  780 */       if (chunkIntersects(param1BoundingBox, 37, 21, 43, 43)) {
/*  781 */         generateBox(param1WorldGenLevel, param1BoundingBox, 37, 0, 21, 43, 0, 43, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  783 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 37, 1, 22, 43, 14, 43);
/*      */ 
/*      */         
/*  786 */         generateBox(param1WorldGenLevel, param1BoundingBox, 37, 12, 22, 39, 12, 39, BASE_GRAY, BASE_GRAY, false);
/*  787 */         generateBox(param1WorldGenLevel, param1BoundingBox, 37, 12, 21, 39, 12, 21, BASE_LIGHT, BASE_LIGHT, false); byte b;
/*  788 */         for (b = 0; b < 4; b++) {
/*  789 */           generateBox(param1WorldGenLevel, param1BoundingBox, 43 - b, b + 9, 21, 43 - b, b + 9, 43 - b, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  791 */         for (b = 23; b <= 39; b += 3) {
/*  792 */           placeBlock(param1WorldGenLevel, DOT_DECO_DATA, 38, 13, b, param1BoundingBox);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  798 */       if (chunkIntersects(param1BoundingBox, 15, 37, 42, 43)) {
/*  799 */         generateBox(param1WorldGenLevel, param1BoundingBox, 21, 0, 37, 36, 0, 43, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  801 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 21, 1, 37, 36, 14, 43);
/*      */ 
/*      */         
/*  804 */         generateBox(param1WorldGenLevel, param1BoundingBox, 21, 12, 37, 36, 12, 39, BASE_GRAY, BASE_GRAY, false); byte b;
/*  805 */         for (b = 0; b < 4; b++) {
/*  806 */           generateBox(param1WorldGenLevel, param1BoundingBox, 15 + b, b + 9, 43 - b, 42 - b, b + 9, 43 - b, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  808 */         for (b = 21; b <= 36; b += 3)
/*  809 */           placeBlock(param1WorldGenLevel, DOT_DECO_DATA, b, 13, 38, param1BoundingBox); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentEntryRoom
/*      */     extends OceanMonumentPiece {
/*      */     public OceanMonumentEntryRoom(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition) {
/*  817 */       super(StructurePieceType.OCEAN_MONUMENT_ENTRY_ROOM, 1, param1Direction, param1RoomDefinition, 1, 1, 1);
/*      */     }
/*      */     
/*      */     public OceanMonumentEntryRoom(CompoundTag param1CompoundTag) {
/*  821 */       super(StructurePieceType.OCEAN_MONUMENT_ENTRY_ROOM, param1CompoundTag);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/*  827 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 0, 2, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  828 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 3, 0, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  829 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 1, 2, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  830 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 2, 0, 7, 2, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  831 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  832 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 0, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/*  835 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 7, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/*  838 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 0, 2, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  839 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 1, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/*  841 */       if (this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/*  842 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 7, 4, 2, 7);
/*      */       }
/*  844 */       if (this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/*  845 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 3, 1, 2, 4);
/*      */       }
/*  847 */       if (this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()])
/*  848 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 6, 1, 3, 7, 2, 4); 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentSimpleRoom
/*      */     extends OceanMonumentPiece {
/*      */     private int mainDesign;
/*      */     
/*      */     public OceanMonumentSimpleRoom(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition, RandomSource param1RandomSource) {
/*  857 */       super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_ROOM, 1, param1Direction, param1RoomDefinition, 1, 1, 1);
/*  858 */       this.mainDesign = param1RandomSource.nextInt(3);
/*      */     }
/*      */     
/*      */     public OceanMonumentSimpleRoom(CompoundTag param1CompoundTag) {
/*  862 */       super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_ROOM, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/*  867 */       if (this.roomDefinition.index / 25 > 0) {
/*  868 */         generateDefaultFloor(param1WorldGenLevel, param1BoundingBox, 0, 0, this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       }
/*  870 */       if (this.roomDefinition.connections[Direction.UP.get3DDataValue()] == null) {
/*  871 */         generateBoxOnFillOnly(param1WorldGenLevel, param1BoundingBox, 1, 4, 1, 6, 4, 6, BASE_GRAY);
/*      */       }
/*      */       
/*  874 */       boolean bool = (this.mainDesign != 0 && param1RandomSource.nextBoolean() && !this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()] && !this.roomDefinition.hasOpening[Direction.UP.get3DDataValue()] && this.roomDefinition.countOpenings() > 1) ? true : false;
/*      */       
/*  876 */       if (this.mainDesign == 0) {
/*      */         
/*  878 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 0, 2, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  879 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 0, 2, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  880 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 0, 2, 2, BASE_GRAY, BASE_GRAY, false);
/*  881 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 0, 2, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  882 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 1, 2, 1, param1BoundingBox);
/*      */ 
/*      */         
/*  885 */         generateBox(param1WorldGenLevel, param1BoundingBox, 5, 1, 0, 7, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  886 */         generateBox(param1WorldGenLevel, param1BoundingBox, 5, 3, 0, 7, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  887 */         generateBox(param1WorldGenLevel, param1BoundingBox, 7, 2, 0, 7, 2, 2, BASE_GRAY, BASE_GRAY, false);
/*  888 */         generateBox(param1WorldGenLevel, param1BoundingBox, 5, 2, 0, 6, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  889 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 6, 2, 1, param1BoundingBox);
/*      */ 
/*      */         
/*  892 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 5, 2, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  893 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 5, 2, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  894 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 5, 0, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  895 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 7, 2, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  896 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 1, 2, 6, param1BoundingBox);
/*      */ 
/*      */         
/*  899 */         generateBox(param1WorldGenLevel, param1BoundingBox, 5, 1, 5, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  900 */         generateBox(param1WorldGenLevel, param1BoundingBox, 5, 3, 5, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  901 */         generateBox(param1WorldGenLevel, param1BoundingBox, 7, 2, 5, 7, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  902 */         generateBox(param1WorldGenLevel, param1BoundingBox, 5, 2, 7, 6, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  903 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 6, 2, 6, param1BoundingBox);
/*      */         
/*  905 */         if (this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/*  906 */           generateBox(param1WorldGenLevel, param1BoundingBox, 3, 3, 0, 4, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/*  908 */           generateBox(param1WorldGenLevel, param1BoundingBox, 3, 3, 0, 4, 3, 1, BASE_LIGHT, BASE_LIGHT, false);
/*  909 */           generateBox(param1WorldGenLevel, param1BoundingBox, 3, 2, 0, 4, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  910 */           generateBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 0, 4, 1, 1, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  912 */         if (this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/*  913 */           generateBox(param1WorldGenLevel, param1BoundingBox, 3, 3, 7, 4, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/*  915 */           generateBox(param1WorldGenLevel, param1BoundingBox, 3, 3, 6, 4, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  916 */           generateBox(param1WorldGenLevel, param1BoundingBox, 3, 2, 7, 4, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  917 */           generateBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 6, 4, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  919 */         if (this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/*  920 */           generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 3, 0, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/*  922 */           generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 3, 1, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*  923 */           generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 3, 0, 2, 4, BASE_GRAY, BASE_GRAY, false);
/*  924 */           generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 3, 1, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  926 */         if (this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
/*  927 */           generateBox(param1WorldGenLevel, param1BoundingBox, 7, 3, 3, 7, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/*  929 */           generateBox(param1WorldGenLevel, param1BoundingBox, 6, 3, 3, 7, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*  930 */           generateBox(param1WorldGenLevel, param1BoundingBox, 7, 2, 3, 7, 2, 4, BASE_GRAY, BASE_GRAY, false);
/*  931 */           generateBox(param1WorldGenLevel, param1BoundingBox, 6, 1, 3, 7, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  933 */       } else if (this.mainDesign == 1) {
/*      */         
/*  935 */         generateBox(param1WorldGenLevel, param1BoundingBox, 2, 1, 2, 2, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  936 */         generateBox(param1WorldGenLevel, param1BoundingBox, 2, 1, 5, 2, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
/*  937 */         generateBox(param1WorldGenLevel, param1BoundingBox, 5, 1, 5, 5, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
/*  938 */         generateBox(param1WorldGenLevel, param1BoundingBox, 5, 1, 2, 5, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  939 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 2, 2, 2, param1BoundingBox);
/*  940 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 2, 2, 5, param1BoundingBox);
/*  941 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 5, 2, 5, param1BoundingBox);
/*  942 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 5, 2, 2, param1BoundingBox);
/*      */ 
/*      */         
/*  945 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 0, 1, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  946 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 1, 0, 3, 1, BASE_LIGHT, BASE_LIGHT, false);
/*  947 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 7, 1, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  948 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 6, 0, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  949 */         generateBox(param1WorldGenLevel, param1BoundingBox, 6, 1, 7, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  950 */         generateBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 6, 7, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  951 */         generateBox(param1WorldGenLevel, param1BoundingBox, 6, 1, 0, 7, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  952 */         generateBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 1, 7, 3, 1, BASE_LIGHT, BASE_LIGHT, false);
/*  953 */         placeBlock(param1WorldGenLevel, BASE_GRAY, 1, 2, 0, param1BoundingBox);
/*  954 */         placeBlock(param1WorldGenLevel, BASE_GRAY, 0, 2, 1, param1BoundingBox);
/*  955 */         placeBlock(param1WorldGenLevel, BASE_GRAY, 1, 2, 7, param1BoundingBox);
/*  956 */         placeBlock(param1WorldGenLevel, BASE_GRAY, 0, 2, 6, param1BoundingBox);
/*  957 */         placeBlock(param1WorldGenLevel, BASE_GRAY, 6, 2, 7, param1BoundingBox);
/*  958 */         placeBlock(param1WorldGenLevel, BASE_GRAY, 7, 2, 6, param1BoundingBox);
/*  959 */         placeBlock(param1WorldGenLevel, BASE_GRAY, 6, 2, 0, param1BoundingBox);
/*  960 */         placeBlock(param1WorldGenLevel, BASE_GRAY, 7, 2, 1, param1BoundingBox);
/*  961 */         if (!this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/*  962 */           generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  963 */           generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 0, 6, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  964 */           generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 0, 6, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  966 */         if (!this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/*  967 */           generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 7, 6, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  968 */           generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 7, 6, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  969 */           generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 7, 6, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  971 */         if (!this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/*  972 */           generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 1, 0, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  973 */           generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 1, 0, 2, 6, BASE_GRAY, BASE_GRAY, false);
/*  974 */           generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 1, 0, 1, 6, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  976 */         if (!this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
/*  977 */           generateBox(param1WorldGenLevel, param1BoundingBox, 7, 3, 1, 7, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  978 */           generateBox(param1WorldGenLevel, param1BoundingBox, 7, 2, 1, 7, 2, 6, BASE_GRAY, BASE_GRAY, false);
/*  979 */           generateBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 1, 7, 1, 6, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  981 */       } else if (this.mainDesign == 2) {
/*  982 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  983 */         generateBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 0, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  984 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 0, 6, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  985 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 7, 6, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/*  987 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 0, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*  988 */         generateBox(param1WorldGenLevel, param1BoundingBox, 7, 2, 0, 7, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*  989 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 0, 6, 2, 0, BASE_BLACK, BASE_BLACK, false);
/*  990 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 7, 6, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*      */         
/*  992 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 0, 0, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  993 */         generateBox(param1WorldGenLevel, param1BoundingBox, 7, 3, 0, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  994 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  995 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 7, 6, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/*  997 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 3, 0, 2, 4, BASE_BLACK, BASE_BLACK, false);
/*  998 */         generateBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 3, 7, 2, 4, BASE_BLACK, BASE_BLACK, false);
/*  999 */         generateBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 0, 4, 2, 0, BASE_BLACK, BASE_BLACK, false);
/* 1000 */         generateBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 7, 4, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*      */         
/* 1002 */         if (this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1003 */           generateWaterBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 0, 4, 2, 0);
/*      */         }
/* 1005 */         if (this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1006 */           generateWaterBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 7, 4, 2, 7);
/*      */         }
/* 1008 */         if (this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1009 */           generateWaterBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 3, 0, 2, 4);
/*      */         }
/* 1011 */         if (this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1012 */           generateWaterBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 3, 7, 2, 4);
/*      */         }
/*      */       } 
/* 1015 */       if (bool) {
/* 1016 */         generateBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 3, 4, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1017 */         generateBox(param1WorldGenLevel, param1BoundingBox, 3, 2, 3, 4, 2, 4, BASE_GRAY, BASE_GRAY, false);
/* 1018 */         generateBox(param1WorldGenLevel, param1BoundingBox, 3, 3, 3, 4, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentSimpleTopRoom extends OceanMonumentPiece {
/*      */     public OceanMonumentSimpleTopRoom(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition) {
/* 1025 */       super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_TOP_ROOM, 1, param1Direction, param1RoomDefinition, 1, 1, 1);
/*      */     }
/*      */     
/*      */     public OceanMonumentSimpleTopRoom(CompoundTag param1CompoundTag) {
/* 1029 */       super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_TOP_ROOM, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 1034 */       if (this.roomDefinition.index / 25 > 0) {
/* 1035 */         generateDefaultFloor(param1WorldGenLevel, param1BoundingBox, 0, 0, this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       }
/* 1037 */       if (this.roomDefinition.connections[Direction.UP.get3DDataValue()] == null) {
/* 1038 */         generateBoxOnFillOnly(param1WorldGenLevel, param1BoundingBox, 1, 4, 1, 6, 4, 6, BASE_GRAY);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1043 */       for (byte b = 1; b <= 6; b++) {
/* 1044 */         for (byte b1 = 1; b1 <= 6; b1++) {
/* 1045 */           if (param1RandomSource.nextInt(3) != 0) {
/* 1046 */             int i = 2 + ((param1RandomSource.nextInt(4) == 0) ? 0 : 1);
/* 1047 */             BlockState blockState = Blocks.WET_SPONGE.defaultBlockState();
/* 1048 */             generateBox(param1WorldGenLevel, param1BoundingBox, b, i, b1, b, 3, b1, blockState, blockState, false);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1053 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1054 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 0, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1055 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 0, 6, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1056 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 7, 6, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1058 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 0, 2, 7, BASE_BLACK, BASE_BLACK, false);
/* 1059 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 2, 0, 7, 2, 7, BASE_BLACK, BASE_BLACK, false);
/* 1060 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 0, 6, 2, 0, BASE_BLACK, BASE_BLACK, false);
/* 1061 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 7, 6, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*      */       
/* 1063 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 0, 0, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1064 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 3, 0, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1065 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1066 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 7, 6, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1068 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 3, 0, 2, 4, BASE_BLACK, BASE_BLACK, false);
/* 1069 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 3, 7, 2, 4, BASE_BLACK, BASE_BLACK, false);
/* 1070 */       generateBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 0, 4, 2, 0, BASE_BLACK, BASE_BLACK, false);
/* 1071 */       generateBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 7, 4, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*      */       
/* 1073 */       if (this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()])
/* 1074 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 0, 4, 2, 0); 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentDoubleYRoom
/*      */     extends OceanMonumentPiece
/*      */   {
/*      */     public OceanMonumentDoubleYRoom(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition) {
/* 1082 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Y_ROOM, 1, param1Direction, param1RoomDefinition, 1, 2, 1);
/*      */     }
/*      */     
/*      */     public OceanMonumentDoubleYRoom(CompoundTag param1CompoundTag) {
/* 1086 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Y_ROOM, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 1091 */       if (this.roomDefinition.index / 25 > 0) {
/* 1092 */         generateDefaultFloor(param1WorldGenLevel, param1BoundingBox, 0, 0, this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       }
/* 1094 */       OceanMonumentPieces.RoomDefinition roomDefinition1 = this.roomDefinition.connections[Direction.UP.get3DDataValue()];
/* 1095 */       if (roomDefinition1.connections[Direction.UP.get3DDataValue()] == null) {
/* 1096 */         generateBoxOnFillOnly(param1WorldGenLevel, param1BoundingBox, 1, 8, 1, 6, 8, 6, BASE_GRAY);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1101 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 4, 0, 0, 4, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1102 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 4, 0, 7, 4, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1103 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 4, 0, 6, 4, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1104 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 4, 7, 6, 4, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1106 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 4, 1, 2, 4, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1107 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 4, 2, 1, 4, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1108 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 4, 1, 5, 4, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1109 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 4, 2, 6, 4, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1110 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 4, 5, 2, 4, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1111 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 4, 5, 1, 4, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1112 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 4, 5, 5, 4, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1113 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 4, 5, 6, 4, 5, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1115 */       OceanMonumentPieces.RoomDefinition roomDefinition2 = this.roomDefinition;
/* 1116 */       for (byte b = 1; b <= 5; b += 4) {
/* 1117 */         byte b1 = 0;
/* 1118 */         if (roomDefinition2.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1119 */           generateBox(param1WorldGenLevel, param1BoundingBox, 2, b, b1, 2, b + 2, b1, BASE_LIGHT, BASE_LIGHT, false);
/* 1120 */           generateBox(param1WorldGenLevel, param1BoundingBox, 5, b, b1, 5, b + 2, b1, BASE_LIGHT, BASE_LIGHT, false);
/* 1121 */           generateBox(param1WorldGenLevel, param1BoundingBox, 3, b + 2, b1, 4, b + 2, b1, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/* 1123 */           generateBox(param1WorldGenLevel, param1BoundingBox, 0, b, b1, 7, b + 2, b1, BASE_LIGHT, BASE_LIGHT, false);
/* 1124 */           generateBox(param1WorldGenLevel, param1BoundingBox, 0, b + 1, b1, 7, b + 1, b1, BASE_GRAY, BASE_GRAY, false);
/*      */         } 
/* 1126 */         b1 = 7;
/* 1127 */         if (roomDefinition2.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1128 */           generateBox(param1WorldGenLevel, param1BoundingBox, 2, b, b1, 2, b + 2, b1, BASE_LIGHT, BASE_LIGHT, false);
/* 1129 */           generateBox(param1WorldGenLevel, param1BoundingBox, 5, b, b1, 5, b + 2, b1, BASE_LIGHT, BASE_LIGHT, false);
/* 1130 */           generateBox(param1WorldGenLevel, param1BoundingBox, 3, b + 2, b1, 4, b + 2, b1, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/* 1132 */           generateBox(param1WorldGenLevel, param1BoundingBox, 0, b, b1, 7, b + 2, b1, BASE_LIGHT, BASE_LIGHT, false);
/* 1133 */           generateBox(param1WorldGenLevel, param1BoundingBox, 0, b + 1, b1, 7, b + 1, b1, BASE_GRAY, BASE_GRAY, false);
/*      */         } 
/* 1135 */         byte b2 = 0;
/* 1136 */         if (roomDefinition2.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1137 */           generateBox(param1WorldGenLevel, param1BoundingBox, b2, b, 2, b2, b + 2, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1138 */           generateBox(param1WorldGenLevel, param1BoundingBox, b2, b, 5, b2, b + 2, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1139 */           generateBox(param1WorldGenLevel, param1BoundingBox, b2, b + 2, 3, b2, b + 2, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/* 1141 */           generateBox(param1WorldGenLevel, param1BoundingBox, b2, b, 0, b2, b + 2, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1142 */           generateBox(param1WorldGenLevel, param1BoundingBox, b2, b + 1, 0, b2, b + 1, 7, BASE_GRAY, BASE_GRAY, false);
/*      */         } 
/* 1144 */         b2 = 7;
/* 1145 */         if (roomDefinition2.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1146 */           generateBox(param1WorldGenLevel, param1BoundingBox, b2, b, 2, b2, b + 2, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1147 */           generateBox(param1WorldGenLevel, param1BoundingBox, b2, b, 5, b2, b + 2, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1148 */           generateBox(param1WorldGenLevel, param1BoundingBox, b2, b + 2, 3, b2, b + 2, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/* 1150 */           generateBox(param1WorldGenLevel, param1BoundingBox, b2, b, 0, b2, b + 2, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1151 */           generateBox(param1WorldGenLevel, param1BoundingBox, b2, b + 1, 0, b2, b + 1, 7, BASE_GRAY, BASE_GRAY, false);
/*      */         } 
/* 1153 */         roomDefinition2 = roomDefinition1;
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentDoubleXRoom
/*      */     extends OceanMonumentPiece {
/*      */     public OceanMonumentDoubleXRoom(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition) {
/* 1161 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_X_ROOM, 1, param1Direction, param1RoomDefinition, 2, 1, 1);
/*      */     }
/*      */     
/*      */     public OceanMonumentDoubleXRoom(CompoundTag param1CompoundTag) {
/* 1165 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_X_ROOM, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 1170 */       OceanMonumentPieces.RoomDefinition roomDefinition1 = this.roomDefinition.connections[Direction.EAST.get3DDataValue()];
/* 1171 */       OceanMonumentPieces.RoomDefinition roomDefinition2 = this.roomDefinition;
/* 1172 */       if (this.roomDefinition.index / 25 > 0) {
/* 1173 */         generateDefaultFloor(param1WorldGenLevel, param1BoundingBox, 8, 0, roomDefinition1.hasOpening[Direction.DOWN.get3DDataValue()]);
/* 1174 */         generateDefaultFloor(param1WorldGenLevel, param1BoundingBox, 0, 0, roomDefinition2.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       } 
/* 1176 */       if (roomDefinition2.connections[Direction.UP.get3DDataValue()] == null) {
/* 1177 */         generateBoxOnFillOnly(param1WorldGenLevel, param1BoundingBox, 1, 4, 1, 7, 4, 6, BASE_GRAY);
/*      */       }
/* 1179 */       if (roomDefinition1.connections[Direction.UP.get3DDataValue()] == null) {
/* 1180 */         generateBoxOnFillOnly(param1WorldGenLevel, param1BoundingBox, 8, 4, 1, 14, 4, 6, BASE_GRAY);
/*      */       }
/*      */ 
/*      */       
/* 1184 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 0, 0, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1185 */       generateBox(param1WorldGenLevel, param1BoundingBox, 15, 3, 0, 15, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1186 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 0, 15, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1187 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 7, 14, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1188 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 0, 2, 7, BASE_GRAY, BASE_GRAY, false);
/* 1189 */       generateBox(param1WorldGenLevel, param1BoundingBox, 15, 2, 0, 15, 2, 7, BASE_GRAY, BASE_GRAY, false);
/* 1190 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 0, 15, 2, 0, BASE_GRAY, BASE_GRAY, false);
/* 1191 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 7, 14, 2, 7, BASE_GRAY, BASE_GRAY, false);
/* 1192 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1193 */       generateBox(param1WorldGenLevel, param1BoundingBox, 15, 1, 0, 15, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1194 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 0, 15, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1195 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 7, 14, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/* 1198 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 1, 0, 10, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1199 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 2, 0, 9, 2, 3, BASE_GRAY, BASE_GRAY, false);
/* 1200 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 3, 0, 10, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1202 */       placeBlock(param1WorldGenLevel, LAMP_BLOCK, 6, 2, 3, param1BoundingBox);
/* 1203 */       placeBlock(param1WorldGenLevel, LAMP_BLOCK, 9, 2, 3, param1BoundingBox);
/*      */ 
/*      */       
/* 1206 */       if (roomDefinition2.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1207 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 0, 4, 2, 0);
/*      */       }
/* 1209 */       if (roomDefinition2.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1210 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 7, 4, 2, 7);
/*      */       }
/* 1212 */       if (roomDefinition2.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1213 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 3, 0, 2, 4);
/*      */       }
/* 1215 */       if (roomDefinition1.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1216 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 11, 1, 0, 12, 2, 0);
/*      */       }
/* 1218 */       if (roomDefinition1.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1219 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 11, 1, 7, 12, 2, 7);
/*      */       }
/* 1221 */       if (roomDefinition1.hasOpening[Direction.EAST.get3DDataValue()])
/* 1222 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 15, 1, 3, 15, 2, 4); 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentDoubleZRoom
/*      */     extends OceanMonumentPiece {
/*      */     public OceanMonumentDoubleZRoom(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition) {
/* 1229 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Z_ROOM, 1, param1Direction, param1RoomDefinition, 1, 1, 2);
/*      */     }
/*      */     
/*      */     public OceanMonumentDoubleZRoom(CompoundTag param1CompoundTag) {
/* 1233 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Z_ROOM, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 1238 */       OceanMonumentPieces.RoomDefinition roomDefinition1 = this.roomDefinition.connections[Direction.NORTH.get3DDataValue()];
/* 1239 */       OceanMonumentPieces.RoomDefinition roomDefinition2 = this.roomDefinition;
/* 1240 */       if (this.roomDefinition.index / 25 > 0) {
/* 1241 */         generateDefaultFloor(param1WorldGenLevel, param1BoundingBox, 0, 8, roomDefinition1.hasOpening[Direction.DOWN.get3DDataValue()]);
/* 1242 */         generateDefaultFloor(param1WorldGenLevel, param1BoundingBox, 0, 0, roomDefinition2.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       } 
/* 1244 */       if (roomDefinition2.connections[Direction.UP.get3DDataValue()] == null) {
/* 1245 */         generateBoxOnFillOnly(param1WorldGenLevel, param1BoundingBox, 1, 4, 1, 6, 4, 7, BASE_GRAY);
/*      */       }
/* 1247 */       if (roomDefinition1.connections[Direction.UP.get3DDataValue()] == null) {
/* 1248 */         generateBoxOnFillOnly(param1WorldGenLevel, param1BoundingBox, 1, 4, 8, 6, 4, 14, BASE_GRAY);
/*      */       }
/*      */ 
/*      */       
/* 1252 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 0, 0, 3, 15, BASE_LIGHT, BASE_LIGHT, false);
/* 1253 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 3, 0, 7, 3, 15, BASE_LIGHT, BASE_LIGHT, false);
/* 1254 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 0, 7, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1255 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 15, 6, 3, 15, BASE_LIGHT, BASE_LIGHT, false);
/* 1256 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 0, 2, 15, BASE_GRAY, BASE_GRAY, false);
/* 1257 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 2, 0, 7, 2, 15, BASE_GRAY, BASE_GRAY, false);
/* 1258 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 0, 7, 2, 0, BASE_GRAY, BASE_GRAY, false);
/* 1259 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 15, 6, 2, 15, BASE_GRAY, BASE_GRAY, false);
/* 1260 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 0, 0, 1, 15, BASE_LIGHT, BASE_LIGHT, false);
/* 1261 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 0, 7, 1, 15, BASE_LIGHT, BASE_LIGHT, false);
/* 1262 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 0, 7, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1263 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 15, 6, 1, 15, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/* 1266 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 1, 1, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1267 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 1, 1, 6, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1268 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 1, 1, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1269 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 3, 1, 6, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1270 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 13, 1, 1, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1271 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 1, 13, 6, 1, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1272 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 13, 1, 3, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1273 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 3, 13, 6, 3, 14, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/* 1276 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 1, 6, 2, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1277 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 1, 6, 5, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1278 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 1, 9, 2, 3, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1279 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 1, 9, 5, 3, 9, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1281 */       generateBox(param1WorldGenLevel, param1BoundingBox, 3, 2, 6, 4, 2, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1282 */       generateBox(param1WorldGenLevel, param1BoundingBox, 3, 2, 9, 4, 2, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1283 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 2, 7, 2, 2, 8, BASE_LIGHT, BASE_LIGHT, false);
/* 1284 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 2, 7, 5, 2, 8, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1286 */       placeBlock(param1WorldGenLevel, LAMP_BLOCK, 2, 2, 5, param1BoundingBox);
/* 1287 */       placeBlock(param1WorldGenLevel, LAMP_BLOCK, 5, 2, 5, param1BoundingBox);
/* 1288 */       placeBlock(param1WorldGenLevel, LAMP_BLOCK, 2, 2, 10, param1BoundingBox);
/* 1289 */       placeBlock(param1WorldGenLevel, LAMP_BLOCK, 5, 2, 10, param1BoundingBox);
/* 1290 */       placeBlock(param1WorldGenLevel, BASE_LIGHT, 2, 3, 5, param1BoundingBox);
/* 1291 */       placeBlock(param1WorldGenLevel, BASE_LIGHT, 5, 3, 5, param1BoundingBox);
/* 1292 */       placeBlock(param1WorldGenLevel, BASE_LIGHT, 2, 3, 10, param1BoundingBox);
/* 1293 */       placeBlock(param1WorldGenLevel, BASE_LIGHT, 5, 3, 10, param1BoundingBox);
/*      */ 
/*      */       
/* 1296 */       if (roomDefinition2.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1297 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 0, 4, 2, 0);
/*      */       }
/* 1299 */       if (roomDefinition2.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1300 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 3, 7, 2, 4);
/*      */       }
/* 1302 */       if (roomDefinition2.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1303 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 3, 0, 2, 4);
/*      */       }
/* 1305 */       if (roomDefinition1.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1306 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 15, 4, 2, 15);
/*      */       }
/* 1308 */       if (roomDefinition1.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1309 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 11, 0, 2, 12);
/*      */       }
/* 1311 */       if (roomDefinition1.hasOpening[Direction.EAST.get3DDataValue()])
/* 1312 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 11, 7, 2, 12); 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentDoubleXYRoom
/*      */     extends OceanMonumentPiece {
/*      */     public OceanMonumentDoubleXYRoom(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition) {
/* 1319 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_XY_ROOM, 1, param1Direction, param1RoomDefinition, 2, 2, 1);
/*      */     }
/*      */     
/*      */     public OceanMonumentDoubleXYRoom(CompoundTag param1CompoundTag) {
/* 1323 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_XY_ROOM, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 1328 */       OceanMonumentPieces.RoomDefinition roomDefinition1 = this.roomDefinition.connections[Direction.EAST.get3DDataValue()];
/* 1329 */       OceanMonumentPieces.RoomDefinition roomDefinition2 = this.roomDefinition;
/* 1330 */       OceanMonumentPieces.RoomDefinition roomDefinition3 = roomDefinition2.connections[Direction.UP.get3DDataValue()];
/* 1331 */       OceanMonumentPieces.RoomDefinition roomDefinition4 = roomDefinition1.connections[Direction.UP.get3DDataValue()];
/*      */       
/* 1333 */       if (this.roomDefinition.index / 25 > 0) {
/* 1334 */         generateDefaultFloor(param1WorldGenLevel, param1BoundingBox, 8, 0, roomDefinition1.hasOpening[Direction.DOWN.get3DDataValue()]);
/* 1335 */         generateDefaultFloor(param1WorldGenLevel, param1BoundingBox, 0, 0, roomDefinition2.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       } 
/* 1337 */       if (roomDefinition3.connections[Direction.UP.get3DDataValue()] == null) {
/* 1338 */         generateBoxOnFillOnly(param1WorldGenLevel, param1BoundingBox, 1, 8, 1, 7, 8, 6, BASE_GRAY);
/*      */       }
/* 1340 */       if (roomDefinition4.connections[Direction.UP.get3DDataValue()] == null) {
/* 1341 */         generateBoxOnFillOnly(param1WorldGenLevel, param1BoundingBox, 8, 8, 1, 14, 8, 6, BASE_GRAY);
/*      */       }
/*      */ 
/*      */       
/* 1345 */       for (byte b = 1; b <= 7; b++) {
/* 1346 */         BlockState blockState = BASE_LIGHT;
/* 1347 */         if (b == 2 || b == 6) {
/* 1348 */           blockState = BASE_GRAY;
/*      */         }
/* 1350 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, b, 0, 0, b, 7, blockState, blockState, false);
/* 1351 */         generateBox(param1WorldGenLevel, param1BoundingBox, 15, b, 0, 15, b, 7, blockState, blockState, false);
/* 1352 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, b, 0, 15, b, 0, blockState, blockState, false);
/* 1353 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, b, 7, 14, b, 7, blockState, blockState, false);
/*      */       } 
/*      */ 
/*      */       
/* 1357 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 1, 3, 2, 7, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1358 */       generateBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 2, 4, 7, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1359 */       generateBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 5, 4, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1360 */       generateBox(param1WorldGenLevel, param1BoundingBox, 13, 1, 3, 13, 7, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1361 */       generateBox(param1WorldGenLevel, param1BoundingBox, 11, 1, 2, 12, 7, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1362 */       generateBox(param1WorldGenLevel, param1BoundingBox, 11, 1, 5, 12, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1364 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 1, 3, 5, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1365 */       generateBox(param1WorldGenLevel, param1BoundingBox, 10, 1, 3, 10, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1367 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 7, 2, 10, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1368 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 5, 2, 5, 7, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1369 */       generateBox(param1WorldGenLevel, param1BoundingBox, 10, 5, 2, 10, 7, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1370 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 5, 5, 5, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1371 */       generateBox(param1WorldGenLevel, param1BoundingBox, 10, 5, 5, 10, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1372 */       placeBlock(param1WorldGenLevel, BASE_LIGHT, 6, 6, 2, param1BoundingBox);
/* 1373 */       placeBlock(param1WorldGenLevel, BASE_LIGHT, 9, 6, 2, param1BoundingBox);
/* 1374 */       placeBlock(param1WorldGenLevel, BASE_LIGHT, 6, 6, 5, param1BoundingBox);
/* 1375 */       placeBlock(param1WorldGenLevel, BASE_LIGHT, 9, 6, 5, param1BoundingBox);
/*      */       
/* 1377 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 4, 3, 6, 4, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1378 */       generateBox(param1WorldGenLevel, param1BoundingBox, 9, 4, 3, 10, 4, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1379 */       placeBlock(param1WorldGenLevel, LAMP_BLOCK, 5, 4, 2, param1BoundingBox);
/* 1380 */       placeBlock(param1WorldGenLevel, LAMP_BLOCK, 5, 4, 5, param1BoundingBox);
/* 1381 */       placeBlock(param1WorldGenLevel, LAMP_BLOCK, 10, 4, 2, param1BoundingBox);
/* 1382 */       placeBlock(param1WorldGenLevel, LAMP_BLOCK, 10, 4, 5, param1BoundingBox);
/*      */ 
/*      */       
/* 1385 */       if (roomDefinition2.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1386 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 0, 4, 2, 0);
/*      */       }
/* 1388 */       if (roomDefinition2.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1389 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 7, 4, 2, 7);
/*      */       }
/* 1391 */       if (roomDefinition2.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1392 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 3, 0, 2, 4);
/*      */       }
/* 1394 */       if (roomDefinition1.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1395 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 11, 1, 0, 12, 2, 0);
/*      */       }
/* 1397 */       if (roomDefinition1.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1398 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 11, 1, 7, 12, 2, 7);
/*      */       }
/* 1400 */       if (roomDefinition1.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1401 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 15, 1, 3, 15, 2, 4);
/*      */       }
/* 1403 */       if (roomDefinition3.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1404 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 3, 5, 0, 4, 6, 0);
/*      */       }
/* 1406 */       if (roomDefinition3.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1407 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 3, 5, 7, 4, 6, 7);
/*      */       }
/* 1409 */       if (roomDefinition3.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1410 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 0, 5, 3, 0, 6, 4);
/*      */       }
/* 1412 */       if (roomDefinition4.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1413 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 11, 5, 0, 12, 6, 0);
/*      */       }
/* 1415 */       if (roomDefinition4.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1416 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 11, 5, 7, 12, 6, 7);
/*      */       }
/* 1418 */       if (roomDefinition4.hasOpening[Direction.EAST.get3DDataValue()])
/* 1419 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 15, 5, 3, 15, 6, 4); 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentDoubleYZRoom
/*      */     extends OceanMonumentPiece {
/*      */     public OceanMonumentDoubleYZRoom(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition) {
/* 1426 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_YZ_ROOM, 1, param1Direction, param1RoomDefinition, 1, 2, 2);
/*      */     }
/*      */     
/*      */     public OceanMonumentDoubleYZRoom(CompoundTag param1CompoundTag) {
/* 1430 */       super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_YZ_ROOM, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 1435 */       OceanMonumentPieces.RoomDefinition roomDefinition1 = this.roomDefinition.connections[Direction.NORTH.get3DDataValue()];
/* 1436 */       OceanMonumentPieces.RoomDefinition roomDefinition2 = this.roomDefinition;
/* 1437 */       OceanMonumentPieces.RoomDefinition roomDefinition3 = roomDefinition1.connections[Direction.UP.get3DDataValue()];
/* 1438 */       OceanMonumentPieces.RoomDefinition roomDefinition4 = roomDefinition2.connections[Direction.UP.get3DDataValue()];
/* 1439 */       if (this.roomDefinition.index / 25 > 0) {
/* 1440 */         generateDefaultFloor(param1WorldGenLevel, param1BoundingBox, 0, 8, roomDefinition1.hasOpening[Direction.DOWN.get3DDataValue()]);
/* 1441 */         generateDefaultFloor(param1WorldGenLevel, param1BoundingBox, 0, 0, roomDefinition2.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       } 
/* 1443 */       if (roomDefinition4.connections[Direction.UP.get3DDataValue()] == null) {
/* 1444 */         generateBoxOnFillOnly(param1WorldGenLevel, param1BoundingBox, 1, 8, 1, 6, 8, 7, BASE_GRAY);
/*      */       }
/* 1446 */       if (roomDefinition3.connections[Direction.UP.get3DDataValue()] == null) {
/* 1447 */         generateBoxOnFillOnly(param1WorldGenLevel, param1BoundingBox, 1, 8, 8, 6, 8, 14, BASE_GRAY);
/*      */       }
/*      */       
/*      */       byte b;
/* 1451 */       for (b = 1; b <= 7; b++) {
/* 1452 */         BlockState blockState = BASE_LIGHT;
/* 1453 */         if (b == 2 || b == 6) {
/* 1454 */           blockState = BASE_GRAY;
/*      */         }
/* 1456 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, b, 0, 0, b, 15, blockState, blockState, false);
/* 1457 */         generateBox(param1WorldGenLevel, param1BoundingBox, 7, b, 0, 7, b, 15, blockState, blockState, false);
/* 1458 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, b, 0, 6, b, 0, blockState, blockState, false);
/* 1459 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, b, 15, 6, b, 15, blockState, blockState, false);
/*      */       } 
/*      */ 
/*      */       
/* 1463 */       for (b = 1; b <= 7; b++) {
/* 1464 */         BlockState blockState = BASE_BLACK;
/* 1465 */         if (b == 2 || b == 6) {
/* 1466 */           blockState = LAMP_BLOCK;
/*      */         }
/* 1468 */         generateBox(param1WorldGenLevel, param1BoundingBox, 3, b, 7, 4, b, 8, blockState, blockState, false);
/*      */       } 
/*      */ 
/*      */       
/* 1472 */       if (roomDefinition2.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1473 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 0, 4, 2, 0);
/*      */       }
/* 1475 */       if (roomDefinition2.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1476 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 3, 7, 2, 4);
/*      */       }
/* 1478 */       if (roomDefinition2.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1479 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 3, 0, 2, 4);
/*      */       }
/* 1481 */       if (roomDefinition1.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1482 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 15, 4, 2, 15);
/*      */       }
/* 1484 */       if (roomDefinition1.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1485 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 11, 0, 2, 12);
/*      */       }
/* 1487 */       if (roomDefinition1.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1488 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 11, 7, 2, 12);
/*      */       }
/*      */       
/* 1491 */       if (roomDefinition4.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1492 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 3, 5, 0, 4, 6, 0);
/*      */       }
/* 1494 */       if (roomDefinition4.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1495 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 7, 5, 3, 7, 6, 4);
/* 1496 */         generateBox(param1WorldGenLevel, param1BoundingBox, 5, 4, 2, 6, 4, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1497 */         generateBox(param1WorldGenLevel, param1BoundingBox, 6, 1, 2, 6, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1498 */         generateBox(param1WorldGenLevel, param1BoundingBox, 6, 1, 5, 6, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/* 1500 */       if (roomDefinition4.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1501 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 0, 5, 3, 0, 6, 4);
/* 1502 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, 4, 2, 2, 4, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1503 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 2, 1, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1504 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 5, 1, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/* 1506 */       if (roomDefinition3.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1507 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 3, 5, 15, 4, 6, 15);
/*      */       }
/* 1509 */       if (roomDefinition3.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1510 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 0, 5, 11, 0, 6, 12);
/* 1511 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, 4, 10, 2, 4, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1512 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 10, 1, 3, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1513 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 13, 1, 3, 13, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/* 1515 */       if (roomDefinition3.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1516 */         generateWaterBox(param1WorldGenLevel, param1BoundingBox, 7, 5, 11, 7, 6, 12);
/* 1517 */         generateBox(param1WorldGenLevel, param1BoundingBox, 5, 4, 10, 6, 4, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1518 */         generateBox(param1WorldGenLevel, param1BoundingBox, 6, 1, 10, 6, 3, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1519 */         generateBox(param1WorldGenLevel, param1BoundingBox, 6, 1, 13, 6, 3, 13, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentCoreRoom extends OceanMonumentPiece {
/*      */     public OceanMonumentCoreRoom(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition) {
/* 1526 */       super(StructurePieceType.OCEAN_MONUMENT_CORE_ROOM, 1, param1Direction, param1RoomDefinition, 2, 2, 2);
/*      */     }
/*      */     
/*      */     public OceanMonumentCoreRoom(CompoundTag param1CompoundTag) {
/* 1530 */       super(StructurePieceType.OCEAN_MONUMENT_CORE_ROOM, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 1535 */       generateBoxOnFillOnly(param1WorldGenLevel, param1BoundingBox, 1, 8, 0, 14, 8, 14, BASE_GRAY);
/*      */ 
/*      */ 
/*      */       
/* 1539 */       byte b = 7;
/* 1540 */       BlockState blockState = BASE_LIGHT;
/* 1541 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 7, 0, 0, 7, 15, blockState, blockState, false);
/* 1542 */       generateBox(param1WorldGenLevel, param1BoundingBox, 15, 7, 0, 15, 7, 15, blockState, blockState, false);
/* 1543 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 7, 0, 15, 7, 0, blockState, blockState, false);
/* 1544 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 7, 15, 14, 7, 15, blockState, blockState, false);
/*      */       
/* 1546 */       for (b = 1; b <= 6; b++) {
/* 1547 */         blockState = BASE_LIGHT;
/* 1548 */         if (b == 2 || b == 6) {
/* 1549 */           blockState = BASE_GRAY;
/*      */         }
/*      */         
/* 1552 */         for (byte b1 = 0; b1 <= 15; b1 += 15) {
/* 1553 */           generateBox(param1WorldGenLevel, param1BoundingBox, b1, b, 0, b1, b, 1, blockState, blockState, false);
/* 1554 */           generateBox(param1WorldGenLevel, param1BoundingBox, b1, b, 6, b1, b, 9, blockState, blockState, false);
/* 1555 */           generateBox(param1WorldGenLevel, param1BoundingBox, b1, b, 14, b1, b, 15, blockState, blockState, false);
/*      */         } 
/* 1557 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, b, 0, 1, b, 0, blockState, blockState, false);
/* 1558 */         generateBox(param1WorldGenLevel, param1BoundingBox, 6, b, 0, 9, b, 0, blockState, blockState, false);
/* 1559 */         generateBox(param1WorldGenLevel, param1BoundingBox, 14, b, 0, 14, b, 0, blockState, blockState, false);
/*      */         
/* 1561 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, b, 15, 14, b, 15, blockState, blockState, false);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1566 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 3, 6, 9, 6, 9, BASE_BLACK, BASE_BLACK, false);
/* 1567 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 4, 7, 8, 5, 8, Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
/* 1568 */       for (b = 3; b <= 6; b += 3) {
/* 1569 */         for (byte b1 = 6; b1 <= 9; b1 += 3) {
/* 1570 */           placeBlock(param1WorldGenLevel, LAMP_BLOCK, b1, b, 6, param1BoundingBox);
/* 1571 */           placeBlock(param1WorldGenLevel, LAMP_BLOCK, b1, b, 9, param1BoundingBox);
/*      */         } 
/*      */       } 
/*      */       
/* 1575 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 1, 6, 5, 2, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1576 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 1, 9, 5, 2, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1577 */       generateBox(param1WorldGenLevel, param1BoundingBox, 10, 1, 6, 10, 2, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1578 */       generateBox(param1WorldGenLevel, param1BoundingBox, 10, 1, 9, 10, 2, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1579 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 1, 5, 6, 2, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1580 */       generateBox(param1WorldGenLevel, param1BoundingBox, 9, 1, 5, 9, 2, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1581 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 1, 10, 6, 2, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1582 */       generateBox(param1WorldGenLevel, param1BoundingBox, 9, 1, 10, 9, 2, 10, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1584 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 2, 5, 5, 6, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1585 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 2, 10, 5, 6, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1586 */       generateBox(param1WorldGenLevel, param1BoundingBox, 10, 2, 5, 10, 6, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1587 */       generateBox(param1WorldGenLevel, param1BoundingBox, 10, 2, 10, 10, 6, 10, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1589 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 7, 1, 5, 7, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1590 */       generateBox(param1WorldGenLevel, param1BoundingBox, 10, 7, 1, 10, 7, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1591 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 7, 9, 5, 7, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1592 */       generateBox(param1WorldGenLevel, param1BoundingBox, 10, 7, 9, 10, 7, 14, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1594 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 7, 5, 6, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1595 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 7, 10, 6, 7, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1596 */       generateBox(param1WorldGenLevel, param1BoundingBox, 9, 7, 5, 14, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1597 */       generateBox(param1WorldGenLevel, param1BoundingBox, 9, 7, 10, 14, 7, 10, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/* 1600 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 1, 2, 2, 1, 3, BASE_LIGHT, BASE_LIGHT, false);
/* 1601 */       generateBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 2, 3, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1602 */       generateBox(param1WorldGenLevel, param1BoundingBox, 13, 1, 2, 13, 1, 3, BASE_LIGHT, BASE_LIGHT, false);
/* 1603 */       generateBox(param1WorldGenLevel, param1BoundingBox, 12, 1, 2, 12, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1604 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 1, 12, 2, 1, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1605 */       generateBox(param1WorldGenLevel, param1BoundingBox, 3, 1, 13, 3, 1, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1606 */       generateBox(param1WorldGenLevel, param1BoundingBox, 13, 1, 12, 13, 1, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1607 */       generateBox(param1WorldGenLevel, param1BoundingBox, 12, 1, 13, 12, 1, 13, BASE_LIGHT, BASE_LIGHT, false);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentWingRoom extends OceanMonumentPiece {
/*      */     private int mainDesign;
/*      */     
/*      */     public OceanMonumentWingRoom(Direction param1Direction, BoundingBox param1BoundingBox, int param1Int) {
/* 1615 */       super(StructurePieceType.OCEAN_MONUMENT_WING_ROOM, param1Direction, 1, param1BoundingBox);
/* 1616 */       this.mainDesign = param1Int & 0x1;
/*      */     }
/*      */     
/*      */     public OceanMonumentWingRoom(CompoundTag param1CompoundTag) {
/* 1620 */       super(StructurePieceType.OCEAN_MONUMENT_WING_ROOM, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 1625 */       if (this.mainDesign == 0) {
/* 1626 */         byte b; for (b = 0; b < 4; b++) {
/* 1627 */           generateBox(param1WorldGenLevel, param1BoundingBox, 10 - b, 3 - b, 20 - b, 12 + b, 3 - b, 20, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/* 1629 */         generateBox(param1WorldGenLevel, param1BoundingBox, 7, 0, 6, 15, 0, 16, BASE_LIGHT, BASE_LIGHT, false);
/* 1630 */         generateBox(param1WorldGenLevel, param1BoundingBox, 6, 0, 6, 6, 3, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 1631 */         generateBox(param1WorldGenLevel, param1BoundingBox, 16, 0, 6, 16, 3, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 1632 */         generateBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 7, 7, 1, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 1633 */         generateBox(param1WorldGenLevel, param1BoundingBox, 15, 1, 7, 15, 1, 20, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/* 1635 */         generateBox(param1WorldGenLevel, param1BoundingBox, 7, 1, 6, 9, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1636 */         generateBox(param1WorldGenLevel, param1BoundingBox, 13, 1, 6, 15, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1637 */         generateBox(param1WorldGenLevel, param1BoundingBox, 8, 1, 7, 9, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1638 */         generateBox(param1WorldGenLevel, param1BoundingBox, 13, 1, 7, 14, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1639 */         generateBox(param1WorldGenLevel, param1BoundingBox, 9, 0, 5, 13, 0, 5, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/* 1641 */         generateBox(param1WorldGenLevel, param1BoundingBox, 10, 0, 7, 12, 0, 7, BASE_BLACK, BASE_BLACK, false);
/* 1642 */         generateBox(param1WorldGenLevel, param1BoundingBox, 8, 0, 10, 8, 0, 12, BASE_BLACK, BASE_BLACK, false);
/* 1643 */         generateBox(param1WorldGenLevel, param1BoundingBox, 14, 0, 10, 14, 0, 12, BASE_BLACK, BASE_BLACK, false);
/*      */         
/* 1645 */         for (b = 18; b >= 7; b -= 3) {
/* 1646 */           placeBlock(param1WorldGenLevel, LAMP_BLOCK, 6, 3, b, param1BoundingBox);
/* 1647 */           placeBlock(param1WorldGenLevel, LAMP_BLOCK, 16, 3, b, param1BoundingBox);
/*      */         } 
/* 1649 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 10, 0, 10, param1BoundingBox);
/* 1650 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 12, 0, 10, param1BoundingBox);
/* 1651 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 10, 0, 12, param1BoundingBox);
/* 1652 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 12, 0, 12, param1BoundingBox);
/*      */         
/* 1654 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 8, 3, 6, param1BoundingBox);
/* 1655 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 14, 3, 6, param1BoundingBox);
/*      */ 
/*      */         
/* 1658 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 4, 2, 4, param1BoundingBox);
/* 1659 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 4, 1, 4, param1BoundingBox);
/* 1660 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 4, 0, 4, param1BoundingBox);
/*      */         
/* 1662 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 18, 2, 4, param1BoundingBox);
/* 1663 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 18, 1, 4, param1BoundingBox);
/* 1664 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 18, 0, 4, param1BoundingBox);
/*      */         
/* 1666 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 4, 2, 18, param1BoundingBox);
/* 1667 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 4, 1, 18, param1BoundingBox);
/* 1668 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 4, 0, 18, param1BoundingBox);
/*      */         
/* 1670 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 18, 2, 18, param1BoundingBox);
/* 1671 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 18, 1, 18, param1BoundingBox);
/* 1672 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 18, 0, 18, param1BoundingBox);
/*      */ 
/*      */         
/* 1675 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 9, 7, 20, param1BoundingBox);
/* 1676 */         placeBlock(param1WorldGenLevel, BASE_LIGHT, 13, 7, 20, param1BoundingBox);
/* 1677 */         generateBox(param1WorldGenLevel, param1BoundingBox, 6, 0, 21, 7, 4, 21, BASE_LIGHT, BASE_LIGHT, false);
/* 1678 */         generateBox(param1WorldGenLevel, param1BoundingBox, 15, 0, 21, 16, 4, 21, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/* 1680 */         spawnElder(param1WorldGenLevel, param1BoundingBox, 11, 2, 16);
/* 1681 */       } else if (this.mainDesign == 1) {
/* 1682 */         generateBox(param1WorldGenLevel, param1BoundingBox, 9, 3, 18, 13, 3, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 1683 */         generateBox(param1WorldGenLevel, param1BoundingBox, 9, 0, 18, 9, 2, 18, BASE_LIGHT, BASE_LIGHT, false);
/* 1684 */         generateBox(param1WorldGenLevel, param1BoundingBox, 13, 0, 18, 13, 2, 18, BASE_LIGHT, BASE_LIGHT, false);
/* 1685 */         byte b1 = 9;
/* 1686 */         byte b2 = 20;
/* 1687 */         byte b3 = 5; byte b4;
/* 1688 */         for (b4 = 0; b4 < 2; b4++) {
/* 1689 */           placeBlock(param1WorldGenLevel, BASE_LIGHT, b1, 6, 20, param1BoundingBox);
/* 1690 */           placeBlock(param1WorldGenLevel, LAMP_BLOCK, b1, 5, 20, param1BoundingBox);
/* 1691 */           placeBlock(param1WorldGenLevel, BASE_LIGHT, b1, 4, 20, param1BoundingBox);
/* 1692 */           b1 = 13;
/*      */         } 
/*      */         
/* 1695 */         generateBox(param1WorldGenLevel, param1BoundingBox, 7, 3, 7, 15, 3, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1696 */         b1 = 10;
/* 1697 */         for (b4 = 0; b4 < 2; b4++) {
/* 1698 */           generateBox(param1WorldGenLevel, param1BoundingBox, b1, 0, 10, b1, 6, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1699 */           generateBox(param1WorldGenLevel, param1BoundingBox, b1, 0, 12, b1, 6, 12, BASE_LIGHT, BASE_LIGHT, false);
/* 1700 */           placeBlock(param1WorldGenLevel, LAMP_BLOCK, b1, 0, 10, param1BoundingBox);
/* 1701 */           placeBlock(param1WorldGenLevel, LAMP_BLOCK, b1, 0, 12, param1BoundingBox);
/* 1702 */           placeBlock(param1WorldGenLevel, LAMP_BLOCK, b1, 4, 10, param1BoundingBox);
/* 1703 */           placeBlock(param1WorldGenLevel, LAMP_BLOCK, b1, 4, 12, param1BoundingBox);
/* 1704 */           b1 = 12;
/*      */         } 
/* 1706 */         b1 = 8;
/* 1707 */         for (b4 = 0; b4 < 2; b4++) {
/* 1708 */           generateBox(param1WorldGenLevel, param1BoundingBox, b1, 0, 7, b1, 2, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1709 */           generateBox(param1WorldGenLevel, param1BoundingBox, b1, 0, 14, b1, 2, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1710 */           b1 = 14;
/*      */         } 
/* 1712 */         generateBox(param1WorldGenLevel, param1BoundingBox, 8, 3, 8, 8, 3, 13, BASE_BLACK, BASE_BLACK, false);
/* 1713 */         generateBox(param1WorldGenLevel, param1BoundingBox, 14, 3, 8, 14, 3, 13, BASE_BLACK, BASE_BLACK, false);
/*      */         
/* 1715 */         spawnElder(param1WorldGenLevel, param1BoundingBox, 11, 5, 13);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentPenthouse extends OceanMonumentPiece {
/*      */     public OceanMonumentPenthouse(Direction param1Direction, BoundingBox param1BoundingBox) {
/* 1722 */       super(StructurePieceType.OCEAN_MONUMENT_PENTHOUSE, param1Direction, 1, param1BoundingBox);
/*      */     }
/*      */     
/*      */     public OceanMonumentPenthouse(CompoundTag param1CompoundTag) {
/* 1726 */       super(StructurePieceType.OCEAN_MONUMENT_PENTHOUSE, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 1731 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, -1, 2, 11, -1, 11, BASE_LIGHT, BASE_LIGHT, false);
/* 1732 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, -1, 0, 1, -1, 11, BASE_GRAY, BASE_GRAY, false);
/* 1733 */       generateBox(param1WorldGenLevel, param1BoundingBox, 12, -1, 0, 13, -1, 11, BASE_GRAY, BASE_GRAY, false);
/* 1734 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, -1, 0, 11, -1, 1, BASE_GRAY, BASE_GRAY, false);
/* 1735 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, -1, 12, 11, -1, 13, BASE_GRAY, BASE_GRAY, false);
/*      */       
/* 1737 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 0, 0, 0, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1738 */       generateBox(param1WorldGenLevel, param1BoundingBox, 13, 0, 0, 13, 0, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1739 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 0, 0, 12, 0, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1740 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 0, 13, 12, 0, 13, BASE_LIGHT, BASE_LIGHT, false);
/*      */       byte b1;
/* 1742 */       for (b1 = 2; b1 <= 11; b1 += 3) {
/* 1743 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 0, 0, b1, param1BoundingBox);
/* 1744 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, 13, 0, b1, param1BoundingBox);
/* 1745 */         placeBlock(param1WorldGenLevel, LAMP_BLOCK, b1, 0, 0, param1BoundingBox);
/*      */       } 
/*      */       
/* 1748 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 0, 3, 4, 0, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1749 */       generateBox(param1WorldGenLevel, param1BoundingBox, 9, 0, 3, 11, 0, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1750 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 0, 9, 9, 0, 11, BASE_LIGHT, BASE_LIGHT, false);
/* 1751 */       placeBlock(param1WorldGenLevel, BASE_LIGHT, 5, 0, 8, param1BoundingBox);
/* 1752 */       placeBlock(param1WorldGenLevel, BASE_LIGHT, 8, 0, 8, param1BoundingBox);
/* 1753 */       placeBlock(param1WorldGenLevel, BASE_LIGHT, 10, 0, 10, param1BoundingBox);
/* 1754 */       placeBlock(param1WorldGenLevel, BASE_LIGHT, 3, 0, 10, param1BoundingBox);
/* 1755 */       generateBox(param1WorldGenLevel, param1BoundingBox, 3, 0, 3, 3, 0, 7, BASE_BLACK, BASE_BLACK, false);
/* 1756 */       generateBox(param1WorldGenLevel, param1BoundingBox, 10, 0, 3, 10, 0, 7, BASE_BLACK, BASE_BLACK, false);
/* 1757 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 0, 10, 7, 0, 10, BASE_BLACK, BASE_BLACK, false);
/*      */       
/* 1759 */       b1 = 3;
/* 1760 */       for (byte b2 = 0; b2 < 2; b2++) {
/* 1761 */         for (byte b = 2; b <= 8; b += 3) {
/* 1762 */           generateBox(param1WorldGenLevel, param1BoundingBox, b1, 0, b, b1, 2, b, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/* 1764 */         b1 = 10;
/*      */       } 
/* 1766 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 0, 10, 5, 2, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1767 */       generateBox(param1WorldGenLevel, param1BoundingBox, 8, 0, 10, 8, 2, 10, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1769 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, -1, 7, 7, -1, 8, BASE_BLACK, BASE_BLACK, false);
/*      */ 
/*      */       
/* 1772 */       generateWaterBox(param1WorldGenLevel, param1BoundingBox, 6, -1, 3, 7, -1, 4);
/*      */       
/* 1774 */       spawnElder(param1WorldGenLevel, param1BoundingBox, 6, 1, 6);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RoomDefinition {
/*      */     final int index;
/* 1780 */     final RoomDefinition[] connections = new RoomDefinition[6];
/* 1781 */     final boolean[] hasOpening = new boolean[6];
/*      */     boolean claimed;
/*      */     boolean isSource;
/*      */     private int scanIndex;
/*      */     
/*      */     public RoomDefinition(int param1Int) {
/* 1787 */       this.index = param1Int;
/*      */     }
/*      */     
/*      */     public void setConnection(Direction param1Direction, RoomDefinition param1RoomDefinition) {
/* 1791 */       this.connections[param1Direction.get3DDataValue()] = param1RoomDefinition;
/* 1792 */       param1RoomDefinition.connections[param1Direction.getOpposite().get3DDataValue()] = this;
/*      */     }
/*      */     
/*      */     public void updateOpenings() {
/* 1796 */       for (byte b = 0; b < 6; b++) {
/* 1797 */         this.hasOpening[b] = (this.connections[b] != null);
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean findSource(int param1Int) {
/* 1802 */       if (this.isSource) {
/* 1803 */         return true;
/*      */       }
/* 1805 */       this.scanIndex = param1Int;
/* 1806 */       for (byte b = 0; b < 6; b++) {
/* 1807 */         if (this.connections[b] != null && this.hasOpening[b] && 
/* 1808 */           (this.connections[b]).scanIndex != param1Int && this.connections[b].findSource(param1Int)) {
/* 1809 */           return true;
/*      */         }
/*      */       } 
/*      */       
/* 1813 */       return false;
/*      */     }
/*      */     
/*      */     public boolean isSpecial() {
/* 1817 */       return (this.index >= 75);
/*      */     }
/*      */     
/*      */     public int countOpenings() {
/* 1821 */       byte b1 = 0;
/* 1822 */       for (byte b2 = 0; b2 < 6; b2++) {
/* 1823 */         if (this.hasOpening[b2]) {
/* 1824 */           b1++;
/*      */         }
/*      */       } 
/* 1827 */       return b1;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class FitSimpleRoom
/*      */     implements MonumentRoomFitter
/*      */   {
/*      */     public boolean fits(OceanMonumentPieces.RoomDefinition param1RoomDefinition) {
/* 1840 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition, RandomSource param1RandomSource) {
/* 1845 */       param1RoomDefinition.claimed = true;
/* 1846 */       return new OceanMonumentPieces.OceanMonumentSimpleRoom(param1Direction, param1RoomDefinition, param1RandomSource);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FitSimpleTopRoom
/*      */     implements MonumentRoomFitter {
/*      */     public boolean fits(OceanMonumentPieces.RoomDefinition param1RoomDefinition) {
/* 1853 */       return (!param1RoomDefinition.hasOpening[Direction.WEST.get3DDataValue()] && !param1RoomDefinition.hasOpening[Direction.EAST.get3DDataValue()] && !param1RoomDefinition.hasOpening[Direction.NORTH.get3DDataValue()] && !param1RoomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()] && !param1RoomDefinition.hasOpening[Direction.UP.get3DDataValue()]);
/*      */     }
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition, RandomSource param1RandomSource) {
/* 1858 */       param1RoomDefinition.claimed = true;
/* 1859 */       return new OceanMonumentPieces.OceanMonumentSimpleTopRoom(param1Direction, param1RoomDefinition);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FitDoubleYRoom
/*      */     implements MonumentRoomFitter {
/*      */     public boolean fits(OceanMonumentPieces.RoomDefinition param1RoomDefinition) {
/* 1866 */       return (param1RoomDefinition.hasOpening[Direction.UP.get3DDataValue()] && !(param1RoomDefinition.connections[Direction.UP.get3DDataValue()]).claimed);
/*      */     }
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition, RandomSource param1RandomSource) {
/* 1871 */       param1RoomDefinition.claimed = true;
/* 1872 */       (param1RoomDefinition.connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1873 */       return new OceanMonumentPieces.OceanMonumentDoubleYRoom(param1Direction, param1RoomDefinition);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FitDoubleXRoom
/*      */     implements MonumentRoomFitter {
/*      */     public boolean fits(OceanMonumentPieces.RoomDefinition param1RoomDefinition) {
/* 1880 */       return (param1RoomDefinition.hasOpening[Direction.EAST.get3DDataValue()] && !(param1RoomDefinition.connections[Direction.EAST.get3DDataValue()]).claimed);
/*      */     }
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition, RandomSource param1RandomSource) {
/* 1885 */       param1RoomDefinition.claimed = true;
/* 1886 */       (param1RoomDefinition.connections[Direction.EAST.get3DDataValue()]).claimed = true;
/* 1887 */       return new OceanMonumentPieces.OceanMonumentDoubleXRoom(param1Direction, param1RoomDefinition);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FitDoubleZRoom
/*      */     implements MonumentRoomFitter {
/*      */     public boolean fits(OceanMonumentPieces.RoomDefinition param1RoomDefinition) {
/* 1894 */       return (param1RoomDefinition.hasOpening[Direction.NORTH.get3DDataValue()] && !(param1RoomDefinition.connections[Direction.NORTH.get3DDataValue()]).claimed);
/*      */     }
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition, RandomSource param1RandomSource) {
/* 1899 */       OceanMonumentPieces.RoomDefinition roomDefinition = param1RoomDefinition;
/* 1900 */       if (!param1RoomDefinition.hasOpening[Direction.NORTH.get3DDataValue()] || (param1RoomDefinition.connections[Direction.NORTH.get3DDataValue()]).claimed) {
/* 1901 */         roomDefinition = param1RoomDefinition.connections[Direction.SOUTH.get3DDataValue()];
/*      */       }
/* 1903 */       roomDefinition.claimed = true;
/* 1904 */       (roomDefinition.connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/* 1905 */       return new OceanMonumentPieces.OceanMonumentDoubleZRoom(param1Direction, roomDefinition);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FitDoubleXYRoom
/*      */     implements MonumentRoomFitter {
/*      */     public boolean fits(OceanMonumentPieces.RoomDefinition param1RoomDefinition) {
/* 1912 */       if (param1RoomDefinition.hasOpening[Direction.EAST.get3DDataValue()] && !(param1RoomDefinition.connections[Direction.EAST.get3DDataValue()]).claimed && 
/* 1913 */         param1RoomDefinition.hasOpening[Direction.UP.get3DDataValue()] && !(param1RoomDefinition.connections[Direction.UP.get3DDataValue()]).claimed) {
/* 1914 */         OceanMonumentPieces.RoomDefinition roomDefinition = param1RoomDefinition.connections[Direction.EAST.get3DDataValue()];
/*      */         
/* 1916 */         return (roomDefinition.hasOpening[Direction.UP.get3DDataValue()] && !(roomDefinition.connections[Direction.UP.get3DDataValue()]).claimed);
/*      */       } 
/*      */       
/* 1919 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition, RandomSource param1RandomSource) {
/* 1924 */       param1RoomDefinition.claimed = true;
/* 1925 */       (param1RoomDefinition.connections[Direction.EAST.get3DDataValue()]).claimed = true;
/* 1926 */       (param1RoomDefinition.connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1927 */       ((param1RoomDefinition.connections[Direction.EAST.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1928 */       return new OceanMonumentPieces.OceanMonumentDoubleXYRoom(param1Direction, param1RoomDefinition);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FitDoubleYZRoom
/*      */     implements MonumentRoomFitter {
/*      */     public boolean fits(OceanMonumentPieces.RoomDefinition param1RoomDefinition) {
/* 1935 */       if (param1RoomDefinition.hasOpening[Direction.NORTH.get3DDataValue()] && !(param1RoomDefinition.connections[Direction.NORTH.get3DDataValue()]).claimed && 
/* 1936 */         param1RoomDefinition.hasOpening[Direction.UP.get3DDataValue()] && !(param1RoomDefinition.connections[Direction.UP.get3DDataValue()]).claimed) {
/* 1937 */         OceanMonumentPieces.RoomDefinition roomDefinition = param1RoomDefinition.connections[Direction.NORTH.get3DDataValue()];
/*      */         
/* 1939 */         return (roomDefinition.hasOpening[Direction.UP.get3DDataValue()] && !(roomDefinition.connections[Direction.UP.get3DDataValue()]).claimed);
/*      */       } 
/*      */       
/* 1942 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition, RandomSource param1RandomSource) {
/* 1947 */       param1RoomDefinition.claimed = true;
/* 1948 */       (param1RoomDefinition.connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/* 1949 */       (param1RoomDefinition.connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1950 */       ((param1RoomDefinition.connections[Direction.NORTH.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1951 */       return new OceanMonumentPieces.OceanMonumentDoubleYZRoom(param1Direction, param1RoomDefinition);
/*      */     }
/*      */   }
/*      */   
/*      */   private static interface MonumentRoomFitter {
/*      */     boolean fits(OceanMonumentPieces.RoomDefinition param1RoomDefinition);
/*      */     
/*      */     OceanMonumentPieces.OceanMonumentPiece create(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition, RandomSource param1RandomSource);
/*      */   }
/*      */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */