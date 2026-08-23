/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ 
/*    */ public class WorldGenerationContext {
/*    */   private final int minY;
/*    */   private final int height;
/*    */   
/*    */   public WorldGenerationContext(ChunkGenerator paramChunkGenerator, LevelHeightAccessor paramLevelHeightAccessor) {
/* 11 */     this.minY = Math.max(paramLevelHeightAccessor.getMinY(), paramChunkGenerator.getMinY());
/* 12 */     this.height = Math.min(paramLevelHeightAccessor.getHeight(), paramChunkGenerator.getGenDepth());
/*    */   }
/*    */   
/*    */   public int getMinGenY() {
/* 16 */     return this.minY;
/*    */   }
/*    */   
/*    */   public int getGenDepth() {
/* 20 */     return this.height;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\WorldGenerationContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */