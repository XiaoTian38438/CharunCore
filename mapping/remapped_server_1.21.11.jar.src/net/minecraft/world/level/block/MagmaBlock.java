/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MagmaBlock extends Block {
/* 16 */   public static final MapCodec<MagmaBlock> CODEC = simpleCodec(MagmaBlock::new);
/*    */   private static final int BUBBLE_COLUMN_CHECK_DELAY = 20;
/*    */   
/*    */   public MapCodec<MagmaBlock> codec() {
/* 20 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public MagmaBlock(BlockBehaviour.Properties paramProperties) {
/* 26 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stepOn(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Entity paramEntity) {
/* 31 */     if (!paramEntity.isSteppingCarefully() && paramEntity instanceof net.minecraft.world.entity.LivingEntity) {
/* 32 */       paramEntity.hurt(paramLevel.damageSources().hotFloor(), 1.0F);
/*    */     }
/*    */     
/* 35 */     super.stepOn(paramLevel, paramBlockPos, paramBlockState, paramEntity);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 40 */     BubbleColumnBlock.updateColumn((LevelAccessor)paramServerLevel, paramBlockPos.above(), paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 45 */     if (paramDirection == Direction.UP && paramBlockState2.is(Blocks.WATER)) {
/* 46 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 20);
/*    */     }
/*    */     
/* 49 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 54 */     paramLevel.scheduleTick(paramBlockPos, this, 20);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\MagmaBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */