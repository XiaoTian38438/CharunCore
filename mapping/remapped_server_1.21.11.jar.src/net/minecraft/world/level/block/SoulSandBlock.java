/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SoulSandBlock extends Block {
/* 19 */   public static final MapCodec<SoulSandBlock> CODEC = simpleCodec(SoulSandBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<SoulSandBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */   
/* 26 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 14.0D);
/*    */   
/*    */   private static final int BUBBLE_COLUMN_CHECK_DELAY = 20;
/*    */   
/*    */   public SoulSandBlock(BlockBehaviour.Properties paramProperties) {
/* 31 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 36 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getBlockSupportShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 41 */     return Shapes.block();
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getVisualShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 46 */     return Shapes.block();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 51 */     BubbleColumnBlock.updateColumn((LevelAccessor)paramServerLevel, paramBlockPos.above(), paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 56 */     if (paramDirection == Direction.UP && paramBlockState2.is(Blocks.WATER)) {
/* 57 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 20);
/*    */     }
/*    */     
/* 60 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 65 */     paramLevel.scheduleTick(paramBlockPos, this, 20);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 70 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getShadeBrightness(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 75 */     return 0.2F;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SoulSandBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */