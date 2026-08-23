/*    */ package net.minecraft.server.level.progress;
/*    */ 
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class LevelLoadProgressTracker
/*    */   implements LevelLoadListener {
/*    */   private static final int PREPARE_SERVER_WEIGHT = 10;
/* 11 */   private static final int EXPECTED_PLAYER_CHUNKS = Mth.square(7);
/*    */   
/*    */   private final boolean includePlayerChunks;
/*    */   
/*    */   private int totalWeight;
/*    */   
/*    */   private int finalizedWeight;
/*    */   
/*    */   private int segmentWeight;
/*    */   
/*    */   private float segmentFraction;
/*    */   private volatile float progress;
/*    */   
/*    */   public LevelLoadProgressTracker(boolean paramBoolean) {
/* 25 */     this.includePlayerChunks = paramBoolean;
/*    */   }
/*    */   
/*    */   public void start(LevelLoadListener.Stage paramStage, int paramInt) {
/*    */     byte b;
/* 30 */     if (!tracksStage(paramStage)) {
/*    */       return;
/*    */     }
/* 33 */     switch (paramStage) {
/*    */       case LOAD_INITIAL_CHUNKS:
/* 35 */         b = this.includePlayerChunks ? EXPECTED_PLAYER_CHUNKS : 0;
/*    */         
/* 37 */         this.totalWeight = 10 + paramInt + b;
/* 38 */         beginSegment(10);
/* 39 */         finishSegment();
/* 40 */         beginSegment(paramInt); break;
/*    */       case LOAD_PLAYER_CHUNKS:
/* 42 */         beginSegment(EXPECTED_PLAYER_CHUNKS);
/*    */         break;
/*    */     } 
/*    */   }
/*    */   private void beginSegment(int paramInt) {
/* 47 */     this.segmentWeight = paramInt;
/* 48 */     this.segmentFraction = 0.0F;
/* 49 */     updateProgress();
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(LevelLoadListener.Stage paramStage, int paramInt1, int paramInt2) {
/* 54 */     if (tracksStage(paramStage)) {
/* 55 */       this.segmentFraction = (paramInt2 == 0) ? 0.0F : (paramInt1 / paramInt2);
/* 56 */       updateProgress();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void finish(LevelLoadListener.Stage paramStage) {
/* 62 */     if (tracksStage(paramStage)) {
/* 63 */       finishSegment();
/*    */     }
/*    */   }
/*    */   
/*    */   private void finishSegment() {
/* 68 */     this.finalizedWeight += this.segmentWeight;
/* 69 */     this.segmentWeight = 0;
/* 70 */     updateProgress();
/*    */   }
/*    */   
/*    */   private boolean tracksStage(LevelLoadListener.Stage paramStage) {
/* 74 */     switch (paramStage) { case LOAD_INITIAL_CHUNKS: case LOAD_PLAYER_CHUNKS:  }  return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void updateProgress() {
/* 82 */     if (this.totalWeight == 0) {
/* 83 */       this.progress = 0.0F;
/*    */     } else {
/* 85 */       float f = this.finalizedWeight + this.segmentFraction * this.segmentWeight;
/* 86 */       this.progress = f / this.totalWeight;
/*    */     } 
/*    */   }
/*    */   
/*    */   public float get() {
/* 91 */     return this.progress;
/*    */   }
/*    */   
/*    */   public void updateFocus(ResourceKey<Level> paramResourceKey, ChunkPos paramChunkPos) {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\progress\LevelLoadProgressTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */