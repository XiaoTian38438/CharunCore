/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.VineBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class VinesFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public VinesFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/* 14 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/* 31 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 32 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 33 */     paramFeaturePlaceContext.config();
/* 34 */     if (!worldGenLevel.isEmptyBlock(blockPos)) {
/* 35 */       return false;
/*    */     }
/*    */     
/* 38 */     for (Direction direction : Direction.values()) {
/* 39 */       if (direction != Direction.DOWN)
/*    */       {
/*    */ 
/*    */         
/* 43 */         if (VineBlock.isAcceptableNeighbour((BlockGetter)worldGenLevel, blockPos.relative(direction), direction)) {
/* 44 */           worldGenLevel.setBlock(blockPos, (BlockState)Blocks.VINE.defaultBlockState().setValue((Property)VineBlock.getPropertyForFace(direction), Boolean.valueOf(true)), 2);
/* 45 */           return true;
/*    */         }  } 
/*    */     } 
/* 48 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\VinesFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */