/*     */ package net.minecraft.world.level.chunk.status;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.server.level.GenerationChunkHolder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ThreadedLevelLightEngine;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.StaticCache2D;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ImposterProtoChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.ProtoChunk;
/*     */ import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChunkStatusTasks {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static boolean isLighted(ChunkAccess paramChunkAccess) {
/*  31 */     return (paramChunkAccess.getPersistedStatus().isOrAfter(ChunkStatus.LIGHT) && paramChunkAccess.isLightCorrect());
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> passThrough(WorldGenContext paramWorldGenContext, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D, ChunkAccess paramChunkAccess) {
/*  35 */     return CompletableFuture.completedFuture(paramChunkAccess);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateStructureStarts(WorldGenContext paramWorldGenContext, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D, ChunkAccess paramChunkAccess) {
/*  39 */     ServerLevel serverLevel = paramWorldGenContext.level();
/*  40 */     if (serverLevel.getServer().getWorldData().worldGenOptions().generateStructures()) {
/*  41 */       paramWorldGenContext.generator().createStructures(serverLevel.registryAccess(), serverLevel.getChunkSource().getGeneratorState(), serverLevel.structureManager(), paramChunkAccess, paramWorldGenContext.structureManager(), serverLevel.dimension());
/*     */     }
/*  43 */     serverLevel.onStructureStartsAvailable(paramChunkAccess);
/*  44 */     return CompletableFuture.completedFuture(paramChunkAccess);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> loadStructureStarts(WorldGenContext paramWorldGenContext, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D, ChunkAccess paramChunkAccess) {
/*  48 */     paramWorldGenContext.level().onStructureStartsAvailable(paramChunkAccess);
/*  49 */     return CompletableFuture.completedFuture(paramChunkAccess);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateStructureReferences(WorldGenContext paramWorldGenContext, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D, ChunkAccess paramChunkAccess) {
/*  53 */     ServerLevel serverLevel = paramWorldGenContext.level();
/*  54 */     WorldGenRegion worldGenRegion = new WorldGenRegion(serverLevel, paramStaticCache2D, paramChunkStep, paramChunkAccess);
/*  55 */     paramWorldGenContext.generator().createReferences((WorldGenLevel)worldGenRegion, serverLevel.structureManager().forWorldGenRegion(worldGenRegion), paramChunkAccess);
/*  56 */     return CompletableFuture.completedFuture(paramChunkAccess);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateBiomes(WorldGenContext paramWorldGenContext, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D, ChunkAccess paramChunkAccess) {
/*  60 */     ServerLevel serverLevel = paramWorldGenContext.level();
/*  61 */     WorldGenRegion worldGenRegion = new WorldGenRegion(serverLevel, paramStaticCache2D, paramChunkStep, paramChunkAccess);
/*  62 */     return paramWorldGenContext.generator().createBiomes(serverLevel.getChunkSource().randomState(), Blender.of(worldGenRegion), serverLevel.structureManager().forWorldGenRegion(worldGenRegion), paramChunkAccess);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateNoise(WorldGenContext paramWorldGenContext, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D, ChunkAccess paramChunkAccess) {
/*  66 */     ServerLevel serverLevel = paramWorldGenContext.level();
/*  67 */     WorldGenRegion worldGenRegion = new WorldGenRegion(serverLevel, paramStaticCache2D, paramChunkStep, paramChunkAccess);
/*  68 */     return paramWorldGenContext.generator().fillFromNoise(Blender.of(worldGenRegion), serverLevel.getChunkSource().randomState(), serverLevel.structureManager().forWorldGenRegion(worldGenRegion), paramChunkAccess).thenApply(paramChunkAccess -> {
/*     */           if (paramChunkAccess instanceof ProtoChunk) {
/*     */             ProtoChunk protoChunk = (ProtoChunk)paramChunkAccess;
/*     */             BelowZeroRetrogen belowZeroRetrogen = protoChunk.getBelowZeroRetrogen();
/*     */             if (belowZeroRetrogen != null) {
/*     */               BelowZeroRetrogen.replaceOldBedrock(protoChunk);
/*     */               if (belowZeroRetrogen.hasBedrockHoles()) {
/*     */                 belowZeroRetrogen.applyBedrockMask(protoChunk);
/*     */               }
/*     */             } 
/*     */           } 
/*     */           return paramChunkAccess;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateSurface(WorldGenContext paramWorldGenContext, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D, ChunkAccess paramChunkAccess) {
/*  85 */     ServerLevel serverLevel = paramWorldGenContext.level();
/*  86 */     WorldGenRegion worldGenRegion = new WorldGenRegion(serverLevel, paramStaticCache2D, paramChunkStep, paramChunkAccess);
/*  87 */     paramWorldGenContext.generator().buildSurface(worldGenRegion, serverLevel.structureManager().forWorldGenRegion(worldGenRegion), serverLevel.getChunkSource().randomState(), paramChunkAccess);
/*  88 */     return CompletableFuture.completedFuture(paramChunkAccess);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateCarvers(WorldGenContext paramWorldGenContext, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D, ChunkAccess paramChunkAccess) {
/*  92 */     ServerLevel serverLevel = paramWorldGenContext.level();
/*  93 */     WorldGenRegion worldGenRegion = new WorldGenRegion(serverLevel, paramStaticCache2D, paramChunkStep, paramChunkAccess);
/*  94 */     if (paramChunkAccess instanceof ProtoChunk) { ProtoChunk protoChunk = (ProtoChunk)paramChunkAccess;
/*  95 */       Blender.addAroundOldChunksCarvingMaskFilter((WorldGenLevel)worldGenRegion, protoChunk); }
/*     */     
/*  97 */     paramWorldGenContext.generator().applyCarvers(worldGenRegion, serverLevel.getSeed(), serverLevel.getChunkSource().randomState(), serverLevel.getBiomeManager(), serverLevel.structureManager().forWorldGenRegion(worldGenRegion), paramChunkAccess);
/*  98 */     return CompletableFuture.completedFuture(paramChunkAccess);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateFeatures(WorldGenContext paramWorldGenContext, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D, ChunkAccess paramChunkAccess) {
/* 102 */     ServerLevel serverLevel = paramWorldGenContext.level();
/* 103 */     Heightmap.primeHeightmaps(paramChunkAccess, EnumSet.of(Heightmap.Types.MOTION_BLOCKING, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Heightmap.Types.OCEAN_FLOOR, Heightmap.Types.WORLD_SURFACE));
/* 104 */     WorldGenRegion worldGenRegion = new WorldGenRegion(serverLevel, paramStaticCache2D, paramChunkStep, paramChunkAccess);
/* 105 */     if (!SharedConstants.DEBUG_DISABLE_FEATURES) {
/* 106 */       paramWorldGenContext.generator().applyBiomeDecoration((WorldGenLevel)worldGenRegion, paramChunkAccess, serverLevel.structureManager().forWorldGenRegion(worldGenRegion));
/*     */     }
/*     */     
/* 109 */     Blender.generateBorderTicks(worldGenRegion, paramChunkAccess);
/* 110 */     return CompletableFuture.completedFuture(paramChunkAccess);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> initializeLight(WorldGenContext paramWorldGenContext, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D, ChunkAccess paramChunkAccess) {
/* 114 */     ThreadedLevelLightEngine threadedLevelLightEngine = paramWorldGenContext.lightEngine();
/* 115 */     paramChunkAccess.initializeLightSources();
/* 116 */     ((ProtoChunk)paramChunkAccess).setLightEngine((LevelLightEngine)threadedLevelLightEngine);
/* 117 */     boolean bool = isLighted(paramChunkAccess);
/*     */     
/* 119 */     return threadedLevelLightEngine.initializeLight(paramChunkAccess, bool);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> light(WorldGenContext paramWorldGenContext, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D, ChunkAccess paramChunkAccess) {
/* 123 */     boolean bool = isLighted(paramChunkAccess);
/* 124 */     return paramWorldGenContext.lightEngine().lightChunk(paramChunkAccess, bool);
/*     */   }
/*     */ 
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateSpawn(WorldGenContext paramWorldGenContext, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D, ChunkAccess paramChunkAccess) {
/* 129 */     if (!paramChunkAccess.isUpgrading()) {
/* 130 */       paramWorldGenContext.generator().spawnOriginalMobs(new WorldGenRegion(paramWorldGenContext.level(), paramStaticCache2D, paramChunkStep, paramChunkAccess));
/*     */     }
/* 132 */     return CompletableFuture.completedFuture(paramChunkAccess);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> full(WorldGenContext paramWorldGenContext, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D, ChunkAccess paramChunkAccess) {
/* 136 */     ChunkPos chunkPos = paramChunkAccess.getPos();
/* 137 */     GenerationChunkHolder generationChunkHolder = (GenerationChunkHolder)paramStaticCache2D.get(chunkPos.x, chunkPos.z);
/* 138 */     return CompletableFuture.supplyAsync(() -> { LevelChunk levelChunk; ProtoChunk protoChunk = (ProtoChunk)paramChunkAccess; ServerLevel serverLevel = paramWorldGenContext.level(); if (protoChunk instanceof ImposterProtoChunk) { ImposterProtoChunk imposterProtoChunk = (ImposterProtoChunk)protoChunk; levelChunk = imposterProtoChunk.getWrapped(); } else { levelChunk = new LevelChunk(serverLevel, protoChunk, ()); paramGenerationChunkHolder.replaceProtoChunk(new ImposterProtoChunk(levelChunk, false)); }  Objects.requireNonNull(paramGenerationChunkHolder); levelChunk.setFullStatus(paramGenerationChunkHolder::getFullStatus); levelChunk.runPostLoad(); levelChunk.setLoaded(true); levelChunk.registerAllBlockEntitiesAfterLevelLoad(); levelChunk.registerTickContainerInLevel(serverLevel); levelChunk.setUnsavedListener(paramWorldGenContext.unsavedListener()); return (ChunkAccess)levelChunk; }paramWorldGenContext
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
/* 160 */         .mainThreadExecutor());
/*     */   }
/*     */   
/*     */   private static void postLoadProtoChunk(ServerLevel paramServerLevel, ValueInput.ValueInputList paramValueInputList) {
/* 164 */     if (!paramValueInputList.isEmpty())
/* 165 */       paramServerLevel.addWorldGenChunkEntities(EntityType.loadEntitiesRecursive(paramValueInputList, (Level)paramServerLevel, EntitySpawnReason.LOAD)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\status\ChunkStatusTasks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */