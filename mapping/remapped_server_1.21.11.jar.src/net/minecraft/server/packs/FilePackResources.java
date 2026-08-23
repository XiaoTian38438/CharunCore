/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.resources.IoSupplier;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FilePackResources
/*     */   extends AbstractPackResources {
/*  26 */   static final Logger LOGGER = LogUtils.getLogger();
/*     */   private final SharedZipFileAccess zipFileAccess;
/*     */   private final String prefix;
/*     */   
/*     */   FilePackResources(PackLocationInfo paramPackLocationInfo, SharedZipFileAccess paramSharedZipFileAccess, String paramString) {
/*  31 */     super(paramPackLocationInfo);
/*  32 */     this.zipFileAccess = paramSharedZipFileAccess;
/*  33 */     this.prefix = paramString;
/*     */   }
/*     */   
/*     */   private static String getPathFromLocation(PackType paramPackType, Identifier paramIdentifier) {
/*  37 */     return String.format(Locale.ROOT, "%s/%s/%s", new Object[] { paramPackType.getDirectory(), paramIdentifier.getNamespace(), paramIdentifier.getPath() });
/*     */   }
/*     */ 
/*     */   
/*     */   public IoSupplier<InputStream> getRootResource(String... paramVarArgs) {
/*  42 */     return getResource(String.join("/", (CharSequence[])paramVarArgs));
/*     */   }
/*     */ 
/*     */   
/*     */   public IoSupplier<InputStream> getResource(PackType paramPackType, Identifier paramIdentifier) {
/*  47 */     return getResource(getPathFromLocation(paramPackType, paramIdentifier));
/*     */   }
/*     */   
/*     */   private String addPrefix(String paramString) {
/*  51 */     if (this.prefix.isEmpty()) {
/*  52 */       return paramString;
/*     */     }
/*     */     
/*  55 */     return this.prefix + "/" + this.prefix;
/*     */   }
/*     */   
/*     */   private IoSupplier<InputStream> getResource(String paramString) {
/*  59 */     ZipFile zipFile = this.zipFileAccess.getOrCreateZipFile();
/*  60 */     if (zipFile == null) {
/*  61 */       return null;
/*     */     }
/*     */     
/*  64 */     ZipEntry zipEntry = zipFile.getEntry(addPrefix(paramString));
/*  65 */     if (zipEntry == null) {
/*  66 */       return null;
/*     */     }
/*     */     
/*  69 */     return IoSupplier.create(zipFile, zipEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getNamespaces(PackType paramPackType) {
/*  74 */     ZipFile zipFile = this.zipFileAccess.getOrCreateZipFile();
/*  75 */     if (zipFile == null) {
/*  76 */       return Set.of();
/*     */     }
/*     */     
/*  79 */     Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
/*     */     
/*  81 */     HashSet<String> hashSet = Sets.newHashSet();
/*     */     
/*  83 */     String str = addPrefix(paramPackType.getDirectory() + "/");
/*     */     
/*  85 */     while (enumeration.hasMoreElements()) {
/*  86 */       ZipEntry zipEntry = enumeration.nextElement();
/*     */       
/*  88 */       String str1 = zipEntry.getName();
/*  89 */       String str2 = extractNamespace(str, str1);
/*  90 */       if (!str2.isEmpty()) {
/*  91 */         if (Identifier.isValidNamespace(str2)) {
/*  92 */           hashSet.add(str2); continue;
/*     */         } 
/*  94 */         LOGGER.warn("Non [a-z0-9_.-] character in namespace {} in pack {}, ignoring", str2, this.zipFileAccess.file);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  99 */     return hashSet;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static String extractNamespace(String paramString1, String paramString2) {
/* 104 */     if (!paramString2.startsWith(paramString1)) {
/* 105 */       return "";
/*     */     }
/*     */     
/* 108 */     int i = paramString1.length();
/* 109 */     int j = paramString2.indexOf('/', i);
/* 110 */     if (j == -1) {
/* 111 */       return paramString2.substring(i);
/*     */     }
/* 113 */     return paramString2.substring(i, j);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 118 */     this.zipFileAccess.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void listResources(PackType paramPackType, String paramString1, String paramString2, PackResources.ResourceOutput paramResourceOutput) {
/* 123 */     ZipFile zipFile = this.zipFileAccess.getOrCreateZipFile();
/* 124 */     if (zipFile == null) {
/*     */       return;
/*     */     }
/* 127 */     Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
/* 128 */     String str1 = addPrefix(paramPackType.getDirectory() + "/" + paramPackType.getDirectory() + "/");
/* 129 */     String str2 = str1 + str1 + "/";
/*     */     
/* 131 */     while (enumeration.hasMoreElements()) {
/* 132 */       ZipEntry zipEntry = enumeration.nextElement();
/* 133 */       if (zipEntry.isDirectory()) {
/*     */         continue;
/*     */       }
/*     */       
/* 137 */       String str3 = zipEntry.getName();
/* 138 */       if (!str3.startsWith(str2)) {
/*     */         continue;
/*     */       }
/*     */       
/* 142 */       String str4 = str3.substring(str1.length());
/* 143 */       Identifier identifier = Identifier.tryBuild(paramString1, str4);
/* 144 */       if (identifier != null) {
/* 145 */         paramResourceOutput.accept(identifier, IoSupplier.create(zipFile, zipEntry)); continue;
/*     */       } 
/* 147 */       LOGGER.warn("Invalid path in datapack: {}:{}, ignoring", paramString1, str4);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class SharedZipFileAccess
/*     */     implements AutoCloseable {
/*     */     final File file;
/*     */     private ZipFile zipFile;
/*     */     private boolean failedToLoad;
/*     */     
/*     */     SharedZipFileAccess(File param1File) {
/* 158 */       this.file = param1File;
/*     */     }
/*     */     
/*     */     ZipFile getOrCreateZipFile() {
/* 162 */       if (this.failedToLoad) {
/* 163 */         return null;
/*     */       }
/*     */       
/* 166 */       if (this.zipFile == null) {
/*     */         try {
/* 168 */           this.zipFile = new ZipFile(this.file);
/* 169 */         } catch (IOException iOException) {
/* 170 */           FilePackResources.LOGGER.error("Failed to open pack {}", this.file, iOException);
/* 171 */           this.failedToLoad = true;
/* 172 */           return null;
/*     */         } 
/*     */       }
/*     */       
/* 176 */       return this.zipFile;
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() {
/* 181 */       if (this.zipFile != null) {
/* 182 */         IOUtils.closeQuietly(this.zipFile);
/* 183 */         this.zipFile = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void finalize() throws Throwable {
/* 190 */       close();
/* 191 */       super.finalize();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FileResourcesSupplier implements Pack.ResourcesSupplier {
/*     */     private final File content;
/*     */     
/*     */     public FileResourcesSupplier(Path param1Path) {
/* 199 */       this(param1Path.toFile());
/*     */     }
/*     */     
/*     */     public FileResourcesSupplier(File param1File) {
/* 203 */       this.content = param1File;
/*     */     }
/*     */ 
/*     */     
/*     */     public PackResources openPrimary(PackLocationInfo param1PackLocationInfo) {
/* 208 */       FilePackResources.SharedZipFileAccess sharedZipFileAccess = new FilePackResources.SharedZipFileAccess(this.content);
/* 209 */       return new FilePackResources(param1PackLocationInfo, sharedZipFileAccess, "");
/*     */     }
/*     */ 
/*     */     
/*     */     public PackResources openFull(PackLocationInfo param1PackLocationInfo, Pack.Metadata param1Metadata) {
/* 214 */       FilePackResources.SharedZipFileAccess sharedZipFileAccess = new FilePackResources.SharedZipFileAccess(this.content);
/*     */       
/* 216 */       FilePackResources filePackResources = new FilePackResources(param1PackLocationInfo, sharedZipFileAccess, "");
/* 217 */       List list = param1Metadata.overlays();
/* 218 */       if (list.isEmpty()) {
/* 219 */         return filePackResources;
/*     */       }
/*     */       
/* 222 */       ArrayList<FilePackResources> arrayList = new ArrayList(list.size());
/* 223 */       for (String str : list) {
/* 224 */         arrayList.add(new FilePackResources(param1PackLocationInfo, sharedZipFileAccess, str));
/*     */       }
/*     */       
/* 227 */       return new CompositePackResources(filePackResources, (List)arrayList);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\FilePackResources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */