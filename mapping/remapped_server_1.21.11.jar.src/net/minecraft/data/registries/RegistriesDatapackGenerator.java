/*    */ package net.minecraft.data.registries;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Encoder;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.RegistryDataLoader;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class RegistriesDatapackGenerator implements DataProvider {
/*    */   private final PackOutput output;
/*    */   
/*    */   public RegistriesDatapackGenerator(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 24 */     this.registries = paramCompletableFuture;
/* 25 */     this.output = paramPackOutput;
/*    */   }
/*    */   private final CompletableFuture<HolderLookup.Provider> registries;
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput paramCachedOutput) {
/* 30 */     return this.registries.thenCompose(paramProvider -> {
/*    */           RegistryOps registryOps = paramProvider.createSerializationContext((DynamicOps)JsonOps.INSTANCE);
/*    */           return CompletableFuture.allOf((CompletableFuture<?>[])RegistryDataLoader.WORLDGEN_REGISTRIES.stream().flatMap(()).toArray(()));
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private <T> Optional<CompletableFuture<?>> dumpRegistryCap(CachedOutput paramCachedOutput, HolderLookup.Provider paramProvider, DynamicOps<JsonElement> paramDynamicOps, RegistryDataLoader.RegistryData<T> paramRegistryData) {
/* 39 */     ResourceKey resourceKey = paramRegistryData.key();
/* 40 */     return paramProvider.lookup(resourceKey).map(paramRegistryLookup -> {
/*    */           PackOutput.PathProvider pathProvider = this.output.createRegistryElementsPathProvider(paramResourceKey);
/*    */           return CompletableFuture.allOf((CompletableFuture<?>[])paramRegistryLookup.listElements().map(()).toArray(()));
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static <E> CompletableFuture<?> dumpValue(Path paramPath, CachedOutput paramCachedOutput, DynamicOps<JsonElement> paramDynamicOps, Encoder<E> paramEncoder, E paramE) {
/* 50 */     return (CompletableFuture)paramEncoder.encodeStart(paramDynamicOps, paramE).mapOrElse(paramJsonElement -> DataProvider.saveStable(paramCachedOutput, paramJsonElement, paramPath), paramError -> CompletableFuture.failedFuture(new IllegalStateException("Couldn't generate file '" + String.valueOf(paramPath) + "': " + paramError.message())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 58 */     return "Registries";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\registries\RegistriesDatapackGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */