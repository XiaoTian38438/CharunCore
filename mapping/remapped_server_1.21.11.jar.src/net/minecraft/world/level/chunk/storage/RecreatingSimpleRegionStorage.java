/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixer;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import org.apache.commons.io.FileUtils;
/*    */ 
/*    */ public class RecreatingSimpleRegionStorage
/*    */   extends SimpleRegionStorage {
/*    */   private final IOWorker writeWorker;
/*    */   private final Path writeFolder;
/*    */   
/*    */   public RecreatingSimpleRegionStorage(RegionStorageInfo paramRegionStorageInfo1, Path paramPath1, RegionStorageInfo paramRegionStorageInfo2, Path paramPath2, DataFixer paramDataFixer, boolean paramBoolean, DataFixTypes paramDataFixTypes, Supplier<LegacyTagFixer> paramSupplier) {
/* 19 */     super(paramRegionStorageInfo1, paramPath1, paramDataFixer, paramBoolean, paramDataFixTypes, paramSupplier);
/* 20 */     this.writeFolder = paramPath2;
/* 21 */     this.writeWorker = new IOWorker(paramRegionStorageInfo2, paramPath2, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<Void> write(ChunkPos paramChunkPos, Supplier<CompoundTag> paramSupplier) {
/* 26 */     markChunkDone(paramChunkPos);
/* 27 */     return this.writeWorker.store(paramChunkPos, paramSupplier);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 32 */     super.close();
/* 33 */     this.writeWorker.close();
/* 34 */     if (this.writeFolder.toFile().exists())
/* 35 */       FileUtils.deleteDirectory(this.writeFolder.toFile()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\storage\RecreatingSimpleRegionStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */