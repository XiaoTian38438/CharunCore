/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.HopperBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class HopperBlock extends BaseEntityBlock {
/*  41 */   public static final MapCodec<HopperBlock> CODEC = simpleCodec(HopperBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<HopperBlock> codec() {
/*  45 */     return CODEC;
/*     */   }
/*     */   
/*  48 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING_HOPPER;
/*  49 */   public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   private final Map<Direction, VoxelShape> interactionShapes;
/*     */   
/*     */   public HopperBlock(BlockBehaviour.Properties paramProperties) {
/*  55 */     super(paramProperties);
/*  56 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.DOWN)).setValue((Property)ENABLED, Boolean.valueOf(true)));
/*     */     
/*  58 */     VoxelShape voxelShape = Block.column(12.0D, 11.0D, 16.0D);
/*  59 */     this.shapes = makeShapes(voxelShape);
/*  60 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  66 */       .interactionShapes = (Map<Direction, VoxelShape>)ImmutableMap.builderWithExpectedSize(5).putAll(Shapes.rotateHorizontal(Shapes.or(voxelShape, Block.boxZ(4.0D, 8.0D, 10.0D, 0.0D, 4.0D)))).put(Direction.DOWN, voxelShape).build();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes(VoxelShape paramVoxelShape) {
/*  70 */     VoxelShape voxelShape1 = Shapes.or(
/*  71 */         Block.column(16.0D, 10.0D, 16.0D), 
/*  72 */         Block.column(8.0D, 4.0D, 10.0D));
/*     */     
/*  74 */     VoxelShape voxelShape2 = Shapes.join(voxelShape1, paramVoxelShape, BooleanOp.ONLY_FIRST);
/*     */     
/*  76 */     Map map = Shapes.rotateAll(Block.boxZ(4.0D, 4.0D, 8.0D, 0.0D, 8.0D), (new Vec3(8.0D, 6.0D, 8.0D)).scale(0.0625D));
/*     */     
/*  78 */     return getShapeForEachState(paramBlockState -> Shapes.or(paramVoxelShape, Shapes.join((VoxelShape)paramMap.get(paramBlockState.getValue((Property)FACING)), Shapes.block(), BooleanOp.AND)), (Property<?>[])new Property[] { (Property)ENABLED });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  87 */     return this.shapes.apply(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getInteractionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  92 */     return this.interactionShapes.get(paramBlockState.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  97 */     Direction direction = paramBlockPlaceContext.getClickedFace().getOpposite();
/*  98 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (direction.getAxis() == Direction.Axis.Y) ? (Comparable)Direction.DOWN : (Comparable)direction)).setValue((Property)ENABLED, Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 103 */     return (BlockEntity)new HopperBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 108 */     return paramLevel.isClientSide() ? null : createTickerHelper(paramBlockEntityType, BlockEntityType.HOPPER, HopperBlockEntity::pushItemsTick);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 113 */     if (paramBlockState2.is(paramBlockState1.getBlock())) {
/*     */       return;
/*     */     }
/* 116 */     checkPoweredState(paramLevel, paramBlockPos, paramBlockState1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 121 */     if (!paramLevel.isClientSide()) { BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof HopperBlockEntity) { HopperBlockEntity hopperBlockEntity = (HopperBlockEntity)blockEntity;
/* 122 */         paramPlayer.openMenu((MenuProvider)hopperBlockEntity);
/* 123 */         paramPlayer.awardStat(Stats.INSPECT_HOPPER); }
/*     */        }
/* 125 */      return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 130 */     checkPoweredState(paramLevel, paramBlockPos, paramBlockState);
/*     */   }
/*     */   
/*     */   private void checkPoweredState(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 134 */     boolean bool = !paramLevel.hasNeighborSignal(paramBlockPos);
/* 135 */     if (bool != ((Boolean)paramBlockState.getValue((Property)ENABLED)).booleanValue()) {
/* 136 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)ENABLED, Boolean.valueOf(bool)), 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 142 */     Containers.updateNeighboursAfterDestroy(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 147 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 152 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(paramLevel.getBlockEntity(paramBlockPos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 157 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 162 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 167 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)ENABLED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/* 172 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 173 */     if (blockEntity instanceof HopperBlockEntity) {
/* 174 */       HopperBlockEntity.entityInside(paramLevel, paramBlockPos, paramBlockState, paramEntity, (HopperBlockEntity)blockEntity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 180 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\HopperBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */