/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DoorHingeSide;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DoorBlock extends Block {
/*     */   static {
/*  44 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BlockSetType.CODEC.fieldOf("block_set_type").forGetter(DoorBlock::type), (App)propertiesCodec()).apply((Applicative)paramInstance, DoorBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<DoorBlock> CODEC;
/*     */   
/*     */   public MapCodec<? extends DoorBlock> codec() {
/*  51 */     return CODEC;
/*     */   }
/*     */   
/*  54 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  55 */   public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
/*  56 */   public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
/*  57 */   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
/*  58 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*  60 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(16.0D, 13.0D, 16.0D));
/*     */   
/*     */   private final BlockSetType type;
/*     */   
/*     */   protected DoorBlock(BlockSetType paramBlockSetType, BlockBehaviour.Properties paramProperties) {
/*  65 */     super(paramProperties.sound(paramBlockSetType.soundType()));
/*  66 */     this.type = paramBlockSetType;
/*  67 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)OPEN, Boolean.valueOf(false))).setValue((Property)HINGE, (Comparable)DoorHingeSide.LEFT)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)HALF, (Comparable)DoubleBlockHalf.LOWER));
/*     */   }
/*     */   
/*     */   public BlockSetType type() {
/*  71 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  76 */     Direction direction1 = (Direction)paramBlockState.getValue((Property)FACING);
/*  77 */     Direction direction2 = ((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue() ? ((paramBlockState.getValue((Property)HINGE) == DoorHingeSide.RIGHT) ? direction1.getCounterClockWise() : direction1.getClockWise()) : direction1;
/*     */     
/*  79 */     return SHAPES.get(direction2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  84 */     DoubleBlockHalf doubleBlockHalf = (DoubleBlockHalf)paramBlockState1.getValue((Property)HALF);
/*  85 */     if (paramDirection.getAxis() == Direction.Axis.Y) if (((doubleBlockHalf == DoubleBlockHalf.LOWER) ? true : false) == ((paramDirection == Direction.UP) ? true : false)) {
/*     */         
/*  87 */         if (paramBlockState2.getBlock() instanceof DoorBlock && paramBlockState2.getValue((Property)HALF) != doubleBlockHalf)
/*     */         {
/*  89 */           return (BlockState)paramBlockState2.setValue((Property)HALF, (Comparable)doubleBlockHalf);
/*     */         }
/*  91 */         return Blocks.AIR.defaultBlockState();
/*     */       } 
/*     */ 
/*     */     
/*  95 */     if (doubleBlockHalf == DoubleBlockHalf.LOWER && paramDirection == Direction.DOWN && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  96 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  99 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, Explosion paramExplosion, BiConsumer<ItemStack, BlockPos> paramBiConsumer) {
/* 104 */     if (paramExplosion.canTriggerBlocks() && paramBlockState.getValue((Property)HALF) == DoubleBlockHalf.LOWER && this.type.canOpenByWindCharge() && !((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/* 105 */       setOpen((Entity)null, (Level)paramServerLevel, paramBlockState, paramBlockPos, !isOpen(paramBlockState));
/*     */     }
/* 107 */     super.onExplosionHit(paramBlockState, paramServerLevel, paramBlockPos, paramExplosion, paramBiConsumer);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Player paramPlayer) {
/* 112 */     if (!paramLevel.isClientSide() && (paramPlayer.preventsBlockDrops() || !paramPlayer.hasCorrectToolForDrops(paramBlockState))) {
/* 113 */       DoublePlantBlock.preventDropFromBottomPart(paramLevel, paramBlockPos, paramBlockState, paramPlayer);
/*     */     }
/*     */     
/* 116 */     return super.playerWillDestroy(paramLevel, paramBlockPos, paramBlockState, paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 121 */     switch (paramPathComputationType) { default: throw new MatchException(null, null);case LAND: case AIR: case WATER: break; }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 129 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/* 130 */     Level level = paramBlockPlaceContext.getLevel();
/* 131 */     if (blockPos.getY() < level.getMaxY() && level.getBlockState(blockPos.above()).canBeReplaced(paramBlockPlaceContext)) {
/* 132 */       boolean bool = (level.hasNeighborSignal(blockPos) || level.hasNeighborSignal(blockPos.above())) ? true : false;
/*     */       
/* 134 */       return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection())).setValue((Property)HINGE, (Comparable)getHinge(paramBlockPlaceContext))).setValue((Property)POWERED, Boolean.valueOf(bool))).setValue((Property)OPEN, Boolean.valueOf(bool))).setValue((Property)HALF, (Comparable)DoubleBlockHalf.LOWER);
/*     */     } 
/*     */     
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/* 142 */     paramLevel.setBlock(paramBlockPos.above(), (BlockState)paramBlockState.setValue((Property)HALF, (Comparable)DoubleBlockHalf.UPPER), 3);
/*     */   }
/*     */   
/*     */   private DoorHingeSide getHinge(BlockPlaceContext paramBlockPlaceContext) {
/* 146 */     Level level = paramBlockPlaceContext.getLevel();
/* 147 */     BlockPos blockPos1 = paramBlockPlaceContext.getClickedPos();
/* 148 */     Direction direction1 = paramBlockPlaceContext.getHorizontalDirection();
/* 149 */     BlockPos blockPos2 = blockPos1.above();
/*     */     
/* 151 */     Direction direction2 = direction1.getCounterClockWise();
/* 152 */     BlockPos blockPos3 = blockPos1.relative(direction2);
/* 153 */     BlockState blockState1 = level.getBlockState(blockPos3);
/* 154 */     BlockPos blockPos4 = blockPos2.relative(direction2);
/* 155 */     BlockState blockState2 = level.getBlockState(blockPos4);
/*     */     
/* 157 */     Direction direction3 = direction1.getClockWise();
/* 158 */     BlockPos blockPos5 = blockPos1.relative(direction3);
/* 159 */     BlockState blockState3 = level.getBlockState(blockPos5);
/* 160 */     BlockPos blockPos6 = blockPos2.relative(direction3);
/* 161 */     BlockState blockState4 = level.getBlockState(blockPos6);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     int i = (blockState1.isCollisionShapeFullBlock((BlockGetter)level, blockPos3) ? -1 : 0) + (blockState2.isCollisionShapeFullBlock((BlockGetter)level, blockPos4) ? -1 : 0) + (blockState3.isCollisionShapeFullBlock((BlockGetter)level, blockPos5) ? 1 : 0) + (blockState4.isCollisionShapeFullBlock((BlockGetter)level, blockPos6) ? 1 : 0);
/*     */     
/* 168 */     boolean bool1 = (blockState1.getBlock() instanceof DoorBlock && blockState1.getValue((Property)HALF) == DoubleBlockHalf.LOWER) ? true : false;
/* 169 */     boolean bool2 = (blockState3.getBlock() instanceof DoorBlock && blockState3.getValue((Property)HALF) == DoubleBlockHalf.LOWER) ? true : false;
/*     */     
/* 171 */     if ((bool1 && !bool2) || i > 0) {
/* 172 */       return DoorHingeSide.RIGHT;
/*     */     }
/* 174 */     if ((bool2 && !bool1) || i < 0) {
/* 175 */       return DoorHingeSide.LEFT;
/*     */     }
/*     */     
/* 178 */     int j = direction1.getStepX();
/* 179 */     int k = direction1.getStepZ();
/*     */     
/* 181 */     Vec3 vec3 = paramBlockPlaceContext.getClickLocation();
/* 182 */     double d1 = vec3.x - blockPos1.getX();
/* 183 */     double d2 = vec3.z - blockPos1.getZ();
/*     */     
/* 185 */     return ((j < 0 && d2 < 0.5D) || (j > 0 && d2 > 0.5D) || (k < 0 && d1 > 0.5D) || (k > 0 && d1 < 0.5D)) ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 190 */     if (!this.type.canOpenByHand()) {
/* 191 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 194 */     paramBlockState = (BlockState)paramBlockState.cycle((Property)OPEN);
/* 195 */     paramLevel.setBlock(paramBlockPos, paramBlockState, 10);
/* 196 */     playSound((Entity)paramPlayer, paramLevel, paramBlockPos, ((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue());
/* 197 */     paramLevel.gameEvent((Entity)paramPlayer, isOpen(paramBlockState) ? (Holder)GameEvent.BLOCK_OPEN : (Holder)GameEvent.BLOCK_CLOSE, paramBlockPos);
/* 198 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen(BlockState paramBlockState) {
/* 206 */     return ((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setOpen(Entity paramEntity, Level paramLevel, BlockState paramBlockState, BlockPos paramBlockPos, boolean paramBoolean) {
/* 210 */     if (!paramBlockState.is(this) || ((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue() == paramBoolean) {
/*     */       return;
/*     */     }
/*     */     
/* 214 */     paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)OPEN, Boolean.valueOf(paramBoolean)), 10);
/* 215 */     playSound(paramEntity, paramLevel, paramBlockPos, paramBoolean);
/* 216 */     paramLevel.gameEvent(paramEntity, paramBoolean ? (Holder)GameEvent.BLOCK_OPEN : (Holder)GameEvent.BLOCK_CLOSE, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 221 */     boolean bool = (paramLevel.hasNeighborSignal(paramBlockPos) || paramLevel.hasNeighborSignal(paramBlockPos.relative((paramBlockState.getValue((Property)HALF) == DoubleBlockHalf.LOWER) ? Direction.UP : Direction.DOWN)));
/* 222 */     if (!defaultBlockState().is(paramBlock) && bool != ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/* 223 */       if (bool != ((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue()) {
/* 224 */         playSound((Entity)null, paramLevel, paramBlockPos, bool);
/* 225 */         paramLevel.gameEvent(null, bool ? (Holder)GameEvent.BLOCK_OPEN : (Holder)GameEvent.BLOCK_CLOSE, paramBlockPos);
/*     */       } 
/* 227 */       paramLevel.setBlock(paramBlockPos, (BlockState)((BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(bool))).setValue((Property)OPEN, Boolean.valueOf(bool)), 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 234 */     BlockPos blockPos = paramBlockPos.below();
/* 235 */     BlockState blockState = paramLevelReader.getBlockState(blockPos);
/* 236 */     if (paramBlockState.getValue((Property)HALF) == DoubleBlockHalf.LOWER) {
/* 237 */       return blockState.isFaceSturdy((BlockGetter)paramLevelReader, blockPos, Direction.UP);
/*     */     }
/* 239 */     return blockState.is(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private void playSound(Entity paramEntity, Level paramLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 244 */     paramLevel.playSound(paramEntity, paramBlockPos, paramBoolean ? this.type.doorOpen() : this.type.doorClose(), SoundSource.BLOCKS, 1.0F, paramLevel.getRandom().nextFloat() * 0.1F + 0.9F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 249 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 254 */     if (paramMirror == Mirror.NONE) {
/* 255 */       return paramBlockState;
/*     */     }
/* 257 */     return (BlockState)paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING))).cycle((Property)HINGE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long getSeed(BlockState paramBlockState, BlockPos paramBlockPos) {
/* 262 */     return Mth.getSeed(paramBlockPos.getX(), paramBlockPos.below((paramBlockState.getValue((Property)HALF) == DoubleBlockHalf.LOWER) ? 0 : 1).getY(), paramBlockPos.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 267 */     paramBuilder.add(new Property[] { (Property)HALF, (Property)FACING, (Property)OPEN, (Property)HINGE, (Property)POWERED });
/*     */   }
/*     */   
/*     */   public static boolean isWoodenDoor(Level paramLevel, BlockPos paramBlockPos) {
/* 271 */     return isWoodenDoor(paramLevel.getBlockState(paramBlockPos));
/*     */   }
/*     */   
/*     */   public static boolean isWoodenDoor(BlockState paramBlockState) {
/* 275 */     Block block = paramBlockState.getBlock(); if (block instanceof DoorBlock) { DoorBlock doorBlock = (DoorBlock)block; if (doorBlock.type().canOpenByHand()); }  return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\DoorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */