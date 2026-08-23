/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class FungusBlock extends VegetationBlock implements BonemealableBlock {
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("feature").forGetter(()), (App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("grows_on").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, FungusBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<FungusBlock> CODEC;
/*    */   private static final double BONEMEAL_SUCCESS_PROBABILITY = 0.4D;
/*    */   
/*    */   public MapCodec<FungusBlock> codec() {
/* 32 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 9.0D);
/*    */   
/*    */   private final Block requiredBlock;
/*    */   private final ResourceKey<ConfiguredFeature<?, ?>> feature;
/*    */   
/*    */   protected FungusBlock(ResourceKey<ConfiguredFeature<?, ?>> paramResourceKey, Block paramBlock, BlockBehaviour.Properties paramProperties) {
/* 43 */     super(paramProperties);
/* 44 */     this.feature = paramResourceKey;
/* 45 */     this.requiredBlock = paramBlock;
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 50 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 55 */     return (paramBlockState.is(BlockTags.NYLIUM) || paramBlockState.is(Blocks.MYCELIUM) || paramBlockState.is(Blocks.SOUL_SOIL) || super.mayPlaceOn(paramBlockState, paramBlockGetter, paramBlockPos));
/*    */   }
/*    */   
/*    */   private Optional<? extends Holder<ConfiguredFeature<?, ?>>> getFeature(LevelReader paramLevelReader) {
/* 59 */     return paramLevelReader.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(this.feature);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 65 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.below());
/* 66 */     return blockState.is(this.requiredBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 71 */     return (paramRandomSource.nextFloat() < 0.4D);
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 76 */     getFeature((LevelReader)paramServerLevel).ifPresent(paramHolder -> ((ConfiguredFeature)paramHolder.value()).place((WorldGenLevel)paramServerLevel, paramServerLevel.getChunkSource().getGenerator(), paramRandomSource, paramBlockPos));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\FungusBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */