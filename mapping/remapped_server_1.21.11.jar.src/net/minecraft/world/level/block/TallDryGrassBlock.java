/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.sounds.AmbientDesertBlockSoundsPlayer;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TallDryGrassBlock extends DryVegetationBlock implements BonemealableBlock {
/* 16 */   public static final MapCodec<TallDryGrassBlock> CODEC = simpleCodec(TallDryGrassBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<TallDryGrassBlock> codec() {
/* 20 */     return CODEC;
/*    */   }
/*    */   
/* 23 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 16.0D);
/*    */   
/*    */   protected TallDryGrassBlock(BlockBehaviour.Properties paramProperties) {
/* 26 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 31 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 36 */     AmbientDesertBlockSoundsPlayer.playAmbientDryGrassSounds(paramLevel, paramBlockPos, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 41 */     return BonemealableBlock.hasSpreadableNeighbourPos(paramLevelReader, paramBlockPos, Blocks.SHORT_DRY_GRASS.defaultBlockState());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 51 */     BonemealableBlock.findSpreadableNeighbourPos((Level)paramServerLevel, paramBlockPos, Blocks.SHORT_DRY_GRASS.defaultBlockState()).ifPresent(paramBlockPos -> paramServerLevel.setBlockAndUpdate(paramBlockPos, Blocks.SHORT_DRY_GRASS.defaultBlockState()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TallDryGrassBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */