/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class FireflyBushBlock extends VegetationBlock implements BonemealableBlock {
/*    */   private static final double FIREFLY_CHANCE_PER_TICK = 0.7D;
/*    */   private static final double FIREFLY_HORIZONTAL_RANGE = 10.0D;
/*    */   
/*    */   public FireflyBushBlock(BlockBehaviour.Properties paramProperties) {
/* 24 */     super(paramProperties);
/*    */   }
/*    */   private static final double FIREFLY_VERTICAL_RANGE = 5.0D; private static final int FIREFLY_SPAWN_MAX_BRIGHTNESS_LEVEL = 13; private static final int FIREFLY_AMBIENT_SOUND_CHANCE_ONE_IN = 30;
/* 27 */   public static final MapCodec<FireflyBushBlock> CODEC = simpleCodec(FireflyBushBlock::new);
/*    */ 
/*    */   
/*    */   protected MapCodec<? extends FireflyBushBlock> codec() {
/* 31 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 36 */     if (paramRandomSource.nextInt(30) == 0 && ((Boolean)paramLevel.environmentAttributes().getValue(EnvironmentAttributes.FIREFLY_BUSH_SOUNDS, paramBlockPos)).booleanValue() && paramLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, paramBlockPos) <= paramBlockPos.getY()) {
/* 37 */       paramLevel.playLocalSound(paramBlockPos, SoundEvents.FIREFLY_BUSH_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
/*    */     }
/*    */     
/* 40 */     if (paramLevel.getMaxLocalRawBrightness(paramBlockPos) <= 13 && paramRandomSource.nextDouble() <= 0.7D) {
/* 41 */       double d1 = paramBlockPos.getX() + paramRandomSource.nextDouble() * 10.0D - 5.0D;
/* 42 */       double d2 = paramBlockPos.getY() + paramRandomSource.nextDouble() * 5.0D;
/* 43 */       double d3 = paramBlockPos.getZ() + paramRandomSource.nextDouble() * 10.0D - 5.0D;
/* 44 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.FIREFLY, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 50 */     return BonemealableBlock.hasSpreadableNeighbourPos(paramLevelReader, paramBlockPos, paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 55 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 60 */     BonemealableBlock.findSpreadableNeighbourPos((Level)paramServerLevel, paramBlockPos, paramBlockState).ifPresent(paramBlockPos -> paramServerLevel.setBlockAndUpdate(paramBlockPos, defaultBlockState()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\FireflyBushBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */