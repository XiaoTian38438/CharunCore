/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public interface ServerLevelAccessor
/*    */   extends LevelAccessor {
/*    */   ServerLevel getLevel();
/*    */   
/*    */   DifficultyInstance getCurrentDifficultyAt(BlockPos paramBlockPos);
/*    */   
/*    */   default void addFreshEntityWithPassengers(Entity paramEntity) {
/* 15 */     paramEntity.getSelfAndPassengers().forEach(this::addFreshEntity);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\ServerLevelAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */