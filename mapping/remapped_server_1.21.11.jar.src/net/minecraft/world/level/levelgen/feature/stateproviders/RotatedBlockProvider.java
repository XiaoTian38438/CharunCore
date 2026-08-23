/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.RotatedPillarBlock;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RotatedBlockProvider extends BlockStateProvider {
/*    */   public static final MapCodec<RotatedBlockProvider> CODEC;
/*    */   
/*    */   static {
/* 14 */     CODEC = BlockState.CODEC.fieldOf("state").xmap(BlockBehaviour.BlockStateBase::getBlock, Block::defaultBlockState).xmap(RotatedBlockProvider::new, paramRotatedBlockProvider -> paramRotatedBlockProvider.block);
/*    */   }
/*    */   private final Block block;
/*    */   
/*    */   public RotatedBlockProvider(Block paramBlock) {
/* 19 */     this.block = paramBlock;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 24 */     return BlockStateProviderType.ROTATED_BLOCK_PROVIDER;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getState(RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 29 */     Direction.Axis axis = Direction.Axis.getRandom(paramRandomSource);
/* 30 */     return (BlockState)this.block.defaultBlockState().trySetValue((Property)RotatedPillarBlock.AXIS, (Comparable)axis);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\RotatedBlockProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */