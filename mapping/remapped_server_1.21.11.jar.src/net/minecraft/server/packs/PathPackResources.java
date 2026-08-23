/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.resources.IoSupplier;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PathPackResources
/*     */   extends AbstractPackResources {
/*  32 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  33 */   private static final Joiner PATH_JOINER = Joiner.on("/");
/*     */   
/*     */   private final Path root;
/*     */   
/*     */   public PathPackResources(PackLocationInfo paramPackLocationInfo, Path paramPath) {
/*  38 */     super(paramPackLocationInfo);
/*  39 */     this.root = paramPath;
/*     */   }
/*     */ 
/*     */   
/*     */   public IoSupplier<InputStream> getRootResource(String... paramVarArgs) {
/*  44 */     FileUtil.validatePath(paramVarArgs);
/*     */     
/*  46 */     Path path = FileUtil.resolvePath(this.root, List.of(paramVarArgs));
/*  47 */     if (Files.exists(path, new java.nio.file.LinkOption[0])) {
/*  48 */       return IoSupplier.create(path);
/*     */     }
/*  50 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean validatePath(Path paramPath) {
/*  57 */     if (!SharedConstants.DEBUG_VALIDATE_RESOURCE_PATH_CASE) {
/*  58 */       return true;
/*     */     }
/*  60 */     if (paramPath.getFileSystem() != FileSystems.getDefault()) {
/*  61 */       return true;
/*     */     }
/*     */     
/*     */     try {
/*  65 */       return paramPath.toRealPath(new java.nio.file.LinkOption[0]).endsWith(paramPath);
/*  66 */     } catch (IOException iOException) {
/*  67 */       LOGGER.warn("Failed to resolve real path for {}", paramPath, iOException);
/*  68 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public IoSupplier<InputStream> getResource(PackType paramPackType, Identifier paramIdentifier) {
/*  74 */     Path path = this.root.resolve(paramPackType.getDirectory()).resolve(paramIdentifier.getNamespace());
/*  75 */     return getResource(paramIdentifier, path);
/*     */   }
/*     */   
/*     */   public static IoSupplier<InputStream> getResource(Identifier paramIdentifier, Path paramPath) {
/*  79 */     return (IoSupplier<InputStream>)FileUtil.decomposePath(paramIdentifier.getPath()).mapOrElse(paramList -> {
/*     */           Path path = FileUtil.resolvePath(paramPath, paramList);
/*     */           return returnFileIfExists(path);
/*     */         }paramError -> {
/*     */           LOGGER.error("Invalid path {}: {}", paramIdentifier, paramError.message());
/*     */           return null;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static IoSupplier<InputStream> returnFileIfExists(Path paramPath) {
/*  92 */     if (Files.exists(paramPath, new java.nio.file.LinkOption[0]) && validatePath(paramPath)) {
/*  93 */       return IoSupplier.create(paramPath);
/*     */     }
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void listResources(PackType paramPackType, String paramString1, String paramString2, PackResources.ResourceOutput paramResourceOutput) {
/* 100 */     FileUtil.decomposePath(paramString2)
/* 101 */       .ifSuccess(paramList -> {
/*     */           Path path = this.root.resolve(paramPackType.getDirectory()).resolve(paramString);
/*     */           
/*     */           listPath(paramString, path, paramList, paramResourceOutput);
/* 105 */         }).ifError(paramError -> LOGGER.error("Invalid path {}: {}", paramString, paramError.message()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void listPath(String paramString, Path paramPath, List<String> paramList, PackResources.ResourceOutput paramResourceOutput) {
/* 111 */     Path path = FileUtil.resolvePath(paramPath, paramList); 
/* 112 */     try { Stream<Path> stream = Files.find(path, 2147483647, PathPackResources::isRegularFile, new java.nio.file.FileVisitOption[0]); 
/* 113 */       try { stream.forEach(paramPath2 -> {
/*     */               String str = PATH_JOINER.join(paramPath1.relativize(paramPath2));
/*     */               Identifier identifier = Identifier.tryBuild(paramString, str);
/*     */               if (identifier == null) {
/*     */                 Util.logAndPauseIfInIde(String.format(Locale.ROOT, "Invalid path in pack: %s:%s, ignoring", new Object[] { paramString, str }));
/*     */               } else {
/*     */                 paramResourceOutput.accept(identifier, IoSupplier.create(paramPath2));
/*     */               } 
/*     */             });
/* 122 */         if (stream != null) stream.close();  } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (NoSuchFileException|java.nio.file.NotDirectoryException noSuchFileException)
/*     */     {  }
/* 124 */     catch (IOException iOException)
/* 125 */     { LOGGER.error("Failed to list path {}", path, iOException); }
/*     */   
/*     */   }
/*     */   
/*     */   private static boolean isRegularFile(Path paramPath, BasicFileAttributes paramBasicFileAttributes) {
/* 130 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 131 */       return (paramBasicFileAttributes.isRegularFile() && !StringUtils.equalsIgnoreCase(paramPath.getFileName().toString(), ".ds_store"));
/*     */     }
/* 133 */     return paramBasicFileAttributes.isRegularFile();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getNamespaces(PackType paramPackType) {
/* 138 */     HashSet<String> hashSet = Sets.newHashSet();
/* 139 */     Path path = this.root.resolve(paramPackType.getDirectory());
/*     */     
/* 141 */     try { DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path); 
/* 142 */       try { for (Path path1 : directoryStream) {
/* 143 */           String str = path1.getFileName().toString();
/*     */           
/* 145 */           if (Identifier.isValidNamespace(str)) {
/* 146 */             hashSet.add(str); continue;
/*     */           } 
/* 148 */           LOGGER.warn("Non [a-z0-9_.-] character in namespace {} in pack {}, ignoring", str, this.root);
/*     */         } 
/*     */         
/* 151 */         if (directoryStream != null) directoryStream.close();  } catch (Throwable throwable) { if (directoryStream != null) try { directoryStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (NoSuchFileException|java.nio.file.NotDirectoryException noSuchFileException)
/*     */     {  }
/* 153 */     catch (IOException iOException)
/* 154 */     { LOGGER.error("Failed to list path {}", path, iOException); }
/*     */     
/* 156 */     return hashSet;
/*     */   }
/*     */   
/*     */   public void close() {}
/*     */   
/*     */   public static class PathResourcesSupplier
/*     */     implements Pack.ResourcesSupplier
/*     */   {
/*     */     private final Path content;
/*     */     
/*     */     public PathResourcesSupplier(Path param1Path) {
/* 167 */       this.content = param1Path;
/*     */     }
/*     */ 
/*     */     
/*     */     public PackResources openPrimary(PackLocationInfo param1PackLocationInfo) {
/* 172 */       return new PathPackResources(param1PackLocationInfo, this.content);
/*     */     }
/*     */ 
/*     */     
/*     */     public PackResources openFull(PackLocationInfo param1PackLocationInfo, Pack.Metadata param1Metadata) {
/* 177 */       PackResources packResources = openPrimary(param1PackLocationInfo);
/*     */       
/* 179 */       List list = param1Metadata.overlays();
/* 180 */       if (list.isEmpty()) {
/* 181 */         return packResources;
/*     */       }
/*     */       
/* 184 */       ArrayList<PathPackResources> arrayList = new ArrayList(list.size());
/* 185 */       for (String str : list) {
/* 186 */         Path path = this.content.resolve(str);
/* 187 */         arrayList.add(new PathPackResources(param1PackLocationInfo, path));
/*     */       } 
/*     */       
/* 190 */       return new CompositePackResources(packResources, (List)arrayList);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\PathPackResources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */