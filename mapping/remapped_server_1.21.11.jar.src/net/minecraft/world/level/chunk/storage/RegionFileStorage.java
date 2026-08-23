/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtAccounter;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.StreamTagVisitor;
/*     */ import net.minecraft.util.ExceptionCollector;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public final class RegionFileStorage
/*     */   implements AutoCloseable {
/*     */   public static final String ANVIL_EXTENSION = ".mca";
/*     */   private static final int MAX_CACHE_SIZE = 256;
/*  22 */   private final Long2ObjectLinkedOpenHashMap<RegionFile> regionCache = new Long2ObjectLinkedOpenHashMap();
/*     */   private final RegionStorageInfo info;
/*     */   private final Path folder;
/*     */   private final boolean sync;
/*     */   
/*     */   RegionFileStorage(RegionStorageInfo paramRegionStorageInfo, Path paramPath, boolean paramBoolean) {
/*  28 */     this.folder = paramPath;
/*  29 */     this.sync = paramBoolean;
/*  30 */     this.info = paramRegionStorageInfo;
/*     */   }
/*     */   
/*     */   private RegionFile getRegionFile(ChunkPos paramChunkPos) throws IOException {
/*  34 */     long l = ChunkPos.asLong(paramChunkPos.getRegionX(), paramChunkPos.getRegionZ());
/*  35 */     RegionFile regionFile1 = (RegionFile)this.regionCache.getAndMoveToFirst(l);
/*  36 */     if (regionFile1 != null) {
/*  37 */       return regionFile1;
/*     */     }
/*     */     
/*  40 */     if (this.regionCache.size() >= 256) {
/*  41 */       ((RegionFile)this.regionCache.removeLast()).close();
/*     */     }
/*     */     
/*  44 */     FileUtil.createDirectoriesSafe(this.folder);
/*     */     
/*  46 */     Path path = this.folder.resolve("r." + paramChunkPos.getRegionX() + "." + paramChunkPos.getRegionZ() + ".mca");
/*  47 */     RegionFile regionFile2 = new RegionFile(this.info, path, this.folder, this.sync);
/*  48 */     this.regionCache.putAndMoveToFirst(l, regionFile2);
/*  49 */     return regionFile2;
/*     */   }
/*     */   
/*     */   public CompoundTag read(ChunkPos paramChunkPos) throws IOException {
/*  53 */     RegionFile regionFile = getRegionFile(paramChunkPos);
/*  54 */     DataInputStream dataInputStream = regionFile.getChunkDataInputStream(paramChunkPos); 
/*  55 */     try { if (dataInputStream == null)
/*  56 */       { CompoundTag compoundTag1 = null;
/*     */ 
/*     */ 
/*     */         
/*  60 */         if (dataInputStream != null) dataInputStream.close();  return compoundTag1; }  CompoundTag compoundTag = NbtIo.read(dataInputStream); if (dataInputStream != null) dataInputStream.close();  return compoundTag; } catch (Throwable throwable) { if (dataInputStream != null)
/*     */         try { dataInputStream.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  64 */      } public void scanChunk(ChunkPos paramChunkPos, StreamTagVisitor paramStreamTagVisitor) throws IOException { RegionFile regionFile = getRegionFile(paramChunkPos);
/*  65 */     DataInputStream dataInputStream = regionFile.getChunkDataInputStream(paramChunkPos); 
/*  66 */     try { if (dataInputStream != null) {
/*  67 */         NbtIo.parse(dataInputStream, paramStreamTagVisitor, NbtAccounter.unlimitedHeap());
/*     */       }
/*  69 */       if (dataInputStream != null) dataInputStream.close();  } catch (Throwable throwable) { if (dataInputStream != null)
/*     */         try { dataInputStream.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  73 */      } protected void write(ChunkPos paramChunkPos, CompoundTag paramCompoundTag) throws IOException { if (SharedConstants.DEBUG_DONT_SAVE_WORLD) {
/*     */       return;
/*     */     }
/*  76 */     RegionFile regionFile = getRegionFile(paramChunkPos);
/*  77 */     if (paramCompoundTag == null) {
/*  78 */       regionFile.clear(paramChunkPos);
/*     */     } else {
/*  80 */       DataOutputStream dataOutputStream = regionFile.getChunkDataOutputStream(paramChunkPos); 
/*  81 */       try { NbtIo.write(paramCompoundTag, dataOutputStream);
/*  82 */         if (dataOutputStream != null) dataOutputStream.close();  }
/*     */       catch (Throwable throwable) { if (dataOutputStream != null)
/*     */           try { dataOutputStream.close(); }
/*     */           catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */             throw throwable; }
/*     */     
/*  88 */     }  } public void close() throws IOException { ExceptionCollector exceptionCollector = new ExceptionCollector();
/*  89 */     for (ObjectIterator<RegionFile> objectIterator = this.regionCache.values().iterator(); objectIterator.hasNext(); ) { RegionFile regionFile = objectIterator.next();
/*     */       try {
/*  91 */         regionFile.close();
/*  92 */       } catch (IOException iOException) {
/*  93 */         exceptionCollector.add(iOException);
/*     */       }  }
/*     */     
/*  96 */     exceptionCollector.throwIfPresent(); }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 100 */     for (ObjectIterator<RegionFile> objectIterator = this.regionCache.values().iterator(); objectIterator.hasNext(); ) { RegionFile regionFile = objectIterator.next();
/* 101 */       regionFile.flush(); }
/*     */   
/*     */   }
/*     */   
/*     */   public RegionStorageInfo info() {
/* 106 */     return this.info;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\storage\RegionFileStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */