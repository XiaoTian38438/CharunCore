/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface CommonLevelAccessor
/*    */   extends EntityGetter, LevelReader, LevelSimulatedRW
/*    */ {
/*    */   default <T extends net.minecraft.world.level.block.entity.BlockEntity> Optional<T> getBlockEntity(BlockPos paramBlockPos, BlockEntityType<T> paramBlockEntityType) {
/* 18 */     return super.getBlockEntity(paramBlockPos, paramBlockEntityType);
/*    */   }
/*    */ 
/*    */   
/*    */   default List<VoxelShape> getEntityCollisions(Entity paramEntity, AABB paramAABB) {
/* 23 */     return super.getEntityCollisions(paramEntity, paramAABB);
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean isUnobstructed(Entity paramEntity, VoxelShape paramVoxelShape) {
/* 28 */     return super.isUnobstructed(paramEntity, paramVoxelShape);
/*    */   }
/*    */ 
/*    */   
/*    */   default BlockPos getHeightmapPos(Heightmap.Types paramTypes, BlockPos paramBlockPos) {
/* 33 */     return super.getHeightmapPos(paramTypes, paramBlockPos);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\CommonLevelAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */