/*    */ package net.minecraft.world.entity.animal.fish;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.pathfinder.PathType;
/*    */ 
/*    */ public abstract class WaterAnimal
/*    */   extends PathfinderMob
/*    */ {
/*    */   public static final int AMBIENT_SOUND_INTERVAL = 120;
/*    */   
/*    */   protected WaterAnimal(EntityType<? extends WaterAnimal> paramEntityType, Level paramLevel) {
/* 23 */     super(paramEntityType, paramLevel);
/*    */     
/* 25 */     setPathfindingMalus(PathType.WATER, 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean checkSpawnObstruction(LevelReader paramLevelReader) {
/* 30 */     return paramLevelReader.isUnobstructed((Entity)this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getAmbientSoundInterval() {
/* 35 */     return 120;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBaseExperienceReward(ServerLevel paramServerLevel) {
/* 40 */     return 1 + this.random.nextInt(3);
/*    */   }
/*    */   
/*    */   protected void handleAirSupply(ServerLevel paramServerLevel, int paramInt) {
/* 44 */     if (isAlive() && !isInWater()) {
/* 45 */       setAirSupply(paramInt - 1);
/* 46 */       if (shouldTakeDrowningDamage()) {
/* 47 */         setAirSupply(0);
/* 48 */         hurtServer(paramServerLevel, damageSources().drown(), 2.0F);
/*    */       } 
/*    */     } else {
/* 51 */       setAirSupply(300);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void baseTick() {
/* 57 */     int i = getAirSupply();
/* 58 */     super.baseTick();
/* 59 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 60 */       handleAirSupply(serverLevel, i); }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPushedByFluid() {
/* 67 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBeLeashed() {
/* 72 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<? extends WaterAnimal> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 76 */     int i = paramLevelAccessor.getSeaLevel();
/* 77 */     int j = i - 13;
/* 78 */     return (paramBlockPos.getY() >= j && paramBlockPos
/* 79 */       .getY() <= i && paramLevelAccessor
/* 80 */       .getFluidState(paramBlockPos.below()).is(FluidTags.WATER) && paramLevelAccessor
/* 81 */       .getBlockState(paramBlockPos.above()).is(Blocks.WATER));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\fish\WaterAnimal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */