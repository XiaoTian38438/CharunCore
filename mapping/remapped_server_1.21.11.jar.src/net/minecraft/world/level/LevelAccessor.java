/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkSource;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.redstone.NeighborUpdater;
/*    */ import net.minecraft.world.level.storage.LevelData;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.ticks.ScheduledTick;
/*    */ import net.minecraft.world.ticks.TickPriority;
/*    */ 
/*    */ public interface LevelAccessor extends CommonLevelAccessor, LevelReader, ScheduledTickAccess {
/*    */   long nextSubTickCount();
/*    */   
/*    */   default <T> ScheduledTick<T> createTick(BlockPos paramBlockPos, T paramT, int paramInt, TickPriority paramTickPriority) {
/* 31 */     return new ScheduledTick(paramT, paramBlockPos, getGameTime() + paramInt, paramTickPriority, nextSubTickCount());
/*    */   }
/*    */ 
/*    */   
/*    */   default <T> ScheduledTick<T> createTick(BlockPos paramBlockPos, T paramT, int paramInt) {
/* 36 */     return new ScheduledTick(paramT, paramBlockPos, getGameTime() + paramInt, nextSubTickCount());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   LevelData getLevelData();
/*    */ 
/*    */   
/*    */   default long getGameTime() {
/* 45 */     return getLevelData().getGameTime();
/*    */   }
/*    */   
/*    */   MinecraftServer getServer();
/*    */   
/*    */   default Difficulty getDifficulty() {
/* 51 */     return getLevelData().getDifficulty();
/*    */   }
/*    */ 
/*    */   
/*    */   ChunkSource getChunkSource();
/*    */   
/*    */   default boolean hasChunk(int paramInt1, int paramInt2) {
/* 58 */     return getChunkSource().hasChunk(paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   
/*    */   RandomSource getRandom();
/*    */   
/*    */   default void updateNeighborsAt(BlockPos paramBlockPos, Block paramBlock) {}
/*    */   
/*    */   default void neighborShapeChanged(Direction paramDirection, BlockPos paramBlockPos1, BlockPos paramBlockPos2, BlockState paramBlockState, @UpdateFlags int paramInt1, int paramInt2) {
/* 67 */     NeighborUpdater.executeShapeUpdate(this, paramDirection, paramBlockPos1, paramBlockPos2, paramBlockState, paramInt1, paramInt2 - 1);
/*    */   }
/*    */   
/*    */   default void playSound(Entity paramEntity, BlockPos paramBlockPos, SoundEvent paramSoundEvent, SoundSource paramSoundSource) {
/* 71 */     playSound(paramEntity, paramBlockPos, paramSoundEvent, paramSoundSource, 1.0F, 1.0F);
/*    */   }
/*    */   
/*    */   void playSound(Entity paramEntity, BlockPos paramBlockPos, SoundEvent paramSoundEvent, SoundSource paramSoundSource, float paramFloat1, float paramFloat2);
/*    */   
/*    */   void addParticle(ParticleOptions paramParticleOptions, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6);
/*    */   
/*    */   void levelEvent(Entity paramEntity, int paramInt1, BlockPos paramBlockPos, int paramInt2);
/*    */   
/*    */   default void levelEvent(int paramInt1, BlockPos paramBlockPos, int paramInt2) {
/* 81 */     levelEvent(null, paramInt1, paramBlockPos, paramInt2);
/*    */   }
/*    */   
/*    */   void gameEvent(Holder<GameEvent> paramHolder, Vec3 paramVec3, GameEvent.Context paramContext);
/*    */   
/*    */   default void gameEvent(Entity paramEntity, Holder<GameEvent> paramHolder, Vec3 paramVec3) {
/* 87 */     gameEvent(paramHolder, paramVec3, new GameEvent.Context(paramEntity, null));
/*    */   }
/*    */   
/*    */   default void gameEvent(Entity paramEntity, Holder<GameEvent> paramHolder, BlockPos paramBlockPos) {
/* 91 */     gameEvent(paramHolder, paramBlockPos, new GameEvent.Context(paramEntity, null));
/*    */   }
/*    */   
/*    */   default void gameEvent(Holder<GameEvent> paramHolder, BlockPos paramBlockPos, GameEvent.Context paramContext) {
/* 95 */     gameEvent(paramHolder, Vec3.atCenterOf((Vec3i)paramBlockPos), paramContext);
/*    */   }
/*    */   
/*    */   default void gameEvent(ResourceKey<GameEvent> paramResourceKey, BlockPos paramBlockPos, GameEvent.Context paramContext) {
/* 99 */     gameEvent((Holder<GameEvent>)registryAccess().lookupOrThrow(Registries.GAME_EVENT).getOrThrow(paramResourceKey), paramBlockPos, paramContext);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\LevelAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */