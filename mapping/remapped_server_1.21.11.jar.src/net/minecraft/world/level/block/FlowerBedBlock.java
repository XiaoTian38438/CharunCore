/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FlowerBedBlock extends VegetationBlock implements BonemealableBlock, SegmentableBlock {
/*  24 */   public static final MapCodec<FlowerBedBlock> CODEC = simpleCodec(FlowerBedBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<FlowerBedBlock> codec() {
/*  28 */     return CODEC;
/*     */   }
/*     */   
/*  31 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*  32 */   public static final IntegerProperty AMOUNT = BlockStateProperties.FLOWER_AMOUNT;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   protected FlowerBedBlock(BlockBehaviour.Properties paramProperties) {
/*  37 */     super(paramProperties);
/*  38 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)AMOUNT, Integer.valueOf(1)));
/*     */     
/*  40 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  44 */     return getShapeForEachState(getShapeCalculator(FACING, AMOUNT));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/*  49 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/*  54 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeReplaced(BlockState paramBlockState, BlockPlaceContext paramBlockPlaceContext) {
/*  59 */     if (canBeReplaced(paramBlockState, paramBlockPlaceContext, AMOUNT)) {
/*  60 */       return true;
/*     */     }
/*  62 */     return super.canBeReplaced(paramBlockState, paramBlockPlaceContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  67 */     return this.shapes.apply(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getShapeHeight() {
/*  72 */     return 3.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public IntegerProperty getSegmentAmountProperty() {
/*  77 */     return AMOUNT;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  82 */     return getStateForPlacement(paramBlockPlaceContext, this, AMOUNT, FACING);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  87 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)AMOUNT });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  97 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 102 */     int i = ((Integer)paramBlockState.getValue((Property)AMOUNT)).intValue();
/* 103 */     if (i < 4) {
/* 104 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)AMOUNT, Integer.valueOf(i + 1)), 2);
/*     */     } else {
/* 106 */       popResource((Level)paramServerLevel, paramBlockPos, new ItemStack(this));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\FlowerBedBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */