/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import it.unimi.dsi.fastutil.ints.IntListIterator;
/*    */ import java.util.stream.IntStream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.RandomizableContainer;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ 
/*    */ public class BonusChestFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public BonusChestFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/* 23 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/* 28 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 29 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 30 */     ChunkPos chunkPos = new ChunkPos(paramFeaturePlaceContext.origin());
/* 31 */     IntArrayList intArrayList1 = Util.toShuffledList(IntStream.rangeClosed(chunkPos.getMinBlockX(), chunkPos.getMaxBlockX()), randomSource);
/* 32 */     IntArrayList intArrayList2 = Util.toShuffledList(IntStream.rangeClosed(chunkPos.getMinBlockZ(), chunkPos.getMaxBlockZ()), randomSource);
/* 33 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*    */     
/* 35 */     for (IntListIterator<Integer> intListIterator = intArrayList1.iterator(); intListIterator.hasNext(); ) { Integer integer = intListIterator.next();
/* 36 */       for (IntListIterator<Integer> intListIterator1 = intArrayList2.iterator(); intListIterator1.hasNext(); ) { Integer integer1 = intListIterator1.next();
/* 37 */         mutableBlockPos.set(integer.intValue(), 0, integer1.intValue());
/* 38 */         BlockPos blockPos = worldGenLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (BlockPos)mutableBlockPos);
/*    */         
/* 40 */         if (worldGenLevel.isEmptyBlock(blockPos) || worldGenLevel.getBlockState(blockPos).getCollisionShape((BlockGetter)worldGenLevel, blockPos).isEmpty()) {
/* 41 */           worldGenLevel.setBlock(blockPos, Blocks.CHEST.defaultBlockState(), 2);
/*    */           
/* 43 */           RandomizableContainer.setBlockEntityLootTable((BlockGetter)worldGenLevel, randomSource, blockPos, BuiltInLootTables.SPAWN_BONUS_CHEST);
/*    */           
/* 45 */           BlockState blockState = Blocks.TORCH.defaultBlockState();
/*    */           
/* 47 */           for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 48 */             BlockPos blockPos1 = blockPos.relative(direction);
/* 49 */             if (blockState.canSurvive((LevelReader)worldGenLevel, blockPos1)) {
/* 50 */               worldGenLevel.setBlock(blockPos1, blockState, 2);
/*    */             }
/*    */           } 
/* 53 */           return true;
/*    */         }  }
/*    */        }
/*    */ 
/*    */     
/* 58 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\BonusChestFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */