/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class BuddingAmethystBlock extends AmethystBlock {
/* 13 */   public static final MapCodec<BuddingAmethystBlock> CODEC = simpleCodec(BuddingAmethystBlock::new);
/*    */   public static final int GROWTH_CHANCE = 5;
/*    */   
/*    */   public MapCodec<BuddingAmethystBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private static final Direction[] DIRECTIONS = Direction.values();
/*    */   
/*    */   public BuddingAmethystBlock(BlockBehaviour.Properties paramProperties) {
/* 25 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 30 */     if (paramRandomSource.nextInt(5) != 0) {
/*    */       return;
/*    */     }
/*    */     
/* 34 */     Direction direction = DIRECTIONS[paramRandomSource.nextInt(DIRECTIONS.length)];
/* 35 */     BlockPos blockPos = paramBlockPos.relative(direction);
/* 36 */     BlockState blockState = paramServerLevel.getBlockState(blockPos);
/* 37 */     Block block = null;
/* 38 */     if (canClusterGrowAtState(blockState)) {
/* 39 */       block = Blocks.SMALL_AMETHYST_BUD;
/* 40 */     } else if (blockState.is(Blocks.SMALL_AMETHYST_BUD) && blockState.getValue((Property)AmethystClusterBlock.FACING) == direction) {
/* 41 */       block = Blocks.MEDIUM_AMETHYST_BUD;
/* 42 */     } else if (blockState.is(Blocks.MEDIUM_AMETHYST_BUD) && blockState.getValue((Property)AmethystClusterBlock.FACING) == direction) {
/* 43 */       block = Blocks.LARGE_AMETHYST_BUD;
/* 44 */     } else if (blockState.is(Blocks.LARGE_AMETHYST_BUD) && blockState.getValue((Property)AmethystClusterBlock.FACING) == direction) {
/* 45 */       block = Blocks.AMETHYST_CLUSTER;
/*    */     } 
/*    */     
/* 48 */     if (block != null) {
/*    */ 
/*    */       
/* 51 */       BlockState blockState1 = (BlockState)((BlockState)block.defaultBlockState().setValue((Property)AmethystClusterBlock.FACING, (Comparable)direction)).setValue((Property)AmethystClusterBlock.WATERLOGGED, Boolean.valueOf((blockState.getFluidState().getType() == Fluids.WATER)));
/* 52 */       paramServerLevel.setBlockAndUpdate(blockPos, blockState1);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static boolean canClusterGrowAtState(BlockState paramBlockState) {
/* 57 */     return (paramBlockState.isAir() || (paramBlockState.is(Blocks.WATER) && paramBlockState.getFluidState().getAmount() == 8));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BuddingAmethystBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */