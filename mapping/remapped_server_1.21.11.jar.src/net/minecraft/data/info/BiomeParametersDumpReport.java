/*    */ package net.minecraft.data.info;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Encoder;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.nio.file.Path;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.Climate;
/*    */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class BiomeParametersDumpReport implements DataProvider {
/* 29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Path topPath;
/*    */   
/*    */   private final CompletableFuture<HolderLookup.Provider> registries;
/* 34 */   private static final MapCodec<ResourceKey<Biome>> ENTRY_CODEC = ResourceKey.codec(Registries.BIOME).fieldOf("biome");
/*    */   
/* 36 */   private static final Codec<Climate.ParameterList<ResourceKey<Biome>>> CODEC = Climate.ParameterList.codec(ENTRY_CODEC).fieldOf("biomes").codec();
/*    */   
/*    */   public BiomeParametersDumpReport(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 39 */     this.topPath = paramPackOutput.getOutputFolder(PackOutput.Target.REPORTS).resolve("biome_parameters");
/* 40 */     this.registries = paramCompletableFuture;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput paramCachedOutput) {
/* 45 */     return this.registries.thenCompose(paramProvider -> {
/*    */           RegistryOps registryOps = paramProvider.createSerializationContext((DynamicOps)JsonOps.INSTANCE);
/*    */           ArrayList arrayList = new ArrayList();
/*    */           MultiNoiseBiomeSourceParameterList.knownPresets().forEach(());
/*    */           return CompletableFuture.allOf((CompletableFuture<?>[])arrayList.toArray(()));
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static <E> CompletableFuture<?> dumpValue(Path paramPath, CachedOutput paramCachedOutput, DynamicOps<JsonElement> paramDynamicOps, Encoder<E> paramEncoder, E paramE) {
/* 56 */     Optional<JsonElement> optional = paramEncoder.encodeStart(paramDynamicOps, paramE).resultOrPartial(paramString -> LOGGER.error("Couldn't serialize element {}: {}", paramPath, paramString));
/* 57 */     if (optional.isPresent()) {
/* 58 */       return DataProvider.saveStable(paramCachedOutput, optional.get(), paramPath);
/*    */     }
/* 60 */     return CompletableFuture.completedFuture(null);
/*    */   }
/*    */   
/*    */   private Path createPath(Identifier paramIdentifier) {
/* 64 */     return this.topPath.resolve(paramIdentifier.getNamespace()).resolve(paramIdentifier.getPath() + ".json");
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 69 */     return "Biome Parameters";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\info\BiomeParametersDumpReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */