/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileResourcesSupplier
/*     */   implements Pack.ResourcesSupplier
/*     */ {
/*     */   private final File content;
/*     */   
/*     */   public FileResourcesSupplier(Path paramPath) {
/* 199 */     this(paramPath.toFile());
/*     */   }
/*     */   
/*     */   public FileResourcesSupplier(File paramFile) {
/* 203 */     this.content = paramFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public PackResources openPrimary(PackLocationInfo paramPackLocationInfo) {
/* 208 */     FilePackResources.SharedZipFileAccess sharedZipFileAccess = new FilePackResources.SharedZipFileAccess(this.content);
/* 209 */     return new FilePackResources(paramPackLocationInfo, sharedZipFileAccess, "");
/*     */   }
/*     */ 
/*     */   
/*     */   public PackResources openFull(PackLocationInfo paramPackLocationInfo, Pack.Metadata paramMetadata) {
/* 214 */     FilePackResources.SharedZipFileAccess sharedZipFileAccess = new FilePackResources.SharedZipFileAccess(this.content);
/*     */     
/* 216 */     FilePackResources filePackResources = new FilePackResources(paramPackLocationInfo, sharedZipFileAccess, "");
/* 217 */     List list = paramMetadata.overlays();
/* 218 */     if (list.isEmpty()) {
/* 219 */       return filePackResources;
/*     */     }
/*     */     
/* 222 */     ArrayList<FilePackResources> arrayList = new ArrayList(list.size());
/* 223 */     for (String str : list) {
/* 224 */       arrayList.add(new FilePackResources(paramPackLocationInfo, sharedZipFileAccess, str));
/*     */     }
/*     */     
/* 227 */     return new CompositePackResources(filePackResources, (List)arrayList);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\FilePackResources$FileResourcesSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */