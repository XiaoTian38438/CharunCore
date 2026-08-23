/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SimpleStateProvider extends BlockStateProvider {
/*    */   public static final MapCodec<SimpleStateProvider> CODEC;
/*    */   
/*    */   static {
/*  9 */     CODEC = BlockState.CODEC.fieldOf("state").xmap(SimpleStateProvider::new, paramSimpleStateProvider -> paramSimpleStateProvider.state);
/*    */   }
/*    */   private final BlockState state;
/*    */   
/*    */   protected SimpleStateProvider(BlockState paramBlockState) {
/* 14 */     this.state = paramBlockState;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 19 */     return BlockStateProviderType.SIMPLE_STATE_PROVIDER;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getState(RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 24 */     return this.state;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\SimpleStateProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */