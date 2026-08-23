/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.ticks.LevelTickAccess;
/*    */ import net.minecraft.world.ticks.ScheduledTick;
/*    */ import net.minecraft.world.ticks.TickPriority;
/*    */ 
/*    */ public interface ScheduledTickAccess {
/*    */   <T> ScheduledTick<T> createTick(BlockPos paramBlockPos, T paramT, int paramInt, TickPriority paramTickPriority);
/*    */   
/*    */   <T> ScheduledTick<T> createTick(BlockPos paramBlockPos, T paramT, int paramInt);
/*    */   
/*    */   LevelTickAccess<Block> getBlockTicks();
/*    */   
/*    */   default void scheduleTick(BlockPos paramBlockPos, Block paramBlock, int paramInt, TickPriority paramTickPriority) {
/* 18 */     getBlockTicks().schedule(createTick(paramBlockPos, paramBlock, paramInt, paramTickPriority));
/*    */   }
/*    */   
/*    */   default void scheduleTick(BlockPos paramBlockPos, Block paramBlock, int paramInt) {
/* 22 */     getBlockTicks().schedule(createTick(paramBlockPos, paramBlock, paramInt));
/*    */   }
/*    */   
/*    */   LevelTickAccess<Fluid> getFluidTicks();
/*    */   
/*    */   default void scheduleTick(BlockPos paramBlockPos, Fluid paramFluid, int paramInt, TickPriority paramTickPriority) {
/* 28 */     getFluidTicks().schedule(createTick(paramBlockPos, paramFluid, paramInt, paramTickPriority));
/*    */   }
/*    */   
/*    */   default void scheduleTick(BlockPos paramBlockPos, Fluid paramFluid, int paramInt) {
/* 32 */     getFluidTicks().schedule(createTick(paramBlockPos, paramFluid, paramInt));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\ScheduledTickAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */