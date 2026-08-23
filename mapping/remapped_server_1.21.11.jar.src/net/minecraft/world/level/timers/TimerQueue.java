/*     */ package net.minecraft.world.level.timers;
/*     */ import com.google.common.collect.HashBasedTable;
/*     */ import com.google.common.collect.Table;
/*     */ import com.google.common.primitives.UnsignedLong;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Objects;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TimerQueue<T> {
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final String CALLBACK_DATA_TAG = "Callback";
/*     */   private static final String TIMER_NAME_TAG = "Name";
/*     */   private static final String TIMER_TRIGGER_TIME_TAG = "TriggerTime";
/*     */   private final TimerCallbacks<T> callbacksRegistry;
/*     */   
/*     */   public static class Event<T> { public final long triggerTime;
/*     */     public final UnsignedLong sequentialId;
/*     */     public final String id;
/*     */     public final TimerCallback<T> callback;
/*     */     
/*     */     Event(long param1Long, UnsignedLong param1UnsignedLong, String param1String, TimerCallback<T> param1TimerCallback) {
/*  35 */       this.triggerTime = param1Long;
/*  36 */       this.sequentialId = param1UnsignedLong;
/*  37 */       this.id = param1String;
/*  38 */       this.callback = param1TimerCallback;
/*     */     } }
/*     */ 
/*     */   
/*     */   private static <T> Comparator<Event<T>> createComparator() {
/*  43 */     return Comparator.comparingLong(paramEvent -> paramEvent.triggerTime).thenComparing(paramEvent -> paramEvent.sequentialId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  48 */   private final Queue<Event<T>> queue = new PriorityQueue<>((Comparator)createComparator());
/*     */   
/*  50 */   private UnsignedLong sequentialId = UnsignedLong.ZERO;
/*     */   
/*  52 */   private final Table<String, Long, Event<T>> events = (Table<String, Long, Event<T>>)HashBasedTable.create();
/*     */   
/*     */   public TimerQueue(TimerCallbacks<T> paramTimerCallbacks, Stream<? extends Dynamic<?>> paramStream) {
/*  55 */     this(paramTimerCallbacks);
/*  56 */     this.queue.clear();
/*  57 */     this.events.clear();
/*  58 */     this.sequentialId = UnsignedLong.ZERO;
/*     */     
/*  60 */     paramStream.forEach(paramDynamic -> {
/*     */           Tag tag = (Tag)paramDynamic.convert((DynamicOps)NbtOps.INSTANCE).getValue();
/*     */           if (tag instanceof CompoundTag) {
/*     */             CompoundTag compoundTag = (CompoundTag)tag;
/*     */             loadEvent(compoundTag);
/*     */           } else {
/*     */             LOGGER.warn("Invalid format of events: {}", tag);
/*     */           } 
/*     */         });
/*     */   }
/*     */   public TimerQueue(TimerCallbacks<T> paramTimerCallbacks) {
/*  71 */     this.callbacksRegistry = paramTimerCallbacks;
/*     */   }
/*     */   
/*     */   public void tick(T paramT, long paramLong) {
/*     */     while (true) {
/*  76 */       Event event = this.queue.peek();
/*  77 */       if (event == null || event.triggerTime > paramLong) {
/*     */         break;
/*     */       }
/*     */       
/*  81 */       this.queue.remove();
/*  82 */       this.events.remove(event.id, Long.valueOf(paramLong));
/*     */       
/*  84 */       event.callback.handle(paramT, this, paramLong);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void schedule(String paramString, long paramLong, TimerCallback<T> paramTimerCallback) {
/*  89 */     if (this.events.contains(paramString, Long.valueOf(paramLong))) {
/*     */       return;
/*     */     }
/*  92 */     this.sequentialId = this.sequentialId.plus(UnsignedLong.ONE);
/*  93 */     Event<T> event = new Event<>(paramLong, this.sequentialId, paramString, paramTimerCallback);
/*  94 */     this.events.put(paramString, Long.valueOf(paramLong), event);
/*  95 */     this.queue.add(event);
/*     */   }
/*     */   
/*     */   public int remove(String paramString) {
/*  99 */     Collection collection = this.events.row(paramString).values();
/* 100 */     Objects.requireNonNull(this.queue); collection.forEach(this.queue::remove);
/* 101 */     int i = collection.size();
/* 102 */     collection.clear();
/* 103 */     return i;
/*     */   }
/*     */   
/*     */   public Set<String> getEventsIds() {
/* 107 */     return Collections.unmodifiableSet(this.events.rowKeySet());
/*     */   }
/*     */   
/*     */   private void loadEvent(CompoundTag paramCompoundTag) {
/* 111 */     TimerCallback<T> timerCallback = paramCompoundTag.read("Callback", this.callbacksRegistry.codec()).orElse(null);
/* 112 */     if (timerCallback != null) {
/* 113 */       String str = paramCompoundTag.getStringOr("Name", "");
/* 114 */       long l = paramCompoundTag.getLongOr("TriggerTime", 0L);
/* 115 */       schedule(str, l, timerCallback);
/*     */     } 
/*     */   }
/*     */   
/*     */   private CompoundTag storeEvent(Event<T> paramEvent) {
/* 120 */     CompoundTag compoundTag = new CompoundTag();
/* 121 */     compoundTag.putString("Name", paramEvent.id);
/* 122 */     compoundTag.putLong("TriggerTime", paramEvent.triggerTime);
/* 123 */     compoundTag.store("Callback", this.callbacksRegistry.codec(), paramEvent.callback);
/* 124 */     return compoundTag;
/*     */   }
/*     */   
/*     */   public ListTag store() {
/* 128 */     ListTag listTag = new ListTag();
/* 129 */     Objects.requireNonNull(listTag); this.queue.stream().sorted(createComparator()).map(this::storeEvent).forEach(listTag::add);
/* 130 */     return listTag;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\timers\TimerQueue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */