/*    */ package net.minecraft.server.level.progress;
/*    */ 
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements LevelLoadListener
/*    */ {
/*    */   public void start(LevelLoadListener.Stage paramStage, int paramInt) {
/* 12 */     first.start(paramStage, paramInt);
/* 13 */     second.start(paramStage, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(LevelLoadListener.Stage paramStage, int paramInt1, int paramInt2) {
/* 18 */     first.update(paramStage, paramInt1, paramInt2);
/* 19 */     second.update(paramStage, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void finish(LevelLoadListener.Stage paramStage) {
/* 24 */     first.finish(paramStage);
/* 25 */     second.finish(paramStage);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateFocus(ResourceKey<Level> paramResourceKey, ChunkPos paramChunkPos) {
/* 30 */     first.updateFocus(paramResourceKey, paramChunkPos);
/* 31 */     second.updateFocus(paramResourceKey, paramChunkPos);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\progress\LevelLoadListener$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */