/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.server.level.ChunkHolder;
/*     */ import net.minecraft.server.level.ChunkLevel;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.server.level.FullChunkStatus;
/*     */ import net.minecraft.server.level.Ticket;
/*     */ import net.minecraft.server.level.TicketType;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TicketStorage
/*     */   extends SavedData
/*     */ {
/*     */   private static final int INITIAL_TICKET_LIST_CAPACITY = 4;
/*  41 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  43 */   private static final Codec<Pair<ChunkPos, Ticket>> TICKET_ENTRY = Codec.mapPair(ChunkPos.CODEC
/*  44 */       .fieldOf("chunk_pos"), Ticket.CODEC)
/*     */     
/*  46 */     .codec(); public static final Codec<TicketStorage> CODEC;
/*     */   static {
/*  48 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)TICKET_ENTRY.listOf().optionalFieldOf("tickets", List.of()).forGetter(TicketStorage::packTickets)).apply((Applicative)paramInstance, TicketStorage::fromPacked));
/*     */   }
/*     */ 
/*     */   
/*  52 */   public static final SavedDataType<TicketStorage> TYPE = new SavedDataType("chunks", TicketStorage::new, CODEC, DataFixTypes.SAVED_DATA_FORCED_CHUNKS);
/*     */   
/*     */   private final Long2ObjectOpenHashMap<List<Ticket>> tickets;
/*     */   private final Long2ObjectOpenHashMap<List<Ticket>> deactivatedTickets;
/*  56 */   private LongSet chunksWithForcedTickets = (LongSet)new LongOpenHashSet();
/*     */   private ChunkUpdated loadingChunkUpdatedListener;
/*     */   private ChunkUpdated simulationChunkUpdatedListener;
/*     */   
/*     */   private TicketStorage(Long2ObjectOpenHashMap<List<Ticket>> paramLong2ObjectOpenHashMap1, Long2ObjectOpenHashMap<List<Ticket>> paramLong2ObjectOpenHashMap2) {
/*  61 */     this.tickets = paramLong2ObjectOpenHashMap1;
/*  62 */     this.deactivatedTickets = paramLong2ObjectOpenHashMap2;
/*  63 */     updateForcedChunks();
/*     */   }
/*     */   
/*     */   public TicketStorage() {
/*  67 */     this(new Long2ObjectOpenHashMap(4), new Long2ObjectOpenHashMap());
/*     */   }
/*     */   
/*     */   private static TicketStorage fromPacked(List<Pair<ChunkPos, Ticket>> paramList) {
/*  71 */     Long2ObjectOpenHashMap<List<Ticket>> long2ObjectOpenHashMap = new Long2ObjectOpenHashMap();
/*  72 */     for (Pair<ChunkPos, Ticket> pair : paramList) {
/*  73 */       ChunkPos chunkPos = (ChunkPos)pair.getFirst();
/*  74 */       List<Ticket> list = (List)long2ObjectOpenHashMap.computeIfAbsent(chunkPos.toLong(), paramLong -> new ObjectArrayList(4));
/*  75 */       list.add((Ticket)pair.getSecond());
/*     */     } 
/*     */     
/*  78 */     return new TicketStorage(new Long2ObjectOpenHashMap(4), long2ObjectOpenHashMap);
/*     */   }
/*     */   
/*     */   private List<Pair<ChunkPos, Ticket>> packTickets() {
/*  82 */     ArrayList<Pair<ChunkPos, Ticket>> arrayList = new ArrayList();
/*  83 */     forEachTicket((paramChunkPos, paramTicket) -> {
/*     */           if (paramTicket.getType().persist()) {
/*     */             paramList.add(new Pair(paramChunkPos, paramTicket));
/*     */           }
/*     */         });
/*  88 */     return arrayList;
/*     */   }
/*     */   
/*     */   private void forEachTicket(BiConsumer<ChunkPos, Ticket> paramBiConsumer) {
/*  92 */     forEachTicket(paramBiConsumer, this.tickets);
/*  93 */     forEachTicket(paramBiConsumer, this.deactivatedTickets);
/*     */   }
/*     */   
/*     */   private static void forEachTicket(BiConsumer<ChunkPos, Ticket> paramBiConsumer, Long2ObjectOpenHashMap<List<Ticket>> paramLong2ObjectOpenHashMap) {
/*  97 */     for (ObjectIterator<Long2ObjectMap.Entry> objectIterator = Long2ObjectMaps.fastIterable((Long2ObjectMap)paramLong2ObjectOpenHashMap).iterator(); objectIterator.hasNext(); ) { Long2ObjectMap.Entry entry = objectIterator.next();
/*  98 */       ChunkPos chunkPos = new ChunkPos(entry.getLongKey());
/*  99 */       for (Ticket ticket : entry.getValue()) {
/* 100 */         paramBiConsumer.accept(chunkPos, ticket);
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   public void activateAllDeactivatedTickets() {
/* 106 */     for (ObjectIterator<Long2ObjectMap.Entry> objectIterator = Long2ObjectMaps.fastIterable((Long2ObjectMap)this.deactivatedTickets).iterator(); objectIterator.hasNext(); ) { Long2ObjectMap.Entry entry = objectIterator.next();
/* 107 */       for (Ticket ticket : entry.getValue()) {
/* 108 */         addTicket(entry.getLongKey(), ticket);
/*     */       } }
/*     */     
/* 111 */     this.deactivatedTickets.clear();
/*     */   }
/*     */   
/*     */   public void setLoadingChunkUpdatedListener(ChunkUpdated paramChunkUpdated) {
/* 115 */     this.loadingChunkUpdatedListener = paramChunkUpdated;
/*     */   }
/*     */   
/*     */   public void setSimulationChunkUpdatedListener(ChunkUpdated paramChunkUpdated) {
/* 119 */     this.simulationChunkUpdatedListener = paramChunkUpdated;
/*     */   }
/*     */   
/*     */   public boolean hasTickets() {
/* 123 */     return !this.tickets.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean shouldKeepDimensionActive() {
/* 127 */     for (ObjectIterator<List> objectIterator = this.tickets.values().iterator(); objectIterator.hasNext(); ) { List list = objectIterator.next();
/* 128 */       for (Ticket ticket : list) {
/* 129 */         if (ticket.getType().shouldKeepDimensionActive()) {
/* 130 */           return true;
/*     */         }
/*     */       }  }
/*     */     
/* 134 */     return false;
/*     */   }
/*     */   
/*     */   public List<Ticket> getTickets(long paramLong) {
/* 138 */     return (List<Ticket>)this.tickets.getOrDefault(paramLong, List.of());
/*     */   }
/*     */   
/*     */   private List<Ticket> getOrCreateTickets(long paramLong) {
/* 142 */     return (List<Ticket>)this.tickets.computeIfAbsent(paramLong, paramLong -> new ObjectArrayList(4));
/*     */   }
/*     */   
/*     */   public void addTicketWithRadius(TicketType paramTicketType, ChunkPos paramChunkPos, int paramInt) {
/* 146 */     Ticket ticket = new Ticket(paramTicketType, ChunkLevel.byStatus(FullChunkStatus.FULL) - paramInt);
/* 147 */     addTicket(paramChunkPos.toLong(), ticket);
/*     */   }
/*     */   
/*     */   public void addTicket(Ticket paramTicket, ChunkPos paramChunkPos) {
/* 151 */     addTicket(paramChunkPos.toLong(), paramTicket);
/*     */   }
/*     */   
/*     */   public boolean addTicket(long paramLong, Ticket paramTicket) {
/* 155 */     List<Ticket> list = getOrCreateTickets(paramLong);
/* 156 */     for (Ticket ticket : list) {
/* 157 */       if (isTicketSameTypeAndLevel(paramTicket, ticket)) {
/* 158 */         ticket.resetTicksLeft();
/* 159 */         setDirty();
/* 160 */         return false;
/*     */       } 
/*     */     } 
/*     */     
/* 164 */     int i = getTicketLevelAt(list, true);
/* 165 */     int j = getTicketLevelAt(list, false);
/* 166 */     list.add(paramTicket);
/*     */     
/* 168 */     if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/* 169 */       LOGGER.debug("ATI {} {}", new ChunkPos(paramLong), paramTicket);
/*     */     }
/* 171 */     if (paramTicket.getType().doesSimulate() && 
/* 172 */       paramTicket.getTicketLevel() < i && this.simulationChunkUpdatedListener != null) {
/* 173 */       this.simulationChunkUpdatedListener.update(paramLong, paramTicket.getTicketLevel(), true);
/*     */     }
/*     */     
/* 176 */     if (paramTicket.getType().doesLoad() && 
/* 177 */       paramTicket.getTicketLevel() < j && this.loadingChunkUpdatedListener != null) {
/* 178 */       this.loadingChunkUpdatedListener.update(paramLong, paramTicket.getTicketLevel(), true);
/*     */     }
/*     */     
/* 181 */     if (paramTicket.getType().equals(TicketType.FORCED)) {
/* 182 */       this.chunksWithForcedTickets.add(paramLong);
/*     */     }
/* 184 */     setDirty();
/* 185 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isTicketSameTypeAndLevel(Ticket paramTicket1, Ticket paramTicket2) {
/* 189 */     return (paramTicket2.getType() == paramTicket1.getType() && paramTicket2.getTicketLevel() == paramTicket1.getTicketLevel());
/*     */   }
/*     */   
/*     */   public int getTicketLevelAt(long paramLong, boolean paramBoolean) {
/* 193 */     return getTicketLevelAt(getTickets(paramLong), paramBoolean);
/*     */   }
/*     */   
/*     */   private static int getTicketLevelAt(List<Ticket> paramList, boolean paramBoolean) {
/* 197 */     Ticket ticket = getLowestTicket(paramList, paramBoolean);
/* 198 */     return (ticket == null) ? (ChunkLevel.MAX_LEVEL + 1) : ticket.getTicketLevel();
/*     */   }
/*     */   
/*     */   private static Ticket getLowestTicket(List<Ticket> paramList, boolean paramBoolean) {
/* 202 */     if (paramList == null) {
/* 203 */       return null;
/*     */     }
/* 205 */     Ticket ticket = null;
/* 206 */     for (Ticket ticket1 : paramList) {
/* 207 */       if (ticket == null || ticket1.getTicketLevel() < ticket.getTicketLevel()) {
/* 208 */         if (paramBoolean && ticket1.getType().doesSimulate()) {
/* 209 */           ticket = ticket1; continue;
/* 210 */         }  if (!paramBoolean && ticket1.getType().doesLoad()) {
/* 211 */           ticket = ticket1;
/*     */         }
/*     */       } 
/*     */     } 
/* 215 */     return ticket;
/*     */   }
/*     */   
/*     */   public void removeTicketWithRadius(TicketType paramTicketType, ChunkPos paramChunkPos, int paramInt) {
/* 219 */     Ticket ticket = new Ticket(paramTicketType, ChunkLevel.byStatus(FullChunkStatus.FULL) - paramInt);
/* 220 */     removeTicket(paramChunkPos.toLong(), ticket);
/*     */   }
/*     */   
/*     */   public void removeTicket(Ticket paramTicket, ChunkPos paramChunkPos) {
/* 224 */     removeTicket(paramChunkPos.toLong(), paramTicket);
/*     */   }
/*     */   
/*     */   public boolean removeTicket(long paramLong, Ticket paramTicket) {
/* 228 */     List<Ticket> list = (List)this.tickets.get(paramLong);
/* 229 */     if (list == null) {
/* 230 */       return false;
/*     */     }
/*     */     
/* 233 */     boolean bool = false;
/* 234 */     for (Iterator<Ticket> iterator = list.iterator(); iterator.hasNext(); ) {
/* 235 */       Ticket ticket = iterator.next();
/* 236 */       if (isTicketSameTypeAndLevel(paramTicket, ticket)) {
/* 237 */         iterator.remove();
/* 238 */         if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/* 239 */           LOGGER.debug("RTI {} {}", new ChunkPos(paramLong), ticket);
/*     */         }
/* 241 */         bool = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 246 */     if (!bool) {
/* 247 */       return false;
/*     */     }
/*     */     
/* 250 */     if (list.isEmpty()) {
/* 251 */       this.tickets.remove(paramLong);
/*     */     }
/*     */     
/* 254 */     if (paramTicket.getType().doesSimulate() && this.simulationChunkUpdatedListener != null) {
/* 255 */       this.simulationChunkUpdatedListener.update(paramLong, getTicketLevelAt(list, true), false);
/*     */     }
/* 257 */     if (paramTicket.getType().doesLoad() && this.loadingChunkUpdatedListener != null) {
/* 258 */       this.loadingChunkUpdatedListener.update(paramLong, getTicketLevelAt(list, false), false);
/*     */     }
/* 260 */     if (paramTicket.getType().equals(TicketType.FORCED)) {
/* 261 */       updateForcedChunks();
/*     */     }
/* 263 */     setDirty();
/* 264 */     return true;
/*     */   }
/*     */   
/*     */   private void updateForcedChunks() {
/* 268 */     this.chunksWithForcedTickets = getAllChunksWithTicketThat(paramTicket -> paramTicket.getType().equals(TicketType.FORCED));
/*     */   }
/*     */   
/*     */   public String getTicketDebugString(long paramLong, boolean paramBoolean) {
/* 272 */     List<Ticket> list = getTickets(paramLong);
/* 273 */     Ticket ticket = getLowestTicket(list, paramBoolean);
/* 274 */     return (ticket == null) ? "no_ticket" : ticket.toString();
/*     */   }
/*     */   
/*     */   public void purgeStaleTickets(ChunkMap paramChunkMap) {
/* 278 */     removeTicketIf((paramTicket, paramLong) -> { if (canTicketExpire(paramChunkMap, paramTicket, paramLong)) { paramTicket.decreaseTicksLeft(); return paramTicket.isTimedOut(); }  return false; }null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 285 */     setDirty();
/*     */   }
/*     */   
/*     */   private boolean canTicketExpire(ChunkMap paramChunkMap, Ticket paramTicket, long paramLong) {
/* 289 */     if (!paramTicket.getType().hasTimeout()) {
/* 290 */       return false;
/*     */     }
/* 292 */     if (paramTicket.getType().canExpireIfUnloaded()) {
/* 293 */       return true;
/*     */     }
/* 295 */     ChunkHolder chunkHolder = paramChunkMap.getUpdatingChunkIfPresent(paramLong);
/*     */ 
/*     */ 
/*     */     
/* 299 */     return (chunkHolder == null || chunkHolder.isReadyForSaving());
/*     */   }
/*     */ 
/*     */   
/*     */   public void deactivateTicketsOnClosing() {
/* 304 */     removeTicketIf((paramTicket, paramLong) -> (paramTicket.getType() != TicketType.UNKNOWN), this.deactivatedTickets);
/*     */   }
/*     */   
/*     */   public void removeTicketIf(TicketPredicate paramTicketPredicate, Long2ObjectOpenHashMap<List<Ticket>> paramLong2ObjectOpenHashMap) {
/* 308 */     ObjectIterator objectIterator = this.tickets.long2ObjectEntrySet().fastIterator();
/* 309 */     boolean bool = false;
/* 310 */     while (objectIterator.hasNext()) {
/* 311 */       Long2ObjectMap.Entry entry = (Long2ObjectMap.Entry)objectIterator.next();
/* 312 */       Iterator<Ticket> iterator = ((List)entry.getValue()).iterator();
/* 313 */       long l = entry.getLongKey();
/* 314 */       boolean bool1 = false;
/* 315 */       boolean bool2 = false;
/* 316 */       while (iterator.hasNext()) {
/* 317 */         Ticket ticket = iterator.next();
/* 318 */         if (paramTicketPredicate.test(ticket, l)) {
/* 319 */           if (paramLong2ObjectOpenHashMap != null) {
/* 320 */             List<Ticket> list = (List)paramLong2ObjectOpenHashMap.computeIfAbsent(l, paramLong -> new ObjectArrayList(((List)paramEntry.getValue()).size()));
/* 321 */             list.add(ticket);
/*     */           } 
/* 323 */           iterator.remove();
/* 324 */           if (ticket.getType().doesLoad()) {
/* 325 */             bool2 = true;
/*     */           }
/* 327 */           if (ticket.getType().doesSimulate()) {
/* 328 */             bool1 = true;
/*     */           }
/* 330 */           if (ticket.getType().equals(TicketType.FORCED)) {
/* 331 */             bool = true;
/*     */           }
/*     */         } 
/*     */       } 
/* 335 */       if (!bool2 && !bool1) {
/*     */         continue;
/*     */       }
/* 338 */       if (bool2 && this.loadingChunkUpdatedListener != null) {
/* 339 */         this.loadingChunkUpdatedListener.update(l, getTicketLevelAt((List<Ticket>)entry.getValue(), false), false);
/*     */       }
/* 341 */       if (bool1 && this.simulationChunkUpdatedListener != null) {
/* 342 */         this.simulationChunkUpdatedListener.update(l, getTicketLevelAt((List<Ticket>)entry.getValue(), true), false);
/*     */       }
/* 344 */       setDirty();
/* 345 */       if (((List)entry.getValue()).isEmpty()) {
/* 346 */         objectIterator.remove();
/*     */       }
/*     */     } 
/* 349 */     if (bool) {
/* 350 */       updateForcedChunks();
/*     */     }
/*     */   }
/*     */   
/*     */   public void replaceTicketLevelOfType(int paramInt, TicketType paramTicketType) {
/* 355 */     ArrayList<Pair> arrayList = new ArrayList();
/* 356 */     for (ObjectIterator<Long2ObjectMap.Entry> objectIterator = this.tickets.long2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) { Long2ObjectMap.Entry entry = objectIterator.next();
/* 357 */       for (Ticket ticket : entry.getValue()) {
/* 358 */         if (ticket.getType() == paramTicketType) {
/* 359 */           arrayList.add(Pair.of(ticket, Long.valueOf(entry.getLongKey())));
/*     */         }
/*     */       }  }
/*     */     
/* 363 */     for (Pair pair : arrayList) {
/* 364 */       Long long_ = (Long)pair.getSecond();
/* 365 */       Ticket ticket = (Ticket)pair.getFirst();
/* 366 */       removeTicket(long_.longValue(), ticket);
/* 367 */       TicketType ticketType = ticket.getType();
/* 368 */       addTicket(long_.longValue(), new Ticket(ticketType, paramInt));
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean updateChunkForced(ChunkPos paramChunkPos, boolean paramBoolean) {
/* 373 */     Ticket ticket = new Ticket(TicketType.FORCED, ChunkMap.FORCED_TICKET_LEVEL);
/* 374 */     if (paramBoolean) {
/* 375 */       return addTicket(paramChunkPos.toLong(), ticket);
/*     */     }
/* 377 */     return removeTicket(paramChunkPos.toLong(), ticket);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LongSet getForceLoadedChunks() {
/* 383 */     return this.chunksWithForcedTickets;
/*     */   }
/*     */   
/*     */   private LongSet getAllChunksWithTicketThat(Predicate<Ticket> paramPredicate) {
/* 387 */     LongOpenHashSet longOpenHashSet = new LongOpenHashSet();
/* 388 */     for (ObjectIterator<Long2ObjectMap.Entry> objectIterator = Long2ObjectMaps.fastIterable((Long2ObjectMap)this.tickets).iterator(); objectIterator.hasNext(); ) { Long2ObjectMap.Entry entry = objectIterator.next();
/* 389 */       for (Ticket ticket : entry.getValue()) {
/* 390 */         if (paramPredicate.test(ticket)) {
/* 391 */           longOpenHashSet.add(entry.getLongKey());
/*     */         }
/*     */       }  }
/*     */ 
/*     */     
/* 396 */     return (LongSet)longOpenHashSet;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ChunkUpdated {
/*     */     void update(long param1Long, int param1Int, boolean param1Boolean);
/*     */   }
/*     */   
/*     */   public static interface TicketPredicate {
/*     */     boolean test(Ticket param1Ticket, long param1Long);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\TicketStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */