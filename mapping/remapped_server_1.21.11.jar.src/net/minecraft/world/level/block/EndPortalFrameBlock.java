/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
/*     */ import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class EndPortalFrameBlock extends Block {
/*  27 */   public static final MapCodec<EndPortalFrameBlock> CODEC = simpleCodec(EndPortalFrameBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<EndPortalFrameBlock> codec() {
/*  31 */     return CODEC;
/*     */   }
/*     */   
/*  34 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  35 */   public static final BooleanProperty HAS_EYE = BlockStateProperties.EYE;
/*     */   
/*  37 */   private static final VoxelShape SHAPE_EMPTY = Block.column(16.0D, 0.0D, 13.0D);
/*  38 */   private static final VoxelShape SHAPE_FULL = Shapes.or(SHAPE_EMPTY, 
/*     */       
/*  40 */       Block.column(8.0D, 13.0D, 16.0D));
/*     */   
/*     */   private static BlockPattern portalShape;
/*     */ 
/*     */   
/*     */   public EndPortalFrameBlock(BlockBehaviour.Properties paramProperties) {
/*  46 */     super(paramProperties);
/*  47 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)HAS_EYE, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState paramBlockState) {
/*  52 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  57 */     return ((Boolean)paramBlockState.getValue((Property)HAS_EYE)).booleanValue() ? SHAPE_FULL : SHAPE_EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  62 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite())).setValue((Property)HAS_EYE, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/*  67 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/*  72 */     if (((Boolean)paramBlockState.getValue((Property)HAS_EYE)).booleanValue()) {
/*  73 */       return 15;
/*     */     }
/*     */     
/*  76 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/*  81 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/*  86 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  91 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)HAS_EYE });
/*     */   }
/*     */   
/*     */   public static BlockPattern getOrCreatePortalShape() {
/*  95 */     if (portalShape == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 109 */       portalShape = BlockPatternBuilder.start().aisle(new String[] { "?vvv?", ">???<", ">???<", ">???<", "?^^^?" }).where('?', BlockInWorld.hasState(BlockStatePredicate.ANY)).where('^', BlockInWorld.hasState((Predicate)BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).where((Property)HAS_EYE, (Predicate)Predicates.equalTo(Boolean.valueOf(true))).where((Property)FACING, (Predicate)Predicates.equalTo(Direction.SOUTH)))).where('>', BlockInWorld.hasState((Predicate)BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).where((Property)HAS_EYE, (Predicate)Predicates.equalTo(Boolean.valueOf(true))).where((Property)FACING, (Predicate)Predicates.equalTo(Direction.WEST)))).where('v', BlockInWorld.hasState((Predicate)BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).where((Property)HAS_EYE, (Predicate)Predicates.equalTo(Boolean.valueOf(true))).where((Property)FACING, (Predicate)Predicates.equalTo(Direction.NORTH)))).where('<', BlockInWorld.hasState((Predicate)BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).where((Property)HAS_EYE, (Predicate)Predicates.equalTo(Boolean.valueOf(true))).where((Property)FACING, (Predicate)Predicates.equalTo(Direction.EAST)))).build();
/*     */     }
/* 111 */     return portalShape;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 116 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\EndPortalFrameBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */