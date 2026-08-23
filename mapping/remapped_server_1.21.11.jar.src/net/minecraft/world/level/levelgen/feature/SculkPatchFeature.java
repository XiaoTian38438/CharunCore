/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SculkShriekerBlock;
/*    */ import net.minecraft.world.level.block.SculkSpreader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SculkPatchConfiguration;
/*    */ 
/*    */ public class SculkPatchFeature extends Feature<SculkPatchConfiguration> {
/*    */   public SculkPatchFeature(Codec<SculkPatchConfiguration> paramCodec) {
/* 20 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<SculkPatchConfiguration> paramFeaturePlaceContext) {
/* 25 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 26 */     BlockPos blockPos1 = paramFeaturePlaceContext.origin();
/* 27 */     if (!canSpreadFrom((LevelAccessor)worldGenLevel, blockPos1)) {
/* 28 */       return false;
/*    */     }
/* 30 */     SculkPatchConfiguration sculkPatchConfiguration = paramFeaturePlaceContext.config();
/* 31 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 32 */     SculkSpreader sculkSpreader = SculkSpreader.createWorldGenSpreader();
/* 33 */     int i = sculkPatchConfiguration.spreadRounds() + sculkPatchConfiguration.growthRounds();
/* 34 */     for (byte b1 = 0; b1 < i; b1++) {
/* 35 */       byte b3; for (b3 = 0; b3 < sculkPatchConfiguration.chargeCount(); b3++) {
/* 36 */         sculkSpreader.addCursors(blockPos1, sculkPatchConfiguration.amountPerCharge());
/*    */       }
/* 38 */       b3 = (b1 < sculkPatchConfiguration.spreadRounds()) ? 1 : 0;
/* 39 */       for (byte b4 = 0; b4 < sculkPatchConfiguration.spreadAttempts(); b4++) {
/* 40 */         sculkSpreader.updateCursors((LevelAccessor)worldGenLevel, blockPos1, randomSource, b3);
/*    */       }
/* 42 */       sculkSpreader.clear();
/*    */     } 
/* 44 */     BlockPos blockPos2 = blockPos1.below();
/* 45 */     if (randomSource.nextFloat() <= sculkPatchConfiguration.catalystChance() && worldGenLevel.getBlockState(blockPos2).isCollisionShapeFullBlock((BlockGetter)worldGenLevel, blockPos2)) {
/* 46 */       worldGenLevel.setBlock(blockPos1, Blocks.SCULK_CATALYST.defaultBlockState(), 3);
/*    */     }
/* 48 */     int j = sculkPatchConfiguration.extraRareGrowths().sample(randomSource);
/* 49 */     for (byte b2 = 0; b2 < j; b2++) {
/* 50 */       BlockPos blockPos = blockPos1.offset(randomSource.nextInt(5) - 2, 0, randomSource.nextInt(5) - 2);
/* 51 */       if (worldGenLevel.getBlockState(blockPos).isAir() && worldGenLevel.getBlockState(blockPos.below()).isFaceSturdy((BlockGetter)worldGenLevel, blockPos.below(), Direction.UP)) {
/* 52 */         worldGenLevel.setBlock(blockPos, (BlockState)Blocks.SCULK_SHRIEKER.defaultBlockState().setValue((Property)SculkShriekerBlock.CAN_SUMMON, Boolean.valueOf(true)), 3);
/*    */       }
/*    */     } 
/* 55 */     return true;
/*    */   }
/*    */   
/*    */   private boolean canSpreadFrom(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 59 */     BlockState blockState = paramLevelAccessor.getBlockState(paramBlockPos);
/* 60 */     if (blockState.getBlock() instanceof net.minecraft.world.level.block.SculkBehaviour) {
/* 61 */       return true;
/*    */     }
/* 63 */     if (blockState.isAir() || (blockState.is(Blocks.WATER) && blockState.getFluidState().isSource())) {
/* 64 */       Objects.requireNonNull(paramBlockPos); return Direction.stream().map(paramBlockPos::relative).anyMatch(paramBlockPos -> paramLevelAccessor.getBlockState(paramBlockPos).isCollisionShapeFullBlock((BlockGetter)paramLevelAccessor, paramBlockPos));
/*    */     } 
/* 66 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\SculkPatchFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */