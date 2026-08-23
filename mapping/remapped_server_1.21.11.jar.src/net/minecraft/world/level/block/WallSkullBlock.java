/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WallSkullBlock extends AbstractSkullBlock {
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)SkullBlock.Type.CODEC.fieldOf("kind").forGetter(AbstractSkullBlock::getType), (App)propertiesCodec()).apply((Applicative)paramInstance, WallSkullBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<WallSkullBlock> CODEC;
/*    */   
/*    */   public MapCodec<? extends WallSkullBlock> codec() {
/* 26 */     return CODEC;
/*    */   }
/*    */   
/* 29 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*    */   
/* 31 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(8.0D, 8.0D, 16.0D));
/*    */   
/*    */   protected WallSkullBlock(SkullBlock.Type paramType, BlockBehaviour.Properties paramProperties) {
/* 34 */     super(paramType, paramProperties);
/* 35 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)Direction.NORTH));
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 40 */     return SHAPES.get(paramBlockState.getValue((Property)FACING));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 45 */     BlockState blockState = super.getStateForPlacement(paramBlockPlaceContext);
/*    */     
/* 47 */     Level level = paramBlockPlaceContext.getLevel();
/* 48 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*    */     
/* 50 */     Direction[] arrayOfDirection = paramBlockPlaceContext.getNearestLookingDirections();
/* 51 */     for (Direction direction : arrayOfDirection) {
/* 52 */       if (direction.getAxis().isHorizontal()) {
/*    */ 
/*    */ 
/*    */         
/* 56 */         Direction direction1 = direction.getOpposite();
/*    */         
/* 58 */         blockState = (BlockState)blockState.setValue((Property)FACING, (Comparable)direction1);
/* 59 */         if (!level.getBlockState(blockPos.relative(direction)).canBeReplaced(paramBlockPlaceContext)) {
/* 60 */           return blockState;
/*    */         }
/*    */       } 
/*    */     } 
/* 64 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 69 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 74 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 79 */     super.createBlockStateDefinition(paramBuilder);
/* 80 */     paramBuilder.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WallSkullBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */