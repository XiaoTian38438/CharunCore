/*     */ package net.minecraft.world.level.block.state.properties;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.FrontAndTop;
/*     */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*     */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
/*     */ import net.minecraft.world.level.block.entity.vault.VaultState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BlockStateProperties
/*     */ {
/*  18 */   public static final BooleanProperty ATTACHED = BooleanProperty.create("attached");
/*  19 */   public static final BooleanProperty BERRIES = BooleanProperty.create("berries");
/*  20 */   public static final BooleanProperty BLOOM = BooleanProperty.create("bloom");
/*  21 */   public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
/*  22 */   public static final BooleanProperty CAN_SUMMON = BooleanProperty.create("can_summon");
/*  23 */   public static final BooleanProperty CONDITIONAL = BooleanProperty.create("conditional");
/*  24 */   public static final BooleanProperty DISARMED = BooleanProperty.create("disarmed");
/*  25 */   public static final BooleanProperty DRAG = BooleanProperty.create("drag");
/*  26 */   public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");
/*  27 */   public static final BooleanProperty EXTENDED = BooleanProperty.create("extended");
/*  28 */   public static final BooleanProperty EYE = BooleanProperty.create("eye");
/*  29 */   public static final BooleanProperty FALLING = BooleanProperty.create("falling");
/*  30 */   public static final BooleanProperty HANGING = BooleanProperty.create("hanging");
/*  31 */   public static final BooleanProperty HAS_BOTTLE_0 = BooleanProperty.create("has_bottle_0");
/*  32 */   public static final BooleanProperty HAS_BOTTLE_1 = BooleanProperty.create("has_bottle_1");
/*  33 */   public static final BooleanProperty HAS_BOTTLE_2 = BooleanProperty.create("has_bottle_2");
/*  34 */   public static final BooleanProperty HAS_RECORD = BooleanProperty.create("has_record");
/*  35 */   public static final BooleanProperty HAS_BOOK = BooleanProperty.create("has_book");
/*  36 */   public static final BooleanProperty INVERTED = BooleanProperty.create("inverted");
/*  37 */   public static final BooleanProperty IN_WALL = BooleanProperty.create("in_wall");
/*  38 */   public static final BooleanProperty LIT = BooleanProperty.create("lit");
/*  39 */   public static final BooleanProperty LOCKED = BooleanProperty.create("locked");
/*  40 */   public static final BooleanProperty NATURAL = BooleanProperty.create("natural");
/*  41 */   public static final BooleanProperty OCCUPIED = BooleanProperty.create("occupied");
/*  42 */   public static final BooleanProperty OPEN = BooleanProperty.create("open");
/*  43 */   public static final BooleanProperty PERSISTENT = BooleanProperty.create("persistent");
/*  44 */   public static final BooleanProperty POWERED = BooleanProperty.create("powered");
/*  45 */   public static final BooleanProperty SHORT = BooleanProperty.create("short");
/*  46 */   public static final BooleanProperty SHRIEKING = BooleanProperty.create("shrieking");
/*  47 */   public static final BooleanProperty SIGNAL_FIRE = BooleanProperty.create("signal_fire");
/*  48 */   public static final BooleanProperty SNOWY = BooleanProperty.create("snowy");
/*  49 */   public static final BooleanProperty TIP = BooleanProperty.create("tip");
/*  50 */   public static final BooleanProperty TRIGGERED = BooleanProperty.create("triggered");
/*  51 */   public static final BooleanProperty UNSTABLE = BooleanProperty.create("unstable");
/*  52 */   public static final BooleanProperty WATERLOGGED = BooleanProperty.create("waterlogged");
/*     */   
/*  54 */   public static final EnumProperty<Direction.Axis> HORIZONTAL_AXIS = EnumProperty.create("axis", Direction.Axis.class, new Direction.Axis[] { Direction.Axis.X, Direction.Axis.Z });
/*  55 */   public static final EnumProperty<Direction.Axis> AXIS = EnumProperty.create("axis", Direction.Axis.class);
/*     */   
/*  57 */   public static final BooleanProperty UP = BooleanProperty.create("up");
/*  58 */   public static final BooleanProperty DOWN = BooleanProperty.create("down");
/*  59 */   public static final BooleanProperty NORTH = BooleanProperty.create("north");
/*  60 */   public static final BooleanProperty EAST = BooleanProperty.create("east");
/*  61 */   public static final BooleanProperty SOUTH = BooleanProperty.create("south");
/*  62 */   public static final BooleanProperty WEST = BooleanProperty.create("west");
/*     */   public static final EnumProperty<Direction> FACING_HOPPER;
/*  64 */   public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class, new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN });
/*     */   static {
/*  66 */     FACING_HOPPER = EnumProperty.create("facing", Direction.class, paramDirection -> (paramDirection != Direction.UP));
/*  67 */   } public static final EnumProperty<Direction> HORIZONTAL_FACING = EnumProperty.create("facing", Direction.class, (Predicate<Direction>)Direction.Plane.HORIZONTAL);
/*  68 */   public static final IntegerProperty FLOWER_AMOUNT = IntegerProperty.create("flower_amount", 1, 4);
/*  69 */   public static final IntegerProperty SEGMENT_AMOUNT = IntegerProperty.create("segment_amount", 1, 4);
/*     */   
/*  71 */   public static final EnumProperty<FrontAndTop> ORIENTATION = EnumProperty.create("orientation", FrontAndTop.class);
/*     */   
/*  73 */   public static final EnumProperty<AttachFace> ATTACH_FACE = EnumProperty.create("face", AttachFace.class);
/*  74 */   public static final EnumProperty<BellAttachType> BELL_ATTACHMENT = EnumProperty.create("attachment", BellAttachType.class);
/*     */   
/*  76 */   public static final EnumProperty<WallSide> EAST_WALL = EnumProperty.create("east", WallSide.class);
/*  77 */   public static final EnumProperty<WallSide> NORTH_WALL = EnumProperty.create("north", WallSide.class);
/*  78 */   public static final EnumProperty<WallSide> SOUTH_WALL = EnumProperty.create("south", WallSide.class);
/*  79 */   public static final EnumProperty<WallSide> WEST_WALL = EnumProperty.create("west", WallSide.class);
/*     */   
/*  81 */   public static final EnumProperty<RedstoneSide> EAST_REDSTONE = EnumProperty.create("east", RedstoneSide.class);
/*  82 */   public static final EnumProperty<RedstoneSide> NORTH_REDSTONE = EnumProperty.create("north", RedstoneSide.class);
/*  83 */   public static final EnumProperty<RedstoneSide> SOUTH_REDSTONE = EnumProperty.create("south", RedstoneSide.class);
/*  84 */   public static final EnumProperty<RedstoneSide> WEST_REDSTONE = EnumProperty.create("west", RedstoneSide.class);
/*     */   
/*  86 */   public static final EnumProperty<DoubleBlockHalf> DOUBLE_BLOCK_HALF = EnumProperty.create("half", DoubleBlockHalf.class);
/*  87 */   public static final EnumProperty<Half> HALF = EnumProperty.create("half", Half.class);
/*  88 */   public static final EnumProperty<SideChainPart> SIDE_CHAIN_PART = EnumProperty.create("side_chain", SideChainPart.class);
/*     */   
/*  90 */   public static final EnumProperty<RailShape> RAIL_SHAPE = EnumProperty.create("shape", RailShape.class); static {
/*  91 */     RAIL_SHAPE_STRAIGHT = EnumProperty.create("shape", RailShape.class, paramRailShape -> 
/*  92 */         (paramRailShape != RailShape.NORTH_EAST && paramRailShape != RailShape.NORTH_WEST && paramRailShape != RailShape.SOUTH_EAST && paramRailShape != RailShape.SOUTH_WEST));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final EnumProperty<RailShape> RAIL_SHAPE_STRAIGHT;
/*     */   
/*     */   public static final int MAX_AGE_1 = 1;
/*     */   public static final int MAX_AGE_2 = 2;
/*     */   public static final int MAX_AGE_3 = 3;
/*     */   public static final int MAX_AGE_4 = 4;
/*     */   public static final int MAX_AGE_5 = 5;
/*     */   public static final int MAX_AGE_7 = 7;
/*     */   public static final int MAX_AGE_15 = 15;
/*     */   public static final int MAX_AGE_25 = 25;
/* 106 */   public static final IntegerProperty AGE_1 = IntegerProperty.create("age", 0, 1);
/* 107 */   public static final IntegerProperty AGE_2 = IntegerProperty.create("age", 0, 2);
/* 108 */   public static final IntegerProperty AGE_3 = IntegerProperty.create("age", 0, 3);
/* 109 */   public static final IntegerProperty AGE_4 = IntegerProperty.create("age", 0, 4);
/* 110 */   public static final IntegerProperty AGE_5 = IntegerProperty.create("age", 0, 5);
/* 111 */   public static final IntegerProperty AGE_7 = IntegerProperty.create("age", 0, 7);
/* 112 */   public static final IntegerProperty AGE_15 = IntegerProperty.create("age", 0, 15);
/* 113 */   public static final IntegerProperty AGE_25 = IntegerProperty.create("age", 0, 25);
/*     */   
/* 115 */   public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 6);
/* 116 */   public static final IntegerProperty CANDLES = IntegerProperty.create("candles", 1, 4);
/* 117 */   public static final IntegerProperty DELAY = IntegerProperty.create("delay", 1, 4);
/*     */   public static final int MAX_DISTANCE = 7;
/* 119 */   public static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 1, 7);
/* 120 */   public static final IntegerProperty EGGS = IntegerProperty.create("eggs", 1, 4);
/* 121 */   public static final IntegerProperty HATCH = IntegerProperty.create("hatch", 0, 2);
/* 122 */   public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 1, 8);
/*     */   public static final int MIN_LEVEL = 0;
/*     */   public static final int MIN_LEVEL_CAULDRON = 1;
/*     */   public static final int MAX_LEVEL_3 = 3;
/*     */   public static final int MAX_LEVEL_8 = 8;
/* 127 */   public static final IntegerProperty LEVEL_CAULDRON = IntegerProperty.create("level", 1, 3);
/* 128 */   public static final IntegerProperty LEVEL_COMPOSTER = IntegerProperty.create("level", 0, 8);
/* 129 */   public static final IntegerProperty LEVEL_FLOWING = IntegerProperty.create("level", 1, 8);
/* 130 */   public static final IntegerProperty LEVEL_HONEY = IntegerProperty.create("honey_level", 0, 5);
/*     */   public static final int MAX_LEVEL_15 = 15;
/* 132 */   public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 15);
/* 133 */   public static final IntegerProperty MOISTURE = IntegerProperty.create("moisture", 0, 7);
/* 134 */   public static final IntegerProperty NOTE = IntegerProperty.create("note", 0, 24);
/* 135 */   public static final IntegerProperty PICKLES = IntegerProperty.create("pickles", 1, 4);
/* 136 */   public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);
/* 137 */   public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 1);
/*     */   public static final int STABILITY_MAX_DISTANCE = 7;
/* 139 */   public static final IntegerProperty STABILITY_DISTANCE = IntegerProperty.create("distance", 0, 7);
/*     */   public static final int MIN_RESPAWN_ANCHOR_CHARGES = 0;
/*     */   public static final int MAX_RESPAWN_ANCHOR_CHARGES = 4;
/* 142 */   public static final IntegerProperty RESPAWN_ANCHOR_CHARGES = IntegerProperty.create("charges", 0, 4);
/* 143 */   public static final IntegerProperty DRIED_GHAST_HYDRATION_LEVELS = IntegerProperty.create("hydration", 0, 3);
/*     */   
/* 145 */   public static final IntegerProperty ROTATION_16 = IntegerProperty.create("rotation", 0, RotationSegment.getMaxSegmentIndex());
/*     */   
/* 147 */   public static final EnumProperty<BedPart> BED_PART = EnumProperty.create("part", BedPart.class);
/* 148 */   public static final EnumProperty<ChestType> CHEST_TYPE = EnumProperty.create("type", ChestType.class);
/* 149 */   public static final EnumProperty<ComparatorMode> MODE_COMPARATOR = EnumProperty.create("mode", ComparatorMode.class);
/* 150 */   public static final EnumProperty<DoorHingeSide> DOOR_HINGE = EnumProperty.create("hinge", DoorHingeSide.class);
/* 151 */   public static final EnumProperty<NoteBlockInstrument> NOTEBLOCK_INSTRUMENT = EnumProperty.create("instrument", NoteBlockInstrument.class);
/* 152 */   public static final EnumProperty<PistonType> PISTON_TYPE = EnumProperty.create("type", PistonType.class);
/* 153 */   public static final EnumProperty<SlabType> SLAB_TYPE = EnumProperty.create("type", SlabType.class);
/* 154 */   public static final EnumProperty<StairsShape> STAIRS_SHAPE = EnumProperty.create("shape", StairsShape.class);
/* 155 */   public static final EnumProperty<StructureMode> STRUCTUREBLOCK_MODE = EnumProperty.create("mode", StructureMode.class);
/* 156 */   public static final EnumProperty<BambooLeaves> BAMBOO_LEAVES = EnumProperty.create("leaves", BambooLeaves.class);
/* 157 */   public static final EnumProperty<Tilt> TILT = EnumProperty.create("tilt", Tilt.class);
/*     */   
/* 159 */   public static final EnumProperty<Direction> VERTICAL_DIRECTION = EnumProperty.create("vertical_direction", Direction.class, new Direction[] { Direction.UP, Direction.DOWN });
/* 160 */   public static final EnumProperty<DripstoneThickness> DRIPSTONE_THICKNESS = EnumProperty.create("thickness", DripstoneThickness.class);
/* 161 */   public static final EnumProperty<SculkSensorPhase> SCULK_SENSOR_PHASE = EnumProperty.create("sculk_sensor_phase", SculkSensorPhase.class);
/* 162 */   public static final BooleanProperty SLOT_0_OCCUPIED = BooleanProperty.create("slot_0_occupied");
/* 163 */   public static final BooleanProperty SLOT_1_OCCUPIED = BooleanProperty.create("slot_1_occupied");
/* 164 */   public static final BooleanProperty SLOT_2_OCCUPIED = BooleanProperty.create("slot_2_occupied");
/* 165 */   public static final BooleanProperty SLOT_3_OCCUPIED = BooleanProperty.create("slot_3_occupied");
/* 166 */   public static final BooleanProperty SLOT_4_OCCUPIED = BooleanProperty.create("slot_4_occupied");
/* 167 */   public static final BooleanProperty SLOT_5_OCCUPIED = BooleanProperty.create("slot_5_occupied");
/* 168 */   public static final IntegerProperty DUSTED = IntegerProperty.create("dusted", 0, 3);
/* 169 */   public static final BooleanProperty CRACKED = BooleanProperty.create("cracked");
/* 170 */   public static final BooleanProperty CRAFTING = BooleanProperty.create("crafting");
/* 171 */   public static final EnumProperty<TrialSpawnerState> TRIAL_SPAWNER_STATE = EnumProperty.create("trial_spawner_state", TrialSpawnerState.class);
/* 172 */   public static final EnumProperty<VaultState> VAULT_STATE = EnumProperty.create("vault_state", VaultState.class);
/* 173 */   public static final EnumProperty<CreakingHeartState> CREAKING_HEART_STATE = EnumProperty.create("creaking_heart_state", CreakingHeartState.class);
/* 174 */   public static final BooleanProperty OMINOUS = BooleanProperty.create("ominous");
/* 175 */   public static final EnumProperty<TestBlockMode> TEST_BLOCK_MODE = EnumProperty.create("mode", TestBlockMode.class);
/* 176 */   public static final BooleanProperty MAP = BooleanProperty.create("map");
/* 177 */   public static final EnumProperty<CopperGolemStatueBlock.Pose> COPPER_GOLEM_POSE = EnumProperty.create("copper_golem_pose", CopperGolemStatueBlock.Pose.class);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\state\properties\BlockStateProperties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */