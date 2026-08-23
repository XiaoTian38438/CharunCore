/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
/*     */ import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BlockEntityType<T extends BlockEntity>
/*     */ {
/*  24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static Identifier getKey(BlockEntityType<?> paramBlockEntityType) {
/*  27 */     return BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(paramBlockEntityType);
/*     */   }
/*     */   
/*  30 */   public static final BlockEntityType<FurnaceBlockEntity> FURNACE = register("furnace", FurnaceBlockEntity::new, new Block[] { Blocks.FURNACE });
/*  31 */   public static final BlockEntityType<ChestBlockEntity> CHEST = register("chest", ChestBlockEntity::new, new Block[] { Blocks.CHEST, Blocks.COPPER_CHEST, Blocks.EXPOSED_COPPER_CHEST, Blocks.WEATHERED_COPPER_CHEST, Blocks.OXIDIZED_COPPER_CHEST, Blocks.WAXED_COPPER_CHEST, Blocks.WAXED_EXPOSED_COPPER_CHEST, Blocks.WAXED_WEATHERED_COPPER_CHEST, Blocks.WAXED_OXIDIZED_COPPER_CHEST });
/*  32 */   public static final BlockEntityType<TrappedChestBlockEntity> TRAPPED_CHEST = register("trapped_chest", TrappedChestBlockEntity::new, new Block[] { Blocks.TRAPPED_CHEST });
/*  33 */   public static final BlockEntityType<EnderChestBlockEntity> ENDER_CHEST = register("ender_chest", EnderChestBlockEntity::new, new Block[] { Blocks.ENDER_CHEST });
/*  34 */   public static final BlockEntityType<JukeboxBlockEntity> JUKEBOX = register("jukebox", JukeboxBlockEntity::new, new Block[] { Blocks.JUKEBOX });
/*  35 */   public static final BlockEntityType<DispenserBlockEntity> DISPENSER = register("dispenser", DispenserBlockEntity::new, new Block[] { Blocks.DISPENSER });
/*  36 */   public static final BlockEntityType<DropperBlockEntity> DROPPER = register("dropper", DropperBlockEntity::new, new Block[] { Blocks.DROPPER });
/*  37 */   public static final BlockEntityType<SignBlockEntity> SIGN = register("sign", SignBlockEntity::new, new Block[] { Blocks.OAK_SIGN, Blocks.SPRUCE_SIGN, Blocks.BIRCH_SIGN, Blocks.ACACIA_SIGN, Blocks.CHERRY_SIGN, Blocks.JUNGLE_SIGN, Blocks.DARK_OAK_SIGN, Blocks.PALE_OAK_SIGN, Blocks.OAK_WALL_SIGN, Blocks.SPRUCE_WALL_SIGN, Blocks.BIRCH_WALL_SIGN, Blocks.ACACIA_WALL_SIGN, Blocks.CHERRY_WALL_SIGN, Blocks.JUNGLE_WALL_SIGN, Blocks.DARK_OAK_WALL_SIGN, Blocks.PALE_OAK_WALL_SIGN, Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN, Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN, Blocks.MANGROVE_SIGN, Blocks.MANGROVE_WALL_SIGN, Blocks.BAMBOO_SIGN, Blocks.BAMBOO_WALL_SIGN });
/*  38 */   public static final BlockEntityType<HangingSignBlockEntity> HANGING_SIGN = register("hanging_sign", HangingSignBlockEntity::new, new Block[] { Blocks.OAK_HANGING_SIGN, Blocks.SPRUCE_HANGING_SIGN, Blocks.BIRCH_HANGING_SIGN, Blocks.ACACIA_HANGING_SIGN, Blocks.CHERRY_HANGING_SIGN, Blocks.JUNGLE_HANGING_SIGN, Blocks.DARK_OAK_HANGING_SIGN, Blocks.PALE_OAK_HANGING_SIGN, Blocks.CRIMSON_HANGING_SIGN, Blocks.WARPED_HANGING_SIGN, Blocks.MANGROVE_HANGING_SIGN, Blocks.BAMBOO_HANGING_SIGN, Blocks.OAK_WALL_HANGING_SIGN, Blocks.SPRUCE_WALL_HANGING_SIGN, Blocks.BIRCH_WALL_HANGING_SIGN, Blocks.ACACIA_WALL_HANGING_SIGN, Blocks.CHERRY_WALL_HANGING_SIGN, Blocks.JUNGLE_WALL_HANGING_SIGN, Blocks.DARK_OAK_WALL_HANGING_SIGN, Blocks.PALE_OAK_WALL_HANGING_SIGN, Blocks.CRIMSON_WALL_HANGING_SIGN, Blocks.WARPED_WALL_HANGING_SIGN, Blocks.MANGROVE_WALL_HANGING_SIGN, Blocks.BAMBOO_WALL_HANGING_SIGN });
/*  39 */   public static final BlockEntityType<SpawnerBlockEntity> MOB_SPAWNER = register("mob_spawner", SpawnerBlockEntity::new, new Block[] { Blocks.SPAWNER });
/*  40 */   public static final BlockEntityType<CreakingHeartBlockEntity> CREAKING_HEART = register("creaking_heart", CreakingHeartBlockEntity::new, new Block[] { Blocks.CREAKING_HEART });
/*  41 */   public static final BlockEntityType<PistonMovingBlockEntity> PISTON = register("piston", PistonMovingBlockEntity::new, new Block[] { Blocks.MOVING_PISTON });
/*  42 */   public static final BlockEntityType<BrewingStandBlockEntity> BREWING_STAND = register("brewing_stand", BrewingStandBlockEntity::new, new Block[] { Blocks.BREWING_STAND });
/*  43 */   public static final BlockEntityType<EnchantingTableBlockEntity> ENCHANTING_TABLE = register("enchanting_table", EnchantingTableBlockEntity::new, new Block[] { Blocks.ENCHANTING_TABLE });
/*  44 */   public static final BlockEntityType<TheEndPortalBlockEntity> END_PORTAL = register("end_portal", TheEndPortalBlockEntity::new, new Block[] { Blocks.END_PORTAL });
/*  45 */   public static final BlockEntityType<BeaconBlockEntity> BEACON = register("beacon", BeaconBlockEntity::new, new Block[] { Blocks.BEACON });
/*  46 */   public static final BlockEntityType<SkullBlockEntity> SKULL = register("skull", SkullBlockEntity::new, new Block[] { Blocks.SKELETON_SKULL, Blocks.SKELETON_WALL_SKULL, Blocks.CREEPER_HEAD, Blocks.CREEPER_WALL_HEAD, Blocks.DRAGON_HEAD, Blocks.DRAGON_WALL_HEAD, Blocks.ZOMBIE_HEAD, Blocks.ZOMBIE_WALL_HEAD, Blocks.WITHER_SKELETON_SKULL, Blocks.WITHER_SKELETON_WALL_SKULL, Blocks.PLAYER_HEAD, Blocks.PLAYER_WALL_HEAD, Blocks.PIGLIN_HEAD, Blocks.PIGLIN_WALL_HEAD });
/*  47 */   public static final BlockEntityType<DaylightDetectorBlockEntity> DAYLIGHT_DETECTOR = register("daylight_detector", DaylightDetectorBlockEntity::new, new Block[] { Blocks.DAYLIGHT_DETECTOR });
/*  48 */   public static final BlockEntityType<HopperBlockEntity> HOPPER = register("hopper", HopperBlockEntity::new, new Block[] { Blocks.HOPPER });
/*  49 */   public static final BlockEntityType<ComparatorBlockEntity> COMPARATOR = register("comparator", ComparatorBlockEntity::new, new Block[] { Blocks.COMPARATOR });
/*  50 */   public static final BlockEntityType<BannerBlockEntity> BANNER = register("banner", BannerBlockEntity::new, new Block[] { Blocks.WHITE_BANNER, Blocks.ORANGE_BANNER, Blocks.MAGENTA_BANNER, Blocks.LIGHT_BLUE_BANNER, Blocks.YELLOW_BANNER, Blocks.LIME_BANNER, Blocks.PINK_BANNER, Blocks.GRAY_BANNER, Blocks.LIGHT_GRAY_BANNER, Blocks.CYAN_BANNER, Blocks.PURPLE_BANNER, Blocks.BLUE_BANNER, Blocks.BROWN_BANNER, Blocks.GREEN_BANNER, Blocks.RED_BANNER, Blocks.BLACK_BANNER, Blocks.WHITE_WALL_BANNER, Blocks.ORANGE_WALL_BANNER, Blocks.MAGENTA_WALL_BANNER, Blocks.LIGHT_BLUE_WALL_BANNER, Blocks.YELLOW_WALL_BANNER, Blocks.LIME_WALL_BANNER, Blocks.PINK_WALL_BANNER, Blocks.GRAY_WALL_BANNER, Blocks.LIGHT_GRAY_WALL_BANNER, Blocks.CYAN_WALL_BANNER, Blocks.PURPLE_WALL_BANNER, Blocks.BLUE_WALL_BANNER, Blocks.BROWN_WALL_BANNER, Blocks.GREEN_WALL_BANNER, Blocks.RED_WALL_BANNER, Blocks.BLACK_WALL_BANNER });
/*  51 */   public static final BlockEntityType<StructureBlockEntity> STRUCTURE_BLOCK = register("structure_block", StructureBlockEntity::new, new Block[] { Blocks.STRUCTURE_BLOCK });
/*  52 */   public static final BlockEntityType<TheEndGatewayBlockEntity> END_GATEWAY = register("end_gateway", TheEndGatewayBlockEntity::new, new Block[] { Blocks.END_GATEWAY });
/*  53 */   public static final BlockEntityType<CommandBlockEntity> COMMAND_BLOCK = register("command_block", CommandBlockEntity::new, new Block[] { Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.REPEATING_COMMAND_BLOCK });
/*  54 */   public static final BlockEntityType<ShulkerBoxBlockEntity> SHULKER_BOX = register("shulker_box", ShulkerBoxBlockEntity::new, new Block[] { Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX });
/*  55 */   public static final BlockEntityType<BedBlockEntity> BED = register("bed", BedBlockEntity::new, new Block[] { Blocks.RED_BED, Blocks.BLACK_BED, Blocks.BLUE_BED, Blocks.BROWN_BED, Blocks.CYAN_BED, Blocks.GRAY_BED, Blocks.GREEN_BED, Blocks.LIGHT_BLUE_BED, Blocks.LIGHT_GRAY_BED, Blocks.LIME_BED, Blocks.MAGENTA_BED, Blocks.ORANGE_BED, Blocks.PINK_BED, Blocks.PURPLE_BED, Blocks.WHITE_BED, Blocks.YELLOW_BED });
/*  56 */   public static final BlockEntityType<ConduitBlockEntity> CONDUIT = register("conduit", ConduitBlockEntity::new, new Block[] { Blocks.CONDUIT });
/*  57 */   public static final BlockEntityType<BarrelBlockEntity> BARREL = register("barrel", BarrelBlockEntity::new, new Block[] { Blocks.BARREL });
/*  58 */   public static final BlockEntityType<SmokerBlockEntity> SMOKER = register("smoker", SmokerBlockEntity::new, new Block[] { Blocks.SMOKER });
/*  59 */   public static final BlockEntityType<BlastFurnaceBlockEntity> BLAST_FURNACE = register("blast_furnace", BlastFurnaceBlockEntity::new, new Block[] { Blocks.BLAST_FURNACE });
/*  60 */   public static final BlockEntityType<LecternBlockEntity> LECTERN = register("lectern", LecternBlockEntity::new, new Block[] { Blocks.LECTERN });
/*  61 */   public static final BlockEntityType<BellBlockEntity> BELL = register("bell", BellBlockEntity::new, new Block[] { Blocks.BELL });
/*  62 */   public static final BlockEntityType<JigsawBlockEntity> JIGSAW = register("jigsaw", JigsawBlockEntity::new, new Block[] { Blocks.JIGSAW });
/*  63 */   public static final BlockEntityType<CampfireBlockEntity> CAMPFIRE = register("campfire", CampfireBlockEntity::new, new Block[] { Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE });
/*  64 */   public static final BlockEntityType<BeehiveBlockEntity> BEEHIVE = register("beehive", BeehiveBlockEntity::new, new Block[] { Blocks.BEE_NEST, Blocks.BEEHIVE });
/*  65 */   public static final BlockEntityType<SculkSensorBlockEntity> SCULK_SENSOR = register("sculk_sensor", SculkSensorBlockEntity::new, new Block[] { Blocks.SCULK_SENSOR });
/*  66 */   public static final BlockEntityType<CalibratedSculkSensorBlockEntity> CALIBRATED_SCULK_SENSOR = register("calibrated_sculk_sensor", CalibratedSculkSensorBlockEntity::new, new Block[] { Blocks.CALIBRATED_SCULK_SENSOR });
/*  67 */   public static final BlockEntityType<SculkCatalystBlockEntity> SCULK_CATALYST = register("sculk_catalyst", SculkCatalystBlockEntity::new, new Block[] { Blocks.SCULK_CATALYST });
/*  68 */   public static final BlockEntityType<SculkShriekerBlockEntity> SCULK_SHRIEKER = register("sculk_shrieker", SculkShriekerBlockEntity::new, new Block[] { Blocks.SCULK_SHRIEKER });
/*  69 */   public static final BlockEntityType<ChiseledBookShelfBlockEntity> CHISELED_BOOKSHELF = register("chiseled_bookshelf", ChiseledBookShelfBlockEntity::new, new Block[] { Blocks.CHISELED_BOOKSHELF });
/*  70 */   public static final BlockEntityType<ShelfBlockEntity> SHELF = register("shelf", ShelfBlockEntity::new, new Block[] { Blocks.ACACIA_SHELF, Blocks.BAMBOO_SHELF, Blocks.BIRCH_SHELF, Blocks.CHERRY_SHELF, Blocks.CRIMSON_SHELF, Blocks.DARK_OAK_SHELF, Blocks.JUNGLE_SHELF, Blocks.MANGROVE_SHELF, Blocks.OAK_SHELF, Blocks.PALE_OAK_SHELF, Blocks.SPRUCE_SHELF, Blocks.WARPED_SHELF });
/*  71 */   public static final BlockEntityType<BrushableBlockEntity> BRUSHABLE_BLOCK = register("brushable_block", BrushableBlockEntity::new, new Block[] { Blocks.SUSPICIOUS_SAND, Blocks.SUSPICIOUS_GRAVEL });
/*  72 */   public static final BlockEntityType<DecoratedPotBlockEntity> DECORATED_POT = register("decorated_pot", DecoratedPotBlockEntity::new, new Block[] { Blocks.DECORATED_POT });
/*  73 */   public static final BlockEntityType<CrafterBlockEntity> CRAFTER = register("crafter", CrafterBlockEntity::new, new Block[] { Blocks.CRAFTER });
/*  74 */   public static final BlockEntityType<TrialSpawnerBlockEntity> TRIAL_SPAWNER = register("trial_spawner", TrialSpawnerBlockEntity::new, new Block[] { Blocks.TRIAL_SPAWNER });
/*  75 */   public static final BlockEntityType<VaultBlockEntity> VAULT = register("vault", VaultBlockEntity::new, new Block[] { Blocks.VAULT });
/*  76 */   public static final BlockEntityType<TestBlockEntity> TEST_BLOCK = register("test_block", TestBlockEntity::new, new Block[] { Blocks.TEST_BLOCK });
/*  77 */   public static final BlockEntityType<TestInstanceBlockEntity> TEST_INSTANCE_BLOCK = register("test_instance_block", TestInstanceBlockEntity::new, new Block[] { Blocks.TEST_INSTANCE_BLOCK });
/*  78 */   public static final BlockEntityType<CopperGolemStatueBlockEntity> COPPER_GOLEM_STATUE = register("copper_golem_statue", CopperGolemStatueBlockEntity::new, new Block[] { Blocks.COPPER_GOLEM_STATUE, Blocks.EXPOSED_COPPER_GOLEM_STATUE, Blocks.WEATHERED_COPPER_GOLEM_STATUE, Blocks.OXIDIZED_COPPER_GOLEM_STATUE, Blocks.WAXED_COPPER_GOLEM_STATUE, Blocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE, Blocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE, Blocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE });
/*     */   
/*     */   private static <T extends BlockEntity> BlockEntityType<T> register(String paramString, BlockEntitySupplier<? extends T> paramBlockEntitySupplier, Block... paramVarArgs) {
/*  81 */     if (paramVarArgs.length == 0) {
/*  82 */       LOGGER.warn("Block entity type {} requires at least one valid block to be defined!", paramString);
/*     */     }
/*     */     
/*  85 */     Util.fetchChoiceType(References.BLOCK_ENTITY, paramString);
/*  86 */     return (BlockEntityType<T>)Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, paramString, new BlockEntityType<>(paramBlockEntitySupplier, Set.of(paramVarArgs)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   private static final Set<BlockEntityType<?>> OP_ONLY_CUSTOM_DATA = Set.of(COMMAND_BLOCK, LECTERN, SIGN, HANGING_SIGN, MOB_SPAWNER, TRIAL_SPAWNER);
/*     */ 
/*     */ 
/*     */   
/*     */   private final BlockEntitySupplier<? extends T> factory;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Set<Block> validBlocks;
/*     */ 
/*     */ 
/*     */   
/* 104 */   private final Holder.Reference<BlockEntityType<?>> builtInRegistryHolder = BuiltInRegistries.BLOCK_ENTITY_TYPE.createIntrusiveHolder(this);
/*     */   
/*     */   private BlockEntityType(BlockEntitySupplier<? extends T> paramBlockEntitySupplier, Set<Block> paramSet) {
/* 107 */     this.factory = paramBlockEntitySupplier;
/* 108 */     this.validBlocks = paramSet;
/*     */   }
/*     */   
/*     */   public T create(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 112 */     return this.factory.create(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValid(BlockState paramBlockState) {
/* 119 */     return this.validBlocks.contains(paramBlockState.getBlock());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Holder.Reference<BlockEntityType<?>> builtInRegistryHolder() {
/* 127 */     return this.builtInRegistryHolder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getBlockEntity(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 137 */     BlockEntity blockEntity = paramBlockGetter.getBlockEntity(paramBlockPos);
/* 138 */     if (blockEntity == null || blockEntity.getType() != this) {
/* 139 */       return null;
/*     */     }
/* 141 */     return (T)blockEntity;
/*     */   }
/*     */   
/*     */   public boolean onlyOpCanSetNbt() {
/* 145 */     return OP_ONLY_CUSTOM_DATA.contains(this);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface BlockEntitySupplier<T extends BlockEntity> {
/*     */     T create(BlockPos param1BlockPos, BlockState param1BlockState);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\BlockEntityType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */