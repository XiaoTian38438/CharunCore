/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
/*    */ 
/*    */ public class DiskFeature extends Feature<DiskConfiguration> {
/*    */   public DiskFeature(Codec<DiskConfiguration> paramCodec) {
/* 13 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<DiskConfiguration> paramFeaturePlaceContext) {
/* 18 */     DiskConfiguration diskConfiguration = paramFeaturePlaceContext.config();
/* 19 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 20 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 21 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 22 */     boolean bool = false;
/*    */     
/* 24 */     int i = blockPos.getY();
/* 25 */     int j = i + diskConfiguration.halfHeight();
/* 26 */     int k = i - diskConfiguration.halfHeight() - 1;
/*    */     
/* 28 */     int m = diskConfiguration.radius().sample(randomSource);
/*    */     
/* 30 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*    */     
/* 32 */     for (BlockPos blockPos1 : BlockPos.betweenClosed(blockPos.offset(-m, 0, -m), blockPos.offset(m, 0, m))) {
/* 33 */       int n = blockPos1.getX() - blockPos.getX();
/* 34 */       int i1 = blockPos1.getZ() - blockPos.getZ();
/* 35 */       if (n * n + i1 * i1 > m * m) {
/*    */         continue;
/*    */       }
/*    */       
/* 39 */       bool |= placeColumn(diskConfiguration, worldGenLevel, randomSource, j, k, mutableBlockPos.set((Vec3i)blockPos1));
/*    */     } 
/*    */     
/* 42 */     return bool;
/*    */   }
/*    */   
/*    */   protected boolean placeColumn(DiskConfiguration paramDiskConfiguration, WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, int paramInt1, int paramInt2, BlockPos.MutableBlockPos paramMutableBlockPos) {
/* 46 */     boolean bool1 = false;
/* 47 */     boolean bool2 = false;
/*    */     
/* 49 */     for (int i = paramInt1; i > paramInt2; i--) {
/* 50 */       paramMutableBlockPos.setY(i);
/* 51 */       if (paramDiskConfiguration.target().test(paramWorldGenLevel, paramMutableBlockPos)) {
/* 52 */         BlockState blockState = paramDiskConfiguration.stateProvider().getState(paramWorldGenLevel, paramRandomSource, (BlockPos)paramMutableBlockPos);
/* 53 */         paramWorldGenLevel.setBlock((BlockPos)paramMutableBlockPos, blockState, 2);
/* 54 */         if (!bool2) {
/* 55 */           markAboveForPostProcessing(paramWorldGenLevel, (BlockPos)paramMutableBlockPos);
/*    */         }
/* 57 */         bool1 = true;
/* 58 */         bool2 = true;
/*    */       } else {
/* 60 */         bool2 = false;
/*    */       } 
/*    */     } 
/*    */     
/* 64 */     return bool1;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\DiskFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */