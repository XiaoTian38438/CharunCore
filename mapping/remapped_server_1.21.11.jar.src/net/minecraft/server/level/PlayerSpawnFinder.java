/*     */ package net.minecraft.server.level;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class PlayerSpawnFinder {
/*  30 */   private static final EntityDimensions PLAYER_DIMENSIONS = EntityType.PLAYER.getDimensions();
/*     */   
/*     */   private static final int ABSOLUTE_MAX_ATTEMPTS = 1024;
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*     */   private final BlockPos spawnSuggestion;
/*     */   
/*     */   private final int radius;
/*     */   private final int candidateCount;
/*     */   private final int coprime;
/*     */   private final int offset;
/*     */   private int nextCandidateIndex;
/*  43 */   private final CompletableFuture<Vec3> finishedFuture = new CompletableFuture<>();
/*     */   
/*     */   private PlayerSpawnFinder(ServerLevel paramServerLevel, BlockPos paramBlockPos, int paramInt) {
/*  46 */     this.level = paramServerLevel;
/*  47 */     this.spawnSuggestion = paramBlockPos;
/*  48 */     this.radius = paramInt;
/*  49 */     long l = paramInt * 2L + 1L;
/*  50 */     this.candidateCount = (int)Math.min(1024L, l * l);
/*  51 */     this.coprime = getCoprime(this.candidateCount);
/*  52 */     this.offset = RandomSource.create().nextInt(this.candidateCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public static CompletableFuture<Vec3> findSpawn(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/*  57 */     if (!paramServerLevel.dimensionType().hasSkyLight() || paramServerLevel.getServer().getWorldData().getGameType() == GameType.ADVENTURE) {
/*  58 */       return CompletableFuture.completedFuture(fixupSpawnHeight((CollisionGetter)paramServerLevel, paramBlockPos));
/*     */     }
/*     */     
/*  61 */     int i = Math.max(0, ((Integer)paramServerLevel.getGameRules().get(GameRules.RESPAWN_RADIUS)).intValue());
/*  62 */     int j = Mth.floor(paramServerLevel.getWorldBorder().getDistanceToBorder(paramBlockPos.getX(), paramBlockPos.getZ()));
/*  63 */     if (j < i) {
/*  64 */       i = j;
/*     */     }
/*  66 */     if (j <= 1) {
/*  67 */       i = 1;
/*     */     }
/*     */     
/*  70 */     PlayerSpawnFinder playerSpawnFinder = new PlayerSpawnFinder(paramServerLevel, paramBlockPos, i);
/*  71 */     playerSpawnFinder.scheduleNext();
/*     */     
/*  73 */     return playerSpawnFinder.finishedFuture;
/*     */   }
/*     */   
/*     */   private void scheduleNext() {
/*  77 */     int i = this.nextCandidateIndex++;
/*  78 */     if (i < this.candidateCount) {
/*  79 */       int j = (this.offset + this.coprime * i) % this.candidateCount;
/*  80 */       int k = j % (this.radius * 2 + 1);
/*  81 */       int m = j / (this.radius * 2 + 1);
/*  82 */       int n = this.spawnSuggestion.getX() + k - this.radius;
/*  83 */       int i1 = this.spawnSuggestion.getZ() + m - this.radius;
/*  84 */       scheduleCandidate(n, i1, i, () -> {
/*     */             BlockPos blockPos = getOverworldRespawnPos(this.level, paramInt1, paramInt2);
/*  86 */             return (blockPos != null && noCollisionNoLiquid((CollisionGetter)this.level, blockPos)) ? Optional.of(Vec3.atBottomCenterOf((Vec3i)blockPos)) : Optional.empty();
/*     */ 
/*     */           
/*     */           });
/*     */     } else {
/*     */       
/*  92 */       scheduleCandidate(this.spawnSuggestion.getX(), this.spawnSuggestion.getZ(), i, () -> Optional.of(fixupSpawnHeight((CollisionGetter)this.level, this.spawnSuggestion)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Vec3 fixupSpawnHeight(CollisionGetter paramCollisionGetter, BlockPos paramBlockPos) {
/*  99 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/*     */     
/* 101 */     while (!noCollisionNoLiquid(paramCollisionGetter, (BlockPos)mutableBlockPos) && mutableBlockPos.getY() < paramCollisionGetter.getMaxY()) {
/* 102 */       mutableBlockPos.move(Direction.UP);
/*     */     }
/*     */     
/* 105 */     mutableBlockPos.move(Direction.DOWN);
/* 106 */     while (noCollisionNoLiquid(paramCollisionGetter, (BlockPos)mutableBlockPos) && mutableBlockPos.getY() > paramCollisionGetter.getMinY()) {
/* 107 */       mutableBlockPos.move(Direction.DOWN);
/*     */     }
/* 109 */     mutableBlockPos.move(Direction.UP);
/*     */     
/* 111 */     return Vec3.atBottomCenterOf((Vec3i)mutableBlockPos);
/*     */   }
/*     */   
/*     */   private static boolean noCollisionNoLiquid(CollisionGetter paramCollisionGetter, BlockPos paramBlockPos) {
/* 115 */     return paramCollisionGetter.noCollision(null, PLAYER_DIMENSIONS.makeBoundingBox(paramBlockPos.getBottomCenter()), true);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getCoprime(int paramInt) {
/* 120 */     return (paramInt <= 16) ? (paramInt - 1) : 17;
/*     */   }
/*     */   
/*     */   private void scheduleCandidate(int paramInt1, int paramInt2, int paramInt3, Supplier<Optional<Vec3>> paramSupplier) {
/* 124 */     if (this.finishedFuture.isDone()) {
/*     */       return;
/*     */     }
/*     */     
/* 128 */     int i = SectionPos.blockToSectionCoord(paramInt1);
/* 129 */     int j = SectionPos.blockToSectionCoord(paramInt2);
/* 130 */     this.level.getChunkSource().addTicketAndLoadWithRadius(TicketType.SPAWN_SEARCH, new ChunkPos(i, j), 0).whenCompleteAsync((paramObject, paramThrowable) -> {
/*     */           if (paramThrowable == null)
/*     */             try {
/*     */               Optional<Vec3> optional = paramSupplier.get();
/*     */               if (optional.isPresent()) {
/*     */                 this.finishedFuture.complete(optional.get());
/*     */               } else {
/*     */                 scheduleNext();
/*     */               } 
/* 139 */             } catch (Throwable throwable) {
/*     */               paramThrowable = throwable;
/*     */             }  
/*     */           if (paramThrowable != null) {
/*     */             CrashReport crashReport = CrashReport.forThrowable(paramThrowable, "Searching for spawn");
/*     */             CrashReportCategory crashReportCategory = crashReport.addCategory("Spawn Lookup");
/*     */             Objects.requireNonNull(this.spawnSuggestion);
/*     */             crashReportCategory.setDetail("Origin", this.spawnSuggestion::toString);
/*     */             crashReportCategory.setDetail("Radius", ());
/*     */             crashReportCategory.setDetail("Candidate", ());
/*     */             crashReportCategory.setDetail("Progress", ());
/*     */             this.finishedFuture.completeExceptionally((Throwable)new ReportedException(crashReport));
/*     */           } 
/* 152 */         }(Executor)this.level.getServer());
/*     */   }
/*     */   
/*     */   protected static BlockPos getOverworldRespawnPos(ServerLevel paramServerLevel, int paramInt1, int paramInt2) {
/* 156 */     boolean bool = paramServerLevel.dimensionType().hasCeiling();
/*     */ 
/*     */ 
/*     */     
/* 160 */     LevelChunk levelChunk = paramServerLevel.getChunk(SectionPos.blockToSectionCoord(paramInt1), SectionPos.blockToSectionCoord(paramInt2));
/* 161 */     int i = bool ? paramServerLevel.getChunkSource().getGenerator().getSpawnHeight((LevelHeightAccessor)paramServerLevel) : levelChunk.getHeight(Heightmap.Types.MOTION_BLOCKING, paramInt1 & 0xF, paramInt2 & 0xF);
/*     */ 
/*     */     
/* 164 */     if (i < paramServerLevel.getMinY()) {
/* 165 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 169 */     int j = levelChunk.getHeight(Heightmap.Types.WORLD_SURFACE, paramInt1 & 0xF, paramInt2 & 0xF);
/* 170 */     if (j <= i && j > levelChunk.getHeight(Heightmap.Types.OCEAN_FLOOR, paramInt1 & 0xF, paramInt2 & 0xF)) {
/* 171 */       return null;
/*     */     }
/*     */     
/* 174 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*     */     
/* 176 */     for (int k = i + 1; k >= paramServerLevel.getMinY(); k--) {
/* 177 */       mutableBlockPos.set(paramInt1, k, paramInt2);
/* 178 */       BlockState blockState = paramServerLevel.getBlockState((BlockPos)mutableBlockPos);
/*     */ 
/*     */       
/* 181 */       if (!blockState.getFluidState().isEmpty()) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 186 */       if (Block.isFaceFull(blockState.getCollisionShape((BlockGetter)paramServerLevel, (BlockPos)mutableBlockPos), Direction.UP)) {
/* 187 */         return mutableBlockPos.above().immutable();
/*     */       }
/*     */     } 
/* 190 */     return null;
/*     */   }
/*     */   
/*     */   public static BlockPos getSpawnPosInChunk(ServerLevel paramServerLevel, ChunkPos paramChunkPos) {
/* 194 */     if (SharedConstants.debugVoidTerrain(paramChunkPos)) {
/* 195 */       return null;
/*     */     }
/*     */     
/* 198 */     for (int i = paramChunkPos.getMinBlockX(); i <= paramChunkPos.getMaxBlockX(); i++) {
/* 199 */       for (int j = paramChunkPos.getMinBlockZ(); j <= paramChunkPos.getMaxBlockZ(); j++) {
/* 200 */         BlockPos blockPos = getOverworldRespawnPos(paramServerLevel, i, j);
/* 201 */         if (blockPos != null) {
/* 202 */           return blockPos;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 207 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\PlayerSpawnFinder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */