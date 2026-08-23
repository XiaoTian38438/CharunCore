/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
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
/*     */ public class MonumentBuilding
/*     */   extends OceanMonumentPieces.OceanMonumentPiece
/*     */ {
/*     */   private static final int WIDTH = 58;
/*     */   private static final int HEIGHT = 22;
/*     */   private static final int DEPTH = 58;
/*     */   public static final int BIOME_RANGE_CHECK = 29;
/*     */   private static final int TOP_POSITION = 61;
/*     */   private OceanMonumentPieces.RoomDefinition sourceRoom;
/*     */   private OceanMonumentPieces.RoomDefinition coreRoom;
/* 209 */   private final List<OceanMonumentPieces.OceanMonumentPiece> childPieces = Lists.newArrayList();
/*     */   
/*     */   public MonumentBuilding(RandomSource paramRandomSource, int paramInt1, int paramInt2, Direction paramDirection) {
/* 212 */     super(StructurePieceType.OCEAN_MONUMENT_BUILDING, paramDirection, 0, makeBoundingBox(paramInt1, 39, paramInt2, paramDirection, 58, 23, 58));
/*     */     
/* 214 */     setOrientation(paramDirection);
/*     */     
/* 216 */     List<OceanMonumentPieces.RoomDefinition> list = generateRoomGraph(paramRandomSource);
/*     */     
/* 218 */     this.sourceRoom.claimed = true;
/* 219 */     this.childPieces.add(new OceanMonumentPieces.OceanMonumentEntryRoom(paramDirection, this.sourceRoom));
/* 220 */     this.childPieces.add(new OceanMonumentPieces.OceanMonumentCoreRoom(paramDirection, this.coreRoom));
/*     */     
/* 222 */     ArrayList<OceanMonumentPieces.FitDoubleXYRoom> arrayList = Lists.newArrayList();
/* 223 */     arrayList.add(new OceanMonumentPieces.FitDoubleXYRoom());
/* 224 */     arrayList.add(new OceanMonumentPieces.FitDoubleYZRoom());
/* 225 */     arrayList.add(new OceanMonumentPieces.FitDoubleZRoom());
/* 226 */     arrayList.add(new OceanMonumentPieces.FitDoubleXRoom());
/* 227 */     arrayList.add(new OceanMonumentPieces.FitDoubleYRoom());
/* 228 */     arrayList.add(new OceanMonumentPieces.FitSimpleTopRoom());
/* 229 */     arrayList.add(new OceanMonumentPieces.FitSimpleRoom());
/*     */     
/* 231 */     for (OceanMonumentPieces.RoomDefinition roomDefinition : list) {
/* 232 */       if (!roomDefinition.claimed && !roomDefinition.isSpecial())
/*     */       {
/* 234 */         for (OceanMonumentPieces.MonumentRoomFitter monumentRoomFitter : arrayList) {
/* 235 */           if (monumentRoomFitter.fits(roomDefinition)) {
/* 236 */             this.childPieces.add(monumentRoomFitter.create(paramDirection, roomDefinition, paramRandomSource));
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 244 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(9, 0, 22);
/* 245 */     for (OceanMonumentPieces.OceanMonumentPiece oceanMonumentPiece : this.childPieces) {
/* 246 */       oceanMonumentPiece.getBoundingBox().move((Vec3i)mutableBlockPos);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 251 */     BoundingBox boundingBox1 = BoundingBox.fromCorners((Vec3i)getWorldPos(1, 1, 1), (Vec3i)getWorldPos(23, 8, 21));
/* 252 */     BoundingBox boundingBox2 = BoundingBox.fromCorners((Vec3i)getWorldPos(34, 1, 1), (Vec3i)getWorldPos(56, 8, 21));
/* 253 */     BoundingBox boundingBox3 = BoundingBox.fromCorners((Vec3i)getWorldPos(22, 13, 22), (Vec3i)getWorldPos(35, 17, 35));
/*     */ 
/*     */     
/* 256 */     int i = paramRandomSource.nextInt();
/* 257 */     this.childPieces.add(new OceanMonumentPieces.OceanMonumentWingRoom(paramDirection, boundingBox1, i++));
/* 258 */     this.childPieces.add(new OceanMonumentPieces.OceanMonumentWingRoom(paramDirection, boundingBox2, i++));
/*     */     
/* 260 */     this.childPieces.add(new OceanMonumentPieces.OceanMonumentPenthouse(paramDirection, boundingBox3));
/*     */   }
/*     */ 
/*     */   
/*     */   public MonumentBuilding(CompoundTag paramCompoundTag) {
/* 265 */     super(StructurePieceType.OCEAN_MONUMENT_BUILDING, paramCompoundTag);
/*     */   }
/*     */   
/*     */   private List<OceanMonumentPieces.RoomDefinition> generateRoomGraph(RandomSource paramRandomSource) {
/* 269 */     OceanMonumentPieces.RoomDefinition[] arrayOfRoomDefinition = new OceanMonumentPieces.RoomDefinition[75];
/*     */     byte b1;
/* 271 */     for (b1 = 0; b1 < 5; b1++) {
/* 272 */       for (byte b = 0; b < 4; b++) {
/* 273 */         boolean bool = false;
/* 274 */         int i = getRoomIndex(b1, 0, b);
/* 275 */         arrayOfRoomDefinition[i] = new OceanMonumentPieces.RoomDefinition(i);
/*     */       } 
/*     */     } 
/* 278 */     for (b1 = 0; b1 < 5; b1++) {
/* 279 */       for (byte b = 0; b < 4; b++) {
/* 280 */         boolean bool = true;
/* 281 */         int i = getRoomIndex(b1, 1, b);
/* 282 */         arrayOfRoomDefinition[i] = new OceanMonumentPieces.RoomDefinition(i);
/*     */       } 
/*     */     } 
/* 285 */     for (b1 = 1; b1 < 4; b1++) {
/* 286 */       for (byte b = 0; b < 2; b++) {
/* 287 */         byte b3 = 2;
/* 288 */         int i = getRoomIndex(b1, 2, b);
/* 289 */         arrayOfRoomDefinition[i] = new OceanMonumentPieces.RoomDefinition(i);
/*     */       } 
/*     */     } 
/*     */     
/* 293 */     this.sourceRoom = arrayOfRoomDefinition[GRIDROOM_SOURCE_INDEX];
/*     */     
/* 295 */     for (b1 = 0; b1 < 5; b1++) {
/* 296 */       for (byte b = 0; b < 5; b++) {
/* 297 */         for (byte b3 = 0; b3 < 3; b3++) {
/* 298 */           int i = getRoomIndex(b1, b3, b);
/* 299 */           if (arrayOfRoomDefinition[i] != null)
/*     */           {
/*     */             
/* 302 */             for (Direction direction : Direction.values()) {
/* 303 */               int j = b1 + direction.getStepX();
/* 304 */               int k = b3 + direction.getStepY();
/* 305 */               int m = b + direction.getStepZ();
/* 306 */               if (j >= 0 && j < 5 && m >= 0 && m < 5 && k >= 0 && k < 3) {
/* 307 */                 int n = getRoomIndex(j, k, m);
/* 308 */                 if (arrayOfRoomDefinition[n] != null)
/*     */                 {
/*     */                   
/* 311 */                   if (m == b) {
/* 312 */                     arrayOfRoomDefinition[i].setConnection(direction, arrayOfRoomDefinition[n]);
/*     */                   } else {
/* 314 */                     arrayOfRoomDefinition[i].setConnection(direction.getOpposite(), arrayOfRoomDefinition[n]);
/*     */                   }  } 
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 322 */     OceanMonumentPieces.RoomDefinition roomDefinition1 = new OceanMonumentPieces.RoomDefinition(1003);
/* 323 */     OceanMonumentPieces.RoomDefinition roomDefinition2 = new OceanMonumentPieces.RoomDefinition(1001);
/* 324 */     OceanMonumentPieces.RoomDefinition roomDefinition3 = new OceanMonumentPieces.RoomDefinition(1002);
/* 325 */     arrayOfRoomDefinition[GRIDROOM_TOP_CONNECT_INDEX].setConnection(Direction.UP, roomDefinition1);
/* 326 */     arrayOfRoomDefinition[GRIDROOM_LEFTWING_CONNECT_INDEX].setConnection(Direction.SOUTH, roomDefinition2);
/* 327 */     arrayOfRoomDefinition[GRIDROOM_RIGHTWING_CONNECT_INDEX].setConnection(Direction.SOUTH, roomDefinition3);
/* 328 */     roomDefinition1.claimed = true;
/* 329 */     roomDefinition2.claimed = true;
/* 330 */     roomDefinition3.claimed = true;
/* 331 */     this.sourceRoom.isSource = true;
/*     */ 
/*     */     
/* 334 */     this.coreRoom = arrayOfRoomDefinition[getRoomIndex(paramRandomSource.nextInt(4), 0, 2)];
/* 335 */     this.coreRoom.claimed = true;
/* 336 */     (this.coreRoom.connections[Direction.EAST.get3DDataValue()]).claimed = true;
/* 337 */     (this.coreRoom.connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/* 338 */     ((this.coreRoom.connections[Direction.EAST.get3DDataValue()]).connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/* 339 */     (this.coreRoom.connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 340 */     ((this.coreRoom.connections[Direction.EAST.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 341 */     ((this.coreRoom.connections[Direction.NORTH.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 342 */     (((this.coreRoom.connections[Direction.EAST.get3DDataValue()]).connections[Direction.NORTH.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/*     */     
/* 344 */     ObjectArrayList objectArrayList = new ObjectArrayList();
/* 345 */     for (OceanMonumentPieces.RoomDefinition roomDefinition : arrayOfRoomDefinition) {
/* 346 */       if (roomDefinition != null) {
/* 347 */         roomDefinition.updateOpenings();
/* 348 */         objectArrayList.add(roomDefinition);
/*     */       } 
/*     */     } 
/* 351 */     roomDefinition1.updateOpenings();
/*     */     
/* 353 */     Util.shuffle((List)objectArrayList, paramRandomSource);
/* 354 */     byte b2 = 1;
/* 355 */     for (ObjectListIterator<OceanMonumentPieces.RoomDefinition> objectListIterator = objectArrayList.iterator(); objectListIterator.hasNext(); ) { OceanMonumentPieces.RoomDefinition roomDefinition = objectListIterator.next();
/*     */       
/* 357 */       byte b3 = 0;
/* 358 */       byte b4 = 0;
/* 359 */       while (b3 < 2 && b4 < 5) {
/* 360 */         b4++;
/*     */         
/* 362 */         int i = paramRandomSource.nextInt(6);
/* 363 */         if (roomDefinition.hasOpening[i]) {
/* 364 */           int j = Direction.from3DDataValue(i).getOpposite().get3DDataValue();
/*     */ 
/*     */           
/* 367 */           roomDefinition.hasOpening[i] = false;
/* 368 */           (roomDefinition.connections[i]).hasOpening[j] = false;
/*     */           
/* 370 */           if (roomDefinition.findSource(b2++) && roomDefinition.connections[i].findSource(b2++)) {
/* 371 */             b3++;
/*     */             continue;
/*     */           } 
/* 374 */           roomDefinition.hasOpening[i] = true;
/* 375 */           (roomDefinition.connections[i]).hasOpening[j] = true;
/*     */         } 
/*     */       }  }
/*     */ 
/*     */     
/* 380 */     objectArrayList.add(roomDefinition1);
/* 381 */     objectArrayList.add(roomDefinition2);
/* 382 */     objectArrayList.add(roomDefinition3);
/*     */     
/* 384 */     return (List<OceanMonumentPieces.RoomDefinition>)objectArrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/* 389 */     int i = Math.max(paramWorldGenLevel.getSeaLevel(), 64) - this.boundingBox.minY();
/*     */     
/* 391 */     generateWaterBox(paramWorldGenLevel, paramBoundingBox, 0, 0, 0, 58, i, 58);
/*     */ 
/*     */     
/* 394 */     generateWing(false, 0, paramWorldGenLevel, paramRandomSource, paramBoundingBox);
/*     */ 
/*     */     
/* 397 */     generateWing(true, 33, paramWorldGenLevel, paramRandomSource, paramBoundingBox);
/*     */ 
/*     */     
/* 400 */     generateEntranceArchs(paramWorldGenLevel, paramRandomSource, paramBoundingBox);
/*     */     
/* 402 */     generateEntranceWall(paramWorldGenLevel, paramRandomSource, paramBoundingBox);
/* 403 */     generateRoofPiece(paramWorldGenLevel, paramRandomSource, paramBoundingBox);
/*     */     
/* 405 */     generateLowerWall(paramWorldGenLevel, paramRandomSource, paramBoundingBox);
/* 406 */     generateMiddleWall(paramWorldGenLevel, paramRandomSource, paramBoundingBox);
/* 407 */     generateUpperWall(paramWorldGenLevel, paramRandomSource, paramBoundingBox);
/*     */     
/*     */     byte b;
/* 410 */     for (b = 0; b < 7; b++) {
/* 411 */       for (byte b1 = 0; b1 < 7; ) {
/* 412 */         if (b1 == 0 && b == 3)
/*     */         {
/* 414 */           b1 = 6;
/*     */         }
/*     */         
/* 417 */         int j = b * 9;
/* 418 */         int k = b1 * 9;
/* 419 */         for (byte b2 = 0; b2 < 4; b2++) {
/* 420 */           for (byte b3 = 0; b3 < 4; b3++) {
/* 421 */             placeBlock(paramWorldGenLevel, BASE_LIGHT, j + b2, 0, k + b3, paramBoundingBox);
/* 422 */             fillColumnDown(paramWorldGenLevel, BASE_LIGHT, j + b2, -1, k + b3, paramBoundingBox);
/*     */           } 
/*     */         } 
/*     */         
/* 426 */         if (b == 0 || b == 6) {
/* 427 */           b1++; continue;
/*     */         } 
/* 429 */         b1 += 6;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 435 */     for (b = 0; b < 5; b++) {
/* 436 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, -1 - b, 0 + b * 2, -1 - b, -1 - b, 23, 58 + b);
/* 437 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 58 + b, 0 + b * 2, -1 - b, 58 + b, 23, 58 + b);
/* 438 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 0 - b, 0 + b * 2, -1 - b, 57 + b, 23, -1 - b);
/* 439 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 0 - b, 0 + b * 2, 58 + b, 57 + b, 23, 58 + b);
/*     */     } 
/*     */     
/* 442 */     for (OceanMonumentPieces.OceanMonumentPiece oceanMonumentPiece : this.childPieces) {
/* 443 */       if (oceanMonumentPiece.getBoundingBox().intersects(paramBoundingBox)) {
/* 444 */         oceanMonumentPiece.postProcess(paramWorldGenLevel, paramStructureManager, paramChunkGenerator, paramRandomSource, paramBoundingBox, paramChunkPos, paramBlockPos);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateWing(boolean paramBoolean, int paramInt, WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, BoundingBox paramBoundingBox) {
/* 451 */     byte b = 24;
/* 452 */     if (chunkIntersects(paramBoundingBox, paramInt, 0, paramInt + 23, 20)) {
/* 453 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt + 0, 0, 0, paramInt + 24, 0, 20, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 455 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, paramInt + 0, 1, 0, paramInt + 24, 10, 20);
/*     */       int i;
/* 457 */       for (i = 0; i < 4; i++) {
/* 458 */         generateBox(paramWorldGenLevel, paramBoundingBox, paramInt + i, i + 1, i, paramInt + i, i + 1, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 459 */         generateBox(paramWorldGenLevel, paramBoundingBox, paramInt + i + 7, i + 5, i + 7, paramInt + i + 7, i + 5, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 460 */         generateBox(paramWorldGenLevel, paramBoundingBox, paramInt + 17 - i, i + 5, i + 7, paramInt + 17 - i, i + 5, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 461 */         generateBox(paramWorldGenLevel, paramBoundingBox, paramInt + 24 - i, i + 1, i, paramInt + 24 - i, i + 1, 20, BASE_LIGHT, BASE_LIGHT, false);
/*     */         
/* 463 */         generateBox(paramWorldGenLevel, paramBoundingBox, paramInt + i + 1, i + 1, i, paramInt + 23 - i, i + 1, i, BASE_LIGHT, BASE_LIGHT, false);
/* 464 */         generateBox(paramWorldGenLevel, paramBoundingBox, paramInt + i + 8, i + 5, i + 7, paramInt + 16 - i, i + 5, i + 7, BASE_LIGHT, BASE_LIGHT, false);
/*     */       } 
/* 466 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt + 4, 4, 4, paramInt + 6, 4, 20, BASE_GRAY, BASE_GRAY, false);
/* 467 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt + 7, 4, 4, paramInt + 17, 4, 6, BASE_GRAY, BASE_GRAY, false);
/* 468 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt + 18, 4, 4, paramInt + 20, 4, 20, BASE_GRAY, BASE_GRAY, false);
/* 469 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt + 11, 8, 11, paramInt + 13, 8, 20, BASE_GRAY, BASE_GRAY, false);
/* 470 */       placeBlock(paramWorldGenLevel, DOT_DECO_DATA, paramInt + 12, 9, 12, paramBoundingBox);
/* 471 */       placeBlock(paramWorldGenLevel, DOT_DECO_DATA, paramInt + 12, 9, 15, paramBoundingBox);
/* 472 */       placeBlock(paramWorldGenLevel, DOT_DECO_DATA, paramInt + 12, 9, 18, paramBoundingBox);
/*     */       
/* 474 */       i = paramInt + (paramBoolean ? 19 : 5);
/* 475 */       int j = paramInt + (paramBoolean ? 5 : 19); byte b1;
/* 476 */       for (b1 = 20; b1 >= 5; b1 -= 3) {
/* 477 */         placeBlock(paramWorldGenLevel, DOT_DECO_DATA, i, 5, b1, paramBoundingBox);
/*     */       }
/* 479 */       for (b1 = 19; b1 >= 7; b1 -= 3) {
/* 480 */         placeBlock(paramWorldGenLevel, DOT_DECO_DATA, j, 5, b1, paramBoundingBox);
/*     */       }
/* 482 */       for (b1 = 0; b1 < 4; b1++) {
/* 483 */         int k = paramBoolean ? (paramInt + 24 - 17 - b1 * 3) : (paramInt + 17 - b1 * 3);
/* 484 */         placeBlock(paramWorldGenLevel, DOT_DECO_DATA, k, 5, 5, paramBoundingBox);
/*     */       } 
/* 486 */       placeBlock(paramWorldGenLevel, DOT_DECO_DATA, j, 5, 5, paramBoundingBox);
/*     */ 
/*     */       
/* 489 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt + 11, 1, 12, paramInt + 13, 7, 12, BASE_GRAY, BASE_GRAY, false);
/* 490 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt + 12, 1, 11, paramInt + 12, 7, 13, BASE_GRAY, BASE_GRAY, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateEntranceArchs(WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, BoundingBox paramBoundingBox) {
/* 496 */     if (chunkIntersects(paramBoundingBox, 22, 5, 35, 17)) {
/*     */       
/* 498 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 25, 0, 0, 32, 8, 20);
/*     */ 
/*     */       
/* 501 */       for (byte b = 0; b < 4; b++) {
/* 502 */         generateBox(paramWorldGenLevel, paramBoundingBox, 24, 2, 5 + b * 4, 24, 4, 5 + b * 4, BASE_LIGHT, BASE_LIGHT, false);
/* 503 */         generateBox(paramWorldGenLevel, paramBoundingBox, 22, 4, 5 + b * 4, 23, 4, 5 + b * 4, BASE_LIGHT, BASE_LIGHT, false);
/* 504 */         placeBlock(paramWorldGenLevel, BASE_LIGHT, 25, 5, 5 + b * 4, paramBoundingBox);
/* 505 */         placeBlock(paramWorldGenLevel, BASE_LIGHT, 26, 6, 5 + b * 4, paramBoundingBox);
/* 506 */         placeBlock(paramWorldGenLevel, LAMP_BLOCK, 26, 5, 5 + b * 4, paramBoundingBox);
/*     */         
/* 508 */         generateBox(paramWorldGenLevel, paramBoundingBox, 33, 2, 5 + b * 4, 33, 4, 5 + b * 4, BASE_LIGHT, BASE_LIGHT, false);
/* 509 */         generateBox(paramWorldGenLevel, paramBoundingBox, 34, 4, 5 + b * 4, 35, 4, 5 + b * 4, BASE_LIGHT, BASE_LIGHT, false);
/* 510 */         placeBlock(paramWorldGenLevel, BASE_LIGHT, 32, 5, 5 + b * 4, paramBoundingBox);
/* 511 */         placeBlock(paramWorldGenLevel, BASE_LIGHT, 31, 6, 5 + b * 4, paramBoundingBox);
/* 512 */         placeBlock(paramWorldGenLevel, LAMP_BLOCK, 31, 5, 5 + b * 4, paramBoundingBox);
/*     */         
/* 514 */         generateBox(paramWorldGenLevel, paramBoundingBox, 27, 6, 5 + b * 4, 30, 6, 5 + b * 4, BASE_GRAY, BASE_GRAY, false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateEntranceWall(WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, BoundingBox paramBoundingBox) {
/* 522 */     if (chunkIntersects(paramBoundingBox, 15, 20, 42, 21)) {
/* 523 */       generateBox(paramWorldGenLevel, paramBoundingBox, 15, 0, 21, 42, 0, 21, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 525 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 26, 1, 21, 31, 3, 21);
/*     */ 
/*     */ 
/*     */       
/* 529 */       generateBox(paramWorldGenLevel, paramBoundingBox, 21, 12, 21, 36, 12, 21, BASE_GRAY, BASE_GRAY, false);
/* 530 */       generateBox(paramWorldGenLevel, paramBoundingBox, 17, 11, 21, 40, 11, 21, BASE_GRAY, BASE_GRAY, false);
/* 531 */       generateBox(paramWorldGenLevel, paramBoundingBox, 16, 10, 21, 41, 10, 21, BASE_GRAY, BASE_GRAY, false);
/* 532 */       generateBox(paramWorldGenLevel, paramBoundingBox, 15, 7, 21, 42, 9, 21, BASE_GRAY, BASE_GRAY, false);
/* 533 */       generateBox(paramWorldGenLevel, paramBoundingBox, 16, 6, 21, 41, 6, 21, BASE_GRAY, BASE_GRAY, false);
/* 534 */       generateBox(paramWorldGenLevel, paramBoundingBox, 17, 5, 21, 40, 5, 21, BASE_GRAY, BASE_GRAY, false);
/* 535 */       generateBox(paramWorldGenLevel, paramBoundingBox, 21, 4, 21, 36, 4, 21, BASE_GRAY, BASE_GRAY, false);
/* 536 */       generateBox(paramWorldGenLevel, paramBoundingBox, 22, 3, 21, 26, 3, 21, BASE_GRAY, BASE_GRAY, false);
/* 537 */       generateBox(paramWorldGenLevel, paramBoundingBox, 31, 3, 21, 35, 3, 21, BASE_GRAY, BASE_GRAY, false);
/* 538 */       generateBox(paramWorldGenLevel, paramBoundingBox, 23, 2, 21, 25, 2, 21, BASE_GRAY, BASE_GRAY, false);
/* 539 */       generateBox(paramWorldGenLevel, paramBoundingBox, 32, 2, 21, 34, 2, 21, BASE_GRAY, BASE_GRAY, false);
/*     */ 
/*     */       
/* 542 */       generateBox(paramWorldGenLevel, paramBoundingBox, 28, 4, 20, 29, 4, 21, BASE_LIGHT, BASE_LIGHT, false);
/* 543 */       placeBlock(paramWorldGenLevel, BASE_LIGHT, 27, 3, 21, paramBoundingBox);
/* 544 */       placeBlock(paramWorldGenLevel, BASE_LIGHT, 30, 3, 21, paramBoundingBox);
/* 545 */       placeBlock(paramWorldGenLevel, BASE_LIGHT, 26, 2, 21, paramBoundingBox);
/* 546 */       placeBlock(paramWorldGenLevel, BASE_LIGHT, 31, 2, 21, paramBoundingBox);
/* 547 */       placeBlock(paramWorldGenLevel, BASE_LIGHT, 25, 1, 21, paramBoundingBox);
/* 548 */       placeBlock(paramWorldGenLevel, BASE_LIGHT, 32, 1, 21, paramBoundingBox); byte b;
/* 549 */       for (b = 0; b < 7; b++) {
/* 550 */         placeBlock(paramWorldGenLevel, BASE_BLACK, 28 - b, 6 + b, 21, paramBoundingBox);
/* 551 */         placeBlock(paramWorldGenLevel, BASE_BLACK, 29 + b, 6 + b, 21, paramBoundingBox);
/*     */       } 
/* 553 */       for (b = 0; b < 4; b++) {
/* 554 */         placeBlock(paramWorldGenLevel, BASE_BLACK, 28 - b, 9 + b, 21, paramBoundingBox);
/* 555 */         placeBlock(paramWorldGenLevel, BASE_BLACK, 29 + b, 9 + b, 21, paramBoundingBox);
/*     */       } 
/* 557 */       placeBlock(paramWorldGenLevel, BASE_BLACK, 28, 12, 21, paramBoundingBox);
/* 558 */       placeBlock(paramWorldGenLevel, BASE_BLACK, 29, 12, 21, paramBoundingBox);
/* 559 */       for (b = 0; b < 3; b++) {
/* 560 */         placeBlock(paramWorldGenLevel, BASE_BLACK, 22 - b * 2, 8, 21, paramBoundingBox);
/* 561 */         placeBlock(paramWorldGenLevel, BASE_BLACK, 22 - b * 2, 9, 21, paramBoundingBox);
/*     */         
/* 563 */         placeBlock(paramWorldGenLevel, BASE_BLACK, 35 + b * 2, 8, 21, paramBoundingBox);
/* 564 */         placeBlock(paramWorldGenLevel, BASE_BLACK, 35 + b * 2, 9, 21, paramBoundingBox);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 569 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 15, 13, 21, 42, 15, 21);
/* 570 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 15, 1, 21, 15, 6, 21);
/* 571 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 16, 1, 21, 16, 5, 21);
/* 572 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 17, 1, 21, 20, 4, 21);
/* 573 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 21, 1, 21, 21, 3, 21);
/* 574 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 22, 1, 21, 22, 2, 21);
/* 575 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 23, 1, 21, 24, 1, 21);
/* 576 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 42, 1, 21, 42, 6, 21);
/* 577 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 41, 1, 21, 41, 5, 21);
/* 578 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 37, 1, 21, 40, 4, 21);
/* 579 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 36, 1, 21, 36, 3, 21);
/* 580 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 33, 1, 21, 34, 1, 21);
/* 581 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 35, 1, 21, 35, 2, 21);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateRoofPiece(WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, BoundingBox paramBoundingBox) {
/* 589 */     if (chunkIntersects(paramBoundingBox, 21, 21, 36, 36)) {
/* 590 */       generateBox(paramWorldGenLevel, paramBoundingBox, 21, 0, 22, 36, 0, 36, BASE_GRAY, BASE_GRAY, false);
/*     */ 
/*     */ 
/*     */       
/* 594 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 21, 1, 22, 36, 23, 36);
/*     */ 
/*     */       
/* 597 */       for (byte b = 0; b < 4; b++) {
/* 598 */         generateBox(paramWorldGenLevel, paramBoundingBox, 21 + b, 13 + b, 21 + b, 36 - b, 13 + b, 21 + b, BASE_LIGHT, BASE_LIGHT, false);
/* 599 */         generateBox(paramWorldGenLevel, paramBoundingBox, 21 + b, 13 + b, 36 - b, 36 - b, 13 + b, 36 - b, BASE_LIGHT, BASE_LIGHT, false);
/* 600 */         generateBox(paramWorldGenLevel, paramBoundingBox, 21 + b, 13 + b, 22 + b, 21 + b, 13 + b, 35 - b, BASE_LIGHT, BASE_LIGHT, false);
/* 601 */         generateBox(paramWorldGenLevel, paramBoundingBox, 36 - b, 13 + b, 22 + b, 36 - b, 13 + b, 35 - b, BASE_LIGHT, BASE_LIGHT, false);
/*     */       } 
/* 603 */       generateBox(paramWorldGenLevel, paramBoundingBox, 25, 16, 25, 32, 16, 32, BASE_GRAY, BASE_GRAY, false);
/* 604 */       generateBox(paramWorldGenLevel, paramBoundingBox, 25, 17, 25, 25, 19, 25, BASE_LIGHT, BASE_LIGHT, false);
/* 605 */       generateBox(paramWorldGenLevel, paramBoundingBox, 32, 17, 25, 32, 19, 25, BASE_LIGHT, BASE_LIGHT, false);
/* 606 */       generateBox(paramWorldGenLevel, paramBoundingBox, 25, 17, 32, 25, 19, 32, BASE_LIGHT, BASE_LIGHT, false);
/* 607 */       generateBox(paramWorldGenLevel, paramBoundingBox, 32, 17, 32, 32, 19, 32, BASE_LIGHT, BASE_LIGHT, false);
/*     */       
/* 609 */       placeBlock(paramWorldGenLevel, BASE_LIGHT, 26, 20, 26, paramBoundingBox);
/* 610 */       placeBlock(paramWorldGenLevel, BASE_LIGHT, 27, 21, 27, paramBoundingBox);
/* 611 */       placeBlock(paramWorldGenLevel, LAMP_BLOCK, 27, 20, 27, paramBoundingBox);
/* 612 */       placeBlock(paramWorldGenLevel, BASE_LIGHT, 26, 20, 31, paramBoundingBox);
/* 613 */       placeBlock(paramWorldGenLevel, BASE_LIGHT, 27, 21, 30, paramBoundingBox);
/* 614 */       placeBlock(paramWorldGenLevel, LAMP_BLOCK, 27, 20, 30, paramBoundingBox);
/* 615 */       placeBlock(paramWorldGenLevel, BASE_LIGHT, 31, 20, 31, paramBoundingBox);
/* 616 */       placeBlock(paramWorldGenLevel, BASE_LIGHT, 30, 21, 30, paramBoundingBox);
/* 617 */       placeBlock(paramWorldGenLevel, LAMP_BLOCK, 30, 20, 30, paramBoundingBox);
/* 618 */       placeBlock(paramWorldGenLevel, BASE_LIGHT, 31, 20, 26, paramBoundingBox);
/* 619 */       placeBlock(paramWorldGenLevel, BASE_LIGHT, 30, 21, 27, paramBoundingBox);
/* 620 */       placeBlock(paramWorldGenLevel, LAMP_BLOCK, 30, 20, 27, paramBoundingBox);
/*     */       
/* 622 */       generateBox(paramWorldGenLevel, paramBoundingBox, 28, 21, 27, 29, 21, 27, BASE_GRAY, BASE_GRAY, false);
/* 623 */       generateBox(paramWorldGenLevel, paramBoundingBox, 27, 21, 28, 27, 21, 29, BASE_GRAY, BASE_GRAY, false);
/* 624 */       generateBox(paramWorldGenLevel, paramBoundingBox, 28, 21, 30, 29, 21, 30, BASE_GRAY, BASE_GRAY, false);
/* 625 */       generateBox(paramWorldGenLevel, paramBoundingBox, 30, 21, 28, 30, 21, 29, BASE_GRAY, BASE_GRAY, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateLowerWall(WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, BoundingBox paramBoundingBox) {
/* 632 */     if (chunkIntersects(paramBoundingBox, 0, 21, 6, 58)) {
/* 633 */       generateBox(paramWorldGenLevel, paramBoundingBox, 0, 0, 21, 6, 0, 57, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 635 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 0, 1, 21, 6, 7, 57);
/*     */ 
/*     */       
/* 638 */       generateBox(paramWorldGenLevel, paramBoundingBox, 4, 4, 21, 6, 4, 53, BASE_GRAY, BASE_GRAY, false); byte b;
/* 639 */       for (b = 0; b < 4; b++) {
/* 640 */         generateBox(paramWorldGenLevel, paramBoundingBox, b, b + 1, 21, b, b + 1, 57 - b, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/* 642 */       for (b = 23; b < 53; b += 3) {
/* 643 */         placeBlock(paramWorldGenLevel, DOT_DECO_DATA, 5, 5, b, paramBoundingBox);
/*     */       }
/* 645 */       placeBlock(paramWorldGenLevel, DOT_DECO_DATA, 5, 5, 52, paramBoundingBox);
/*     */       
/* 647 */       for (b = 0; b < 4; b++) {
/* 648 */         generateBox(paramWorldGenLevel, paramBoundingBox, b, b + 1, 21, b, b + 1, 57 - b, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/*     */       
/* 651 */       generateBox(paramWorldGenLevel, paramBoundingBox, 4, 1, 52, 6, 3, 52, BASE_GRAY, BASE_GRAY, false);
/* 652 */       generateBox(paramWorldGenLevel, paramBoundingBox, 5, 1, 51, 5, 3, 53, BASE_GRAY, BASE_GRAY, false);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 657 */     if (chunkIntersects(paramBoundingBox, 51, 21, 58, 58)) {
/* 658 */       generateBox(paramWorldGenLevel, paramBoundingBox, 51, 0, 21, 57, 0, 57, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 660 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 51, 1, 21, 57, 7, 57);
/*     */ 
/*     */       
/* 663 */       generateBox(paramWorldGenLevel, paramBoundingBox, 51, 4, 21, 53, 4, 53, BASE_GRAY, BASE_GRAY, false); byte b;
/* 664 */       for (b = 0; b < 4; b++) {
/* 665 */         generateBox(paramWorldGenLevel, paramBoundingBox, 57 - b, b + 1, 21, 57 - b, b + 1, 57 - b, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/* 667 */       for (b = 23; b < 53; b += 3) {
/* 668 */         placeBlock(paramWorldGenLevel, DOT_DECO_DATA, 52, 5, b, paramBoundingBox);
/*     */       }
/* 670 */       placeBlock(paramWorldGenLevel, DOT_DECO_DATA, 52, 5, 52, paramBoundingBox);
/*     */ 
/*     */       
/* 673 */       generateBox(paramWorldGenLevel, paramBoundingBox, 51, 1, 52, 53, 3, 52, BASE_GRAY, BASE_GRAY, false);
/* 674 */       generateBox(paramWorldGenLevel, paramBoundingBox, 52, 1, 51, 52, 3, 53, BASE_GRAY, BASE_GRAY, false);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 679 */     if (chunkIntersects(paramBoundingBox, 0, 51, 57, 57)) {
/* 680 */       generateBox(paramWorldGenLevel, paramBoundingBox, 7, 0, 51, 50, 0, 57, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 682 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 7, 1, 51, 50, 10, 57);
/*     */ 
/*     */       
/* 685 */       for (byte b = 0; b < 4; b++) {
/* 686 */         generateBox(paramWorldGenLevel, paramBoundingBox, b + 1, b + 1, 57 - b, 56 - b, b + 1, 57 - b, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateMiddleWall(WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, BoundingBox paramBoundingBox) {
/* 694 */     if (chunkIntersects(paramBoundingBox, 7, 21, 13, 50)) {
/* 695 */       generateBox(paramWorldGenLevel, paramBoundingBox, 7, 0, 21, 13, 0, 50, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 697 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 7, 1, 21, 13, 10, 50);
/*     */ 
/*     */       
/* 700 */       generateBox(paramWorldGenLevel, paramBoundingBox, 11, 8, 21, 13, 8, 53, BASE_GRAY, BASE_GRAY, false); byte b;
/* 701 */       for (b = 0; b < 4; b++) {
/* 702 */         generateBox(paramWorldGenLevel, paramBoundingBox, b + 7, b + 5, 21, b + 7, b + 5, 54, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/* 704 */       for (b = 21; b <= 45; b += 3) {
/* 705 */         placeBlock(paramWorldGenLevel, DOT_DECO_DATA, 12, 9, b, paramBoundingBox);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 711 */     if (chunkIntersects(paramBoundingBox, 44, 21, 50, 54)) {
/* 712 */       generateBox(paramWorldGenLevel, paramBoundingBox, 44, 0, 21, 50, 0, 50, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 714 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 44, 1, 21, 50, 10, 50);
/*     */ 
/*     */       
/* 717 */       generateBox(paramWorldGenLevel, paramBoundingBox, 44, 8, 21, 46, 8, 53, BASE_GRAY, BASE_GRAY, false); byte b;
/* 718 */       for (b = 0; b < 4; b++) {
/* 719 */         generateBox(paramWorldGenLevel, paramBoundingBox, 50 - b, b + 5, 21, 50 - b, b + 5, 54, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/* 721 */       for (b = 21; b <= 45; b += 3) {
/* 722 */         placeBlock(paramWorldGenLevel, DOT_DECO_DATA, 45, 9, b, paramBoundingBox);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 728 */     if (chunkIntersects(paramBoundingBox, 8, 44, 49, 54)) {
/* 729 */       generateBox(paramWorldGenLevel, paramBoundingBox, 14, 0, 44, 43, 0, 50, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 731 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 14, 1, 44, 43, 10, 50);
/*     */       
/*     */       byte b;
/* 734 */       for (b = 12; b <= 45; b += 3) {
/* 735 */         placeBlock(paramWorldGenLevel, DOT_DECO_DATA, b, 9, 45, paramBoundingBox);
/* 736 */         placeBlock(paramWorldGenLevel, DOT_DECO_DATA, b, 9, 52, paramBoundingBox);
/* 737 */         if (b == 12 || b == 18 || b == 24 || b == 33 || b == 39 || b == 45) {
/* 738 */           placeBlock(paramWorldGenLevel, DOT_DECO_DATA, b, 9, 47, paramBoundingBox);
/* 739 */           placeBlock(paramWorldGenLevel, DOT_DECO_DATA, b, 9, 50, paramBoundingBox);
/* 740 */           placeBlock(paramWorldGenLevel, DOT_DECO_DATA, b, 10, 45, paramBoundingBox);
/* 741 */           placeBlock(paramWorldGenLevel, DOT_DECO_DATA, b, 10, 46, paramBoundingBox);
/* 742 */           placeBlock(paramWorldGenLevel, DOT_DECO_DATA, b, 10, 51, paramBoundingBox);
/* 743 */           placeBlock(paramWorldGenLevel, DOT_DECO_DATA, b, 10, 52, paramBoundingBox);
/* 744 */           placeBlock(paramWorldGenLevel, DOT_DECO_DATA, b, 11, 47, paramBoundingBox);
/* 745 */           placeBlock(paramWorldGenLevel, DOT_DECO_DATA, b, 11, 50, paramBoundingBox);
/* 746 */           placeBlock(paramWorldGenLevel, DOT_DECO_DATA, b, 12, 48, paramBoundingBox);
/* 747 */           placeBlock(paramWorldGenLevel, DOT_DECO_DATA, b, 12, 49, paramBoundingBox);
/*     */         } 
/*     */       } 
/*     */       
/* 751 */       for (b = 0; b < 3; b++) {
/* 752 */         generateBox(paramWorldGenLevel, paramBoundingBox, 8 + b, 5 + b, 54, 49 - b, 5 + b, 54, BASE_GRAY, BASE_GRAY, false);
/*     */       }
/* 754 */       generateBox(paramWorldGenLevel, paramBoundingBox, 11, 8, 54, 46, 8, 54, BASE_LIGHT, BASE_LIGHT, false);
/* 755 */       generateBox(paramWorldGenLevel, paramBoundingBox, 14, 8, 44, 43, 8, 53, BASE_GRAY, BASE_GRAY, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateUpperWall(WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, BoundingBox paramBoundingBox) {
/* 762 */     if (chunkIntersects(paramBoundingBox, 14, 21, 20, 43)) {
/* 763 */       generateBox(paramWorldGenLevel, paramBoundingBox, 14, 0, 21, 20, 0, 43, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 765 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 14, 1, 22, 20, 14, 43);
/*     */ 
/*     */       
/* 768 */       generateBox(paramWorldGenLevel, paramBoundingBox, 18, 12, 22, 20, 12, 39, BASE_GRAY, BASE_GRAY, false);
/* 769 */       generateBox(paramWorldGenLevel, paramBoundingBox, 18, 12, 21, 20, 12, 21, BASE_LIGHT, BASE_LIGHT, false); byte b;
/* 770 */       for (b = 0; b < 4; b++) {
/* 771 */         generateBox(paramWorldGenLevel, paramBoundingBox, b + 14, b + 9, 21, b + 14, b + 9, 43 - b, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/* 773 */       for (b = 23; b <= 39; b += 3) {
/* 774 */         placeBlock(paramWorldGenLevel, DOT_DECO_DATA, 19, 13, b, paramBoundingBox);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 780 */     if (chunkIntersects(paramBoundingBox, 37, 21, 43, 43)) {
/* 781 */       generateBox(paramWorldGenLevel, paramBoundingBox, 37, 0, 21, 43, 0, 43, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 783 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 37, 1, 22, 43, 14, 43);
/*     */ 
/*     */       
/* 786 */       generateBox(paramWorldGenLevel, paramBoundingBox, 37, 12, 22, 39, 12, 39, BASE_GRAY, BASE_GRAY, false);
/* 787 */       generateBox(paramWorldGenLevel, paramBoundingBox, 37, 12, 21, 39, 12, 21, BASE_LIGHT, BASE_LIGHT, false); byte b;
/* 788 */       for (b = 0; b < 4; b++) {
/* 789 */         generateBox(paramWorldGenLevel, paramBoundingBox, 43 - b, b + 9, 21, 43 - b, b + 9, 43 - b, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/* 791 */       for (b = 23; b <= 39; b += 3) {
/* 792 */         placeBlock(paramWorldGenLevel, DOT_DECO_DATA, 38, 13, b, paramBoundingBox);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 798 */     if (chunkIntersects(paramBoundingBox, 15, 37, 42, 43)) {
/* 799 */       generateBox(paramWorldGenLevel, paramBoundingBox, 21, 0, 37, 36, 0, 43, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 801 */       generateWaterBox(paramWorldGenLevel, paramBoundingBox, 21, 1, 37, 36, 14, 43);
/*     */ 
/*     */       
/* 804 */       generateBox(paramWorldGenLevel, paramBoundingBox, 21, 12, 37, 36, 12, 39, BASE_GRAY, BASE_GRAY, false); byte b;
/* 805 */       for (b = 0; b < 4; b++) {
/* 806 */         generateBox(paramWorldGenLevel, paramBoundingBox, 15 + b, b + 9, 43 - b, 42 - b, b + 9, 43 - b, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/* 808 */       for (b = 21; b <= 36; b += 3)
/* 809 */         placeBlock(paramWorldGenLevel, DOT_DECO_DATA, b, 13, 38, paramBoundingBox); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces$MonumentBuilding.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */