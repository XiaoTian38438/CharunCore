/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class WorldGenTickAccess<T>
/*    */   implements LevelTickAccess<T> {
/*    */   private final Function<BlockPos, TickContainerAccess<T>> containerGetter;
/*    */   
/*    */   public WorldGenTickAccess(Function<BlockPos, TickContainerAccess<T>> paramFunction) {
/* 11 */     this.containerGetter = paramFunction;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasScheduledTick(BlockPos paramBlockPos, T paramT) {
/* 16 */     return ((TickContainerAccess<T>)this.containerGetter.apply(paramBlockPos)).hasScheduledTick(paramBlockPos, paramT);
/*    */   }
/*    */ 
/*    */   
/*    */   public void schedule(ScheduledTick<T> paramScheduledTick) {
/* 21 */     ((TickContainerAccess<T>)this.containerGetter.apply(paramScheduledTick.pos())).schedule(paramScheduledTick);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean willTickThisTick(BlockPos paramBlockPos, T paramT) {
/* 26 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int count() {
/* 32 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\ticks\WorldGenTickAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */