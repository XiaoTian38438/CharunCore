/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public class BonemealableFeaturePlacerBlock extends Block implements BonemealableBlock {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("feature").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, BonemealableFeaturePlacerBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<BonemealableFeaturePlacerBlock> CODEC;
/*    */   
/*    */   private final ResourceKey<ConfiguredFeature<?, ?>> feature;
/*    */   
/*    */   public MapCodec<BonemealableFeaturePlacerBlock> codec() {
/* 25 */     return CODEC;
/*    */   }
/*    */   
/*    */   public BonemealableFeaturePlacerBlock(ResourceKey<ConfiguredFeature<?, ?>> paramResourceKey, BlockBehaviour.Properties paramProperties) {
/* 29 */     super(paramProperties);
/* 30 */     this.feature = paramResourceKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 35 */     return paramLevelReader.getBlockState(paramBlockPos.above()).isAir();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 40 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 45 */     paramServerLevel.registryAccess()
/* 46 */       .lookup(Registries.CONFIGURED_FEATURE)
/* 47 */       .flatMap(paramRegistry -> paramRegistry.get(this.feature))
/* 48 */       .ifPresent(paramReference -> ((ConfiguredFeature)paramReference.value()).place((WorldGenLevel)paramServerLevel, paramServerLevel.getChunkSource().getGenerator(), paramRandomSource, paramBlockPos.above()));
/*    */   }
/*    */ 
/*    */   
/*    */   public BonemealableBlock.Type getType() {
/* 53 */     return BonemealableBlock.Type.NEIGHBOR_SPREADER;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BonemealableFeaturePlacerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */