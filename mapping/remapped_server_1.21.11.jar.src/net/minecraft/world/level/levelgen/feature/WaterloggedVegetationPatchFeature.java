/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
/*    */ 
/*    */ public class WaterloggedVegetationPatchFeature extends VegetationPatchFeature {
/*    */   public WaterloggedVegetationPatchFeature(Codec<VegetationPatchConfiguration> paramCodec) {
/* 21 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Set<BlockPos> placeGroundPatch(WorldGenLevel paramWorldGenLevel, VegetationPatchConfiguration paramVegetationPatchConfiguration, RandomSource paramRandomSource, BlockPos paramBlockPos, Predicate<BlockState> paramPredicate, int paramInt1, int paramInt2) {
/* 26 */     Set<BlockPos> set = super.placeGroundPatch(paramWorldGenLevel, paramVegetationPatchConfiguration, paramRandomSource, paramBlockPos, paramPredicate, paramInt1, paramInt2);
/* 27 */     HashSet<BlockPos> hashSet = new HashSet();
/* 28 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 29 */     for (BlockPos blockPos : set) {
/* 30 */       if (!isExposed(paramWorldGenLevel, set, blockPos, mutableBlockPos)) {
/* 31 */         hashSet.add(blockPos);
/*    */       }
/*    */     } 
/* 34 */     for (BlockPos blockPos : hashSet) {
/* 35 */       paramWorldGenLevel.setBlock(blockPos, Blocks.WATER.defaultBlockState(), 2);
/*    */     }
/* 37 */     return hashSet;
/*    */   }
/*    */   
/*    */   private static boolean isExposed(WorldGenLevel paramWorldGenLevel, Set<BlockPos> paramSet, BlockPos paramBlockPos, BlockPos.MutableBlockPos paramMutableBlockPos) {
/* 41 */     return (isExposedDirection(paramWorldGenLevel, paramBlockPos, paramMutableBlockPos, Direction.NORTH) || 
/* 42 */       isExposedDirection(paramWorldGenLevel, paramBlockPos, paramMutableBlockPos, Direction.EAST) || 
/* 43 */       isExposedDirection(paramWorldGenLevel, paramBlockPos, paramMutableBlockPos, Direction.SOUTH) || 
/* 44 */       isExposedDirection(paramWorldGenLevel, paramBlockPos, paramMutableBlockPos, Direction.WEST) || 
/* 45 */       isExposedDirection(paramWorldGenLevel, paramBlockPos, paramMutableBlockPos, Direction.DOWN));
/*    */   }
/*    */   
/*    */   private static boolean isExposedDirection(WorldGenLevel paramWorldGenLevel, BlockPos paramBlockPos, BlockPos.MutableBlockPos paramMutableBlockPos, Direction paramDirection) {
/* 49 */     paramMutableBlockPos.setWithOffset((Vec3i)paramBlockPos, paramDirection);
/* 50 */     return !paramWorldGenLevel.getBlockState((BlockPos)paramMutableBlockPos).isFaceSturdy((BlockGetter)paramWorldGenLevel, (BlockPos)paramMutableBlockPos, paramDirection.getOpposite());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean placeVegetation(WorldGenLevel paramWorldGenLevel, VegetationPatchConfiguration paramVegetationPatchConfiguration, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 55 */     if (super.placeVegetation(paramWorldGenLevel, paramVegetationPatchConfiguration, paramChunkGenerator, paramRandomSource, paramBlockPos.below())) {
/* 56 */       BlockState blockState = paramWorldGenLevel.getBlockState(paramBlockPos);
/* 57 */       if (blockState.hasProperty((Property)BlockStateProperties.WATERLOGGED) && !((Boolean)blockState.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue()) {
/* 58 */         paramWorldGenLevel.setBlock(paramBlockPos, (BlockState)blockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)), 2);
/*    */       }
/* 60 */       return true;
/*    */     } 
/* 62 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\WaterloggedVegetationPatchFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */