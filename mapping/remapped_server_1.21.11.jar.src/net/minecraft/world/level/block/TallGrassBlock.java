/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TallGrassBlock extends VegetationBlock implements BonemealableBlock {
/* 15 */   public static final MapCodec<TallGrassBlock> CODEC = simpleCodec(TallGrassBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<TallGrassBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */   
/* 22 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 13.0D);
/*    */   
/*    */   protected TallGrassBlock(BlockBehaviour.Properties paramProperties) {
/* 25 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 30 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 35 */     return (getGrownBlock(paramBlockState).defaultBlockState().canSurvive(paramLevelReader, paramBlockPos) && paramLevelReader.isEmptyBlock(paramBlockPos.above()));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 40 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 45 */     DoublePlantBlock.placeAt((LevelAccessor)paramServerLevel, getGrownBlock(paramBlockState).defaultBlockState(), paramBlockPos, 2);
/*    */   }
/*    */   
/*    */   private static DoublePlantBlock getGrownBlock(BlockState paramBlockState) {
/* 49 */     return paramBlockState.is(Blocks.FERN) ? (DoublePlantBlock)Blocks.LARGE_FERN : (DoublePlantBlock)Blocks.TALL_GRASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TallGrassBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */