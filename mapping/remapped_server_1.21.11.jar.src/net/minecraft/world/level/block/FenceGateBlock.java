/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FenceGateBlock extends HorizontalDirectionalBlock {
/*     */   static {
/*  41 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)WoodType.CODEC.fieldOf("wood_type").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, FenceGateBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<FenceGateBlock> CODEC;
/*     */   
/*     */   public MapCodec<FenceGateBlock> codec() {
/*  48 */     return CODEC;
/*     */   }
/*     */   
/*  51 */   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
/*  52 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  53 */   public static final BooleanProperty IN_WALL = BlockStateProperties.IN_WALL;
/*     */   
/*  55 */   private static final Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateHorizontalAxis(Block.cube(16.0D, 16.0D, 4.0D)); private static final Map<Direction.Axis, VoxelShape> SHAPES_WALL; static {
/*  56 */     SHAPES_WALL = Maps.newEnumMap(Util.mapValues(SHAPES, paramVoxelShape -> Shapes.join(paramVoxelShape, Block.column(16.0D, 13.0D, 16.0D), BooleanOp.ONLY_FIRST)));
/*     */   }
/*  58 */   private static final Map<Direction.Axis, VoxelShape> SHAPE_COLLISION = Shapes.rotateHorizontalAxis(Block.column(16.0D, 4.0D, 0.0D, 24.0D));
/*  59 */   private static final Map<Direction.Axis, VoxelShape> SHAPE_SUPPORT = Shapes.rotateHorizontalAxis(Block.column(16.0D, 4.0D, 5.0D, 24.0D));
/*     */   
/*  61 */   private static final Map<Direction.Axis, VoxelShape> SHAPE_OCCLUSION = Shapes.rotateHorizontalAxis(Shapes.or(
/*  62 */         Block.box(0.0D, 5.0D, 7.0D, 2.0D, 16.0D, 9.0D), 
/*  63 */         Block.box(14.0D, 5.0D, 7.0D, 16.0D, 16.0D, 9.0D))); private static final Map<Direction.Axis, VoxelShape> SHAPE_OCCLUSION_WALL; private final WoodType type;
/*     */   
/*     */   static {
/*  66 */     SHAPE_OCCLUSION_WALL = Maps.newEnumMap(Util.mapValues(SHAPE_OCCLUSION, paramVoxelShape -> paramVoxelShape.move(0.0D, -0.1875D, 0.0D).optimize()));
/*     */   }
/*     */ 
/*     */   
/*     */   public FenceGateBlock(WoodType paramWoodType, BlockBehaviour.Properties paramProperties) {
/*  71 */     super(paramProperties.sound(paramWoodType.soundType()));
/*  72 */     this.type = paramWoodType;
/*     */     
/*  74 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)OPEN, Boolean.valueOf(false))).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)IN_WALL, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  79 */     Direction.Axis axis = ((Direction)paramBlockState.getValue((Property)FACING)).getAxis();
/*  80 */     return (((Boolean)paramBlockState.getValue((Property)IN_WALL)).booleanValue() ? SHAPES_WALL : SHAPES).get(axis);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  85 */     Direction.Axis axis = paramDirection.getAxis();
/*  86 */     if (((Direction)paramBlockState1.getValue((Property)FACING)).getClockWise().getAxis() == axis) {
/*  87 */       boolean bool = (isWall(paramBlockState2) || isWall(paramLevelReader.getBlockState(paramBlockPos1.relative(paramDirection.getOpposite())))) ? true : false;
/*  88 */       return (BlockState)paramBlockState1.setValue((Property)IN_WALL, Boolean.valueOf(bool));
/*     */     } 
/*     */     
/*  91 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getBlockSupportShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  96 */     Direction.Axis axis = ((Direction)paramBlockState.getValue((Property)FACING)).getAxis();
/*     */     
/*  98 */     return ((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue() ? Shapes.empty() : SHAPE_SUPPORT.get(axis);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 103 */     Direction.Axis axis = ((Direction)paramBlockState.getValue((Property)FACING)).getAxis();
/* 104 */     return ((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue() ? Shapes.empty() : SHAPE_COLLISION.get(axis);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getOcclusionShape(BlockState paramBlockState) {
/* 109 */     Direction.Axis axis = ((Direction)paramBlockState.getValue((Property)FACING)).getAxis();
/* 110 */     return (((Boolean)paramBlockState.getValue((Property)IN_WALL)).booleanValue() ? SHAPE_OCCLUSION_WALL : SHAPE_OCCLUSION).get(axis);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 115 */     switch (paramPathComputationType) {
/*     */       case LAND:
/* 117 */         return ((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue();
/*     */       case WATER:
/* 119 */         return false;
/*     */       case AIR:
/* 121 */         return ((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue();
/*     */     } 
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 129 */     Level level = paramBlockPlaceContext.getLevel();
/* 130 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*     */     
/* 132 */     boolean bool = level.hasNeighborSignal(blockPos);
/* 133 */     Direction direction = paramBlockPlaceContext.getHorizontalDirection();
/*     */     
/* 135 */     Direction.Axis axis = direction.getAxis();
/*     */     
/* 137 */     boolean bool1 = ((axis == Direction.Axis.Z && (isWall(level.getBlockState(blockPos.west())) || isWall(level.getBlockState(blockPos.east())))) || (axis == Direction.Axis.X && (isWall(level.getBlockState(blockPos.north())) || isWall(level.getBlockState(blockPos.south()))))) ? true : false;
/* 138 */     return (BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)direction)).setValue((Property)OPEN, Boolean.valueOf(bool))).setValue((Property)POWERED, Boolean.valueOf(bool))).setValue((Property)IN_WALL, Boolean.valueOf(bool1));
/*     */   }
/*     */   
/*     */   private boolean isWall(BlockState paramBlockState) {
/* 142 */     return paramBlockState.is(BlockTags.WALLS);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 147 */     if (((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue()) {
/* 148 */       paramBlockState = (BlockState)paramBlockState.setValue((Property)OPEN, Boolean.valueOf(false));
/* 149 */       paramLevel.setBlock(paramBlockPos, paramBlockState, 10);
/*     */     } else {
/*     */       
/* 152 */       Direction direction = paramPlayer.getDirection();
/* 153 */       if (paramBlockState.getValue((Property)FACING) == direction.getOpposite()) {
/* 154 */         paramBlockState = (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)direction);
/*     */       }
/* 156 */       paramBlockState = (BlockState)paramBlockState.setValue((Property)OPEN, Boolean.valueOf(true));
/* 157 */       paramLevel.setBlock(paramBlockPos, paramBlockState, 10);
/*     */     } 
/*     */     
/* 160 */     boolean bool = ((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue();
/*     */     
/* 162 */     paramLevel.playSound((Entity)paramPlayer, paramBlockPos, bool ? this.type.fenceGateOpen() : this.type.fenceGateClose(), SoundSource.BLOCKS, 1.0F, paramLevel.getRandom().nextFloat() * 0.1F + 0.9F);
/* 163 */     paramLevel.gameEvent((Entity)paramPlayer, bool ? (Holder)GameEvent.BLOCK_OPEN : (Holder)GameEvent.BLOCK_CLOSE, paramBlockPos);
/* 164 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, Explosion paramExplosion, BiConsumer<ItemStack, BlockPos> paramBiConsumer) {
/* 169 */     if (paramExplosion.canTriggerBlocks() && !((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/* 170 */       boolean bool = ((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue();
/* 171 */       paramServerLevel.setBlockAndUpdate(paramBlockPos, (BlockState)paramBlockState.setValue((Property)OPEN, Boolean.valueOf(!bool)));
/*     */       
/* 173 */       paramServerLevel.playSound(null, paramBlockPos, bool ? this.type.fenceGateClose() : this.type.fenceGateOpen(), SoundSource.BLOCKS, 1.0F, paramServerLevel.getRandom().nextFloat() * 0.1F + 0.9F);
/* 174 */       paramServerLevel.gameEvent(bool ? (Holder)GameEvent.BLOCK_CLOSE : (Holder)GameEvent.BLOCK_OPEN, paramBlockPos, GameEvent.Context.of(paramBlockState));
/*     */     } 
/* 176 */     super.onExplosionHit(paramBlockState, paramServerLevel, paramBlockPos, paramExplosion, paramBiConsumer);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 181 */     if (paramLevel.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 185 */     boolean bool = paramLevel.hasNeighborSignal(paramBlockPos);
/* 186 */     if (((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() != bool) {
/* 187 */       paramLevel.setBlock(paramBlockPos, (BlockState)((BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(bool))).setValue((Property)OPEN, Boolean.valueOf(bool)), 2);
/* 188 */       if (((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue() != bool) {
/* 189 */         paramLevel.playSound(null, paramBlockPos, bool ? this.type.fenceGateOpen() : this.type.fenceGateClose(), SoundSource.BLOCKS, 1.0F, paramLevel.getRandom().nextFloat() * 0.1F + 0.9F);
/* 190 */         paramLevel.gameEvent(null, bool ? (Holder)GameEvent.BLOCK_OPEN : (Holder)GameEvent.BLOCK_CLOSE, paramBlockPos);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 197 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)OPEN, (Property)POWERED, (Property)IN_WALL });
/*     */   }
/*     */   
/*     */   public static boolean connectsToDirection(BlockState paramBlockState, Direction paramDirection) {
/* 201 */     return (((Direction)paramBlockState.getValue((Property)FACING)).getAxis() == paramDirection.getClockWise().getAxis());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\FenceGateBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */