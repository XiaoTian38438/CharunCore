/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.FileSystemUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class VanillaPackResourcesBuilder
/*     */ {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger(); public static Consumer<VanillaPackResourcesBuilder> developmentConfig = paramVanillaPackResourcesBuilder -> {
/*     */     
/*     */     }; private static final Map<PackType, Path> ROOT_DIR_BY_TYPE;
/*     */   static {
/*  33 */     ROOT_DIR_BY_TYPE = (Map<PackType, Path>)Util.make(() -> {
/*     */           synchronized (VanillaPackResources.class) {
/*     */             ImmutableMap.Builder builder = ImmutableMap.builder();
/*     */             
/*     */             for (PackType packType : PackType.values()) {
/*     */               String str = "/" + packType.getDirectory() + "/.mcassetsroot";
/*     */               
/*     */               URL uRL = VanillaPackResources.class.getResource(str);
/*     */               if (uRL == null) {
/*     */                 LOGGER.error("File {} does not exist in classpath", str);
/*     */               } else {
/*     */                 try {
/*     */                   URI uRI = uRL.toURI();
/*     */                   String str1 = uRI.getScheme();
/*     */                   if (!"jar".equals(str1) && !"file".equals(str1)) {
/*     */                     LOGGER.warn("Assets URL '{}' uses unexpected schema", uRI);
/*     */                   }
/*     */                   Path path = FileSystemUtil.safeGetPath(uRI);
/*     */                   builder.put(packType, path.getParent());
/*  52 */                 } catch (Exception exception) {
/*     */                   LOGGER.error("Couldn't resolve path to vanilla assets", exception);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */             return builder.build();
/*     */           } 
/*     */         });
/*  60 */   } private final Set<Path> rootPaths = new LinkedHashSet<>();
/*  61 */   private final Map<PackType, Set<Path>> pathsForType = new EnumMap<>(PackType.class);
/*     */   
/*  63 */   private BuiltInMetadata metadata = BuiltInMetadata.of();
/*  64 */   private final Set<String> namespaces = new HashSet<>();
/*     */   
/*     */   private boolean validateDirPath(Path paramPath) {
/*  67 */     if (!Files.exists(paramPath, new java.nio.file.LinkOption[0])) {
/*  68 */       return false;
/*     */     }
/*  70 */     if (!Files.isDirectory(paramPath, new java.nio.file.LinkOption[0])) {
/*  71 */       throw new IllegalArgumentException("Path " + String.valueOf(paramPath.toAbsolutePath()) + " is not directory");
/*     */     }
/*  73 */     return true;
/*     */   }
/*     */   
/*     */   private void pushRootPath(Path paramPath) {
/*  77 */     if (validateDirPath(paramPath)) {
/*  78 */       this.rootPaths.add(paramPath);
/*     */     }
/*     */   }
/*     */   
/*     */   private void pushPathForType(PackType paramPackType, Path paramPath) {
/*  83 */     if (validateDirPath(paramPath)) {
/*  84 */       ((Set<Path>)this.pathsForType.computeIfAbsent(paramPackType, paramPackType -> new LinkedHashSet())).add(paramPath);
/*     */     }
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder pushJarResources() {
/*  89 */     ROOT_DIR_BY_TYPE.forEach((paramPackType, paramPath) -> {
/*     */           pushRootPath(paramPath.getParent());
/*     */           pushPathForType(paramPackType, paramPath);
/*     */         });
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VanillaPackResourcesBuilder pushClasspathResources(PackType paramPackType, Class<?> paramClass) {
/*  99 */     Enumeration<URL> enumeration = null;
/*     */     try {
/* 101 */       enumeration = paramClass.getClassLoader().getResources(paramPackType.getDirectory() + "/");
/* 102 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/* 105 */     while (enumeration != null && enumeration.hasMoreElements()) {
/* 106 */       URL uRL = enumeration.nextElement();
/*     */       try {
/* 108 */         URI uRI = uRL.toURI();
/* 109 */         if ("file".equals(uRI.getScheme())) {
/* 110 */           Path path = Paths.get(uRI);
/* 111 */           pushRootPath(path.getParent());
/* 112 */           pushPathForType(paramPackType, path);
/*     */         } 
/* 114 */       } catch (Exception exception) {
/* 115 */         LOGGER.error("Failed to extract path from {}", uRL, exception);
/*     */       } 
/*     */     } 
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder applyDevelopmentConfig() {
/* 122 */     developmentConfig.accept(this);
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VanillaPackResourcesBuilder pushUniversalPath(Path paramPath) {
/* 130 */     pushRootPath(paramPath);
/* 131 */     for (PackType packType : PackType.values()) {
/* 132 */       pushPathForType(packType, paramPath.resolve(packType.getDirectory()));
/*     */     }
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VanillaPackResourcesBuilder pushAssetPath(PackType paramPackType, Path paramPath) {
/* 141 */     pushRootPath(paramPath);
/* 142 */     pushPathForType(paramPackType, paramPath);
/* 143 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder setMetadata(BuiltInMetadata paramBuiltInMetadata) {
/* 147 */     this.metadata = paramBuiltInMetadata;
/* 148 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder exposeNamespace(String... paramVarArgs) {
/* 152 */     this.namespaces.addAll(Arrays.asList(paramVarArgs));
/* 153 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResources build(PackLocationInfo paramPackLocationInfo) {
/* 157 */     return new VanillaPackResources(paramPackLocationInfo, this.metadata, 
/*     */ 
/*     */         
/* 160 */         Set.copyOf(this.namespaces), 
/* 161 */         copyAndReverse(this.rootPaths), 
/* 162 */         Util.makeEnumMap(PackType.class, paramPackType -> copyAndReverse(this.pathsForType.getOrDefault(paramPackType, Set.of()))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Path> copyAndReverse(Collection<Path> paramCollection) {
/* 167 */     ArrayList<Path> arrayList = new ArrayList<>(paramCollection);
/* 168 */     Collections.reverse(arrayList);
/* 169 */     return List.copyOf(arrayList);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\VanillaPackResourcesBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */