/*     */ package net.minecraft.server.packs.resources;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MultiPackResourceManager
/*     */   implements CloseableResourceManager
/*     */ {
/*  21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Map<String, FallbackResourceManager> namespacedManagers;
/*     */   private final List<PackResources> packs;
/*     */   
/*     */   public MultiPackResourceManager(PackType paramPackType, List<PackResources> paramList) {
/*  27 */     this.packs = List.copyOf(paramList);
/*     */     
/*  29 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*     */     
/*  31 */     List list = paramList.stream().flatMap(paramPackResources -> paramPackResources.getNamespaces(paramPackType).stream()).distinct().toList();
/*     */     
/*  33 */     for (PackResources packResources : paramList) {
/*  34 */       ResourceFilterSection resourceFilterSection = getPackFilterSection(packResources);
/*  35 */       Set set = packResources.getNamespaces(paramPackType);
/*     */       
/*  37 */       Predicate<Identifier> predicate = (resourceFilterSection != null) ? (paramIdentifier -> paramResourceFilterSection.isPathFiltered(paramIdentifier.getPath())) : null;
/*     */       
/*  39 */       for (String str : list) {
/*  40 */         boolean bool = set.contains(str);
/*  41 */         boolean bool1 = (resourceFilterSection != null && resourceFilterSection.isNamespaceFiltered(str)) ? true : false;
/*  42 */         if (bool || bool1) {
/*  43 */           FallbackResourceManager fallbackResourceManager = (FallbackResourceManager)hashMap.get(str);
/*  44 */           if (fallbackResourceManager == null) {
/*  45 */             fallbackResourceManager = new FallbackResourceManager(paramPackType, str);
/*  46 */             hashMap.put(str, fallbackResourceManager);
/*     */           } 
/*     */           
/*  49 */           if (bool && bool1) {
/*  50 */             fallbackResourceManager.push(packResources, predicate); continue;
/*  51 */           }  if (bool) {
/*  52 */             fallbackResourceManager.push(packResources); continue;
/*     */           } 
/*  54 */           fallbackResourceManager.pushFilterOnly(packResources.packId(), predicate);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  59 */     this.namespacedManagers = (Map)hashMap;
/*     */   }
/*     */   
/*     */   private ResourceFilterSection getPackFilterSection(PackResources paramPackResources) {
/*     */     try {
/*  64 */       return (ResourceFilterSection)paramPackResources.getMetadataSection(ResourceFilterSection.TYPE);
/*  65 */     } catch (IOException iOException) {
/*  66 */       LOGGER.error("Failed to get filter section from pack {}", paramPackResources.packId());
/*     */       
/*  68 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Set<String> getNamespaces() {
/*  73 */     return this.namespacedManagers.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Resource> getResource(Identifier paramIdentifier) {
/*  78 */     ResourceManager resourceManager = this.namespacedManagers.get(paramIdentifier.getNamespace());
/*     */     
/*  80 */     if (resourceManager != null) {
/*  81 */       return resourceManager.getResource(paramIdentifier);
/*     */     }
/*     */     
/*  84 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Resource> getResourceStack(Identifier paramIdentifier) {
/*  89 */     ResourceManager resourceManager = this.namespacedManagers.get(paramIdentifier.getNamespace());
/*     */     
/*  91 */     if (resourceManager != null) {
/*  92 */       return resourceManager.getResourceStack(paramIdentifier);
/*     */     }
/*  94 */     return List.of();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Identifier, Resource> listResources(String paramString, Predicate<Identifier> paramPredicate) {
/* 100 */     checkTrailingDirectoryPath(paramString);
/*     */     
/* 102 */     TreeMap<Object, Object> treeMap = new TreeMap<>();
/*     */ 
/*     */     
/* 105 */     for (FallbackResourceManager fallbackResourceManager : this.namespacedManagers.values()) {
/* 106 */       treeMap.putAll(fallbackResourceManager.listResources(paramString, paramPredicate));
/*     */     }
/*     */     
/* 109 */     return (Map)treeMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Identifier, List<Resource>> listResourceStacks(String paramString, Predicate<Identifier> paramPredicate) {
/* 114 */     checkTrailingDirectoryPath(paramString);
/*     */     
/* 116 */     TreeMap<Object, Object> treeMap = new TreeMap<>();
/*     */ 
/*     */     
/* 119 */     for (FallbackResourceManager fallbackResourceManager : this.namespacedManagers.values()) {
/* 120 */       treeMap.putAll(fallbackResourceManager.listResourceStacks(paramString, paramPredicate));
/*     */     }
/*     */     
/* 123 */     return (Map)treeMap;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void checkTrailingDirectoryPath(String paramString) {
/* 128 */     if (paramString.endsWith("/")) {
/* 129 */       throw new IllegalArgumentException("Trailing slash in path " + paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<PackResources> listPacks() {
/* 135 */     return this.packs.stream();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 140 */     this.packs.forEach(PackResources::close);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\resources\MultiPackResourceManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */