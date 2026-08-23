/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public class SimpleRegionStorage
/*     */   implements AutoCloseable
/*     */ {
/*     */   private final IOWorker worker;
/*     */   private final DataFixer fixerUpper;
/*     */   private final DataFixTypes dataFixType;
/*     */   private final Supplier<LegacyTagFixer> legacyFixer;
/*     */   
/*     */   public SimpleRegionStorage(RegionStorageInfo paramRegionStorageInfo, Path paramPath, DataFixer paramDataFixer, boolean paramBoolean, DataFixTypes paramDataFixTypes) {
/*  31 */     this(paramRegionStorageInfo, paramPath, paramDataFixer, paramBoolean, paramDataFixTypes, LegacyTagFixer.EMPTY);
/*     */   }
/*     */   
/*     */   public SimpleRegionStorage(RegionStorageInfo paramRegionStorageInfo, Path paramPath, DataFixer paramDataFixer, boolean paramBoolean, DataFixTypes paramDataFixTypes, Supplier<LegacyTagFixer> paramSupplier) {
/*  35 */     this.fixerUpper = paramDataFixer;
/*  36 */     this.dataFixType = paramDataFixTypes;
/*  37 */     this.worker = new IOWorker(paramRegionStorageInfo, paramPath, paramBoolean);
/*  38 */     Objects.requireNonNull(paramSupplier); this.legacyFixer = (Supplier<LegacyTagFixer>)Suppliers.memoize(paramSupplier::get);
/*     */   }
/*     */   
/*     */   public boolean isOldChunkAround(ChunkPos paramChunkPos, int paramInt) {
/*  42 */     return this.worker.isOldChunkAround(paramChunkPos, paramInt);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Optional<CompoundTag>> read(ChunkPos paramChunkPos) {
/*  46 */     return this.worker.loadAsync(paramChunkPos);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> write(ChunkPos paramChunkPos, CompoundTag paramCompoundTag) {
/*  50 */     return write(paramChunkPos, () -> paramCompoundTag);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> write(ChunkPos paramChunkPos, Supplier<CompoundTag> paramSupplier) {
/*  54 */     markChunkDone(paramChunkPos);
/*  55 */     return this.worker.store(paramChunkPos, paramSupplier);
/*     */   }
/*     */   
/*     */   public CompoundTag upgradeChunkTag(CompoundTag paramCompoundTag1, int paramInt, CompoundTag paramCompoundTag2) {
/*  59 */     int i = NbtUtils.getDataVersion(paramCompoundTag1, paramInt);
/*  60 */     if (i == SharedConstants.getCurrentVersion().dataVersion().version()) {
/*  61 */       return paramCompoundTag1;
/*     */     }
/*     */     
/*     */     try {
/*  65 */       paramCompoundTag1 = ((LegacyTagFixer)this.legacyFixer.get()).applyFix(paramCompoundTag1);
/*     */ 
/*     */       
/*  68 */       injectDatafixingContext(paramCompoundTag1, paramCompoundTag2);
/*  69 */       paramCompoundTag1 = this.dataFixType.updateToCurrentVersion(this.fixerUpper, paramCompoundTag1, Math.max(((LegacyTagFixer)this.legacyFixer.get()).targetDataVersion(), i));
/*  70 */       removeDatafixingContext(paramCompoundTag1);
/*     */ 
/*     */       
/*  73 */       NbtUtils.addCurrentDataVersion(paramCompoundTag1);
/*     */       
/*  75 */       return paramCompoundTag1;
/*  76 */     } catch (Exception exception) {
/*  77 */       CrashReport crashReport = CrashReport.forThrowable(exception, "Updated chunk");
/*  78 */       CrashReportCategory crashReportCategory = crashReport.addCategory("Updated chunk details");
/*  79 */       crashReportCategory.setDetail("Data version", Integer.valueOf(i));
/*  80 */       throw new ReportedException(crashReport);
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompoundTag upgradeChunkTag(CompoundTag paramCompoundTag, int paramInt) {
/*  85 */     return upgradeChunkTag(paramCompoundTag, paramInt, null);
/*     */   }
/*     */   
/*     */   public Dynamic<Tag> upgradeChunkTag(Dynamic<Tag> paramDynamic, int paramInt) {
/*  89 */     return new Dynamic(paramDynamic.getOps(), upgradeChunkTag((CompoundTag)paramDynamic.getValue(), paramInt, null));
/*     */   }
/*     */   
/*     */   public static void injectDatafixingContext(CompoundTag paramCompoundTag1, CompoundTag paramCompoundTag2) {
/*  93 */     if (paramCompoundTag2 != null) {
/*  94 */       paramCompoundTag1.put("__context", (Tag)paramCompoundTag2);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void removeDatafixingContext(CompoundTag paramCompoundTag) {
/*  99 */     paramCompoundTag.remove("__context");
/*     */   }
/*     */   
/*     */   protected void markChunkDone(ChunkPos paramChunkPos) {
/* 103 */     ((LegacyTagFixer)this.legacyFixer.get()).markChunkDone(paramChunkPos);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> synchronize(boolean paramBoolean) {
/* 107 */     return this.worker.synchronize(paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 112 */     this.worker.close();
/*     */   }
/*     */   
/*     */   public ChunkScanAccess chunkScanner() {
/* 116 */     return this.worker;
/*     */   }
/*     */   
/*     */   public RegionStorageInfo storageInfo() {
/* 120 */     return this.worker.storageInfo();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\storage\SimpleRegionStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */