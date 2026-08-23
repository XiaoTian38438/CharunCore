/*    */ package net.minecraft.server.level.progress;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class LoggingLevelLoadListener implements LevelLoadListener {
/* 13 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final boolean includePlayerChunks;
/*    */   
/*    */   private final LevelLoadProgressTracker progressTracker;
/*    */   
/*    */   private boolean closed;
/* 20 */   private long startTime = Long.MAX_VALUE;
/* 21 */   private long nextLogTime = Long.MAX_VALUE;
/*    */   
/*    */   public LoggingLevelLoadListener(boolean paramBoolean) {
/* 24 */     this.includePlayerChunks = paramBoolean;
/* 25 */     this.progressTracker = new LevelLoadProgressTracker(paramBoolean);
/*    */   }
/*    */   
/*    */   public static LoggingLevelLoadListener forDedicatedServer() {
/* 29 */     return new LoggingLevelLoadListener(false);
/*    */   }
/*    */   
/*    */   public static LoggingLevelLoadListener forSingleplayer() {
/* 33 */     return new LoggingLevelLoadListener(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void start(LevelLoadListener.Stage paramStage, int paramInt) {
/* 38 */     if (this.closed) {
/*    */       return;
/*    */     }
/* 41 */     if (this.startTime == Long.MAX_VALUE) {
/* 42 */       long l = Util.getMillis();
/* 43 */       this.startTime = l;
/* 44 */       this.nextLogTime = l;
/*    */     } 
/* 46 */     this.progressTracker.start(paramStage, paramInt);
/* 47 */     switch (paramStage) { case PREPARE_GLOBAL_SPAWN:
/* 48 */         LOGGER.info("Selecting global world spawn..."); break;
/* 49 */       case LOAD_INITIAL_CHUNKS: LOGGER.info("Loading {} persistent chunks...", Integer.valueOf(paramInt)); break;
/* 50 */       case LOAD_PLAYER_CHUNKS: LOGGER.info("Loading {} chunks for player spawn...", Integer.valueOf(paramInt));
/*    */         break; }
/*    */   
/*    */   }
/*    */   
/*    */   public void update(LevelLoadListener.Stage paramStage, int paramInt1, int paramInt2) {
/* 56 */     if (this.closed) {
/*    */       return;
/*    */     }
/* 59 */     this.progressTracker.update(paramStage, paramInt1, paramInt2);
/* 60 */     if (Util.getMillis() > this.nextLogTime) {
/* 61 */       this.nextLogTime += 500L;
/* 62 */       int i = Mth.floor(this.progressTracker.get() * 100.0F);
/* 63 */       LOGGER.info(Component.translatable("menu.preparingSpawn", new Object[] { Integer.valueOf(i) }).getString());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void finish(LevelLoadListener.Stage paramStage) {
/* 69 */     if (this.closed) {
/*    */       return;
/*    */     }
/* 72 */     this.progressTracker.finish(paramStage);
/* 73 */     LevelLoadListener.Stage stage = this.includePlayerChunks ? LevelLoadListener.Stage.LOAD_PLAYER_CHUNKS : LevelLoadListener.Stage.LOAD_INITIAL_CHUNKS;
/* 74 */     if (paramStage == stage) {
/* 75 */       LOGGER.info("Time elapsed: {} ms", Long.valueOf(Util.getMillis() - this.startTime));
/* 76 */       this.nextLogTime = Long.MAX_VALUE;
/* 77 */       this.closed = true;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void updateFocus(ResourceKey<Level> paramResourceKey, ChunkPos paramChunkPos) {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\progress\LoggingLevelLoadListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */