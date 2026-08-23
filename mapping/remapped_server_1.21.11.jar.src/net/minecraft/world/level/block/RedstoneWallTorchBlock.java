/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class RedstoneWallTorchBlock extends RedstoneTorchBlock {
/*  25 */   public static final MapCodec<RedstoneWallTorchBlock> CODEC = simpleCodec(RedstoneWallTorchBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<RedstoneWallTorchBlock> codec() {
/*  29 */     return CODEC;
/*     */   }
/*     */   
/*  32 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  33 */   public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
/*     */   
/*     */   protected RedstoneWallTorchBlock(BlockBehaviour.Properties paramProperties) {
/*  36 */     super(paramProperties);
/*  37 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)LIT, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  42 */     return WallTorchBlock.getShape(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  47 */     return WallTorchBlock.canSurvive(paramLevelReader, paramBlockPos, (Direction)paramBlockState.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  52 */     if (paramDirection.getOpposite() == paramBlockState1.getValue((Property)FACING) && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  53 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  55 */     return paramBlockState1;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  60 */     BlockState blockState = Blocks.WALL_TORCH.getStateForPlacement(paramBlockPlaceContext);
/*  61 */     return (blockState == null) ? null : (BlockState)defaultBlockState().setValue((Property)FACING, blockState.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  66 */     if (!((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  70 */     Direction direction = ((Direction)paramBlockState.getValue((Property)FACING)).getOpposite();
/*  71 */     double d1 = 0.27D;
/*  72 */     double d2 = paramBlockPos.getX() + 0.5D + (paramRandomSource.nextDouble() - 0.5D) * 0.2D + 0.27D * direction.getStepX();
/*  73 */     double d3 = paramBlockPos.getY() + 0.7D + (paramRandomSource.nextDouble() - 0.5D) * 0.2D + 0.22D;
/*  74 */     double d4 = paramBlockPos.getZ() + 0.5D + (paramRandomSource.nextDouble() - 0.5D) * 0.2D + 0.27D * direction.getStepZ();
/*     */     
/*  76 */     paramLevel.addParticle((ParticleOptions)DustParticleOptions.REDSTONE, d2, d3, d4, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasNeighborSignal(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  81 */     Direction direction = ((Direction)paramBlockState.getValue((Property)FACING)).getOpposite();
/*     */     
/*  83 */     return paramLevel.hasSignal(paramBlockPos.relative(direction), direction);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/*  88 */     if (((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue() && paramBlockState.getValue((Property)FACING) != paramDirection) {
/*  89 */       return 15;
/*     */     }
/*     */     
/*  92 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/*  97 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 102 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 107 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)LIT });
/*     */   }
/*     */ 
/*     */   
/*     */   protected Orientation randomOrientation(Level paramLevel, BlockState paramBlockState) {
/* 112 */     return ExperimentalRedstoneUtils.initialOrientation(paramLevel, ((Direction)paramBlockState.getValue((Property)FACING)).getOpposite(), Direction.UP);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\RedstoneWallTorchBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */