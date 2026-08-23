/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.stats.ServerStatsCounter;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.SpawnGroupData;
/*    */ import net.minecraft.world.entity.monster.Phantom;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.CustomSpawner;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.NaturalSpawner;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class PhantomSpawner implements CustomSpawner {
/*    */   public void tick(ServerLevel paramServerLevel, boolean paramBoolean) {
/* 27 */     if (!paramBoolean) {
/*    */       return;
/*    */     }
/*    */     
/* 31 */     if (!((Boolean)paramServerLevel.getGameRules().get(GameRules.SPAWN_PHANTOMS)).booleanValue()) {
/*    */       return;
/*    */     }
/*    */     
/* 35 */     RandomSource randomSource = paramServerLevel.random;
/*    */     
/* 37 */     this.nextTick--;
/* 38 */     if (this.nextTick > 0) {
/*    */       return;
/*    */     }
/* 41 */     this.nextTick += (60 + randomSource.nextInt(60)) * 20;
/*    */     
/* 43 */     if (paramServerLevel.getSkyDarken() < 5 && paramServerLevel.dimensionType().hasSkyLight()) {
/*    */       return;
/*    */     }
/*    */     
/* 47 */     for (ServerPlayer serverPlayer : paramServerLevel.players()) {
/* 48 */       if (serverPlayer.isSpectator()) {
/*    */         continue;
/*    */       }
/* 51 */       BlockPos blockPos1 = serverPlayer.blockPosition();
/* 52 */       if (paramServerLevel.dimensionType().hasSkyLight() && (blockPos1.getY() < paramServerLevel.getSeaLevel() || !paramServerLevel.canSeeSky(blockPos1))) {
/*    */         continue;
/*    */       }
/* 55 */       DifficultyInstance difficultyInstance = paramServerLevel.getCurrentDifficultyAt(blockPos1);
/* 56 */       if (!difficultyInstance.isHarderThan(randomSource.nextFloat() * 3.0F)) {
/*    */         continue;
/*    */       }
/*    */       
/* 60 */       ServerStatsCounter serverStatsCounter = serverPlayer.getStats();
/* 61 */       int i = Mth.clamp(serverStatsCounter.getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)), 1, 2147483647);
/* 62 */       char c = '巀';
/* 63 */       if (randomSource.nextInt(i) < 72000) {
/*    */         continue;
/*    */       }
/*    */       
/* 67 */       BlockPos blockPos2 = blockPos1.above(20 + randomSource.nextInt(15)).east(-10 + randomSource.nextInt(21)).south(-10 + randomSource.nextInt(21));
/* 68 */       BlockState blockState = paramServerLevel.getBlockState(blockPos2);
/* 69 */       FluidState fluidState = paramServerLevel.getFluidState(blockPos2);
/* 70 */       if (!NaturalSpawner.isValidEmptySpawnBlock((BlockGetter)paramServerLevel, blockPos2, blockState, fluidState, EntityType.PHANTOM)) {
/*    */         continue;
/*    */       }
/*    */       
/* 74 */       SpawnGroupData spawnGroupData = null;
/* 75 */       int j = 1 + randomSource.nextInt(difficultyInstance.getDifficulty().getId() + 1);
/* 76 */       for (byte b = 0; b < j; b++) {
/* 77 */         Phantom phantom = (Phantom)EntityType.PHANTOM.create((Level)paramServerLevel, EntitySpawnReason.NATURAL);
/* 78 */         if (phantom != null) {
/* 79 */           phantom.snapTo(blockPos2, 0.0F, 0.0F);
/* 80 */           spawnGroupData = phantom.finalizeSpawn((ServerLevelAccessor)paramServerLevel, difficultyInstance, EntitySpawnReason.NATURAL, spawnGroupData);
/* 81 */           paramServerLevel.addFreshEntityWithPassengers((Entity)phantom);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private int nextTick;
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\PhantomSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */