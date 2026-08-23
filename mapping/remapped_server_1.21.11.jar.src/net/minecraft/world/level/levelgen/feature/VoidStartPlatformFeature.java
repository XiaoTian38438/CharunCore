/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class VoidStartPlatformFeature
/*    */   extends Feature<NoneFeatureConfiguration> {
/* 12 */   private static final BlockPos PLATFORM_OFFSET = new BlockPos(8, 3, 8);
/* 13 */   private static final ChunkPos PLATFORM_ORIGIN_CHUNK = new ChunkPos(PLATFORM_OFFSET);
/*    */   private static final int PLATFORM_RADIUS = 16;
/*    */   private static final int PLATFORM_RADIUS_CHUNKS = 1;
/*    */   
/*    */   public VoidStartPlatformFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/* 18 */     super(paramCodec);
/*    */   }
/*    */   
/*    */   private static int checkerboardDistance(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 22 */     return Math.max(Math.abs(paramInt1 - paramInt3), Math.abs(paramInt2 - paramInt4));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/* 27 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 28 */     ChunkPos chunkPos = new ChunkPos(paramFeaturePlaceContext.origin());
/* 29 */     if (checkerboardDistance(chunkPos.x, chunkPos.z, PLATFORM_ORIGIN_CHUNK.x, PLATFORM_ORIGIN_CHUNK.z) > 1) {
/* 30 */       return true;
/*    */     }
/*    */     
/* 33 */     BlockPos blockPos = PLATFORM_OFFSET.atY(paramFeaturePlaceContext.origin().getY() + PLATFORM_OFFSET.getY());
/* 34 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 35 */     for (int i = chunkPos.getMinBlockZ(); i <= chunkPos.getMaxBlockZ(); i++) {
/* 36 */       for (int j = chunkPos.getMinBlockX(); j <= chunkPos.getMaxBlockX(); j++) {
/* 37 */         if (checkerboardDistance(blockPos.getX(), blockPos.getZ(), j, i) <= 16) {
/*    */ 
/*    */           
/* 40 */           mutableBlockPos.set(j, blockPos.getY(), i);
/* 41 */           if (mutableBlockPos.equals(blockPos)) {
/* 42 */             worldGenLevel.setBlock((BlockPos)mutableBlockPos, Blocks.COBBLESTONE.defaultBlockState(), 2);
/*    */           } else {
/* 44 */             worldGenLevel.setBlock((BlockPos)mutableBlockPos, Blocks.STONE.defaultBlockState(), 2);
/*    */           } 
/*    */         } 
/*    */       } 
/* 48 */     }  return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\VoidStartPlatformFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */