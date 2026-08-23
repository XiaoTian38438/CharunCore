/*     */ package net.minecraft.server.packs.repository;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashMap;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.PackLocationInfo;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.server.packs.VanillaPackResources;
/*     */ import net.minecraft.world.level.validation.DirectoryValidator;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class BuiltInPackSource
/*     */   implements RepositorySource
/*     */ {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final String VANILLA_ID = "vanilla";
/*     */   
/*     */   public static final String TESTS_ID = "tests";
/*  30 */   public static final KnownPack CORE_PACK_INFO = KnownPack.vanilla("core");
/*     */   
/*     */   private final PackType packType;
/*     */   private final VanillaPackResources vanillaPack;
/*     */   private final Identifier packDir;
/*     */   private final DirectoryValidator validator;
/*     */   
/*     */   public BuiltInPackSource(PackType paramPackType, VanillaPackResources paramVanillaPackResources, Identifier paramIdentifier, DirectoryValidator paramDirectoryValidator) {
/*  38 */     this.packType = paramPackType;
/*  39 */     this.vanillaPack = paramVanillaPackResources;
/*  40 */     this.packDir = paramIdentifier;
/*  41 */     this.validator = paramDirectoryValidator;
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadPacks(Consumer<Pack> paramConsumer) {
/*  46 */     Pack pack = createVanillaPack((PackResources)this.vanillaPack);
/*  47 */     if (pack != null) {
/*  48 */       paramConsumer.accept(pack);
/*     */     }
/*  50 */     listBundledPacks(paramConsumer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VanillaPackResources getVanillaPack() {
/*  58 */     return this.vanillaPack;
/*     */   }
/*     */   
/*     */   private void listBundledPacks(Consumer<Pack> paramConsumer) {
/*  62 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*     */     
/*  64 */     Objects.requireNonNull(hashMap); populatePackList(hashMap::put);
/*     */     
/*  66 */     hashMap.forEach((paramString, paramFunction) -> {
/*     */           Pack pack = paramFunction.apply(paramString);
/*     */           if (pack != null) {
/*     */             paramConsumer.accept(pack);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   protected void populatePackList(BiConsumer<String, Function<String, Pack>> paramBiConsumer) {
/*  75 */     this.vanillaPack.listRawPaths(this.packType, this.packDir, paramPath -> discoverPacksInPath(paramPath, paramBiConsumer));
/*     */   }
/*     */   
/*     */   protected void discoverPacksInPath(Path paramPath, BiConsumer<String, Function<String, Pack>> paramBiConsumer) {
/*  79 */     if (paramPath != null && Files.isDirectory(paramPath, new java.nio.file.LinkOption[0])) {
/*     */       try {
/*  81 */         FolderRepositorySource.discoverPacks(paramPath, this.validator, (paramPath, paramResourcesSupplier) -> paramBiConsumer.accept(pathToId(paramPath), ()));
/*     */       
/*     */       }
/*  84 */       catch (IOException iOException) {
/*  85 */         LOGGER.warn("Failed to discover packs in {}", paramPath, iOException);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static String pathToId(Path paramPath) {
/*  91 */     return StringUtils.removeEnd(paramPath.getFileName().toString(), ".zip");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Pack.ResourcesSupplier fixedResources(final PackResources instance) {
/*  97 */     return new Pack.ResourcesSupplier()
/*     */       {
/*     */         public PackResources openPrimary(PackLocationInfo param1PackLocationInfo) {
/* 100 */           return instance;
/*     */         }
/*     */ 
/*     */         
/*     */         public PackResources openFull(PackLocationInfo param1PackLocationInfo, Pack.Metadata param1Metadata) {
/* 105 */           return instance;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   protected abstract Pack createVanillaPack(PackResources paramPackResources);
/*     */   
/*     */   protected abstract Component getPackTitle(String paramString);
/*     */   
/*     */   protected abstract Pack createBuiltinPack(String paramString, Pack.ResourcesSupplier paramResourcesSupplier, Component paramComponent);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\repository\BuiltInPackSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */