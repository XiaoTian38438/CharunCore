/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeightedStateProvider extends BlockStateProvider {
/*    */   public static final MapCodec<WeightedStateProvider> CODEC;
/*    */   
/*    */   static {
/* 11 */     CODEC = WeightedList.nonEmptyCodec(BlockState.CODEC).comapFlatMap(WeightedStateProvider::create, paramWeightedStateProvider -> paramWeightedStateProvider.weightedList).fieldOf("entries");
/*    */   }
/*    */   private final WeightedList<BlockState> weightedList;
/*    */   private static DataResult<WeightedStateProvider> create(WeightedList<BlockState> paramWeightedList) {
/* 15 */     if (paramWeightedList.isEmpty()) {
/* 16 */       return DataResult.error(() -> "WeightedStateProvider with no states");
/*    */     }
/* 18 */     return DataResult.success(new WeightedStateProvider(paramWeightedList));
/*    */   }
/*    */   
/*    */   public WeightedStateProvider(WeightedList<BlockState> paramWeightedList) {
/* 22 */     this.weightedList = paramWeightedList;
/*    */   }
/*    */   
/*    */   public WeightedStateProvider(WeightedList.Builder<BlockState> paramBuilder) {
/* 26 */     this(paramBuilder.build());
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 31 */     return BlockStateProviderType.WEIGHTED_STATE_PROVIDER;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getState(RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 36 */     return (BlockState)this.weightedList.getRandomOrThrow(paramRandomSource);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\WeightedStateProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */