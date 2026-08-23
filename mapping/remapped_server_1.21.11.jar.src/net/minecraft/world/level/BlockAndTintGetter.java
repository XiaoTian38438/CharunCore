/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ 
/*    */ public interface BlockAndTintGetter
/*    */   extends BlockGetter {
/*    */   float getShade(Direction paramDirection, boolean paramBoolean);
/*    */   
/*    */   LevelLightEngine getLightEngine();
/*    */   
/*    */   int getBlockTint(BlockPos paramBlockPos, ColorResolver paramColorResolver);
/*    */   
/*    */   default int getBrightness(LightLayer paramLightLayer, BlockPos paramBlockPos) {
/* 16 */     return getLightEngine().getLayerListener(paramLightLayer).getLightValue(paramBlockPos);
/*    */   }
/*    */   
/*    */   default int getRawBrightness(BlockPos paramBlockPos, int paramInt) {
/* 20 */     return getLightEngine().getRawBrightness(paramBlockPos, paramInt);
/*    */   }
/*    */   
/*    */   default boolean canSeeSky(BlockPos paramBlockPos) {
/* 24 */     return (getBrightness(LightLayer.SKY, paramBlockPos) >= 15);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\BlockAndTintGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */