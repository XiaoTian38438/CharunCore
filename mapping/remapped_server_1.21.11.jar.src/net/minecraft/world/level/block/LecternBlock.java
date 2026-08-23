/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.LecternBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LecternBlock extends BaseEntityBlock {
/*  46 */   public static final MapCodec<LecternBlock> CODEC = simpleCodec(LecternBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<LecternBlock> codec() {
/*  50 */     return CODEC;
/*     */   }
/*     */   
/*  53 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  54 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  55 */   public static final BooleanProperty HAS_BOOK = BlockStateProperties.HAS_BOOK;
/*     */   
/*  57 */   private static final VoxelShape SHAPE_COLLISION = Shapes.or(
/*  58 */       Block.column(16.0D, 0.0D, 2.0D), 
/*  59 */       Block.column(8.0D, 2.0D, 14.0D));
/*     */ 
/*     */   
/*  62 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Shapes.or(
/*  63 */         Block.boxZ(16.0D, 10.0D, 14.0D, 1.0D, 5.333333D), new VoxelShape[] {
/*  64 */           Block.boxZ(16.0D, 12.0D, 16.0D, 5.333333D, 9.666667D), 
/*  65 */           Block.boxZ(16.0D, 14.0D, 18.0D, 9.666667D, 14.0D), SHAPE_COLLISION
/*     */         }));
/*     */   
/*     */   private static final int PAGE_CHANGE_IMPULSE_TICKS = 2;
/*     */ 
/*     */   
/*     */   protected LecternBlock(BlockBehaviour.Properties paramProperties) {
/*  72 */     super(paramProperties);
/*  73 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)HAS_BOOK, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getOcclusionShape(BlockState paramBlockState) {
/*  78 */     return SHAPE_COLLISION;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState paramBlockState) {
/*  83 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  88 */     Level level = paramBlockPlaceContext.getLevel();
/*  89 */     ItemStack itemStack = paramBlockPlaceContext.getItemInHand();
/*  90 */     Player player = paramBlockPlaceContext.getPlayer();
/*  91 */     boolean bool = false;
/*     */     
/*  93 */     if (!level.isClientSide() && player != null && player.canUseGameMasterBlocks()) {
/*  94 */       TypedEntityData typedEntityData = (TypedEntityData)itemStack.get(DataComponents.BLOCK_ENTITY_DATA);
/*  95 */       if (typedEntityData != null && typedEntityData.contains("Book")) {
/*  96 */         bool = true;
/*     */       }
/*     */     } 
/*  99 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite())).setValue((Property)HAS_BOOK, Boolean.valueOf(bool));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 104 */     return SHAPE_COLLISION;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 109 */     return SHAPES.get(paramBlockState.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 114 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 119 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 124 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)POWERED, (Property)HAS_BOOK });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 129 */     return (BlockEntity)new LecternBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */   
/*     */   public static boolean tryPlaceBook(LivingEntity paramLivingEntity, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, ItemStack paramItemStack) {
/* 133 */     if (!((Boolean)paramBlockState.getValue((Property)HAS_BOOK)).booleanValue()) {
/* 134 */       if (!paramLevel.isClientSide()) {
/* 135 */         placeBook(paramLivingEntity, paramLevel, paramBlockPos, paramBlockState, paramItemStack);
/*     */       }
/* 137 */       return true;
/*     */     } 
/*     */     
/* 140 */     return false;
/*     */   }
/*     */   
/*     */   private static void placeBook(LivingEntity paramLivingEntity, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, ItemStack paramItemStack) {
/* 144 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 145 */     if (blockEntity instanceof LecternBlockEntity) { LecternBlockEntity lecternBlockEntity = (LecternBlockEntity)blockEntity;
/* 146 */       lecternBlockEntity.setBook(paramItemStack.consumeAndReturn(1, paramLivingEntity));
/* 147 */       resetBookState((Entity)paramLivingEntity, paramLevel, paramBlockPos, paramBlockState, true);
/* 148 */       paramLevel.playSound(null, paramBlockPos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1.0F, 1.0F); }
/*     */   
/*     */   }
/*     */   
/*     */   public static void resetBookState(Entity paramEntity, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 153 */     BlockState blockState = (BlockState)((BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)HAS_BOOK, Boolean.valueOf(paramBoolean));
/* 154 */     paramLevel.setBlock(paramBlockPos, blockState, 3);
/* 155 */     paramLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of(paramEntity, blockState));
/* 156 */     updateBelow(paramLevel, paramBlockPos, paramBlockState);
/*     */   }
/*     */   
/*     */   public static void signalPageChange(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 160 */     changePowered(paramLevel, paramBlockPos, paramBlockState, true);
/* 161 */     paramLevel.scheduleTick(paramBlockPos, paramBlockState.getBlock(), 2);
/* 162 */     paramLevel.levelEvent(1043, paramBlockPos, 0);
/*     */   }
/*     */   
/*     */   private static void changePowered(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 166 */     paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(paramBoolean)), 3);
/* 167 */     updateBelow(paramLevel, paramBlockPos, paramBlockState);
/*     */   }
/*     */   
/*     */   private static void updateBelow(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 171 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(paramLevel, ((Direction)paramBlockState.getValue((Property)FACING)).getOpposite(), Direction.UP);
/* 172 */     paramLevel.updateNeighborsAt(paramBlockPos.below(), paramBlockState.getBlock(), orientation);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 177 */     changePowered((Level)paramServerLevel, paramBlockPos, paramBlockState, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 182 */     if (((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/* 183 */       updateBelow((Level)paramServerLevel, paramBlockPos, paramBlockState);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState paramBlockState) {
/* 189 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 194 */     return ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 199 */     return (paramDirection == Direction.UP && ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 204 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 209 */     if (((Boolean)paramBlockState.getValue((Property)HAS_BOOK)).booleanValue()) {
/* 210 */       BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 211 */       if (blockEntity instanceof LecternBlockEntity) {
/* 212 */         return ((LecternBlockEntity)blockEntity).getRedstoneSignal();
/*     */       }
/*     */     } 
/*     */     
/* 216 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/* 221 */     if (((Boolean)paramBlockState.getValue((Property)HAS_BOOK)).booleanValue()) {
/* 222 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/* 225 */     if (paramItemStack.is(ItemTags.LECTERN_BOOKS)) {
/* 226 */       return tryPlaceBook((LivingEntity)paramPlayer, paramLevel, paramBlockPos, paramBlockState, paramItemStack) ? (InteractionResult)InteractionResult.SUCCESS : (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 229 */     if (paramItemStack.isEmpty() && paramInteractionHand == InteractionHand.MAIN_HAND)
/*     */     {
/*     */       
/* 232 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 235 */     return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 240 */     if (((Boolean)paramBlockState.getValue((Property)HAS_BOOK)).booleanValue()) {
/* 241 */       if (!paramLevel.isClientSide()) {
/* 242 */         openScreen(paramLevel, paramBlockPos, paramPlayer);
/*     */       }
/* 244 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */ 
/*     */     
/* 248 */     return (InteractionResult)InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MenuProvider getMenuProvider(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 253 */     if (!((Boolean)paramBlockState.getValue((Property)HAS_BOOK)).booleanValue()) {
/* 254 */       return null;
/*     */     }
/*     */     
/* 257 */     return super.getMenuProvider(paramBlockState, paramLevel, paramBlockPos);
/*     */   }
/*     */   
/*     */   private void openScreen(Level paramLevel, BlockPos paramBlockPos, Player paramPlayer) {
/* 261 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 262 */     if (blockEntity instanceof LecternBlockEntity) {
/* 263 */       paramPlayer.openMenu((MenuProvider)blockEntity);
/* 264 */       paramPlayer.awardStat(Stats.INTERACT_WITH_LECTERN);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 270 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\LecternBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */