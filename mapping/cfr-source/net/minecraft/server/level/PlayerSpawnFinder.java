/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.server.level;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class PlayerSpawnFinder {
    private static final EntityDimensions PLAYER_DIMENSIONS = EntityType.PLAYER.getDimensions();
    private static final int ABSOLUTE_MAX_ATTEMPTS = 1024;
    private final ServerLevel level;
    private final BlockPos spawnSuggestion;
    private final int radius;
    private final int candidateCount;
    private final int coprime;
    private final int offset;
    private int nextCandidateIndex;
    private final CompletableFuture<Vec3> finishedFuture = new CompletableFuture();

    private PlayerSpawnFinder(ServerLevel serverLevel, BlockPos blockPos, int n) {
        this.level = serverLevel;
        this.spawnSuggestion = blockPos;
        this.radius = n;
        long l = (long)n * 2L + 1L;
        this.candidateCount = (int)Math.min(1024L, l * l);
        this.coprime = PlayerSpawnFinder.getCoprime(this.candidateCount);
        this.offset = RandomSource.create().nextInt(this.candidateCount);
    }

    public static CompletableFuture<Vec3> findSpawn(ServerLevel serverLevel, BlockPos blockPos) {
        if (!serverLevel.dimensionType().hasSkyLight() || serverLevel.getServer().getWorldData().getGameType() == GameType.ADVENTURE) {
            return CompletableFuture.completedFuture(PlayerSpawnFinder.fixupSpawnHeight(serverLevel, blockPos));
        }
        int n = Math.max(0, serverLevel.getGameRules().get(GameRules.RESPAWN_RADIUS));
        int n2 = Mth.floor(serverLevel.getWorldBorder().getDistanceToBorder(blockPos.getX(), blockPos.getZ()));
        if (n2 < n) {
            n = n2;
        }
        if (n2 <= 1) {
            n = 1;
        }
        PlayerSpawnFinder playerSpawnFinder = new PlayerSpawnFinder(serverLevel, blockPos, n);
        playerSpawnFinder.scheduleNext();
        return playerSpawnFinder.finishedFuture;
    }

    private void scheduleNext() {
        int n;
        if ((n = this.nextCandidateIndex++) < this.candidateCount) {
            int n2 = (this.offset + this.coprime * n) % this.candidateCount;
            int n3 = n2 % (this.radius * 2 + 1);
            int n4 = n2 / (this.radius * 2 + 1);
            int n5 = this.spawnSuggestion.getX() + n3 - this.radius;
            int n6 = this.spawnSuggestion.getZ() + n4 - this.radius;
            this.scheduleCandidate(n5, n6, n, () -> {
                BlockPos blockPos = PlayerSpawnFinder.getOverworldRespawnPos(this.level, n5, n6);
                if (blockPos != null && PlayerSpawnFinder.noCollisionNoLiquid(this.level, blockPos)) {
                    return Optional.of(Vec3.atBottomCenterOf(blockPos));
                }
                return Optional.empty();
            });
        } else {
            this.scheduleCandidate(this.spawnSuggestion.getX(), this.spawnSuggestion.getZ(), n, () -> Optional.of(PlayerSpawnFinder.fixupSpawnHeight(this.level, this.spawnSuggestion)));
        }
    }

    private static Vec3 fixupSpawnHeight(CollisionGetter collisionGetter, BlockPos blockPos) {
        BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
        while (!PlayerSpawnFinder.noCollisionNoLiquid(collisionGetter, mutableBlockPos) && mutableBlockPos.getY() < collisionGetter.getMaxY()) {
            mutableBlockPos.move(Direction.UP);
        }
        mutableBlockPos.move(Direction.DOWN);
        while (PlayerSpawnFinder.noCollisionNoLiquid(collisionGetter, mutableBlockPos) && mutableBlockPos.getY() > collisionGetter.getMinY()) {
            mutableBlockPos.move(Direction.DOWN);
        }
        mutableBlockPos.move(Direction.UP);
        return Vec3.atBottomCenterOf(mutableBlockPos);
    }

    private static boolean noCollisionNoLiquid(CollisionGetter collisionGetter, BlockPos blockPos) {
        return collisionGetter.noCollision(null, PLAYER_DIMENSIONS.makeBoundingBox(blockPos.getBottomCenter()), true);
    }

    private static int getCoprime(int n) {
        return n <= 16 ? n - 1 : 17;
    }

    private void scheduleCandidate(int n, int n2, int n3, Supplier<Optional<Vec3>> supplier) {
        if (this.finishedFuture.isDone()) {
            return;
        }
        int n4 = SectionPos.blockToSectionCoord(n);
        int n5 = SectionPos.blockToSectionCoord(n2);
        this.level.getChunkSource().addTicketAndLoadWithRadius(TicketType.SPAWN_SEARCH, new ChunkPos(n4, n5), 0).whenCompleteAsync((object, throwable) -> {
            Object object2;
            if (throwable == null) {
                try {
                    object2 = (Optional)supplier.get();
                    if (((Optional)object2).isPresent()) {
                        this.finishedFuture.complete((Vec3)((Optional)object2).get());
                    } else {
                        this.scheduleNext();
                    }
                }
                catch (Throwable throwable2) {
                    throwable = throwable2;
                }
            }
            if (throwable != null) {
                object2 = CrashReport.forThrowable(throwable, "Searching for spawn");
                CrashReportCategory crashReportCategory = ((CrashReport)object2).addCategory("Spawn Lookup");
                crashReportCategory.setDetail("Origin", this.spawnSuggestion::toString);
                crashReportCategory.setDetail("Radius", () -> Integer.toString(this.radius));
                crashReportCategory.setDetail("Candidate", () -> "[" + n + "," + n2 + "]");
                crashReportCategory.setDetail("Progress", () -> n3 + " out of " + this.candidateCount);
                this.finishedFuture.completeExceptionally(new ReportedException((CrashReport)object2));
            }
        }, (Executor)this.level.getServer());
    }

    protected static @Nullable BlockPos getOverworldRespawnPos(ServerLevel serverLevel, int n, int n2) {
        int n3;
        boolean bl = serverLevel.dimensionType().hasCeiling();
        LevelChunk levelChunk = serverLevel.getChunk(SectionPos.blockToSectionCoord(n), SectionPos.blockToSectionCoord(n2));
        int n4 = n3 = bl ? serverLevel.getChunkSource().getGenerator().getSpawnHeight(serverLevel) : levelChunk.getHeight(Heightmap.Types.MOTION_BLOCKING, n & 0xF, n2 & 0xF);
        if (n3 < serverLevel.getMinY()) {
            return null;
        }
        int n5 = levelChunk.getHeight(Heightmap.Types.WORLD_SURFACE, n & 0xF, n2 & 0xF);
        if (n5 <= n3 && n5 > levelChunk.getHeight(Heightmap.Types.OCEAN_FLOOR, n & 0xF, n2 & 0xF)) {
            return null;
        }
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int i = n3 + 1; i >= serverLevel.getMinY(); --i) {
            mutableBlockPos.set(n, i, n2);
            BlockState blockState = serverLevel.getBlockState(mutableBlockPos);
            if (!blockState.getFluidState().isEmpty()) break;
            if (!Block.isFaceFull(blockState.getCollisionShape(serverLevel, mutableBlockPos), Direction.UP)) continue;
            return ((BlockPos)mutableBlockPos.above()).immutable();
        }
        return null;
    }

    public static @Nullable BlockPos getSpawnPosInChunk(ServerLevel serverLevel, ChunkPos chunkPos) {
        if (SharedConstants.debugVoidTerrain(chunkPos)) {
            return null;
        }
        for (int i = chunkPos.getMinBlockX(); i <= chunkPos.getMaxBlockX(); ++i) {
            for (int j = chunkPos.getMinBlockZ(); j <= chunkPos.getMaxBlockZ(); ++j) {
                BlockPos blockPos = PlayerSpawnFinder.getOverworldRespawnPos(serverLevel, i, j);
                if (blockPos == null) continue;
                return blockPos;
            }
        }
        return null;
    }
}

