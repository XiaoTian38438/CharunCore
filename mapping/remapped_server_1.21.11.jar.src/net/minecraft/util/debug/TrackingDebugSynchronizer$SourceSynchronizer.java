/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugChunkValuePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugEntityValuePacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SourceSynchronizer<T>
/*     */   extends TrackingDebugSynchronizer<T>
/*     */ {
/* 120 */   private final Map<ChunkPos, TrackingDebugSynchronizer.ValueSource<T>> chunkSources = new HashMap<>();
/* 121 */   private final Map<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> blockEntitySources = new HashMap<>();
/* 122 */   private final Map<UUID, TrackingDebugSynchronizer.ValueSource<T>> entitySources = new HashMap<>();
/*     */   
/*     */   public SourceSynchronizer(DebugSubscription<T> paramDebugSubscription) {
/* 125 */     super(paramDebugSubscription);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void clear() {
/* 130 */     this.chunkSources.clear();
/* 131 */     this.blockEntitySources.clear();
/* 132 */     this.entitySources.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pollAndSendUpdates(ServerLevel paramServerLevel) {
/* 137 */     for (Map.Entry<ChunkPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.chunkSources.entrySet()) {
/* 138 */       DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource<T>)entry.getValue()).pollUpdate(this.subscription);
/* 139 */       if (update != null) {
/* 140 */         ChunkPos chunkPos = (ChunkPos)entry.getKey();
/* 141 */         sendToPlayersTrackingChunk(paramServerLevel, chunkPos, (Packet<? super ClientGamePacketListener>)new ClientboundDebugChunkValuePacket(chunkPos, update));
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     for (Map.Entry<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.blockEntitySources.entrySet()) {
/* 146 */       DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource<T>)entry.getValue()).pollUpdate(this.subscription);
/* 147 */       if (update != null) {
/* 148 */         BlockPos blockPos = (BlockPos)entry.getKey();
/* 149 */         ChunkPos chunkPos = new ChunkPos(blockPos);
/* 150 */         sendToPlayersTrackingChunk(paramServerLevel, chunkPos, (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(blockPos, update));
/*     */       } 
/*     */     } 
/*     */     
/* 154 */     for (Map.Entry<UUID, TrackingDebugSynchronizer.ValueSource<T>> entry : this.entitySources.entrySet()) {
/* 155 */       DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource<T>)entry.getValue()).pollUpdate(this.subscription);
/* 156 */       if (update != null) {
/* 157 */         Entity entity = Objects.<Entity>requireNonNull(paramServerLevel.getEntity((UUID)entry.getKey()));
/* 158 */         sendToPlayersTrackingEntity(paramServerLevel, entity, (Packet<? super ClientGamePacketListener>)new ClientboundDebugEntityValuePacket(entity.getId(), update));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void registerChunk(ChunkPos paramChunkPos, DebugValueSource.ValueGetter<T> paramValueGetter) {
/* 164 */     this.chunkSources.put(paramChunkPos, new TrackingDebugSynchronizer.ValueSource<>(paramValueGetter));
/*     */   }
/*     */   
/*     */   public void registerBlockEntity(BlockPos paramBlockPos, DebugValueSource.ValueGetter<T> paramValueGetter) {
/* 168 */     this.blockEntitySources.put(paramBlockPos, new TrackingDebugSynchronizer.ValueSource<>(paramValueGetter));
/*     */   }
/*     */   
/*     */   public void registerEntity(UUID paramUUID, DebugValueSource.ValueGetter<T> paramValueGetter) {
/* 172 */     this.entitySources.put(paramUUID, new TrackingDebugSynchronizer.ValueSource<>(paramValueGetter));
/*     */   }
/*     */   
/*     */   public void dropChunk(ChunkPos paramChunkPos) {
/* 176 */     this.chunkSources.remove(paramChunkPos);
/*     */ 
/*     */     
/* 179 */     Objects.requireNonNull(paramChunkPos); this.blockEntitySources.keySet().removeIf(paramChunkPos::contains);
/*     */   }
/*     */   
/*     */   public void dropBlockEntity(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 183 */     TrackingDebugSynchronizer.ValueSource valueSource = this.blockEntitySources.remove(paramBlockPos);
/* 184 */     if (valueSource != null) {
/* 185 */       ChunkPos chunkPos = new ChunkPos(paramBlockPos);
/* 186 */       sendToPlayersTrackingChunk(paramServerLevel, chunkPos, (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(paramBlockPos, this.subscription.emptyUpdate()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void dropEntity(Entity paramEntity) {
/* 191 */     this.entitySources.remove(paramEntity.getUUID());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void sendInitialChunk(ServerPlayer paramServerPlayer, ChunkPos paramChunkPos) {
/* 196 */     TrackingDebugSynchronizer.ValueSource valueSource = this.chunkSources.get(paramChunkPos);
/* 197 */     if (valueSource != null && valueSource.lastSyncedValue != null) {
/* 198 */       paramServerPlayer.connection.send((Packet)new ClientboundDebugChunkValuePacket(paramChunkPos, this.subscription.packUpdate(valueSource.lastSyncedValue)));
/*     */     }
/*     */     
/* 201 */     for (Map.Entry<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.blockEntitySources.entrySet()) {
/* 202 */       T t = ((TrackingDebugSynchronizer.ValueSource)entry.getValue()).lastSyncedValue;
/* 203 */       if (t == null) {
/*     */         continue;
/*     */       }
/* 206 */       BlockPos blockPos = (BlockPos)entry.getKey();
/* 207 */       if (paramChunkPos.contains(blockPos)) {
/* 208 */         paramServerPlayer.connection.send((Packet)new ClientboundDebugBlockValuePacket(blockPos, this.subscription.packUpdate(t)));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void sendInitialEntity(ServerPlayer paramServerPlayer, Entity paramEntity) {
/* 215 */     TrackingDebugSynchronizer.ValueSource valueSource = this.entitySources.get(paramEntity.getUUID());
/* 216 */     if (valueSource != null && valueSource.lastSyncedValue != null)
/* 217 */       paramServerPlayer.connection.send((Packet)new ClientboundDebugEntityValuePacket(paramEntity.getId(), this.subscription.packUpdate(valueSource.lastSyncedValue))); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\debug\TrackingDebugSynchronizer$SourceSynchronizer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */