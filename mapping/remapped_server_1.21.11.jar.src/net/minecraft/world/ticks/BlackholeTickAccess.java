/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BlackholeTickAccess
/*    */ {
/*  9 */   private static final TickContainerAccess<Object> CONTAINER_BLACKHOLE = new TickContainerAccess()
/*    */     {
/*    */       public void schedule(ScheduledTick<Object> param1ScheduledTick) {}
/*    */ 
/*    */ 
/*    */       
/*    */       public boolean hasScheduledTick(BlockPos param1BlockPos, Object param1Object) {
/* 16 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public int count() {
/* 21 */         return 0;
/*    */       }
/*    */     };
/*    */   
/* 25 */   private static final LevelTickAccess<Object> LEVEL_BLACKHOLE = new LevelTickAccess()
/*    */     {
/*    */       public void schedule(ScheduledTick<Object> param1ScheduledTick) {}
/*    */ 
/*    */ 
/*    */       
/*    */       public boolean hasScheduledTick(BlockPos param1BlockPos, Object param1Object) {
/* 32 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean willTickThisTick(BlockPos param1BlockPos, Object param1Object) {
/* 37 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public int count() {
/* 42 */         return 0;
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   public static <T> TickContainerAccess<T> emptyContainer() {
/* 48 */     return (TickContainerAccess)CONTAINER_BLACKHOLE;
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> LevelTickAccess<T> emptyLevelList() {
/* 53 */     return (LevelTickAccess)LEVEL_BLACKHOLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\ticks\BlackholeTickAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */