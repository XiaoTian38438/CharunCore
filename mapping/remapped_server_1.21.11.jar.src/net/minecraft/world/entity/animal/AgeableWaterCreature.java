/*    */ package net.minecraft.world.entity.animal;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.AgeableMob;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.pathfinder.PathType;
/*    */ 
/*    */ public abstract class AgeableWaterCreature extends AgeableMob {
/*    */   protected AgeableWaterCreature(EntityType<? extends AgeableWaterCreature> paramEntityType, Level paramLevel) {
/* 19 */     super(paramEntityType, paramLevel);
/*    */     
/* 21 */     setPathfindingMalus(PathType.WATER, 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean checkSpawnObstruction(LevelReader paramLevelReader) {
/* 26 */     return paramLevelReader.isUnobstructed((Entity)this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getAmbientSoundInterval() {
/* 31 */     return 120;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBaseExperienceReward(ServerLevel paramServerLevel) {
/* 36 */     return 1 + this.random.nextInt(3);
/*    */   }
/*    */   
/*    */   protected void handleAirSupply(int paramInt) {
/* 40 */     if (isAlive() && !isInWater()) {
/* 41 */       setAirSupply(paramInt - 1);
/* 42 */       if (shouldTakeDrowningDamage()) {
/* 43 */         setAirSupply(0);
/* 44 */         hurt(damageSources().drown(), 2.0F);
/*    */       } 
/*    */     } else {
/* 47 */       setAirSupply(300);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void baseTick() {
/* 53 */     int i = getAirSupply();
/* 54 */     super.baseTick();
/* 55 */     handleAirSupply(i);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPushedByFluid() {
/* 61 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBeLeashed() {
/* 66 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean checkSurfaceAgeableWaterCreatureSpawnRules(EntityType<? extends AgeableWaterCreature> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 70 */     int i = paramLevelAccessor.getSeaLevel();
/* 71 */     int j = i - 13;
/* 72 */     return (paramBlockPos.getY() >= j && paramBlockPos
/* 73 */       .getY() <= i && paramLevelAccessor
/* 74 */       .getFluidState(paramBlockPos.below()).is(FluidTags.WATER) && paramLevelAccessor
/* 75 */       .getBlockState(paramBlockPos.above()).is(Blocks.WATER));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\AgeableWaterCreature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */