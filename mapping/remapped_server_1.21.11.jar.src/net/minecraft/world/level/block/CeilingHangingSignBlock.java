/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.stream.Collectors;
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
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CeilingHangingSignBlock extends SignBlock {
/*     */   static {
/*  44 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)WoodType.CODEC.fieldOf("wood_type").forGetter(SignBlock::type), (App)propertiesCodec()).apply((Applicative)paramInstance, CeilingHangingSignBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<CeilingHangingSignBlock> CODEC;
/*     */   
/*     */   public MapCodec<CeilingHangingSignBlock> codec() {
/*  51 */     return CODEC;
/*     */   }
/*     */   
/*  54 */   public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
/*  55 */   public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;
/*     */   
/*  57 */   private static final VoxelShape SHAPE_DEFAULT = Block.column(10.0D, 0.0D, 16.0D); private static final Map<Integer, VoxelShape> SHAPES; static {
/*  58 */     SHAPES = (Map<Integer, VoxelShape>)Shapes.rotateHorizontal(Block.column(14.0D, 2.0D, 0.0D, 10.0D)).entrySet().stream().collect(Collectors.toMap(paramEntry -> Integer.valueOf(RotationSegment.convertToSegment((Direction)paramEntry.getKey())), Map.Entry::getValue));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CeilingHangingSignBlock(WoodType paramWoodType, BlockBehaviour.Properties paramProperties) {
/*  64 */     super(paramWoodType, paramProperties.sound(paramWoodType.hangingSignSoundType()));
/*  65 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ROTATION, Integer.valueOf(0))).setValue((Property)ATTACHED, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*  70 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof SignBlockEntity) { SignBlockEntity signBlockEntity = (SignBlockEntity)blockEntity;
/*  71 */       if (shouldTryToChainAnotherHangingSign(paramPlayer, paramBlockHitResult, signBlockEntity, paramItemStack)) {
/*  72 */         return (InteractionResult)InteractionResult.PASS;
/*     */       } }
/*     */     
/*  75 */     return super.useItemOn(paramItemStack, paramBlockState, paramLevel, paramBlockPos, paramPlayer, paramInteractionHand, paramBlockHitResult);
/*     */   }
/*     */   
/*     */   private boolean shouldTryToChainAnotherHangingSign(Player paramPlayer, BlockHitResult paramBlockHitResult, SignBlockEntity paramSignBlockEntity, ItemStack paramItemStack) {
/*  79 */     return (!paramSignBlockEntity.canExecuteClickCommands(paramSignBlockEntity.isFacingFrontText(paramPlayer), paramPlayer) && paramItemStack
/*  80 */       .getItem() instanceof net.minecraft.world.item.HangingSignItem && paramBlockHitResult.getDirection().equals(Direction.DOWN));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  85 */     return paramLevelReader.getBlockState(paramBlockPos.above()).isFaceSturdy((BlockGetter)paramLevelReader, paramBlockPos.above(), Direction.DOWN, SupportType.CENTER);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  90 */     Level level = paramBlockPlaceContext.getLevel();
/*  91 */     FluidState fluidState = level.getFluidState(paramBlockPlaceContext.getClickedPos());
/*  92 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos().above();
/*  93 */     BlockState blockState = level.getBlockState(blockPos);
/*  94 */     boolean bool = blockState.is(BlockTags.ALL_HANGING_SIGNS);
/*  95 */     Direction direction = Direction.fromYRot(paramBlockPlaceContext.getRotation());
/*  96 */     boolean bool1 = (!Block.isFaceFull(blockState.getCollisionShape((BlockGetter)level, blockPos), Direction.DOWN) || paramBlockPlaceContext.isSecondaryUseActive()) ? true : false;
/*     */     
/*  98 */     if (bool && !paramBlockPlaceContext.isSecondaryUseActive()) {
/*  99 */       if (blockState.hasProperty((Property)WallHangingSignBlock.FACING)) {
/* 100 */         Direction direction1 = (Direction)blockState.getValue((Property)WallHangingSignBlock.FACING);
/* 101 */         if (direction1.getAxis().test(direction)) {
/* 102 */           bool1 = false;
/*     */         }
/* 104 */       } else if (blockState.hasProperty((Property)ROTATION)) {
/* 105 */         Optional<Direction> optional = RotationSegment.convertToDirection(((Integer)blockState.getValue((Property)ROTATION)).intValue());
/* 106 */         if (optional.isPresent() && ((Direction)optional.get()).getAxis().test(direction)) {
/* 107 */           bool1 = false;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 112 */     int i = !bool1 ? RotationSegment.convertToSegment(direction.getOpposite()) : RotationSegment.convertToSegment(paramBlockPlaceContext.getRotation() + 180.0F);
/* 113 */     return (BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)ATTACHED, Boolean.valueOf(bool1))).setValue((Property)ROTATION, Integer.valueOf(i))).setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 118 */     return SHAPES.getOrDefault(paramBlockState.getValue((Property)ROTATION), SHAPE_DEFAULT);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getBlockSupportShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 123 */     return getShape(paramBlockState, paramBlockGetter, paramBlockPos, CollisionContext.empty());
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 128 */     if (paramDirection == Direction.UP && !canSurvive(paramBlockState1, paramLevelReader, paramBlockPos1)) {
/* 129 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/* 131 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getYRotationDegrees(BlockState paramBlockState) {
/* 136 */     return RotationSegment.convertToDegrees(((Integer)paramBlockState.getValue((Property)ROTATION)).intValue());
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 141 */     return (BlockState)paramBlockState.setValue((Property)ROTATION, Integer.valueOf(paramRotation.rotate(((Integer)paramBlockState.getValue((Property)ROTATION)).intValue(), 16)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 146 */     return (BlockState)paramBlockState.setValue((Property)ROTATION, Integer.valueOf(paramMirror.mirror(((Integer)paramBlockState.getValue((Property)ROTATION)).intValue(), 16)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 151 */     paramBuilder.add(new Property[] { (Property)ROTATION, (Property)ATTACHED, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 156 */     return (BlockEntity)new HangingSignBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 161 */     return createTickerHelper(paramBlockEntityType, BlockEntityType.HANGING_SIGN, SignBlockEntity::tick);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CeilingHangingSignBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */