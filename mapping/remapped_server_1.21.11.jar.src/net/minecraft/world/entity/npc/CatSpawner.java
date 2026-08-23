/*    */ package net.minecraft.world.entity.npc;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.tags.StructureTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.SpawnPlacements;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*    */ import net.minecraft.world.entity.animal.feline.Cat;
/*    */ import net.minecraft.world.level.CustomSpawner;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class CatSpawner implements CustomSpawner {
/*    */   private static final int TICK_DELAY = 1200;
/*    */   private int nextTick;
/*    */   
/*    */   public void tick(ServerLevel paramServerLevel, boolean paramBoolean) {
/* 28 */     this.nextTick--;
/* 29 */     if (this.nextTick > 0) {
/*    */       return;
/*    */     }
/*    */     
/* 33 */     this.nextTick = 1200;
/*    */     
/* 35 */     ServerPlayer serverPlayer = paramServerLevel.getRandomPlayer();
/* 36 */     if (serverPlayer == null) {
/*    */       return;
/*    */     }
/*    */     
/* 40 */     RandomSource randomSource = paramServerLevel.random;
/* 41 */     int i = (8 + randomSource.nextInt(24)) * (randomSource.nextBoolean() ? -1 : 1);
/* 42 */     int j = (8 + randomSource.nextInt(24)) * (randomSource.nextBoolean() ? -1 : 1);
/* 43 */     BlockPos blockPos = serverPlayer.blockPosition().offset(i, 0, j);
/*    */ 
/*    */     
/* 46 */     byte b = 10;
/* 47 */     if (!paramServerLevel.hasChunksAt(blockPos.getX() - 10, blockPos.getZ() - 10, blockPos.getX() + 10, blockPos.getZ() + 10)) {
/*    */       return;
/*    */     }
/*    */     
/* 51 */     if (SpawnPlacements.isSpawnPositionOk(EntityType.CAT, (LevelReader)paramServerLevel, blockPos)) {
/* 52 */       if (paramServerLevel.isCloseToVillage(blockPos, 2)) {
/* 53 */         spawnInVillage(paramServerLevel, blockPos);
/* 54 */       } else if (paramServerLevel.structureManager().getStructureWithPieceAt(blockPos, StructureTags.CATS_SPAWN_IN).isValid()) {
/* 55 */         spawnInHut(paramServerLevel, blockPos);
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   private void spawnInVillage(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 61 */     byte b = 48;
/* 62 */     if (paramServerLevel.getPoiManager().getCountInRange(paramHolder -> paramHolder.is(PoiTypes.HOME), paramBlockPos, 48, PoiManager.Occupancy.IS_OCCUPIED) > 4L) {
/* 63 */       List list = paramServerLevel.getEntitiesOfClass(Cat.class, (new AABB(paramBlockPos)).inflate(48.0D, 8.0D, 48.0D));
/* 64 */       if (list.size() < 5) {
/* 65 */         spawnCat(paramBlockPos, paramServerLevel, false);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private void spawnInHut(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 71 */     byte b = 16;
/* 72 */     List list = paramServerLevel.getEntitiesOfClass(Cat.class, (new AABB(paramBlockPos)).inflate(16.0D, 8.0D, 16.0D));
/* 73 */     if (list.isEmpty()) {
/* 74 */       spawnCat(paramBlockPos, paramServerLevel, true);
/*    */     }
/*    */   }
/*    */   
/*    */   private void spawnCat(BlockPos paramBlockPos, ServerLevel paramServerLevel, boolean paramBoolean) {
/* 79 */     Cat cat = (Cat)EntityType.CAT.create((Level)paramServerLevel, EntitySpawnReason.NATURAL);
/* 80 */     if (cat == null) {
/*    */       return;
/*    */     }
/*    */     
/* 84 */     cat.finalizeSpawn((ServerLevelAccessor)paramServerLevel, paramServerLevel.getCurrentDifficultyAt(paramBlockPos), EntitySpawnReason.NATURAL, null);
/* 85 */     if (paramBoolean) {
/* 86 */       cat.setPersistenceRequired();
/*    */     }
/* 88 */     cat.snapTo(paramBlockPos, 0.0F, 0.0F);
/* 89 */     paramServerLevel.addFreshEntityWithPassengers((Entity)cat);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\npc\CatSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */