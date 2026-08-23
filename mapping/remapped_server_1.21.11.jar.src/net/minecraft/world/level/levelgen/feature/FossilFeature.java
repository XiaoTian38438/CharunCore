/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.Mirror;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ import org.apache.commons.lang3.mutable.MutableInt;
/*    */ 
/*    */ public class FossilFeature extends Feature<FossilFeatureConfiguration> {
/*    */   public FossilFeature(Codec<FossilFeatureConfiguration> paramCodec) {
/* 25 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<FossilFeatureConfiguration> paramFeaturePlaceContext) {
/* 30 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 31 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 32 */     BlockPos blockPos1 = paramFeaturePlaceContext.origin();
/* 33 */     Rotation rotation = Rotation.getRandom(randomSource);
/* 34 */     FossilFeatureConfiguration fossilFeatureConfiguration = paramFeaturePlaceContext.config();
/*    */     
/* 36 */     int i = randomSource.nextInt(fossilFeatureConfiguration.fossilStructures.size());
/*    */ 
/*    */     
/* 39 */     StructureTemplateManager structureTemplateManager = worldGenLevel.getLevel().getServer().getStructureManager();
/* 40 */     StructureTemplate structureTemplate1 = structureTemplateManager.getOrCreate(fossilFeatureConfiguration.fossilStructures.get(i));
/* 41 */     StructureTemplate structureTemplate2 = structureTemplateManager.getOrCreate(fossilFeatureConfiguration.overlayStructures.get(i));
/* 42 */     ChunkPos chunkPos = new ChunkPos(blockPos1);
/*    */ 
/*    */     
/* 45 */     BoundingBox boundingBox = new BoundingBox(chunkPos.getMinBlockX() - 16, worldGenLevel.getMinY(), chunkPos.getMinBlockZ() - 16, chunkPos.getMaxBlockX() + 16, worldGenLevel.getMaxY(), chunkPos.getMaxBlockZ() + 16);
/*    */     
/* 47 */     StructurePlaceSettings structurePlaceSettings = (new StructurePlaceSettings()).setRotation(rotation).setBoundingBox(boundingBox).setRandom(randomSource);
/*    */     
/* 49 */     Vec3i vec3i = structureTemplate1.getSize(rotation);
/*    */     
/* 51 */     BlockPos blockPos2 = blockPos1.offset(-vec3i.getX() / 2, 0, -vec3i.getZ() / 2);
/*    */     
/* 53 */     int j = blockPos1.getY();
/*    */     int k;
/* 55 */     for (k = 0; k < vec3i.getX(); k++) {
/* 56 */       for (byte b = 0; b < vec3i.getZ(); b++) {
/* 57 */         j = Math.min(j, worldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, blockPos2.getX() + k, blockPos2.getZ() + b));
/*    */       }
/*    */     } 
/* 60 */     k = Math.max(j - 15 - randomSource.nextInt(10), worldGenLevel.getMinY() + 10);
/*    */     
/* 62 */     BlockPos blockPos3 = structureTemplate1.getZeroPositionWithTransform(blockPos2.atY(k), Mirror.NONE, rotation);
/*    */     
/* 64 */     if (countEmptyCorners(worldGenLevel, structureTemplate1.getBoundingBox(structurePlaceSettings, blockPos3)) > fossilFeatureConfiguration.maxEmptyCornersAllowed) {
/* 65 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 69 */     structurePlaceSettings.clearProcessors();
/* 70 */     Objects.requireNonNull(structurePlaceSettings); ((StructureProcessorList)fossilFeatureConfiguration.fossilProcessors.value()).list().forEach(structurePlaceSettings::addProcessor);
/* 71 */     structureTemplate1.placeInWorld((ServerLevelAccessor)worldGenLevel, blockPos3, blockPos3, structurePlaceSettings, randomSource, 260);
/*    */ 
/*    */     
/* 74 */     structurePlaceSettings.clearProcessors();
/* 75 */     Objects.requireNonNull(structurePlaceSettings); ((StructureProcessorList)fossilFeatureConfiguration.overlayProcessors.value()).list().forEach(structurePlaceSettings::addProcessor);
/* 76 */     structureTemplate2.placeInWorld((ServerLevelAccessor)worldGenLevel, blockPos3, blockPos3, structurePlaceSettings, randomSource, 260);
/*    */     
/* 78 */     return true;
/*    */   }
/*    */   
/*    */   private static int countEmptyCorners(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox) {
/* 82 */     MutableInt mutableInt = new MutableInt(0);
/* 83 */     paramBoundingBox.forAllCorners(paramBlockPos -> {
/*    */           BlockState blockState = paramWorldGenLevel.getBlockState(paramBlockPos);
/*    */           if (blockState.isAir() || blockState.is(Blocks.LAVA) || blockState.is(Blocks.WATER)) {
/*    */             paramMutableInt.add(1);
/*    */           }
/*    */         });
/* 89 */     return mutableInt.intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\FossilFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */