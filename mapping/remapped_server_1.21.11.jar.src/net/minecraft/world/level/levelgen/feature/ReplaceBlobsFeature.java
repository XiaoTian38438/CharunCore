/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
/*    */ 
/*    */ public class ReplaceBlobsFeature extends Feature<ReplaceSphereConfiguration> {
/*    */   public ReplaceBlobsFeature(Codec<ReplaceSphereConfiguration> paramCodec) {
/* 16 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<ReplaceSphereConfiguration> paramFeaturePlaceContext) {
/* 21 */     ReplaceSphereConfiguration replaceSphereConfiguration = paramFeaturePlaceContext.config();
/* 22 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 23 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 24 */     Block block = replaceSphereConfiguration.targetState.getBlock();
/* 25 */     BlockPos blockPos = findTarget((LevelAccessor)worldGenLevel, paramFeaturePlaceContext.origin().mutable().clamp(Direction.Axis.Y, worldGenLevel.getMinY() + 1, worldGenLevel.getMaxY()), block);
/* 26 */     if (blockPos == null) {
/* 27 */       return false;
/*    */     }
/*    */     
/* 30 */     int i = replaceSphereConfiguration.radius().sample(randomSource);
/* 31 */     int j = replaceSphereConfiguration.radius().sample(randomSource);
/* 32 */     int k = replaceSphereConfiguration.radius().sample(randomSource);
/* 33 */     int m = Math.max(i, Math.max(j, k));
/*    */     
/* 35 */     boolean bool = false;
/* 36 */     for (BlockPos blockPos1 : BlockPos.withinManhattan(blockPos, i, j, k)) {
/* 37 */       if (blockPos1.distManhattan((Vec3i)blockPos) > m) {
/*    */         break;
/*    */       }
/*    */ 
/*    */       
/* 42 */       BlockState blockState = worldGenLevel.getBlockState(blockPos1);
/* 43 */       if (blockState.is(block)) {
/* 44 */         setBlock((LevelWriter)worldGenLevel, blockPos1, replaceSphereConfiguration.replaceState);
/* 45 */         bool = true;
/*    */       } 
/*    */     } 
/*    */     
/* 49 */     return bool;
/*    */   }
/*    */   
/*    */   private static BlockPos findTarget(LevelAccessor paramLevelAccessor, BlockPos.MutableBlockPos paramMutableBlockPos, Block paramBlock) {
/* 53 */     while (paramMutableBlockPos.getY() > paramLevelAccessor.getMinY() + 1) {
/* 54 */       BlockState blockState = paramLevelAccessor.getBlockState((BlockPos)paramMutableBlockPos);
/* 55 */       if (blockState.is(paramBlock)) {
/* 56 */         return (BlockPos)paramMutableBlockPos;
/*    */       }
/*    */       
/* 59 */       paramMutableBlockPos.move(Direction.DOWN);
/*    */     } 
/* 61 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\ReplaceBlobsFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */