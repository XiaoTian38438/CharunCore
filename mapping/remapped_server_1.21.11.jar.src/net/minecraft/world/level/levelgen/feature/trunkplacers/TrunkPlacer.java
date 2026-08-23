/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ 
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public abstract class TrunkPlacer {
/* 23 */   public static final Codec<TrunkPlacer> CODEC = BuiltInRegistries.TRUNK_PLACER_TYPE.byNameCodec().dispatch(TrunkPlacer::type, TrunkPlacerType::codec);
/*    */   
/*    */   private static final int MAX_BASE_HEIGHT = 32;
/*    */   private static final int MAX_RAND = 24;
/*    */   public static final int MAX_HEIGHT = 80;
/*    */   
/*    */   protected static <P extends TrunkPlacer> Products.P3<RecordCodecBuilder.Mu<P>, Integer, Integer, Integer> trunkPlacerParts(RecordCodecBuilder.Instance<P> paramInstance) {
/* 30 */     return paramInstance.group(
/* 31 */         (App)Codec.intRange(0, 32).fieldOf("base_height").forGetter(paramTrunkPlacer -> Integer.valueOf(paramTrunkPlacer.baseHeight)), 
/* 32 */         (App)Codec.intRange(0, 24).fieldOf("height_rand_a").forGetter(paramTrunkPlacer -> Integer.valueOf(paramTrunkPlacer.heightRandA)), 
/* 33 */         (App)Codec.intRange(0, 24).fieldOf("height_rand_b").forGetter(paramTrunkPlacer -> Integer.valueOf(paramTrunkPlacer.heightRandB)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected final int baseHeight;
/*    */   protected final int heightRandA;
/*    */   protected final int heightRandB;
/*    */   
/*    */   public TrunkPlacer(int paramInt1, int paramInt2, int paramInt3) {
/* 42 */     this.baseHeight = paramInt1;
/* 43 */     this.heightRandA = paramInt2;
/* 44 */     this.heightRandB = paramInt3;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getTreeHeight(RandomSource paramRandomSource) {
/* 52 */     return this.baseHeight + paramRandomSource.nextInt(this.heightRandA + 1) + paramRandomSource.nextInt(this.heightRandB + 1);
/*    */   }
/*    */   
/*    */   private static boolean isDirt(LevelSimulatedReader paramLevelSimulatedReader, BlockPos paramBlockPos) {
/* 56 */     return paramLevelSimulatedReader.isStateAtPosition(paramBlockPos, paramBlockState -> 
/* 57 */         (Feature.isDirt(paramBlockState) && !paramBlockState.is(Blocks.GRASS_BLOCK) && !paramBlockState.is(Blocks.MYCELIUM)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected static void setDirtAt(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration) {
/* 64 */     if (paramTreeConfiguration.forceDirt || !isDirt(paramLevelSimulatedReader, paramBlockPos))
/*    */     {
/* 66 */       paramBiConsumer.accept(paramBlockPos, paramTreeConfiguration.dirtProvider.getState(paramRandomSource, paramBlockPos));
/*    */     }
/*    */   }
/*    */   
/*    */   protected boolean placeLog(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration) {
/* 71 */     return placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, paramBlockPos, paramTreeConfiguration, Function.identity());
/*    */   }
/*    */   
/*    */   protected boolean placeLog(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration, Function<BlockState, BlockState> paramFunction) {
/* 75 */     if (validTreePos(paramLevelSimulatedReader, paramBlockPos)) {
/* 76 */       paramBiConsumer.accept(paramBlockPos, paramFunction.apply(paramTreeConfiguration.trunkProvider.getState(paramRandomSource, paramBlockPos)));
/*    */       
/* 78 */       return true;
/*    */     } 
/* 80 */     return false;
/*    */   }
/*    */   
/*    */   protected void placeLogIfFree(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, BlockPos.MutableBlockPos paramMutableBlockPos, TreeConfiguration paramTreeConfiguration) {
/* 84 */     if (isFree(paramLevelSimulatedReader, (BlockPos)paramMutableBlockPos)) {
/* 85 */       placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, (BlockPos)paramMutableBlockPos, paramTreeConfiguration);
/*    */     }
/*    */   }
/*    */   
/*    */   protected boolean validTreePos(LevelSimulatedReader paramLevelSimulatedReader, BlockPos paramBlockPos) {
/* 90 */     return TreeFeature.validTreePos(paramLevelSimulatedReader, paramBlockPos);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFree(LevelSimulatedReader paramLevelSimulatedReader, BlockPos paramBlockPos) {
/* 95 */     return (validTreePos(paramLevelSimulatedReader, paramBlockPos) || paramLevelSimulatedReader.isStateAtPosition(paramBlockPos, paramBlockState -> paramBlockState.is(BlockTags.LOGS)));
/*    */   }
/*    */   
/*    */   protected abstract TrunkPlacerType<?> type();
/*    */   
/*    */   public abstract List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, int paramInt, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\TrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */