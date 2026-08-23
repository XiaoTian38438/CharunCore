/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.LayerConfiguration;
/*    */ 
/*    */ public class FillLayerFeature
/*    */   extends Feature<LayerConfiguration> {
/*    */   public FillLayerFeature(Codec<LayerConfiguration> paramCodec) {
/* 11 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<LayerConfiguration> paramFeaturePlaceContext) {
/* 16 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 17 */     LayerConfiguration layerConfiguration = paramFeaturePlaceContext.config();
/* 18 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 19 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*    */     
/* 21 */     for (byte b = 0; b < 16; b++) {
/* 22 */       for (byte b1 = 0; b1 < 16; b1++) {
/* 23 */         int i = blockPos.getX() + b;
/* 24 */         int j = blockPos.getZ() + b1;
/* 25 */         int k = worldGenLevel.getMinY() + layerConfiguration.height;
/* 26 */         mutableBlockPos.set(i, k, j);
/*    */         
/* 28 */         if (worldGenLevel.getBlockState((BlockPos)mutableBlockPos).isAir()) {
/* 29 */           worldGenLevel.setBlock((BlockPos)mutableBlockPos, layerConfiguration.state, 2);
/*    */         }
/*    */       } 
/*    */     } 
/* 33 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\FillLayerFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */