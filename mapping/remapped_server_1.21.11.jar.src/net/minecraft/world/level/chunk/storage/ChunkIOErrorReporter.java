/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ChunkIOErrorReporter
/*    */ {
/*    */   void reportChunkLoadFailure(Throwable paramThrowable, RegionStorageInfo paramRegionStorageInfo, ChunkPos paramChunkPos);
/*    */   
/*    */   void reportChunkSaveFailure(Throwable paramThrowable, RegionStorageInfo paramRegionStorageInfo, ChunkPos paramChunkPos);
/*    */   
/*    */   static ReportedException createMisplacedChunkReport(ChunkPos paramChunkPos1, ChunkPos paramChunkPos2) {
/* 20 */     CrashReport crashReport = CrashReport.forThrowable(new IllegalStateException("Retrieved chunk position " + String.valueOf(paramChunkPos1) + " does not match requested " + String.valueOf(paramChunkPos2)), "Chunk found in invalid location");
/* 21 */     CrashReportCategory crashReportCategory = crashReport.addCategory("Misplaced Chunk");
/* 22 */     Objects.requireNonNull(paramChunkPos1); crashReportCategory.setDetail("Stored Position", paramChunkPos1::toString);
/* 23 */     return new ReportedException(crashReport);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default void reportMisplacedChunk(ChunkPos paramChunkPos1, ChunkPos paramChunkPos2, RegionStorageInfo paramRegionStorageInfo) {
/* 30 */     reportChunkLoadFailure((Throwable)createMisplacedChunkReport(paramChunkPos1, paramChunkPos2), paramRegionStorageInfo, paramChunkPos2);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\storage\ChunkIOErrorReporter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */