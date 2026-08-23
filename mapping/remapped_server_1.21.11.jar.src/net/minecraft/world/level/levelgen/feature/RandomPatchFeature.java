/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
/*    */ 
/*    */ public class RandomPatchFeature extends Feature<RandomPatchConfiguration> {
/*    */   public RandomPatchFeature(Codec<RandomPatchConfiguration> paramCodec) {
/* 11 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<RandomPatchConfiguration> paramFeaturePlaceContext) {
/* 16 */     RandomPatchConfiguration randomPatchConfiguration = paramFeaturePlaceContext.config();
/* 17 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 18 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 19 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/*    */     
/* 21 */     byte b1 = 0;
/*    */     
/* 23 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 24 */     int i = randomPatchConfiguration.xzSpread() + 1;
/* 25 */     int j = randomPatchConfiguration.ySpread() + 1;
/* 26 */     for (byte b2 = 0; b2 < randomPatchConfiguration.tries(); b2++) {
/* 27 */       mutableBlockPos.setWithOffset((Vec3i)blockPos, randomSource.nextInt(i) - randomSource.nextInt(i), randomSource.nextInt(j) - randomSource.nextInt(j), randomSource.nextInt(i) - randomSource.nextInt(i));
/* 28 */       if (((PlacedFeature)randomPatchConfiguration.feature().value()).place(worldGenLevel, paramFeaturePlaceContext.chunkGenerator(), randomSource, (BlockPos)mutableBlockPos)) {
/* 29 */         b1++;
/*    */       }
/*    */     } 
/*    */     
/* 33 */     return (b1 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\RandomPatchFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */