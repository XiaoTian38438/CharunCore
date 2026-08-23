/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SnowyDirtBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class SnowAndFreezeFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public SnowAndFreezeFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/* 17 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/* 22 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 23 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 24 */     BlockPos.MutableBlockPos mutableBlockPos1 = new BlockPos.MutableBlockPos();
/* 25 */     BlockPos.MutableBlockPos mutableBlockPos2 = new BlockPos.MutableBlockPos();
/*    */     
/* 27 */     for (byte b = 0; b < 16; b++) {
/* 28 */       for (byte b1 = 0; b1 < 16; b1++) {
/* 29 */         int i = blockPos.getX() + b;
/* 30 */         int j = blockPos.getZ() + b1;
/* 31 */         int k = worldGenLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, i, j);
/*    */         
/* 33 */         mutableBlockPos1.set(i, k, j);
/* 34 */         mutableBlockPos2.set((Vec3i)mutableBlockPos1).move(Direction.DOWN, 1);
/*    */         
/* 36 */         Biome biome = (Biome)worldGenLevel.getBiome((BlockPos)mutableBlockPos1).value();
/*    */         
/* 38 */         if (biome.shouldFreeze((LevelReader)worldGenLevel, (BlockPos)mutableBlockPos2, false)) {
/* 39 */           worldGenLevel.setBlock((BlockPos)mutableBlockPos2, Blocks.ICE.defaultBlockState(), 2);
/*    */         }
/* 41 */         if (biome.shouldSnow((LevelReader)worldGenLevel, (BlockPos)mutableBlockPos1)) {
/* 42 */           worldGenLevel.setBlock((BlockPos)mutableBlockPos1, Blocks.SNOW.defaultBlockState(), 2);
/*    */           
/* 44 */           BlockState blockState = worldGenLevel.getBlockState((BlockPos)mutableBlockPos2);
/* 45 */           if (blockState.hasProperty((Property)SnowyDirtBlock.SNOWY)) {
/* 46 */             worldGenLevel.setBlock((BlockPos)mutableBlockPos2, (BlockState)blockState.setValue((Property)SnowyDirtBlock.SNOWY, Boolean.valueOf(true)), 2);
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/* 51 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\SnowAndFreezeFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */