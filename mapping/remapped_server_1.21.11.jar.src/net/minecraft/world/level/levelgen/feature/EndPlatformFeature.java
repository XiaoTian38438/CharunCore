/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class EndPlatformFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public EndPlatformFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/* 12 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/* 17 */     createEndPlatform((ServerLevelAccessor)paramFeaturePlaceContext.level(), paramFeaturePlaceContext.origin(), false);
/* 18 */     return true;
/*    */   }
/*    */   
/*    */   public static void createEndPlatform(ServerLevelAccessor paramServerLevelAccessor, BlockPos paramBlockPos, boolean paramBoolean) {
/* 22 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/*    */     
/* 24 */     for (byte b = -2; b <= 2; b++) {
/* 25 */       for (byte b1 = -2; b1 <= 2; b1++) {
/* 26 */         for (byte b2 = -1; b2 < 3; b2++) {
/* 27 */           BlockPos.MutableBlockPos mutableBlockPos1 = mutableBlockPos.set((Vec3i)paramBlockPos).move(b1, b2, b);
/* 28 */           Block block = (b2 == -1) ? Blocks.OBSIDIAN : Blocks.AIR;
/* 29 */           if (!paramServerLevelAccessor.getBlockState((BlockPos)mutableBlockPos1).is(block)) {
/* 30 */             if (paramBoolean) {
/* 31 */               paramServerLevelAccessor.destroyBlock((BlockPos)mutableBlockPos1, true, null);
/*    */             }
/* 33 */             paramServerLevelAccessor.setBlock((BlockPos)mutableBlockPos1, block.defaultBlockState(), 3);
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\EndPlatformFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */