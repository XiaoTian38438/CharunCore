/*     */ package net.minecraft.world.level.gameevent.vibrations;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.VibrationParticleOption;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Ticker
/*     */ {
/*     */   static void tick(Level paramLevel, VibrationSystem.Data paramData, VibrationSystem.User paramUser) {
/*     */     ServerLevel serverLevel;
/* 333 */     if (paramLevel instanceof ServerLevel) { serverLevel = (ServerLevel)paramLevel; }
/*     */     else
/*     */     { return; }
/*     */     
/* 337 */     if (paramData.currentVibration == null) {
/* 338 */       trySelectAndScheduleVibration(serverLevel, paramData, paramUser);
/*     */     }
/*     */     
/* 341 */     if (paramData.currentVibration == null) {
/*     */       return;
/*     */     }
/*     */     
/* 345 */     boolean bool = (paramData.getTravelTimeInTicks() > 0);
/* 346 */     tryReloadVibrationParticle(serverLevel, paramData, paramUser);
/* 347 */     paramData.decrementTravelTime();
/*     */     
/* 349 */     if (paramData.getTravelTimeInTicks() <= 0) {
/* 350 */       bool = receiveVibration(serverLevel, paramData, paramUser, paramData.currentVibration);
/*     */     }
/*     */     
/* 353 */     if (bool) {
/* 354 */       paramUser.onDataChanged();
/*     */     }
/*     */   }
/*     */   
/*     */   private static void trySelectAndScheduleVibration(ServerLevel paramServerLevel, VibrationSystem.Data paramData, VibrationSystem.User paramUser) {
/* 359 */     paramData.getSelectionStrategy().chosenCandidate(paramServerLevel.getGameTime()).ifPresent(paramVibrationInfo -> {
/*     */           paramData.setCurrentVibration(paramVibrationInfo);
/*     */           Vec3 vec3 = paramVibrationInfo.pos();
/*     */           paramData.setTravelTimeInTicks(paramUser.calculateTravelTimeInTicks(paramVibrationInfo.distance()));
/*     */           paramServerLevel.sendParticles((ParticleOptions)new VibrationParticleOption(paramUser.getPositionSource(), paramData.getTravelTimeInTicks()), vec3.x, vec3.y, vec3.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
/*     */           paramUser.onDataChanged();
/*     */           paramData.getSelectionStrategy().startOver();
/*     */         });
/*     */   }
/*     */   
/*     */   private static void tryReloadVibrationParticle(ServerLevel paramServerLevel, VibrationSystem.Data paramData, VibrationSystem.User paramUser) {
/* 370 */     if (!paramData.shouldReloadVibrationParticle()) {
/*     */       return;
/*     */     }
/*     */     
/* 374 */     if (paramData.currentVibration == null) {
/* 375 */       paramData.setReloadVibrationParticle(false);
/*     */       
/*     */       return;
/*     */     } 
/* 379 */     Vec3 vec31 = paramData.currentVibration.pos();
/* 380 */     PositionSource positionSource = paramUser.getPositionSource();
/* 381 */     Vec3 vec32 = positionSource.getPosition((Level)paramServerLevel).orElse(vec31);
/* 382 */     int i = paramData.getTravelTimeInTicks();
/*     */     
/* 384 */     int j = paramUser.calculateTravelTimeInTicks(paramData.currentVibration.distance());
/* 385 */     double d1 = 1.0D - i / j;
/*     */     
/* 387 */     double d2 = Mth.lerp(d1, vec31.x, vec32.x);
/* 388 */     double d3 = Mth.lerp(d1, vec31.y, vec32.y);
/* 389 */     double d4 = Mth.lerp(d1, vec31.z, vec32.z);
/*     */     
/* 391 */     boolean bool = (paramServerLevel.sendParticles((ParticleOptions)new VibrationParticleOption(positionSource, i), d2, d3, d4, 1, 0.0D, 0.0D, 0.0D, 0.0D) > 0) ? true : false;
/*     */     
/* 393 */     if (bool) {
/* 394 */       paramData.setReloadVibrationParticle(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean receiveVibration(ServerLevel paramServerLevel, VibrationSystem.Data paramData, VibrationSystem.User paramUser, VibrationInfo paramVibrationInfo) {
/* 399 */     BlockPos blockPos1 = BlockPos.containing((Position)paramVibrationInfo.pos());
/* 400 */     BlockPos blockPos2 = paramUser.getPositionSource().getPosition((Level)paramServerLevel).map(BlockPos::containing).orElse(blockPos1);
/*     */ 
/*     */ 
/*     */     
/* 404 */     if (paramUser.requiresAdjacentChunksToBeTicking() && !areAdjacentChunksTicking((Level)paramServerLevel, blockPos2)) {
/* 405 */       return false;
/*     */     }
/*     */     
/* 408 */     paramUser.onReceiveVibration(paramServerLevel, blockPos1, paramVibrationInfo
/*     */ 
/*     */         
/* 411 */         .gameEvent(), paramVibrationInfo
/* 412 */         .getEntity(paramServerLevel).orElse(null), paramVibrationInfo
/* 413 */         .getProjectileOwner(paramServerLevel).orElse(null), 
/* 414 */         VibrationSystem.Listener.distanceBetweenInBlocks(blockPos1, blockPos2));
/*     */ 
/*     */ 
/*     */     
/* 418 */     paramData.setCurrentVibration(null);
/* 419 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean areAdjacentChunksTicking(Level paramLevel, BlockPos paramBlockPos) {
/* 423 */     ChunkPos chunkPos = new ChunkPos(paramBlockPos);
/*     */     
/* 425 */     for (int i = chunkPos.x - 1; i <= chunkPos.x + 1; i++) {
/* 426 */       for (int j = chunkPos.z - 1; j <= chunkPos.z + 1; j++) {
/* 427 */         if (!paramLevel.shouldTickBlocksAt(ChunkPos.asLong(i, j)) || paramLevel.getChunkSource().getChunkNow(i, j) == null) {
/* 428 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 433 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSystem$Ticker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */