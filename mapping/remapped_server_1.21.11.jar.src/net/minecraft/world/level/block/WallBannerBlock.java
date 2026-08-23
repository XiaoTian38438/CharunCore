/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WallBannerBlock extends AbstractBannerBlock {
/*    */   static {
/* 23 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)DyeColor.CODEC.fieldOf("color").forGetter(AbstractBannerBlock::getColor), (App)propertiesCodec()).apply((Applicative)paramInstance, WallBannerBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<WallBannerBlock> CODEC;
/*    */   
/*    */   public MapCodec<WallBannerBlock> codec() {
/* 30 */     return CODEC;
/*    */   }
/*    */   
/* 33 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*    */   
/* 35 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(16.0D, 0.0D, 12.5D, 14.0D, 16.0D));
/*    */   
/*    */   public WallBannerBlock(DyeColor paramDyeColor, BlockBehaviour.Properties paramProperties) {
/* 38 */     super(paramDyeColor, paramProperties);
/* 39 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 45 */     return paramLevelReader.getBlockState(paramBlockPos.relative(((Direction)paramBlockState.getValue((Property)FACING)).getOpposite())).isSolid();
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 50 */     if (paramDirection == ((Direction)paramBlockState1.getValue((Property)FACING)).getOpposite() && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 51 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 54 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 59 */     return SHAPES.get(paramBlockState.getValue((Property)FACING));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 64 */     BlockState blockState = defaultBlockState();
/*    */     
/* 66 */     Level level = paramBlockPlaceContext.getLevel();
/* 67 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*    */     
/* 69 */     Direction[] arrayOfDirection = paramBlockPlaceContext.getNearestLookingDirections();
/* 70 */     for (Direction direction : arrayOfDirection) {
/* 71 */       if (direction.getAxis().isHorizontal()) {
/*    */ 
/*    */ 
/*    */         
/* 75 */         Direction direction1 = direction.getOpposite();
/*    */         
/* 77 */         blockState = (BlockState)blockState.setValue((Property)FACING, (Comparable)direction1);
/* 78 */         if (blockState.canSurvive((LevelReader)level, blockPos)) {
/* 79 */           return blockState;
/*    */         }
/*    */       } 
/*    */     } 
/* 83 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 88 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 93 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 98 */     paramBuilder.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WallBannerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */