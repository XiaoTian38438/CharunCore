/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
/*    */ 
/*    */ public class ReplaceBlockFeature
/*    */   extends Feature<ReplaceBlockConfiguration> {
/*    */   public ReplaceBlockFeature(Codec<ReplaceBlockConfiguration> paramCodec) {
/* 12 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<ReplaceBlockConfiguration> paramFeaturePlaceContext) {
/* 17 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 18 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 19 */     ReplaceBlockConfiguration replaceBlockConfiguration = paramFeaturePlaceContext.config();
/* 20 */     for (OreConfiguration.TargetBlockState targetBlockState : replaceBlockConfiguration.targetStates) {
/* 21 */       if (targetBlockState.target.test(worldGenLevel.getBlockState(blockPos), paramFeaturePlaceContext.random())) {
/* 22 */         worldGenLevel.setBlock(blockPos, targetBlockState.state, 2);
/*    */         break;
/*    */       } 
/*    */     } 
/* 26 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\ReplaceBlockFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */