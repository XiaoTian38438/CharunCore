/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
/*    */ 
/*    */ public class BlockColumnFeature
/*    */   extends Feature<BlockColumnConfiguration> {
/*    */   public BlockColumnFeature(Codec<BlockColumnConfiguration> paramCodec) {
/* 12 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<BlockColumnConfiguration> paramFeaturePlaceContext) {
/* 17 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 18 */     BlockColumnConfiguration blockColumnConfiguration = paramFeaturePlaceContext.config();
/* 19 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/*    */     
/* 21 */     int i = blockColumnConfiguration.layers().size();
/* 22 */     int[] arrayOfInt = new int[i];
/* 23 */     int j = 0;
/* 24 */     for (byte b1 = 0; b1 < i; b1++) {
/* 25 */       arrayOfInt[b1] = ((BlockColumnConfiguration.Layer)blockColumnConfiguration.layers().get(b1)).height().sample(randomSource);
/* 26 */       j += arrayOfInt[b1];
/*    */     } 
/* 28 */     if (j == 0) {
/* 29 */       return false;
/*    */     }
/*    */     
/* 32 */     BlockPos.MutableBlockPos mutableBlockPos1 = paramFeaturePlaceContext.origin().mutable();
/* 33 */     BlockPos.MutableBlockPos mutableBlockPos2 = mutableBlockPos1.mutable().move(blockColumnConfiguration.direction()); byte b2;
/* 34 */     for (b2 = 0; b2 < j; b2++) {
/* 35 */       if (!blockColumnConfiguration.allowedPlacement().test(worldGenLevel, mutableBlockPos2)) {
/* 36 */         truncate(arrayOfInt, j, b2, blockColumnConfiguration.prioritizeTip());
/*    */         break;
/*    */       } 
/* 39 */       mutableBlockPos2.move(blockColumnConfiguration.direction());
/*    */     } 
/*    */     
/* 42 */     for (b2 = 0; b2 < i; b2++) {
/* 43 */       int k = arrayOfInt[b2];
/* 44 */       if (k != 0) {
/*    */ 
/*    */ 
/*    */         
/* 48 */         BlockColumnConfiguration.Layer layer = blockColumnConfiguration.layers().get(b2);
/* 49 */         for (byte b = 0; b < k; b++) {
/* 50 */           worldGenLevel.setBlock((BlockPos)mutableBlockPos1, layer.state().getState(randomSource, (BlockPos)mutableBlockPos1), 2);
/* 51 */           mutableBlockPos1.move(blockColumnConfiguration.direction());
/*    */         } 
/*    */       } 
/* 54 */     }  return true;
/*    */   }
/*    */ 
/*    */   
/*    */   private static void truncate(int[] paramArrayOfint, int paramInt1, int paramInt2, boolean paramBoolean) {
/* 59 */     int i = paramInt1 - paramInt2;
/* 60 */     byte b1 = paramBoolean ? 1 : -1;
/* 61 */     byte b2 = paramBoolean ? 0 : (paramArrayOfint.length - 1);
/* 62 */     byte b3 = paramBoolean ? paramArrayOfint.length : -1;
/*    */     int j;
/* 64 */     for (j = b2; j != b3 && i > 0; j += b1) {
/* 65 */       int k = paramArrayOfint[j];
/* 66 */       int m = Math.min(k, i);
/* 67 */       i -= m;
/* 68 */       paramArrayOfint[j] = paramArrayOfint[j] - m;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\BlockColumnFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */