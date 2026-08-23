/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class DirtPathBlock extends Block {
/* 18 */   public static final MapCodec<DirtPathBlock> CODEC = simpleCodec(DirtPathBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<DirtPathBlock> codec() {
/* 22 */     return CODEC;
/*    */   }
/*    */   
/* 25 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 15.0D);
/*    */   
/*    */   protected DirtPathBlock(BlockBehaviour.Properties paramProperties) {
/* 28 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean useShapeForLightOcclusion(BlockState paramBlockState) {
/* 33 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 38 */     if (!defaultBlockState().canSurvive((LevelReader)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos())) {
/* 39 */       return Block.pushEntitiesUp(defaultBlockState(), Blocks.DIRT.defaultBlockState(), (LevelAccessor)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos());
/*    */     }
/* 41 */     return super.getStateForPlacement(paramBlockPlaceContext);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 46 */     if (paramDirection == Direction.UP && 
/* 47 */       !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 48 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 1);
/*    */     }
/*    */     
/* 51 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 56 */     FarmBlock.turnToDirt(null, paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 61 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.above());
/* 62 */     return (!blockState.isSolid() || blockState.getBlock() instanceof FenceGateBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 67 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 72 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\DirtPathBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */