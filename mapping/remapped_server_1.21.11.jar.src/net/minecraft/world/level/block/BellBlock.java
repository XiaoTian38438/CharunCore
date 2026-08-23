/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BellBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BellAttachType;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BellBlock extends BaseEntityBlock {
/*  45 */   public static final MapCodec<BellBlock> CODEC = simpleCodec(BellBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<BellBlock> codec() {
/*  49 */     return CODEC;
/*     */   }
/*     */   
/*  52 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  53 */   public static final EnumProperty<BellAttachType> ATTACHMENT = BlockStateProperties.BELL_ATTACHMENT;
/*  54 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*  56 */   private static final VoxelShape BELL_SHAPE = Shapes.or(
/*  57 */       Block.column(6.0D, 6.0D, 13.0D), 
/*  58 */       Block.column(8.0D, 4.0D, 6.0D));
/*     */ 
/*     */   
/*  61 */   private static final VoxelShape SHAPE_CEILING = Shapes.or(BELL_SHAPE, Block.column(2.0D, 13.0D, 16.0D));
/*  62 */   private static final Map<Direction.Axis, VoxelShape> SHAPE_FLOOR = Shapes.rotateHorizontalAxis(Block.cube(16.0D, 16.0D, 8.0D));
/*  63 */   private static final Map<Direction.Axis, VoxelShape> SHAPE_DOUBLE_WALL = Shapes.rotateHorizontalAxis(Shapes.or(BELL_SHAPE, Block.column(2.0D, 16.0D, 13.0D, 15.0D)));
/*  64 */   private static final Map<Direction, VoxelShape> SHAPE_SINGLE_WALL = Shapes.rotateHorizontal(Shapes.or(BELL_SHAPE, Block.boxZ(2.0D, 13.0D, 15.0D, 0.0D, 13.0D)));
/*     */   
/*     */   public static final int EVENT_BELL_RING = 1;
/*     */   
/*     */   public BellBlock(BlockBehaviour.Properties paramProperties) {
/*  69 */     super(paramProperties);
/*  70 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)ATTACHMENT, (Comparable)BellAttachType.FLOOR)).setValue((Property)POWERED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/*  75 */     boolean bool = paramLevel.hasNeighborSignal(paramBlockPos);
/*     */     
/*  77 */     if (bool != ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/*  78 */       if (bool) {
/*  79 */         attemptToRing(paramLevel, paramBlockPos, (Direction)null);
/*     */       }
/*  81 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(bool)), 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onProjectileHit(Level paramLevel, BlockState paramBlockState, BlockHitResult paramBlockHitResult, Projectile paramProjectile) {
/*  87 */     Entity entity = paramProjectile.getOwner();
/*  88 */     Player player2 = (Player)entity, player1 = (entity instanceof Player) ? player2 : null;
/*  89 */     onHit(paramLevel, paramBlockState, paramBlockHitResult, player1, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  94 */     return onHit(paramLevel, paramBlockState, paramBlockHitResult, paramPlayer, true) ? (InteractionResult)InteractionResult.SUCCESS : (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public boolean onHit(Level paramLevel, BlockState paramBlockState, BlockHitResult paramBlockHitResult, Player paramPlayer, boolean paramBoolean) {
/*  98 */     Direction direction = paramBlockHitResult.getDirection();
/*  99 */     BlockPos blockPos = paramBlockHitResult.getBlockPos();
/* 100 */     boolean bool = (!paramBoolean || isProperHit(paramBlockState, direction, (paramBlockHitResult.getLocation()).y - blockPos.getY())) ? true : false;
/* 101 */     if (bool) {
/* 102 */       boolean bool1 = attemptToRing((Entity)paramPlayer, paramLevel, blockPos, direction);
/* 103 */       if (bool1 && paramPlayer != null) {
/* 104 */         paramPlayer.awardStat(Stats.BELL_RING);
/*     */       }
/* 106 */       return true;
/*     */     } 
/* 108 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isProperHit(BlockState paramBlockState, Direction paramDirection, double paramDouble) {
/* 112 */     if (paramDirection.getAxis() == Direction.Axis.Y || paramDouble > 0.8123999834060669D) {
/* 113 */       return false;
/*     */     }
/*     */     
/* 116 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/* 117 */     BellAttachType bellAttachType = (BellAttachType)paramBlockState.getValue((Property)ATTACHMENT);
/*     */     
/* 119 */     switch (bellAttachType) {
/*     */       case FLOOR:
/* 121 */         return (direction.getAxis() == paramDirection.getAxis());
/*     */       case SINGLE_WALL:
/*     */       case DOUBLE_WALL:
/* 124 */         return (direction.getAxis() != paramDirection.getAxis());
/*     */       case CEILING:
/* 126 */         return true;
/*     */     } 
/* 128 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attemptToRing(Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 133 */     return attemptToRing((Entity)null, paramLevel, paramBlockPos, paramDirection);
/*     */   }
/*     */   
/*     */   public boolean attemptToRing(Entity paramEntity, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 137 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 138 */     if (!paramLevel.isClientSide() && blockEntity instanceof BellBlockEntity) {
/* 139 */       if (paramDirection == null) {
/* 140 */         paramDirection = (Direction)paramLevel.getBlockState(paramBlockPos).getValue((Property)FACING);
/*     */       }
/* 142 */       ((BellBlockEntity)blockEntity).onHit(paramDirection);
/* 143 */       paramLevel.playSound(null, paramBlockPos, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2.0F, 1.0F);
/* 144 */       paramLevel.gameEvent(paramEntity, (Holder)GameEvent.BLOCK_CHANGE, paramBlockPos);
/* 145 */       return true;
/*     */     } 
/* 147 */     return false;
/*     */   }
/*     */   
/*     */   private VoxelShape getVoxelShape(BlockState paramBlockState) {
/* 151 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/* 152 */     switch ((BellAttachType)paramBlockState.getValue((Property)ATTACHMENT)) { default: throw new MatchException(null, null);case FLOOR: case CEILING: case SINGLE_WALL: case DOUBLE_WALL: break; }  return 
/*     */ 
/*     */ 
/*     */       
/* 156 */       SHAPE_DOUBLE_WALL.get(direction.getAxis());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 162 */     return getVoxelShape(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 167 */     return getVoxelShape(paramBlockState);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 173 */     Direction direction = paramBlockPlaceContext.getClickedFace();
/* 174 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/* 175 */     Level level = paramBlockPlaceContext.getLevel();
/* 176 */     Direction.Axis axis = direction.getAxis();
/*     */     
/* 178 */     if (axis == Direction.Axis.Y) {
/* 179 */       BlockState blockState = (BlockState)((BlockState)defaultBlockState().setValue((Property)ATTACHMENT, (direction == Direction.DOWN) ? (Comparable)BellAttachType.CEILING : (Comparable)BellAttachType.FLOOR)).setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection());
/*     */       
/* 181 */       if (blockState.canSurvive((LevelReader)paramBlockPlaceContext.getLevel(), blockPos)) {
/* 182 */         return blockState;
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 187 */       boolean bool = ((axis == Direction.Axis.X && level.getBlockState(blockPos.west()).isFaceSturdy((BlockGetter)level, blockPos.west(), Direction.EAST) && level.getBlockState(blockPos.east()).isFaceSturdy((BlockGetter)level, blockPos.east(), Direction.WEST)) || (axis == Direction.Axis.Z && level.getBlockState(blockPos.north()).isFaceSturdy((BlockGetter)level, blockPos.north(), Direction.SOUTH) && level.getBlockState(blockPos.south()).isFaceSturdy((BlockGetter)level, blockPos.south(), Direction.NORTH))) ? true : false;
/*     */       
/* 189 */       BlockState blockState = (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)direction.getOpposite())).setValue((Property)ATTACHMENT, bool ? (Comparable)BellAttachType.DOUBLE_WALL : (Comparable)BellAttachType.SINGLE_WALL);
/*     */       
/* 191 */       if (blockState.canSurvive((LevelReader)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos())) {
/* 192 */         return blockState;
/*     */       }
/* 194 */       boolean bool1 = level.getBlockState(blockPos.below()).isFaceSturdy((BlockGetter)level, blockPos.below(), Direction.UP);
/*     */       
/* 196 */       blockState = (BlockState)blockState.setValue((Property)ATTACHMENT, bool1 ? (Comparable)BellAttachType.FLOOR : (Comparable)BellAttachType.CEILING);
/*     */       
/* 198 */       if (blockState.canSurvive((LevelReader)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos())) {
/* 199 */         return blockState;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 204 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, Explosion paramExplosion, BiConsumer<ItemStack, BlockPos> paramBiConsumer) {
/* 209 */     if (paramExplosion.canTriggerBlocks()) {
/* 210 */       attemptToRing((Level)paramServerLevel, paramBlockPos, (Direction)null);
/*     */     }
/* 212 */     super.onExplosionHit(paramBlockState, paramServerLevel, paramBlockPos, paramExplosion, paramBiConsumer);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 217 */     BellAttachType bellAttachType = (BellAttachType)paramBlockState1.getValue((Property)ATTACHMENT);
/*     */     
/* 219 */     Direction direction = getConnectedDirection(paramBlockState1).getOpposite();
/* 220 */     if (direction == paramDirection && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1) && bellAttachType != BellAttachType.DOUBLE_WALL) {
/* 221 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 224 */     if (paramDirection.getAxis() == ((Direction)paramBlockState1.getValue((Property)FACING)).getAxis()) {
/* 225 */       if (bellAttachType == BellAttachType.DOUBLE_WALL && !paramBlockState2.isFaceSturdy((BlockGetter)paramLevelReader, paramBlockPos2, paramDirection))
/* 226 */         return (BlockState)((BlockState)paramBlockState1.setValue((Property)ATTACHMENT, (Comparable)BellAttachType.SINGLE_WALL)).setValue((Property)FACING, (Comparable)paramDirection.getOpposite()); 
/* 227 */       if (bellAttachType == BellAttachType.SINGLE_WALL && direction.getOpposite() == paramDirection && paramBlockState2.isFaceSturdy((BlockGetter)paramLevelReader, paramBlockPos2, (Direction)paramBlockState1.getValue((Property)FACING))) {
/* 228 */         return (BlockState)paramBlockState1.setValue((Property)ATTACHMENT, (Comparable)BellAttachType.DOUBLE_WALL);
/*     */       }
/*     */     } 
/*     */     
/* 232 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 237 */     Direction direction = getConnectedDirection(paramBlockState).getOpposite();
/*     */     
/* 239 */     if (direction == Direction.UP) {
/* 240 */       return Block.canSupportCenter(paramLevelReader, paramBlockPos.above(), Direction.DOWN);
/*     */     }
/* 242 */     return FaceAttachedHorizontalDirectionalBlock.canAttach(paramLevelReader, paramBlockPos, direction);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Direction getConnectedDirection(BlockState paramBlockState) {
/* 247 */     switch ((BellAttachType)paramBlockState.getValue((Property)ATTACHMENT)) {
/*     */       case CEILING:
/* 249 */         return Direction.DOWN;
/*     */       case FLOOR:
/* 251 */         return Direction.UP;
/*     */     } 
/* 253 */     return ((Direction)paramBlockState.getValue((Property)FACING)).getOpposite();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 259 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)ATTACHMENT, (Property)POWERED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 264 */     return (BlockEntity)new BellBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 269 */     return createTickerHelper(paramBlockEntityType, BlockEntityType.BELL, paramLevel.isClientSide() ? BellBlockEntity::clientTick : BellBlockEntity::serverTick);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 274 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 279 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 284 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BellBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */