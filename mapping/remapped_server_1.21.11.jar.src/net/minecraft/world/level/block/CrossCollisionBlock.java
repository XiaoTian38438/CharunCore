/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class CrossCollisionBlock extends Block implements SimpleWaterloggedBlock {
/*  22 */   public static final BooleanProperty NORTH = PipeBlock.NORTH;
/*  23 */   public static final BooleanProperty EAST = PipeBlock.EAST;
/*  24 */   public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
/*  25 */   public static final BooleanProperty WEST = PipeBlock.WEST;
/*  26 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED; public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION; private final Function<BlockState, VoxelShape> collisionShapes; private final Function<BlockState, VoxelShape> shapes; static {
/*  27 */     PROPERTY_BY_DIRECTION = (Map<Direction, BooleanProperty>)PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(paramEntry -> ((Direction)paramEntry.getKey()).getAxis().isHorizontal()).collect(Util.toMap());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected CrossCollisionBlock(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, BlockBehaviour.Properties paramProperties) {
/*  33 */     super(paramProperties);
/*     */     
/*  35 */     this.collisionShapes = makeShapes(paramFloat1, paramFloat5, paramFloat3, 0.0F, paramFloat5);
/*  36 */     this.shapes = makeShapes(paramFloat1, paramFloat2, paramFloat3, 0.0F, paramFloat4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Function<BlockState, VoxelShape> makeShapes(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
/*  43 */     VoxelShape voxelShape = Block.column(paramFloat1, 0.0D, paramFloat2);
/*  44 */     Map map = Shapes.rotateHorizontal(Block.boxZ(paramFloat3, paramFloat4, paramFloat5, 0.0D, 8.0D));
/*     */     
/*  46 */     return getShapeForEachState(paramBlockState -> { VoxelShape voxelShape = paramVoxelShape; for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) { if (((Boolean)paramBlockState.getValue((Property)entry.getValue())).booleanValue()) voxelShape = Shapes.or(voxelShape, (VoxelShape)paramMap.get(entry.getKey()));  }  return voxelShape; }(Property<?>[])new Property[] { (Property)WATERLOGGED });
/*     */   }
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
/*     */   protected boolean propagatesSkylightDown(BlockState paramBlockState) {
/*  60 */     return !((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  65 */     return this.shapes.apply(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  70 */     return this.collisionShapes.apply(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/*  75 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  76 */       return Fluids.WATER.getSource(false);
/*     */     }
/*  78 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/*  88 */     switch (paramRotation) {
/*     */       case LEFT_RIGHT:
/*  90 */         return (BlockState)((BlockState)((BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)SOUTH))).setValue((Property)EAST, paramBlockState.getValue((Property)WEST))).setValue((Property)SOUTH, paramBlockState.getValue((Property)NORTH))).setValue((Property)WEST, paramBlockState.getValue((Property)EAST));
/*     */       case FRONT_BACK:
/*  92 */         return (BlockState)((BlockState)((BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)EAST))).setValue((Property)EAST, paramBlockState.getValue((Property)SOUTH))).setValue((Property)SOUTH, paramBlockState.getValue((Property)WEST))).setValue((Property)WEST, paramBlockState.getValue((Property)NORTH));
/*     */       case null:
/*  94 */         return (BlockState)((BlockState)((BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)WEST))).setValue((Property)EAST, paramBlockState.getValue((Property)NORTH))).setValue((Property)SOUTH, paramBlockState.getValue((Property)EAST))).setValue((Property)WEST, paramBlockState.getValue((Property)SOUTH));
/*     */     } 
/*  96 */     return paramBlockState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 102 */     switch (paramMirror) {
/*     */       case LEFT_RIGHT:
/* 104 */         return (BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)SOUTH))).setValue((Property)SOUTH, paramBlockState.getValue((Property)NORTH));
/*     */       case FRONT_BACK:
/* 106 */         return (BlockState)((BlockState)paramBlockState.setValue((Property)EAST, paramBlockState.getValue((Property)WEST))).setValue((Property)WEST, paramBlockState.getValue((Property)EAST));
/*     */     } 
/*     */ 
/*     */     
/* 110 */     return super.mirror(paramBlockState, paramMirror);
/*     */   }
/*     */   
/*     */   protected abstract MapCodec<? extends CrossCollisionBlock> codec();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CrossCollisionBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */