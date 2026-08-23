/*     */ package net.minecraft.world.ticks;
/*     */ import it.unimi.dsi.fastutil.longs.Long2LongMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2LongMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.LongSummaryStatistics;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.LongPredicate;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ 
/*     */ public class LevelTicks<T> implements LevelTickAccess<T> {
/*     */   private static final Comparator<LevelChunkTicks<?>> CONTAINER_DRAIN_ORDER;
/*     */   private final LongPredicate tickCheck;
/*     */   
/*     */   static {
/*  32 */     CONTAINER_DRAIN_ORDER = ((paramLevelChunkTicks1, paramLevelChunkTicks2) -> ScheduledTick.INTRA_TICK_DRAIN_ORDER.compare(paramLevelChunkTicks1.peek(), paramLevelChunkTicks2.peek()));
/*     */   }
/*     */ 
/*     */   
/*  36 */   private final Long2ObjectMap<LevelChunkTicks<T>> allContainers = (Long2ObjectMap<LevelChunkTicks<T>>)new Long2ObjectOpenHashMap(); private final Long2LongMap nextTickForContainer; private final Queue<LevelChunkTicks<T>> containersToTick; private final Queue<ScheduledTick<T>> toRunThisTick; public LevelTicks(LongPredicate paramLongPredicate) {
/*  37 */     this.nextTickForContainer = (Long2LongMap)Util.make(new Long2LongOpenHashMap(), paramLong2LongOpenHashMap -> paramLong2LongOpenHashMap.defaultReturnValue(Long.MAX_VALUE));
/*     */     
/*  39 */     this.containersToTick = (Queue)new PriorityQueue<>(CONTAINER_DRAIN_ORDER);
/*  40 */     this.toRunThisTick = new ArrayDeque<>();
/*  41 */     this.alreadyRunThisTick = new ArrayList<>();
/*     */ 
/*     */     
/*  44 */     this.toRunThisTickSet = (Set<ScheduledTick<?>>)new ObjectOpenCustomHashSet(ScheduledTick.UNIQUE_TICK_HASH);
/*     */     
/*  46 */     this.chunkScheduleUpdater = ((paramLevelChunkTicks, paramScheduledTick) -> {
/*     */         if (paramScheduledTick.equals(paramLevelChunkTicks.peek())) {
/*     */           updateContainerScheduling(paramScheduledTick);
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */     
/*  54 */     this.tickCheck = paramLongPredicate;
/*     */   }
/*     */   private final List<ScheduledTick<T>> alreadyRunThisTick; private final Set<ScheduledTick<?>> toRunThisTickSet; private final BiConsumer<LevelChunkTicks<T>, ScheduledTick<T>> chunkScheduleUpdater;
/*     */   
/*     */   public void addContainer(ChunkPos paramChunkPos, LevelChunkTicks<T> paramLevelChunkTicks) {
/*  59 */     long l = paramChunkPos.toLong();
/*  60 */     this.allContainers.put(l, paramLevelChunkTicks);
/*  61 */     ScheduledTick<T> scheduledTick = paramLevelChunkTicks.peek();
/*  62 */     if (scheduledTick != null) {
/*  63 */       this.nextTickForContainer.put(l, scheduledTick.triggerTick());
/*     */     }
/*     */     
/*  66 */     paramLevelChunkTicks.setOnTickAdded(this.chunkScheduleUpdater);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeContainer(ChunkPos paramChunkPos) {
/*  71 */     long l = paramChunkPos.toLong();
/*  72 */     LevelChunkTicks levelChunkTicks = (LevelChunkTicks)this.allContainers.remove(l);
/*  73 */     this.nextTickForContainer.remove(l);
/*  74 */     if (levelChunkTicks != null) {
/*  75 */       levelChunkTicks.setOnTickAdded(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void schedule(ScheduledTick<T> paramScheduledTick) {
/*  81 */     long l = ChunkPos.asLong(paramScheduledTick.pos());
/*  82 */     LevelChunkTicks<T> levelChunkTicks = (LevelChunkTicks)this.allContainers.get(l);
/*  83 */     if (levelChunkTicks == null) {
/*  84 */       Util.logAndPauseIfInIde("Trying to schedule tick in not loaded position " + String.valueOf(paramScheduledTick.pos()));
/*     */       return;
/*     */     } 
/*  87 */     levelChunkTicks.schedule(paramScheduledTick);
/*     */   }
/*     */   
/*     */   public void tick(long paramLong, int paramInt, BiConsumer<BlockPos, T> paramBiConsumer) {
/*  91 */     ProfilerFiller profilerFiller = Profiler.get();
/*  92 */     profilerFiller.push("collect");
/*  93 */     collectTicks(paramLong, paramInt, profilerFiller);
/*  94 */     profilerFiller.popPush("run");
/*  95 */     profilerFiller.incrementCounter("ticksToRun", this.toRunThisTick.size());
/*  96 */     runCollectedTicks(paramBiConsumer);
/*  97 */     profilerFiller.popPush("cleanup");
/*  98 */     cleanupAfterTick();
/*  99 */     profilerFiller.pop();
/*     */   }
/*     */   
/*     */   private void collectTicks(long paramLong, int paramInt, ProfilerFiller paramProfilerFiller) {
/* 103 */     sortContainersToTick(paramLong);
/* 104 */     paramProfilerFiller.incrementCounter("containersToTick", this.containersToTick.size());
/* 105 */     drainContainers(paramLong, paramInt);
/* 106 */     rescheduleLeftoverContainers();
/*     */   }
/*     */   
/*     */   private void sortContainersToTick(long paramLong) {
/* 110 */     ObjectIterator objectIterator = Long2LongMaps.fastIterator(this.nextTickForContainer);
/* 111 */     while (objectIterator.hasNext()) {
/* 112 */       Long2LongMap.Entry entry = (Long2LongMap.Entry)objectIterator.next();
/* 113 */       long l1 = entry.getLongKey();
/* 114 */       long l2 = entry.getLongValue();
/* 115 */       if (l2 <= paramLong) {
/* 116 */         LevelChunkTicks<T> levelChunkTicks = (LevelChunkTicks)this.allContainers.get(l1);
/* 117 */         if (levelChunkTicks == null) {
/*     */           
/* 119 */           objectIterator.remove(); continue;
/*     */         } 
/* 121 */         ScheduledTick scheduledTick = levelChunkTicks.peek();
/* 122 */         if (scheduledTick == null) {
/*     */           
/* 124 */           objectIterator.remove(); continue;
/* 125 */         }  if (scheduledTick.triggerTick() > paramLong) {
/*     */           
/* 127 */           entry.setValue(scheduledTick.triggerTick()); continue;
/* 128 */         }  if (this.tickCheck.test(l1)) {
/*     */           
/* 130 */           objectIterator.remove();
/* 131 */           this.containersToTick.add(levelChunkTicks);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void drainContainers(long paramLong, int paramInt) {
/*     */     LevelChunkTicks<T> levelChunkTicks;
/* 141 */     while (canScheduleMoreTicks(paramInt) && (levelChunkTicks = this.containersToTick.poll()) != null) {
/* 142 */       ScheduledTick<T> scheduledTick1 = levelChunkTicks.poll();
/*     */       
/* 144 */       scheduleForThisTick(scheduledTick1);
/*     */ 
/*     */       
/* 147 */       drainFromCurrentContainer(this.containersToTick, levelChunkTicks, paramLong, paramInt);
/* 148 */       ScheduledTick<T> scheduledTick2 = levelChunkTicks.peek();
/* 149 */       if (scheduledTick2 != null) {
/* 150 */         if (scheduledTick2.triggerTick() <= paramLong && canScheduleMoreTicks(paramInt)) {
/*     */           
/* 152 */           this.containersToTick.add(levelChunkTicks);
/*     */           continue;
/*     */         } 
/* 155 */         updateContainerScheduling(scheduledTick2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void rescheduleLeftoverContainers() {
/* 163 */     for (LevelChunkTicks<T> levelChunkTicks : this.containersToTick)
/*     */     {
/* 165 */       updateContainerScheduling(levelChunkTicks.peek());
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateContainerScheduling(ScheduledTick<T> paramScheduledTick) {
/* 170 */     this.nextTickForContainer.put(ChunkPos.asLong(paramScheduledTick.pos()), paramScheduledTick.triggerTick());
/*     */   }
/*     */   
/*     */   private void drainFromCurrentContainer(Queue<LevelChunkTicks<T>> paramQueue, LevelChunkTicks<T> paramLevelChunkTicks, long paramLong, int paramInt) {
/* 174 */     if (!canScheduleMoreTicks(paramInt)) {
/*     */       return;
/*     */     }
/*     */     
/* 178 */     LevelChunkTicks<T> levelChunkTicks = paramQueue.peek();
/* 179 */     ScheduledTick<T> scheduledTick = (levelChunkTicks != null) ? levelChunkTicks.peek() : null;
/*     */     
/* 181 */     while (canScheduleMoreTicks(paramInt)) {
/* 182 */       ScheduledTick<T> scheduledTick1 = paramLevelChunkTicks.peek();
/* 183 */       if (scheduledTick1 == null || scheduledTick1.triggerTick() > paramLong) {
/*     */         break;
/*     */       }
/*     */       
/* 187 */       if (scheduledTick != null && ScheduledTick.INTRA_TICK_DRAIN_ORDER.compare(scheduledTick1, scheduledTick) > 0) {
/*     */         break;
/*     */       }
/*     */       
/* 191 */       paramLevelChunkTicks.poll();
/* 192 */       scheduleForThisTick(scheduledTick1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void scheduleForThisTick(ScheduledTick<T> paramScheduledTick) {
/* 197 */     this.toRunThisTick.add(paramScheduledTick);
/*     */   }
/*     */   
/*     */   private boolean canScheduleMoreTicks(int paramInt) {
/* 201 */     return (this.toRunThisTick.size() < paramInt);
/*     */   }
/*     */   
/*     */   private void runCollectedTicks(BiConsumer<BlockPos, T> paramBiConsumer) {
/* 205 */     while (!this.toRunThisTick.isEmpty()) {
/*     */ 
/*     */       
/* 208 */       ScheduledTick<T> scheduledTick = this.toRunThisTick.poll();
/* 209 */       if (!this.toRunThisTickSet.isEmpty()) {
/* 210 */         this.toRunThisTickSet.remove(scheduledTick);
/*     */       }
/* 212 */       this.alreadyRunThisTick.add(scheduledTick);
/* 213 */       paramBiConsumer.accept(scheduledTick.pos(), scheduledTick.type());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cleanupAfterTick() {
/* 218 */     this.toRunThisTick.clear();
/* 219 */     this.containersToTick.clear();
/* 220 */     this.alreadyRunThisTick.clear();
/* 221 */     this.toRunThisTickSet.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasScheduledTick(BlockPos paramBlockPos, T paramT) {
/* 228 */     LevelChunkTicks<T> levelChunkTicks = (LevelChunkTicks)this.allContainers.get(ChunkPos.asLong(paramBlockPos));
/* 229 */     return (levelChunkTicks != null && levelChunkTicks.hasScheduledTick(paramBlockPos, paramT));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean willTickThisTick(BlockPos paramBlockPos, T paramT) {
/* 235 */     calculateTickSetIfNeeded();
/* 236 */     return this.toRunThisTickSet.contains(ScheduledTick.probe(paramT, paramBlockPos));
/*     */   }
/*     */   
/*     */   private void calculateTickSetIfNeeded() {
/* 240 */     if (this.toRunThisTickSet.isEmpty() && !this.toRunThisTick.isEmpty()) {
/* 241 */       this.toRunThisTickSet.addAll(this.toRunThisTick);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void forContainersInArea(BoundingBox paramBoundingBox, PosAndContainerConsumer<T> paramPosAndContainerConsumer) {
/* 251 */     int i = SectionPos.posToSectionCoord(paramBoundingBox.minX());
/* 252 */     int j = SectionPos.posToSectionCoord(paramBoundingBox.minZ());
/*     */     
/* 254 */     int k = SectionPos.posToSectionCoord(paramBoundingBox.maxX());
/* 255 */     int m = SectionPos.posToSectionCoord(paramBoundingBox.maxZ());
/*     */     
/* 257 */     for (int n = i; n <= k; n++) {
/* 258 */       for (int i1 = j; i1 <= m; i1++) {
/* 259 */         long l = ChunkPos.asLong(n, i1);
/* 260 */         LevelChunkTicks<T> levelChunkTicks = (LevelChunkTicks)this.allContainers.get(l);
/* 261 */         if (levelChunkTicks != null) {
/* 262 */           paramPosAndContainerConsumer.accept(l, levelChunkTicks);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clearArea(BoundingBox paramBoundingBox) {
/* 269 */     Predicate<? super ScheduledTick<T>> predicate = paramScheduledTick -> paramBoundingBox.isInside((Vec3i)paramScheduledTick.pos());
/* 270 */     forContainersInArea(paramBoundingBox, (paramLong, paramLevelChunkTicks) -> {
/*     */           ScheduledTick scheduledTick = paramLevelChunkTicks.peek();
/*     */           
/*     */           paramLevelChunkTicks.removeIf(paramPredicate);
/*     */           ScheduledTick<T> scheduledTick1 = paramLevelChunkTicks.peek();
/*     */           if (scheduledTick1 != scheduledTick) {
/*     */             if (scheduledTick1 != null) {
/*     */               updateContainerScheduling(scheduledTick1);
/*     */             } else {
/*     */               this.nextTickForContainer.remove(paramLong);
/*     */             } 
/*     */           }
/*     */         });
/* 283 */     this.alreadyRunThisTick.removeIf(predicate);
/* 284 */     this.toRunThisTick.removeIf(predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyArea(BoundingBox paramBoundingBox, Vec3i paramVec3i) {
/* 295 */     copyAreaFrom(this, paramBoundingBox, paramVec3i);
/*     */   }
/*     */   
/*     */   public void copyAreaFrom(LevelTicks<T> paramLevelTicks, BoundingBox paramBoundingBox, Vec3i paramVec3i) {
/* 299 */     ArrayList arrayList = new ArrayList();
/*     */     
/* 301 */     Predicate predicate = paramScheduledTick -> paramBoundingBox.isInside((Vec3i)paramScheduledTick.pos());
/*     */     
/* 303 */     Objects.requireNonNull(arrayList); paramLevelTicks.alreadyRunThisTick.stream().filter(predicate).forEach(arrayList::add);
/* 304 */     Objects.requireNonNull(arrayList); paramLevelTicks.toRunThisTick.stream().filter(predicate).forEach(arrayList::add);
/*     */     
/* 306 */     paramLevelTicks.forContainersInArea(paramBoundingBox, (paramLong, paramLevelChunkTicks) -> {
/*     */           Objects.requireNonNull(paramList); paramLevelChunkTicks.getAll().filter(paramPredicate).forEach(paramList::add);
/* 308 */         }); LongSummaryStatistics longSummaryStatistics = arrayList.stream().mapToLong(ScheduledTick::subTickOrder).summaryStatistics();
/* 309 */     long l1 = longSummaryStatistics.getMin();
/* 310 */     long l2 = longSummaryStatistics.getMax();
/*     */     
/* 312 */     arrayList.forEach(paramScheduledTick -> schedule(new ScheduledTick<>(paramScheduledTick.type(), paramScheduledTick.pos().offset(paramVec3i), paramScheduledTick.triggerTick(), paramScheduledTick.priority(), paramScheduledTick.subTickOrder() - paramLong1 + paramLong2 + 1L)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int count() {
/* 317 */     return this.allContainers.values().stream().mapToInt(TickAccess::count).sum();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface PosAndContainerConsumer<T> {
/*     */     void accept(long param1Long, LevelChunkTicks<T> param1LevelChunkTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\ticks\LevelTicks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */