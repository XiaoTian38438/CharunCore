/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.util.Unit;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class ReloadableResourceManager
/*    */   implements ResourceManager, AutoCloseable {
/* 22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private CloseableResourceManager resources;
/* 25 */   private final List<PreparableReloadListener> listeners = Lists.newArrayList();
/*    */   private final PackType type;
/*    */   
/*    */   public ReloadableResourceManager(PackType paramPackType) {
/* 29 */     this.type = paramPackType;
/* 30 */     this.resources = new MultiPackResourceManager(paramPackType, List.of());
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 35 */     this.resources.close();
/*    */   }
/*    */   
/*    */   public void registerReloadListener(PreparableReloadListener paramPreparableReloadListener) {
/* 39 */     this.listeners.add(paramPreparableReloadListener);
/*    */   }
/*    */   
/*    */   public ReloadInstance createReload(Executor paramExecutor1, Executor paramExecutor2, CompletableFuture<Unit> paramCompletableFuture, List<PackResources> paramList) {
/* 43 */     LOGGER.info("Reloading ResourceManager: {}", LogUtils.defer(() -> paramList.stream().map(PackResources::packId).collect(Collectors.joining(", "))));
/*    */     
/* 45 */     this.resources.close();
/* 46 */     this.resources = new MultiPackResourceManager(this.type, paramList);
/* 47 */     return SimpleReloadInstance.create(this.resources, this.listeners, paramExecutor1, paramExecutor2, paramCompletableFuture, LOGGER.isDebugEnabled());
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Resource> getResource(Identifier paramIdentifier) {
/* 52 */     return this.resources.getResource(paramIdentifier);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<String> getNamespaces() {
/* 57 */     return this.resources.getNamespaces();
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Resource> getResourceStack(Identifier paramIdentifier) {
/* 62 */     return this.resources.getResourceStack(paramIdentifier);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<Identifier, Resource> listResources(String paramString, Predicate<Identifier> paramPredicate) {
/* 67 */     return this.resources.listResources(paramString, paramPredicate);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<Identifier, List<Resource>> listResourceStacks(String paramString, Predicate<Identifier> paramPredicate) {
/* 72 */     return this.resources.listResourceStacks(paramString, paramPredicate);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<PackResources> listPacks() {
/* 77 */     return this.resources.listPacks();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\resources\ReloadableResourceManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */