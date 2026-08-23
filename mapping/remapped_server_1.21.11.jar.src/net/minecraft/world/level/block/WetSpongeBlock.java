/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WetSpongeBlock extends Block {
/* 16 */   public static final MapCodec<WetSpongeBlock> CODEC = simpleCodec(WetSpongeBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<WetSpongeBlock> codec() {
/* 20 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected WetSpongeBlock(BlockBehaviour.Properties paramProperties) {
/* 24 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 29 */     if (((Boolean)paramLevel.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, paramBlockPos)).booleanValue()) {
/* 30 */       paramLevel.setBlock(paramBlockPos, Blocks.SPONGE.defaultBlockState(), 3);
/* 31 */       paramLevel.levelEvent(2009, paramBlockPos, 0);
/* 32 */       paramLevel.playSound(null, paramBlockPos, SoundEvents.WET_SPONGE_DRIES, SoundSource.BLOCKS, 1.0F, (1.0F + paramLevel.getRandom().nextFloat() * 0.2F) * 0.7F);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 38 */     Direction direction = Direction.getRandom(paramRandomSource);
/* 39 */     if (direction == Direction.UP) {
/*    */       return;
/*    */     }
/* 42 */     BlockPos blockPos = paramBlockPos.relative(direction);
/* 43 */     BlockState blockState = paramLevel.getBlockState(blockPos);
/* 44 */     if (paramBlockState.canOcclude() && blockState.isFaceSturdy((BlockGetter)paramLevel, blockPos, direction.getOpposite())) {
/*    */       return;
/*    */     }
/*    */     
/* 48 */     double d1 = paramBlockPos.getX();
/* 49 */     double d2 = paramBlockPos.getY();
/* 50 */     double d3 = paramBlockPos.getZ();
/*    */ 
/*    */ 
/*    */     
/* 54 */     if (direction == Direction.DOWN) {
/* 55 */       d2 -= 0.05D;
/* 56 */       d1 += paramRandomSource.nextDouble();
/* 57 */       d3 += paramRandomSource.nextDouble();
/*    */     } else {
/* 59 */       d2 += paramRandomSource.nextDouble() * 0.8D;
/* 60 */       if (direction.getAxis() == Direction.Axis.X) {
/* 61 */         d3 += paramRandomSource.nextDouble();
/* 62 */         if (direction == Direction.EAST) {
/* 63 */           d1 += 1.1D;
/*    */         } else {
/* 65 */           d1 += 0.05D;
/*    */         } 
/*    */       } else {
/* 68 */         d1 += paramRandomSource.nextDouble();
/* 69 */         if (direction == Direction.SOUTH) {
/* 70 */           d3 += 1.1D;
/*    */         } else {
/* 72 */           d3 += 0.05D;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 77 */     paramLevel.addParticle((ParticleOptions)ParticleTypes.DRIPPING_WATER, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WetSpongeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */