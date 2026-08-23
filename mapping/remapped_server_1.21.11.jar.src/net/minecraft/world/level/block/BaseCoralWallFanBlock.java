/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
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
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BaseCoralWallFanBlock extends BaseCoralFanBlock {
/*  23 */   public static final MapCodec<BaseCoralWallFanBlock> CODEC = simpleCodec(BaseCoralWallFanBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<? extends BaseCoralWallFanBlock> codec() {
/*  27 */     return CODEC;
/*     */   }
/*     */   
/*  30 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  32 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(16.0D, 8.0D, 5.0D, 16.0D));
/*     */   
/*     */   protected BaseCoralWallFanBlock(BlockBehaviour.Properties paramProperties) {
/*  35 */     super(paramProperties);
/*  36 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)WATERLOGGED, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  41 */     return SHAPES.get(paramBlockState.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/*  46 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/*  51 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  56 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  61 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  62 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*     */     
/*  65 */     if (paramDirection.getOpposite() == paramBlockState1.getValue((Property)FACING) && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  66 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  69 */     return paramBlockState1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  74 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/*  75 */     BlockPos blockPos = paramBlockPos.relative(direction.getOpposite());
/*  76 */     BlockState blockState = paramLevelReader.getBlockState(blockPos);
/*     */     
/*  78 */     return blockState.isFaceSturdy((BlockGetter)paramLevelReader, blockPos, direction);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  83 */     BlockState blockState = super.getStateForPlacement(paramBlockPlaceContext);
/*     */     
/*  85 */     Level level = paramBlockPlaceContext.getLevel();
/*  86 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*     */     
/*  88 */     Direction[] arrayOfDirection = paramBlockPlaceContext.getNearestLookingDirections();
/*  89 */     for (Direction direction : arrayOfDirection) {
/*  90 */       if (direction.getAxis().isHorizontal()) {
/*     */ 
/*     */ 
/*     */         
/*  94 */         blockState = (BlockState)blockState.setValue((Property)FACING, (Comparable)direction.getOpposite());
/*  95 */         if (blockState.canSurvive((LevelReader)level, blockPos)) {
/*  96 */           return blockState;
/*     */         }
/*     */       } 
/*     */     } 
/* 100 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BaseCoralWallFanBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */