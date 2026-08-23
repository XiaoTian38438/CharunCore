/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugChunkValuePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugEntityValuePacket;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TrackingDebugSynchronizer<T>
/*     */ {
/*     */   protected final DebugSubscription<T> subscription;
/*  36 */   private final Set<UUID> subscribedPlayers = (Set<UUID>)new ObjectOpenHashSet();
/*     */   
/*     */   public TrackingDebugSynchronizer(DebugSubscription<T> paramDebugSubscription) {
/*  39 */     this.subscription = paramDebugSubscription;
/*     */   }
/*     */   
/*     */   public final void tick(ServerLevel paramServerLevel) {
/*  43 */     for (ServerPlayer serverPlayer : paramServerLevel.players()) {
/*  44 */       boolean bool1 = this.subscribedPlayers.contains(serverPlayer.getUUID());
/*  45 */       boolean bool2 = serverPlayer.debugSubscriptions().contains(this.subscription);
/*  46 */       if (bool2 == bool1) {
/*     */         continue;
/*     */       }
/*  49 */       if (bool2) {
/*  50 */         addSubscriber(serverPlayer); continue;
/*     */       } 
/*  52 */       this.subscribedPlayers.remove(serverPlayer.getUUID());
/*     */     } 
/*     */     
/*  55 */     this.subscribedPlayers.removeIf(paramUUID -> (paramServerLevel.getPlayerByUUID(paramUUID) == null));
/*     */ 
/*     */     
/*  58 */     if (!this.subscribedPlayers.isEmpty()) {
/*  59 */       pollAndSendUpdates(paramServerLevel);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addSubscriber(ServerPlayer paramServerPlayer) {
/*  64 */     this.subscribedPlayers.add(paramServerPlayer.getUUID());
/*     */     
/*  66 */     paramServerPlayer.getChunkTrackingView().forEach(paramChunkPos -> {
/*     */           if (!paramServerPlayer.connection.chunkSender.isPending(paramChunkPos.toLong())) {
/*     */             startTrackingChunk(paramServerPlayer, paramChunkPos);
/*     */           }
/*     */         });
/*     */     
/*  72 */     (paramServerPlayer.level().getChunkSource()).chunkMap.forEachEntityTrackedBy(paramServerPlayer, paramEntity -> startTrackingEntity(paramServerPlayer, paramEntity));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void sendToPlayersTrackingChunk(ServerLevel paramServerLevel, ChunkPos paramChunkPos, Packet<? super ClientGamePacketListener> paramPacket) {
/*  78 */     ChunkMap chunkMap = (paramServerLevel.getChunkSource()).chunkMap;
/*  79 */     for (UUID uUID : this.subscribedPlayers) {
/*  80 */       Player player = paramServerLevel.getPlayerByUUID(uUID); if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player; if (chunkMap.isChunkTracked(serverPlayer, paramChunkPos.x, paramChunkPos.z))
/*  81 */           serverPlayer.connection.send(paramPacket);  }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void sendToPlayersTrackingEntity(ServerLevel paramServerLevel, Entity paramEntity, Packet<? super ClientGamePacketListener> paramPacket) {
/*  87 */     ChunkMap chunkMap = (paramServerLevel.getChunkSource()).chunkMap;
/*  88 */     chunkMap.sendToTrackingPlayersFiltered(paramEntity, paramPacket, paramServerPlayer -> this.subscribedPlayers.contains(paramServerPlayer.getUUID()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void startTrackingChunk(ServerPlayer paramServerPlayer, ChunkPos paramChunkPos) {
/*  96 */     if (this.subscribedPlayers.contains(paramServerPlayer.getUUID())) {
/*  97 */       sendInitialChunk(paramServerPlayer, paramChunkPos);
/*     */     }
/*     */   }
/*     */   
/*     */   public final void startTrackingEntity(ServerPlayer paramServerPlayer, Entity paramEntity) {
/* 102 */     if (this.subscribedPlayers.contains(paramServerPlayer.getUUID())) {
/* 103 */       sendInitialEntity(paramServerPlayer, paramEntity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void clear() {}
/*     */ 
/*     */   
/*     */   protected void pollAndSendUpdates(ServerLevel paramServerLevel) {}
/*     */   
/*     */   protected void sendInitialChunk(ServerPlayer paramServerPlayer, ChunkPos paramChunkPos) {}
/*     */   
/*     */   protected void sendInitialEntity(ServerPlayer paramServerPlayer, Entity paramEntity) {}
/*     */   
/*     */   public static class SourceSynchronizer<T>
/*     */     extends TrackingDebugSynchronizer<T>
/*     */   {
/* 120 */     private final Map<ChunkPos, TrackingDebugSynchronizer.ValueSource<T>> chunkSources = new HashMap<>();
/* 121 */     private final Map<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> blockEntitySources = new HashMap<>();
/* 122 */     private final Map<UUID, TrackingDebugSynchronizer.ValueSource<T>> entitySources = new HashMap<>();
/*     */     
/*     */     public SourceSynchronizer(DebugSubscription<T> param1DebugSubscription) {
/* 125 */       super(param1DebugSubscription);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void clear() {
/* 130 */       this.chunkSources.clear();
/* 131 */       this.blockEntitySources.clear();
/* 132 */       this.entitySources.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void pollAndSendUpdates(ServerLevel param1ServerLevel) {
/* 137 */       for (Map.Entry<ChunkPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.chunkSources.entrySet()) {
/* 138 */         DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource<T>)entry.getValue()).pollUpdate(this.subscription);
/* 139 */         if (update != null) {
/* 140 */           ChunkPos chunkPos = (ChunkPos)entry.getKey();
/* 141 */           sendToPlayersTrackingChunk(param1ServerLevel, chunkPos, (Packet<? super ClientGamePacketListener>)new ClientboundDebugChunkValuePacket(chunkPos, update));
/*     */         } 
/*     */       } 
/*     */       
/* 145 */       for (Map.Entry<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.blockEntitySources.entrySet()) {
/* 146 */         DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource<T>)entry.getValue()).pollUpdate(this.subscription);
/* 147 */         if (update != null) {
/* 148 */           BlockPos blockPos = (BlockPos)entry.getKey();
/* 149 */           ChunkPos chunkPos = new ChunkPos(blockPos);
/* 150 */           sendToPlayersTrackingChunk(param1ServerLevel, chunkPos, (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(blockPos, update));
/*     */         } 
/*     */       } 
/*     */       
/* 154 */       for (Map.Entry<UUID, TrackingDebugSynchronizer.ValueSource<T>> entry : this.entitySources.entrySet()) {
/* 155 */         DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource<T>)entry.getValue()).pollUpdate(this.subscription);
/* 156 */         if (update != null) {
/* 157 */           Entity entity = Objects.<Entity>requireNonNull(param1ServerLevel.getEntity((UUID)entry.getKey()));
/* 158 */           sendToPlayersTrackingEntity(param1ServerLevel, entity, (Packet<? super ClientGamePacketListener>)new ClientboundDebugEntityValuePacket(entity.getId(), update));
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void registerChunk(ChunkPos param1ChunkPos, DebugValueSource.ValueGetter<T> param1ValueGetter) {
/* 164 */       this.chunkSources.put(param1ChunkPos, new TrackingDebugSynchronizer.ValueSource<>(param1ValueGetter));
/*     */     }
/*     */     
/*     */     public void registerBlockEntity(BlockPos param1BlockPos, DebugValueSource.ValueGetter<T> param1ValueGetter) {
/* 168 */       this.blockEntitySources.put(param1BlockPos, new TrackingDebugSynchronizer.ValueSource<>(param1ValueGetter));
/*     */     }
/*     */     
/*     */     public void registerEntity(UUID param1UUID, DebugValueSource.ValueGetter<T> param1ValueGetter) {
/* 172 */       this.entitySources.put(param1UUID, new TrackingDebugSynchronizer.ValueSource<>(param1ValueGetter));
/*     */     }
/*     */     
/*     */     public void dropChunk(ChunkPos param1ChunkPos) {
/* 176 */       this.chunkSources.remove(param1ChunkPos);
/*     */ 
/*     */       
/* 179 */       Objects.requireNonNull(param1ChunkPos); this.blockEntitySources.keySet().removeIf(param1ChunkPos::contains);
/*     */     }
/*     */     
/*     */     public void dropBlockEntity(ServerLevel param1ServerLevel, BlockPos param1BlockPos) {
/* 183 */       TrackingDebugSynchronizer.ValueSource valueSource = this.blockEntitySources.remove(param1BlockPos);
/* 184 */       if (valueSource != null) {
/* 185 */         ChunkPos chunkPos = new ChunkPos(param1BlockPos);
/* 186 */         sendToPlayersTrackingChunk(param1ServerLevel, chunkPos, (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(param1BlockPos, this.subscription.emptyUpdate()));
/*     */       } 
/*     */     }
/*     */     
/*     */     public void dropEntity(Entity param1Entity) {
/* 191 */       this.entitySources.remove(param1Entity.getUUID());
/*     */     }
/*     */ 
/*     */     
/*     */     protected void sendInitialChunk(ServerPlayer param1ServerPlayer, ChunkPos param1ChunkPos) {
/* 196 */       TrackingDebugSynchronizer.ValueSource valueSource = this.chunkSources.get(param1ChunkPos);
/* 197 */       if (valueSource != null && valueSource.lastSyncedValue != null) {
/* 198 */         param1ServerPlayer.connection.send((Packet)new ClientboundDebugChunkValuePacket(param1ChunkPos, this.subscription.packUpdate(valueSource.lastSyncedValue)));
/*     */       }
/*     */       
/* 201 */       for (Map.Entry<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.blockEntitySources.entrySet()) {
/* 202 */         T t = ((TrackingDebugSynchronizer.ValueSource)entry.getValue()).lastSyncedValue;
/* 203 */         if (t == null) {
/*     */           continue;
/*     */         }
/* 206 */         BlockPos blockPos = (BlockPos)entry.getKey();
/* 207 */         if (param1ChunkPos.contains(blockPos)) {
/* 208 */           param1ServerPlayer.connection.send((Packet)new ClientboundDebugBlockValuePacket(blockPos, this.subscription.packUpdate(t)));
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void sendInitialEntity(ServerPlayer param1ServerPlayer, Entity param1Entity) {
/* 215 */       TrackingDebugSynchronizer.ValueSource valueSource = this.entitySources.get(param1Entity.getUUID());
/* 216 */       if (valueSource != null && valueSource.lastSyncedValue != null)
/* 217 */         param1ServerPlayer.connection.send((Packet)new ClientboundDebugEntityValuePacket(param1Entity.getId(), this.subscription.packUpdate(valueSource.lastSyncedValue))); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ValueSource<T>
/*     */   {
/*     */     private final DebugValueSource.ValueGetter<T> getter;
/*     */     T lastSyncedValue;
/*     */     
/*     */     ValueSource(DebugValueSource.ValueGetter<T> param1ValueGetter) {
/* 227 */       this.getter = param1ValueGetter;
/*     */     }
/*     */     
/*     */     public DebugSubscription.Update<T> pollUpdate(DebugSubscription<T> param1DebugSubscription) {
/* 231 */       T t = this.getter.get();
/* 232 */       if (!Objects.equals(t, this.lastSyncedValue)) {
/* 233 */         this.lastSyncedValue = t;
/* 234 */         return param1DebugSubscription.packUpdate(t);
/*     */       } 
/* 236 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PoiSynchronizer extends TrackingDebugSynchronizer<DebugPoiInfo> {
/*     */     public PoiSynchronizer() {
/* 242 */       super(DebugSubscriptions.POIS);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void sendInitialChunk(ServerPlayer param1ServerPlayer, ChunkPos param1ChunkPos) {
/* 247 */       ServerLevel serverLevel = param1ServerPlayer.level();
/* 248 */       PoiManager poiManager = serverLevel.getPoiManager();
/* 249 */       poiManager.getInChunk(param1Holder -> true, param1ChunkPos, PoiManager.Occupancy.ANY).forEach(param1PoiRecord -> param1ServerPlayer.connection.send((Packet)new ClientboundDebugBlockValuePacket(param1PoiRecord.getPos(), this.subscription.packUpdate(new DebugPoiInfo(param1PoiRecord)))));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onPoiAdded(ServerLevel param1ServerLevel, PoiRecord param1PoiRecord) {
/* 257 */       sendToPlayersTrackingChunk(param1ServerLevel, new ChunkPos(param1PoiRecord.getPos()), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(param1PoiRecord.getPos(), this.subscription.packUpdate(new DebugPoiInfo(param1PoiRecord))));
/*     */     }
/*     */     
/*     */     public void onPoiRemoved(ServerLevel param1ServerLevel, BlockPos param1BlockPos) {
/* 261 */       sendToPlayersTrackingChunk(param1ServerLevel, new ChunkPos(param1BlockPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(param1BlockPos, this.subscription.emptyUpdate()));
/*     */     }
/*     */     
/*     */     public void onPoiTicketCountChanged(ServerLevel param1ServerLevel, BlockPos param1BlockPos) {
/* 265 */       sendToPlayersTrackingChunk(param1ServerLevel, new ChunkPos(param1BlockPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(param1BlockPos, this.subscription.packUpdate(param1ServerLevel.getPoiManager().getDebugPoiInfo(param1BlockPos))));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class VillageSectionSynchronizer extends TrackingDebugSynchronizer<Unit> {
/*     */     public VillageSectionSynchronizer() {
/* 271 */       super(DebugSubscriptions.VILLAGE_SECTIONS);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void sendInitialChunk(ServerPlayer param1ServerPlayer, ChunkPos param1ChunkPos) {
/* 276 */       ServerLevel serverLevel = param1ServerPlayer.level();
/* 277 */       PoiManager poiManager = serverLevel.getPoiManager();
/* 278 */       poiManager.getInChunk(param1Holder -> true, param1ChunkPos, PoiManager.Occupancy.ANY).forEach(param1PoiRecord -> {
/*     */             SectionPos sectionPos = SectionPos.of(param1PoiRecord.getPos());
/*     */             forEachVillageSectionUpdate(param1ServerLevel, sectionPos, ());
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onPoiAdded(ServerLevel param1ServerLevel, PoiRecord param1PoiRecord) {
/* 290 */       sendVillageSectionsPacket(param1ServerLevel, param1PoiRecord.getPos());
/*     */     }
/*     */     
/*     */     public void onPoiRemoved(ServerLevel param1ServerLevel, BlockPos param1BlockPos) {
/* 294 */       sendVillageSectionsPacket(param1ServerLevel, param1BlockPos);
/*     */     }
/*     */     
/*     */     private void sendVillageSectionsPacket(ServerLevel param1ServerLevel, BlockPos param1BlockPos) {
/* 298 */       forEachVillageSectionUpdate(param1ServerLevel, SectionPos.of(param1BlockPos), (param1SectionPos, param1Boolean) -> {
/*     */             BlockPos blockPos = param1SectionPos.center();
/*     */             if (param1Boolean.booleanValue()) {
/*     */               sendToPlayersTrackingChunk(param1ServerLevel, new ChunkPos(blockPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(blockPos, this.subscription.packUpdate(Unit.INSTANCE)));
/*     */             } else {
/*     */               sendToPlayersTrackingChunk(param1ServerLevel, new ChunkPos(blockPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(blockPos, this.subscription.emptyUpdate()));
/*     */             } 
/*     */           });
/*     */     }
/*     */     
/*     */     private static void forEachVillageSectionUpdate(ServerLevel param1ServerLevel, SectionPos param1SectionPos, BiConsumer<SectionPos, Boolean> param1BiConsumer) {
/* 309 */       for (byte b = -1; b <= 1; b++) {
/* 310 */         for (byte b1 = -1; b1 <= 1; b1++) {
/* 311 */           for (byte b2 = -1; b2 <= 1; b2++) {
/* 312 */             SectionPos sectionPos = param1SectionPos.offset(b1, b2, b);
/* 313 */             if (param1ServerLevel.isVillage(sectionPos.center())) {
/* 314 */               param1BiConsumer.accept(sectionPos, Boolean.valueOf(true));
/*     */             } else {
/* 316 */               param1BiConsumer.accept(sectionPos, Boolean.valueOf(false));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\debug\TrackingDebugSynchronizer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */