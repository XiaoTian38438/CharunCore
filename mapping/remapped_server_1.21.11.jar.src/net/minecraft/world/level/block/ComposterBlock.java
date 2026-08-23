/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2FloatMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.WorldlyContainer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ComposterBlock extends Block implements WorldlyContainerHolder {
/*  44 */   public static final MapCodec<ComposterBlock> CODEC = simpleCodec(ComposterBlock::new); public static final int READY = 8; public static final int MIN_LEVEL = 0;
/*     */   public static final int MAX_LEVEL = 7;
/*     */   
/*     */   public MapCodec<ComposterBlock> codec() {
/*  48 */     return CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_COMPOSTER;
/*     */   
/*  56 */   public static final Object2FloatMap<ItemLike> COMPOSTABLES = (Object2FloatMap<ItemLike>)new Object2FloatOpenHashMap(); private static final int HOLE_WIDTH = 12; private static final VoxelShape[] SHAPES;
/*     */   
/*     */   public static void bootStrap() {
/*  59 */     COMPOSTABLES.defaultReturnValue(-1.0F);
/*     */     
/*  61 */     float f1 = 0.3F;
/*  62 */     float f2 = 0.5F;
/*  63 */     float f3 = 0.65F;
/*  64 */     float f4 = 0.85F;
/*  65 */     float f5 = 1.0F;
/*     */     
/*  67 */     add(0.3F, (ItemLike)Items.JUNGLE_LEAVES);
/*  68 */     add(0.3F, (ItemLike)Items.OAK_LEAVES);
/*  69 */     add(0.3F, (ItemLike)Items.SPRUCE_LEAVES);
/*  70 */     add(0.3F, (ItemLike)Items.DARK_OAK_LEAVES);
/*  71 */     add(0.3F, (ItemLike)Items.PALE_OAK_LEAVES);
/*  72 */     add(0.3F, (ItemLike)Items.ACACIA_LEAVES);
/*  73 */     add(0.3F, (ItemLike)Items.CHERRY_LEAVES);
/*  74 */     add(0.3F, (ItemLike)Items.BIRCH_LEAVES);
/*  75 */     add(0.3F, (ItemLike)Items.AZALEA_LEAVES);
/*  76 */     add(0.3F, (ItemLike)Items.MANGROVE_LEAVES);
/*  77 */     add(0.3F, (ItemLike)Items.OAK_SAPLING);
/*  78 */     add(0.3F, (ItemLike)Items.SPRUCE_SAPLING);
/*  79 */     add(0.3F, (ItemLike)Items.BIRCH_SAPLING);
/*  80 */     add(0.3F, (ItemLike)Items.JUNGLE_SAPLING);
/*  81 */     add(0.3F, (ItemLike)Items.ACACIA_SAPLING);
/*  82 */     add(0.3F, (ItemLike)Items.CHERRY_SAPLING);
/*  83 */     add(0.3F, (ItemLike)Items.DARK_OAK_SAPLING);
/*  84 */     add(0.3F, (ItemLike)Items.PALE_OAK_SAPLING);
/*  85 */     add(0.3F, (ItemLike)Items.MANGROVE_PROPAGULE);
/*  86 */     add(0.3F, (ItemLike)Items.BEETROOT_SEEDS);
/*  87 */     add(0.3F, (ItemLike)Items.DRIED_KELP);
/*  88 */     add(0.3F, (ItemLike)Items.SHORT_GRASS);
/*  89 */     add(0.3F, (ItemLike)Items.KELP);
/*  90 */     add(0.3F, (ItemLike)Items.MELON_SEEDS);
/*  91 */     add(0.3F, (ItemLike)Items.PUMPKIN_SEEDS);
/*  92 */     add(0.3F, (ItemLike)Items.SEAGRASS);
/*  93 */     add(0.3F, (ItemLike)Items.SWEET_BERRIES);
/*  94 */     add(0.3F, (ItemLike)Items.GLOW_BERRIES);
/*  95 */     add(0.3F, (ItemLike)Items.WHEAT_SEEDS);
/*  96 */     add(0.3F, (ItemLike)Items.MOSS_CARPET);
/*  97 */     add(0.3F, (ItemLike)Items.PALE_MOSS_CARPET);
/*  98 */     add(0.3F, (ItemLike)Items.PALE_HANGING_MOSS);
/*  99 */     add(0.3F, (ItemLike)Items.PINK_PETALS);
/* 100 */     add(0.3F, (ItemLike)Items.WILDFLOWERS);
/* 101 */     add(0.3F, (ItemLike)Items.LEAF_LITTER);
/* 102 */     add(0.3F, (ItemLike)Items.SMALL_DRIPLEAF);
/* 103 */     add(0.3F, (ItemLike)Items.HANGING_ROOTS);
/* 104 */     add(0.3F, (ItemLike)Items.MANGROVE_ROOTS);
/* 105 */     add(0.3F, (ItemLike)Items.TORCHFLOWER_SEEDS);
/* 106 */     add(0.3F, (ItemLike)Items.PITCHER_POD);
/* 107 */     add(0.3F, (ItemLike)Items.FIREFLY_BUSH);
/* 108 */     add(0.3F, (ItemLike)Items.BUSH);
/* 109 */     add(0.3F, (ItemLike)Items.CACTUS_FLOWER);
/* 110 */     add(0.3F, (ItemLike)Items.DRY_SHORT_GRASS);
/* 111 */     add(0.3F, (ItemLike)Items.DRY_TALL_GRASS);
/*     */     
/* 113 */     add(0.5F, (ItemLike)Items.DRIED_KELP_BLOCK);
/* 114 */     add(0.5F, (ItemLike)Items.TALL_GRASS);
/* 115 */     add(0.5F, (ItemLike)Items.FLOWERING_AZALEA_LEAVES);
/* 116 */     add(0.5F, (ItemLike)Items.CACTUS);
/* 117 */     add(0.5F, (ItemLike)Items.SUGAR_CANE);
/* 118 */     add(0.5F, (ItemLike)Items.VINE);
/* 119 */     add(0.5F, (ItemLike)Items.NETHER_SPROUTS);
/* 120 */     add(0.5F, (ItemLike)Items.WEEPING_VINES);
/* 121 */     add(0.5F, (ItemLike)Items.TWISTING_VINES);
/* 122 */     add(0.5F, (ItemLike)Items.MELON_SLICE);
/* 123 */     add(0.5F, (ItemLike)Items.GLOW_LICHEN);
/*     */     
/* 125 */     add(0.65F, (ItemLike)Items.SEA_PICKLE);
/* 126 */     add(0.65F, (ItemLike)Items.LILY_PAD);
/* 127 */     add(0.65F, (ItemLike)Items.PUMPKIN);
/* 128 */     add(0.65F, (ItemLike)Items.CARVED_PUMPKIN);
/* 129 */     add(0.65F, (ItemLike)Items.MELON);
/* 130 */     add(0.65F, (ItemLike)Items.APPLE);
/* 131 */     add(0.65F, (ItemLike)Items.BEETROOT);
/* 132 */     add(0.65F, (ItemLike)Items.CARROT);
/* 133 */     add(0.65F, (ItemLike)Items.COCOA_BEANS);
/* 134 */     add(0.65F, (ItemLike)Items.POTATO);
/* 135 */     add(0.65F, (ItemLike)Items.WHEAT);
/* 136 */     add(0.65F, (ItemLike)Items.BROWN_MUSHROOM);
/* 137 */     add(0.65F, (ItemLike)Items.RED_MUSHROOM);
/* 138 */     add(0.65F, (ItemLike)Items.MUSHROOM_STEM);
/* 139 */     add(0.65F, (ItemLike)Items.CRIMSON_FUNGUS);
/* 140 */     add(0.65F, (ItemLike)Items.WARPED_FUNGUS);
/* 141 */     add(0.65F, (ItemLike)Items.NETHER_WART);
/* 142 */     add(0.65F, (ItemLike)Items.CRIMSON_ROOTS);
/* 143 */     add(0.65F, (ItemLike)Items.WARPED_ROOTS);
/* 144 */     add(0.65F, (ItemLike)Items.SHROOMLIGHT);
/* 145 */     add(0.65F, (ItemLike)Items.DANDELION);
/* 146 */     add(0.65F, (ItemLike)Items.POPPY);
/* 147 */     add(0.65F, (ItemLike)Items.BLUE_ORCHID);
/* 148 */     add(0.65F, (ItemLike)Items.ALLIUM);
/* 149 */     add(0.65F, (ItemLike)Items.AZURE_BLUET);
/* 150 */     add(0.65F, (ItemLike)Items.RED_TULIP);
/* 151 */     add(0.65F, (ItemLike)Items.ORANGE_TULIP);
/* 152 */     add(0.65F, (ItemLike)Items.WHITE_TULIP);
/* 153 */     add(0.65F, (ItemLike)Items.PINK_TULIP);
/* 154 */     add(0.65F, (ItemLike)Items.OXEYE_DAISY);
/* 155 */     add(0.65F, (ItemLike)Items.CORNFLOWER);
/* 156 */     add(0.65F, (ItemLike)Items.LILY_OF_THE_VALLEY);
/* 157 */     add(0.65F, (ItemLike)Items.WITHER_ROSE);
/* 158 */     add(0.65F, (ItemLike)Items.OPEN_EYEBLOSSOM);
/* 159 */     add(0.65F, (ItemLike)Items.CLOSED_EYEBLOSSOM);
/* 160 */     add(0.65F, (ItemLike)Items.FERN);
/* 161 */     add(0.65F, (ItemLike)Items.SUNFLOWER);
/* 162 */     add(0.65F, (ItemLike)Items.LILAC);
/* 163 */     add(0.65F, (ItemLike)Items.ROSE_BUSH);
/* 164 */     add(0.65F, (ItemLike)Items.PEONY);
/* 165 */     add(0.65F, (ItemLike)Items.LARGE_FERN);
/* 166 */     add(0.65F, (ItemLike)Items.SPORE_BLOSSOM);
/* 167 */     add(0.65F, (ItemLike)Items.AZALEA);
/* 168 */     add(0.65F, (ItemLike)Items.MOSS_BLOCK);
/* 169 */     add(0.65F, (ItemLike)Items.PALE_MOSS_BLOCK);
/* 170 */     add(0.65F, (ItemLike)Items.BIG_DRIPLEAF);
/*     */     
/* 172 */     add(0.85F, (ItemLike)Items.HAY_BLOCK);
/* 173 */     add(0.85F, (ItemLike)Items.BROWN_MUSHROOM_BLOCK);
/* 174 */     add(0.85F, (ItemLike)Items.RED_MUSHROOM_BLOCK);
/* 175 */     add(0.85F, (ItemLike)Items.NETHER_WART_BLOCK);
/* 176 */     add(0.85F, (ItemLike)Items.WARPED_WART_BLOCK);
/* 177 */     add(0.85F, (ItemLike)Items.FLOWERING_AZALEA);
/* 178 */     add(0.85F, (ItemLike)Items.BREAD);
/* 179 */     add(0.85F, (ItemLike)Items.BAKED_POTATO);
/* 180 */     add(0.85F, (ItemLike)Items.COOKIE);
/* 181 */     add(0.85F, (ItemLike)Items.TORCHFLOWER);
/* 182 */     add(0.85F, (ItemLike)Items.PITCHER_PLANT);
/*     */     
/* 184 */     add(1.0F, (ItemLike)Items.CAKE);
/* 185 */     add(1.0F, (ItemLike)Items.PUMPKIN_PIE);
/*     */   }
/*     */   
/*     */   private static void add(float paramFloat, ItemLike paramItemLike) {
/* 189 */     COMPOSTABLES.put(paramItemLike.asItem(), paramFloat);
/*     */   }
/*     */   
/*     */   static {
/* 193 */     SHAPES = (VoxelShape[])Util.make(() -> {
/*     */           VoxelShape[] arrayOfVoxelShape = Block.boxes(8, ());
/*     */           arrayOfVoxelShape[8] = arrayOfVoxelShape[7];
/*     */           return arrayOfVoxelShape;
/*     */         });
/*     */   }
/*     */   public ComposterBlock(BlockBehaviour.Properties paramProperties) {
/* 200 */     super(paramProperties);
/* 201 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LEVEL, Integer.valueOf(0)));
/*     */   }
/*     */   
/*     */   public static void handleFill(Level paramLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 205 */     BlockState blockState = paramLevel.getBlockState(paramBlockPos);
/*     */     
/* 207 */     paramLevel.playLocalSound(paramBlockPos, paramBoolean ? SoundEvents.COMPOSTER_FILL_SUCCESS : SoundEvents.COMPOSTER_FILL, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */     
/* 209 */     double d1 = blockState.getShape((BlockGetter)paramLevel, paramBlockPos).max(Direction.Axis.Y, 0.5D, 0.5D) + 0.03125D;
/* 210 */     double d2 = 2.0D;
/* 211 */     double d3 = 0.1875D;
/* 212 */     double d4 = 0.625D;
/*     */     
/* 214 */     RandomSource randomSource = paramLevel.getRandom();
/* 215 */     for (byte b = 0; b < 10; b++) {
/* 216 */       double d5 = randomSource.nextGaussian() * 0.02D;
/* 217 */       double d6 = randomSource.nextGaussian() * 0.02D;
/* 218 */       double d7 = randomSource.nextGaussian() * 0.02D;
/* 219 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.COMPOSTER, paramBlockPos
/*     */           
/* 221 */           .getX() + 0.1875D + 0.625D * randomSource.nextFloat(), paramBlockPos
/* 222 */           .getY() + d1 + randomSource.nextFloat() * (1.0D - d1), paramBlockPos
/* 223 */           .getZ() + 0.1875D + 0.625D * randomSource.nextFloat(), d5, d6, d7);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 231 */     return SHAPES[((Integer)paramBlockState.getValue((Property)LEVEL)).intValue()];
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getInteractionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 236 */     return Shapes.block();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 242 */     return SHAPES[0];
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 247 */     if (((Integer)paramBlockState1.getValue((Property)LEVEL)).intValue() == 7) {
/* 248 */       paramLevel.scheduleTick(paramBlockPos, paramBlockState1.getBlock(), 20);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/* 254 */     int i = ((Integer)paramBlockState.getValue((Property)LEVEL)).intValue();
/*     */     
/* 256 */     if (i < 8 && COMPOSTABLES.containsKey(paramItemStack.getItem())) {
/* 257 */       if (i < 7 && !paramLevel.isClientSide()) {
/* 258 */         BlockState blockState = addItem((Entity)paramPlayer, paramBlockState, (LevelAccessor)paramLevel, paramBlockPos, paramItemStack);
/* 259 */         paramLevel.levelEvent(1500, paramBlockPos, (paramBlockState != blockState) ? 1 : 0);
/* 260 */         paramPlayer.awardStat(Stats.ITEM_USED.get(paramItemStack.getItem()));
/*     */         
/* 262 */         paramItemStack.consume(1, (LivingEntity)paramPlayer);
/*     */       } 
/*     */       
/* 265 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 268 */     return super.useItemOn(paramItemStack, paramBlockState, paramLevel, paramBlockPos, paramPlayer, paramInteractionHand, paramBlockHitResult);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 273 */     int i = ((Integer)paramBlockState.getValue((Property)LEVEL)).intValue();
/*     */     
/* 275 */     if (i == 8) {
/* 276 */       extractProduce((Entity)paramPlayer, paramBlockState, paramLevel, paramBlockPos);
/* 277 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 280 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public static BlockState insertItem(Entity paramEntity, BlockState paramBlockState, ServerLevel paramServerLevel, ItemStack paramItemStack, BlockPos paramBlockPos) {
/* 284 */     int i = ((Integer)paramBlockState.getValue((Property)LEVEL)).intValue();
/*     */     
/* 286 */     if (i < 7 && COMPOSTABLES.containsKey(paramItemStack.getItem())) {
/* 287 */       BlockState blockState = addItem(paramEntity, paramBlockState, (LevelAccessor)paramServerLevel, paramBlockPos, paramItemStack);
/* 288 */       paramItemStack.shrink(1);
/* 289 */       return blockState;
/*     */     } 
/*     */     
/* 292 */     return paramBlockState;
/*     */   }
/*     */   
/*     */   public static BlockState extractProduce(Entity paramEntity, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 296 */     if (!paramLevel.isClientSide()) {
/* 297 */       Vec3 vec3 = Vec3.atLowerCornerWithOffset((Vec3i)paramBlockPos, 0.5D, 1.01D, 0.5D).offsetRandomXZ(paramLevel.random, 0.7F);
/* 298 */       ItemEntity itemEntity = new ItemEntity(paramLevel, vec3.x(), vec3.y(), vec3.z(), new ItemStack((ItemLike)Items.BONE_MEAL));
/* 299 */       itemEntity.setDefaultPickUpDelay();
/* 300 */       paramLevel.addFreshEntity((Entity)itemEntity);
/*     */     } 
/*     */     
/* 303 */     BlockState blockState = empty(paramEntity, paramBlockState, (LevelAccessor)paramLevel, paramBlockPos);
/* 304 */     paramLevel.playSound(null, paramBlockPos, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 305 */     return blockState;
/*     */   }
/*     */   
/*     */   static BlockState empty(Entity paramEntity, BlockState paramBlockState, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 309 */     BlockState blockState = (BlockState)paramBlockState.setValue((Property)LEVEL, Integer.valueOf(0));
/* 310 */     paramLevelAccessor.setBlock(paramBlockPos, blockState, 3);
/* 311 */     paramLevelAccessor.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of(paramEntity, blockState));
/* 312 */     return blockState;
/*     */   }
/*     */   
/*     */   static BlockState addItem(Entity paramEntity, BlockState paramBlockState, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, ItemStack paramItemStack) {
/* 316 */     int i = ((Integer)paramBlockState.getValue((Property)LEVEL)).intValue();
/* 317 */     float f = COMPOSTABLES.getFloat(paramItemStack.getItem());
/* 318 */     if ((i == 0 && f > 0.0F) || paramLevelAccessor.getRandom().nextDouble() < f) {
/* 319 */       int j = i + 1;
/* 320 */       BlockState blockState = (BlockState)paramBlockState.setValue((Property)LEVEL, Integer.valueOf(j));
/* 321 */       paramLevelAccessor.setBlock(paramBlockPos, blockState, 3);
/* 322 */       paramLevelAccessor.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of(paramEntity, blockState));
/*     */       
/* 324 */       if (j == 7) {
/* 325 */         paramLevelAccessor.scheduleTick(paramBlockPos, paramBlockState.getBlock(), 20);
/*     */       }
/*     */       
/* 328 */       return blockState;
/*     */     } 
/* 330 */     return paramBlockState;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 335 */     if (((Integer)paramBlockState.getValue((Property)LEVEL)).intValue() == 7) {
/* 336 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.cycle((Property)LEVEL), 3);
/* 337 */       paramServerLevel.playSound(null, paramBlockPos, SoundEvents.COMPOSTER_READY, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 343 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 348 */     return ((Integer)paramBlockState.getValue((Property)LEVEL)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 353 */     paramBuilder.add(new Property[] { (Property)LEVEL });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 358 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldlyContainer getContainer(BlockState paramBlockState, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 363 */     int i = ((Integer)paramBlockState.getValue((Property)LEVEL)).intValue();
/* 364 */     if (i == 8) {
/* 365 */       return new OutputContainer(paramBlockState, paramLevelAccessor, paramBlockPos, new ItemStack((ItemLike)Items.BONE_MEAL));
/*     */     }
/*     */     
/* 368 */     if (i < 7) {
/* 369 */       return new InputContainer(paramBlockState, paramLevelAccessor, paramBlockPos);
/*     */     }
/*     */     
/* 372 */     return new EmptyContainer();
/*     */   }
/*     */   
/*     */   private static class EmptyContainer extends SimpleContainer implements WorldlyContainer {
/*     */     public EmptyContainer() {
/* 377 */       super(0);
/*     */     }
/*     */ 
/*     */     
/*     */     public int[] getSlotsForFace(Direction param1Direction) {
/* 382 */       return new int[0];
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canPlaceItemThroughFace(int param1Int, ItemStack param1ItemStack, Direction param1Direction) {
/* 387 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canTakeItemThroughFace(int param1Int, ItemStack param1ItemStack, Direction param1Direction) {
/* 392 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class OutputContainer extends SimpleContainer implements WorldlyContainer {
/*     */     private final BlockState state;
/*     */     private final LevelAccessor level;
/*     */     private final BlockPos pos;
/*     */     private boolean changed;
/*     */     
/*     */     public OutputContainer(BlockState param1BlockState, LevelAccessor param1LevelAccessor, BlockPos param1BlockPos, ItemStack param1ItemStack) {
/* 403 */       super(new ItemStack[] { param1ItemStack });
/* 404 */       this.state = param1BlockState;
/* 405 */       this.level = param1LevelAccessor;
/* 406 */       this.pos = param1BlockPos;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getMaxStackSize() {
/* 411 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int[] getSlotsForFace(Direction param1Direction) {
/* 416 */       (new int[1])[0] = 0; return (param1Direction == Direction.DOWN) ? new int[1] : new int[0];
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canPlaceItemThroughFace(int param1Int, ItemStack param1ItemStack, Direction param1Direction) {
/* 421 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canTakeItemThroughFace(int param1Int, ItemStack param1ItemStack, Direction param1Direction) {
/* 426 */       return (!this.changed && param1Direction == Direction.DOWN && param1ItemStack.is(Items.BONE_MEAL));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setChanged() {
/* 431 */       ComposterBlock.empty((Entity)null, this.state, this.level, this.pos);
/* 432 */       this.changed = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class InputContainer extends SimpleContainer implements WorldlyContainer {
/*     */     private final BlockState state;
/*     */     private final LevelAccessor level;
/*     */     private final BlockPos pos;
/*     */     private boolean changed;
/*     */     
/*     */     public InputContainer(BlockState param1BlockState, LevelAccessor param1LevelAccessor, BlockPos param1BlockPos) {
/* 443 */       super(1);
/* 444 */       this.state = param1BlockState;
/* 445 */       this.level = param1LevelAccessor;
/* 446 */       this.pos = param1BlockPos;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getMaxStackSize() {
/* 451 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int[] getSlotsForFace(Direction param1Direction) {
/* 456 */       (new int[1])[0] = 0; return (param1Direction == Direction.UP) ? new int[1] : new int[0];
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canPlaceItemThroughFace(int param1Int, ItemStack param1ItemStack, Direction param1Direction) {
/* 461 */       return (!this.changed && param1Direction == Direction.UP && ComposterBlock.COMPOSTABLES.containsKey(param1ItemStack.getItem()));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canTakeItemThroughFace(int param1Int, ItemStack param1ItemStack, Direction param1Direction) {
/* 466 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setChanged() {
/* 471 */       ItemStack itemStack = getItem(0);
/* 472 */       if (!itemStack.isEmpty()) {
/* 473 */         this.changed = true;
/* 474 */         BlockState blockState = ComposterBlock.addItem((Entity)null, this.state, this.level, this.pos, itemStack);
/* 475 */         this.level.levelEvent(1500, this.pos, (blockState != this.state) ? 1 : 0);
/* 476 */         removeItemNoUpdate(0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\ComposterBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */