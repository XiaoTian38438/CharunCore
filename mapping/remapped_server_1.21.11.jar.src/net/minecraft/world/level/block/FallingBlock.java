/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.particles.BlockParticleOption;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.ParticleUtils;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class FallingBlock
/*    */   extends Block
/*    */   implements Fallable {
/*    */   public FallingBlock(BlockBehaviour.Properties paramProperties) {
/* 25 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends FallingBlock> codec();
/*    */ 
/*    */   
/*    */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 33 */     paramLevel.scheduleTick(paramBlockPos, this, getDelayAfterPlace());
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 38 */     paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, getDelayAfterPlace());
/*    */     
/* 40 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 45 */     if (!isFree(paramServerLevel.getBlockState(paramBlockPos.below())) || paramBlockPos.getY() < paramServerLevel.getMinY()) {
/*    */       return;
/*    */     }
/*    */     
/* 49 */     FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall((Level)paramServerLevel, paramBlockPos, paramBlockState);
/* 50 */     falling(fallingBlockEntity);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void falling(FallingBlockEntity paramFallingBlockEntity) {}
/*    */   
/*    */   protected int getDelayAfterPlace() {
/* 57 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isFree(BlockState paramBlockState) {
/* 62 */     return (paramBlockState.isAir() || paramBlockState.is(BlockTags.FIRE) || paramBlockState.liquid() || paramBlockState.canBeReplaced());
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 67 */     if (paramRandomSource.nextInt(16) == 0) {
/* 68 */       BlockPos blockPos = paramBlockPos.below();
/*    */       
/* 70 */       if (isFree(paramLevel.getBlockState(blockPos)))
/* 71 */         ParticleUtils.spawnParticleBelow(paramLevel, paramBlockPos, paramRandomSource, (ParticleOptions)new BlockParticleOption(ParticleTypes.FALLING_DUST, paramBlockState)); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract int getDustColor(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\FallingBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */