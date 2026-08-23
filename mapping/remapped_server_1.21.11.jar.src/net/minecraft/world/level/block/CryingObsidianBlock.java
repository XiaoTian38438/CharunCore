/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class CryingObsidianBlock extends Block {
/* 12 */   public static final MapCodec<CryingObsidianBlock> CODEC = simpleCodec(CryingObsidianBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<CryingObsidianBlock> codec() {
/* 16 */     return CODEC;
/*    */   }
/*    */   
/*    */   public CryingObsidianBlock(BlockBehaviour.Properties paramProperties) {
/* 20 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 25 */     if (paramRandomSource.nextInt(5) != 0) {
/*    */       return;
/*    */     }
/*    */     
/* 29 */     Direction direction = Direction.getRandom(paramRandomSource);
/* 30 */     if (direction == Direction.UP) {
/*    */       return;
/*    */     }
/* 33 */     BlockPos blockPos = paramBlockPos.relative(direction);
/* 34 */     BlockState blockState = paramLevel.getBlockState(blockPos);
/* 35 */     if (paramBlockState.canOcclude() && blockState.isFaceSturdy((BlockGetter)paramLevel, blockPos, direction.getOpposite())) {
/*    */       return;
/*    */     }
/*    */     
/* 39 */     double d1 = (direction.getStepX() == 0) ? paramRandomSource.nextDouble() : (0.5D + direction.getStepX() * 0.6D);
/* 40 */     double d2 = (direction.getStepY() == 0) ? paramRandomSource.nextDouble() : (0.5D + direction.getStepY() * 0.6D);
/* 41 */     double d3 = (direction.getStepZ() == 0) ? paramRandomSource.nextDouble() : (0.5D + direction.getStepZ() * 0.6D);
/*    */     
/* 43 */     paramLevel.addParticle((ParticleOptions)ParticleTypes.DRIPPING_OBSIDIAN_TEAR, paramBlockPos.getX() + d1, paramBlockPos.getY() + d2, paramBlockPos.getZ() + d3, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CryingObsidianBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */