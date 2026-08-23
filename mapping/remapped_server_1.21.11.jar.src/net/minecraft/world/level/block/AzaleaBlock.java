/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class AzaleaBlock extends VegetationBlock implements BonemealableBlock {
/* 18 */   public static final MapCodec<AzaleaBlock> CODEC = simpleCodec(AzaleaBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<AzaleaBlock> codec() {
/* 22 */     return CODEC;
/*    */   }
/*    */   
/* 25 */   private static final VoxelShape SHAPE = Shapes.or(
/* 26 */       Block.column(16.0D, 8.0D, 16.0D), 
/* 27 */       Block.column(4.0D, 0.0D, 8.0D));
/*    */ 
/*    */   
/*    */   protected AzaleaBlock(BlockBehaviour.Properties paramProperties) {
/* 31 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 36 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 41 */     return (paramBlockState.is(Blocks.CLAY) || super.mayPlaceOn(paramBlockState, paramBlockGetter, paramBlockPos));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 46 */     return paramLevelReader.getFluidState(paramBlockPos.above()).isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 51 */     return (paramLevel.random.nextFloat() < 0.45D);
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 56 */     TreeGrower.AZALEA.growTree(paramServerLevel, paramServerLevel.getChunkSource().getGenerator(), paramBlockPos, paramBlockState, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 61 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\AzaleaBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */