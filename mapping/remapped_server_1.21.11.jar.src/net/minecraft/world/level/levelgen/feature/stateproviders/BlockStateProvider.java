/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class BlockStateProvider {
/* 11 */   public static final Codec<BlockStateProvider> CODEC = BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE.byNameCodec().dispatch(BlockStateProvider::type, BlockStateProviderType::codec);
/*    */   
/*    */   public static SimpleStateProvider simple(BlockState paramBlockState) {
/* 14 */     return new SimpleStateProvider(paramBlockState);
/*    */   }
/*    */   
/*    */   public static SimpleStateProvider simple(Block paramBlock) {
/* 18 */     return new SimpleStateProvider(paramBlock.defaultBlockState());
/*    */   }
/*    */   
/*    */   protected abstract BlockStateProviderType<?> type();
/*    */   
/*    */   public abstract BlockState getState(RandomSource paramRandomSource, BlockPos paramBlockPos);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\BlockStateProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */