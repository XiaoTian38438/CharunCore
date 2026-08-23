/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtAccounter;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.util.FastBufferedInputStream;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DimensionDataStorage implements AutoCloseable {
/*  41 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  43 */   private final Map<SavedDataType<?>, Optional<SavedData>> cache = new HashMap<>();
/*     */   
/*     */   private final DataFixer fixerUpper;
/*     */   private final HolderLookup.Provider registries;
/*     */   private final Path dataFolder;
/*  48 */   private CompletableFuture<?> pendingWriteFuture = CompletableFuture.completedFuture(null);
/*     */   
/*     */   public DimensionDataStorage(Path paramPath, DataFixer paramDataFixer, HolderLookup.Provider paramProvider) {
/*  51 */     this.fixerUpper = paramDataFixer;
/*  52 */     this.dataFolder = paramPath;
/*  53 */     this.registries = paramProvider;
/*     */   }
/*     */   
/*     */   private Path getDataFile(String paramString) {
/*  57 */     return this.dataFolder.resolve(paramString + ".dat");
/*     */   }
/*     */   
/*     */   public <T extends SavedData> T computeIfAbsent(SavedDataType<T> paramSavedDataType) {
/*  61 */     T t = (T)get((SavedDataType)paramSavedDataType);
/*  62 */     if (t != null) {
/*  63 */       return t;
/*     */     }
/*  65 */     SavedData savedData = paramSavedDataType.constructor().get();
/*  66 */     set(paramSavedDataType, (T)savedData);
/*  67 */     return (T)savedData;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends SavedData> T get(SavedDataType<T> paramSavedDataType) {
/*  72 */     Optional<?> optional = this.cache.get(paramSavedDataType);
/*  73 */     if (optional == null) {
/*  74 */       optional = Optional.ofNullable(readSavedData(paramSavedDataType));
/*  75 */       this.cache.put(paramSavedDataType, optional);
/*     */     } 
/*  77 */     return (T)optional.orElse(null);
/*     */   }
/*     */   
/*     */   private <T extends SavedData> T readSavedData(SavedDataType<T> paramSavedDataType) {
/*     */     try {
/*  82 */       Path path = getDataFile(paramSavedDataType.id());
/*  83 */       if (Files.exists(path, new java.nio.file.LinkOption[0])) {
/*  84 */         CompoundTag compoundTag = readTagFromDisk(paramSavedDataType.id(), paramSavedDataType.dataFixType(), SharedConstants.getCurrentVersion().dataVersion().version());
/*  85 */         RegistryOps registryOps = this.registries.createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/*  86 */         return (T)paramSavedDataType.codec().parse((DynamicOps)registryOps, compoundTag.get("data"))
/*  87 */           .resultOrPartial(paramString -> LOGGER.error("Failed to parse saved data for '{}': {}", paramSavedDataType, paramString))
/*  88 */           .orElse(null);
/*     */       } 
/*  90 */     } catch (Exception exception) {
/*  91 */       LOGGER.error("Error loading saved data: {}", paramSavedDataType, exception);
/*     */     } 
/*  93 */     return null;
/*     */   }
/*     */   
/*     */   public <T extends SavedData> void set(SavedDataType<T> paramSavedDataType, T paramT) {
/*  97 */     this.cache.put(paramSavedDataType, Optional.of((SavedData)paramT));
/*  98 */     paramT.setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag readTagFromDisk(String paramString, DataFixTypes paramDataFixTypes, int paramInt) throws IOException {
/* 103 */     InputStream inputStream = Files.newInputStream(getDataFile(paramString), new java.nio.file.OpenOption[0]); 
/* 104 */     try { PushbackInputStream pushbackInputStream = new PushbackInputStream((InputStream)new FastBufferedInputStream(inputStream), 2);
/*     */       
/*     */       try { CompoundTag compoundTag1;
/* 107 */         if (isGzip(pushbackInputStream)) {
/* 108 */           compoundTag1 = NbtIo.readCompressed(pushbackInputStream, NbtAccounter.unlimitedHeap());
/*     */         } else {
/* 110 */           DataInputStream dataInputStream = new DataInputStream(pushbackInputStream); 
/* 111 */           try { compoundTag1 = NbtIo.read(dataInputStream);
/* 112 */             dataInputStream.close(); } catch (Throwable throwable) { try { dataInputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */              throw throwable; }
/*     */         
/* 115 */         }  int i = NbtUtils.getDataVersion(compoundTag1, 1343);
/* 116 */         CompoundTag compoundTag2 = paramDataFixTypes.update(this.fixerUpper, compoundTag1, i, paramInt);
/* 117 */         pushbackInputStream.close(); if (inputStream != null) inputStream.close();  return compoundTag2; } catch (Throwable throwable) { try { pushbackInputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable throwable) { if (inputStream != null)
/*     */         try { inputStream.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 121 */      } private boolean isGzip(PushbackInputStream paramPushbackInputStream) throws IOException { byte[] arrayOfByte = new byte[2];
/* 122 */     boolean bool = false;
/* 123 */     int i = paramPushbackInputStream.read(arrayOfByte, 0, 2);
/* 124 */     if (i == 2) {
/* 125 */       int j = (arrayOfByte[1] & 0xFF) << 8 | arrayOfByte[0] & 0xFF;
/* 126 */       if (j == 35615) {
/* 127 */         bool = true;
/*     */       }
/*     */     } 
/* 130 */     if (i != 0) {
/* 131 */       paramPushbackInputStream.unread(arrayOfByte, 0, i);
/*     */     }
/* 133 */     return bool; }
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<?> scheduleSave() {
/* 138 */     Map<SavedDataType<?>, CompoundTag> map = collectDirtyTagsToSave();
/* 139 */     if (map.isEmpty()) {
/* 140 */       return CompletableFuture.completedFuture(null);
/*     */     }
/*     */     
/* 143 */     int i = Util.maxAllowedExecutorThreads();
/* 144 */     int j = map.size();
/*     */ 
/*     */     
/* 147 */     if (j > i) {
/*     */       
/* 149 */       this.pendingWriteFuture = this.pendingWriteFuture.thenCompose(paramObject -> {
/*     */             ArrayList<CompletableFuture<Void>> arrayList = new ArrayList(paramInt1);
/*     */ 
/*     */             
/*     */             int i = Mth.positiveCeilDiv(paramInt2, paramInt1);
/*     */             
/*     */             for (List list : Iterables.partition(paramMap.entrySet(), i)) {
/*     */               arrayList.add(CompletableFuture.runAsync((), (Executor)Util.ioPool()));
/*     */             }
/*     */             
/*     */             return CompletableFuture.allOf((CompletableFuture<?>[])arrayList.toArray(()));
/*     */           });
/*     */     } else {
/* 162 */       this.pendingWriteFuture = this.pendingWriteFuture.thenCompose(paramObject -> CompletableFuture.allOf((CompletableFuture<?>[])paramMap.entrySet().stream().map(()).toArray(())));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     return this.pendingWriteFuture;
/*     */   }
/*     */   
/*     */   private Map<SavedDataType<?>, CompoundTag> collectDirtyTagsToSave() {
/* 173 */     Object2ObjectArrayMap object2ObjectArrayMap = new Object2ObjectArrayMap();
/* 174 */     RegistryOps registryOps = this.registries.createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/* 175 */     this.cache.forEach((paramSavedDataType, paramOptional) -> paramOptional.filter(SavedData::isDirty).ifPresent(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     return (Map<SavedDataType<?>, CompoundTag>)object2ObjectArrayMap;
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends SavedData> CompoundTag encodeUnchecked(SavedDataType<T> paramSavedDataType, SavedData paramSavedData, RegistryOps<Tag> paramRegistryOps) {
/* 186 */     Codec codec = paramSavedDataType.codec();
/* 187 */     CompoundTag compoundTag = new CompoundTag();
/* 188 */     compoundTag.put("data", (Tag)codec.encodeStart((DynamicOps)paramRegistryOps, paramSavedData).getOrThrow());
/* 189 */     NbtUtils.addCurrentDataVersion(compoundTag);
/* 190 */     return compoundTag;
/*     */   }
/*     */   
/*     */   private void tryWrite(SavedDataType<?> paramSavedDataType, CompoundTag paramCompoundTag) {
/* 194 */     Path path = getDataFile(paramSavedDataType.id());
/*     */     try {
/* 196 */       NbtIo.writeCompressed(paramCompoundTag, path);
/* 197 */     } catch (IOException iOException) {
/* 198 */       LOGGER.error("Could not save data to {}", path.getFileName(), iOException);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveAndJoin() {
/* 203 */     scheduleSave().join();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 209 */     saveAndJoin();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\DimensionDataStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */