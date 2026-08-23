/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class EndIslandFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public EndIslandFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/* 13 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/* 18 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 19 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 20 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 21 */     float f = randomSource.nextInt(3) + 4.0F;
/* 22 */     byte b = 0;
/* 23 */     while (f > 0.5F) {
/* 24 */       for (int i = Mth.floor(-f); i <= Mth.ceil(f); i++) {
/* 25 */         for (int j = Mth.floor(-f); j <= Mth.ceil(f); j++) {
/* 26 */           if ((i * i + j * j) <= (f + 1.0F) * (f + 1.0F)) {
/* 27 */             setBlock((LevelWriter)worldGenLevel, blockPos.offset(i, b, j), Blocks.END_STONE.defaultBlockState());
/*    */           }
/*    */         } 
/*    */       } 
/* 31 */       f -= randomSource.nextInt(2) + 0.5F;
/* 32 */       b--;
/*    */     } 
/*    */     
/* 35 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\EndIslandFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */