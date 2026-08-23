/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BulkSectionAccess implements AutoCloseable {
/*    */   private final LevelAccessor level;
/* 14 */   private final Long2ObjectMap<LevelChunkSection> acquiredSections = (Long2ObjectMap<LevelChunkSection>)new Long2ObjectOpenHashMap();
/*    */   private LevelChunkSection lastSection;
/*    */   private long lastSectionKey;
/*    */   
/*    */   public BulkSectionAccess(LevelAccessor paramLevelAccessor) {
/* 19 */     this.level = paramLevelAccessor;
/*    */   }
/*    */   
/*    */   public LevelChunkSection getSection(BlockPos paramBlockPos) {
/* 23 */     int i = this.level.getSectionIndex(paramBlockPos.getY());
/* 24 */     if (i < 0 || i >= this.level.getSectionsCount()) {
/* 25 */       return null;
/*    */     }
/* 27 */     long l = SectionPos.asLong(paramBlockPos);
/* 28 */     if (this.lastSection == null || this.lastSectionKey != l) {
/* 29 */       this.lastSection = (LevelChunkSection)this.acquiredSections.computeIfAbsent(l, paramLong -> {
/*    */             ChunkAccess chunkAccess = this.level.getChunk(SectionPos.blockToSectionCoord(paramBlockPos.getX()), SectionPos.blockToSectionCoord(paramBlockPos.getZ()));
/*    */             LevelChunkSection levelChunkSection = chunkAccess.getSection(paramInt);
/*    */             levelChunkSection.acquire();
/*    */             return levelChunkSection;
/*    */           });
/* 35 */       this.lastSectionKey = l;
/*    */     } 
/* 37 */     return this.lastSection;
/*    */   }
/*    */   
/*    */   public BlockState getBlockState(BlockPos paramBlockPos) {
/* 41 */     LevelChunkSection levelChunkSection = getSection(paramBlockPos);
/*    */     
/* 43 */     if (levelChunkSection == null) {
/* 44 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 46 */     int i = SectionPos.sectionRelative(paramBlockPos.getX());
/* 47 */     int j = SectionPos.sectionRelative(paramBlockPos.getY());
/* 48 */     int k = SectionPos.sectionRelative(paramBlockPos.getZ());
/* 49 */     return levelChunkSection.getBlockState(i, j, k);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 54 */     for (ObjectIterator<LevelChunkSection> objectIterator = this.acquiredSections.values().iterator(); objectIterator.hasNext(); ) { LevelChunkSection levelChunkSection = objectIterator.next();
/* 55 */       levelChunkSection.release(); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\BulkSectionAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */