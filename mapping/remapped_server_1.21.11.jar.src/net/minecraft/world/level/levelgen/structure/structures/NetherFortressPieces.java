/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.StructureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.FenceBlock;
/*      */ import net.minecraft.world.level.block.StairBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.Fluids;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ 
/*      */ public class NetherFortressPieces
/*      */ {
/*      */   private static final int MAX_DEPTH = 30;
/*      */   private static final int LOWEST_Y_POSITION = 10;
/*      */   public static final int MAGIC_START_Y = 64;
/*      */   
/*      */   private static class PieceWeight {
/*      */     public final Class<? extends NetherFortressPieces.NetherBridgePiece> pieceClass;
/*      */     public final int weight;
/*      */     public int placeCount;
/*      */     public final int maxPlaceCount;
/*      */     public final boolean allowInRow;
/*      */     
/*      */     public PieceWeight(Class<? extends NetherFortressPieces.NetherBridgePiece> param1Class, int param1Int1, int param1Int2, boolean param1Boolean) {
/*   46 */       this.pieceClass = param1Class;
/*   47 */       this.weight = param1Int1;
/*   48 */       this.maxPlaceCount = param1Int2;
/*   49 */       this.allowInRow = param1Boolean;
/*      */     }
/*      */     
/*      */     public PieceWeight(Class<? extends NetherFortressPieces.NetherBridgePiece> param1Class, int param1Int1, int param1Int2) {
/*   53 */       this(param1Class, param1Int1, param1Int2, false);
/*      */     }
/*      */     
/*      */     public boolean doPlace(int param1Int) {
/*   57 */       return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*      */     }
/*      */     
/*      */     public boolean isValid() {
/*   61 */       return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*      */     }
/*      */   }
/*      */   
/*   65 */   static final PieceWeight[] BRIDGE_PIECE_WEIGHTS = new PieceWeight[] { new PieceWeight((Class)BridgeStraight.class, 30, 0, true), new PieceWeight((Class)BridgeCrossing.class, 10, 4), new PieceWeight((Class)RoomCrossing.class, 10, 4), new PieceWeight((Class)StairsRoom.class, 10, 3), new PieceWeight((Class)MonsterThrone.class, 5, 2), new PieceWeight((Class)CastleEntrance.class, 5, 1) };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   73 */   static final PieceWeight[] CASTLE_PIECE_WEIGHTS = new PieceWeight[] { new PieceWeight((Class)CastleSmallCorridorPiece.class, 25, 0, true), new PieceWeight((Class)CastleSmallCorridorCrossingPiece.class, 15, 5), new PieceWeight((Class)CastleSmallCorridorRightTurnPiece.class, 5, 10), new PieceWeight((Class)CastleSmallCorridorLeftTurnPiece.class, 5, 10), new PieceWeight((Class)CastleCorridorStairsPiece.class, 10, 3, true), new PieceWeight((Class)CastleCorridorTBalconyPiece.class, 7, 2), new PieceWeight((Class)CastleStalkRoom.class, 5, 2) };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static NetherBridgePiece findAndCreateBridgePieceFactory(PieceWeight paramPieceWeight, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, Direction paramDirection, int paramInt4) {
/*      */     CastleStalkRoom castleStalkRoom;
/*   84 */     Class<? extends NetherBridgePiece> clazz = paramPieceWeight.pieceClass;
/*   85 */     BridgeStraight bridgeStraight = null;
/*      */     
/*   87 */     if (clazz == BridgeStraight.class) {
/*   88 */       bridgeStraight = BridgeStraight.createPiece(paramStructurePieceAccessor, paramRandomSource, paramInt1, paramInt2, paramInt3, paramDirection, paramInt4);
/*   89 */     } else if (clazz == BridgeCrossing.class) {
/*   90 */       BridgeCrossing bridgeCrossing = BridgeCrossing.createPiece(paramStructurePieceAccessor, paramInt1, paramInt2, paramInt3, paramDirection, paramInt4);
/*   91 */     } else if (clazz == RoomCrossing.class) {
/*   92 */       RoomCrossing roomCrossing = RoomCrossing.createPiece(paramStructurePieceAccessor, paramInt1, paramInt2, paramInt3, paramDirection, paramInt4);
/*   93 */     } else if (clazz == StairsRoom.class) {
/*   94 */       StairsRoom stairsRoom = StairsRoom.createPiece(paramStructurePieceAccessor, paramInt1, paramInt2, paramInt3, paramInt4, paramDirection);
/*   95 */     } else if (clazz == MonsterThrone.class) {
/*   96 */       MonsterThrone monsterThrone = MonsterThrone.createPiece(paramStructurePieceAccessor, paramInt1, paramInt2, paramInt3, paramInt4, paramDirection);
/*   97 */     } else if (clazz == CastleEntrance.class) {
/*   98 */       CastleEntrance castleEntrance = CastleEntrance.createPiece(paramStructurePieceAccessor, paramRandomSource, paramInt1, paramInt2, paramInt3, paramDirection, paramInt4);
/*   99 */     } else if (clazz == CastleSmallCorridorPiece.class) {
/*  100 */       CastleSmallCorridorPiece castleSmallCorridorPiece = CastleSmallCorridorPiece.createPiece(paramStructurePieceAccessor, paramInt1, paramInt2, paramInt3, paramDirection, paramInt4);
/*  101 */     } else if (clazz == CastleSmallCorridorRightTurnPiece.class) {
/*  102 */       CastleSmallCorridorRightTurnPiece castleSmallCorridorRightTurnPiece = CastleSmallCorridorRightTurnPiece.createPiece(paramStructurePieceAccessor, paramRandomSource, paramInt1, paramInt2, paramInt3, paramDirection, paramInt4);
/*  103 */     } else if (clazz == CastleSmallCorridorLeftTurnPiece.class) {
/*  104 */       CastleSmallCorridorLeftTurnPiece castleSmallCorridorLeftTurnPiece = CastleSmallCorridorLeftTurnPiece.createPiece(paramStructurePieceAccessor, paramRandomSource, paramInt1, paramInt2, paramInt3, paramDirection, paramInt4);
/*  105 */     } else if (clazz == CastleCorridorStairsPiece.class) {
/*  106 */       CastleCorridorStairsPiece castleCorridorStairsPiece = CastleCorridorStairsPiece.createPiece(paramStructurePieceAccessor, paramInt1, paramInt2, paramInt3, paramDirection, paramInt4);
/*  107 */     } else if (clazz == CastleCorridorTBalconyPiece.class) {
/*  108 */       CastleCorridorTBalconyPiece castleCorridorTBalconyPiece = CastleCorridorTBalconyPiece.createPiece(paramStructurePieceAccessor, paramInt1, paramInt2, paramInt3, paramDirection, paramInt4);
/*  109 */     } else if (clazz == CastleSmallCorridorCrossingPiece.class) {
/*  110 */       CastleSmallCorridorCrossingPiece castleSmallCorridorCrossingPiece = CastleSmallCorridorCrossingPiece.createPiece(paramStructurePieceAccessor, paramInt1, paramInt2, paramInt3, paramDirection, paramInt4);
/*  111 */     } else if (clazz == CastleStalkRoom.class) {
/*  112 */       castleStalkRoom = CastleStalkRoom.createPiece(paramStructurePieceAccessor, paramInt1, paramInt2, paramInt3, paramDirection, paramInt4);
/*      */     } 
/*  114 */     return castleStalkRoom;
/*      */   }
/*      */   
/*      */   private static abstract class NetherBridgePiece extends StructurePiece {
/*      */     protected NetherBridgePiece(StructurePieceType param1StructurePieceType, int param1Int, BoundingBox param1BoundingBox) {
/*  119 */       super(param1StructurePieceType, param1Int, param1BoundingBox);
/*      */     }
/*      */     
/*      */     public NetherBridgePiece(StructurePieceType param1StructurePieceType, CompoundTag param1CompoundTag) {
/*  123 */       super(param1StructurePieceType, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {}
/*      */ 
/*      */     
/*      */     private int updatePieceWeight(List<NetherFortressPieces.PieceWeight> param1List) {
/*  131 */       boolean bool = false;
/*  132 */       int i = 0;
/*  133 */       for (NetherFortressPieces.PieceWeight pieceWeight : param1List) {
/*  134 */         if (pieceWeight.maxPlaceCount > 0 && pieceWeight.placeCount < pieceWeight.maxPlaceCount) {
/*  135 */           bool = true;
/*      */         }
/*  137 */         i += pieceWeight.weight;
/*      */       } 
/*  139 */       return bool ? i : -1;
/*      */     }
/*      */     
/*      */     private NetherBridgePiece generatePiece(NetherFortressPieces.StartPiece param1StartPiece, List<NetherFortressPieces.PieceWeight> param1List, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction, int param1Int4) {
/*  143 */       int i = updatePieceWeight(param1List);
/*  144 */       boolean bool = (i > 0 && param1Int4 <= 30) ? true : false;
/*      */       
/*  146 */       byte b = 0;
/*  147 */       while (b < 5 && bool) {
/*  148 */         b++;
/*      */         
/*  150 */         int j = param1RandomSource.nextInt(i);
/*  151 */         for (NetherFortressPieces.PieceWeight pieceWeight : param1List) {
/*  152 */           j -= pieceWeight.weight;
/*  153 */           if (j < 0) {
/*  154 */             if (!pieceWeight.doPlace(param1Int4) || (pieceWeight == param1StartPiece.previousPiece && !pieceWeight.allowInRow)) {
/*      */               break;
/*      */             }
/*      */             
/*  158 */             NetherBridgePiece netherBridgePiece = NetherFortressPieces.findAndCreateBridgePieceFactory(pieceWeight, param1StructurePieceAccessor, param1RandomSource, param1Int1, param1Int2, param1Int3, param1Direction, param1Int4);
/*  159 */             if (netherBridgePiece != null) {
/*  160 */               pieceWeight.placeCount++;
/*  161 */               param1StartPiece.previousPiece = pieceWeight;
/*      */               
/*  163 */               if (!pieceWeight.isValid()) {
/*  164 */                 param1List.remove(pieceWeight);
/*      */               }
/*  166 */               return netherBridgePiece;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*  171 */       return NetherFortressPieces.BridgeEndFiller.createPiece(param1StructurePieceAccessor, param1RandomSource, param1Int1, param1Int2, param1Int3, param1Direction, param1Int4);
/*      */     }
/*      */     
/*      */     private StructurePiece generateAndAddPiece(NetherFortressPieces.StartPiece param1StartPiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction, int param1Int4, boolean param1Boolean) {
/*  175 */       if (Math.abs(param1Int1 - param1StartPiece.getBoundingBox().minX()) > 112 || Math.abs(param1Int3 - param1StartPiece.getBoundingBox().minZ()) > 112) {
/*  176 */         return NetherFortressPieces.BridgeEndFiller.createPiece(param1StructurePieceAccessor, param1RandomSource, param1Int1, param1Int2, param1Int3, param1Direction, param1Int4);
/*      */       }
/*  178 */       List<NetherFortressPieces.PieceWeight> list = param1StartPiece.availableBridgePieces;
/*  179 */       if (param1Boolean) {
/*  180 */         list = param1StartPiece.availableCastlePieces;
/*      */       }
/*  182 */       NetherBridgePiece netherBridgePiece = generatePiece(param1StartPiece, list, param1StructurePieceAccessor, param1RandomSource, param1Int1, param1Int2, param1Int3, param1Direction, param1Int4 + 1);
/*  183 */       if (netherBridgePiece != null) {
/*  184 */         param1StructurePieceAccessor.addPiece(netherBridgePiece);
/*  185 */         param1StartPiece.pendingChildren.add(netherBridgePiece);
/*      */       } 
/*  187 */       return netherBridgePiece;
/*      */     }
/*      */     
/*      */     protected StructurePiece generateChildForward(NetherFortressPieces.StartPiece param1StartPiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource, int param1Int1, int param1Int2, boolean param1Boolean) {
/*  191 */       Direction direction = getOrientation();
/*  192 */       if (direction != null) {
/*  193 */         switch (direction) {
/*      */           case NORTH:
/*  195 */             return generateAndAddPiece(param1StartPiece, param1StructurePieceAccessor, param1RandomSource, this.boundingBox.minX() + param1Int1, this.boundingBox.minY() + param1Int2, this.boundingBox.minZ() - 1, direction, getGenDepth(), param1Boolean);
/*      */           case SOUTH:
/*  197 */             return generateAndAddPiece(param1StartPiece, param1StructurePieceAccessor, param1RandomSource, this.boundingBox.minX() + param1Int1, this.boundingBox.minY() + param1Int2, this.boundingBox.maxZ() + 1, direction, getGenDepth(), param1Boolean);
/*      */           case WEST:
/*  199 */             return generateAndAddPiece(param1StartPiece, param1StructurePieceAccessor, param1RandomSource, this.boundingBox.minX() - 1, this.boundingBox.minY() + param1Int2, this.boundingBox.minZ() + param1Int1, direction, getGenDepth(), param1Boolean);
/*      */           case EAST:
/*  201 */             return generateAndAddPiece(param1StartPiece, param1StructurePieceAccessor, param1RandomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY() + param1Int2, this.boundingBox.minZ() + param1Int1, direction, getGenDepth(), param1Boolean);
/*      */         } 
/*      */       }
/*  204 */       return null;
/*      */     }
/*      */     
/*      */     protected StructurePiece generateChildLeft(NetherFortressPieces.StartPiece param1StartPiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource, int param1Int1, int param1Int2, boolean param1Boolean) {
/*  208 */       Direction direction = getOrientation();
/*  209 */       if (direction != null) {
/*  210 */         switch (direction) {
/*      */           case NORTH:
/*  212 */             return generateAndAddPiece(param1StartPiece, param1StructurePieceAccessor, param1RandomSource, this.boundingBox.minX() - 1, this.boundingBox.minY() + param1Int1, this.boundingBox.minZ() + param1Int2, Direction.WEST, getGenDepth(), param1Boolean);
/*      */           case SOUTH:
/*  214 */             return generateAndAddPiece(param1StartPiece, param1StructurePieceAccessor, param1RandomSource, this.boundingBox.minX() - 1, this.boundingBox.minY() + param1Int1, this.boundingBox.minZ() + param1Int2, Direction.WEST, getGenDepth(), param1Boolean);
/*      */           case WEST:
/*  216 */             return generateAndAddPiece(param1StartPiece, param1StructurePieceAccessor, param1RandomSource, this.boundingBox.minX() + param1Int2, this.boundingBox.minY() + param1Int1, this.boundingBox.minZ() - 1, Direction.NORTH, getGenDepth(), param1Boolean);
/*      */           case EAST:
/*  218 */             return generateAndAddPiece(param1StartPiece, param1StructurePieceAccessor, param1RandomSource, this.boundingBox.minX() + param1Int2, this.boundingBox.minY() + param1Int1, this.boundingBox.minZ() - 1, Direction.NORTH, getGenDepth(), param1Boolean);
/*      */         } 
/*      */       }
/*  221 */       return null;
/*      */     }
/*      */     
/*      */     protected StructurePiece generateChildRight(NetherFortressPieces.StartPiece param1StartPiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource, int param1Int1, int param1Int2, boolean param1Boolean) {
/*  225 */       Direction direction = getOrientation();
/*  226 */       if (direction != null) {
/*  227 */         switch (direction) {
/*      */           case NORTH:
/*  229 */             return generateAndAddPiece(param1StartPiece, param1StructurePieceAccessor, param1RandomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY() + param1Int1, this.boundingBox.minZ() + param1Int2, Direction.EAST, getGenDepth(), param1Boolean);
/*      */           case SOUTH:
/*  231 */             return generateAndAddPiece(param1StartPiece, param1StructurePieceAccessor, param1RandomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY() + param1Int1, this.boundingBox.minZ() + param1Int2, Direction.EAST, getGenDepth(), param1Boolean);
/*      */           case WEST:
/*  233 */             return generateAndAddPiece(param1StartPiece, param1StructurePieceAccessor, param1RandomSource, this.boundingBox.minX() + param1Int2, this.boundingBox.minY() + param1Int1, this.boundingBox.maxZ() + 1, Direction.SOUTH, getGenDepth(), param1Boolean);
/*      */           case EAST:
/*  235 */             return generateAndAddPiece(param1StartPiece, param1StructurePieceAccessor, param1RandomSource, this.boundingBox.minX() + param1Int2, this.boundingBox.minY() + param1Int1, this.boundingBox.maxZ() + 1, Direction.SOUTH, getGenDepth(), param1Boolean);
/*      */         } 
/*      */       }
/*  238 */       return null;
/*      */     }
/*      */     
/*      */     protected static boolean isOkBox(BoundingBox param1BoundingBox) {
/*  242 */       return (param1BoundingBox.minY() > 10);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class StartPiece
/*      */     extends BridgeCrossing {
/*      */     NetherFortressPieces.PieceWeight previousPiece;
/*  249 */     final List<NetherFortressPieces.PieceWeight> availableBridgePieces = new ArrayList<>();
/*  250 */     final List<NetherFortressPieces.PieceWeight> availableCastlePieces = new ArrayList<>();
/*      */ 
/*      */     
/*  253 */     public final List<StructurePiece> pendingChildren = Lists.newArrayList();
/*      */     
/*      */     public StartPiece(RandomSource param1RandomSource, int param1Int1, int param1Int2) {
/*  256 */       super(param1Int1, param1Int2, getRandomHorizontalDirection(param1RandomSource));
/*      */       
/*  258 */       for (NetherFortressPieces.PieceWeight pieceWeight : NetherFortressPieces.BRIDGE_PIECE_WEIGHTS) {
/*  259 */         pieceWeight.placeCount = 0;
/*  260 */         this.availableBridgePieces.add(pieceWeight);
/*      */       } 
/*      */       
/*  263 */       for (NetherFortressPieces.PieceWeight pieceWeight : NetherFortressPieces.CASTLE_PIECE_WEIGHTS) {
/*  264 */         pieceWeight.placeCount = 0;
/*  265 */         this.availableCastlePieces.add(pieceWeight);
/*      */       } 
/*      */     }
/*      */     
/*      */     public StartPiece(CompoundTag param1CompoundTag) {
/*  270 */       super(StructurePieceType.NETHER_FORTRESS_START, param1CompoundTag);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class BridgeStraight extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 10;
/*      */     private static final int DEPTH = 19;
/*      */     
/*      */     public BridgeStraight(int param1Int, RandomSource param1RandomSource, BoundingBox param1BoundingBox, Direction param1Direction) {
/*  280 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_STRAIGHT, param1Int, param1BoundingBox);
/*      */       
/*  282 */       setOrientation(param1Direction);
/*      */     }
/*      */     
/*      */     public BridgeStraight(CompoundTag param1CompoundTag) {
/*  286 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_STRAIGHT, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece param1StructurePiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource) {
/*  291 */       generateChildForward((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 1, 3, false);
/*      */     }
/*      */     
/*      */     public static BridgeStraight createPiece(StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction, int param1Int4) {
/*  295 */       BoundingBox boundingBox = BoundingBox.orientBox(param1Int1, param1Int2, param1Int3, -1, -3, 0, 5, 10, 19, param1Direction);
/*      */       
/*  297 */       if (!isOkBox(boundingBox) || param1StructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/*  298 */         return null;
/*      */       }
/*      */       
/*  301 */       return new BridgeStraight(param1Int4, param1RandomSource, boundingBox, param1Direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/*  307 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 0, 4, 4, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  309 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 5, 0, 3, 7, 18, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  312 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 5, 0, 0, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  313 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 5, 0, 4, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  316 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 4, 2, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  317 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 13, 4, 2, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  318 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 0, 4, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  319 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 15, 4, 1, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  321 */       for (byte b = 0; b <= 4; b++) {
/*  322 */         for (byte b1 = 0; b1 <= 2; b1++) {
/*  323 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, b1, param1BoundingBox);
/*  324 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, 18 - b1, param1BoundingBox);
/*      */         } 
/*      */       } 
/*      */       
/*  328 */       BlockState blockState1 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*  329 */       BlockState blockState2 = (BlockState)blockState1.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  330 */       BlockState blockState3 = (BlockState)blockState1.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true));
/*  331 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 1, 0, 4, 1, blockState2, blockState2, false);
/*  332 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 4, 0, 4, 4, blockState2, blockState2, false);
/*  333 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 14, 0, 4, 14, blockState2, blockState2, false);
/*  334 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 1, 17, 0, 4, 17, blockState2, blockState2, false);
/*  335 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 1, 1, 4, 4, 1, blockState3, blockState3, false);
/*  336 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 3, 4, 4, 4, 4, blockState3, blockState3, false);
/*  337 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 3, 14, 4, 4, 14, blockState3, blockState3, false);
/*  338 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 1, 17, 4, 4, 17, blockState3, blockState3, false);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class BridgeEndFiller
/*      */     extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 10;
/*      */     private static final int DEPTH = 8;
/*      */     private final int selfSeed;
/*      */     
/*      */     public BridgeEndFiller(int param1Int, RandomSource param1RandomSource, BoundingBox param1BoundingBox, Direction param1Direction) {
/*  350 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_END_FILLER, param1Int, param1BoundingBox);
/*      */       
/*  352 */       setOrientation(param1Direction);
/*  353 */       this.selfSeed = param1RandomSource.nextInt();
/*      */     }
/*      */     
/*      */     public BridgeEndFiller(CompoundTag param1CompoundTag) {
/*  357 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_END_FILLER, param1CompoundTag);
/*  358 */       this.selfSeed = param1CompoundTag.getIntOr("Seed", 0);
/*      */     }
/*      */     
/*      */     public static BridgeEndFiller createPiece(StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction, int param1Int4) {
/*  362 */       BoundingBox boundingBox = BoundingBox.orientBox(param1Int1, param1Int2, param1Int3, -1, -3, 0, 5, 10, 8, param1Direction);
/*      */       
/*  364 */       if (!isOkBox(boundingBox) || param1StructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/*  365 */         return null;
/*      */       }
/*      */       
/*  368 */       return new BridgeEndFiller(param1Int4, param1RandomSource, boundingBox, param1Direction);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {
/*  373 */       super.addAdditionalSaveData(param1StructurePieceSerializationContext, param1CompoundTag);
/*      */       
/*  375 */       param1CompoundTag.putInt("Seed", this.selfSeed);
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/*  380 */       RandomSource randomSource = RandomSource.create(this.selfSeed);
/*      */       
/*      */       int i;
/*  383 */       for (i = 0; i <= 4; i++) {
/*  384 */         for (byte b = 3; b <= 4; b++) {
/*  385 */           int j = randomSource.nextInt(8);
/*  386 */           generateBox(param1WorldGenLevel, param1BoundingBox, i, b, 0, i, b, j, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  392 */       i = randomSource.nextInt(8);
/*  393 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 5, 0, 0, 5, i, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  396 */       i = randomSource.nextInt(8);
/*  397 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 5, 0, 4, 5, i, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */ 
/*      */       
/*  401 */       for (i = 0; i <= 4; i++) {
/*  402 */         int j = randomSource.nextInt(5);
/*  403 */         generateBox(param1WorldGenLevel, param1BoundingBox, i, 2, 0, i, 2, j, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       } 
/*  405 */       for (i = 0; i <= 4; i++) {
/*  406 */         for (byte b = 0; b <= 1; b++) {
/*  407 */           int j = randomSource.nextInt(3);
/*  408 */           generateBox(param1WorldGenLevel, param1BoundingBox, i, b, 0, i, b, j, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class BridgeCrossing extends NetherBridgePiece {
/*      */     private static final int WIDTH = 19;
/*      */     private static final int HEIGHT = 10;
/*      */     private static final int DEPTH = 19;
/*      */     
/*      */     public BridgeCrossing(int param1Int, BoundingBox param1BoundingBox, Direction param1Direction) {
/*  420 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, param1Int, param1BoundingBox);
/*      */       
/*  422 */       setOrientation(param1Direction);
/*      */     }
/*      */     
/*      */     protected BridgeCrossing(int param1Int1, int param1Int2, Direction param1Direction) {
/*  426 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, 0, StructurePiece.makeBoundingBox(param1Int1, 64, param1Int2, param1Direction, 19, 10, 19));
/*      */       
/*  428 */       setOrientation(param1Direction);
/*      */     }
/*      */     
/*      */     protected BridgeCrossing(StructurePieceType param1StructurePieceType, CompoundTag param1CompoundTag) {
/*  432 */       super(param1StructurePieceType, param1CompoundTag);
/*      */     }
/*      */     
/*      */     public BridgeCrossing(CompoundTag param1CompoundTag) {
/*  436 */       this(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece param1StructurePiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource) {
/*  441 */       generateChildForward((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 8, 3, false);
/*  442 */       generateChildLeft((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 3, 8, false);
/*  443 */       generateChildRight((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 3, 8, false);
/*      */     }
/*      */     
/*      */     public static BridgeCrossing createPiece(StructurePieceAccessor param1StructurePieceAccessor, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction, int param1Int4) {
/*  447 */       BoundingBox boundingBox = BoundingBox.orientBox(param1Int1, param1Int2, param1Int3, -8, -3, 0, 19, 10, 19, param1Direction);
/*      */       
/*  449 */       if (!isOkBox(boundingBox) || param1StructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/*  450 */         return null;
/*      */       }
/*      */       
/*  453 */       return new BridgeCrossing(param1Int4, boundingBox, param1Direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/*  459 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 3, 0, 11, 4, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  460 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 7, 18, 4, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  462 */       generateBox(param1WorldGenLevel, param1BoundingBox, 8, 5, 0, 10, 7, 18, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  463 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 5, 8, 18, 7, 10, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */       
/*  465 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 5, 0, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  466 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 5, 11, 7, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  467 */       generateBox(param1WorldGenLevel, param1BoundingBox, 11, 5, 0, 11, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  468 */       generateBox(param1WorldGenLevel, param1BoundingBox, 11, 5, 11, 11, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  469 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 5, 7, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  470 */       generateBox(param1WorldGenLevel, param1BoundingBox, 11, 5, 7, 18, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  471 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 5, 11, 7, 5, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  472 */       generateBox(param1WorldGenLevel, param1BoundingBox, 11, 5, 11, 18, 5, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  475 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 2, 0, 11, 2, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  476 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 2, 13, 11, 2, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  477 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 0, 0, 11, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  478 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 0, 15, 11, 1, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false); byte b;
/*  479 */       for (b = 7; b <= 11; b++) {
/*  480 */         for (byte b1 = 0; b1 <= 2; b1++) {
/*  481 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, b1, param1BoundingBox);
/*  482 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, 18 - b1, param1BoundingBox);
/*      */         } 
/*      */       } 
/*      */       
/*  486 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 7, 5, 2, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  487 */       generateBox(param1WorldGenLevel, param1BoundingBox, 13, 2, 7, 18, 2, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  488 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 7, 3, 1, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  489 */       generateBox(param1WorldGenLevel, param1BoundingBox, 15, 0, 7, 18, 1, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  490 */       for (b = 0; b <= 2; b++) {
/*  491 */         for (byte b1 = 7; b1 <= 11; b1++) {
/*  492 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, b1, param1BoundingBox);
/*  493 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), 18 - b, -1, b1, param1BoundingBox);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class RoomCrossing extends NetherBridgePiece {
/*      */     private static final int WIDTH = 7;
/*      */     private static final int HEIGHT = 9;
/*      */     private static final int DEPTH = 7;
/*      */     
/*      */     public RoomCrossing(int param1Int, BoundingBox param1BoundingBox, Direction param1Direction) {
/*  505 */       super(StructurePieceType.NETHER_FORTRESS_ROOM_CROSSING, param1Int, param1BoundingBox);
/*      */       
/*  507 */       setOrientation(param1Direction);
/*      */     }
/*      */     
/*      */     public RoomCrossing(CompoundTag param1CompoundTag) {
/*  511 */       super(StructurePieceType.NETHER_FORTRESS_ROOM_CROSSING, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece param1StructurePiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource) {
/*  516 */       generateChildForward((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 2, 0, false);
/*  517 */       generateChildLeft((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 0, 2, false);
/*  518 */       generateChildRight((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 0, 2, false);
/*      */     }
/*      */     
/*      */     public static RoomCrossing createPiece(StructurePieceAccessor param1StructurePieceAccessor, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction, int param1Int4) {
/*  522 */       BoundingBox boundingBox = BoundingBox.orientBox(param1Int1, param1Int2, param1Int3, -2, 0, 0, 7, 9, 7, param1Direction);
/*      */       
/*  524 */       if (!isOkBox(boundingBox) || param1StructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/*  525 */         return null;
/*      */       }
/*      */       
/*  528 */       return new RoomCrossing(param1Int4, boundingBox, param1Direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/*  534 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  536 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 6, 7, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  539 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 1, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  540 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 6, 1, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  541 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 2, 0, 6, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  542 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 2, 6, 6, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  543 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 0, 6, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  544 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 5, 0, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  545 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 2, 0, 6, 6, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  546 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 2, 5, 6, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  549 */       BlockState blockState1 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  550 */       BlockState blockState2 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */       
/*  552 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 6, 0, 4, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  553 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 5, 0, 4, 5, 0, blockState1, blockState1, false);
/*  554 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 6, 6, 4, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  555 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 5, 6, 4, 5, 6, blockState1, blockState1, false);
/*  556 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 6, 2, 0, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  557 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 5, 2, 0, 5, 4, blockState2, blockState2, false);
/*  558 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 6, 2, 6, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  559 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 5, 2, 6, 5, 4, blockState2, blockState2, false);
/*      */ 
/*      */       
/*  562 */       for (byte b = 0; b <= 6; b++) {
/*  563 */         for (byte b1 = 0; b1 <= 6; b1++)
/*  564 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, b1, param1BoundingBox); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class StairsRoom
/*      */     extends NetherBridgePiece {
/*      */     private static final int WIDTH = 7;
/*      */     private static final int HEIGHT = 11;
/*      */     private static final int DEPTH = 7;
/*      */     
/*      */     public StairsRoom(int param1Int, BoundingBox param1BoundingBox, Direction param1Direction) {
/*  576 */       super(StructurePieceType.NETHER_FORTRESS_STAIRS_ROOM, param1Int, param1BoundingBox);
/*      */       
/*  578 */       setOrientation(param1Direction);
/*      */     }
/*      */     
/*      */     public StairsRoom(CompoundTag param1CompoundTag) {
/*  582 */       super(StructurePieceType.NETHER_FORTRESS_STAIRS_ROOM, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece param1StructurePiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource) {
/*  587 */       generateChildRight((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 6, 2, false);
/*      */     }
/*      */     
/*      */     public static StairsRoom createPiece(StructurePieceAccessor param1StructurePieceAccessor, int param1Int1, int param1Int2, int param1Int3, int param1Int4, Direction param1Direction) {
/*  591 */       BoundingBox boundingBox = BoundingBox.orientBox(param1Int1, param1Int2, param1Int3, -2, 0, 0, 7, 11, 7, param1Direction);
/*      */       
/*  593 */       if (!isOkBox(boundingBox) || param1StructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/*  594 */         return null;
/*      */       }
/*      */       
/*  597 */       return new StairsRoom(param1Int4, boundingBox, param1Direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/*  603 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  605 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 6, 10, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  608 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 1, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  609 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 2, 0, 6, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  610 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 1, 0, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  611 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 2, 1, 6, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  612 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 6, 5, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  615 */       BlockState blockState1 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  616 */       BlockState blockState2 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */       
/*  618 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 2, 0, 5, 4, blockState2, blockState2, false);
/*  619 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 3, 2, 6, 5, 2, blockState2, blockState2, false);
/*  620 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 3, 4, 6, 5, 4, blockState2, blockState2, false);
/*      */ 
/*      */       
/*  623 */       placeBlock(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), 5, 2, 5, param1BoundingBox);
/*  624 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 2, 5, 4, 3, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  625 */       generateBox(param1WorldGenLevel, param1BoundingBox, 3, 2, 5, 3, 4, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  626 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 2, 5, 2, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  627 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 5, 1, 6, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  630 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 7, 1, 5, 7, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  631 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 8, 2, 6, 8, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  634 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 6, 0, 4, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  635 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 5, 0, 4, 5, 0, blockState1, blockState1, false);
/*      */       
/*  637 */       for (byte b = 0; b <= 6; b++) {
/*  638 */         for (byte b1 = 0; b1 <= 6; b1++)
/*  639 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, b1, param1BoundingBox); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class MonsterThrone
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     private static final int WIDTH = 7;
/*      */     private static final int HEIGHT = 8;
/*      */     private static final int DEPTH = 9;
/*      */     private boolean hasPlacedSpawner;
/*      */     
/*      */     public MonsterThrone(int param1Int, BoundingBox param1BoundingBox, Direction param1Direction) {
/*  653 */       super(StructurePieceType.NETHER_FORTRESS_MONSTER_THRONE, param1Int, param1BoundingBox);
/*      */       
/*  655 */       setOrientation(param1Direction);
/*      */     }
/*      */     
/*      */     public MonsterThrone(CompoundTag param1CompoundTag) {
/*  659 */       super(StructurePieceType.NETHER_FORTRESS_MONSTER_THRONE, param1CompoundTag);
/*  660 */       this.hasPlacedSpawner = param1CompoundTag.getBooleanOr("Mob", false);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {
/*  665 */       super.addAdditionalSaveData(param1StructurePieceSerializationContext, param1CompoundTag);
/*      */       
/*  667 */       param1CompoundTag.putBoolean("Mob", this.hasPlacedSpawner);
/*      */     }
/*      */     
/*      */     public static MonsterThrone createPiece(StructurePieceAccessor param1StructurePieceAccessor, int param1Int1, int param1Int2, int param1Int3, int param1Int4, Direction param1Direction) {
/*  671 */       BoundingBox boundingBox = BoundingBox.orientBox(param1Int1, param1Int2, param1Int3, -2, 0, 0, 7, 8, 9, param1Direction);
/*      */       
/*  673 */       if (!isOkBox(boundingBox) || param1StructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/*  674 */         return null;
/*      */       }
/*      */       
/*  677 */       return new MonsterThrone(param1Int4, boundingBox, param1Direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/*  683 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 6, 7, 7, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  686 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 0, 0, 5, 1, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  687 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 1, 5, 2, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  688 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 2, 5, 3, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  689 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 4, 3, 5, 4, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  692 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 0, 1, 4, 2, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  693 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 2, 0, 5, 4, 2, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  694 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 5, 2, 1, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  695 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 5, 2, 5, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  696 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 5, 3, 0, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  697 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 5, 3, 6, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  698 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 5, 8, 5, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  700 */       BlockState blockState1 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  701 */       BlockState blockState2 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */       
/*  703 */       placeBlock(param1WorldGenLevel, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 1, 6, 3, param1BoundingBox);
/*  704 */       placeBlock(param1WorldGenLevel, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 5, 6, 3, param1BoundingBox);
/*      */       
/*  706 */       placeBlock(param1WorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true))).setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true)), 0, 6, 3, param1BoundingBox);
/*  707 */       placeBlock(param1WorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true)), 6, 6, 3, param1BoundingBox);
/*      */       
/*  709 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 6, 4, 0, 6, 7, blockState2, blockState2, false);
/*  710 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 6, 4, 6, 6, 7, blockState2, blockState2, false);
/*      */       
/*  712 */       placeBlock(param1WorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 0, 6, 8, param1BoundingBox);
/*  713 */       placeBlock(param1WorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 6, 6, 8, param1BoundingBox);
/*      */       
/*  715 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 6, 8, 5, 6, 8, blockState1, blockState1, false);
/*      */       
/*  717 */       placeBlock(param1WorldGenLevel, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 1, 7, 8, param1BoundingBox);
/*  718 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 7, 8, 4, 7, 8, blockState1, blockState1, false);
/*  719 */       placeBlock(param1WorldGenLevel, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 5, 7, 8, param1BoundingBox);
/*      */       
/*  721 */       placeBlock(param1WorldGenLevel, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 2, 8, 8, param1BoundingBox);
/*  722 */       placeBlock(param1WorldGenLevel, blockState1, 3, 8, 8, param1BoundingBox);
/*  723 */       placeBlock(param1WorldGenLevel, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 4, 8, 8, param1BoundingBox);
/*      */       
/*  725 */       if (!this.hasPlacedSpawner) {
/*  726 */         BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(3, 5, 5);
/*  727 */         if (param1BoundingBox.isInside((Vec3i)mutableBlockPos)) {
/*  728 */           this.hasPlacedSpawner = true;
/*  729 */           param1WorldGenLevel.setBlock((BlockPos)mutableBlockPos, Blocks.SPAWNER.defaultBlockState(), 2);
/*      */           
/*  731 */           BlockEntity blockEntity = param1WorldGenLevel.getBlockEntity((BlockPos)mutableBlockPos);
/*  732 */           if (blockEntity instanceof SpawnerBlockEntity) { SpawnerBlockEntity spawnerBlockEntity = (SpawnerBlockEntity)blockEntity;
/*  733 */             spawnerBlockEntity.setEntityId(EntityType.BLAZE, param1RandomSource); }
/*      */         
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  739 */       for (byte b = 0; b <= 6; b++) {
/*  740 */         for (byte b1 = 0; b1 <= 6; b1++)
/*  741 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, b1, param1BoundingBox); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleEntrance
/*      */     extends NetherBridgePiece {
/*      */     private static final int WIDTH = 13;
/*      */     private static final int HEIGHT = 14;
/*      */     private static final int DEPTH = 13;
/*      */     
/*      */     public CastleEntrance(int param1Int, RandomSource param1RandomSource, BoundingBox param1BoundingBox, Direction param1Direction) {
/*  753 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_ENTRANCE, param1Int, param1BoundingBox);
/*      */       
/*  755 */       setOrientation(param1Direction);
/*      */     }
/*      */     
/*      */     public CastleEntrance(CompoundTag param1CompoundTag) {
/*  759 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_ENTRANCE, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece param1StructurePiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource) {
/*  764 */       generateChildForward((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 5, 3, true);
/*      */     }
/*      */     
/*      */     public static CastleEntrance createPiece(StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction, int param1Int4) {
/*  768 */       BoundingBox boundingBox = BoundingBox.orientBox(param1Int1, param1Int2, param1Int3, -5, -3, 0, 13, 14, 13, param1Direction);
/*      */       
/*  770 */       if (!isOkBox(boundingBox) || param1StructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/*  771 */         return null;
/*      */       }
/*      */       
/*  774 */       return new CastleEntrance(param1Int4, param1RandomSource, boundingBox, param1Direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/*  780 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  782 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 5, 0, 12, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  785 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  786 */       generateBox(param1WorldGenLevel, param1BoundingBox, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  787 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  788 */       generateBox(param1WorldGenLevel, param1BoundingBox, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  789 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  790 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  791 */       generateBox(param1WorldGenLevel, param1BoundingBox, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  792 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  795 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  798 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 8, 0, 7, 8, 0, Blocks.NETHER_BRICK_FENCE.defaultBlockState(), Blocks.NETHER_BRICK_FENCE.defaultBlockState(), false);
/*      */       
/*  800 */       BlockState blockState1 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  801 */       BlockState blockState2 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */       
/*      */       byte b;
/*  804 */       for (b = 1; b <= 11; b += 2) {
/*  805 */         generateBox(param1WorldGenLevel, param1BoundingBox, b, 10, 0, b, 11, 0, blockState1, blockState1, false);
/*  806 */         generateBox(param1WorldGenLevel, param1BoundingBox, b, 10, 12, b, 11, 12, blockState1, blockState1, false);
/*  807 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 10, b, 0, 11, b, blockState2, blockState2, false);
/*  808 */         generateBox(param1WorldGenLevel, param1BoundingBox, 12, 10, b, 12, 11, b, blockState2, blockState2, false);
/*  809 */         placeBlock(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, 13, 0, param1BoundingBox);
/*  810 */         placeBlock(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, 13, 12, param1BoundingBox);
/*  811 */         placeBlock(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), 0, 13, b, param1BoundingBox);
/*  812 */         placeBlock(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), 12, 13, b, param1BoundingBox);
/*  813 */         if (b != 11) {
/*  814 */           placeBlock(param1WorldGenLevel, blockState1, b + 1, 13, 0, param1BoundingBox);
/*  815 */           placeBlock(param1WorldGenLevel, blockState1, b + 1, 13, 12, param1BoundingBox);
/*  816 */           placeBlock(param1WorldGenLevel, blockState2, 0, 13, b + 1, param1BoundingBox);
/*  817 */           placeBlock(param1WorldGenLevel, blockState2, 12, 13, b + 1, param1BoundingBox);
/*      */         } 
/*      */       } 
/*  820 */       placeBlock(param1WorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 0, param1BoundingBox);
/*  821 */       placeBlock(param1WorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 12, param1BoundingBox);
/*  822 */       placeBlock(param1WorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 12, param1BoundingBox);
/*  823 */       placeBlock(param1WorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 0, param1BoundingBox);
/*      */ 
/*      */       
/*  826 */       for (b = 3; b <= 9; b += 2) {
/*  827 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, 7, b, 1, 8, b, (BlockState)blockState2.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), (BlockState)blockState2.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), false);
/*  828 */         generateBox(param1WorldGenLevel, param1BoundingBox, 11, 7, b, 11, 8, b, (BlockState)blockState2.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), (BlockState)blockState2.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), false);
/*      */       } 
/*      */ 
/*      */       
/*  832 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  833 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  835 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  836 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  837 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  838 */       generateBox(param1WorldGenLevel, param1BoundingBox, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  840 */       for (b = 4; b <= 8; b++) {
/*  841 */         for (byte b1 = 0; b1 <= 2; b1++) {
/*  842 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, b1, param1BoundingBox);
/*  843 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, 12 - b1, param1BoundingBox);
/*      */         } 
/*      */       } 
/*  846 */       for (b = 0; b <= 2; b++) {
/*  847 */         for (byte b1 = 4; b1 <= 8; b1++) {
/*  848 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, b1, param1BoundingBox);
/*  849 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), 12 - b, -1, b1, param1BoundingBox);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  854 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 5, 5, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  855 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 1, 6, 6, 4, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  856 */       placeBlock(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), 6, 0, 6, param1BoundingBox);
/*  857 */       placeBlock(param1WorldGenLevel, Blocks.LAVA.defaultBlockState(), 6, 5, 6, param1BoundingBox);
/*      */       
/*  859 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(6, 5, 6);
/*  860 */       if (param1BoundingBox.isInside((Vec3i)mutableBlockPos))
/*  861 */         param1WorldGenLevel.scheduleTick((BlockPos)mutableBlockPos, (Fluid)Fluids.LAVA, 0); 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleStalkRoom
/*      */     extends NetherBridgePiece {
/*      */     private static final int WIDTH = 13;
/*      */     private static final int HEIGHT = 14;
/*      */     private static final int DEPTH = 13;
/*      */     
/*      */     public CastleStalkRoom(int param1Int, BoundingBox param1BoundingBox, Direction param1Direction) {
/*  872 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, param1Int, param1BoundingBox);
/*      */       
/*  874 */       setOrientation(param1Direction);
/*      */     }
/*      */     
/*      */     public CastleStalkRoom(CompoundTag param1CompoundTag) {
/*  878 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece param1StructurePiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource) {
/*  883 */       generateChildForward((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 5, 3, true);
/*  884 */       generateChildForward((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 5, 11, true);
/*      */     }
/*      */     
/*      */     public static CastleStalkRoom createPiece(StructurePieceAccessor param1StructurePieceAccessor, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction, int param1Int4) {
/*  888 */       BoundingBox boundingBox = BoundingBox.orientBox(param1Int1, param1Int2, param1Int3, -5, -3, 0, 13, 14, 13, param1Direction);
/*      */       
/*  890 */       if (!isOkBox(boundingBox) || param1StructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/*  891 */         return null;
/*      */       }
/*      */       
/*  894 */       return new CastleStalkRoom(param1Int4, boundingBox, param1Direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/*  900 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  902 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 5, 0, 12, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  905 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  906 */       generateBox(param1WorldGenLevel, param1BoundingBox, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  907 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  908 */       generateBox(param1WorldGenLevel, param1BoundingBox, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  909 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  910 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  911 */       generateBox(param1WorldGenLevel, param1BoundingBox, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  912 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  915 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  917 */       BlockState blockState1 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  918 */       BlockState blockState2 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*  919 */       BlockState blockState3 = (BlockState)blockState2.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true));
/*  920 */       BlockState blockState4 = (BlockState)blockState2.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*      */       
/*      */       byte b1;
/*  923 */       for (b1 = 1; b1 <= 11; b1 += 2) {
/*  924 */         generateBox(param1WorldGenLevel, param1BoundingBox, b1, 10, 0, b1, 11, 0, blockState1, blockState1, false);
/*  925 */         generateBox(param1WorldGenLevel, param1BoundingBox, b1, 10, 12, b1, 11, 12, blockState1, blockState1, false);
/*  926 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 10, b1, 0, 11, b1, blockState2, blockState2, false);
/*  927 */         generateBox(param1WorldGenLevel, param1BoundingBox, 12, 10, b1, 12, 11, b1, blockState2, blockState2, false);
/*  928 */         placeBlock(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b1, 13, 0, param1BoundingBox);
/*  929 */         placeBlock(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b1, 13, 12, param1BoundingBox);
/*  930 */         placeBlock(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), 0, 13, b1, param1BoundingBox);
/*  931 */         placeBlock(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), 12, 13, b1, param1BoundingBox);
/*  932 */         if (b1 != 11) {
/*  933 */           placeBlock(param1WorldGenLevel, blockState1, b1 + 1, 13, 0, param1BoundingBox);
/*  934 */           placeBlock(param1WorldGenLevel, blockState1, b1 + 1, 13, 12, param1BoundingBox);
/*  935 */           placeBlock(param1WorldGenLevel, blockState2, 0, 13, b1 + 1, param1BoundingBox);
/*  936 */           placeBlock(param1WorldGenLevel, blockState2, 12, 13, b1 + 1, param1BoundingBox);
/*      */         } 
/*      */       } 
/*  939 */       placeBlock(param1WorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 0, param1BoundingBox);
/*  940 */       placeBlock(param1WorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 12, param1BoundingBox);
/*  941 */       placeBlock(param1WorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 12, param1BoundingBox);
/*  942 */       placeBlock(param1WorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 0, param1BoundingBox);
/*      */ 
/*      */       
/*  945 */       for (b1 = 3; b1 <= 9; b1 += 2) {
/*  946 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, 7, b1, 1, 8, b1, blockState3, blockState3, false);
/*  947 */         generateBox(param1WorldGenLevel, param1BoundingBox, 11, 7, b1, 11, 8, b1, blockState4, blockState4, false);
/*      */       } 
/*      */ 
/*      */       
/*  951 */       BlockState blockState5 = (BlockState)Blocks.NETHER_BRICK_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.NORTH); byte b2;
/*  952 */       for (b2 = 0; b2 <= 6; b2++) {
/*  953 */         int i = b2 + 4;
/*  954 */         for (byte b = 5; b <= 7; b++) {
/*  955 */           placeBlock(param1WorldGenLevel, blockState5, b, 5 + b2, i, param1BoundingBox);
/*      */         }
/*  957 */         if (i >= 5 && i <= 8) {
/*  958 */           generateBox(param1WorldGenLevel, param1BoundingBox, 5, 5, i, 7, b2 + 4, i, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  959 */         } else if (i >= 9 && i <= 10) {
/*  960 */           generateBox(param1WorldGenLevel, param1BoundingBox, 5, 8, i, 7, b2 + 4, i, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         } 
/*  962 */         if (b2 >= 1) {
/*  963 */           generateBox(param1WorldGenLevel, param1BoundingBox, 5, 6 + b2, i, 7, 9 + b2, i, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */         }
/*      */       } 
/*  966 */       for (b2 = 5; b2 <= 7; b2++) {
/*  967 */         placeBlock(param1WorldGenLevel, blockState5, b2, 12, 11, param1BoundingBox);
/*      */       }
/*  969 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 6, 7, 5, 7, 7, blockState4, blockState4, false);
/*  970 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 6, 7, 7, 7, 7, blockState3, blockState3, false);
/*  971 */       generateBox(param1WorldGenLevel, param1BoundingBox, 5, 13, 12, 7, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  974 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 5, 2, 3, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  975 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 5, 9, 3, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  976 */       generateBox(param1WorldGenLevel, param1BoundingBox, 2, 5, 4, 2, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  977 */       generateBox(param1WorldGenLevel, param1BoundingBox, 9, 5, 2, 10, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  978 */       generateBox(param1WorldGenLevel, param1BoundingBox, 9, 5, 9, 10, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  979 */       generateBox(param1WorldGenLevel, param1BoundingBox, 10, 5, 4, 10, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  980 */       BlockState blockState6 = (BlockState)blockState5.setValue((Property)StairBlock.FACING, (Comparable)Direction.EAST);
/*  981 */       BlockState blockState7 = (BlockState)blockState5.setValue((Property)StairBlock.FACING, (Comparable)Direction.WEST);
/*  982 */       placeBlock(param1WorldGenLevel, blockState7, 4, 5, 2, param1BoundingBox);
/*  983 */       placeBlock(param1WorldGenLevel, blockState7, 4, 5, 3, param1BoundingBox);
/*  984 */       placeBlock(param1WorldGenLevel, blockState7, 4, 5, 9, param1BoundingBox);
/*  985 */       placeBlock(param1WorldGenLevel, blockState7, 4, 5, 10, param1BoundingBox);
/*  986 */       placeBlock(param1WorldGenLevel, blockState6, 8, 5, 2, param1BoundingBox);
/*  987 */       placeBlock(param1WorldGenLevel, blockState6, 8, 5, 3, param1BoundingBox);
/*  988 */       placeBlock(param1WorldGenLevel, blockState6, 8, 5, 9, param1BoundingBox);
/*  989 */       placeBlock(param1WorldGenLevel, blockState6, 8, 5, 10, param1BoundingBox);
/*      */ 
/*      */       
/*  992 */       generateBox(param1WorldGenLevel, param1BoundingBox, 3, 4, 4, 4, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
/*  993 */       generateBox(param1WorldGenLevel, param1BoundingBox, 8, 4, 4, 9, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
/*  994 */       generateBox(param1WorldGenLevel, param1BoundingBox, 3, 5, 4, 4, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
/*  995 */       generateBox(param1WorldGenLevel, param1BoundingBox, 8, 5, 4, 9, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
/*      */ 
/*      */       
/*  998 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  999 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1001 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1002 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1003 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1004 */       generateBox(param1WorldGenLevel, param1BoundingBox, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       byte b3;
/* 1006 */       for (b3 = 4; b3 <= 8; b3++) {
/* 1007 */         for (byte b = 0; b <= 2; b++) {
/* 1008 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b3, -1, b, param1BoundingBox);
/* 1009 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b3, -1, 12 - b, param1BoundingBox);
/*      */         } 
/*      */       } 
/* 1012 */       for (b3 = 0; b3 <= 2; b3++) {
/* 1013 */         for (byte b = 4; b <= 8; b++) {
/* 1014 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b3, -1, b, param1BoundingBox);
/* 1015 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), 12 - b3, -1, b, param1BoundingBox);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleSmallCorridorPiece extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 7;
/*      */     private static final int DEPTH = 5;
/*      */     
/*      */     public CastleSmallCorridorPiece(int param1Int, BoundingBox param1BoundingBox, Direction param1Direction) {
/* 1027 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR, param1Int, param1BoundingBox);
/*      */       
/* 1029 */       setOrientation(param1Direction);
/*      */     }
/*      */     
/*      */     public CastleSmallCorridorPiece(CompoundTag param1CompoundTag) {
/* 1033 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece param1StructurePiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource) {
/* 1038 */       generateChildForward((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 1, 0, true);
/*      */     }
/*      */     
/*      */     public static CastleSmallCorridorPiece createPiece(StructurePieceAccessor param1StructurePieceAccessor, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction, int param1Int4) {
/* 1042 */       BoundingBox boundingBox = BoundingBox.orientBox(param1Int1, param1Int2, param1Int3, -1, 0, 0, 5, 7, 5, param1Direction);
/*      */       
/* 1044 */       if (!isOkBox(boundingBox) || param1StructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/* 1045 */         return null;
/*      */       }
/*      */       
/* 1048 */       return new CastleSmallCorridorPiece(param1Int4, boundingBox, param1Direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 1054 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1056 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */       
/* 1058 */       BlockState blockState = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */ 
/*      */       
/* 1061 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1062 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1063 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 1, 0, 4, 1, blockState, blockState, false);
/* 1064 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 3, 0, 4, 3, blockState, blockState, false);
/* 1065 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 3, 1, 4, 4, 1, blockState, blockState, false);
/* 1066 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 3, 3, 4, 4, 3, blockState, blockState, false);
/*      */ 
/*      */       
/* 1069 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1072 */       for (byte b = 0; b <= 4; b++) {
/* 1073 */         for (byte b1 = 0; b1 <= 4; b1++)
/* 1074 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, b1, param1BoundingBox); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleSmallCorridorCrossingPiece
/*      */     extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 7;
/*      */     private static final int DEPTH = 5;
/*      */     
/*      */     public CastleSmallCorridorCrossingPiece(int param1Int, BoundingBox param1BoundingBox, Direction param1Direction) {
/* 1086 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_CROSSING, param1Int, param1BoundingBox);
/*      */       
/* 1088 */       setOrientation(param1Direction);
/*      */     }
/*      */     
/*      */     public CastleSmallCorridorCrossingPiece(CompoundTag param1CompoundTag) {
/* 1092 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_CROSSING, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece param1StructurePiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource) {
/* 1097 */       generateChildForward((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 1, 0, true);
/* 1098 */       generateChildLeft((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 0, 1, true);
/* 1099 */       generateChildRight((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 0, 1, true);
/*      */     }
/*      */     
/*      */     public static CastleSmallCorridorCrossingPiece createPiece(StructurePieceAccessor param1StructurePieceAccessor, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction, int param1Int4) {
/* 1103 */       BoundingBox boundingBox = BoundingBox.orientBox(param1Int1, param1Int2, param1Int3, -1, 0, 0, 5, 7, 5, param1Direction);
/*      */       
/* 1105 */       if (!isOkBox(boundingBox) || param1StructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/* 1106 */         return null;
/*      */       }
/*      */       
/* 1109 */       return new CastleSmallCorridorCrossingPiece(param1Int4, boundingBox, param1Direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 1115 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1117 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1120 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1121 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1122 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 4, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1123 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 2, 4, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1126 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1129 */       for (byte b = 0; b <= 4; b++) {
/* 1130 */         for (byte b1 = 0; b1 <= 4; b1++)
/* 1131 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, b1, param1BoundingBox); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleSmallCorridorRightTurnPiece
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 7;
/*      */     private static final int DEPTH = 5;
/*      */     private boolean isNeedingChest;
/*      */     
/*      */     public CastleSmallCorridorRightTurnPiece(int param1Int, RandomSource param1RandomSource, BoundingBox param1BoundingBox, Direction param1Direction) {
/* 1145 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_RIGHT_TURN, param1Int, param1BoundingBox);
/*      */       
/* 1147 */       setOrientation(param1Direction);
/*      */       
/* 1149 */       this.isNeedingChest = (param1RandomSource.nextInt(3) == 0);
/*      */     }
/*      */     
/*      */     public CastleSmallCorridorRightTurnPiece(CompoundTag param1CompoundTag) {
/* 1153 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_RIGHT_TURN, param1CompoundTag);
/* 1154 */       this.isNeedingChest = param1CompoundTag.getBooleanOr("Chest", false);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {
/* 1159 */       super.addAdditionalSaveData(param1StructurePieceSerializationContext, param1CompoundTag);
/*      */       
/* 1161 */       param1CompoundTag.putBoolean("Chest", this.isNeedingChest);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece param1StructurePiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource) {
/* 1166 */       generateChildRight((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 0, 1, true);
/*      */     }
/*      */     
/*      */     public static CastleSmallCorridorRightTurnPiece createPiece(StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction, int param1Int4) {
/* 1170 */       BoundingBox boundingBox = BoundingBox.orientBox(param1Int1, param1Int2, param1Int3, -1, 0, 0, 5, 7, 5, param1Direction);
/*      */       
/* 1172 */       if (!isOkBox(boundingBox) || param1StructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/* 1173 */         return null;
/*      */       }
/*      */       
/* 1176 */       return new CastleSmallCorridorRightTurnPiece(param1Int4, param1RandomSource, boundingBox, param1Direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 1182 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1184 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */       
/* 1186 */       BlockState blockState1 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1187 */       BlockState blockState2 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */ 
/*      */       
/* 1190 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1191 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 1, 0, 4, 1, blockState2, blockState2, false);
/* 1192 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 3, 0, 4, 3, blockState2, blockState2, false);
/*      */       
/* 1194 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1196 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 2, 4, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1197 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 4, 1, 4, 4, blockState1, blockState1, false);
/* 1198 */       generateBox(param1WorldGenLevel, param1BoundingBox, 3, 3, 4, 3, 4, 4, blockState1, blockState1, false);
/*      */       
/* 1200 */       if (this.isNeedingChest && 
/* 1201 */         param1BoundingBox.isInside((Vec3i)getWorldPos(1, 2, 3))) {
/* 1202 */         this.isNeedingChest = false;
/* 1203 */         createChest(param1WorldGenLevel, param1BoundingBox, param1RandomSource, 1, 2, 3, BuiltInLootTables.NETHER_BRIDGE);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1208 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1211 */       for (byte b = 0; b <= 4; b++) {
/* 1212 */         for (byte b1 = 0; b1 <= 4; b1++)
/* 1213 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, b1, param1BoundingBox); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleSmallCorridorLeftTurnPiece
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 7;
/*      */     private static final int DEPTH = 5;
/*      */     private boolean isNeedingChest;
/*      */     
/*      */     public CastleSmallCorridorLeftTurnPiece(int param1Int, RandomSource param1RandomSource, BoundingBox param1BoundingBox, Direction param1Direction) {
/* 1227 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_LEFT_TURN, param1Int, param1BoundingBox);
/*      */       
/* 1229 */       setOrientation(param1Direction);
/*      */       
/* 1231 */       this.isNeedingChest = (param1RandomSource.nextInt(3) == 0);
/*      */     }
/*      */     
/*      */     public CastleSmallCorridorLeftTurnPiece(CompoundTag param1CompoundTag) {
/* 1235 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_LEFT_TURN, param1CompoundTag);
/* 1236 */       this.isNeedingChest = param1CompoundTag.getBooleanOr("Chest", false);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {
/* 1241 */       super.addAdditionalSaveData(param1StructurePieceSerializationContext, param1CompoundTag);
/*      */       
/* 1243 */       param1CompoundTag.putBoolean("Chest", this.isNeedingChest);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece param1StructurePiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource) {
/* 1248 */       generateChildLeft((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 0, 1, true);
/*      */     }
/*      */     
/*      */     public static CastleSmallCorridorLeftTurnPiece createPiece(StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction, int param1Int4) {
/* 1252 */       BoundingBox boundingBox = BoundingBox.orientBox(param1Int1, param1Int2, param1Int3, -1, 0, 0, 5, 7, 5, param1Direction);
/*      */       
/* 1254 */       if (!isOkBox(boundingBox) || param1StructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/* 1255 */         return null;
/*      */       }
/*      */       
/* 1258 */       return new CastleSmallCorridorLeftTurnPiece(param1Int4, param1RandomSource, boundingBox, param1Direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 1264 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1266 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */       
/* 1268 */       BlockState blockState1 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1269 */       BlockState blockState2 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */ 
/*      */       
/* 1272 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1273 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 3, 1, 4, 4, 1, blockState2, blockState2, false);
/* 1274 */       generateBox(param1WorldGenLevel, param1BoundingBox, 4, 3, 3, 4, 4, 3, blockState2, blockState2, false);
/*      */       
/* 1276 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1278 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 4, 3, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1279 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 4, 1, 4, 4, blockState1, blockState1, false);
/* 1280 */       generateBox(param1WorldGenLevel, param1BoundingBox, 3, 3, 4, 3, 4, 4, blockState1, blockState1, false);
/*      */       
/* 1282 */       if (this.isNeedingChest && 
/* 1283 */         param1BoundingBox.isInside((Vec3i)getWorldPos(3, 2, 3))) {
/* 1284 */         this.isNeedingChest = false;
/* 1285 */         createChest(param1WorldGenLevel, param1BoundingBox, param1RandomSource, 3, 2, 3, BuiltInLootTables.NETHER_BRIDGE);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1290 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1293 */       for (byte b = 0; b <= 4; b++) {
/* 1294 */         for (byte b1 = 0; b1 <= 4; b1++)
/* 1295 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, b1, param1BoundingBox); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleCorridorStairsPiece
/*      */     extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 14;
/*      */     private static final int DEPTH = 10;
/*      */     
/*      */     public CastleCorridorStairsPiece(int param1Int, BoundingBox param1BoundingBox, Direction param1Direction) {
/* 1307 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS, param1Int, param1BoundingBox);
/*      */       
/* 1309 */       setOrientation(param1Direction);
/*      */     }
/*      */     
/*      */     public CastleCorridorStairsPiece(CompoundTag param1CompoundTag) {
/* 1313 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece param1StructurePiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource) {
/* 1318 */       generateChildForward((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 1, 0, true);
/*      */     }
/*      */     
/*      */     public static CastleCorridorStairsPiece createPiece(StructurePieceAccessor param1StructurePieceAccessor, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction, int param1Int4) {
/* 1322 */       BoundingBox boundingBox = BoundingBox.orientBox(param1Int1, param1Int2, param1Int3, -1, -7, 0, 5, 14, 10, param1Direction);
/*      */       
/* 1324 */       if (!isOkBox(boundingBox) || param1StructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/* 1325 */         return null;
/*      */       }
/*      */       
/* 1328 */       return new CastleCorridorStairsPiece(param1Int4, boundingBox, param1Direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 1334 */       BlockState blockState1 = (BlockState)Blocks.NETHER_BRICK_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.SOUTH);
/* 1335 */       BlockState blockState2 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */       
/* 1337 */       for (byte b = 0; b <= 9; b++) {
/* 1338 */         int i = Math.max(1, 7 - b);
/* 1339 */         int j = Math.min(Math.max(i + 5, 14 - b), 13);
/* 1340 */         byte b1 = b;
/*      */ 
/*      */         
/* 1343 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, b1, 4, i, b1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         
/* 1345 */         generateBox(param1WorldGenLevel, param1BoundingBox, 1, i + 1, b1, 3, j - 1, b1, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1346 */         if (b <= 6) {
/* 1347 */           placeBlock(param1WorldGenLevel, blockState1, 1, i + 1, b1, param1BoundingBox);
/* 1348 */           placeBlock(param1WorldGenLevel, blockState1, 2, i + 1, b1, param1BoundingBox);
/* 1349 */           placeBlock(param1WorldGenLevel, blockState1, 3, i + 1, b1, param1BoundingBox);
/*      */         } 
/*      */         
/* 1352 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, j, b1, 4, j, b1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         
/* 1354 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, i + 1, b1, 0, j - 1, b1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1355 */         generateBox(param1WorldGenLevel, param1BoundingBox, 4, i + 1, b1, 4, j - 1, b1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1356 */         if ((b & 0x1) == 0) {
/* 1357 */           generateBox(param1WorldGenLevel, param1BoundingBox, 0, i + 2, b1, 0, i + 3, b1, blockState2, blockState2, false);
/* 1358 */           generateBox(param1WorldGenLevel, param1BoundingBox, 4, i + 2, b1, 4, i + 3, b1, blockState2, blockState2, false);
/*      */         } 
/*      */ 
/*      */         
/* 1362 */         for (byte b2 = 0; b2 <= 4; b2++)
/* 1363 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b2, -1, b1, param1BoundingBox); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleCorridorTBalconyPiece
/*      */     extends NetherBridgePiece {
/*      */     private static final int WIDTH = 9;
/*      */     private static final int HEIGHT = 7;
/*      */     private static final int DEPTH = 9;
/*      */     
/*      */     public CastleCorridorTBalconyPiece(int param1Int, BoundingBox param1BoundingBox, Direction param1Direction) {
/* 1375 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY, param1Int, param1BoundingBox);
/*      */       
/* 1377 */       setOrientation(param1Direction);
/*      */     }
/*      */     
/*      */     public CastleCorridorTBalconyPiece(CompoundTag param1CompoundTag) {
/* 1381 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY, param1CompoundTag);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece param1StructurePiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource) {
/* 1386 */       byte b = 1;
/*      */       
/* 1388 */       Direction direction = getOrientation();
/* 1389 */       if (direction == Direction.WEST || direction == Direction.NORTH) {
/* 1390 */         b = 5;
/*      */       }
/*      */       
/* 1393 */       generateChildLeft((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 0, b, (param1RandomSource.nextInt(8) > 0));
/* 1394 */       generateChildRight((NetherFortressPieces.StartPiece)param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, 0, b, (param1RandomSource.nextInt(8) > 0));
/*      */     }
/*      */     
/*      */     public static CastleCorridorTBalconyPiece createPiece(StructurePieceAccessor param1StructurePieceAccessor, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction, int param1Int4) {
/* 1398 */       BoundingBox boundingBox = BoundingBox.orientBox(param1Int1, param1Int2, param1Int3, -3, 0, 0, 9, 7, 9, param1Direction);
/*      */       
/* 1400 */       if (!isOkBox(boundingBox) || param1StructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/* 1401 */         return null;
/*      */       }
/*      */       
/* 1404 */       return new CastleCorridorTBalconyPiece(param1Int4, boundingBox, param1Direction);
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 1409 */       BlockState blockState1 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 1410 */       BlockState blockState2 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*      */ 
/*      */       
/* 1413 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 0, 8, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1415 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 8, 5, 8, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */       
/* 1417 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 6, 0, 8, 6, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1420 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 0, 2, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1421 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 2, 0, 8, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1422 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 0, 1, 4, 0, blockState2, blockState2, false);
/* 1423 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 3, 0, 7, 4, 0, blockState2, blockState2, false);
/*      */ 
/*      */       
/* 1426 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 2, 4, 8, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1427 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 1, 4, 2, 2, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1428 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 1, 4, 7, 2, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1431 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 8, 7, 3, 8, blockState2, blockState2, false);
/* 1432 */       placeBlock(param1WorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 0, 3, 8, param1BoundingBox);
/* 1433 */       placeBlock(param1WorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 8, 3, 8, param1BoundingBox);
/* 1434 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 6, 0, 3, 7, blockState1, blockState1, false);
/* 1435 */       generateBox(param1WorldGenLevel, param1BoundingBox, 8, 3, 6, 8, 3, 7, blockState1, blockState1, false);
/*      */ 
/*      */       
/* 1438 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 3, 4, 0, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1439 */       generateBox(param1WorldGenLevel, param1BoundingBox, 8, 3, 4, 8, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1440 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 3, 5, 2, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1441 */       generateBox(param1WorldGenLevel, param1BoundingBox, 6, 3, 5, 7, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1442 */       generateBox(param1WorldGenLevel, param1BoundingBox, 1, 4, 5, 1, 5, 5, blockState2, blockState2, false);
/* 1443 */       generateBox(param1WorldGenLevel, param1BoundingBox, 7, 4, 5, 7, 5, 5, blockState2, blockState2, false);
/*      */ 
/*      */       
/* 1446 */       for (byte b = 0; b <= 5; b++) {
/* 1447 */         for (byte b1 = 0; b1 <= 8; b1++)
/* 1448 */           fillColumnDown(param1WorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b1, -1, b, param1BoundingBox); 
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */