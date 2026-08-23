/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.thread.ConsecutiveExecutor;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.entity.ChunkEntities;
/*     */ import net.minecraft.world.level.entity.EntityPersistentStorage;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EntityStorage implements EntityPersistentStorage<Entity> {
/*  32 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String ENTITIES_TAG = "Entities";
/*     */   
/*     */   private static final String POSITION_TAG = "Position";
/*     */   private final ServerLevel level;
/*     */   private final SimpleRegionStorage simpleRegionStorage;
/*  39 */   private final LongSet emptyChunks = (LongSet)new LongOpenHashSet();
/*     */   private final ConsecutiveExecutor entityDeserializerQueue;
/*     */   
/*     */   public EntityStorage(SimpleRegionStorage paramSimpleRegionStorage, ServerLevel paramServerLevel, Executor paramExecutor) {
/*  43 */     this.simpleRegionStorage = paramSimpleRegionStorage;
/*  44 */     this.level = paramServerLevel;
/*  45 */     this.entityDeserializerQueue = new ConsecutiveExecutor(paramExecutor, "entity-deserializer");
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkEntities<Entity>> loadEntities(ChunkPos paramChunkPos) {
/*  50 */     if (this.emptyChunks.contains(paramChunkPos.toLong())) {
/*  51 */       return CompletableFuture.completedFuture(emptyChunk(paramChunkPos));
/*     */     }
/*  53 */     CompletableFuture<Optional<CompoundTag>> completableFuture = this.simpleRegionStorage.read(paramChunkPos);
/*  54 */     reportLoadFailureIfPresent(completableFuture, paramChunkPos);
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
/*  86 */     Objects.requireNonNull(this.entityDeserializerQueue); return completableFuture.thenApplyAsync(paramOptional -> { if (paramOptional.isEmpty()) { this.emptyChunks.add(paramChunkPos.toLong()); return emptyChunk(paramChunkPos); }  try { ChunkPos chunkPos = ((CompoundTag)paramOptional.get()).read("Position", ChunkPos.CODEC).orElseThrow(); if (!Objects.equals(paramChunkPos, chunkPos)) { LOGGER.error("Chunk file at {} is in the wrong location. (Expected {}, got {})", new Object[] { paramChunkPos, paramChunkPos, chunkPos }); this.level.getServer().reportMisplacedChunk(chunkPos, paramChunkPos, this.simpleRegionStorage.storageInfo()); }  } catch (Exception exception) { LOGGER.warn("Failed to parse chunk {} position info", paramChunkPos, exception); this.level.getServer().reportChunkLoadFailure(exception, this.simpleRegionStorage.storageInfo(), paramChunkPos); }  CompoundTag compoundTag = this.simpleRegionStorage.upgradeChunkTag(paramOptional.get(), -1); ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(ChunkAccess.problemPath(paramChunkPos), LOGGER); try { ValueInput valueInput = TagValueInput.create((ProblemReporter)scopedCollector, (HolderLookup.Provider)this.level.registryAccess(), compoundTag); ValueInput.ValueInputList valueInputList = valueInput.childrenListOrEmpty("Entities"); List list = EntityType.loadEntitiesRecursive(valueInputList, (Level)this.level, EntitySpawnReason.LOAD).toList(); ChunkEntities chunkEntities = new ChunkEntities(paramChunkPos, list); scopedCollector.close(); return chunkEntities; } catch (Throwable throwable) { try { scopedCollector.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  }this.entityDeserializerQueue::schedule);
/*     */   }
/*     */   
/*     */   private static ChunkEntities<Entity> emptyChunk(ChunkPos paramChunkPos) {
/*  90 */     return new ChunkEntities(paramChunkPos, List.of());
/*     */   }
/*     */ 
/*     */   
/*     */   public void storeEntities(ChunkEntities<Entity> paramChunkEntities) {
/*  95 */     ChunkPos chunkPos = paramChunkEntities.getPos();
/*  96 */     if (paramChunkEntities.isEmpty()) {
/*  97 */       if (this.emptyChunks.add(chunkPos.toLong())) {
/*  98 */         reportSaveFailureIfPresent(this.simpleRegionStorage.write(chunkPos, IOWorker.STORE_EMPTY), chunkPos);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 103 */     ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(ChunkAccess.problemPath(chunkPos), LOGGER); 
/* 104 */     try { ListTag listTag = new ListTag();
/*     */       
/* 106 */       paramChunkEntities.getEntities().forEach(paramEntity -> {
/*     */             TagValueOutput tagValueOutput = TagValueOutput.createWithContext(paramScopedCollector.forChild(paramEntity.problemPath()), (HolderLookup.Provider)paramEntity.registryAccess());
/*     */             
/*     */             if (paramEntity.save((ValueOutput)tagValueOutput)) {
/*     */               CompoundTag compoundTag = tagValueOutput.buildResult();
/*     */               paramListTag.add(compoundTag);
/*     */             } 
/*     */           });
/* 114 */       CompoundTag compoundTag = NbtUtils.addCurrentDataVersion(new CompoundTag());
/* 115 */       compoundTag.put("Entities", (Tag)listTag);
/* 116 */       compoundTag.store("Position", ChunkPos.CODEC, chunkPos);
/* 117 */       reportSaveFailureIfPresent(this.simpleRegionStorage.write(chunkPos, compoundTag), chunkPos);
/* 118 */       this.emptyChunks.remove(chunkPos.toLong());
/* 119 */       scopedCollector.close(); }
/*     */     catch (Throwable throwable) { try { scopedCollector.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 123 */      } private void reportSaveFailureIfPresent(CompletableFuture<?> paramCompletableFuture, ChunkPos paramChunkPos) { paramCompletableFuture.exceptionally(paramThrowable -> {
/*     */           LOGGER.error("Failed to store entity chunk {}", paramChunkPos, paramThrowable);
/*     */           this.level.getServer().reportChunkSaveFailure(paramThrowable, this.simpleRegionStorage.storageInfo(), paramChunkPos);
/*     */           return null;
/*     */         }); }
/*     */ 
/*     */   
/*     */   private void reportLoadFailureIfPresent(CompletableFuture<?> paramCompletableFuture, ChunkPos paramChunkPos) {
/* 131 */     paramCompletableFuture.exceptionally(paramThrowable -> {
/*     */           LOGGER.error("Failed to load entity chunk {}", paramChunkPos, paramThrowable);
/*     */           this.level.getServer().reportChunkLoadFailure(paramThrowable, this.simpleRegionStorage.storageInfo(), paramChunkPos);
/*     */           return null;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush(boolean paramBoolean) {
/* 140 */     this.simpleRegionStorage.synchronize(paramBoolean).join();
/* 141 */     this.entityDeserializerQueue.runAll();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 146 */     this.simpleRegionStorage.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\storage\EntityStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */