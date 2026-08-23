/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class HugeMushroomBlock extends Block {
/* 18 */   public static final MapCodec<HugeMushroomBlock> CODEC = simpleCodec(HugeMushroomBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<HugeMushroomBlock> codec() {
/* 22 */     return CODEC;
/*    */   }
/*    */   
/* 25 */   public static final BooleanProperty NORTH = PipeBlock.NORTH;
/* 26 */   public static final BooleanProperty EAST = PipeBlock.EAST;
/* 27 */   public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
/* 28 */   public static final BooleanProperty WEST = PipeBlock.WEST;
/* 29 */   public static final BooleanProperty UP = PipeBlock.UP;
/* 30 */   public static final BooleanProperty DOWN = PipeBlock.DOWN;
/*    */   
/* 32 */   private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
/*    */   
/*    */   public HugeMushroomBlock(BlockBehaviour.Properties paramProperties) {
/* 35 */     super(paramProperties);
/* 36 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, Boolean.valueOf(true))).setValue((Property)EAST, Boolean.valueOf(true))).setValue((Property)SOUTH, Boolean.valueOf(true))).setValue((Property)WEST, Boolean.valueOf(true))).setValue((Property)UP, Boolean.valueOf(true))).setValue((Property)DOWN, Boolean.valueOf(true)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 41 */     Level level = paramBlockPlaceContext.getLevel();
/* 42 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*    */     
/* 44 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState()
/* 45 */       .setValue((Property)DOWN, Boolean.valueOf(!level.getBlockState(blockPos.below()).is(this))))
/* 46 */       .setValue((Property)UP, Boolean.valueOf(!level.getBlockState(blockPos.above()).is(this))))
/* 47 */       .setValue((Property)NORTH, Boolean.valueOf(!level.getBlockState(blockPos.north()).is(this))))
/* 48 */       .setValue((Property)EAST, Boolean.valueOf(!level.getBlockState(blockPos.east()).is(this))))
/* 49 */       .setValue((Property)SOUTH, Boolean.valueOf(!level.getBlockState(blockPos.south()).is(this))))
/* 50 */       .setValue((Property)WEST, Boolean.valueOf(!level.getBlockState(blockPos.west()).is(this)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 56 */     if (paramBlockState2.is(this)) {
/* 57 */       return (BlockState)paramBlockState1.setValue((Property)PROPERTY_BY_DIRECTION.get(paramDirection), Boolean.valueOf(false));
/*    */     }
/* 59 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 64 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)paramBlockState
/* 65 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(paramRotation.rotate(Direction.NORTH)), paramBlockState.getValue((Property)NORTH)))
/* 66 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(paramRotation.rotate(Direction.SOUTH)), paramBlockState.getValue((Property)SOUTH)))
/* 67 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(paramRotation.rotate(Direction.EAST)), paramBlockState.getValue((Property)EAST)))
/* 68 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(paramRotation.rotate(Direction.WEST)), paramBlockState.getValue((Property)WEST)))
/* 69 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(paramRotation.rotate(Direction.UP)), paramBlockState.getValue((Property)UP)))
/* 70 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(paramRotation.rotate(Direction.DOWN)), paramBlockState.getValue((Property)DOWN));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 76 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)paramBlockState
/* 77 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(paramMirror.mirror(Direction.NORTH)), paramBlockState.getValue((Property)NORTH)))
/* 78 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(paramMirror.mirror(Direction.SOUTH)), paramBlockState.getValue((Property)SOUTH)))
/* 79 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(paramMirror.mirror(Direction.EAST)), paramBlockState.getValue((Property)EAST)))
/* 80 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(paramMirror.mirror(Direction.WEST)), paramBlockState.getValue((Property)WEST)))
/* 81 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(paramMirror.mirror(Direction.UP)), paramBlockState.getValue((Property)UP)))
/* 82 */       .setValue((Property)PROPERTY_BY_DIRECTION.get(paramMirror.mirror(Direction.DOWN)), paramBlockState.getValue((Property)DOWN));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 88 */     paramBuilder.add(new Property[] { (Property)UP, (Property)DOWN, (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\HugeMushroomBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */