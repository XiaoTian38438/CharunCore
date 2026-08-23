/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class SculkBlock extends DropExperienceBlock implements SculkBehaviour {
/* 15 */   public static final MapCodec<SculkBlock> CODEC = simpleCodec(SculkBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<SculkBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */   
/*    */   public SculkBlock(BlockBehaviour.Properties paramProperties) {
/* 23 */     super((IntProvider)ConstantInt.of(1), paramProperties);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int attemptUseCharge(SculkSpreader.ChargeCursor paramChargeCursor, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, RandomSource paramRandomSource, SculkSpreader paramSculkSpreader, boolean paramBoolean) {
/* 29 */     int i = paramChargeCursor.getCharge();
/* 30 */     if (i == 0 || paramRandomSource.nextInt(paramSculkSpreader.chargeDecayRate()) != 0) {
/* 31 */       return i;
/*    */     }
/*    */     
/* 34 */     BlockPos blockPos = paramChargeCursor.getPos();
/* 35 */     boolean bool = blockPos.closerThan((Vec3i)paramBlockPos, paramSculkSpreader.noGrowthRadius());
/* 36 */     if (bool || !canPlaceGrowth(paramLevelAccessor, blockPos)) {
/* 37 */       if (paramRandomSource.nextInt(paramSculkSpreader.additionalDecayRate()) != 0) {
/* 38 */         return i;
/*    */       }
/* 40 */       return i - (bool ? 1 : getDecayPenalty(paramSculkSpreader, blockPos, paramBlockPos, i));
/*    */     } 
/* 42 */     int j = paramSculkSpreader.growthSpawnCost();
/* 43 */     if (paramRandomSource.nextInt(j) < i) {
/* 44 */       BlockPos blockPos1 = blockPos.above();
/* 45 */       BlockState blockState = getRandomGrowthState(paramLevelAccessor, blockPos1, paramRandomSource, paramSculkSpreader.isWorldGeneration());
/* 46 */       paramLevelAccessor.setBlock(blockPos1, blockState, 3);
/* 47 */       paramLevelAccessor.playSound(null, blockPos, blockState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
/*    */     } 
/* 49 */     return Math.max(0, i - j);
/*    */   }
/*    */   
/*    */   private static int getDecayPenalty(SculkSpreader paramSculkSpreader, BlockPos paramBlockPos1, BlockPos paramBlockPos2, int paramInt) {
/* 53 */     int i = paramSculkSpreader.noGrowthRadius();
/* 54 */     float f1 = Mth.square((float)Math.sqrt(paramBlockPos1.distSqr((Vec3i)paramBlockPos2)) - i);
/* 55 */     int j = Mth.square(24 - i);
/*    */ 
/*    */     
/* 58 */     float f2 = Math.min(1.0F, f1 / j);
/* 59 */     return Math.max(1, (int)(paramInt * f2 * 0.5F));
/*    */   }
/*    */   
/*    */   private BlockState getRandomGrowthState(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, RandomSource paramRandomSource, boolean paramBoolean) {
/*    */     BlockState blockState;
/* 64 */     if (paramRandomSource.nextInt(11) == 0) {
/* 65 */       blockState = (BlockState)Blocks.SCULK_SHRIEKER.defaultBlockState().setValue((Property)SculkShriekerBlock.CAN_SUMMON, Boolean.valueOf(paramBoolean));
/*    */     } else {
/* 67 */       blockState = Blocks.SCULK_SENSOR.defaultBlockState();
/*    */     } 
/*    */     
/* 70 */     if (blockState.hasProperty((Property)BlockStateProperties.WATERLOGGED) && !paramLevelAccessor.getFluidState(paramBlockPos).isEmpty()) {
/* 71 */       return (BlockState)blockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
/*    */     }
/* 73 */     return blockState;
/*    */   }
/*    */   
/*    */   private static boolean canPlaceGrowth(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 77 */     BlockState blockState = paramLevelAccessor.getBlockState(paramBlockPos.above());
/* 78 */     if (!blockState.isAir() && (!blockState.is(Blocks.WATER) || !blockState.getFluidState().is((Fluid)Fluids.WATER))) {
/* 79 */       return false;
/*    */     }
/*    */     
/* 82 */     byte b = 0;
/* 83 */     for (BlockPos blockPos : BlockPos.betweenClosed(paramBlockPos.offset(-4, 0, -4), paramBlockPos.offset(4, 2, 4))) {
/* 84 */       BlockState blockState1 = paramLevelAccessor.getBlockState(blockPos);
/* 85 */       if (blockState1.is(Blocks.SCULK_SENSOR) || blockState1.is(Blocks.SCULK_SHRIEKER)) {
/* 86 */         b++;
/*    */       }
/* 88 */       if (b > 2) {
/* 89 */         return false;
/*    */       }
/*    */     } 
/* 92 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canChangeBlockStateOnSpread() {
/* 97 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SculkBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */