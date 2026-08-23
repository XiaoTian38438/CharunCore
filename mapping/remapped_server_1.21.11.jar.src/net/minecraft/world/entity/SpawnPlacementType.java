/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ 
/*    */ public interface SpawnPlacementType
/*    */ {
/*    */   boolean isSpawnPositionOk(LevelReader paramLevelReader, BlockPos paramBlockPos, EntityType<?> paramEntityType);
/*    */   
/*    */   default BlockPos adjustSpawnPosition(LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 11 */     return paramBlockPos;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\SpawnPlacementType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */