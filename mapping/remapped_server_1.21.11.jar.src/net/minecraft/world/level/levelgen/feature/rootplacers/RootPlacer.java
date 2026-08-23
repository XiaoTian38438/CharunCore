/*    */ package net.minecraft.world.level.levelgen.feature.rootplacers;
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public abstract class RootPlacer {
/* 22 */   public static final Codec<RootPlacer> CODEC = BuiltInRegistries.ROOT_PLACER_TYPE.byNameCodec().dispatch(RootPlacer::type, RootPlacerType::codec);
/*    */   
/*    */   protected final IntProvider trunkOffsetY;
/*    */   protected final BlockStateProvider rootProvider;
/*    */   protected final Optional<AboveRootPlacement> aboveRootPlacement;
/*    */   
/*    */   protected static <P extends RootPlacer> Products.P3<RecordCodecBuilder.Mu<P>, IntProvider, BlockStateProvider, Optional<AboveRootPlacement>> rootPlacerParts(RecordCodecBuilder.Instance<P> paramInstance) {
/* 29 */     return paramInstance.group((App)IntProvider.CODEC
/* 30 */         .fieldOf("trunk_offset_y").forGetter(paramRootPlacer -> paramRootPlacer.trunkOffsetY), (App)BlockStateProvider.CODEC
/* 31 */         .fieldOf("root_provider").forGetter(paramRootPlacer -> paramRootPlacer.rootProvider), (App)AboveRootPlacement.CODEC
/* 32 */         .optionalFieldOf("above_root_placement").forGetter(paramRootPlacer -> paramRootPlacer.aboveRootPlacement));
/*    */   }
/*    */ 
/*    */   
/*    */   public RootPlacer(IntProvider paramIntProvider, BlockStateProvider paramBlockStateProvider, Optional<AboveRootPlacement> paramOptional) {
/* 37 */     this.trunkOffsetY = paramIntProvider;
/* 38 */     this.rootProvider = paramBlockStateProvider;
/* 39 */     this.aboveRootPlacement = paramOptional;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean canPlaceRoot(LevelSimulatedReader paramLevelSimulatedReader, BlockPos paramBlockPos) {
/* 47 */     return TreeFeature.validTreePos(paramLevelSimulatedReader, paramBlockPos);
/*    */   }
/*    */   
/*    */   protected void placeRoot(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration) {
/* 51 */     if (!canPlaceRoot(paramLevelSimulatedReader, paramBlockPos)) {
/*    */       return;
/*    */     }
/* 54 */     paramBiConsumer.accept(paramBlockPos, getPotentiallyWaterloggedState(paramLevelSimulatedReader, paramBlockPos, this.rootProvider.getState(paramRandomSource, paramBlockPos)));
/* 55 */     if (this.aboveRootPlacement.isPresent()) {
/* 56 */       AboveRootPlacement aboveRootPlacement = this.aboveRootPlacement.get();
/* 57 */       BlockPos blockPos = paramBlockPos.above();
/* 58 */       if (paramRandomSource.nextFloat() < aboveRootPlacement.aboveRootPlacementChance() && paramLevelSimulatedReader.isStateAtPosition(blockPos, BlockBehaviour.BlockStateBase::isAir)) {
/* 59 */         paramBiConsumer.accept(blockPos, getPotentiallyWaterloggedState(paramLevelSimulatedReader, blockPos, aboveRootPlacement.aboveRootProvider().getState(paramRandomSource, blockPos)));
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   protected BlockState getPotentiallyWaterloggedState(LevelSimulatedReader paramLevelSimulatedReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 65 */     if (paramBlockState.hasProperty((Property)BlockStateProperties.WATERLOGGED)) {
/* 66 */       boolean bool = paramLevelSimulatedReader.isFluidAtPosition(paramBlockPos, paramFluidState -> paramFluidState.is(FluidTags.WATER));
/* 67 */       return (BlockState)paramBlockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(bool));
/*    */     } 
/* 69 */     return paramBlockState;
/*    */   }
/*    */   
/*    */   public BlockPos getTrunkOrigin(BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 73 */     return paramBlockPos.above(this.trunkOffsetY.sample(paramRandomSource));
/*    */   }
/*    */   
/*    */   protected abstract RootPlacerType<?> type();
/*    */   
/*    */   public abstract boolean placeRoots(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, BlockPos paramBlockPos1, BlockPos paramBlockPos2, TreeConfiguration paramTreeConfiguration);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\rootplacers\RootPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */