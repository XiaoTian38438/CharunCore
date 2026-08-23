/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RootedDirtBlock extends Block implements BonemealableBlock {
/* 12 */   public static final MapCodec<RootedDirtBlock> CODEC = simpleCodec(RootedDirtBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<RootedDirtBlock> codec() {
/* 16 */     return CODEC;
/*    */   }
/*    */   
/*    */   public RootedDirtBlock(BlockBehaviour.Properties paramProperties) {
/* 20 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 25 */     return paramLevelReader.getBlockState(paramBlockPos.below()).isAir();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 30 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 35 */     paramServerLevel.setBlockAndUpdate(paramBlockPos.below(), Blocks.HANGING_ROOTS.defaultBlockState());
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getParticlePos(BlockPos paramBlockPos) {
/* 40 */     return paramBlockPos.below();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\RootedDirtBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */