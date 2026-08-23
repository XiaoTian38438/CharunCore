/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.features.NetherFeatures;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public class NyliumBlock extends Block implements BonemealableBlock {
/* 20 */   public static final MapCodec<NyliumBlock> CODEC = simpleCodec(NyliumBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<NyliumBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected NyliumBlock(BlockBehaviour.Properties paramProperties) {
/* 28 */     super(paramProperties);
/*    */   }
/*    */   
/*    */   private static boolean canBeNylium(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 32 */     BlockPos blockPos = paramBlockPos.above();
/* 33 */     BlockState blockState = paramLevelReader.getBlockState(blockPos);
/*    */ 
/*    */     
/* 36 */     int i = LightEngine.getLightBlockInto(paramBlockState, blockState, Direction.UP, blockState.getLightBlock());
/* 37 */     return (i < 15);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 42 */     if (!canBeNylium(paramBlockState, (LevelReader)paramServerLevel, paramBlockPos)) {
/* 43 */       paramServerLevel.setBlockAndUpdate(paramBlockPos, Blocks.NETHERRACK.defaultBlockState());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 49 */     return paramLevelReader.getBlockState(paramBlockPos.above()).isAir();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 54 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 59 */     BlockState blockState = paramServerLevel.getBlockState(paramBlockPos);
/* 60 */     BlockPos blockPos = paramBlockPos.above();
/* 61 */     ChunkGenerator chunkGenerator = paramServerLevel.getChunkSource().getGenerator();
/* 62 */     Registry<ConfiguredFeature<?, ?>> registry = paramServerLevel.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE);
/* 63 */     if (blockState.is(Blocks.CRIMSON_NYLIUM)) {
/* 64 */       place(registry, NetherFeatures.CRIMSON_FOREST_VEGETATION_BONEMEAL, paramServerLevel, chunkGenerator, paramRandomSource, blockPos);
/* 65 */     } else if (blockState.is(Blocks.WARPED_NYLIUM)) {
/* 66 */       place(registry, NetherFeatures.WARPED_FOREST_VEGETATION_BONEMEAL, paramServerLevel, chunkGenerator, paramRandomSource, blockPos);
/* 67 */       place(registry, NetherFeatures.NETHER_SPROUTS_BONEMEAL, paramServerLevel, chunkGenerator, paramRandomSource, blockPos);
/* 68 */       if (paramRandomSource.nextInt(8) == 0) {
/* 69 */         place(registry, NetherFeatures.TWISTING_VINES_BONEMEAL, paramServerLevel, chunkGenerator, paramRandomSource, blockPos);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private void place(Registry<ConfiguredFeature<?, ?>> paramRegistry, ResourceKey<ConfiguredFeature<?, ?>> paramResourceKey, ServerLevel paramServerLevel, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 75 */     paramRegistry.get(paramResourceKey).ifPresent(paramReference -> ((ConfiguredFeature)paramReference.value()).place((WorldGenLevel)paramServerLevel, paramChunkGenerator, paramRandomSource, paramBlockPos));
/*    */   }
/*    */ 
/*    */   
/*    */   public BonemealableBlock.Type getType() {
/* 80 */     return BonemealableBlock.Type.NEIGHBOR_SPREADER;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\NyliumBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */