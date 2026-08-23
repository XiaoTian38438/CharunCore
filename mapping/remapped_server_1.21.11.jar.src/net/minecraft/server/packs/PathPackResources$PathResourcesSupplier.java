/*     */ package net.minecraft.server.packs;
/*     */ 
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
/*     */ public class PathResourcesSupplier
/*     */   implements Pack.ResourcesSupplier
/*     */ {
/*     */   private final Path content;
/*     */   
/*     */   public PathResourcesSupplier(Path paramPath) {
/* 167 */     this.content = paramPath;
/*     */   }
/*     */ 
/*     */   
/*     */   public PackResources openPrimary(PackLocationInfo paramPackLocationInfo) {
/* 172 */     return new PathPackResources(paramPackLocationInfo, this.content);
/*     */   }
/*     */ 
/*     */   
/*     */   public PackResources openFull(PackLocationInfo paramPackLocationInfo, Pack.Metadata paramMetadata) {
/* 177 */     PackResources packResources = openPrimary(paramPackLocationInfo);
/*     */     
/* 179 */     List list = paramMetadata.overlays();
/* 180 */     if (list.isEmpty()) {
/* 181 */       return packResources;
/*     */     }
/*     */     
/* 184 */     ArrayList<PathPackResources> arrayList = new ArrayList(list.size());
/* 185 */     for (String str : list) {
/* 186 */       Path path = this.content.resolve(str);
/* 187 */       arrayList.add(new PathPackResources(paramPackLocationInfo, path));
/*     */     } 
/*     */     
/* 190 */     return new CompositePackResources(packResources, (List)arrayList);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\PathPackResources$PathResourcesSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */