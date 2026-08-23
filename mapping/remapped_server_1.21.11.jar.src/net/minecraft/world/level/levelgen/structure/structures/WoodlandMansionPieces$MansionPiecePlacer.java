/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MansionPiecePlacer
/*     */ {
/*     */   private final StructureTemplateManager structureTemplateManager;
/*     */   private final RandomSource random;
/*     */   private int startX;
/*     */   private int startY;
/*     */   
/*     */   public MansionPiecePlacer(StructureTemplateManager paramStructureTemplateManager, RandomSource paramRandomSource) {
/* 140 */     this.structureTemplateManager = paramStructureTemplateManager;
/* 141 */     this.random = paramRandomSource;
/*     */   }
/*     */   
/*     */   public void createMansion(BlockPos paramBlockPos, Rotation paramRotation, List<WoodlandMansionPieces.WoodlandMansionPiece> paramList, WoodlandMansionPieces.MansionGrid paramMansionGrid) {
/* 145 */     WoodlandMansionPieces.PlacementData placementData1 = new WoodlandMansionPieces.PlacementData();
/* 146 */     placementData1.position = paramBlockPos;
/* 147 */     placementData1.rotation = paramRotation;
/* 148 */     placementData1.wallType = "wall_flat";
/*     */     
/* 150 */     WoodlandMansionPieces.PlacementData placementData2 = new WoodlandMansionPieces.PlacementData();
/*     */ 
/*     */     
/* 153 */     entrance(paramList, placementData1);
/* 154 */     placementData2.position = placementData1.position.above(8);
/* 155 */     placementData2.rotation = placementData1.rotation;
/* 156 */     placementData2.wallType = "wall_window";
/*     */     
/* 158 */     if (!paramList.isEmpty());
/*     */ 
/*     */ 
/*     */     
/* 162 */     WoodlandMansionPieces.SimpleGrid simpleGrid1 = paramMansionGrid.baseGrid;
/* 163 */     WoodlandMansionPieces.SimpleGrid simpleGrid2 = paramMansionGrid.thirdFloorGrid;
/*     */     
/* 165 */     this.startX = paramMansionGrid.entranceX + 1;
/* 166 */     this.startY = paramMansionGrid.entranceY + 1;
/* 167 */     int i = paramMansionGrid.entranceX + 1;
/* 168 */     int j = paramMansionGrid.entranceY;
/*     */     
/* 170 */     traverseOuterWalls(paramList, placementData1, simpleGrid1, Direction.SOUTH, this.startX, this.startY, i, j);
/* 171 */     traverseOuterWalls(paramList, placementData2, simpleGrid1, Direction.SOUTH, this.startX, this.startY, i, j);
/*     */ 
/*     */     
/* 174 */     WoodlandMansionPieces.PlacementData placementData3 = new WoodlandMansionPieces.PlacementData();
/* 175 */     placementData3.position = placementData1.position.above(19);
/* 176 */     placementData3.rotation = placementData1.rotation;
/* 177 */     placementData3.wallType = "wall_window";
/*     */     
/* 179 */     boolean bool = false;
/* 180 */     for (byte b1 = 0; b1 < simpleGrid2.height && !bool; b1++) {
/* 181 */       for (int k = simpleGrid2.width - 1; k >= 0 && !bool; k--) {
/* 182 */         if (WoodlandMansionPieces.MansionGrid.isHouse(simpleGrid2, k, b1)) {
/* 183 */           placementData3.position = placementData3.position.relative(paramRotation.rotate(Direction.SOUTH), 8 + (b1 - this.startY) * 8);
/* 184 */           placementData3.position = placementData3.position.relative(paramRotation.rotate(Direction.EAST), (k - this.startX) * 8);
/* 185 */           traverseWallPiece(paramList, placementData3);
/* 186 */           traverseOuterWalls(paramList, placementData3, simpleGrid2, Direction.SOUTH, k, b1, k, b1);
/* 187 */           bool = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 193 */     createRoof(paramList, paramBlockPos.above(16), paramRotation, simpleGrid1, simpleGrid2);
/* 194 */     createRoof(paramList, paramBlockPos.above(27), paramRotation, simpleGrid2, null);
/*     */     
/* 196 */     if (!paramList.isEmpty());
/*     */ 
/*     */ 
/*     */     
/* 200 */     WoodlandMansionPieces.FloorRoomCollection[] arrayOfFloorRoomCollection = new WoodlandMansionPieces.FloorRoomCollection[3];
/* 201 */     arrayOfFloorRoomCollection[0] = new WoodlandMansionPieces.FirstFloorRoomCollection();
/* 202 */     arrayOfFloorRoomCollection[1] = new WoodlandMansionPieces.SecondFloorRoomCollection();
/* 203 */     arrayOfFloorRoomCollection[2] = new WoodlandMansionPieces.ThirdFloorRoomCollection();
/*     */     
/* 205 */     for (byte b2 = 0; b2 < 3; b2++) {
/* 206 */       BlockPos blockPos = paramBlockPos.above(8 * b2 + ((b2 == 2) ? 3 : 0));
/* 207 */       WoodlandMansionPieces.SimpleGrid simpleGrid3 = paramMansionGrid.floorRooms[b2];
/* 208 */       WoodlandMansionPieces.SimpleGrid simpleGrid4 = (b2 == 2) ? simpleGrid2 : simpleGrid1;
/*     */ 
/*     */       
/* 211 */       String str1 = (b2 == 0) ? "carpet_south_1" : "carpet_south_2";
/* 212 */       String str2 = (b2 == 0) ? "carpet_west_1" : "carpet_west_2";
/* 213 */       for (byte b3 = 0; b3 < simpleGrid4.height; b3++) {
/* 214 */         for (byte b = 0; b < simpleGrid4.width; b++) {
/* 215 */           if (simpleGrid4.get(b, b3) == 1) {
/* 216 */             BlockPos blockPos1 = blockPos.relative(paramRotation.rotate(Direction.SOUTH), 8 + (b3 - this.startY) * 8);
/* 217 */             blockPos1 = blockPos1.relative(paramRotation.rotate(Direction.EAST), (b - this.startX) * 8);
/* 218 */             paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "corridor_floor", blockPos1, paramRotation));
/*     */             
/* 220 */             if (simpleGrid4.get(b, b3 - 1) == 1 || (simpleGrid3.get(b, b3 - 1) & 0x800000) == 8388608) {
/* 221 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "carpet_north", blockPos1.relative(paramRotation.rotate(Direction.EAST), 1).above(), paramRotation));
/*     */             }
/* 223 */             if (simpleGrid4.get(b + 1, b3) == 1 || (simpleGrid3.get(b + 1, b3) & 0x800000) == 8388608) {
/* 224 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "carpet_east", blockPos1.relative(paramRotation.rotate(Direction.SOUTH), 1).relative(paramRotation.rotate(Direction.EAST), 5).above(), paramRotation));
/*     */             }
/* 226 */             if (simpleGrid4.get(b, b3 + 1) == 1 || (simpleGrid3.get(b, b3 + 1) & 0x800000) == 8388608) {
/* 227 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, str1, blockPos1.relative(paramRotation.rotate(Direction.SOUTH), 5).relative(paramRotation.rotate(Direction.WEST), 1), paramRotation));
/*     */             }
/* 229 */             if (simpleGrid4.get(b - 1, b3) == 1 || (simpleGrid3.get(b - 1, b3) & 0x800000) == 8388608) {
/* 230 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, str2, blockPos1.relative(paramRotation.rotate(Direction.WEST), 1).relative(paramRotation.rotate(Direction.NORTH), 1), paramRotation));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 236 */       String str3 = (b2 == 0) ? "indoors_wall_1" : "indoors_wall_2";
/* 237 */       String str4 = (b2 == 0) ? "indoors_door_1" : "indoors_door_2";
/* 238 */       ArrayList<Direction> arrayList = Lists.newArrayList();
/* 239 */       for (byte b4 = 0; b4 < simpleGrid4.height; b4++) {
/* 240 */         for (byte b = 0; b < simpleGrid4.width; b++) {
/* 241 */           boolean bool1 = (b2 == 2 && simpleGrid4.get(b, b4) == 3) ? true : false;
/* 242 */           if (simpleGrid4.get(b, b4) == 2 || bool1) {
/* 243 */             int k = simpleGrid3.get(b, b4);
/* 244 */             int m = k & 0xF0000;
/* 245 */             int n = k & 0xFFFF;
/*     */ 
/*     */             
/* 248 */             bool1 = (bool1 && (k & 0x800000) == 8388608) ? true : false;
/*     */             
/* 250 */             arrayList.clear();
/* 251 */             if ((k & 0x200000) == 2097152) {
/* 252 */               for (Direction direction1 : Direction.Plane.HORIZONTAL) {
/* 253 */                 if (simpleGrid4.get(b + direction1.getStepX(), b4 + direction1.getStepZ()) == 1) {
/* 254 */                   arrayList.add(direction1);
/*     */                 }
/*     */               } 
/*     */             }
/* 258 */             Direction direction = null;
/* 259 */             if (!arrayList.isEmpty()) {
/* 260 */               direction = arrayList.get(this.random.nextInt(arrayList.size()));
/* 261 */             } else if ((k & 0x100000) == 1048576) {
/*     */               
/* 263 */               direction = Direction.UP;
/*     */             } 
/*     */             
/* 266 */             BlockPos blockPos1 = blockPos.relative(paramRotation.rotate(Direction.SOUTH), 8 + (b4 - this.startY) * 8);
/* 267 */             blockPos1 = blockPos1.relative(paramRotation.rotate(Direction.EAST), -1 + (b - this.startX) * 8);
/*     */             
/* 269 */             if (WoodlandMansionPieces.MansionGrid.isHouse(simpleGrid4, b - 1, b4) && !paramMansionGrid.isRoomId(simpleGrid4, b - 1, b4, b2, n)) {
/* 270 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (direction == Direction.WEST) ? str4 : str3, blockPos1, paramRotation));
/*     */             }
/* 272 */             if (simpleGrid4.get(b + 1, b4) == 1 && !bool1) {
/* 273 */               BlockPos blockPos2 = blockPos1.relative(paramRotation.rotate(Direction.EAST), 8);
/* 274 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (direction == Direction.EAST) ? str4 : str3, blockPos2, paramRotation));
/*     */             } 
/* 276 */             if (WoodlandMansionPieces.MansionGrid.isHouse(simpleGrid4, b, b4 + 1) && !paramMansionGrid.isRoomId(simpleGrid4, b, b4 + 1, b2, n)) {
/* 277 */               BlockPos blockPos2 = blockPos1.relative(paramRotation.rotate(Direction.SOUTH), 7);
/* 278 */               blockPos2 = blockPos2.relative(paramRotation.rotate(Direction.EAST), 7);
/* 279 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (direction == Direction.SOUTH) ? str4 : str3, blockPos2, paramRotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */             } 
/* 281 */             if (simpleGrid4.get(b, b4 - 1) == 1 && !bool1) {
/* 282 */               BlockPos blockPos2 = blockPos1.relative(paramRotation.rotate(Direction.NORTH), 1);
/* 283 */               blockPos2 = blockPos2.relative(paramRotation.rotate(Direction.EAST), 7);
/* 284 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (direction == Direction.NORTH) ? str4 : str3, blockPos2, paramRotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */             } 
/*     */             
/* 287 */             if (m == 65536) {
/* 288 */               addRoom1x1(paramList, blockPos1, paramRotation, direction, arrayOfFloorRoomCollection[b2]);
/* 289 */             } else if (m == 131072 && direction != null) {
/*     */               
/* 291 */               Direction direction1 = paramMansionGrid.get1x2RoomDirection(simpleGrid4, b, b4, b2, n);
/* 292 */               boolean bool2 = ((k & 0x400000) == 4194304) ? true : false;
/* 293 */               addRoom1x2(paramList, blockPos1, paramRotation, direction1, direction, arrayOfFloorRoomCollection[b2], bool2);
/* 294 */             } else if (m == 262144 && direction != null && direction != Direction.UP) {
/*     */               
/* 296 */               Direction direction1 = direction.getClockWise();
/* 297 */               if (!paramMansionGrid.isRoomId(simpleGrid4, b + direction1.getStepX(), b4 + direction1.getStepZ(), b2, n)) {
/* 298 */                 direction1 = direction1.getOpposite();
/*     */               }
/* 300 */               addRoom2x2(paramList, blockPos1, paramRotation, direction1, direction, arrayOfFloorRoomCollection[b2]);
/* 301 */             } else if (m == 262144 && direction == Direction.UP) {
/* 302 */               addRoom2x2Secret(paramList, blockPos1, paramRotation, arrayOfFloorRoomCollection[b2]);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void traverseOuterWalls(List<WoodlandMansionPieces.WoodlandMansionPiece> paramList, WoodlandMansionPieces.PlacementData paramPlacementData, WoodlandMansionPieces.SimpleGrid paramSimpleGrid, Direction paramDirection, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 311 */     int i = paramInt1;
/* 312 */     int j = paramInt2;
/* 313 */     Direction direction = paramDirection;
/*     */     
/*     */     do {
/* 316 */       if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid, i + paramDirection.getStepX(), j + paramDirection.getStepZ())) {
/*     */         
/* 318 */         traverseTurn(paramList, paramPlacementData);
/* 319 */         paramDirection = paramDirection.getClockWise();
/* 320 */         if (i != paramInt3 || j != paramInt4 || direction != paramDirection) {
/* 321 */           traverseWallPiece(paramList, paramPlacementData);
/*     */         }
/* 323 */       } else if (WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid, i + paramDirection.getStepX(), j + paramDirection.getStepZ()) && WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid, i + paramDirection.getStepX() + paramDirection.getCounterClockWise().getStepX(), j + paramDirection.getStepZ() + paramDirection.getCounterClockWise().getStepZ())) {
/*     */         
/* 325 */         traverseInnerTurn(paramList, paramPlacementData);
/* 326 */         i += paramDirection.getStepX();
/* 327 */         j += paramDirection.getStepZ();
/* 328 */         paramDirection = paramDirection.getCounterClockWise();
/*     */       } else {
/* 330 */         i += paramDirection.getStepX();
/* 331 */         j += paramDirection.getStepZ();
/* 332 */         if (i != paramInt3 || j != paramInt4 || direction != paramDirection) {
/* 333 */           traverseWallPiece(paramList, paramPlacementData);
/*     */         }
/*     */       } 
/* 336 */     } while (i != paramInt3 || j != paramInt4 || direction != paramDirection);
/*     */   }
/*     */   
/*     */   private void createRoof(List<WoodlandMansionPieces.WoodlandMansionPiece> paramList, BlockPos paramBlockPos, Rotation paramRotation, WoodlandMansionPieces.SimpleGrid paramSimpleGrid1, WoodlandMansionPieces.SimpleGrid paramSimpleGrid2) {
/*     */     byte b;
/* 341 */     for (b = 0; b < paramSimpleGrid1.height; b++) {
/* 342 */       for (byte b1 = 0; b1 < paramSimpleGrid1.width; b1++) {
/* 343 */         BlockPos blockPos = paramBlockPos;
/* 344 */         blockPos = blockPos.relative(paramRotation.rotate(Direction.SOUTH), 8 + (b - this.startY) * 8);
/* 345 */         blockPos = blockPos.relative(paramRotation.rotate(Direction.EAST), (b1 - this.startX) * 8);
/*     */ 
/*     */         
/* 348 */         boolean bool = (paramSimpleGrid2 != null && WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid2, b1, b)) ? true : false;
/*     */         
/* 350 */         if (WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1, b) && !bool) {
/* 351 */           paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof", blockPos.above(3), paramRotation));
/*     */           
/* 353 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1 + 1, b)) {
/* 354 */             BlockPos blockPos1 = blockPos.relative(paramRotation.rotate(Direction.EAST), 6);
/* 355 */             paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", blockPos1, paramRotation));
/*     */           } 
/* 357 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1 - 1, b)) {
/* 358 */             BlockPos blockPos1 = blockPos.relative(paramRotation.rotate(Direction.EAST), 0);
/* 359 */             blockPos1 = blockPos1.relative(paramRotation.rotate(Direction.SOUTH), 7);
/* 360 */             paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", blockPos1, paramRotation.getRotated(Rotation.CLOCKWISE_180)));
/*     */           } 
/* 362 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1, b - 1)) {
/* 363 */             BlockPos blockPos1 = blockPos.relative(paramRotation.rotate(Direction.WEST), 1);
/* 364 */             paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", blockPos1, paramRotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*     */           } 
/* 366 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1, b + 1)) {
/* 367 */             BlockPos blockPos1 = blockPos.relative(paramRotation.rotate(Direction.EAST), 6);
/* 368 */             blockPos1 = blockPos1.relative(paramRotation.rotate(Direction.SOUTH), 6);
/* 369 */             paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", blockPos1, paramRotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 375 */     if (paramSimpleGrid2 != null) {
/* 376 */       for (b = 0; b < paramSimpleGrid1.height; b++) {
/* 377 */         for (byte b1 = 0; b1 < paramSimpleGrid1.width; b1++) {
/* 378 */           BlockPos blockPos = paramBlockPos;
/* 379 */           blockPos = blockPos.relative(paramRotation.rotate(Direction.SOUTH), 8 + (b - this.startY) * 8);
/* 380 */           blockPos = blockPos.relative(paramRotation.rotate(Direction.EAST), (b1 - this.startX) * 8);
/*     */ 
/*     */           
/* 383 */           boolean bool = WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid2, b1, b);
/*     */           
/* 385 */           if (WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1, b) && bool) {
/*     */             
/* 387 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1 + 1, b)) {
/* 388 */               BlockPos blockPos1 = blockPos.relative(paramRotation.rotate(Direction.EAST), 7);
/* 389 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", blockPos1, paramRotation));
/*     */             } 
/* 391 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1 - 1, b)) {
/* 392 */               BlockPos blockPos1 = blockPos.relative(paramRotation.rotate(Direction.WEST), 1);
/* 393 */               blockPos1 = blockPos1.relative(paramRotation.rotate(Direction.SOUTH), 6);
/* 394 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", blockPos1, paramRotation.getRotated(Rotation.CLOCKWISE_180)));
/*     */             } 
/* 396 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1, b - 1)) {
/* 397 */               BlockPos blockPos1 = blockPos.relative(paramRotation.rotate(Direction.WEST), 0);
/* 398 */               blockPos1 = blockPos1.relative(paramRotation.rotate(Direction.NORTH), 1);
/* 399 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", blockPos1, paramRotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*     */             } 
/* 401 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1, b + 1)) {
/* 402 */               BlockPos blockPos1 = blockPos.relative(paramRotation.rotate(Direction.EAST), 6);
/* 403 */               blockPos1 = blockPos1.relative(paramRotation.rotate(Direction.SOUTH), 7);
/* 404 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", blockPos1, paramRotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */             } 
/*     */             
/* 407 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1 + 1, b)) {
/* 408 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1, b - 1)) {
/* 409 */                 BlockPos blockPos1 = blockPos.relative(paramRotation.rotate(Direction.EAST), 7);
/* 410 */                 blockPos1 = blockPos1.relative(paramRotation.rotate(Direction.NORTH), 2);
/* 411 */                 paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", blockPos1, paramRotation));
/*     */               } 
/* 413 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1, b + 1)) {
/* 414 */                 BlockPos blockPos1 = blockPos.relative(paramRotation.rotate(Direction.EAST), 8);
/* 415 */                 blockPos1 = blockPos1.relative(paramRotation.rotate(Direction.SOUTH), 7);
/* 416 */                 paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", blockPos1, paramRotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */               } 
/*     */             } 
/* 419 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1 - 1, b)) {
/* 420 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1, b - 1)) {
/* 421 */                 BlockPos blockPos1 = blockPos.relative(paramRotation.rotate(Direction.WEST), 2);
/* 422 */                 blockPos1 = blockPos1.relative(paramRotation.rotate(Direction.NORTH), 1);
/* 423 */                 paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", blockPos1, paramRotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*     */               } 
/* 425 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1, b + 1)) {
/* 426 */                 BlockPos blockPos1 = blockPos.relative(paramRotation.rotate(Direction.WEST), 1);
/* 427 */                 blockPos1 = blockPos1.relative(paramRotation.rotate(Direction.SOUTH), 8);
/* 428 */                 paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", blockPos1, paramRotation.getRotated(Rotation.CLOCKWISE_180)));
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 436 */     for (b = 0; b < paramSimpleGrid1.height; b++) {
/* 437 */       for (byte b1 = 0; b1 < paramSimpleGrid1.width; b1++) {
/* 438 */         BlockPos blockPos = paramBlockPos;
/* 439 */         blockPos = blockPos.relative(paramRotation.rotate(Direction.SOUTH), 8 + (b - this.startY) * 8);
/* 440 */         blockPos = blockPos.relative(paramRotation.rotate(Direction.EAST), (b1 - this.startX) * 8);
/*     */ 
/*     */         
/* 443 */         boolean bool = (paramSimpleGrid2 != null && WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid2, b1, b)) ? true : false;
/*     */         
/* 445 */         if (WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1, b) && !bool) {
/* 446 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1 + 1, b)) {
/* 447 */             BlockPos blockPos1 = blockPos.relative(paramRotation.rotate(Direction.EAST), 6);
/* 448 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1, b + 1)) {
/* 449 */               BlockPos blockPos2 = blockPos1.relative(paramRotation.rotate(Direction.SOUTH), 6);
/* 450 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", blockPos2, paramRotation));
/* 451 */             } else if (WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1 + 1, b + 1)) {
/* 452 */               BlockPos blockPos2 = blockPos1.relative(paramRotation.rotate(Direction.SOUTH), 5);
/* 453 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", blockPos2, paramRotation));
/*     */             } 
/* 455 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1, b - 1)) {
/* 456 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", blockPos1, paramRotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/* 457 */             } else if (WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1 + 1, b - 1)) {
/* 458 */               BlockPos blockPos2 = blockPos.relative(paramRotation.rotate(Direction.EAST), 9);
/* 459 */               blockPos2 = blockPos2.relative(paramRotation.rotate(Direction.NORTH), 2);
/* 460 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", blockPos2, paramRotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */             } 
/*     */           } 
/* 463 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1 - 1, b)) {
/* 464 */             BlockPos blockPos1 = blockPos.relative(paramRotation.rotate(Direction.EAST), 0);
/* 465 */             blockPos1 = blockPos1.relative(paramRotation.rotate(Direction.SOUTH), 0);
/* 466 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1, b + 1)) {
/* 467 */               BlockPos blockPos2 = blockPos1.relative(paramRotation.rotate(Direction.SOUTH), 6);
/* 468 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", blockPos2, paramRotation.getRotated(Rotation.CLOCKWISE_90)));
/* 469 */             } else if (WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1 - 1, b + 1)) {
/* 470 */               BlockPos blockPos2 = blockPos1.relative(paramRotation.rotate(Direction.SOUTH), 8);
/* 471 */               blockPos2 = blockPos2.relative(paramRotation.rotate(Direction.WEST), 3);
/* 472 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", blockPos2, paramRotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*     */             } 
/* 474 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1, b - 1)) {
/* 475 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", blockPos1, paramRotation.getRotated(Rotation.CLOCKWISE_180)));
/* 476 */             } else if (WoodlandMansionPieces.MansionGrid.isHouse(paramSimpleGrid1, b1 - 1, b - 1)) {
/* 477 */               BlockPos blockPos2 = blockPos1.relative(paramRotation.rotate(Direction.SOUTH), 1);
/* 478 */               paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", blockPos2, paramRotation.getRotated(Rotation.CLOCKWISE_180)));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void entrance(List<WoodlandMansionPieces.WoodlandMansionPiece> paramList, WoodlandMansionPieces.PlacementData paramPlacementData) {
/* 487 */     Direction direction = paramPlacementData.rotation.rotate(Direction.WEST);
/* 488 */     paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "entrance", paramPlacementData.position.relative(direction, 9), paramPlacementData.rotation));
/* 489 */     paramPlacementData.position = paramPlacementData.position.relative(paramPlacementData.rotation.rotate(Direction.SOUTH), 16);
/*     */   }
/*     */   
/*     */   private void traverseWallPiece(List<WoodlandMansionPieces.WoodlandMansionPiece> paramList, WoodlandMansionPieces.PlacementData paramPlacementData) {
/* 493 */     paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramPlacementData.wallType, paramPlacementData.position.relative(paramPlacementData.rotation.rotate(Direction.EAST), 7), paramPlacementData.rotation));
/* 494 */     paramPlacementData.position = paramPlacementData.position.relative(paramPlacementData.rotation.rotate(Direction.SOUTH), 8);
/*     */   }
/*     */   
/*     */   private void traverseTurn(List<WoodlandMansionPieces.WoodlandMansionPiece> paramList, WoodlandMansionPieces.PlacementData paramPlacementData) {
/* 498 */     paramPlacementData.position = paramPlacementData.position.relative(paramPlacementData.rotation.rotate(Direction.SOUTH), -1);
/* 499 */     paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "wall_corner", paramPlacementData.position, paramPlacementData.rotation));
/* 500 */     paramPlacementData.position = paramPlacementData.position.relative(paramPlacementData.rotation.rotate(Direction.SOUTH), -7);
/* 501 */     paramPlacementData.position = paramPlacementData.position.relative(paramPlacementData.rotation.rotate(Direction.WEST), -6);
/* 502 */     paramPlacementData.rotation = paramPlacementData.rotation.getRotated(Rotation.CLOCKWISE_90);
/*     */   }
/*     */   
/*     */   private void traverseInnerTurn(List<WoodlandMansionPieces.WoodlandMansionPiece> paramList, WoodlandMansionPieces.PlacementData paramPlacementData) {
/* 506 */     paramPlacementData.position = paramPlacementData.position.relative(paramPlacementData.rotation.rotate(Direction.SOUTH), 6);
/* 507 */     paramPlacementData.position = paramPlacementData.position.relative(paramPlacementData.rotation.rotate(Direction.EAST), 8);
/* 508 */     paramPlacementData.rotation = paramPlacementData.rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*     */   }
/*     */   
/*     */   private void addRoom1x1(List<WoodlandMansionPieces.WoodlandMansionPiece> paramList, BlockPos paramBlockPos, Rotation paramRotation, Direction paramDirection, WoodlandMansionPieces.FloorRoomCollection paramFloorRoomCollection) {
/* 512 */     Rotation rotation = Rotation.NONE;
/* 513 */     String str = paramFloorRoomCollection.get1x1(this.random);
/* 514 */     if (paramDirection != Direction.EAST) {
/* 515 */       if (paramDirection == Direction.NORTH) {
/* 516 */         rotation = rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/* 517 */       } else if (paramDirection == Direction.WEST) {
/* 518 */         rotation = rotation.getRotated(Rotation.CLOCKWISE_180);
/* 519 */       } else if (paramDirection == Direction.SOUTH) {
/* 520 */         rotation = rotation.getRotated(Rotation.CLOCKWISE_90);
/*     */       } else {
/*     */         
/* 523 */         str = paramFloorRoomCollection.get1x1Secret(this.random);
/*     */       } 
/*     */     }
/* 526 */     BlockPos blockPos1 = StructureTemplate.getZeroPositionWithTransform(new BlockPos(1, 0, 0), Mirror.NONE, rotation, 7, 7);
/* 527 */     rotation = rotation.getRotated(paramRotation);
/* 528 */     blockPos1 = blockPos1.rotate(paramRotation);
/* 529 */     BlockPos blockPos2 = paramBlockPos.offset(blockPos1.getX(), 0, blockPos1.getZ());
/* 530 */     paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, str, blockPos2, rotation));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addRoom1x2(List<WoodlandMansionPieces.WoodlandMansionPiece> paramList, BlockPos paramBlockPos, Rotation paramRotation, Direction paramDirection1, Direction paramDirection2, WoodlandMansionPieces.FloorRoomCollection paramFloorRoomCollection, boolean paramBoolean) {
/* 537 */     if (paramDirection2 == Direction.EAST && paramDirection1 == Direction.SOUTH) {
/*     */ 
/*     */       
/* 540 */       BlockPos blockPos = paramBlockPos.relative(paramRotation.rotate(Direction.EAST), 1);
/* 541 */       paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramFloorRoomCollection.get1x2SideEntrance(this.random, paramBoolean), blockPos, paramRotation));
/* 542 */     } else if (paramDirection2 == Direction.EAST && paramDirection1 == Direction.NORTH) {
/*     */ 
/*     */       
/* 545 */       BlockPos blockPos = paramBlockPos.relative(paramRotation.rotate(Direction.EAST), 1);
/* 546 */       blockPos = blockPos.relative(paramRotation.rotate(Direction.SOUTH), 6);
/* 547 */       paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramFloorRoomCollection.get1x2SideEntrance(this.random, paramBoolean), blockPos, paramRotation, Mirror.LEFT_RIGHT));
/* 548 */     } else if (paramDirection2 == Direction.WEST && paramDirection1 == Direction.NORTH) {
/*     */ 
/*     */       
/* 551 */       BlockPos blockPos = paramBlockPos.relative(paramRotation.rotate(Direction.EAST), 7);
/* 552 */       blockPos = blockPos.relative(paramRotation.rotate(Direction.SOUTH), 6);
/* 553 */       paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramFloorRoomCollection.get1x2SideEntrance(this.random, paramBoolean), blockPos, paramRotation.getRotated(Rotation.CLOCKWISE_180)));
/* 554 */     } else if (paramDirection2 == Direction.WEST && paramDirection1 == Direction.SOUTH) {
/*     */ 
/*     */       
/* 557 */       BlockPos blockPos = paramBlockPos.relative(paramRotation.rotate(Direction.EAST), 7);
/* 558 */       paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramFloorRoomCollection.get1x2SideEntrance(this.random, paramBoolean), blockPos, paramRotation, Mirror.FRONT_BACK));
/* 559 */     } else if (paramDirection2 == Direction.SOUTH && paramDirection1 == Direction.EAST) {
/*     */ 
/*     */       
/* 562 */       BlockPos blockPos = paramBlockPos.relative(paramRotation.rotate(Direction.EAST), 1);
/* 563 */       paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramFloorRoomCollection.get1x2SideEntrance(this.random, paramBoolean), blockPos, paramRotation.getRotated(Rotation.CLOCKWISE_90), Mirror.LEFT_RIGHT));
/* 564 */     } else if (paramDirection2 == Direction.SOUTH && paramDirection1 == Direction.WEST) {
/*     */ 
/*     */       
/* 567 */       BlockPos blockPos = paramBlockPos.relative(paramRotation.rotate(Direction.EAST), 7);
/* 568 */       paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramFloorRoomCollection.get1x2SideEntrance(this.random, paramBoolean), blockPos, paramRotation.getRotated(Rotation.CLOCKWISE_90)));
/* 569 */     } else if (paramDirection2 == Direction.NORTH && paramDirection1 == Direction.WEST) {
/*     */ 
/*     */       
/* 572 */       BlockPos blockPos = paramBlockPos.relative(paramRotation.rotate(Direction.EAST), 7);
/* 573 */       blockPos = blockPos.relative(paramRotation.rotate(Direction.SOUTH), 6);
/* 574 */       paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramFloorRoomCollection.get1x2SideEntrance(this.random, paramBoolean), blockPos, paramRotation.getRotated(Rotation.CLOCKWISE_90), Mirror.FRONT_BACK));
/* 575 */     } else if (paramDirection2 == Direction.NORTH && paramDirection1 == Direction.EAST) {
/*     */ 
/*     */       
/* 578 */       BlockPos blockPos = paramBlockPos.relative(paramRotation.rotate(Direction.EAST), 1);
/* 579 */       blockPos = blockPos.relative(paramRotation.rotate(Direction.SOUTH), 6);
/* 580 */       paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramFloorRoomCollection.get1x2SideEntrance(this.random, paramBoolean), blockPos, paramRotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/* 581 */     } else if (paramDirection2 == Direction.SOUTH && paramDirection1 == Direction.NORTH) {
/*     */ 
/*     */ 
/*     */       
/* 585 */       BlockPos blockPos = paramBlockPos.relative(paramRotation.rotate(Direction.EAST), 1);
/* 586 */       blockPos = blockPos.relative(paramRotation.rotate(Direction.NORTH), 8);
/* 587 */       paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramFloorRoomCollection.get1x2FrontEntrance(this.random, paramBoolean), blockPos, paramRotation));
/* 588 */     } else if (paramDirection2 == Direction.NORTH && paramDirection1 == Direction.SOUTH) {
/*     */ 
/*     */ 
/*     */       
/* 592 */       BlockPos blockPos = paramBlockPos.relative(paramRotation.rotate(Direction.EAST), 7);
/* 593 */       blockPos = blockPos.relative(paramRotation.rotate(Direction.SOUTH), 14);
/* 594 */       paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramFloorRoomCollection.get1x2FrontEntrance(this.random, paramBoolean), blockPos, paramRotation.getRotated(Rotation.CLOCKWISE_180)));
/* 595 */     } else if (paramDirection2 == Direction.WEST && paramDirection1 == Direction.EAST) {
/*     */       
/* 597 */       BlockPos blockPos = paramBlockPos.relative(paramRotation.rotate(Direction.EAST), 15);
/* 598 */       paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramFloorRoomCollection.get1x2FrontEntrance(this.random, paramBoolean), blockPos, paramRotation.getRotated(Rotation.CLOCKWISE_90)));
/* 599 */     } else if (paramDirection2 == Direction.EAST && paramDirection1 == Direction.WEST) {
/*     */       
/* 601 */       BlockPos blockPos = paramBlockPos.relative(paramRotation.rotate(Direction.WEST), 7);
/* 602 */       blockPos = blockPos.relative(paramRotation.rotate(Direction.SOUTH), 6);
/* 603 */       paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramFloorRoomCollection.get1x2FrontEntrance(this.random, paramBoolean), blockPos, paramRotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/* 604 */     } else if (paramDirection2 == Direction.UP && paramDirection1 == Direction.EAST) {
/*     */       
/* 606 */       BlockPos blockPos = paramBlockPos.relative(paramRotation.rotate(Direction.EAST), 15);
/* 607 */       paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramFloorRoomCollection.get1x2Secret(this.random), blockPos, paramRotation.getRotated(Rotation.CLOCKWISE_90)));
/* 608 */     } else if (paramDirection2 == Direction.UP && paramDirection1 == Direction.SOUTH) {
/*     */ 
/*     */ 
/*     */       
/* 612 */       BlockPos blockPos = paramBlockPos.relative(paramRotation.rotate(Direction.EAST), 1);
/* 613 */       blockPos = blockPos.relative(paramRotation.rotate(Direction.NORTH), 0);
/* 614 */       paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramFloorRoomCollection.get1x2Secret(this.random), blockPos, paramRotation));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addRoom2x2(List<WoodlandMansionPieces.WoodlandMansionPiece> paramList, BlockPos paramBlockPos, Rotation paramRotation, Direction paramDirection1, Direction paramDirection2, WoodlandMansionPieces.FloorRoomCollection paramFloorRoomCollection) {
/* 619 */     byte b1 = 0;
/* 620 */     byte b2 = 0;
/* 621 */     Rotation rotation = paramRotation;
/* 622 */     Mirror mirror = Mirror.NONE;
/*     */ 
/*     */ 
/*     */     
/* 626 */     if (paramDirection2 == Direction.EAST && paramDirection1 == Direction.SOUTH) {
/*     */ 
/*     */       
/* 629 */       b1 = -7;
/* 630 */     } else if (paramDirection2 == Direction.EAST && paramDirection1 == Direction.NORTH) {
/*     */ 
/*     */       
/* 633 */       b1 = -7;
/* 634 */       b2 = 6;
/* 635 */       mirror = Mirror.LEFT_RIGHT;
/* 636 */     } else if (paramDirection2 == Direction.NORTH && paramDirection1 == Direction.EAST) {
/*     */ 
/*     */ 
/*     */       
/* 640 */       b1 = 1;
/* 641 */       b2 = 14;
/* 642 */       rotation = paramRotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/* 643 */     } else if (paramDirection2 == Direction.NORTH && paramDirection1 == Direction.WEST) {
/*     */ 
/*     */ 
/*     */       
/* 647 */       b1 = 7;
/* 648 */       b2 = 14;
/* 649 */       rotation = paramRotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/* 650 */       mirror = Mirror.LEFT_RIGHT;
/* 651 */     } else if (paramDirection2 == Direction.SOUTH && paramDirection1 == Direction.WEST) {
/*     */ 
/*     */ 
/*     */       
/* 655 */       b1 = 7;
/* 656 */       b2 = -8;
/* 657 */       rotation = paramRotation.getRotated(Rotation.CLOCKWISE_90);
/* 658 */     } else if (paramDirection2 == Direction.SOUTH && paramDirection1 == Direction.EAST) {
/*     */ 
/*     */ 
/*     */       
/* 662 */       b1 = 1;
/* 663 */       b2 = -8;
/* 664 */       rotation = paramRotation.getRotated(Rotation.CLOCKWISE_90);
/* 665 */       mirror = Mirror.LEFT_RIGHT;
/* 666 */     } else if (paramDirection2 == Direction.WEST && paramDirection1 == Direction.NORTH) {
/*     */ 
/*     */       
/* 669 */       b1 = 15;
/* 670 */       b2 = 6;
/* 671 */       rotation = paramRotation.getRotated(Rotation.CLOCKWISE_180);
/* 672 */     } else if (paramDirection2 == Direction.WEST && paramDirection1 == Direction.SOUTH) {
/*     */ 
/*     */       
/* 675 */       b1 = 15;
/* 676 */       mirror = Mirror.FRONT_BACK;
/*     */     } 
/*     */     
/* 679 */     BlockPos blockPos = paramBlockPos.relative(paramRotation.rotate(Direction.EAST), b1);
/* 680 */     blockPos = blockPos.relative(paramRotation.rotate(Direction.SOUTH), b2);
/* 681 */     paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramFloorRoomCollection.get2x2(this.random), blockPos, rotation, mirror));
/*     */   }
/*     */   
/*     */   private void addRoom2x2Secret(List<WoodlandMansionPieces.WoodlandMansionPiece> paramList, BlockPos paramBlockPos, Rotation paramRotation, WoodlandMansionPieces.FloorRoomCollection paramFloorRoomCollection) {
/* 685 */     BlockPos blockPos = paramBlockPos.relative(paramRotation.rotate(Direction.EAST), 1);
/* 686 */     paramList.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, paramFloorRoomCollection.get2x2Secret(this.random), blockPos, paramRotation, Mirror.NONE));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces$MansionPiecePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */