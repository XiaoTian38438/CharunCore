/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BambooSaplingBlock extends Block implements BonemealableBlock {
/* 21 */   public static final MapCodec<BambooSaplingBlock> CODEC = simpleCodec(BambooSaplingBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<BambooSaplingBlock> codec() {
/* 25 */     return CODEC;
/*    */   }
/*    */   
/* 28 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 12.0D);
/*    */   
/*    */   public BambooSaplingBlock(BlockBehaviour.Properties paramProperties) {
/* 31 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 36 */     return SHAPE.move(paramBlockState.getOffset(paramBlockPos));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 41 */     if (paramRandomSource.nextInt(3) == 0 && paramServerLevel.isEmptyBlock(paramBlockPos.above()) && paramServerLevel.getRawBrightness(paramBlockPos.above(), 0) >= 9) {
/* 42 */       growBamboo((Level)paramServerLevel, paramBlockPos);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 48 */     return paramLevelReader.getBlockState(paramBlockPos.below()).is(BlockTags.BAMBOO_PLANTABLE_ON);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 53 */     if (!paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 54 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 57 */     if (paramDirection == Direction.UP && paramBlockState2.is(Blocks.BAMBOO)) {
/* 58 */       return Blocks.BAMBOO.defaultBlockState();
/*    */     }
/*    */     
/* 61 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 66 */     return new ItemStack((ItemLike)Items.BAMBOO);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 71 */     return paramLevelReader.getBlockState(paramBlockPos.above()).isAir();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 76 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 81 */     growBamboo((Level)paramServerLevel, paramBlockPos);
/*    */   }
/*    */   
/*    */   protected void growBamboo(Level paramLevel, BlockPos paramBlockPos) {
/* 85 */     paramLevel.setBlock(paramBlockPos.above(), (BlockState)Blocks.BAMBOO.defaultBlockState().setValue((Property)BambooStalkBlock.LEAVES, (Comparable)BambooLeaves.SMALL), 3);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BambooSaplingBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */