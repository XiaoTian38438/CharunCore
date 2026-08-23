/*    */ package net.minecraft.world.level.block;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public abstract class PipeBlock extends Block {
/* 20 */   public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
/* 21 */   public static final BooleanProperty EAST = BlockStateProperties.EAST;
/* 22 */   public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
/* 23 */   public static final BooleanProperty WEST = BlockStateProperties.WEST;
/* 24 */   public static final BooleanProperty UP = BlockStateProperties.UP;
/* 25 */   public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
/*    */   
/* 27 */   public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = (Map<Direction, BooleanProperty>)ImmutableMap.copyOf(Maps.newEnumMap(Map.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST, Direction.UP, UP, Direction.DOWN, DOWN)));
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final Function<BlockState, VoxelShape> shapes;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected PipeBlock(float paramFloat, BlockBehaviour.Properties paramProperties) {
/* 39 */     super(paramProperties);
/*    */     
/* 41 */     this.shapes = makeShapes(paramFloat);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Function<BlockState, VoxelShape> makeShapes(float paramFloat) {
/* 48 */     VoxelShape voxelShape = Block.cube(paramFloat);
/* 49 */     Map map = Shapes.rotateAll(Block.boxZ(paramFloat, 0.0D, 8.0D));
/*    */     
/* 51 */     return getShapeForEachState(paramBlockState -> {
/*    */           VoxelShape voxelShape = paramVoxelShape;
/*    */           for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
/*    */             if (((Boolean)paramBlockState.getValue((Property)entry.getValue())).booleanValue()) {
/*    */               voxelShape = Shapes.or((VoxelShape)paramMap.get(entry.getKey()), voxelShape);
/*    */             }
/*    */           } 
/*    */           return voxelShape;
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean propagatesSkylightDown(BlockState paramBlockState) {
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 69 */     return this.shapes.apply(paramBlockState);
/*    */   }
/*    */   
/*    */   protected abstract MapCodec<? extends PipeBlock> codec();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\PipeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */