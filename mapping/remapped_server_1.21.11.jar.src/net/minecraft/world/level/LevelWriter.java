/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ public interface LevelWriter
/*    */ {
/*    */   boolean setBlock(BlockPos paramBlockPos, BlockState paramBlockState, @UpdateFlags int paramInt1, int paramInt2);
/*    */   
/*    */   default boolean setBlock(BlockPos paramBlockPos, BlockState paramBlockState, @UpdateFlags int paramInt) {
/* 14 */     return setBlock(paramBlockPos, paramBlockState, paramInt, 512);
/*    */   }
/*    */ 
/*    */   
/*    */   boolean removeBlock(BlockPos paramBlockPos, boolean paramBoolean);
/*    */   
/*    */   default boolean destroyBlock(BlockPos paramBlockPos, boolean paramBoolean) {
/* 21 */     return destroyBlock(paramBlockPos, paramBoolean, null);
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean destroyBlock(BlockPos paramBlockPos, boolean paramBoolean, Entity paramEntity) {
/* 26 */     return destroyBlock(paramBlockPos, paramBoolean, paramEntity, 512);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean destroyBlock(BlockPos paramBlockPos, boolean paramBoolean, Entity paramEntity, int paramInt);
/*    */ 
/*    */   
/*    */   default boolean addFreshEntity(Entity paramEntity) {
/* 35 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\LevelWriter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */