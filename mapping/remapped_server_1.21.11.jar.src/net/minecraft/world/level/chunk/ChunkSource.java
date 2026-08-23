/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import java.io.IOException;
/*    */ import java.util.function.BooleanSupplier;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ 
/*    */ public abstract class ChunkSource
/*    */   implements LightChunkGetter, AutoCloseable
/*    */ {
/*    */   public LevelChunk getChunk(int paramInt1, int paramInt2, boolean paramBoolean) {
/* 14 */     return (LevelChunk)getChunk(paramInt1, paramInt2, ChunkStatus.FULL, paramBoolean);
/*    */   }
/*    */   
/*    */   public LevelChunk getChunkNow(int paramInt1, int paramInt2) {
/* 18 */     return getChunk(paramInt1, paramInt2, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public LightChunk getChunkForLighting(int paramInt1, int paramInt2) {
/* 23 */     return getChunk(paramInt1, paramInt2, ChunkStatus.EMPTY, false);
/*    */   }
/*    */   
/*    */   public boolean hasChunk(int paramInt1, int paramInt2) {
/* 27 */     return (getChunk(paramInt1, paramInt2, ChunkStatus.FULL, false) != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract ChunkAccess getChunk(int paramInt1, int paramInt2, ChunkStatus paramChunkStatus, boolean paramBoolean);
/*    */ 
/*    */   
/*    */   public abstract void tick(BooleanSupplier paramBooleanSupplier, boolean paramBoolean);
/*    */ 
/*    */   
/*    */   public void onSectionEmptinessChanged(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {}
/*    */ 
/*    */   
/*    */   public abstract String gatherStats();
/*    */ 
/*    */   
/*    */   public abstract int getLoadedChunksCount();
/*    */ 
/*    */   
/*    */   public void close() throws IOException {}
/*    */ 
/*    */   
/*    */   public abstract LevelLightEngine getLightEngine();
/*    */   
/*    */   public void setSpawnSettings(boolean paramBoolean) {}
/*    */   
/*    */   public boolean updateChunkForced(ChunkPos paramChunkPos, boolean paramBoolean) {
/* 54 */     return false;
/*    */   }
/*    */   
/*    */   public LongSet getForceLoadedChunks() {
/* 58 */     return LongSet.of();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\ChunkSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */