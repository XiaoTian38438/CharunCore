/*     */ package net.minecraft.server;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.FileToIdConverter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.tags.TagLoader;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerFunctionLibrary implements PreparableReloadListener {
/*  33 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */   
/*  36 */   public static final ResourceKey<Registry<CommandFunction<CommandSourceStack>>> TYPE_KEY = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("function"));
/*     */   
/*  38 */   private static final FileToIdConverter LISTER = new FileToIdConverter(Registries.elementsDirPath(TYPE_KEY), ".mcfunction");
/*     */   
/*  40 */   private volatile Map<Identifier, CommandFunction<CommandSourceStack>> functions = (Map<Identifier, CommandFunction<CommandSourceStack>>)ImmutableMap.of(); private final TagLoader<CommandFunction<CommandSourceStack>> tagsLoader; private volatile Map<Identifier, List<CommandFunction<CommandSourceStack>>> tags; private final PermissionSet functionCompilationPermissions; private final CommandDispatcher<CommandSourceStack> dispatcher;
/*  41 */   public ServerFunctionLibrary(PermissionSet paramPermissionSet, CommandDispatcher<CommandSourceStack> paramCommandDispatcher) { this.tagsLoader = new TagLoader((paramIdentifier, paramBoolean) -> getFunction(paramIdentifier), Registries.tagsDirPath(TYPE_KEY));
/*  42 */     this.tags = Map.of();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     this.functionCompilationPermissions = paramPermissionSet;
/*  65 */     this.dispatcher = paramCommandDispatcher; } public Optional<CommandFunction<CommandSourceStack>> getFunction(Identifier paramIdentifier) {
/*     */     return Optional.ofNullable(this.functions.get(paramIdentifier));
/*     */   } public Map<Identifier, CommandFunction<CommandSourceStack>> getFunctions() {
/*     */     return this.functions;
/*     */   } public CompletableFuture<Void> reload(PreparableReloadListener.SharedState paramSharedState, Executor paramExecutor1, PreparableReloadListener.PreparationBarrier paramPreparationBarrier, Executor paramExecutor2) {
/*  70 */     ResourceManager resourceManager = paramSharedState.resourceManager();
/*  71 */     CompletableFuture<?> completableFuture1 = CompletableFuture.supplyAsync(() -> this.tagsLoader.load(paramResourceManager), paramExecutor1);
/*     */ 
/*     */ 
/*     */     
/*  75 */     CompletableFuture<?> completableFuture2 = CompletableFuture.supplyAsync(() -> LISTER.listMatchingResources(paramResourceManager), paramExecutor1).thenCompose(paramMap -> {
/*     */           HashMap hashMap = Maps.newHashMap();
/*     */ 
/*     */           
/*     */           CommandSourceStack commandSourceStack = Commands.createCompilationContext(this.functionCompilationPermissions);
/*     */ 
/*     */           
/*     */           for (Map.Entry entry : paramMap.entrySet()) {
/*     */             Identifier identifier1 = (Identifier)entry.getKey();
/*     */             
/*     */             Identifier identifier2 = LISTER.fileToId(identifier1);
/*     */             
/*     */             hashMap.put(identifier2, CompletableFuture.supplyAsync((), paramExecutor));
/*     */           } 
/*     */           
/*     */           CompletableFuture[] arrayOfCompletableFuture = (CompletableFuture[])hashMap.values().toArray((Object[])new CompletableFuture[0]);
/*     */           
/*     */           return CompletableFuture.allOf((CompletableFuture<?>[])arrayOfCompletableFuture).handle(());
/*     */         });
/*     */     
/*  95 */     Objects.requireNonNull(paramPreparationBarrier); return completableFuture1.thenCombine(completableFuture2, Pair::of).thenCompose(paramPreparationBarrier::wait)
/*  96 */       .thenAcceptAsync(paramPair -> {
/*     */           Map map = (Map)paramPair.getSecond();
/*     */           ImmutableMap.Builder builder = ImmutableMap.builder();
/*     */           map.forEach(());
/*     */           this.functions = (Map<Identifier, CommandFunction<CommandSourceStack>>)builder.build();
/*     */           this.tags = this.tagsLoader.build((Map)paramPair.getFirst());
/*     */         }paramExecutor2);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<CommandFunction<CommandSourceStack>> getTag(Identifier paramIdentifier) {
/*     */     return this.tags.getOrDefault(paramIdentifier, List.of());
/*     */   }
/*     */   
/*     */   public Iterable<Identifier> getAvailableTags() {
/*     */     return this.tags.keySet();
/*     */   }
/*     */   
/*     */   private static List<String> readLines(Resource paramResource) {
/*     */     
/* 116 */     try { BufferedReader bufferedReader = paramResource.openAsReader(); 
/* 117 */       try { List<String> list = bufferedReader.lines().toList();
/* 118 */         if (bufferedReader != null) bufferedReader.close();  return list; } catch (Throwable throwable) { if (bufferedReader != null) try { bufferedReader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException iOException)
/* 119 */     { throw new CompletionException(iOException); }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\ServerFunctionLibrary.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */