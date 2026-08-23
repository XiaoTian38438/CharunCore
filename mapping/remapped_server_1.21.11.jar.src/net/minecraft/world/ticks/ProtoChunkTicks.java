/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class ProtoChunkTicks<T> implements SerializableTickContainer<T>, TickContainerAccess<T> {
/* 11 */   private final List<SavedTick<T>> ticks = Lists.newArrayList();
/*    */ 
/*    */   
/* 14 */   private final Set<SavedTick<?>> ticksPerPosition = (Set<SavedTick<?>>)new ObjectOpenCustomHashSet(SavedTick.UNIQUE_TICK_HASH);
/*    */ 
/*    */ 
/*    */   
/*    */   public void schedule(ScheduledTick<T> paramScheduledTick) {
/* 19 */     SavedTick<T> savedTick = new SavedTick(paramScheduledTick.type(), paramScheduledTick.pos(), 0, paramScheduledTick.priority());
/* 20 */     schedule(savedTick);
/*    */   }
/*    */   
/*    */   private void schedule(SavedTick<T> paramSavedTick) {
/* 24 */     if (this.ticksPerPosition.add(paramSavedTick)) {
/* 25 */       this.ticks.add(paramSavedTick);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasScheduledTick(BlockPos paramBlockPos, T paramT) {
/* 31 */     return this.ticksPerPosition.contains(SavedTick.probe(paramT, paramBlockPos));
/*    */   }
/*    */ 
/*    */   
/*    */   public int count() {
/* 36 */     return this.ticks.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public List<SavedTick<T>> pack(long paramLong) {
/* 41 */     return this.ticks;
/*    */   }
/*    */   
/*    */   public List<SavedTick<T>> scheduledTicks() {
/* 45 */     return List.copyOf(this.ticks);
/*    */   }
/*    */   
/*    */   public static <T> ProtoChunkTicks<T> load(List<SavedTick<T>> paramList) {
/* 49 */     ProtoChunkTicks<T> protoChunkTicks = new ProtoChunkTicks();
/* 50 */     Objects.requireNonNull(protoChunkTicks); paramList.forEach(protoChunkTicks::schedule);
/* 51 */     return protoChunkTicks;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\ticks\ProtoChunkTicks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */