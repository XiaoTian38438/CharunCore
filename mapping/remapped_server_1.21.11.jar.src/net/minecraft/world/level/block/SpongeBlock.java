/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.redstone.Orientation;
/*    */ 
/*    */ public class SpongeBlock extends Block {
/* 17 */   public static final MapCodec<SpongeBlock> CODEC = simpleCodec(SpongeBlock::new); public static final int MAX_DEPTH = 6;
/*    */   public static final int MAX_COUNT = 64;
/*    */   
/*    */   public MapCodec<SpongeBlock> codec() {
/* 21 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   private static final Direction[] ALL_DIRECTIONS = Direction.values();
/*    */   
/*    */   protected SpongeBlock(BlockBehaviour.Properties paramProperties) {
/* 30 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 35 */     if (paramBlockState2.is(paramBlockState1.getBlock())) {
/*    */       return;
/*    */     }
/* 38 */     tryAbsorbWater(paramLevel, paramBlockPos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 43 */     tryAbsorbWater(paramLevel, paramBlockPos);
/* 44 */     super.neighborChanged(paramBlockState, paramLevel, paramBlockPos, paramBlock, paramOrientation, paramBoolean);
/*    */   }
/*    */   
/*    */   protected void tryAbsorbWater(Level paramLevel, BlockPos paramBlockPos) {
/* 48 */     if (removeWaterBreadthFirstSearch(paramLevel, paramBlockPos)) {
/*    */       
/* 50 */       paramLevel.setBlock(paramBlockPos, Blocks.WET_SPONGE.defaultBlockState(), 2);
/* 51 */       paramLevel.playSound(null, paramBlockPos, SoundEvents.SPONGE_ABSORB, SoundSource.BLOCKS, 1.0F, 1.0F);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean removeWaterBreadthFirstSearch(Level paramLevel, BlockPos paramBlockPos) {
/* 58 */     return (BlockPos.breadthFirstTraversal(paramBlockPos, 6, 65, (paramBlockPos, paramConsumer) -> { for (Direction direction : ALL_DIRECTIONS) paramConsumer.accept(paramBlockPos.relative(direction));  }paramBlockPos2 -> { if (paramBlockPos2.equals(paramBlockPos1)) return BlockPos.TraversalNodeStatus.ACCEPT;  BlockState blockState = paramLevel.getBlockState(paramBlockPos2); FluidState fluidState = paramLevel.getFluidState(paramBlockPos2); if (!fluidState.is(FluidTags.WATER)) return BlockPos.TraversalNodeStatus.SKIP;  Block block = blockState.getBlock(); if (block instanceof BucketPickup) { BucketPickup bucketPickup = (BucketPickup)block; if (!bucketPickup.pickupBlock(null, (LevelAccessor)paramLevel, paramBlockPos2, blockState).isEmpty()) return BlockPos.TraversalNodeStatus.ACCEPT;  }  if (blockState.getBlock() instanceof LiquidBlock) { paramLevel.setBlock(paramBlockPos2, Blocks.AIR.defaultBlockState(), 3); } else if (blockState.is(Blocks.KELP) || blockState.is(Blocks.KELP_PLANT) || blockState.is(Blocks.SEAGRASS) || blockState.is(Blocks.TALL_SEAGRASS)) { BlockEntity blockEntity = blockState.hasBlockEntity() ? paramLevel.getBlockEntity(paramBlockPos2) : null; dropResources(blockState, (LevelAccessor)paramLevel, paramBlockPos2, blockEntity); paramLevel.setBlock(paramBlockPos2, Blocks.AIR.defaultBlockState(), 3); } else { return BlockPos.TraversalNodeStatus.SKIP; }  return BlockPos.TraversalNodeStatus.ACCEPT; }) > 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SpongeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */