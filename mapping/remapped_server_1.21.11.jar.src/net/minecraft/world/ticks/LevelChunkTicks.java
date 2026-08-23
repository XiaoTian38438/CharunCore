/*     */ package net.minecraft.world.ticks;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ 
/*     */ public class LevelChunkTicks<T>
/*     */   implements SerializableTickContainer<T>, TickContainerAccess<T>
/*     */ {
/*  18 */   private final Queue<ScheduledTick<T>> tickQueue = (Queue)new PriorityQueue<>(ScheduledTick.DRAIN_ORDER);
/*     */ 
/*     */   
/*     */   private List<SavedTick<T>> pendingTicks;
/*     */ 
/*     */   
/*  24 */   private final Set<ScheduledTick<?>> ticksPerPosition = (Set<ScheduledTick<?>>)new ObjectOpenCustomHashSet(ScheduledTick.UNIQUE_TICK_HASH);
/*     */   
/*     */   private BiConsumer<LevelChunkTicks<T>, ScheduledTick<T>> onTickAdded;
/*     */ 
/*     */   
/*     */   public LevelChunkTicks() {}
/*     */   
/*     */   public LevelChunkTicks(List<SavedTick<T>> paramList) {
/*  32 */     this.pendingTicks = paramList;
/*  33 */     for (SavedTick<T> savedTick : paramList) {
/*  34 */       this.ticksPerPosition.add(ScheduledTick.probe(savedTick.type(), savedTick.pos()));
/*     */     }
/*     */   }
/*     */   
/*     */   public void setOnTickAdded(BiConsumer<LevelChunkTicks<T>, ScheduledTick<T>> paramBiConsumer) {
/*  39 */     this.onTickAdded = paramBiConsumer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledTick<T> peek() {
/*  46 */     return this.tickQueue.peek();
/*     */   }
/*     */   
/*     */   public ScheduledTick<T> poll() {
/*  50 */     ScheduledTick<T> scheduledTick = this.tickQueue.poll();
/*  51 */     if (scheduledTick != null) {
/*  52 */       this.ticksPerPosition.remove(scheduledTick);
/*     */     }
/*  54 */     return scheduledTick;
/*     */   }
/*     */ 
/*     */   
/*     */   public void schedule(ScheduledTick<T> paramScheduledTick) {
/*  59 */     if (this.ticksPerPosition.add(paramScheduledTick)) {
/*  60 */       scheduleUnchecked(paramScheduledTick);
/*     */     }
/*     */   }
/*     */   
/*     */   private void scheduleUnchecked(ScheduledTick<T> paramScheduledTick) {
/*  65 */     this.tickQueue.add(paramScheduledTick);
/*     */     
/*  67 */     if (this.onTickAdded != null) {
/*  68 */       this.onTickAdded.accept(this, paramScheduledTick);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasScheduledTick(BlockPos paramBlockPos, T paramT) {
/*  75 */     return this.ticksPerPosition.contains(ScheduledTick.probe(paramT, paramBlockPos));
/*     */   }
/*     */   
/*     */   public void removeIf(Predicate<ScheduledTick<T>> paramPredicate) {
/*  79 */     for (Iterator<ScheduledTick<T>> iterator = this.tickQueue.iterator(); iterator.hasNext(); ) {
/*  80 */       ScheduledTick<T> scheduledTick = iterator.next();
/*  81 */       if (paramPredicate.test(scheduledTick)) {
/*  82 */         iterator.remove();
/*  83 */         this.ticksPerPosition.remove(scheduledTick);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Stream<ScheduledTick<T>> getAll() {
/*  89 */     return this.tickQueue.stream();
/*     */   }
/*     */ 
/*     */   
/*     */   public int count() {
/*  94 */     return this.tickQueue.size() + ((this.pendingTicks != null) ? this.pendingTicks.size() : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<SavedTick<T>> pack(long paramLong) {
/*  99 */     ArrayList<SavedTick<T>> arrayList = new ArrayList(this.tickQueue.size());
/* 100 */     if (this.pendingTicks != null) {
/* 101 */       arrayList.addAll(this.pendingTicks);
/*     */     }
/* 103 */     for (ScheduledTick<T> scheduledTick : this.tickQueue) {
/* 104 */       arrayList.add(scheduledTick.toSavedTick(paramLong));
/*     */     }
/* 106 */     return arrayList;
/*     */   }
/*     */   
/*     */   public void unpack(long paramLong) {
/* 110 */     if (this.pendingTicks != null) {
/* 111 */       int i = -this.pendingTicks.size();
/* 112 */       for (SavedTick<T> savedTick : this.pendingTicks)
/*     */       {
/* 114 */         scheduleUnchecked(savedTick.unpack(paramLong, i++));
/*     */       }
/*     */     } 
/*     */     
/* 118 */     this.pendingTicks = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\ticks\LevelChunkTicks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */