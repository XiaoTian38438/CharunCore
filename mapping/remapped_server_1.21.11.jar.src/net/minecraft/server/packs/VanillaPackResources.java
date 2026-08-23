/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*     */ import net.minecraft.server.packs.resources.IoSupplier;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class VanillaPackResources implements PackResources {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final PackLocationInfo location;
/*     */   
/*     */   private final BuiltInMetadata metadata;
/*     */   
/*     */   private final Set<String> namespaces;
/*     */   private final List<Path> rootPaths;
/*     */   private final Map<PackType, List<Path>> pathsForType;
/*     */   
/*     */   VanillaPackResources(PackLocationInfo paramPackLocationInfo, BuiltInMetadata paramBuiltInMetadata, Set<String> paramSet, List<Path> paramList, Map<PackType, List<Path>> paramMap) {
/*  36 */     this.location = paramPackLocationInfo;
/*  37 */     this.metadata = paramBuiltInMetadata;
/*  38 */     this.namespaces = paramSet;
/*  39 */     this.rootPaths = paramList;
/*  40 */     this.pathsForType = paramMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public IoSupplier<InputStream> getRootResource(String... paramVarArgs) {
/*  45 */     FileUtil.validatePath(paramVarArgs);
/*     */     
/*  47 */     List<String> list = List.of(paramVarArgs);
/*  48 */     for (Path path1 : this.rootPaths) {
/*  49 */       Path path2 = FileUtil.resolvePath(path1, list);
/*  50 */       if (Files.exists(path2, new java.nio.file.LinkOption[0]) && PathPackResources.validatePath(path2)) {
/*  51 */         return IoSupplier.create(path2);
/*     */       }
/*     */     } 
/*  54 */     return null;
/*     */   }
/*     */   
/*     */   public void listRawPaths(PackType paramPackType, Identifier paramIdentifier, Consumer<Path> paramConsumer) {
/*  58 */     FileUtil.decomposePath(paramIdentifier.getPath())
/*  59 */       .ifSuccess(paramList -> {
/*     */           String str = paramIdentifier.getNamespace();
/*     */ 
/*     */           
/*     */           for (Path path1 : this.pathsForType.get(paramPackType)) {
/*     */             Path path2 = path1.resolve(str);
/*     */             
/*     */             paramConsumer.accept(FileUtil.resolvePath(path2, paramList));
/*     */           } 
/*  68 */         }).ifError(paramError -> LOGGER.error("Invalid path {}: {}", paramIdentifier, paramError.message()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void listResources(PackType paramPackType, String paramString1, String paramString2, PackResources.ResourceOutput paramResourceOutput) {
/*  75 */     FileUtil.decomposePath(paramString2)
/*  76 */       .ifSuccess(paramList -> {
/*     */           List<Path> list = this.pathsForType.get(paramPackType);
/*     */           
/*     */           int i = list.size();
/*     */           
/*     */           if (i == 1) {
/*     */             getResources(paramResourceOutput, paramString, list.get(0), paramList);
/*     */           } else if (i > 1) {
/*     */             HashMap<Object, Object> hashMap = new HashMap<>();
/*     */             
/*     */             for (byte b = 0; b < i - 1; b++) {
/*     */               Objects.requireNonNull(hashMap);
/*     */               getResources(hashMap::putIfAbsent, paramString, list.get(b), paramList);
/*     */             } 
/*     */             Path path = list.get(i - 1);
/*     */             if (hashMap.isEmpty()) {
/*     */               getResources(paramResourceOutput, paramString, path, paramList);
/*     */             } else {
/*     */               Objects.requireNonNull(hashMap);
/*     */               getResources(hashMap::putIfAbsent, paramString, path, paramList);
/*     */               hashMap.forEach(paramResourceOutput);
/*     */             } 
/*     */           } 
/*  99 */         }).ifError(paramError -> LOGGER.error("Invalid path {}: {}", paramString, paramError.message()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void getResources(PackResources.ResourceOutput paramResourceOutput, String paramString, Path paramPath, List<String> paramList) {
/* 105 */     Path path = paramPath.resolve(paramString);
/* 106 */     PathPackResources.listPath(paramString, path, paramList, paramResourceOutput);
/*     */   }
/*     */ 
/*     */   
/*     */   public IoSupplier<InputStream> getResource(PackType paramPackType, Identifier paramIdentifier) {
/* 111 */     return (IoSupplier<InputStream>)FileUtil.decomposePath(paramIdentifier.getPath()).mapOrElse(paramList -> {
/*     */           String str = paramIdentifier.getNamespace();
/*     */           for (Path path1 : this.pathsForType.get(paramPackType)) {
/*     */             Path path2 = FileUtil.resolvePath(path1.resolve(str), paramList);
/*     */             if (Files.exists(path2, new java.nio.file.LinkOption[0]) && PathPackResources.validatePath(path2)) {
/*     */               return IoSupplier.create(path2);
/*     */             }
/*     */           } 
/*     */           return null;
/*     */         }paramError -> {
/*     */           LOGGER.error("Invalid path {}: {}", paramIdentifier, paramError.message());
/*     */           return null;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getNamespaces(PackType paramPackType) {
/* 131 */     return this.namespaces;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getMetadataSection(MetadataSectionType<T> paramMetadataSectionType) {
/* 136 */     IoSupplier<InputStream> ioSupplier = getRootResource(new String[] { "pack.mcmeta" });
/* 137 */     if (ioSupplier != null) {
/* 138 */       try { InputStream inputStream = (InputStream)ioSupplier.get(); 
/* 139 */         try { T t = (T)AbstractPackResources.getMetadataFromStream((MetadataSectionType)paramMetadataSectionType, inputStream, this.location);
/* 140 */           if (t != null)
/* 141 */           { T t1 = t;
/*     */             
/* 143 */             if (inputStream != null) inputStream.close();  return t1; }  if (inputStream != null) inputStream.close();  } catch (Throwable throwable) { if (inputStream != null) try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 148 */     return this.metadata.get(paramMetadataSectionType);
/*     */   }
/*     */ 
/*     */   
/*     */   public PackLocationInfo location() {
/* 153 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceProvider asProvider() {
/* 165 */     return paramIdentifier -> Optional.<IoSupplier<InputStream>>ofNullable(getResource(PackType.CLIENT_RESOURCES, paramIdentifier)).map(());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\VanillaPackResources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */