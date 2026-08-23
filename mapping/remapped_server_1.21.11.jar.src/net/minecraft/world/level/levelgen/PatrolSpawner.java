/*     */ package net.minecraft.world.level.levelgen;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.monster.PatrollingMonster;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CustomSpawner;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public class PatrolSpawner implements CustomSpawner {
/*     */   public void tick(ServerLevel paramServerLevel, boolean paramBoolean) {
/*  22 */     if (!paramBoolean) {
/*     */       return;
/*     */     }
/*     */     
/*  26 */     if (!((Boolean)paramServerLevel.getGameRules().get(GameRules.SPAWN_PATROLS)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  30 */     RandomSource randomSource = paramServerLevel.random;
/*     */     
/*  32 */     this.nextTick--;
/*  33 */     if (this.nextTick > 0) {
/*     */       return;
/*     */     }
/*     */     
/*  37 */     this.nextTick += 12000 + randomSource.nextInt(1200);
/*     */     
/*  39 */     if (!paramServerLevel.isBrightOutside()) {
/*     */       return;
/*     */     }
/*     */     
/*  43 */     if (randomSource.nextInt(5) != 0) {
/*     */       return;
/*     */     }
/*     */     
/*  47 */     int i = paramServerLevel.players().size();
/*  48 */     if (i < 1) {
/*     */       return;
/*     */     }
/*     */     
/*  52 */     Player player = paramServerLevel.players().get(randomSource.nextInt(i));
/*  53 */     if (player.isSpectator()) {
/*     */       return;
/*     */     }
/*     */     
/*  57 */     if (paramServerLevel.isCloseToVillage(player.blockPosition(), 2)) {
/*     */       return;
/*     */     }
/*     */     
/*  61 */     int j = (24 + randomSource.nextInt(24)) * (randomSource.nextBoolean() ? -1 : 1);
/*  62 */     int k = (24 + randomSource.nextInt(24)) * (randomSource.nextBoolean() ? -1 : 1);
/*  63 */     BlockPos.MutableBlockPos mutableBlockPos = player.blockPosition().mutable().move(j, 0, k);
/*     */ 
/*     */     
/*  66 */     byte b1 = 10;
/*  67 */     if (!paramServerLevel.hasChunksAt(mutableBlockPos.getX() - 10, mutableBlockPos.getZ() - 10, mutableBlockPos.getX() + 10, mutableBlockPos.getZ() + 10)) {
/*     */       return;
/*     */     }
/*     */     
/*  71 */     if (!((Boolean)paramServerLevel.environmentAttributes().getValue(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, (BlockPos)mutableBlockPos)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  75 */     int m = (int)Math.ceil(paramServerLevel.getCurrentDifficultyAt((BlockPos)mutableBlockPos).getEffectiveDifficulty()) + 1;
/*  76 */     for (byte b2 = 0; b2 < m; b2++) {
/*  77 */       mutableBlockPos.setY(paramServerLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (BlockPos)mutableBlockPos).getY());
/*     */       
/*  79 */       if (b2 == 0) {
/*  80 */         if (!spawnPatrolMember(paramServerLevel, (BlockPos)mutableBlockPos, randomSource, true)) {
/*     */           break;
/*     */         }
/*     */       } else {
/*  84 */         spawnPatrolMember(paramServerLevel, (BlockPos)mutableBlockPos, randomSource, false);
/*     */       } 
/*     */       
/*  87 */       mutableBlockPos.setX(mutableBlockPos.getX() + randomSource.nextInt(5) - randomSource.nextInt(5));
/*  88 */       mutableBlockPos.setZ(mutableBlockPos.getZ() + randomSource.nextInt(5) - randomSource.nextInt(5));
/*     */     } 
/*     */   }
/*     */   private int nextTick;
/*     */   private boolean spawnPatrolMember(ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource, boolean paramBoolean) {
/*  93 */     BlockState blockState = paramServerLevel.getBlockState(paramBlockPos);
/*  94 */     if (!NaturalSpawner.isValidEmptySpawnBlock((BlockGetter)paramServerLevel, paramBlockPos, blockState, blockState.getFluidState(), EntityType.PILLAGER)) {
/*  95 */       return false;
/*     */     }
/*     */     
/*  98 */     if (!PatrollingMonster.checkPatrollingMonsterSpawnRules(EntityType.PILLAGER, (LevelAccessor)paramServerLevel, EntitySpawnReason.PATROL, paramBlockPos, paramRandomSource)) {
/*  99 */       return false;
/*     */     }
/*     */     
/* 102 */     PatrollingMonster patrollingMonster = (PatrollingMonster)EntityType.PILLAGER.create((Level)paramServerLevel, EntitySpawnReason.PATROL);
/* 103 */     if (patrollingMonster != null) {
/* 104 */       if (paramBoolean) {
/* 105 */         patrollingMonster.setPatrolLeader(true);
/* 106 */         patrollingMonster.findPatrolTarget();
/*     */       } 
/*     */       
/* 109 */       patrollingMonster.setPos(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ());
/* 110 */       patrollingMonster.finalizeSpawn((ServerLevelAccessor)paramServerLevel, paramServerLevel.getCurrentDifficultyAt(paramBlockPos), EntitySpawnReason.PATROL, null);
/*     */       
/* 112 */       paramServerLevel.addFreshEntityWithPassengers((Entity)patrollingMonster);
/* 113 */       return true;
/*     */     } 
/*     */     
/* 116 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\PatrolSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */