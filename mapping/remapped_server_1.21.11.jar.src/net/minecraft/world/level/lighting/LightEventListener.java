/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ public interface LightEventListener {
/*    */   void checkBlock(BlockPos paramBlockPos);
/*    */   
/*    */   boolean hasLightWork();
/*    */   
/*    */   int runLightUpdates();
/*    */   
/*    */   default void updateSectionStatus(BlockPos paramBlockPos, boolean paramBoolean) {
/* 15 */     updateSectionStatus(SectionPos.of(paramBlockPos), paramBoolean);
/*    */   }
/*    */   
/*    */   void updateSectionStatus(SectionPos paramSectionPos, boolean paramBoolean);
/*    */   
/*    */   void setLightEnabled(ChunkPos paramChunkPos, boolean paramBoolean);
/*    */   
/*    */   void propagateLightSources(ChunkPos paramChunkPos);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\lighting\LightEventListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */