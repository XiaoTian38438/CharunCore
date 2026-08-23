/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalInt;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Column;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.UnderwaterMagmaConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnderwaterMagmaFeature
/*    */   extends Feature<UnderwaterMagmaConfiguration>
/*    */ {
/*    */   public UnderwaterMagmaFeature(Codec<UnderwaterMagmaConfiguration> paramCodec) {
/* 30 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<UnderwaterMagmaConfiguration> paramFeaturePlaceContext) {
/* 35 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 36 */     BlockPos blockPos1 = paramFeaturePlaceContext.origin();
/* 37 */     UnderwaterMagmaConfiguration underwaterMagmaConfiguration = paramFeaturePlaceContext.config();
/* 38 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/*    */     
/* 40 */     OptionalInt optionalInt = getFloorY(worldGenLevel, blockPos1, underwaterMagmaConfiguration);
/* 41 */     if (optionalInt.isEmpty()) {
/* 42 */       return false;
/*    */     }
/* 44 */     BlockPos blockPos2 = blockPos1.atY(optionalInt.getAsInt());
/*    */     
/* 46 */     Vec3i vec3i = new Vec3i(underwaterMagmaConfiguration.placementRadiusAroundFloor, underwaterMagmaConfiguration.placementRadiusAroundFloor, underwaterMagmaConfiguration.placementRadiusAroundFloor);
/* 47 */     BoundingBox boundingBox = BoundingBox.fromCorners((Vec3i)blockPos2.subtract(vec3i), (Vec3i)blockPos2.offset(vec3i));
/* 48 */     return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 54 */       (BlockPos.betweenClosedStream(boundingBox).filter(paramBlockPos -> (paramRandomSource.nextFloat() < paramUnderwaterMagmaConfiguration.placementProbabilityPerValidPosition)).filter(paramBlockPos -> isValidPlacement(paramWorldGenLevel, paramBlockPos)).mapToInt(paramBlockPos -> { paramWorldGenLevel.setBlock(paramBlockPos, Blocks.MAGMA_BLOCK.defaultBlockState(), 2); return 1; }).sum() > 0);
/*    */   }
/*    */   
/*    */   private static OptionalInt getFloorY(WorldGenLevel paramWorldGenLevel, BlockPos paramBlockPos, UnderwaterMagmaConfiguration paramUnderwaterMagmaConfiguration) {
/* 58 */     Predicate predicate1 = paramBlockState -> paramBlockState.is(Blocks.WATER);
/* 59 */     Predicate predicate2 = paramBlockState -> !paramBlockState.is(Blocks.WATER);
/* 60 */     Optional optional = Column.scan((LevelSimulatedReader)paramWorldGenLevel, paramBlockPos, paramUnderwaterMagmaConfiguration.floorSearchRange, predicate1, predicate2);
/* 61 */     return optional.map(Column::getFloor).orElseGet(OptionalInt::empty);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean isValidPlacement(WorldGenLevel paramWorldGenLevel, BlockPos paramBlockPos) {
/* 68 */     if (isWaterOrAir(paramWorldGenLevel.getBlockState(paramBlockPos)) || isVisibleFromOutside((LevelAccessor)paramWorldGenLevel, paramBlockPos.below(), Direction.UP)) {
/* 69 */       return false;
/*    */     }
/* 71 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 72 */       if (isVisibleFromOutside((LevelAccessor)paramWorldGenLevel, paramBlockPos.relative(direction), direction.getOpposite())) {
/* 73 */         return false;
/*    */       }
/*    */     } 
/* 76 */     return true;
/*    */   }
/*    */   
/*    */   private static boolean isWaterOrAir(BlockState paramBlockState) {
/* 80 */     return (paramBlockState.is(Blocks.WATER) || paramBlockState.isAir());
/*    */   }
/*    */   
/*    */   private boolean isVisibleFromOutside(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, Direction paramDirection) {
/* 84 */     BlockState blockState = paramLevelAccessor.getBlockState(paramBlockPos);
/* 85 */     VoxelShape voxelShape = blockState.getFaceOcclusionShape(paramDirection);
/* 86 */     return (voxelShape == Shapes.empty() || !Block.isShapeFullBlock(voxelShape));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\UnderwaterMagmaFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */