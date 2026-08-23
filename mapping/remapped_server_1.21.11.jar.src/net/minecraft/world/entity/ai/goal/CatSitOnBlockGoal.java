/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.animal.feline.Cat;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.BedBlock;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BedPart;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class CatSitOnBlockGoal extends MoveToBlockGoal {
/*    */   public CatSitOnBlockGoal(Cat paramCat, double paramDouble) {
/* 18 */     super((PathfinderMob)paramCat, paramDouble, 8);
/* 19 */     this.cat = paramCat;
/*    */   }
/*    */   private final Cat cat;
/*    */   
/*    */   public boolean canUse() {
/* 24 */     return (this.cat.isTame() && !this.cat.isOrderedToSit() && super.canUse());
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 29 */     super.start();
/* 30 */     this.cat.setInSittingPose(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 35 */     super.stop();
/* 36 */     this.cat.setInSittingPose(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 41 */     super.tick();
/*    */     
/* 43 */     this.cat.setInSittingPose(isReachedTarget());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isValidTarget(LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 48 */     if (!paramLevelReader.isEmptyBlock(paramBlockPos.above())) {
/* 49 */       return false;
/*    */     }
/*    */     
/* 52 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos);
/*    */ 
/*    */     
/* 55 */     if (blockState.is(Blocks.CHEST))
/* 56 */       return (ChestBlockEntity.getOpenCount((BlockGetter)paramLevelReader, paramBlockPos) < 1); 
/* 57 */     if (blockState.is(Blocks.FURNACE) && ((Boolean)blockState.getValue((Property)FurnaceBlock.LIT)).booleanValue()) {
/* 58 */       return true;
/*    */     }
/* 60 */     return blockState.is(BlockTags.BEDS, paramBlockStateBase -> ((Boolean)paramBlockStateBase.getOptionalValue((Property)BedBlock.PART).map(()).orElse(Boolean.valueOf(true))).booleanValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\CatSitOnBlockGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */