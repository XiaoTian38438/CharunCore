/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ import com.google.common.collect.Lists;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Tuple;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.ChestBlock;
/*      */ import net.minecraft.world.level.block.Mirror;
/*      */ import net.minecraft.world.level.block.Rotation;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ 
/*      */ public class WoodlandMansionPieces {
/*      */   public static class WoodlandMansionPiece extends TemplateStructurePiece {
/*      */     public WoodlandMansionPiece(StructureTemplateManager param1StructureTemplateManager, String param1String, BlockPos param1BlockPos, Rotation param1Rotation) {
/*   40 */       this(param1StructureTemplateManager, param1String, param1BlockPos, param1Rotation, Mirror.NONE);
/*      */     }
/*      */     
/*      */     public WoodlandMansionPiece(StructureTemplateManager param1StructureTemplateManager, String param1String, BlockPos param1BlockPos, Rotation param1Rotation, Mirror param1Mirror) {
/*   44 */       super(StructurePieceType.WOODLAND_MANSION_PIECE, 0, param1StructureTemplateManager, makeLocation(param1String), param1String, makeSettings(param1Mirror, param1Rotation), param1BlockPos);
/*      */     }
/*      */     
/*      */     public WoodlandMansionPiece(StructureTemplateManager param1StructureTemplateManager, CompoundTag param1CompoundTag) {
/*   48 */       super(StructurePieceType.WOODLAND_MANSION_PIECE, param1CompoundTag, param1StructureTemplateManager, param1Identifier -> makeSettings(param1CompoundTag.read("Mi", Mirror.LEGACY_CODEC).orElseThrow(), param1CompoundTag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
/*      */     }
/*      */ 
/*      */     
/*      */     protected Identifier makeTemplateLocation() {
/*   53 */       return makeLocation(this.templateName);
/*      */     }
/*      */     
/*      */     private static Identifier makeLocation(String param1String) {
/*   57 */       return Identifier.withDefaultNamespace("woodland_mansion/" + param1String);
/*      */     }
/*      */     
/*      */     private static StructurePlaceSettings makeSettings(Mirror param1Mirror, Rotation param1Rotation) {
/*   61 */       return (new StructurePlaceSettings()).setIgnoreEntities(true).setRotation(param1Rotation).setMirror(param1Mirror).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {
/*   66 */       super.addAdditionalSaveData(param1StructurePieceSerializationContext, param1CompoundTag);
/*      */       
/*   68 */       param1CompoundTag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*   69 */       param1CompoundTag.store("Mi", Mirror.LEGACY_CODEC, this.placeSettings.getMirror());
/*      */     }
/*      */ 
/*      */     
/*      */     protected void handleDataMarker(String param1String, BlockPos param1BlockPos, ServerLevelAccessor param1ServerLevelAccessor, RandomSource param1RandomSource, BoundingBox param1BoundingBox) {
/*   74 */       if (param1String.startsWith("Chest")) {
/*   75 */         Rotation rotation = this.placeSettings.getRotation();
/*   76 */         BlockState blockState = Blocks.CHEST.defaultBlockState();
/*   77 */         if ("ChestWest".equals(param1String)) {
/*   78 */           blockState = (BlockState)blockState.setValue((Property)ChestBlock.FACING, (Comparable)rotation.rotate(Direction.WEST));
/*   79 */         } else if ("ChestEast".equals(param1String)) {
/*   80 */           blockState = (BlockState)blockState.setValue((Property)ChestBlock.FACING, (Comparable)rotation.rotate(Direction.EAST));
/*   81 */         } else if ("ChestSouth".equals(param1String)) {
/*   82 */           blockState = (BlockState)blockState.setValue((Property)ChestBlock.FACING, (Comparable)rotation.rotate(Direction.SOUTH));
/*   83 */         } else if ("ChestNorth".equals(param1String)) {
/*   84 */           blockState = (BlockState)blockState.setValue((Property)ChestBlock.FACING, (Comparable)rotation.rotate(Direction.NORTH));
/*      */         } 
/*   86 */         createChest(param1ServerLevelAccessor, param1BoundingBox, param1RandomSource, param1BlockPos, BuiltInLootTables.WOODLAND_MANSION, blockState);
/*      */       } else {
/*   88 */         int i; byte b; ArrayList<Mob> arrayList = new ArrayList();
/*   89 */         switch (param1String) {
/*      */           case "Mage":
/*   91 */             arrayList.add((Mob)EntityType.EVOKER.create((Level)param1ServerLevelAccessor.getLevel(), EntitySpawnReason.STRUCTURE));
/*      */             break;
/*      */           case "Warrior":
/*   94 */             arrayList.add((Mob)EntityType.VINDICATOR.create((Level)param1ServerLevelAccessor.getLevel(), EntitySpawnReason.STRUCTURE));
/*      */             break;
/*      */           case "Group of Allays":
/*   97 */             i = param1ServerLevelAccessor.getRandom().nextInt(3) + 1;
/*   98 */             for (b = 0; b < i; b++) {
/*   99 */               arrayList.add((Mob)EntityType.ALLAY.create((Level)param1ServerLevelAccessor.getLevel(), EntitySpawnReason.STRUCTURE));
/*      */             }
/*      */             break;
/*      */           
/*      */           default:
/*      */             return;
/*      */         } 
/*  106 */         for (Mob mob : arrayList) {
/*  107 */           if (mob == null) {
/*      */             continue;
/*      */           }
/*  110 */           mob.setPersistenceRequired();
/*  111 */           mob.snapTo(param1BlockPos, 0.0F, 0.0F);
/*  112 */           mob.finalizeSpawn(param1ServerLevelAccessor, param1ServerLevelAccessor.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.STRUCTURE, null);
/*  113 */           param1ServerLevelAccessor.addFreshEntityWithPassengers((Entity)mob);
/*  114 */           param1ServerLevelAccessor.setBlock(param1BlockPos, Blocks.AIR.defaultBlockState(), 2);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static void generateMansion(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation, List<WoodlandMansionPiece> paramList, RandomSource paramRandomSource) {
/*  121 */     MansionGrid mansionGrid = new MansionGrid(paramRandomSource);
/*  122 */     MansionPiecePlacer mansionPiecePlacer = new MansionPiecePlacer(paramStructureTemplateManager, paramRandomSource);
/*  123 */     mansionPiecePlacer.createMansion(paramBlockPos, paramRotation, paramList, mansionGrid);
/*      */   }
/*      */   
/*      */   private static class PlacementData
/*      */   {
/*      */     public Rotation rotation;
/*      */     public BlockPos position;
/*      */     public String wallType;
/*      */   }
/*      */   
/*      */   private static class MansionPiecePlacer {
/*      */     private final StructureTemplateManager structureTemplateManager;
/*      */     private final RandomSource random;
/*      */     private int startX;
/*      */     private int startY;
/*      */     
/*      */     public MansionPiecePlacer(StructureTemplateManager param1StructureTemplateManager, RandomSource param1RandomSource) {
/*  140 */       this.structureTemplateManager = param1StructureTemplateManager;
/*  141 */       this.random = param1RandomSource;
/*      */     }
/*      */     
/*      */     public void createMansion(BlockPos param1BlockPos, Rotation param1Rotation, List<WoodlandMansionPieces.WoodlandMansionPiece> param1List, WoodlandMansionPieces.MansionGrid param1MansionGrid) {
/*  145 */       WoodlandMansionPieces.PlacementData placementData1 = new WoodlandMansionPieces.PlacementData();
/*  146 */       placementData1.position = param1BlockPos;
/*  147 */       placementData1.rotation = param1Rotation;
/*  148 */       placementData1.wallType = "wall_flat";
/*      */       
/*  150 */       WoodlandMansionPieces.PlacementData placementData2 = new WoodlandMansionPieces.PlacementData();
/*      */ 
/*      */       
/*  153 */       entrance(param1List, placementData1);
/*  154 */       placementData2.position = placementData1.position.above(8);
/*  155 */       placementData2.rotation = placementData1.rotation;
/*  156 */       placementData2.wallType = "wall_window";
/*      */       
/*  158 */       if (!param1List.isEmpty());
/*      */ 
/*      */ 
/*      */       
/*  162 */       WoodlandMansionPieces.SimpleGrid simpleGrid1 = param1MansionGrid.baseGrid;
/*  163 */       WoodlandMansionPieces.SimpleGrid simpleGrid2 = param1MansionGrid.thirdFloorGrid;
/*      */       
/*  165 */       this.startX = param1MansionGrid.entranceX + 1;
/*  166 */       this.startY = param1MansionGrid.entranceY + 1;
/*  167 */       int i = param1MansionGrid.entranceX + 1;
/*  168 */       int j = param1MansionGrid.entranceY;
/*      */       
/*  170 */       traverseOuterWalls(param1List, placementData1, simpleGrid1, Direction.SOUTH, this.startX, this.startY, i, j);
/*  171 */       traverseOuterWalls(param1List, placementData2, simpleGrid1, Direction.SOUTH, this.startX, this.startY, i, j);
/*      */ 
/*      */       
/*  174 */       WoodlandMansionPieces.PlacementData placementData3 = new WoodlandMansionPieces.PlacementData();
/*  175 */       placementData3.position = placementData1.position.above(19);
/*  176 */       placementData3.rotation = placementData1.rotation;
/*  177 */       placementData3.wallType = "wall_window";
/*      */       
/*  179 */       boolean bool = false;
/*  180 */       for (byte b1 = 0; b1 < simpleGrid2.height && !bool; b1++) {
/*  181 */         for (int k = simpleGrid2.width - 1; k >= 0 && !bool; k--) {
/*  182 */           if (WoodlandMansionPieces.MansionGrid.isHouse(simpleGrid2, k, b1)) {
/*  183 */             placementData3.position = placementData3.position.relative(param1Rotation.rotate(Direction.SOUTH), 8 + (b1 - this.startY) * 8);
/*  184 */             placementData3.position = placementData3.position.relative(param1Rotation.rotate(Direction.EAST), (k - this.startX) * 8);
/*  185 */             traverseWallPiece(param1List, placementData3);
/*  186 */             traverseOuterWalls(param1List, placementData3, simpleGrid2, Direction.SOUTH, k, b1, k, b1);
/*  187 */             bool = true;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  193 */       createRoof(param1List, param1BlockPos.above(16), param1Rotation, simpleGrid1, simpleGrid2);
/*  194 */       createRoof(param1List, param1BlockPos.above(27), param1Rotation, simpleGrid2, null);
/*      */       
/*  196 */       if (!param1List.isEmpty());
/*      */ 
/*      */ 
/*      */       
/*  200 */       WoodlandMansionPieces.FloorRoomCollection[] arrayOfFloorRoomCollection = new WoodlandMansionPieces.FloorRoomCollection[3];
/*  201 */       arrayOfFloorRoomCollection[0] = new WoodlandMansionPieces.FirstFloorRoomCollection();
/*  202 */       arrayOfFloorRoomCollection[1] = new WoodlandMansionPieces.SecondFloorRoomCollection();
/*  203 */       arrayOfFloorRoomCollection[2] = new WoodlandMansionPieces.ThirdFloorRoomCollection();
/*      */       
/*  205 */       for (byte b2 = 0; b2 < 3; b2++) {
/*  206 */         BlockPos blockPos = param1BlockPos.above(8 * b2 + ((b2 == 2) ? 3 : 0));
/*  207 */         WoodlandMansionPieces.SimpleGrid simpleGrid3 = param1MansionGrid.floorRooms[b2];
/*  208 */         WoodlandMansionPieces.SimpleGrid simpleGrid4 = (b2 == 2) ? simpleGrid2 : simpleGrid1;
/*      */ 
/*      */         
/*  211 */         String str1 = (b2 == 0) ? "carpet_south_1" : "carpet_south_2";
/*  212 */         String str2 = (b2 == 0) ? "carpet_west_1" : "carpet_west_2";
/*  213 */         for (byte b3 = 0; b3 < simpleGrid4.height; b3++) {
/*  214 */           for (byte b = 0; b < simpleGrid4.width; b++) {
/*  215 */             if (simpleGrid4.get(b, b3) == 1) {
/*  216 */               BlockPos blockPos1 = blockPos.relative(param1Rotation.rotate(Direction.SOUTH), 8 + (b3 - this.startY) * 8);
/*  217 */               blockPos1 = blockPos1.relative(param1Rotation.rotate(Direction.EAST), (b - this.startX) * 8);
/*  218 */               param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "corridor_floor", blockPos1, param1Rotation));
/*      */               
/*  220 */               if (simpleGrid4.get(b, b3 - 1) == 1 || (simpleGrid3.get(b, b3 - 1) & 0x800000) == 8388608) {
/*  221 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "carpet_north", blockPos1.relative(param1Rotation.rotate(Direction.EAST), 1).above(), param1Rotation));
/*      */               }
/*  223 */               if (simpleGrid4.get(b + 1, b3) == 1 || (simpleGrid3.get(b + 1, b3) & 0x800000) == 8388608) {
/*  224 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "carpet_east", blockPos1.relative(param1Rotation.rotate(Direction.SOUTH), 1).relative(param1Rotation.rotate(Direction.EAST), 5).above(), param1Rotation));
/*      */               }
/*  226 */               if (simpleGrid4.get(b, b3 + 1) == 1 || (simpleGrid3.get(b, b3 + 1) & 0x800000) == 8388608) {
/*  227 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, str1, blockPos1.relative(param1Rotation.rotate(Direction.SOUTH), 5).relative(param1Rotation.rotate(Direction.WEST), 1), param1Rotation));
/*      */               }
/*  229 */               if (simpleGrid4.get(b - 1, b3) == 1 || (simpleGrid3.get(b - 1, b3) & 0x800000) == 8388608) {
/*  230 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, str2, blockPos1.relative(param1Rotation.rotate(Direction.WEST), 1).relative(param1Rotation.rotate(Direction.NORTH), 1), param1Rotation));
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/*  236 */         String str3 = (b2 == 0) ? "indoors_wall_1" : "indoors_wall_2";
/*  237 */         String str4 = (b2 == 0) ? "indoors_door_1" : "indoors_door_2";
/*  238 */         ArrayList<Direction> arrayList = Lists.newArrayList();
/*  239 */         for (byte b4 = 0; b4 < simpleGrid4.height; b4++) {
/*  240 */           for (byte b = 0; b < simpleGrid4.width; b++) {
/*  241 */             boolean bool1 = (b2 == 2 && simpleGrid4.get(b, b4) == 3) ? true : false;
/*  242 */             if (simpleGrid4.get(b, b4) == 2 || bool1) {
/*  243 */               int k = simpleGrid3.get(b, b4);
/*  244 */               int m = k & 0xF0000;
/*  245 */               int n = k & 0xFFFF;
/*      */ 
/*      */               
/*  248 */               bool1 = (bool1 && (k & 0x800000) == 8388608) ? true : false;
/*      */               
/*  250 */               arrayList.clear();
/*  251 */               if ((k & 0x200000) == 2097152) {
/*  252 */                 for (Direction direction1 : Direction.Plane.HORIZONTAL) {
/*  253 */                   if (simpleGrid4.get(b + direction1.getStepX(), b4 + direction1.getStepZ()) == 1) {
/*  254 */                     arrayList.add(direction1);
/*      */                   }
/*      */                 } 
/*      */               }
/*  258 */               Direction direction = null;
/*  259 */               if (!arrayList.isEmpty()) {
/*  260 */                 direction = arrayList.get(this.random.nextInt(arrayList.size()));
/*  261 */               } else if ((k & 0x100000) == 1048576) {
/*      */                 
/*  263 */                 direction = Direction.UP;
/*      */               } 
/*      */               
/*  266 */               BlockPos blockPos1 = blockPos.relative(param1Rotation.rotate(Direction.SOUTH), 8 + (b4 - this.startY) * 8);
/*  267 */               blockPos1 = blockPos1.relative(param1Rotation.rotate(Direction.EAST), -1 + (b - this.startX) * 8);
/*      */               
/*  269 */               if (WoodlandMansionPieces.MansionGrid.isHouse(simpleGrid4, b - 1, b4) && !param1MansionGrid.isRoomId(simpleGrid4, b - 1, b4, b2, n)) {
/*  270 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (direction == Direction.WEST) ? str4 : str3, blockPos1, param1Rotation));
/*      */               }
/*  272 */               if (simpleGrid4.get(b + 1, b4) == 1 && !bool1) {
/*  273 */                 BlockPos blockPos2 = blockPos1.relative(param1Rotation.rotate(Direction.EAST), 8);
/*  274 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (direction == Direction.EAST) ? str4 : str3, blockPos2, param1Rotation));
/*      */               } 
/*  276 */               if (WoodlandMansionPieces.MansionGrid.isHouse(simpleGrid4, b, b4 + 1) && !param1MansionGrid.isRoomId(simpleGrid4, b, b4 + 1, b2, n)) {
/*  277 */                 BlockPos blockPos2 = blockPos1.relative(param1Rotation.rotate(Direction.SOUTH), 7);
/*  278 */                 blockPos2 = blockPos2.relative(param1Rotation.rotate(Direction.EAST), 7);
/*  279 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (direction == Direction.SOUTH) ? str4 : str3, blockPos2, param1Rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */               } 
/*  281 */               if (simpleGrid4.get(b, b4 - 1) == 1 && !bool1) {
/*  282 */                 BlockPos blockPos2 = blockPos1.relative(param1Rotation.rotate(Direction.NORTH), 1);
/*  283 */                 blockPos2 = blockPos2.relative(param1Rotation.rotate(Direction.EAST), 7);
/*  284 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (direction == Direction.NORTH) ? str4 : str3, blockPos2, param1Rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */               } 
/*      */               
/*  287 */               if (m == 65536) {
/*  288 */                 addRoom1x1(param1List, blockPos1, param1Rotation, direction, arrayOfFloorRoomCollection[b2]);
/*  289 */               } else if (m == 131072 && direction != null) {
/*      */                 
/*  291 */                 Direction direction1 = param1MansionGrid.get1x2RoomDirection(simpleGrid4, b, b4, b2, n);
/*  292 */                 boolean bool2 = ((k & 0x400000) == 4194304) ? true : false;
/*  293 */                 addRoom1x2(param1List, blockPos1, param1Rotation, direction1, direction, arrayOfFloorRoomCollection[b2], bool2);
/*  294 */               } else if (m == 262144 && direction != null && direction != Direction.UP) {
/*      */                 
/*  296 */                 Direction direction1 = direction.getClockWise();
/*  297 */                 if (!param1MansionGrid.isRoomId(simpleGrid4, b + direction1.getStepX(), b4 + direction1.getStepZ(), b2, n)) {
/*  298 */                   direction1 = direction1.getOpposite();
/*      */                 }
/*  300 */                 addRoom2x2(param1List, blockPos1, param1Rotation, direction1, direction, arrayOfFloorRoomCollection[b2]);
/*  301 */               } else if (m == 262144 && direction == Direction.UP) {
/*  302 */                 addRoom2x2Secret(param1List, blockPos1, param1Rotation, arrayOfFloorRoomCollection[b2]);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     private void traverseOuterWalls(List<WoodlandMansionPieces.WoodlandMansionPiece> param1List, WoodlandMansionPieces.PlacementData param1PlacementData, WoodlandMansionPieces.SimpleGrid param1SimpleGrid, Direction param1Direction, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
/*  311 */       int i = param1Int1;
/*  312 */       int j = param1Int2;
/*  313 */       Direction direction = param1Direction;
/*      */       
/*      */       do {
/*  316 */         if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid, i + param1Direction.getStepX(), j + param1Direction.getStepZ())) {
/*      */           
/*  318 */           traverseTurn(param1List, param1PlacementData);
/*  319 */           param1Direction = param1Direction.getClockWise();
/*  320 */           if (i != param1Int3 || j != param1Int4 || direction != param1Direction) {
/*  321 */             traverseWallPiece(param1List, param1PlacementData);
/*      */           }
/*  323 */         } else if (WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid, i + param1Direction.getStepX(), j + param1Direction.getStepZ()) && WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid, i + param1Direction.getStepX() + param1Direction.getCounterClockWise().getStepX(), j + param1Direction.getStepZ() + param1Direction.getCounterClockWise().getStepZ())) {
/*      */           
/*  325 */           traverseInnerTurn(param1List, param1PlacementData);
/*  326 */           i += param1Direction.getStepX();
/*  327 */           j += param1Direction.getStepZ();
/*  328 */           param1Direction = param1Direction.getCounterClockWise();
/*      */         } else {
/*  330 */           i += param1Direction.getStepX();
/*  331 */           j += param1Direction.getStepZ();
/*  332 */           if (i != param1Int3 || j != param1Int4 || direction != param1Direction) {
/*  333 */             traverseWallPiece(param1List, param1PlacementData);
/*      */           }
/*      */         } 
/*  336 */       } while (i != param1Int3 || j != param1Int4 || direction != param1Direction);
/*      */     }
/*      */     
/*      */     private void createRoof(List<WoodlandMansionPieces.WoodlandMansionPiece> param1List, BlockPos param1BlockPos, Rotation param1Rotation, WoodlandMansionPieces.SimpleGrid param1SimpleGrid1, WoodlandMansionPieces.SimpleGrid param1SimpleGrid2) {
/*      */       byte b;
/*  341 */       for (b = 0; b < param1SimpleGrid1.height; b++) {
/*  342 */         for (byte b1 = 0; b1 < param1SimpleGrid1.width; b1++) {
/*  343 */           BlockPos blockPos = param1BlockPos;
/*  344 */           blockPos = blockPos.relative(param1Rotation.rotate(Direction.SOUTH), 8 + (b - this.startY) * 8);
/*  345 */           blockPos = blockPos.relative(param1Rotation.rotate(Direction.EAST), (b1 - this.startX) * 8);
/*      */ 
/*      */           
/*  348 */           boolean bool = (param1SimpleGrid2 != null && WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid2, b1, b)) ? true : false;
/*      */           
/*  350 */           if (WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1, b) && !bool) {
/*  351 */             param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof", blockPos.above(3), param1Rotation));
/*      */             
/*  353 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1 + 1, b)) {
/*  354 */               BlockPos blockPos1 = blockPos.relative(param1Rotation.rotate(Direction.EAST), 6);
/*  355 */               param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", blockPos1, param1Rotation));
/*      */             } 
/*  357 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1 - 1, b)) {
/*  358 */               BlockPos blockPos1 = blockPos.relative(param1Rotation.rotate(Direction.EAST), 0);
/*  359 */               blockPos1 = blockPos1.relative(param1Rotation.rotate(Direction.SOUTH), 7);
/*  360 */               param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", blockPos1, param1Rotation.getRotated(Rotation.CLOCKWISE_180)));
/*      */             } 
/*  362 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1, b - 1)) {
/*  363 */               BlockPos blockPos1 = blockPos.relative(param1Rotation.rotate(Direction.WEST), 1);
/*  364 */               param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", blockPos1, param1Rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*      */             } 
/*  366 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1, b + 1)) {
/*  367 */               BlockPos blockPos1 = blockPos.relative(param1Rotation.rotate(Direction.EAST), 6);
/*  368 */               blockPos1 = blockPos1.relative(param1Rotation.rotate(Direction.SOUTH), 6);
/*  369 */               param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", blockPos1, param1Rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  375 */       if (param1SimpleGrid2 != null) {
/*  376 */         for (b = 0; b < param1SimpleGrid1.height; b++) {
/*  377 */           for (byte b1 = 0; b1 < param1SimpleGrid1.width; b1++) {
/*  378 */             BlockPos blockPos = param1BlockPos;
/*  379 */             blockPos = blockPos.relative(param1Rotation.rotate(Direction.SOUTH), 8 + (b - this.startY) * 8);
/*  380 */             blockPos = blockPos.relative(param1Rotation.rotate(Direction.EAST), (b1 - this.startX) * 8);
/*      */ 
/*      */             
/*  383 */             boolean bool = WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid2, b1, b);
/*      */             
/*  385 */             if (WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1, b) && bool) {
/*      */               
/*  387 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1 + 1, b)) {
/*  388 */                 BlockPos blockPos1 = blockPos.relative(param1Rotation.rotate(Direction.EAST), 7);
/*  389 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", blockPos1, param1Rotation));
/*      */               } 
/*  391 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1 - 1, b)) {
/*  392 */                 BlockPos blockPos1 = blockPos.relative(param1Rotation.rotate(Direction.WEST), 1);
/*  393 */                 blockPos1 = blockPos1.relative(param1Rotation.rotate(Direction.SOUTH), 6);
/*  394 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", blockPos1, param1Rotation.getRotated(Rotation.CLOCKWISE_180)));
/*      */               } 
/*  396 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1, b - 1)) {
/*  397 */                 BlockPos blockPos1 = blockPos.relative(param1Rotation.rotate(Direction.WEST), 0);
/*  398 */                 blockPos1 = blockPos1.relative(param1Rotation.rotate(Direction.NORTH), 1);
/*  399 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", blockPos1, param1Rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*      */               } 
/*  401 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1, b + 1)) {
/*  402 */                 BlockPos blockPos1 = blockPos.relative(param1Rotation.rotate(Direction.EAST), 6);
/*  403 */                 blockPos1 = blockPos1.relative(param1Rotation.rotate(Direction.SOUTH), 7);
/*  404 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", blockPos1, param1Rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */               } 
/*      */               
/*  407 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1 + 1, b)) {
/*  408 */                 if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1, b - 1)) {
/*  409 */                   BlockPos blockPos1 = blockPos.relative(param1Rotation.rotate(Direction.EAST), 7);
/*  410 */                   blockPos1 = blockPos1.relative(param1Rotation.rotate(Direction.NORTH), 2);
/*  411 */                   param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", blockPos1, param1Rotation));
/*      */                 } 
/*  413 */                 if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1, b + 1)) {
/*  414 */                   BlockPos blockPos1 = blockPos.relative(param1Rotation.rotate(Direction.EAST), 8);
/*  415 */                   blockPos1 = blockPos1.relative(param1Rotation.rotate(Direction.SOUTH), 7);
/*  416 */                   param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", blockPos1, param1Rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */                 } 
/*      */               } 
/*  419 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1 - 1, b)) {
/*  420 */                 if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1, b - 1)) {
/*  421 */                   BlockPos blockPos1 = blockPos.relative(param1Rotation.rotate(Direction.WEST), 2);
/*  422 */                   blockPos1 = blockPos1.relative(param1Rotation.rotate(Direction.NORTH), 1);
/*  423 */                   param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", blockPos1, param1Rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*      */                 } 
/*  425 */                 if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1, b + 1)) {
/*  426 */                   BlockPos blockPos1 = blockPos.relative(param1Rotation.rotate(Direction.WEST), 1);
/*  427 */                   blockPos1 = blockPos1.relative(param1Rotation.rotate(Direction.SOUTH), 8);
/*  428 */                   param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", blockPos1, param1Rotation.getRotated(Rotation.CLOCKWISE_180)));
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */       
/*  436 */       for (b = 0; b < param1SimpleGrid1.height; b++) {
/*  437 */         for (byte b1 = 0; b1 < param1SimpleGrid1.width; b1++) {
/*  438 */           BlockPos blockPos = param1BlockPos;
/*  439 */           blockPos = blockPos.relative(param1Rotation.rotate(Direction.SOUTH), 8 + (b - this.startY) * 8);
/*  440 */           blockPos = blockPos.relative(param1Rotation.rotate(Direction.EAST), (b1 - this.startX) * 8);
/*      */ 
/*      */           
/*  443 */           boolean bool = (param1SimpleGrid2 != null && WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid2, b1, b)) ? true : false;
/*      */           
/*  445 */           if (WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1, b) && !bool) {
/*  446 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1 + 1, b)) {
/*  447 */               BlockPos blockPos1 = blockPos.relative(param1Rotation.rotate(Direction.EAST), 6);
/*  448 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1, b + 1)) {
/*  449 */                 BlockPos blockPos2 = blockPos1.relative(param1Rotation.rotate(Direction.SOUTH), 6);
/*  450 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", blockPos2, param1Rotation));
/*  451 */               } else if (WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1 + 1, b + 1)) {
/*  452 */                 BlockPos blockPos2 = blockPos1.relative(param1Rotation.rotate(Direction.SOUTH), 5);
/*  453 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", blockPos2, param1Rotation));
/*      */               } 
/*  455 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1, b - 1)) {
/*  456 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", blockPos1, param1Rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*  457 */               } else if (WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1 + 1, b - 1)) {
/*  458 */                 BlockPos blockPos2 = blockPos.relative(param1Rotation.rotate(Direction.EAST), 9);
/*  459 */                 blockPos2 = blockPos2.relative(param1Rotation.rotate(Direction.NORTH), 2);
/*  460 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", blockPos2, param1Rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */               } 
/*      */             } 
/*  463 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1 - 1, b)) {
/*  464 */               BlockPos blockPos1 = blockPos.relative(param1Rotation.rotate(Direction.EAST), 0);
/*  465 */               blockPos1 = blockPos1.relative(param1Rotation.rotate(Direction.SOUTH), 0);
/*  466 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1, b + 1)) {
/*  467 */                 BlockPos blockPos2 = blockPos1.relative(param1Rotation.rotate(Direction.SOUTH), 6);
/*  468 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", blockPos2, param1Rotation.getRotated(Rotation.CLOCKWISE_90)));
/*  469 */               } else if (WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1 - 1, b + 1)) {
/*  470 */                 BlockPos blockPos2 = blockPos1.relative(param1Rotation.rotate(Direction.SOUTH), 8);
/*  471 */                 blockPos2 = blockPos2.relative(param1Rotation.rotate(Direction.WEST), 3);
/*  472 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", blockPos2, param1Rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*      */               } 
/*  474 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1, b - 1)) {
/*  475 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", blockPos1, param1Rotation.getRotated(Rotation.CLOCKWISE_180)));
/*  476 */               } else if (WoodlandMansionPieces.MansionGrid.isHouse(param1SimpleGrid1, b1 - 1, b - 1)) {
/*  477 */                 BlockPos blockPos2 = blockPos1.relative(param1Rotation.rotate(Direction.SOUTH), 1);
/*  478 */                 param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", blockPos2, param1Rotation.getRotated(Rotation.CLOCKWISE_180)));
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     private void entrance(List<WoodlandMansionPieces.WoodlandMansionPiece> param1List, WoodlandMansionPieces.PlacementData param1PlacementData) {
/*  487 */       Direction direction = param1PlacementData.rotation.rotate(Direction.WEST);
/*  488 */       param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "entrance", param1PlacementData.position.relative(direction, 9), param1PlacementData.rotation));
/*  489 */       param1PlacementData.position = param1PlacementData.position.relative(param1PlacementData.rotation.rotate(Direction.SOUTH), 16);
/*      */     }
/*      */     
/*      */     private void traverseWallPiece(List<WoodlandMansionPieces.WoodlandMansionPiece> param1List, WoodlandMansionPieces.PlacementData param1PlacementData) {
/*  493 */       param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1PlacementData.wallType, param1PlacementData.position.relative(param1PlacementData.rotation.rotate(Direction.EAST), 7), param1PlacementData.rotation));
/*  494 */       param1PlacementData.position = param1PlacementData.position.relative(param1PlacementData.rotation.rotate(Direction.SOUTH), 8);
/*      */     }
/*      */     
/*      */     private void traverseTurn(List<WoodlandMansionPieces.WoodlandMansionPiece> param1List, WoodlandMansionPieces.PlacementData param1PlacementData) {
/*  498 */       param1PlacementData.position = param1PlacementData.position.relative(param1PlacementData.rotation.rotate(Direction.SOUTH), -1);
/*  499 */       param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "wall_corner", param1PlacementData.position, param1PlacementData.rotation));
/*  500 */       param1PlacementData.position = param1PlacementData.position.relative(param1PlacementData.rotation.rotate(Direction.SOUTH), -7);
/*  501 */       param1PlacementData.position = param1PlacementData.position.relative(param1PlacementData.rotation.rotate(Direction.WEST), -6);
/*  502 */       param1PlacementData.rotation = param1PlacementData.rotation.getRotated(Rotation.CLOCKWISE_90);
/*      */     }
/*      */     
/*      */     private void traverseInnerTurn(List<WoodlandMansionPieces.WoodlandMansionPiece> param1List, WoodlandMansionPieces.PlacementData param1PlacementData) {
/*  506 */       param1PlacementData.position = param1PlacementData.position.relative(param1PlacementData.rotation.rotate(Direction.SOUTH), 6);
/*  507 */       param1PlacementData.position = param1PlacementData.position.relative(param1PlacementData.rotation.rotate(Direction.EAST), 8);
/*  508 */       param1PlacementData.rotation = param1PlacementData.rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*      */     }
/*      */     
/*      */     private void addRoom1x1(List<WoodlandMansionPieces.WoodlandMansionPiece> param1List, BlockPos param1BlockPos, Rotation param1Rotation, Direction param1Direction, WoodlandMansionPieces.FloorRoomCollection param1FloorRoomCollection) {
/*  512 */       Rotation rotation = Rotation.NONE;
/*  513 */       String str = param1FloorRoomCollection.get1x1(this.random);
/*  514 */       if (param1Direction != Direction.EAST) {
/*  515 */         if (param1Direction == Direction.NORTH) {
/*  516 */           rotation = rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*  517 */         } else if (param1Direction == Direction.WEST) {
/*  518 */           rotation = rotation.getRotated(Rotation.CLOCKWISE_180);
/*  519 */         } else if (param1Direction == Direction.SOUTH) {
/*  520 */           rotation = rotation.getRotated(Rotation.CLOCKWISE_90);
/*      */         } else {
/*      */           
/*  523 */           str = param1FloorRoomCollection.get1x1Secret(this.random);
/*      */         } 
/*      */       }
/*  526 */       BlockPos blockPos1 = StructureTemplate.getZeroPositionWithTransform(new BlockPos(1, 0, 0), Mirror.NONE, rotation, 7, 7);
/*  527 */       rotation = rotation.getRotated(param1Rotation);
/*  528 */       blockPos1 = blockPos1.rotate(param1Rotation);
/*  529 */       BlockPos blockPos2 = param1BlockPos.offset(blockPos1.getX(), 0, blockPos1.getZ());
/*  530 */       param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, str, blockPos2, rotation));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void addRoom1x2(List<WoodlandMansionPieces.WoodlandMansionPiece> param1List, BlockPos param1BlockPos, Rotation param1Rotation, Direction param1Direction1, Direction param1Direction2, WoodlandMansionPieces.FloorRoomCollection param1FloorRoomCollection, boolean param1Boolean) {
/*  537 */       if (param1Direction2 == Direction.EAST && param1Direction1 == Direction.SOUTH) {
/*      */ 
/*      */         
/*  540 */         BlockPos blockPos = param1BlockPos.relative(param1Rotation.rotate(Direction.EAST), 1);
/*  541 */         param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1FloorRoomCollection.get1x2SideEntrance(this.random, param1Boolean), blockPos, param1Rotation));
/*  542 */       } else if (param1Direction2 == Direction.EAST && param1Direction1 == Direction.NORTH) {
/*      */ 
/*      */         
/*  545 */         BlockPos blockPos = param1BlockPos.relative(param1Rotation.rotate(Direction.EAST), 1);
/*  546 */         blockPos = blockPos.relative(param1Rotation.rotate(Direction.SOUTH), 6);
/*  547 */         param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1FloorRoomCollection.get1x2SideEntrance(this.random, param1Boolean), blockPos, param1Rotation, Mirror.LEFT_RIGHT));
/*  548 */       } else if (param1Direction2 == Direction.WEST && param1Direction1 == Direction.NORTH) {
/*      */ 
/*      */         
/*  551 */         BlockPos blockPos = param1BlockPos.relative(param1Rotation.rotate(Direction.EAST), 7);
/*  552 */         blockPos = blockPos.relative(param1Rotation.rotate(Direction.SOUTH), 6);
/*  553 */         param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1FloorRoomCollection.get1x2SideEntrance(this.random, param1Boolean), blockPos, param1Rotation.getRotated(Rotation.CLOCKWISE_180)));
/*  554 */       } else if (param1Direction2 == Direction.WEST && param1Direction1 == Direction.SOUTH) {
/*      */ 
/*      */         
/*  557 */         BlockPos blockPos = param1BlockPos.relative(param1Rotation.rotate(Direction.EAST), 7);
/*  558 */         param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1FloorRoomCollection.get1x2SideEntrance(this.random, param1Boolean), blockPos, param1Rotation, Mirror.FRONT_BACK));
/*  559 */       } else if (param1Direction2 == Direction.SOUTH && param1Direction1 == Direction.EAST) {
/*      */ 
/*      */         
/*  562 */         BlockPos blockPos = param1BlockPos.relative(param1Rotation.rotate(Direction.EAST), 1);
/*  563 */         param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1FloorRoomCollection.get1x2SideEntrance(this.random, param1Boolean), blockPos, param1Rotation.getRotated(Rotation.CLOCKWISE_90), Mirror.LEFT_RIGHT));
/*  564 */       } else if (param1Direction2 == Direction.SOUTH && param1Direction1 == Direction.WEST) {
/*      */ 
/*      */         
/*  567 */         BlockPos blockPos = param1BlockPos.relative(param1Rotation.rotate(Direction.EAST), 7);
/*  568 */         param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1FloorRoomCollection.get1x2SideEntrance(this.random, param1Boolean), blockPos, param1Rotation.getRotated(Rotation.CLOCKWISE_90)));
/*  569 */       } else if (param1Direction2 == Direction.NORTH && param1Direction1 == Direction.WEST) {
/*      */ 
/*      */         
/*  572 */         BlockPos blockPos = param1BlockPos.relative(param1Rotation.rotate(Direction.EAST), 7);
/*  573 */         blockPos = blockPos.relative(param1Rotation.rotate(Direction.SOUTH), 6);
/*  574 */         param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1FloorRoomCollection.get1x2SideEntrance(this.random, param1Boolean), blockPos, param1Rotation.getRotated(Rotation.CLOCKWISE_90), Mirror.FRONT_BACK));
/*  575 */       } else if (param1Direction2 == Direction.NORTH && param1Direction1 == Direction.EAST) {
/*      */ 
/*      */         
/*  578 */         BlockPos blockPos = param1BlockPos.relative(param1Rotation.rotate(Direction.EAST), 1);
/*  579 */         blockPos = blockPos.relative(param1Rotation.rotate(Direction.SOUTH), 6);
/*  580 */         param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1FloorRoomCollection.get1x2SideEntrance(this.random, param1Boolean), blockPos, param1Rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*  581 */       } else if (param1Direction2 == Direction.SOUTH && param1Direction1 == Direction.NORTH) {
/*      */ 
/*      */ 
/*      */         
/*  585 */         BlockPos blockPos = param1BlockPos.relative(param1Rotation.rotate(Direction.EAST), 1);
/*  586 */         blockPos = blockPos.relative(param1Rotation.rotate(Direction.NORTH), 8);
/*  587 */         param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1FloorRoomCollection.get1x2FrontEntrance(this.random, param1Boolean), blockPos, param1Rotation));
/*  588 */       } else if (param1Direction2 == Direction.NORTH && param1Direction1 == Direction.SOUTH) {
/*      */ 
/*      */ 
/*      */         
/*  592 */         BlockPos blockPos = param1BlockPos.relative(param1Rotation.rotate(Direction.EAST), 7);
/*  593 */         blockPos = blockPos.relative(param1Rotation.rotate(Direction.SOUTH), 14);
/*  594 */         param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1FloorRoomCollection.get1x2FrontEntrance(this.random, param1Boolean), blockPos, param1Rotation.getRotated(Rotation.CLOCKWISE_180)));
/*  595 */       } else if (param1Direction2 == Direction.WEST && param1Direction1 == Direction.EAST) {
/*      */         
/*  597 */         BlockPos blockPos = param1BlockPos.relative(param1Rotation.rotate(Direction.EAST), 15);
/*  598 */         param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1FloorRoomCollection.get1x2FrontEntrance(this.random, param1Boolean), blockPos, param1Rotation.getRotated(Rotation.CLOCKWISE_90)));
/*  599 */       } else if (param1Direction2 == Direction.EAST && param1Direction1 == Direction.WEST) {
/*      */         
/*  601 */         BlockPos blockPos = param1BlockPos.relative(param1Rotation.rotate(Direction.WEST), 7);
/*  602 */         blockPos = blockPos.relative(param1Rotation.rotate(Direction.SOUTH), 6);
/*  603 */         param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1FloorRoomCollection.get1x2FrontEntrance(this.random, param1Boolean), blockPos, param1Rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*  604 */       } else if (param1Direction2 == Direction.UP && param1Direction1 == Direction.EAST) {
/*      */         
/*  606 */         BlockPos blockPos = param1BlockPos.relative(param1Rotation.rotate(Direction.EAST), 15);
/*  607 */         param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1FloorRoomCollection.get1x2Secret(this.random), blockPos, param1Rotation.getRotated(Rotation.CLOCKWISE_90)));
/*  608 */       } else if (param1Direction2 == Direction.UP && param1Direction1 == Direction.SOUTH) {
/*      */ 
/*      */ 
/*      */         
/*  612 */         BlockPos blockPos = param1BlockPos.relative(param1Rotation.rotate(Direction.EAST), 1);
/*  613 */         blockPos = blockPos.relative(param1Rotation.rotate(Direction.NORTH), 0);
/*  614 */         param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1FloorRoomCollection.get1x2Secret(this.random), blockPos, param1Rotation));
/*      */       } 
/*      */     }
/*      */     
/*      */     private void addRoom2x2(List<WoodlandMansionPieces.WoodlandMansionPiece> param1List, BlockPos param1BlockPos, Rotation param1Rotation, Direction param1Direction1, Direction param1Direction2, WoodlandMansionPieces.FloorRoomCollection param1FloorRoomCollection) {
/*  619 */       byte b1 = 0;
/*  620 */       byte b2 = 0;
/*  621 */       Rotation rotation = param1Rotation;
/*  622 */       Mirror mirror = Mirror.NONE;
/*      */ 
/*      */ 
/*      */       
/*  626 */       if (param1Direction2 == Direction.EAST && param1Direction1 == Direction.SOUTH) {
/*      */ 
/*      */         
/*  629 */         b1 = -7;
/*  630 */       } else if (param1Direction2 == Direction.EAST && param1Direction1 == Direction.NORTH) {
/*      */ 
/*      */         
/*  633 */         b1 = -7;
/*  634 */         b2 = 6;
/*  635 */         mirror = Mirror.LEFT_RIGHT;
/*  636 */       } else if (param1Direction2 == Direction.NORTH && param1Direction1 == Direction.EAST) {
/*      */ 
/*      */ 
/*      */         
/*  640 */         b1 = 1;
/*  641 */         b2 = 14;
/*  642 */         rotation = param1Rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*  643 */       } else if (param1Direction2 == Direction.NORTH && param1Direction1 == Direction.WEST) {
/*      */ 
/*      */ 
/*      */         
/*  647 */         b1 = 7;
/*  648 */         b2 = 14;
/*  649 */         rotation = param1Rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*  650 */         mirror = Mirror.LEFT_RIGHT;
/*  651 */       } else if (param1Direction2 == Direction.SOUTH && param1Direction1 == Direction.WEST) {
/*      */ 
/*      */ 
/*      */         
/*  655 */         b1 = 7;
/*  656 */         b2 = -8;
/*  657 */         rotation = param1Rotation.getRotated(Rotation.CLOCKWISE_90);
/*  658 */       } else if (param1Direction2 == Direction.SOUTH && param1Direction1 == Direction.EAST) {
/*      */ 
/*      */ 
/*      */         
/*  662 */         b1 = 1;
/*  663 */         b2 = -8;
/*  664 */         rotation = param1Rotation.getRotated(Rotation.CLOCKWISE_90);
/*  665 */         mirror = Mirror.LEFT_RIGHT;
/*  666 */       } else if (param1Direction2 == Direction.WEST && param1Direction1 == Direction.NORTH) {
/*      */ 
/*      */         
/*  669 */         b1 = 15;
/*  670 */         b2 = 6;
/*  671 */         rotation = param1Rotation.getRotated(Rotation.CLOCKWISE_180);
/*  672 */       } else if (param1Direction2 == Direction.WEST && param1Direction1 == Direction.SOUTH) {
/*      */ 
/*      */         
/*  675 */         b1 = 15;
/*  676 */         mirror = Mirror.FRONT_BACK;
/*      */       } 
/*      */       
/*  679 */       BlockPos blockPos = param1BlockPos.relative(param1Rotation.rotate(Direction.EAST), b1);
/*  680 */       blockPos = blockPos.relative(param1Rotation.rotate(Direction.SOUTH), b2);
/*  681 */       param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1FloorRoomCollection.get2x2(this.random), blockPos, rotation, mirror));
/*      */     }
/*      */     
/*      */     private void addRoom2x2Secret(List<WoodlandMansionPieces.WoodlandMansionPiece> param1List, BlockPos param1BlockPos, Rotation param1Rotation, WoodlandMansionPieces.FloorRoomCollection param1FloorRoomCollection) {
/*  685 */       BlockPos blockPos = param1BlockPos.relative(param1Rotation.rotate(Direction.EAST), 1);
/*  686 */       param1List.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, param1FloorRoomCollection.get2x2Secret(this.random), blockPos, param1Rotation, Mirror.NONE));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class MansionGrid
/*      */   {
/*      */     private static final int DEFAULT_SIZE = 11;
/*      */     
/*      */     private static final int CLEAR = 0;
/*      */     
/*      */     private static final int CORRIDOR = 1;
/*      */     
/*      */     private static final int ROOM = 2;
/*      */     private static final int START_ROOM = 3;
/*      */     private static final int TEST_ROOM = 4;
/*      */     private static final int BLOCKED = 5;
/*      */     private static final int ROOM_1x1 = 65536;
/*      */     private static final int ROOM_1x2 = 131072;
/*      */     private static final int ROOM_2x2 = 262144;
/*      */     private static final int ROOM_ORIGIN_FLAG = 1048576;
/*      */     private static final int ROOM_DOOR_FLAG = 2097152;
/*      */     private static final int ROOM_STAIRS_FLAG = 4194304;
/*      */     private static final int ROOM_CORRIDOR_FLAG = 8388608;
/*      */     private static final int ROOM_TYPE_MASK = 983040;
/*      */     private static final int ROOM_ID_MASK = 65535;
/*      */     private final RandomSource random;
/*      */     final WoodlandMansionPieces.SimpleGrid baseGrid;
/*      */     final WoodlandMansionPieces.SimpleGrid thirdFloorGrid;
/*      */     final WoodlandMansionPieces.SimpleGrid[] floorRooms;
/*      */     final int entranceX;
/*      */     final int entranceY;
/*      */     
/*      */     public MansionGrid(RandomSource param1RandomSource) {
/*  720 */       this.random = param1RandomSource;
/*      */       
/*  722 */       byte b = 11;
/*  723 */       this.entranceX = 7;
/*  724 */       this.entranceY = 4;
/*      */       
/*  726 */       this.baseGrid = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/*  727 */       this.baseGrid.set(this.entranceX, this.entranceY, this.entranceX + 1, this.entranceY + 1, 3);
/*  728 */       this.baseGrid.set(this.entranceX - 1, this.entranceY, this.entranceX - 1, this.entranceY + 1, 2);
/*  729 */       this.baseGrid.set(this.entranceX + 2, this.entranceY - 2, this.entranceX + 3, this.entranceY + 3, 5);
/*  730 */       this.baseGrid.set(this.entranceX + 1, this.entranceY - 2, this.entranceX + 1, this.entranceY - 1, 1);
/*  731 */       this.baseGrid.set(this.entranceX + 1, this.entranceY + 2, this.entranceX + 1, this.entranceY + 3, 1);
/*  732 */       this.baseGrid.set(this.entranceX - 1, this.entranceY - 1, 1);
/*  733 */       this.baseGrid.set(this.entranceX - 1, this.entranceY + 2, 1);
/*      */       
/*  735 */       this.baseGrid.set(0, 0, 11, 1, 5);
/*  736 */       this.baseGrid.set(0, 9, 11, 11, 5);
/*      */       
/*  738 */       recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY - 2, Direction.WEST, 6);
/*  739 */       recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY + 3, Direction.WEST, 6);
/*  740 */       recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY - 1, Direction.WEST, 3);
/*  741 */       recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY + 2, Direction.WEST, 3);
/*  742 */       while (cleanEdges(this.baseGrid));
/*      */ 
/*      */       
/*  745 */       this.floorRooms = new WoodlandMansionPieces.SimpleGrid[3];
/*  746 */       this.floorRooms[0] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/*  747 */       this.floorRooms[1] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/*  748 */       this.floorRooms[2] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/*  749 */       identifyRooms(this.baseGrid, this.floorRooms[0]);
/*  750 */       identifyRooms(this.baseGrid, this.floorRooms[1]);
/*      */ 
/*      */       
/*  753 */       this.floorRooms[0].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
/*  754 */       this.floorRooms[1].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
/*      */       
/*  756 */       this.thirdFloorGrid = new WoodlandMansionPieces.SimpleGrid(this.baseGrid.width, this.baseGrid.height, 5);
/*  757 */       setupThirdFloor();
/*  758 */       identifyRooms(this.thirdFloorGrid, this.floorRooms[2]);
/*      */     }
/*      */     
/*      */     public static boolean isHouse(WoodlandMansionPieces.SimpleGrid param1SimpleGrid, int param1Int1, int param1Int2) {
/*  762 */       int i = param1SimpleGrid.get(param1Int1, param1Int2);
/*  763 */       return (i == 1 || i == 2 || i == 3 || i == 4);
/*      */     }
/*      */     
/*      */     public boolean isRoomId(WoodlandMansionPieces.SimpleGrid param1SimpleGrid, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
/*  767 */       return ((this.floorRooms[param1Int3].get(param1Int1, param1Int2) & 0xFFFF) == param1Int4);
/*      */     }
/*      */     
/*      */     public Direction get1x2RoomDirection(WoodlandMansionPieces.SimpleGrid param1SimpleGrid, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
/*  771 */       for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  772 */         if (isRoomId(param1SimpleGrid, param1Int1 + direction.getStepX(), param1Int2 + direction.getStepZ(), param1Int3, param1Int4)) {
/*  773 */           return direction;
/*      */         }
/*      */       } 
/*  776 */       return null;
/*      */     }
/*      */     
/*      */     private void recursiveCorridor(WoodlandMansionPieces.SimpleGrid param1SimpleGrid, int param1Int1, int param1Int2, Direction param1Direction, int param1Int3) {
/*  780 */       if (param1Int3 <= 0) {
/*      */         return;
/*      */       }
/*      */       
/*  784 */       param1SimpleGrid.set(param1Int1, param1Int2, 1);
/*  785 */       param1SimpleGrid.setif(param1Int1 + param1Direction.getStepX(), param1Int2 + param1Direction.getStepZ(), 0, 1);
/*      */       
/*  787 */       for (byte b = 0; b < 8; b++) {
/*  788 */         Direction direction = Direction.from2DDataValue(this.random.nextInt(4));
/*  789 */         if (direction != param1Direction.getOpposite())
/*      */         {
/*      */           
/*  792 */           if (direction != Direction.EAST || !this.random.nextBoolean()) {
/*      */ 
/*      */ 
/*      */             
/*  796 */             int i = param1Int1 + param1Direction.getStepX();
/*  797 */             int j = param1Int2 + param1Direction.getStepZ();
/*  798 */             if (param1SimpleGrid.get(i + direction.getStepX(), j + direction.getStepZ()) == 0 && param1SimpleGrid.get(i + direction.getStepX() * 2, j + direction.getStepZ() * 2) == 0) {
/*  799 */               recursiveCorridor(param1SimpleGrid, param1Int1 + param1Direction.getStepX() + direction.getStepX(), param1Int2 + param1Direction.getStepZ() + direction.getStepZ(), direction, param1Int3 - 1); break;
/*      */             } 
/*      */           }  } 
/*      */       } 
/*  803 */       Direction direction1 = param1Direction.getClockWise();
/*  804 */       Direction direction2 = param1Direction.getCounterClockWise();
/*  805 */       param1SimpleGrid.setif(param1Int1 + direction1.getStepX(), param1Int2 + direction1.getStepZ(), 0, 2);
/*  806 */       param1SimpleGrid.setif(param1Int1 + direction2.getStepX(), param1Int2 + direction2.getStepZ(), 0, 2);
/*      */       
/*  808 */       param1SimpleGrid.setif(param1Int1 + param1Direction.getStepX() + direction1.getStepX(), param1Int2 + param1Direction.getStepZ() + direction1.getStepZ(), 0, 2);
/*  809 */       param1SimpleGrid.setif(param1Int1 + param1Direction.getStepX() + direction2.getStepX(), param1Int2 + param1Direction.getStepZ() + direction2.getStepZ(), 0, 2);
/*  810 */       param1SimpleGrid.setif(param1Int1 + param1Direction.getStepX() * 2, param1Int2 + param1Direction.getStepZ() * 2, 0, 2);
/*  811 */       param1SimpleGrid.setif(param1Int1 + direction1.getStepX() * 2, param1Int2 + direction1.getStepZ() * 2, 0, 2);
/*  812 */       param1SimpleGrid.setif(param1Int1 + direction2.getStepX() * 2, param1Int2 + direction2.getStepZ() * 2, 0, 2);
/*      */     }
/*      */     
/*      */     private boolean cleanEdges(WoodlandMansionPieces.SimpleGrid param1SimpleGrid) {
/*  816 */       boolean bool = false;
/*  817 */       for (byte b = 0; b < param1SimpleGrid.height; b++) {
/*  818 */         for (byte b1 = 0; b1 < param1SimpleGrid.width; b1++) {
/*  819 */           if (param1SimpleGrid.get(b1, b) == 0) {
/*  820 */             int i = 0;
/*  821 */             i += isHouse(param1SimpleGrid, b1 + 1, b) ? 1 : 0;
/*  822 */             i += isHouse(param1SimpleGrid, b1 - 1, b) ? 1 : 0;
/*  823 */             i += isHouse(param1SimpleGrid, b1, b + 1) ? 1 : 0;
/*  824 */             i += isHouse(param1SimpleGrid, b1, b - 1) ? 1 : 0;
/*      */             
/*  826 */             if (i >= 3) {
/*      */               
/*  828 */               param1SimpleGrid.set(b1, b, 2);
/*  829 */               bool = true;
/*  830 */             } else if (i == 2) {
/*      */               
/*  832 */               int j = 0;
/*  833 */               j += isHouse(param1SimpleGrid, b1 + 1, b + 1) ? 1 : 0;
/*  834 */               j += isHouse(param1SimpleGrid, b1 - 1, b + 1) ? 1 : 0;
/*  835 */               j += isHouse(param1SimpleGrid, b1 + 1, b - 1) ? 1 : 0;
/*  836 */               j += isHouse(param1SimpleGrid, b1 - 1, b - 1) ? 1 : 0;
/*  837 */               if (j <= 1) {
/*  838 */                 param1SimpleGrid.set(b1, b, 2);
/*  839 */                 bool = true;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*  845 */       return bool;
/*      */     }
/*      */ 
/*      */     
/*      */     private void setupThirdFloor() {
/*  850 */       ArrayList<Tuple> arrayList = Lists.newArrayList();
/*  851 */       WoodlandMansionPieces.SimpleGrid simpleGrid = this.floorRooms[1];
/*  852 */       for (byte b1 = 0; b1 < this.thirdFloorGrid.height; b1++) {
/*  853 */         for (byte b = 0; b < this.thirdFloorGrid.width; b++) {
/*  854 */           int m = simpleGrid.get(b, b1);
/*  855 */           int n = m & 0xF0000;
/*  856 */           if (n == 131072 && (m & 0x200000) == 2097152) {
/*  857 */             arrayList.add(new Tuple(Integer.valueOf(b), Integer.valueOf(b1)));
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  862 */       if (arrayList.isEmpty()) {
/*      */         
/*  864 */         this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
/*      */         
/*      */         return;
/*      */       } 
/*  868 */       Tuple tuple = arrayList.get(this.random.nextInt(arrayList.size()));
/*  869 */       int i = simpleGrid.get(((Integer)tuple.getA()).intValue(), ((Integer)tuple.getB()).intValue());
/*  870 */       simpleGrid.set(((Integer)tuple.getA()).intValue(), ((Integer)tuple.getB()).intValue(), i | 0x400000);
/*  871 */       Direction direction1 = get1x2RoomDirection(this.baseGrid, ((Integer)tuple.getA()).intValue(), ((Integer)tuple.getB()).intValue(), 1, i & 0xFFFF);
/*  872 */       int j = ((Integer)tuple.getA()).intValue() + direction1.getStepX();
/*  873 */       int k = ((Integer)tuple.getB()).intValue() + direction1.getStepZ();
/*      */       
/*  875 */       for (byte b2 = 0; b2 < this.thirdFloorGrid.height; b2++) {
/*  876 */         for (byte b = 0; b < this.thirdFloorGrid.width; b++) {
/*  877 */           if (!isHouse(this.baseGrid, b, b2)) {
/*  878 */             this.thirdFloorGrid.set(b, b2, 5);
/*  879 */           } else if (b == ((Integer)tuple.getA()).intValue() && b2 == ((Integer)tuple.getB()).intValue()) {
/*  880 */             this.thirdFloorGrid.set(b, b2, 3);
/*  881 */           } else if (b == j && b2 == k) {
/*  882 */             this.thirdFloorGrid.set(b, b2, 3);
/*  883 */             this.floorRooms[2].set(b, b2, 8388608);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  888 */       ArrayList<Direction> arrayList1 = Lists.newArrayList();
/*  889 */       for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  890 */         if (this.thirdFloorGrid.get(j + direction.getStepX(), k + direction.getStepZ()) == 0) {
/*  891 */           arrayList1.add(direction);
/*      */         }
/*      */       } 
/*      */       
/*  895 */       if (arrayList1.isEmpty()) {
/*      */         
/*  897 */         this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
/*  898 */         simpleGrid.set(((Integer)tuple.getA()).intValue(), ((Integer)tuple.getB()).intValue(), i);
/*      */         return;
/*      */       } 
/*  901 */       Direction direction2 = arrayList1.get(this.random.nextInt(arrayList1.size()));
/*  902 */       recursiveCorridor(this.thirdFloorGrid, j + direction2.getStepX(), k + direction2.getStepZ(), direction2, 4);
/*  903 */       while (cleanEdges(this.thirdFloorGrid));
/*      */     }
/*      */ 
/*      */     
/*      */     private void identifyRooms(WoodlandMansionPieces.SimpleGrid param1SimpleGrid1, WoodlandMansionPieces.SimpleGrid param1SimpleGrid2) {
/*  908 */       ObjectArrayList objectArrayList = new ObjectArrayList(); byte b;
/*  909 */       for (b = 0; b < param1SimpleGrid1.height; b++) {
/*  910 */         for (byte b1 = 0; b1 < param1SimpleGrid1.width; b1++) {
/*  911 */           if (param1SimpleGrid1.get(b1, b) == 2) {
/*  912 */             objectArrayList.add(new Tuple(Integer.valueOf(b1), Integer.valueOf(b)));
/*      */           }
/*      */         } 
/*      */       } 
/*  916 */       Util.shuffle((List)objectArrayList, this.random);
/*      */       
/*  918 */       b = 10;
/*  919 */       for (ObjectListIterator<Tuple> objectListIterator = objectArrayList.iterator(); objectListIterator.hasNext(); ) { Tuple tuple = objectListIterator.next();
/*  920 */         int i = ((Integer)tuple.getA()).intValue();
/*  921 */         int j = ((Integer)tuple.getB()).intValue();
/*      */         
/*  923 */         if (param1SimpleGrid2.get(i, j) == 0) {
/*  924 */           int k = i;
/*  925 */           int m = i;
/*  926 */           int n = j;
/*  927 */           int i1 = j;
/*  928 */           int i2 = 65536;
/*  929 */           if (param1SimpleGrid2.get(i + 1, j) == 0 && param1SimpleGrid2.get(i, j + 1) == 0 && param1SimpleGrid2.get(i + 1, j + 1) == 0 && param1SimpleGrid1
/*  930 */             .get(i + 1, j) == 2 && param1SimpleGrid1.get(i, j + 1) == 2 && param1SimpleGrid1.get(i + 1, j + 1) == 2) {
/*      */             
/*  932 */             m++;
/*  933 */             i1++;
/*  934 */             i2 = 262144;
/*  935 */           } else if (param1SimpleGrid2.get(i - 1, j) == 0 && param1SimpleGrid2.get(i, j + 1) == 0 && param1SimpleGrid2.get(i - 1, j + 1) == 0 && param1SimpleGrid1
/*  936 */             .get(i - 1, j) == 2 && param1SimpleGrid1.get(i, j + 1) == 2 && param1SimpleGrid1.get(i - 1, j + 1) == 2) {
/*      */             
/*  938 */             k--;
/*  939 */             i1++;
/*  940 */             i2 = 262144;
/*  941 */           } else if (param1SimpleGrid2.get(i - 1, j) == 0 && param1SimpleGrid2.get(i, j - 1) == 0 && param1SimpleGrid2.get(i - 1, j - 1) == 0 && param1SimpleGrid1
/*  942 */             .get(i - 1, j) == 2 && param1SimpleGrid1.get(i, j - 1) == 2 && param1SimpleGrid1.get(i - 1, j - 1) == 2) {
/*      */             
/*  944 */             k--;
/*  945 */             n--;
/*  946 */             i2 = 262144;
/*  947 */           } else if (param1SimpleGrid2.get(i + 1, j) == 0 && param1SimpleGrid1.get(i + 1, j) == 2) {
/*  948 */             m++;
/*  949 */             i2 = 131072;
/*  950 */           } else if (param1SimpleGrid2.get(i, j + 1) == 0 && param1SimpleGrid1.get(i, j + 1) == 2) {
/*  951 */             i1++;
/*  952 */             i2 = 131072;
/*  953 */           } else if (param1SimpleGrid2.get(i - 1, j) == 0 && param1SimpleGrid1.get(i - 1, j) == 2) {
/*  954 */             k--;
/*  955 */             i2 = 131072;
/*  956 */           } else if (param1SimpleGrid2.get(i, j - 1) == 0 && param1SimpleGrid1.get(i, j - 1) == 2) {
/*  957 */             n--;
/*  958 */             i2 = 131072;
/*      */           } 
/*      */ 
/*      */           
/*  962 */           int i3 = this.random.nextBoolean() ? k : m;
/*  963 */           int i4 = this.random.nextBoolean() ? n : i1;
/*  964 */           int i5 = 2097152;
/*  965 */           if (!param1SimpleGrid1.edgesTo(i3, i4, 1)) {
/*  966 */             i3 = (i3 == k) ? m : k;
/*  967 */             i4 = (i4 == n) ? i1 : n;
/*  968 */             if (!param1SimpleGrid1.edgesTo(i3, i4, 1)) {
/*  969 */               i4 = (i4 == n) ? i1 : n;
/*  970 */               if (!param1SimpleGrid1.edgesTo(i3, i4, 1)) {
/*  971 */                 i3 = (i3 == k) ? m : k;
/*  972 */                 i4 = (i4 == n) ? i1 : n;
/*  973 */                 if (!param1SimpleGrid1.edgesTo(i3, i4, 1)) {
/*      */                   
/*  975 */                   i5 = 0;
/*  976 */                   i3 = k;
/*  977 */                   i4 = n;
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*  982 */           for (int i6 = n; i6 <= i1; i6++) {
/*  983 */             for (int i7 = k; i7 <= m; i7++) {
/*  984 */               if (i7 == i3 && i6 == i4) {
/*  985 */                 param1SimpleGrid2.set(i7, i6, 0x100000 | i5 | i2 | b);
/*      */               } else {
/*  987 */                 param1SimpleGrid2.set(i7, i6, i2 | b);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/*  992 */           b++;
/*      */         }  }
/*      */     
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SimpleGrid {
/*      */     private final int[][] grid;
/*      */     final int width;
/*      */     final int height;
/*      */     private final int valueIfOutside;
/*      */     
/*      */     public SimpleGrid(int param1Int1, int param1Int2, int param1Int3) {
/* 1005 */       this.width = param1Int1;
/* 1006 */       this.height = param1Int2;
/* 1007 */       this.valueIfOutside = param1Int3;
/* 1008 */       this.grid = new int[param1Int1][param1Int2];
/*      */     }
/*      */     
/*      */     public void set(int param1Int1, int param1Int2, int param1Int3) {
/* 1012 */       if (param1Int1 >= 0 && param1Int1 < this.width && param1Int2 >= 0 && param1Int2 < this.height) {
/* 1013 */         this.grid[param1Int1][param1Int2] = param1Int3;
/*      */       }
/*      */     }
/*      */     
/*      */     public void set(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5) {
/* 1018 */       for (int i = param1Int2; i <= param1Int4; i++) {
/* 1019 */         for (int j = param1Int1; j <= param1Int3; j++) {
/* 1020 */           set(j, i, param1Int5);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public int get(int param1Int1, int param1Int2) {
/* 1026 */       if (param1Int1 >= 0 && param1Int1 < this.width && param1Int2 >= 0 && param1Int2 < this.height) {
/* 1027 */         return this.grid[param1Int1][param1Int2];
/*      */       }
/* 1029 */       return this.valueIfOutside;
/*      */     }
/*      */     
/*      */     public void setif(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
/* 1033 */       if (get(param1Int1, param1Int2) == param1Int3) {
/* 1034 */         set(param1Int1, param1Int2, param1Int4);
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean edgesTo(int param1Int1, int param1Int2, int param1Int3) {
/* 1039 */       return (get(param1Int1 - 1, param1Int2) == param1Int3 || get(param1Int1 + 1, param1Int2) == param1Int3 || get(param1Int1, param1Int2 + 1) == param1Int3 || get(param1Int1, param1Int2 - 1) == param1Int3);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static abstract class FloorRoomCollection
/*      */   {
/*      */     public abstract String get1x1(RandomSource param1RandomSource);
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract String get1x1Secret(RandomSource param1RandomSource);
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract String get1x2SideEntrance(RandomSource param1RandomSource, boolean param1Boolean);
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract String get1x2FrontEntrance(RandomSource param1RandomSource, boolean param1Boolean);
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract String get1x2Secret(RandomSource param1RandomSource);
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract String get2x2(RandomSource param1RandomSource);
/*      */ 
/*      */     
/*      */     public abstract String get2x2Secret(RandomSource param1RandomSource);
/*      */   }
/*      */ 
/*      */   
/*      */   private static class FirstFloorRoomCollection
/*      */     extends FloorRoomCollection
/*      */   {
/*      */     public String get1x1(RandomSource param1RandomSource) {
/* 1078 */       return "1x1_a" + param1RandomSource.nextInt(5) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x1Secret(RandomSource param1RandomSource) {
/* 1083 */       return "1x1_as" + param1RandomSource.nextInt(4) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2SideEntrance(RandomSource param1RandomSource, boolean param1Boolean) {
/* 1088 */       return "1x2_a" + param1RandomSource.nextInt(9) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2FrontEntrance(RandomSource param1RandomSource, boolean param1Boolean) {
/* 1093 */       return "1x2_b" + param1RandomSource.nextInt(5) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2Secret(RandomSource param1RandomSource) {
/* 1098 */       return "1x2_s" + param1RandomSource.nextInt(2) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get2x2(RandomSource param1RandomSource) {
/* 1103 */       return "2x2_a" + param1RandomSource.nextInt(4) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get2x2Secret(RandomSource param1RandomSource) {
/* 1108 */       return "2x2_s1";
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SecondFloorRoomCollection
/*      */     extends FloorRoomCollection {
/*      */     public String get1x1(RandomSource param1RandomSource) {
/* 1115 */       return "1x1_b" + param1RandomSource.nextInt(5) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x1Secret(RandomSource param1RandomSource) {
/* 1120 */       return "1x1_as" + param1RandomSource.nextInt(4) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2SideEntrance(RandomSource param1RandomSource, boolean param1Boolean) {
/* 1125 */       if (param1Boolean) {
/* 1126 */         return "1x2_c_stairs";
/*      */       }
/* 1128 */       return "1x2_c" + param1RandomSource.nextInt(4) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2FrontEntrance(RandomSource param1RandomSource, boolean param1Boolean) {
/* 1133 */       if (param1Boolean) {
/* 1134 */         return "1x2_d_stairs";
/*      */       }
/* 1136 */       return "1x2_d" + param1RandomSource.nextInt(5) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get1x2Secret(RandomSource param1RandomSource) {
/* 1141 */       return "1x2_se" + param1RandomSource.nextInt(1) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get2x2(RandomSource param1RandomSource) {
/* 1146 */       return "2x2_b" + param1RandomSource.nextInt(5) + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String get2x2Secret(RandomSource param1RandomSource) {
/* 1151 */       return "2x2_s1";
/*      */     }
/*      */   }
/*      */   
/*      */   private static class ThirdFloorRoomCollection extends SecondFloorRoomCollection {}
/*      */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */