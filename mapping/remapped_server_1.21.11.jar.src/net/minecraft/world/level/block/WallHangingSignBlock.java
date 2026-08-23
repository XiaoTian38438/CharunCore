/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WallHangingSignBlock extends SignBlock {
/*     */   static {
/*  40 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)WoodType.CODEC.fieldOf("wood_type").forGetter(SignBlock::type), (App)propertiesCodec()).apply((Applicative)paramInstance, WallHangingSignBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<WallHangingSignBlock> CODEC;
/*     */   
/*     */   public MapCodec<WallHangingSignBlock> codec() {
/*  47 */     return CODEC;
/*     */   }
/*     */   
/*  50 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  52 */   private static final Map<Direction.Axis, VoxelShape> SHAPES_PLANK = Shapes.rotateHorizontalAxis(Block.column(16.0D, 4.0D, 14.0D, 16.0D));
/*  53 */   private static final Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateHorizontalAxis(Shapes.or(SHAPES_PLANK
/*  54 */         .get(Direction.Axis.Z), 
/*  55 */         Block.column(14.0D, 2.0D, 0.0D, 10.0D)));
/*     */ 
/*     */   
/*     */   public WallHangingSignBlock(WoodType paramWoodType, BlockBehaviour.Properties paramProperties) {
/*  59 */     super(paramWoodType, paramProperties.sound(paramWoodType.hangingSignSoundType()));
/*  60 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*  65 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof SignBlockEntity) { SignBlockEntity signBlockEntity = (SignBlockEntity)blockEntity;
/*  66 */       if (shouldTryToChainAnotherHangingSign(paramBlockState, paramPlayer, paramBlockHitResult, signBlockEntity, paramItemStack)) {
/*  67 */         return (InteractionResult)InteractionResult.PASS;
/*     */       } }
/*     */     
/*  70 */     return super.useItemOn(paramItemStack, paramBlockState, paramLevel, paramBlockPos, paramPlayer, paramInteractionHand, paramBlockHitResult);
/*     */   }
/*     */   
/*     */   private boolean shouldTryToChainAnotherHangingSign(BlockState paramBlockState, Player paramPlayer, BlockHitResult paramBlockHitResult, SignBlockEntity paramSignBlockEntity, ItemStack paramItemStack) {
/*  74 */     return (!paramSignBlockEntity.canExecuteClickCommands(paramSignBlockEntity.isFacingFrontText(paramPlayer), paramPlayer) && paramItemStack
/*  75 */       .getItem() instanceof net.minecraft.world.item.HangingSignItem && !isHittingEditableSide(paramBlockHitResult, paramBlockState));
/*     */   }
/*     */   
/*     */   private boolean isHittingEditableSide(BlockHitResult paramBlockHitResult, BlockState paramBlockState) {
/*  79 */     return (paramBlockHitResult.getDirection().getAxis() == ((Direction)paramBlockState.getValue((Property)FACING)).getAxis());
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  84 */     return SHAPES.get(((Direction)paramBlockState.getValue((Property)FACING)).getAxis());
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getBlockSupportShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  89 */     return getShape(paramBlockState, paramBlockGetter, paramBlockPos, CollisionContext.empty());
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  94 */     return SHAPES_PLANK.get(((Direction)paramBlockState.getValue((Property)FACING)).getAxis());
/*     */   }
/*     */   
/*     */   public boolean canPlace(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  98 */     Direction direction1 = ((Direction)paramBlockState.getValue((Property)FACING)).getClockWise();
/*  99 */     Direction direction2 = ((Direction)paramBlockState.getValue((Property)FACING)).getCounterClockWise();
/*     */     
/* 101 */     return (canAttachTo(paramLevelReader, paramBlockState, paramBlockPos.relative(direction1), direction2) || canAttachTo(paramLevelReader, paramBlockState, paramBlockPos.relative(direction2), direction1));
/*     */   }
/*     */   
/*     */   public boolean canAttachTo(LevelReader paramLevelReader, BlockState paramBlockState, BlockPos paramBlockPos, Direction paramDirection) {
/* 105 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos);
/*     */ 
/*     */     
/* 108 */     if (blockState.is(BlockTags.WALL_HANGING_SIGNS)) {
/* 109 */       return ((Direction)blockState.getValue((Property)FACING)).getAxis().test((Direction)paramBlockState.getValue((Property)FACING));
/*     */     }
/*     */     
/* 112 */     return blockState.isFaceSturdy((BlockGetter)paramLevelReader, paramBlockPos, paramDirection, SupportType.FULL);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 117 */     BlockState blockState = defaultBlockState();
/* 118 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*     */     
/* 120 */     Level level = paramBlockPlaceContext.getLevel();
/* 121 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*     */     
/* 123 */     for (Direction direction : paramBlockPlaceContext.getNearestLookingDirections()) {
/* 124 */       if (direction.getAxis().isHorizontal() && !direction.getAxis().test(paramBlockPlaceContext.getClickedFace())) {
/*     */ 
/*     */ 
/*     */         
/* 128 */         Direction direction1 = direction.getOpposite();
/* 129 */         blockState = (BlockState)blockState.setValue((Property)FACING, (Comparable)direction1);
/* 130 */         if (blockState.canSurvive((LevelReader)level, blockPos) && canPlace(blockState, (LevelReader)level, blockPos)) {
/* 131 */           return (BlockState)blockState.setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/*     */         }
/*     */       } 
/*     */     } 
/* 135 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 140 */     if (paramDirection.getAxis() == ((Direction)paramBlockState1.getValue((Property)FACING)).getClockWise().getAxis() && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 141 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/* 143 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getYRotationDegrees(BlockState paramBlockState) {
/* 148 */     return ((Direction)paramBlockState.getValue((Property)FACING)).toYRot();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 153 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 158 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 163 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 168 */     return (BlockEntity)new HangingSignBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 178 */     return createTickerHelper(paramBlockEntityType, BlockEntityType.HANGING_SIGN, SignBlockEntity::tick);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WallHangingSignBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */