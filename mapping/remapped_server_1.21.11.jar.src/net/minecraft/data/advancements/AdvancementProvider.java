/*    */ package net.minecraft.data.advancements;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class AdvancementProvider implements DataProvider {
/*    */   private final PackOutput.PathProvider pathProvider;
/*    */   private final List<AdvancementSubProvider> subProviders;
/*    */   private final CompletableFuture<HolderLookup.Provider> registries;
/*    */   
/*    */   public AdvancementProvider(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture, List<AdvancementSubProvider> paramList) {
/* 26 */     this.pathProvider = paramPackOutput.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
/* 27 */     this.subProviders = paramList;
/* 28 */     this.registries = paramCompletableFuture;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput paramCachedOutput) {
/* 33 */     return this.registries.thenCompose(paramProvider -> {
/*    */           HashSet hashSet = new HashSet();
/*    */           ArrayList arrayList = new ArrayList();
/*    */           Consumer<AdvancementHolder> consumer = ();
/*    */           for (AdvancementSubProvider advancementSubProvider : this.subProviders) {
/*    */             advancementSubProvider.generate(paramProvider, consumer);
/*    */           }
/*    */           return CompletableFuture.allOf((CompletableFuture<?>[])arrayList.toArray(()));
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 55 */     return "Advancements";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\advancements\AdvancementProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */