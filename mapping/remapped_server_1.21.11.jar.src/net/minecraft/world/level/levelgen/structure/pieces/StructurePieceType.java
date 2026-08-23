/*     */ package net.minecraft.world.level.levelgen.structure.pieces;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.OceanRuinPieces;
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
/*     */ public interface StructurePieceType
/*     */ {
/*  28 */   public static final StructurePieceType MINE_SHAFT_CORRIDOR = setPieceId(net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces.MineShaftCorridor::new, "MSCorridor");
/*  29 */   public static final StructurePieceType MINE_SHAFT_CROSSING = setPieceId(net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces.MineShaftCrossing::new, "MSCrossing");
/*  30 */   public static final StructurePieceType MINE_SHAFT_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces.MineShaftRoom::new, "MSRoom");
/*  31 */   public static final StructurePieceType MINE_SHAFT_STAIRS = setPieceId(net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces.MineShaftStairs::new, "MSStairs");
/*  32 */   public static final StructurePieceType NETHER_FORTRESS_BRIDGE_CROSSING = setPieceId(net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces.BridgeCrossing::new, "NeBCr");
/*  33 */   public static final StructurePieceType NETHER_FORTRESS_BRIDGE_END_FILLER = setPieceId(net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces.BridgeEndFiller::new, "NeBEF");
/*  34 */   public static final StructurePieceType NETHER_FORTRESS_BRIDGE_STRAIGHT = setPieceId(net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces.BridgeStraight::new, "NeBS");
/*  35 */   public static final StructurePieceType NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS = setPieceId(net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces.CastleCorridorStairsPiece::new, "NeCCS");
/*  36 */   public static final StructurePieceType NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY = setPieceId(net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces.CastleCorridorTBalconyPiece::new, "NeCTB");
/*  37 */   public static final StructurePieceType NETHER_FORTRESS_CASTLE_ENTRANCE = setPieceId(net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces.CastleEntrance::new, "NeCE");
/*  38 */   public static final StructurePieceType NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_CROSSING = setPieceId(net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces.CastleSmallCorridorCrossingPiece::new, "NeSCSC");
/*  39 */   public static final StructurePieceType NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_LEFT_TURN = setPieceId(net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces.CastleSmallCorridorLeftTurnPiece::new, "NeSCLT");
/*  40 */   public static final StructurePieceType NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR = setPieceId(net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces.CastleSmallCorridorPiece::new, "NeSC");
/*  41 */   public static final StructurePieceType NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_RIGHT_TURN = setPieceId(net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces.CastleSmallCorridorRightTurnPiece::new, "NeSCRT");
/*  42 */   public static final StructurePieceType NETHER_FORTRESS_CASTLE_STALK_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces.CastleStalkRoom::new, "NeCSR");
/*  43 */   public static final StructurePieceType NETHER_FORTRESS_MONSTER_THRONE = setPieceId(net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces.MonsterThrone::new, "NeMT");
/*  44 */   public static final StructurePieceType NETHER_FORTRESS_ROOM_CROSSING = setPieceId(net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces.RoomCrossing::new, "NeRC");
/*  45 */   public static final StructurePieceType NETHER_FORTRESS_STAIRS_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces.StairsRoom::new, "NeSR");
/*  46 */   public static final StructurePieceType NETHER_FORTRESS_START = setPieceId(net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces.StartPiece::new, "NeStart");
/*  47 */   public static final StructurePieceType STRONGHOLD_CHEST_CORRIDOR = setPieceId(net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces.ChestCorridor::new, "SHCC");
/*  48 */   public static final StructurePieceType STRONGHOLD_FILLER_CORRIDOR = setPieceId(net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces.FillerCorridor::new, "SHFC");
/*  49 */   public static final StructurePieceType STRONGHOLD_FIVE_CROSSING = setPieceId(net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces.FiveCrossing::new, "SH5C");
/*  50 */   public static final StructurePieceType STRONGHOLD_LEFT_TURN = setPieceId(net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces.LeftTurn::new, "SHLT");
/*  51 */   public static final StructurePieceType STRONGHOLD_LIBRARY = setPieceId(net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces.Library::new, "SHLi");
/*  52 */   public static final StructurePieceType STRONGHOLD_PORTAL_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces.PortalRoom::new, "SHPR");
/*  53 */   public static final StructurePieceType STRONGHOLD_PRISON_HALL = setPieceId(net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces.PrisonHall::new, "SHPH");
/*  54 */   public static final StructurePieceType STRONGHOLD_RIGHT_TURN = setPieceId(net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces.RightTurn::new, "SHRT");
/*  55 */   public static final StructurePieceType STRONGHOLD_ROOM_CROSSING = setPieceId(net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces.RoomCrossing::new, "SHRC");
/*  56 */   public static final StructurePieceType STRONGHOLD_STAIRS_DOWN = setPieceId(net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces.StairsDown::new, "SHSD");
/*  57 */   public static final StructurePieceType STRONGHOLD_START = setPieceId(net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces.StartPiece::new, "SHStart");
/*  58 */   public static final StructurePieceType STRONGHOLD_STRAIGHT = setPieceId(net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces.Straight::new, "SHS");
/*  59 */   public static final StructurePieceType STRONGHOLD_STRAIGHT_STAIRS_DOWN = setPieceId(net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces.StraightStairsDown::new, "SHSSD");
/*  60 */   public static final StructurePieceType JUNGLE_PYRAMID_PIECE = setPieceId(net.minecraft.world.level.levelgen.structure.structures.JungleTemplePiece::new, "TeJP");
/*  61 */   public static final StructurePieceType OCEAN_RUIN = setTemplatePieceId(OceanRuinPieces.OceanRuinPiece::create, "ORP");
/*  62 */   public static final StructurePieceType IGLOO = setTemplatePieceId(net.minecraft.world.level.levelgen.structure.structures.IglooPieces.IglooPiece::new, "Iglu");
/*  63 */   public static final StructurePieceType RUINED_PORTAL = setTemplatePieceId(net.minecraft.world.level.levelgen.structure.structures.RuinedPortalPiece::new, "RUPO");
/*  64 */   public static final StructurePieceType SWAMPLAND_HUT = setPieceId(net.minecraft.world.level.levelgen.structure.structures.SwampHutPiece::new, "TeSH");
/*  65 */   public static final StructurePieceType DESERT_PYRAMID_PIECE = setPieceId(net.minecraft.world.level.levelgen.structure.structures.DesertPyramidPiece::new, "TeDP");
/*  66 */   public static final StructurePieceType OCEAN_MONUMENT_BUILDING = setPieceId(net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.MonumentBuilding::new, "OMB");
/*  67 */   public static final StructurePieceType OCEAN_MONUMENT_CORE_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.OceanMonumentCoreRoom::new, "OMCR");
/*  68 */   public static final StructurePieceType OCEAN_MONUMENT_DOUBLE_X_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.OceanMonumentDoubleXRoom::new, "OMDXR");
/*  69 */   public static final StructurePieceType OCEAN_MONUMENT_DOUBLE_XY_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.OceanMonumentDoubleXYRoom::new, "OMDXYR");
/*  70 */   public static final StructurePieceType OCEAN_MONUMENT_DOUBLE_Y_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.OceanMonumentDoubleYRoom::new, "OMDYR");
/*  71 */   public static final StructurePieceType OCEAN_MONUMENT_DOUBLE_YZ_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.OceanMonumentDoubleYZRoom::new, "OMDYZR");
/*  72 */   public static final StructurePieceType OCEAN_MONUMENT_DOUBLE_Z_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.OceanMonumentDoubleZRoom::new, "OMDZR");
/*  73 */   public static final StructurePieceType OCEAN_MONUMENT_ENTRY_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.OceanMonumentEntryRoom::new, "OMEntry");
/*  74 */   public static final StructurePieceType OCEAN_MONUMENT_PENTHOUSE = setPieceId(net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.OceanMonumentPenthouse::new, "OMPenthouse");
/*  75 */   public static final StructurePieceType OCEAN_MONUMENT_SIMPLE_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.OceanMonumentSimpleRoom::new, "OMSimple");
/*  76 */   public static final StructurePieceType OCEAN_MONUMENT_SIMPLE_TOP_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.OceanMonumentSimpleTopRoom::new, "OMSimpleT");
/*  77 */   public static final StructurePieceType OCEAN_MONUMENT_WING_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.OceanMonumentWingRoom::new, "OMWR");
/*  78 */   public static final StructurePieceType END_CITY_PIECE = setTemplatePieceId(net.minecraft.world.level.levelgen.structure.structures.EndCityPieces.EndCityPiece::new, "ECP");
/*  79 */   public static final StructurePieceType WOODLAND_MANSION_PIECE = setTemplatePieceId(net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionPieces.WoodlandMansionPiece::new, "WMP");
/*  80 */   public static final StructurePieceType BURIED_TREASURE_PIECE = setPieceId(net.minecraft.world.level.levelgen.structure.structures.BuriedTreasurePieces.BuriedTreasurePiece::new, "BTP");
/*  81 */   public static final StructurePieceType SHIPWRECK_PIECE = setTemplatePieceId(net.minecraft.world.level.levelgen.structure.structures.ShipwreckPieces.ShipwreckPiece::new, "Shipwreck");
/*  82 */   public static final StructurePieceType NETHER_FOSSIL = setTemplatePieceId(net.minecraft.world.level.levelgen.structure.structures.NetherFossilPieces.NetherFossilPiece::new, "NeFos");
/*  83 */   public static final StructurePieceType JIGSAW = setFullContextPieceId(net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece::new, "jigsaw");
/*     */   
/*     */   StructurePiece load(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag);
/*     */   
/*     */   public static interface ContextlessType
/*     */     extends StructurePieceType {
/*     */     StructurePiece load(CompoundTag param1CompoundTag);
/*     */     
/*     */     default StructurePiece load(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {
/*  92 */       return load(param1CompoundTag);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface StructureTemplateType
/*     */     extends StructurePieceType {
/*     */     StructurePiece load(StructureTemplateManager param1StructureTemplateManager, CompoundTag param1CompoundTag);
/*     */     
/*     */     default StructurePiece load(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {
/* 101 */       return load(param1StructurePieceSerializationContext.structureTemplateManager(), param1CompoundTag);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static StructurePieceType setFullContextPieceId(StructurePieceType paramStructurePieceType, String paramString) {
/* 107 */     return (StructurePieceType)Registry.register(BuiltInRegistries.STRUCTURE_PIECE, paramString.toLowerCase(Locale.ROOT), paramStructurePieceType);
/*     */   }
/*     */   
/*     */   private static StructurePieceType setPieceId(ContextlessType paramContextlessType, String paramString) {
/* 111 */     return setFullContextPieceId(paramContextlessType, paramString);
/*     */   }
/*     */   
/*     */   private static StructurePieceType setTemplatePieceId(StructureTemplateType paramStructureTemplateType, String paramString) {
/* 115 */     return setFullContextPieceId(paramStructureTemplateType, paramString);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\pieces\StructurePieceType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */