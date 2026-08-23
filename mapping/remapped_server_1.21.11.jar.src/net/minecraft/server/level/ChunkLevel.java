/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import net.minecraft.world.level.chunk.status.ChunkPyramid;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStep;
/*    */ import org.jetbrains.annotations.Contract;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChunkLevel
/*    */ {
/*    */   private static final int FULL_CHUNK_LEVEL = 33;
/*    */   private static final int BLOCK_TICKING_LEVEL = 32;
/*    */   private static final int ENTITY_TICKING_LEVEL = 31;
/* 16 */   private static final ChunkStep FULL_CHUNK_STEP = ChunkPyramid.GENERATION_PYRAMID.getStepTo(ChunkStatus.FULL);
/* 17 */   public static final int RADIUS_AROUND_FULL_CHUNK = FULL_CHUNK_STEP.accumulatedDependencies().getRadius();
/* 18 */   public static final int MAX_LEVEL = 33 + RADIUS_AROUND_FULL_CHUNK;
/*    */   
/*    */   public static ChunkStatus generationStatus(int paramInt) {
/* 21 */     return getStatusAroundFullChunk(paramInt - 33, null);
/*    */   }
/*    */   
/*    */   @Contract("_,!null->!null;_,_->_")
/*    */   public static ChunkStatus getStatusAroundFullChunk(int paramInt, ChunkStatus paramChunkStatus) {
/* 26 */     if (paramInt > RADIUS_AROUND_FULL_CHUNK) {
/* 27 */       return paramChunkStatus;
/*    */     }
/* 29 */     if (paramInt <= 0) {
/* 30 */       return ChunkStatus.FULL;
/*    */     }
/* 32 */     return FULL_CHUNK_STEP.accumulatedDependencies().get(paramInt);
/*    */   }
/*    */   
/*    */   public static ChunkStatus getStatusAroundFullChunk(int paramInt) {
/* 36 */     return getStatusAroundFullChunk(paramInt, ChunkStatus.EMPTY);
/*    */   }
/*    */   
/*    */   public static int byStatus(ChunkStatus paramChunkStatus) {
/* 40 */     return 33 + FULL_CHUNK_STEP.getAccumulatedRadiusOf(paramChunkStatus);
/*    */   }
/*    */   
/*    */   public static FullChunkStatus fullStatus(int paramInt) {
/* 44 */     if (paramInt <= 31)
/* 45 */       return FullChunkStatus.ENTITY_TICKING; 
/* 46 */     if (paramInt <= 32)
/* 47 */       return FullChunkStatus.BLOCK_TICKING; 
/* 48 */     if (paramInt <= 33) {
/* 49 */       return FullChunkStatus.FULL;
/*    */     }
/* 51 */     return FullChunkStatus.INACCESSIBLE;
/*    */   }
/*    */   
/*    */   public static int byStatus(FullChunkStatus paramFullChunkStatus) {
/* 55 */     switch (paramFullChunkStatus) { default: throw new MatchException(null, null);case INACCESSIBLE: case FULL: case BLOCK_TICKING: case ENTITY_TICKING: break; }  return 
/*    */ 
/*    */ 
/*    */       
/* 59 */       31;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isEntityTicking(int paramInt) {
/* 64 */     return (paramInt <= 31);
/*    */   }
/*    */   
/*    */   public static boolean isBlockTicking(int paramInt) {
/* 68 */     return (paramInt <= 32);
/*    */   }
/*    */   
/*    */   public static boolean isLoaded(int paramInt) {
/* 72 */     return (paramInt <= MAX_LEVEL);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\ChunkLevel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */